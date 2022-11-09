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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class JobAdmBO {

	@Autowired
	private BaseDAO baseDAO;

	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Obtiene toda la lista de jobs, es decir los usuarios marcados en bbdd
	 * como tipo 'CARGO'
	 * @return la lista de jobs de la bbdd
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.jobAdmAll", null);
	}

	/**
	 * Obtiene la lista de todos los cargos con sus provincias incluidas
	 * @return Listado de cargos
	 */
	public List<AbstractBaseDTO> queryListWithProvinces() {
		return baseDAO.queryListMoreParameters("administration.jobAdmProvinceAll", null);
	}

	/**
	 * Método que devuelve una lista con todos los cargos (vigentes o no) de la aplicación
	 * @return Listado de cargos
	 */
	public List<AbstractBaseDTO> queryAllJobsWithProvinceList() {
		return baseDAO.queryListMoreParameters("administration.allJobsWithProvince", null);
	}

	/**
	 * M&eacute;todo que guarda en BD los cambios realizados sobre las listas de cargos a a&ntilde;adir y a eliminar.
	 * @param jobList Listado de cargos a a&ntilde;adir.
	 * @param jobDeleteList Listado de cargos a eliminar.
	 */
	@Transactional(readOnly = false)
	public void saveJobList(List<AbstractBaseDTO> jobList,
			List<AbstractBaseDTO> jobDeleteList) {
		for (AbstractBaseDTO job : jobList) {
			if (job.isUpdated()) {
				job.setUpdated(false);
				baseDAO.insertOrUpdate(job);
			}
		}		
		//baseDAO.updateList(jobList);
		baseDAO.deleteList(jobDeleteList);
	}

	
	/**
	 * M&eacute;todo que a&ntilde;ade un objeto cargo vac&iacute;o a la lista de cargos.
	 * @param jobList Listado de cargos.
	 */
	public void addJob(List<AbstractBaseDTO> jobList) {
		PfUsersDTO job = new PfUsersDTO();
		job.setCtype(Constants.C_TYPE_USER_JOB);
		// job.setDname(insertTextString);
		jobList.add(0, job);
	}

	/**
	 * Método que comprueba si un cargo existe en una lista de cargos.
	 * @param jobList Listado de cargos.
	 * @param job Cargo a buscar.
	 * @param indexInList Posici&oacute;n del cargo en la lista.
	 * @return "True" si el cargo existe, "false" en caso contrario.
	 */
	public boolean existsCJob(List<AbstractBaseDTO> jobList, PfUsersDTO job, int indexInList) {
		boolean ret = false;
		PfUsersDTO auxJob;
		if (jobList != null && !jobList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = jobList.iterator();
			while (it.hasNext() && ret == false) {
				auxJob = (PfUsersDTO) it.next();
				// Not the same row and different code
				if (auxJob.getCidentifier().toUpperCase().equals(
						job.getCidentifier().toUpperCase())
						&& !job.getPrimaryKeyString().equals(
								auxJob.getPrimaryKeyString())) {
					ret = true;
				}
				if (auxJob.getCidentifier().toUpperCase().equals(
						job.getCidentifier().toUpperCase())
						&& auxJob.getPrimaryKey() == null
						&& job.getPrimaryKey() == null && !auxJob.equals(job)) {
					if (indexInList <= -1
							&& countCJobInList(auxJob, jobList) > 0) {
						// this case is when we add a new job
						ret = true;
					} else if (countCJobInList(auxJob, jobList) > 1) {
						// this case is when we are modifing a job
						ret = true;

					}
				}
			}
		}
		return ret;
	}

	/**
	 * Método que comprueba si existe un identificador de usuario en la base de datos que coincida con el del cargo
	 * @param job Cargo
	 * @return True si existe, False en caso contrario
	 */
	public boolean existsJobId(PfUsersDTO job) {
		boolean exists = false;
		PfUsersDTO jobWithSameId = (PfUsersDTO) 
			baseDAO.queryElementOneParameter("request.userById", "id", job.getCidentifier().toUpperCase());
		if (jobWithSameId != null) {
			exists = true;
		}
		return exists;
	}

	/**
	 * M&eacute;todo que comprueba si la descripción de un cargo existe en una lista de cargos.
	 * @param jobList Listado de cargos.
	 * @param job Cargo a buscar.
	 * @param indexInList Posici&oacute;n del cargo en la lista.
	 * @return "True" si el cargo existe, "false" en caso contrario.
	 */
	public boolean existsDescriptionJob(List<AbstractBaseDTO> jobList, PfUsersDTO job, String codProvincia, int indexInList) {
		boolean ret = false;
		PfUsersDTO auxJob;
		if (jobList != null && !jobList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = jobList.iterator();
			while (it.hasNext() && ret == false) {
				auxJob = (PfUsersDTO) it.next();

				if (auxJob.getPfProvince() != null &&
					auxJob.getPfProvince().getCcodigoprovincia().equals(codProvincia)) {
					// Not the same row and different code
					/*if (auxJob.getCidentifier().toUpperCase().equals(
							job.getCidentifier().toUpperCase())
							&& !job.getPrimaryKeyString().equals(
									auxJob.getPrimaryKeyString())) {*/
					if (auxJob.getDname().toUpperCase().equals(
							job.getDname().toUpperCase())
							&& !job.getPrimaryKeyString().equals(
									auxJob.getPrimaryKeyString())) {
						ret = true;
					}
					/*if (auxJob.getCidentifier().toUpperCase().equals(
							job.getCidentifier().toUpperCase())
							&& auxJob.getPrimaryKey() == null
							&& job.getPrimaryKey() == null && !auxJob.equals(job)) {*/
					if (auxJob.getDname().toUpperCase().equals(
							job.getDname().toUpperCase())
							&& auxJob.getPrimaryKey() == null
							&& job.getPrimaryKey() == null && !auxJob.equals(job)) {
						if (indexInList <= -1
								&& countDescriptionJobInList(auxJob, jobList) > 0) {
							// this case is when we add a new job
							ret = true;
						} else if (countDescriptionJobInList(auxJob, jobList) > 1) {
							// this case is when we are modifing a job
							ret = true;
	
						}
					}
				}
			}
		}
		return ret;
	}
	
	/**
	 * Método que comprueba si existe al menos un cargo en la lista de cargos sin identificador asignado.
	 * @param jobList Listado de cargos.
	 * @return "True" si encuentra alg&uacute;n cargo sin identificador, "false" en caso contrario.
	 */
	public boolean existsNotFilledIdentifierJob(List<AbstractBaseDTO> jobList) {
		boolean anyEmpty = false;
		for (Iterator<AbstractBaseDTO> iterator = jobList.iterator(); iterator.hasNext()
				&& !anyEmpty;) {
			PfUsersDTO job = (PfUsersDTO) iterator.next();
			if (job.getCidentifier().equals("")) {
				anyEmpty = true;
			}
		}
		return anyEmpty;
	}

	/**
	 * Método que comprueba si existe al menos una tarea en la lista de tareas sin descripci&oacute;n asignada.
	 * @param jobList Listado de cargos.
	 * @return "True" si encuentra alg&uacute;n cargo sin descripci&oacute;n, "false" en caso contrario.
	 */
	public boolean existsNotFilledDescriptionJob(List<AbstractBaseDTO> jobList) {
		boolean anyEmpty = false;
		for (Iterator<AbstractBaseDTO> iterator = jobList.iterator(); iterator
				.hasNext()
				&& !anyEmpty;) {
			PfUsersDTO job = (PfUsersDTO) iterator.next();
			if (job.getDname().equals("")) {
				anyEmpty = true;
			}
		}
		return anyEmpty;
	}

	/**
	 * Método que devuelve todos los cargos que pertenecen a una determinada provincia
	 * @param province Provincia
	 * @return Lista de cargos
	 */
	public List<AbstractBaseDTO> getJobsByProvince(String province) {
		return baseDAO.queryListOneParameter("administration.jobsByProvince", "provincia", province);
	}

	/**
	 * Método que devuelve todos los cargos que pertenecen a las provincias de la lista
	 * @param provinceList
	 * @param value 
	 * @return
	 */
	public List<AbstractBaseDTO> getJobsByProvinces(List<AbstractBaseDTO> provinceList, String value) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("find", "%" + value.toUpperCase() + "%");
		queryParams.put("provinceList", provinceList);
		return baseDAO.queryListMoreParameters("administration.jobsByProvinces", queryParams);
	}

	/**
	 * Método que devuelve todos los cargos (vigentes o no) que pertenecen a una determinada provincia
	 * @param province Provincia
	 * @return Lista de cargos
	 */
	public List<AbstractBaseDTO> getAllJobsByProvince(String province) {
		return baseDAO.queryListOneParameter("administration.allJobsByProvince", "provincia", province);
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que se repite un cargo dentro de una lista.
	 * @param job Cargo a buscar.
	 * @param list Listado de cargos.
	 * @return N&uacute;mero de repeticiones del cargo en la lista.
	 */
	private int countCJobInList(PfUsersDTO job, List<AbstractBaseDTO> list) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfUsersDTO pfUsersDTO = (PfUsersDTO) iterator.next();
			if (pfUsersDTO.getCidentifier() != null
					&& pfUsersDTO.getCidentifier().toUpperCase().equals(
							job.getCidentifier().toUpperCase())) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * M&eacute;todo que calcula el n&uacute;mero de veces que se repite la descripción de un cargo dentro de una lista.
	 * @param job Cargo a buscar.
	 * @param list Listado de cargos.
	 * @return N&uacute;mero de repeticiones de la descripción del cargo en la lista.
	 */
	private int countDescriptionJobInList(PfUsersDTO job, List<AbstractBaseDTO> list) {
		int count = 0;
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfUsersDTO pfUsersDTO = (PfUsersDTO) iterator.next();
			if (pfUsersDTO.getDname() != null
					&& pfUsersDTO.getDname().toUpperCase().equals(
							job.getDname().toUpperCase())) {
				count++;
			}
		}
		return count;
	}
	

	/**
	 * M&eacute;todo que duplica un cargo.
	 * @param job Cargo a duplicar.
	 * @return Copia del cargo.
	 */
	public PfUsersDTO getJobCopy(PfUsersDTO job) {
		PfUsersDTO copy = new PfUsersDTO();
		copy.setCanagram(job.getCanagram());
		copy.setCcreated(job.getCcreated());
		copy.setCidentifier(job.getCidentifier());
		copy.setCmodified(job.getCmodified());
		copy.setCtype(job.getCtype());
		copy.setDname(job.getDname());
		copy.setDsurname1(job.getDsurname1());
		copy.setDsurname2(job.getDsurname2());
		copy.setFcreated(job.getFcreated());
		copy.setFmodified(job.getFmodified());
		copy.setLvalid(job.getLvalid());
		copy.setLvisible(job.getLvisible());
		copy.setPfProvince(job.getPfProvince());
		copy.setLshownotifwarning(job.getLshownotifwarning());
		copy.setPrimaryKey(job.getPrimaryKey());
		return copy;
	}
	
	public ArrayList<String> requestAssociatedWS(PfUsersDTO job) {
		ArrayList<String> warnings = requestAssociated(job);
		warnings.addAll(jobAssociated(job));
		return warnings;
	}

	/**
	 * M&eacute;todo que comprueba si una tarea est&aacute; asociada a alg&uacute;n usuario, firmante,
	 * remitente, etiqueta o comentario. Genera un mensaje de aviso seg&uacute;n el caso.
	 * @param job Tarea a comprobar.
	 * @return CAdena de texto con el mensaje de aviso en caso de encontrar alguna asociaci&oacute;n.
	 */
	public ArrayList<String> requestAssociated(PfUsersDTO job) {
		
		ArrayList<String> warnings = new ArrayList<String>();
		PfUsersDTO userAux = null;
		List<AbstractBaseDTO> listAux = null;
		// Check only if user exists in DB
		if (job != null && job.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("paramUser", job);

			// Comprobación de que el usuario no sea firmante de ninguna peticion
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToSigners", queryParams);
			if (userAux != null && userAux.getPfSigners() != null
					&& !userAux.getPfSigners().isEmpty()) {
				warnings.add(messages.getProperty("errorJobIsSigner"));
			}

			// Comprobación de que el usuario no sea remitente de ninguna petición
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToRemitters", queryParams);
			if (userAux != null && userAux.getPfUsersRemitters() != null
					&& !userAux.getPfUsersRemitters().isEmpty()) {
				warnings.add(messages.getProperty("errorJobIsRemitter"));
			}

			// Comprobación de que el usuario no haya escrito comentarios en peticiones
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToComments", queryParams);
			if (userAux != null && userAux.getPfComments() != null
					&& !userAux.getPfComments().isEmpty()) {
				warnings.add(messages.getProperty("errorJobHasComments"));
			}				

			// Comprobación de que el usuario no tenga validadores ni sea validador
			userAux =  (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToValidators", queryParams);
			if (userAux != null && userAux.getValidadorDe()!= null
					&& !userAux.getValidadorDe().isEmpty()) {
				warnings.add(messages.getProperty("errorJobHasValidators"));
			}

			if (userAux != null && userAux.getValidadores() != null
					&& !userAux.getValidadores().isEmpty()) {
				warnings.add(messages.getProperty("errorJobHasValidators"));
			}

			// Comprobación de que el usuario no tenga etiquetas peticion
			listAux = baseDAO.queryListMoreParameters("administration.userAssociatedToRequestsTags", queryParams);

			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorJobHasRequestTags"));
			}				

			// Comprobación de que el usuario no tenga autorizaciones
			listAux =  baseDAO.queryListMoreParameters("administration.userAssociatedToAuthorizations", queryParams);
			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorJobHasAuth"));
			}

		}
		return warnings;
	}
	
	/**
	 * Comprobación de que el cargo no esté asociado
	 * @param job
	 * @return
	 */
	public ArrayList<String> jobAssociated(PfUsersDTO job) {
		ArrayList<String> warnings = new ArrayList<String>();
		if (job != null && job.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("job", job);				
			Long l  = baseDAO.queryCount("administration.jobAdmAssociated", queryParams);
			if (l > 0) {
				warnings.add(messages.getProperty("errorJobAssociated"));
			}
		}
		return warnings;
	}

	
	@Transactional(readOnly = false)
	public void updateVisibility (List<UserEnvelope> userList, boolean visible) {
		for (UserEnvelope userEnv : userList) {
			updateVisibility(userEnv, visible);
		}
	}

	@Transactional(readOnly = false)
	public void updateVisibility (UserEnvelope userEnv, boolean visible) {
		PfUsersDTO userDTO = (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersAndJobWithProvincePk", "pk", userEnv.getPk());
		userDTO.setLvisible(visible);
		baseDAO.insertOrUpdate(userDTO);
	}

	/**
	 * Devuelve un cargo que coincide con el nombre recibido
	 * @param pfJobsDTO
	 * @return
	 */
	private PfUsersDTO getJobByDescription(PfUsersDTO pfJobsDTO) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dname", pfJobsDTO.getDname());
		parameters.put("pkProvince", pfJobsDTO.getPfProvince().getPrimaryKey());
		List<PfUsersDTO> resultList = baseDAO.queryListMoreParameters("administration.jobByDescription", parameters);
		if(!resultList.isEmpty()){
			return resultList.get(0);
		}else{
			return null;
		}
	}

	/**
	 * Devuelve un cargo que coincide con el identificador recibido
	 * @param pfJobsDTO
	 * @return
	 */
	public PfUsersDTO getJobByCode(String cidentifier) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("administration.jobByCode", "cidentifier", cidentifier);
	} 

