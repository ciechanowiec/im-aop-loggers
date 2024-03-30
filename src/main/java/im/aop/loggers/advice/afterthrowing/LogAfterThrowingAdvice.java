package im.aop.loggers.advice.afterthrowing;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Spring's AOP Advice for {@link LogAfterThrowing}.
 *
 * @author Andy Lian
 */
@Aspect
public class LogAfterThrowingAdvice {

  @Autowired
  private LogAfterThrowingService logAfterThrowingService;

  @Pointcut("execution(* *(..))")
  void allMethods() {
  }

  @Pointcut("execution(String *.toString())")
  void toStringMethod() {
  }

  @Pointcut(value = "@annotation(logAfterThrowing)", argNames = "logAfterThrowing")
  void logAfterThrowingMethodContext(final LogAfterThrowing logAfterThrowing) {
  }

  @AfterThrowing(
      value = "allMethods() && logAfterThrowingMethodContext(logAfterThrowing)",
      argNames = "joinPoint, logAfterThrowing, exception",
      throwing = "exception")
  void logAfterThrowingMethodContext(
      final JoinPoint joinPoint,
      final LogAfterThrowing logAfterThrowing,
      final Throwable exception) {
    logAfterThrowing(joinPoint, logAfterThrowing, exception);
  }

  @Pointcut(value = "@within(logAfterThrowing)", argNames = "logAfterThrowing")
  void logAfterThrowingClassContext(final LogAfterThrowing logAfterThrowing) {
  }

  @AfterThrowing(
      value =
          "allMethods() && !toStringMethod() && logAfterThrowingClassContext(logAfterThrowing)",
      argNames = "joinPoint, logAfterThrowing, exception",
      throwing = "exception")
  void logAfterThrowingClassContext(
      final JoinPoint joinPoint,
      final LogAfterThrowing logAfterThrowing,
      final Throwable exception) {
    logAfterThrowing(joinPoint, logAfterThrowing, exception);
  }

  protected void logAfterThrowing(
      final JoinPoint joinPoint,
      final LogAfterThrowing logAfterThrowing,
      final Throwable exception) {
    logAfterThrowingService.logAfterThrowing(joinPoint, logAfterThrowing, exception);
  }
}
