package im.aop.loggers.advice.afterreturning;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring's AOP Advice for {@link LogAfterReturning}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogAfterReturningAdvice {

  @Autowired
  private LogAfterReturningService logAfterReturningService;

  @Pointcut("execution(* *(..))")
  void allMethods() {
  }

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {
  }

  @Pointcut(value = "@annotation(logAfterReturning)", argNames = "logAfterReturning")
  void logAfterReturningMethodContext(final LogAfterReturning logAfterReturning) {
  }

  @AfterReturning(
      value = "allMethods() && logAfterReturningMethodContext(logAfterReturning)",
      argNames = "joinPoint, logAfterReturning, returnValue",
      returning = "returnValue")
  void logAfterReturningMethodContext(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue) {
    logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }

  @Pointcut(value = "@within(logAfterReturning)", argNames = "logAfterReturning")
  void logAfterReturningClassContext(final LogAfterReturning logAfterReturning) {
  }

  @AfterReturning(
      value =
          "allMethods() && !toStringMethod() && logAfterReturningClassContext(logAfterReturning)",
      argNames = "joinPoint, logAfterReturning, returnValue",
      returning = "returnValue")
  void logAfterReturningClassContext(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue) {
    logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }

  protected void logAfterReturning(
      final JoinPoint joinPoint,
      final LogAfterReturning logAfterReturning,
      final Object returnValue) {
    logAfterReturningService.logAfterReturning(joinPoint, logAfterReturning, returnValue);
  }
}
