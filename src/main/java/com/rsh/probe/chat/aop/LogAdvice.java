package com.rsh.probe.chat.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
/**
 * writes a trace of each called method into the log at trace level
 * method entry is logged with all parameters
 * leaving a method is logged with the return value
 * the log entries look nice for object whith a good toString implementation
 *
 * which methods of which classes are logged you can determin in application.properties or command line respectivelly
 * TODO this is not appropriate in production
 */
public class LogAdvice {

	@Pointcut(value="execution(* com.rsh.probe.chat.*.*.*(..) )")
	public void loggingPointcut() { }
	@Around("loggingPointcut()")
	public Object applicationLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		String methodName = proceedingJoinPoint.getSignature().getName();
		Class clazz = proceedingJoinPoint.getTarget().getClass();
		String className = clazz.toString();
		Object[] array = proceedingJoinPoint.getArgs();

		Logger log = LoggerFactory.getLogger(clazz);

		log.trace("calling " + className + ": " + methodName + "(" + Arrays.stream(array).map(Object::toString).collect(Collectors.joining()) + ")");

		Object object = proceedingJoinPoint.proceed();
		log.trace("leaving: " + className + ": " + methodName + " returning: " + "(" + Arrays.stream(array).map(Object::toString).collect(Collectors.joining()) + ")");

		return object;
	}
}
