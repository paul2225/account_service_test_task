package com.example.account_service.statistics;

import java.time.LocalDateTime;

public class MethodStatistics {
    private LocalDateTime firstExecutionTime;
    private Long totalExecutionNumber;

    public MethodStatistics(LocalDateTime firstExecutionTime, Long totalExecutionNumber) {
        this.firstExecutionTime = firstExecutionTime;
        this.totalExecutionNumber = totalExecutionNumber;
    }

    public LocalDateTime getFirstExecutionTime() {
        return firstExecutionTime;
    }

    public void setFirstExecutionTime(LocalDateTime firstExecutionTime) {
        this.firstExecutionTime = firstExecutionTime;
    }

    public Long getTotalExecutionNumber() {
        return totalExecutionNumber;
    }

    public void setTotalExecutionNumber(Long totalExecutionNumber) {
        this.totalExecutionNumber = totalExecutionNumber;
    }
}
