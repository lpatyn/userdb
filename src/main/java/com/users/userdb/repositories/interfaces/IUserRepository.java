package com.users.userdb.repositories.interfaces;

import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    List<User> findAll();
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    User create(NewUserDTO newUserDTO);
    User update(User user);
    boolean delete(int id);

}
