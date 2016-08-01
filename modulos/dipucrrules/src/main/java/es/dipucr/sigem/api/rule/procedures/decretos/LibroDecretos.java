package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.lowagie.text.pdf.PdfWriter;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe ticket #164]
 * Clase para la generación del libro de decretos
 * Se rehace la clase de Telefónica por tratarse de un código ininteligible
 * Se pasa el código a una Regla en vez de un action, por ser más cómodo 
 * @author Felipe
 * @since 05.10.2010
 * 
 * [eCenpri-Felipe #903]
 * Reestructuración de todo el código para la generación de una diligencia previa
 * @since 05.06.2013
 */
public class LibroDecretos
{
	
	private static final Logger logger = Logger.getLogger(LibroDecretos.class);

	/**
	 * Constantes
	 */
	private static final String _DOC_LIBRO_DECRETOS = Constants.DECRETOS._DOC_LIBRO_DECRETOS;
	private static final String _DOC_LIBRO_DECRETOS_DILIGENCIA = Constants.DECRETOS._DOC_LIBRO_DECRETOS_DILIGENCIA;
	private static final String _DOC_CONTRAPORTADA = Constants.TIPODOC.CONTRAPORTADA;
	private static final String _TRAMITE_FIRMAS = Constants.DECRETOS._TRAM_FIRMAS_Y_TRALADO;
	private static final String _COD_TRAM_DILIGENCIA = Constants.DECRETOS._COD_TRAM_LIBRODEC_DILIG;
	
	/**
	 * Nombres de variables del sistema
	 */
	private static final String _TITULO_RECHAZADOS_VAR_NAME = "LIBRO_DECRETOS_TITULO_RECHAZADOS";
	
	/**
	 * Variables (Parámetros)
	 */
	protected Date dFechaIniDec = null;
	protected Date dFechaFinDec = null;
	protected int iInicioDec = Integer.MIN_VALUE;
	protected int iFinDec = Integer.MIN_VALUE;
	protected int iPrimerDecreto = Integer.MIN_VALUE;
	protected int iUltimoDecreto = Integer.MIN_VALUE;
	protected String sTituloRechazados = null;
	protected ArrayList<String> listFicheros = null;
	protected ArrayList<IItem> listRechazados = null; //[eCenpri-Felipe #798]
	protected IEntitiesAPI entitiesAPI = null; //[eCenpri-Felipe #798]
	protected IInvesflowAPI invesFlowAPI = null; //[eCenpri-Felipe #798]
	
	//Históricos
	boolean primero = true;
	/**
	 * Validación y obtención de los parámetros
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
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
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object generarLibro(IRuleContext rulectx, boolean bLimitarPermisos,
			String password, int tipoVisualizacion) throws ISPACRuleException {
		
//		return generarLibro(rulectx, bLimitarPermisos, password, tipoVisualizacion, false);
		return generarLibroFinal(rulectx, bLimitarPermisos, password);
	}
	
	/**
	 * 
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @param bPreviaLibroYFirmaDiligencia
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object generarDiligencia(IRuleContext rulectx) throws ISPACRuleException {
		
		return generarLibro(rulectx, false, null, PdfWriter.PageModeUseOutlines, true);
	}
	
	
	/**
	 * Código general para la generación del libro de decretos
	 * y de la diligencia juntos o por separado
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @param bPreviaLibroYFirmaDiligencia
	 * @return
	 * @throws ISPACRuleException
	 */
	private Object generarLibro(IRuleContext rulectx, boolean bLimitarPermisos,
			String password, int tipoVisualizacion, boolean bPreviaLibroYFirmaDiligencia) throws ISPACRuleException {

		int idDocLibro = Integer.MIN_VALUE;
		
        String sRutaPortada = null;
        
        IItem itemDocPortadaLibro = null;
        IItem itemDocContraportadaLibro = null;
        
        File filePortada = null;
        File fileContraPortada = null;
        File fileLibro = null;
        
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    invesFlowAPI = cct.getAPI();
	  	    entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			/*********************************************************************
			 * OBTENCIÓN DE LOS DOCUMENTOS DE DECRETO
			 ********************************************************************/
			calcularDecretosLibroHistorico(cct);
			
			/*********************************************************************
			 * GENERAMOS LA DILIGENCIA A PARTIR DE LA PLANTILLA
			 ********************************************************************/
			
			if (bPreviaLibroYFirmaDiligencia){
		        //Obtenemos el titulo para los decretos rechazados
				sTituloRechazados = ConfigurationMgr.getVarGlobal(cct, _TITULO_RECHAZADOS_VAR_NAME);
				//Generamos el documento
				crearDocumento(rulectx, null, _DOC_LIBRO_DECRETOS_DILIGENCIA);
			}
	        
	        /*********************************************************************
			 * GENERAMOS UN DOCUMENTO CON LA PORTADA Y LO CONVERTIMOS EN PDF
			 ********************************************************************/
	        //Obtenemos el tipo de documento
			itemDocPortadaLibro = crearDocumento(rulectx, _DOC_LIBRO_DECRETOS, _DOC_LIBRO_DECRETOS);
			
			//Formateamos la plantilla
			String sInfopag = itemDocPortadaLibro.getString("INFOPAG");
			idDocLibro = itemDocPortadaLibro.getKeyInt();
			
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
        	
        	/*********************************************************************
			 * ADJUNTAR DILIGENCIA
			 * (sólo si estamos en el 2o trámite y la diligencia ha
			 * sido firmada en el 1er trámite)
			 * Añadir la diligencia al libro e incluirla también como adjunto
			 ********************************************************************/
        	
        	String sInfopagDiligencia = null;
        	
        	if (!bPreviaLibroYFirmaDiligencia){
	        	//Obtenemos el documento de diligencia del primer trámite
        		IItem itemDocDiligenciaTram1 = null;
        		try{
        			itemDocDiligenciaTram1 = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_LIBRO_DECRETOS_DILIGENCIA);
        		}
        		catch(Exception ex){
        			throw new ISPACRuleException("No existe el documento de diligencia firmada");
        		}
				
	        	//La ponemos como primer documento de la lista de ficheros para que aparezca en el libro
				sInfopagDiligencia = itemDocDiligenciaTram1.getString("INFOPAG_RDE");
				if (StringUtils.isEmpty(sInfopagDiligencia))
					throw new ISPACRuleException("No existe el documento de diligencia firmada");
				
	        	listFicheros.add(0, sInfopagDiligencia);
        	}
        	
	  		
	        /*********************************************************************
			 * GENERACIÓN DEL LIBRO DE DECRETOS
			 ********************************************************************/
	  		//Generamos el libro de decretos y lo sustituimos la plantilla por este
        	fileLibro = PdfUtil.concatenarPublicacion(cct, listFicheros, filePortada, fileContraPortada, tipoVisualizacion);

        	//Protegemos el documento de Copiar, Pegar, Imprimir, etc
//        	PdfUtil.limitarPermisosConPassword(fileLibro, "dipucr");
        	if (null != password && !password.isEmpty()){
        		if (bLimitarPermisos){
                	PdfUtil.limitarPermisosConPassword(fileLibro, password);
        		}
        		else{
        			PdfUtil.protegerConPassword(fileLibro, password);
        		}
        	}
        	else{
        		if (bLimitarPermisos){
                	PdfUtil.limitarPermisos(fileLibro);
        		}
        	}
        	
        	//Adjuntamos la diligencia al libro
        	if (!bPreviaLibroYFirmaDiligencia){
        		File fileDiligencia = DocumentosUtil.getFile(cct, sInfopagDiligencia, null, null);
        		PdfUtil.anexarDocumento(fileLibro, fileDiligencia, "Diligencia.pdf");
        		if(fileDiligencia != null && fileDiligencia.exists()) fileDiligencia.delete();
        	}
        	
    		DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocLibro, fileLibro, Constants._EXTENSION_PDF, _DOC_LIBRO_DECRETOS);
    		
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
	 * Código general para la generación del libro de decretos
	 * y de la diligencia juntos o por separado
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @param bPreviaLibroYFirmaDiligencia
	 * @return
	 * @throws ISPACRuleException
	 */
	private Object generarLibroFinal(IRuleContext rulectx, boolean bLimitarPermisos,
			String password) throws ISPACRuleException {

		int idDocLibro = Integer.MIN_VALUE;
		
        IItem itemDocLibroDecretos = null;
        IItem itemDocLibroDecretosTram1 = null;
        IItem itemDocDiligenciaTram1 = null;
        
        File fileLibro = null;
        File fileLibroTram1 = null;
        File fileDiligencia = null;
        File filePortada = null;
        File fileRestoLibro = null;
        
        String sInfopag = null;
        String sInfopagDiligencia = null;
        
        ArrayList<File> listFicherosConcatenar = null;
        
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    invesFlowAPI = cct.getAPI();
	  	    entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			/*********************************************************************
			 * OBTENEMOS LOS DOCUMENTOS DEL PRIMER TRÁMITE
			 ********************************************************************/
			
			//Obtenemos el documento de Libro generado en el primer trámite
			try{
				itemDocLibroDecretosTram1 = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_LIBRO_DECRETOS);
				if (null == itemDocLibroDecretosTram1){
					throw new ISPACRuleException("No existe el documento de libro de decretos del primer trámite");
				}
			}
			catch(Exception ex){
    			throw new ISPACRuleException("No existe el documento de libro de decretos del primer trámite");
    		}
			
			try{
				itemDocDiligenciaTram1 = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_LIBRO_DECRETOS_DILIGENCIA);
				if (null == itemDocDiligenciaTram1){
					throw new ISPACRuleException("No existe el documento de diligencia firmada");
				}
			}
			catch(Exception ex){
    			throw new ISPACRuleException("No existe el documento de diligencia firmada");
    		}
	        
	        /*********************************************************************
			 * CONCATENAMOS LAS PARTES PARA GENERAR EL LIBRO DE DECRETOS
			 * Portada + Diligencia + Resto_del_Libro
			 ********************************************************************/
	        //Obtenemos el tipo de documento
			itemDocLibroDecretos = DocumentosUtil.crearDocumentoTramite(rulectx, _DOC_LIBRO_DECRETOS);
			idDocLibro = itemDocLibroDecretos.getKeyInt();
			
			sInfopag = itemDocLibroDecretosTram1.getString("INFOPAG");
			fileLibroTram1 = DocumentosUtil.getFile(cct, sInfopag, null, null);
			
			sInfopagDiligencia = itemDocDiligenciaTram1.getString("INFOPAG_RDE");
			if (StringUtils.isEmpty(sInfopagDiligencia)){
				throw new ISPACRuleException("No existe el documento de diligencia firmada");
			}
			fileDiligencia = DocumentosUtil.getFile(cct, sInfopagDiligencia, null, null);
			
			//Creamos el archivo del libro
			listFicherosConcatenar = new ArrayList<File>();
			
			//Obtenemos la portada como la primera página del libro del primer trámite
			filePortada = FileTemporaryManager.getInstance().newFile();
			PdfUtil.obtenerSeccion(fileLibroTram1, filePortada, 1, 1);
			listFicherosConcatenar.add(filePortada);
			
			//Añadimos la diligencia
			listFicherosConcatenar.add(fileDiligencia);
			
			//Obtenemos el resto el libro (todo menos la portada)
			fileRestoLibro = FileTemporaryManager.getInstance().newFile();
			PdfUtil.obtenerSeccion(fileLibroTram1, fileRestoLibro, 2, Integer.MAX_VALUE);
			listFicherosConcatenar.add(fileRestoLibro);
			
			//Concatenamos las partes
			fileLibro = PdfUtil.concatenarArchivos(listFicherosConcatenar);
			
        	
        	/*********************************************************************
			 * AÑADIR DILIGENCIA COMO ADJUNTO
			 * Añadir la diligencia al libro e incluirla también como adjunto
			 ********************************************************************/
        	
        	PdfUtil.anexarDocumento(fileLibro, fileDiligencia, "Diligencia.pdf");
        	
	        /*********************************************************************
			 * GENERACIÓN DEL LIBRO DE DECRETOS
			 ********************************************************************/

        	//Protegemos el documento de Copiar, Pegar, Imprimir, etc
