package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.dao.procedure.ContratacionDocPresentarSDAO;

public class CargaDocumentacionPresentar implements IRule{
	private static final Logger logger = Logger.getLogger(CargaDocumentacionPresentar.class);
	public String sobre1 = "SOBRE A";
	public String sobre1TipoDoc = "1 - Documentación administrativa";
	public Vector<String> sobre1Docs = new Vector<String>();
	
	public String sobre2 = "SOBRE B";
	public String sobre2TipoDoc = "2 - Oferta técnica o evaluable mediante juicio de valor";
	public Vector<String> sobre2Docs = new Vector<String>();
	
	public String sobre3 = "SOBRE C";
	public String sobre3TipoDoc = "3 - Oferta económica o evaluable mediante fórmulas";
	public Vector<String> sobre3Docs = new Vector<String>();
	
	public String calle = "C/Toledo, nº1";
	public String localidad = "Ciudad Real";
	public String provincia = "Ciudad Real";
	public String lugar = "Diputación Provincial de Ciudad Real";
	public String cp = "13003";
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			/*************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			 /***********************************************************/
			
			insertarDatosSobreA(entitiesAPI, cct, rulectx.getNumExp());
			
			insertarDatosSobreB(entitiesAPI, cct, rulectx.getNumExp());
			
			insertarDatosSobreC(entitiesAPI, cct, rulectx.getNumExp());
			
			
	        
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error a la hora de insertar los sobres. ",e);
		}
		
		return new Boolean(true);
	}

	private void insertarDatosSobreA(IEntitiesAPI entitiesAPI, ClientContext cct, String numexp) throws ISPACRuleException {
		try {
			IItem itemDocPres = entitiesAPI.createEntity("CONTRATACION_DOC_PRESENTAR","");
			
			//Sobre A
			itemDocPres.set("NUMEXP", numexp);
			itemDocPres.set("NOMBRESOBRE", sobre1); 
			itemDocPres.set("TIPO_DOC", sobre1TipoDoc); 
			itemDocPres.set("CALLE", calle); 
			itemDocPres.set("LOCALIDAD", localidad); 
			itemDocPres.set("PROVINCIA", provincia); 
			itemDocPres.set("LUGAR", lugar);
			itemDocPres.set("CP", cp); 
			itemDocPres.store(cct);
	        
	        //Obtengo el id
	        int id = itemDocPres.getInt("ID");
	        
	        DbCnt cnt = cct.getConnection();
	        
	        for(int i = 0; i < sobre1Docs.size(); i++){
	        	ContratacionDocPresentarSDAO pcftdao = new ContratacionDocPresentarSDAO(cnt);
	 			pcftdao.createNew(cnt);
	 			pcftdao.set("FIELD", "LIST_DOC");
	 			pcftdao.set("REG_ID", id);
	 			pcftdao.set("VALUE", sobre1Docs.get(i));
	 			pcftdao.store(cnt);
	 			
	        }
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la inserción del sobre 1. ",e);
		}
		
	}
	
	private void insertarDatosSobreB(IEntitiesAPI entitiesAPI, ClientContext cct, String numexp) throws ISPACRuleException {
		try {
			IItem itemDocPres = entitiesAPI.createEntity("CONTRATACION_DOC_PRESENTAR","");
			
			//Sobre A
			itemDocPres.set("NUMEXP", numexp);
			itemDocPres.set("NOMBRESOBRE", sobre2); 
			itemDocPres.set("TIPO_DOC", sobre2TipoDoc); 
			itemDocPres.set("CALLE", calle); 
			itemDocPres.set("LOCALIDAD", localidad); 
			itemDocPres.set("PROVINCIA", provincia); 
			itemDocPres.set("LUGAR", lugar);
			itemDocPres.set("CP", cp); 
			itemDocPres.store(cct);
	        
	        //Obtengo el id
	        int id = itemDocPres.getInt("ID");
	        
	        DbCnt cnt = cct.getConnection();
	        
	        for(int i = 0; i < sobre2Docs.size(); i++){
	        	
	        	ContratacionDocPresentarSDAO pcftdao = new ContratacionDocPresentarSDAO(cnt);
	 			pcftdao.createNew(cnt);
	 			pcftdao.set("FIELD", "LIST_DOC");
	 			pcftdao.set("REG_ID", id);
	 			pcftdao.set("VALUE", sobre2Docs.get(i));
	 			pcftdao.store(cnt);

	        }
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la inserción del sobre 2. ",e);
		}
		
	}
	
	private void insertarDatosSobreC(IEntitiesAPI entitiesAPI, ClientContext cct, String numexp) throws ISPACRuleException {
		try {
			IItem itemDocPres = entitiesAPI.createEntity("CONTRATACION_DOC_PRESENTAR","");
			
			//Sobre A
			itemDocPres.set("NUMEXP", numexp);
			itemDocPres.set("NOMBRESOBRE", sobre3); 
			itemDocPres.set("TIPO_DOC", sobre3TipoDoc); 
			itemDocPres.set("CALLE", calle); 
			itemDocPres.set("LOCALIDAD", localidad); 
			itemDocPres.set("PROVINCIA", provincia); 
			itemDocPres.set("LUGAR", lugar);
			itemDocPres.set("CP", cp); 
			itemDocPres.store(cct);
	        
	        //Obtengo el id
	        int id = itemDocPres.getInt("ID");
	        
	        DbCnt cnt = cct.getConnection();
	        
	        for(int i = 0; i < sobre3Docs.size(); i++){
	        	
	        	ContratacionDocPresentarSDAO pcftdao = new ContratacionDocPresentarSDAO(cnt);
	 			pcftdao.createNew(cnt);
	 			pcftdao.set("FIELD", "LIST_DOC");
	 			pcftdao.set("REG_ID", id);
	 			pcftdao.set("VALUE", sobre3Docs.get(i));
	 			pcftdao.store(cnt);
	        }
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error en la inserción del sobre 3. ",e);
		}
		
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
