package es.dipucr.sigem.api.rule.procedures.sms;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class CargaListaMensajesRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    @SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        //Inicializo la lista de mensajes con los participantes del expediente
			//String strPlantillaTexto = Messages.getString("cTxt_sms");
	        String organo = SecretariaUtil.getNombreOrganoSesion(rulectx, rulectx.getNumExp());
	        String articulo = "";
	        IItem sesion = getSesion(rulectx);
	        if(organo.equals("PLENO")){
	        	articulo = "el";
	        }
	        else{
	        	articulo = "la";
	        }
	        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy"); 
	        String fecha = dateFormatter.format(sesion.getDate("FECHA"));
	        String strPlantillaTexto = "Se ha convocado la sesión que celebrará "+articulo+" "+organo+" de la Diputación " +
	        		"de Ciudad Real el día "+fecha+" a las "+sesion.getString("HORA");
	        IItemCollection partsCol = entitiesAPI.getParticipants(rulectx.getNumExp(), "", "");
	        Iterator<IItem> itParts = partsCol.iterator();
	        while(itParts.hasNext())
	        {
	        	IItem iParticipante = (IItem)itParts.next();
	        	
	        	int id_participante = iParticipante.getInt("ID");
	        	if (!existeReceptor(rulectx, id_participante))
	        	{
		        	IItem iMensaje = entitiesAPI.createEntity("TSOL_SMS", rulectx.getNumExp());
		        	iMensaje.set("ID_PARTICIPANTE", id_participante);
		        	iMensaje.set("NIF", iParticipante.getString("NDOC"));
		        	iMensaje.set("NOMBRE", iParticipante.getString("NOMBRE"));
		        	String strTfno = iParticipante.getString("TFNO_MOVIL");
		        	if(strTfno!=null && strTfno.length()>0)
		        	{
		        		iMensaje.set("TFNO_MOVIL", strTfno);
		        		if(telefonoValido(strTfno))
		        		{
		        			iMensaje.set("ENVIAR_SMS", "SI");
		        		}
		        		else
		        		{
		        			iMensaje.set("ENVIAR_SMS", "NO");
		        		}
		        	}
	        		else
	        		{
	        			iMensaje.set("ENVIAR_SMS", "NO");
	        		}
        			iMensaje.set("ENVIADO_SMS", "NO");
        			iMensaje.set("COMPROBANTE_SMS", "NO");
        			iMensaje.set("TEXTO", strPlantillaTexto);
		        	iMensaje.store(cct);
	        	}
	        }
        	return new Boolean(true);
        } 
	    catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido inicializar la lista de mensajes",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
	private IItem getSesion(IRuleContext rulectx) throws ISPACException 
	{
		IItem iSesion = null;
		IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
		String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
		IItemCollection itemCollection = entitiesAPI.queryEntities("SECR_SESION", strQuery);
		@SuppressWarnings("unchecked")
		Iterator<IItem> it = itemCollection.iterator();
		if (it.hasNext())
		{
			iSesion = (IItem)it.next();
		}
		return iSesion;
	}
	
    private boolean existeReceptor(IRuleContext rulectx, int id) throws ISPACRuleException
    {
    	try{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();

	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND ID_PARTICIPANTE="+id;
	        IItemCollection col = entitiesAPI.queryEntities("TSOL_SMS", strQuery);
	        return col.iterator().hasNext();
        } 
		catch(Exception e) 
		{
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("Error en existeReceptor.",e);
        }
    }
    
	private boolean telefonoValido(String strTfno)
	{
		boolean valido = strTfno != null && strTfno.length()==9;
		
		if(valido)
		{
			try
			{
				Integer.parseInt(strTfno);
			}
			catch(Exception e)
			{
				valido=false;
			}
		}
		return valido;
	}
 }