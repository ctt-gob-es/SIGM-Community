package es.dipucr.sigem.api.rule.common.convocatorias;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrIniciaExpConvocatoria implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrIniciaExpConvocatoria.class);

	public static String nombre_tabla = "DPCR_CONV_EXP_CONV";
	public static String nombre_tabla2 = "DPCR_CONV_EXP_CONV_ASO";
	public static String nombre_tabla3 = "DPCR_CONV_EXP_CONV_CIU";
	public static String nombre_tabla4 = "DPCR_EXP_ALTA_CONVENIOS_CULT";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());
		String numexpSolGenerica = "";
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			
			numexpSolGenerica = rulectx.getNumExp();
			String numexpConvocatoria = "";
			String numExpSolicitudEspecifica = "";
			
			//Recuperamos el numexp de la convocatoria
			IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", numexpSolGenerica);
			Iterator solicitudIterator = solicitudCollection.iterator();
			if(solicitudIterator.hasNext()){
				IItem solicitudGenerica = (IItem)solicitudIterator.next();
				numexpConvocatoria = solicitudGenerica.getString("NUMEXP_PADRE");
				
				//Recuperamos el id del procedimiento de la convocatoria que se ha solicitado
				IItem expedienteConvocatoria = ExpedientesUtil.getExpediente(cct, numexpConvocatoria);
				
				if(expedienteConvocatoria != null){
					String codProcedimientoConvocatoria = expedienteConvocatoria.getString("CODPROCEDIMIENTO");

					//Iniciamos el expediente con el código del expediente de la solicitud que le corresponde
					IItemCollection expedienteSolicitudCollection = entitiesAPI.queryEntities(nombre_tabla, "WHERE VALOR = '"+codProcedimientoConvocatoria+"'" );
					if(expedienteSolicitudCollection.toList().size()==0){
						expedienteSolicitudCollection = entitiesAPI.queryEntities(nombre_tabla2, "WHERE VALOR = '"+codProcedimientoConvocatoria+"'" );
					}
					if(expedienteSolicitudCollection.toList().size()==0){
						expedienteSolicitudCollection = entitiesAPI.queryEntities(nombre_tabla3, "WHERE VALOR = '"+codProcedimientoConvocatoria+"'" );
					}
					if(expedienteSolicitudCollection.toList().size()==0){
						expedienteSolicitudCollection = entitiesAPI.queryEntities(nombre_tabla4, "WHERE VALOR = '"+codProcedimientoConvocatoria+"'" );
					}
					Iterator expedienteSolicitudIterator = expedienteSolicitudCollection.iterator();
					if(expedienteSolicitudIterator.hasNext()){
						String codProcedimientoSolicitud = ((IItem)expedienteSolicitudIterator.next()).getString("SUSTITUTO");
						
						IItemCollection expedienteSolicitudCTProcCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = '"+codProcedimientoSolicitud+"'");
						
						Iterator expedienteSolicitudCTProcIterator =expedienteSolicitudCTProcCollection.iterator();
						
						if(expedienteSolicitudCTProcIterator.hasNext()){
							
							int idProceso = ((IItem)expedienteSolicitudCTProcIterator.next()).getInt("ID");
							
							IItem expedienteGenerico = ExpedientesUtil.getExpediente(cct, numexpSolGenerica);
							IItem expedienteEspecifico = ExpedientesRelacionadosUtil.iniciaExpediente(cct, idProceso, expedienteGenerico);
															
							numExpSolicitudEspecifica = expedienteEspecifico.getString("NUMEXP");
							
							expedienteEspecifico.set("ASUNTO", expedienteEspecifico.getString("NOMBREPROCEDIMIENTO") + ": " + expedienteEspecifico.getString("IDENTIDADTITULAR"));
							expedienteEspecifico.store(cct);
							
							IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
	
							registro.set("NUMEXP_PADRE", numexpConvocatoria);
							registro.set("NUMEXP_HIJO", numExpSolicitudEspecifica);
							registro.set("RELACION", "Convocatoria de Subvenciones");
	
							registro.store(cct);
							
							//Asignamos el mismo asunto, los mimos participantes etc, del expediente genérico							
							
							if(expedienteGenerico != null){
								//Participantes 
								ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexpSolGenerica, numExpSolicitudEspecifica);
								
								//Documentos
								IItemCollection documentosSolGenericoCollection = entitiesAPI.getDocuments(numexpSolGenerica, "", "");
								Iterator documentosSolGenericaIterator = documentosSolGenericoCollection.iterator();
								while (documentosSolGenericaIterator.hasNext()){
									IItem documento = (IItem)documentosSolGenericaIterator.next();
									documento.set("NUMEXP", numExpSolicitudEspecifica);
									documento.store(cct);
								}

								//Datos del responsalbe
								IItemCollection respGenericoCollection = entitiesAPI.getEntities("REC_OBLIGADO", numexpSolGenerica);
								Iterator respGenericaIterator = respGenericoCollection.iterator();
								while (respGenericaIterator.hasNext()){
									IItem resp = (IItem)respGenericaIterator.next();
									resp.set("NUMEXP", numExpSolicitudEspecifica);
									resp.store(cct);
								}
							}
						}
					}					
				}
				if(numExpSolicitudEspecifica != null && !numExpSolicitudEspecifica.equals("")){
					solicitudGenerica.set("NUMEXP", numExpSolicitudEspecifica);
					solicitudGenerica.store(cct);
				}
				tx.closeProcess(rulectx.getProcessId());				
			}
		}
		catch(ISPACRuleException e){
			logger.error("Error en el expediente: " + numexpSolGenerica + ". "+ e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error en el expediente: " + numexpSolGenerica + ". "+ e.getMessage(), e);
		}
		
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
