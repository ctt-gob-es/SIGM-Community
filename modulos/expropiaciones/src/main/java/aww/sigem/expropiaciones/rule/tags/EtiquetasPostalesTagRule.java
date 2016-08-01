package aww.sigem.expropiaciones.rule.tags;

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

import aww.sigem.expropiaciones.util.PropietariosUtil;
import aww.sigem.expropiaciones.util.TablaUtilSigem;

public class EtiquetasPostalesTagRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(EtiquetasPostalesTagRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
			////logger.warn("Ejecutando regla EtiquetasPostalesTagRule");
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	
			//Obtener los expropiados relacionados con la expropiación.
			
			////logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());		
			
			List propietarios = PropietariosUtil.propietariosExpropiacion(entitiesAPI, rulectx.getNumExp());
			
			//Genera los encabezados de la tabla
			List titulos = new ArrayList();
			
			//[eCenpri-Manu Ticket #276] + INICIO - ALSIGM3 Etiquetas Expropiaciones
			titulos.add("Col1\n");
			titulos.add("Col2\n");
			titulos.add("Col3\n");
			//[eCenpri-Manu Ticket #276] + FIN - ALSIGM3 Etiquetas Expropiaciones
			
			//return "";
			return TablaUtilSigem.formateaEtiquetas(titulos, propietarios);
			
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	

}
