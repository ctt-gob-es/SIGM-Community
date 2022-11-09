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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.XMLUtil;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.ws.csvstorage.client.CSVDocumentWSService;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ConsultarDocumento;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ConsultarDocumentoRequest;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ConsultarDocumentoResponse;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ContenidoInfo;
import es.seap.minhap.portafirmas.ws.csvstorage.client.GuardarDocumento;
import es.seap.minhap.portafirmas.ws.csvstorage.client.GuardarDocumentoRequest;
import es.seap.minhap.portafirmas.ws.csvstorage.client.GuardarDocumentoResponse;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ModificarDocumento;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ObtenerDocumento;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ObtenerDocumentoRequest;
import es.seap.minhap.portafirmas.ws.csvstorage.client.ObtenerDocumentoResponse;
import es.seap.minhap.portafirmas.ws.csvstorage.clientmanager.CSVStorageClientManager;

@Service
public class NASCustodyServiceBO {
	@Autowired
	private CSVStorageClientManager csvStorageClientManager;
	
	public boolean consultarDocumentoPorCSV (String csv, String dir3) throws CustodyServiceException{
		boolean existe = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ConsultarDocumentoRequest consultarDocumentoRequest = new ConsultarDocumentoRequest();
			consultarDocumentoRequest.setDir3(dir3);
			consultarDocumentoRequest.setCsv(csv);
			ConsultarDocumento consultarDocumento = new ConsultarDocumento();
			consultarDocumento.setConsultarDocumentoRequest(consultarDocumentoRequest);
			ConsultarDocumentoResponse consultarDocumentoResponse = csvDocumentWSService.consultarDocumento(consultarDocumento);
			if (consultarDocumentoResponse!= null && consultarDocumentoResponse.getResponse()!=null 
					&& consultarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(consultarDocumentoResponse.getResponse().getCodigo())){
				existe = true;
			} 
		} catch (Exception e) {
			throw new CustodyServiceException("Error consultando el documento", e);
		}
		return existe; 
	}
	
	public boolean consultarDocumentoENI (String idENI, String dir3) throws CustodyServiceException{
		boolean existe = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ConsultarDocumentoRequest consultarDocumentoRequest = new ConsultarDocumentoRequest();
			consultarDocumentoRequest.setDir3(dir3);
			consultarDocumentoRequest.setIdENI(idENI);
			ConsultarDocumento consultarDocumento = new ConsultarDocumento();
			consultarDocumento.setConsultarDocumentoRequest(consultarDocumentoRequest);
			ConsultarDocumentoResponse consultarDocumentoResponse = csvDocumentWSService.consultarDocumento(consultarDocumento);
			if (consultarDocumentoResponse!= null && consultarDocumentoResponse.getResponse()!=null 
					&& consultarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(consultarDocumentoResponse.getResponse().getCodigo())){
				existe = true;
			} 
		} catch (Exception e) {
			throw new CustodyServiceException("Error consultando el documento", e);
		}
		return existe; 
	}
	
	public boolean guardarDocumentoPorCSV (String csv, String dir3, byte[] contenido) throws CustodyServiceException{
		boolean correcto = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ContenidoInfo contenidoInfo = new ContenidoInfo();
			contenidoInfo.setTipoMIME(Constants.PDF_MIME);
			contenidoInfo.setContenido(contenido);
			GuardarDocumentoRequest guardarDocumentoRequest = new GuardarDocumentoRequest();
			guardarDocumentoRequest.setCsv(csv);
			guardarDocumentoRequest.setContenido(contenidoInfo);
			guardarDocumentoRequest.setDir3(dir3);
			GuardarDocumento guardarDocumento = new GuardarDocumento();
			guardarDocumento.setGuardarDocumentoRequest(guardarDocumentoRequest);
			GuardarDocumentoResponse guardarDocumentoResponse = csvDocumentWSService.guardarDocumento(guardarDocumento);
			
			if (guardarDocumentoResponse!= null && guardarDocumentoResponse.getResponse()!=null 
					&& guardarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(guardarDocumentoResponse.getResponse().getCodigo())){
				correcto = true;
			} else {
				throw new CustodyServiceException("Error guardando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error guardando el documento", e);
		}
		return correcto; 
	}
	
	public boolean guardarDocumentoENI (String idENI, String dir3, byte[] contenido) throws CustodyServiceException{
		boolean correcto = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ContenidoInfo contenidoInfo = new ContenidoInfo();
			contenidoInfo.setTipoMIME(Constants.ENI_MIMETYPE_XML);
			contenidoInfo.setContenido(contenido);
			GuardarDocumentoRequest guardarDocumentoRequest = new GuardarDocumentoRequest();
			guardarDocumentoRequest.setIdEni(idENI);
			guardarDocumentoRequest.setDir3(dir3);
			
			guardarDocumentoRequest.setContenido(contenidoInfo);
			
			GuardarDocumento guardarDocumento = new GuardarDocumento();
			guardarDocumento.setGuardarDocumentoRequest(guardarDocumentoRequest);
			GuardarDocumentoResponse guardarDocumentoResponse = csvDocumentWSService.guardarDocumento(guardarDocumento);

			if (guardarDocumentoResponse!= null && guardarDocumentoResponse.getResponse()!=null 
					&& guardarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(guardarDocumentoResponse.getResponse().getCodigo())){
				correcto = true;
			} else {
				throw new CustodyServiceException("Error guardando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error guardando el documento", e);
		}
		return correcto; 
	}
	
	public boolean modificarDocumentoPorCSV (String csv, String dir3, byte[] contenido) throws CustodyServiceException{
		boolean correcto = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ContenidoInfo contenidoInfo = new ContenidoInfo();
			contenidoInfo.setTipoMIME(Constants.PDF_MIME);
			contenidoInfo.setContenido(contenido);
			GuardarDocumentoRequest guardarDocumentoRequest = new GuardarDocumentoRequest();
			guardarDocumentoRequest.setCsv(csv);
			guardarDocumentoRequest.setContenido(contenidoInfo);
			guardarDocumentoRequest.setDir3(dir3);
			ModificarDocumento modificarDocumento = new ModificarDocumento();
			modificarDocumento.setModificarDocumentoRequest(guardarDocumentoRequest);
			GuardarDocumentoResponse guardarDocumentoResponse = csvDocumentWSService.modificarDocumento(modificarDocumento);
			
			if (guardarDocumentoResponse!= null && guardarDocumentoResponse.getResponse()!=null 
					&& guardarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(guardarDocumentoResponse.getResponse().getCodigo())){
				correcto = true;
			} else {
				throw new CustodyServiceException("Error modificando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error modificando el documento", e);
		}
		return correcto; 
	}
	
	public boolean modificarDocumentoENI (String idENI, String dir3, byte[] contenido) throws CustodyServiceException{
		boolean correcto = false;
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ContenidoInfo contenidoInfo = new ContenidoInfo();
			contenidoInfo.setTipoMIME(Constants.ENI_MIMETYPE_XML);
			contenidoInfo.setContenido(contenido);
			GuardarDocumentoRequest guardarDocumentoRequest = new GuardarDocumentoRequest();
			guardarDocumentoRequest.setIdEni(idENI);
			guardarDocumentoRequest.setContenido(contenidoInfo);
			guardarDocumentoRequest.setDir3(dir3);
			ModificarDocumento modificarDocumento = new ModificarDocumento();
			modificarDocumento.setModificarDocumentoRequest(guardarDocumentoRequest);
			GuardarDocumentoResponse guardarDocumentoResponse = csvDocumentWSService.modificarDocumento(modificarDocumento);
			
			if (guardarDocumentoResponse!= null && guardarDocumentoResponse.getResponse()!=null 
					&& guardarDocumentoResponse.getResponse().getCodigo()!=null 
					&& "0".equals(guardarDocumentoResponse.getResponse().getCodigo())){
				correcto = true;
			} else {
				throw new CustodyServiceException("Error modificando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error modificando el documento", e);
		}
		return correcto; 
	}
	
	public byte[] obtenerDocumentoPorCSV (String csv, String dir3) throws CustodyServiceException{
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ObtenerDocumentoRequest obtenerDocumentoRequest = new ObtenerDocumentoRequest();
			obtenerDocumentoRequest.setCsv(csv);
			obtenerDocumentoRequest.setDir3(dir3);
			ObtenerDocumento obtenerDocumento = new ObtenerDocumento();
			obtenerDocumento.setObtenerDocumentoRequest(obtenerDocumentoRequest);
			ObtenerDocumentoResponse obtenerDocumentoResponse = csvDocumentWSService.obtenerDocumento(obtenerDocumento);
			
			if (obtenerDocumentoResponse!= null && obtenerDocumentoResponse.getDocumentoResponse()!=null 
					&& obtenerDocumentoResponse.getDocumentoResponse().getCodigo()!=null 
					&& "0".equals(obtenerDocumentoResponse.getDocumentoResponse().getCodigo())
					&& obtenerDocumentoResponse.getDocumentoResponse().getContenido() != null
					&& obtenerDocumentoResponse.getDocumentoResponse().getContenido().getContenido() != null){
				return obtenerDocumentoResponse.getDocumentoResponse().getContenido().getContenido();
			} else {
				throw new CustodyServiceException("Error descargando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error descargando el documento", e);
		}
		
	}
	
	public byte[] obtenerDocumentoENI (String idENI, String dir3) throws CustodyServiceException{
		try {
			CSVDocumentWSService csvDocumentWSService = csvStorageClientManager.getCsvDocumentWSService_Cliente();
			
			ObtenerDocumentoRequest obtenerDocumentoRequest = new ObtenerDocumentoRequest();
			obtenerDocumentoRequest.setIdEni(idENI);
			obtenerDocumentoRequest.setDir3(dir3);
			ObtenerDocumento obtenerDocumento = new ObtenerDocumento();
			obtenerDocumento.setObtenerDocumentoRequest(obtenerDocumentoRequest);
			ObtenerDocumentoResponse obtenerDocumentoResponse = csvDocumentWSService.obtenerDocumento(obtenerDocumento);
			
			if (obtenerDocumentoResponse!= null && obtenerDocumentoResponse.getDocumentoResponse()!=null 
					&& obtenerDocumentoResponse.getDocumentoResponse().getCodigo()!=null 
					&& "0".equals(obtenerDocumentoResponse.getDocumentoResponse().getCodigo())
					&& obtenerDocumentoResponse.getDocumentoResponse().getContenido() != null
					&& obtenerDocumentoResponse.getDocumentoResponse().getContenido().getContenido() != null){
				//Hay que sacar el documento del nodo
				byte[] docEni = obtenerDocumentoResponse.getDocumentoResponse().getContenido().getContenido();
				byte[] contenido = XMLUtil.obtenerNodoContenidoEni(docEni);
				return contenido;
			} else {
				throw new CustodyServiceException("Error descargando el documento", new Exception());
			}
		} catch (Exception e) {
			throw new CustodyServiceException("Error descargando el documento", e);
		}
		
	}
	
	
}
