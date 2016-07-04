/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfOut;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.session.book.BookSession;
import com.ieci.tecdoc.isicres.session.security.SecuritySession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import es.msssi.sgm.registropresencial.beans.ParamBookBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase q implementa IGenericBo que gestiona los permisos.
 * 
 * @author cmorenog
 * 
 */
public class PermissionsBo implements IGenericBo {
	private static final Logger LOG = Logger.getLogger(PermissionsBo.class);
	private static final int INDICE0 = 0;
	private static final int INDICE1 = 1;
	private static final int INDICE2 = 2;
	private static final int INDICE3 = 3;
	private static final int INDICE4 = 4;
	private static final int INDICE5 = 5;
	private static final int NOTIENEPERMISO = 0;
	
	/**
	 * Constructor.
	 */
	public PermissionsBo() {
	}
	
	/**
	 * Devuelve los permisos del usuario logado.
	 * 
	 * @param useCaseConf
	 *            configuración de la aplicación.
	 * @param beanPermisos
	 *            otros permisos.
	 * @param archiveId
	 *            id del libro.
	 * @param readonly
	 *            solo lectura.
	 * @return bean con los permisos del usuario.
	 * @throws ValidationException
	 *             error validación.
	 * @throws SessionException
	 *             sesión nula.
	 * @throws SecurityException
	 *             error seguridad.
	 * */
	public ParamBookBean getPermission(UseCaseConf useCaseConf, Object beanPermisos,
			Integer archiveId, boolean readonly) throws ValidationException,
			SessionException, SecurityException {
		ParamBookBean param = null;
		if (beanPermisos == null) {
			param = new ParamBookBean();
		}
		else {
			param = (ParamBookBean) beanPermisos;
		}
		param = getDisPermission(useCaseConf, param, archiveId);
		
		if (archiveId != null) {
			param.setCanQuery(SecuritySession.canQuery(useCaseConf.getSessionID(), archiveId));
			param.setCanCreate(SecuritySession.canCreate(useCaseConf.getSessionID(), archiveId));
			param.setCanModify(SecuritySession.canModify(useCaseConf.getSessionID(), archiveId));
			param.setBookAdmin(SecuritySession
					.isBookAdmin(useCaseConf.getSessionID(), archiveId));
			param.setReadOnly(readonly);
			param.setCanOpenReg(SecuritySession.canOpenCloseReg(useCaseConf.getSessionID()));
			param.setCanOperationIR(SecuritySession.canOperationIR(useCaseConf.getSessionID()));
			
			// [0] crear personas
			// [1] modificar personas
			// [2] establecer fecha registro
			// [3] modificar fecha registro
			// [4] Permiso para campos protegidos
			int[] permission =
					SecuritySession.getScrPermission(useCaseConf.getSessionID(), archiveId);
			param.setCanAddPersons(permission[INDICE0] == NOTIENEPERMISO
					? false : true);
			param.setCanModifyPersons(permission[INDICE1] == NOTIENEPERMISO
					? false : true);
			param.setCanRegFchReg(permission[INDICE2] == NOTIENEPERMISO ? false
					: true);
			param.setCanModifyFchReg(permission[INDICE3] == NOTIENEPERMISO
					? false : true);
			param.setCanUpdateProtectedFields(permission[INDICE4] == NOTIENEPERMISO
					? false : true);
			
		}
		return param;
	}
	
	/**
	 * Devuelve los permisos de distribución del usuario logado.
	 * 
	 * @param useCaseConf
	 *            configuración de la aplicación.
	 * @param beanPermisos
	 *            otros permisos.
	 * @param archiveId 
	 * 				id del libro.
	 * @return bean con los permisos de distribución del usuario.
	 * @throws ValidationException
	 *             error validación.
	 * @throws SessionException
	 *             sesión nula.
	 * @throws SecurityException
	 *             error seguridad.
	 * */
	
	public ParamBookBean
			getDisPermission(UseCaseConf useCaseConf, Object beanPermisos,Integer archiveId)
					throws ValidationException, SessionException,
					SecurityException {
		ParamBookBean param = null;
		if (beanPermisos == null) {
			param = new ParamBookBean();
		}
		else {
			param = (ParamBookBean) beanPermisos;
		}
		// Distribución
		// [0] Aceptar registros distribuidos
		// [1] rechazar registros distribuidos
		// [2] Archivar registros distribuidos
		// [3] Cambiar destino
		// [4] Cambiar destino de registros rechazados
		// [5] Distribuir registros
		Integer[] permDistPermission =
				SecuritySession.getScrDistPermission(useCaseConf.getSessionID());
		param.setCanAcceptRegistersDist(permDistPermission[INDICE0] == NOTIENEPERMISO
				? false : true);
		param.setCanRejectRegistersDist(permDistPermission[INDICE1] == NOTIENEPERMISO
				? false : true);
		param.setCanArchiveRegistersDist(permDistPermission[INDICE2] == NOTIENEPERMISO
				? false : true);
		param.setCanChangeDestRegistersDist(permDistPermission[INDICE3] == NOTIENEPERMISO
				? false : true);
		param.setCanChangeDestRejectRegistersDist(permDistPermission[INDICE4] 
				== NOTIENEPERMISO? false : true);
		param.setCanDistRegistersDist(permDistPermission[INDICE5] == NOTIENEPERMISO
				? false : true);
		
		if (archiveId != null){
			/*
			 * REGISTROS DE SALIDA */
			boolean distributionManualBookOut = Configurator
					.getInstance()
					.getPropertyBoolean(
							ConfigurationKeys.KEY_SERVER_DISTRIBUTION_MANUAL_BOOK_OUT);
			try {
				AxSf axsf = BookSession.getTableFormat(useCaseConf.getSessionID(),
						archiveId, useCaseConf.getEntidadId());
				if (axsf instanceof AxSfOut && !distributionManualBookOut) {
					param.setCanDistRegistersDist(false);
				 }
			}
			catch (BookException e) {
				LOG.error(ErrorConstants.GET_PERMISSIONS_ERROR_MESSAGE, e);
			}
		}
		
		return param;
	}
	
}
