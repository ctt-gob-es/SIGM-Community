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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.CSVQRConstantes;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.UtilesQR;
import es.seap.minhap.portafirmas.utils.XMLUtil;
import es.seap.minhap.portafirmas.utils.application.ApplicationParameterUtil;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilUtilClientManager;
import es.seap.minhap.portafirmas.ws.eeutil.operfirma.InformacionFirma;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ApplicationLogin;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.CSVInfo;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ContenidoInfo;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.CopiaInfo;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo;
import es.seap.minhap.portafirmas.ws.eeutil.utilfirma.OpcionesPagina;
import ieci.tecdoc.sgm.tram.ws.server.dto.Binario;
import ieci.tecdoc.sgm.tram.ws.server.sigem.TramitacionWebService;
import ieci.tecdoc.sgm.tram.ws.server.sigem.TramitacionWebServiceService;
import ieci.tecdoc.sgm.tram.ws.server.sigem.TramitadorWebServiceConstanst;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EEUtilUtilFirmaBO {

	private static final String ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE = "Se ha producido un error en la llamada a generarCopiaFirma de EEUtilUtilFirma";
    private static final TramitadorWebServiceConstanst wsdl_constants = new TramitadorWebServiceConstanst();
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private RequestDAO requestDAO;
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private SignatoryBO signatoryBO;

	@Autowired
	private EeUtilUtilClientManager eeUtilUtilClientManager;

	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;

	@Autowired
	private UtilComponent util;
	
	@Autowired
	private UtilesQR utilesQR;
	
	@Autowired
	private MailToAdminBO mailToAdminBO;

	@Autowired
	private TagBO tagBO;
	
	@Resource(name = "configCSVQRProperties")
	private Properties properties;
	
	Logger log = Logger.getLogger(EEUtilUtilFirmaBO.class);

	public byte[] getDocumentWithSignInfo (byte[] sign, byte[] document, String tipoMIME, String tipoFirma, String tipoInforme, String csv,  String ambitoDocumento, PfApplicationsDTO peticionario) throws EeutilException {
		return getDocumentWithSignInfo (sign, document, tipoMIME, tipoFirma, tipoInforme, csv,  ambitoDocumento, false, peticionario, null);
	}
	
	public byte[] getDocumentWithSignInfo (byte[] sign, byte[] document, String tipoMIME, String tipoFirma, String tipoInforme, String csv,  String ambitoDocumento, PfApplicationsDTO peticionario, PfSignsDTO signDTO) throws EeutilException {
		return getDocumentWithSignInfo (sign, document, tipoMIME, tipoFirma, tipoInforme, csv,  ambitoDocumento, false, peticionario, signDTO);
	}
	
	public byte[] getNormalizedDocumentWithSignInfo (byte[] sign, byte[] document, String tipoMIME, String tipoFirma, String tipoInforme, String csv,  String ambitoDocumento, PfApplicationsDTO peticionario) throws EeutilException {
		return getDocumentWithSignInfo (sign, document, tipoMIME, tipoFirma, tipoInforme, csv,  ambitoDocumento, true, peticionario, null);
	}

	private byte[] getDocumentWithSignInfo (byte[] sign, byte[] document, String tipoMIME, String tipoFirma, String tipoInforme, String csv,  String ambitoDocumento, boolean normalized, PfApplicationsDTO peticionario, PfSignsDTO signDTO) throws EeutilException {
	
		byte fichero[] = null;
		byte ficheroAuxiliar[] = null;
		String datosDocumentoSigem = signDTO.getPfDocument().getPfRequest().getDreference();
		String datosDocumentoSigem_[];
		String host="https://sei1.dipucr.es:4443";
		String expediente ="";
		String id_documento ="";
		String entidad = "";
		QName SERVICE_NAME = new QName("http://server.ws.tram.sgm.tecdoc.ieci", "TramitacionWebServiceService");
		
		if(null != datosDocumentoSigem && !datosDocumentoSigem.equals("")) {
			
			datosDocumentoSigem_=datosDocumentoSigem.split("_");			
			
			entidad=datosDocumentoSigem_[2];
			id_documento=datosDocumentoSigem_[1];			
			
			try {
				   
				 //URL wsdlURL = TramitacionWebServiceService.WSDL_LOCATION;	      
				 //URL wsdlURL = new URL(wsdl_constants.getWsdlUrl().get(entidad).getWsdl());
				URL wsdlURL = new URL(wsdl_constants.getWsdlUrl().get(entidad));
				 
				 TramitacionWebServiceService ss = new TramitacionWebServiceService(wsdlURL, SERVICE_NAME);
				 TramitacionWebService port = ss.getTramitacionWebService();  
				        
				 log.warn("Invoking getJustificanteFirma...");
				 String _getJustificanteFirma_idEntidad = entidad;
				 String _getJustificanteFirma_guid = id_documento;
				 Binario _getJustificanteFirma__return = port.getJustificanteFirma(_getJustificanteFirma_idEntidad, _getJustificanteFirma_guid);
				 log.warn("getJustificanteFirma.result=" + _getJustificanteFirma__return);

				  //FileUtils.writeByteArrayToFile(new File("D:/prueba.pdf"), _getJustificanteFirma__return.getContenido());
				  //try (FileOutputStream stream = new FileOutputStream("D:/prueba.pdf")) {
				  //          stream.write(_getJustificanteFirma__return.getContenido());
				  // }	
				 fichero = _getJustificanteFirma__return.getContenido();
				
				
			} catch (Exception t) {
				mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_GENERAR_INFORME, csv, t);
				log.error(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE , t);
				throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + t.getMessage());
			}
			
			if(null !=fichero)
				return fichero;
			else {
				try {
					File input = new File("/portafirmas/config/documentos/informe_ayuda.pdf");				
					ficheroAuxiliar = FileUtils.readFileToByteArray(input);
					return ficheroAuxiliar;
				} catch (Exception e) {
					mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_GENERAR_INFORME, csv, e);
					log.error(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE , e);
					throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
				}
			}
				
			
		}
		
		return null;
		

		

	}
	
	private ListaFirmaInfo completarYmodificarListaFirmantes(ListaFirmaInfo listaFirmantesUtil, PfSignsDTO signDTO) {
		ListaFirmaInfo listaFirmantesResult = new ListaFirmaInfo();
		es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo.InformacionFirmas informacionFirmasResult =
				new es.seap.minhap.portafirmas.ws.eeutil.utilfirma.ListaFirmaInfo.InformacionFirmas();
		listaFirmantesResult.setInformacionFirmas(informacionFirmasResult);
		List<es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo> listaUtilResult =
				listaFirmantesResult.getInformacionFirmas().getInformacionFirmas();
		
		List<es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo> listaUtil = 
				listaFirmantesUtil.getInformacionFirmas().getInformacionFirmas();
		for (es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo firmaInfoUtilFor : listaUtil){
			es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo firmaInfoUtil = 
					new es.seap.minhap.portafirmas.ws.eeutil.utilfirma.FirmaInfo();
			firmaInfoUtil.setNifcif(""); //Quitamos el NIF para que no aparezca en el informe
			firmaInfoUtil.setNombre(firmaInfoUtilFor.getNombre());
			firmaInfoUtil.setApellido1(firmaInfoUtilFor.getApellido1());
			firmaInfoUtil.setApellido2(firmaInfoUtilFor.getApellido2());
			firmaInfoUtil.setFecha(firmaInfoUtilFor.getFecha());
			//firmaInfoUtil.setExtras(firmaInfoOper.getExtras()); //Quitamos este campo para que no aparezcan las NOTAS
			
			//Añadimos la acción concatenadola a la fecha
			try {
				List<AbstractBaseDTO> signLineList = tagBO.querySignLineList(signDTO.getPfSigner().getPfSignLine().getPfRequest());
				Iterator<AbstractBaseDTO> itLineasFirma = signLineList.iterator();
				boolean accionEncontrada = false;
				while (itLineasFirma.hasNext() && !accionEncontrada) {
					PfSignLinesDTO signLine = (PfSignLinesDTO) itLineasFirma.next();
					Iterator<PfSignersDTO> itFirmantes = signLine.getPfSigners().iterator();
					while (itFirmantes.hasNext() && !accionEncontrada) {
						PfSignersDTO firmante = itFirmantes.next();
						if (firmante.getPfUser().getCidentifier().equals(firmaInfoUtilFor.getNifcif())){
							accionEncontrada = true;
							firmaInfoUtil.setFecha(firmaInfoUtilFor.getFecha() + " | " + signLine.getAccionFirmante().getCdescription());
						}
					}
				}
			} catch (Exception e1) {
				
			}
			
			//en el informe hay que dejar el sello de tiempo pero lo concatenamos en fecha	//F - (Sello de Tiempo: 13/02/2018 12:09)
			String extras = firmaInfoUtilFor.getExtras();
			try {
				if (extras!= null && !"".equals(extras) && extras.indexOf("(Sello de Tiempo:")>=0 ){
					String selloTiempo = extras.substring(extras.indexOf("(Sello de Tiempo:")+1, extras.length()-1);
					firmaInfoUtil.setFecha(firmaInfoUtil.getFecha() + " | " + selloTiempo);
				}
			} catch (Exception e) {
				
			}
			//Añadimos si el usuario es delegado o sustituto concatenado al nombre
			if (signDTO != null && signDTO.getPfSigner()!= null && signDTO.getPfSigner().getTipoAutorizacion() !=null 
					&& signDTO.getPfSigner().getTipoAutorizacion().getDauthorizationType()!=null) {
				if (firmaInfoUtilFor.getApellido2() != null) {
					firmaInfoUtil.setApellido2(firmaInfoUtilFor.getApellido2() + " (" + signDTO.getPfSigner().getTipoAutorizacion().getCauthorizationType()+ ")");
				} else if (firmaInfoUtilFor.getApellido1() != null) {
					firmaInfoUtil.setApellido1(firmaInfoUtilFor.getApellido1() + " (" + signDTO.getPfSigner().getTipoAutorizacion().getCauthorizationType()+ ")");
				} else if (firmaInfoUtilFor.getNombre() != null) {
					firmaInfoUtil.setNombre(firmaInfoUtilFor.getNombre() + " (" + signDTO.getPfSigner().getTipoAutorizacion().getCauthorizationType()+ ")");
				} 
			}
			listaUtilResult.add(firmaInfoUtil);
		}
		
		return listaFirmantesResult;
	}

	private ApplicationLogin createUtilApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EEUtilUtilFirma es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EEUtilUtilFirma es nulo");
		}
		ApplicationLogin appInfo = 
				new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
		return appInfo;
	}

	/**
	 * @param evento
	 * @param csv 
	 * @throws EeutilException
	 */
