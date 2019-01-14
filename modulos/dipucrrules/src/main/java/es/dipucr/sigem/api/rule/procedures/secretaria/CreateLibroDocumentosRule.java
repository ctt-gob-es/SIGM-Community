package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
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
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [dipucr-Felipe #869]
 * Crea el libro de documentos
 */
public abstract class CreateLibroDocumentosRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(CreateLibroDocumentosRule.class);
    
    protected ArrayList<String> listDocumentos = null;
    protected Date fechaInicio = null;
    protected Date fechaFin = null;
    
    protected String plantillaLibro = null;
    protected String TIPODOC_LIBRO = "Libro de Documentos";
	private static final String CONTRAPORTADA = Constants.TIPODOC.CONTRAPORTADA;
    

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

    	try{
        	IClientContext cct = rulectx.getClientContext();
        	IInvesflowAPI invesflowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            String numexp = rulectx.getNumExp();
            IItemCollection colFechas = entitiesAPI.getEntities("LIBRO", numexp);
            
            if (!colFechas.next()){
            	rulectx.setInfoMessage("Debe rellenar las fechas en la pestaña 'Libro de Documentos'");
            	return false;
            }
            else{
            	IItem itemFechas = colFechas.value();
            	fechaInicio =  itemFechas.getDate("FECHA_INICIO");
            	fechaFin = itemFechas.getDate("FECHA_FIN");
            	
            	if (null == fechaInicio || null == fechaFin){
            		rulectx.setInfoMessage("Debe rellenar las fechas en la pestaña 'Libro de Documentos'");
                	return false;
            	}
            	else{
            		listDocumentos = getListDocumentos(rulectx);
            		if (listDocumentos.size() <= 0){
            			rulectx.setInfoMessage("No se han encontrado ningún documento en las fechas seleccionadas");
                    	return false;
            		}
            	}
            }
            return true;
            
        } catch(Exception e){
        	String error = "Error al crear el libro de informes de secretaría";
        	LOGGER.error(error, e);
            throw new ISPACRuleException(error, e);
        }
    	
    }

    protected abstract ArrayList<String> getListDocumentos(IRuleContext rulectx) throws ISPACRuleException;

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
    	
        try{
        	IClientContext cct = rulectx.getClientContext();
        	IInvesflowAPI invesflowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            /*********************************************************************
			 * GENERAMOS UN DOCUMENTO CON LA PORTADA Y LO CONVERTIMOS EN PDF
			 ********************************************************************/
	        //Obtenemos el tipo de documento
			IItem itemDocPortadaLibro = crearDocumento(rulectx, TIPODOC_LIBRO, plantillaLibro);
			
			//Formateamos la plantilla
			String sInfopag = itemDocPortadaLibro.getString("INFOPAG");
			int idDocLibro = itemDocPortadaLibro.getKeyInt();
			
			//Convertimos a pdf la plantilla
			String sRutaPortada = DocumentConverter.convert2PDF(invesflowAPI, sInfopag, Constants._EXTENSION_ODT);
	        //Ponemos el pdf de la portada como el primero de la lista
			File filePortada = new File(sRutaPortada);
        	
        	/*********************************************************************
			 * IDEM CON LA CONTRAPORTADA PERO AL FINAL
			 ********************************************************************/
        	//Obtenemos el tipo de documento
        	IItem itemDocContraportadaLibro = crearDocumento(rulectx, CONTRAPORTADA, CONTRAPORTADA);
			
        	//Convertimos a pdf la plantilla
			String sInfopagContra = itemDocContraportadaLibro.getString("INFOPAG");
			String sRutaContraPortada = DocumentConverter.convert2PDF(invesflowAPI, sInfopagContra, Constants._EXTENSION_ODT);
			
	        //Ponemos el pdf de la portada como el último de la lista
			File fileContraPortada = new File(sRutaContraPortada);
        	
        	//Borramos el documento de contraportada
        	entitiesAPI.deleteDocument(itemDocContraportadaLibro);
        	
	        /*********************************************************************
			 * GENERACIÓN DEL LIBRO DE DECRETOS
			 ********************************************************************/
	  		//Generamos el libro de decretos y lo sustituimos la plantilla por este
        	File fileLibro = PdfUtil.concatenarPublicacion(cct, listDocumentos, filePortada, fileContraPortada);
        	
    		DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocLibro, fileLibro, Constants._EXTENSION_PDF, plantillaLibro);
    		
    		if(filePortada != null && filePortada.exists()) filePortada.delete();
    		if(fileLibro != null && fileLibro.exists()) fileLibro.delete();
    		if(fileContraPortada != null && fileContraPortada.exists()) fileContraPortada.delete();
    		
    		return new Boolean(true);
            
        } catch(Exception e){
        	String error = "Error al crear el libro de documentos";
        	LOGGER.error(error, e);
            throw new ISPACRuleException(error, e);
        }
    }
    
    /**
	 * 
	 * @param rulectx Contexto de la regla
	 * @param listaAnuncios Lista de anuncios de la factura
	 * @param nombreFichero Nombre del fichero
	 * @param nombrePlantilla Nombre de la plantilla
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private IItem crearDocumento(IRuleContext rulectx, String tipoDoc, String nombrePlantilla) throws Exception{
		
		IItem entityDoc = null;
		OpenOfficeHelper ooHelper = null;
		IItemCollection collection = null;
		Iterator it = null;
		XComponent xComponent = null;
		File file = null;
		File file1 = null;
		
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------
			
			// Crear un nuevo documento de factura
			DocumentosUtil.generarDocumento(rulectx, tipoDoc, nombrePlantilla, "borrar");
	    	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantilla + " - borrar");
	    	file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//Reemplazamos las cadenas de las portadas
			formatearVariables(rulectx, xComponent);
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			file1.delete();
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, tipoDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

			entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, nombrePlantilla, file, Constants._EXTENSION_ODT);
			file.delete();
			//Borra los documentos intermedios del gestor documental
	        collection = DocumentosUtil.getDocumentsByDescripcion(rulectx.getNumExp(), rulectx, "borrar");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
		} catch(Exception e){
        	String error = "Error al crear el libro de documentos";
			LOGGER.error(error, e);
			throw new Exception(error, e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
		return entityDoc;
	}
	
	/**
	 * Formatear el documento inicial de diligencia
	 * @param xComponent
	 * @param bRechazados 
	 * @throws ISPACException 
	 */
	private void formatearVariables(IRuleContext rulectx, XComponent xComponent) throws ISPACException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iMes = Integer.MIN_VALUE;
		int iAnyo = Integer.MIN_VALUE;
		String sMes = null;
		Calendar c = null;
		
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		
		//Buscamos la cadena de parámetros
		XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
		XReplaceDescriptor xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
        
		//Fecha inicio
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", sdf.format(fechaInicio));
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", sdf.format(fechaFin));
		c = DateUtil.getCalendar(fechaFin);
		//Mes de la fecha inicio
		iMes = c.get(Calendar.MONTH) + 1;
		sMes = DipucrCommonFunctions.getNombreMes(iMes);
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", sMes);
		//Año de la fecha inicio
		iAnyo = c.get(Calendar.YEAR);
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", String.valueOf(iAnyo));
	}
	
	/**
	 * 
	 * @param replaceable
	 * @param replaceDescriptor
	 * @param searchString
	 * @param replaceString
	 */
	private void reemplazarCadena(XReplaceable xReplaceable,
			XReplaceDescriptor xReplaceDescriptor, String searchString, String replaceString) {
		
		//Buscamos
		xReplaceDescriptor.setSearchString(searchString);
		//Reemplazamos
        xReplaceDescriptor.setReplaceString(replaceString);
        xReplaceable.replaceAll(xReplaceDescriptor);
	}

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
    }
}
