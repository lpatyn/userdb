package com.users.userdb.services;

import com.users.userdb.dto.request.NewPasswordDTO;
import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.request.UpdateUserDTO;
import com.users.userdb.dto.response.UserDTO;
import com.users.userdb.entities.User;
import com.users.userdb.exceptions.UserNotFoundException;
import com.users.userdb.repositories.UserRepository;
import com.users.userdb.repositories.interfaces.IUserRepository;
import com.users.userdb.services.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.users.userdb.constants.ApplicationConstants.DATE_FORMAT;

@Service
public class UserService implements IUserService {

    IUserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> findAll() {

        return userRepository
                .findAll()
                .stream()
                .map(u -> new UserDTO(
                        u.getId(), u.getName(), u.getLastName(), u.getEmail(),
                        DATE_FORMAT.format(u.getBirthdate())))
                .toList();
    }

    @Override
    public UserDTO findById(int id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) throw new UserNotFoundException("id", "" + id);

        User user = userOptional.get();

        return new UserDTO(
                user.getId(), user.getName(), user.getLastName(),
                user.getEmail(), DATE_FORMAT.format(user.getBirthdate())
        );
    }

    @Override
    public UserDTO create(NewUserDTO newUserDTO) {
        User createdUser = userRepository.create(newUserDTO);

        return new UserDTO(
                createdUser.getId(), createdUser.getName(), createdUser.getLastName(),
                createdUser.getEmail(), DATE_FORMAT.format(createdUser.getBirthdate())
        );
    }

    @Override
    public UserDTO update(int id, UpdateUserDTO updateUserDTO) {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isEmpty()) throw new UserNotFoundException("id", "" + id);

        User user = userToUpdate.get();

        user.setName(updateUserDTO.getName());
        user.setLastName(updateUserDTO.getLastName());
        user.setEmail(updateUserDTO.getEmail());
        user.setBirthdate(updateUserDTO.getBirthdate());

        User updatedUser = userRepository.update(user);

        return new UserDTO(
                updatedUser.getId(), updatedUser.getName(), updatedUser.getLastName(),
                updatedUser.getEmail(), DATE_FORMAT.format(updatedUser.getBirthdate())
        );
    }

    @Override
    public UserDTO changePassword(NewPasswordDTO newPasswordDTO) {
        Optional<User> userToUpdate = userRepository.findByEmail(newPasswordDTO.getEmail());

        if (userToUpdate.isEmpty()) throw new UserNotFoundException("email", newPasswordDTO.getEmail());

        User user = userToUpdate.get();

        user.setPassword(newPasswordDTO.getNewPassword());

        User updatedUser = userRepository.update(user);

        return new UserDTO(
                updatedUser.getId(), updatedUser.getName(), updatedUser.getLastName(),
                updatedUser.getEmail(), DATE_FORMAT.format(updatedUser.getBirthdate())
        );
    }

    @Override
    public void delete(int id) {
        boolean result = userRepository.delete(id);

        if (!result) throw new UserNotFoundException("id", "" + id);
    }

}
