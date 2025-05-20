package br.dev.diisk.application.cases.expense;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.dev.diisk.application.UtilService;
import br.dev.diisk.application.dtos.expense.UpdateFixedExpenseDto;
import br.dev.diisk.application.mappers.expense.UpdateFixedExpenseDtoToFixedExpenseMapper;
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
public class UpdateFixedExpenseCase {

    private final IFixedExpenseRepository fixedExpenseRepository;
    private final UpdateFixedExpenseDtoToFixedExpenseMapper mapper;
    private final GetFixedExpenseCase getFixedExpenseCase;
    private final UtilService utilService;
    private final MessageService messageService;

    public FixedExpense execute(User user, Long fixedExpenseId, UpdateFixedExpenseDto dto) {

        validateExcessiveFieldsAmountAndTotalAmount(dto);
        validateExcessiveFieldsInstallmentsAndEndReference(dto);

        FixedExpense fixedExpense = getFixedExpenseCase.execute(user, fixedExpenseId);

        doTypeChangesValidations(dto, fixedExpense);

        mapper.update(user, dto, fixedExpense);

        validateIfCategoryIsValid(user, fixedExpense);

        applyDefaultValues(dto, fixedExpense);

        validateIfEndsBeforeStarts(fixedExpense);

        validateIfDurationIsGreaterThanOneMonth(fixedExpense);

        fixedExpenseRepository.save(fixedExpense);

        return fixedExpense;

    }

    private void doTypeChangesValidations(UpdateFixedExpenseDto dto, FixedExpense fixedExpense) {
        if (dto.getType() != null && dto.getType() != fixedExpense.getType()) {
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

        }
    }

    private void validateIfHasOptionalDeadline(UpdateFixedExpenseDto dto) {
        if (dto.getEndReference() == null && dto.getTotalInstallments() == null)
            throw new RequiredOptionalFieldException(getClass(), "endReference, totalInstallments");

    }

    private void validateIfHasOptionalAmount(UpdateFixedExpenseDto dto) {
        if (dto.getTotalAmount() == null && dto.getAmount() == null)
            throw new RequiredOptionalFieldException(getClass(), "amount, totalAmount");
    }

    private void validateRequiredAmount(UpdateFixedExpenseDto dto) {
        if (dto.getAmount() == null)
            throw new RequiredFieldException(getClass(), "amount");
    }

    private void validateExcessiveFieldsAmountAndTotalAmount(UpdateFixedExpenseDto dto) {
        if (dto.getAmount() != null && dto.getTotalAmount() != null) {
            throw new ExcessiveOptionalFieldException(getClass(), "amount, totalAmount");
        }
    }

    private void validateExcessiveFieldsInstallmentsAndEndReference(UpdateFixedExpenseDto dto) {
        if (dto.getEndReference() != null && dto.getTotalInstallments() != null)
            throw new ExcessiveOptionalFieldException(getClass(), "endReference, totalInstallments");
    }

    private void validateIfCategoryIsValid(User user, FixedExpense expense) {
        if (expense.getCategory().getType() != CategoryTypeEnum.EXPENSE)
            throw new DbValueNotFoundException(getClass(), "categoryId");
    }

    private void applyDefaultValues(UpdateFixedExpenseDto dto, FixedExpense fixedExpense) {
        switch (fixedExpense.getType()) {
            case UNDEFINED_TIME:
                fixedExpense.setEndReference(null);
                fixedExpense.setTotalInstallments(null);
                fixedExpense.setTotalAmount(null);
                break;
            case DEADLINE:
                if (dto.getStartReference() != null || dto.getEndReference() != null
                        || dto.getTotalInstallments() != null) {
                    if (dto.getStartReference() != null)
                        fixedExpense.setStartReference(utilService.toReference(fixedExpense.getStartReference()));

                    if (dto.getEndReference() != null)
                        fixedExpense.setEndReference(utilService.toReference(fixedExpense.getEndReference(), true));

                    if (dto.getTotalInstallments() != null) {
                        fixedExpense.setEndReference(utilService.toReference(
                                fixedExpense.getStartReference()
                                        .plusMonths(fixedExpense.getTotalInstallments() - 1),
                                true));
                    } else {
                        fixedExpense
                                .setTotalInstallments(
                                        utilService.getMonthsBetweenReferences(fixedExpense.getStartReference(),
                                                fixedExpense.getEndReference()) + 1);
                    }
                }
                if (dto.getTotalAmount() != null)

                    fixedExpense.setAmount(utilService.divide(dto.getTotalAmount(),
                            new BigDecimal(fixedExpense.getTotalInstallments())));

                if (dto.getAmount() != null)

                    fixedExpense.setTotalAmount(
                            dto.getAmount().multiply(new BigDecimal(fixedExpense.getTotalInstallments())));

                break;
        }
    }

    private void validateIfDurationIsGreaterThanOneMonth(FixedExpense fixedExpense) {
        if (fixedExpense.getType() == FixedExpenseTypeEnum.DEADLINE && fixedExpense.getTotalInstallments() <= 1)
            throw new PeriodRangeLesserException(getClass(),
                    fixedExpense.getStartReference().toString()
                            + " - "
                            + fixedExpense.getEndReference().toString()
                            + ", "
                            + messageService.getMessage("wording.expected").toLowerCase()
                            + ": 2 "
                            + messageService.getMessage("wording.months").toLowerCase());

    }

    private void validateIfEndsBeforeStarts(FixedExpense fixedExpense) {
        if (fixedExpense.getType() == FixedExpenseTypeEnum.DEADLINE)
            if (fixedExpense.getEndReference().isBefore(fixedExpense.getStartReference()))
                throw new PeriodOrderException(getClass(),
                        fixedExpense.getStartReference().toString() + " - "
                                + fixedExpense.getEndReference().toString());

    }

}
