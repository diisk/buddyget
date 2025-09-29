package br.dev.diisk.application.finance.income_recurring.cases;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.income_recurring.dtos.PayIncomeRecurringParams;
import br.dev.diisk.application.finance.income_transaction.cases.AddIncomeTransactionCase;
import br.dev.diisk.application.finance.income_transaction.dtos.AddIncomeTransactionParams;
import br.dev.diisk.application.shared.services.UtilService;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.shared.exceptions.BusinessException;
import br.dev.diisk.domain.shared.exceptions.NullOrEmptyException;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayIncomeRecurringCase {

    private final GetIncomeRecurringCase getIncomeRecurringCase;
    private final AddIncomeTransactionCase addIncomeTransactionCase;
    private final UtilService utilService;

    @Transactional
    public IncomeTransaction execute(User user, Long id, PayIncomeRecurringParams params) {

        if (params.getPaymentDate() == null)
            throw new NullOrEmptyException("A data de pagamento é obrigatória", getClass());

        if (params.getReferenceDate() == null)
            throw new NullOrEmptyException("A data de referência é obrigatória", getClass());

        if (id == null)
            throw new NullOrEmptyException("O id da receita recorrente é obrigatório", getClass());

        IncomeRecurring incomeRecurring = getIncomeRecurringCase.execute(user, id);

        LocalDateTime now = LocalDateTime.now();
        Boolean hasEndDate = incomeRecurring.getEndDate() != null;
        if (params.getPaymentDate().isAfter(now))
            throw new BusinessException(getClass(), "A data de pagamento não pode ser maior que a data atual");

        if (hasEndDate && params.getReferenceDate().isAfter(incomeRecurring.getEndDate()))
            throw new BusinessException(getClass(),
                    "A data de referência não pode ser maior que a data de término da receita recorrente.");

        if (!hasEndDate && params.getReferenceDate().isAfter(now))
            throw new BusinessException(getClass(),
                    "A data de referência não pode ser maior que a data atual para receitas recorrentes sem data término.");

        AddIncomeTransactionParams transactionParams = new AddIncomeTransactionParams();

        transactionParams.setCategoryId(incomeRecurring.getCategoryId());
        transactionParams.setPaymentDate(params.getPaymentDate());
        transactionParams.setDescription(incomeRecurring.getDescription());
        transactionParams.setValue(incomeRecurring.getValue());
        transactionParams.setGoalId(incomeRecurring.getGoal() != null ? incomeRecurring.getGoal().getId() : null);
        transactionParams.setRecurringReferenceDate(utilService.getFirstDayMonthReference(params.getReferenceDate()));
        transactionParams.setIncomeRecurringId(incomeRecurring.getId());

        IncomeTransaction incomeTransaction = addIncomeTransactionCase.execute(user, transactionParams);

        return incomeTransaction;
    }
}
