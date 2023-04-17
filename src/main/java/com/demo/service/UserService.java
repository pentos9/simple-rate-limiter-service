package com.demo.service;

import com.demo.domain.User;
import com.demo.repository.UserRepository;
import com.demo.service.dto.pagination.SearchReq;
import com.demo.service.dto.UserDTO;
import com.demo.service.iface.IRateLimiterService;
import com.demo.web.rest.errors.TooManyRequestException;
import com.demo.web.rest.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    @Value("${user.search.tps.max:5}")
    private Long maxTPS;

    @Resource
    private IRateLimiterService rateLimiterService;

    public List<UserDTO> search(SearchReq requestParam) {
        checkTPS(requestParam.getClientIP(), maxTPS);

        Pageable pageable = PageRequest.of(requestParam.getPageNum() - 1, requestParam.getPageSize());
        Page page = userRepository.findAll(pageable);
        List<User> userPOs = page.getContent();
        return BeanCopyUtil.mapList(userPOs, UserDTO.class);
    }

    /**
     * max TPS every 15 seconds
     *
     * @param clientIP
     * @param maxTPS
     */
    private void checkTPS(String clientIP, Long maxTPS) {
        boolean isOK = rateLimiterService.acquire("user:search:rate-limiter:" + clientIP, maxTPS, 15L);
        if (!isOK) {
            throw new TooManyRequestException();
        }
    }
}
