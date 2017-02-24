package es.ieci.tecdoc.fwktd.sir.api.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import es.ieci.tecdoc.fwktd.sir.core.service.ServicioIntercambioRegistral;

/**
 * Job que realiza la recepción de ficheros de intercambio mediante el sistema
 * de ficheros.
 * 
 * @author Iecisa
 * @version $Revision$
 * 
 */
public class RecepcionFicherosJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(RecepcionFicherosJob.class);

    /**
     * Servicio de intercambio registral.
     */
    private ServicioIntercambioRegistral servicioIntercambioRegistral = null;

    /**
     * Constructor.
     */
    public RecepcionFicherosJob() {
	super();
	if (servicioIntercambioRegistral == null) {
	    ApplicationContext context =
		    new ClassPathXmlApplicationContext(
			    new String[] { "classpath:/beans/fwktd-sir-api-applicationContext.xml" });
	    servicioIntercambioRegistral =
		    (ServicioIntercambioRegistral) context
			    .getBean("fwktd_sir_servicioIntercambioRegistralImpl");
	}
    }

    public ServicioIntercambioRegistral getServicioIntercambioRegistral() {
	return servicioIntercambioRegistral;
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

	logger.info("Inicio del job para la recepción de ficheros de intercambio mediante el sistema de ficheros");

	try {

	    // Procesar los ficheros
	    getServicioIntercambioRegistral().procesarFicherosRecibidos();

	    context.setResult("Ok");

	}
	catch (Throwable e) {
	    logger.error("Error al lanzar la recepción de ficheros de intercambio", e);
	    context.setResult("Error al lanzar la recepción de ficheros de intercambio: "
		    + e.toString());
	    throw new JobExecutionException(
		    "Error al lanzar la recepción de ficheros de intercambio", e);
	}
	finally {
	    logger.info("Fin del job para la recepción de ficheros de intercambio mediante el sistema de ficheros");
	}
    }

}
