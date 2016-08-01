package es.dipucr.contratacion.rule;

import java.util.Vector;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CargarDatosSobres extends CargaDocumentacionPresentar{
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		sobre1 = "SOBRE A";
		sobre1TipoDoc = "1 - Documentación administrativa";
		sobre1Docs = new Vector<String>();
		sobre1Docs.add("Los que acrediten la personalidad juridica del empresario: DNI o fotocopia autorizada del mismo, o documento que le sustituya reglamentariamente si se trata de extranjeros. En el caso de actuar en representación, acompañarán el Poder que así lo acredite y la Escritura de Constitución o modificación de la Sociedad, inscrita en el Registro Mercantil");
		sobre1Docs.add("En el caso de concurrir  a la licitación varias empresas constituyendo una unión temporal, cada una de ellas deberá acreditar su personalidad y capacidad");
		sobre1Docs.add("Documento de clasificación empresarial en el caso que se exija y si no se exige, deberán presentar documentos que acrediten requisitos de solvencia económica y financiera y solvencia técnica");
		sobre1Docs.add("Para las empresas extranjeras declaración de someterse a la jurisdicción de los Juzgados y tribunales españoles de cualquier orden");
		sobre1Docs.add("Declaración jurada o declaración expresa respolsable de no hallare incursos en ninguna de las prohibiciones de contratar conforme al art. 60 TRLCSP");
		sobre1Docs.add("Declaración responsable de no existencia de empresas vinculadas con el oferente, o en su caso relación de empresas vinculadas con el mismo");
		sobre1Docs.add("Relación de equipo, maquinaria, personas facultativo/titulado y medios auxiliares que la empresa se compromete a poner a disposición");
		sobre1Docs.add("Programa de construcción de las obras que asegure la ejecución en los plazos");
		sobre1Docs.add("Voluntario el Certificado expedido por el Registro de Licitadores de Castilla La  Mancha acompañado de una Declaración expresa responsable relativa a la no alteración de los datos que constan en el referido Registro");
		sobre1Docs.add("Voluntario el Certificado expedido por el Registro Oficial de Licitadores y Empresas Clasificadas del Estado acompañado de una Declaración expresa responsable relativa a la no alteración de los datos que constan en el referido Registro");
		sobre1Docs.add("Declaración de documentos confidenciales para el caso de que el licitador considere que tienen carácter confidencial determinados documentos incorporados a su oferta");
	
		
		sobre2 = "SOBRE B";
		sobre2TipoDoc = "2 - Oferta técnica o evaluable mediante juicio de valor";
		sobre2Docs = new Vector<String>();
			
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
