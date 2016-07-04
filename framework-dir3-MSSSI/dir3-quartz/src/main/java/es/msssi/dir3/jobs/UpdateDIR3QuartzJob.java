/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.jobs;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import es.msssi.dir3.api.service.impl.UpdateServiceDCOImpl;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.errors.Dir3QuartzErrorConstants;

 
public class UpdateDIR3QuartzJob extends QuartzJobBean implements StatefulJob  {
    private static final Logger logger = Logger.getLogger(UpdateDIR3QuartzJob.class);
    private UpdateServiceDCOImpl updateServiceDCO;
    private static ApplicationContext contexto = null;
    
    private static final String APPLICATION_CONTEXT_KEY  
    = "applicationContext";
    protected void executeInternal(
	JobExecutionContext jobCtx) 
	throws JobExecutionException {
	SchedulerContext schedulerContext = null;  
	 logger.info("INICIO DE LA EJECUCIÓN DEL PROCESO DE ACTUALIZACIÓN DE DIR3");
	try {   
            schedulerContext = jobCtx.getScheduler()  
                 .getContext();  
        } catch(SchedulerException e) {  
            throw new JobExecutionException(  
                "Failure accessing scheduler context", e);  
        }  
	try {
	    contexto = (ApplicationContext)  
	             schedulerContext.get(APPLICATION_CONTEXT_KEY);
	    updateServiceDCO = (UpdateServiceDCOImpl) contexto.getBean("apiUpdateServiceDCOImpl");
	    updateServiceDCO.setContext(contexto);
	    updateServiceDCO.updateDCO();
	    logger.info("FIN DE LA EJECUCIÓN DEL PROCESO DE ACTUALIZACIÓN DE DIR3");
	}
	catch (DIR3Exception dir3Exception) {
	    logger.error(
		Dir3QuartzErrorConstants.EXECUTEJOB, dir3Exception);
	    throw new JobExecutionException(
		Dir3QuartzErrorConstants.EXECUTEJOB, dir3Exception);
	}
	catch (Exception dir3Exception) {
	    logger.error(
		Dir3QuartzErrorConstants.EXECUTEJOB, dir3Exception);
	    throw new JobExecutionException(
		Dir3QuartzErrorConstants.EXECUTEJOB, dir3Exception);
	}
    }
}
