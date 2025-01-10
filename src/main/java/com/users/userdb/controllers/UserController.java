package com.users.userdb.controllers;

import com.users.userdb.dto.request.NewPasswordDTO;
import com.users.userdb.dto.request.NewUserDTO;
import com.users.userdb.dto.request.UpdateUserDTO;
import com.users.userdb.dto.response.SuccessDTO;
import com.users.userdb.dto.response.UserDTO;
import com.users.userdb.services.UserService;
import com.users.userdb.services.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    IUserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<List<UserDTO>>> findAll() {
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "Success",
                userService.findAll()
        ), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<UserDTO>> findById(@PathVariable Long id) {
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "User found",
                userService.findById(id)
        ), HttpStatus.OK);
    }

    @PostMapping
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<UserDTO>> create(@Valid @RequestBody NewUserDTO newUserDTO) {
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "User creation success",
                userService.create(newUserDTO)
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<UserDTO>> update(@Valid @RequestBody UpdateUserDTO updateUserDTO,
                                                      @PathVariable Long id) {
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "User modification success",
                userService.update(id, updateUserDTO)
        ), HttpStatus.OK);
    }

    @PostMapping("/password-change")
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<UserDTO>> changePassword(@Valid @RequestBody NewPasswordDTO newPasswordDTO) {
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "Password change success",
                userService.changePassword(newPasswordDTO)
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<SuccessDTO<String>> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(new SuccessDTO<>(
                true,
                "User deletion success",
                "User with id " + id + " successfully deleted"
        ), HttpStatus.OK);
    }

}
