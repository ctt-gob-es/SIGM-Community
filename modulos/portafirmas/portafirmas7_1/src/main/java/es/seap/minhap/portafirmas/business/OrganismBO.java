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

package es.seap.minhap.portafirmas.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.map.directorio.manager.impl.Unidad;
import es.map.directorio.manager.impl.Unidades;
import es.map.directorio.manager.impl.wsexport.FormatoFichero;
import es.map.directorio.manager.impl.wsexport.RespuestaWS;
import es.map.directorio.manager.impl.wsexport.TipoConsultaUO;
import es.map.directorio.manager.impl.wsexport.UnidadesWs;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfResponseOrganismosDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.OrganismoSelect;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrganismBO {
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private ProvinceBO provinceBO;

	Logger log = Logger.getLogger(OrganismBO.class);

	@SuppressWarnings("unchecked")
	public List<OrganismoSelect> queryOrganismos(String filtro){
		String queryJPA = "select new es.seap.minhap.portafirmas.web.beans.OrganismoSelect ( organismo.codigo || ' - ' || organismo.denominacion, organismo.codigo ) from PfUnidadOrganizacionalDTO organismo where "
				+ " upper(organismo.denominacion) like upper('"+"%"+filtro+"%"+"') or upper(organismo.codigo) like upper('"+"%"+filtro+"%"+"') order by "
				+ " organismo.denominacion ";
		return baseDAO.excecQueryWithoutAbstractDTO(queryJPA, null);
	}
	
	public PfUnidadOrganizacionalDTO getOrganismoById(String id){		
		PfUnidadOrganizacionalDTO organizacion = 
				(PfUnidadOrganizacionalDTO) baseDAO.queryElementOneParameter("request.queryOrganismById", "id", Long.parseLong(id));
		return organizacion;
	}
	
	public PfUnidadOrganizacionalDTO getOrganismoByCode(String code){		
		PfUnidadOrganizacionalDTO organizacion = 
				(PfUnidadOrganizacionalDTO) baseDAO.queryElementOneParameter("request.queryOrganismByCode", "codigoOrganismo", code);
		return organizacion;
	}
	
	/**
	 * Valida el borrado de un organismo
	 * @param pfProvinceDTO
	 * @param errors
	 */
	public void validateDeleteOrganismo(PfUnidadOrganizacionalDTO pfUnidadOrganizacionalDTO, PfUsersDTO usuario, ArrayList<String> errors) {
		
		PfProvinceAdminDTO adminDelete = new PfProvinceAdminDTO();		
		
		List<AbstractBaseDTO> provincesList = new ArrayList<AbstractBaseDTO>();		
		provincesList = getAdminProvincesRelation(pfUnidadOrganizacionalDTO);
		
		for (AbstractBaseDTO province : provincesList){
					
			if(provinceBO.checkSeatReferences((PfProvinceDTO)province, usuario, adminDelete)) {
				errors.add("Existen usuarios en una sede del organismo y no se permite su borrado.");
			}
		}
		
		if (!pfUnidadOrganizacionalDTO.getPfUsuariosList().isEmpty()){
			errors.add("El organismo tiene administradores y no se permite su borrado.");
		}
	}
	
	/**
	 * Método que devuelve el listado de provincias del organismo que administra un usuario
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public List<AbstractBaseDTO> getAdminProvincesRelation(PfUnidadOrganizacionalDTO unidadOrg) {
		List<AbstractBaseDTO> provinces =
			baseDAO.queryListOneParameter("request.queryAdminProvincesRelation", "unidadOrg", unidadOrg);
		return provinces;
	}
	
	@Transactional(readOnly = false)
	public void deleteOrganismo(PfUnidadOrganizacionalDTO pfUnidadOrganizacionalDTO) {		
		baseDAO.delete(pfUnidadOrganizacionalDTO);
	}
	
	/**
	 * Método que devuelve todos los organismos disponibles en BBDD
	 * @return Listado de provincias disponibles
	 */
	public List<AbstractBaseDTO> getAllOrganisms() {
		List<AbstractBaseDTO> provinces = 
			baseDAO.queryListOneParameterList("request.queryAllOrganisms", null, null);
		return provinces;
	}
	
	/**
	 * Obtiene el objeto a persistir en BB.DD. a partir de los datos de la vista
	 * @param seat
	 * @return
	 */
	public PfUnidadOrganizacionalDTO envelopeToDTO(OrganismoSelect organism) {
		PfUnidadOrganizacionalDTO pfUnidadOrganizacional = null;
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		BigInteger codPais = BigInteger.valueOf(724);
		
		if(organism.getId() == null || organism.getId().isEmpty()) {
			pfUnidadOrganizacional = new PfUnidadOrganizacionalDTO();
		} else {
			pfUnidadOrganizacional = this.getOrganismoById(organism.getId());
		}
		
		//PfResponseOrganismosDTO responseOrganismos = new PfResponseOrganismosDTO();
		
		//responseOrganismos.setFechaConsulta(date);
		
		pfUnidadOrganizacional.setDenominacion(organism.getDenominacion());
		pfUnidadOrganizacional.setDenomUnidadSuperior(organism.getDenominacion());
		pfUnidadOrganizacional.setCodigo(organism.getCodigo());
		pfUnidadOrganizacional.setNivelJerarquico(BigInteger.ONE);
		pfUnidadOrganizacional.setEsEdp("V");
		pfUnidadOrganizacional.setCodAmbTerritorial(BigInteger.ZERO);
		pfUnidadOrganizacional.setCodAmbentGeografica(BigInteger.ZERO);
		pfUnidadOrganizacional.setCodAmbPais(BigInteger.ZERO);
		pfUnidadOrganizacional.setNivelAdministracion(BigInteger.ONE);
		pfUnidadOrganizacional.setCodExterno(codPais);		
		pfUnidadOrganizacional.setFechaAltaOficial(dateFormat.format(date));
		//pfUnidadOrganizacional.setIdPfResponseOrganismos(responseOrganismos);
		
		//baseDAO.insertOrUpdate(responseOrganismos);
		
		return pfUnidadOrganizacional;
	}
	
	/**
	 * Guarda la sede que recibe como parametro
	 * @param pfProvinceDTO
	 */
	@Transactional(readOnly = false)
	public void saveOrganism(PfUnidadOrganizacionalDTO pfUnidadOrganizacional) {
		baseDAO.insertOrUpdate(pfUnidadOrganizacional);
	}
	
	@Transactional(readOnly = false)
	public void saveUser(PfUsersDTO pfUsersDTO) {
		baseDAO.insertOrUpdate(pfUsersDTO);
	}
	
	/**
	 * Valida el borrado de una sede
	 * @param pfProvinceDTO
	 * @param errors
	 */
	public void validateDeleteOrganismSeat(PfProvinceDTO pfProvinceDTO, ArrayList<String> errors) {
		if(checkOrganismSeatReferences(pfProvinceDTO)) {
			errors.add("La sede está referenciada y no se permite su borrado.");
		}
	}
	
	/**
	 * Método que comprueba si una sede tiene referencias a usuarios
	 * @param seat Sede
	 * @return True si tiene referencias, False en caso contrario
	 */
	public boolean checkOrganismSeatReferences(PfProvinceDTO seat) {
		List<AbstractBaseDTO> userReferences =
			baseDAO.queryListOneParameter("request.usersFromProvince", "seat", seat);
		if (userReferences.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteSedeParaAdministradorDeOrganismo(PfProvinceAdminDTO provinceAdmin, PfProvinceDTO pfProvinceDTO) {
		if(provinceAdmin!=null && provinceAdmin.getPrimaryKey()!=null){
			baseDAO.delete(provinceAdmin);
		}
		baseDAO.delete(pfProvinceDTO);
	}
	
	/**
	 * Método que devuelve la provincia del organismo que administra un usuario
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public PfProvinceAdminDTO getProvinceAdminByUser(PfUsersDTO userPfProvinceDTO, PfProvinceDTO pfProvinceDTO) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", userPfProvinceDTO);
		params.put("province", pfProvinceDTO);
		
		PfProvinceAdminDTO provinces = (PfProvinceAdminDTO) baseDAO.queryElementMoreParameters(
				"request.queryAdminUserProvinces", params);
		return provinces;
	}
}
