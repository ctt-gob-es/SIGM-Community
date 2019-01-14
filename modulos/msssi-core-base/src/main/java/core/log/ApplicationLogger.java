package core.log;

import org.apache.log4j.Logger;

public class ApplicationLogger {

    private static final Logger LOGGER = Logger.getLogger(ApplicationLogger.class);
    
    private ApplicationLogger(){        
    }

    public static void error(String error) {
        LOGGER.error(error);
    }

    public static void error(Throwable error) {
        LOGGER.error(error.getMessage());
    }

    public static void error(String format, Exception originalException) {
        LOGGER.error(originalException.getMessage());        
    }
}
