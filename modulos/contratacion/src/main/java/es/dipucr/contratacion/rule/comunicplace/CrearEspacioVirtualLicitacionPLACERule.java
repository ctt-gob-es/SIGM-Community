package es.dipucr.contratacion.rule.comunicplace;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.MandarPublicacionPLACSPRule;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.Lotes;
import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;

public class CrearEspacioVirtualLicitacionPLACERule extends MandarPublicacionPLACSPRule{
	
	public static final Logger logger = Logger.getLogger(CrearEspacioVirtualLicitacionPLACERule.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		OPERACIONPLACE = "EVL";
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean tienePliego = true;
		try{
			// --------------------------------------------------------------------
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			
			//Se comprueba la si tiene Lotes que el sumatorio sea el mismo que 'Datos del Contrato' -> Precio Estimado del Contrato
			Iterator<?> itGenerico = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DATOS_CONTRATO", "NUMEXP='"+rulectx.getNumExp()+"'");
			while(itGenerico.hasNext()){
				IItem datosContrato = (IItem) itGenerico.next();
				double presupuestosinIVATotal = 0.0;
				Peticion peticionDep = DipucrFuncionesComunesSW.getPeticion(cct, rulectx.getNumExp());
				String presSinIVATotal = peticionDep.getPresupuestoSinIva();
				//if(StringUtils.isNotEmpty(datosContrato.getString("PRECIO_ESTIMADO_CONTRATO"))) presupuestosinIVATotal = Double.parseDouble(datosContrato.getString("PRECIO_ESTIMADO_CONTRATO"));
				if(StringUtils.isNotEmpty(datosContrato.getString("PRECIO_ESTIMADO_CONTRATO"))) presupuestosinIVATotal = Double.parseDouble(presSinIVATotal);
				if(StringUtils.isNotEmpty(datosContrato.getString("LOTES")) && datosContrato.getString("LOTES").equals("SI")){
					Lotes lotes = DipucrFuncionesComunesSW.getLotes(cct, rulectx.getNumExp());
					double presTotalSinIVALotes = 0;
					for(int i = 0; i < lotes.getLotes().length; i++){
						if(lotes.getLotes()[i]!=null && lotes.getLotes()[i].getPresupuestoSinIva()!=null){
							presTotalSinIVALotes = presTotalSinIVALotes + lotes.getLotes()[i].getPresupuestoSinIva();
						}						
					}
					if(presTotalSinIVALotes != presupuestosinIVATotal){
						tienePliego = false;
						rulectx.setInfoMessage("El precio estimado del contrato (Pestaña Necesidad y Objeto del Contrato)."+presupuestosinIVATotal+" es distinto al sumatorio del Importe (sin impuestos) de cada Lote."+presTotalSinIVALotes);
					}
				}
			}
			//Compruebo que no se haya mandado antes el anuncio.
			IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();				
				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){
					DatosContrato datContrato = DipucrFuncionesComunesSW.getDatosContrato(cct, rulectx.getNumExp());
					
					if(!datContrato.getTipoContrato().getId().equals("50")){
						String strQuery = "WHERE (NOMBRE = 'Pliego de Clausulas Económico - Administrativas' OR NOMBRE ='Pliego de Prescripciones Técnicas') " +
								"AND NUMEXP='"+rulectx.getNumExp()+"' AND FAPROBACION IS NOT NULL";
		
				        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, strQuery);
		
				        if (!(collection.toList().size() >= 2))
				        {
				        	tienePliego = false;
				        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contratación porque no existen " +
				        			"los documentos Pliego de Clausulas Económico - Administrativas y Pliego de Prescripciones Técnicas");
				        }
					}
					else{
						String strQuery = "WHERE NOMBRE = 'Pliego de Clausulas Económico - Administrativas' " +
								"AND NUMEXP='"+rulectx.getNumExp()+"' AND FAPROBACION IS NOT NULL";
		
				        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, strQuery);
		
				        if (!(collection.toList().size() == 1))
				        {
				        	tienePliego = false;
				        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contratación porque no existen " +
				        			"el documento Pliego de Clausulas Económico - Administrativas");
				        }
					}
				}
				else{
					tienePliego = false;
		        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contratación porque en la entidad 'Resultado de la Licitación'"
		        			+ " en el campo Envío anuncio es igual a SI");
				}

			}
			
			
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} 
		return tienePliego;
	}
}
