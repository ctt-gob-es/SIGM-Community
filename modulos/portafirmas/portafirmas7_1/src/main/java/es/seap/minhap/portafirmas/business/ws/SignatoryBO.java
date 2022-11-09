/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.business.ws;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilOperClientManager;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.ApplicationLogin;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InformacionFirma;

/**
 * @author domingo
 * 
 * El objetivo principal de esta clase es obtener obtener información del paquete
 * eeutil-oper-firma y devolverla adaptada a los tipos de eeutil-util-firma
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SignatoryBO {

	@Autowired
	private EeUtilOperClientManager eeUtilOperClientManager;

	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;

	Logger log = Logger.getLogger(SignatoryBO.class);

	/**
	 * Obtiene la lista de firmantes con el nif reseteado
	 * @param operAppInfo
	 * @param sign
	 * @param document
	 * @param object
	 * @return
	 * @throws EeutilException
	 * @throws es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException
	 */
	public es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo obtenerFirmantesSinNif(
			byte[] sign, Object object) 
					throws EeutilException, InSideException {
		return obtenerFirmantes(sign, object, false);
	}

	/**
	 * Obtiene la lista de firmantes pero conservando su nif
	 * @param operAppInfo
	 * @param sign
	 * @param document
	 * @param object
	 * @return
	 * @throws EeutilException
	 * @throws es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException
	 */
	public es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo obtenerFirmantes(
			byte[] sign, Object object)
					throws EeutilException, InSideException {
		return obtenerFirmantes(sign, object, true);
	}

	/**
	 * @param operAppInfo
	 * @param sign
	 * @param document
	 * @param object
	 * @param conservarNif Si es true, hay que mantener el nif de los firmantes si no, se sustituye por cadena vacía.
	 * @return
	 * @throws EeutilException
	 * @throws es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException
	 */
	private es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo obtenerFirmantes(
			byte[] sign, Object object, boolean conservarNif) 
					throws EeutilException, es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException {
		// Carga de la configuración
		Map<String, String> params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		// Se obtiene el stub que contiene la operación para obtener firmantes
		EeUtilService eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		// Se obtiene los datos para el logado
		ApplicationLogin operAppInfo = createOperApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));

		// Se obtiene la lista de firmantes
		es.seap.minhap.portafirmas.ws.eeutil.operfirma.ListaFirmaInfo listaFirmantesOper =
				eeutilOperClient.obtenerFirmantes(operAppInfo, sign, new byte[0], null);

		return listaFirmantesConversor(listaFirmantesOper, conservarNif);
	}

	/**
	 * Crea el objeto de autenticación contra eeutil
	 * @param idaplicacion identificador de la aplicación Portafirmas en eeeutil
	 * @param password password de la aplicación Portafirmas en eeutil.
	 * @return 
	 * @throws EeutilException
	 */
	private ApplicationLogin createOperApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EEUtilOperFirma es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EEUtilOperFirma es nulo");
		}
		ApplicationLogin appInfo = new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
		return appInfo;
	}

	/**
	 * Convierte la lista de firmantes de tipo operfirma.ListaFirmaInfo a utilfirma.ListaFirmaInfo
	 * @param listaFirmantesOper
	 * @param conservarNif
	 * @return
	 */
	private es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo listaFirmantesConversor(
			es.seap.minhap.portafirmas.ws.eeutil.operfirma.ListaFirmaInfo listaFirmantesOper, boolean conservarNif) {
		// Lista de origen, del modulo eutil-oper
		List<es.seap.minhap.portafirmas.ws.eeutil.operfirma.FirmaInfo> listaOper = 
				listaFirmantesOper.getInformacionFirmas().getInformacionFirmas();
		// Lista de retorno, del modulo eutil-util
		es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo listaFirmantesUtil =
				new es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo();
		es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo.InformacionFirmas informacionFirmas =
				new es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo.InformacionFirmas();
		listaFirmantesUtil.setInformacionFirmas(informacionFirmas);
		List<es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo> listaUtil =
				listaFirmantesUtil.getInformacionFirmas().getInformacionFirmas();
		// Se recorre la lista origen para crear objetos e insertarlos en la lista de retorno
		for (Iterator<es.seap.minhap.portafirmas.ws.eeutil.operfirma.FirmaInfo> it = listaOper.iterator(); it.hasNext();) {
			es.seap.minhap.portafirmas.ws.eeutil.operfirma.FirmaInfo firmaInfoOper = 
					(es.seap.minhap.portafirmas.ws.eeutil.operfirma.FirmaInfo) it.next();
			es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo firmaInfoUtil = 
					new es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo();
			if(conservarNif) {
				firmaInfoUtil.setNifcif(firmaInfoOper.getNifcif());
			} else {
				firmaInfoUtil.setNifcif("");
			}
			firmaInfoUtil.setNombre(firmaInfoOper.getNombre());
			firmaInfoUtil.setApellido1(firmaInfoOper.getApellido1());
			firmaInfoUtil.setApellido2(firmaInfoOper.getApellido2());
			firmaInfoUtil.setFecha(firmaInfoOper.getFecha());
			firmaInfoUtil.setExtras(firmaInfoOper.getExtras());
			listaUtil.add(firmaInfoUtil);
		}
		return listaFirmantesUtil;
	}
	
	/**
	 * Obtiene la informacion de una Firma
	 * @param operAppInfo
	 * @param sign
	 * @param document
	 * @param object
	 * @return
	 * @throws EeutilException
	 * @throws es.seap.minhap.portafirmas.ws.eeutil.operfirma.InSideException
	 */
	public InformacionFirma obtenerInformacionFirma(byte[] sign) throws EeutilException, InSideException {
		
		// Carga de la configuración
		Map<String, String> params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		// Se obtiene el stub que contiene la operación para obtener firmantes
		EeUtilService eeutilOperClient = eeUtilOperClientManager.getEEUtilOperFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_URL));
		// Se obtiene los datos para el logado
		ApplicationLogin operAppInfo = createOperApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD));

		es.seap.minhap.portafirmas.ws.eeutil.operfirma.OpcionesObtenerInformacionFirma opciones  = new es.seap.minhap.portafirmas.ws.eeutil.operfirma.OpcionesObtenerInformacionFirma();
		opciones.setObtenerFirmantes(true);
		opciones.setObtenerDatosFirmados(true);
		opciones.setObtenerTipoFirma(true);
		
		// Se obtiene la lista de firmantes
		InformacionFirma infoFirma = eeutilOperClient.obtenerInformacionFirma(operAppInfo, sign, opciones);

		return infoFirma;
	}
}
