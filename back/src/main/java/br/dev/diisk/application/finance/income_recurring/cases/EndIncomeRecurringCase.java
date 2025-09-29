package br.dev.diisk.application.finance.income_recurring.cases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.application.finance.income_recurring.dtos.EndIncomeRecurringParams;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EndIncomeRecurringCase {

    private final GetIncomeRecurringCase getIncomeRecurringCase;

    private final IIncomeRecurringRepository incomeRecurringRepository;
    private final IIncomeTransactionRepository incomeTransactionRepository;

    @Transactional
    public IncomeRecurring execute(User user, EndIncomeRecurringParams params, Long incomeRecurringId) {
        IncomeRecurring incomeRecurring = getIncomeRecurringCase.execute(user, incomeRecurringId);

        List<IncomeTransaction> paidTransactions = incomeTransactionRepository
                .findAllRecurringRelatedBy(List.of(incomeRecurringId));

        

        for (IncomeTransaction transaction : paidTransactions) {
            if (transaction.getRecurringReferenceDate() != null && transaction.getRecurringReferenceDate().isAfter(params.getEndDate()))
                throw new BusinessException(getClass(),
                        "Não é possível definir uma data de término anterior a uma despesa já paga.");

        }

        incomeRecurring.defineEndDate(LocalDateTime.now());
        incomeRecurringRepository.save(incomeRecurring);

        return incomeRecurring;

    }
}
