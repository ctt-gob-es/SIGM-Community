package aww.sigem.expropiaciones.rule.planificador;

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

/**
 * Calcula los días totales necesarios para el levantamiento de actas
 */
public class CalcularDiasTotalesOcupacionRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(CalcularDiasTotalesOcupacionRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {		
		try {
			//logger.warn("Ejecutando regla CalcularDiasTotalesOcupacionRule");
		
			// Obtener las fincas
			//Obtener las fincas relacionadas con la Expropiacion
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			//Obtiene los números de expediente de las fincas
			String strQuery = "WHERE NUMEXP_PADRE = '" + rulectx.getNumExp() + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
			Iterator it = collection.iterator();
			IItem item = null;
			List expFincas = new ArrayList();
			
			//logger.warn("Expediente de Expropiacion: " + rulectx.getNumExp());

			String expQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
			IItemCollection collExp = entitiesAPI.queryEntities("EXPR_EEF", expQuery);
			Iterator itExp = collExp.iterator();
			int intervalo = 0;
			int margenEntreFinca = 1;
			
			IItem itemExp = null;
			if (itExp.hasNext()) {
				itemExp = (IItem) itExp.next();			
				intervalo = itemExp.getInt("INTERVALO_MUNICIPIOS");	
				
				// Si el intervalo es menor que 0 -> No se ha inicializado el campo por lo que se asignan 30 minutos.
				if (intervalo < 0){
					intervalo = 30;
					itemExp.set("INTERVALO_MUNICIPIOS", 30);
					itemExp.store(cct);
				}				
				
			} else throw new ISPACRuleException("Se ha producido un error. EXPEDIENTE EXPROPIACION FORZOSA No encontrado");
						
			if(!it.hasNext()){
				logger.error("No se encontraron Fincas");
				return null;
			}
			
			while (it.hasNext()) {
			   item = (IItem)it.next();
			   expFincas.add(item.getString("NUMEXP_HIJO"));
			   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
			}
			
			/*
			 * Una vez que se obtienen los expedientes de las fincas, hay que ordenarlos siguiendo los criterios municipio.
			 * En este caso no se va a tener en cuenta nada más porque estamos calculando los días necesarios.
			 */
						
			Iterator itExpFincas = expFincas.iterator();
				
			String whereOrdenFinca="WHERE NUMEXP = '" + itExpFincas.next() + "'";
			while (itExpFincas.hasNext()) {
				whereOrdenFinca+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
			}
			
			String clausulas = "ORDER BY MUNICIPIO";
	
			IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", whereOrdenFinca + clausulas);
			IItem itemFinca = null;
			String municipioParajeFinca = "";
			String municipioFinca = "";
			String municipioFincaAnterior = "";
			int indiceSeparador = 0;
			
			int minutosTotal = 0;
			// Obtener numPropietarios de cada finca
			Iterator itFincas = collectionFincas.iterator();
			while(itFincas.hasNext()) {
				
				itemFinca = (IItem)itFincas.next();
				//Obtener numPropietarios
				int numPropietarios = 0;
				String expFinca = itemFinca.getString("NUMEXP");
				//logger.warn("Finca: " + expFinca);
				
				municipioParajeFinca=itemFinca.getString("MUNICIPIO");
				
				if (municipioParajeFinca == null)
					municipioParajeFinca = "";
					
				indiceSeparador = municipioParajeFinca.indexOf("-");
				
				// Si encuentra el separador se debe quedar solo con el municipio.
				if (indiceSeparador!=-1)
					municipioFinca = municipioParajeFinca.substring(0, indiceSeparador);
				else
					municipioFinca = municipioParajeFinca.substring(0, municipioParajeFinca.length());
				
				// Primera iteracion
				if (municipioFincaAnterior.equals("")){
					municipioFincaAnterior = municipioFinca;
					//logger.warn("Inicializar municipioFincaAnterior a " + municipioFinca);
				}
				
				// Si el municipio de la finca anterior no es igual al de la actual -> sumar 30 min y actualizar el municipio.
				if (!municipioFincaAnterior.trim().equals(municipioFinca.trim()) && !municipioFinca.trim().equals("Desconocido".trim())){
					minutosTotal +=intervalo;
					municipioFincaAnterior = municipioFinca;
				}
				
				//logger.warn("Finca: " + expFinca + ", municipio " + municipioFinca);				
				
				String fincaQuery = "WHERE NUMEXP_PADRE = '" + expFinca + "'";
				IItemCollection collPropietarios = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", fincaQuery);
				Iterator itPropietarios = collPropietarios.iterator();

				while(itPropietarios.hasNext()) {
					numPropietarios++;
					itPropietarios.next();
				}
				
				if(numPropietarios <1) {
					logger.error("No se encontraron propietarios");
					minutosTotal += margenEntreFinca;
				} else {
					//logger.warn("Esta finca tiene " + numPropietarios + " propietarios.");
				}
				
				// Si nº numPropietarios >= 1 y <= 3 -> minutosTotal += 5
				if(numPropietarios >= 1 && numPropietarios <=3) {
					minutosTotal += margenEntreFinca;
					//logger.warn("Le asignamos 5 minutos.");
				}
				// Si nº numPropietarios > 3 y <= 10 -> minutosTotal += 10
				if(numPropietarios > 3 && numPropietarios <= 10) {
					minutosTotal += margenEntreFinca * 2;
					//logger.warn("Le asignamos 10 minutos.");
				}
				// Si nº numPropietarios > 10 -> minutosTotal += 15
				if(numPropietarios > 10) {
					minutosTotal += margenEntreFinca * 3;
					//logger.warn("Le asignamos 15 minutos.");
				}
			
			}	
			//logger.warn("En total necesitamos " + minutosTotal + " minutos.");
			
			// Hora inicio: 9:30; Hora fin: 14:30; Total 5 horas, 300 min
			int minutosDia = 300;
			int margenDia = 30;
			//logger.warn("Tenemos " + minutosDia + " minutos al día con margen de " + margenDia + " minutos cada uno.");
			// numero entero de días numeroDias = minutosTotal / 300
			int numeroDias = minutosTotal / minutosDia;
			int resto = minutosTotal % minutosDia;
			if(numeroDias==0) {
				numeroDias++;
				//logger.warn("Sólo se necesita un día");
			} else {
				//logger.warn("El número mínimo de días es " + numeroDias + " nos sobran " + resto + " minutos");
				// Calcular Ajuste
				// ajuste: hasta 30 minutos más por dia
				int ajuste = numeroDias * margenDia;
				//logger.warn("Con " + numeroDias + " podríamos tener hasta " + ajuste + " minutos más de margen");
				if(resto > ajuste) {
					//logger.warn("Como nos sobran más minutos de los posibles debemos sumar un día más");
					numeroDias++;
				}
			}
			//logger.warn("El número final de días es " + numeroDias);
			
			//Obtiene los números de expediente del eef
			
			if (itemExp != null) {
				itemExp.set("OCUPACION_DIAS_NECESARIOS", numeroDias);				
				itemExp.store(rulectx.getClientContext());
			} else throw new ISPACRuleException("Se ha producido un error. EXPEDIENTE EXPROPIACION FORZOSA No encontrado");

			//logger.warn("Fin de ejecución regla CalcularDiasTotalesOcupacionRule");
			return null;
		
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
