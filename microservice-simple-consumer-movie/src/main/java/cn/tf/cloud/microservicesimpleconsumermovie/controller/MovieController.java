package cn.tf.cloud.microservicesimpleconsumermovie.controller;

import cn.tf.cloud.microservicesimpleconsumermovie.entity.User;
import cn.tf.cloud.microservicesimpleconsumermovie.feign.UserFeignClient;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Import(FeignClientsConfiguration.class)
@RestController
public class MovieController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private LoadBalancerClient loadBalancerClient;
  @Autowired
  private UserFeignClient userFeignClient;

  @GetMapping("/feign_user/{id}")
  public User findByFeign(@PathVariable Long id) {
    return this.userFeignClient.findById(id);
  }

  /*private UserFeignClient userUserFeignClient;

  private UserFeignClient adminUserFeignClient;

  @Autowired
  public MovieController(Decoder decoder, Encoder encoder, Client client, Contract contract) {
    // 这边的decoder、encoder、client、contract，可以debug看看是什么实例。
    this.userUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
            .requestInterceptor(new BasicAuthRequestInterceptor("user", "password1")).target(UserFeignClient.class, "http://microservice-provider-user/");
    this.adminUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
            .requestInterceptor(new BasicAuthRequestInterceptor("admin", "password2"))
            .target(UserFeignClient.class, "http://microservice-provider-user/");
  }

  @GetMapping("/user-user/{id}")
  public User findByIdUser(@PathVariable Long id) {
    return this.userUserFeignClient.findById(id);
  }

  @GetMapping("/user-admin/{id}")
  public User findByIdAdmin(@PathVariable Long id) {
    return this.adminUserFeignClient.findById(id);
  }
*/

  @GetMapping("/user/{id}")
  public User findById(@PathVariable Long id) {
    return this.restTemplate.getForObject("http://microservice-provider-user/user/" + id, User.class);
  }

  @GetMapping("/log-user-instance")
  public void logUserInstance() {
    ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-provider-user");
    // 打印当前选择的是哪个节点
    MovieController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
  }



}
