package br.dev.diisk.application.cases.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.dtos.expense.AddFixedExpenseDto;
import br.dev.diisk.application.mappers.expense.AddFixedExpenseDtoToFixedExpenseMapper;
import br.dev.diisk.domain.entities.expense.FixedExpense;
import br.dev.diisk.domain.entities.user.User;
import br.dev.diisk.domain.enums.category.CategoryTypeEnum;
import br.dev.diisk.domain.enums.expense.FixedExpenseTypeEnum;
import br.dev.diisk.domain.exceptions.ExcessiveOptionalFieldException;
import br.dev.diisk.domain.exceptions.RequiredFieldException;
import br.dev.diisk.domain.exceptions.RequiredOptionalFieldException;
import br.dev.diisk.domain.exceptions.date.PeriodOrderException;
import br.dev.diisk.domain.exceptions.date.PeriodRangeLesserException;
import br.dev.diisk.domain.exceptions.persistence.DbValueNotFoundException;
import br.dev.diisk.domain.repositories.expense.IFixedExpenseRepository;
import br.dev.diisk.infra.services.MessageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddFixedExpenseCase {

    private final IFixedExpenseRepository fixedExpenseRepository;
    private final AddFixedExpenseDtoToFixedExpenseMapper addFixedExpenseDtoToFixedExpense;
    private final UtilService utilService;
    private final MessageService messageService;

    public FixedExpense execute(User user, AddFixedExpenseDto dto) {

        validateDatesOrder(dto);
        switch (dto.getType()) {
            case UNDEFINED_TIME:
                validateRequiredAmount(dto);
                break;
            case DEADLINE:
                validateExcessiveFieldsAmountAndTotalAmount(dto);
                validateExcessiveFieldsInstallmentsAndEndReference(dto);
                validateIfHasOptionalAmount(dto);
                validateIfHasOptionalDeadline(dto);
                break;
        }

        applyDefaultValues(dto);
        validateIfDurationIsGreaterThanOneMonth(dto);

        FixedExpense fixedExpense = addFixedExpenseDtoToFixedExpense.apply(user, dto);

        validateIfCategoryIsValid(user, fixedExpense);

        fixedExpense.setUser(user);

        fixedExpenseRepository.save(fixedExpense);

        return fixedExpense;

    }

    private void validateIfHasOptionalDeadline(AddFixedExpenseDto dto) {
        if (dto.getEndReference() == null && dto.getTotalInstallments() == null)
            throw new RequiredOptionalFieldException(getClass(), "endReference, totalInstallments");

    }

    private void validateIfHasOptionalAmount(AddFixedExpenseDto dto) {
        if (dto.getTotalAmount() == null && dto.getAmount() == null)
            throw new RequiredOptionalFieldException(getClass(), "amount, totalAmount");
    }

    private void validateRequiredAmount(AddFixedExpenseDto dto) {
        if (dto.getAmount() == null)
            throw new RequiredFieldException(getClass(), "amount");
    }

    private void validateExcessiveFieldsAmountAndTotalAmount(AddFixedExpenseDto dto) {
        if (dto.getAmount() != null && dto.getTotalAmount() != null) {
            throw new ExcessiveOptionalFieldException(getClass(), "amount, totalAmount");
        }
    }

    private void validateExcessiveFieldsInstallmentsAndEndReference(AddFixedExpenseDto dto) {
        if (dto.getEndReference() != null && dto.getTotalInstallments() != null)
            throw new ExcessiveOptionalFieldException(getClass(), "endReference, totalInstallments");
    }

    private void validateIfCategoryIsValid(User user, FixedExpense expense) {
        if (expense.getCategory().getType() != CategoryTypeEnum.EXPENSE)
            throw new DbValueNotFoundException(getClass(), "categoryId");
    }

    private void validateIfDurationIsGreaterThanOneMonth(AddFixedExpenseDto dto) {
        if (dto.getType() == FixedExpenseTypeEnum.DEADLINE && dto.getTotalInstallments() <= 1)
            throw new PeriodRangeLesserException(getClass(),
                    dto.getStartReference().toString()
                            + " - "
                            + dto.getEndReference().toString()
                            + ", "
                            + messageService.getMessage("wording.expected").toLowerCase()
                            + ": 2 "
                            + messageService.getMessage("wording.months").toLowerCase());

    }

    private void applyDefaultValues(AddFixedExpenseDto dto) {
        dto.setStartReference(utilService.toReference(dto.getStartReference()));

        switch (dto.getType()) {
            case UNDEFINED_TIME:
                dto.setEndReference(null);
                dto.setTotalInstallments(null);
                dto.setTotalAmount(null);
                break;
            case DEADLINE:
                if (dto.getTotalInstallments() == null) {
                    dto.setEndReference(utilService.toReference(dto.getEndReference(), true));
                    dto.setTotalInstallments(
                            utilService.getMonthsBetweenReferences(dto.getStartReference(), dto.getEndReference()) + 1);
                } else if (dto.getEndReference() == null) {
                    LocalDateTime endReference = dto.getStartReference().plusMonths(dto.getTotalInstallments() - 1);
                    dto.setEndReference(utilService.toReference(endReference, true));
                }

                if (dto.getAmount() == null)
                    dto.setAmount(utilService.divide(dto.getTotalAmount(), new BigDecimal(dto.getTotalInstallments())));

                if (dto.getTotalAmount() == null)
                    dto.setTotalAmount(dto.getAmount().multiply(new BigDecimal(dto.getTotalInstallments())));
                break;

        }
    }

    private void validateDatesOrder(AddFixedExpenseDto dto) {
        if (dto.getEndReference() != null && dto.getEndReference().isBefore(dto.getStartReference())) {
            throw new PeriodOrderException(getClass(),
                    dto.getStartReference().toString() + " - " + dto.getEndReference().toString());
        }
    }

}
