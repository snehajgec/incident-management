package com.finleap.incidentmanagement.service;

import com.finleap.incidentmanagement.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    UserDTO findByUserId(Integer userId);

    List<UserDTO> findAllUsers();

    UserDTO editUser(Integer userId, UserDTO userDTO);

    void deleteUser(Integer userId);
}
