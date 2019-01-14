package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.rrhh.RespuestaSolicitudLicenciasRule;
import es.dipucr.tablon.services.TablonWSProxy;


/**
 * [dipucr-Felipe 3#382]
 * Regla para eliminar una publicación del tablón de edictos electrónico eTablón
 * @author Felipe
 * @since 16.11.2016
 */
public class EliminarPublicacionTablonRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(RespuestaSolicitudLicenciasRule.class);
	
	protected static final String COD_TRAM_CERTIFICADO = "ETABLON_CERT";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			IItemCollection colPublicacion = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexp);
			
			if (colPublicacion.toList().size() == 0){
				rulectx.setInfoMessage("No se puede eliminar el anuncio del tablón pues no está publicado. Id Publicacion = null");
				return false;
			}
			IItem itemPublicacion = (IItem)colPublicacion.iterator().next();
			String idPublicacion = itemPublicacion.getString("ID_PUBLICACION");
			if (StringUtils.isEmpty(idPublicacion)){
				rulectx.setInfoMessage("No se puede eliminar el anuncio del tablón pues no está publicado. Id Publicacion = null");
				return false;
			}
			
			//Hacemos la petición al servicio web
			TablonWSProxy wsTablon = new TablonWSProxy();
			
//			boolean bResult = wsTablon.eliminarPublicacion(Integer.valueOf(idPublicacion));//[dipucr-Felipe 3#382]
			String codEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			boolean bResult = wsTablon.eliminarPublicaciones(codEntidad, numexp);
			
			if(!bResult){
				rulectx.setInfoMessage("El anuncio no ha podido eliminarse del tablón");
				return false;
			}
			else{
				//Si todo ha ido bien, eliminamos id de publicación y el trámite de diligencia
				Object objNull = null;
				itemPublicacion.set("ID_PUBLICACION", objNull);
				itemPublicacion.store(cct);
				
				IItem itemCtTramite = TramitesUtil.getTramiteByCode(rulectx, COD_TRAM_CERTIFICADO);
				StringBuffer sbQuery = new StringBuffer();
				sbQuery.append(" WHERE NUMEXP='" + numexp + "'");
				sbQuery.append(" AND ID_CTTRAMITE = " + itemCtTramite.getKeyInt());
				IItemCollection colTramitesDiligencia = entitiesAPI.queryEntities("SPAC_TRAMITES", sbQuery.toString());
				
				@SuppressWarnings("rawtypes")
				Iterator itTramitesDiligencia = colTramitesDiligencia.iterator();
				while(itTramitesDiligencia.hasNext()){
					IItem itemTramDiligencia = (IItem) itTramitesDiligencia.next();
					entitiesAPI.deleteTask(itemTramDiligencia.getKeyInt());
				}
			}
		}
		catch (Exception e) {
			String error = "Error al eliminar la publicación en el tablón en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage();
        	logger.error(error, e);
			throw new ISPACRuleException(error, e);
		}
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
