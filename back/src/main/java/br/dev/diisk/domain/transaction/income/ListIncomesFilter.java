package br.dev.diisk.domain.transaction.income;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListIncomesFilter {

        private LocalDateTime startReferenceDate;
        private LocalDateTime endReferenceDate;
        private Boolean onlyPending;
        private Long categoryId;
}
