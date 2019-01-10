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
 * Job que comprueba el time-out de la recepción de mensajes de ACK o ERROR en los envíos
 * de ficheros de datos de intercambio.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class ComprobarTimeOutEnviosJob extends QuartzJobBean {

	private static Logger logger = LoggerFactory
			.getLogger(ComprobarTimeOutEnviosJob.class);

	/**
	 * Servicio de intercambio registral.
	 */
	private static ServicioIntercambioRegistral servicioIntercambioRegistral = null;

	/**
	 * Constructor.
	 */
	public ComprobarTimeOutEnviosJob() {
	    super();
	    if (servicioIntercambioRegistral == null){
	    ApplicationContext context = 
		    	 new ClassPathXmlApplicationContext(new String[] {"classpath:/beans/fwktd-sir-api-applicationContext.xml"});
	    servicioIntercambioRegistral = (ServicioIntercambioRegistral) context.getBean("fwktd_sir_servicioIntercambioRegistralImpl");
	    }
	}

	public ServicioIntercambioRegistral getServicioIntercambioRegistral() {
		return servicioIntercambioRegistral;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		logger.info("Inicio del job para la comprobación del time-out en los envíos");

		try {

			// Procesar los ficheros
			getServicioIntercambioRegistral().comprobarTimeOutEnvios();

			context.setResult("Ok");

		} catch (Throwable e) {
			logger.error("Error al lanzar la comprobación del time-out en los envíos", e);
			context.setResult("Error al lanzar la comprobación del time-out en los envíos: "
					+ e.toString());
			throw new JobExecutionException(
					"Error al lanzar la comprobación del time-out en los envíos", e);
		} finally {
			logger.info("Fin del job para la comprobación del time-out en los envíos");
		}
	}

}
