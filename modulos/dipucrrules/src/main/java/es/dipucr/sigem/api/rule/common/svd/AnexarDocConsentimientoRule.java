package es.dipucr.sigem.api.rule.common.svd;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class AnexarDocConsentimientoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(AnexarDocConsentimientoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		
		try {
			//comprueba desde donde se ha inicia el expediente
			//si no tiene utramitadora, idseccioniniciadora, seccioniniciadora quiere decir que se ha registrado desde el registro telemático
			//Por lo tanto adjunto la Solicitud Registro y si es al contrarío quiere decir o que se ha iniciado el expediente desde el 
			//registro distribuido o iniciado manualmente
			//IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
			//if(expediente.getString("UTRAMITADORA")==null && expediente.getString("IDSECCIONINICIADORA")==null && expediente.getString("SECCIONINICIADORA")==null){
				String sqlQuery = "(NOMBRE='Solicitud Registro') OR (TP_REG = 'ENTRADA' AND DESCRIPCION like '%Justificante de Registro%')";
				IItemCollection itColDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), sqlQuery, "FDOC ASC LIMIT 1");
				Iterator<IItem> itDoc = itColDoc.iterator();
				while(itDoc.hasNext()){
					IItem doc = itDoc.next();
					
					int tpdoc = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "doc-consent");
					String sTpdoc = DocumentosUtil.getTipoDocNombreByCodigo(rulectx.getClientContext(), "doc-consent");
					
					DocumentosUtil.copiaDocumentoSinFirmaTramite(rulectx, doc, rulectx.getNumExp(), rulectx.getTaskId(), tpdoc, sTpdoc);
				}
			//}
			
		} catch (ISPACException e) {
			logger.error("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
