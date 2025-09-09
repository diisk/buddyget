package br.dev.diisk.application.finance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.finance.Recurring;
import br.dev.diisk.domain.finance.expense_transaction.ExpenseTransaction;
import br.dev.diisk.infra.finance.expense_transaction.ExpenseTransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjustRecurringCase {

    private final UtilService utilService;
    private final ExpenseTransactionRepository expenseTransactionRepository;
    private final ListRecurringReferenceDatesCase listRecurringReferenceDatesCase;

    public void execute(Recurring recurring, Function<Recurring, Void> saveFunction) {
        List<ExpenseTransaction> relatedTransactions = expenseTransactionRepository
                .findAllRecurringRelatedBy(List.of(recurring.getId()));
        List<LocalDateTime> referenceDates = listRecurringReferenceDatesCase.execute(recurring);
        Boolean isActive = referenceDates.stream().anyMatch(refDate -> relatedTransactions.stream()
                .noneMatch(rt -> rt.getPaymentDate() != null
                        && utilService.isReferenceDateEquals(refDate, rt.getRecurringReferenceDate())));
        if (recurring.isActive() != isActive) {
            recurring.setActive(isActive);
            saveFunction.apply(recurring);
        }
    }
}
