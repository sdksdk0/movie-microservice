package cn.tf.cloud.microservicesimpleprovideruser.controller;

import cn.tf.cloud.microservicesimpleprovideruser.entity.User;
import cn.tf.cloud.microservicesimpleprovideruser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping("/user/{id}")
  public User findById(@PathVariable Long id) {
    User findOne = this.userRepository.findById(id).get();
    return findOne;
  }
}
