package es.dipucr.sigem.api.rule.common.documento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrAutogeneraDocumentoEspecificoInitTramite extends DipucrAutoGeneraDocIniTramiteRule{

	private static final Logger logger = Logger.getLogger(DipucrAutogeneraDocumentoEspecificoInitTramite.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		try{
			IClientContext cct = rulectx.getClientContext();
			
			plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
			
			if(StringUtils.isNotEmpty(plantilla)){
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);			
			}
		}
		catch(ISPACException e){
			logger.error("Error al generar el documento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento. " + e.getMessage(), e);
		}
		catch(Exception e){
			logger.error("Error al generar el documento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento. " + e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
			
			//----------------------------------------------------------------------------------------------
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();

	    	String recurso = "";
	    	String observaciones = "";
	    	
			
			IItemCollection partCol = ParticipantesUtil.getParticipantes(cct, numexp, "ROL='INT'", "");
			Iterator partIt = partCol.iterator();
			
			if(partIt.hasNext()){
				
				IItem part = (IItem)partIt.next();
				
				if ((String)part.get("RECURSO")!=null){
	        		recurso = (String)part.get("RECURSO");
	        	}else{
	        		recurso = "";
	        	}
	        	if ((String)part.get("OBSERVACIONES")!=null){
	        		observaciones = (String)part.get("OBSERVACIONES");
	        	}else{
	        		observaciones = "";
	        	}
	        	
		        // Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
		        IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", "WHERE VALOR = '"+recurso+"'");
		        if (colRecurso.iterator().hasNext()){
		        	IItem iRecurso = (IItem)colRecurso.iterator().next();
		        	recurso = iRecurso.getString("SUSTITUTO");
		        }
		        if (recurso.equals("")){
		        	recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
		        }
		        else{
		        	recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
		        }
				cct.setSsVariable("NOMBRE", part.getString("NOMBRE"));
				cct.setSsVariable("NDOC", part.getString("NDOC")); //[eCenpri-Felipe #947]
				cct.setSsVariable("DIRNOT", part.getString("DIRNOT"));
				cct.setSsVariable("C_POSTAL", part.getString("C_POSTAL"));
				cct.setSsVariable("LOCALIDAD", part.getString("LOCALIDAD"));
				cct.setSsVariable("CAUT", part.getString("CAUT"));
	        	cct.setSsVariable("RECURSO", recurso);
	        	cct.setSsVariable("OBSERVACIONES", observaciones);
			}
				
		} catch (ISPACException e) {
			logger.error("Error al setear las variables de sesión. " + e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {	
		try {
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("NOMBRE");
			cct.deleteSsVariable("NDOC"); //[eCenpri-Felipe #947]
			cct.deleteSsVariable("DIRNOT");
			cct.deleteSsVariable("C_POSTAL");
			cct.deleteSsVariable("LOCALIDAD");
			cct.deleteSsVariable("CAUT");
			cct.deleteSsVariable("RECURSO");
			cct.deleteSsVariable("OBSERVACIONES");
		} catch (ISPACException e) {
			logger.error("Error al setear las variables de sesión. " + e.getMessage(), e);
		}
	}
}
