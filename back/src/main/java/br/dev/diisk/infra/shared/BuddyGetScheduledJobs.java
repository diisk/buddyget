package br.dev.diisk.infra.shared;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.dev.diisk.application.finance.income_transaction.cases.GenerateMonthlyIncomeTransactionCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jobs específicos para o sistema BuddyGet
 * Exemplos de tarefas que podem ser executadas periodicamente
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BuddyGetScheduledJobs {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final GenerateMonthlyIncomeTransactionCase generateMonthlyIncomeTransactionCase;

    @Value("${spring.profiles.active:dev}")
    private String env;

    // /**
    // * Job que executa de 4 em 4 horas para processamento de despesas recorrentes
    // * Executa às 00:00, 04:00, 08:00, 12:00, 16:00, 20:00
    // */
    // @Scheduled(cron = "0 0 */4 * * *")
    // public void processRecurringExpenses() {
    // log.info("Iniciando processamento de despesas recorrentes - {}",
    // LocalDateTime.now().format(formatter));

    // try {
    // // Lógica para processar despesas fixas que devem ser criadas
    // processFixedExpenses();

    // log.info("Processamento de despesas recorrentes concluído com sucesso");

    // } catch (Exception e) {
    // log.error("Erro ao processar despesas recorrentes: {}", e.getMessage(), e);
    // }
    // }

    /**
     * Job de teste que executa a cada 30 segundos (apenas para desenvolvimento)
     * Comente ou remova em produção
     */
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    public void testJob() {
        if (env != "dev")
            return;
        System.out.println("Job de teste executando - env:" + env + " :" + LocalDateTime.now().format(formatter));
        generateMonthlyIncomeTransactionCase.execute();
        System.out.println("Jobs executados com sucesso - env:" + env + " :" + LocalDateTime.now().format(formatter));
    }

    /**
     * Job que executa a cada 4 horas para gerar transações de receitas mensais
     */
    @Scheduled(fixedRate = 4 * 60 * 60 * 1000, initialDelay = 10000)
    public void generateMonthlyIncomeTransactionJob() {
        if (env.toLowerCase() == "dev")
            return;
        generateMonthlyIncomeTransactionCase.execute();
    }

}
