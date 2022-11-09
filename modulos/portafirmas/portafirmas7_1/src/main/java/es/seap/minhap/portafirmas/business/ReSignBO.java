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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.guadaltel.framework.signer.impl.util.ConstantsSigner;
import es.seap.minhap.portafirmas.business.ws.Afirma5BO;
import es.seap.minhap.portafirmas.business.ws.EEUtilOperFirmaBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilUtilFirmaBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputSign;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.RespuestaAmpliarFirma;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ReSignBO {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private Afirma5BO afirma5BO;
	
	@Autowired
	private DateComponent date;
	
	@Autowired
	private EEUtilOperFirmaBO eeUtilOperFirmaBO;
	
	@Autowired
	private EEUtilUtilFirmaBO eeUtilUtilFirmaBO;
	
	@Autowired
	private CustodyServiceFactory custodyServiceFactory;
	
	@Autowired
	private SignBO signBO;

	public RequestTagListDTO getListPaginatedByValidDate(Paginator paginator, Date validDate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("fecha", validDate);
		
		int firstPosition = (paginator.getCurrentPage() - 1) * paginator.getPageSize();
		
		long countData = baseDAO.queryCount("sign.signsByValidDateCount", parameters);
		List<AbstractBaseDTO> data = baseDAO.queryPaginatedListMoreParameters("sign.signsByValidDate", parameters, firstPosition, paginator.getPageSize()); 
		
		return new RequestTagListDTO(data, countData);
	}
	
	@Transactional(readOnly = false, rollbackFor={CustodyServiceException.class, EeutilException.class, IOException.class, Afirma5Exception.class})	
	public void reSign(Long idSign, PfUsersDTO userDTO) throws CustodyServiceException, EeutilException, IOException, Afirma5Exception {
		//obtenemos la firma a resellar
		PfSignsDTO oldSignDTO = (PfSignsDTO) baseDAO.queryElementOneParameter(
				"sign.sign", "primaryKey", idSign);
		
		
		PfRequestsDTO peticion = oldSignDTO.getPfSigner().getPfSignLine().getPfRequest();
		
		// Obtenemos la última firma realizada asociada a esa petición.
		if (peticion.getPfSignsLines().size()>1){
			final PfSignsDTO signToResign = custodyServiceFactory.signFileQuery(oldSignDTO.getPfDocument().getChash());
			if (!signToResign.getPrimaryKey().equals(oldSignDTO.getPrimaryKey())){
				log.debug("Refirmamos una firma diferente a la seleccionada");
				log.debug("Peticion: "+peticion.getPrimaryKey()+" "+peticion.getDsubject());
				log.debug("Firma inicial: "+oldSignDTO.getPrimaryKey()+" "+oldSignDTO.getPfSigner().getPfUser().getFullName());
				log.debug("Firma a refirmar: "+signToResign.getPrimaryKey()+" "+signToResign.getPfSigner().getPfUser().getFullName());
				oldSignDTO = signToResign;
			}
		}

		
		//duplicamos la linea de firma
		PfSignsDTO newSignDto = setReSignData(oldSignDTO, userDTO);
		
		//resellamos la firma antigua
		byte[] signResult = resign(oldSignDTO);
				
		//actualizamos con los datos de la ampliacion
		if (eeUtilUtilFirmaBO.checkCSV()) {
			String mime = Util.getInstance().loadSignMime().get(oldSignDTO.getCformat());
			String csv;
			try {
				csv = eeUtilUtilFirmaBO.getCSV(signResult, mime);
			} catch (EeutilException e) {
				throw new EeutilException("Error al ampliar firma, formato incompatible " + oldSignDTO.getCformat(), e);
			}
			newSignDto.setCsv(csv);
		}
		
		//actualizamos la firma antigua con la nueva fecha de validez
		//updateValidDate(oldSignDTO);
		// Actualizamos todas las firmas.
		for(PfSignsDTO firma: oldSignDTO.getPfDocument().getPfSigns()){
			if (!firma.getPrimaryKey().equals(newSignDto.getPrimaryKey())){
				updateValidDate(firma);
			}
		}

		//guardamos los datos de la nueva firma
		String storageType = custodyServiceFactory.storageTypePorTipoDocumento(Constants.tipoDocumentoACustodiar.FIRMAS.name());
		signBO.saveSign(newSignDto, userDTO, signResult, custodyServiceFactory.createCustodyServiceInput(storageType));		
	}
	
	private PfSignsDTO setReSignData(PfSignsDTO oldSignDTO, PfUsersDTO userDTO) {
		long idConf = oldSignDTO.getPfDocument().getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		Configuration conf = signBO.loadSignProperties(idConf);
		
		PfSignsDTO newSignDto = new PfSignsDTO(oldSignDTO.getPfSigner(), userDTO,
				oldSignDTO.getPfDocument(), null, null,
				null, null, Calendar.getInstance().getTime(), oldSignDTO.getCtype(),
				oldSignDTO.getCformat(), null, oldSignDTO.getLmovil());
		newSignDto.setPrimaryKey(null);
		String transactionId = new Long(new Date().getTime()).toString();
		newSignDto.setCtransaction(transactionId);
		
		newSignDto.setCapplication(conf.getString(ConstantsSigner.AFIRMA5_APPLICATION));
		try {
			URL url = new URL(conf.getString(ConstantsSigner.AFIRMA5_ENDPOINT));
			newSignDto.setCserver(url.getHost());
		} catch (MalformedURLException e) {
			// TODO hacer lo mismo igual que para la firma
		}
		updateValidDate(newSignDto);
		return newSignDto;
	}
	
	private byte[] resign(PfSignsDTO sign) throws CustodyServiceException, IOException, Afirma5Exception, EeutilException {
		//obtenemos la firma antigua
		byte[] firmaOld = getSign(sign);
		
		long idConf = sign.getPfDocument().getPfRequest().getPfApplication().getPfConfiguration().getPrimaryKey();
		
		byte[] signResult = null;
		if (afirma5BO.estaActivoSello(idConf)) {
			String formatoAmpliacion = sign.getPfDocument().getDmime().equalsIgnoreCase(Constants.SIGN_MIMETYPE_PDF) ? 
					Afirma5Constantes.UPGRADE_TIMESTAMP_PDF :
					Afirma5Constantes.UPGRADE_TIMESTAMP;
			//ampliamos la firma directamente contra @firma
			RespuestaAmpliarFirma resultAfirma = afirma5BO.ampliarFirma(formatoAmpliacion, firmaOld, null, idConf);
			signResult = resultAfirma.getFirmaAmpliada();
		} else {
			String formatoAmpliacion = sign.getPfDocument().getDmime().equalsIgnoreCase(Constants.SIGN_MIMETYPE_PDF) ? 
					Constants.RESIGN_FORMAT_PADES_LTV :
					Constants.RESIGN_FORMAT_X_L;
			//ampliamos la firma mediante el Eeutil
			signResult = eeUtilOperFirmaBO.ampliarFirma(formatoAmpliacion, firmaOld, null, idConf); 
		}
		return signResult;
	}
	
	private void updateValidDate(PfSignsDTO sign) {
		sign.setfValid(date.stringToDate(Constants.RESIGN_VALID_DATE));
		baseDAO.insertOrUpdate(sign);
	}
	
	private byte[] getSign(PfSignsDTO sign) throws CustodyServiceException, IOException {
		CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(sign.getCtype());
		
		CustodyServiceOutputSign custodySign = new CustodyServiceOutputSign();
		custodySign.setType(Constants.SIGN_TYPE_SERVER);
		custodySign.setIdentifier(sign.getPrimaryKeyString());
		custodySign.setUri(sign.getCuri());
		custodySign.setIdEni(sign.getRefNASIdEniFirma());
		custodySign.setRefNasDir3(sign.getRefNASDir3Firma());

		return custodyService.downloadSign(custodySign);
	}
	
}
