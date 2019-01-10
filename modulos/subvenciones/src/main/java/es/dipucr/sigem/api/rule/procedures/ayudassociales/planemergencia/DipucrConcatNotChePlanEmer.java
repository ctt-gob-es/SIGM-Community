package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrConcatNotChePlanEmer implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrConcatNotChePlanEmer.class);

    private IClientContext cct;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No puede darse este caso        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            // ----------------------------------------------------------------------------------------------
            cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            String numexp = rulectx.getNumExp();

            int idTramiteAnterior = 0;
            int nConvocatoria = 0;
            // Calculamos el nª de resolución en el que estamos
            String tramite = "2ª Propuesta";

            nConvocatoria = TramitesUtil.cuentaTramitesByNombreTramite(cct, numexp, tramite);
            if(nConvocatoria <= 0){
                nConvocatoria = 1;
            }

            // Primero borramos los cheques que haya generados de esta
            // convocatoria
            String resolucion = "-P" + nConvocatoria + "-" + Calendar.getInstance().get(Calendar.YEAR);

            IItemCollection valesBorrarCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.NOMBRE_TABLA, ConstantesString.WHERE + ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.NUMEXP + " = '" + rulectx.getNumExp() + "' AND " + ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.CODIGO + " LIKE '%" + resolucion + "'");
            Iterator<?> valesBorrarIterator = valesBorrarCollection.iterator();
            while (valesBorrarIterator.hasNext()) {
                IItem valeBorrar = (IItem) valesBorrarIterator.next();
                valeBorrar.delete(cct);
            }

            // Recuperamos todas las notificaciones del trámite anterior
            // Tomamos el tramite ID anterior
            IItemCollection tramiteAnteriorCollection = entitiesAPI.getDocuments(numexp, DocumentosUtil.NOMBRE + " = 'Notificación'", DocumentosUtil.FDOC + " DESC");
            Iterator<?> tramiteAnteriorIterator = tramiteAnteriorCollection.iterator();
            if (tramiteAnteriorIterator.hasNext()){
                idTramiteAnterior = ((IItem) tramiteAnteriorIterator.next()).getInt(DocumentosUtil.ID_TRAMITE);
            }

            IItemCollection notificacionesCollection = entitiesAPI.getDocuments(numexp, DocumentosUtil.ID_TRAMITE + " = '" + idTramiteAnterior + "' AND " + DocumentosUtil.NOMBRE + " = 'Notificación'", "");
            Iterator<?> notificacionesIterator = notificacionesCollection.iterator();
            int contador = 0;
            while (notificacionesIterator.hasNext()) {
                IItem notificacion = (IItem) notificacionesIterator.next();
                contador++;
                generaNotificacion(contador, notificacion, rulectx, "" + nConvocatoria, numexp);
            }
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    private void generaNotificacion(int contador, IItem notificacion, IRuleContext rulectx, String nConvocatoria, String numexpConv) {

        String nombre = "";
        String descripcion = "";
        String nombreInteresado = "";
        String nifInteresado = "";
        String importe = "30";
        String codCiudad = "";
        String codigo = "";
        String trimestre = "";
        String tipoAyuda = "";

        int valesYaImpresos = 0;
        int maxVales = 0;
        int nCheques = 0;
        File fileVales = null;
        File notificacionPDF = null;
        File notificacionFile = null;
        String numexp = "";

        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            cct.beginTX();
            String nombreNotif = SubvencionesUtils.getString(notificacion, DocumentosUtil.DESCRIPCION);
            String[] splitNombreNotif = nombreNotif.split("-");
            
            if (null != splitNombreNotif && splitNombreNotif.length >= 2) {
                numexp = splitNombreNotif[0].trim();

                LOGGER.debug("" + contador + " vamos a generar los cheques del expediente: " + numexp);

                IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
                Iterator<?> solicitudIterator = solicitudCollection.iterator();

                if (solicitudIterator.hasNext()) {
                    IItem solicitud = (IItem) solicitudIterator.next();
                    
                    nombreInteresado = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                    nifInteresado = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                    codCiudad = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                    trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
                    
                    descripcion = contador + ".- Notificación - " + numexp + " - " + nombreInteresado;
                    nombre = contador + ".- Notificación - " + nombreInteresado;
                    codigo = codCiudad + "-" + nifInteresado;
                    tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                    LOGGER.debug("nombreInteresado: " + nombreInteresado);
                    LOGGER.debug("nifInteresado: " + nifInteresado);
                    LOGGER.debug("codCiudad: " + codCiudad);
                    LOGGER.debug("trimestre: " + trimestre);
                    LOGGER.debug("descripcion: " + descripcion);
                    LOGGER.debug("nombre: " + nombre);
                    LOGGER.debug("codigo: " + codigo);
                    LOGGER.debug("tipoAyuda: " + tipoAyuda);
                }

                if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)) {
                    IItemCollection cantidadesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                    Iterator<?> cantidadesIterator = cantidadesCollection.iterator();
                    if (cantidadesIterator.hasNext()) {
                        IItem cantidades = (IItem) cantidadesIterator.next();

                        if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                            valesYaImpresos = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                            maxVales = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);
                        } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                            valesYaImpresos = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                            maxVales = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);
                        } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                            valesYaImpresos = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);
                            maxVales = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                        } else {
                            valesYaImpresos = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                            maxVales = SubvencionesUtils.getInt(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                        }

                        nCheques = maxVales - valesYaImpresos;                        

                        InformacionVales vales = new InformacionVales(nombre, nCheques, nombreInteresado, importe, codigo, nConvocatoria, numexpConv);
                        
                        fileVales = generaVales(rulectx, vales);
                        
                        notificacionPDF = getFile(SubvencionesUtils.getString(notificacion, DocumentosUtil.INFOPAG));

                        if (null != fileVales) {
                            ArrayList<File> documentos = new ArrayList<File>();
                            documentos.add(notificacionPDF);
                            documentos.add(fileVales);

                            notificacionFile = PdfUtil.concatenarArchivos(documentos);
                        } else {
                            notificacionFile = notificacionPDF;
                        }
                    }
                } else {
                    notificacionFile = getFile(SubvencionesUtils.getString(notificacion, DocumentosUtil.INFOPAG));
                }
                LOGGER.debug("Tenemos notifiacion?");

                if (notificacionFile != null) {
                    LOGGER.debug("SÍ");
                    int tpdoc = DocumentosUtil.getIdTipoDocByCodigo(cct, "NOT-CHEQUES");
                    LOGGER.debug("tenemos tipo de documento: " + tpdoc);
                    LOGGER.debug("descripcion: " + descripcion);
                    LOGGER.debug("notificacionFile: " + notificacionFile);

                    IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, descripcion, notificacionFile, Constants._EXTENSION_PDF);

                    entityDoc.set(DocumentosUtil.DESTINO, SubvencionesUtils.getString(notificacion, DocumentosUtil.DESTINO));
                    entityDoc.set(DocumentosUtil.DESTINO_ID, SubvencionesUtils.getString(notificacion, DocumentosUtil.DESTINO_ID));

                    entityDoc.store(cct);
                }
                cct.endTX(true);
                if (fileVales != null && fileVales.exists()){
                    fileVales.delete();
                }
                if (notificacionPDF != null && notificacionPDF.exists()){
                    notificacionPDF.delete();
                }
                if (notificacionFile != null && notificacionFile.exists()){
                    notificacionFile.delete();
                }
            }
        } catch (ISPACRuleException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar la notificación del plan de emergencia del expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar la notificación del plan de emergencia del expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    private File generaVales(IRuleContext rulectx, InformacionVales vales) {
        String rutaVales = "";
        File fileVales = null;
        try {
            LOGGER.debug("ahora vamos a generar los cheques.");
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();

            String nombreFicheroSalida1 = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + vales.getNombre() + ".pdf";

            HashMap<String, String> map = new HashMap<String, String>();
            /**
             * [Teresa Ticket #59 INICIO]Cambio de la ruta de las imágenes 
             * **/
            map.put("IMAGES_REPOSITORY_PATH", SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb"));
            /**
             * [Teresa Ticket #59 FIN]Cambio de la ruta de las imágenes 
             * **/

            // Definimos cual será nuestra fuente de datos
            ArrayList<SERSOPlanEmergencia> objetos = new ArrayList<SERSOPlanEmergencia>();

            String codigo1, codigo2, codigo3, codigo4;
            codigo1 = codigo2 = codigo3 = codigo4 = "";
            int i = 0;
            for (i = 0; i < vales.getCheques(); i++) {
                IItem codigoGuardar = entitiesAPI.createEntity(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.NOMBRE_TABLA, vales.getNumexpConv());
                if (i % 4 == 0) {
                    codigo1 = vales.getCodigo() + "-" + (i + 1) + "-" + vales.getCheques() + "-P" + vales.getnConvocatoria() + "-" + Calendar.getInstance().get(Calendar.YEAR);
                    codigoGuardar.set(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.CODIGO, codigo1);
                    LOGGER.debug("guardamos el codigo: " + codigo1 + " en la tabla");
                }
                if (i % 4 == 1) {
                    codigo2 = vales.getCodigo() + "-" + (i + 1) + "-" + vales.getCheques() + "-P" + vales.getnConvocatoria() + "-" + Calendar.getInstance().get(Calendar.YEAR);
                    codigoGuardar.set(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.CODIGO, codigo2);
                    LOGGER.debug("guardamos el codigo: " + codigo2 + " en la tabla");
                }
                if (i % 4 == 2) {
                    codigo3 = vales.getCodigo() + "-" + (i + 1) + "-" + vales.getCheques() + "-P" + vales.getnConvocatoria() + "-" + Calendar.getInstance().get(Calendar.YEAR);
                    codigoGuardar.set(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.CODIGO, codigo3);
                    LOGGER.debug("guardamos el codigo: " + codigo3 + " en la tabla");
                }
                if (i % 4 == 3) {
                    codigo4 = vales.getCodigo() + "-" + (i + 1) + "-" + vales.getCheques() + "-P" + vales.getnConvocatoria() + "-" + Calendar.getInstance().get(Calendar.YEAR);
                    codigoGuardar.set(ConstantesPlanEmergencia.DpcrSERSOChequesGenerados.CODIGO, codigo4);
                    LOGGER.debug("guardamos el codigo: " + codigo4 + " en la tabla");
                }
                if (i % 4 == 3) {
                    SERSOPlanEmergencia cheques = new SERSOPlanEmergencia();
                    cheques.setCodigo1(codigo1); 
                    cheques.setNombre1(vales.getNombreInteresado());
                    cheques.setImporte1(vales.getImporte());
                    cheques.setFecha1("");
                    
                    cheques.setCodigo2(codigo2); 
                    cheques.setNombre2(vales.getNombreInteresado());
                    cheques.setImporte2(vales.getImporte());
                    cheques.setFecha1("");
                    
                    cheques.setCodigo3(codigo3); 
                    cheques.setNombre3(vales.getNombreInteresado());
                    cheques.setImporte3(vales.getImporte());
                    cheques.setFecha1("");
                    
                    cheques.setCodigo4(codigo4); 
                    cheques.setNombre4(vales.getNombreInteresado());
                    cheques.setImporte4(vales.getImporte());
                    cheques.setFecha1("");
                    
                    objetos.add(cheques);
                    codigo1 = codigo2 = codigo3 = codigo4 = "";
                }

                codigoGuardar.store(cct);
            }
            if ((i - 1) % 4 != 3) {
                SERSOPlanEmergencia cheques = new SERSOPlanEmergencia();
                cheques.setCodigo1(codigo1); 
                cheques.setNombre1(vales.getNombreInteresado());
                cheques.setImporte1(vales.getImporte());
                cheques.setFecha1("");
                
                cheques.setCodigo2(codigo2); 
                cheques.setNombre2(vales.getNombreInteresado());
                cheques.setImporte2(vales.getImporte());
                cheques.setFecha1("");
                
                cheques.setCodigo3(codigo3); 
                cheques.setNombre3(vales.getNombreInteresado());
                cheques.setImporte3(vales.getImporte());
                cheques.setFecha1("");
                
                cheques.setCodigo4(codigo4); 
                cheques.setNombre4(vales.getNombreInteresado());
                cheques.setImporte4(vales.getImporte());
                cheques.setFecha1("");
                
                objetos.add(cheques);
            }
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objetos);

            /**
             * [Teresa Ticket #59 INICIO]Añadir el primer parámetro que es el nombre de la carpeta 
             * **/
            JasperReport report = JasperReportUtil.obtenerObjetoJasper(File.separator + "SERSO" + File.separator + "PlanEmergencia", "ChequesSERSO-PE.jrxml");
            /**
             * [Teresa Ticket #59 FIN]Añadir el primer parámetro que es el nombre de la carpeta 
             * **/

            // Rellenamos el informe con la conexion creada y sus parametros
            // establecidos
            JasperPrint print = JasperReportUtil.rellenarInforme(report, map, ds);

            if (nombreFicheroSalida1.toUpperCase().indexOf("PDF") > 0) {
                rutaVales = nombreFicheroSalida1;
            } else{
                rutaVales = nombreFicheroSalida1 + ".pdf";
            }

            fileVales = new File(rutaVales);
            if (!fileVales.exists()) {
                JasperReportUtil.exportarReportAPdf(rutaVales, print);
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar los vales del plan de emergencia con código de barras del expediente: " + vales.getNumexpConv() + ". " + e.getMessage(), e);
        }
        return fileVales;
    }

    /**
     * Obtiene el fimero a firmar y lo convierte a pdf
     */
    protected File getFile(String docRef) throws ISPACException {

        IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
        Object connectorSession = null;
        File file = null;

        try {
            connectorSession = gendocAPI.createConnectorSession();

            String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, docRef));
            if(!"pdf".equalsIgnoreCase(extension)) {

                // Convertir el documento original a PDF
                file = convert2PDF(docRef, extension);

            } else {

                // Se obtiene el documento del repositorio documental
                String fileName = FileTemporaryManager.getInstance().newFileName("." + extension);
                fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;

                OutputStream out = new FileOutputStream(fileName);
                gendocAPI.getDocument(connectorSession, docRef, out);

                file = new File(fileName);
            }

        } catch (FileNotFoundException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al obtener el fichero: " + docRef + ". " + e.getMessage(), e);
            throw new ISPACException(ConstantesString.LOGGER_ERROR + " al obtener el fichero: " + docRef + ". " + e.getMessage(), e);
        } finally {
            if (connectorSession != null) {
                gendocAPI.closeConnectorSession(connectorSession);
            }
        }
        return file;
    }

    private File convert2PDF(String infoPag, String extension) throws ISPACException {
        // Convertir el documento a pdf
        String docFilePath = DocumentConverter.convert2PDF(cct.getAPI(), infoPag, extension);

        // Obtener la información del fichero convertido
        File file = new File(docFilePath);
        if (!file.exists()){
            throw new ISPACException("No se ha podido convertir el documento a PDF");
        }

        return file;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public class SERSOPlanEmergencia {

        private String codigo1;
        private String nombre1;
        private String importe1;
        private String fecha1;

        private String codigo2;
        private String nombre2;
        private String importe2;
        private String fecha2;

        private String codigo3;
        private String nombre3;
        private String importe3;
        private String fecha3;

        private String codigo4;
        private String nombre4;
        private String importe4;
        private String fecha4;

        public SERSOPlanEmergencia() {

            this.codigo1 = "";
            this.nombre1 = "";
            this.importe1 = "";
            this.fecha1 = "";

            this.codigo2 = "";
            this.nombre2 = "";
            this.importe2 = "";
            this.fecha2 = "";

            this.codigo3 = "";
            this.nombre3 = "";
            this.importe3 = "";
            this.fecha3 = "";

            this.codigo4 = "";
            this.nombre4 = "";
            this.importe4 = "";
            this.fecha4 = "";
        }

        public void setCodigo1(String codigo1) {
            this.codigo1 = codigo1;
        }

        public String getCodigo1() {
            return codigo1;
        }

        public void setNombre1(String nombre1) {
            this.nombre1 = nombre1;
        }

        public String getNombre1() {
            return nombre1;
        }

        public void setFecha1(String fecha1) {
            this.fecha1 = fecha1;
        }

        public String getFecha1() {
            return fecha1;
        }

        public void setImporte1(String importe1) {
            this.importe1 = importe1;
        }

        public String getImporte1() {
            return importe1;
        }

        public void setCodigo2(String codigo2) {
            this.codigo2 = codigo2;
        }

        public String getCodigo2() {
            return codigo2;
        }

        public void setNombre2(String nombre2) {
            this.nombre2 = nombre2;
        }

        public String getNombre2() {
            return nombre2;
        }

        public void setImporte2(String importe2) {
            this.importe2 = importe2;
        }

        public String getImporte2() {
            return importe2;
        }

        public void setFecha2(String fecha2) {
            this.fecha2 = fecha2;
        }

        public String getFecha2() {
            return fecha2;
        }

        public void setCodigo3(String codigo3) {
            this.codigo3 = codigo3;
        }

        public String getCodigo3() {
            return codigo3;
        }

        public void setImporte3(String importe3) {
            this.importe3 = importe3;
        }

        public String getImporte3() {
            return importe3;
        }

        public void setNombre3(String nombre3) {
            this.nombre3 = nombre3;
        }

        public String getNombre3() {
            return nombre3;
        }

        public void setFecha3(String fecha3) {
            this.fecha3 = fecha3;
        }

        public String getFecha3() {
            return fecha3;
        }

        public void setCodigo4(String codigo4) {
            this.codigo4 = codigo4;
        }

        public String getCodigo4() {
            return codigo4;
        }

        public void setImporte4(String importe4) {
            this.importe4 = importe4;
        }

        public String getImporte4() {
            return importe4;
        }

        public void setNombre4(String nombre4) {
            this.nombre4 = nombre4;
        }

        public String getNombre4() {
            return nombre4;
        }

        public void setFecha4(String fecha4) {
            this.fecha4 = fecha4;
        }

        public String getFecha4() {
            return fecha4;
        }
    }
    
    public class InformacionVales{
        private String nombre;
        private int cheques;
        private String nombreInteresado;
        private String importe;
        private String codigo;    
        private String nConvocatoria;
        private String numexpConv;
        
        public InformacionVales(String nombre, int cheques, String nombreInteresado, String importe, String codigo, String nConvocatoria, String numexpConv) {
            this.nombre = nombre;
            this.cheques = cheques;
            this.nombreInteresado = nombreInteresado;
            this.importe = importe;
            this.codigo = codigo;
            this.nConvocatoria = nConvocatoria;
            this.numexpConv = numexpConv;
            
        }
        
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public int getCheques() {
            return cheques;
        }
        public void setCheques(int cheques) {
            this.cheques = cheques;
        }
        public String getNombreInteresado() {
            return nombreInteresado;
        }
        public void setNombreInteresado(String nombreInteresado) {
            this.nombreInteresado = nombreInteresado;
        }
        public String getImporte() {
            return importe;
        }
        public void setImporte(String importe) {
            this.importe = importe;
        }
        public String getCodigo() {
            return codigo;
        }
        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }
        public String getnConvocatoria() {
            return nConvocatoria;
        }
        public void setnConvocatoria(String nConvocatoria) {
            this.nConvocatoria = nConvocatoria;
        }
        public String getNumexpConv() {
            return numexpConv;
        }
        public void setNumexpConv(String numexpConv) {
            this.numexpConv = numexpConv;
        }        
    }
}
