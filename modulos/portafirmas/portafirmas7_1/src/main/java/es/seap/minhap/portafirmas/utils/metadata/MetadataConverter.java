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

package es.seap.minhap.portafirmas.utils.metadata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsMetadataDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.MetadataNotValidException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.EnumeracionEstadoElaboracion;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoDocumental;
//import es.seap.minhap.portafirmas.ws.inside.eni.documentoe.metadatos.TipoEstadoElaboracion;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.EnumeracionEstadoElaboracion;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumental;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoEstadoElaboracion;

import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.MetadatoAdicional;


@Component
public class MetadataConverter {
	
	@Autowired
	UtilComponent util;
	
	public static final String DEFAULT_METADATA_ADITIONAL_TYPE = "xsd:string";

	public es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoMetadatosAdicionales convertToGInside(List<MetadatoAdicional> metadatosAdicionales) throws MetadataNotValidException {
		es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoMetadatosAdicionales retorno = null;
		
		if (util.isNotEmpty(metadatosAdicionales)) {
			retorno = new es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoMetadatosAdicionales();
			
			for (MetadatoAdicional metadatoPortafirmas : metadatosAdicionales) {
				MetadatoAdicional metadatoGInside = new MetadatoAdicional();
				metadatoGInside.setNombre(metadatoPortafirmas.getNombre());
				metadatoGInside.setTipo(metadatoPortafirmas.getTipo());
				
				metadatoGInside.setValor(metadatoPortafirmas.getValor());
				retorno.getMetadatoAdicional().add(metadatoGInside);
			}
		}
		
		return retorno;
	}
	
	public List<PfApplicationsMetadataDTO> documentEniToApplicationsMetadataDTO(DocumentEni documentEni, PfApplicationsDTO application, PfUsersDTO user) {
		
		List<PfApplicationsMetadataDTO> applicationsMetadataDTOList = new ArrayList<PfApplicationsMetadataDTO>(); 
		
		Map<String, String> metadatosEniMap = createMetadatosEniMap(documentEni.getMetadatosEni());

		// Rellenamos los Metadatos ENI
		for (Map.Entry<String, String> metadatoEni : metadatosEniMap.entrySet()) {
		    PfApplicationsMetadataDTO applicationsMetadataDTO = fillApplicationsMetadataDTO(
		    		metadatoEni.getKey(), metadatoEni.getValue(), Boolean.TRUE, Boolean.TRUE, user.getCidentifier());
		    applicationsMetadataDTO.setPfApplication(application);
		    
		    applicationsMetadataDTOList.add(applicationsMetadataDTO);
		}
		
		// Rellenamos los Metadatos Adicionales
		if (util.isNotEmpty(documentEni.getMetadatosAdicionales())) {
			for (MetadatoAdicional metadatoAdicional : documentEni.getMetadatosAdicionales()) {
				  boolean isMandatory = false;
				  if (documentEni.getMetadatosObligatorios() != null 
						  && documentEni.getMetadatosObligatorios().contains(metadatoAdicional.getNombre())) {
					  isMandatory = true;
				  }
				  PfApplicationsMetadataDTO applicationsMetadataDTO = fillApplicationsMetadataDTO(
						  metadatoAdicional.getNombre(), (String) metadatoAdicional.getValor(), Boolean.FALSE, isMandatory, user.getCidentifier());
				  applicationsMetadataDTO.setPfApplication(application);
				    
				  applicationsMetadataDTOList.add(applicationsMetadataDTO);
			}
		}
		
		return applicationsMetadataDTOList;
	}
	
	
	public DocumentEni applicationsMetadataDTOToDocumentEni(List<PfApplicationsMetadataDTO> appMetadataDtoList) {
		DocumentEni documentEni = new DocumentEni();
		TipoDocumentoConversionInside.MetadatosEni metadatosEni = new TipoDocumentoConversionInside.MetadatosEni();
		List<MetadatoAdicional> metadatosAdicionales = new ArrayList<MetadatoAdicional>();
		List<String> mandatoryMetadataList = new ArrayList<String>(); 
		
		for (PfApplicationsMetadataDTO appMetadataDto : appMetadataDtoList) {
			
			if (appMetadataDto.getLeni()) {
				getMetadatosEni(appMetadataDto, metadatosEni); 
			} else {	
				MetadatoAdicional metadatoAdicional = new MetadatoAdicional();
				metadatoAdicional.setNombre(appMetadataDto.getDname());
				metadatoAdicional.setValor(appMetadataDto.getDvalue());
				metadatosAdicionales.add(metadatoAdicional);
				if (appMetadataDto.getLmandatory()) {
					mandatoryMetadataList.add(appMetadataDto.getDname());
				}
			}
		}
		
		documentEni.setMetadatosEni(metadatosEni);
		documentEni.setMetadatosAdicionales(metadatosAdicionales);
		documentEni.setMetadatosObligatorios(mandatoryMetadataList);
		
		return documentEni;
	}
	
