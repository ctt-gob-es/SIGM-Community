package es.dipucr.sicres30.utils;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.registro.DocumentInfo;
import ieci.tecdoc.sgm.core.services.registro.FieldInfo;
import ieci.tecdoc.sgm.core.services.registro.PersonInfo;
import ieci.tecdoc.sgm.core.services.registro.RegisterInfo;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.utils.ISUnitsValidator;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.lowagie.text.pdf.codec.Base64;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.diccionarios.EstadosElaboracion;
import es.dipucr.metadatos.diccionarios.OrigenCiudadanoAdministracion;
import es.dipucr.metadatos.diccionarios.TiposDocumentales;
import es.dipucr.metadatos.diccionarios.TiposFirmas;
import es.dipucr.orve.beans.ORVEBeanInterface;
import es.dipucr.orve.sw.constantes.ConstantesWSExportacion;
import es.dipucr.sicres30.FicheroIntercambioSICRES3;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeAnexo;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeAsunto;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeDestino;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeFormularioGenerico;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeInteresado;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeInternosControl;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeOrigenORemitente;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;

public class SICRES30Utils {
    
    public static final Logger LOGGER = Logger.getLogger(SICRES30Utils.class);
    
    public static final String FORMATO_FECHA_REGISTRO_SIGEM = "dd-MM-yyyy";
    
    public static final String MENSAJE_ORIGEN = "origen";
    public static final String MENSAJE_DESTINO = "destino";
    
    public static final String MENSAJE_ERROR_COMPROBAR_UNIDAD = "ERROR al comprobar la uniad de ";
    public static final String MENSAJE_NO_EXISTE_SCR_ORGS = ", no existe la unidad (scr_orgs): ";
    public static final String MENSAJE_EN_LA_ENTIDAD = " en la entidad: ";
    
    /**
     * Estado de registro presencial: COMPLETO
     */
    public static final String ESTADO_REGISTRO_PRESENCIAL_COMPLEO = "0";
    
    public static final String NO_ES_PARA_INTERCAMBIO_REGISTRAL = "0";
    public static final String ES_PARA_INTERCAMBIO_REGISTRAL = "1";

    
    public static FicheroIntercambioSICRES3 getRegistroFormatoSICRES3(String registro) {
        FicheroIntercambioSICRES3 objRegistroSICRES3 = null;
        
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(registro);
            objRegistroSICRES3 = (FicheroIntercambioSICRES3) jaxbUnmarshaller.unmarshal(reader);
            
        } catch (JAXBException e){            
            LOGGER.error("ERROR al unmarshalear el registro. " + e.getMessage(), e);
        }
        
