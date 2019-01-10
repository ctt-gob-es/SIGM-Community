package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
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

import com.lowagie.text.pdf.PdfWriter;
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
 * [eCenpri-Felipe ticket #164]
 * Clase para la generación del libro de decretos
 * Se rehace la clase de Telefónica por tratarse de un código ininteligible
 * Se pasa el código a una Regla en vez de un action, por ser más cómodo 
 * @author Felipe
 * @since 05.10.2010
 */
public class DecretosConsulta{
	
	private static final Logger logger = Logger.getLogger(DecretosConsulta.class);

	/**
	 * Constantes
	 */
	private static final String _DOC_LIBRO_DECRETOS = "Listado de Decretos";
	private static final String _DOC_CONTRAPORTADA = Constants.TIPODOC.CONTRAPORTADA;
	private static final String _DOC_DECRETO = Constants.DECRETOS._DOC_DECRETO;
	private static final String _TRAMITE_FIRMAS = Constants.DECRETOS._TRAM_FIRMAS_Y_TRALADO;
	
	/**
	 * Variables (Parámetros)
	 */
	protected Date dFechaIniDec = null;
	protected Date dFechaFinDec = null;
	protected int iInicioDec = Integer.MIN_VALUE;
	protected int iFinDec = Integer.MIN_VALUE;
	protected int iPrimerDecreto = Integer.MIN_VALUE;
	protected int iUltimoDecreto = Integer.MIN_VALUE;
	
	/**
	 * Validación y obtención de los parámetros
	 */
	public boolean validarFechas(IRuleContext rulectx) throws ISPACRuleException {
		
		String sNumexp = null;
		IEntitiesAPI entitiesAPI = null;
		
		try{
			entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			sNumexp = rulectx.getNumExp();
			
			//Recuperamos los parámetros de filtrado
			IItemCollection colParametros = entitiesAPI.getEntities("SGD_LIBRO_DECRETOS", sNumexp);
			if (!colParametros.iterator().hasNext()){
				rulectx.setInfoMessage("Es necesario rellenar los parámetros de busqueda en la " +
						"pestaña Libro decretos");
				return false;
			}
			IItem itemParametros = (IItem)colParametros.iterator().next();
			
			dFechaIniDec = itemParametros.getDate("FECHA_INICIO_DECRETO");
			dFechaFinDec = itemParametros.getDate("FECHA_FIN_DECRETO");
			iInicioDec = itemParametros.getInt("NUM_DECRETO_INICIO");
			iFinDec = itemParametros.getInt("NUM_DECRETO_FIN");
			
			if (null == dFechaIniDec && null == dFechaFinDec
					&& iInicioDec == Integer.MIN_VALUE && iFinDec == Integer.MIN_VALUE){
				rulectx.setInfoMessage("Es necesario incluir algún parámetro de búsqueda");
				return false;
			}
			
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al realizar las validaciones", e);
		}
		return true;
	}
	
	/**
	 * Generación del libro de decretos
	 */
	public Object generarLibro(IRuleContext rulectx) throws ISPACRuleException {

		int idDocumento = Integer.MIN_VALUE;
		
        String sInfopagRde = null;
        String sRutaPortada = null;
        
        StringBuffer sbQueryDecretos = null;
        
        IItem itemDocPortadaLibro = null;
        IItem itemDocContraportadaLibro = null;
        IItem itemDocDecreto = null;
        
        File filePortada = null;
        File fileContraPortada = null;
        File fileLibro = null;
        
        ArrayList<String> listFicheros = null;
        
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
	  	    IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			/*********************************************************************
			 * OBTENCIÓN DE LOS DOCUMENTOS DE DECRETO
			 ********************************************************************/
			//Obtenemos los decretos que cumplan con las condiciones de búsqueda
			//Tenemos que ir montando la query en función de los parámetros recibidos
			sbQueryDecretos = new StringBuffer();
			sbQueryDecretos.append(" WHERE DOC.NUMEXP = DCR.NUMEXP");
			sbQueryDecretos.append(" AND DOC.NOMBRE = '" + _DOC_DECRETO + "'");
			sbQueryDecretos.append(" AND DOC.EXTENSION_RDE = '" + Constants._EXTENSION_PDF + "'");
			sbQueryDecretos.append(" AND DOC.FAPROBACION IS NOT NULL");
			sbQueryDecretos.append(" AND (DCR.INCLUIDO_LIBRO IS NULL OR DCR.INCLUIDO_LIBRO = '0')");
			sbQueryDecretos.append(" AND DCR.FECHA_DECRETO IS NOT NULL");
			sbQueryDecretos.append(" AND DCR.NUMERO_DECRETO IS NOT NULL");
			//Teresa
			//sbQueryDecretos.append(" AND DCR.DPTO_SERVICIO='Personal'");
			//sbQueryDecretos.append(" AND (DCR.EXTRACTO_DECRETO LIKE '%Productividad Singularizada%' OR DCR.EXTRACTO_DECRETO LIKE '%Productividad individualizada%')");
			/*sbQueryDecretos.append(" AND UPPER(DCR.EXTRACTO_DECRETO) LIKE upper('Product%') " +
					"AND UPPER(DCR.EXTRACTO_DECRETO) NOT LIKE upper('%variable%') " +
					"AND UPPER(DCR.EXTRACTO_DECRETO) NOT LIKE upper('%normalizada%') " +
					"AND UPPER (DCR.EXTRACTO_DECRETO) NOT LIKE upper('%m%dulos%')");
			
			sbQueryDecretos.append(" AND UPPER(DCR.EXTRACTO_DECRETO) LIKE upper('Product%')" +
					" AND UPPER (DCR.EXTRACTO_DECRETO) LIKE upper('%singula%')");*/
			
			sbQueryDecretos.append(" AND UPPER(DCR.EXTRACTO_DECRETO) LIKE upper('Product%')");
			//Fin teresa
			if (null != dFechaIniDec){
				sbQueryDecretos.append(" AND DCR.FECHA_DECRETO >= DATE('" + dFechaIniDec + "')");
			}
			if (null != dFechaFinDec){
				sbQueryDecretos.append(" AND DCR.FECHA_DECRETO <= DATE('" + dFechaFinDec + "')");
			}
			if (iInicioDec != Integer.MIN_VALUE){
				sbQueryDecretos.append(" AND DCR.NUMERO_DECRETO >= " + iInicioDec);
			}
			if (iFinDec != Integer.MIN_VALUE){
				sbQueryDecretos.append(" AND DCR.NUMERO_DECRETO <= " + iFinDec);
			}
			sbQueryDecretos.append(" AND TRA.ID = DOC.ID_TRAMITE_PCD");
			sbQueryDecretos.append(" AND TRA.NOMBRE = '" + _TRAMITE_FIRMAS + "'");
			sbQueryDecretos.append(" ORDER BY DCR.NUMERO_DECRETO ASC");
			
			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");
			factory.addTable("SGD_DECRETO", "DCR");
			factory.addTable("SPAC_P_TRAMITES", "TRA");
	
			//Realizamos la query
			CollectionDAO collectionJoin =
				factory.queryTableJoin(cct.getConnection(), sbQueryDecretos.toString());
			collectionJoin.disconnect();
			
			listFicheros = new ArrayList<String>();
        	boolean primero = true;
        	int iNumDecreto = Integer.MIN_VALUE;
	        
	        //Recorremos los documentos obtenidos
	        while (collectionJoin.next())
	        {
	        	itemDocDecreto = (IItem) collectionJoin.value();
	        	sInfopagRde = itemDocDecreto.getString("DOC:INFOPAG_RDE");
	        	iNumDecreto = itemDocDecreto.getInt("DCR:NUMERO_DECRETO");
	        	
	        	if (primero){
	        		iPrimerDecreto = iNumDecreto;
	        		primero = false;
	        	}
	        	iUltimoDecreto = iNumDecreto;
	        	
	        	listFicheros.add(sInfopagRde);
	        }
	        
	        //Limpiamos memoria
	        collectionJoin = null;
	        System.gc();
	        
	        /*********************************************************************
			 * GENERAMOS UN DOCUMENTO CON LA PLANTILLA, LO CONVERTIMOS EN PDF Y LO INSERTAMOS EL PRIMERO DE LA LISTA
			 ********************************************************************/
	        //Obtenemos el tipo de documento
			itemDocPortadaLibro = crearDocumento(rulectx, _DOC_LIBRO_DECRETOS, _DOC_LIBRO_DECRETOS);
			
			//Formateamos la plantilla
			String sInfopag = itemDocPortadaLibro.getString("INFOPAG");
			idDocumento = itemDocPortadaLibro.getKeyInt();
			
			//Convertimos a pdf la plantilla
			sRutaPortada = DocumentConverter.convert2PDF(invesFlowAPI, sInfopag, Constants._EXTENSION_ODT);
	        //Ponemos el pdf de la portada como el primero de la lista
			filePortada = new File(sRutaPortada);
        	
        	/*********************************************************************
			 * IDEM CON LA CONTRAPORTADA PERO AL FINAL
			 ********************************************************************/
        	//Obtenemos el tipo de documento
        	itemDocContraportadaLibro = crearDocumento(rulectx, _DOC_CONTRAPORTADA, _DOC_CONTRAPORTADA);
			
        	//Convertimos a pdf la plantilla
			String sInfopagContra = itemDocContraportadaLibro.getString("INFOPAG");
			String sRutaContraPortada = DocumentConverter.convert2PDF(invesFlowAPI, sInfopagContra, Constants._EXTENSION_ODT);
			
	        //Ponemos el pdf de la portada como el último de la lista
			fileContraPortada = new File(sRutaContraPortada);
        	
        	//Borramos el documento de contraportada
        	entitiesAPI.deleteDocument(itemDocContraportadaLibro);
        	
        	//Limpiamos memoria antes de concatenar
        	System.gc();
	  		
	        /*********************************************************************
			 * GENERACIÓN DEL LIBRO DE DECRETOS
			 ********************************************************************/
	  		//Generamos el libro de decretos y lo sustituimos la plantilla por este
        	fileLibro = PdfUtil.concatenarPublicacion(cct, listFicheros, filePortada, fileContraPortada, PdfWriter.PageModeUseOutlines);

        	//Protegemos el documento de Copiar, Pegar, Imprimir, etc
//        	PdfUtil.limitarPermisosConPassword(fileLibro, "dipucr");
    		
    		DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocumento, fileLibro, Constants._EXTENSION_PDF, _DOC_LIBRO_DECRETOS);
    		
    		if(filePortada != null && filePortada.exists()) filePortada.delete();
    		if(fileLibro != null && fileLibro.exists()) fileLibro.delete();
    		if(fileContraPortada != null && fileContraPortada.exists()) fileContraPortada.delete();
    		
		}
		catch (Exception e) {
			
			throw new ISPACRuleException("Error al generar el libro de decretos", e);
		}
		return null;
	}


	/**
	 * Método que formatea la portada del libro, añadiendo los parámetros
	 * @param component
	 */
	private void formatearPortada(XComponent xComponent) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		int iDia = Integer.MIN_VALUE;
		int iMes = Integer.MIN_VALUE;
		int iAnyo = Integer.MIN_VALUE;
		String sMes = null;
		Calendar c = null;
		
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		
		//Buscamos la cadena de parámetros
		XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
		XReplaceDescriptor xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
        
		//Primer decreto y último decreto
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DECRETO_INICIO]", String.valueOf(iPrimerDecreto));
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DECRETO_FIN]", String.valueOf(iUltimoDecreto));
		
		//Fecha inicio
		if (null != dFechaIniDec){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", sdf.format(dFechaIniDec));
			c = DateUtil.getCalendar(dFechaIniDec);
			//Dia de la fecha inicio
			iDia = c.get(Calendar.DAY_OF_MONTH);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", String.valueOf(iDia));
			//Mes de la fecha inicio
			iMes = c.get(Calendar.MONTH) + 1;
//			sMes = DipucrCommonFunctions.getNombreMes(iMes).toUpperCase();
			sMes = DipucrCommonFunctions.getNombreMes(iMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", sMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", sMes);
			//Año de la fecha inicio
			iAnyo = c.get(Calendar.YEAR);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", String.valueOf(iAnyo));
		}
		else{
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", "");
		}
		
		//Fecha fin
		if (null != dFechaFinDec){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", sdf.format(dFechaFinDec));
			c = DateUtil.getCalendar(dFechaFinDec);
			//Dia de la fecha inicio
			iDia = c.get(Calendar.DAY_OF_MONTH);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_FIN]", String.valueOf(iDia));
			//Mes de la fecha inicio
			iMes = c.get(Calendar.MONTH) + 1;
			sMes = DipucrCommonFunctions.getNombreMes(iMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_FIN]", sMes);
		}
		else{
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_FIN]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_FIN]", "");
		}
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
	
	
	/**
	 * 
	 * @param rulectx Contexto de la regla
	 * @param listaAnuncios Lista de anuncios de la factura
	 * @param nombreFichero Nombre del fichero
	 * @param nombrePlantilla Nombre de la plantilla
	 * @throws Exception
	 * @author Felipe-ecenpri
	 */
	@SuppressWarnings("rawtypes")
	private IItem crearDocumento(IRuleContext rulectx, String nombreFichero, String nombrePlantilla)
			throws Exception{
		
		IItem entityDoc = null;
		OpenOfficeHelper ooHelper = null;
		IItemCollection collection = null;
		Iterator it = null;
		XComponent xComponent = null;
		File file = null;
		File file1 = null;
		String nombreCompleto = null;
		
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------
			
			// Crear un nuevo documento de factura
			DocumentosUtil.generarDocumento(rulectx, nombrePlantilla, nombrePlantilla, "borrar");
	    	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantilla + " - borrar");
	    	file1 = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//Añadimos los datos de fechas y decretos sólo si se trata de la portada
			if (nombrePlantilla.equals(_DOC_LIBRO_DECRETOS)){
				formatearPortada(xComponent);
			}
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			file1.delete();
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, nombrePlantilla, DocumentosUtil.BUSQUEDA_EXACTA, false);

			if (null == nombreFichero || nombreFichero.equals("")){
				nombreCompleto = nombrePlantilla;
			}
			else{
				nombreCompleto = nombrePlantilla + " - " + nombreFichero;
			}

			entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, nombreCompleto, file, Constants._EXTENSION_ODT);
			file.delete();
			//Borra los documentos intermedios del gestor documental
	        collection = DocumentosUtil.getDocumentsByDescripcion(rulectx.getNumExp(), rulectx, "borrar");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	entitiesAPI.deleteDocument(doc);
	        }
		}
		catch(Exception e){
			logger.error("Error al crear el documento " + nombreCompleto + "." + e.getMessage(), e);
			throw new Exception("Error al crear el documento " + nombreCompleto + "." + e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
		return entityDoc;
	}

}
