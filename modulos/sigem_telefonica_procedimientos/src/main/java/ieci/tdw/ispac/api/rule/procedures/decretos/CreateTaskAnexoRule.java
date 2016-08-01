package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 27/05/2009
 * @propósito Al iniciar el trámite "Notificaciones", se crea un documento para este trámite
 * 				Este documento apuntará al mismo documento Anexo Decreto, en el repositorio, que el del trámite "Creación de Decreto"
 * 				Es decir, las modificaciones del Decreto desde ambos trámites, se harán sobre el mismo documento del repositorio.
 */
public class CreateTaskAnexoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			// Solución, para que en un trámite salgan documentos de otros trámites: 
			// Al iniciar el trámite Preparación de firmas, se crea un nuevo registro en la tabla spac_dt_documentos.
			// Es decir, "anexamos un documento". Y luego le damos los mismos valores que el registro del trámite Creación de Decreto.
			// Por lo tanto, existe un documento anexado para cada uno de los dos trámites,
			// pero en realidad, el repositorio documental, son el mismo documento.
			// Y lo que cambies del CONTENIDO del documento desde el trámite Preparación de firmas, se cambia en el 
			//  CONTENIDO del documento del trámite Creación del Decreto.
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

			// Obtener el Decreto adjuntado en la fase de Inicio, para copiar: infopag, descripción, idplantilla, extensión
			// Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado sólo un doc (ValidateNumDocsTramiteRule)
			
			// Obtener el documento que tiene hasta el momento el expediente
			//IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "", "FDOC DESC");
			// Obtener el documento Anexo del expediente
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Anexo'", "FDOC DESC");
			IItem decreto = null;
			if (documentsCollection!=null && documentsCollection.next()){
				decreto = (IItem)documentsCollection.iterator().next();
			}
				
			// Crear el documento del mismo tipo que el Decreto del trámite Creación del Decreto
			IItem nuevoDecreto = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(),decreto.getInt("ID_TPDOC"));
			
			// Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
			if (decreto!=null){
				String infopag = decreto.getString("INFOPAG");
				String infopagrde = decreto.getString("INFOPAG_RDE");
				String repositorio = decreto.getString("REPOSITORIO");
				String codCotejo = decreto.getString("COD_COTEJO");
				String descripcion = decreto.getString("DESCRIPCION");
				String extension = decreto.getString("EXTENSION");			
				int idPlantilla = decreto.getInt("ID_PLANTILLA");

				nuevoDecreto.set("INFOPAG", infopag);
				nuevoDecreto.set("INFOPAG_RDE", infopagrde);
				nuevoDecreto.set("REPOSITORIO", repositorio);
				nuevoDecreto.set("COD_COTEJO", codCotejo);
				nuevoDecreto.set("DESCRIPCION", descripcion);
				nuevoDecreto.set("EXTENSION", extension);
				if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
					nuevoDecreto.set("ID_PLANTILLA", idPlantilla);
				}
				nuevoDecreto.store(rulectx.getClientContext());
			}
			
			// Otra opción (SIN IMPLEMENTAR) es anexar el documento en la Fase de Inicio.
			// Luego al crear el trámite de la fase firmas, asignarle el documento anterior
			// Cambiar de estado del documento a 'no firmado' para volverlo a firmar
			// Al cerrar el trámite volver a asignar el documento a su trámite inicial (Fase de Introducción)
			// Entonces, si luego vieramos los trámites: Creación de trámites sí que tiene documento, 
			// pero Preparación de firmas no tiene nada adjuntado
			
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
