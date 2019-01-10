package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.gendoc.converter.DocumentConverter;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.DateUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.cxf.configuration.security.DNConstraintsType;

import com.lowagie.text.pdf.PdfWriter;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe ticket #163]
 * [eCenpri-Felipe ticket #911] Reestructuración de todo el código para admitir la firma de la diligencia
 * Clase para la generación del libro de actas
 * @author Felipe
 * @since 17.05.2011
 */
public class LibroActas
{
	/**
	 * Constantes
	 */
	private static final String _DOC_LIBRO_ACTAS = Constants.TIPODOC.LIBRO_ACTAS;
	private static final String _DOC_LIBRO_ACTAS_DILIGENCIA = Constants.TIPODOC.LIBRO_ACTAS_DILIG;
	private static final String _DOC_DILIG_LIBACTAS_JUNTA = Constants.TIPODOC.LIBRO_ACTAS_DILIG_JUNTA;
	private static final String _DOC_DILIG_LIBACTAS_COMISION = Constants.TIPODOC.LIBRO_ACTAS_DILIG_COMISION;
	private static final String _DOC_CONTRAPORTADA = Constants.TIPODOC.CONTRAPORTADA;
	private static final String _COD_TRAM_DILIGENCIA = Constants.SECRETARIATRAMITES.COD_TRAM_LIBROACTAS_DILIG;
	
	/**
	 * Variables (Parámetros)
	 */
	protected Date dFechaIniActa = null;
	protected Date dFechaFinActa = null;
	protected String sOrgano = null;
	protected String sArea = null;
	protected String sPrimerActa = null;
	protected String sUltimoActa = null;
	protected ArrayList<String> listFicheros = null;

	protected IEntitiesAPI entitiesAPI = null;
	protected IInvesflowAPI invesFlowAPI = null;

	
	//Históricos
	boolean primero = true;
	
	/**
	 * Validación y obtención de los parámetros
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public boolean validarParametros(IRuleContext rulectx) throws ISPACRuleException {
		
		String numexp = null;
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			//*********************************************
			
			numexp = rulectx.getNumExp();
			String strQuery = "WHERE NUMEXP = '" + numexp + "'";
			IItemCollection collection = entitiesAPI.queryEntities("SECR_LIBRO_ACTAS", strQuery);
			
			if (collection.toList().size() == 0){
				rulectx.setInfoMessage("Es necesario incluir ambas fechas de búsqueda " +
					 "en la pestaña Libro de Actas.");
				return false;
			}
			else{
				IItem itemLibroActas = (IItem) collection.toList().get(0);
				dFechaIniActa = itemLibroActas.getDate("FECHA_INICIO_ACTA");
				dFechaFinActa = itemLibroActas.getDate("FECHA_FIN_ACTA");
				
				if (null == dFechaIniActa || null == dFechaFinActa)
				{
					rulectx.setInfoMessage("Es necesario incluir ambas fechas de búsqueda " +
						 "en la pestaña Libro de Actas.");
					return false;
				}
				else{
					//NUEVO: OBTENEMOS EL ÓRGANO Y EL ÁREA DE LA DEFINICIÓN DEL PROCEDIMIENTO
					//Estos datos estarán en el campo OBJETO de la tabla SPAC_CT_PROCEDIMIENTOS
					IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
					int pcdId = itemExpediente.getInt("ID_PCD");
					IItem itemProcedimiento = procedureAPI.getProcedureById(pcdId);
					String sOrganoArea = itemProcedimiento.getString("CTPROCEDIMIENTOS:OBJETO");
					
					if (StringUtils.isEmpty(sOrganoArea)){
						rulectx.setInfoMessage("Se ha producido un error al generar el Libro de Actas.\n" + 
								"Es necesario configurar el Órgano en la definición del procedimiento.\n" +
								"Hable con su administrador.");
						return false;
					}
					else{
						String [] arrOrganoArea = sOrganoArea.split("-");
						if (arrOrganoArea.length == 1){
							sOrgano = arrOrganoArea[0];
						}
						else{
							sOrgano = arrOrganoArea[0];
							sArea = arrOrganoArea[1];
						}
						
						itemLibroActas.set("ORGANO", sOrgano);
						itemLibroActas.set("AREA", sArea);
						itemLibroActas.store(cct);
					}
				}
			}
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al realizar las validaciones", e);
		}
		return true;
	}
		
	/**
	 * Generación del libro de actas
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object generarLibro(IRuleContext rulectx, boolean bLimitarPermisos,
			String password, int tipoVisualizacion) throws ISPACRuleException {
		
		return generarLibroActas(rulectx, bLimitarPermisos, password, PdfWriter.PageModeUseOutlines, false);
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
		
		return generarLibroActas(rulectx, false, null, PdfWriter.PageModeUseOutlines, true);
	}
	
	/**
	 * Generación del libro de actas
	 * @param rulectx
	 * @param bLimitarPermisos
	 * @param password
	 * @param tipoVisualizacion
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object generarLibroActas(IRuleContext rulectx, boolean bLimitarPermisos,
			String password, int tipoVisualizacion, boolean bPreviaLibroYFirmaDiligencia) throws ISPACRuleException {

		int idDocumento = Integer.MIN_VALUE;
		
        String sRutaPortada = null;
        
        IItem itemDocDiligencia = null;
        IItem itemDocPortadaLibro = null;
        IItem itemDocContraportadaLibro = null;
        IItem itemDocLibroActas = null;
        
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
			 * OBTENCIÓN DE LOS DOCUMENTOS DE ACTA
			 ********************************************************************/
			calcularActasLibroHistorico(cct);
			
	        
			/*********************************************************************
			 * GENERAMOS LA DILIGENCIA A PARTIR DE LA PLANTILLA
			 ********************************************************************/
			//Creación del documento
	        //Tendremos dos plantillas en función del Órgano
	        //Una para PLENO y JGOB; otra para MESA y COMISIÓN
			if (bPreviaLibroYFirmaDiligencia){
		        if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_PLENO)
		        	|| sOrgano.equals(Constants.SECRETARIAPROC.TIPO_JUNTA))
		        {
		        	itemDocDiligencia = crearDocumento
		        		(rulectx, _DOC_DILIG_LIBACTAS_JUNTA, _DOC_DILIG_LIBACTAS_JUNTA);	        	
		        }
		        else{
		        	itemDocDiligencia = crearDocumento
		        		(rulectx, _DOC_DILIG_LIBACTAS_COMISION, _DOC_DILIG_LIBACTAS_COMISION);
		        }
		        itemDocDiligencia.set("NOMBRE", _DOC_LIBRO_ACTAS_DILIGENCIA);
		        itemDocDiligencia.set("DESCRIPCION", _DOC_LIBRO_ACTAS_DILIGENCIA);
		        int tipoDocDiligencia = DocumentosUtil.getTipoDoc(cct, _DOC_LIBRO_ACTAS_DILIGENCIA, DocumentosUtil.BUSQUEDA_EXACTA, false);
		        itemDocDiligencia.set("ID_TPDOC", tipoDocDiligencia);
		        itemDocDiligencia.store(cct);
			}
			else{
				try{
					itemDocDiligencia = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_LIBRO_ACTAS_DILIGENCIA);
					if (null == itemDocDiligencia){
						throw new ISPACRuleException("No existe el documento de diligencia firmada");
					}
				}
				catch(Exception ex){
	    			throw new ISPACRuleException("No existe el documento de diligencia firmada");
	    		}
				//La ponemos como primer documento de la lista de ficheros para que aparezca en el libro
				String sInfopagDiligencia = itemDocDiligencia.getString("INFOPAG_RDE");
				if (StringUtils.isEmpty(sInfopagDiligencia))
					throw new ISPACRuleException("No existe el documento de diligencia firmada");
				
				listFicheros.add(0, sInfopagDiligencia);
			}
			
			
	        /*********************************************************************
			 * GENERAMOS UN DOCUMENTO CON LA PORTADA Y LO CONVERTIMOS EN PDF
			 ********************************************************************/
	        //Obtenemos el tipo de documento
			itemDocPortadaLibro = crearDocumento(rulectx, _DOC_LIBRO_ACTAS, _DOC_LIBRO_ACTAS);
			
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
        	
	  		
	        /*********************************************************************
			 * GENERACIÓN DEL LIBRO DE ACTAS
			 ********************************************************************/
	  		//Generamos el libro de actas y lo sustituimos la plantilla por este
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
        	
    		itemDocLibroActas = DocumentosUtil.anexaDocumento(rulectx, rulectx.getTaskId(), idDocumento, fileLibro, Constants._EXTENSION_PDF, _DOC_LIBRO_ACTAS);
    		itemDocLibroActas.set("NOMBRE", _DOC_LIBRO_ACTAS); //[eCenpri-Felipe #571]
    		itemDocLibroActas.store(cct);

    		if(filePortada != null && filePortada.exists()) filePortada.delete();
    		if(fileLibro != null && fileLibro.exists()) fileLibro.delete();
    		if(fileContraPortada != null && fileContraPortada.exists()) fileContraPortada.delete();
    		
		}
		catch (Exception e) {
			
			throw new ISPACRuleException("Error al generar el libro de actas", e);
		}
		return null;
	}
	

	/**
	 * 
	 * Calculo de los documentos de acta que formarán parte del libro
	 * @param cct
	 * @return
	 * @throws ISPACException
	 */
	private void calcularActasLibroHistorico(IClientContext cct)
			throws ISPACException {
		
		String sInfopagRde;
		
		if(listFicheros == null)
			listFicheros = new ArrayList<String>();
		
		String sNumActa = null;
		
		StringBuffer consulta = new StringBuffer(" WHERE ");		
		consulta.append(" FECHA BETWEEN DATE('" + dFechaIniActa + "')");
		consulta.append(" AND DATE('" + dFechaFinActa + "')");
		consulta.append(" AND ORGANO = '" + sOrgano + "'");
		if (null != sArea && !sArea.equals("")){
			consulta.append(" AND AREA = '" + sArea + "'");
		}
		consulta.append(" ORDER BY FECHA");
		
		IItemCollection sesionCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SECR_SESION, consulta.toString());
		for(Object sesionO : sesionCollection.toList()){
			IItem sesion = (IItem) sesionO;
			String numexp = sesion.getString("NUMEXP");
			
			IItemCollection docsCollection = null;
			
			StringBuffer consultaDoc = new StringBuffer();
			String ordenDoc = "";
			
			if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_PLENO)){
				
				consultaDoc.append(" (NOMBRE = '" + Constants.TIPODOC.ACTA_PLENO + "' OR ");
				consultaDoc.append(" NOMBRE = '" + Constants.TIPODOC.ACTA_PLENO_AUDIO + "')");//[dipucr-Felipe #663]
				consultaDoc.append(" AND FAPROBACION IS NOT NULL");
				
				StringBuffer consultaTram = new StringBuffer();
				consultaTram.append(" ((NOMBRE = '" + Constants.SECRETARIATRAMITES.SESION_PLENO_DEBATES + "'");
				consultaTram.append(" OR NOMBRE = '" + Constants.SECRETARIATRAMITES.SESION_PLENO_EXTRACTO + "'");
				consultaTram.append(" OR NOMBRE = '" + Constants.SECRETARIATRAMITES.SESION_PLENO_AUDIO + "'");//[dipucr-Felipe #663]
				consultaTram.append(" OR NOMBRE = '" + Constants.SECRETARIATRAMITES.SESION_PLENO_AUDIO_BIS + "'");
				//[dipucr-Felipe #663] En pruebas, y no sé si en alguna entidad más, se llama distinto el trámite
				consultaTram.append(" OR NOMBRE = '" + Constants.SECRETARIATRAMITES.SESION_NO_CELEBRADA + "'))"); 
				
				IItemCollection dtTramitesCollection = TramitesUtil.getTramites(cct, numexp, consultaTram.toString(), "");
				consultaDoc.append(" AND ID_TRAMITE_PCD IN ");				
				ArrayList<String> listaId = new ArrayList<String>();
				for(Object tramitesO : dtTramitesCollection.toList()){				
					IItem tramite = (IItem)tramitesO;
					String id_tram_pcd = tramite.getString("ID_TRAM_PCD");
				
					listaId.add(id_tram_pcd);					
				}
				consultaDoc.append (DipucrCommonFunctions.convertirListaInSQL(listaId));
				ordenDoc = " NUMEXP, ID_TRAMITE_PCD DESC LIMIT 1";
			}
			else{

				consultaDoc.append(" NOMBRE = '" + getDocumentoByOrgano() + "'");
				consultaDoc.append(" AND FAPROBACION IS NOT NULL");
				ordenDoc = " ID DESC LIMIT 1";				
			}			
			
			docsCollection = DocumentosUtil.getDocumentos(cct, numexp, consultaDoc.toString(), ordenDoc);
			
			if(docsCollection != null && docsCollection.next()){
				IItem docActa = (IItem) docsCollection.iterator().next();
				sInfopagRde = docActa.getString("INFOPAG_RDE");
				sNumActa = sesion.getString("NUMCONV");
				
				if (primero){
					sPrimerActa = sNumActa;
					primero = false;
				}
				sUltimoActa = sNumActa;

				listFicheros.add(sInfopagRde);
			}
		}

	}
	
	
	/**
	 * Borrar el libro de actas del primer trámite
	 * Evitamos así sobrecargar la memoria de la máquina con dos documentos grandes 
	 * @param rulectx
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object eliminarPreviaLibroActas(IRuleContext rulectx) throws ISPACRuleException{
	
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    entitiesAPI = cct.getAPI().getEntitiesAPI();
	  	    //*********************************************
	  	    
	  	    StringBuffer sbQuery = new StringBuffer();
			sbQuery.append(" WHERE DOC.NUMEXP = '" + rulectx.getNumExp() + "'");
			sbQuery.append(" AND DOC.NOMBRE = '" + _DOC_LIBRO_ACTAS + "'");
			sbQuery.append(" AND DOC.ID_TRAMITE_PCD = PTRAM.ID");
			sbQuery.append(" AND PTRAM.ID_CTTRAMITE = CTTRAM.ID");
			sbQuery.append(" AND CTTRAM.COD_TRAM = '" + _COD_TRAM_DILIGENCIA + "'");
			
			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");
			factory.addTable("SPAC_P_TRAMITES", "PTRAM");
			factory.addTable("SPAC_CT_TRAMITES", "CTTRAM");

			//Realizamos la query
			DbCnt cnt = cct.getConnection();
			CollectionDAO collectionJoin = factory.queryTableJoin(cnt, sbQuery.toString());
			collectionJoin.disconnect();
			
			cct.releaseConnection(cnt);
	  	    
			//Obtenemos el documento y lo eliminamos
	        if (collectionJoin.next())
	        {
	        	IItem itemDocLibroActasTram1 = (IItem) collectionJoin.value();
	        	int idDoc = itemDocLibroActasTram1.getInt("DOC:ID");
	        	
	        	entitiesAPI.deleteDocument(idDoc);
	        }
		
		}
		catch (Exception e) {	
			throw new ISPACRuleException("Error el documento previa de " +
					"libro de actas del primer trámite", e);
		}
		return null;
	}


	/**
	 * Método que formatea la portada del libro, añadiendo los parámetros
	 * @param entitiesAPI 
	 * @param component
	 * @throws ISPACRuleException 
	 */
	private void formatearVariables(IEntitiesAPI entitiesAPI, XComponent xComponent) 
			throws ISPACRuleException {
		
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
        
		//Primer acta y último acta
//		String sDescOrgano = getSustituto(entitiesAPI, "SECR_VLDTBL_ORGANOS", sOrgano);
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ORGANO]", getDescripcionOrgano(entitiesAPI, false));
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ORGANO2]", getDescripcionOrgano(entitiesAPI, true));
//		String sDescArea = getSustituto(entitiesAPI, "SECR_VLDTBL_AREAS", sArea);
//		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[AREA]", sDescArea);
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ACTA_INICIO]", String.valueOf(sPrimerActa));
		reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ACTA_FIN]", String.valueOf(sUltimoActa));
		
		//Fecha inicio
		if (null != dFechaIniActa){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", sdf.format(dFechaIniActa));
			c = DateUtil.getCalendar(dFechaIniActa);
			//Dia de la fecha inicio
			iDia = c.get(Calendar.DAY_OF_MONTH);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", String.valueOf(iDia));
			//Mes de la fecha inicio
			iMes = c.get(Calendar.MONTH) + 1;
//			sMes = DipucrCommonFunctions.getNombreMes(iMes).toUpperCase();
			sMes = DipucrCommonFunctions.getNombreMes(iMes);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", sMes);
//			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", sMes);
			//Año de la fecha inicio
			iAnyo = c.get(Calendar.YEAR);
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", String.valueOf(iAnyo));
		}
		else{
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[DIA_INICIO]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES_INICIO]", "");
//			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[MES]", "");
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[ANYO]", "");
		}
		
		//Fecha fin
		if (null != dFechaFinActa){
			reemplazarCadena(xReplaceable, xReplaceDescriptor, "[FECHA_FIN]", sdf.format(dFechaFinActa));
			c = DateUtil.getCalendar(dFechaFinActa);
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
	 * [eCenpri-Felipe #911]
	 * @param rulectx Contexto de la regla
	 * @param listaAnuncios Lista de anuncios de la factura
	 * @param nombreTipoDoc Nombre del tipo de documento
	 * @param nombrePlantilla Nombre de la plantilla
	 * @throws Exception
	 * @author Felipe-ecenpri
	 */
	@SuppressWarnings("rawtypes")
	private IItem crearDocumento(IRuleContext rulectx, String nombreTipoDoc, String nombrePlantilla)
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
			DocumentosUtil.generarDocumento(rulectx, nombreTipoDoc, nombrePlantilla, "borrar");
	    	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantilla + " - borrar");
	    	file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null); //[dipucr-Felipe 3#249]
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//Añadimos los datos de fechas y actas sólo si se trata de la portada
			if (!nombreTipoDoc.equals(_DOC_CONTRAPORTADA)){
				formatearVariables(entitiesAPI, xComponent);
			}
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(Constants._EXTENSION_ODT);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			file1.delete();
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, nombreTipoDoc, DocumentosUtil.BUSQUEDA_EXACTA, false);

			if (StringUtils.isEmpty(nombrePlantilla)){
				nombreCompleto = nombreTipoDoc;
			}
			else{
				nombreCompleto = nombreTipoDoc + " - " + nombrePlantilla;
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
			throw new ISPACRuleException("Error al crear el Libro de Actas: " + e.getMessage());
		}
		finally{
			if(ooHelper!= null) ooHelper.dispose();
		}
		return entityDoc;
	}

	
	/**
     * En función del Órgano devuelve el nombre del documento a buscar
     * @return
     */
	private String getDocumentoByOrgano() {
		
		String sDocumento = null;
		
		if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_PLENO)){//Sin usar
			sDocumento = Constants.TIPODOC.ACTA_PLENO;
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_JUNTA)){
			sDocumento = Constants.TIPODOC.ACTA_JUNTA;
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_MESA)){
			sDocumento = Constants.TIPODOC.ACTA_MESA;
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_COMISION)){
			sDocumento = Constants.TIPODOC.ACTA_COMISION;
		}
		return sDocumento;
	}
	
	/**
     * En función del Órgano devuelve la descripción
     * @return
	 * @throws ISPACRuleException 
     */
	private String getDescripcionOrgano(IEntitiesAPI entitiesAPI, boolean bConArticulo) throws ISPACRuleException {
		
		StringBuffer sbDescripcion = new StringBuffer();
		
		if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_PLENO)){
			if (bConArticulo) sbDescripcion.append("DEL ");
			sbDescripcion.append("PLENO");
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_JUNTA)){
			if (bConArticulo) sbDescripcion.append("DE LA ");
			sbDescripcion.append("JUNTA DE GOBIERNO");
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_MESA)){
			if (bConArticulo) sbDescripcion.append("DE LA ");
			sbDescripcion.append("MESA DE CONTRATACIÓN");
		}
		else if (sOrgano.equals(Constants.SECRETARIAPROC.TIPO_COMISION)){
			if (bConArticulo) sbDescripcion.append("DE LA ");
			sbDescripcion.append("COMISIÓN INFORMATIVA PERMANENTE");
			sbDescripcion.append(" DE ");
			String sDescArea = getSustituto(entitiesAPI, "SECR_VLDTBL_AREAS", sArea);
			sbDescripcion.append(sDescArea.toUpperCase());
		}
		return sbDescripcion.toString();
	}
	
	/**
     * Devuelve el sustituto de valor de la tabla pasada como parámetro
     * @return
	 * @throws ISPACRuleException 
     */
	private String getSustituto(IEntitiesAPI entitiesAPI, String entidad, String valor) 
			throws ISPACRuleException
	{
		String resultado = null;
		
		try{
	        String strQuery = "WHERE VALOR = '" + valor + "'";
	        IItemCollection collection = entitiesAPI.queryEntities(entidad, strQuery);
	        if (collection.toList().size() == 1){
	        	IItem item = (IItem)collection.toList().get(0);
	        	resultado = item.getString("SUSTITUTO");
	        }
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al obtener el sustituto del valor " +
					valor + " en la tabla de validación " + entidad + ": " + e.getMessage());
		}
        return resultado;
	}
}
