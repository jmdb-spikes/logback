package jmdb.spikes.logback;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import static java.lang.String.format;


public class Log4jDemoApp {

    private static Logger log = Logger.getLogger(Log4jDemoApp.class);
    private static final String LOG_PATTERN = "[%t] %-5p %-25c{1} %x : %m%n";

    public static void main(String[] args) {
        initialiseConsoleLogging();

        log.trace("VERY LOW LEVEL");
        log.debug("Some debug...");
        log.info(format("Hello [%s]", "jim"));
        log.warn("Ooh a warning...");
        log.error("OMG! AN ERROR!!!");
        log.error("AN Error with stack trace!", new IllegalAccessException("BOOM!"));
    }

    public static void initialiseConsoleLogging() {
        Logger logger = Logger.getRootLogger();

        logger.setLevel(Level.TRACE);
        logger.addAppender(new ConsoleAppender(new PatternLayout(LOG_PATTERN)));
    }

}