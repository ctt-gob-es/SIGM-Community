package es.dipucr.contratacion.rule;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;


import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class FicheroMayorTamanoRule implements IRule{
	private static final Logger logger = Logger.getLogger(FicheroMayorTamanoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean ficherosPeque = true; 
		StringBuffer ficherosGrandes = new StringBuffer("Los ficheros siguientes sobrepasan el tamaño de 20MB: ");
		try {
			String sQuery = "ID_TRAMITE="+rulectx.getTaskId();
			IItemCollection collectiDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), sQuery, "");
			Iterator<IItem> itCol = collectiDoc.iterator();
			while(itCol.hasNext()){
				IItem itDoc = itCol.next();
				String infopag = "";
				String ext = "";
				if(itDoc.getString("INFOPAG_RDE")!=null){
					infopag = itDoc.getString("INFOPAG_RDE");
					ext =  itDoc.getString("EXTENSION_RDE");
				}else{
					infopag = itDoc.getString("INFOPAG");
					ext =  itDoc.getString("EXTENSION");
				}
				File file = DocumentosUtil.getFile(rulectx.getClientContext(), infopag, "prueba", ext);
				//20971520B -> 20MB
				if(file.length()>20971520){
					ficherosGrandes.append(itDoc.getString("DESCRIPCION") +", ");
					ficherosPeque = false;
				}				
				file.delete();
				file = null;
			}
			if(!ficherosPeque){
				rulectx.setInfoMessage(ficherosGrandes.toString());
			}
		}catch (ISPACException e) {
			logger.error("Error al buscar los documentos con mayor tamaño. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al buscar los documentos con mayor tamaño. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		return ficherosPeque;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		
	}

}