	private Map<String, String> createMetadatosEniMap(TipoDocumentoConversionInside.MetadatosEni metadatosEni) {
		
		Map<String, String> metadatosEniMap = new HashMap<String, String>();
		if (!StringUtils.isEmpty(metadatosEni.getVersionNTI())) {
			metadatosEniMap.put(Constants.METADATO_ENI_VERSION_NTI, metadatosEni.getVersionNTI());
		}
		if (!StringUtils.isEmpty(metadatosEni.getIdentificador())) {
			metadatosEniMap.put(Constants.METADATO_ENI_IDENTIFICADOR, metadatosEni.getIdentificador());
		}
		if (metadatosEni.getTipoDocumental() != null 
				&& !StringUtils.isEmpty(metadatosEni.getTipoDocumental().value())) {
			metadatosEniMap.put(Constants.METADATO_ENI_TIPO_DOCUMENTAL, metadatosEni.getTipoDocumental().value());
		}
		if (metadatosEni.getEstadoElaboracion() != null 
				&& !StringUtils.isEmpty(metadatosEni.getEstadoElaboracion().getValorEstadoElaboracion().value())) {
			metadatosEniMap.put(Constants.METADATO_ENI_ESTADO_ELABORACION, metadatosEni.getEstadoElaboracion().getValorEstadoElaboracion().value());
		}
		
		if (metadatosEni.isOrigenCiudadanoAdministracion()) {
			metadatosEniMap.put(Constants.METADATO_ENI_ORIGEN, "1");
		} else {
			metadatosEniMap.put(Constants.METADATO_ENI_ORIGEN, "0");
		}
		
		if (metadatosEni.getOrgano().size() > 0) {
			int numOrganos = 0;
			for (String organo : metadatosEni.getOrgano()) {
				String nombre =  Constants.METADATO_ENI_ORGANO + numOrganos;
				metadatosEniMap.put(nombre, organo);
				numOrganos++;
			}
		}
		
		return metadatosEniMap;
	}
	
	private PfApplicationsMetadataDTO fillApplicationsMetadataDTO(String name, String value, Boolean isEni, Boolean isMandatory, String userIdentifier) {
		
		PfApplicationsMetadataDTO applicationsMetadataDTO = new PfApplicationsMetadataDTO();
		applicationsMetadataDTO.setLeni(isEni);
		applicationsMetadataDTO.setDname(name);
		applicationsMetadataDTO.setDvalue(value);
		applicationsMetadataDTO.setLmandatory(isMandatory);
		
		applicationsMetadataDTO.setCcreated(userIdentifier);
		applicationsMetadataDTO.setFcreated(Calendar.getInstance().getTime());
		
		return applicationsMetadataDTO;
	}
	
	private void getMetadatosEni(PfApplicationsMetadataDTO appMetadataDto, TipoDocumentoConversionInside.MetadatosEni metadatosEni) {
	
		if (appMetadataDto.getDname().equals(Constants.METADATO_ENI_VERSION_NTI)) {
			metadatosEni.setVersionNTI(appMetadataDto.getDvalue());
		}
		
		if (appMetadataDto.getDname().equals(Constants.METADATO_ENI_IDENTIFICADOR)) {
			metadatosEni.setIdentificador(appMetadataDto.getDvalue());
		}
		
		if (appMetadataDto.getDname().equals(Constants.METADATO_ENI_ESTADO_ELABORACION)) {
			metadatosEni.setEstadoElaboracion(new TipoEstadoElaboracion());
			metadatosEni.getEstadoElaboracion()
					.setValorEstadoElaboracion(
							EnumeracionEstadoElaboracion.fromValue(appMetadataDto.getDvalue()));
			
			
		}
		
		if (appMetadataDto.getDname().equals(Constants.METADATO_ENI_ORIGEN)) {
			if ("1".equals(appMetadataDto.getDvalue())) {
				metadatosEni.setOrigenCiudadanoAdministracion(true);
			} else if ("0".equals(appMetadataDto.getDvalue())) {
				metadatosEni.setOrigenCiudadanoAdministracion(false);
			}
			
		}
		
		if (appMetadataDto.getDname().equals(Constants.METADATO_ENI_TIPO_DOCUMENTAL)) {
			metadatosEni.setTipoDocumental(TipoDocumental.fromValue(appMetadataDto.getDvalue()));
			
		}
		
		if (appMetadataDto.getDname().startsWith(Constants.METADATO_ENI_ORGANO)) {
			metadatosEni.getOrgano().add(appMetadataDto.getDvalue());
		}

	}
	
}
