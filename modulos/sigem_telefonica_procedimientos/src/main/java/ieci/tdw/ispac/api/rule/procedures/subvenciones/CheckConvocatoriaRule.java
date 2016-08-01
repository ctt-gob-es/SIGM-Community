package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CheckConvocatoriaRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
	        IItem conv = rulectx.getItem();
        	String num_acuerdo_aprobacion = conv.getString("NUM_ACUERDO_APROBACION");
        	String num_decreto_aprobacion = conv.getString("NUM_DECRETO_APROBACION");
        	if (num_acuerdo_aprobacion!=null && num_acuerdo_aprobacion.length() > 0)
	        {
	        	String strRes = validarAcuerdo(rulectx, num_acuerdo_aprobacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
        	else if (num_decreto_aprobacion!=null && num_decreto_aprobacion.length() > 0)
	        {
        		String strRes = validarDecreto(rulectx, num_decreto_aprobacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }

        	String num_acuerdo_resolucion = conv.getString("NUM_ACUERDO_RESOLUCION");
        	String num_decreto_resolucion = conv.getString("NUM_DECRETO_RESOLUCION");
        	if (num_acuerdo_resolucion!=null && num_acuerdo_resolucion.length() > 0)
	        {
        		String strRes = validarAcuerdo(rulectx, num_acuerdo_resolucion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
	        else if (num_decreto_resolucion!=null && num_decreto_resolucion.length() > 0)
	        {
	        	String strRes = validarDecreto(rulectx, num_decreto_resolucion);
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
        	throw new ISPACRuleException("No se ha podido validar el cambio en la convocatoria",e);
        }
    	return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {
    	boolean ok = true;
    	try
    	{
	        IItem conv = rulectx.getItem();

	        //Se convierte el código de área a string para poder usarlo
        	//cómodamente desde las plantillas de reintegro
    		String strValor = conv.getString("AREA");
    		if (strValor != null)
    		{
	    		String strArea = "";
		        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		        String strQuery = "WHERE VALOR='" + strValor + "'";
		        IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_AREAS", strQuery);
		        Iterator it = coll.iterator();
		        if (it.hasNext())
		        {
		        	IItem item = (IItem)it.next();
		        	strArea = item.getString("SUSTITUTO");
		        }
		        conv.set("AREA_STR", strArea);
    		}
    		
    		//Reviso los números de acuerdo
        	String num_acuerdo_aprobacion = conv.getString("NUM_ACUERDO_APROBACION");
        	String num_decreto_aprobacion = conv.getString("NUM_DECRETO_APROBACION");
	        if (num_acuerdo_aprobacion!=null && num_acuerdo_aprobacion.length() > 0)
	        {
	        	tratarAcuerdo(rulectx, num_acuerdo_aprobacion, true);
	        }
	        else if (num_decreto_aprobacion!=null && num_decreto_aprobacion.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_aprobacion, true);
	        }

	        String num_acuerdo_resolucion = conv.getString("NUM_ACUERDO_RESOLUCION");
        	String num_decreto_resolucion = conv.getString("NUM_DECRETO_RESOLUCION");
	        if (num_acuerdo_resolucion!=null && num_acuerdo_resolucion.length() > 0)
	        {
	        	tratarAcuerdo(rulectx, num_acuerdo_resolucion, false);
	        }
	        else if (num_decreto_resolucion!=null && num_decreto_resolucion.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_resolucion, false);
	        }
        }
    	catch(Exception e)
    	{

    		if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido comprobar la convocatoria",e);
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
	
	private void tratarAcuerdo(IRuleContext rulectx, String num_acuerdo, boolean isAprobacion) throws ISPACRuleException
	{
		try
		{
			String strModo = "MODO";
			String strFecha = "FECHA";
			if (!isAprobacion) strModo = "MODO_RESOLUCION";
			if (!isAprobacion) strFecha = "FECHA_RESOLUCION";
			
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
	        IItem conv = rulectx.getItem();
    		if (organo!=null && organo.compareTo("PLEN")==0)
    		{
    			conv.set(strModo,"Acuerdo del Pleno Corporativo");
    		}
    		else if (organo!=null && organo.compareTo("JGOB")==0)
    		{
    			conv.set(strModo,"Acuerdo de la Junta de Gobierno");
    		}
    		else
    		{
    			conv.set(strModo,"Acuerdo");
    		}
    		Date fecha = sesion.getDate("FECHA");
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        conv.set(strFecha, dateformat.format(fecha));
	        
	        SimpleDateFormat df2 = new SimpleDateFormat("yyyy", new Locale("es"));
	        String strYear = df2.format(fecha);
	        if (isAprobacion)
	        {
	        	conv.set("ANIO", strYear);
	        }
	        else
	        {
	        	conv.set("ANIO_RESOLUCION", strYear);
	        }
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}

	private void tratarDecreto(IRuleContext rulectx, String num_decreto, boolean isAprobacion) throws ISPACRuleException
	{
		try
		{
			String strModo = "MODO";
			String strFecha = "FECHA";
			if (!isAprobacion) strModo = "MODO_RESOLUCION";
			if (!isAprobacion) strFecha = "FECHA_RESOLUCION";

			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
        	String year = num_decreto.substring(0, 4);
        	String numero = num_decreto.substring(5);
        	String strQuery = "WHERE ANIO="+year+" AND NUMERO_DECRETO="+numero;
        	IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
        	Iterator it = coll.iterator();
    		IItem decreto = (IItem)it.next();
	        IItem conv = rulectx.getItem();
			conv.set(strModo,"Decreto de la Presidencia");
    		Date fecha = decreto.getDate("FECHA_DECRETO");
	        SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
	        conv.set(strFecha, dateformat.format(fecha));
	        
	        SimpleDateFormat df2 = new SimpleDateFormat("yyyy", new Locale("es"));
	        String strYear = df2.format(fecha);
	        if (isAprobacion)
	        {
	        	conv.set("ANIO", strYear);
	        }
	        else
	        {
	        	conv.set("ANIO_RESOLUCION", strYear);
	        }
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}

}