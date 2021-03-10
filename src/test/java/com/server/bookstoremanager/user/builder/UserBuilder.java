package com.server.bookstoremanager.user.builder;

import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.enums.Gender;
import com.server.bookstoremanager.user.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Nikolas";

    @Builder.Default
    private Integer age = 22;

    @Builder.Default
    private Gender gender = Gender.MALE;

    @Builder.Default
    private String email = "nikolas@teste.com";

    @Builder.Default
    private String username = "nikolas";

    @Builder.Default
    private String password = "123456";

    @Builder.Default
    private LocalDate birthDate = LocalDate.of(1998, 4, 9);

    @Builder.Default
    private Role role = Role.USER;

    public UserDTO buildUserDTO() {
        return new UserDTO(id,
                name,
                age,
                gender,
                email,
                username,
                password,
                birthDate,
                role
        );
    }
}
