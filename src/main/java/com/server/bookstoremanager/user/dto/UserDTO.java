package com.server.bookstoremanager.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.bookstoremanager.user.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @Max(120)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
}
