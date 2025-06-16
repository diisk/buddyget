package br.dev.diisk.presentation.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.application.shared.services.ITokenService;
import br.dev.diisk.application.user.cases.AddUserCase;
import br.dev.diisk.application.user.cases.LoginUserCase;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.auth.dtos.LoginRequest;
import br.dev.diisk.presentation.auth.dtos.LoginResponse;
import br.dev.diisk.presentation.auth.dtos.RefreshTokenResponse;
import br.dev.diisk.presentation.auth.dtos.RegisterRequest;
import br.dev.diisk.presentation.auth.dtos.RegisterResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AddUserCase addUserCase;
    private final LoginUserCase loginUserCase;
    private final ITokenService tokenService;
    private final IResponseService responseService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        User user = addUserCase.execute(
                request.name(),
                request.email(),
                request.password());
        RegisterResponse response = new RegisterResponse(user);
        return responseService.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        String token = loginUserCase.execute(request.email(), request.password());
        LoginResponse response = new LoginResponse(token);
        return responseService.ok(response);
    }

    @PreAuthorize("hasAuthority('DEFAULT')")
    @SecurityRequirement(name = "bearer-key")
    @GetMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<RefreshTokenResponse>> refreshToken(@AuthenticationPrincipal User user) {
        String token = tokenService.generateToken(user.getId().toString());
        RefreshTokenResponse response = new RefreshTokenResponse(token);
        return responseService.ok(response);
    }

}
