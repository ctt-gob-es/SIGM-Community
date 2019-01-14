package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.expedients.InterestedPerson;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * [dipucr-Felipe #546 / #573]
 * Carga los participantes de la convocatoria o propuesta de ayudas
 */
public class CargarParticipantesAyudasssRule extends DipucrAutoGeneraDocIniTramiteRule {

	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(CargarParticipantesAyudasssRule.class);
	
	private ArrayList<InterestedPerson> listInteresadosNoIncluidos;
		
	public static final double[] DISTRIBUCION_2_COLUMNAS_RS = {25, 75};
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		tipoDocumento = "Listado participantes que faltan en sigem";
		plantilla = "Listado participantes no incluidos Ayudas";
		refTablas = "%TABLA1%";
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			//*********************************************
			
			logger.info("INICIO - " + this.getClass().getName());
			String numexp = rulectx.getNumExp();
			
			listInteresadosNoIncluidos = new ArrayList<InterestedPerson>();
			
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("WHERE NUMEXP='");
			sbQuery.append(numexp);
			sbQuery.append("' ORDER BY NOMBRE_EMPLEADO, BENEFICIARIO");
			
			IItemCollection colAyudasss = entitiesAPI.queryEntities("AYUDASSS", sbQuery.toString());
			@SuppressWarnings("unchecked")
			List<IItem> listAyudasss  = colAyudasss.toList();
			
			String nifEmpleadoAnterior = null;
			
			entitiesAPI.deleteEntities("SPAC_DT_INTERVINIENTES", "WHERE NUMEXP = '" + numexp + "'");
			for (IItem itemAyuda : listAyudasss){
				
				String nif = itemAyuda.getString("NIF_EMPLEADO");
				if (!nif.equals(nifEmpleadoAnterior)){
				
					String tipoRegimen = itemAyuda.getString("TIPO_CONTRATO");
					String recurso = AyudasssUtil.getRecurso(tipoRegimen);
					
					boolean result = ParticipantesUtil.insertarParticipanteByNIF
					(
							rulectx,
							numexp,
							nif,
							ParticipantesUtil._TIPO_INTERESADO, 
							ParticipantesUtil._TIPO_PERSONA_FISICA,
							itemAyuda.getString("EMAIL"),
							recurso
					);
					if (!result){
						InterestedPerson persona = new InterestedPerson();
						persona.setNifcif(nif);
						persona.setName(itemAyuda.getString("NOMBRE_EMPLEADO"));
						listInteresadosNoIncluidos.add(persona);
					}
				}
				nifEmpleadoAnterior = nif;
			}
	
			logger.info("FIN - " + this.getClass().getName());
			
		}
		catch(Exception ex){
			String error = "Error al cargar los partipantes de las ayudas de estudios " + ex.getMessage();
			logger.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
		
		return true;
	}
	

	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {

		int numFilas = 0;
        
        try{
            numFilas = listInteresadosNoIncluidos.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 
            		numFilas + 1, DISTRIBUCION_2_COLUMNAS_RS.length);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, DISTRIBUCION_2_COLUMNAS_RS);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "NIF");
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "NOMBRE");
                
                int i = 1;
                for (InterestedPerson persona : listInteresadosNoIncluidos){
                    
                	i++;
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, persona.getNifcif());
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, persona.getName());
                }
            }
        } catch (Exception e) {
            logger.error("Error al generar la tabla de participantes no incluidos "
            		+ "de ayudas: " + e.getMessage(), e);
        }
    }
	
}
