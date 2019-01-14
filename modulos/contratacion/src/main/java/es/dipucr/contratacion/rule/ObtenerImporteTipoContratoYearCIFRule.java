package es.dipucr.contratacion.rule;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class ObtenerImporteTipoContratoYearCIFRule extends DipucrAutoGeneraDocIniTramiteRule {
	
	private static final Logger logger = Logger.getLogger(ObtenerImporteTipoContratoYearCIFRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + ObtenerImporteTipoContratoYearCIFRule.class);

		IClientContext cct = rulectx.getClientContext();
		
		plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
		
		if(StringUtils.isNotEmpty(plantilla)){
			tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
		}
		refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1;

		logger.warn("FIN - " + ObtenerImporteTipoContratoYearCIFRule.class);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp){
        
        try{
        	if (LibreOfficeUtil.ReferenciasTablas.TABLA1.equals(refTabla)){ 
        		
        		//DPCR2018/1
        		String [] vNumexp = numexp.split("/");
        		String year = "";
        		if(vNumexp.length>0){
        			year = vNumexp[0];
        		}
        		
        		ArrayList<String> tiposContratos = new ArrayList<String>();
    			tiposContratos.add("3 - Obras");
    			tiposContratos.add("2 - Servicios");
    			tiposContratos.add("1 - Suministros");
    			
    			IItemCollection itPartic = ParticipantesUtil.getParticipantesByRol(rulectx.getClientContext(), numexp, "INT");
    			Iterator<IItem> itePartici = itPartic.iterator();
    			
    			int numFilas = itPartic.toList().size();
    			
    			XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas+1, 5);
        		
        		if(null != tabla){
        			double[] distribucionColumnas = {15, 53, 10, 10, 12};
        			LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);
        		
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "CIF");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "Nombre");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Obras");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "Servicios");
        			LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, "Suministros");	
        			int i = 0;
	    			while(itePartici.hasNext()){
	    				i++;
	    				
	    				IItem participante = itePartici.next();
	    				String cif = "";				
	    				if(participante!=null && StringUtils.isNotEmpty(participante.getString(ParticipantesUtil.NDOC))){
	    					cif = participante.getString(ParticipantesUtil.NDOC);
	    					LibreOfficeUtil.setTextoCelda(tabla, 1, (i+1), cif);
	    				}
	    				String nombre = "";
	    				if(StringUtils.isNotEmpty(participante.getString(ParticipantesUtil.NOMBRE))){
	    					nombre = participante.getString(ParticipantesUtil.NOMBRE);
	    					LibreOfficeUtil.setTextoCelda(tabla, 2, (i+1), nombre);
	    				}
	    				
	    				StringBuilder numexpErroneos = new StringBuilder();
	    				for (int j = 0; j < tiposContratos.size(); j++) {
	    					double cantiaAdj = 0.0;
	    					String squery = "NUMEXP IN (SELECT NUMEXP FROM CONTRATACION_DATOS_CONTRATO WHERE PROC_ADJ='6 - Contrato Menor' AND TIPO_CONTRATO='"+tiposContratos.get(j)+"') AND NIF_ADJUDICATARIA='"+cif+"' "
	    							+ "AND NUMEXP LIKE '"+year+"/%'";
	    					Iterator<IItem> itDatosTrami = ConsultasGenericasUtil.queryEntities(rulectx, "CONTRATACION_DATOS_TRAMIT", squery);
	    					while(itDatosTrami.hasNext()){
	    						IItem datosTrami = itDatosTrami.next();
	    						String numexpCIF = "";
	    						if(StringUtils.isNotEmpty(datosTrami.getString("NUMEXP"))){
	    							numexpCIF = datosTrami.getString("NUMEXP");
	    						}
	    						if(datosTrami!=null && StringUtils.isNotEmpty(datosTrami.getString("IMP_ADJ_SINIVA"))){
	    							String adjSinIVA = datosTrami.getString("IMP_ADJ_SINIVA");
	    							try{
	    								Double impSinIVA = Double.parseDouble(adjSinIVA);
	    								cantiaAdj += impSinIVA;
	    							}
	    							catch (NumberFormatException e) {
	    								numexpErroneos.append(numexpCIF+" -> "+adjSinIVA+"; ");
	    							}
	    						}
	    					}
	    					if(numexpErroneos.length()!=0){
	    						LibreOfficeUtil.setTextoCelda(tabla, j+3, (i+1), "EXP erróneos. "+numexpErroneos.toString());
	    					}
	    					else{
	    						numexpErroneos = new StringBuilder();	
	    						int color = 0;
	    						if(tiposContratos.get(j).equals("3 - Obras") && cantiaAdj>DipucrFuncionesComunes.CUANTIACONTRATISTAOBRAS){
	    							color=0xFF0000;
	    						}
	    						else{
	    							if(tiposContratos.get(j).equals("2 - Servicios") && cantiaAdj>DipucrFuncionesComunes.CUANTIACONTRATISTASERVICIOS){
	    								color=0xFF0000;
		    						}
	    							else{
	    								if(tiposContratos.get(j).equals("1 - Suministros") && cantiaAdj>DipucrFuncionesComunes.CUANTIACONTRATISTASUMINISTROS){
	    									color=0xFF0000;
			    						}
	    							}
	    						}	    						
		    					LibreOfficeUtil.setTextoCelda(tabla, j+3, (i+1), cantiaAdj+"", null, null, color);
	    					}	    					
	    				}
	    			}
        		}
			}
		}
        catch (ISPACException e) {
        	logger.error("Error Numexp "+numexp+" - "+e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error Numexp "+numexp+" - "+e.getMessage(), e);
		}
	}

}
