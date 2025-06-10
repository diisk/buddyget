package br.dev.diisk.presentation.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.application.user.cases.UpdateUserCase;
import br.dev.diisk.application.user.cases.UpdateUserPasswordCase;
import br.dev.diisk.application.user.dtos.UpdateUserParams;
import br.dev.diisk.application.user.dtos.UpdateUserPasswordParams;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.user.dtos.UpdatePasswordRequest;
import br.dev.diisk.presentation.user.dtos.UpdateUserRequest;
import br.dev.diisk.presentation.user.dtos.UserResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@PreAuthorize("hasAuthority('DEFAULT')")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class UserController {

    private final UpdateUserCase updateUserCase;
    private final UpdateUserPasswordCase updateUserPasswordCase;
    private final IResponseService responseService;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UserResponse>> getUser(@AuthenticationPrincipal User user) {
        UserResponse response = new UserResponse(user);
        return responseService.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<SuccessResponse<UserResponse>> updateUser(@AuthenticationPrincipal User user,
            @RequestBody @Valid UpdateUserRequest request) {
        UpdateUserParams params = new UpdateUserParams();
        params.setName(request.name());
        User updatedUser = updateUserCase.execute(user, params);
        UserResponse response = new UserResponse(updatedUser);
        return responseService.ok(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse<Boolean>> updatePassword(@AuthenticationPrincipal User user,
            @RequestBody @Valid UpdatePasswordRequest request) {
        UpdateUserPasswordParams params = new UpdateUserPasswordParams();
        params.setNewPassword(request.newPassword());
        params.setPassword(request.password());
        updateUserPasswordCase.execute(user, params);
        return responseService.ok(true);
    }

}
