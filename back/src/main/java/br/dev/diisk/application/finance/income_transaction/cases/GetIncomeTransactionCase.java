package br.dev.diisk.application.finance.income_transaction.cases;

import org.springframework.stereotype.Service;

import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.shared.exceptions.DatabaseValueNotFoundException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetIncomeTransactionCase {

    private final IIncomeTransactionRepository incomeRepository;

    @Transactional
    public IncomeTransaction execute(User user, Long incomeTransactionId) {
        IncomeTransaction incomeTransaction = incomeRepository.findById(incomeTransactionId).orElse(null);
        if (incomeTransaction == null || incomeTransaction.getUserId() != user.getId())
            throw new DatabaseValueNotFoundException(getClass(), incomeTransactionId.toString());

        return incomeTransaction;

    }
}
