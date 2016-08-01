package es.dipucr.sigem.api.rule.procedures.secretaria;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class CargaBorradorActaAnteriorRule implements IRule{
	
	protected static final Logger logger = Logger.getLogger(CargaBorradorActaAnteriorRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
			//Obtengo el número de la sesión que estamos
	        IItem itemSesion = SecretariaUtil.getSesion(rulectx, rulectx.getNumExp());
	        String numsesion = itemSesion.getString("NUMCONV");
	        String organo = itemSesion.getString("ORGANO");
	        
	        logger.warn("numsesion "+numsesion);
	        logger.warn("sesion "+organo);
	        
	        String [] vNumSesion = numsesion.split("/");
	        
	        logger.warn("vNumSesion "+vNumSesion);
	        if(vNumSesion!=null && vNumSesion.length>0){
	        	String numConv = vNumSesion[0];
	        	String anio = vNumSesion[1];
	        	
	        	int numAnterior = Integer.parseInt(numConv)-1;
	        	
	        	logger.warn("numConv "+numConv+"' / '"+anio);
	        	//Ontengo el expediente de la sesion anterior
	        	String strQuery = "WHERE YEAR='"+anio+"' AND ORGANO='"+organo+"' AND NUMERO="+numAnterior;
	        	IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery+" ORDER BY ID");
	        	Iterator<IItem> it = collection.iterator();
	        	while (it.hasNext()){
	        		IItem sesionAnterior = it.next();
	        		String numexpSesionAnterior = sesionAnterior.getString("NUMEXP");
	        		logger.warn("Num expediente sesion anterior. "+numexpSesionAnterior);
	        		
	        		//Vamos a obtener el documento del borrador del acta
	        		strQuery = "NOMBRE LIKE 'Borrador de Acta de Pleno'";
	        		IItemCollection documentos = DocumentosUtil.getDocumentos(cct, numexpSesionAnterior, strQuery, "FAPROBACION DESC");
	        		
	        		IItem itemDoc = (IItem) documentos.iterator().next();
	        		
	        		File fileDoc = DocumentosUtil.getFile(cct, itemDoc.getString("INFOPAG"), null, null);
	        		
	        		int tpdoc = DocumentosUtil.getIdTipoDocByCodigo(cct, "act-ses-ant");	        		
	        		IItem doc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, "Aprobación acta anterior ", fileDoc, itemDoc.getString("EXTENSION"));
	        	}
	        }
	        

		} catch (ISPACException e) {
			logger.error("Error. " + e.getMessage(), e);
			throw new ISPACRuleException("Error "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		
	}

}
