package es.dipucr.sigem.api.rule.procedures.ayudasSociales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;

/**
 * [dipucr-Felipe #546]
 * Genera el borrador de decreto para las ayudas de estudios
 */
public class CrearBorradorAyudasPropuestaRule extends DipucrAutoGeneraDocIniTramiteRule {

	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(CrearBorradorAyudasPropuestaRule.class);
	
	private ArrayList<InfoAyudaEstudios> listAyudas;
	private IItem itemPropuesta = null;
	private String tipoRegimen = null;
	
	public static final double[] DISTRIBUCION_2_COLUMNAS_RS = {75, 25};
	
	private final String DATE_PATTERN = FechasUtil.DOC_DATE_PATTERN;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		tipoDocumento = "Borrador Decreto Ayudas";
		plantilla = "Borrador Decreto Ayudas Propuesta";
		refTablas = "%TABLA1%";
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			//*********************************************
			
			LOGGER.info("INICIO - " + this.getClass().getName());
			String numexp = rulectx.getNumExp();
			
			itemPropuesta = entitiesAPI.getEntities("AYUDASSS_PROPUESTA", numexp).value();
			tipoRegimen = itemPropuesta.getString("TIPO_REGIMEN");
			
			listAyudas = new ArrayList<InfoAyudaEstudios>();
			
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("SELECT NIF_EMPLEADO AS NIF, NOMBRE_EMPLEADO AS NOMBRE, TIPO_CONTRATO AS TIPO, ");
			sbQuery.append("SUM(CAST(IMPORTE_CONCEDIDO AS FLOAT)) AS TOTAL");
			sbQuery.append(" FROM AYUDASSS WHERE NUMEXP='");
			sbQuery.append(numexp);
			sbQuery.append("' GROUP BY NIF_EMPLEADO, NOMBRE_EMPLEADO, TIPO_CONTRATO ORDER BY NOMBRE_EMPLEADO");
			
			DbResultSet dbresult = cct.getConnection().executeQuery(sbQuery.toString());
			ResultSet rs = dbresult.getResultSet(); 
			
			if (null != rs){
				while(rs.next()){
					
					InfoAyudaEstudios info = new InfoAyudaEstudios();
					info.setNif(rs.getString("NIF"));
					info.setNombre(rs.getString("NOMBRE"));
					info.setTipoEmpleado(rs.getString("TIPO"));
					info.setTotal(rs.getDouble("TOTAL"));
					
					listAyudas.add(info);
				}
			}
	
			LOGGER.info("FIN - " + this.getClass().getName());
			
		}
		catch(Exception ex){
			String error = "Error al generar el documento de borrador de las ayudas de propuesta " + ex.getMessage();
			LOGGER.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
		
		return true;
	}
	

	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {

		int numFilas = 0;
        
        try{
        	numFilas = listAyudas.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 
            		numFilas + 1, DISTRIBUCION_2_COLUMNAS_RS.length);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, DISTRIBUCION_2_COLUMNAS_RS);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "BENEFICIARIO");
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "TOTAL CONCEDIDO");
                
                int i = 1;
                for (InfoAyudaEstudios info : listAyudas){
                    
                	i++;
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, info.getNombre());
                    BigDecimal bdTotal = new BigDecimal(info.getTotal()).setScale(2, RoundingMode.HALF_DOWN);
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, bdTotal + " €");
                    
                }
            }
        } catch (Exception e) {
        	LOGGER.error("Error al generar el documento de borrador de ayudas de estudios: "
            		 + e.getMessage(), e);
        }
    }
	
	@Override
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
        try {
            setFechaSsVariable(cct, "FECHA");
            cct.setSsVariable("TIPO_REGIMEN", AyudasssUtil.getDescRegimen(tipoRegimen));
            cct.setSsVariable("CONVENIO", AyudasssUtil.getDescConvenio(tipoRegimen));
        }
        catch(ISPACException e){
            LOGGER.error(e.getMessage(), e);
        }
    }

	private void setFechaSsVariable(IClientContext cct, String nombre) throws ISPACException {
		if (null != itemPropuesta.getDate(nombre)){
			cct.setSsVariable(nombre, FechasUtil.getFormattedDate(itemPropuesta.getDate(nombre), DATE_PATTERN));
		}
		else{
			cct.setSsVariable(nombre, "[ERROR " + nombre + "]");
		}
	}

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("FECHA");
            cct.deleteSsVariable("TIPO_REGIMEN");
            cct.deleteSsVariable("CONVENIO");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
	
}
