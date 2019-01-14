package es.dipucr.sigem.api.rule.procedures.personal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrIniciaExpPersonal  implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrIniciaExpPersonal.class);

	public static String nombre_tabla = "PERSONAL_DPCR_PERS_CONV_PERS";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - DipucrIniciaExpConvocatoria");
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        ITXTransaction tx = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
	        IProcess process = null;
	        
			String numexpSolGenerica = rulectx.getNumExp();
			String numexpConvocatoria = "";
			String numExpSolicitudEspecifica = "";
			
			//Recuperamos el numexp de la convocatoria
			IItemCollection solicitudCollection = entitiesAPI.getEntities("DPCR_INICIA_EXP_GENERICO", numexpSolGenerica);
			Iterator<IItem> solicitudIterator = solicitudCollection.iterator();
			if(solicitudIterator.hasNext()){
				IItem solicitudGenerica = (IItem)solicitudIterator.next();
				numexpConvocatoria = solicitudGenerica.getString("NUMEXP_PADRE");
				
				//Recuperamos el id del procedimiento de la convocatoria que se ha solicitado
				IItemCollection expedienteConvocatoriaCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, numexpConvocatoria);
				Iterator<IItem> expedienteConvocatoriaIterator = expedienteConvocatoriaCollection.iterator();
				
				if(expedienteConvocatoriaIterator.hasNext()){
					String codProcedimientoConvocatoria = ((IItem) expedienteConvocatoriaIterator.next()).getString("CODPROCEDIMIENTO");

					//Iniciamos el expediente con el código del expediente de la solicitud que le corresponde
					IItemCollection expedienteSolicitudCollection = entitiesAPI.queryEntities(nombre_tabla, "WHERE VALOR = '"+codProcedimientoConvocatoria+"'" );
					Iterator<IItem> expedienteSolicitudIterator = expedienteSolicitudCollection.iterator();
					if(expedienteSolicitudIterator.hasNext()){
						String codProcedimientoSolicitud = ((IItem)expedienteSolicitudIterator.next()).getString("SUSTITUTO");
						
						//IItemCollection expedienteSolicitudCTProcCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = '"+codProcedimientoSolicitud+"'");
						//2 ->vigente
						//3 -> historico
						String sql = "SELECT ID FROM SPAC_P_PROCEDIMIENTOS WHERE estado = 2 AND CAST(id_pcd_bpm AS integer) IN ( SELECT id FROM spac_ct_procedimientos WHERE cod_pcd = '"+codProcedimientoSolicitud+"')";
//						IItemCollection expedienteSolicitudCTProcCollection = entitiesAPI.queryEntities("SPAC_P_PROCEDIMIENTOS", sql);
//						Iterator expedienteSolicitudCTProcIterator =expedienteSolicitudCTProcCollection.iterator();
//						
						 ResultSet expedienteSolicitudCTProcIterator = cct.getConnection().executeQuery(sql).getResultSet();
						if(expedienteSolicitudCTProcIterator.next()){
							
							int idProceso = expedienteSolicitudCTProcIterator.getInt("ID");
														
							// Obtener el código de procedimiento para el número de expediente
							Map<String, String> params = new HashMap<String, String>();
							params.put("COD_PCD", codProcedimientoSolicitud);
	
							// Crear el proceso del expediente
							int nIdProcess2 = tx.createProcess(idProceso, params);
														
							process = invesFlowAPI.getProcess(nIdProcess2);
							
							numExpSolicitudEspecifica = process.getString("NUMEXP");
	
							IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
	
							registro.set("NUMEXP_PADRE", numexpConvocatoria);
							registro.set("NUMEXP_HIJO", numExpSolicitudEspecifica);
							registro.set("RELACION", "Solicitud Convocatoria");
	
							registro.store(cct);
							
							//Asignamos el mismo asunto, los mimos participantes etc, del expediente genérico							
							
							//Datos del Expediente
							IItemCollection expedienteGenericoColection = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, numexpSolGenerica);
							Iterator<IItem> expedienteGenericoIterator = expedienteGenericoColection.iterator();
							
							if(expedienteGenericoIterator.hasNext()){
								IItem expedienteGenerico = (IItem) expedienteGenericoIterator.next();
								
								IItemCollection expedienteEspecificoColection = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, numExpSolicitudEspecifica);
								Iterator<IItem> expedienteEspecificoIterator = expedienteEspecificoColection.iterator();
								
								if(expedienteEspecificoIterator.hasNext()){
									IItem expedienteEspecifico = (IItem) expedienteEspecificoIterator.next();
									
									expedienteEspecifico.set("ASUNTO", expedienteGenerico.getString("ASUNTO"));
									expedienteEspecifico.set("NREG", expedienteGenerico.getString("NREG"));
									expedienteEspecifico.set("FREG", expedienteGenerico.getDate("FREG"));
									expedienteEspecifico.set("NIFCIFTITULAR", expedienteGenerico.getString("NIFCIFTITULAR"));
									expedienteEspecifico.set("DOMICILIO", expedienteGenerico.getString("DOMICILIO"));
									expedienteEspecifico.set("CIUDAD", expedienteGenerico.getString("CIUDAD"));
									expedienteEspecifico.set("REGIONPAIS", expedienteGenerico.getString("REGIONPAIS"));
									expedienteEspecifico.set("CPOSTAL", expedienteGenerico.getString("CPOSTAL"));
									expedienteEspecifico.set("IDENTIDADTITULAR", expedienteGenerico.getString("IDENTIDADTITULAR"));
									expedienteEspecifico.set("ROLTITULAR", expedienteGenerico.getString("ROLTITULAR"));
									expedienteEspecifico.set("TIPODIRECCIONINTERESADO", expedienteGenerico.getString("TIPODIRECCIONINTERESADO"));
									
									expedienteEspecifico.store(cct);
								}
								
								//Participantes 
								ParticipantesUtil.importarParticipantes(cct, entitiesAPI, numexpSolGenerica, numExpSolicitudEspecifica);
								
								//Documentos
								IItemCollection documentosSolGenericoCollection = entitiesAPI.getEntities(Constants.TABLASBBDD.SPAC_DT_DOCUMENTOS, numexpSolGenerica);
								Iterator<IItem> documentosSolGenericaIterator = documentosSolGenericoCollection.iterator();
								while (documentosSolGenericaIterator.hasNext()){
									IItem documento = (IItem)documentosSolGenericaIterator.next();
									documento.set("NUMEXP", numExpSolicitudEspecifica);
									documento.store(cct);
								}
							}
						}
					}					
				}
				if(numExpSolicitudEspecifica != null && !numExpSolicitudEspecifica.equals("")){
					solicitudGenerica.set("NUMEXP", numExpSolicitudEspecifica);
					solicitudGenerica.store(cct);
					Iterator<IItem> itPuesto = ConsultasGenericasUtil.queryEntities(rulectx, "PERSONAL_PUESTO_TRABAJO", "NUMEXP='"+numexpSolGenerica+"'");
					while(itPuesto.hasNext()){
						IItem puesto = itPuesto.next();
						puesto.set("NUMEXP", numExpSolicitudEspecifica);
						puesto.store(cct);
					}
				}
				tx.closeProcess(rulectx.getProcessId());				
			}

		}
		catch(ISPACRuleException e){
			logger.error("Se produjo una excepciÃ³n", e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error("Se produjo una excepciÃ³n", e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (SQLException e) {
			logger.error("Se produjo una excepciÃ³n", e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		logger.warn("FIN - DipucrIniciaExpConvocatoria");
		
		
		
		return true;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
