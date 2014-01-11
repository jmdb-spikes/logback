package jmdb.spikes.logback;


import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogbackWithCustomDateFormat {

    private static final Logger logger = LoggerFactory.getLogger(LogbackWithCustomDateFormat.class);

    public static void main(String[] args) {
        new LogbackWithCustomDateFormat().run();
    }

    private LogbackWithCustomDateFormat() {
        init();
    }

    public void run() {
        logger.info("Hello logback!");
    }

    private void init() {
        LogbackDemoApp.resetLogging();
        LogbackDemoApp.initialiseConsoleLogging(Level.INFO, "%date{yyyy-mm-dd'T'hh:MM:ss.SSSZZ}  %message%n");
    }



}