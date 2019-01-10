package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;


public class GenerateCertificadoAsistenciaRule implements IRule {
	
	protected String nombreDocCertificado = "Certificado de Asistencia";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			IClientContext cct = rulectx.getClientContext();

			cct.setSsVariable("DESCR_ORGANO", getDescripcionOrgano(rulectx));

			IItem entityTemplate = DocumentosUtil.generarDocumento(rulectx, nombreDocCertificado, nombreDocCertificado);

			// Referencia al fichero del documento en el gestor documental
			entityTemplate.set("EXTENSION", DocumentosUtil.obtenerExtensionDocPorEntidad());
			entityTemplate.store(cct);

			cct.deleteSsVariable("DESCR_ORGANO");

		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
		return true;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// Nada que hacer al cancelar
	}

	private String getDescripcionOrgano(IRuleContext rulectx) throws ISPACException {
		String strDescr = "";

		try {
			String strOrgano = SecretariaUtil.getSesion(rulectx, null).getString("ORGANO");
			if (strOrgano.compareTo("PLEN") == 0) {
				strDescr = "del ";
			} else {
				strDescr = "de la ";
			}
			strDescr += SecretariaUtil.getNombreOrganoSesion(rulectx, null);
			if (strOrgano.compareTo("COMI") == 0) {
				strDescr += " del área de " + SecretariaUtil.getNombreAreaSesion(rulectx, null);
			}
		} catch (ISPACException e) {
			throw new ISPACException(e);
		}
		return strDescr;
	}
}
