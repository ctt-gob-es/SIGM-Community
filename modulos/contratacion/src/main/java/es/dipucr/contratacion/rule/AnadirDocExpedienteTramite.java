package es.dipucr.contratacion.rule;

import java.io.File;
import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class AnadirDocExpedienteTramite implements IRule{
	private static final Logger logger = Logger.getLogger(AnadirDocExpedienteTramite.class);
	
	String numexpPeticionContratacion = "";
	String codigoTramite = "";
	String nombreDescripcionDoc = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*****************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/*****************************************************************/
			
			if(!numexpPeticionContratacion.equals("") && !codigoTramite.equals("")){
				/**Creaci�n del tr�mite*/
				int idTramiteCodTramite = TramitesUtil.crearTramite(cct, codigoTramite, numexpPeticionContratacion);
				/**Fin Creaci�n tr�mite**/
				
				/**Creaci�n del documento en el tr�mite**/
				IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId()+"", "FDOC DESC");
				IItem contenido = null;
				Iterator <IItem> itDoc = documentsCollection.iterator();
				if(itDoc.hasNext()){
					while (itDoc.hasNext()){
						contenido = (IItem)itDoc.next();
						File documentoPropuesta = null;
						String extension = "";
						if(contenido.getString("INFOPAG_RDE")!=null){
							documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG_RDE"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION_RDE"));
							extension = contenido.getString("EXTENSION_RDE");
						}
						else{
							documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION"));
							extension = contenido.getString("EXTENSION");
						}
						
						IItem docAnexado = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteCodTramite, contenido.getInt("ID_TPDOC"), nombreDescripcionDoc, documentoPropuesta, extension);
						if(contenido.getDate("FAPROBACION")!=null){
							docAnexado.set("FAPROBACION", contenido.getDate("FAPROBACION"));
							docAnexado.store(cct);
						}
					}
				}else{
					logger.warn("No se ha encontrado ning�n documento en ese tr�mite");
					throw new ISPACInfo("No se ha encontrado ning�n documento en ese tr�mite");
				}
				/**FIN Creaci�n documento Tr�mite**/
				
				/**Por �ltimo cerrar el tr�mite que se ha creado autom�ticamente**/
				if(!codigoTramite.equals("Sol-Inf-Tecn-Inc")){
					TramitesUtil.cerrarTramite(idTramiteCodTramite, rulectx);
				}
				
			}

		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
