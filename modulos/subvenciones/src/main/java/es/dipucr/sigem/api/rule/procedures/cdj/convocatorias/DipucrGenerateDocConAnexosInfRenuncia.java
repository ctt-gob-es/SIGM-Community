package es.dipucr.sigem.api.rule.procedures.cdj.convocatorias;


import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrGenerateDocConAnexosInfRenuncia implements IRule {

    private static final Logger LOGGER = Logger.getLogger(DipucrGenerateDocConAnexosInfRenuncia.class);

    protected String plantilla = "";
    protected String tipoDocumento = "";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try {
            IClientContext cct = rulectx.getClientContext();

            plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

            if (StringUtils.isNotEmpty(plantilla)) {
                tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar la plantilla específica del expediente: " + rulectx.getNumExp() + ". " + e.getMessage(),
                    e);
        }
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {

        try {
            /*********************************************************/
            IClientContext cct = rulectx.getClientContext();
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
            /*********************************************************/
            
            String numexp = rulectx.getNumExp();
            
            File file = null;

            String nombreDocumento = "";
            int documentTypeId = 0;

            Document document = null;
            // Creamos un reader para el documento
            PdfReader reader = null;

            Iterator<?> tramitesIterator = TramitesUtil.getTramites(cct, numexp, " ID != '" + rulectx.getTaskId() + "' ", " ID DESC").iterator();
            
            if(tramitesIterator.hasNext()){
                tramitesIterator.next();
                IItem tramiteAnt = (IItem)tramitesIterator.next();
                
                String idTramAnt = tramiteAnt.getString("ID_TRAM_EXP");
                    
                documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
    
                IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), " NUMEXP='" + rulectx.getNumExp() + "' AND ID_TPDOC = '" + documentTypeId + "' AND ID_TRAMITE='" + idTramAnt + "'", "");
                Iterator<?> iDoc = documentos.iterator();

                while (iDoc.hasNext()) {
                    IItem doc = (IItem) iDoc.next();
                    String infoPag = doc.getString("INFOPAG");
                    String extension = doc.getString("EXTENSION");

                    // Plantilla de Notificaciones
                    File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
                    
                    // Convertir el documento original a PDF
                    String docFilePath = DocumentConverter.convert2PDF(cct.getAPI(), infoPag, extension);

                    // Obtener la información del fichero convertido
                    file = new File(docFilePath);
                    if (!file.exists()){
                        throw new ISPACException("No se ha podido convertir el documento a PDF");
                    }

                    String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"
                            + FileTemporaryManager.getInstance().newFileName() + ".pdf";
                    File resultado = new File(rutaFileName);
                    FileOutputStream resultadoFO = new FileOutputStream(resultado.getPath());

                    FileInputStream fisFileAnexo = new FileInputStream(file);
                    reader = new PdfReader((InputStream) fisFileAnexo);
                    reader.consolidateNamedDestinations();
                    int n = reader.getNumberOfPages();

                    document = new Document(reader.getPageSizeWithRotation(1));

                    document.setMargins(0, 0, 0, 0);

                    PdfWriter writer = PdfWriter.getInstance(document, resultadoFO);
                    writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);

                    document.open();
                    for (int i = 1; i <= n; i++) {
                        document.newPage();
                        Image imagen = Image.getInstance(writer.getImportedPage(reader, i));
                        imagen.scalePercent(100);
                        document.add(imagen);

                    }
                    
                    String sqlQuery1 = "NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE = 'Anexo' AND ID_TRAMITE='" + idTramAnt + "'";
                    IItemCollection documentosAnexar = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery1, "");
                    Iterator<?> iDocAnexar = documentosAnexar.iterator();

                    while (iDocAnexar.hasNext()) {
                        IItem docAnexar = (IItem) iDocAnexar.next();

                        String infoPagAnexar = "";
                        String ext = "";
                        if (docAnexar.getString("INFOPAG_RDE") != null) {
                            infoPagAnexar = docAnexar.getString("INFOPAG_RDE");
                            ext = docAnexar.getString("EXTENSION_RDE");
                        } else {
                            infoPagAnexar = docAnexar.getString("INFOPAG");
                            ext = docAnexar.getString("EXTENSION");
                        }

                        String descripcion = docAnexar.getString("DESCRIPCION");

                        File fileAnexo = DocumentosUtil.getFile(cct, infoPagAnexar, null, null);

                        nombreDocumento = descripcion + "." + ext;

                        aniadeDocumento(writer, fileAnexo.getAbsolutePath(), nombreDocumento, normalizar(descripcion));

                        fileAnexo.delete();
                        fileAnexo = null;
                    }

                    document.close();
                    resultadoFO.close();
                    reader.close();

                    IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, tipoDocumento, resultado, "pdf");
                    entityDoc.set("DESCRIPCION", doc.getString("DESCRIPCION"));

                    entityDoc.store(cct);
                    file.delete();
                    file = null;
                    resultado.delete();
                    resultado = null;
                    DocumentosUtil.deleteFile(rutaFileName);                    

                    if (resultado1 != null && resultado1.exists()){
                        resultado1.delete();
                    }
                    resultado1 = null;
                    if (fisFileAnexo != null){
                        fisFileAnexo.close();
                    }
                    fisFileAnexo = null;
                    writer.close();
                    writer = null;
                    DocumentosUtil.deleteFile(docFilePath);
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar los documentos. " + e.getMessage(), e);
        } catch (FileNotFoundException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar los documentos. " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar los documentos. " + e.getMessage(), e);
        } catch (DocumentException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al generar los documentos. " + e.getMessage(), e);
        }
        return Boolean.TRUE;
    }

    private static String normalizar(String name) {
        name = StringUtils.replace(name, "/", "_");
        name = StringUtils.replace(name, "\\", "_");
        return name;
    }

    private void aniadeDocumento(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) {

        try {

            PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
            if (pfs != null){
                writer.addFileAttachment(descripcionAdjunto, pfs);
            }

        } catch (IOException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al añadir documento al pdf. " + e.getMessage(), e);
        }
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

}
