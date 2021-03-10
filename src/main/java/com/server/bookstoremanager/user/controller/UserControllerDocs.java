package com.server.bookstoremanager.user.controller;

import com.server.bookstoremanager.user.dto.MessageDTO;
import com.server.bookstoremanager.user.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("System Users Management")
public interface UserControllerDocs {

    @ApiOperation(value = "User creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success user creation"),
            @ApiResponse(code = 400, message = "Missing required fields,, or an error on validation field rules")
    })
    MessageDTO create(UserDTO userDTO);

    @ApiOperation(value = "User update operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success user updated"),
            @ApiResponse(code = 400, message = "Missing required fields,, or an error on validation field rules")
    })
    MessageDTO update(Long id, UserDTO userDTO);

    @ApiOperation(value = "User exclusion operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success user exclusion"),
            @ApiResponse(code = 404, message = "User with informed id not found")
    })
    void delete(Long id);
}
