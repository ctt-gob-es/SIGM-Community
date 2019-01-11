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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;



import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager.IntercambioRegistralActualizadorEstadosManager;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.DataMailDAO;
import es.msssi.sgm.registropresencial.beans.DistributionMail;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.DistributionMailDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.mail.RegistroPresencialMail;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;

public class UpdateStatesSIRJobSigem  extends QuartzJobBean {
	private static Logger logger = LoggerFactory
		.getLogger(UpdateStatesSIRJobSigem.class);
	private static IntercambioRegistralActualizadorEstadosManager manager;
    private static ApplicationContext appContext;
	        
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
		if (appContext == null) {
            appContext =
                    RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
                            .getApplicationContext();
        }
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
	try {
	    logger.info("Inicio del job para actualizar estado SIR");
	    MultiEntityContextHolder.setEntity("000");
	    List<DataMailDAO> listDataMailDAO = manager.actualizarEstadoEnviados();
	    
	    // Se envía mail siempre que se haya activado la notificación
        // Enviar email al origen del Intercambio registral si tiene activadas las notificaciones
	    if(!listDataMailDAO.isEmpty()) {
	    	for(DataMailDAO dataMailDAO: listDataMailDAO) {
	    		sendMail(dataMailDAO);
	    	}
	    }
	}
	catch (Exception e) {
	    logger.error("Error al lanzar la actualización de asientos", e);
	    throw new JobExecutionException("Error en el proceso de UpdateStatesSIRJobSigem", e);
	}finally {
		logger.info("Fin del job para actualizar estado SIR");
	}
	
    }
    
    /**
     * Envia los mails a los receptores del intercambio registral.
     *
     * @param locale          locale.
     * @param sendMail        Array con el listado de registros de intercambio registral y el tipo de registro.
     * @param id              id del destino del intercambio registral.
     */
    private void sendMail(DataMailDAO dataMailDAO) {

        RegistroPresencialMail rpMail = new RegistroPresencialMail();
        List<DistributionMail> distributionsMail = null;
        
        distributionsMail = getIntercambioRegistralMail(dataMailDAO.getCodeTramUnit());

        if (distributionsMail != null && !distributionsMail.isEmpty()) {
        	String mailSubject =
                    ResourceRP.getInstance(Locale.getDefault()).getProperty(KeysRP.EMAIL_SUBJECT_INTERCAMBIOREG_PROPERTY);
            // obtener los destinatarios
            for (DistributionMail distMail : distributionsMail) {
                try {
                    rpMail.sendMailIntercambioRegistral(distMail.getName(), distMail.getEmail(),
                            mailSubject, dataMailDAO.getTextSender(), dataMailDAO.getTypeRegister());
                } catch (Throwable t) {
                	logger.error("Error en el envio de email: " + mailSubject, t);
                }
            }
        }
    }
    
    /**
     * Recuperar la configuracion de usuario.
     *
     * @param id              id del elemento a intercambiar.
     * @return
     * @throws RPGenericException
     */
    private List<DistributionMail> getIntercambioRegistralMail(String codeTramUnit) {
        logger.trace("Entrando en ConfigurationBo.getConfigurationUser()");
        List<DistributionMail> distributionMail = null;
        try {
            
        	DistributionMailDAO distributionMailDAO =
                        (DistributionMailDAO) appContext.getBean("distributionMailDAO");
            
            distributionMail = distributionMailDAO.getMailsIntercambioRegByOrg(codeTramUnit);
            
        } catch (Exception exception) {
            logger.error(ErrorConstants.GET_INTERCAMBIOREGMAIL_ERROR_MESSAGE, exception);
        }
        return distributionMail;
    }
    
    
}
