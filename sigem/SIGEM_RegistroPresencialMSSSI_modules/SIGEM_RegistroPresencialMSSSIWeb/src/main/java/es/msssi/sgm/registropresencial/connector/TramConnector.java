/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.connector;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxDoch;
import com.ieci.tecdoc.common.isicres.AxPageh;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;

import es.ieci.tecdoc.fwktd.util.file.FileUtils;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.sigm.tramitador.ws.ArrayOfDocumentoExpedientes;
import es.sigm.tramitador.ws.ArrayOfInteresadoExpediente;
import es.sigm.tramitador.ws.BuscadorExpedientes;
import es.sigm.tramitador.ws.DatosComunesExpediente;
import es.sigm.tramitador.ws.DocumentoExpediente;
import es.sigm.tramitador.ws.InteresadoExpediente;
import es.sigm.tramitador.ws.PeticionConsultaExpedientes;
import es.sigm.tramitador.ws.PeticionIniciarExpediente;
import es.sigm.tramitador.ws.RespuestaConsultaExpedientes;
import es.sigm.tramitador.ws.RespuestaIniciarExpediente;
import es.sigm.tramitador.ws.facade.SigmTramitadorService;

public class TramConnector {
    private static Logger log = Logger.getLogger(TramConnector.class.getName());
    private String wsUser = null;
    private String wsPass = null;
    private String wsProcedure = null;
    private BookUseCase bookUseCase = null;

    
    /**
     * 
     * @param bean
     * @param book
     * @param useCaseConf
     * @return
     * @throws RPGenericException
     */
    public boolean iniciarExpedient(InputRegisterBean bean, UseCaseConf useCaseConf,
	    ScrRegstate book) throws RPGenericException {
	SigmTramitadorService sigmTramitadorService = null;
	PeticionIniciarExpediente request = null;
	RespuestaIniciarExpediente result = null;
	try {
	    sigmTramitadorService = new SigmTramitadorService();
	    request = mappingRegistertoExpedient(bean, useCaseConf, book);
	    result = sigmTramitadorService.iniciarExpediente(request);
	}
	catch (Exception e) {
	    log.error("Se ha producido un error en la creación del expediente", e);
	    throw new RPGenericException(RPGenericErrorCode.CREATE_EXPEDIENT_ERROR_MESSAGE, e);
	}
	return result.isIniciarExpedienteReturn();
    }
    
    /**
     * 
     * @param nreg
     * @param codPcd
     * @param useCaseConf
     * @return
     * @throws RPGenericException
     */
    public RespuestaConsultaExpedientes consultarExpediente(InputRegisterBean bean, String codPcd, UseCaseConf useCaseConf) throws RPGenericException {
	SigmTramitadorService sigmTramitadorService = null;
	PeticionConsultaExpedientes request = null;
	RespuestaConsultaExpedientes result = null;
	try {
	    sigmTramitadorService = new SigmTramitadorService();
	    request = mappingConsultatoExpedient(bean, codPcd, useCaseConf);
	    result = sigmTramitadorService.consultaExpedientes(request);
	}
	catch (Exception e) {
	    log.error("Se ha producido un error en la consulta del expediente", e);
	    throw new RPGenericException(RPGenericErrorCode.CONSULT_EXPEDIENT_ERROR_MESSAGE, e);
	}
	return result;
    }
    
    /**
     * 
     * @param bean
     * @param book
     * @param useCaseConf
     * @return
     * @throws RPGenericException
     */
    public RespuestaIniciarExpediente iniciarExpedientWithResponse(InputRegisterBean bean, UseCaseConf useCaseConf,
	    ScrRegstate book) throws RPGenericException {
	SigmTramitadorService sigmTramitadorService = null;
	PeticionIniciarExpediente request = null;
	RespuestaIniciarExpediente result = null;
	try {
	    sigmTramitadorService = new SigmTramitadorService();
	    request = mappingRegistertoExpedient(bean, useCaseConf, book);
	    result = sigmTramitadorService.iniciarExpediente(request);
	}
	catch (Exception e) {
	    log.error("Se ha producido un error en la creación del expediente", e);
	    throw new RPGenericException(RPGenericErrorCode.CREATE_EXPEDIENT_ERROR_MESSAGE, e);
	}
	return result;
    }

