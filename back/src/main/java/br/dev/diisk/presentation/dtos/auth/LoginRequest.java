package br.dev.diisk.presentation.dtos.auth;

import br.dev.diisk.domain.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

        @Email(message = ValidationMessages.INVALID_FIELD)
        private String email;
        @NotBlank(message = ValidationMessages.BLANK_OR_NULL_FIELD)
        private String password;
}
