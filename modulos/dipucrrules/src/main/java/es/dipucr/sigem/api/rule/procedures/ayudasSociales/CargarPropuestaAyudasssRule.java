package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.webempleado.domain.AyudaSocial;
import es.dipucr.webempleado.model.mapping.Propuesta;
import es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSProxy;


/**
 * [dipucr-Felipe #576]
 * Recupera los datos de la propuesta y sus ayudas relacionadas
 */
public class CargarPropuestaAyudasssRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CargarPropuestaAyudasssRule.class);
	
	protected IItem itemPropuesta;
	protected Propuesta propuesta;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			
			String numexp = rulectx.getNumExp();
			
			//Petición al webservice
			IItemCollection colAyudasss = entitiesAPI.getEntities("AYUDASSS_PROPUESTA", numexp);
			
			if(colAyudasss.toList().size() == 0){
				rulectx.setInfoMessage("Debe rellenar el campo 'Id Propuesta' en la pestaña 'Ayudas Sociales - Propuesta'");
				return false;
			}
			else{
				itemPropuesta = (IItem) colAyudasss.iterator().next();
			
				String idPropuesta = itemPropuesta.getString("ID_PROPUESTA");
				if (StringUtils.isEmpty(idPropuesta)){
					rulectx.setInfoMessage("Debe rellenar el campo 'Id Propuesta' en la pestaña 'Ayudas Sociales - Propuesta'");
					return false;
				}
				
				AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
				propuesta = ws.getPropuesta(Integer.valueOf(idPropuesta));
				
				if (null == propuesta){
					rulectx.setInfoMessage("La propuesta con id " + idPropuesta + " no existe en la base de datos");
					return false;
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al validar los datos de la pestaña "
					+ "'Ayudas Sociales - Propuesta' " + e.getMessage(), e);
		}
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			
			String numexp = rulectx.getNumExp();

			if (null != propuesta){
			
				itemPropuesta.set("FECHA", propuesta.getFPropuesta().getTime());
				itemPropuesta.set("TIPO_SOLICITUD", propuesta.getTipoSolicitud());
				itemPropuesta.set("TIPO_REGIMEN", propuesta.getTipoRegimen());
				
				itemPropuesta.store(cct);

				AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
				int idPropuesta = itemPropuesta.getInt("ID_PROPUESTA");
				
				AyudaSocial[] arrAyudas = ws.getAyudasPropuesta(idPropuesta);
				entitiesAPI.deleteEntities("AYUDASSS", "WHERE NUMEXP = '" + numexp + "'");//borrado inicial

				for (AyudaSocial ayuda : arrAyudas){
					
					IItem itemAyuda = entitiesAPI.createEntity("AYUDASSS", numexp);
					itemAyuda.set("ID_AYUDA", ayuda.getIdAyuda());
					itemAyuda.set("NIF_EMPLEADO", ayuda.getNif());
					itemAyuda.set("NOMBRE_EMPLEADO", ayuda.getNombre());
					itemAyuda.set("ANO_AYUDA", ayuda.getAnoAyuda());
					itemAyuda.set("ID_GRUPO", ayuda.getIdGrupo());
					itemAyuda.set("GRUPO", ayuda.getDescGrupo());
					itemAyuda.set("ID_CONCEPTO", ayuda.getIdConcepto());
					itemAyuda.set("CONCEPTO", ayuda.getDescConcepto());
					itemAyuda.set("PARENTESCO", ayuda.getParentesco());
					itemAyuda.set("BENEFICIARIO", ayuda.getBeneficiario());
					if (null != ayuda.getFechaBeneficiario()){
						itemAyuda.set("FECHA_NAC_BENEFICIARIO", FechasUtil.getFormattedDate(ayuda.getFechaBeneficiario().getTime()));
					}
					itemAyuda.set("IMPORTE", ayuda.getImporteSolicitado());
					itemAyuda.set("IMPORTE_CONCEDIDO", ayuda.getImporteConcedido());
					itemAyuda.set("OBSERVACIONES", ayuda.getObservaciones());
					itemAyuda.set("TIPO_CONTRATO", ayuda.getTipoEmpleado());
					itemAyuda.set("EMAIL", ayuda.getEmail());
					itemAyuda.set("TELEFONO", ayuda.getTelefono());
					
					itemAyuda.store(cct);
				}
			}
		
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al cargar la propuesta y sus ayudas relacionadas " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
