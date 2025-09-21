package br.dev.diisk.presentation.finance.income_recurring;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.dev.diisk.application.finance.income_recurring.cases.AddIncomeRecurringCase;
import br.dev.diisk.application.finance.income_recurring.cases.DeleteIncomeRecurringCase;
import br.dev.diisk.application.finance.income_recurring.cases.EndIncomeRecurringCase;
import br.dev.diisk.application.finance.income_recurring.cases.ListIncomeRecurringsCase;
import br.dev.diisk.application.finance.income_recurring.cases.PayIncomeRecurringCase;
import br.dev.diisk.application.finance.income_recurring.dtos.AddIncomeRecurringParams;
import br.dev.diisk.application.finance.income_recurring.dtos.EndIncomeRecurringParams;
import br.dev.diisk.application.finance.income_recurring.dtos.PayIncomeRecurringParams;
import br.dev.diisk.application.shared.services.IResponseService;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.ListIncomeRecurringsFilter;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.infra.shared.dtos.PageResponse;
import br.dev.diisk.infra.shared.dtos.SuccessResponse;
import br.dev.diisk.presentation.finance.income_recurring.dtos.IncomeRecurringResponse;
import br.dev.diisk.presentation.finance.income_recurring.dtos.PayIncomeRecurringRequest;
import br.dev.diisk.presentation.finance.income_recurring.dtos.CreateIncomeRecurringRequest;
import br.dev.diisk.presentation.finance.income_recurring.dtos.EndIncomeRecurringRequest;
import br.dev.diisk.presentation.finance.income_transaction.dtos.IncomeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/recurring/incomes")
@RequiredArgsConstructor
public class IncomeRecurringController {

        private final IResponseService responseService;
        private final AddIncomeRecurringCase addIncomeRecurringCase;
        private final ListIncomeRecurringsCase listIncomeRecurringsCase;
        private final DeleteIncomeRecurringCase deleteIncomeRecurringCase;
        private final PayIncomeRecurringCase payIncomeRecurringCase;
        private final EndIncomeRecurringCase endIncomeRecurringCase;

        @GetMapping
        public ResponseEntity<SuccessResponse<PageResponse<IncomeRecurringResponse>>> listIncomeRecurrings(
                        @RequestParam(required = false) String searchString,
                        Pageable pageable,
                        @AuthenticationPrincipal User user) {
                ListIncomeRecurringsFilter filter = new ListIncomeRecurringsFilter();
                filter.setSearchString(searchString);
                Page<IncomeRecurring> incomes = listIncomeRecurringsCase.execute(
                                user, filter, pageable);
                PageResponse<IncomeRecurringResponse> response = responseService.getPageResponse(user, incomes,
                                IncomeRecurringResponse::new);
                return responseService.ok(response);
        }

        @PostMapping("/{id}/pay")
        public ResponseEntity<SuccessResponse<IncomeResponse>> payIncomeRecurring(
                        @RequestBody @Valid PayIncomeRecurringRequest request, @PathVariable Long id,
                        @AuthenticationPrincipal User user) {
                PayIncomeRecurringParams params = new PayIncomeRecurringParams();
                params.setReferenceDate(request.referenceDate());
                params.setPaymentDate(request.paymentDate());

                IncomeTransaction incomeTransaction = payIncomeRecurringCase.execute(user, id, params);
                IncomeResponse response = new IncomeResponse(incomeTransaction);

                return responseService.ok(response);
        }

        @PostMapping
        public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> createRecurringIncome(
                        @RequestBody @Valid CreateIncomeRecurringRequest request,
                        @AuthenticationPrincipal User user) {
                AddIncomeRecurringParams params = new AddIncomeRecurringParams();
                params.setDescription(request.description());
                params.setStartDate(request.startDate());
                params.setEndDate(request.endDate());
                params.setValue(request.value());
                params.setCategoryId(request.categoryId());
                IncomeRecurring incomeRecurring = addIncomeRecurringCase.execute(
                                user, params);
                IncomeRecurringResponse response = new IncomeRecurringResponse(incomeRecurring);
                return responseService.ok(response);
        }

        @PatchMapping("/{id}/end")
        public ResponseEntity<SuccessResponse<IncomeRecurringResponse>> endIncomeRecurring(
                        @PathVariable Long id, @RequestBody @Valid EndIncomeRecurringRequest request,
                        @AuthenticationPrincipal User user) {
                EndIncomeRecurringParams params = new EndIncomeRecurringParams();
                params.setEndDate(request.endDate());
                IncomeRecurring incomeRecurring = endIncomeRecurringCase.execute(user, params, id);
                IncomeRecurringResponse response = new IncomeRecurringResponse(incomeRecurring);
                return responseService.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<SuccessResponse<Boolean>> deleteRecurringIncome(@PathVariable Long id,
                        @AuthenticationPrincipal User user) {
                deleteIncomeRecurringCase.execute(user, id);
                return responseService.ok(true);
        }
}
