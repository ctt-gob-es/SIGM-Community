package es.msssi.sgm.registropresencial.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.UnlockRegisterDAO;

public class UnlockRegisterJobSigem extends QuartzJobBean {
    private static Logger logger = LoggerFactory.getLogger(UnlockRegisterJobSigem.class);
    private UnlockRegisterDAO unlockRegisterDAO;
    private static ApplicationContext appContext;
    
    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
    }

    /**
     * Constructor.
     */
    public UnlockRegisterJobSigem() {
	super();
	unlockRegisterDAO = (UnlockRegisterDAO) appContext.getBean("unlockRegisterDAO");
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	try {
	    logger.info("Inicio del job para desbloquear registros");
	    unlockRegisterDAO.unlockRegisters();

	}
	catch (Exception e) {
	    logger.error("Error al lanzar la actualización de asientos", e);
	    throw new JobExecutionException("Error en el proceso de UnlockRegisterJobSigem", e);
	}
	finally {
	    logger.info("Fin del job para desbloquear registros");
	}

    }

}
