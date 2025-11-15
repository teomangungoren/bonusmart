package com.bonusmart.auth_api.service;

import com.bonusmart.auth_api.persistence.entity.User;
import com.bonusmart.auth_api.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(User user) {
        return userRepository.save(user);
    }

    public User getValidatedUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        checkUserPassword(password, user.getPassword());

        return user;
    }



    public void checkUserPassword(String requestedPassword, String storedPassword) {
        if(!passwordEncoder.matches(requestedPassword, storedPassword)) {
          throw new RuntimeException("Password does not match");
        }
    }



}
