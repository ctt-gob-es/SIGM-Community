package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.util.Iterator;
import java.util.List;

public class GenerateTrasladosRule implements IRule {

	private OpenOfficeHelper ooHelper = null;
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtener las propuestas y urgencias incluidas en la sesión
	        List listPropuestas = CommonFunctions.getPropuestas(rulectx, entitiesAPI);
	        Iterator it = listPropuestas.iterator();
	        IItem iProp = null;

	        //Para cada propuesta se generan los tags a incluir en el certificado de acuerdos/dictamen
	        String extracto = "";
	        String acuerdos = "";
	        String dictamen = "";
	        String num_acuerdo = "";
	        String num_dictamen = "";
        	String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
        	boolean esAcuerdo = (strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0 );
	        while (it.hasNext())
	        {
	        	iProp = (IItem)it.next();
	        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
	        	//Sólo generamos el documento si hace falta
	        	if (tieneTrasladados(rulectx,numexp_origen))
	        	{
		        	if (iProp.get("EXTRACTO")!=null) extracto = (String)iProp.get("EXTRACTO"); else extracto = "";
		        	cct.setSsVariable("EXTRACTO", extracto);
		        	if (esAcuerdo)
		        	{
			        	if (iProp.get("ACUERDOS")!=null) acuerdos = (String)iProp.get("ACUERDOS"); else acuerdos = "";
			        	acuerdos = acuerdos.replaceAll("\r\n", "\r");
			        	cct.setSsVariable("ACUERDOS", acuerdos);
			        	num_acuerdo = getNumero(rulectx, numexp_origen,"SECR_ACUERDO");
			        	cct.setSsVariable("NUMERO_ACUERDO", num_acuerdo);
		        	}
		        	else
		        	{
			        	if (iProp.get("DICTAMEN")!=null) dictamen = (String)iProp.get("DICTAMEN"); else dictamen = "";
			        	dictamen = dictamen.replaceAll("\r\n", "\r");
			        	cct.setSsVariable("DICTAMEN", dictamen);
			        	num_dictamen = getNumero(rulectx, numexp_origen,"SECR_DICTAMEN");
			        	cct.setSsVariable("NUMERO_DICTAMEN", num_dictamen);
		        	}
		        	
		        	//Generación del documento
		        	String strNombreDoc = "";
		        	String strNombreDocCab = "";
		        	String strNombreDocPie = "";
					String strNumero = esAcuerdo? "NUMERO_ACUERDO":"NUMERO_DICTAMEN";
	        		String strDescr = cct.getSsVariable(strNumero);		        		
		        	if (esAcuerdo)
		        	{
		        		strNombreDoc = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasAc");
		        		strNombreDocCab = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasAcCab");
		        		strNombreDocPie = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasAcPie");
		        	}
		        	else
		        	{
		        		strNombreDoc = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasDi");
		        		strNombreDocCab = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasDiCab");
		        		strNombreDocPie = CommonFunctions.getNombreDoc(rulectx, "Secr-TrasDiPie");
		        	}
		        	CommonFunctions.generarDocumento(rulectx, strNombreDocCab, null);
		        	CommonFunctions.generarDocumento(rulectx, strNombreDocPie, null);
		        	CommonFunctions.concatenaPartes(rulectx, numexp_origen, strNombreDocCab, strNombreDocPie, strNombreDoc, strDescr, ooHelper);
		        	
		        	cct.deleteSsVariable("EXTRACTO");
		        	if (esAcuerdo)
		        	{
			        	cct.deleteSsVariable("ACUERDOS");	        	
			        	cct.deleteSsVariable("NUMERO_ACUERDO");
		        	}
		        	else
		        	{
			        	cct.deleteSsVariable("DICTAMEN");	        	
			        	cct.deleteSsVariable("NUMERO_DICTAMEN");
		        	}
	        	}
	        }
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se han podido generar las certificaciones",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private String getNumero(IRuleContext rulectx, String numExp, String strTabla) throws ISPACException 
	{
		String numAcuerdo = "?";
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP_ORIGEN='"+numExp+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities(strTabla, strQuery);
		Iterator it = itemCollection.iterator();
		if (it.hasNext())
		{
			IItem iAcuerdo = (IItem)it.next();
			numAcuerdo = iAcuerdo.getString("NUMERO") + "/" + iAcuerdo.getString("YEAR"); 
		}
		else
		{
			throw new ISPACInfo("Se ha producido un error al obtener el número de acuerdo/dictamen.");
		}
		return numAcuerdo;
	}

	private boolean tieneTrasladados(IRuleContext rulectx, String numExp) throws ISPACException 
	{
		boolean tiene = false;
		
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();

		String strQuery = "WHERE ROL='TRAS' AND NUMEXP='"+numExp+"'";
    	IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
    	Iterator itParticipantes = participantes.iterator();
    	if (itParticipantes.hasNext())
    	{
    		tiene = true;
    	}
		
		return tiene;
	}

}