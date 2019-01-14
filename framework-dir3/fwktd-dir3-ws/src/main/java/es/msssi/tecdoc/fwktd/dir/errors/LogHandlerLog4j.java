package es.msssi.tecdoc.fwktd.dir.errors;

import org.apache.log4j.Logger;

public class LogHandlerLog4j  {

    private static final String SEPARATOR = ": ";

    private static final String EXCEPTION_TRACE = "Exception Trace:";

    private static final Logger logger = Logger.getLogger(LogHandlerLog4j.class);

    public void doinfo(String s) {
	logger.info(s);
    }

    public void dowarn(String s) {
	logger.warn(s);
    }

    public void doerror(String s) {
	logger.error(s);
    }

    public void doexception(Throwable t) {
	logger.error(t.getClass().getName() + SEPARATOR + t.getMessage());
	logger.error(EXCEPTION_TRACE, t);
    }

    public void doinfo(String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.info(s);
    }

    public void dowarn(String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.warn(s);
    }

    public void doerror(String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.error(s);
    }

    public void doexception(Throwable t, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.error(t.getClass().getName() + SEPARATOR + t.getMessage());
	logger.error(EXCEPTION_TRACE, t);
    }
}
