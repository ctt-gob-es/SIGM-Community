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

package es.seap.minhap.portafirmas.utils.document.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.EeutilFirmaBO;
import es.seap.minhap.portafirmas.business.ws.GInsideBO;
import es.seap.minhap.portafirmas.business.ws.NASCustodyServiceBO;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputDocument;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputReport;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.ws.inside.GInsideConfigManager;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.EnumeracionEstadoElaboracion;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumental;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoEstadoElaboracion;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NASCustodyServiceInputImpl implements CustodyServiceInput {
	
	@Autowired
	private NASCustodyServiceBO nasCustodyServiceBO;
	
	@Autowired
	private GInsideBO ginsideBO;
	
	@Autowired
	private GInsideConfigManager ginsideConfigManager;
	
	@Autowired
	private EeutilFirmaBO eeutilFirmaBO;
	
	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameterMap = new HashMap<String, Object>();

	public void initialize(Map<String, Object> parameterMap)
			throws CustodyServiceException {
	}

	public String uploadFile(CustodyServiceInputDocument document,
			InputStream input) throws CustodyServiceException {
		try {
			String idENI = document.getIdEni();
			String dir3 = document.getRefNasDir3();
			DocumentEni documentEni = new DocumentEni();
			//byte[] ficheroFirmado = eeutilFirmaBO.firmarEnServidorXadesDetached(IOUtils.toByteArray(input));
			documentEni.setContenido(IOUtils.toByteArray(input));
			ArrayList<String> listaOrganos = new ArrayList<String>();
			listaOrganos.add(dir3);
			documentEni.setOrganoList(listaOrganos);
			TipoDocumentoConversionInside.MetadatosEni metadatosEni = new TipoDocumentoConversionInside.MetadatosEni();
			metadatosEni.setIdentificador(idENI);
			metadatosEni.getOrgano().add(dir3);
			//metadatosEni.setOrigenCiudadanoAdministracion(false);
			TipoEstadoElaboracion tipoEstadoElaboracion = new TipoEstadoElaboracion();
			tipoEstadoElaboracion.setValorEstadoElaboracion(EnumeracionEstadoElaboracion.EE_99);//Otros
			metadatosEni.setEstadoElaboracion(tipoEstadoElaboracion);
			metadatosEni.setTipoDocumental(TipoDocumental.TD_14);//Solicitud
			documentEni.setMetadatosEni(metadatosEni);
			byte[] contenido = ginsideBO.convertirDocumentoAEniConMAdicionales(documentEni, false, null, ginsideConfigManager.configuracionPorDefecto());

			if (nasCustodyServiceBO.consultarDocumentoENI(idENI, dir3)){
				nasCustodyServiceBO.modificarDocumentoENI(idENI, dir3, contenido);
			} else {
				nasCustodyServiceBO.guardarDocumentoENI(idENI, dir3, contenido);
			}
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando el fichero", e);
		}
	}
	
	public String uploadSign(CustodyServiceInputSign sign, InputStream input)
			throws CustodyServiceException {
		try {
			
			String idENI = sign.getIdEni();
			String dir3 = sign.getRefNasDir3();
			
			DocumentEni documentEni = new DocumentEni();
			documentEni.setContenido(IOUtils.toByteArray(input));
			try {
				input.reset();//Lo dejamos en la posición inicial para que se pueda volver a leer 
			} catch (Exception e) {
				//A veces el imput no permite esta operación
			}
			documentEni.setContenidoId(idENI);
			ArrayList<String> listaOrganos = new ArrayList<String>();
			listaOrganos.add(dir3);
			documentEni.setOrganoList(listaOrganos);
			TipoDocumentoConversionInside.MetadatosEni metadatosEni = new TipoDocumentoConversionInside.MetadatosEni();
			metadatosEni.setIdentificador(idENI);
			metadatosEni.getOrgano().add(dir3);
			//metadatosEni.setOrigenCiudadanoAdministracion(false);
			TipoEstadoElaboracion tipoEstadoElaboracion = new TipoEstadoElaboracion();
			tipoEstadoElaboracion.setValorEstadoElaboracion(EnumeracionEstadoElaboracion.EE_01);//Original
			metadatosEni.setEstadoElaboracion(tipoEstadoElaboracion);
			metadatosEni.setTipoDocumental(TipoDocumental.TD_01);//Resolucion
			documentEni.setMetadatosEni(metadatosEni);
			byte[] contenido = ginsideBO.convertirDocumentoAEniConMAdicionales(documentEni, false, null, ginsideConfigManager.configuracionPorDefecto());
			
			
			if (nasCustodyServiceBO.consultarDocumentoENI(idENI, dir3)){
				nasCustodyServiceBO.modificarDocumentoENI(idENI, dir3, contenido);
			} else {
				nasCustodyServiceBO.guardarDocumentoENI(idENI, dir3, contenido);
			}
			
			
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando la firma", e);
		}
	}
	
	public String uploadReport (CustodyServiceInputReport report, InputStream input)
			throws CustodyServiceException {
		try {
			
			String csv = report.getCsv();
			String dir3 = report.getRefNasDir3();
			
			if (nasCustodyServiceBO.consultarDocumentoPorCSV(csv, dir3)){
				nasCustodyServiceBO.modificarDocumentoPorCSV(csv, dir3, IOUtils.toByteArray(input));
			} else {
				nasCustodyServiceBO.guardarDocumentoPorCSV(csv, dir3, IOUtils.toByteArray(input));
			}
			
			
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando el informe", e);
		}
	}

	
	public String uploadNormalizedReport (CustodyServiceInputReport report, InputStream input)
			throws CustodyServiceException {
		try {
			
			String csv = report.getCsv();
			String dir3 = report.getRefNasDir3();
			
			if (nasCustodyServiceBO.consultarDocumentoPorCSV(csv, dir3)){
				nasCustodyServiceBO.modificarDocumentoPorCSV(csv, dir3, IOUtils.toByteArray(input));
			} else {
				nasCustodyServiceBO.guardarDocumentoPorCSV(csv, dir3, IOUtils.toByteArray(input));
			}
			
			
			return null;
		} catch (Exception e) {			
			throw new CustodyServiceException("Error guardando el informe normalizado", e);
		}
	}
	
	
}