    /**
     * 
     * @param codPcd
     * @param nreg
     * @param useCaseConf
     * @return
     */
    private PeticionConsultaExpedientes mappingConsultatoExpedient(InputRegisterBean bean, String codPcd, UseCaseConf useCaseConf) {
    	
    	PeticionConsultaExpedientes request = new PeticionConsultaExpedientes();
    	request.setIdEntidad(useCaseConf.getEntidadId());
    	request.setUserName(useCaseConf.getUserName());
    	request.setUserPassword(useCaseConf.getPassword());
    	BuscadorExpedientes buscadorExp = new BuscadorExpedientes();
    	String docIdent = bean.getInteresados().get(0).getDocIndentidad();
    	
    	// Si tiene representante, recuperamos el documento de Indentificación del representante 
    	// y no del interesdo para poder consultar el expediente creado
    	if (bean.getInteresados().get(0).getRepresentante() != null
			    && (StringUtils.isNotBlank(bean.getInteresados().get(0).getRepresentante().getDocIndentidad()))) 
		{
    		docIdent = bean.getInteresados().get(0).getRepresentante().getDocIndentidad();
		}
    	
    	log.info("Datos de consulta: " + codPcd + "-" + docIdent);
    	buscadorExp.setCodigoProcedimiento(codPcd);
    	buscadorExp.setNumeroIdentificacion(docIdent);
    	request.setBuscadorExpedientes(buscadorExp);
    	
    	return request;
    }
    
    private PeticionIniciarExpediente mappingRegistertoExpedient(InputRegisterBean bean,
	    UseCaseConf useCaseConf, ScrRegstate book) {
	PeticionIniciarExpediente request = new PeticionIniciarExpediente();
	request.setIdEntidad(useCaseConf.getEntidadId());

	request.setUserName(useCaseConf.getUserName());

	request.setUserPassword(useCaseConf.getPassword());

	// DATOS COMUNES
	DatosComunesExpediente datosComunes = new DatosComunesExpediente();
	if(bean.getFld2() != null) {
		SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		datosComunes.setFechaRegistro(date.format(bean.getFld2()));
	}
	if (bean.getFld8() != null) {
	    datosComunes.setIdOrganismo(bean.getFld8().getCode());
	}
	
	datosComunes.setNumeroRegistro(bean.getFld1());
	datosComunes.setTipoAsunto(wsProcedure);

	ArrayOfInteresadoExpediente interesados = null;
	interesados = mappingInterest(bean.getInteresados());

	datosComunes.setInteresados(interesados);
	request.setDatosComunes(datosComunes);

	// DOCUMENTOS
	ArrayOfDocumentoExpedientes docs = null;
	docs = mappingDocument(bean, useCaseConf, book);
	request.setDocumentos(docs);

	// DATOS ESPECIFICOS
	request.setDatosEspecificos(mappingDatosEspecificos(bean));
	return request;
    }

    private ArrayOfDocumentoExpedientes mappingDocument(InputRegisterBean bean,
	    UseCaseConf useCaseConf, ScrRegstate book) {
	ArrayOfDocumentoExpedientes docs = null;

	try {
	    bookUseCase = new BookUseCase();
	    List<Axdoch> listDocument = getRegisterAttachedDocuments(bean, useCaseConf, book);
	    if (listDocument != null && !listDocument.isEmpty()) {
		docs = new ArrayOfDocumentoExpedientes();
		DocumentoExpediente documentoExpediente = null;
		for (Axdoch doc : listDocument) {
		    if (doc.getPages() != null && !doc.getPages().isEmpty()) 
		    {
				for (Axpageh page : (List<Axpageh>) doc.getPages()) {
				    try 
				    {   	
				    	// Revisar
				    	//if(page.getPageSigned() != null) {
					    	
							byte[] content =
								bookUseCase.getFile(useCaseConf, book.getId(),
									bean.getFdrid(), page.getDocId(), page.getId());
							documentoExpediente = new DocumentoExpediente();
							documentoExpediente.setCode("Anexo a la Solicitud");
							documentoExpediente.setContent(content);
							documentoExpediente.setExtension(page.getLoc());
							documentoExpediente.setLenght(content.length);
							documentoExpediente.setName(page.getName());
							docs.getDocumentosExpediente().add(documentoExpediente);
				    	//}
				    }
				    catch (ValidationException e) {
				    }
				    catch (BookException e) {
				    }
				}
		    }
		}
	    }
	}
	catch (Exception e) {

	}
	return docs;
    }

