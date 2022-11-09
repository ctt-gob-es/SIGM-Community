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

package es.seap.minhap.portafirmas.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.metadata.ApplicationMetadataBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.metadata.MetadataConverter;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalRequest;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalResponse;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ArchivoTemporalResponseList;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.NuevosArchivosAnexosResponseList;
import es.seap.minhap.portafirmas.ws.inside.GInsideConfigManager;

@Controller
@RequestMapping("nuevosAnexosPeticionController")
public class NuevosAnexosPeticionController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	ApplicationMetadataBO applicationMetadataBO;
	
	@Autowired 
	MetadataConverter metadataConverter;
	
	@Autowired
	DateComponent dateComponent;
	
	@Autowired
	BinaryDocumentsBO binaryDocumentsBO;
	
	@Autowired
	GInsideConfigManager ginsideConfigManager;
	
	private static BigInteger temporalesAnexos = new BigInteger("0");

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	private static synchronized BigInteger getTemporalesAnexos() {
		temporalesAnexos=temporalesAnexos.add(new BigInteger("1"));
		return temporalesAnexos;
	}
	
	/**
	 * Método que carga inicialmente, la bandeja de peticiones 
	 * @param model Modelo de datos
	 * @return Página direccionada
	 * @throws IOException 
	 */
	@RequestMapping(value = "/addFile", method = RequestMethod.POST)
	public @ResponseBody ArchivoTemporalResponse addFile(@RequestBody ArchivoTemporalRequest archivoTemporalRequest) throws IOException {

		BigInteger tempDirId=null;
		
		if(archivoTemporalRequest.getCarpetaTemporal() == null 
				|| "".equalsIgnoreCase(archivoTemporalRequest.getCarpetaTemporal())
				|| "null".equalsIgnoreCase(archivoTemporalRequest.getCarpetaTemporal())
				){
			tempDirId = getTemporalesAnexos();
		}else{
			tempDirId = new BigInteger(archivoTemporalRequest.getCarpetaTemporal());
		}
		
		BigInteger tempDirFileId = getTemporalesAnexos();
		
		String tempDir = Constants.PATH_TEMP+Constants.FS +Constants.PATH_TEMP_NEW_ATTACHMENTS_REQUEST+Constants.FS+ tempDirId.toString()+Constants.FS+tempDirFileId+Constants.FS;
		
		File f = new File(tempDir);
		f.mkdirs();
		
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(archivoTemporalRequest.getContenidoDeArchivo().getBytes());
        FileOutputStream fos = new FileOutputStream(tempDir+archivoTemporalRequest.getNombreDeArchivo());
        fos.write(decoded);
        fos.close();
		
		return new ArchivoTemporalResponse(tempDirId.toString(), tempDirFileId.toString(), archivoTemporalRequest.getTipoArchivo());
	}

	@RequestMapping(value = "/removeFile", method = RequestMethod.POST)
	public @ResponseBody String removeFile(@RequestBody ArchivoTemporalResponse archivoTemporalResponse) throws IOException {

		String tempDirId=archivoTemporalResponse.getIdCarpetaTemporal();
		
		String tempDirFileId = archivoTemporalResponse.getIdCarpetaTemporalArchivo();
		
		String tempDir = Constants.PATH_TEMP+Constants.FS +Constants.PATH_TEMP_NEW_ATTACHMENTS_REQUEST+Constants.FS+ tempDirId+Constants.FS+tempDirFileId+Constants.FS;
		
		File directory = new File(tempDir);
		
		if(directory.exists() && directory.isDirectory()){
			FileUtils.deleteDirectory(directory);
		}
		
		return "{}";
	}

	@RequestMapping(value = "/saveFiles", method = RequestMethod.POST)
	public @ResponseBody NuevosArchivosAnexosResponseList saveFiles(@RequestBody ArchivoTemporalResponseList archivoTemporalResponseList) throws Exception{
		
		NuevosArchivosAnexosResponseList returnValue = null;
		
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO pfUser = authorization.getUserDTO();
		try {
			returnValue = requestBO.guardarNuevosAnexos(archivoTemporalResponseList, pfUser);
			for(ArchivoTemporalResponse it: archivoTemporalResponseList.getListArchivoTemporalResponse()){
				removeFile(it);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return returnValue;
	}
	
}