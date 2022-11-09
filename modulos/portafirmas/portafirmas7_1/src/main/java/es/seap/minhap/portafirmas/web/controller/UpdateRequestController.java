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
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.administration.UpdateRequestAdmBO;
import es.seap.minhap.portafirmas.business.beans.UpdateRequestUploadedDocuments;
import es.seap.minhap.portafirmas.business.beans.UploadedDocument;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UpdateRequest;

@ Controller
@ RequestMapping("updateRequest")
public class UpdateRequestController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Resource
	private UpdateRequestUploadedDocuments uploadedFiles;	
	
	@Autowired
	UpdateRequestAdmBO updateRequestAdmBO;
	
	@Autowired
	private ApplicationBO appBO;
	
	@RequestMapping(value = "/loadUpdateRequest")
	public ModelAndView loadUpdateRequest(ModelMap model) {
		
		// Se borran los ficheros de la carpeta temporal
		uploadedFiles.removeAlls();	
				
		// Se crea/ adjunta el modelo del formulario
		model.addAttribute("updateRequest", new UpdateRequest());
		log.debug("** Comienzo de Update Request");
		return new ModelAndView("updateRequest", model);
	}
	
	@RequestMapping(value = "/uploadFile")
	public void subeDocumento(@ ModelAttribute("updateRequest") UpdateRequest updateRequest,
							  @RequestParam(value = "file") final MultipartFile multipart,
							 final HttpServletResponse response) throws IOException {
		log.debug("Metodo subeDocumento("+multipart.getSize()+")");
	
		String error = validarUpdateRequestFile(multipart.getOriginalFilename(),multipart.getSize());
		try {
			
			if(error.isEmpty()){
				// Se crea el documento
				log.debug("** Creacion del fichero " + multipart.getOriginalFilename() +" TIPO: " +  multipart.getContentType()
						+" Y TAMAÑO: " +  multipart.getSize());
				
				// Se crea el fichero
				String fileName = multipart.getOriginalFilename() + "-" + System.currentTimeMillis() + ".data";
				PfDocumentsDTO document = new PfDocumentsDTO();
				
				document.setDname(Util.getInstance().getNameFile(fileName));
				document.setDmime(Util.getInstance().getMimeTypeOf(fileName));
				
				UploadedDocument uploadedDocument = new UploadedDocument();
				uploadedDocument.setDocument(document);
				uploadedDocument.setName(multipart.getOriginalFilename());
				uploadedDocument.setFile(fileName);
				uploadedDocument.setSize(multipart.getSize());
	
				multipart.transferTo(new File(Constants.PATH_TEMP + fileName));
	
				Integer docId = uploadedFiles.addDocument(uploadedDocument);
				multipart.transferTo(new File(Constants.PATH_TEMP + fileName));
				
				// Se envía la respuesta de éxito
				response.setContentType("application/json");
				response.getWriter().write("{\"id\":" + docId + ",\"success\":true}");
			}else{
				// Se envía la respuesta el porque
				response.setContentType("application/json");
				response.getWriter().write("{\"success\":false,\"error\":\"error\"}");
			}
		} catch (Exception e) {
			log.debug("Error al adjuntar el documento " + multipart.getOriginalFilename());
			e.printStackTrace();
			response.setContentType("application/json");
			response.getWriter().write("{\"success\":false,\"error\":\"applicationError\"}");
		}
		
	}
	
	private String validarUpdateRequestFile(String nombre, long size) {
		// TODO Auto-generated method stub
		String resultado ="";
		long max = 8000000L;
		if (size >= max ) {
			log.debug("** MAX: " + max + ", SIZE: " + size);
			resultado = "{\"success\":false,\"error\":\"exceeded limit\"}";
		}else if(nombre.lastIndexOf(".txt")==-1){
			resultado = "{\"success\":false,\"error\":\"extension not validate\"}";
		}else if(size == 0){
			resultado = "{\"success\":false,\"error\":\"file is empty\"}";
		}
		log.debug("** validacion del fichero " + nombre +", con " + size + " es " +  resultado);
		return resultado;
	}

	@RequestMapping(value = "/deleteDoc")
	public ModelAndView borraDocumento(@ModelAttribute("updateRequest") UpdateRequest updateRequest,
			   @RequestParam(value = "type") final String type,
			   @RequestParam(value = "id") final String id) {
		
		ModelAndView result = new ModelAndView();
		UploadedDocument doc = uploadedFiles.getDocuments().get(id);
		File file = new File(Constants.PATH_TEMP + doc.getFile());
		file.delete();
		uploadedFiles.removeDocument(id);
		result.addObject("updateRequest", updateRequest);
		result.setViewName("updateRequest");
		return result;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ ModelAttribute("updateRequest") UpdateRequest updateRequest,
			SessionStatus status){
		
		String resultado = "";
		List<File> attachedFiles = new LinkedList<File>();
		List<String> filesNameList = new ArrayList<String>();
		
		if (uploadedFiles.getDocuments().isEmpty()) {
			log.debug("*** Los archivos estan vacios");
			resultado = "No hay ningun archivo seleccionado para procesar";
		} else {
			resultado = "Los archivos (";
			for (UploadedDocument doc : uploadedFiles.getDocuments().values()) {
				// Se recupera el archivo de la carpeta temporal
				File file = new File(Constants.PATH_TEMP + doc.getFile());
				log.debug(" Nombre del fichero: " + file.getName() + ", Tamaño: " + file.length());
				attachedFiles.add(file);
				filesNameList.add(file.getName());
				resultado = resultado + file.getName() + ", ";
			}
			resultado = resultado + ") se han procesado correctamente.";
			updateRequestAdmBO.processFileList(filesNameList);
			
		}
		
		// Se borran los ficheros de la carpeta temporal
		uploadedFiles.removeAlls();
		for (File file : attachedFiles) {
			file.delete();
		}
		
		ModelMap model = new ModelMap();
		model.addAttribute("updateRequest", updateRequest);
		model.addAttribute("updateRequestMsm", 7);
		model.addAttribute("message", resultado);
		
		// Cargamos datos iniciales de la pestaña de configuración global.
		// TODO: Pensar una forma mejor de hacerlo para no repetir código.
		
		List<AbstractBaseDTO> resultado_global = appBO.listTypesApp("GLOBAL");
		List<AbstractBaseDTO> listValores = appBO.listValuesParams(resultado_global);
		model.addAttribute("listGlobal",resultado_global);
		model.addAttribute("listValores", listValores);
		
		List<AbstractBaseDTO> resultado_login = appBO.listTypesApp("LOGIN");
		List<AbstractBaseDTO> listValoresLogin = appBO.listValuesParams(resultado_login);
		model.addAttribute("listLogin", resultado_login);
		model.addAttribute("listValoresLogin", listValoresLogin);
		
		List<AbstractBaseDTO> resultado_smd = appBO.listTypesApp("SM_DOCEL");
		List<AbstractBaseDTO> listValoresSmd = appBO.listValuesParams(resultado_smd);
		model.addAttribute("listSMD", resultado_smd);
		model.addAttribute("listValoresSMD", listValoresSmd);
		
		List<AbstractBaseDTO> resultado_smp = appBO.listTypesApp("SP_DOCEL");
		List<AbstractBaseDTO> listValoresSmp = appBO.listValuesParams(resultado_smp);
		model.addAttribute("listSMP", resultado_smp);
		model.addAttribute("listValoresSMP", listValoresSmp);
		
		List<AbstractBaseDTO> resultado_estilo = appBO.listTypesApp("ESTILO");
		List<AbstractBaseDTO> listValoresEstilo = appBO.listValuesParams(resultado_estilo);
		model.addAttribute("listEstilo", resultado_estilo);
		model.addAttribute("listValoresEstilo", listValoresEstilo);
		
		
		return new ModelAndView("administration", model);
		
	}

	
	
}
