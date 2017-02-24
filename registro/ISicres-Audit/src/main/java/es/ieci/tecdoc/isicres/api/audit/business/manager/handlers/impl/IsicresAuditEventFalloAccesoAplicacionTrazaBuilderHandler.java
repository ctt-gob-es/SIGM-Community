/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.ieci.tecdoc.isicres.api.audit.business.manager.handlers.impl;

import java.util.ArrayList;
import java.util.List;

import es.ieci.tecdoc.fwktd.audit.core.vo.TrazaAuditoriaVO;
import es.ieci.tecdoc.isicres.api.audit.business.manager.handlers.IsicresAuditoriaEventHandler;
import es.ieci.tecdoc.isicres.api.audit.business.vo.enums.IsicresEventAuditHandlerTypeEnum;
import es.ieci.tecdoc.isicres.api.audit.business.vo.enums.IsicresEventAuditTypeEnum;
import es.ieci.tecdoc.isicres.api.audit.business.vo.enums.IsicresObjectAuditTypeEnum;
import es.ieci.tecdoc.isicres.api.audit.business.vo.events.IsicresAuditEventAccesoAplicacionVO;
import es.ieci.tecdoc.isicres.api.audit.business.vo.events.IsicresAuditEventFalloAccesoAplicacionVO;
import es.ieci.tecdoc.isicres.api.audit.business.vo.events.IsicresAuditEventVO;

/**
 * Clase manejadora para la generación de trazas de auditoría para el evento de fallo en la entrada a la aplicación de registro
 * @author IECISA
 *
 */
public class IsicresAuditEventFalloAccesoAplicacionTrazaBuilderHandler implements
		IsicresAuditoriaEventHandler {

	public IsicresEventAuditHandlerTypeEnum getTypeHandler() {
		return IsicresEventAuditHandlerTypeEnum.TRAZA_BUILDER;
	}

	public Object handle(IsicresAuditEventVO isicresAuditEvent) {
		List<TrazaAuditoriaVO> result = new ArrayList<TrazaAuditoriaVO>();

		IsicresAuditEventFalloAccesoAplicacionVO eventoFalloAccesoAplicacion = (IsicresAuditEventFalloAccesoAplicacionVO) isicresAuditEvent;

		// Crear traza de auditoria de acceso a la aplicacion
		result.add(generateTrazaAccesoApplicacion(eventoFalloAccesoAplicacion));

		return result;
	}

	protected TrazaAuditoriaVO generateTrazaAccesoApplicacion(
		IsicresAuditEventFalloAccesoAplicacionVO eventoFalloAccesoAplicacion) {

		TrazaAuditoriaVO result = new TrazaAuditoriaVO();

		populateBasicUserEventTrazaAuditoria(eventoFalloAccesoAplicacion, result);

		// tipo de evento FALLO ACCESO APLICACION DE REGISTRO
		result.setEventType(new Long(
				IsicresEventAuditTypeEnum.FALLO_ACCESO_APLICACION.getValue()));

		// descripción del evento que se produjo con info adicional
		String eventDescription = IsicresEventAuditTypeEnum.FALLO_ACCESO_APLICACION.getName();
		result.setEventDescription(eventDescription);

		// tipo de objeto de negocio que auditamos registro, campo de regitro,
		// usuario, campo de usuario, etc ..
		result.setObjectType(String.valueOf(IsicresObjectAuditTypeEnum.USUARIO
				.getValue()));

		result.setObjectTypeDescription(IsicresObjectAuditTypeEnum.USUARIO
				.getName());

		return result;
	}

	protected void populateBasicUserEventTrazaAuditoria(
		IsicresAuditEventFalloAccesoAplicacionVO eventoFalloAccesoAplicacion,
			TrazaAuditoriaVO traza) {

		IsicresAuditEventTrazaBuilderHandlerHelper
				.populateBasicUserEventTrazaAuditoria(eventoFalloAccesoAplicacion,
						traza);

	}

}
