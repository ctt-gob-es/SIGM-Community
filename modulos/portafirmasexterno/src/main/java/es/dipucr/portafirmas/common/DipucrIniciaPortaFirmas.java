package es.dipucr.portafirmas.common;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class DipucrIniciaPortaFirmas implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaPortaFirmas.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {			
			logger.info("INICIO - " + this.getClass().getName());
	        generaPortaFirma(rulectx);			
			logger.info("FIN - " + this.getClass().getName());
		} catch (ISPACException e) { 
			logger.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al generar la comunicación administrativa. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar la comunicación administrativa. " + e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void generaPortaFirma(IRuleContext rulectx) throws Exception {
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		//IInvesflowAPI invesflowAPI = cct.getAPI();
		//ITXTransaction tx = invesflowAPI.getTransactionAPI();
		//IProcedureAPI procedureAPI = invesflowAPI.getProcedureAPI();
		
		String numExpHijo = "";
		try{
			numExpHijo = ExpedientesRelacionadosUtil.iniciaExpedienteHijoFirmaDocExternos(cct, rulectx.getNumExp(), false, true);
			if(StringUtils.isNotEmpty(numExpHijo)){
				IItem expediente = ExpedientesUtil.getExpediente(cct, numExpHijo);
				String nombrePro = "";
				if(expediente!=null && StringUtils.isNotEmpty(expediente.getString("NOMBREPROCEDIMIENTO"))){
					nombrePro = expediente.getString("NOMBREPROCEDIMIENTO");
				}
				SpacExpRelacionadosInfoAPI expRelacionadosInfos = new SpacExpRelacionadosInfoAPI(cct);
				expRelacionadosInfos.addSpacExpRelacionadosInfo(rulectx.getNumExp(), numExpHijo, rulectx.getTaskId(), 0, null);
				
				TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), nombrePro+" - Exp.Relacionado: "+numExpHijo);
			}
			
		}		
		catch(ISPACRuleException e){
			logger.error("Error al iniciar el expediente de Firma de documentos externos. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al iniciar el expediente de Firma de documentos externos. NUMEXP "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		
		if(StringUtils.isNotEmpty(numExpHijo)){
			int idTramite=0;
			
			idTramite = TramitesUtil.crearTramite(cct, "gen-doc-firmar", numExpHijo);

			//No se puede añadir los documentos 
			IItemCollection itemColDoc = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), rulectx.getTaskId());
			Iterator<IItem> itDoc = itemColDoc.iterator();
			if(itDoc.hasNext()){
				IItem docAntiguo = itDoc.next();
				int tpdoc = docAntiguo.getInt("ID_TPDOC");
				String sTpdoc = docAntiguo.getString("NOMBRE"); 
				String tipoDocMostrar = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
				if(StringUtils.isEmpty(tipoDocMostrar)){
					tipoDocMostrar =  "convenio";
				}
				tpdoc = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), tipoDocMostrar);
				IItem nuevoDoc = null;
				if(StringUtils.isNotEmpty(docAntiguo.getString("INFOPAG_RDE"))){
					File fileCopiarSinFirma = DocumentosUtil.getFile(rulectx.getClientContext(), docAntiguo.getString("INFOPAG_RDE"), docAntiguo.getString("NOMBRE"), docAntiguo.getString("EXTENSION_RDE"));					
					nuevoDoc = DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), idTramite, tpdoc, sTpdoc,fileCopiarSinFirma, docAntiguo.getString("EXTENSION_RDE"));
				}
				else{
					nuevoDoc = DocumentosUtil.copiaDocumentoSinFirmaTramite(rulectx, docAntiguo, numExpHijo, idTramite, tpdoc, sTpdoc);
				}
			}
			
			
				
		}
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IItemCollection docTramites = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), rulectx.getTaskId());
			Iterator<IItem> itDocTr = docTramites.iterator();
			if(!itDocTr.hasNext()){
				rulectx.setInfoMessage("Falta anexar el documento a firmar");
				return false;
			}
			else {
				/*IItem doc = itDocTr.next();
				if(doc!=null){
					String infopagRDE = doc.getString("INFOPAG_RDE");
				}*/
				return true;
			}
			
		} catch (ISPACException e) {
			logger.error("Error al obtener el documento de convenio con el NUMEXP. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el documento de convenio con el NUMEXP. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
	}
	
	
}