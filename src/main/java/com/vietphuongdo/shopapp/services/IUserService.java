package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.UpdateUserDTO;
import com.vietphuongdo.shopapp.dtos.UserDTO;
import com.vietphuongdo.shopapp.entities.User;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String phoneNumber, String password, Long roleId) throws Exception;
    User getUserDetailsFromToken(String token) throws Exception;
    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;
}
