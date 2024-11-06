package org.project.food_management.service;

import org.project.food_management.model.User;
import org.project.food_management.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByJwtToken(String jwt) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            throw new Exception("User not found with email: " + email);
        }
        return user;
    }


}
