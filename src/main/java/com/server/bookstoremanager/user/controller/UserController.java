package com.server.bookstoremanager.user.controller;

import com.server.bookstoremanager.user.dto.JwtRequest;
import com.server.bookstoremanager.user.dto.JwtResponse;
import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import com.server.bookstoremanager.user.service.AuthenticationService;
import com.server.bookstoremanager.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private UserService userService;

    private AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService,  AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@RequestBody @Valid UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @PutMapping("/{id}")
    public MessageDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
        return userService.update(id, userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping(value = "/authenticate")
    public JwtResponse createAuthenticationToken(@RequestBody @Valid JwtRequest jwtRequest) {
        return authenticationService.createAuthenticationToken(jwtRequest);
    }
}
