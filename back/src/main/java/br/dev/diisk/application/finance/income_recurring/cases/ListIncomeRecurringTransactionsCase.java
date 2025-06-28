package br.dev.diisk.application.finance.income_recurring.cases;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListIncomeRecurringTransactionsCase {

    private final IIncomeTransactionRepository incomeRepository;

    @Transactional
    public List<IncomeTransaction> execute(User user, Long incomeRecurringId) {
        Set<IncomeTransaction> transactions = incomeRepository.findByRecurring(user.getId(), incomeRecurringId);
        List<IncomeTransaction> transactionsList = transactions.stream()
                .sorted((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()))
                .sorted((t1, t2) -> compareTransactions(t1, t2))
                .toList();

        return transactionsList;

    }

    private Integer compareTransactions(IncomeTransaction t1, IncomeTransaction t2) {
        Integer compareValue1 = getTransactionCompareValue(t1);
        Integer compareValue2 = getTransactionCompareValue(t2);
        return compareValue2.compareTo(compareValue1);
    }

    private Integer getTransactionCompareValue(IncomeTransaction transaction) {
        switch (transaction.getStatus().toLowerCase()) {
            case "recebido":
                return 0;
            case "pendente":
                return 1;
            default:
                return -1;
        }
    }
}
