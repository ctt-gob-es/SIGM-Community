package es.dipucr.sigem.api.rule.procedures.personal;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.webempleado.domain.beans.PeriodoLaboral;
import es.dipucr.webempleado.model.mapping.Expediente;
import es.dipucr.webempleado.services.personal.PersonalWSProxy;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class CargaGradoPersonalAntigServPrestRule extends DipucrAutoGeneraDocIniTramiteRule {
	
	public static final Logger LOGGER = Logger.getLogger(CargaGradoPersonalAntigServPrestRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		LOGGER.info("INICIO - " + this.getClass().getName());
		try {

			IClientContext cct = rulectx.getClientContext();

			plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());

			if (StringUtils.isNotEmpty(plantilla)) {
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
			}
			refTablas = "%TABLA1%";

		} catch (ISPACException e) {
			LOGGER.error("Error al generar el documento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento. " + e.getMessage(), e);
		}
		LOGGER.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI, String numexp) {
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// ----------------------------------------------------------------------------------------------
			if (refTabla.equals("%TABLA1%")) {

				
				String consulta = "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"'  AND RELACION='Solicitud Convocatoria') ORDER BY IDENTIDADTITULAR ASC";
		    	IItemCollection collAllPropsExp = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", consulta);
				Iterator<IItem> itExpRel = collAllPropsExp.iterator();
				
				PersonalWSProxy personal = new PersonalWSProxy();
				
				//int numFilas = collAllPropsExp.toList().size();
				
				int numFilasTotales = calculoNumFilasTotales(cct, rulectx.getNumExp());
				

				XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilasTotales + 1, 13);
				double[] distribucionColumnas = { 11, 29, 11, 11, 7, 11, 20, 10, 10, 10, 10 , 10, 10};
				LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);				
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "CIF", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "Nombre", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Fecha Antigüedad Absoluta", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "Fecha Antigüedad Plaza", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, "Grado Consolidación", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, "Fecha Consolidación Grado", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 7, "Categoria", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 8, "Régimen", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 9, "Grupo", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 10, "Servicio", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 11, "Dias - Meses - Años", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 12, "Fecha alta", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 13, "Fecha Baja", new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
            	
            	int i = 1;
		        while(itExpRel.hasNext()){
		        	IItem hijoSolicitud = itExpRel.next();
		        	String numExpSolicitud = "";
		        	if(hijoSolicitud.getString(ParticipantesUtil.NUMEXP)!=null) numExpSolicitud = hijoSolicitud.getString(ParticipantesUtil.NUMEXP);
		        	IItemCollection partiHijos = ParticipantesUtil.getParticipantes( cct, numExpSolicitud,  ParticipantesUtil.ROL+" = 'INT'", "");
		        	Iterator<IItem> itpartiHijos = partiHijos.iterator();		        	
		        	while(itpartiHijos.hasNext()){		        		
		        		IItem hijos = itpartiHijos.next();
						String nombre = "";
						if (hijos.getString(ParticipantesUtil.NOMBRE) != null)nombre = hijos.getString(ParticipantesUtil.NOMBRE);
						String dni = "";
						if (hijos.getString(ParticipantesUtil.NDOC) != null)dni = hijos.getString(ParticipantesUtil.NDOC);
						if (!dni.isEmpty() && !nombre.isEmpty()) {
							i++;
		                    LibreOfficeUtil.setTextoCelda(tabla, 1, i, dni, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
		                    LibreOfficeUtil.setTextoCelda(tabla, 2, i, nombre, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
							Expediente expdienteCiuda = personal.getExpediente(dni);
							if(expdienteCiuda!=null){
								String sFechaAntigAbso = "";
								if(expdienteCiuda.getFAbsol()!=null){
									Calendar fechaAntigAbso=expdienteCiuda.getFAbsol();
									Date date = fechaAntigAbso.getTime();             
									SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
									sFechaAntigAbso = format1.format(date);
								}			
			                     LibreOfficeUtil.setTextoCelda(tabla, 3, i, sFechaAntigAbso, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
			                     
			                     String sFechaAntigPlaza = "";
			                     if(expdienteCiuda.getFAntP()!=null){
			                    	 Calendar fechaAntigAbso=expdienteCiuda.getFAntP();
			                    	 Date date = fechaAntigAbso.getTime();             
			                    	 SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			                    	 sFechaAntigPlaza = format1.format(date);
			                     }				                     
			                     LibreOfficeUtil.setTextoCelda(tabla, 4, i, sFechaAntigPlaza, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
			                     
			                     String sGradoConsolidacion = "";
			                     if(expdienteCiuda.getGradoCon()!=null) sGradoConsolidacion = expdienteCiuda.getGradoCon();				                     
			                     LibreOfficeUtil.setTextoCelda(tabla, 5, i, sGradoConsolidacion, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
			                     
			                     String sConsolidacion = "";
			                     if(expdienteCiuda.getFConsol()!=null){
			                    	 Calendar fechaConsol=expdienteCiuda.getFConsol();
			                    	 Date date = fechaConsol.getTime();             
			                    	 SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
			                    	 sConsolidacion = format1.format(date);
			                     }				                     
			                     LibreOfficeUtil.setTextoCelda(tabla, 6, i, sConsolidacion, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
							}
							PeriodoLaboral[] vPeriodoLab = personal.getVidaLaboral(dni);
							if(vPeriodoLab!=null && vPeriodoLab.length>0){
								for(int j=0; j<vPeriodoLab.length; j++){
									if(j!=0 && j!=vPeriodoLab.length){
										i++;
										LibreOfficeUtil.setTextoCelda(tabla, 1, i, "");
						                LibreOfficeUtil.setTextoCelda(tabla, 2, i, "");
						                LibreOfficeUtil.setTextoCelda(tabla, 3, i, "");
						                LibreOfficeUtil.setTextoCelda(tabla, 4, i, "");
						                LibreOfficeUtil.setTextoCelda(tabla, 5, i, "");
						                LibreOfficeUtil.setTextoCelda(tabla, 6, i, "");
									}
									PeriodoLaboral periodoLab = vPeriodoLab[j];
									String categoria = "";
									if(periodoLab.getCategoria()!=null) categoria = periodoLab.getCategoria();
									LibreOfficeUtil.setTextoCelda(tabla, 7, i, categoria, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
									
									String regimen = "";
									if(periodoLab.getRegimen()!=null) regimen = periodoLab.getRegimen();
									LibreOfficeUtil.setTextoCelda(tabla, 8, i, regimen, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
									
									String grupo = "";
									if(periodoLab.getGrupo()!=null) grupo = periodoLab.getGrupo();
									LibreOfficeUtil.setTextoCelda(tabla, 9, i, grupo, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
									
									String servicio = "";
									if(periodoLab.getServicio()!=null) servicio = periodoLab.getServicio();
									LibreOfficeUtil.setTextoCelda(tabla, 10, i, servicio, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
									
									String total = "";
									if(periodoLab.getDias()>=0 && periodoLab.getMeses()>=0 && periodoLab.getAnios()>=0){
										total = periodoLab.getDias()+" - "+periodoLab.getMeses()+" - "+periodoLab.getAnios();
									}
									LibreOfficeUtil.setTextoCelda(tabla, 11, i, total, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);									
									
									String sFechaAlta = "";
				                     if(periodoLab.getFechaAlta()!=null){
				                    	 Calendar fechaConsol=periodoLab.getFechaAlta();
				                    	 Date date = fechaConsol.getTime();             
				                    	 SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
				                    	 sFechaAlta = format1.format(date);
				                     }				                     
				                     LibreOfficeUtil.setTextoCelda(tabla, 12, i, sFechaAlta, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
				                     
				                     String sFechaBaja = "";
				                     if(periodoLab.getFechaBaja()!=null){
				                    	 Calendar fechaConsol=periodoLab.getFechaBaja();
				                    	 Date date = fechaConsol.getTime();             
				                    	 SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
				                    	 sFechaBaja = format1.format(date);
				                     }				                     
				                     LibreOfficeUtil.setTextoCelda(tabla, 13, i, sFechaBaja, new Float(6.0), LibreOfficeUtil.Fuentes.ARIAL);
								}
								
							}							
						}
		        	}
		        }
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (RemoteException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		}

	}

	private int calculoNumFilasTotales(ClientContext cct, String numexp) {
		int numFilasTotales = 0;
		try {
		// ----------------------------------------------------------------------------------------------
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		// ----------------------------------------------------------------------------------------------
		
		String consulta = "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+numexp+"'  AND RELACION='Solicitud Convocatoria') ORDER BY IDENTIDADTITULAR ASC";
    	IItemCollection collAllPropsExp = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", consulta);
		Iterator<IItem> itExpRel = collAllPropsExp.iterator();
		
		PersonalWSProxy personal = new PersonalWSProxy();
		  while(itExpRel.hasNext()){
	        	IItem hijoSolicitud = itExpRel.next();
	        	String numExpSolicitud = "";
	        	if(hijoSolicitud.getString(ParticipantesUtil.NUMEXP)!=null) numExpSolicitud = hijoSolicitud.getString(ParticipantesUtil.NUMEXP);
	        	IItemCollection partiHijos = ParticipantesUtil.getParticipantes( cct, numExpSolicitud,  ParticipantesUtil.ROL+" = 'INT'", "");
	        	Iterator<IItem> itpartiHijos = partiHijos.iterator();		        	
	        	while(itpartiHijos.hasNext()){		        		
	        		IItem hijos = itpartiHijos.next();
					String dni = "";
					if (hijos.getString(ParticipantesUtil.NDOC) != null)dni = hijos.getString(ParticipantesUtil.NDOC);
					if (!dni.isEmpty()) {
						PeriodoLaboral[] vPeriodoLab = personal.getVidaLaboral(dni);
						if(vPeriodoLab!=null){
							numFilasTotales = numFilasTotales + vPeriodoLab.length;
						}
						else{
							numFilasTotales = numFilasTotales + 1;
						}
					}
	        	}
		  }
		} catch (ISPACException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (RemoteException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} 
		return numFilasTotales;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
