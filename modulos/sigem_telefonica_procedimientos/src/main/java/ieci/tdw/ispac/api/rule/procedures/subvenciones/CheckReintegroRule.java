package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class CheckReintegroRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	boolean ok = true;
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Obtención del reintegro antes y después de haber sido guardado
	        IItem reintegroNew = rulectx.getItem();
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SUBV_REINTEGRO", strQuery);
	        Iterator it = coll.iterator();
	        if (!it.hasNext())
	        {
	        	rulectx.setInfoMessage("No se ha encontrado el número de expediente del Reintegro de Subvenciones");
	        	ok = false;
	        }
	        else
	        {
		        //Comprobación del número de expediente de la solicitud
	        	String strNumexpSolNew = reintegroNew.getString("NUMEXP_SOL");
		        strQuery = "WHERE NUMEXP='" + strNumexpSolNew + "'";
		        coll = entitiesAPI.queryEntities("SUBV_SOLICITUD", strQuery);
		        it = coll.iterator();
		        if (!it.hasNext())
		        {
		        	rulectx.setInfoMessage("El número de expediente '"+strNumexpSolNew+"' no se corresponde con una Solicitud de Subvenciones válida");
		        	ok = false;
		        }
	        }
	        
	        //Comprobación del acuerdo/decreto de inicio
	        IItem reintegro = rulectx.getItem();
        	String num_acuerdo_ini = reintegro.getString("NUM_ACUERDO_INI");
        	String num_decreto_ini = reintegro.getString("NUM_DECRETO_INI");
        	if (num_acuerdo_ini!=null && num_acuerdo_ini.length() > 0)
	        {
	        	String strRes = validarAcuerdo(rulectx, num_acuerdo_ini);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
        	else if (num_decreto_ini!=null && num_decreto_ini.length() > 0)
	        {
        		String strRes = validarDecreto(rulectx, num_decreto_ini);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }

        	//Comprobación del acuerdo/decreto de resolución
        	String num_acuerdo_fin = reintegro.getString("NUM_ACUERDO_FIN");
        	String num_decreto_fin = reintegro.getString("NUM_DECRETO_FIN");
        	if (num_acuerdo_fin!=null && num_acuerdo_fin.length() > 0)
	        {
        		String strRes = validarAcuerdo(rulectx, num_acuerdo_fin);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
	        else if (num_decreto_fin!=null && num_decreto_fin.length() > 0)
	        {
	        	String strRes = validarDecreto(rulectx, num_decreto_fin);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
	        
        }
    	catch(Exception e)
    	{
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido validar el cambio en la entidad de reintegro",e);
        }
    	return ok;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	boolean ok = true;
    	try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        IItem reintegroNew = rulectx.getItem();

	        //Solicitud
        	String strNumexpSolNew = reintegroNew.getString("NUMEXP_SOL");
	        String strQuery = "WHERE NUMEXP='" + strNumexpSolNew + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SUBV_SOLICITUD", strQuery);
        	IItem solicitud = (IItem)coll.iterator().next();

        	//Convocatoria
        	String strNumexpConv = solicitud.getString("NUMEXP_CONV");
	        strQuery = "WHERE NUMEXP='" + strNumexpConv + "'";
	        coll = entitiesAPI.queryEntities("SUBV_CONVOCATORIA", strQuery);
        	IItem convocatoria = (IItem)coll.iterator().next();

	        //Solicitante
	        strQuery = "WHERE NUMEXP='" + strNumexpSolNew + "'";
	        coll = entitiesAPI.queryEntities("SUBV_ENTIDAD", strQuery);
        	IItem entidad = (IItem)coll.iterator().next();

        	//Se guarda una copia de los datos de la solicitud
        	String strNombre = entidad.getString("NOMBRE");
        	reintegroNew.set("NOMBRE", strNombre);
        	String strTitulo = solicitud.getString("PROYECTO");
        	reintegroNew.set("TITULO", strTitulo);
        	String strArea = convocatoria.getString("AREA");
        	reintegroNew.set("AREA", strArea);
        	
	        //Se relacionan los expedientes de reintegro y solicitud
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        coll = entitiesAPI.queryEntities("SUBV_REINTEGRO", strQuery);
	        IItem reintegroOld = (IItem)(coll.iterator().next());
        	String strNumexpSolOld = reintegroOld.getString("NUMEXP_SOL");
        	if ( (strNumexpSolOld==null || strNumexpSolOld.length() == 0) && strNumexpSolNew.length() > 0 )
        	{
        		//Añadir la relación
        		int entityId = CommonFunctions.getEntityId(rulectx, "SPAC_EXP_RELACIONADOS");
        		IItem relacion = entitiesAPI.createEntity(entityId);
        		relacion.set("NUMEXP_PADRE", strNumexpSolNew);
        		relacion.set("NUMEXP_HIJO", rulectx.getNumExp());
        		relacion.set("RELACION", "Reintegro/Solicitud");
        		relacion.store(cct);
        	}
        	else if ( strNumexpSolOld.length() > 0 && (strNumexpSolNew==null || strNumexpSolNew.length() == 0) )
        	{
        		//Eliminar la relación
        		strQuery = "WHERE NUMEXP_PADRE = '" + strNumexpSolOld + "'" +
        			" AND NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
    			entitiesAPI.deleteEntities("SPAC_EXP_RELACIONADOS", strQuery);
        	}
        	else if ( strNumexpSolOld.compareTo(strNumexpSolNew) != 0)
        	{
        		//Modificar la relación
        		strQuery = "WHERE NUMEXP_PADRE = '" + strNumexpSolOld + "'" +
    				" AND NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
        		coll = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
        		Iterator it = coll.iterator();
        		if (it.hasNext())
        		{
        			IItem relacion = (IItem)it.next();
	        		relacion.set("NUMEXP_PADRE", strNumexpSolNew);
	        		relacion.store(cct);
        		}
        	}
        	
    		//Reviso los números de acuerdo
        	String num_acuerdo_ini = reintegroNew.getString("NUM_ACUERDO_INI");
        	String num_decreto_ini = reintegroNew.getString("NUM_DECRETO_INI");
	        if (num_acuerdo_ini!=null && num_acuerdo_ini.length() > 0)
	        {
	        	tratarAcuerdo(rulectx, num_acuerdo_ini, true);
	        }
	        else if (num_decreto_ini!=null && num_decreto_ini.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_ini, true);
	        }

	        String num_acuerdo_fin = reintegroNew.getString("NUM_ACUERDO_FIN");
        	String num_decreto_fin = reintegroNew.getString("NUM_DECRETO_FIN");
	        if (num_acuerdo_fin!=null && num_acuerdo_fin.length() > 0)
	        {
	        	tratarAcuerdo(rulectx, num_acuerdo_fin, false);
	        }
	        else if (num_decreto_fin!=null && num_decreto_fin.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_fin, false);
	        }
        	
        }
    	catch(Exception e)
    	{
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido comprobar la solicitud",e);
        }
        return new Boolean(ok);
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private String validarAcuerdo(IRuleContext rulectx, String num_acuerdo) throws ISPACRuleException
	{
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	    	if (num_acuerdo.length() < 6)
	    	{
	    		return "El formato del número de acuerdo no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
	    	}
	    	String year = num_acuerdo.substring(0, 4);
	    	String numero = num_acuerdo.substring(5);
	    	try
	    	{
	    		Integer.parseInt(year);
	    		Integer.parseInt(numero);
	    	}
	    	catch(NumberFormatException e)
	    	{
	    		return "El formato del número de acuerdo no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
	    	}
	    	String strQuery = "WHERE YEAR="+year+" AND NUMERO="+numero;
	    	IItemCollection coll = entitiesAPI.queryEntities("SECR_ACUERDO", strQuery);
	    	Iterator it = coll.iterator();
	    	if (!it.hasNext())
	    	{
	    		return "No se encuentra el acuerdo con número " + num_acuerdo;
	    	}
			IItem acuerdo = (IItem)it.next();
			String numexpSesion = acuerdo.getString("NUMEXP");
			strQuery = "WHERE NUMEXP='"+numexpSesion+"'";
			coll = entitiesAPI.queryEntities("SECR_SESION", strQuery);
			it = coll.iterator();
	    	if (!it.hasNext())
	    	{
	    		return "No se encuentra la sesión de gobierno asociada al acuerdo " + num_acuerdo;
	    	}
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
		return null;
	}
	
	private String validarDecreto(IRuleContext rulectx, String num_decreto) throws ISPACRuleException
	{
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
        	if (num_decreto.length() < 6)
        	{
        		return "El formato del número de decreto no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
        	}
        	String year = num_decreto.substring(0, 4);
        	String numero = num_decreto.substring(5);
        	try
        	{
        		Integer.parseInt(year);
        		Integer.parseInt(numero);
        	}
        	catch(NumberFormatException e)
        	{
        		return "El formato del número de decreto no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
        	}
        	String strQuery = "WHERE ANIO="+year+" AND NUMERO_DECRETO="+numero;
        	IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
        	Iterator it = coll.iterator();
        	if (!it.hasNext())
        	{
        		return "No se encuentra el decreto con número " + num_decreto;
        	}
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
		return null;
    }
	
	private void tratarAcuerdo(IRuleContext rulectx, String num_acuerdo, boolean isInicio) throws ISPACRuleException
	{
		try
		{
			String strModo = "MODO_INI";
			String strFecha = "FECHA_INI";
			if (!isInicio) strModo = "MODO_FIN";
			if (!isInicio) strFecha = "FECHA_FIN";
			
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
        	String year = num_acuerdo.substring(0, 4);
        	String numero = num_acuerdo.substring(5);
        	String strQuery = "WHERE YEAR="+year+" AND NUMERO="+numero;
        	IItemCollection coll = entitiesAPI.queryEntities("SECR_ACUERDO", strQuery);
        	Iterator it = coll.iterator();
    		IItem acuerdo = (IItem)it.next();
    		String numexpSesion = acuerdo.getString("NUMEXP");
    		strQuery = "WHERE NUMEXP='"+numexpSesion+"'";
    		coll = entitiesAPI.queryEntities("SECR_SESION", strQuery);
    		it = coll.iterator();
    		IItem sesion = (IItem)it.next();
    		String organo = sesion.getString("ORGANO");
	        IItem reintegro = rulectx.getItem();
    		if (organo!=null && organo.compareTo("PLEN")==0)
    		{
    			reintegro.set(strModo,"Acuerdo del Pleno Corporativo");
    		}
    		else if (organo!=null && organo.compareTo("JGOB")==0)
    		{
    			reintegro.set(strModo,"Acuerdo de la Junta de Gobierno");
    		}
    		else
    		{
    			reintegro.set(strModo,"Acuerdo");
    		}
    		Date fecha = sesion.getDate("FECHA");
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        reintegro.set(strFecha, dateformat.format(fecha));
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}

	private void tratarDecreto(IRuleContext rulectx, String num_decreto, boolean isInicio) throws ISPACRuleException
	{
		try
		{
			String strModo = "MODO_INI";
			String strFecha = "FECHA_INI";
			if (!isInicio) strModo = "MODO_FIN";
			if (!isInicio) strFecha = "FECHA_FIN";

			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
        	String year = num_decreto.substring(0, 4);
        	String numero = num_decreto.substring(5);
        	String strQuery = "WHERE ANIO="+year+" AND NUMERO_DECRETO="+numero;
        	IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
        	Iterator it = coll.iterator();
    		IItem decreto = (IItem)it.next();
	        IItem reintegro = rulectx.getItem();
	        reintegro.set(strModo,"Decreto de la Presidencia");
    		Date fecha = decreto.getDate("FECHA_DECRETO");
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        reintegro.set(strFecha, dateformat.format(fecha));
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}
	
}