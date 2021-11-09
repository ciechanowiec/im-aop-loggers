package im.aop.loggers.advice.before;

import java.time.Duration;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import im.aop.loggers.AopLoggersProperties;
import im.aop.loggers.Level;
import im.aop.loggers.messageinterpolation.JoinPointStringSupplierRegistrar;
import im.aop.loggers.messageinterpolation.StringSubstitutor;
import im.aop.loggers.messageinterpolation.StringSupplierLookup;
import im.aop.loggers.util.LoggerUtil;

public class LogBeforeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogBeforeService.class);

  private static final LoggerUtil LOGGER_SERVICE = new LoggerUtil();

  private static final StringSubstitutor STRING_SUBSTITUTOR = new StringSubstitutor();

  private static final JoinPointStringSupplierRegistrar JOIN_POINT_STRING_SUPPLIER_REGISTRAR =
      new JoinPointStringSupplierRegistrar();

  private final AopLoggersProperties aopLoggersProperties;

  public LogBeforeService(final AopLoggersProperties aopLoggersProperties) {
    this.aopLoggersProperties = Objects.requireNonNull(aopLoggersProperties);
  }

  public void logBefore(final JoinPoint joinPoint, final LogBefore annotation) {
    final long enteringTime = System.nanoTime();

    final Logger logger = LOGGER_SERVICE.getLogger(annotation.declaringClass(), joinPoint);
    final Level enteringLevel = getEnteringLevel(annotation.level());
    if (isLoggerLevelDisabled(logger, enteringLevel)) {
      logElapsed(enteringTime);
      return;
    }

    final StringSupplierLookup stringLookup = new StringSupplierLookup();

    logEnteringMessage(
        joinPoint, enteringLevel, annotation.enteringMessage(), logger, stringLookup);
    logElapsed(enteringTime);
  }

  private void logElapsed(long enteringTime) {
    LOGGER.debug("[logBefore] elapsed [{}]", Duration.ofNanos(System.nanoTime() - enteringTime));
  }

  private boolean isLoggerLevelDisabled(final Logger logger, final Level level) {
    return LOGGER_SERVICE.isEnabled(logger, level) == false;
  }

  private void logEnteringMessage(
      final JoinPoint joinPoint,
      final Level enteringLevel,
      final String enteringMessage,
      final Logger logger,
      final StringSupplierLookup stringLookup) {
    JOIN_POINT_STRING_SUPPLIER_REGISTRAR.register(stringLookup, joinPoint);

    final String message =
        STRING_SUBSTITUTOR.substitute(getEnteringMesage(enteringMessage), stringLookup);

    LOGGER_SERVICE.log(logger, enteringLevel, message);
  }

  private Level getEnteringLevel(final Level enteringLevel) {
    return enteringLevel == Level.DEFAULT ? aopLoggersProperties.getEnteringLevel() : enteringLevel;
  }

  private String getEnteringMesage(final String enteringMessage) {
    return enteringMessage.length() == 0
        ? aopLoggersProperties.getEnteringMessage()
        : enteringMessage;
  }
}
