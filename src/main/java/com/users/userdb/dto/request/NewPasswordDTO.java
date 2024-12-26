package com.users.userdb.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.users.userdb.constants.ApplicationConstants.*;
import static com.users.userdb.constants.ValidationMessages.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordDTO {

    @Size(min = 1, message = NOT_EMPTY)
    @Email(message = EMAIL)
    @NotNull(message = NOT_NULL)
    private String email;

    @Size(min = PASSWORD_MIN, message = SIZE_MIN + PASSWORD_MIN)
    @NotNull(message = NOT_NULL)
    private String newPassword;

}
