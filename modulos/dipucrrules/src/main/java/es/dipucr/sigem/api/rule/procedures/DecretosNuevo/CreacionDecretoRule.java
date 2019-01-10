package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CreacionDecretoRule implements IRule{
	
	
	
	
	
	public static final Logger logger = Logger.getLogger(CreacionDecretoRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		OpenOfficeHelper ooHelper = null;
		try{
			//------------------------------------------------------------------
	        IClientContext cct = rulectx.getClientContext();
	        ooHelper = OpenOfficeHelper.getInstance();
	        String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
	        //------------------------------------------------------------------
	        logger.warn("Inicio carga documento decreto");
	        
	        	        
	        //Creación del trámite 'Creación del Decreto'
	        int codTramiteCreac = TramitesUtil.crearTramite("CREAR_DECRETOS", rulectx);
	        
	        //Monto el decreto y la plantilla de notificaciones.
	        
	        String tipoDocDecreto = DocumentosUtil.getNombreTipoDocByCod(cct, "Decretos");
	        String nombrePlantillaDecretoCabecera = DocumentosUtil.getNombrePlantillaByCod(cct, "Decr-Cab");
	        logger.warn("DECRETO CABECERA INICIO tipoDocDecreto "+tipoDocDecreto);
	        logger.warn("nombrePlantilla "+nombrePlantillaDecretoCabecera);
	        DocumentosUtil.generarDocumento(rulectx, nombrePlantillaDecretoCabecera, tipoDocDecreto, null, rulectx.getNumExp(), codTramiteCreac);
	        String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantillaDecretoCabecera);
        	File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
        	XComponent xComponent = ooHelper.loadDocument("file://" + file.getPath());
    		file.delete();
    		logger.info("FIN CABECERA DECRETO");
    		
    		//CABECERA NOTIFICACIONES
    		String tipoDocNotif = DocumentosUtil.getNombreTipoDocByCod(cct, "Not-Tram-Res");
	        String nombrePlantillaNotiCabecera = DocumentosUtil.getNombrePlantillaByCod(cct, "Plant-Noti-Cab");
    		logger.info("NOTIFICACIONES CABECERA INICIO "+tipoDocNotif);
        	DocumentosUtil.generarDocumento(rulectx, nombrePlantillaNotiCabecera, tipoDocNotif, null, rulectx.getNumExp(), codTramiteCreac);
        	String strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantillaNotiCabecera);
        	File fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
        	XComponent xComponentNotificaciones = ooHelper.loadDocument("file://" + fileNotificaciones.getPath());
        	fileNotificaciones.delete();
    		logger.info("FIN CABECERA NOTIFICACIÓN");
    		
    		//Obtengo el documento del tramite
	        String nombreDoc = DocumentosUtil.getNombrePlantillaByCod(cct, "Cont-Decr-Plant");
	        IItemCollection documentsCollection = DocumentosUtil.getDocumentsByNombre(rulectx.getNumExp(), rulectx, nombreDoc);
	        Iterator<IItem> itDoc = documentsCollection.iterator();
	        String autor = "";
	        String autorInfo = "";
	        while(itDoc.hasNext()){
	        	IItem itDocContenidoDecreto = itDoc.next();
	        	String infopag = itDocContenidoDecreto.getString("INFOPAG");
		        autor = itDocContenidoDecreto.getString("AUTOR");
		        autorInfo = itDocContenidoDecreto.getString("AUTOR_INFO");
	        	
		        //Cuerpo de decreto
	        	file = DocumentosUtil.getFile(cct, infopag, null, null);
	        	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
	    		file.delete();
	    		//Cuerpo de notificaciones
	    		fileNotificaciones = DocumentosUtil.getFile(cct, infopag, null, null);
	    		DipucrCommonFunctions.ConcatenaByFormat(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), extensionEntidad);
	        	fileNotificaciones.delete();
	        }
	        
	        //Pie

	        String nombrePlantillaDecretoPie = DocumentosUtil.getNombrePlantillaByCod(cct, "Decr-Pie");
	        logger.warn("nombrePlantilla "+nombrePlantillaDecretoPie);
	    	DocumentosUtil.generarDocumento(rulectx, nombrePlantillaDecretoPie, tipoDocDecreto, null, rulectx.getNumExp(), codTramiteCreac);
	    	strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantillaDecretoPie);
	    	logger.info(strInfoPag);
	    	file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
	    	DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
			file.delete();
			logger.info("FIN PIE DECRETO");
			
			//Pie Notificaciones
	        String nombrePlantillaNotiPie = DocumentosUtil.getNombrePlantillaByCod(cct, "Plant-Noti-Pie");
    		logger.info("NOTIFICACIONES CABECERA INICIO "+nombrePlantillaNotiPie);
	    	DocumentosUtil.generarDocumento(rulectx, nombrePlantillaNotiPie, tipoDocNotif, null, rulectx.getNumExp(), codTramiteCreac);
	    	strInfoPagNotificaciones = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, nombrePlantillaNotiPie);
	    	logger.info(strInfoPagNotificaciones);
	    	fileNotificaciones = DocumentosUtil.getFile(cct, strInfoPagNotificaciones, null, null);
	    	DipucrCommonFunctions.ConcatenaByFormat(xComponentNotificaciones, "file://" + fileNotificaciones.getPath(), extensionEntidad);
	    	fileNotificaciones.delete();
			logger.info("FIN PIE DECRETO");
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName("."+extensionEntidad);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			
			//Guarda el resultado en repositorio temporal
			String fileNameNotificaciones = FileTemporaryManager.getInstance().newFileName("."+extensionEntidad);
			fileNameNotificaciones = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileNameNotificaciones;
			fileNotificaciones = new File(fileNameNotificaciones);
			
			
			String filter = DocumentosUtil.getFiltroOpenOffice(extensionEntidad);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(), filter);			
			OpenOfficeHelper.saveDocument(xComponentNotificaciones,"file://" + fileNotificaciones.getPath(), filter);
			

			//Guarda el resultado en gestor documental
			int idTpdoc = DocumentosUtil.getIdTipoDocByCodigo(cct, "Decreto");
			String nombrePlantillaDecreto = DocumentosUtil.getNombrePlantillaByCod(cct, "Decreto");
			IItem docDecreto = DocumentosUtil.generaYAnexaDocumento(rulectx, codTramiteCreac, idTpdoc, nombrePlantillaDecreto, file, extensionEntidad);
			docDecreto.set("AUTOR", autor);
	        docDecreto.set("AUTOR_INFO", autorInfo);
	        docDecreto.store(cct);
	        
			if(file != null && file.exists()) file.delete();
			
			//Guarda el resultado en gestor documental Notificaciones
			int tpdocNotif = DocumentosUtil.getIdTipoDocByCodigo(cct, "Plant-Notif-Decr");
			String nombrePlantillaNotif = DocumentosUtil.getNombrePlantillaByCod(cct, "Plant-Notif-Decr");
			IItem docNot = DocumentosUtil.generaYAnexaDocumento(rulectx, codTramiteCreac, tpdocNotif, nombrePlantillaNotif, fileNotificaciones, extensionEntidad);
			docNot.set("AUTOR", autor);
			docNot.set("AUTOR_INFO", autorInfo);
			docNot.store(cct);
			
			//Borra los documentos intermedios del gestor documental
			String sql = "NOMBRE LIKE '%" + tipoDocDecreto + "%' OR NOMBRE='"+tipoDocNotif+"'";
			DocumentosUtil.eliminarDocumento(rulectx, sql);
			
			logger.warn("Inicio carga documento decreto");
			
			DecretosUtil.crearDocInfNotifTra(ooHelper, rulectx, codTramiteCreac);
	        
	        logger.warn("Fin carga documento decreto");
		}catch(ISPACException e) 
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		//Comprobar que este el documento 'Contenido del Decreto'
		return true;
	}

}
