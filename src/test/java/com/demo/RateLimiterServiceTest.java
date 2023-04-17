package com.demo;

import com.demo.service.RateLimiterService;
import com.demo.web.rest.errors.TooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
@SpringBootTest(classes = {SimpleRateLimiterApplication.class}, properties = "application.properties")
public class RateLimiterServiceTest {

    @Resource
    private RateLimiterService rateLimiterService;

    private String keyPrefix = "user:search:test:rate-limiter:";
    private Long max;
    private Long expiredTime;

    @BeforeEach
    public void init() {
        this.keyPrefix = "user:search:test:rate-limiter:";
        this.max = 5L;
        this.expiredTime = 10L;
    }

    @Test
    public void normal() {
        Integer concurrentWorker = max.intValue();
        Assertions.assertTrue(concurrentWorker <= max);
        Long totalFail = doCall(concurrentWorker);
        Assertions.assertEquals(0, totalFail, "Total expected fail-request is Wrong");
    }


    private Long doCall(Integer concurrentWorker) {
        String uuid = UUID.randomUUID().toString();

        LongAdder failAdder = new LongAdder();
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentWorker);
        for (int i = 0; i < concurrentWorker; i++) {
            executorService.execute(() -> {
                await(latch);
                boolean isOK = rateLimiterService.acquire(keyPrefix + uuid, max, expiredTime);
                if (!isOK) {
                    failAdder.add(1);
                }
            });
        }

        latch.countDown();
        sleep(expiredTime * 1000 + 2000); // let's say it is 2 second longer than expired time
        log.info("RateLimiterServiceTest#tooManyRequest Too many Request failAdder count:{}", failAdder.longValue());
        executorService.shutdown();

        return failAdder.longValue();
    }

    @Test
    public void tooManyRequest() {
        Integer concurrentWorker = 30;
        Assertions.assertTrue(concurrentWorker > this.max);
        Long totalFail = doCall(concurrentWorker);

        Assertions.assertEquals(concurrentWorker - max, totalFail, "Total expected fail-request is Wrong");
        Assertions.assertThrows(TooManyRequestException.class, () -> {
            if (totalFail > 0) {
                throw new TooManyRequestException();
            }
        });
    }

    public void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sleep(Long expiredTime) {
        try {
            Thread.sleep(expiredTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
