package br.dev.diisk.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.auth.UserResponse;
import br.dev.diisk.presentation.dtos.auth.RefreshTokenResponse;
import br.dev.diisk.presentation.dtos.auth.LoginRequest;
import br.dev.diisk.presentation.dtos.auth.LoginResponse;
import br.dev.diisk.presentation.dtos.auth.RegisterRequest;
import br.dev.diisk.presentation.dtos.auth.RegisterResponse;
import br.dev.diisk.presentation.dtos.auth.UpdatePasswordRequest;
import br.dev.diisk.presentation.dtos.auth.UpdateUserRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final IResponseService responseService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {

       
        return responseService.created(null, null);
    }
    
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
 
        return responseService.ok(null);
    }

    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserResponse>> getUser(@AuthenticationPrincipal User user) {
 
        return responseService.ok(null);
    }

    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @PatchMapping("/me")
    public ResponseEntity<SuccessResponse<UserResponse>> updateUser(@AuthenticationPrincipal User user, @Valid UpdateUserRequest request) {
 
        return responseService.ok(null);
    }
    
    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse<Boolean>> updatePassword(@AuthenticationPrincipal User user, @Valid UpdatePasswordRequest request) {
        
        return responseService.ok(true);
    }

    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<RefreshTokenResponse>> refreshToken(@AuthenticationPrincipal User user) {
        
        return responseService.ok(null);
    }
    

}