        return objRegistroSICRES3;
    }
    
    public static String insertaRegistroEnSIGEM(ORVEBeanInterface orveBean, FicheroIntercambioSICRES3 registroFormatoSICRES3) {
        
        String nregSIGEM = null;
        
        try{
            ServicioRegistro servicioRegistro = LocalizadorServicios.getServicioRegistro();

            UserInfo user = orveBean.getUserInfo();
            Integer bookId = orveBean.getBookId();
            
            PersonInfo[] inter = SICRES30Utils.getInteresados(registroFormatoSICRES3);
            FieldInfo[] atts = SICRES30Utils.getFieldInfo(orveBean, registroFormatoSICRES3, inter);
            DocumentInfo[] documents = SICRES30Utils.getDocumentsInfo(orveBean, registroFormatoSICRES3);
                    
            RegisterInfo registerInfo = servicioRegistro.createFolder(user, bookId, atts, inter, documents, orveBean.getEntidad());
            
            if(null != registerInfo){
                nregSIGEM = registerInfo.getNumber();
            }
            
        } catch(SigemException e){
            LOGGER.error("ERROR al recuperar el SW del registro. " + e.getMessage(), e);
        }
        
        return nregSIGEM;
    }
    
    public static DocumentInfo[] getDocumentsInfo(ORVEBeanInterface orveBean, FicheroIntercambioSICRES3 registroFormatoSICRES3) {
        DocumentInfo[] documentsInfo = null;
        
        Map<String, String> mapDocumentos = new HashMap<String, String>();
        
        List<DeAnexo> listaAnexos = registroFormatoSICRES3.getDeAnexo();
        
        Date dFechaOrveRegistroOriginal = getFechaRegistroOriginal(registroFormatoSICRES3);
        
        if(null != listaAnexos && !listaAnexos.isEmpty()){
            documentsInfo = new DocumentInfo[listaAnexos.size()];
            
            for(DeAnexo anexo : listaAnexos){
                String identificadorFichero = anexo.getIdentificadorFichero();
                String identificadorDocumentoFirmado = anexo.getIdentificadorDocumentoFirmado();
                String nombreFicheroAnexado = anexo.getNombreFicheroAnexado();
                
                if(StringUtils.isEmpty(identificadorDocumentoFirmado)){
                    mapDocumentos.put(identificadorFichero, nombreFicheroAnexado);
                }
            }
            
            for(int i = 0; i < listaAnexos.size(); i++){
                
                DeAnexo anexo = listaAnexos.get(i);
                
                DocumentInfo documento = new DocumentInfo();
                
                String identificadorDocumentoFirmado = anexo.getIdentificadorDocumentoFirmado();
                String nombreFicheroAnexado = anexo.getNombreFicheroAnexado();
                
                if(StringUtils.isNotEmpty(identificadorDocumentoFirmado) && null != mapDocumentos.get(identificadorDocumentoFirmado)){
                    documento.setDocumentName(mapDocumentos.get(identificadorDocumentoFirmado));                    
                    documento.setDocumentContent(Base64.decode(new String(anexo.getAnexo())));
                    
                } else {
                    documento.setDocumentName(nombreFicheroAnexado);
                    documento.setDocumentContent(anexo.getAnexo());
                }
                
                
                documento.setExtension(FileUtils.getExtensionByNombreDoc(documento.getDocumentName()));
                
                documento.setFileName(anexo.getNombreFicheroAnexado());
                documento.setPageName(anexo.getNombreFicheroAnexado());
                
                MetadatosDocumentoBean metadatosDocumento = getMetadatos(dFechaOrveRegistroOriginal, anexo);
                
                documento.setMetadatosDocumento((Object)metadatosDocumento);
                
                documentsInfo[i] = documento;
            }
        }
        
        return documentsInfo;
    }
    
    public static Date getFechaRegistroOriginal( FicheroIntercambioSICRES3 registroFormatoSICRES3) {
        Date dFechaOrveRegistroOriginal = null;
        
        try {
            SimpleDateFormat formateadorFechaORVERecepcion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_RECEPCION);
            dFechaOrveRegistroOriginal = formateadorFechaORVERecepcion.parse(registroFormatoSICRES3.getDeOrigenORemitente().getFechaHoraEntrada());
        
        } catch (ParseException e) {
            LOGGER.error("ERROR al recuperar la fecha del registro original. registroFormatoSICRES3: " + registroFormatoSICRES3 + ". " + e.getMessage(), e);
        }
        
        return dFechaOrveRegistroOriginal;
    }

    public static MetadatosDocumentoBean getMetadatos(Date dFechaOrveRegistroOriginal, DeAnexo anexo) {
        MetadatosDocumentoBean metadatosDocumento = new MetadatosDocumentoBean();
        
        metadatosDocumento.setVersionNTI(MetadatosDocumentoBean.VERSION_NTI_VALOR);        
        metadatosDocumento.setFechaCaptura(dFechaOrveRegistroOriginal);
        metadatosDocumento.setOrigen(OrigenCiudadanoAdministracion.ADMINISTRACION);
        metadatosDocumento.setNombreFormato(FilenameUtils.getExtension(anexo.getNombreFicheroAnexado()));        
                        
        if(null != anexo.getFirmaDocumento() && 0 < anexo.getFirmaDocumento().length){
            metadatosDocumento.setEstadoElaboracion(SICRES30Utils.getEstadoElaboracionByValidezORVE(anexo.getValidezDocumento()));
            metadatosDocumento.setTipoDocumental(TiposDocumentales.SOLICITUD);                    
            metadatosDocumento.setTipoFirma(TiposFirmas.TIPO_FIRMA_CADES_DETACHED);                    
            metadatosDocumento.setCsv(new String(anexo.getFirmaDocumento()));
            
        } else {
            metadatosDocumento.setEstadoElaboracion(EstadosElaboracion.ORIGINAL);
            metadatosDocumento.setTipoDocumental("");
        }
        
        return metadatosDocumento;
    }

    public static PersonInfo[] getInteresados(FicheroIntercambioSICRES3 registroFormatoSICRES3) {

        PersonInfo[] interesados = null;
        List<DeInteresado> listaInteresado = registroFormatoSICRES3.getDeInteresado();
        
        if(null != listaInteresado && !listaInteresado.isEmpty()){
            interesados = new PersonInfo[listaInteresado.size()];
            
            for(int i = 0; i< listaInteresado.size(); i++){
                DeInteresado interesado = listaInteresado.get(i);
                
                PersonInfo personInfo = new PersonInfo();
                                
                String nombreRazonSocialPerson = getNombreRazonSocialPerson(interesado);
                
                personInfo.setPersonName(nombreRazonSocialPerson);
                
                if(StringUtils.isNotEmpty(interesado.getDireccionInteresado())){
                    personInfo.setDirection(interesado.getDireccionInteresado());
                }
                
                interesados[i] = personInfo;
            }
        }
        
        return interesados;
    }
    
    public static FieldInfo[] getFieldInfo(ORVEBeanInterface orveBean, FicheroIntercambioSICRES3 registroFormatoSICRES3, PersonInfo[] personInfos) {
        
        List<FieldInfo> listaAtributos = new ArrayList<FieldInfo>();

        SICRES30Utils.getCamposComunes(orveBean, listaAtributos);
        SICRES30Utils.getCamposDestino(orveBean, registroFormatoSICRES3.getDeDestino(), listaAtributos);
        SICRES30Utils.getCamposOrigenORemitente(orveBean, registroFormatoSICRES3.getDeOrigenORemitente(), listaAtributos);
        SICRES30Utils.getCamposAsunto(registroFormatoSICRES3.getDeAsunto(), listaAtributos);
        SICRES30Utils.getCamposFormularioGenerico(registroFormatoSICRES3.getDeFormularioGenerico(), listaAtributos);
        SICRES30Utils.getCamposInternosControl(registroFormatoSICRES3.getDeInternosControl(), listaAtributos);            
        SICRES30Utils.getCamposInteresado(personInfos, listaAtributos);
        
        FieldInfo[] arrayFieldInfo = new FieldInfo[listaAtributos.size()];
        return listaAtributos.toArray(arrayFieldInfo);
    }

    public static void getCamposComunes(ORVEBeanInterface orveBean, List<FieldInfo> listaAtributos) {
        //Usuario
        listaAtributos.add(getFieldInfo("3", orveBean.getUserInfo().getUserName()));
        //Oficina de registro 
        listaAtributos.add(getFieldInfo("5", orveBean.getCodOficina())); 
        //Estado completo
        listaAtributos.add(getFieldInfo("6", SICRES30Utils.ESTADO_REGISTRO_PRESENCIAL_COMPLEO)); 
        // Tipo de Transporte
        listaAtributos.add(getFieldInfo("14", orveBean.getTipoTransporte())); 
        //Tipo de asunto
        listaAtributos.add(getFieldInfo("16", orveBean.getTipoAsunto())); 
        //Solicita
        listaAtributos.add(getFieldInfo("503", SICRES30Utils.ES_PARA_INTERCAMBIO_REGISTRAL));  
    }
    
    public static void getCamposInteresado(PersonInfo[] personInfos, List<FieldInfo> listaAtributos) {
        if (!ArrayUtils.isEmpty(personInfos)) {                
            // Nombre del interviniente principal
            String personName = personInfos[0].getPersonName();                
            
            if(StringUtils.isNotEmpty(personName)){
                listaAtributos.add(getFieldInfo("9", personName));
            }
        }
    }

    public static void getCamposInternosControl(DeInternosControl internosControl, List<FieldInfo> listaAtributos) {

        if(null != internosControl){
            if(StringUtils.isNotEmpty(internosControl.getObservacionesApunte())){
                listaAtributos.add(getFieldInfo("18", internosControl.getObservacionesApunte())); //Comentario
            }
            
            if(StringUtils.isNotEmpty(internosControl.getDocumentacionFisica())){
                if("1".equalsIgnoreCase(internosControl.getDocumentacionFisica())){
                    // Acompaña documentación física (u otros soportes) requerida
                    listaAtributos.add(getFieldInfo("504", "1")); 
                
                } else if("2".equalsIgnoreCase(internosControl.getDocumentacionFisica())){
                    // Acompaña documentación física (u otros soportes) complementaria
                    listaAtributos.add(getFieldInfo("505", "1")); 
                
                } else {
                    // No acompaña documentación física ni otros soportes
                    listaAtributos.add(getFieldInfo("506", "1")); 
                }
            }
        }        
    }

    public static void getCamposFormularioGenerico(DeFormularioGenerico formularioGenerico, List<FieldInfo> listaAtributos) {
        
        if(null!= formularioGenerico){
            if(StringUtils.isNotEmpty(formularioGenerico.getExpone())){
                //Expone
                listaAtributos.add(getFieldInfo("501", formularioGenerico.getExpone()));  
            }
            
            if(StringUtils.isNotEmpty(formularioGenerico.getSolicita())){
                //Solicita
                listaAtributos.add(getFieldInfo("502", formularioGenerico.getSolicita()));  
            }
        }        
    }

    public static void getCamposAsunto(DeAsunto asunto, List<FieldInfo> listaAtributos) {
        
        if(null != asunto && StringUtils.isNotEmpty(asunto.getResumen())){
            listaAtributos.add(getFieldInfo("17", asunto.getResumen())); //Asunto
        }
    }

    public static void getCamposOrigenORemitente(ORVEBeanInterface orveBean, DeOrigenORemitente origenORemitente, List<FieldInfo> listaAtributos) {

        if(null != origenORemitente){

            if(StringUtils.isNotEmpty(origenORemitente.getCodigoUnidadTramitacionOrigen())){            
                
                Session session;
                try {
                    session = HibernateUtil.currentSession(orveBean.getEntidad().getIdentificador());
                    ScrOrg scrOrg = ISUnitsValidator.getUnit(session, origenORemitente.getCodigoUnidadTramitacionOrigen(), true, null);
                    
                    if(null != scrOrg){
                        //Origen
                        listaAtributos.add(getFieldInfo("7", origenORemitente.getCodigoUnidadTramitacionOrigen()));
                    }
                    
                } catch (HibernateException e) {
                    LOGGER.error(MENSAJE_ERROR_COMPROBAR_UNIDAD + MENSAJE_ORIGEN + ". " + e.getMessage(), e);
                    
                } catch (ValidationException e) {
                    LOGGER.error(MENSAJE_ERROR_COMPROBAR_UNIDAD + MENSAJE_ORIGEN + MENSAJE_NO_EXISTE_SCR_ORGS + origenORemitente.getCodigoUnidadTramitacionOrigen() + MENSAJE_EN_LA_ENTIDAD + orveBean.getEntidad().getIdentificador() + ". " + e.getMessage(), e); 
                }
            } 
            
            if(StringUtils.isNotEmpty(origenORemitente.getNumeroRegistroEntrada())){
                //Número de registro de entrada original
                listaAtributos.add(getFieldInfo("10", origenORemitente.getNumeroRegistroEntrada())); 
            }
            
            String fechaRegistroOriginal = "";
            
            try {
                SimpleDateFormat formateadorFechaORVERecepcion = new SimpleDateFormat(ConstantesWSExportacion.FORMATO_FECHA_ORVE_RECEPCION);
                Date dFechaOrveRegistroOriginal = formateadorFechaORVERecepcion.parse(origenORemitente.getFechaHoraEntrada());
            
                SimpleDateFormat formateadorFechaRegistroSigem = new SimpleDateFormat(SICRES30Utils.FORMATO_FECHA_REGISTRO_SIGEM);
                fechaRegistroOriginal = formateadorFechaRegistroSigem.format(dFechaOrveRegistroOriginal);
                
                //dd-MM-yyyy Fecha de registro de entrada original
                listaAtributos.add(getFieldInfo("12", fechaRegistroOriginal));    
                
            } catch (ParseException e) {
                LOGGER.error("ERROR al formatear la fecha: " + origenORemitente.getFechaHoraEntrada() + ". " + e.getMessage(), e);
            }
        }
    }

    public static void getCamposDestino(ORVEBeanInterface orveBean, DeDestino destino, List<FieldInfo> listaAtributos) {

        if(null != destino && StringUtils.isNotEmpty(destino.getCodigoUnidadTramitacionDestino())){

            Session session;
            try {
                session = HibernateUtil.currentSession(orveBean.getEntidad().getIdentificador());
                ScrOrg scrOrg = ISUnitsValidator.getUnit(session, destino.getCodigoUnidadTramitacionDestino(), true, null);
                
                if(null != scrOrg){
                    listaAtributos.add(getFieldInfo("8", destino.getCodigoUnidadTramitacionDestino()));    //Destino
                }
                
            } catch (HibernateException e) {
                LOGGER.error(MENSAJE_ERROR_COMPROBAR_UNIDAD + MENSAJE_DESTINO + e.getMessage(), e);
                
            } catch (ValidationException e) {
                LOGGER.error(MENSAJE_ERROR_COMPROBAR_UNIDAD + MENSAJE_DESTINO + MENSAJE_NO_EXISTE_SCR_ORGS + destino.getCodigoUnidadTramitacionDestino() + MENSAJE_EN_LA_ENTIDAD + orveBean.getEntidad().getIdentificador() + ". " + e.getMessage(), e); 
            }
        }         
    }
    
    public static String getNombreRazonSocialPerson(DeInteresado interesado) {
        String nombrePerson = "";
        
        if(StringUtils.isNotEmpty(interesado.getNombreInteresado())){            
            nombrePerson += interesado.getNombreInteresado();
        }
        if(StringUtils.isNotEmpty(interesado.getPrimerApellidoInteresado())){
            if(StringUtils.isNotEmpty(nombrePerson)){
                nombrePerson += " ";
            }
            nombrePerson += interesado.getPrimerApellidoInteresado();
        }
        if(StringUtils.isNotEmpty(interesado.getSegundoApellidoInteresado())){
            if(StringUtils.isNotEmpty(nombrePerson)){
                nombrePerson += " ";
            }
            nombrePerson += " " + interesado.getSegundoApellidoInteresado();
        }
        
        if(StringUtils.isEmpty(nombrePerson) && StringUtils.isNotEmpty(interesado.getRazonSocialInteresado())){
            nombrePerson = interesado.getRazonSocialInteresado();
        }
        
        if(StringUtils.isNotEmpty(interesado.getDocumentoIdentificacionInteresado())){
            nombrePerson = interesado.getDocumentoIdentificacionInteresado() + " " + nombrePerson;
        }

        return nombrePerson;
    }
    
    public static String getEstadoElaboracionByValidezORVE(String validezDocumento) {
        String estadoElaboracion = null;
        
        if(StringUtils.isNotEmpty(validezDocumento)){
            estadoElaboracion = ConstantesWSExportacion.MAPEO_VALIDEZ_ORVE_ESTADO_ELABORACION_NTI.get(validezDocumento);
        }
        
        if(StringUtils.isEmpty(estadoElaboracion)){
            estadoElaboracion = EstadosElaboracion.COPIA_AUTENTICA_DOC_PAPEL;
        }
        
        return estadoElaboracion;
    }
    
    public static FieldInfo getFieldInfo(String fieldId, String value) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setFieldId(fieldId);
        fieldInfo.setValue(value);
        return fieldInfo;
    }
    
    private SICRES30Utils(){
    }
}
