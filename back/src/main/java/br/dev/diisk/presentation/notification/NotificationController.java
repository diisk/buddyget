package br.dev.diisk.presentation.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final IResponseService responseService;

    @GetMapping
    public ResponseEntity<SuccessResponse<PageResponse<NotificationResponse>>> listNotifications(
            Pageable pageable) {
        return responseService.ok(null);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<SuccessResponse<Boolean>> markNotificationAsRead(@PathVariable Long id) {
        return responseService.ok(true);
    }
}
