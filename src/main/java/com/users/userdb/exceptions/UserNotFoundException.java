package com.users.userdb.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String key;
    private final String value;

    public UserNotFoundException(String key, String value) {
        super("Requested user not found");
        this.key = key;
        this.value = value;
    }

}
