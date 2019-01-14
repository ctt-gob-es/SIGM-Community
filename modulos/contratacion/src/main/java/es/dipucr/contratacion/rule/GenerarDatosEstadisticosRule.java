package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbResultSet;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;

public class GenerarDatosEstadisticosRule extends DipucrAutoGeneraDocIniTramiteRule {
	
private static final Logger logger = Logger.getLogger(GenerarDatosEstadisticosRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GenerarDatosEstadisticosRule.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;

		logger.warn("FIN - " + GenerarDatosEstadisticosRule.class);
		return true;
	}
	
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("RESULTADO", estadisticaProyCont(rulectx));
		} catch (ISPACException e) {
			logger.error("Error al calcular las estadísticas en el numexp. "+e.getMessage(), e);
		}
	}
	
	private String estadisticaProyCont(IRuleContext rulectx) {
		
		StringBuffer stResultado = new StringBuffer();
		
		IClientContext cct = rulectx.getClientContext();
		DbCnt cnt = null;
		DbResultSet rs = null;
		try {
			
			cnt = cct.getConnection();
			rs = cnt.executeQuery(consultaFecha(rulectx));
			ResultSet estadisticas = rs.getResultSet();
			
			Vector<String[]> total = new Vector<String []>();
		   	String [] valorIndiv = new String [2];
		   	if(estadisticas.next()){
	   			valorIndiv[0]= estadisticas.getString("PROC_ADJ");
	   			valorIndiv[1]= estadisticas.getString("IMP_ADJ_SINIVA");
		   	}
		   	
		   	while (estadisticas.next()){
		   		String procedimiento = estadisticas.getString("PROC_ADJ");
		   		String importe = estadisticas.getString("IMP_ADJ_SINIVA");
		   		logger.warn("procedimiento "+procedimiento);
		   		logger.warn("importe "+importe);
		   		if(!valorIndiv[0].equals(procedimiento)){
		   			total.add(valorIndiv);
		   			valorIndiv = new String [2];
		   			valorIndiv[0]= procedimiento;
		   			valorIndiv[1]= importe;
		   		}
		   		else{
		   			valorIndiv[1] = Float.parseFloat(valorIndiv[1]) + Float.parseFloat(importe)+"";
		   		}
		 	}
		   	total.add(valorIndiv);
		   	for (int i = 0; i < total.size(); i++) {
		   		String [] res = total.get(i);
		   		stResultado.append("-"+res[0]+": "+res[1]+"Euros\n");			
			}
			
		} catch (ISPACException e) {
			logger.error("Error al calcular las estadísticas en el numexp. "+e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("Error al calcular las estadísticas en el numexp. "+e.getMessage(), e);
		} finally {
			if (cct != rs ){
				cct.releaseConnection(cnt);
			}
		}
		return stResultado.toString();
	}
	
	public String consultaFecha(IRuleContext rulectx){
		String strQueryNombre = "";
		String fechaInicio = "";
		String fechaFin = "";
		try {
			Iterator<IItem> itcol = ConsultasGenericasUtil.queryEntities(rulectx, "DPCR_ESTADISTICAS_REGISTRO", "NUMEXP='"+rulectx.getNumExp()+"'");
			if(itcol.hasNext()){
				IItem fechas = itcol.next();
				if(fechas.getDate("FECHA_INI")!=null){
					fechaInicio = fechas.getDate("FECHA_INI").toString();
					logger.warn("fechaInicio "+fechaInicio);
				}
				if(fechas.getDate("FECHA_FIN")!=null){
					fechaFin = fechas.getDate("FECHA_FIN").toString();
					logger.warn("fechaFin "+fechaFin);
				}
			}
			
			strQueryNombre = "select * from ( select numexp, proc_adj from contratacion_datos_contrato where numexp in "
					+ "(select numexp from spac_expedientes where codprocedimiento in (select cod_pcd from spac_ct_procedimientos where id_padre in "
					+ "(select id from spac_ct_procedimientos where nombre = 'Procedimiento de Contratación')))) AS tabla1 "
					+ "join (select numexp, imp_adj_siniva, imp_adj_coniva, f_contrato  from contratacion_datos_tramit where numexp in "
					+ "(select numexp from spac_expedientes where codprocedimiento in (select cod_pcd from spac_ct_procedimientos where id_padre in "
					+ "(select id from spac_ct_procedimientos where nombre = 'Procedimiento de Contratación'))) and imp_adj_siniva is not null) "
					+ "AS tabla2 on tabla1.numexp=tabla2.numexp and (tabla2.f_contrato >= '"+fechaInicio+"' and tabla2.f_contrato <= '"+fechaFin+"') "
					+ "order by tabla1.proc_adj, tabla2.f_contrato";
			logger.warn("strQueryNombre "+strQueryNombre);
		} catch (ISPACException e) {
			logger.error("Error al calcular las estadísticas en el numexp. "+e.getMessage(), e);
		}
		
		return strQueryNombre;
	}

	public void InsertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp){
              
        try{
        	if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){ 
        		       		
        		ResultSet estadisticas = rulectx.getClientContext().getConnection().executeQuery(consultaFecha(rulectx)).getResultSet();
        		int numFilas = numeroFilas(rulectx, consultaFecha(rulectx));
        		
        		XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas+1, 4);
        		
        		if(null != tabla){
        			double[] distribucionColumnas = {20, 40, 20, 20};
        			LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
        		
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "Num. expediente");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "Procedimiento");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Importe");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "F. Contrato");	
					
				   	int i = 0;
				   	while (estadisticas.next()){
				   		i++;
				   		LibreOfficeUtil.setTextoCelda(tabla, 1, (i+1), estadisticas.getString("NUMEXP"));			    	
				   		LibreOfficeUtil.setTextoCelda(tabla, 2, (i+1), estadisticas.getString("PROC_ADJ"));
				   		LibreOfficeUtil.setTextoCelda(tabla, 3, (i+1), estadisticas.getString("IMP_ADJ_SINIVA"));
				   		LibreOfficeUtil.setTextoCelda(tabla, 4, (i+1), estadisticas.getDate("F_CONTRATO")+"");
				 	}
		        }
			}
		}
        catch (ISPACException e) {
        	logger.error("Error al calcular las estadísticas en el numexp: " + numexp + " . " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al calcular las estadísticas en el numexp: " + numexp + " . " + e.getMessage(), e);
		}
	}
	
	private int numeroFilas(IRuleContext rulectx, String strQueryNombre) throws ISPACException, SQLException {
		int numFilas = 0;
		try {
			ResultSet rsEsta = rulectx.getClientContext().getConnection().executeQuery(strQueryNombre).getResultSet();
			while(rsEsta.next()){
				numFilas+=1;
			}
		} catch (ISPACException e) {
			logger.error("Error al ejecutar la consulta: \n" + strQueryNombre + " \n para calcular las estadísticas. " + e.getMessage(), e);
			throw new ISPACException("Error al ejecutar la consulta: \n" + strQueryNombre + " \n para calcular las estadísticas. " + e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("Error al ejecutar la consulta: \n" + strQueryNombre + " \n para calcular las estadísticas. " + e.getMessage(), e);
			throw new SQLException("Error al ejecutar la consulta: \n" + strQueryNombre + " \n para calcular las estadísticas. " + e.getMessage(), e);
		}
		
		return numFilas;
	}
}