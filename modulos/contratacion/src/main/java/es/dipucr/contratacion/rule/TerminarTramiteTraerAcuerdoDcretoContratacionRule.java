package es.dipucr.contratacion.rule;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class TerminarTramiteTraerAcuerdoDcretoContratacionRule extends TerminarTramiteTraerAcuerdoDcretoConvocatoriaMesaRule {

	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try
		{
    		//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Identificador del tramite actual.
            int idTask = rulectx.getTaskId();
            
            //Obtengo los expedientes relacionados
            String consultaSQL = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION LIKE '%"+idTask+"%'";
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
			
			Iterator<IItem> it = itemCollection.iterator();
	        IItem item = null;
	        
	        String numexp_hijo = "";
	        
	        while (it.hasNext()) {
                item = ((IItem)it.next());
                numexp_hijo = item.getString("NUMEXP_HIJO");
	        }
	        
	        //Ya se ha obtenido el numero de expediente relacionado
	        //Ahora con ese expediente miro si tiene expedientes relacionado y seria una propuesta
	        //Si no tuviera sería un decreto
	        IItem iExped = entitiesAPI.getExpedient(numexp_hijo);
	        String nombreProced = "";
	        if(iExped != null){
	        	nombreProced = iExped.getString("NOMBREPROCEDIMIENTO");
	        }
	        else{
	        	rulectx.setInfoMessage(Constants.MENSAJE.MENSAJERELACIONEXPEDIENTE);
	        }
	        
	        nombreProced = nombreProced.toLowerCase();
	        
	        //expediente creado como una propuesta
	        String[] proced = new String [2];
	        String numexpProced = "";
	        String nombreProc = "";
	        String numexpHijo_Propuesta = numexp_hijo;
	        if(nombreProced.contains(Constants.PROPUESTA.NOMBRE)){
	        	consultaSQL = "WHERE NUMEXP_PADRE = '" + numexp_hijo + "'";
		        itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
		        
		        it = itemCollection.iterator();
		        
		        //vector que almacena a que procedimientos esta asociado una propuesta
		        Vector<String> vSecret = new Vector<String>();
		        String nombreProcedSecretaria = "";
		        while(it.hasNext()){
		        	 item = ((IItem)it.next());
		        	 numexpHijo_Propuesta = item.getString("NUMEXP_HIJO");
		        	 IItem iExpedHijo = entitiesAPI.getExpedient(numexpHijo_Propuesta);
		        	 nombreProcedSecretaria = iExpedHijo.getString("NOMBREPROCEDIMIENTO");
		        	 //Para que no salga en expedientes relacionados el procedimientos de subvenciones
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 String datosProce = numexpHijo_Propuesta +"&"+nombreProcedSecretaria;
		        		 vSecret.add(datosProce);
		        	 }
		        }
		        if(vSecret.size() >= 2){
		        	proced = devolverProcedimientoMayor(vSecret);
		        	numexpProced = proced[0];
		        	nombreProc = proced[1];
		        }
		        else{
		        	 if(!nombreProcedSecretaria.contains(Constants.SUBVENCIONES.RELACION)){
		        		 numexpProced = numexpHijo_Propuesta;
		        	 }
		        }
	        }
	        else{
	        	//decreto
	        	numexpProced = numexpHijo_Propuesta;
	        }
	        
	        
	        IItem sesion = SecretariaUtil.getSesion(rulectx, numexpProced);
	        Date fechaCelebracion = sesion.getDate("FECHA");
			String fechaCe = FechasUtil.getFormattedDate(fechaCelebracion,"dd'-'MM'-'yy");
			String nombreFinal = "";
			if (nombreProc.equals(Constants.SECRETARIAPROC.JUNTA)){
				nombreFinal = "Certificado Junta Gobierno dia ";
			}
			if(nombreProc.equals(Constants.SECRETARIAPROC.PLENO)){
				nombreFinal = "Certificado Pleno dia ";
			}
			if(nombreProc.equals(Constants.SECRETARIAPROC.MESA)){
				nombreFinal = "Dictamen Mesa Contratacion dia ";
			}
			descripcion = nombreFinal+fechaCe;
		}catch(Exception e) {
			throw new ISPACRuleException(e);
		}

	
		
		return true;
	}
	
	private String[] devolverProcedimientoMayor(Vector<String> secret) {
		String procedi[] = new String [2];
		for(int i=0; i<secret.size(); i++){
			String sRes = (String) secret.elementAt(i);
			String [] dtosProc = sRes.split("&");
			String nombProc = dtosProc[1];
			
			if(nombProc.contains(Constants.SECRETARIAPROC.JUNTA) || nombProc.contains(Constants.SECRETARIAPROC.PLENO) || nombProc.equals(Constants.SECRETARIAPROC.MESA)){
				procedi[0] = dtosProc[0];
				procedi[1] = nombProc;
				
			}
		}
		return procedi;
	}
	
	
}
