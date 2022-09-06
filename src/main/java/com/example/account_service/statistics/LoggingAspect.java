package com.example.account_service.statistics;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Aspect
@Component
@EnableScheduling
public class LoggingAspect {

    private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class.getName());
    /**
     * We can store methodsStats in DB if we need to
     */
    private final Map<String, MethodStatistics> methodsStats = new HashMap<>();

    @After("execution(* com.example.account_service.service.impl.AccountServiceImpl.*(..))")
    public void logAroundAllMethods(JoinPoint joinPoint)
    {
        if(!methodsStats.containsKey(joinPoint.getSignature().getName())) {
            methodsStats.put(joinPoint.getSignature().getName(), new MethodStatistics(LocalDateTime.now(), 0L));
        }
        synchronized (this) {
            MethodStatistics methodStatistics = methodsStats.get(joinPoint.getSignature().getName());
            methodStatistics.setTotalExecutionNumber(methodStatistics.getTotalExecutionNumber() + 1);
        }
    }

    public void cleanUpLogs() {
        methodsStats.clear();
    }

    public long getMethodTotalExecutionNumber(String methodName) {
        return methodsStats.get(methodName).getTotalExecutionNumber();
    }

    public double getMethodExecutionsPerMinute(String methodName) {
        long totalExecutionNumber = getMethodTotalExecutionNumber(methodName);
        LocalDateTime firstExecution = methodsStats.get(methodName).getFirstExecutionTime();
        LocalDateTime now = LocalDateTime.now();
        long minutes = firstExecution.until(now, ChronoUnit.MINUTES);
        if (minutes == 0L) {
            minutes++;
        }
        return totalExecutionNumber / minutes;
    }

    @Scheduled(fixedRate = 60000)
    private void logStatistics() {
        for(String methodName : methodsStats.keySet()) {
            LOGGER.info("Method name: %s | Total calls: %s | Calls per minute %s".formatted(methodName,
                    getMethodTotalExecutionNumber(methodName), getMethodExecutionsPerMinute(methodName)));
        }
    }
}
