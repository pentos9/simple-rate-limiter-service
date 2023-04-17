
# Simple rate-limiter-service
Rate limiting is used to control the amount of incoming and outgoing traffic.
This is a very simple rate limiter built on top of Redis and Lua script engine.

# How to build this project
this project_path

```$xslt
cd project_path
mvn clean package
```

or if you wanna skip unit test

```$xslt
cd project_path
mvn clean package -Dmaven.test.skip=true
```

# How to run this project 
after you build this project, you can run it like this:

```$xslt
cd project_path/target
java -jar simple-rate-limiter-service-1.0-SNAPSHOT.jar
```

# Tech Stack

- Spring Boot : 2.3.0.RELEASE
- Swagger 3
- Junit 5
- H2
- Redis 4.0.11 (local test)
- Orika (bean copy library)
- Cors supported

# Swagger Address
A simple search-user API is provided!

just point your browser to: http://localhost:5100/swagger-ui/index.html

# Throttling logic
Implement a Rate limiter with Redis and Lua script, using INCRBY and expire Command!

com.demo.service.iface.RateLimiterService#acquire

```$xslt
    @Override
    public boolean acquire(String key, Long limitMax, Long expiredTime) {
        log.info(String.format("RateLimiterService#acquire key:%s,limitMax:%s,expiredTime:%s", key, limitMax, expiredTime));
        List<String> keys = Arrays.asList(key);
        String rateLimiterLuaScript = LuaScriptUtil.getRateLimiterScript();
        RedisScript<Boolean> luaScript = new DefaultRedisScript<>(rateLimiterLuaScript, Boolean.class);
        boolean result = stringRedisTemplate.execute(luaScript, keys, String.valueOf(limitMax), String.valueOf(expiredTime));
        return result;
    }
```

lua script

com.demo.web.rest.util.LuaScriptUtil#getRateLimiterScript
#

```$xslt
public class LuaScriptUtil {
    public static String getRateLimiterScript() {
        StringBuilder builder = new StringBuilder();
        builder.append("local key = KEYS[1] ");
        builder.append("local limit = tonumber(ARGV[1]) ");
        builder.append("local expiredTime = tostring(ARGV[2]) ");
        builder.append("local current = tonumber(redis.call('get', key) or \"0\") "); // lua return type number
        builder.append("if current + 1 > limit then ");
        builder.append("    return 0 ");
        builder.append("else ");
        builder.append("    redis.call(\"INCRBY\", key,\"1\") ");
        builder.append("    redis.call(\"expire\", key, expiredTime) ");
        builder.append("    return 1 ");
        builder.append("end ");
        return builder.toString();
    }
}

```

# Unit Test

Unit Test Location: /simple-rate-limiter-service/src/test

```$xslt
 └── test
        ├── java
        │   └── com
        │       └── demo
        │           └── RateLimiterServiceTest.java
        └── resources
            └── application.properties
```
Mock concurrent request with CountDownLatch

```$xslt
RateLimiterServiceTest#tooManyRequest

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

```



# Project Structure
This is how the project looks like: 

```$xslt
├── README.md
├── pom.xml
├── simple-rate-limiter-service.iml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── demo
    │   │           ├── SimpleRateLimiterApplication.java
    │   │           ├── config
    │   │           │   ├── CorsConfig.java
    │   │           │   └── Swagger3Config.java
    │   │           ├── domain
    │   │           │   └── User.java
    │   │           ├── repository
    │   │           │   └── UserRepository.java
    │   │           ├── service
    │   │           │   ├── DataInitService.java
    │   │           │   ├── RateLimiterService.java
    │   │           │   ├── UserService.java
    │   │           │   ├── dto
    │   │           │   │   ├── UserDTO.java
    │   │           │   │   └── pagination
    │   │           │   │       ├── PaginationResultDTO.java
    │   │           │   │       └── SearchReq.java
    │   │           │   └── iface
    │   │           │       └── IRateLimiterService.java
    │   │           └── web
    │   │               └── rest
    │   │                   ├── UserController.java
    │   │                   ├── common
    │   │                   │   ├── Constants.java
    │   │                   │   ├── GlobalExceptionHandler.java
    │   │                   │   └── Pagination.java
    │   │                   ├── errors
    │   │                   │   ├── BadRequestException.java
    │   │                   │   ├── BaseException.java
    │   │                   │   ├── BizException.java
    │   │                   │   └── TooManyRequestException.java
    │   │                   ├── util
    │   │                   │   ├── ArrayUtil.java
    │   │                   │   ├── BeanCopyUtil.java
    │   │                   │   ├── JsonUtil.java
    │   │                   │   ├── LuaScriptUtil.java
    │   │                   │   └── WebUtils.java
    │   │                   └── vm
    │   │                       ├── OtherVM.java
    │   │                       ├── UserCreateVM.java
    │   │                       └── UserVM.java
    │   └── resources
    │       └── application.properties
    └── test
        ├── java
        │   └── com
        │       └── demo
        │           └── RateLimiterServiceTest.java
        └── resources
            └── application.properties
```

# Docker Supported 

To be continue...
