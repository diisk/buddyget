package br.dev.diisk.presentation.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.dashboard.dtos.DashboardResumeResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IResponseService responseService;

    @GetMapping("/resume")
    public ResponseEntity<SuccessResponse<DashboardResumeResponse>> getResume() {
        return responseService.ok(null);
    }

}
