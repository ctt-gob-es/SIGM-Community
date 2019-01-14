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
import es.dipucr.webempleado.model.mapping.Convocatoria;
import es.dipucr.webempleado.services.ayudasSociales.AyudasSocialesWSProxy;


/**
 * [dipucr-Felipe #546]
 * Recupera los datos de la convocatoria de estudios a partir de el año seleccionado
 */
public class CalcularConvocatoriaEstudiosRule implements IRule 
{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CalcularConvocatoriaEstudiosRule.class);
	
	protected IItem itemAyudaEstudios;
	
	
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
			IItemCollection colAyudasss = entitiesAPI.getEntities("AYUDASSS_ESTUDIOS", numexp);
			
			if(colAyudasss.toList().size() == 0){
				rulectx.setInfoMessage("Debe rellenar el campo 'Año' en la pestaña 'Ayudas Sociales - Estudios'");
				return false;
			}
			else{
				itemAyudaEstudios = (IItem) colAyudasss.iterator().next();
			
				String anio = itemAyudaEstudios.getString("ANIO");
				if (StringUtils.isEmpty(anio)){
					rulectx.setInfoMessage("Debe rellenar el campo 'Año' en la pestaña 'Ayudas Sociales - Estudios'");
					return false;
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error al validar los datos de la pestaña "
					+ "'Ayudas Sociales - Estudios' " + e.getMessage(), e);
		}
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			
			String numexp = rulectx.getNumExp();

			if (null != itemAyudaEstudios){
			
				AyudasSocialesWSProxy ws = new AyudasSocialesWSProxy();
				String anio = itemAyudaEstudios.getString("ANIO");
				Convocatoria convocatoria = ws.getConvocatoriaByAnio(anio);
				int idConvocatoria = convocatoria.getIdConvocatoria();
				
				itemAyudaEstudios.set("ID_CONVOCATORIA", idConvocatoria);
				itemAyudaEstudios.set("FECHA_INI_CONV", convocatoria.getFIniConvocatoria().getTime());
				itemAyudaEstudios.set("FECHA_FIN_CONV", convocatoria.getFFinConvocatoria().getTime());
				itemAyudaEstudios.set("NOMBRE_CONV", convocatoria.getDConvocatoria());
				itemAyudaEstudios.set("ESTADO", convocatoria.getEstado());
				itemAyudaEstudios.set("DECRETO_COMISION", convocatoria.getRegComision());
				itemAyudaEstudios.set("FECHA_DEC_COMISION", convocatoria.getFComision().getTime());
				itemAyudaEstudios.set("DECRETO_RESOLUCION", convocatoria.getRegResolucion());
				itemAyudaEstudios.set("FECHA_DEC_RESOLUCION", convocatoria.getFResolucion().getTime());
				itemAyudaEstudios.set("DOTACION", convocatoria.getDotacionEconomica().doubleValue());
				String curso = anio + "/" + (Integer.valueOf(anio) + 1);
				itemAyudaEstudios.set("CURSO", curso);
				
				convocatoria = ws.calcularPuntosTotales(idConvocatoria);
				
				itemAyudaEstudios.set("TOTAL_PUNTOS", convocatoria.getTotalPuntos());
				itemAyudaEstudios.set("IMPORTE_PUNTO", convocatoria.getImportePunto());
				itemAyudaEstudios.set("IMPORTE_DISTRIBUIDO", convocatoria.getImporteDistribuido());
				
				itemAyudaEstudios.store(cct);
				
				AyudaSocial[] arrAyudas = ws.getAyudasEstudiosConvocatoria(idConvocatoria);
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
					itemAyuda.set("IMPORTE_CONCEDIDO", ayuda.getImporteConcedido());
					itemAyuda.set("PUNTOS_ESTUDIOS", ayuda.getPuntos());
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
			throw new ISPACRuleException("Error al recuperar los beneficiarios de la ayuda de estudios " + e.getMessage(), e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
