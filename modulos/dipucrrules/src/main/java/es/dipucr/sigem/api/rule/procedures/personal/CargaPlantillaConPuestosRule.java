package es.dipucr.sigem.api.rule.procedures.personal;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.Exception;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class CargaPlantillaConPuestosRule extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger LOGGER = Logger.getLogger(CargaPlantillaConPuestosRule.class);

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

	public void insertaTabla(IRuleContext rulectx, XComponent component, String refTabla, IEntitiesAPI entitiesAPI,
			String numexp) {
		try {
			if (refTabla.equals("%TABLA1%")) {
				IItemCollection icPersonalPuestoTrabajo = entitiesAPI.getEntities("PERSONAL_PUESTO_TRABAJO",rulectx.getNumExp());
				Iterator<IItem> itPersonalPuestoTrabajo = icPersonalPuestoTrabajo.iterator();
				
				int numFilas = icPersonalPuestoTrabajo.toList().size();
				XTextTable tabla = LibreOfficeUtil.insertaTablaEnPosicion(component, refTabla, numFilas + 1, 8);
				double[] distribucionColumnas = { 5, 27, 5, 12, 12, 13, 13, 13};
				LibreOfficeUtil.colocaColumnas(tabla, distribucionColumnas);				
				LibreOfficeUtil.setTextoCeldaCabecera(tabla, 1, "Número Puesto");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 2, "Denominación");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 3, "Adscripción Grupo");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 4, "Complemento Genérico");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 5, "Complemento Específico");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 6, "Ubicación");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 7, "Tipo Jornada");
            	LibreOfficeUtil.setTextoCeldaCabecera(tabla, 8, "Forma Provisión");
				
            	int i = 1;
				while (itPersonalPuestoTrabajo.hasNext()) {
					IItem itemPuestoTrabajo = itPersonalPuestoTrabajo.next();
					String numPuesto = "";
					String denominacion = "";
					if (itemPuestoTrabajo.getString("PUESTOTRABAJO") != null)
						numPuesto = itemPuestoTrabajo.getString("PUESTOTRABAJO");
					String[] vnumPuesto = numPuesto.split(" - ");
					if (vnumPuesto.length > 1) {
						numPuesto = vnumPuesto[0];
						denominacion = vnumPuesto[1];
					}
					String adcripcionGrupo = "";
					if (itemPuestoTrabajo.getString("GRUPO") != null)
						adcripcionGrupo = itemPuestoTrabajo.getString("GRUPO");
					String complemDestino = "";
					if (itemPuestoTrabajo.getFloat("COMP_DEST") > 0)
						complemDestino = itemPuestoTrabajo.getFloat("COMP_DEST") + "";
					String complemEspec = "";
					if (itemPuestoTrabajo.getString("COMP_ESP")  != null)
						complemEspec = itemPuestoTrabajo.getString("COMP_ESP");
					String ubicacion = "";
					if (itemPuestoTrabajo.getString("UBICACION") != null)
						ubicacion = itemPuestoTrabajo.getString("UBICACION");
					String tipoJornada = "";
					if (itemPuestoTrabajo.getString("JORNADA") != null){
						String[] vJornada = itemPuestoTrabajo.getString("JORNADA").split(" - ");
						if (vJornada.length > 1) {
							tipoJornada = vJornada[1];
						}
					}						
					String formaProvision = "";
					if (itemPuestoTrabajo.getString("PROVISION") != null){
						String[] vFormaProvi = itemPuestoTrabajo.getString("PROVISION").split(" - ");
						if (vFormaProvi.length > 1) {
							formaProvision = vFormaProvi[1];
						}
					}
					
					 i++;
                     LibreOfficeUtil.setTextoCelda(tabla, 1, i, numPuesto);
                     LibreOfficeUtil.setTextoCelda(tabla, 2, i, denominacion);
                     LibreOfficeUtil.setTextoCelda(tabla, 3, i, adcripcionGrupo);
                     LibreOfficeUtil.setTextoCelda(tabla, 4, i, complemDestino);
                     LibreOfficeUtil.setTextoCelda(tabla, 5, i, complemEspec);
                     LibreOfficeUtil.setTextoCelda(tabla, 6, i, ubicacion);
                     LibreOfficeUtil.setTextoCelda(tabla, 7, i, tipoJornada);
                     LibreOfficeUtil.setTextoCelda(tabla, 8, i, formaProvision);
				}
			}
		} catch (ISPACRuleException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (ISPACException e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al montar la tabla en el expediente " + numexp + " - " + e.getMessage(), e);
		}

	}
}
