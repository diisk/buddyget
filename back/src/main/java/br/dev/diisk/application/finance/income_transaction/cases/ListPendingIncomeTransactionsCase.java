package br.dev.diisk.application.finance.income_transaction.cases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.finance.ListRecurringReferenceDatesCase;
import br.dev.diisk.application.finance.income_transaction.dtos.PendingIncomeTransactionDTO;
import br.dev.diisk.domain.finance.TransactionStatusEnum;
import br.dev.diisk.domain.finance.income_recurring.IncomeRecurring;
import br.dev.diisk.domain.finance.income_recurring.IIncomeRecurringRepository;
import br.dev.diisk.domain.finance.income_transaction.IncomeTransaction;
import br.dev.diisk.domain.finance.income_transaction.IIncomeTransactionRepository;
import br.dev.diisk.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListPendingIncomeTransactionsCase {

    private final IIncomeTransactionRepository incomeRepository;
    private final IIncomeRecurringRepository incomeRecurringRepository;
    private final ListRecurringReferenceDatesCase listRecurringReferenceDates;

    @Transactional
    public List<PendingIncomeTransactionDTO> execute(User user) {
        List<PendingIncomeTransactionDTO> pendingTransactions = incomeRepository.findAllPendingBy(user.getId())
                .stream().map(PendingIncomeTransactionDTO::new).collect(Collectors.toList());
        List<IncomeRecurring> activeRecurrings = incomeRecurringRepository.findAllActiveBy(user.getId());
        List<Long> incomeRecurringIds = activeRecurrings.stream()
                .map(IncomeRecurring::getId)
                .toList();
        List<IncomeTransaction> recurringRelatedTransactions = incomeRepository
                .findAllRecurringRelatedBy(incomeRecurringIds);

        for (IncomeRecurring recurring : activeRecurrings) {
            List<IncomeTransaction> relatedTransactions = recurringRelatedTransactions.stream()
                    .filter(transaction -> {
                        IncomeRecurring transactionRecurring = transaction.getIncomeRecurring();
                        return transactionRecurring != null && transactionRecurring.getId().equals(recurring.getId());
                    }).toList();

            List<LocalDateTime> missingReferenceDates = listRecurringReferenceDates.execute(recurring).stream()
                    .filter(referenceDate -> {
                        return relatedTransactions.stream()
                                .noneMatch(transaction -> transaction.getRecurringReferenceDate()
                                        .isEqual(referenceDate));
                    }).toList();

            if (missingReferenceDates.size() == 0)
                continue;

            List<PendingIncomeTransactionDTO> newTransactions = missingReferenceDates.stream()
                    .map(referenceDate -> {
                        String status = TransactionStatusEnum.PENDING.getDescription();
                        return new PendingIncomeTransactionDTO(recurring, referenceDate,
                                status);
                    })
                    .collect(Collectors.toList());
            pendingTransactions.addAll(newTransactions);
        }

        pendingTransactions.sort((a, b) -> a.createdAt().compareTo(b.createdAt()));
        return pendingTransactions;
    }
}
