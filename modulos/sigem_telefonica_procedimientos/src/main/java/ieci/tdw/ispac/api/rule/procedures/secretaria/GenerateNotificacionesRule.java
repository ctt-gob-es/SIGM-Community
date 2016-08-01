package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class GenerateNotificacionesRule implements IRule {

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

	        //Para cada propuesta se generan los tags a incluir en el documento de notificación
        	String strOrgano = CommonFunctions.getOrganoSesion(rulectx, null);
        	boolean esAcuerdo = (strOrgano.compareTo("PLEN")==0 || strOrgano.compareTo("JGOB")==0 );
	        String extracto = "";
	        String acuerdos = "";
	        String dictamen = "";
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        String fecha = dateformat.format(new Date());
	        int nOrden = 0;
	        String numAcuerdo = "";
	        String numDictamen = "";
	        while (it.hasNext())
	        {
	        	nOrden++;
	        	iProp = (IItem)it.next();
	        	String numexp_origen = iProp.getString("NUMEXP_ORIGEN");
	        	if (iProp.get("EXTRACTO")!=null) extracto = (String)iProp.get("EXTRACTO"); else extracto = "";
	        	cct.setSsVariable("EXTRACTO", extracto);
	        	cct.setSsVariable("FECHA", fecha);
	        	if (esAcuerdo)
	        	{
		        	if (iProp.get("ACUERDOS")!=null) acuerdos = (String)iProp.get("ACUERDOS"); else acuerdos = "";
		        	acuerdos = acuerdos.replaceAll("\r\n", "\r");
		        	cct.setSsVariable("ACUERDOS", acuerdos);
		        	numAcuerdo = createNumero(rulectx, numexp_origen, "SECR_ACUERDO"); 
		        	cct.setSsVariable("NUMERO_ACUERDO", numAcuerdo);
	        	}
	        	else
	        	{
		        	if (iProp.get("DICTAMEN")!=null) dictamen = (String)iProp.get("DICTAMEN"); else dictamen = "";
		        	dictamen = dictamen.replaceAll("\r\n", "\r");
		        	cct.setSsVariable("DICTAMEN", dictamen);
		        	numDictamen = createNumero(rulectx, numexp_origen, "SECR_DICTAMEN"); 
		        	cct.setSsVariable("NUMERO_DICTAMEN", numDictamen);
	        	}
	        	
	        	//Se genera una notificación por cada participante de la propuesta
				String strQuery = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+numexp_origen+"' ORDER BY ID";
	        	IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
	        	Iterator itParticipante = participantes.iterator();
				String ndoc = "";
				String nombre = "";
		    	String dirnot = "";
		    	String c_postal = "";
		    	String localidad = "";
		    	String caut = "";
		    	String tipoDireccion = "";//[eCenpri-Felipe #956]
	        	while (itParticipante.hasNext())
	        	{
	        		IItem participante = (IItem)itParticipante.next();

					// Añadir a la sesion los datos para poder utilizar <ispatag sessionvar='var'> en la plantilla
		        	if ((String)participante.get("NDOC")!=null) ndoc = (String)participante.get("NDOC");
		        	if ((String)participante.get("NOMBRE")!=null) nombre = (String)participante.get("NOMBRE");
		        	if ((String)participante.get("DIRNOT")!=null) dirnot = (String)participante.get("DIRNOT");
		        	if ((String)participante.get("C_POSTAL")!=null) c_postal = (String)participante.get("C_POSTAL");
		        	if ((String)participante.get("LOCALIDAD")!=null) localidad = (String)participante.get("LOCALIDAD");
		        	if ((String)participante.get("CAUT")!=null) caut = (String)participante.get("CAUT");
		        	if ((String)participante.get("TIPO_DIRECCION")!=null) tipoDireccion = (String)participante.get("TIPO_DIRECCION"); //[eCenpri-Felipe #956]
		        	cct.setSsVariable("NDOC", ndoc);
		        	cct.setSsVariable("NOMBRE", nombre);
		        	cct.setSsVariable("DIRNOT", dirnot);
		        	cct.setSsVariable("C_POSTAL", c_postal);
		        	cct.setSsVariable("LOCALIDAD", localidad);
		        	cct.setSsVariable("CAUT", caut);
		        	cct.setSsVariable("TIPO_DIRECCION", tipoDireccion);//[eCenpri-Felipe #956]
		        	
		        	//Generación del documento
		        	String strNombreDoc = "";
		        	String strNombreDocCab = "";
		        	String strNombreDocPie = "";
		        	String strNombreDocAcu = "";
					String strNumero = esAcuerdo? "NUMERO_ACUERDO":"NUMERO_DICTAMEN";
	        		String strDescr = cct.getSsVariable(strNumero) + " - " + cct.getSsVariable("NOMBRE") ;		        		
		        	if (esAcuerdo)
		        	{
		        		strNombreDoc = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifAc");
		        		strNombreDocCab = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifAcCab");
		        		strNombreDocPie = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifAcPie");
		        		strNombreDocAcu = CommonFunctions.getNombreDoc(rulectx, "Secr-AcuseAc");
		        	}
		        	else
		        	{
		        		strNombreDoc = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifDi");
		        		strNombreDocCab = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifDiCab");
		        		strNombreDocPie = CommonFunctions.getNombreDoc(rulectx, "Secr-NotifDiPie");
		        		strNombreDocAcu = CommonFunctions.getNombreDoc(rulectx, "Secr-AcuseDi");
		        	}
		        	CommonFunctions.generarDocumento(rulectx, strNombreDocCab, null);
		        	CommonFunctions.generarDocumento(rulectx, strNombreDocPie, null);
		        	CommonFunctions.concatenaPartes(rulectx, numexp_origen, strNombreDocCab, strNombreDocPie, strNombreDoc, strDescr, ooHelper);

		        	//Generación del acuse de recibo
		        	CommonFunctions.generarDocumento(rulectx, strNombreDocAcu, strDescr);
		        	
					cct.deleteSsVariable("NDOC");
					cct.deleteSsVariable("NOMBRE");
					cct.deleteSsVariable("DIRNOT");
					cct.deleteSsVariable("C_POSTAL");
					cct.deleteSsVariable("LOCALIDAD");
					cct.deleteSsVariable("CAUT");
					cct.deleteSsVariable("TIPO_DIRECCION");//[eCenpri-Felipe #956]
	        	}
	        	cct.deleteSsVariable("EXTRACTO");
	        	cct.deleteSsVariable("FECHA");
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
	        	
	        	//Guardo el número de acuerdo en la propuesta de origen
	        	String strNumero = esAcuerdo? numAcuerdo:numDictamen;
				strQuery = "WHERE NUMEXP='" + numexp_origen + "'";	        	
	        	IItemCollection props = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        	Iterator itProps = props.iterator();
	        	if (itProps.hasNext())
	        	{
	        		IItem propuesta = (IItem)itProps.next();
	        		propuesta.set("NUM_ACUERDO", strNumero);
	        		propuesta.store(cct);
	        	}
	        }
	        
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido generar las notificaciones",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

	private String createNumero(IRuleContext rulectx, String numExp, String strTabla) throws ISPACException
	{
		String numAcuerdo = "?";
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Primero se comprueba si el número de acuerdo ya existe
	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND NUMEXP_ORIGEN='"+numExp+"'";
	        IItemCollection collection = entitiesAPI.queryEntities(strTabla, strQuery);
	        Iterator it = collection.iterator();
	        if (it.hasNext())
	        {
	        	//Existe, se devuelve el numero como está
	        	IItem iAcuerdo = (IItem)it.next();
	        	numAcuerdo = iAcuerdo.getString("NUMERO") + "/" + iAcuerdo.getString("YEAR");
	        }
	        else
	        {
	        	//No existe, hay que crearlo
		        Calendar c = Calendar.getInstance();
		        int year = c.get(Calendar.YEAR);
	
		        strQuery = "WHERE YEAR='"+String.valueOf(year)+"'";
		        collection = entitiesAPI.queryEntities(strTabla, strQuery);
		        int numero = collection.toList().size();
		        numero = numero + 1;
		        numAcuerdo = String.valueOf(numero) + "/" + String.valueOf(year);
		        
		        IItem iAcuerdo = entitiesAPI.createEntity(strTabla, rulectx.getNumExp());
		        iAcuerdo.set("YEAR", year);
		        iAcuerdo.set("NUMERO", numero);
		        iAcuerdo.set("NUMEXP_ORIGEN", numExp);
		        iAcuerdo.store(cct);
	        }
        	return numAcuerdo;
    		
        } 
		catch(ISPACException e)
		{
        	throw new ISPACException(e);
		}
	}

}