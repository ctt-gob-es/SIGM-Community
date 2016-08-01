package es.dipucr.sigem.api.rule.common.resolucion;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrPreparaEntidadParaResolucionRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(DipucrPreparaEntidadParaResolucionRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			logger.info("INICIO - DipucrPreparaEntidadParaResolucionRule");
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			
			//Actualiza el campo estado de la entidad
	        //de modo que permita mostrar los enlaces para crear Propuesta/Decreto	        
	        IItemCollection col = entitiesAPI.getEntities(Constants.TABLASBBDD.SUBV_CONVOCATORIA, numexp);
	        Iterator itConv = col.iterator();
	        
	        //Recuperamos los datos de la firma para pasarlos luego a la propuesta
	        String cargo = "";
	        
	        IItemCollection colProp = entitiesAPI.getDocuments(numexp, "UPPER(DESCRIPCION) LIKE '%"+Constants.CONSTANTES.PROPUESTA+"%'", "");
	        Iterator itProp = colProp.iterator();
	        while(itProp.hasNext()){
		        IItem iPropuesta = (IItem)itProp.next();
		        String idDocumento = iPropuesta.getString("ID");
		        
		        String strQuery = "WHERE ID_DOCUMENTO = "+idDocumento;
		        IItemCollection circuitoFirma = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", strQuery);
		        Iterator itCircuitos = circuitoFirma.iterator();
		        
		        if(itCircuitos.hasNext()){
		        	IItem circuito = (IItem) itCircuitos.next();
		        	int idCircuito = circuito.getInt("ID_CIRCUITO");
			        
			        strQuery="SELECT DESCRIPCION FROM SPAC_CTOS_FIRMA_CABECERA WHERE ID_CIRCUITO = "+idCircuito+"";
			        ResultSet circuitoFirmaDetalle = cct.getConnection().executeQuery(strQuery).getResultSet();
			        if(circuitoFirmaDetalle==null)
		          	{
		          		throw new ISPACInfo("No hay ningun procediento asociado");
		          	}
		          	if(circuitoFirmaDetalle.next()) if (circuitoFirmaDetalle.getString("DESCRIPCION")!=null) cargo = circuitoFirmaDetalle.getString("DESCRIPCION"); else cargo="";
		          	if(cargo.length()>20) cargo = cargo.substring(0,19);
		        }
	        }
	        
	         IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
	         String titulo  = "";
	         if(expediente != null){
	        	 titulo = expediente.getString("ASUNTO");
	         }
	        
	        if (itConv.hasNext()){	        	
		        IItem entidad = (IItem)itConv.next();
		        entidad.set("ESTADO", "Inicio");		        
		        entidad.set("NUM_VICEP", cargo);
		        entidad.set("TITULO", titulo);
		        entidad.store(cct);
	        }
	        else{
	        	IItem item = entitiesAPI.createEntity(Constants.TABLASBBDD.SUBV_CONVOCATORIA,numexp);
				item.set("NUMEXP", rulectx.getNumExp()); 
				item.set("ESTADO", "Inicio");
				item.set("NUM_VICEP", cargo);
				item.set("TITULO", titulo);
		        item.store(rulectx.getClientContext());
	        }
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
		} catch (ISPACInfo e) {
			logger.error("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al preparar el expediente para la resolución, " + e.getMessage(), e);
		}
		logger.info("FIN - DipucrPreparaEntidadParaResolucionRule");
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
