package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FileBean;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.common.utils.XslTemplatesUtil;
import es.dipucr.sigem.api.rule.common.utils.ZipUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Regla que concatena el justificante de registro
 * con la factura en pdf y sus posibles anexos
 * @author dipucr-Felipe
 * @since 06.05.2014
 */
public class DipucrFaceConcatenarFacturaRule implements IRule {

	/**
	 * logger.
	 */
	private static final Log logger = LogFactory.getLog(DipucrFaceConcatenarFacturaRule.class);

	/**
	 * Nombre del documento.
	 */
	public static final String DOCUMENT_NAME = "PDF con la conformación de eFactura";
	
	private int tramiteId;
	private IEntitiesAPI entitiesAPI;
	private IInvesflowAPI invesflowAPI;
	
	/**
	 * Método init
	 */
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	/**
	 * Método validate
	 */
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Función de personalización del evento. Aquí se definen las acciones
	 * asociadas al evento
	 * 
	 * @return Se debe devolver un objeto si el tipo de evento lo necesita
	 *         (calculo de responsables, etc)
	 */
	public final Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			invesflowAPI = rulectx.getClientContext().getAPI();
			entitiesAPI = invesflowAPI.getEntitiesAPI();			
			tramiteId = rulectx.getTaskId();
			return internalExecute(rulectx);
		} catch (Exception e) {
			logger.error("[Factur@] Error al ejecutar la regla", e);
			throw new ISPACRuleException("Error al ejecutar la regla que "
					+ "concatena la factura con su justificante", e);
		}
	}

	/**
	 * Función de personalización del evento. Aquí se definen las acciones
	 * asociadas al evento
	 * 
	 * @param rulectx
	 *            contexto de la regla.
	 * @return Se debe devolver un objeto si el tipo de evento lo necesita
	 *         (calculo de responsables, etc)
	 * @throws Exception.
	 */
	protected Object internalExecute(IRuleContext ruleContext) throws Exception {
		if (logger.isInfoEnabled())
			logger.info("[Factur@] Executing Rule " + this.getClass());

		// Añadimos el PDF.
		addConformacionPdf(ruleContext, entitiesAPI);

		return null;
	}

	/**
	 * Crea el PDF de conformación.
	 * 
	 * @param ruleContext
	 *            contexto del cliente.
	 * @param efactura
	 *            efactura.
	 * @param entitiesAPI 
	 * @return documento de la conformación.
	 * @throws ISPACRuleException
	 *             en caso de error.
	 */
	private IItem addConformacionPdf(IRuleContext ruleContext, IEntitiesAPI entitiesAPI)
			throws ISPACRuleException {
		
		File fileResultadoTemp;
		File fileResultado;
		IClientContext cct = ruleContext.getClientContext();
		String numexp = ruleContext.getNumExp(); 
		try {
			if (logger.isInfoEnabled())
				logger.info("[Factur@] Generando el documento 'PDF con la conformación de eFactura'");

			FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
			
			//Recuperamos el justificante
			String infoPagJustificante = DocumentosUtil.getInfoPagByNombre(numexp, ruleContext, "Justificante");
			File fileJustificante =  DocumentosUtil.getFile(cct, infoPagJustificante, "justificante", null);
			
			//No añadimos la información de la tabla eFactura, sólo el justificante y la factura
			fileResultado = fileJustificante;
			
			//*************
			//obtenemos el fichero de la factura entre los anexos
			IItemCollection facturasCollection = entitiesAPI.getDocuments(numexp, "TP_REG = 'ENTRADA' AND UPPER(DESCRIPCION) LIKE '%XSIG'", "");
			
			IItem itemFactura = (IItem) facturasCollection.iterator().next();
			
			File fileFicheroXml = DocumentosUtil.getFile(cct, itemFactura.getString("INFOPAG"), "facturaXml", null);
			String facturaXml = fileFicheroXml.getAbsolutePath();
			String facturaPDF = ftMgr.getFileTemporaryPath() + "/facturaPdf.pdf";
			
			generaFactura(facturaXml, facturaPDF);
			File fileFactura = new File(facturaPDF);

			fileResultadoTemp = concatena(fileResultado, fileFactura);

			//[dipucr-Felipe #1148-extra] Añadimos el XML como anexo
			PdfUtil.anexarDocumento(fileResultadoTemp, fileFicheroXml, "factura.xsig");
			
			//[Dipucr-Manu #533] + ALSIGM3 Al firmarse la conformación de la factura la banda gris sobreescribe el contenido
			PdfUtil.anexarDocumento( fileResultadoTemp, fileFactura, "factura_xsig.pdf");
			
			fileResultado.delete();
			fileFactura.delete();
			fileFicheroXml.delete();
			fileResultado = fileResultadoTemp;
			
			//*************
			//INICIO [dipucr-Felipe #1148] Añadir adjuntos de factura
			//obtenemos los anexos de la factura (fichero zip)
			IItemCollection anexosCollection = entitiesAPI.getDocuments(numexp, "TP_REG = 'ENTRADA' AND UPPER(DESCRIPCION) LIKE '%ZIP'", "");
			
			//Si hay anexo
			if (anexosCollection.toList().size() > 0){	
				
				IItem itemZipAnexo =  (IItem) anexosCollection.iterator().next();
				String sInfopagZipAnexo = itemZipAnexo.getString("INFOPAG");
				
				File fileZipAnexos = DocumentosUtil.getFile
						(cct, sInfopagZipAnexo, null, null);
				
				List<FileBean> listFicheros = ZipUtils.extraerTodosFicheros(fileZipAnexos);
				
				//Los metemos en el PDF como adjuntos
				for (FileBean fb : listFicheros){
                    PdfUtil.anexarDocumento(fileResultado, fb.getFile(), fb.getName());
				}
			}
			//FIN [dipucr-Felipe #1148]
			
			
			String tipoDocumento = DocumentosUtil.getTipoDocumentoByCodPlantilla(cct, DipucrFaceFacturasUtil.COD_DOC_FACTURA);
			int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
			
			IItem document = DocumentosUtil.generaYAnexaDocumento(ruleContext, tramiteId, documentTypeId, DOCUMENT_NAME, fileResultado, Constants._EXTENSION_PDF);
			
			fileResultado.delete();
			return document;
			
		} catch (Exception e) {
			throw new ISPACRuleException("Error al concatenar el documento de factura "
					+ "con el justificante y con los anexos", e);
		}
	}

	/**
	 * 
	 * @param documentoDestino
	 * @param documentoAconcatenar
	 * @return
	 */
	private File concatena(File documentoDestino, File documentoAconcatenar) {

		File documentoConcatenado = null;
		int n = 0;
		try {
			
			PdfImportedPage page = null;
			Image imagen = null;
			//Primero concatenamos el de destino, por si tiene datos, y luego el que vamos a concantenar
			FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
			documentoConcatenado = ftMgr.newFile("." + Constants._EXTENSION_PDF);

			FileInputStream fi = new FileInputStream(documentoDestino);
			PdfReader reader = new PdfReader(fi);			
			
			Document documento = new Document(reader.getPageSizeWithRotation(1));
			documento.setMargins(0, 0, 0, 0);
			
			FileOutputStream fou = new FileOutputStream(documentoConcatenado);
			PdfWriter writer = PdfWriter.getInstance(documento, fou);
			writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);
			
			documento.open();
			
			n = reader.getNumberOfPages();
			for (int i = 0; i < n;) {
				i++;
				documento.newPage();	
				page = writer.getImportedPage(reader, i);
				imagen = Image.getInstance(page);
				documento.add(imagen);			
			}
			
//			No ponemos adjuntos
//			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer,
//					documentoAconcatenar.getAbsolutePath(), documentoAconcatenar.getName(), null);
//			if (pfs != null)
//				writer.addFileAttachment("", pfs);	
						
			reader.close();
			fi.close();
			
			fi = new FileInputStream(documentoAconcatenar);
			reader = new PdfReader(fi);			
			
			n = reader.getNumberOfPages();
			for (int i = 0; i < n;) {
				i++;
				documento.newPage();	
				page = writer.getImportedPage(reader, i);
				imagen = Image.getInstance(page);				
				documento.add(imagen);
			}
			
//			No ponemos adjuntos
//			pfs = PdfFileSpecification.fileEmbedded(writer,
//					documentoAconcatenar.getAbsolutePath(), documentoAconcatenar.getName(), null);
//			if (pfs != null)
//				writer.addFileAttachment("", pfs);	
			
			reader.close();
			fi.close();
			
			documento.close();
			fou.close();
			
		} catch (DocumentException e) {
        	logger.error("Error al concatenar los documentos. " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
        	logger.error("Error al concatenar los documentos. " + e.getMessage(), e);
		} catch (IOException e) {
        	logger.error("Error al concatenar los documentos. " + e.getMessage(), e);
		} catch (ISPACException e) {
        	logger.error("Error al concatenar los documentos. " + e.getMessage(), e);
		}
		return documentoConcatenado;
	}

	/**
	 * Método cancel
	 */
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	/**
	 * Método que genera la factura a partir del XML
	 * @param facturaXml
	 * @param rutaDestino
	 * @throws Exception
	 */
	public void generaFactura(String facturaXml, String rutaDestino) throws Exception 
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document documento = builder.parse(new File(facturaXml));
		
		String versionp=documento.getElementsByTagName("SchemaVersion").item(0).getTextContent();
		
		FileInputStream fis = new FileInputStream(facturaXml);
		byte [] in = new byte [fis.available()];
		fis.read(in);
		fis.close();
	
		String rutaXSL = "";
		
		//INICIO [dipucr-Felipe (Tisa-Ibiza) #1229]
		if(versionp.trim().toUpperCase().equals("3.2".trim().toUpperCase())){
			rutaXSL = XslTemplatesUtil.getInstance().getPath("factura", "/UNEDOCS_32_PDF_ES.xsl");//[dipucr-Felipe 3#82]
		}
		else if(versionp.trim().toUpperCase().equals("3.2.1".trim().toUpperCase())){
			rutaXSL = XslTemplatesUtil.getInstance().getPath("factura", "/UNEDOCS_321_PDF_ES.xsl");//[dipucr-Felipe 3#82]
		}
		else if(versionp.trim().toUpperCase().equals("3.2.2".trim().toUpperCase())){//[dipucr-Felipe #682]
			rutaXSL = XslTemplatesUtil.getInstance().getPath("factura", "/UNEDOCS_322_PDF_ES.xsl");
		}
		else{
			throw new Exception("Formato de factura no reconocido");
		}//FIN [dipucr-Felipe (Tisa-Ibiza) #1229]
		
		byte [] out= make(in, rutaXSL);
		FileOutputStream fox = new FileOutputStream(rutaDestino);
		fox.write(out);
		fox.close();
	}
	
	/**
	 * 
	 * @param is
	 * @param xslt
	 * @return
	 * @throws Exception
	 */
	public byte [] make(byte [] is,String xslt) throws Exception
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		make(bais, baos, xslt);
		byte [] out = baos.toByteArray();
		return(out);
	}
	
	/**
	 * Generamos las facturas en PDF
	 * @param is
	 * @param os
	 * @param xslt
	 * @return
	 * @throws Exception
	 */
	public String make(InputStream is, OutputStream os,String xslt) throws Exception
	{
		try
		{
	      	File xsltFile = new File(xslt);
	        FopFactory fopFactory = FopFactory.newInstance();
	        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setAuthor("T-Systems");
            foUserAgent.setCreator("T-Systems");
            
            try {
                // Construct fop with desired output format
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
                // Setup XSLT
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
                // Set the value of a <param> in the stylesheet
                transformer.setParameter("cv", "XXXXXXX");
            
                // Setup input for XSLT transformation
                Source src = new StreamSource(is);
            
                // Resulting SAX events (the generated FO) must be piped through to FOP
                Result res = new SAXResult(fop.getDefaultHandler());
    
                // Start XSLT transformation and FOP processing                
                transformer.transform(src, res);
                
            }
            finally{
            	os.close();
            }
		}
		catch(Exception e)
		{
	      	throw(e);
		}
		return("1");
	}
}
