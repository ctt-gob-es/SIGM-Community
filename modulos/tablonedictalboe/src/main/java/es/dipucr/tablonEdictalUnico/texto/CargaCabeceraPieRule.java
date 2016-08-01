package es.dipucr.tablonEdictalUnico.texto;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;

public class CargaCabeceraPieRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargaCabeceraPieRule.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}


	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}


	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IItem itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "No habiéndose podido practicar directamente la notificación personal de los ACTOS  y a los OBLIGADOS TRIBUTARIOS  que posteriormente se relacionan, a pesar de haberse intentado en la forma debida y por causas no imputables a esta Administración Tributaria, se procede, de conformidad con lo establecido en el artículo 112 apartado 1 de la Ley 58/2003, de 17 de diciembre, General Tributaria, a citar a los mismos para que comparezcan para ser notificados del acto que a continuación se expresa: ");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "ACTO PENDIENTE DE NOTIFICAR: Providencia de Apremio.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "TABLA");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "PLAZO: Quince días naturales, contados a partir del siguiente al de la publicación del presente anuncio en el Boletín Oficial del Estado.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "ORGANO RESPONSABLE: Servicio de Gestión Tributaria, Inspección y Recaudación.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "ADVERTENCIA: Cuando transcurrido el plazo no se hubiese comparecido, la notificación se entenderá producida, a todos los efectos legales, desde el día siguiente al del vencimiento del plazo señalado para comparecer.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "Contra los actos objeto de notificación podrá interponerse recurso de reposición ante la Tesorería de la Diputación, en el plazo de un mes a contar desde el día siguiente al que tenga efectividad la notificación de los citados actos, de conformidad con lo establecido en el artículo 14.2 c) del R.D. Legislativo 2/2004 de 5 de marzo, por el que se aprueba el Texto Refundido de  la Ley  Reguladora de las Haciendas Locales.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "Contra la desestimación expresa o presunta del recurso de reposición podrá interponerse recurso contencioso-administrativo ante los Juzgados de lo Contencioso-administrativo de la provincia de Ciudad Real, en el plazo de dos meses a contar desde el día siguiente de la recepción de la notificación de la desestimación, cuando ésta sea expresa, o en el plazo de seis meses a contar desde el día siguiente en que el referido recurso de reposición se entienda desestimado de forma presunta.");
			itAnuncioTexto.store(rulectx.getClientContext());
			itAnuncioTexto = ConsultasGenericasUtil.createEntities(rulectx, "TABLON_EDICTAL_BOE_PARRAFOS");
			itAnuncioTexto.set("PARRAFO", "SUSPENSION DEL PROCEDIMIENTO: La interposición del recurso no paraliza la tramitación del procedimiento de apremio, el cual sólo se suspenderá en los casos y condiciones previstos en el artículo 165.1 de la Ley 58/2003 de 17 de diciembre General Tributaria.");
			itAnuncioTexto.store(rulectx.getClientContext());
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. Numexp: "+rulectx.getNumExp(),e);
		}
		return new Boolean(true);
	}


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
