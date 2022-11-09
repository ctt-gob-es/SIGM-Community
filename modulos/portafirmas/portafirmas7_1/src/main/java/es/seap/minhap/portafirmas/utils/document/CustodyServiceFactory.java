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

package es.seap.minhap.portafirmas.utils.document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfBlocksDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.document.impl.DBCustodyServiceInputImpl;
import es.seap.minhap.portafirmas.utils.document.impl.DBCustodyServiceOutputImpl;
import es.seap.minhap.portafirmas.utils.document.impl.FilePathCustodyServiceInputImpl;
import es.seap.minhap.portafirmas.utils.document.impl.FilePathCustodyServiceOutputImpl;
import es.seap.minhap.portafirmas.utils.document.impl.NASCustodyServiceInputImpl;
import es.seap.minhap.portafirmas.utils.document.impl.NASCustodyServiceOutputImpl;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class CustodyServiceFactory {

	private Logger log = Logger.getLogger(CustodyServiceFactory.class);
	
	private final String INPUT_PATH = "/portafirmas/repositorio/input";
	private final String OUT_PATH = "/portafirmas/repositorio/output";

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private DBCustodyServiceInputImpl dbCustodyServiceInputImpl;
	
	@Autowired
	private DBCustodyServiceOutputImpl dbCustodyServiceOutputImpl;

	@Autowired
	private NASCustodyServiceInputImpl nasCustodyServiceInputImpl;
	
	@Autowired
	private NASCustodyServiceOutputImpl nasCustodyServiceOutputImpl;
	
	@Autowired
	private FilePathCustodyServiceInputImpl fileCustodyServiceInputImpl;
	
	@Autowired
	private FilePathCustodyServiceOutputImpl filesCustodyServiceOutputImpl;

	public String storageTypePorTipoDocumento(String tipoDocumento) {
		// Check file storage type
		PfConfigurationsParameterDTO storageTypeParam = storageTypeDOCCustodiaQuery(tipoDocumento);
		String storageType = ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(storageTypeParam);
		return storageType;
	}
	
	public CustodyServiceInput createCustodyServiceInput(String storageType)
			throws CustodyServiceException {
		log.info("createCustodyServiceInput init.");
		CustodyServiceInput custodyServiceInput = null;	
			
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			if (storageType.equals(Constants.C_TYPE_FILE_DB)) {
				custodyServiceInput = dbCustodyServiceInputImpl;
				parameterMap.put("baseDAO", baseDAO);
			} else if (storageType.equals(Constants.C_TYPE_FILE_NAS)) {
				custodyServiceInput = nasCustodyServiceInputImpl;
			} else if (storageType.equals(Constants.C_TYPE_FILE_FICHERO)) {
				custodyServiceInput = fileCustodyServiceInputImpl;
				parameterMap.put("RUTA", INPUT_PATH);
			} else {
				parameterMap = loadCustodyMap(storageType);
				String className = (String) parameterMap.get(Constants.C_PARAMETER_STORAGE_INPUTCLASS);
				custodyServiceInput = (CustodyServiceInput) getInstanceImplementation(className);
			}
			if (custodyServiceInput != null) {
				custodyServiceInput.initialize(parameterMap);
			} else {
				log.error("CustodyService implement not initialize");
				throw new CustodyServiceException(
						"CustodyService implement not initialize");
			}
		
		log.info("createCustodyServiceInput end.");
		return custodyServiceInput;
	}
	/**
	 * Recupera una instancia de CustodyServiceOutput que sirve de contenedor para 
	 * hacer el download de documentos, se puede crear un servicio para documentos de
	 * tipo 'BLOB' o para otro tipo de documentos especificados en el par&aacute;metro de entrada
	 * @param storageType el tipo de archivo
	 * @return la instancia de CustodyServiceOutput para hacer el download de documentos
	 * @throws CustodyServiceException si nos e puede crear una instancia del servicio
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_TYPE_FILE_DB
	 * @see #loadCustodyMap(String)
	 */
	public CustodyServiceOutput createCustodyServiceOutput(String storageType)
			throws CustodyServiceException {
		log.info("createCustodyServiceOutput init.");
		CustodyServiceOutput custodyServiceOutput = null;
		//Si hemos pasado el tipo de archivo
		if (storageType != null) {
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			//Si el tipo es almacenamiento de base de datos Binary Large OBjects
			//se crea el servicio de tipo DB
			if (storageType.equals(Constants.C_TYPE_FILE_DB)) {
				custodyServiceOutput = dbCustodyServiceOutputImpl;
				parameterMap.put("baseDAO", baseDAO);
			} else if  (storageType.equals(Constants.C_TYPE_FILE_NAS)) {
				custodyServiceOutput = nasCustodyServiceOutputImpl;
				//en otro caso
			} else if  (storageType.equals(Constants.C_TYPE_FILE_FICHERO)) {
				custodyServiceOutput = filesCustodyServiceOutputImpl;
				parameterMap.put("RUTA", OUT_PATH);
			} else {
				parameterMap = loadCustodyMap(storageType);
				//Obtenemos la clase de implementaci&oacute;n
				String className = (String) parameterMap
						.get(Constants.C_PARAMETER_STORAGE_OUTPUTCLASS);
				//Obtenemos una instancia de la clase
				custodyServiceOutput = (CustodyServiceOutput) getInstanceImplementation(className);
			}
			if (custodyServiceOutput != null) {
				custodyServiceOutput.initialize(parameterMap);
			} else {
				log.error("CustodyService implement not initialize");
				throw new CustodyServiceException(
						"CustodyService implement not initialize");
			}
		}
		log.info("createCustodyServiceOutput end.");
		return custodyServiceOutput;
	}
	/**
	 * Recupera 
	 * @param hashDoc el codigo hash del documento
	 * @return
	 */
	public PfBlocksDTO blockFileQuery(String hashDoc) {
		PfBlocksDTO result = null;
		List<AbstractBaseDTO> signList = baseDAO.queryListOneParameter(
				"request.signsOrderedDateReportBlock", "hashDoc", hashDoc);
		if (signList != null && !signList.isEmpty()) {
			result = ((PfSignsDTO) signList.get(0)).getPfBlockSigns()
					.iterator().next().getPfBlock();
		}
		return result;
	}
	/**
	 * Recupera la primera firma de la lista de firmas del documento que pasamos como par&aacute;metro
	 * @param hashDoc el codigo hash del documento
	 * @return la primera firma del documento o nulo si no lo encuentra
	 */
	public PfSignsDTO signFileQuery(String hashDoc) {
		PfSignsDTO result = null;
		List<AbstractBaseDTO> signList = baseDAO.queryListOneParameter(
				"request.signsOrderedDateReport", "hashDoc", hashDoc);
		if (signList != null && !signList.isEmpty()) {
			result = (PfSignsDTO) signList.get(0);
		}
		return result;
	}

	public PfSignsDTO signFileQuery(String hashDoc, String transactionId) {
		PfSignsDTO result = null;
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("hashDoc", hashDoc);
		parameterMap.put("transactionId", transactionId);
		List<AbstractBaseDTO> signList = baseDAO.queryListMoreParameters(
				"request.signTransaction", parameterMap);
		if (signList != null && !signList.isEmpty()) {
			result = (PfSignsDTO) signList.get(0);
		}
		return result;
	}
	
	public String custodiaDir3PorDefecto() {
		PfConfigurationsParameterDTO dir3 = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.custodia.dir3", null, null);
		return dir3.getTvalue();
	}
	
	private PfConfigurationsParameterDTO storageTypeQuery() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.storageTypeParameter", null, null);
	}
	
	private PfConfigurationsParameterDTO storageTypeDOCCustodiaQuery(String tipoDocumento) {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.storageTypeDOCCustodia", "tipo", tipoDocumento);
	}
	
	/**
	 * Recupera un mapa con los par&aacute;metros de configuraci&oacute;n relacionados con
	 * el tipo que pas&aacute;mos como par&aacute;metro, con clave el codigo de par&aacute;metro y el valor
	 * el valor del par&aacute;metro de configuraci&oacute;n relacionado. Estos par&aacute;metros de configuraci&oacute;n
	 * seran aquellos donde el c&oacute;digo de par&aacute;metro sea del tipo 'CUSTODIA.'||:tipo||'.%' , donde
	 * tipo es el tipo que pasamos como par&aacute;metro
	 * @param name tipo de archivo
	 * @return el mapa con la clase de implementaci&oacute;n de subida de documentos a una ruta de ficheros 'INPUTCLASS'
	 * @throws CustodyServiceException si en los par&aacute;metros de configuraci&oacute;n recuperados
	 * no se encuentra la clase de implementaci&oacute;n de subida de documentos a una ruta de ficheros 'INPUTCLASS'
	 * o no se ha recuperado ningun par&aacute;metro de configuraci&oacute;n relacionado con el tipo pasado como par&aacute;metro
	 */
	private Map<String, Object> loadCustodyMap(String name)
			throws CustodyServiceException {
		//Recuperamos los par&aacute;metros de configuraci&oacute;n donde
		//el c&oacute;digo de par&aacute;metro sea 'CUSTODIA.'||:tipo||'.%'
		//donde tipo es el name que pasamos como par&aacute;metro
		List<AbstractBaseDTO> auxList = baseDAO.queryListOneParameter(
				"administration.custodyParameters", "tipo", name);
		Map<String, Object> parameters = new HashMap<String, Object>();
		String key = null;
		PfConfigurationsParameterDTO param = null;
		//si la lista recuperada no est&aacute; vac&iacute;a
		if (auxList != null && !auxList.isEmpty()) {
			for (AbstractBaseDTO auxParameter : auxList) {
				param = (PfConfigurationsParameterDTO) auxParameter;
				//recuperamos el c&oacute;digo del par&aacute;metro
				key = param.getPfParameter().getCparameter();
				//Obtenemos la ultima parte de la cadena despues del punto para la clave
				key = key
						.substring(key
								.lastIndexOf(Constants.C_PARAMETER_STORAGE_SEPARATOR) + 1);
				parameters.put(key, ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(param));
			}
			//si par&aacute;metros no contiene la clave 'INPUTCLASS'
			//Se lanza una excepci&oacute;n
			if (!parameters
					.containsKey(Constants.C_PARAMETER_STORAGE_INPUTCLASS)) {
				log.error("Implementation class not found:" + name);
				throw new CustodyServiceException(
						"Implementation class not found:" + name);
			}
		} else {
			log.error("Implementation parameters error in custody:" + name);
			throw new CustodyServiceException(
					"Implementation parameters error in custody:" + name);
		}
		return parameters;
	}
	/**
	 * Obtiene una instancia de la clase que pasamos como par&aacute;metro
	 * @param className el nombre de la clase 
	 * @return la instancia de la clase que pasamos como par&aacute;metro
	 * @throws CustodyServiceException si no se puede crear una instancia de la clase
	 * que pasamos como par&aacute;metro
	 */
	// TODO Pendiente de implementar en el caso de usar almacenamientos a base de datos
	@SuppressWarnings("rawtypes")
	private Object getInstanceImplementation(String className)
			throws CustodyServiceException {
		Class c = null;
		Object object = null;
		try {
			c = Class.forName(className);
			object = c.newInstance();
		} catch (Exception e) {
			log.error("Implementation instanciate error in class:" + className);
			throw new CustodyServiceException(
					"Implementation instanciate error in class:" + className);
		}
		return object;
	}

}