//	private void sendMailToAdmin (String evento, String csv) throws EeutilException{
//		String entorno = applicationBO.getEnvironment().getTvalue();
//		if (noticeBO.isAdminNoticeEnabled()) {
//			try {
//				noticeService.doNoticeEeutilException(Constants.EMAIL_NOTICE, Constants.NOTICE_EEUTIL_EXCEPTION, evento, entorno, csv);
//			} catch (NoticeException e) {
//				log.error("No se puede enviar la notificación a los administradores: " + e.getMessage() + e);
//				throw new EeutilException ("No se puede enviar la notificación a los administradores: ", e);
//			}
//		}
//	}

	/**
	 * @param document
	 * @param tipoMIME
	 * @param csv
	 * @param ambito
	 * @param urlValidacion
	 * @param urlQR
	 * @return
	 */
	private static CopiaInfo createCopiaInfoCopiaAutentica (byte[] document, String tipoMIME, String csv, String ambito, String urlValidacion, String urlQR) {
		CopiaInfo copiaInfo = generateDefaultCopiaInfo(document, tipoMIME);

		copiaInfo.setIdAplicacion(ambito); // ambito

		copiaInfo.setUrlSede(urlValidacion); //urlSede
		copiaInfo.setTituloAplicacion("AMBITO"); // tituloAplicacion
		copiaInfo.setTituloURL(""); // tituloURL
		copiaInfo.setLateral(null); // lateral
		copiaInfo.setUrlQR(urlQR); // urlQR

		// Si la firma no tiene CSV este no se imprime en el informe
		if (csv != null) {
			copiaInfo.setCsv(csv); //csv
			copiaInfo.setTituloCSV("");
		}

		return copiaInfo;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CopiaInfo setParameteFromConfig(CopiaInfo copiaInfo, PfApplicationsDTO peticionario) throws EeutilException {
		try {
			CopiaInfo retorno = copiaInfo;

			if (util.isNotEmpty(peticionario.getPfApplicationsParameters())) {

				OpcionesPagina opcionesPagina = new OpcionesPagina();

				for (PfApplicationsParameterDTO parameterDto : peticionario.getPfApplicationsParameters()) {
					ApplicationParameterUtil.PARAMETER_INFORME parameter = ApplicationParameterUtil.PARAMETER_INFORME.getValueOf(parameterDto.getPfParameter().getCparameter());

					if (parameter != null) {
						//invocamos al metodo correspondiente de CopiaInfo para setear el valor de la configuracion
						Class cls = CopiaInfo.class;

						Method method = null;
						Object value = parameterDto.getTvalue();
						if (ApplicationParameterUtil.isOpcionesPaginaParameter(parameter.name())) {
							Class[] paramFloat = new Class[1];
							paramFloat[0] = Float.class;
							method = opcionesPagina.getClass().getDeclaredMethod("set" + StringUtils.capitalize(parameter.name()), paramFloat);
							method.invoke(opcionesPagina, Float.valueOf(value.toString()));
						} else if (ApplicationParameterUtil.isBooleanParameter(parameter.name())
								&& Arrays.asList(Constants.LIST_BOOLEAN_VALUES).contains(value)) {
							Class[] paramBoolean = new Class[1];
							paramBoolean[0] = boolean.class;
							method = cls.getDeclaredMethod("set" + StringUtils.capitalize(parameter.name()), paramBoolean);
							value = value.equals(Constants.C_YES);
							method.invoke(copiaInfo, value);
						} else {
							Class[] paramString = new Class[1];
							paramString[0] = String.class;
							method = cls.getDeclaredMethod("set" + StringUtils.capitalize(parameter.name()), paramString);
							method.invoke(copiaInfo, value);
						}
					}
				}
				retorno.setOpcionesPagina(opcionesPagina);
			}

			return retorno;
		} catch (NoSuchMethodException e) {
			log.error("Se ha producido un error en EEUtilUtilFirma al establecer parametros de configuracion" , e);
			throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
		} catch (SecurityException e) {
			log.error("Se ha producido un error en EEUtilUtilFirma al establecer parametros de configuracion" , e);
			throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
		} catch (IllegalAccessException e) {
			log.error("Se ha producido un error en EEUtilUtilFirma al establecer parametros de configuracion" , e);
			throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("Se ha producido un error en EEUtilUtilFirma al establecer parametros de configuracion" , e);
			throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
		} catch (InvocationTargetException e) {
			log.error("Se ha producido un error en EEUtilUtilFirma al establecer parametros de configuracion" , e);
			throw new EeutilException(ERROR_GENERAR_COPIA_FIRMA_EEUTIL_SERVICE + e.getMessage());
		}
	}

	private static CopiaInfo generateDefaultCopiaInfo(byte[] document, String tipoMIME) {
		CopiaInfo retorno = 
				new CopiaInfo();

		ContenidoInfo contenidoInfo = 
				new ContenidoInfo();
		contenidoInfo.setTipoMIME(tipoMIME);
		contenidoInfo.setContenido(document);
		retorno.setContenido(contenidoInfo);

		retorno.setCsv("");
		retorno.setEstamparLogo(false);
		retorno.setExpediente("");
		retorno.setFecha("");
		retorno.setIdAplicacion("");
		retorno.setNif("");
		retorno.setTituloAplicacion("EMPTY");
		retorno.setTituloExpediente("EMPTY");
		retorno.setTituloFecha("EMPTY");
		retorno.setTituloURL("EMPTY");
		retorno.setUrlSede("EMPTY");
		retorno.setTituloCSV("EMPTY");
		retorno.setTituloNif("EMPTY");
		retorno.setLateral("");
		return retorno;
	}

	private String getAmbitoPlantilla (String ambitoDocumento) {
		String result = ambitoDocumento;
		// Para ámbito EXTERNO, se estampará la cadena "GEN" de "Genérico"
		//if ("EXTERNO".equalsIgnoreCase(ambitoDocumento)) {
		result = "GEN";
		//}
		return result;
	}

	private String getUrlValidacion(String ambitoDocumento) {
		PfConfigurationsParameterDTO parameter = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				"CVE.URL.VALIDACION." + ambitoDocumento);
		return parameter.getTvalue();
	}

	private String getUrlQR(String ambitoDocumento, String cve) {
		String url = null;

		PfConfigurationsParameterDTO parameter = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", "cParam",
				"EEUTIL.PARAM.URLQR");

		if (parameter != null && parameter.getTvalue() != null && 
				!parameter.getTvalue().contentEquals("") && 
				!parameter.getTvalue().contentEquals("-")) {
			url = parameter.getTvalue() + cve;
		}

		return url;
	}

	/**
	 * Comprueba si está activado obtener CSV para la configuracion por defecto
	 * @return
	 * 			true si está activado, false en caso contrario.
	 */
	public boolean checkCSV ()   {
		return util.parametroValorS (eeUtilConfigManager.configuracionPorDefecto(), Constants.C_PARAMETER_EEUTIL_CSV_ACTIVE, eeUtilConfigManager);		
	}

	/**
	 * Comprueba si está activa  la visualización de documentos previamente firmados
	 * @param idConf
	 * @return
	 */
	public boolean checkViewPreSign (long idConf)  {
		return util.parametroValorS (idConf, Constants.C_PARAMETER_EEUTIL_VIS_PREFIRMA_ACTIVE, eeUtilConfigManager);		
	}

	/**
	 * Obtiene el csv de una firma
	 * @param sign firma
	 * @param mime mime de la firma
	 * @return csv de la firma
	 * @throws EeutilException
	 */
	public String getCSV (byte[] sign, String mime) throws EeutilException{

		Map<String, String> params = null;

		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		EeUtilService eeutilUtilclient = null;

		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a getCSV de EEUtilUtilFirma");
			//sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILUTILFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EEUtilUtilFirma");
		}

		try {
			eeutilUtilclient = eeUtilUtilClientManager.getEEUtilUtilFirmaClient(params.get(Constants.C_PARAMETER_EEUTIL_UTIL_FIRMA_URL));
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a getCSV de EEUtilUtilFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILUTILFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EEUtilUtilFirma", t);
		}


		ApplicationLogin appInfo = createUtilApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_UTIL_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_UTIL_FIRMA_PASSWORD));

		CSVInfo csvInfo = createCSVInfo(sign, mime);

		String csv = "";
		try {
			csv = eeutilUtilclient.generarCSV(appInfo, csvInfo);
		} catch (Exception e) {
			log.error("Se ha producido un error en la llamada a generarCSV de EEUtilUtilFirma" , e);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_GENERAR_CSV, e);
			throw new EeutilException ("Se ha producido un error en la llamada a generarCSV de EEUtilUtilFirma" + e.getMessage());
		}

		return csv;
	}

	private static CSVInfo createCSVInfo (byte[] sign, String mime) {
		CSVInfo csvInfo = new CSVInfo();
		csvInfo.setContenido(sign);
		csvInfo.setMime(mime);
		return csvInfo;
	}
}
