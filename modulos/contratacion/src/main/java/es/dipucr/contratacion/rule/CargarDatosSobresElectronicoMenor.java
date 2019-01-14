package es.dipucr.contratacion.rule;

import java.util.Vector;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CargarDatosSobresElectronicoMenor extends CargaDocumentacionPresentar{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sobre1 = "Administrativo";
		sobre1TipoDoc = "1 - Documentación administrativa";
		
		
		
		sobre2 = "SOBRE B";
		sobre2TipoDoc = "2 - Oferta técnica o evaluable mediante juicio de valor";
		sobre2Docs = new Vector<String>();sobre1Docs = new Vector<String>();
		sobre1Docs.add("Económico");
		
			
		sobre3 = "SOBRE C";
		sobre3TipoDoc = "3 - Oferta económica o evaluable mediante fórmulas";
		sobre3Docs = new Vector<String>();
		sobre3Docs.add("Proposición económica");
		
		calle = "C/Toledo, nº17";
		localidad = "Ciudad Real";
		provincia = "Ciudad Real";
		lugar = "Diputación Provincial de Ciudad Real";
		cp = "13071";
		
		return true;
	}
}
