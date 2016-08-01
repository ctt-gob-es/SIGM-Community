package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Date;
import java.util.Iterator;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * 
 * @author felipe
 * @date 23/06/2010
 * @propósito Al iniciar el trámite "Preparación de Anuncio", se crean los documentos para este trámite de la solicitud
 * 				Estos documento apuntarán a los mismos documentos BOP - Anuncio, en el repositorio, que el del trámite "Propuesta de Solicitud"
 * 				Es decir, las modificaciones en los documentos de ambos trámites, se harán sobre el mismo documento del repositorio.
 */
public class CopiarDocumentosSolicitudRule implements IRule {

	public static String Str_Doc = Constants.BOP._DOC_ANUNCIO;
	public static String Str_Doc_Firmado = "BOP - Anuncio Firmado";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			// Solución, para que en un trámite salgan documentos de otros trámites: 
			// Al iniciar el trámite Preparacion de Anuncio, se crea un nuevo registro en la tabla spac_dt_documentos.
			// Es decir, "anexamos un documento". Y luego le damos los mismos valores que el registro del trámite Propuesta de Solicitud.
			// Por lo tanto, existe un documento anexado para cada uno de los dos trámites,
			// pero en realidad, el repositorio documental, son el mismo documento.
			// Y lo que cambies del CONTENIDO del documento desde el trámite Preparación de Anuncio, se cambia en el 
			//  CONTENIDO del documento del trámite Propuesta de Solicitud.
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

			// Obtener los anuncios adjuntados en la primera fase, para copiar: infopag, descripción, idplantilla, extensión
			
			// Obtener los documentos Anuncio que tiene hasta el momento el expediente
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='" + Str_Doc + "'", "FDOC DESC");
			IItem anuncio = null;
			Iterator it = documentsCollection.iterator();
			
			while (it.hasNext()){
				anuncio = (IItem)it.next();
				
				// Creamos dos documentos para el Anuncio de la primera fase. Uno firmado y otro sin firmar
				// Documento sin firmar
				IItem nuevoAnuncio = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(), anuncio.getInt("ID_TPDOC"));
				// Documento firmado
				IItem nuevoAnuncioFirmado = (IItem)genDocAPI.createTaskDocument(rulectx.getTaskId(), anuncio.getInt("ID_TPDOC"));
				
				// Marcamos el anuncio de la primera fase como anuncio firmado, para que no produzca errores
				// al calcular el coste o generar el boletín
				anuncio.set("NOMBRE", Str_Doc_Firmado);
				anuncio.set("DESCRIPCION", Str_Doc_Firmado);
				anuncio.store(rulectx.getClientContext());
				
				// Copiar los valores del pdf, para que aparezca firmado, y le cambiamos nombre y descripción
				if (nuevoAnuncioFirmado!=null){
					
					String infopag = anuncio.getString("INFOPAG");
					String infopagrde = anuncio.getString("INFOPAG_RDE");
					String repositorio = anuncio.getString("REPOSITORIO");
					//String codVerificacion = decreto.getString("COD_VERIFICACION");
					String codCotejo = anuncio.getString("COD_COTEJO");
					String descripcion = anuncio.getString("DESCRIPCION");
					String extension = anuncio.getString("EXTENSION");			
					String extensionRDE = anuncio.getString("EXTENSION_RDE");//2.0
					int idPlantilla = anuncio.getInt("ID_PLANTILLA");
					String estadoFirma = anuncio.getString("ESTADOFIRMA");
					Date fechaAprobacion = anuncio.getDate("FAPROBACION");
					String bloqueo = anuncio.getString("BLOQUEO");
	
					nuevoAnuncio.set("DESCRIPCION", Str_Doc);
					nuevoAnuncio.set("INFOPAG", infopag);
					nuevoAnuncio.set("EXTENSION", extension);
					
					nuevoAnuncioFirmado.set("NOMBRE", Str_Doc_Firmado);
					nuevoAnuncioFirmado.set("DESCRIPCION", Str_Doc_Firmado);
					nuevoAnuncioFirmado.set("INFOPAG", infopag);
					nuevoAnuncioFirmado.set("INFOPAG_RDE", infopagrde);
					nuevoAnuncioFirmado.set("REPOSITORIO", repositorio);
					//nuevoDecreto.set("COD_VERIFICACION", codVerificacion);
					nuevoAnuncioFirmado.set("COD_COTEJO", codCotejo);
					nuevoAnuncioFirmado.set("DESCRIPCION", descripcion);
					nuevoAnuncioFirmado.set("EXTENSION", extension);
					nuevoAnuncioFirmado.set("EXTENSION_RDE", extensionRDE);//2.0
					nuevoAnuncioFirmado.set("ESTADOFIRMA", estadoFirma);
					nuevoAnuncioFirmado.set("FAPROBACION", fechaAprobacion);
					nuevoAnuncioFirmado.set("BLOQUEO", bloqueo);
					
					if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
						nuevoAnuncio.set("ID_PLANTILLA", idPlantilla);
						nuevoAnuncioFirmado.set("ID_PLANTILLA", idPlantilla);
					}
					
					//Guardamos los dos anuncios
					nuevoAnuncio.store(rulectx.getClientContext());
					nuevoAnuncioFirmado.store(rulectx.getClientContext());
				}
			}
			
		}catch(ISPACException e){
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
