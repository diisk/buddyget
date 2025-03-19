package br.dev.diisk.presentation.controllers;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.dev.diisk.application.cases.auth.AuthLoginCase;
import br.dev.diisk.application.cases.auth.AuthRegisterCase;
import br.dev.diisk.application.cases.auth.AuthRenewCase;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.presentation.dtos.auth.LoginRequest;
import br.dev.diisk.presentation.dtos.auth.LoginResponse;
import br.dev.diisk.presentation.dtos.auth.RegisterRequest;
import br.dev.diisk.presentation.dtos.auth.RegisterResponse;
import br.dev.diisk.presentation.dtos.auth.RenewResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthLoginCase authLoginCase;
    private final AuthRegisterCase authRegisterCase;
    private final AuthRenewCase authRenewCase;
    private final ModelMapper mapper;
    private final IResponseService responseService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest dto) {
        String token = authLoginCase.execute(dto.getEmail(),dto.getPassword());
        return responseService.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest dto,
            UriComponentsBuilder uriBuilder) {
        User newUser = authRegisterCase.execute(dto.getName(),dto.getEmail(),dto.getPassword());
        RegisterResponse response = mapper.map(newUser, RegisterResponse.class);
        URI uri = uriBuilder.path("users/{id}").buildAndExpand(response.getId()).toUri();
        return responseService.created(uri, response);
    }

    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/renew")
    public ResponseEntity<SuccessResponse<RenewResponse>> renew(@AuthenticationPrincipal User user) {
        String token = authRenewCase.execute(user);
        return responseService.ok(new RenewResponse(token));
    }

}
