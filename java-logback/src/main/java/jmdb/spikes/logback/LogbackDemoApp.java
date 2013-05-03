package jmdb.spikes.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

public class LogbackDemoApp {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogbackDemoApp.class);

    /**
     * Possible patterns
     *
     * %date{ISO8601}
     * [%-10thread]
     */
    public static void main(String[] args) {
        initialiseConsoleLogging(Level.TRACE, "[%date{yyyy-mm-dd hh:MM:ss.SSS z (Z)}] %-6level %-35class - %message%n");

        log.trace("VERY LOW LEVEL");
        log.debug("Some debug...");
        log.info("Hello [{}]", "jim");
        log.warn("Ooh a warning...");
        log.error("OMG! AN ERROR!!!");
    }

    private static void initialiseConsoleLogging(Level level, String pattern) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        root.setLevel(level);

        ConsoleAppender<ILoggingEvent> consoleAppender = (ConsoleAppender<ILoggingEvent>) root.getAppender("console");

        consoleAppender.stop();

        PatternLayoutEncoder layoutEncoder = new PatternLayoutEncoder();
        layoutEncoder.setPattern(pattern);
        layoutEncoder.setContext(context);
        layoutEncoder.start();

        consoleAppender.setEncoder(layoutEncoder);
        consoleAppender.setContext(context);

        consoleAppender.start();

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }



}