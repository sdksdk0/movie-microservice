package cn.tf.cloud.microservicesimpleconsumermovie.feign;

import cn.tf.cloud.microservicesimpleconsumermovie.entity.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * feign整合Hystrix
 */
//@FeignClient(name = "microservice-provider-user",fallback = FeignClientFallback.class)
@FeignClient(name = "microservice-provider-user",fallbackFactory = FeignClientFallback.class)
public interface UserFeignClient {

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);
}
@Component
class FeignClientFallback implements FallbackFactory<UserFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallback.class);

    @Override
    public UserFeignClient create(Throwable throwable) {
        return new UserFeignClient() {
            @Override
            public User findById(Long id) {
                FeignClientFallback.LOGGER.info("fallback reason was :", throwable);
                User user = new User();
                user.setId(-1L);
                user.setName("默认用户");
                return user;
            }
        };
    }
}
/*@Component
class FeignClientFallback implements UserFeignClient{

    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(-1L);
        user.setName("默认用户");
        return user;
    }
}*/