/*	public void validateJob(String jobCode, ArrayList<String> errors){
		PfUsersDTO pfJobDTOBD = getJobByCode(jobCode);
		if(pfJobDTOBD != null){
			errors.add(messages.getProperty("existsCJob"));
		}
	}*/
	
	/**
	 * Validación de las reglas de negocio
	 * @param pfJobDTO
	 * @param errors
	 */
	public void validateJob(PfUsersDTO pfJobDTO, ArrayList<String> errors) {
		PfUsersDTO pfJobDTO_BD = getJobByCode(pfJobDTO.getCidentifier());
		// Si el código ya existe en base de datos..
		if(pfJobDTO_BD != null) {
			// .. se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando.
			if(pfJobDTO.getPrimaryKey() == null 
					|| !pfJobDTO.getPrimaryKey().equals(pfJobDTO_BD.getPrimaryKey())) {
				errors.add(messages.getProperty("existsCJob"));
			}
		}
		pfJobDTO_BD = getJobByDescription(pfJobDTO);
		// Si la descripción del cargo ya existe en base de datos..
		if(pfJobDTO_BD != null) {
			// .. se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando.
			if(pfJobDTO.getPrimaryKey() == null 
					|| !pfJobDTO.getPrimaryKey().equals(pfJobDTO_BD.getPrimaryKey())) {
				errors.add(messages.getProperty("errorJobExistsForProvince"));
			}
		}
	}

	/**
	 * Persistencia del cargo
	 * @param pfJobDTO
	 */
	@Transactional(readOnly = false)
	public void saveJob(PfUsersDTO pfJobsDTO) {
		baseDAO.insertOrUpdate(pfJobsDTO);
	}

	@Transactional(readOnly = false)
	public void deleteJob(PfUsersDTO pfJobsDTO) {
		baseDAO.delete(pfJobsDTO);
	}

	@Transactional(readOnly = false)
	public void revokeUser(PfUsersDTO pfJobDTO) {
		pfJobDTO.setLvalid(false);
		baseDAO.insertOrUpdate(pfJobDTO);
	}

	/**
	 * M&eacute;todo que comprueba si una tarea est&aacute; asociada a alg&uacute;n usuario, firmante,
	 * remitente, etiqueta o comentario. Genera un mensaje de aviso seg&uacute;n el caso.
	 * @param job Tarea a comprobar.
	 * @return CAdena de texto con el mensaje de aviso en caso de encontrar alguna asociaci&oacute;n.
	 */
	/*public String requestAssociated(PfUsersDTO job) {
		String message = "";
		PfUsersDTO userAux = null;
		// Check only if user exists in DB
		if (job != null && job.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("paramJob", job);
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters(
					"administration.jobAssociatedToRequest", queryParams);
		}
		if (userAux != null) {
			// check that job is not associated to any user
			if (userAux.getPfUsersJobs() != null
					&& !userAux.getPfUsersJobs().isEmpty()) {
				message = errorJobAssociated;
			}
			// check that job isn't signer
			if (userAux.getPfSigners() != null
					&& !userAux.getPfSigners().isEmpty()) {
				message = errorJobIsSigner;
			}
			// check that job isn't remitter
			else if (userAux.getPfUsersRemitters() != null
					&& !userAux.getPfUsersRemitters().isEmpty()) {
				message = errorJobIsRemitter;
			}
			// check that job doesn't have request tags
			else if (userAux.getPfTagsUsers() != null
					&& !userAux.getPfTagsUsers().isEmpty()) {
				message = errorJobHasRequestTags;
			}
			// check that job hasn't comments
			else if (userAux.getPfComments() != null
					&& !userAux.getPfComments().isEmpty()) {
				message = errorJobHasComments;
			}
		}
		return message;
	}*/

}
