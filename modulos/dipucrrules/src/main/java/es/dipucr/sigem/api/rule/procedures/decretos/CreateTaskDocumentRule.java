package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @propósito Al iniciar el trámite "Preparación de firmas", se crea un documento para este trámite
 * 				Este documento apuntará al mismo documento Decreto, en el repositorio, que el del trámite "Creación de Decreto"
 * 				Es decir, las modificaciones del Decreto desde ambos trámites, se harán sobre el mismo documento del repositorio.
 */
public class CreateTaskDocumentRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(CreateTaskDocumentRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try{
			
			// Solución, para que en un trámite salgan documentos de otros trámites: 
			// Al iniciar el trámite Preparación de firmas, se crea un nuevo registro en la tabla spac_dt_documentos.
			// Es decir, "anexamos un documento". Y luego le damos los mismos valores que el registro del trámite Creación de Decreto.
			// Por lo tanto, existe un documento anexado para cada uno de los dos trámites,
			// pero en realidad, el repositorio documental, son el mismo documento.
			// Y lo que cambies del CONTENIDO del documento desde el trámite Preparación de firmas, se cambia en el 
			//  CONTENIDO del documento del trámite Creación del Decreto.
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();


			// Ejecución en un contexto transaccional
			boolean ongoingTX = cct.ongoingTX();
			
			if (!ongoingTX) {
				cct.beginTX();
			}

			// Obtener el Decreto adjuntado en la fase de Inicio, para copiar: infopag, descripción, idplantilla, extensión
			// Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado sólo un doc (ValidateNumDocsTramiteRule)
			
			// Obtener el documento que tiene hasta el momento el expediente
			//IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "", "FDOC DESC");
			// Obtener el documento Decreto del expediente
			
			numexp = rulectx.getNumExp();
			
			IItemCollection documentsCollection = entitiesAPI.getDocuments(numexp, "NOMBRE='Decreto'", "FDOC DESC");
			IItem decreto = null;
			String sIdPlantilla;
			int idTipDoc = 0;
			if (documentsCollection!=null && documentsCollection.next()){
				decreto = (IItem)documentsCollection.iterator().next();
				
				if (decreto!=null){
					sIdPlantilla = decreto.getString("ID_PLANTILLA");
					idTipDoc = decreto.getInt("ID_TPDOC");
				
					String infoPag = decreto.getString("INFOPAG");
					if(StringUtils.isNotEmpty(decreto.getString("INFOPAG_RDE"))){
						infoPag = decreto.getString("INFOPAG_RDE");
					}
					Object connectorSession = genDocAPI.createConnectorSession();
					
					String mimetype = genDocAPI.getMimeType(connectorSession, infoPag);
					String extension = MimetypeMapping.getExtension(mimetype);
					
					File fileDecretoNuevo = DocumentosUtil.getFile(cct, infoPag, null, extension);
				    IItem decretoNuevo = DocumentosUtil.generaYAnexaDocumento(rulectx, idTipDoc, "Decreto", fileDecretoNuevo, extension);
					
				    //INICIO [dipucr-Felipe 3#268]
				    //Cambio el método para evitar copiar datos de rde, cod_cotejo, cod_verificacion, extension_rde 
				    //pues el documento es una copia y no está firmado
				    //Además la extensión que aplico será la rde si esta existe (pdf)
					String descripcion = decreto.getString("DESCRIPCION");
					extension = decreto.getString("EXTENSION");
					if(StringUtils.isNotEmpty(decreto.getString("EXTENSION_RDE"))){
						extension = decreto.getString("EXTENSION_RDE");
					}
					String autor = decreto.getString("AUTOR");
			        String autorInfo = decreto.getString("AUTOR_INFO");
			        
					decretoNuevo.set("DESCRIPCION", descripcion);
					decretoNuevo.set("EXTENSION", extension);
					decretoNuevo.set("AUTOR", autor);
					decretoNuevo.set("AUTOR_INFO", autorInfo);
					if (!StringUtils.isEmpty(sIdPlantilla)){
						decretoNuevo.set("ID_PLANTILLA", sIdPlantilla);
					}
					//FIN [dipucr-Felipe 3#268]
					
					decretoNuevo.store(rulectx.getClientContext());
					cct.endTX(true);
					
					try{
						if (fileDecretoNuevo!=null && fileDecretoNuevo.exists() && !fileDecretoNuevo.delete()){
							logger.error("No se pudo eliminar el documento: " + FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileDecretoNuevo.getName());
						}
					}
					
					catch(Exception e){}
				}
			}
			
			
		}catch(ISPACException e){
			logger.error("Error al generar el nuevo documento en el expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el nuevo documento en el expediente: " + numexp + ". " + e.getMessage(), e);
		}
		
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}  
}
