package org.springframework.samples.petclinic.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

//Component에서 Bean으로 등록
@Component
@Aspect
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //이 애노테이션 주변으로 할일 정의
    //메서드가 호출되기 전, 후, 오류가 났을 때 다 사용할 수 있는게 Around
    //특정 표현식을 쓰면 @annotation 을 안붙이고도 정의할 수 있음
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //스프링이 제공하는 StopWatch
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object ret = joinPoint.proceed();

        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());

        return ret;
    }
}
