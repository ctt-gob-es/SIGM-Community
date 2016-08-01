package es.dipucr.contratacion.rule.comunicplace;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.client.beans.AnuncioLicitacionBean;
import es.dipucr.contratacion.client.beans.Garantia;
import es.dipucr.contratacion.client.beans.PersonalContacto;
import es.dipucr.contratacion.client.beans.PublicacionesOficialesBean;
import es.dipucr.contratacion.client.beans.SobreElectronico;
import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.objeto.BOP;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosEmpresa;
import es.dipucr.contratacion.objeto.DatosLicitacion;
import es.dipucr.contratacion.objeto.DatosTramitacion;
import es.dipucr.contratacion.objeto.DiariosOficiales;
import es.dipucr.contratacion.objeto.Peticion;
import es.dipucr.contratacion.objeto.Solvencia;
import es.dipucr.contratacion.resultadoBeans.Resultado;
import es.dipucr.contratacion.services.PlataformaContratacionProxy;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class EnviarAnuncLicitPLACERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(EnviarAnuncLicitPLACERule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Boolean execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
			//ClientContext cct = (ClientContext) rulectx.getClientContext();
			//IInvesflowAPI invesFlowAPI = cct.getAPI();
			//IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			
	
			
			//Compruebo que no se haya mandado antes el anuncio.
			/*IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();				
				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){*/

					Resultado resultadoAnuncioLicitacion = envioAnuncioLicitacion(rulectx);
					
					
					DipucrFuncionesComunes.envioEstadoExpediente(rulectx, resultadoAnuncioLicitacion, "Licitacion");
				/*}
			}*/
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} 
		
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean tienePliego = true;
		try{
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			
			//Compruebo que no se haya mandado antes el anuncio.
			IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();				
				if(itemAdjudicacion.getString("CONTRATACION_ADJUDICACION")!=null && itemAdjudicacion.getString("CONTRATACION_ADJUDICACION").equals("NO")){
					DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
					
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
	
	private Resultado envioAnuncioLicitacion(IRuleContext rulectx) throws ISPACRuleException {
		Resultado resultadoAnalisisPrevio = null;
		try{
			
			
		
			// --------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// --------------------------------------------------------------------
			//String publishedByUser = UsuariosUtil.getDni(cct);
			//String publishedByUser = "99001215S";
			String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
			if(publishedByUser==null || publishedByUser.equals("")){
				publishedByUser = UsuariosUtil.getDni(cct);
			}
			
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			AnuncioLicitacionBean anuncioLicitacion = new AnuncioLicitacionBean();
			
			//Num Expediente
			anuncioLicitacion.setNumexp(rulectx.getNumExp());
			
			DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
			if(datContrato!=null){
				anuncioLicitacion.setObjetoContrato(datContrato.getObjetoContrato());
				anuncioLicitacion.setProcContratacion(datContrato.getProcedimientoContratacion());
				anuncioLicitacion.setTipoContrato(datContrato.getTipoContrato());
				anuncioLicitacion.setSubTipoContrato(datContrato.getSubTipoContrato());
				anuncioLicitacion.setTipoTramitacion(datContrato.getTipoTramitacion());
				anuncioLicitacion.setTramitacionGasto(datContrato.getTramitacionGasto());
				anuncioLicitacion.setCpv(datContrato.getCpv());
				anuncioLicitacion.setValorEstimadoContrato(datContrato.getValorEstimadoContrato());
			}
			
			DatosTramitacion datosTramitacion = DipucrFuncionesComunes.getDatosTramitacion(rulectx, rulectx.getNumExp());
			if(datosTramitacion!=null){
				if(datosTramitacion.getPresentacionOfertas()!=null){
					anuncioLicitacion.setPresentacionOfertas(datosTramitacion.getPresentacionOfertas());
					anuncioLicitacion.setFechaPresentacionSolcitudesParticipacion(datosTramitacion.getPresentacionOfertas().getEndDate());
					anuncioLicitacion.setDuracionContrato(datosTramitacion.getDuracionContrato());
				}
			}
			
			//Compruebo que el tipo de contrato no sea Patrimonial
			//Porque si no coge los datos del presupuesto de la entidad datos del contrato
			if(!datContrato.getTipoContrato().getId().equals("50")){
				Peticion peticion = DipucrFuncionesComunes.getPeticion(rulectx);
				anuncioLicitacion.setPresupuestoConIva(peticion.getPresupuestoConIva());
				anuncioLicitacion.setPresupuestoSinIva(peticion.getPresupuestoSinIva());
			}
			else{
				String [] presupuesto =  DipucrFuncionesComunes.getDatosContratoPresupuesto(rulectx);
				if(presupuesto!=null && presupuesto.length==2){
					anuncioLicitacion.setPresupuestoConIva(presupuesto[0]);
					anuncioLicitacion.setPresupuestoSinIva(presupuesto[1]);
				}
			}
			
			
			DatosEmpresa datEmpresa = DipucrFuncionesComunes.getDatosEmpresa(rulectx, rulectx.getNumExp());
			anuncioLicitacion.setClasificacion(datEmpresa.getClasificacion());
			anuncioLicitacion.setCondLicit(datEmpresa.getCondLicit());
			anuncioLicitacion.setReqDecl(datEmpresa.getTipoDeclaracion());
			
			DatosLicitacion datosLicitacion= DipucrFuncionesComunes.getDatosLicitacion(rulectx);
			anuncioLicitacion.setTipoPresentacionOferta(datosLicitacion.getTipoPresentacionOferta());	
			anuncioLicitacion.setApliPesu(datosLicitacion.getAplicacionPres());
			//Falta por introducir la entidad 'Criterios de adjudicación'
			anuncioLicitacion.setCriterios(datosLicitacion.getCritAdj());
			anuncioLicitacion.setVarOfert(datosLicitacion.getVariantes());
			anuncioLicitacion.setFundacionPrograma(datosLicitacion.getFundacionPrograma());
			anuncioLicitacion.setFormulaRevisionPrecios(datosLicitacion.getRevisionPrecios());
	
			PublicacionesOficialesBean publicacionesOficiales = null;			
			DiariosOficiales diariosOficiales =  DipucrFuncionesComunes.getDiariosOficiales(rulectx, "AnuncioLicitacionRule");
			BOP bop = DipucrFuncionesComunes.getBOP(rulectx);			
			if(diariosOficiales !=null || bop!=null){
				publicacionesOficiales = new PublicacionesOficialesBean();
				if(diariosOficiales.getDoue() !=null){
					publicacionesOficiales.setEnviarDOUE(diariosOficiales.getDoue().isPublicarDOUE());
				}
				if(diariosOficiales.getBoe() !=null){
					publicacionesOficiales.setEnviarBOE(diariosOficiales.getBoe().isPublicarBOE());
				}
				if(bop!=null){
					publicacionesOficiales.setNombreOtrosDiarios(bop.getNombreBOP());
					publicacionesOficiales.setFechaPubOtrosDiarios(bop.getFechaPublicacion());
					publicacionesOficiales.setPublishURLOtrosDiarios(bop.getUrlPublicacion());
				}
				
				anuncioLicitacion.setDiarios(publicacionesOficiales);
			}
			
			SobreElectronico [] sobreElect = DipucrFuncionesComunes.getSobreElec(rulectx);
			anuncioLicitacion.setSobreElect(sobreElect);
			
			Solvencia solvencia = DipucrFuncionesComunes.getSolvencia(rulectx);
			anuncioLicitacion.setSolvenciaEconomica(solvencia.getSolvenciaEconomica());
			anuncioLicitacion.setSolvenciaTecn(solvencia.getSolvenciaTecn());	
			
			//garantias
			Garantia[] garantia = DipucrFuncionesComunes.getGarantias(rulectx);
			anuncioLicitacion.setGarantia(garantia);
			
			PersonalContacto[] persCon = DipucrFuncionesComunes.getPersonalContacto(rulectx);
			anuncioLicitacion.setPersonalContactoContratacion(persCon[0]);
			anuncioLicitacion.setPersonalContactoSecretaria(persCon[1]);

			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			//Petición

			resultadoAnalisisPrevio = platContratacion.envioPublicacionAnuncioLicitacion(entidad, anuncioLicitacion, publishedByUser);

		}
		catch(ISPACRuleException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (RemoteException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return resultadoAnalisisPrevio;
	}

}
