package com.example.todo.api.user;

import com.example.todo.domain.Response;
import com.example.todo.dto.user.request.UserJoinRequestDto;
import com.example.todo.dto.user.request.UserUpdateRequestDto;
import com.example.todo.dto.user.response.UserJoinResponseDto;
import com.example.todo.dto.user.response.UserUpdateResponseDto;
import com.example.todo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponseDto> createUser(@RequestBody final UserJoinRequestDto joinDto) {
        return Response.success(userService.createUser(joinDto));
    }

    @PutMapping("/users/update")
    public Response<UserUpdateResponseDto> updateUser(@RequestBody final UserUpdateRequestDto updateDto,
                                                      final Authentication authentication) {
        final Long userId = Long.parseLong(authentication.getName());
        return Response.success(userService.updateUser(updateDto, userId));
    }
}
