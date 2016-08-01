package ieci.tdw.ispac.api.rule.procedures.negociado;

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

/**
 * 
 * @author teresa
 * Comprueba en la entidad SGN_NEGOCIADO que se haya introducido un número de propuesta o de decreto correcto.
 * Si es válido asigna el modo de acuerdo, fecha y año de propuesta o decreto a la entidad. 
 *
 */

public class CheckConvocatoriaNegRule implements IRule {
	
	protected String STR_aprobacion = "APROBACION";
	protected String STR_adjudicacion = "ADJUDICACION";
	protected String STR_comision = "COMISION";
	protected String STR_devolucion = "DEVOLUCION";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	try
    	{
	        IItem conv = rulectx.getItem();
	        
	        //Acuerdo aprobación
        	String num_acuerdo_aprobacion = conv.getString("NUM_ACUERDO_APROBACION");
        	String num_decreto_aprobacion = conv.getString("NUM_DECRETO_APROBACION");
        	if (num_acuerdo_aprobacion!=null && num_acuerdo_aprobacion.length() > 0)
	        {
	        	String strRes = validarPropuesta(rulectx, num_acuerdo_aprobacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }else if (num_decreto_aprobacion!=null && num_decreto_aprobacion.length() > 0)
	        {
        		String strRes = validarDecreto(rulectx, num_decreto_aprobacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }

        	//Acuerdo adjudicación
        	String num_acuerdo_adjudicacion = conv.getString("NUM_ACUERDO_ADJUDICACION");
        	String num_decreto_adjudicacion = conv.getString("NUM_DECRETO_ADJUDICACION");
        	if (num_acuerdo_adjudicacion!=null && num_acuerdo_adjudicacion.length() > 0)
	        {
        		String strRes = validarPropuesta(rulectx, num_acuerdo_adjudicacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }else if (num_decreto_adjudicacion!=null && num_decreto_adjudicacion.length() > 0)
	        {
	        	String strRes = validarDecreto(rulectx, num_decreto_adjudicacion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
        
        	//Acuerdo comisión
        	String num_acuerdo_comision = conv.getString("NUM_ACUERDO_COMISION");
        	String num_decreto_comision = conv.getString("NUM_DECRETO_COMISION");
        	if (num_acuerdo_comision!=null && num_acuerdo_comision.length() > 0)
	        {
        		String strRes = validarPropuesta(rulectx, num_acuerdo_comision);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }else if (num_decreto_comision!=null && num_decreto_comision.length() > 0)
	        {
	        	String strRes = validarDecreto(rulectx, num_decreto_comision);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }
    	
        	//Acuerdo devolución
        	String num_acuerdo_devolucion = conv.getString("NUM_ACUERDO_DEVOLUCION");
        	String num_decreto_devolucion = conv.getString("NUM_DECRETO_DEVOLUCION");
        	if (num_acuerdo_devolucion!=null && num_acuerdo_devolucion.length() > 0)
	        {
        		String strRes = validarPropuesta(rulectx, num_acuerdo_devolucion);
	        	if (strRes != null)
	        	{
	        		rulectx.setInfoMessage(strRes);
	        		return false;
	        	}
	        }else if (num_decreto_devolucion!=null && num_decreto_devolucion.length() > 0)
	        {
	        	String strRes = validarDecreto(rulectx, num_decreto_devolucion);
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
	        /*
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
    		*/
    		
    		//Reviso los números de acuerdo
	        
	        //Acuerdo aprobación
        	String num_acuerdo_aprobacion = conv.getString("NUM_ACUERDO_APROBACION");
        	String num_decreto_aprobacion = conv.getString("NUM_DECRETO_APROBACION");
	        if (num_acuerdo_aprobacion!=null && num_acuerdo_aprobacion.length() > 0)
	        {
	        	tratarPropuesta(rulectx, num_acuerdo_aprobacion, STR_aprobacion);
	        }
	        else if (num_decreto_aprobacion!=null && num_decreto_aprobacion.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_aprobacion, STR_aprobacion);
	        }

	        //Acuerdo adjudicación
	        String num_acuerdo_adjudicacion = conv.getString("NUM_ACUERDO_ADJUDICACION");
	        String num_decreto_adjudicacion = conv.getString("NUM_DECRETO_ADJUDICACION");
	        if (num_acuerdo_adjudicacion!=null && num_acuerdo_adjudicacion.length() > 0)
	        {
	        	tratarPropuesta(rulectx, num_acuerdo_adjudicacion, STR_adjudicacion);
	        }
	        else if (num_decreto_adjudicacion!=null && num_decreto_adjudicacion.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_adjudicacion, STR_adjudicacion);
	        }
	        
	        //Acuerdo comisión
	        String num_acuerdo_comision = conv.getString("NUM_ACUERDO_COMISION");
	        String num_decreto_comision = conv.getString("NUM_DECRETO_COMISION");
	        if (num_acuerdo_comision!=null && num_acuerdo_comision.length() > 0)
	        {
	        	tratarPropuesta(rulectx, num_acuerdo_comision, STR_comision);
	        }
	        else if (num_decreto_comision!=null && num_decreto_comision.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_comision, STR_comision);
	        }
	        
	        //Acuerdo devolución
	        String num_acuerdo_devolucion = conv.getString("NUM_ACUERDO_DEVOLUCION");
	        String num_decreto_devolucion = conv.getString("NUM_DECRETO_DEVOLUCION");
	        if (num_acuerdo_devolucion!=null && num_acuerdo_devolucion.length() > 0)
	        {
	        	tratarPropuesta(rulectx, num_acuerdo_devolucion, STR_devolucion);
	        }
	        else if (num_decreto_devolucion!=null && num_decreto_devolucion.length() > 0)
	        {
	        	tratarDecreto(rulectx, num_decreto_devolucion, STR_devolucion);
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
	
	private String validarPropuesta(IRuleContext rulectx, String num_acuerdo) throws ISPACRuleException
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
	
	private void tratarPropuesta(IRuleContext rulectx, String num_acuerdo, String strTipoAcuerdo) throws ISPACRuleException
	{
		try
		{
			String strModo = null;
			String strFecha = null;
			
			if (strTipoAcuerdo.equals(STR_aprobacion)){
				strModo = "MODO";
				strFecha = "FECHA";
			}else if (strTipoAcuerdo.equals(STR_adjudicacion)){
				strModo = "MODO_ADJUDICACION";
				strFecha = "FECHA_ADJUDICACION";
			}else if (strTipoAcuerdo.equals(STR_comision)){
				strModo = "MODO_COMISION";
				strFecha = "FECHA_COMISION";
			}else if (strTipoAcuerdo.equals(STR_devolucion)){
				strModo = "MODO_DEVOLUCION";
				strFecha = "FECHA_DEVOLUCION";
			}
			
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
    		else if (organo!=null && organo.compareTo("JGOV")==0)
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
	        /*
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
	        */
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}

	private void tratarDecreto(IRuleContext rulectx, String num_decreto, String strTipoAcuerdo) throws ISPACRuleException
	{
		try
		{
			String strModo = null;
			String strFecha = null;
			
			if (strTipoAcuerdo.equals(STR_aprobacion)){
				strModo = "MODO";
				strFecha = "FECHA";
			}else if (strTipoAcuerdo.equals(STR_adjudicacion)){
				strModo = "MODO_ADJUDICACION";
				strFecha = "FECHA_ADJUDICACION";
			}else if (strTipoAcuerdo.equals(STR_comision)){
				strModo = "MODO_COMISION";
				strFecha = "FECHA_COMISION";
			}else if (strTipoAcuerdo.equals(STR_devolucion)){
				strModo = "MODO_DEVOLUCION";
				strFecha = "FECHA_DEVOLUCION";
			}

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
	        /*
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
	        */
		}
		catch(Exception e)
		{
        	throw new ISPACRuleException(e);
		}
	}

}