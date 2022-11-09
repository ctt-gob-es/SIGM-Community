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

package es.seap.minhap.portafirmas.business.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.job.PreSignService;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class PreSignBO {

	private Logger log = Logger.getLogger(PreSignBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private QuartzInvoker quartzInvoker;

	transient Configuration conf;

	/**
	 * M&eacute;todo que obtiene todos los documentos no "prefirmados".
	 * @return Listado de documentos no "prefirmados".
	 */
	public List<AbstractBaseDTO> queryDocumentNotPresigned() {
		List<AbstractBaseDTO> listDocuments = baseDAO.queryListMoreParameters("administration.documentNotPresigned", null);
		return listDocuments;
	}

	/**
	 * Recupera el par�metro de configuraci�n con el valor 'PREFIRMA.EXPRESION', este par�metro 
	 * es una expresi�n que indica cu�ndo se debe ejecutar el job de prefirma
	 * @return el par�metro de configuraci�n con la expresi�n que indica cu�ndo se debe ejecutar el job de prefirma
	 */
	public PfConfigurationsParameterDTO queryPreSignExpression() {
		return (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.preSignParameter", null, null);
	}

	/**
	 * M&eacute;todo que obtiene los documentos prefirmados que contienen un determinado fichero.
	 * @param file Fichero a buscar.
	 * @return Documento o documentos prefirmados que contienen dicho fichero.
	 */
	public List<AbstractBaseDTO> queryDocumentPresign(PfFilesDTO file) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("file", file);
		List<AbstractBaseDTO> document = 
			(List<AbstractBaseDTO>) baseDAO.queryListMoreParameters("administration.queryDocumentPreSign",	parameters);
		return document;
	}
	/**
	 * Recupera el par&aacute;metro de configuraci&oacute;n relacionado con el par&aacute;metro 'PREFIRMA'
	 * que indica si se activa la prefirma
	 * @return el par&aacute;metro de configuraci&oacute;n
	 * @see es.seap.minhap.portafirmas.utils.Constants#JOB_PRESIGN
	 */
	public PfConfigurationsParameterDTO queryParamPreSign() {
		PfConfigurationsParameterDTO paramConfDTO =
			(PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter("administration.parameterConfId",
																			"cparam", Constants.JOB_PRESIGN);

		return paramConfDTO;
	}

	/**
	 * M&eacute;todo que guarda los cambios realizados sobre una prefirma.
	 * @param cronExpression Fecha en la que se realiza la firma.
	 * @param preSignOn Indica si la prefirma est&aacute; activa o no.
	 */
	@Transactional(readOnly = false)
	public void savePresign(String cronExpression, boolean preSignOn) {
		PfConfigurationsParameterDTO configuration = queryPreSignExpression();
		configuration.setTvalue(cronExpression);
		baseDAO.insertOrUpdate(configuration);
		
		PfConfigurationsParameterDTO paramConfDTO = queryParamPreSign();
		if(preSignOn) {
			log.debug("Running PreSign job...");
			paramConfDTO.setTvalue(Constants.C_YES);
			executePreSign();
		} else {
			log.debug("Stopping PreSign job...");
			paramConfDTO.setTvalue(Constants.C_NOT);
			deletePreSign();
		}
		baseDAO.insertOrUpdate(paramConfDTO);
	}

	/**
	 * M&eacute;todo que lanza la ejecuci&oacute;n de una tarea de prefirma	
	 * @param cronExpression Expresi&oacute;n cron que define el comienzo de la tarea.
	 */
	public void runJob(String cronExpression) {		
		executePreSign();
	}

	/**
	 * M&eacute;todo que detiene la ejecuci&oacute;n de una tarea de prefirma.
	 */
	public void stopJob() {		
		deletePreSign();
	}

	/**
	 * M&eacute;todo que actualiza el valor de activaci&oacute;n de una prefirma.
	 * @param preSignOn Par&aacute;metro booleano que define si se activa o no la prefirma.
	 */
	@Transactional(readOnly = false)
	public void modifyPreSignParameter(boolean preSignOn) {
		String active = "N";
		if (preSignOn) {
			active = "S";
		}
		PfConfigurationsParameterDTO paramConfDTO = queryParamPreSign();
		paramConfDTO.setTvalue(active);
		baseDAO.insertOrUpdate(paramConfDTO);
	}

	/**
	 * Borra el job de PREFIRMA
	 * @see es.seap.minhap.portafirmas.utils.Constants#JOB_PRESIGN
	 */
	public void deletePreSign() {
		if (quartzInvoker.existJob(Constants.JOB_PRESIGN, Constants.JOB_PRESIGN)) {
			quartzInvoker.deleteJob(Constants.JOB_PRESIGN, Constants.JOB_PRESIGN);
		}
	}
	
	/**
	 * Comprueba si existe el job de PREFIRMA
	 * @return
	 */
	private boolean existsPreSign () {
		return quartzInvoker.existJob(Constants.JOB_PRESIGN, Constants.JOB_PRESIGN);
	}
	/**
	 * Ejecuta el job de PREFIRMA recuperando de la base de datos la expresi�n que indica cu�ndo se debe ejecutar el job de prefirma 
	 * del campo campo con valor 'PREFIRMA.EXPRESION', si existe el job primero lo borra
	 * @see #queryPreSignExpression
	 */
	public void executePreSign() {
		log.debug ("executePreSign init");
		if (!existsPreSign ()) {
			log.debug ("el job no existe, se crea");
			// Comprueba si el par�metro PREFIRMA es igual a 'S'
			PfConfigurationsParameterDTO paramConfDTO = (PfConfigurationsParameterDTO) baseDAO
					.queryElementOneParameter("administration.parameterConfId",
							"cparam", Constants.JOB_PRESIGN);
			//borra el job de prefirma si existe
			//deletePreSign();		
			//recupera el valor del par�metro de PREFIRMA
			String valorParametro = ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(paramConfDTO);
			
			if (valorParametro.equals(Constants.C_YES)) {
				//recuperamos el par�metro con la expresi�n que indica cu�ndo se debe ejecutar el job de prefirma
				String cronExpression = ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(queryPreSignExpression());
				//ejecutamos el job
				quartzInvoker.scheduleJobCron(null, PreSignService.class,
						Constants.JOB_PRESIGN, Constants.JOB_PRESIGN,
						cronExpression);
			}
		} else {
			log.debug ("el job: " +  Constants.JOB_PRESIGN + " ya existía, no se crea");
		}
		log.debug ("executePreSign end");
	}

}
