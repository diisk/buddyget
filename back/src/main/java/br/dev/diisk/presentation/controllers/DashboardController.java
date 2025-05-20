package br.dev.diisk.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.dev.diisk.application.dtos.response.SuccessResponse;
import br.dev.diisk.application.services.IResponseService;
import br.dev.diisk.presentation.dtos.dashboard.*;
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
