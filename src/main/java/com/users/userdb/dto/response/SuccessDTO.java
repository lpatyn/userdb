package com.users.userdb.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessDTO<T> {

    private boolean success;
    private String message;
    private T data;

}
