package es.dipucr.contratacion.rule;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class AnadirDocExpedienteTramite implements IRule{
	private static final Logger logger = Logger.getLogger(AnadirDocExpedienteTramite.class);
	
	String numexpPeticionContratacion = "";
	String codigoTramite = "";
	String nombreDescripcionDoc = "";
	
	int faseActual = 0;

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*****************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/*****************************************************************/
			
			if(!numexpPeticionContratacion.equals("") && !codigoTramite.equals("")){
				
				/**Creaciï¿½n del trï¿½mite*/
				int idTramiteCodTramite = TramitesUtil.crearTramite(cct, codigoTramite, numexpPeticionContratacion);
				/**Fin Creaciï¿½n trï¿½mite**/
				
				SpacExpRelacionadosInfoAPI expRelacionadosInfos = new SpacExpRelacionadosInfoAPI(cct);
				expRelacionadosInfos.addSpacExpRelacionadosInfo(rulectx.getNumExp(), numexpPeticionContratacion, rulectx.getTaskId(), idTramiteCodTramite, null);
				
				TramitesUtil.cargarObservacionesTramite(cct, true,rulectx.getNumExp(), rulectx.getTaskId(), " Exp.Relacionado: "+numexpPeticionContratacion);
				
				/**Creaciï¿½n del documento en el trï¿½mite**/
				IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId()+"", "FDOC DESC");
				IItem contenido = null;
				Iterator <IItem> itDoc = documentsCollection.iterator();
				if(itDoc.hasNext()){
					while (itDoc.hasNext()){
						contenido = (IItem)itDoc.next();
						File documentoPropuesta = null;
						String extension = "";
						if(contenido.getString("INFOPAG_RDE")!=null){
							documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG_RDE"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION_RDE"));
							extension = contenido.getString("EXTENSION_RDE");
						}
						else{
							documentoPropuesta = DocumentosUtil.getFile(cct, contenido.getString("INFOPAG"), contenido.getString("NOMBRE"), contenido.getString("EXTENSION"));
							extension = contenido.getString("EXTENSION");
						}
						
						IItem docAnexado = DocumentosUtil.generaYAnexaDocumento(rulectx, idTramiteCodTramite, contenido.getInt("ID_TPDOC"), nombreDescripcionDoc, documentoPropuesta, extension);
						if(contenido.getDate("FAPROBACION")!=null){
							docAnexado.set("FAPROBACION", contenido.getDate("FAPROBACION"));
							docAnexado.store(cct);
						}
					}
				}else{
					logger.warn("No se ha encontrado ningï¿½n documento en ese trï¿½mite");
					throw new ISPACInfo("No se ha encontrado ningï¿½n documento en ese trï¿½mite");
				}
				/**FIN Creaciï¿½n documento Trï¿½mite**/
				
				/**Por ï¿½ltimo cerrar el trï¿½mite que se ha creado automï¿½ticamente**/
				if(!codigoTramite.equals("Sol-Inf-Tecn-Inc")){
					TramitesUtil.cerrarTramite(idTramiteCodTramite, rulectx);
				}
				
			}

		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return new Boolean (true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminar = true;
		/**
		 * Compruebo si en que fase esrán ambos expedientes antes de crear el trámite.
		 * **/
		try{
			if(faseActual<=0){
				faseActual = rulectx.getStageId();
			}
			//Obtenemos las fase actual del procedimiento de contratacion
			//SPAC_FASES
			IStage stage = rulectx.getClientContext().getAPI().getStage(faseActual);
			int id_fase_spac_p_fase = stage.getInt("ID_FASE");
			//SPAC_P_FASES
			IItem itSpacPFases = FasesUtil.getSpacPFasesById(rulectx, id_fase_spac_p_fase);
			int idCtFase = itSpacPFases.getInt("ID_CTFASE");
			//SPAC_CT_FASES
			String codFaseActualProcContr = FasesUtil.getCodFaseSpacCTFasesById(rulectx.getClientContext(), idCtFase);
			
			//Obtenemos la fase que requiere la fase actual del proc. contratacion
			Iterator<IItem> itFases = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_RELACION_FASES", "SUSTITUTO='"+codFaseActualProcContr+"'");
			String codFaseRequerieExpedDepartamento = "";
			while(itFases.hasNext()){
				IItem faseActual = itFases.next();
				if(null!=faseActual){					
					if(StringUtils.isNotEmpty(faseActual.getString("VALOR"))) codFaseRequerieExpedDepartamento = faseActual.getString("VALOR");					
					if(StringUtils.isNotEmpty(codFaseRequerieExpedDepartamento)){
						if(StringUtils.isEmpty(numexpPeticionContratacion)){
							//Obtenemos las fase actual del procedimiento del departamento
							String consulta = "NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION LIKE '%Petición Contrato%'";
							Iterator<IItem> itSpacExpRelacionados = ConsultasGenericasUtil.queryEntities(rulectx, "SPAC_EXP_RELACIONADOS", consulta);
							while(itSpacExpRelacionados.hasNext()){
								IItem procContraDepart = itSpacExpRelacionados.next();
								if(null!=procContraDepart){
									if(StringUtils.isNotEmpty(procContraDepart.getString("NUMEXP_PADRE"))) numexpPeticionContratacion = procContraDepart.getString("NUMEXP_PADRE");
								}
							}						
						}
						IItem spacFaseDepartamento = FasesUtil.getFase(rulectx.getClientContext(), numexpPeticionContratacion);
						IItem itSpacPFasesDepartamento = FasesUtil.getSpacPFasesById(rulectx, spacFaseDepartamento.getInt("ID_FASE"));
						String codFaseActualContrDepartam = FasesUtil.getCodFaseSpacCTFasesById(rulectx.getClientContext(), itSpacPFasesDepartamento.getInt("ID_CTFASE"));
						if(!codFaseActualContrDepartam.equals(codFaseRequerieExpedDepartamento)){
							terminar = false;
							String nombreFaseActualDepartamento = FasesUtil.getNombreSpacCTFasesByCodFase(rulectx.getClientContext(), codFaseActualContrDepartam);
							String nombreFaseRequiereDepartamento = FasesUtil.getNombreSpacCTFasesByCodFase(rulectx.getClientContext(), codFaseRequerieExpedDepartamento);
							rulectx.setInfoMessage("No se puede terminar trámite porque "
									+ "la fase que actualmente tiene la petición es: "+nombreFaseActualDepartamento+" y debería estar en la fase "+nombreFaseRequiereDepartamento+".  "
									+ "Contacte con el responsable del expediente para que lo posicione en fase correcta.");
						}
					}
				}
			}	
		}catch(ISPACException e){
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		}
		return terminar;
	}

}
