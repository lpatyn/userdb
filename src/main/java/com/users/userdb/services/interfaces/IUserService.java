package com.users.userdb.services.interfaces;

import com.users.userdb.dto.request.NewPasswordDTO;
import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.request.UpdateUserDTO;
import com.users.userdb.dto.response.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> findAll();
    UserDTO findById(int id);
    UserDTO create(NewUserDTO newUserDTO);
    UserDTO update(int id, UpdateUserDTO updateUserDTO);
    UserDTO changePassword(NewPasswordDTO newPasswordDTO);
    void delete(int id);

}
