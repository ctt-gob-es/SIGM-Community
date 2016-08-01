package es.dipucr.sigem.api.rule.procedures.expropiaciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class DipucrAcuseReciboExpropiadoRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(DipucrAcuseReciboExpropiadoRule.class);
	
	protected String STR_acuseReciboEXP = "Acuse de recibo Expropiado";
	

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
            
			//Obtiene los números de expediente de las fincas
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator<?> it = collection.iterator();
			IItem item = null;
			List<String> expFincas = new ArrayList<String>();
			
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			}
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(expFincas.isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			//Obtiene los datos de las fincas
			Iterator<String> itExpFincas = expFincas.iterator();
			strQuery="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
			
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQuery + "ORDER BY MUNICIPIO, NUM_POLIGONO ASC, NUM_PARCELA ASC");
			
			//Si la lista de fincas está vacía no dibujar la tabla
			if(collectionFincas.toList().isEmpty()){
				return "La lista de fincas a expropiar está vacía\n";
			}
			
			Iterator<?> itFincas = collectionFincas.iterator();	
			while (itFincas.hasNext()) {			
				item = (IItem)itFincas.next();
				//Código que extrae una lista de propietarios
				propietariosFinca(entitiesAPI,item.getString("NUMEXP"), rulectx, cct);
			}		

			return new Boolean(true);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e.getMessage(), e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	
	private void propietariosFinca(IEntitiesAPI entitiesAPI, String numExpFinca, IRuleContext rulectx, ClientContext cct) throws Exception{
	
		//Buscar los expedientes de los Expropiados
		String strQuery = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
		IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
		Iterator<?> it = collection.iterator();
		IItem item = null;	
		List<String> expExpropiados = new ArrayList<String>();		
		
		while (it.hasNext()) {
		   item = (IItem)it.next();
		   expExpropiados.add(item.getString("NUMEXP_HIJO"));
		}
		
		if(expExpropiados.size()>0){		
			//Obtiene los datos de los expropiados
			Iterator<String> itExpExpropiados = expExpropiados.iterator();
			strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
			while (itExpExpropiados.hasNext()) {
				strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
			}			
			
			IItemCollection collectionExpropiados = ParticipantesUtil.queryParticipantes(cct, strQuery);
	
			Iterator<?> itExpropiados = collectionExpropiados.iterator();
	
			
			String nombre = "";
			String dirnot = "";
			String localidad = "";
			String cpostal = "";
			String caut = "";
			
			
			while (itExpropiados.hasNext()) {	
				item = (IItem)itExpropiados.next();
				if(item != null){
					if (item.getString("NOMBRE")!=null) nombre = (String)item.getString("NOMBRE"); else nombre = "";
					cct.setSsVariable("NOMBRE", nombre);
					
					if (item.getString("DIRNOT")!=null) dirnot = (String)item.getString("DIRNOT"); else dirnot = "";
					cct.setSsVariable("DIRNOT", dirnot);
					
					if (item.getString("LOCALIDAD")!=null) localidad = (String)item.getString("LOCALIDAD"); else localidad = "";
					cct.setSsVariable("LOCALIDAD", localidad);
	
					if (item.getString("C_POSTAL")!=null) cpostal = (String)item.getString("C_POSTAL"); else cpostal = "";
					cct.setSsVariable("C_POSTAL", cpostal);
	
					if (item.getString("CAUT")!=null) caut = (String)item.getString("CAUT"); else caut = "";
					cct.setSsVariable("CAUT", caut);
					
					cct.setSsVariable("FINCA", numExpFinca);
					
					DocumentosUtil.generarDocumento(rulectx, STR_acuseReciboEXP, nombre);
					
					cct.deleteSsVariable("NOMBRE");
					cct.deleteSsVariable("DIRNOT");
					cct.deleteSsVariable("LOCALIDAD");
					cct.deleteSsVariable("C_POSTAL");
					cct.deleteSsVariable("CAUT");
					cct.deleteSsVariable("FINCA");
				}
	
			}
		}
		
	}
	
	
}

