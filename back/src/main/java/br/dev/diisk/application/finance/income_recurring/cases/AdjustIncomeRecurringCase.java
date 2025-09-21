package br.dev.diisk.application.finance.income_recurring.cases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.AdjustRecurringCase;
import br.dev.diisk.domain.finance.Transaction;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.infra.finance.income_recurring.IncomeRecurringRepository;
import br.dev.diisk.infra.finance.income_transaction.IncomeTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdjustIncomeRecurringCase {

    private final AdjustRecurringCase adjustRecurringCase;
    private final IncomeTransactionRepository incomeTransactionRepository;
    private final IncomeRecurringRepository incomeRecurringRepository;

    @Transactional
    public void execute(IncomeRecurring incomeRecurring) {
        List<Transaction> relatedTransactions = incomeTransactionRepository
                .findAllRecurringRelatedBy(List.of(incomeRecurring.getId())).stream().map(it -> (Transaction) it)
                .toList();
        adjustRecurringCase.execute(incomeRecurring, relatedTransactions, ir -> {
            incomeRecurringRepository.save((IncomeRecurring) ir);
            return null;
        });
    }
}