    /**
     * Obtiene los documentos asociados a un registro.
     */
    public List<Axdoch> getRegisterAttachedDocuments(InputRegisterBean bean,
	    UseCaseConf useCaseConf, ScrRegstate book) {
	boolean openFolderDtr = false;
	List<Axdoch> listDocuments = null;
	RegisterDocumentsBo registerDocumentsBo = null;
	if (registerDocumentsBo == null) {
	    registerDocumentsBo = new RegisterDocumentsBo();
	}
	try {
	    listDocuments =
		    registerDocumentsBo.getDocumentsBasicInfo(useCaseConf, book.getId(), bean.getFdrid(),
			    openFolderDtr);
	}
	catch (RPRegisterException rPRegisterException) {
	}
	return listDocuments;
    }

    private ArrayOfInteresadoExpediente mappingInterest(List<Interesado> listInt) {
	ArrayOfInteresadoExpediente result = null;
	boolean isFirst = true;
	if (listInt != null) {
	    result = new ArrayOfInteresadoExpediente();
	    if (!listInt.isEmpty()) {
		InteresadoExpediente interesadoExp = null;
		for (Interesado inter : listInt) {
			interesadoExp = new InteresadoExpediente();
		    if (isFirst) {
			interesadoExp.setIndPrincipal("S");
			isFirst = false;
			log.info("Datos de creación de terceros: " + inter.getDocIndentidad());
		    }
		    if ("P".equals(inter.getTipo())) {
			interesadoExp.setName((inter.getNombre() + " " + inter.getPapellido())
				.trim());
			if (inter.getSapellido() != null) {
			    interesadoExp.setName(interesadoExp.getName() + " "
				    + inter.getSapellido());
			}
		    }
		    else {
			interesadoExp.setName(inter.getRazonSocial());
		    }
		    interesadoExp.setThirdPartyId(null);
		    interesadoExp.setNifcif(inter.getDocIndentidad());
		    result.getInteresadosExpediente().add(interesadoExp);
		}
	    }
	}
	return result;
    }

    private String mappingDatosEspecificos(InputRegisterBean bean) {
	String specificDataXML =
		"<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>" + "<datos_especificos>";
	if (bean.getFld502() != null) {
	    specificDataXML += "<Solicita>" + bean.getFld502() + "</Solicita>";
	}
	else {
	    specificDataXML += "<Solicita></Solicita>";
	}
	if (bean.getFld501() != null) {
	    specificDataXML += "<Expone>" + bean.getFld501() + "</Expone>";
	}
	else {
	    specificDataXML += "<Expone></Expone>";
	}
	if (bean.getFld17() != null) {
	    specificDataXML += "<Asunto>" + bean.getFld17() + "</Asunto>";
	}
	else {
	    specificDataXML += "<Asunto></Asunto>";
	}
	if (bean.getFld8() != null) {
	    specificDataXML +=
		    "<UID_Dep_Destino>" + bean.getFld8().getCode() + "</UID_Dep_Destino>";
	    specificDataXML +=
		    "<Descripcion_UID_Dep_Destino>" + bean.getFld8().getName()
			    + "</Descripcion_UID_Dep_Destino>";
	}

	specificDataXML += "<Oficina>" + bean.getFld5().getName() + "</Oficina>";
	specificDataXML +=
		"<Descripcion_Oficina>" + bean.getFld5().getName() + "</Descripcion_Oficina>";

	specificDataXML += mappingInterestXML(bean.getInteresados());

	specificDataXML += "</datos_especificos>";

	return specificDataXML;
    }

