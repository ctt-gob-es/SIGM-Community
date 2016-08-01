package es.dipucr.sigem.api.rule.procedures.viasObras.usosExcp;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class DipucrGeneraInfJurAutUsosExc extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraInfJurAutUsosExc .class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " +this.getClass().getName());

		tipoDocumento = "Informe jurídico";
		plantilla = "Informe Jurídico Autorización Usos Excepcionales";

		logger.info("FIN - "+ this.getClass().getName());
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			cct.setSsVariable("ANIO", ""+Calendar.getInstance().get(Calendar.YEAR));
			
			IItemCollection docProviCollection = cct.getAPI().getEntitiesAPI().getDocuments(rulectx.getNumExp(), "UPPER(NOMBRE) LIKE '%PROVIDENCIA%'", " FAPROBACION DESC");
			Iterator docProviIterator = docProviCollection.iterator();
			String fechaProv = "";
			if(docProviIterator.hasNext()){
				IItem docProv = (IItem) docProviIterator.next();
				Date fechaFirma = docProv.getDate("FAPROBACON");
				if(fechaFirma != null)
					fechaProv = new SimpleDateFormat("dd/MM/yyyy").format(fechaFirma);
			}
			cct.setSsVariable("FECHA_PROV", fechaProv);
			
			IItemCollection docInfTecCollection = cct.getAPI().getEntitiesAPI().getDocuments(rulectx.getNumExp(), "UPPER(NOMBRE) LIKE '%INFORME TÉCNICO%'", " FAPROBACION DESC");
			Iterator docInfTecIterator = docInfTecCollection.iterator();
			String fechaInfTec = "";
			if(docInfTecIterator.hasNext()){
				IItem docInfTec = (IItem) docInfTecIterator.next();
				Date fechaFirma = docInfTec.getDate("FAPROBACON");
				if(fechaFirma != null)
					fechaInfTec = new SimpleDateFormat("dd/MM/yyyy").format(fechaFirma);
			}
			cct.setSsVariable("FECHA_INF_TEC", fechaInfTec);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("ANIO");
			cct.deleteSsVariable("FECHA_PROV");
			cct.deleteSsVariable("FECHA_INF_TEC");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
