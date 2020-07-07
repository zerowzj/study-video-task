package com.xdf.ucan.remix.video.task.support.trace;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.joda.time.Period;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class JobTraceAspect {

    private static final String X_NAME = "name";

    private static final String X_TRACK_ID = "track_id";

    @Around("@annotation(trace)")
    public Object around(ProceedingJoinPoint joinPoint, JobTrace trace) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        //
        String jobName = trace.jobName();
        long traceId = System.currentTimeMillis();
        MDC.put(X_NAME, jobName);
        MDC.put(X_TRACK_ID, String.valueOf(traceId));

        Signature signature = joinPoint.getSignature();
        String kind = joinPoint.getKind();
        Object[] args = joinPoint.getArgs();
        try {
            signature.getName();
            signature.getDeclaringType();
            signature.getDeclaringTypeName();

            signature.toLongString();
            signature.toShortString();
            signature.toString();

            return joinPoint.proceed();
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            Period period = new Period(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            log.info("{} cost time [{}]m [{}] sec [{}] ms", signature.toShortString(),
                    period.getMinutes(), period.getSeconds(), period.getMillis());
            MDC.remove(X_NAME);
            MDC.remove(X_TRACK_ID);
        }
    }
}
