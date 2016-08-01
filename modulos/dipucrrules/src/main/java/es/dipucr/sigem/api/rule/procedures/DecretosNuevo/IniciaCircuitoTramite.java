package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import java.util.Iterator;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class IniciaCircuitoTramite implements IRule{
	
	public static final Logger logger = Logger.getLogger(IniciaCircuitoTramite.class);
	/**
	 * Regla que 'tras iniciar' envia todos los documentos del trámite a firmar
	 * al circuito de firma que se haya especificado en el trámite.
	 * **/
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //------------------------------------------------------------------
			IItemCollection itColDocTramite = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), "(ID_TRAMITE="+rulectx.getTaskId()+")", "");
			Iterator <IItem> itDocTramite = itColDocTramite.iterator();
			while(itDocTramite.hasNext()){
				IItem docFirma = itDocTramite.next();
				CircuitosUtil.iniciarCircuitoTramite(rulectx, docFirma.getInt("ID"));
			}
		}catch(ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean resultado = false;
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //------------------------------------------------------------------
			IItemCollection itColDocTramite = DocumentosUtil.getDocumentos(cct, rulectx.getNumExp(), "(ID_TRAMITE="+rulectx.getTaskId()+")", "");
			Iterator <IItem> itDocTramite = itColDocTramite.iterator();
			if(itDocTramite.hasNext()){
				resultado= true;
			}
			else{
				rulectx.setInfoMessage("No existe documento en la trámite para mandar a firmar.");
			}
		}catch(ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return resultado;
	}

}
