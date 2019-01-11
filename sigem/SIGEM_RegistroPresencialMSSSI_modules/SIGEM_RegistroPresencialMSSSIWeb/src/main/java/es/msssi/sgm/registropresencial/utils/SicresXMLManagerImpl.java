package es.msssi.sgm.registropresencial.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.ibm.icu.util.Calendar;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;

import es.ieci.tecdoc.fwktd.sir.core.types.IndicadorPruebaEnum;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.UnidadTramitadoraBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ContadorSIRDAO;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;

/**
 * Implementación del manager de mensajes XML conforme a la normativa SICRES
 * 3.0.
 * 
 * @author Iecisa
 * @version $Revision$
 * 
 */
public class SicresXMLManagerImpl {

    /**
     * Logger de la clase.
     */
    private static final Logger logger = LoggerFactory.getLogger(SicresXMLManagerImpl.class);

    private static final String DEFAULT_FILE_EXTENSION = "bin";

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
    private static ApplicationContext appContext;
    private static ContadorSIRDAO contadorSIRDAO;
    private static DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;
    private BookUseCase bookUseCase = null;

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
	if (documentoElectronicoAnexoManager == null) {
	    documentoElectronicoAnexoManager =
		    IsicresManagerProvider.getInstance().getDocumentoElectronicoAnexoManager();
	}
    }
    /**
     * 
     * Map con los valores de los campos del xml que deben estar en formato
     * base64 junto con su expresión xpath de seleccion
     */
    private Map<String, String> base64Fields;

    /**
     * Indicador de prueba.
     */
    private IndicadorPruebaEnum indicadorPrueba = IndicadorPruebaEnum.NORMAL;

    /**
     * Indica si hay que validar, por defecto, los códigos contra el directorio
     * común.
     */
    private boolean defaultValidacionDirectorioComun = false;

    /**
     * Constructor.
     */
    public SicresXMLManagerImpl() {
	super();
	bookUseCase = new BookUseCase();
    }

    public IndicadorPruebaEnum getIndicadorPrueba() {
	return indicadorPrueba;
    }

    public void setIndicadorPrueba(IndicadorPruebaEnum indicadorPrueba) {
	this.indicadorPrueba = indicadorPrueba;
    }

    public boolean isDefaultValidacionDirectorioComun() {
	return defaultValidacionDirectorioComun;
    }

    public void setDefaultValidacionDirectorioComun(boolean defaultValidacionDirectorioComun) {
	this.defaultValidacionDirectorioComun = defaultValidacionDirectorioComun;
    }

    public Map<String, String> getBase64Fields() {
	return base64Fields;
    }

    public void setBase64Fields(Map<String, String> base64Fields) {
	this.base64Fields = base64Fields;
    }

    /**
     * {@inheritDoc}
     * 
     * @param unidadDestino
     * @param usuario
     * @param libro
     * @param useCaseConf
     * 
     * @see es.ieci.tecdoc.fwktd.sir.api.manager.SicresXMLManager#createXMLFicheroIntercambio(es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO,
     *      boolean)
     */
    public String createXMLFicheroIntercambio(InputRegisterBean inputRegisterBean,
	    boolean docsAttached, UnidadTramitacionIntercambioRegistralVO unidadDestino,
	    String usuario, UseCaseConf useCaseConf, ScrRegstate libro) {
	UnidadTramitacionIntercambioRegistralSIRVO origen = null;
	Document doc = DocumentHelper.createDocument();
	doc.setXMLEncoding("UTF-8");

	// Fichero_Intercambio_SICRES_3
	Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");
	try {
	    UnidadTramitadoraBo unidadTramitadoraBo = new UnidadTramitadoraBo();

	    origen = unidadTramitadoraBo.getUnidadTramitadoraOrg(1);
	}
	catch (RPRegistralExchangeException e) {

	}
	String codigoIntercambio = getCodigoIntercambio(origen);
	// De_Origen_o_Remitente
	addDatosOrigen(rootNode, inputRegisterBean, origen);

	// De_Destino
	addDatosDestino(rootNode, unidadDestino);

	// De_Interesado
	addDatosInteresados(rootNode, inputRegisterBean.getInteresados());

	// De_Asunto
	addDatosAsunto(rootNode, inputRegisterBean);

	// De_Anexo
	addDatosAnexos(rootNode, inputRegisterBean, docsAttached, useCaseConf, libro,
		codigoIntercambio);

	// De_Internos_Control
	addDatosControl(rootNode, inputRegisterBean, usuario, origen, codigoIntercambio);

	// De_Formulario_Generico
	addDatosFormularioGenerico(rootNode, inputRegisterBean);

	return doc.asXML();
    }

    /**
     * {@inheritDoc}
     * 
     * @param unidadDestino
     * @param usuario
     * @param libro
     * @param useCaseConf
     * 
     * @see es.ieci.tecdoc.fwktd.sir.api.manager.SicresXMLManager#createXMLFicheroIntercambio(es.ieci.tecdoc.fwktd.sir.core.vo.AsientoRegistralVO,
     *      boolean)
     */
    public String createXMLFicheroIntercambio(OutputRegisterBean outputRegisterBean,
	    boolean docsAttached, UnidadTramitacionIntercambioRegistralVO unidadDestino,
	    String usuario, UseCaseConf useCaseConf, ScrRegstate libro) {
	UnidadTramitacionIntercambioRegistralSIRVO origen = null;
	Document doc = DocumentHelper.createDocument();
	doc.setXMLEncoding("UTF-8");

	// Fichero_Intercambio_SICRES_3
	Element rootNode = doc.addElement("Fichero_Intercambio_SICRES_3");
	try {
	    UnidadTramitadoraBo unidadTramitadoraBo = new UnidadTramitadoraBo();

	    origen = unidadTramitadoraBo.getUnidadTramitadoraOrg(outputRegisterBean.getFld7().getId());
	}
	catch (RPRegistralExchangeException e) {

	}
	String codigoIntercambio = getCodigoIntercambio(origen);
	// De_Origen_o_Remitente
	addDatosOrigen(rootNode, outputRegisterBean, origen);

	// De_Destino
	addDatosDestino(rootNode, unidadDestino);

	// De_Interesado
	addDatosInteresados(rootNode, outputRegisterBean.getInteresados());

	// De_Asunto
	addDatosAsunto(rootNode, outputRegisterBean);

	// De_Anexo
	addDatosAnexos(rootNode, outputRegisterBean, docsAttached, useCaseConf, libro,
		codigoIntercambio);

	// De_Internos_Control
	addDatosControl(rootNode, outputRegisterBean, usuario, origen, codigoIntercambio);

	// De_Formulario_Generico
	addDatosFormularioGenerico(rootNode, outputRegisterBean);

	return doc.asXML();
    }

    private static void addDatosOrigen(Element rootNode, InputRegisterBean inputRegisterBean,
	    UnidadTramitacionIntercambioRegistralSIRVO origen) {

	// De_Origen_o_Remitente
	Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
	Element elem = null;

	if (origen != null) {
	    // Codigo_Entidad_Registral_Origen
	    if (StringUtils.isNotBlank(origen.getCodeEntity())) {
		elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
		elem.addCDATA(origen.getCodeEntity());
	    }

	    // Decodificacion_Entidad_Registral_Origen
	    if (StringUtils.isNotBlank(origen.getNameEntity())) {
		elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
		elem.addCDATA(origen.getNameEntity());
	    }
	}
	// Numero_Registro_Entrada
	if (StringUtils.isNotBlank(inputRegisterBean.getFld1())) {
	    elem = rootElement.addElement("Numero_Registro_Entrada");
	    elem.addCDATA(inputRegisterBean.getFld1());
	}

	// Fecha_Hora_Entrada
	if (inputRegisterBean.getFld2() != null) {
	    elem = rootElement.addElement("Fecha_Hora_Entrada");
	    elem.addCDATA(SDF.format(inputRegisterBean.getFld2()));
	}

	// Codigo_Unidad_Tramitacion_Origen
	if (inputRegisterBean.getFld8() != null) {
	    elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Origen");
	    elem.addCDATA(inputRegisterBean.getFld8().getCode());
	}

	// Decodificacion_Unidad_Tramitacion_Origen
	if (inputRegisterBean.getFld8() != null) {
	    elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Origen");
	    elem.addCDATA(inputRegisterBean.getFld8().getName());
	}

    }

    private static void addDatosOrigen(Element rootNode, OutputRegisterBean outputRegisterBean,
	    UnidadTramitacionIntercambioRegistralSIRVO origen) {

	// De_Origen_o_Remitente
	Element rootElement = rootNode.addElement("De_Origen_o_Remitente");
	Element elem = null;

	if (origen != null) {
	    // Codigo_Entidad_Registral_Origen
	    if (StringUtils.isNotBlank(origen.getCodeEntity())) {
		elem = rootElement.addElement("Codigo_Entidad_Registral_Origen");
		elem.addCDATA(origen.getCodeEntity());
	    }

	    // Decodificacion_Entidad_Registral_Origen
	    if (StringUtils.isNotBlank(origen.getNameEntity())) {
		elem = rootElement.addElement("Decodificacion_Entidad_Registral_Origen");
		elem.addCDATA(origen.getNameEntity());
	    }
	}
	// Numero_Registro_Entrada
	if (StringUtils.isNotBlank(outputRegisterBean.getFld1())) {
	    elem = rootElement.addElement("Numero_Registro_Entrada");
	    elem.addCDATA(outputRegisterBean.getFld1());
	}

	// Fecha_Hora_Entrada
	if (outputRegisterBean.getFld2() != null) {
	    elem = rootElement.addElement("Fecha_Hora_Entrada");
	    elem.addCDATA(SDF.format(outputRegisterBean.getFld2()));
	}

	// Codigo_Unidad_Tramitacion_Origen
	if (outputRegisterBean.getFld8() != null) {
	    elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Origen");
	    elem.addCDATA(outputRegisterBean.getFld8().getCode());
	}

	// Decodificacion_Unidad_Tramitacion_Origen
	if (outputRegisterBean.getFld8() != null) {
	    elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Origen");
	    elem.addCDATA(outputRegisterBean.getFld8().getName());
	}

    }

    private static void addDatosDestino(Element rootNode,
	    UnidadTramitacionIntercambioRegistralVO unidadDestino) {

	// De_Destino
	Element rootElement = rootNode.addElement("De_Destino");
	Element elem = null;

	// Codigo_Entidad_Registral_Destino
	if (StringUtils.isNotBlank(unidadDestino.getCodeEntity())) {
	    elem = rootElement.addElement("Codigo_Entidad_Registral_Destino");
	    elem.addCDATA(unidadDestino.getCodeEntity());
	}

	// Decodificacion_Entidad_Registral_Destino
	if (StringUtils.isNotBlank(unidadDestino.getNameEntity())) {
	    elem = rootElement.addElement("Decodificacion_Entidad_Registral_Destino");
	    elem.addCDATA(unidadDestino.getNameEntity());
	}

	// Codigo_Unidad_Tramitacion_Destino
	if (StringUtils.isNotBlank(unidadDestino.getCodeTramunit())) {
	    elem = rootElement.addElement("Codigo_Unidad_Tramitacion_Destino");
	    elem.addCDATA(unidadDestino.getCodeTramunit());
	}

	// Decodificacion_Unidad_Tramitacion_Destino
	if (StringUtils.isNotBlank(unidadDestino.getNameTramunit())) {
	    elem = rootElement.addElement("Decodificacion_Unidad_Tramitacion_Destino");
	    elem.addCDATA(unidadDestino.getNameTramunit());
	}
    }

    protected static String getBase64Sring(byte[] dato) {
	String result = null;

	result = Base64.encodeBase64String(dato);

	return result;
    }

    private static void addDatosInteresados(Element rootNode, List<Interesado> list) {

	if (CollectionUtils.isNotEmpty(list)) {
	    for (Interesado interesado : list) {
		if (interesado != null) {

		    // De_Interesado
		    Element rootElement = rootNode.addElement("De_Interesado");
		    Element elem = null;

		    // Tipo_Documento_Identificacion_Interesado
		    if (interesado.getTipodoc() != null) {
			elem = rootElement.addElement("Tipo_Documento_Identificacion_Interesado");
			elem.addCDATA(interesado.getTipodoc());
		    }

		    // Documento_Identificacion_Interesado
		    if (StringUtils.isNotBlank(interesado.getDocIndentidad())) {
			elem = rootElement.addElement("Documento_Identificacion_Interesado");
			elem.addCDATA(interesado.getDocIndentidad());
		    }

		    // Razon_Social_Interesado
		    if (StringUtils.isNotBlank(interesado.getRazonSocial())) {
			elem = rootElement.addElement("Razon_Social_Interesado");
			elem.addCDATA(interesado.getRazonSocial());
		    }

		    // Nombre_Interesado
		    if (StringUtils.isNotBlank(interesado.getNombre())) {
			elem = rootElement.addElement("Nombre_Interesado");
			elem.addCDATA(interesado.getNombre());
		    }

		    // Primer_Apellido_Interesado
		    if (StringUtils.isNotBlank(interesado.getPapellido())) {
			elem = rootElement.addElement("Primer_Apellido_Interesado");
			elem.addCDATA(interesado.getPapellido());
		    }

		    // Segundo_Apellido_Interesado
		    if (StringUtils.isNotBlank(interesado.getSapellido())) {
			elem = rootElement.addElement("Segundo_Apellido_Interesado");
			elem.addCDATA(interesado.getSapellido());
		    }

		    if (interesado.getRepresentante() != null) {
			// Tipo_Documento_Identificacion_Representante
			if (interesado.getRepresentante().getTipodoc() != null) {
			    elem =
				    rootElement
					    .addElement("Tipo_Documento_Identificacion_Representante");
			    elem.addCDATA(interesado.getRepresentante().getTipodoc());
			}

			// Documento_Identificacion_Representante
			if (StringUtils
				.isNotBlank(interesado.getRepresentante().getDocIndentidad())) {
			    elem = rootElement.addElement("Documento_Identificacion_Representante");
			    elem.addCDATA(interesado.getRepresentante().getDocIndentidad());
			}

			// Razon_Social_Representante
			if (StringUtils.isNotBlank(interesado.getRepresentante().getRazonSocial())) {
			    elem = rootElement.addElement("Razon_Social_Representante");
			    elem.addCDATA(interesado.getRepresentante().getRazonSocial());
			}

			// Nombre_Representante
			if (StringUtils.isNotBlank(interesado.getRepresentante().getNombre())) {
			    elem = rootElement.addElement("Nombre_Representante");
			    elem.addCDATA(interesado.getRepresentante().getNombre());
			}

			// Primer_Apellido_Representante
			if (StringUtils.isNotBlank(interesado.getRepresentante().getPapellido())) {
			    elem = rootElement.addElement("Primer_Apellido_Representante");
			    elem.addCDATA(interesado.getRepresentante().getPapellido());
			}

			// Segundo_Apellido_Representante
			if (StringUtils.isNotBlank(interesado.getRepresentante().getSapellido())) {
			    elem = rootElement.addElement("Segundo_Apellido_Representante");
			    elem.addCDATA(interesado.getRepresentante().getSapellido());
			}
		    }
		}
	    }
	}
	else {
	    // De_Interesado es elemento obligatoria su presencia aunque sea
	    // vacio y no vengan interesados
	    Element rootElement = rootNode.addElement("De_Interesado");
	}
    }

    private void addDatosAsunto(Element rootNode, InputRegisterBean inputRegisterBean) {

	// De_Asunto
	Element rootElement = rootNode.addElement("De_Asunto");
	Element elem = null;

	// Resumen
	elem = rootElement.addElement("Resumen");
	if (StringUtils.isNotBlank(inputRegisterBean.getFld17())) {
	    elem.addCDATA(inputRegisterBean.getFld17());
	}

	// Codigo_Asunto_Segun_Destino
	if (StringUtils.isNotBlank(inputRegisterBean.getFld16Name())) {
	    elem = rootElement.addElement("Codigo_Asunto_Segun_Destino");
	    elem.addCDATA(inputRegisterBean.getFld16Name());
	}

	// Numero_Expediente
	if (StringUtils.isNotBlank(inputRegisterBean.getFld19())) {
	    elem = rootElement.addElement("Numero_Expediente");
	    elem.addCDATA(inputRegisterBean.getFld19());
	}
    }

    private void addDatosAsunto(Element rootNode, OutputRegisterBean outputRegisterBean) {

	// De_Asunto
	Element rootElement = rootNode.addElement("De_Asunto");
	Element elem = null;

	// Resumen
	elem = rootElement.addElement("Resumen");
	if (StringUtils.isNotBlank(outputRegisterBean.getFld13())) {
	    elem.addCDATA(outputRegisterBean.getFld13());
	}

	// Codigo_Asunto_Segun_Destino
	if (StringUtils.isNotBlank(outputRegisterBean.getFld12Name())) {
	    elem = rootElement.addElement("Codigo_Asunto_Segun_Destino");
	    elem.addCDATA(outputRegisterBean.getFld12Name());
	}

    }

    private void addDatosAnexos(Element rootNode, InputRegisterBean inputRegisterBean,
	    boolean docsAttached, UseCaseConf useCaseConf, ScrRegstate libro,
	    String codigoIntercambio) {
	List<DocumentoElectronicoAnexoVO> listDocuments =
		new ArrayList<DocumentoElectronicoAnexoVO>();

	try {

	    listDocuments =
		    documentoElectronicoAnexoManager.getDocumentosElectronicoAnexoByRegistro(
			    Long.valueOf(String.valueOf(libro.getIdocarchhdr().getId())),
			    Long.valueOf(String.valueOf(inputRegisterBean.getFdrid())));

	    Map<Long, Map<String,String>> docs = generateIdFiles (listDocuments, codigoIntercambio);
	    for (DocumentoElectronicoAnexoVO doc : listDocuments) {
		Map<String, String> datos = docs.get(doc.getId().getId());
		
		// De_Anexo
		Element rootElement = rootNode.addElement("De_Anexo");
		Element elem = null;

		// Nombre_Fichero_Anexado
		if (StringUtils.isNotBlank(doc.getName())) {
		    elem = rootElement.addElement("Nombre_Fichero_Anexado");
		    elem.addCDATA(doc.getName());
		}

		String identificadorFichero = null;
		identificadorFichero = datos.get("code");
		if (identificadorFichero != null){
		    elem = rootElement.addElement("Identificador_Fichero");
		    elem.addCDATA(identificadorFichero);
		}

		elem = rootElement.addElement("Identificador_Fichero");
		elem.addCDATA(identificadorFichero);

		// Validez_Documento
		if (doc.getTipoValidez() != null) {
		    elem = rootElement.addElement("Validez_Documento");
		    elem.addCDATA(String.valueOf(doc.getTipoValidez().getName()));
		}

		// Tipo_Documento
		if (doc.getTipoDocumentoAnexo() != null) {
		    elem = rootElement.addElement("Tipo_Documento");
		    elem.addCDATA(String.valueOf(doc.getTipoDocumentoAnexo().getName()));
		}
		// Certificado
		if (doc.getDatosFirma() != null && doc.getDatosFirma().getCertificado() != null) {
		    elem = rootElement.addElement("Certificado");
		    elem.addCDATA(getBase64Sring(doc.getDatosFirma().getCertificado().getBytes()));
		}

		// Hash
		if (doc.getDatosFirma() != null && doc.getDatosFirma().getHash() != null) {
		    elem = rootElement.addElement("Hash");
		    elem.addCDATA(getBase64Sring(doc.getDatosFirma().getHash().getBytes()));
		}

		// Tipo_MIME
		if (StringUtils.isNotBlank(doc.getMimeType())
			&& (StringUtils.length(doc.getMimeType()) <= 20)) {
		    elem = rootElement.addElement("Tipo_MIME");
		    elem.addCDATA(doc.getMimeType());
		}

		// Anexo
		if (docsAttached) {
		    byte[] content = null;
		    try {
			content =
				bookUseCase.getFile(useCaseConf, libro.getIdocarchhdr().getId(),
					inputRegisterBean.getFdrid(),
					Integer.valueOf(String.valueOf(doc.getId().getId())),
					Integer.valueOf(String.valueOf(doc.getId().getIdPagina())));
		    }
		    catch (ValidationException e) {

		    }
		    catch (BookException e) {

		    }
		    elem = rootElement.addElement("Anexo");
		    elem.addCDATA(getBase64Sring(content));
		}

		if (datos.get("fileSign") != null){
    		elem = rootElement.addElement("Identificador_Documento_Firmado");
    		elem.addCDATA(datos.get("fileSign"));
		} else {
		    elem = rootElement.addElement("Identificador_Documento_Firmado");
			elem.addCDATA(datos.get("code"));
		}


	    }
	}
	catch (Exception e) {

	}
    }

    private void addDatosAnexos(Element rootNode, OutputRegisterBean outputRegisterBean,
	    boolean docsAttached, UseCaseConf useCaseConf, ScrRegstate libro,
	    String codigoIntercambio) {
	List<DocumentoElectronicoAnexoVO> listDocuments =
		new ArrayList<DocumentoElectronicoAnexoVO>();
	try {

	    listDocuments =
		    documentoElectronicoAnexoManager.getDocumentosElectronicoAnexoByRegistro(
			    Long.valueOf(String.valueOf(libro.getIdocarchhdr().getId())),
			    Long.valueOf(String.valueOf(outputRegisterBean.getFdrid())));

	    Map<Long, Map<String,String>> docs = generateIdFiles (listDocuments, codigoIntercambio);
	    for (DocumentoElectronicoAnexoVO doc : listDocuments) {
		Map<String, String> datos = docs.get(doc.getId().getId());
		
		// De_Anexo
		Element rootElement = rootNode.addElement("De_Anexo");
		Element elem = null;

		// Nombre_Fichero_Anexado
		if (StringUtils.isNotBlank(doc.getName())) {
		    elem = rootElement.addElement("Nombre_Fichero_Anexado");
		    elem.addCDATA(doc.getName());
		}

		// Identificador_Fichero
		
		String identificadorFichero = null;
		identificadorFichero = datos.get("code");
		if (identificadorFichero != null){
		    elem = rootElement.addElement("Identificador_Fichero");
		    elem.addCDATA(identificadorFichero);
		}
		// Validez_Documento
		if (doc.getTipoValidez() != null) {
		    elem = rootElement.addElement("Validez_Documento");
		    elem.addCDATA(String.valueOf(doc.getTipoValidez().getName()));
		}

		// Tipo_Documento
		if (doc.getTipoDocumentoAnexo() != null) {
		    elem = rootElement.addElement("Tipo_Documento");
		    elem.addCDATA(String.valueOf(doc.getTipoDocumentoAnexo().getName()));
		}
		// Certificado
		if (doc.getDatosFirma() != null && doc.getDatosFirma().getCertificado() != null) {
		    elem = rootElement.addElement("Certificado");
		    elem.addCDATA(getBase64Sring(doc.getDatosFirma().getCertificado().getBytes()));
		}

		// Hash
		if (doc.getDatosFirma() != null && doc.getDatosFirma().getHash() != null) {
		    elem = rootElement.addElement("Hash");
		    elem.addCDATA(getBase64Sring(doc.getDatosFirma().getHash().getBytes()));
		}

		// Tipo_MIME
		if (StringUtils.isNotBlank(doc.getMimeType())
			&& (StringUtils.length(doc.getMimeType()) <= 20)) {
		    elem = rootElement.addElement("Tipo_MIME");
		    elem.addCDATA(doc.getMimeType());
		}

		// Anexo
		if (docsAttached) {
		    byte[] content = null;
		    try {
			content =
				bookUseCase.getFile(useCaseConf, libro.getIdocarchhdr().getId(),
					outputRegisterBean.getFdrid(),
					Integer.valueOf(String.valueOf(doc.getId().getId())),
					Integer.valueOf(String.valueOf(doc.getId().getIdPagina())));
		    }
		    catch (ValidationException e) {

		    }
		    catch (BookException e) {

		    }
		    elem = rootElement.addElement("Anexo");
		    elem.addCDATA(getBase64Sring(content));
		}

		if (datos.get("fileSign") != null){
        		elem = rootElement.addElement("Identificador_Documento_Firmado");
        		elem.addCDATA(datos.get("fileSign"));
		} else {
		    elem = rootElement.addElement("Identificador_Documento_Firmado");
    			elem.addCDATA(datos.get("code"));
		}
	    }
	}
	catch (Exception e) {

	}
    }

    private Map<Long, Map<String, String>> generateIdFiles(
	    List<DocumentoElectronicoAnexoVO> listDocuments,String codigoIntercambio) {
	Map<Long, Map<String, String>> result = new HashMap<Long, Map<String,String>>();
	Map<String,String> data = null;
	Map<String,String> dataSign = null;
	int secuencia = 0;
	for (DocumentoElectronicoAnexoVO doc: listDocuments){
	    secuencia ++;
	    String code = generateIdentificadorFichero(codigoIntercambio,secuencia, doc);
	    data = new HashMap <String,String>();
	    data.put("code", code);
	    if (doc.getDatosFirma() != null && doc.getDatosFirma().getIdAttachment()
			.equals(doc.getDatosFirma().getIdAttachmentFirmado())){
		data.put("fileSign", code);
	    } else {
		if (result.get(doc.getDatosFirma().getIdAttachmentFirmado()) != null){
		    dataSign = result.get(doc.getDatosFirma().getIdAttachmentFirmado());
		    data.put("fileSign", dataSign.get("code"));
		}else {
		    data.put("code", code);
		}
	    }
	    result.put(doc.getId().getId(), data);
	}
	
	return result;
    }

    /**
     * Metodo que genera identificador de anxso según el patron
     * identificadorIntercambio_01_secuencia.extension donde secuencia es cadena
     * que repesenta secuencia en formato 0001 (leftpading con 0 y máximo de 4
     * caracteres) donde extesion es la extension del anexo
     * 
     * @param identificadorIntercambio
     * @param secuencia
     * @param doc
     * @return
     */
    protected String generateIdentificadorFichero(String identificadorIntercambio, int secuencia,
	    DocumentoElectronicoAnexoVO doc) {

	String result =
		new StringBuffer().append(identificadorIntercambio).append("_01_")
			.append(StringUtils.leftPad(String.valueOf(secuencia), 4, "0")).append(".")
			.append(getExtension(doc.getName())).toString();

	return result;
    }

    private static void addDatosControl(Element rootNode, InputRegisterBean inputRegisterBean,
	    String usuario, UnidadTramitacionIntercambioRegistralSIRVO origen,
	    String codigoIntercambio) {

	// De_Internos_Control
	Element rootElement = rootNode.addElement("De_Internos_Control");
	Element elem = null;

	// Tipo_Transporte_Entrada
	if (inputRegisterBean.getFld14() != null) {
	    elem = rootElement.addElement("Tipo_Transporte_Entrada");
	    elem.addCDATA(inputRegisterBean.getFld14());
	}

	// Numero_Transporte_Entrada
	if (StringUtils.isNotBlank(inputRegisterBean.getFld15())) {
	    elem = rootElement.addElement("Numero_Transporte_Entrada");
	    elem.addCDATA(inputRegisterBean.getFld15());
	}

	// Nombre_Usuario
	if (StringUtils.isNotBlank(usuario)) {
	    elem = rootElement.addElement("Nombre_Usuario");
	    elem.addCDATA(usuario);
	}

	// Identificador_Intercambio
	if (StringUtils.isNotBlank(codigoIntercambio)) {
	    elem = rootElement.addElement("Identificador_Intercambio");
	    elem.addCDATA(codigoIntercambio);
	}

	// Tipo_Anotacion
	elem = rootElement.addElement("Tipo_Anotacion");
	elem.addCDATA("02");

	// Tipo_Registro
	elem = rootElement.addElement("Tipo_Registro");
	elem.addCDATA("0");

	// Documentacion_Fisica
	if (inputRegisterBean.getFld504() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("1");
	}

	// Documentacion_Fisica
	if (inputRegisterBean.getFld505() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("2");
	}
	// Documentacion_Fisica
	if (inputRegisterBean.getFld506() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("3");
	}

	// Observaciones_Apunte
	if (StringUtils.isNotBlank(inputRegisterBean.getFld507())) {
	    elem = rootElement.addElement("Observaciones_Apunte");
	    elem.addCDATA(inputRegisterBean.getFld507());
	}

	// Indicador_Prueba
	elem = rootElement.addElement("Indicador_Prueba");
	elem.addCDATA("0");

	// Codigo_Entidad_Registral_Inicio
	if (StringUtils.isNotBlank(origen.getCodeEntity())) {
	    elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
	    elem.addCDATA(origen.getCodeEntity());
	}

	// Decodificacion_Entidad_Registral_Inicio
	if (StringUtils.isNotBlank(origen.getNameEntity())) {
	    elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
	    elem.addCDATA(origen.getNameEntity());
	}
    }

    private static void addDatosControl(Element rootNode, OutputRegisterBean outputRegisterBean,
	    String usuario, UnidadTramitacionIntercambioRegistralSIRVO origen,
	    String codigoIntercambio) {

	// De_Internos_Control
	Element rootElement = rootNode.addElement("De_Internos_Control");
	Element elem = null;

	// Tipo_Transporte_Entrada
	if (outputRegisterBean.getFld10() != null) {
	    elem = rootElement.addElement("Tipo_Transporte_Entrada");
	    elem.addCDATA(outputRegisterBean.getFld10());
	}

	// Numero_Transporte_Entrada
	if (StringUtils.isNotBlank(outputRegisterBean.getFld11())) {
	    elem = rootElement.addElement("Numero_Transporte_Entrada");
	    elem.addCDATA(outputRegisterBean.getFld11());
	}

	// Nombre_Usuario
	if (StringUtils.isNotBlank(usuario)) {
	    elem = rootElement.addElement("Nombre_Usuario");
	    elem.addCDATA(usuario);
	}

	// Identificador_Intercambio
	if (StringUtils.isNotBlank(codigoIntercambio)) {
	    elem = rootElement.addElement("Identificador_Intercambio");
	    elem.addCDATA(codigoIntercambio);
	}

	// Tipo_Anotacion
	elem = rootElement.addElement("Tipo_Anotacion");
	elem.addCDATA("02");

	// Tipo_Registro
	elem = rootElement.addElement("Tipo_Registro");
	elem.addCDATA("1");

	// Documentacion_Fisica
	if (outputRegisterBean.getFld504() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("1");
	}

	// Documentacion_Fisica
	if (outputRegisterBean.getFld505() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("2");
	}
	// Documentacion_Fisica
	if (outputRegisterBean.getFld506() != null) {
	    elem = rootElement.addElement("Documentacion_Fisica");
	    elem.addCDATA("3");
	}

	// Observaciones_Apunte
	if (StringUtils.isNotBlank(outputRegisterBean.getFld507())) {
	    elem = rootElement.addElement("Observaciones_Apunte");
	    elem.addCDATA(outputRegisterBean.getFld507());
	}

	// Indicador_Prueba
	elem = rootElement.addElement("Indicador_Prueba");
	elem.addCDATA("0");

	// Codigo_Entidad_Registral_Inicio
	if (StringUtils.isNotBlank(origen.getCodeEntity())) {
	    elem = rootElement.addElement("Codigo_Entidad_Registral_Inicio");
	    elem.addCDATA(origen.getCodeEntity());
	}

	// Decodificacion_Entidad_Registral_Inicio
	if (StringUtils.isNotBlank(origen.getNameEntity())) {
	    elem = rootElement.addElement("Decodificacion_Entidad_Registral_Inicio");
	    elem.addCDATA(origen.getNameEntity());
	}
    }

    private static String getCodigoIntercambio(UnidadTramitacionIntercambioRegistralSIRVO origen) {

	String codigo = null;
	try {
	    if (contadorSIRDAO == null) {
		contadorSIRDAO = (ContadorSIRDAO) appContext.getBean("contadorSIRDAO");
	    }

	    int contador = contadorSIRDAO.getContador(origen.getCodeEntity());
	    int year = Calendar.getInstance().get(Calendar.YEAR);
	    String anio = String.valueOf(year).substring(2);
	    codigo =
		    origen.getCodeEntity() + "_" + anio + "_"
			    + StringUtils.leftPad(String.valueOf(contador), 8, "0");

	}
	catch (Exception e) {

	}
	return codigo;
    }

    private static void addDatosFormularioGenerico(Element rootNode,
	    InputRegisterBean inputRegisterBean) {

	// De_Formulario_Generico
	Element rootElement = rootNode.addElement("De_Formulario_Generico");
	Element elem = null;

	// Expone
	elem = rootElement.addElement("Expone");
	if (StringUtils.isNotBlank(inputRegisterBean.getFld501())) {
	    elem.addCDATA(inputRegisterBean.getFld501());
	}

	// Solicita
	elem = rootElement.addElement("Solicita");
	if (StringUtils.isNotBlank(inputRegisterBean.getFld502())) {
	    elem.addCDATA(inputRegisterBean.getFld502());
	}
    }

    private static void addDatosFormularioGenerico(Element rootNode,
	    OutputRegisterBean outputRegisterBean) {

	// De_Formulario_Generico
	Element rootElement = rootNode.addElement("De_Formulario_Generico");
	Element elem = null;

	// Expone
	elem = rootElement.addElement("Expone");
	if (StringUtils.isNotBlank(outputRegisterBean.getFld501())) {
	    elem.addCDATA(outputRegisterBean.getFld501());
	}

	// Solicita
	elem = rootElement.addElement("Solicita");
	if (StringUtils.isNotBlank(outputRegisterBean.getFld502())) {
	    elem.addCDATA(outputRegisterBean.getFld502());
	}
    }

    /**
     * Obtiene la extensión del anexo.
     * 
     * @param anexo
     *            Información del anexo.
     * @return Extensión del anexo.
     */
    protected static String getExtension(String nombreFichero) {

	String extension = null;

	int dotIndex = StringUtils.lastIndexOf(nombreFichero, ".");

	if ((dotIndex > 0) && (nombreFichero.length() > dotIndex)) {
	    extension = nombreFichero.substring(dotIndex + 1);
	}
	else {
	    extension = DEFAULT_FILE_EXTENSION;
	}

	return extension;
    }

}
