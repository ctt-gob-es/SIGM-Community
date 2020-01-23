/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.actions;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;

import es.msssi.sgm.registropresencial.businessobject.DistributionBo;
import es.msssi.sgm.registropresencial.businessobject.IncompletosBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Action que contiene métodos para la gestión de la distribución.
 * 
 * @author cmorenog
 * */
public class AlertAction extends GenericActions {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AlertAction.class.getName());
	/** clase de negocio de distribución. */
	private DistributionBo distributionBo;
	/** alertas de distribución. */
	private List<String> alertDistribution;
	
	private boolean hayAlertDistribution = false;
	
	private IncompletosBo incompletosBo;
	private List<String> alertIncompletos;
	private boolean hayAlertIncompletos = false;

	/**
	 * Constructor.
	 */
	public AlertAction() {
	}

	/**
	 * Método que muestra las alertas de registros incompletos
	 * al contexto.
	 */
	public void showAlertIncompletos() {
		
		if(incompletosBo == null){
			incompletosBo = new IncompletosBo();
		}
		
		try {
			alertIncompletos = incompletosBo.getListMessageInit(useCaseConf);
			
			if (alertIncompletos != null && alertIncompletos.size() > 0) {
				hayAlertIncompletos = true;
				for (int cont = 0; cont < alertIncompletos.size(); cont++) {

					FacesContext.getCurrentInstance().addMessage( "messagesIncompletos", new FacesMessage(FacesMessage.SEVERITY_INFO, "", alertIncompletos.get(cont)));
				}
			}
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (DistributionException distributionException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, distributionException);
			Utils.redirectToErrorPage(null, distributionException, null);
		}
	}

	/**
	 * Método que muestra las alertas de distribución y las añade como mensajes
	 * al contexto.
	 */
	public void showAlertDistribution() {
		// mostramos las alertas de la distribución
		if (distributionBo == null) {
			distributionBo = new DistributionBo();
		}
		
		try {
			alertDistribution = distributionBo.getListMessageInit(useCaseConf);
			
			if (alertDistribution != null && alertDistribution.size() > 0) {
				hayAlertDistribution = true;
				for (int cont = 0; cont < alertDistribution.size(); cont++) {

					FacesContext.getCurrentInstance().addMessage( "messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "", alertDistribution.get(cont)));
				}
			}
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (DistributionException distributionException) {
			LOG.error(ErrorConstants.SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE, distributionException);
			Utils.redirectToErrorPage(null, distributionException, null);
		}
	}

	/**
	 * Obtiene el valor del parámetro alertDistribution.
	 * 
	 * @return alertDistribution valor del campo a obtener.
	 */
	public List<String> getAlertDistribution() {
		return alertDistribution;
	}

	/**
	 * Guarda el valor del parámetro alertDistribution.
	 * 
	 * @param alertDistribution
	 *            valor del campo a guardar.
	 */
	public void setAlertDistribution(List<String> alertDistribution) {
		this.alertDistribution = alertDistribution;
	}

	public boolean isHayAlertDistribution() {
		return hayAlertDistribution;
	}

	public void setHayAlertDistribution(boolean hayAlertDistribution) {
		this.hayAlertDistribution = hayAlertDistribution;
	}

	/**
	 * Obtiene el valor del parámetro alertIncompletos
	 * 
	 * @return alertIncompletos
	 * 				valor del campo a obtener
	 */
	public List<String> getAlertIncompletos() {
		return alertIncompletos;
	}

	/**
	 * Guarda el valor del parámetro alertIncompletos
	 * 
	 * @return alertIncompletos
	 * 				valor del campo a guardar
	 */
	public void setAlertIncompletos(List<String> alertIncompletos) {
		this.alertIncompletos = alertIncompletos;
	}

	public boolean isHayAlertIncompletos() {
		return hayAlertIncompletos;
	}

	public void setHayAlertIncompletos(boolean hayAlertIncompletos) {
		this.hayAlertIncompletos = hayAlertIncompletos;
	}
	
}
