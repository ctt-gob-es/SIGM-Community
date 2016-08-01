package aww.sigem.expropiaciones.util;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;



public class FicherosUtil {

	private static final Logger logger = Logger.getLogger(FicherosUtil.class);
	
	public static int [] obtenerIdPlantilla(IProcedureAPI procedureAPI, IEntitiesAPI entitiesAPI, int idTramCtl, String plantilla) throws ISPACException{
		
		
		int templateId = 0;
		int documentTypeId = 0;
		
		/*
		 * 0 - documentTypeId
		 * 1 - templateId
		 */
		int identificadores[] = new int[2];
		
		/*
		 * Almacenará los ids del tipo de documento y el tipo de plantilla.
		 */
		
		
		IItemCollection taskTpDocCollection = procedureAPI.getTaskTpDoc(idTramCtl);

		//logger.warn("idTramCtl: " + idTramCtl);
		
		if ((taskTpDocCollection == null)|| (taskTpDocCollection.toList().isEmpty())) {
			//logger.warn("error.decretos.acuses.TaskTpDoc");
			throw new ISPACInfo(// Messages.getString(
					"error.decretos.acuses.TaskTpDoc"
			// )
			);
		}

		//logger.warn("Obteniendo Tipo de documento");
		Iterator it = taskTpDocCollection.iterator();
		while (it.hasNext()) {
			IItem taskTpDoc = (IItem) it.next();
			//logger.warn("nombre doc: " + taskTpDoc.get("CT_TPDOC:NOMBRE"));
			
			//logger.warn("Tipo de doc " + taskTpDoc.get("CT_TPDOC:NOMBRE"));
			
			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(plantilla)) {
				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");				
				identificadores[0] = documentTypeId;			
			} 
		}	
		
		if (documentTypeId != 0) {
			//logger.warn("Obtenido documentTypeId: " + documentTypeId);
			//logger.warn("Obteniendo plantilla ");
			IItemCollection tpDocsTemplatesCollection = procedureAPI
					.getTpDocsTemplates(documentTypeId);
			if ((tpDocsTemplatesCollection == null)
					|| (tpDocsTemplatesCollection.toList().isEmpty())) {
				throw new ISPACInfo(// Messages.getString(
						"error.decretos.acuses.tpDocsTemplates"
				// )
				);
			}
			IItem tpDocsTemplate = (IItem) tpDocsTemplatesCollection
					.iterator().next();
			templateId = tpDocsTemplate.getInt("ID");
			
			identificadores[1] = templateId;
			
			//logger.warn("Obteniendo templateId:" + templateId);
		} else {
			//logger.warn("No se pudo obtener documentTypeId: " + documentTypeId);
		}
		return identificadores;
	}
	
}