//        	PdfUtil.limitarPermisosConPassword(fileLibro, "dipucr");
        	if (null != password && !password.isEmpty()){
        		if (bLimitarPermisos){
                	PdfUtil.limitarPermisosConPassword(fileLibro, password);
        		}
        		else{
        			PdfUtil.protegerConPassword(fileLibro, password);
        		}
        	}
        	else{
        		if (bLimitarPermisos){
                	PdfUtil.limitarPermisos(fileLibro);
        		}
        	}
        	
    		itemDocLibroDecretos = DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocLibro, fileLibro, Constants._EXTENSION_PDF, _DOC_LIBRO_DECRETOS);
    		
    		if(filePortada != null && filePortada.exists()) filePortada.delete();
    		if(fileLibro != null && fileLibro.exists()) fileLibro.delete();
    		if(fileLibroTram1 != null && fileLibroTram1.exists()) fileLibroTram1.delete();
    		if(fileDiligencia != null && fileDiligencia.exists()) fileDiligencia.delete();
    		if(fileRestoLibro != null && fileRestoLibro.exists()) fileRestoLibro.delete();
		}
		catch (Exception e) {
			
			throw new ISPACRuleException("Error al generar el libro de decretos. " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Borrar el libro de decretos del primer trámite
	 * Evitamos así sobrecargar la memoria de la máquina con dos
	 * documentos grandes, que a veces superar los 80 Mb
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object eliminarPreviaLibroDecretos(IRuleContext rulectx) throws ISPACRuleException{
	
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    entitiesAPI = cct.getAPI().getEntitiesAPI();
	  	    //*********************************************
	  	    
	  	    StringBuffer sbQuery = new StringBuffer();
			sbQuery.append(" WHERE DOC.NUMEXP = '" + rulectx.getNumExp() + "'");
			sbQuery.append(" AND DOC.NOMBRE = '" + _DOC_LIBRO_DECRETOS + "'");
			sbQuery.append(" AND DOC.ID_TRAMITE_PCD = PTRAM.ID");
			sbQuery.append(" AND PTRAM.ID_CTTRAMITE = CTTRAM.ID");
			sbQuery.append(" AND CTTRAM.COD_TRAM = '" + _COD_TRAM_DILIGENCIA + "'");
			
			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");
			factory.addTable("SPAC_P_TRAMITES", "PTRAM");
			factory.addTable("SPAC_CT_TRAMITES", "CTTRAM");

			//Realizamos la query
			CollectionDAO collectionJoin =
				factory.queryTableJoin(cct.getConnection(), sbQuery.toString());
			collectionJoin.disconnect();
	  	    
			//Obtenemos el documento y lo eliminamos
	        if (collectionJoin.next())
	        {
	        	IItem itemDocLibroDecretosTram1 = (IItem) collectionJoin.value();
	        	int idDoc = itemDocLibroDecretosTram1.getInt("DOC:ID");
	        	
	        	entitiesAPI.deleteDocument(idDoc);
	        }
		
		}
		catch (Exception e) {	
			throw new ISPACRuleException("Error el documento previa de " +
					"libro de decretos del primer trámite", e);
		}
		return null;
	}

	
	/**
	 * Recupera la lista de decretos que cumplan las condiciones del filtro
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	private void calcularDecretosLibroHistorico(IClientContext cct)
			throws ISPACException {
		
		String sInfopagRde = null;
        String sEstado = null; //[eCenpri-Felipe #798]

        StringBuffer consulta = new StringBuffer(" WHERE ");
        consulta.append(" FECHA_DECRETO IS NOT NULL");
        consulta.append(" AND NUMERO_DECRETO IS NOT NULL");
        if (null != dFechaIniDec){
        	consulta.append(" AND FECHA_DECRETO >= DATE('" + dFechaIniDec + "') ");
        }
        if (null != dFechaFinDec){
        	consulta.append(" AND FECHA_DECRETO < DATE('" + FechasUtil.addDias(dFechaFinDec, 1) + "')");
        }
        if (iInicioDec != Integer.MIN_VALUE){
        	consulta.append(" AND NUMERO_DECRETO >= " + iInicioDec);
		}
		if (iFinDec != Integer.MIN_VALUE){
			consulta.append(" AND NUMERO_DECRETO <= " + iFinDec);
		}
		consulta.append(" ORDER BY ANIO ASC, NUMERO_DECRETO ASC");

		if(listFicheros == null)
			listFicheros = new ArrayList<String>();
		if(listRechazados== null)
			listRechazados = new ArrayList<IItem>();
    	int iNumDecreto = Integer.MIN_VALUE;
    	
        IItemCollection decretosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SGD_DECRETO, consulta.toString());
        for(Object objeto : decretosCollection.toList()){
        	IItem decreto = (IItem) objeto;
        	String numexp_dcr = decreto.getString("NUMEXP");
        	
        	IItem exp = ExpedientesUtil.getExpediente(cct, numexp_dcr);
        	int id_pcd = exp.getInt("ID_PCD");
        	
        	IItemCollection pTramites = entitiesAPI.queryEntities("SPAC_P_TRAMITES", " WHERE NOMBRE = '" + _TRAMITE_FIRMAS + "' AND ID_PCD = " + id_pcd );
        	for (Object tramO : pTramites.toList()){
        		IItem tram = (IItem) tramO;
        		int id_tram = tram.getKeyInt();
        		
        		IItemCollection docDecretoCollection = DocumentosUtil.queryDocumentos(cct, " WHERE NUMEXP = '" + numexp_dcr + "' AND ID_TRAMITE_PCD = " + id_tram);
        	
        		for(Object docDecretoO : docDecretoCollection.toList()){
        			IItem docDecreto = (IItem) docDecretoO;
            	
        			sInfopagRde = docDecreto.getString("INFOPAG_RDE");
        			iNumDecreto = decreto.getInt("NUMERO_DECRETO");
            	
	            	if (primero){
	            		iPrimerDecreto = iNumDecreto;
	            		primero = false;
	            	}
	            	iUltimoDecreto = iNumDecreto;
	            	
	            	//INICIO [eCenpri-Felipe #798]
	            	sEstado = docDecreto.getString("ESTADOFIRMA");
	            	if (sEstado.equals(SignStatesConstants.RECHAZADO)){
	            		listRechazados.add(decreto);
	            	}
	            	else{
	            		listFicheros.add(sInfopagRde);
	            	}
	            	//FIN [eCenpri-Felipe #798]
        		}
        	}        	
        }
        
        //Recorremos los documentos obtenidos
        //Limpiamos memoria
        System.gc();
	}
	
	/**
	 * Formatear el documento inicial de diligencia
	 * @param xComponent
	 * @param bRechazados 
	 * @throws ISPACException 
	 */
	private void formatearVariables(IRuleContext rulectx, XComponent xComponent, boolean bRechazados) throws ISPACException {
		
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
		
		//Si hay decretos rechazados, hacemos la lista
		if (bRechazados && listRechazados.size() > 0){
			
			//Reemplazamos el título de los rechazados
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[TITULO_RECHAZADOS]", sTituloRechazados);
			
			//Vamos insertando los decretos rechazados
			StringBuffer sbListRechazados = new StringBuffer();
	        IItem item = null;
			for (int i=0; i<listRechazados.size(); i++){
				item = listRechazados.get(i);
				sbListRechazados.append(getLineaRechazado(rulectx, item));
				sbListRechazados.append("\r\n");
			}
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[LISTA_RECHAZADOS]", sbListRechazados.toString());
		}
		else{
			//Ponemos los valores a vacío si no hay decretos rechazados
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[TITULO_RECHAZADOS]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[LISTA_RECHAZADOS]", "");
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
	 * [eCenpri-Felipe #798]
	 * Devuelve la línea de texto a mostrar para cada decreto rechazado
	 * @param item
	 * @return
	 * @throws ISPACException 
	 */
	private String getLineaRechazado(IRuleContext rulectx, IItem item) throws ISPACException {
		
		StringBuffer sbTexto = new StringBuffer();
		sbTexto.append("\t\t- Decreto Nº ");
		sbTexto.append(item.getString("NUMERO_DECRETO"));
		sbTexto.append(". Expediente ");
		String sNumexpDecreto = item.getString("NUMEXP");
		sbTexto.append(sNumexpDecreto);
		
		//Obtenemos el nombre del procedimiento
		IItem itemExpedient = ExpedientesUtil.getExpediente(rulectx.getClientContext(), sNumexpDecreto);
		IItem itemProcedure = invesFlowAPI.getProcedure(itemExpedient.getInt("ID_PCD"));
		sbTexto.append(" (");
		sbTexto.append(itemProcedure.getString("NOMBRE"));
		sbTexto.append(")");
		
		return sbTexto.toString();
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
	    	file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null); //[dipucr-Felipe 3#249]
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//Añadimos los datos de fechas y decretos sólo si se trata de la diligencia
			if (nombrePlantilla.equals(_DOC_LIBRO_DECRETOS_DILIGENCIA)){
				formatearVariables(rulectx, xComponent, true);
			}
			else if (nombrePlantilla.equals(_DOC_LIBRO_DECRETOS)){
				formatearVariables(rulectx, xComponent, false);
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
	        ooHelper.dispose();
		}
		catch(Exception e){
			logger.error("Error al crear el documento " + nombreCompleto + "." + e.getMessage(), e);
			throw new Exception("Error al crear el documento " + nombreCompleto + "." + e.getMessage(), e);
		}
		return entityDoc;
	}

}