    private String mappingInterestXML(List<Interesado> listInt) {
	String result = "";
	if (listInt != null) {
	    if (!listInt.isEmpty()) {
		result = "<Solicitantes>";
		int cont = 1;
		String nombre = "";
		for (Interesado inter : listInt) {
		    result += "<Solicitante>";
		    result += "<Numero>" + cont + "</Numero>";
		    if (inter.getRepresentante() == null
			    || (inter.getRepresentante() != null && inter.getRepresentante()
				    .getIdTercero() == null)) 
		    {
		    	result += "<Tipo_Presentacion>solicitante</Tipo_Presentacion>";
		    }
		    else 
		    {
		    	result += "<Tipo_Presentacion>representante</Tipo_Presentacion>";
		    }
		    if ("P".equals(inter.getTipo())) 
		    {
		    	result += "<Tipo_Solicitante>fisica</Tipo_Solicitante>";
		    	result +=
		    			"<Nombre_Solicitante>" + inter.getNombre()
		    			+ "</Nombre_Solicitante>";
				result +=
					"<Apellido1_Solicitante>" + inter.getPapellido()
						+ "</Apellido1_Solicitante>";
				nombre = inter.getNombre() + " " + inter.getPapellido();
				if (inter.getSapellido() != null) {
				    result +=
					    "<Apellido2_Solicitante>" + inter.getSapellido()
						    + "</Apellido2_Solicitante>";
				    nombre += " " + inter.getSapellido();
				}
				else {
				    result += "<Apellido2_Solicitante></Apellido2_Solicitante>";
				}
				result +=
					"<Nombre_Apellidos_Solicitante>" + (nombre).trim()
						+ "</Nombre_Apellidos_Solicitante>";
				nombre = "";
				if (inter.getDocIndentidad() != null) {
				    result += "<Tipo_Identificacion>1</Tipo_Identificacion>";
				    result +=
					    "<Documento_Identidad_Solicitante>" + inter.getDocIndentidad()
						    + "</Documento_Identidad_Solicitante>";
				}
		    }
		    else 
		    {
				result += "<Tipo_Solicitante>juridica</Tipo_Solicitante>";
				result +=
					"<Razon_Social_Solicitante>" + inter.getRazonSocial()
						+ "</Razon_Social_Solicitante>";
				if (inter.getDocIndentidad() != null) {
				    result += "<Tipo_Identificacion></Tipo_Identificacion>";
				    result +=
					    "<Cif_Solicitante>" + inter.getDocIndentidad()
						    + "</Cif_Solicitante>";
				}
		    }
		    if (inter.getRepresentante() != null
			    && inter.getRepresentante().getIdTercero() != null) 
		    {
				if ("P".equals(inter.getRepresentante().getTipo())) 
				{
				    result +=
					    "<Nombre_Representante>" + inter.getRepresentante().getNombre()
						    + "</Nombre_Representante>";
				    result +=
					    "<Apellido1_Representante>"
						    + inter.getRepresentante().getPapellido()
						    + "</Apellido1_Representante>";
				    result +=
					    "<Apellido2_Representante>"
						    + inter.getRepresentante().getSapellido()
						    + "</Apellido2_Representante>";
				    result +=
					    "<Nombre_Apellidos_Representante>"
						    + (inter.getRepresentante().getNombre() + " "
							    + inter.getRepresentante().getPapellido() + " " + inter
							    .getRepresentante().getSapellido()).trim()
						    + "</Nombre_Apellidos_Representante>";
				    if (inter.getRepresentante().getDocIndentidad() != null) {
						result +=
							"<Documento_Identidad_Representante>"
								+ inter.getRepresentante().getDocIndentidad()
								+ "</Documento_Identidad_Representante>";
				    }
				}
				else 
				{
				    result +=
					    "<Razon_Social_Representante>"
						    + inter.getRepresentante().getRazonSocial()
						    + "</Razon_Social_Representante>";
				    if (inter.getRepresentante().getDocIndentidad() != null) 
				    {
						result +=
							"<Cif_Representante>"
								+ inter.getRepresentante().getDocIndentidad()
								+ "</Cif_Representante>";
				    }
				}
		    }
		    result += "</Solicitante>";
		    cont++;
		}
		result += "</Solicitantes>";
	    }
	}
	return result;
    }

    /**
     * @return the wsUser
     */
    public String getWsUser() {
	return wsUser;
    }

    /**
     * @param wsUser
     *            the wsUser to set
     */
    public void setWsUser(String wsUser) {
	this.wsUser = wsUser;
    }

    /**
     * @return the wsPass
     */
    public String getWsPass() {
	return wsPass;
    }

    /**
     * @param wsPass
     *            the wsPass to set
     */
    public void setWsPass(String wsPass) {
	this.wsPass = wsPass;
    }

    /**
     * @return the wsProcedure
     */
    public String getWsProcedure() {
	return wsProcedure;
    }

    /**
     * @param wsProcedure
     *            the wsProcedure to set
     */
    public void setWsProcedure(String wsProcedure) {
	this.wsProcedure = wsProcedure;
    }

}
