package com.finleap.incidentmanagement.service.impl;

import com.finleap.incidentmanagement.dto.UserDTO;
import com.finleap.incidentmanagement.entity.User;
import com.finleap.incidentmanagement.exception.IncidentManagementNotFoundException;
import com.finleap.incidentmanagement.repository.UserRepository;
import com.finleap.incidentmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final String USER_ENTITY = "User";

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user = userRepository.save(user);
        return UserDTO.builder().userId(user.getUserId()).name(user.getName()).build();
    }

    @Override
    public UserDTO findByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(USER_ENTITY, userId.toString()));
       return UserDTO.builder().userId(user.getUserId()).name(user.getName()).build();
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> userList = userRepository.findAll();
        if(userList == null || userList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return userList.stream().map(user -> UserDTO.builder().userId(user.getUserId()).name(user.getName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO editUser(Integer userId, UserDTO userDTO) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(USER_ENTITY, userId.toString()));
        user.setName(userDTO.getName());
        userRepository.save(user);
        userDTO.setUserId(userId);
        return userDTO;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new IncidentManagementNotFoundException(USER_ENTITY, userId.toString()));
        userRepository.delete(user);
    }
}
