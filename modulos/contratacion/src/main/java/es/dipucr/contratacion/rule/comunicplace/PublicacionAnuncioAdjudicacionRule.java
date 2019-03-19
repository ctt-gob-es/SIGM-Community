package es.dipucr.contratacion.rule.comunicplace;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.contratacion.services.PlataformaContratacionDatatypeConfigurationExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionJAXBExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionMalformedURLExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionRemoteExceptionException;
import es.dipucr.contratacion.services.PlataformaContratacionStub;
import es.dipucr.contratacion.services.PlataformaContratacionStub.ComprobacionParamentrosEVL;
import es.dipucr.contratacion.services.PlataformaContratacionStub.PeticionesAlsigm;
import es.dipucr.contratacion.services.PlataformaContratacionUnsupportedEncodingExceptionException;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class PublicacionAnuncioAdjudicacionRule implements IRule{
	
	public static final Logger LOGGER = Logger.getLogger(CrearEspacioVirtualLicitacionPLACERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
			IClientContext cct = rulectx.getClientContext();
			// --------------------------------------------------------------------
			String dniFuncionario = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
			if(dniFuncionario==null || dniFuncionario.equals("")){
				dniFuncionario = UsuariosUtil.getDni(cct);
			}
			
			DipucrFuncionesComunesSW.comprobarDatosEnvioPLACE(rulectx.getClientContext(), rulectx.getNumExp());
			
			TramitesUtil.cargarObservacionesTramite((ClientContext) cct, true,rulectx.getNumExp(), rulectx.getTaskId(), "Mandado al servidor de Contratación");
			
			
			PlataformaContratacionStub platContratacion = new PlataformaContratacionStub(ServiciosWebContratacionFunciones.getDireccionSW());
			
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
			peticionALSIGM.setOperacionPlace("PUB_ANUNC");
			peticionALSIGM.setEmailError("0");
			comprobacionParamentrosEVL1.setPeticPlace(peticionALSIGM);
			
			platContratacion.comprobacionParamentrosEVL(comprobacionParamentrosEVL1);
		}
		catch(ISPACRuleException e){
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (RemoteException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionDatatypeConfigurationExceptionException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionRemoteExceptionException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionUnsupportedEncodingExceptionException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionMalformedURLExceptionException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (PlataformaContratacionJAXBExceptionException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (UnknownHostException e) {
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
