package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.EnviarPublicacionTablonGenericaRule;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class EnviarPublicacionTablonPlenoRule extends EnviarPublicacionTablonGenericaRule {
	
	private static final Logger logger = Logger.getLogger(EnviarPublicacionTablonPlenoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		try {
			nombreDocumento = "Convocatoria del Pleno";
			titulo = "Convocatoria del Pleno";
			IItem sesion = SecretariaUtil.getSesion(rulectx,
					rulectx.getNumExp());
			Date fechaCelebracion = sesion.getDate("FECHA");
			String fechaCe = FechasUtil.getFormattedDate(fechaCelebracion,
					"dd 'de' MMMM 'de' yyyy");
			String horaConv = sesion.getString("HORA");
			descripcion = "Convocatoria de Pleno con fecha " + fechaCe
					+ " y hora " + horaConv;

			codCategoria = "PLENO";
			codServicio = "SEG";
			Date dFechaIniVigencia = new Date();
			Date dFechaFinVigencia = fechaCelebracion;
			calendarIniVigencia = Calendar.getInstance();
			calendarIniVigencia.setTime(dFechaIniVigencia);
			calendarIniVigencia.set(Calendar.HOUR_OF_DAY, 0);
			calendarIniVigencia.set(Calendar.MINUTE, 0);
			calendarIniVigencia.set(Calendar.SECOND, 0);
			calendarFinVigencia = Calendar.getInstance();
			calendarFinVigencia.setTime(dFechaFinVigencia);

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}

		return true;
	}

}
