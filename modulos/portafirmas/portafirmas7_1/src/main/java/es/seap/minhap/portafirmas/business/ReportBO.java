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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.EEUtilUtilFirmaBO;
import es.seap.minhap.portafirmas.business.ws.EeutilFirmaBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceInputReport;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceInput;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ReportBO {


	Logger log = Logger.getLogger(ReportBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private CustodyServiceFactory custodyServiceFactory;

	@Autowired
	private EEUtilUtilFirmaBO eeUtilUtilFirmaBO;
	
	@Autowired
	private EeutilFirmaBO eeutilFirmaBO;
	
	@Autowired
	private UtilComponent util;
	
	public byte[] generarReport (byte[] firma, 
								 byte[] documento, 
								 String mimeDocument, 
								 PfApplicationsDTO peticionario,
								 PfSignsDTO signDTO) throws EeutilException, CustodyServiceException, IOException {
		
		this.generateCSV(signDTO, firma, false);
		byte[] reportBytes = eeUtilUtilFirmaBO.getDocumentWithSignInfo(
				firma,
				documento,
				mimeDocument,
				signDTO.getCformat(),
				Constants.COPIA_AUTENTICA,
				signDTO.getCsv(),
				signDTO.getPfDocument().getPfDocumentScope().getCdescription(),
				peticionario,
				signDTO);
		try {
			reportBytes = eeutilFirmaBO.firmarEnServidor(reportBytes);
			String tipoAlmacenamientoInformes = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.INFORMES.name());
			CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(tipoAlmacenamientoInformes);
			CustodyServiceInputReport report = new CustodyServiceInputReport ();
			report.setIdentifier(signDTO.getPrimaryKeyString());
			report.setSize(new BigDecimal(reportBytes.length));
			report.setCsv(signDTO.getCsv());
			String dir3 = "";
			try {
				UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
				PfUsersDTO user = authorization.getUserDTO();
				dir3 = user.getPfProvince().getCcodigoprovincia();
			} catch (Exception e) {
				//En caso de que sea un WS quien crea la petición no hay usuario logado
				// TODO: Hasta que las aplicaciones no estén asociadas a un dir3 ponemos el genérico de Administración del Estado EA9999999 es configurable en administracion custodia
				dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
			}
			report.setRefNasDir3(dir3);
			custodyService.uploadReport(report, new ByteArrayInputStream (reportBytes));
			signDTO.setcTipoInforme(tipoAlmacenamientoInformes);
			signDTO.setRefNASDir3Informe(dir3);
			baseDAO.insertOrUpdate(signDTO);
		} catch (Exception e) {
			//El informe no se guardó en bbdd pero lo devolvemos igualmente
		}
		
		return reportBytes;
		
	}
	
	public byte[] generarNormalizedReport (
			byte[] firma, 
			byte[] documento, 
			String mimeDocument, 
			PfApplicationsDTO peticionario,
			PfSignsDTO signDTO) throws EeutilException, CustodyServiceException, IOException {

		this.generateCSV(signDTO, firma, true);
		byte[] retorno = eeUtilUtilFirmaBO.getNormalizedDocumentWithSignInfo(
				firma,
				documento,
				mimeDocument,
				signDTO.getCformat(),
				Constants.COPIA_AUTENTICA,
				signDTO.getCsvNormalizado(),
				signDTO.getPfDocument().getPfDocumentScope().getCdescription(),
				peticionario);
		
		retorno = eeutilFirmaBO.firmarEnServidor(retorno);
		
		String tipoAlmacenamientoInformes = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.INFORMES.name());
		CustodyServiceInput custodyService = custodyServiceFactory.createCustodyServiceInput(tipoAlmacenamientoInformes);
		CustodyServiceInputReport report = new CustodyServiceInputReport ();
		report.setIdentifier(signDTO.getPrimaryKeyString());
		report.setSize(new BigDecimal(retorno.length));
		report.setCsv(signDTO.getCsvNormalizado());
		String dir3 = "";
		try {
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			dir3 = user.getPfProvince().getCcodigoprovincia();
		} catch (Exception e) {
			//En caso de que sea un WS quien crea la petición no hay usuario logado
			// TODO: Hasta que las aplicaciones no estén asociadas a un dir3 ponemos el genérico de Administración del Estado EA9999999 es configurable en administracion custodia
			dir3 = custodyServiceFactory.custodiaDir3PorDefecto();
		}
		report.setRefNasDir3(dir3);
		custodyService.uploadNormalizedReport(report, new ByteArrayInputStream (retorno));
		signDTO.setcTipoInformeNormalizado(tipoAlmacenamientoInformes);
		signDTO.setRefNASDir3InfNormalizado(dir3);
		baseDAO.insertOrUpdate(signDTO);
		return retorno;

	}
	
	private void generateCSV(PfSignsDTO signDTO, byte[] firma, boolean normalizado) throws EeutilException {
		if (eeUtilUtilFirmaBO.checkCSV()) {
			try {
				if (!normalizado){
					// Si no existe un CSV guardado en BB.DD. entonces es cuando hay que generar uno.
					if(util.esVacioONulo(signDTO.getCsv())) {
						String csv = eeUtilUtilFirmaBO.getCSV(firma, "");
						signDTO.setCsv(csv);
						baseDAO.insertOrUpdate(signDTO);
					}
				} else {
					// Si no existe un CSV guardado en BB.DD. entonces es cuando hay que generar uno.
					if(util.esVacioONulo(signDTO.getCsvNormalizado())) {
						String csv = eeUtilUtilFirmaBO.getCSV(firma, "");
						signDTO.setCsvNormalizado(csv);
						baseDAO.insertOrUpdate(signDTO);
					}
				}
			} catch (EeutilException pe) {
				String id = (signDTO != null && signDTO.getPfDocument() != null)?null:signDTO.getPfDocument().getPrimaryKeyString();
				log.error("No se ha podido generar el csv de la firma del documento con pk: " + id, pe);
				throw pe;
			}
		}
	}
	
	public boolean variosReport (String reqTagHash) {	
		List<AbstractBaseDTO> documents = baseDAO.queryListOneParameter("request.documentsAllByReqTagHash", "requestTag", reqTagHash);
		return documents.size() > 1;
	}

}
