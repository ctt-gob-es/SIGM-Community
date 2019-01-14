package core.log;

import org.apache.log4j.Logger;

public class SecurityLogger {
    
    private static final Logger LOGGER = Logger.getLogger(SecurityLogger.class);
    
    public static void info(String error) {
        LOGGER.info(error);
    }
    
    public static void error(String error) {
        LOGGER.error(error);
    }

    public static void error(Throwable error) {
        LOGGER.error(error.getMessage());
    }

    public static void error(String format, Exception error) {
        LOGGER.error(error.getMessage());        
    }
    
    public static void error(String format, Throwable error) {
        LOGGER.error(error.getMessage());        
    }

    private SecurityLogger(){        
    }
}
