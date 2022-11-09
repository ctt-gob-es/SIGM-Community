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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.gob.aapp.csvbroker.webservices.querydocument.v1.CSVQueryDocumentException;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.ServletHelperBO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.CSVNotFoundException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Controller
@RequestMapping("valida")
public class ValidCSVController {


	@Autowired 
	private ServletHelperBO servletHelperBO; 

	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	BinaryDocumentsBO binaryDocumentsBO;
	
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Método para cargar la página de usuario
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm() {
		return "validaCSV";
	}

	/**
	 * Método para obtener el CSV a partir del código CSV recibido
	 * @param csv
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String validCSV(@RequestParam("csvId") String csvId, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			
				String errorValidacion = validarDatos(csvId, request);
				
				if (errorValidacion == null){
			
					String csv = formateoCSV(csvId);
					boolean normalizado = false;
					PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByCsv (csv);
					if (signDTO == null){
						signDTO = binaryDocumentsBO.getSignDTOByCsvNormalizado (csv);
						normalizado = true;
					}
					byte[] bytes =obtenerInformeCSV(signDTO, normalizado);
					setupGenericHeadersToResponse (response, binaryDocumentsBO.getNombreReport(signDTO.getPfDocument().getDname()));
					setupGenericContentTypeToResponse (response, Constants.PDF_MIME);
					org.apache.commons.io.IOUtils.copy(new ByteArrayInputStream (bytes), response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
		
					// Se ha lanzado el documento y ya no queremos devolver una vista
					return null;
				}else{					
					log.error(errorValidacion);
					errors.add(errorValidacion);
				}
				
		} catch (PfirmaException e) {
			log.error("Error controlado al validar el CSV: ", e);
			errors.add(e.getMessage());
		} catch (Exception e) {
			log.error("Error desconocido al validar el CSV", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		model.addAttribute("csvId", csvId);
		model.addAttribute("errors", errors);

		return "validaCSV";
	}

	public String formateoCSV (String csvId) throws PfirmaException{

		String csv = null;

		try {
			csv = Util.formatearCsv(csvId);
		} catch (CSVQueryDocumentException e) {
			log.error("ERROR: ValidCSVController.validCSV: " + messages.getProperty("wrongCSV") + ", ", e);
			throw new PfirmaException(messages.getProperty("wrongCSV"));
		}

		return csv;
	}

	public byte[] obtenerInformeCSV (PfSignsDTO signDTO, boolean normalizado) throws PfirmaException{

		try {
			return servletHelperBO.obtenerInformePorCSV(signDTO, applicationBO.queryApplicationPfirma(), normalizado);
		} catch (CSVNotFoundException | DocumentCantBeDownloadedException e) {
			log.error("ERROR: ValidCSVController.validCSV: " + messages.getProperty("CSVNotFound") + ", ", e);
			throw new PfirmaException(messages.getProperty("CSVNotFound"));
		}
	}
	
	public String validarDatos (String csvId, HttpServletRequest request){
		
		String erroresValidacion = null;
		
		String captcha = request.getParameter("captcha");
		
		if (captcha.isEmpty()){			
			erroresValidacion = "El código de seguridad no puede estar vacío.";
		}
		else if (!rpHash(captcha).equals(request.getParameter("captchaHash"))) {	
			erroresValidacion = "El código de seguridad introducido no es correcto.";
		}
		else if (csvId.isEmpty()){			
			erroresValidacion = "Debe insertar un valor para el código CSV.";
		}	
				
		return erroresValidacion;
	}
	
	private String rpHash(String value) { 
	    int hash = 5381; 
	    value = value.toUpperCase(); 
	    for(int i = 0; i < value.length(); i++) { 
	        hash = ((hash << 5) + hash) + value.charAt(i); 
	    } 
	    return String.valueOf(hash); 
	} 
	
	/**
	 * Cabeceras genéricas para descarga de un adjunto.
	 * @param response
	 * @param nameDocument
	 */
	public void setupGenericHeadersToResponse (HttpServletResponse response, String nameDocument) {
		response.setHeader("Content-Disposition", "attachment;filename=\"" + nameDocument + "\"");
		setupGenericHeadersToResponse (response);
	}
	
	/**
	 * 
	 * @param response
	 * @param mimeDocument
	 */
	public void setupGenericContentTypeToResponse (HttpServletResponse response, String mimeDocument) {
		response.setContentType(mimeDocument);
	}
	
	/**
	 * Cabeceras genéricas para descarga.
	 * @param response
	 */
	public void setupGenericHeadersToResponse (HttpServletResponse response) {
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Expires","0");
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma","public");
	}

}
