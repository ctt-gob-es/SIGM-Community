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
import java.util.List;

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
public class CrearBorradorAyudaEstudiosRule extends DipucrAutoGeneraDocIniTramiteRule {

	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(CrearBorradorAyudaEstudiosRule.class);
	
	private ArrayList<InfoAyudaEstudios> listFuncionarios;
	private ArrayList<InfoAyudaEstudios> listLaborales;
	private int totalPuntosFuncionarios = 0;
	private int totalPuntosLaborales = 0;
	private IItem itemConvocatoria = null;
	
	public static final double[] DISTRIBUCION_4_COLUMNAS_RS = {55, 10, 15, 20};
	
	private final String DATE_PATTERN = FechasUtil.DOC_DATE_PATTERN;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		
		tipoDocumento = "Borrador Decreto Ayudas";
		plantilla = "Borrador Decreto Ayuda de Estudios";
		refTablas = "%TABLA1%,%TABLA2%";
		
		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			//*********************************************
			
			LOGGER.info("INICIO - " + this.getClass().getName());
			String numexp = rulectx.getNumExp();
			
			itemConvocatoria = entitiesAPI.getEntities("AYUDASSS_ESTUDIOS", numexp).value();
			
			listFuncionarios = new ArrayList<InfoAyudaEstudios>();
			listLaborales = new ArrayList<InfoAyudaEstudios>();
			
			StringBuffer sbQuery = new StringBuffer();
			sbQuery.append("SELECT NIF_EMPLEADO AS NIF, NOMBRE_EMPLEADO AS NOMBRE, TIPO_CONTRATO AS TIPO, ");
			sbQuery.append("SUM(CAST(PUNTOS_ESTUDIOS AS INTEGER)) AS TOTAL_PUNTOS, SUM(CAST(IMPORTE_CONCEDIDO AS FLOAT)) AS TOTAL");
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
					info.setImportePunto(itemConvocatoria.getDouble("IMPORTE_PUNTO"));
					info.setTotalPuntos(rs.getInt("TOTAL_PUNTOS"));
					info.setTotal(rs.getDouble("TOTAL"));
					
					if (AyudasssUtil.esTipoLaboral(info.getTipoEmpleado())){
						listLaborales.add(info);
						totalPuntosLaborales += info.getTotalPuntos();
					}
					else{
						listFuncionarios.add(info);
						totalPuntosFuncionarios += info.getTotalPuntos();
					}
				}
			}
	
			LOGGER.info("FIN - " + this.getClass().getName());
			
		}
		catch(Exception ex){
			String error = "Error al generar el documento de borrador de las ayudas de estudios " + ex.getMessage();
			LOGGER.error(error, ex);
			throw new ISPACRuleException(error, ex);
		}
		
		return true;
	}
	

	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {

		int numFilas = 0;
        List<InfoAyudaEstudios> listAyudas = null;
        
        try{
        	if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){
        		listAyudas = listFuncionarios;
        	}
        	else if (LibreOfficeUtil.ReferenciasTablas.TABLA2.equals(refTabla)){
        		listAyudas = listLaborales;
        	}
        	numFilas = listAyudas.size();
            
            XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, 
            		numFilas + 1, DISTRIBUCION_4_COLUMNAS_RS.length);
            
            if(null != tabla){
                LibreOfficeUtil.colocaColumnas(tabla, DISTRIBUCION_4_COLUMNAS_RS);
                
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "NOMBRE");
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "PUNTOS");
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "IMPORTE");
                LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "TOTAL");
                
                int i = 1;
                for (InfoAyudaEstudios info : listAyudas){
                    
                	i++;
                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, info.getNombre());
                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, info.getTotalPuntos() + "");
                    LibreOfficeUtil.setTextoCelda(tabla, 3, i, info.getImportePunto() + " €");
                    BigDecimal bdTotal = new BigDecimal(info.getTotal()).setScale(2, RoundingMode.HALF_DOWN);
                    LibreOfficeUtil.setTextoCelda(tabla, 4, i, bdTotal + " €");
                    
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
            cct.setSsVariable("TOTAL_FUNCIONARIOS", String.valueOf(listFuncionarios.size()));
            cct.setSsVariable("TOTAL_LABORALES", String.valueOf(listLaborales.size()));
            cct.setSsVariable("TOTAL_PUNTOS_FUNCIONARIOS", String.valueOf(totalPuntosFuncionarios));
            cct.setSsVariable("TOTAL_PUNTOS_LABORALES", String.valueOf(totalPuntosLaborales));
            
            setFechaSsVariable(cct, "FECHA_SESION");
            setFechaSsVariable(cct, "FECHA_DEC_RESOLUCION");
            setFechaSsVariable(cct, "FECHA_FIN_CONV");
            setFechaSsVariable(cct, "FECHA_DEC_COMISION");
        }
        catch(ISPACException e){
            LOGGER.error(e.getMessage(), e);
        }
    }

	private void setFechaSsVariable(IClientContext cct, String nombre) throws ISPACException {
		if (null != itemConvocatoria.getDate(nombre)){
			cct.setSsVariable(nombre, FechasUtil.getFormattedDate(itemConvocatoria.getDate(nombre), DATE_PATTERN));
		}
		else{
			cct.setSsVariable(nombre, "[ERROR " + nombre + "]");
		}
	}

    public void deleteSsVariables(IClientContext cct) {
        try {
            cct.deleteSsVariable("TOTAL_FUNCIONARIOS");
            cct.deleteSsVariable("TOTAL_LABORALES");
            cct.deleteSsVariable("TOTAL_PUNTOS_FUNCIONARIOS");
            cct.deleteSsVariable("TOTAL_PUNTOS_LABORALES");
            cct.deleteSsVariable("FECHA_SESION");
            cct.deleteSsVariable("FECHA_DEC_RESOLUCION");
            cct.deleteSsVariable("FECHA_FIN_CONV");
            cct.deleteSsVariable("FECHA_DEC_COMISION");
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
	
}
