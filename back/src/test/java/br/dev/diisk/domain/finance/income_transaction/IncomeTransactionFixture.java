package br.dev.diisk.domain.finance.income_transaction;

import br.dev.diisk.domain.category.Category;
import br.dev.diisk.domain.category.CategoryFixture;
import br.dev.diisk.domain.category.CategoryTypeEnum;
import br.dev.diisk.domain.user.User;
import br.dev.diisk.domain.user.UserFixture;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IncomeTransactionFixture {
    
    public static IncomeTransaction umaIncomeTransactionComId(Long id) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        IncomeTransaction incomeTransaction = new IncomeTransaction(
                "Receita Teste",
                category,
                new BigDecimal("1000.00"),
                LocalDateTime.now(),
                user
        );
        incomeTransaction.setId(id);
        setCreatedAt(incomeTransaction, LocalDateTime.now());
        return incomeTransaction;
    }
    
    public static IncomeTransaction umaIncomeTransactionComStatus(Long id, String status) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        LocalDateTime date = status.equalsIgnoreCase("recebido") ? LocalDateTime.now() : null;
        
        IncomeTransaction incomeTransaction = new IncomeTransaction(
                "Receita Teste",
                category,
                new BigDecimal("1000.00"),
                date,
                user
        );
        incomeTransaction.setId(id);
        setCreatedAt(incomeTransaction, LocalDateTime.now());
        return incomeTransaction;
    }
    
    public static IncomeTransaction umaIncomeTransactionComCreatedAt(Long id) {
        User user = UserFixture.umUsuarioComId(1L);
        Category category = CategoryFixture.umaCategoriaComId(1L, CategoryTypeEnum.INCOME, user);
        
        IncomeTransaction incomeTransaction = new IncomeTransaction(
                "Receita Teste",
                category,
                new BigDecimal("1000.00"),
                LocalDateTime.now(),
                user
        );
        incomeTransaction.setId(id);
        setCreatedAt(incomeTransaction, LocalDateTime.now());
        return incomeTransaction;
    }
    
    private static void setCreatedAt(IncomeTransaction transaction, LocalDateTime createdAt) {
        try {
            // Navegar pela hierarquia: IncomeTransaction -> Transaction -> GenericTransaction -> UserRastrableEntity -> RastreableEntity
            Class<?> rastreableEntityClass = transaction.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass();
            Field createdAtField = rastreableEntityClass.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(transaction, createdAt);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao setar createdAt", e);
        }
    }
}
