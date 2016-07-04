/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralActualizadorEstadosManager;

public class UpdateStatesSIRJobSigem  extends QuartzJobBean {
	private static Logger logger = LoggerFactory
		.getLogger(UpdateStatesSIRJobSigem.class);
	private static IntercambioRegistralActualizadorEstadosManager manager ;
    /**
     * Constructor.
     */
    public UpdateStatesSIRJobSigem() {
	super();
	if (manager == null){
	    manager =
		    IsicresManagerProvider.getInstance()
			    .getIntercambioRegistralActualizadorEstadosManager();
	}
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	try {
	    logger.info("Inicio del job para actualizar estado SIR");
	    MultiEntityContextHolder.setEntity("000");
	    manager.actualizarEstadoEnviados();

	}
	catch (Exception e) {
	    logger.error("Error al lanzar la actualización de asientos", e);
	    throw new JobExecutionException("Error en el proceso de UpdateStatesSIRJobSigem", e);
	}finally {
		logger.info("Fin del job para actualizar estado SIR");
	}
	
    }

}
