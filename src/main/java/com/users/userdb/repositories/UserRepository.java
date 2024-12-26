package com.users.userdb.repositories;

import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.entities.User;
import com.users.userdb.repositories.interfaces.IUserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository implements IUserRepository {

    private List<User> usersList = new ArrayList<>();

    @Override
    public List<User> findAll() {
        return usersList;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return usersList.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public Optional<User> findById(int id) {
        return usersList.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public User create(NewUserDTO newUserDTO) {
        User user = new User(
                usersList.size() + 1, newUserDTO.getName(), newUserDTO.getLastName(),
                newUserDTO.getEmail(), newUserDTO.getBirthdate(), newUserDTO.getPassword());
        usersList.add(user);

        return user;
    }

    @Override
    public User update(User user) {
        usersList.replaceAll(u -> {
            if (u.getId() == user.getId()) return user;
            return u;
        });

        return user;
    }

    @Override
    public boolean delete(int id) {
        return usersList.removeIf(u -> u.getId() == id);
    }
}
