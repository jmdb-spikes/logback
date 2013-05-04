package jmdb.spikes.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TriggeringPolicy;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

public class LogbackDemoApp {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogbackDemoApp.class);
    private static final String STANDARD_OPS_FORMAT = "[%date{yyyy-mm-dd'T'hh:MM:ss.SSS z Z]} %-6level %-35logger{35} - %message%n";

    /**
     * Possible patterns
     * <p/>
     * The logger{size} is the TOTAL width of the logger name, so with shorten things starting with left most package
     * <p/>
     * %date{ISO8601} [%-10thread]
     *
     * @see http://logback.qos.ch/manual/layouts.html
     * @see http://en.wikipedia.org/wiki/ISO_8601
     * @see http://docs.oracle.com/javase/1.4.2/docs/api/java/text/SimpleDateFormat.html
     * @see https://github.com/clj-io/logging
     */
    public static void main(String[] args) {

        initialiseConsoleLogging(Level.TRACE, STANDARD_OPS_FORMAT);


        TimeBasedRollingPolicy sizeAndTimeBasedPolicy = sizeAndTimeTriggeringPolicy(
                3, false, "10mb", "/var/log/logback-demo/lbd-application.%d{yyyy-MM-dd}.log");

        initialiseFileLogging("application-log", Level.TRACE, STANDARD_OPS_FORMAT,
                              "/var/log/logback-demo/lbd-application.log",true,
                              sizeAndTimeBasedPolicy);

        printStatus();

        log.trace("VERY LOW LEVEL");
        log.debug("Some debug...");
        log.info("Hello [{}]", "jim");
        log.warn("Ooh a warning...");
        log.error("OMG! AN ERROR!!!");
        log.error("AN Error with stack trace!", new IllegalAccessException("BOOM!"));
    }

    private static void printStatus() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(context);
    }

    private static void initialiseConsoleLogging(Level level, String pattern) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        root.setLevel(level);
        root.setAdditive(true);

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

    private static void initialiseFileLogging(String appenderName, Level level, String pattern,
                                              String filename, boolean append,
                                              TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        root.setLevel(level);

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();

        PatternLayoutEncoder layoutEncoder = new PatternLayoutEncoder();

        layoutEncoder.setPattern(pattern);
        layoutEncoder.setContext(context);


        rollingFileAppender.setEncoder(layoutEncoder);
        rollingFileAppender.setContext(context);

        rollingFileAppender.setFile(filename);

        rollingFileAppender.setAppend(append); //append to or truncate the file

        rollingFileAppender.setRollingPolicy(timeBasedRollingPolicy);
        rollingFileAppender.setTriggeringPolicy(timeBasedRollingPolicy);

        timeBasedRollingPolicy.setContext(context);
        timeBasedRollingPolicy.setParent(rollingFileAppender);

        layoutEncoder.start();
        timeBasedRollingPolicy.start();
        rollingFileAppender.start();

        root.addAppender(rollingFileAppender);

        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

    }



    public static TimeBasedRollingPolicy<ILoggingEvent> sizeAndTimeTriggeringPolicy(
            int maxNumberOfFiles, boolean cleanHistoryOnStart,
            String maxFileSize, String rollingFilePattern) {

        TimeBasedRollingPolicy timeBasedRollingPolicy = new TimeBasedRollingPolicy();

        timeBasedRollingPolicy.setMaxHistory(maxNumberOfFiles);
        timeBasedRollingPolicy.setCleanHistoryOnStart(cleanHistoryOnStart);
        timeBasedRollingPolicy.setFileNamePattern(rollingFilePattern);


        SizeAndTimeBasedFNATP<ILoggingEvent> sizeAndTimeTriggeringPolicy = new SizeAndTimeBasedFNATP<ILoggingEvent>();
        sizeAndTimeTriggeringPolicy.setTimeBasedRollingPolicy(timeBasedRollingPolicy);

        sizeAndTimeTriggeringPolicy.setMaxFileSize(maxFileSize);



        timeBasedRollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeTriggeringPolicy);

        return timeBasedRollingPolicy;
    }


}