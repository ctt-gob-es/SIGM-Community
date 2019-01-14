package es.dipucr.contratacion.rule;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.FasesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class TerminaTramiteInformeTecnicoAgregaExpePadre implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(TerminaTramiteInformeTecnicoAgregaExpePadre.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {	
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try
    	{		
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        //----------------------------------------------------------------------------------------------
	        SpacExpRelacionadosInfoAPI expRelacionadosInfos = new SpacExpRelacionadosInfoAPI(cct);
	        IItem tramInforTecni = expRelacionadosInfos.getInfoSpacExpRelacionaHijoIdTramiteHijo(rulectx.getNumExp(), rulectx.getTaskId());
			if(null != tramInforTecni && tramInforTecni.getInt("ID_TRAMITE_PADRE")>=0){
				int id_tramite_padre = tramInforTecni.getInt("ID_TRAMITE_PADRE");
				IItem tramSolInforTecn = expRelacionadosInfos.getInfoSpacExpRelacionaHijoIdTramiteHijo(rulectx.getNumExp(), id_tramite_padre);
				if(null != tramSolInforTecn && StringUtils.isNotEmpty(tramSolInforTecn.getString("NUMEXP_PADRE"))){
					String expPadreSolicitudInformeTecnic = tramSolInforTecn.getString("NUMEXP_PADRE");
					
					int tramitePadre = TramitesUtil.crearTramite(cct, "inf-tecn-contr", expPadreSolicitudInformeTecnic);					
					if(tramitePadre>0){
						
						IItemCollection itemColDoc = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), rulectx.getTaskId());
						@SuppressWarnings("unchecked")
						Iterator<IItem> itDoc = itemColDoc.iterator();
						while(itDoc.hasNext()){
							
							IItem docAntiguo = itDoc.next();
							int tpdoc = docAntiguo.getInt("ID_TPDOC");
							File fileCopiarSinFirma = null;
							if(StringUtils.isNotEmpty(docAntiguo.getString("INFOPAG_RDE"))){
								fileCopiarSinFirma = DocumentosUtil.getFile(rulectx.getClientContext(), docAntiguo.getString("INFOPAG_RDE"), docAntiguo.getString("NOMBRE"), docAntiguo.getString("EXTENSION_RDE"));					
								DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), tramitePadre, tpdoc, docAntiguo.getString("DESCRIPCION"),fileCopiarSinFirma, docAntiguo.getString("EXTENSION_RDE"));
							}
							if(fileCopiarSinFirma.exists()){
								fileCopiarSinFirma.delete();
							}
						}
					}
					else{
						LOGGER.error("Error al crear el trámite de Informe Técnico del Servicio en el expediente. Numexp. "+expPadreSolicitudInformeTecnic);
			        	throw new ISPACRuleException("Error al crear el trámite de Informe Técnico del Servicio en el expediente. Numexp. "+expPadreSolicitudInformeTecnic);
					}
				}
			}
			
		 			
    	 }
    	catch(ISPACRuleException e) 
        {
    		LOGGER.error("Error al insertar el Informe Técnico en el exp. padre. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("Error al insertar el Informe Técnico en el exp. padre. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        } catch (ISPACException e) 
		{
        	LOGGER.error("Error al insertar el Informe Técnico en el exp. padre. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
        	throw new ISPACRuleException("Error al insertar el Informe Técnico en el exp. padre. Numexp. "+rulectx.getNumExp()+" Error. "+e.getMessage(),e);
		}
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminar = true;
		
		
		/**
		 * Compruebo si en que fase esrán ambos expedientes antes de crear el trámite.
		 * **/
		/*try{
			//Obtenemos las fase actual del procedimiento de contratacion
			//SPAC_FASES
			IStage stage = rulectx.getClientContext().getAPI().getStage(rulectx.getStageId());
			int id_fase_spac_p_fase = stage.getInt("ID_FASE");
			//SPAC_P_FASES
			IItem itSpacPFases = FasesUtil.getSpacPFasesById(rulectx, id_fase_spac_p_fase);
			int idCtFase = itSpacPFases.getInt("ID_CTFASE");
			//SPAC_CT_FASES
			String codFaseActualProcContr = FasesUtil.getCodFaseSpacCTFasesById(rulectx.getClientContext(), idCtFase);
			
			//Obtenemos la fase que requiere la fase actual del proc. contratacion
			Iterator<IItem> itFases = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_RELACION_FASES", "VALOR='"+codFaseActualProcContr+"'");
			String codFaseRequerieExpedProcContratacion = "";
			while(itFases.hasNext()){
				IItem faseActual = itFases.next();
				if(null!=faseActual){
					
					if(StringUtils.isNotEmpty(faseActual.getString("SUSTITUTO"))) codFaseRequerieExpedProcContratacion = faseActual.getString("SUSTITUTO");
				}
			}
			 if(StringUtils.isNotEmpty(codFaseRequerieExpedProcContratacion)){
				 
				SpacExpRelacionadosInfoAPI expRelacionadosInfos = new SpacExpRelacionadosInfoAPI((ClientContext) rulectx.getClientContext());
			    IItem tramInforTecni = expRelacionadosInfos.getInfoSpacExpRelacionaHijoIdTramiteHijo(rulectx.getNumExp(), rulectx.getTaskId());
				if(null != tramInforTecni && tramInforTecni.getInt("ID_TRAMITE_PADRE")>=0){
					int id_tramite_padre = tramInforTecni.getInt("ID_TRAMITE_PADRE");
					IItem tramSolInforTecn = expRelacionadosInfos.getInfoSpacExpRelacionaHijoIdTramiteHijo(rulectx.getNumExp(), id_tramite_padre);
					if(null != tramSolInforTecn && StringUtils.isNotEmpty(tramSolInforTecn.getString("NUMEXP_PADRE"))){
						String expPadreSolicitudInformeTecnic = tramSolInforTecn.getString("NUMEXP_PADRE");
						
						IItem spacFasePadre = FasesUtil.getFase(rulectx.getClientContext(), expPadreSolicitudInformeTecnic);
						if(null!=spacFasePadre){
							IItem itSpacPFasesPadre = FasesUtil.getSpacPFasesById(rulectx, spacFasePadre.getInt("ID_FASE"));
							if(null!=itSpacPFasesPadre){
								String codFaseActualContrPadre = FasesUtil.getCodFaseSpacCTFasesById(rulectx.getClientContext(), itSpacPFasesPadre.getInt("ID_CTFASE"));
								if(StringUtils.isNotEmpty(codFaseActualContrPadre)){
									if(!codFaseActualContrPadre.equals(codFaseRequerieExpedProcContratacion)){
										terminar = false;
										String nombreFaseActualPadre = FasesUtil.getNombreSpacCTFasesByCodFase(rulectx.getClientContext(), codFaseActualContrPadre);
										String nombreFaseRequierePadre = FasesUtil.getNombreSpacCTFasesByCodFase(rulectx.getClientContext(), codFaseRequerieExpedProcContratacion);
										rulectx.setInfoMessage("No se puede terminar trámite porque "
												+ "la fase que actualmente: '"+nombreFaseActualPadre+"' que tiene el expediente: "+expPadreSolicitudInformeTecnic+" debería estar en la fase '"+nombreFaseRequierePadre+"'.  "
												+ "Contacte con el responsable del expediente para que lo posicione en fase correcta.");
									}
								}
								else{
									terminar = false;
									rulectx.setInfoMessage("Es necesario insertar el código de fase en la fase: "+itSpacPFasesPadre.getInt("ID_CTFASE")+". Ponga una incidencia con el error obtenido.");
								}
							}
						}
						else{
							terminar = false;
							rulectx.setInfoMessage("No se puede obtener la fase actual del expediente. "+expPadreSolicitudInformeTecnic+". Ponga una incidencia con el error obtenido.");
						}
					}
				}				
			 }
			
			
		}catch(ISPACException e){
			LOGGER.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	
		}*/
		return terminar;
	}

}
