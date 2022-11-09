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
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import es.map.directorio.manager.impl.wsexport.UnidadesWsVersion;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfOrganismoDIR3DTO;
import es.seap.minhap.portafirmas.domain.PfResponseOrganismosDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalRequest;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OrganismoDIR3BO {
	
	@Autowired
	private BaseDAO baseDAO;
	
	Logger log = Logger.getLogger(OrganismoDIR3BO.class);

	public void actualizarOrganismosDir3(String fechaInicio, String codigo) throws IOException, JAXBException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		PfConfigurationsParameterDTO parametroUrlDir3 = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.C_PARAMETER_URL_DIR3);
		PfConfigurationsParameterDTO parametroUsuarioDir3 = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.C_PARAMETER_USER_DIR3);
		PfConfigurationsParameterDTO parametroClaveDir3 = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.C_PARAMETER_PW_DIR3);
		PfConfigurationsParameterDTO parametroVersionDir3 = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				Constants.C_PARAMETER_VERSION_DIR3);
		
		es.map.directorio.manager.impl.SD01UNDescargaUnidadesService service = new es.map.directorio.manager.impl.SD01UNDescargaUnidadesService(
				new URL(parametroUrlDir3.getTvalue()));
		es.map.directorio.manager.impl.SD01UNDescargaUnidades port = service.getSD01UNDescargaUnidades();
		
		List<?> returnQuery = baseDAO.excecNamedQueryWithoutAbstractDTO("PfResponseOrganismosDTO.findMaxDate", new HashMap<String, Object>());
		
		UnidadesWsVersion unidadesRequestVersion = new UnidadesWsVersion();
		unidadesRequestVersion.setUsuario(parametroUsuarioDir3.getTvalue());
		unidadesRequestVersion.setClave(parametroClaveDir3.getTvalue());
		unidadesRequestVersion.setFormatoFichero(FormatoFichero.XML);
		unidadesRequestVersion.setTipoConsulta(TipoConsultaUO.UNIDADES);
		if (codigo != null && !"".equals(codigo)) {
			unidadesRequestVersion.setCodigo(codigo);
		}
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (fechaInicio!=null && !"".equals(fechaInicio)) {
			//String enviarFecha = df.format(fechaInicio);
			unidadesRequestVersion.setFechaInicio(fechaInicio);
		} else if(returnQuery!=null && !returnQuery.isEmpty() && returnQuery.get(0) != null){
			Date maxFecha = (Date) returnQuery.get(0);
			
			String enviarFecha = df.format(maxFecha);
			unidadesRequestVersion.setFechaInicio(enviarFecha);
		} else {
			Date fechaInicial = new Date();
			fechaInicial.setYear(-20);
			String enviarFecha = df.format(fechaInicial);
			unidadesRequestVersion.setFechaInicio(enviarFecha);
			
		}
		Date fechaActual = new Date();
		String enviarFechaFin = df.format(fechaActual);
		unidadesRequestVersion.setFechaFin(enviarFechaFin);

		RespuestaWS respuestaOrganismos = null;
		if (parametroVersionDir3.getTvalue().equals("1")) {
			respuestaOrganismos = port.exportar(transformarUnidadesWs(unidadesRequestVersion));	
		} else if (parametroVersionDir3.getTvalue().equals("2")) {
			respuestaOrganismos = port.exportarV2(transformarUnidadesWs(unidadesRequestVersion));
		} else if (parametroVersionDir3.getTvalue().equals("3")) {
			respuestaOrganismos = port.exportarV3(unidadesRequestVersion);
		} else {
			respuestaOrganismos = port.exportar(transformarUnidadesWs(unidadesRequestVersion));
		}
		

		byte[] archivoOrganismos = org.apache.commons.codec.binary.Base64
				.decodeBase64(respuestaOrganismos.getFichero());
		
		PfResponseOrganismosDTO archivoRespuesta = new PfResponseOrganismosDTO();
		archivoRespuesta.setFechaConsulta(Calendar.getInstance().getTime());
		archivoRespuesta.setDocumentoObtenido(archivoOrganismos);
		archivoRespuesta.setCodigoRespuesta(respuestaOrganismos.getCodigo());
		archivoRespuesta.setDescripcionRespuesta(respuestaOrganismos.getDescripcion());
		baseDAO.insertOrUpdate(archivoRespuesta);
		
		if (respuestaOrganismos.getCodigo().equals("01")) {
		
			Long timeInstance = Calendar.getInstance().getTimeInMillis();
			
			String nombreTemporal = Constants.PATH_TEMP + "_dir3_" + timeInstance + ".zip";
			
			FileUtils.writeByteArrayToFile(new File(nombreTemporal), archivoOrganismos);
			
			ZipFile zip = new ZipFile(new File(nombreTemporal));
	
			ZipEntry entry;
			
			Enumeration<? extends ZipEntry> e = zip.entries();
			
			while (e.hasMoreElements()) {
				entry = e.nextElement();
				if("unidades.xml".equalsIgnoreCase(entry.getName())){
					
					InputStream is = zip.getInputStream(entry);
					byte[] archivoXml = IOUtils.toByteArray(is);
					
					nombreTemporal = Constants.PATH_TEMP + "_dir3_" + timeInstance + ".xml";
					
					FileUtils.writeByteArrayToFile(new File(nombreTemporal), archivoXml);
					
					ByteArrayInputStream inputXml = new ByteArrayInputStream(archivoXml);
			        JAXBContext jc = JAXBContext.newInstance (Unidades.class);
			        Unmarshaller unmarshaller = jc.createUnmarshaller();
			        Unidades unidadesDesdeServicio = (Unidades) unmarshaller.unmarshal(inputXml);
			        if(unidadesDesdeServicio!=null && unidadesDesdeServicio.getUnidad()!=null){
			        	for(Unidad org: unidadesDesdeServicio.getUnidad()){
			        		try{
			        			crearOrganismo(org, archivoRespuesta);
			        		}catch(Exception ex){
			        			log.error(ex);
			        		}
			        	}
			        }
				}
			}
			if(zip != null){
				zip.close();
			}
		}
	}
	
	private UnidadesWs transformarUnidadesWs (UnidadesWsVersion unidadesRequestVersion) {
		UnidadesWs unidadesRequest = new UnidadesWs();
		unidadesRequest.setUsuario(unidadesRequestVersion.getUsuario());
		unidadesRequest.setClave(unidadesRequestVersion.getClave());
		unidadesRequest.setFormatoFichero(unidadesRequestVersion.getFormatoFichero());
		unidadesRequest.setTipoConsulta(unidadesRequestVersion.getTipoConsulta());
		unidadesRequest.setCodigo(unidadesRequestVersion.getCodigo());
		unidadesRequest.setFechaInicio(unidadesRequestVersion.getFechaInicio());
		unidadesRequest.setFechaFin(unidadesRequestVersion.getFechaFin());
		return unidadesRequest;
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void crearOrganismo(Unidad org, PfResponseOrganismosDTO responseDir3) {
		PfOrganismoDIR3DTO unidadBDFinded = null;
		unidadBDFinded = getOrganismoDIR3ByCode(org.getCodigo());
		PfOrganismoDIR3DTO unidadBD = new PfOrganismoDIR3DTO(org);
		if (unidadBDFinded!=null){
			unidadBD.setPrimaryKey(unidadBDFinded.getPrimaryKey());
		}
		unidadBD.setIdPfResponseOrganismos(responseDir3);
		baseDAO.insertOrUpdate(unidadBD);
	}

	public PfOrganismoDIR3DTO getOrganismoDIR3ById(String id){		
		PfOrganismoDIR3DTO organizacion = 
				(PfOrganismoDIR3DTO) baseDAO.queryElementOneParameter("request.queryOrganismoDIR3ById", "id", Long.parseLong(id));
		return organizacion;
	}
	

	public PfOrganismoDIR3DTO getOrganismoDIR3ByCode(String code){		
		PfOrganismoDIR3DTO organizacion = 
				(PfOrganismoDIR3DTO) baseDAO.queryElementOneParameter("request.queryOrganismoDIR3ByCode", "codigoOrganismo", code);
		return organizacion;
	}
	
	public List<AbstractBaseDTO> getAutocompleteOrganismoDIR3ByFind(String find){		
		Map<String, Object> params = new HashMap<String, Object>();
		String findAux = find.toUpperCase();
		findAux = "%" + findAux + "%";
		params.put("find", findAux);
		return baseDAO.queryListMoreParameters("request.queryOrganismoDIR3ByFind", params);
	}
	
	public List<AbstractBaseDTO> getUltimasActualizacionesDIR3(){		
				List<AbstractBaseDTO> listaActualizaciones =  baseDAO.queryPaginatedListMoreParameters("administration.actualizacionesDIR3All", null, 0, 50);
		return listaActualizaciones;
	}

	public void actualizarOrganismosDir3Manual (ArchivoTemporalRequest archivoTemporalRequest) throws JAXBException {
		log.error("Inicio actualizarOrganismosDir3Manual");
		byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(archivoTemporalRequest.getContenidoDeArchivo().getBytes());
        
        ByteArrayInputStream inputXml = new ByteArrayInputStream(decoded);
        JAXBContext jc = JAXBContext.newInstance (Unidades.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Unidades unidadesDesdeServicio = (Unidades) unmarshaller.unmarshal(inputXml);
        if(unidadesDesdeServicio!=null && unidadesDesdeServicio.getUnidad()!=null){

    		PfResponseOrganismosDTO archivoRespuesta = new PfResponseOrganismosDTO();
    		archivoRespuesta.setFechaConsulta(Calendar.getInstance().getTime());
    		archivoRespuesta.setDocumentoObtenido(decoded);
    		archivoRespuesta.setCodigoRespuesta("99");
    		archivoRespuesta.setDescripcionRespuesta("Manual");
    		baseDAO.insertOrUpdate(archivoRespuesta);
    		
        	for(Unidad org: unidadesDesdeServicio.getUnidad()){
        		try{
        			crearOrganismo(org, archivoRespuesta);
        		}catch(Exception ex){
        			log.error(ex);
        		}
        	}
        }
        log.error("Fin actualizarOrganismosDir3Manual");
	
	}
}
