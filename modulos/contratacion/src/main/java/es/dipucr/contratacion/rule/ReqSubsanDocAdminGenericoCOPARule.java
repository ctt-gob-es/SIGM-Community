package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class ReqSubsanDocAdminGenericoCOPARule extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GeneraCertifRecepPlicas.class);

	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GeneraCertifRecepPlicas.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}

		logger.warn("FIN - " + GeneraCertifRecepPlicas.class);
		return true;
	}

	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
						
			cct.setSsVariable("SUBSANACIONES", calcularSubsanados(rulectx, cct.getAPI().getEntitiesAPI()));

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private String calcularSubsanados(IRuleContext rulectx, IEntitiesAPI entitiesAPI) throws ISPACException{
				
	    String licitador = "";
        String dni = "";							
		String escritura = "";
		String poder = "";
		String declarJurada = "";
		String solvenciaEconomica = "";
		String solvenciaTecnica = "";
		String declaraciónVinculación = "";
		String declaraciónCondiciones = "";
		StringBuffer subsanacion = new StringBuffer("");
        
        ArrayList<String> expedientesResolucion = new ArrayList<String>();
		 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
        IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='Plica' ORDER BY ID ASC");
        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
        String query = "";
        while (exp_relacionadosIterator.hasNext()){
        	String numexpHijo = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");
        	expedientesResolucion.add(numexpHijo);
        	query += "'"+numexpHijo+"',";	        	
        }
        		
		if(query.length()>0){
			query = query.substring(0,query.length()-1);
	    }
		String sQuery = "WHERE NUMEXP IN ("+query+") ORDER BY NREG ASC";
		logger.warn("sQuery "+sQuery);
		IItemCollection expedientesCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, sQuery);
	   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
			
	   	int i = 0;   	
	   	while (expedientesIterator.hasNext()){
	   		i++;
	    	IItem expediente = (IItem) expedientesIterator.next();
	    	licitador = expediente.getString("IDENTIDADTITULAR");
	    	
	    	IItemCollection plicaCol = entitiesAPI.getEntities("CONTRATACION_PLICA", expediente.getString("NUMEXP"));
	    	Iterator<IItem> itPlica = plicaCol.iterator();
	    	
	    	dni = null;
	    	escritura = null;
			poder = null;
			declarJurada = null;
			solvenciaEconomica = null;
			solvenciaTecnica = null;
			declaraciónVinculación = null;
			declaraciónCondiciones = null;
	    	
	    	while(itPlica.hasNext()){
	    		
	    		IItem plica = (IItem)itPlica.next();
	    		dni = plica.getString("DNI");							
	    		escritura = plica.getString("ESCRITURA");
	    		poder = plica.getString("PODER");
	    		declarJurada = plica.getString("DECLARACION_JURADA");
	    		solvenciaEconomica = plica.getString("SOLVENCIA_ECONOMICA");
	    		solvenciaTecnica = plica.getString("SOLVENCIA_TECNICA");
	    		declaraciónVinculación = plica.getString("DECLARACION_VINCULACION");
	    		declaraciónCondiciones = plica.getString("DECLARACION_CONDICIONES"); 
	    	
		    	StringBuffer subsanacionRes = new StringBuffer("No aporta ");
		    	
		    	boolean dentro = false;
		    	
		    	//Si este campo es vacio quiere decir que han entregado toda la documentacion
		    	if(dni != null && (dni.equals("NO") || dni.equals(""))){
		    		subsanacionRes.append("Dni; ");
		    		dentro = true;
		    	}
		    	if(escritura != null && (escritura.equals("NO") || escritura.equals(""))){
		    		subsanacionRes.append("Escritura; ");
		    		dentro = true;
		    	}
		    	if(poder != null && (poder.equals("NO") || poder.equals(""))){
		    		subsanacionRes.append("Poder; ");
		    		dentro = true;
		    	}
		    	if(declarJurada != null && (declarJurada.equals("NO") || declarJurada.equals(""))){
		    		subsanacionRes.append("Declaración Jurada; ");
		    		dentro = true;
		    	}
		    	if(solvenciaEconomica != null && (solvenciaEconomica.equals("NO") || solvenciaEconomica.equals(""))){
		    		subsanacionRes.append("Solvencia Económica; ");
		    		dentro = true;
		    	}
		    	if(solvenciaTecnica != null && (solvenciaTecnica.equals("NO") || solvenciaTecnica.equals(""))){
		    		subsanacionRes.append("Solvencia Técnica; ");
		    		dentro = true;
		    	}
		    	if(declaraciónVinculación != null && (declaraciónVinculación.equals("NO") || declaraciónVinculación.equals(""))){
		    		subsanacionRes.append("Declaración Vinculación; ");	
		    		dentro = true;
		    	}
		    	if(declaraciónCondiciones != null && (declaraciónCondiciones.equals("NO") || declaraciónCondiciones.equals(""))){
		    		subsanacionRes.append("Declaración Condiciones; ");	
		    		dentro = true;
		    	}
		    	if(dentro == true){
		    		subsanacion.append("- Plica nº "+i+" .- "+licitador+" .- "+subsanacionRes.toString()+".\n");	
		    	}
		    	else{
		    		//Comprobamos que exista el documento de subsanación y la fecha de registro de subsanación
		    		String numexp = plica.getString("NUMEXP");
		    		IItemCollection docsCol = entitiesAPI.getDocuments(numexp, "NOMBRE='Subsanación, Justificación o Modificación'", "FREG DESC");
		    		Iterator <IItem> itDocsCol = docsCol.iterator();
		    		if(itDocsCol.hasNext()){
		    			IItem itdocscol = itDocsCol.next();
		    			Date fechaReg = itdocscol.getDate("FREG");
		    			 String patron = "dd/MM/yyyy kk:mm";
		    			 SimpleDateFormat formato = new SimpleDateFormat(patron);
		    			 // formateo
		    			 subsanacion.append("- Plica nº "+i+" .- "+licitador+" .- FECHA DE SUBSANACIÓN: "+formato.format(fechaReg)+".\n");	
		    			;
		    		}
		 	       
		    	}
	    	}
	    	
        }
	   	
	   	logger.warn("subsanacion.toString().equals() "+subsanacion.toString().equals("")+" - "+subsanacion.toString());
    	if(subsanacion.toString().equals("")){
    		subsanacion.append("Está correcta toda la documentación.");
    	}
	   	return subsanacion.toString();
		
	}

	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("SUBSANACIONES");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
}


