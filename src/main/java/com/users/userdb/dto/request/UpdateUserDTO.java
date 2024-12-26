package com.users.userdb.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.users.userdb.constants.ValidationMessages.*;
import static com.users.userdb.constants.ApplicationConstants.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    @Size(min = NAME_MIN, message = SIZE_MIN + NAME_MIN)
    @NotNull(message = NOT_NULL)
    private String name;

    @Size(min = LAST_NAME_MIN, message = SIZE_MIN + LAST_NAME_MIN)
    @NotNull(message = NOT_NULL)
    private String lastName;

    @Size(min = 1, message = NOT_EMPTY)
    @Email(message = EMAIL)
    @NotNull(message = NOT_NULL)
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Past(message = PAST)
    @NotNull(message = NOT_NULL)
    private Date birthdate;

}
