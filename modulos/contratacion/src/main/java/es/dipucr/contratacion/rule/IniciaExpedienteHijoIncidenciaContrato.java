package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class IniciaExpedienteHijoIncidenciaContrato implements IRule{
	
	public static final Logger logger = Logger.getLogger(IniciaExpedienteHijoIncidenciaContrato.class);


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
			 /***********************************************************************/
			/**Inicio de expediente de 'Certificación de obra'**/
			
			/**Creación del expediente de 'Certificación de obra'**/
			//calculo el número de certificación de obra por el número de expedientes relacionados que son Certificación de obra
			String strQuery = "WHERE NUMEXP_PADRE='" + rulectx.getNumExp() + "' AND RELACION LIKE 'Incidencia Obra - %'";
			IItemCollection collectExpRel = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
					
			//Obtenemos el id del procedimiento 'Certificación obra'
			IItemCollection procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = 'INCID-CONTRATO' ");
			Iterator <IItem> procsIterator = procedimientosDelDepartamento.iterator();
			int idCtProcedimientoNuevo = 0;
			while(procsIterator.hasNext()){
				IItem procs = (IItem) procsIterator.next();
				idCtProcedimientoNuevo = procs.getInt("ID");
			}
			String relacion = "Incidencia Obra -  "+(collectExpRel.toList().size()+1)+ " - "+ FechasUtil.getFormattedDate(new Date());
			IItem numexpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteRelacionadoHijo(cct, idCtProcedimientoNuevo, rulectx.getNumExp(), relacion, true, null);			
			numexpHijo.set("ASUNTO", relacion);
			numexpHijo.store(cct);
			/**Fin del expediente de 'Certificación de obra'**/
			
//			/**Creación del trámite 'Propuesta de Aprobación de Certificación de Obra'**/
//			int idTramitePropCertObra = TramitesUtil.crearTramite(cct, "INF-TECNIC", numexpHijo.getString("NUMEXP"));
//			/**Fin Creación trámite**/
//			
//			/**Creación del documento en el trámite**/
//			//Obtengo el número expediente de la peticion
//			IItemCollection expeRel = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'");
//			Iterator<IItem> itexpRel = expeRel.iterator();
//			String numexpPetContrato = "";
//			if(itexpRel.hasNext()){
//				numexpPetContrato = itexpRel.next().getString("NUMEXP_PADRE");
//			}
//			//Obtengo el documento de la Petición
//			IItemCollection documentsCollection = DocumentosUtil.getDocumentos(cct, numexpPetContrato, "NOMBRE LIKE 'Informe Técnico'",  "FDOC DESC");
//			IItem contenido = null;
//			if (documentsCollection!=null && documentsCollection.next()){
//				contenido = (IItem)documentsCollection.iterator().next();
//			}else{
//				documentsCollection = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), "NOMBRE LIKE 'Informe Técnico'",  "FDOC DESC");
//				if (documentsCollection!=null && documentsCollection.next()){
//					contenido = (IItem)documentsCollection.iterator().next();
//				}else{
//					throw new ISPACInfo("No se ha encontrado el documento de Documentación de la propuesta");
//				}
//			}
//			
//			int id_documento = contenido.getInt("ID");
//			File documentoPropuesta = null;
//			String extension = "";
//			if(contenido.getString("INFOPAG_RDE")!=null){
//				documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG_RDE"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION_RDE"));
//				extension = contenido.getString("EXTENSION_RDE");
//			}
//			else{
//				documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION"));
//				extension = contenido.getString("EXTENSION");
//			}
//			
//			IItem docAnexado = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropCertObra, contenido.getInt("ID_TPDOC"), "Informe Técnico", documentoPropuesta, extension);
//			if(contenido.getDate("FAPROBACION")!=null){
//				docAnexado.set("FAPROBACION", contenido.getDate("FAPROBACION"));
//				docAnexado.store(cct);
//			}
//			
//			/**FIN Creación documento Trámite**/
//			
//			/**Actualizar la tabla**/
//			IItem  id_doc = entitiesAPI.createEntity("DPCR_ID_PROPUESTA", numexpHijo.getString("NUMEXP"));
//			id_doc.set("ID_DOC", id_documento);
//			id_doc.store(cct);
//			/**Fin**/
//			
//			/**Obtengo el documento Documentación del incidente**/
//			
//			IItemCollection documentsCollectionDoc = entitiesAPI.getDocuments(numexpPetContrato, "NOMBRE LIKE 'Documentación del incidente'", "FDOC DESC");
//			Iterator<IItem> itDoc = documentsCollectionDoc.iterator();
//			
//			while(itDoc.hasNext()){
//				IItem contenidoDoc = itDoc.next();
//				File documentoPropuestaDoc = null;
//				Object connectorSession = genDocAPI.createConnectorSession();
//				if(contenidoDoc.getString("INFOPAG_RDE")!=null){
//					documentoPropuestaDoc = DocumentosUtil.getFile(cct, contenidoDoc.getString("INFOPAG_RDE"), contenidoDoc.getString("NOMBRE"), contenidoDoc.getString("EXTENSION_RDE"));
//					String mimetype = genDocAPI.getMimeType(connectorSession, contenidoDoc.getString("INFOPAG_RDE"));
//					extension = MimetypeMapping.getExtension(mimetype);
//				}
//				else{
//					documentoPropuestaDoc = DocumentosUtil.getFile(cct, contenidoDoc.getString("INFOPAG"), contenidoDoc.getString("NOMBRE"), contenidoDoc.getString("EXTENSION"));
//					String mimetype = genDocAPI.getMimeType(connectorSession, contenidoDoc.getString("INFOPAG"));
//					extension = MimetypeMapping.getExtension(mimetype);
//				}
//				
//				IItem docAnex = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramitePropCertObra, contenidoDoc.getInt("ID_TPDOC"), contenidoDoc.getString("DESCRIPCION"), documentoPropuestaDoc, extension);
//				if(contenidoDoc.getDate("FAPROBACION")!=null){
//					docAnex.set("FAPROBACION", contenidoDoc.getDate("FAPROBACION"));
//					docAnex.store(cct);
//				}
//			}

			/**FIN incidencia del contrato**/
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
