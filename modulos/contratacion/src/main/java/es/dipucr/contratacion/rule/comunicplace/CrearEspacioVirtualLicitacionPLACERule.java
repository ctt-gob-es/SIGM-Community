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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.Lotes;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.contratacion.services.PlataformaContratacionDatatypeConfigurationExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionJAXBExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionMalformedURLExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionRemoteExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionStub;
import es.dipucr.contratacion.services.PlataformaContratacionStub.ComprobacionParamentrosEVL;
import es.dipucr.contratacion.services.PlataformaContratacionStub.PeticionesAlsigm;
import es.dipucr.contratacion.services.PlataformaContratacionUnsupportedEncodingExceptionException;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class CrearEspacioVirtualLicitacionPLACERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CrearEspacioVirtualLicitacionPLACERule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Boolean execute(IRuleContext rulectx) throws ISPACRuleException {
		try{

			creacionEVLPLACE(rulectx);

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
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// --------------------------------------------------------------------
			
			//Se comprueba la si tiene Lotes que el sumatorio sea el mismo que 'Datos del Contrato' -> Precio Estimado del Contrato
			Iterator<?> itGenerico = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_DATOS_CONTRATO", "NUMEXP='"+rulectx.getNumExp()+"'");
			while(itGenerico.hasNext()){
				IItem datosContrato = (IItem) itGenerico.next();
				double presupuestosinIVATotal = 0.0;
				if(StringUtils.isNotEmpty(datosContrato.getString("PRECIO_ESTIMADO_CONTRATO"))) presupuestosinIVATotal = Double.parseDouble(datosContrato.getString("PRECIO_ESTIMADO_CONTRATO"));
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
						rulectx.setInfoMessage("El precio estimado del contrato."+presupuestosinIVATotal+" es distinto al sumatorio del Importe (sin impuestos) de cada Lote."+presTotalSinIVALotes);
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
						String strQuery = "WHERE (NOMBRE = 'Pliego de Clausulas Econ�mico - Administrativas' OR NOMBRE ='Pliego de Prescripciones T�cnicas') " +
								"AND NUMEXP='"+rulectx.getNumExp()+"' AND FAPROBACION IS NOT NULL";
		
				        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, strQuery);
		
				        if (!(collection.toList().size() >= 2))
				        {
				        	tienePliego = false;
				        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contrataci�n porque no existen " +
				        			"los documentos Pliego de Clausulas Econ�mico - Administrativas y Pliego de Prescripciones T�cnicas");
				        }
					}
					else{
						String strQuery = "WHERE NOMBRE = 'Pliego de Clausulas Econ�mico - Administrativas' " +
								"AND NUMEXP='"+rulectx.getNumExp()+"' AND FAPROBACION IS NOT NULL";
		
				        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, strQuery);
		
				        if (!(collection.toList().size() == 1))
				        {
				        	tienePliego = false;
				        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contrataci�n porque no existen " +
				        			"el documento Pliego de Clausulas Econ�mico - Administrativas");
				        }
					}
				}
				else{
					tienePliego = false;
		        	rulectx.setInfoMessage("No se ha podido mandar el anuncio a la Plataforma de Contrataci�n porque en la entidad 'Resultado de la Licitaci�n'"
		        			+ " en el campo Env�o anuncio es igual a SI");
				}

			}
			
			
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		} 
		return tienePliego;
	}
	
	private void creacionEVLPLACE(IRuleContext rulectx) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
			IClientContext cct = rulectx.getClientContext();
			// --------------------------------------------------------------------
			//String publishedByUser = UsuariosUtil.getDni(cct);
			//String publishedByUser = "99001215S";
			String dniFuncionario = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
			if(dniFuncionario==null || dniFuncionario.equals("")){
				dniFuncionario = UsuariosUtil.getDni(cct);
			}
			
			TramitesUtil.cargarObservacionesTramite((ClientContext) cct, true,rulectx.getNumExp(), rulectx.getTaskId(), "Mandado al servidor de Contrataci�n");
			
			PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
			//int timeout = 5 * 60 * 1000; // 5 minutos
			//platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.SO_TIMEOUT, new Integer(timeout));
			//platContratacion._getServiceClient().getOptions().setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(timeout));
						
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			ComprobacionParamentrosEVL comprobacionParamentrosEVL1 = new ComprobacionParamentrosEVL();
			PeticionesAlsigm peticionALSIGM = new PeticionesAlsigm();
			peticionALSIGM.setDniFuncionPeticion(dniFuncionario);			
			InetAddress	address = InetAddress.getLocalHost();
			String dir = address.getHostAddress();
			peticionALSIGM.setDnsEntidad(dir);
			peticionALSIGM.setExpediente(rulectx.getNumExp());
			peticionALSIGM.setIdEntidad(entidad);
			peticionALSIGM.setIdFase(rulectx.getStageId());
			peticionALSIGM.setIdTramite(rulectx.getTaskId());
			peticionALSIGM.setLeido("0");
			peticionALSIGM.setOperacionPlace("EVL");
			peticionALSIGM.setEmailError("0");
			comprobacionParamentrosEVL1.setPeticPlace(peticionALSIGM);
			
			platContratacion.comprobacionParamentrosEVL(comprobacionParamentrosEVL1);

		}
		catch(ISPACRuleException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (RemoteException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionRemoteExceptionException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionUnsupportedEncodingExceptionException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionMalformedURLExceptionException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionJAXBExceptionException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
	}

}
