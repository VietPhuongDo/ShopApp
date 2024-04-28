package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.UserDTO;
import com.vietphuongdo.shopapp.entities.User;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber,String password) throws Exception;
}
