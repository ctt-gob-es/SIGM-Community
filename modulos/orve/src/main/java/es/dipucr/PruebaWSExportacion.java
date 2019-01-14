package es.dipucr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.codec.Base64;

import es.dipucr.orve.sw.constantes.ConstantesWSExportacion;
import es.dipucr.orve.sw.constantes.ObtenerIdentificadoresMensajesSW;
import es.dipucr.orve.sw.constantes.ObtenerRegistroMensajesSW;
import es.dipucr.sicres30.FicheroIntercambioSICRES3;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeAnexo;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeAsunto;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeDestino;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeInteresado;
import es.dipucr.sicres30.FicheroIntercambioSICRES3.DeOrigenORemitente;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;
import es.minhap.seap.ssweb.demoorve.FiltrosIdentificadores;
import es.minhap.seap.ssweb.demoorve.ObtenerIdentificaoresRespuestaWS;
import es.minhap.seap.ssweb.demoorve.ObtenerRegistroRespuestaWS;
import es.minhap.seap.ssweb.demoorve.Security;
import es.minhap.seap.ssweb.demoorve.WSExportacionPortType;
import es.minhap.seap.ssweb.demoorve.WSExportacionPortTypeProxy;
import es.minhap.seap.ssweb.demoorve.holders.SecurityHolder;
//import java.util.function.ObjDoubleConsumer;

public class PruebaWSExportacion {
    
private static final Logger LOGGER = Logger.getLogger(PruebaWSExportacion.class);
    
    
    public static final String OFICINA1 = "O00002721"; // Jun
    public static final String OFICINA2 = "O00002741"; // Catarroja    
    
    public static final String URL_TRUST_STORE = "M:/Repositorios/alsigm/modulos/orve/certificadosORVE/cacerts";
    public static final String PASS_TRUST_STORE ="changeit";
    
    public static final String USR_001 = "dip-ciudadreal";
    
    //Pruebas
    public static final String END_POINT_SWEXPORTACION_DEMO = "https://ssweb.seap.minhap.es/demoorve/WSExportacion.php";
    public static final String PASS_DEMO = "4LhMTpKO";
    
    //Producción
    public static final String END_POINT_SWEXPORTACION_PRO = "https://ssweb.seap.minhap.es/orve/WSExportacion.php";    
    public static final String PASS_PRO = "bWue8fH0";

    public static void main(String[] args) {

        System.setProperty("org.apache.commons.logging.LogFactory", "org.apache.commons.logging.impl.LogFactoryImpl");

        System.setProperty("javax.net.ssl.trustStore", URL_TRUST_STORE);
        System.setProperty("javax.net.ssl.trustStorePassword", PASS_TRUST_STORE);
        
        
        // DEMO
        System.out.println("####### DEMO ######");
        FiltrosIdentificadores filtros = getFiltros();
        WSExportacionPortType wsExportacion = new WSExportacionPortTypeProxy(END_POINT_SWEXPORTACION_DEMO);            
        Security valorSecurity = new Security(USR_001, PASS_DEMO);
        recuperaOrve(wsExportacion, valorSecurity, filtros);
        
        // PRO
        System.out.println("####### PROUCCIÓN ######");
        es.minhap.seap.ssweb.orve.FiltrosIdentificadores filtrosPro = getFiltrosPro();
        es.minhap.seap.ssweb.orve.WSExportacionPortType wsExportacionPro = new es.minhap.seap.ssweb.orve.WSExportacionPortTypeProxy(END_POINT_SWEXPORTACION_PRO);            
        es.minhap.seap.ssweb.orve.Security valorSecurityPro = new es.minhap.seap.ssweb.orve.Security(USR_001, PASS_PRO);
        recuperaOrvePro(wsExportacionPro, valorSecurityPro, filtrosPro);
        
        System.out.println("################# FIN #################");
    }

