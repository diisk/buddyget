package br.dev.diisk.application.finance.income_recurring.cases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.income_transaction.cases.DeleteIncomeTransactionCase;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteIncomeRecurringCase {

    private final IIncomeRecurringRepository incomeRecurringRepository;
    private final IIncomeTransactionRepository incomeTransactionRepository;
    private final DeleteIncomeTransactionCase deleteIncomeTransactionCase;

    @Transactional
    public void execute(User user, Long incomeRecurringId) {
        IncomeRecurring incomeRecurring = incomeRecurringRepository.findById(incomeRecurringId).orElse(null);

        if (incomeRecurring == null || !incomeRecurring.getUserId().equals(user.getId()))
            return;

        incomeRecurring.delete();

        List<IncomeTransaction> relatedTransactions = incomeTransactionRepository
                .findAllRecurringRelatedBy(List.of(incomeRecurringId));

        for (IncomeTransaction transaction : relatedTransactions)
            deleteIncomeTransactionCase.execute(user, transaction.getId(), true);

        incomeRecurringRepository.save(incomeRecurring);
    }
}
