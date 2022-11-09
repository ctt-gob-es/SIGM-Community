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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.core.signers.AOSignerFactory;
import es.gob.afirma.signers.cades.AOCAdESSigner;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.gob.afirma.signers.xades.AOFacturaESigner;
import es.gob.afirma.signers.xades.AOXAdESSigner;
import es.gob.afirma.signers.xmldsig.AOXMLDSigSigner;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.BinaryDocument;
import es.seap.minhap.portafirmas.business.beans.binarydocuments.OutputStreamContent;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.previsualizacion.PrevisualizacionBO;

@Controller
@RequestMapping("report")
public class ReportController {

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private PrevisualizacionBO previsualizacionBO;

	@Resource(name = "messageProperties")
	private Properties messages;

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Método para cargar la página de usuario
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm() {
		return "generateReport";
	}

	/**
	 * Método para extraer el informe de firma del documento cargado
	 * @param file
	 * @param response
	 * @param model
	 * @return String
	 * @throws Exception 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String createReport(@RequestParam("file") MultipartFile file, HttpServletResponse response, Model model) throws Exception{

		ArrayList<String> errors = new ArrayList<String>();
		byte[] fichero;
		String nombreFichero;
		String tipoFirma;

		try {

			if (file.isEmpty() || file.getSize() == 0)			
				errors.add("No se ha introducido ningún documento.");			
			else{				
				tipoFirma = getTipoFirmaFromSignerGenerico(file.getBytes());

				if(tipoFirma==null)
					errors.add("El documento no tiene firma.");
				else{

					nombreFichero = file.getOriginalFilename();
					fichero = file.getBytes();

					generateHeaders(response, nombreFichero);

					cargaInforme(fichero, nombreFichero, response.getOutputStream());

					response.getOutputStream().flush();
					response.getOutputStream().close();

					return null;
				}
			}

		} catch (Exception e) {
			log.error("Error al adjuntar el documento " + file.getOriginalFilename(), e);
			errors.add(e.getMessage());
		}

		model.addAttribute("file", file);
		model.addAttribute("errors", errors);

		return "generateReport";		
	}

	/**Método para crear el informe de firma
	 * 
	 * @param fichero
	 * @param nombreFichero
	 * @param output
	 * @return BinaryDocument
	 * @throws Exception
	 */
	public BinaryDocument cargaInforme (byte[] fichero, String nombreFichero, OutputStream output) throws Exception{

		BinaryDocument binaryPrevisualizacion = new BinaryDocument ();

		try {

			previsualizacionBO.previsualizacion(new ByteArrayInputStream (fichero), output, applicationBO.queryApplicationPfirma());		
			binaryPrevisualizacion.setName(nombreFichero);
			binaryPrevisualizacion.setMime(Constants.PDF_MIME);
			binaryPrevisualizacion.setContent(new OutputStreamContent (output));

		} catch (Exception e) {
			log.error("No se puede obtener la previsualización, ", e);
			throw new Exception ("No se puede obtener la previsualización : " + nombreFichero + e.toString());
		}

		return binaryPrevisualizacion;
	}

	public void generateHeaders(HttpServletResponse response, String nombreFichero){

		response.setHeader("Content-Disposition", "attachment;filename=\"" + nombreFichero + ".pdf" + "\"");
		response.setHeader("Content-Transfer-Encoding","binary");
		response.setHeader("Expires","0");
		response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma","public");
		response.setContentType(Constants.PDF_MIME);
	}

	/**
	 * Obtiene el tipo de firma a partir del AOSigner
	 * @param sign fichero firmado
	 * @return El tipo de firma, según el signer
	 * @throws IOException 
	 */
	private String getTipoFirmaFromSignerGenerico (byte[] sign) throws IOException {
		String tipoFirma = null;

		es.gob.afirma.core.signers.AOSigner signer = obtenerSigner(sign);

		if (signer instanceof AOCAdESSigner) {
			tipoFirma = Constants.SIGN_FORMAT_CADES;
		} else if (signer instanceof AOPDFSigner) {
			tipoFirma = Constants.SIGN_FORMAT_PDF;
		} else if (signer instanceof AOXAdESSigner) {
			tipoFirma = Constants.SIGN_FORMAT_XADES_IMPLICIT;
		} else if (signer instanceof AOXMLDSigSigner) {
			tipoFirma = Constants.SIGN_FORMAT_XADES_ENVELOPING;
		} else if (signer instanceof AOFacturaESigner) {
			tipoFirma = "FACTURAE";
		} else {
			return null;
		}

		return tipoFirma;
	}

	/**
	 * Se obtiene un objeto para manipular la firma.
	 * @param bytes de la firma
	 * @return Instancia de un objeto para manipular la firma.
	 * @throws IOException 
	 */
	private AOSigner obtenerSigner (byte[] bytes) throws IOException {
		AOSigner signer = AOSignerFactory.getSigner(bytes);			
		return signer;
	}
}
