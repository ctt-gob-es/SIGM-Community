package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GeneracionPropuestasActa implements IRule {

	private OpenOfficeHelper ooHelper = null;
	private static final Logger LOGGER = Logger.getLogger(GeneracionPropuestasActa.class);
	
	String strNombreDocProp = "";
    String strNombreDocCabProp = "";
    String strNombreDocPieProp = "";
    String strNombreDocUrg = "";
    String strNombreDocCabUrg = "";
    String strNombreDocPieUrg = "";
    

    @SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	ClientContext cct = null;
    	Object connectorSession = null;
    	IGenDocAPI gendocAPI = null;
    	
    	//Para cada propuesta se generan los tags a incluir en el certificado de acuerdos/dictamen
        String extracto = "";
        String acuerdos = "";
        String dictamen = "";
        String cargo = "";
        String fechaEmision = "";
        String informes = "";
        String debate = "";
        String votacion = "";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        SimpleDateFormat inputDateformat=new SimpleDateFormat("dd'/'MM'/'yyyy",new Locale("es"));

        int nOrden = 1;
        
        boolean urgencia = false;
    	
    	
    	try {
    		//----------------------------------------------------------------------------------------------
            cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            gendocAPI = cct.getAPI().getGenDocAPI();
            String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
            //----------------------------------------------------------------------------------------------
            
            String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
        	boolean esAcuerdo = strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0;
        	
        	String strCampo = esAcuerdo? "ACUERDOS":"DICTAMEN";
    		
    		String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        List listPropuestas = collection.toList();
	        
	        int numPropuesta = listPropuestas.size();
	        
	        collection = entitiesAPI.queryEntities("SECR_URGENCIAS", strQuery);
	        List listUrgencias = collection.toList();
	        
	        Iterator it = listPropuestas.iterator();
	        
	        IItem iProp = null;
	        
	        try{
	        	//Ticket #1374
	        	//Comrpuebo el número de propuestas que tiene.
	        	if(listPropuestas.toArray().length==1){
	        		//Quiere decir que únicamente existe una sóla propuesta relaciona
	        		nOrden = 0;
	        	} else {
	        		Iterator<IItem> propAprob = ConsultasGenericasUtil.queryEntities(rulectx, "SECR_PROPUESTA", "ORIGEN='0001' AND NUMEXP='"+rulectx.getNumExp()+"'");
	        		if(propAprob.hasNext()){
		        		//Para que no genere el documento borrador y así paso a la siguiente
			        	it.next();
	        		} else {
	        			nOrden = 0;
	        		}
	        	}
	        	
	        	if(!it.hasNext() && !urgencia){
	        		it = listUrgencias.iterator();
	        		urgencia = true;
	        		nOrden = 0;
		        }
	        	
	        	//Documento de propuesta
		        while (it.hasNext()) {
		        	nOrden++;
		        	iProp = (IItem)it.next();
		        	
		        	String numexpOrigen = iProp.getString("NUMEXP_ORIGEN");
		        	LOGGER.warn("numexp_origen "+numexpOrigen);
	    		
		        	if (iProp.getString("EXTRACTO")!=null) extracto = (String)iProp.getString("EXTRACTO"); else extracto = "";
		        	if (iProp.getString("DEBATE")!=null) debate = (String)iProp.getString("DEBATE"); else debate = "";
		        	if (iProp.getString("INFORMES")!=null) informes = (String)iProp.getString("INFORMES"); else informes = "";
		        	if (iProp.getString("DICTAMEN")!=null) dictamen = (String)iProp.getString("DICTAMEN"); else dictamen = "";
		        	if (iProp.getString(strCampo)!=null) acuerdos = (String)iProp.getString(strCampo); else acuerdos = "";

		        	LOGGER.warn("extracto "+extracto);
		
		        	if (iProp.getString("RESULTADO_VOTACION")!=null) {
		        		votacion = iProp.getString("RESULTADO_VOTACION");
		        	} else {
		        		votacion="";
		        	}
		
		        	cct.setSsVariable("EXTRACTO", extracto.toUpperCase());

		        	if(!"".equals(debate)){
		        		debate = "\t"+debate+"\n\n";
		        	}
		        	setLongVariable(rulectx, "DEBATE", debate);
		        	
		        	if(!"".equals(acuerdos)){
		        		acuerdos = "\t"+acuerdos+"\n\n";
		        	}
		        	setLongVariable(rulectx, "ACUERDOS", acuerdos);
		        	
		        	String sOrden = String.valueOf(nOrden);
		        	if(urgencia){
		        		String organo = SecretariaUtil.getOrgano(rulectx);
		        		if("PLEN".equals(organo)){
		        			//se pone mas 2 porque hay q añadir un punto mas de presidencia
		        			sOrden = String.valueOf(numPropuesta+2)+"."+String.valueOf(nOrden);
		        		}
		        		else{
		        			sOrden = String.valueOf(numPropuesta+1)+"."+String.valueOf(nOrden);
		        		}
		        	}
		        	cct.setSsVariable("ORDEN", sOrden);
		        	
		        	if(!"".equals(votacion)){
		        		votacion = votacion+"\n";
		        	}
		        	cct.setSsVariable("RESULTADO_VOTACION", votacion);
		        	
		        	if(!"".equals(informes)){
		        		informes = informes+"\n\n";
		        	}
		        	cct.setSsVariable("INFORMES", informes);
		        	
		        	if(!"".equals(dictamen)){
		        		dictamen = "\t"+dictamen+"\n\n";
		        	}
		        	cct.setSsVariable("DICTAMEN", dictamen+"\n\n");
		        	
		        	strQuery = "WHERE NUMEXP = '" + numexpOrigen +"'";
		 	        IItemCollection coll = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
		 	        
		 	        Iterator itP = coll.iterator();
			        IItem item = null;
			        
			        while (itP.hasNext()) {
			        	
		                item = ((IItem)itP.next());
		                fechaEmision = item.getString("FECHA_EMISION");
		                if(fechaEmision!= null){
			                fechaEmision = dateformat.format(inputDateformat.parse(fechaEmision));
		                }
		                else{
		                	fechaEmision = "";
		                }
		                //logger.warn("fecha_emision. "+fecha_emision);
		                cct.setSsVariable("FECHA_EMISION", fechaEmision);
		                if (item.getString("CARGO")!=null) {
		                	cargo = (String)item.getString("CARGO");
		                } else {
		                	cargo = "";
		                }
		                cct.setSsVariable("CARGO", cargo);
			        }

			        // Abrir transacción para que no se pueda generar un documento sin fichero
			        cct.beginTX();
			        
			        String strNombreDoc = "";
			        String strNombreDocCab = "";
			        String strNombreDocPie = "";
			        
			        String strDescr = "";
			        
			        int longPropu = extracto.length();
			        
			        if(!urgencia){
			        	strNombreDoc = strNombreDocProp;
				        strNombreDocCab = strNombreDocCabProp;
		        		strNombreDocPie = strNombreDocPieProp;		        		
		        		if(longPropu>=78){
		        			strDescr = nOrden +" . "+ extracto.substring(0, 78)+", numexp="+numexpOrigen+"";
		        		} else {
		        			strDescr = nOrden +" . "+ extracto +", numexp="+numexpOrigen+"";
		        		}		        		
			        } else {
			        	strNombreDoc = strNombreDocUrg;
				        strNombreDocCab = strNombreDocCabUrg;
		        		strNombreDocPie = strNombreDocPieUrg;
		        		
		        		if(longPropu>=78){
		        			strDescr = nOrden +" . "+ extracto.substring(0, 78)+", numexp="+numexpOrigen+"";
		        		}
		        		else{
		        			strDescr = nOrden +" . "+ extracto +", numexp="+numexpOrigen+"";
		        		}		        		
			        }
			       
			        DocumentosUtil.generarDocumento(rulectx, strNombreDocCab, null);
		        	DocumentosUtil.generarDocumento(rulectx, strNombreDocPie, null);
			        DipucrCommonFunctions.concatenaPartes(rulectx, numexpOrigen, strNombreDocCab, strNombreDocPie, strNombreDoc, strDescr, ooHelper, extensionEntidad);
									
					
					//Borramos las variables de sistema
					cct.deleteSsVariable("EXTRACTO");
					deleteLongVariable(rulectx, "DEBATE");
					deleteLongVariable(rulectx, "ACUERDOS");
					cct.deleteSsVariable("ORDEN");
					cct.deleteSsVariable("RESULTADO_VOTACION");
					cct.deleteSsVariable("CARGO");
					cct.deleteSsVariable("FECHA_EMISION");
					cct.deleteSsVariable("NUM_SESION_ANTERIOR");
					cct.deleteSsVariable("INFORMES");
		        	cct.deleteSsVariable("DICTAMEN");
		        	
		        	if(!it.hasNext() && !urgencia){
		        		it = listUrgencias.iterator();
		        		urgencia = true;
		        		nOrden = 0;
		        	}
		        }
		       
	        } catch(Exception e) {
	        	// Si se produce algún error se hace rollback de la transacción

				cct.endTX(false);

				String extraInfo = null;
				Throwable eCause = e.getCause();
				
				if (eCause instanceof ISPACException) {
					if (eCause.getCause() instanceof NoConnectException) {
						extraInfo = "exception.extrainfo.documents.openoffice.off"; 
					} else {
						extraInfo = eCause.getCause().getMessage();
					}
				} else if (eCause instanceof DisposedException) {
					extraInfo = "exception.extrainfo.documents.openoffice.stop";
				} else {
					extraInfo = e.getMessage();
				}
				LOGGER.error(extraInfo, e);
				throw new ISPACRuleException(extraInfo, e);
	        } finally {
				if (connectorSession != null) {
					gendocAPI.closeConnectorSession(connectorSession);
				}
			}
	    	
	    	// Si todo ha sido correcto se hace commit de la transacción
			cct.endTX(true);
	        
	        return true;
    		
        } catch(Exception e) {
        	throw new ISPACRuleException(e);
        } finally {
    		if(ooHelper != null) {
    			ooHelper.dispose();
    		}
    	}    	
    }
        	
    private void setLongVariable(IRuleContext rulectx, String nombre, String valor) throws ISPACRuleException {
		try {
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			IItem entity = entitiesAPI.createEntity("TSOL_LONG_VARS", rulectx.getNumExp());
			entity.set("NOMBRE", nombre);
			entity.set("VALOR", valor);
			entity.store(cct);
		} catch(Exception e) {
			throw new ISPACRuleException(e); 	
		}
	}
	
	private void deleteLongVariable(IRuleContext rulectx, String nombre) throws ISPACRuleException {
		try {
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "' AND NOMBRE='" + nombre + "'";
			entitiesAPI.deleteEntities("TSOL_LONG_VARS", strQuery);
		} catch(Exception e) {
			throw new ISPACRuleException(e); 	
		}
	}
	
	public void cancel(IRuleContext arg0) throws ISPACRuleException {
		// Nada que hacer al cancelar
	}
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }
}
