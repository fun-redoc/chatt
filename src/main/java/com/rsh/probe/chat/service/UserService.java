package com.rsh.probe.chat.service;

import com.rsh.probe.chat.model.User;
import com.rsh.probe.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<User> byUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public User save(User user) {
        // TODO this authentication method is very weak, password is transmitted unencoded between browser and server
        //      at least the password is saved encoded => see security configuration (security/SecurityConfiguration) for details
        var storeUser = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()));
        return userRepository.save(storeUser);
    }

}
