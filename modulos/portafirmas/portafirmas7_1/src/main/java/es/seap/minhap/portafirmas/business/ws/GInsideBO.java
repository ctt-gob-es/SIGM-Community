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

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.exceptions.GInsideException;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.metadata.MetadataConverter;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.ws.inside.GInsideClientManager;
import es.seap.minhap.portafirmas.ws.inside.GInsideConfigManager;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoDocumentoConversionInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.DocumentoEniFileInsideConMAdicionales;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.DocumentoEniFileInside;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.TipoMetadatosAdicionales;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.GInsideUserTokenWebService;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.InsideWSException;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class GInsideBO {

	Logger log = Logger.getLogger(GInsideBO.class);

	@Autowired
	private GInsideClientManager ginsideClientManager; 
	
	@Autowired
	private GInsideConfigManager ginsideConfigManager;
	
	@Autowired 
	MetadataConverter metadataConverter;
	
	@Autowired
	UtilComponent util;
		
	/**
	 * Valida una firma
	 * @param sign firma
	 * @param document documento firmado (si es nulo, va implícito en la firma)
	 * @param hash hash firmado (puede ser null).
	 * @param algoritmoHash algoritmo con que se ha calculado el hash
	 * @param tipoFirma tipo de la firma
	 * @param idConf identificador de la configuración
	 * @return "OK" si la firma es válida, motivo de no validez en caso de no ser válida.
	 * @throws EeutilException
	 */
	public byte[] convertirDocumentoAEniConMAdicionales (DocumentEni documentEni, boolean firmadoConCertificado, String mimeType, long idConf) throws GInsideException {
		
		Map<String, String> params = null;
		byte[] content = null;
		
		// Carga de la configuración
		params = ginsideConfigManager.cargarConfiguracion (idConf);
		GInsideUserTokenWebService ginsideUserTokenMtomWebService = null;
			
		if (params == null) {
			throw new GInsideException ("No se han encontrado parametros de configuracion de GINSIDE");
		}
		
		try {
			ginsideUserTokenMtomWebService = ginsideClientManager.getGInsideClient(params);
		} catch (Throwable t) {
			throw new GInsideException ("No se ha podido obtener el stub de GInside", t);
		}
		TipoDocumentoConversionInside tipoDocumentoConversionInside = null;
		try {
			tipoDocumentoConversionInside = new TipoDocumentoConversionInside();
			tipoDocumentoConversionInside.setMetadatosEni(documentEni.getMetadatosEni());
			tipoDocumentoConversionInside.setCsv(documentEni.getCsv());
			tipoDocumentoConversionInside.setContenido(documentEni.getContenido());
			tipoDocumentoConversionInside.setFirmadoConCertificado(firmadoConCertificado);
			
			if (util.isNotEmpty(documentEni.getMetadatosAdicionales())) {
				//convertirmos los metadatos adicionales de ws portafirmas a ws inside
				TipoMetadatosAdicionales tipoMetadatosAdicionales = metadataConverter.convertToGInside(documentEni.getMetadatosAdicionales());
			
				DocumentoEniFileInsideConMAdicionales documentoEniFileInsideConMAdicionales = ginsideUserTokenMtomWebService.convertirDocumentoAEniConMAdicionales(tipoDocumentoConversionInside, tipoMetadatosAdicionales, null, false);
				content = documentoEniFileInsideConMAdicionales.getDocumentoEniBytes();
			} else {
				DocumentoEniFileInside documentoEniFileInsideMtom = ginsideUserTokenMtomWebService.convertirDocumentoAEni(tipoDocumentoConversionInside, null, false);
				content = documentoEniFileInsideMtom.getDocumentoEniBytes();
			}
		} catch (InsideWSException e) {
			log.error("Se ha producido un error en la llamada a convertirDocumentoAEniConMAdicionales de GInsideService", e);
			StringBuffer tmpBuffer = new StringBuffer(e.getMessage());
			tmpBuffer.append(",");
			tmpBuffer.append(e.getFaultInfo().getDescripcion());
			throw new GInsideException(tmpBuffer.toString(), e);
		} catch (Throwable t) {
			log.error("Se ha producido un error en la llamada a convertirDocumentoAEniConMAdicionales de GInsideService", t);
			throw new GInsideException("Se ha producido un error en la llamada a convertirDocumentoAEniConMAdicionales de GInsideService" + t.getMessage());
		}
		
		return content;
	}

}