    private static void recuperaOrvePro(es.minhap.seap.ssweb.orve.WSExportacionPortType wsExportacion, es.minhap.seap.ssweb.orve.Security valorSecurity, es.minhap.seap.ssweb.orve.FiltrosIdentificadores filtros) {
        try{
            es.minhap.seap.ssweb.orve.holders.SecurityHolder securityHolder = new es.minhap.seap.ssweb.orve.holders.SecurityHolder(valorSecurity);
            
            es.minhap.seap.ssweb.orve.ObtenerIdentificaoresRespuestaWS identificadores = wsExportacion.obtenerIdentificadores(securityHolder, filtros);
            
            if(null != identificadores){
                if(ObtenerIdentificadoresMensajesSW.CODIGO_EXITO.equals(identificadores.getCodigo())){                    
                    int[] arrayIdentificadores = identificadores.getIdentificadores();
    
                    System.out.println("IDENTIFICADORES " + identificadores.getIdentificadores().length);
                    
                    int cont = 1;
                    for(int identificador : arrayIdentificadores){
                        System.out.println(cont + " ************************ " + identificador + " *************************");
                        cont++;
    
                        es.minhap.seap.ssweb.orve.ObtenerRegistroRespuestaWS registro = wsExportacion.obtenerRegistro(securityHolder, identificador);
    
                        pintaResultadoPro(registro);
                    }
                } else {
                    System.out.println(identificadores.getCodigo());
                    System.out.println(identificadores.getDescripcion());
                    System.out.println(ConstantesWSExportacion.RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.get(identificadores.getCodigo()));
                }
            }
        } catch (RemoteException e) {
             System.out.println("ERROR en WSExportacion: " + e.getMessage());
             LOGGER.error("ERROR en WSExportacion: " + e.getMessage(), e);
        }
        
    }
    
    private static void recuperaOrve(WSExportacionPortType wsExportacion, Security valorSecurity, FiltrosIdentificadores filtros) {
        try{
            SecurityHolder securityHolder = new SecurityHolder(valorSecurity);
            
            ObtenerIdentificaoresRespuestaWS identificadores = wsExportacion.obtenerIdentificadores(securityHolder, filtros);
            
            if(null != identificadores){
                if(ObtenerIdentificadoresMensajesSW.CODIGO_EXITO.equals(identificadores.getCodigo())){                    
                    int[] arrayIdentificadores = identificadores.getIdentificadores();
    
                    System.out.println("IDENTIFICADORES " + identificadores.getIdentificadores().length);
                    
                    int cont = 1;
                    for(int identificador : arrayIdentificadores){
                        System.out.println(cont + " ************************ " + identificador + " *************************");
                        cont++;
    
                        ObtenerRegistroRespuestaWS registro = wsExportacion.obtenerRegistro(securityHolder, identificador);
    
                        pintaResultado(registro);
                    }
                } else {
                    System.out.println(identificadores.getCodigo());
                    System.out.println(identificadores.getDescripcion());
                    System.out.println(ConstantesWSExportacion.RESULTADO_GET_IDENTIFICADORES_CODIGO_VALORES.get(identificadores.getCodigo()));
                }
            }
        } catch (RemoteException e) {
             System.out.println("ERROR en WSExportacion: " + e.getMessage());
             LOGGER.error("ERROR en WSExportacion: " + e.getMessage(), e);
        }
        
    }

    private static void pintaResultadoPro(es.minhap.seap.ssweb.orve.ObtenerRegistroRespuestaWS registro) {
        if(null != registro && ObtenerRegistroMensajesSW.CODIGO_EXITO.equals(registro.getCodigo())){
            
            System.out.println( registro.getCodigo() + " - " + registro.getDescripcion());
    
            try{
                JAXBContext jaxbContext = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(registro.getRegistro());
                FicheroIntercambioSICRES3 objRegistro = (FicheroIntercambioSICRES3) jaxbUnmarshaller.unmarshal(reader);
                
                pintaResultado(objRegistro);

            } catch (JAXBException e){
                System.out.println("ERROR al unmarshalear el registro: " + registro.getCodigo() + ". " + e.getMessage());
                LOGGER.error("ERROR al unmarshalear el registro: " + registro.getCodigo() + ". " + e.getMessage(), e);                                
            }
            
        } else {
            System.out.println(registro.getCodigo() + " - " + ConstantesWSExportacion.RESULTADO_GET_REGISTRO_CODIGO_VALORES.get(registro.getCodigo()));
        }
    }
    
    private static void pintaResultado(ObtenerRegistroRespuestaWS registro) {
        if(null != registro && ObtenerRegistroMensajesSW.CODIGO_EXITO.equals(registro.getCodigo())){
            
            System.out.println( registro.getCodigo() + " - " + registro.getDescripcion());
    
            try{
                JAXBContext jaxbContext = JAXBContext.newInstance(FicheroIntercambioSICRES3.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(registro.getRegistro());
                FicheroIntercambioSICRES3 objRegistro = (FicheroIntercambioSICRES3) jaxbUnmarshaller.unmarshal(reader);
                
                pintaResultado(objRegistro);

            } catch (JAXBException e){
                System.out.println("ERROR al unmarshalear el registro: " + registro.getCodigo() + ". " + e.getMessage());
                LOGGER.error("ERROR al unmarshalear el registro: " + registro.getCodigo() + ". " + e.getMessage(), e);                                
            }
            
        } else {
            System.out.println(registro.getCodigo() + " - " + ConstantesWSExportacion.RESULTADO_GET_REGISTRO_CODIGO_VALORES.get(registro.getCodigo()));
        }
    }

    private static void pintaResultado(FicheroIntercambioSICRES3 objRegistro) {
        List<DeAnexo> listaAnexos = objRegistro.getDeAnexo();
        System.out.println("LISTA ANEXOS: ");
        for(DeAnexo anexo : listaAnexos){
            System.out.println("--------");
            System.out.println("Fichero: " + anexo.getNombreFicheroAnexado());
            System.out.println("Identificador fichero firmado: " + anexo.getIdentificadorDocumentoFirmado());
            System.out.println("Identificador fichero: " + anexo.getIdentificadorFichero());
            
            File doc = new File("M:/Repositorios/alsigm/modulos/orve/salida/" + anexo.getNombreFicheroAnexado());
            try{
                FileOutputStream fos = new FileOutputStream(doc);
                if(!"CSIG".equalsIgnoreCase(FileUtils.getExtensionByNombreDoc(anexo.getNombreFicheroAnexado()))){
                    fos.write(anexo.getAnexo());
                } else {
                    fos.write(Base64.decode(new String(anexo.getAnexo())));
                }
                fos.flush();
                fos.close();
            } catch(IOException e){
                System.out.println("ERROR al recuperar el anexo. " + e.getMessage());
                LOGGER.error("ERROR al recuperar el anexo. " + e.getMessage(), e);
            }
        }
        
        DeAsunto asunto = objRegistro.getDeAsunto();                                
        System.out.println("**********ASUNTO: **************");
//        System.out.println("CodigoAsuntoSegunDestino: " + asunto.getCodigoAsuntoSegunDestino());
//        System.out.println("NumeroExpediente: " + asunto.getNumeroExpediente());
//        System.out.println("ReferenciaExterna: " + asunto.getReferenciaExterna());
        System.out.println("Resumen: " + asunto.getResumen());
        
        DeDestino destino = objRegistro.getDeDestino();                                
        System.out.println("DESTINO: ");
        System.out.println(destino.getCodigoEntidadRegistralDestino() + " - " + destino.getDecodificacionEntidadRegistralDestino());
        System.out.println(destino.getCodigoUnidadTramitacionDestino() + " - " + destino.getDecodificacionUnidadTramitacionDestino());
//        
//        DeFormularioGenerico formularioGenerico = objRegistro.getDeFormularioGenerico();
//        System.out.println("FORULARIO GENÉRICO: ");
//        System.out.println(formularioGenerico.getExpone());
//        System.out.println(formularioGenerico.getSolicita());                                
        
        List<DeInteresado> listaInteresado = objRegistro.getDeInteresado();
        System.out.println("INTERESADOS: ");
        for(DeInteresado interesado : listaInteresado){
            System.out.println("Interesado: " + interesado.getDocumentoIdentificacionInteresado() + "  " + interesado.getNombreInteresado() + " " + interesado.getPrimerApellidoInteresado() + " " + interesado.getSegundoApellidoInteresado());
            System.out.println("Representante: " + interesado.getDocumentoIdentificacionRepresentante() + "  " + interesado.getNombreRepresentante() + " " + interesado.getPrimerApellidoRepresentante() + " " + interesado.getSegundoApellidoRepresentante());
            System.out.println("(...)");
        }
        
//        DeInternosControl internosControl = objRegistro.getDeInternosControl();
//        System.out.println("INTERNOS CONTROL: ");
////        System.out.println(internosControl.getNombreUsuario());
////        System.out.println(internosControl.getDocumentacionFisica());
//        System.out.println(internosControl.getCodigoEntidadRegistralInicio());
////        System.out.println(internosControl.getContactoUsuario());
//        System.out.println(internosControl.getObservacionesApunte());                                
//        System.out.println("(...)");
        
        DeOrigenORemitente origenORemitente = objRegistro.getDeOrigenORemitente();
        System.out.println("ORIGEN O REMITENTE: ");
        System.out.println(origenORemitente.getCodigoEntidadRegistralOrigen() + " - " + origenORemitente.getDecodificacionEntidadRegistralOrigen());
        System.out.println(origenORemitente.getCodigoUnidadTramitacionOrigen() + " - " + origenORemitente.getDecodificacionUnidadTramitacionOrigen());
        System.out.println(origenORemitente.getFechaHoraEntrada());
        System.out.println(origenORemitente.getNumeroRegistroEntrada());
        
        System.out.println("\n");
        
    }

    private static FiltrosIdentificadores getFiltros() {
        FiltrosIdentificadores filtros = new FiltrosIdentificadores();
        
        filtros.setOficina("");
        filtros.setUnidad("");
        filtros.setNumeroRegistro("");

        filtros.setEstado("");
        filtros.setFechaInicio("");
        filtros.setFechaFin("");
        
        filtros.setEstado(ConstantesWSExportacion.FiltroEstado.RECIBIDOS_CONFIRMADOS);  //fecha de confirmación            
        
        filtros.setFechaInicio("2018-10-01 11:05:07");
        filtros.setFechaFin("2018-10-18 12:09:25");        
        
        filtros.setHistorico(ConstantesWSExportacion.FiltroHistorico.DESACTIVADO);
        
        return filtros;
    }
    
    private static es.minhap.seap.ssweb.orve.FiltrosIdentificadores getFiltrosPro() {
        es.minhap.seap.ssweb.orve.FiltrosIdentificadores filtros = new es.minhap.seap.ssweb.orve.FiltrosIdentificadores();
        
        filtros.setOficina("");
        filtros.setUnidad("");
        filtros.setNumeroRegistro("");

        filtros.setEstado("");
        filtros.setFechaInicio("");
        filtros.setFechaFin("");
        
        filtros.setEstado(ConstantesWSExportacion.FiltroEstado.RECIBIDOS_CONFIRMADOS);  //fecha de confirmación            

        filtros.setFechaInicio("2018-10-01 11:05:07");
        filtros.setFechaFin("2018-10-18 12:09:25");
        
        filtros.setHistorico(ConstantesWSExportacion.FiltroHistorico.DESACTIVADO);
        
        return filtros;
    }
}
