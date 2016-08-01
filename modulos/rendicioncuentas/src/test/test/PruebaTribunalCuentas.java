package test;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos;
import es.dipucr.jaxb.tribunalcuentas.beans.SistemaAdjudicacion;
import es.dipucr.jaxb.tribunalcuentas.beans.TipoContrato;
import es.dipucr.jaxb.tribunalcuentas.beans.TipoEntidad;
import es.dipucr.jaxb.tribunalcuentas.beans.Tramitacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Modificacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadAdjudicacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadFormalizacion;
import es.dipucr.jaxb.tribunalcuentas.beans.RelacionContratos.Contrato.Publicidad.PublicidadLicitacion;

public class PruebaTribunalCuentas {

	public static void main(String[] args) {
		RelacionContratos  relacionContratos =  new RelacionContratos();
		relacionContratos.setEjercicio("2014");
		relacionContratos.setNIF("05695305E");
		relacionContratos.setNomEntidad("Diputacion Provincial de Ciudad Real");
		relacionContratos.setTipoEntidad(TipoEntidad.A);
		Contrato contrato = new Contrato();
		contrato.setAcuerdoMarco("Acuerdo");
		contrato.setAdjDef("ADJ DEF");
		contrato.setComplementario("complementario");
		contrato.setFechaAdjDef("fecha adjudicacion");
		contrato.setFechaForm("Fecha Formalizacion");
		contrato.setFormalizacion("Formalizacion");
		contrato.setLotes("Lotes");
		
		Modificacion modificacion = new Modificacion();
		modificacion.setEjercicioOriginal("Ejercicio Original");
		modificacion.setReferenciaOriginal("ReferenciaOriginal");
		contrato.setModificacion(modificacion);
		
		contrato.setNumLotes(2);
		contrato.setObjeto("Objeto del contrato");
		contrato.setObservaciones("Observaciones");
		Calendar cal = Calendar.getInstance();
		contrato.setPlazoConcesion(new BigDecimal(cal.getTimeInMillis()));
		contrato.setPlazoEjecucion(new BigDecimal(cal.getTimeInMillis()));
		contrato.setPrecAdjudicacion(new BigDecimal(10000));
		contrato.setPrecLicitacion(new BigDecimal(10));
		contrato.setProrroga("prorroga");
		
		Publicidad publicidad = new Publicidad();
		publicidad.setExistePublicidad("Existe publicidad");
		PublicidadAdjudicacion publicidadAdjudicacion = new PublicidadAdjudicacion();
		publicidadAdjudicacion.setFechaContEstado("FechaContEstado");
		publicidadAdjudicacion.setFechaOtros("FechaOtros");
		publicidadAdjudicacion.setFechaPerfil("FechaPerfil");
		publicidad.setPublicidadAdjudicacion(publicidadAdjudicacion);
		
		PublicidadFormalizacion publicidadFormalizacion = new PublicidadFormalizacion();
		publicidadFormalizacion.setFechaBOCA("FechaBOCAFormalizacion");
		publicidadFormalizacion.setFechaBOE("FechaBOEFormalizacion");
		publicidadFormalizacion.setFechaBOP("FechaBOPFormalizacion");
		publicidadFormalizacion.setFechaContEstado("FechaContEstadoFormalizacion");
		publicidadFormalizacion.setFechaDOUE("FechaDOUEFormalizacion");
		publicidadFormalizacion.setFechaOtros("OtrosFormalizacion");
		publicidadFormalizacion.setFechaPerfil("Fecha PerfilFormalizacion");		
		publicidad.setPublicidadFormalizacion(publicidadFormalizacion);
		
		PublicidadLicitacion publicidadLictacion = new PublicidadLicitacion();
		publicidadLictacion.setFechaBOCA("FechaBOCALictacion");
		publicidadLictacion.setFechaBOE("FechaBOELictacion");
		publicidadLictacion.setFechaBOP("FechaBOPLictacion");
		publicidadLictacion.setFechaContEstado("FechaContEstadoLictacion");
		publicidadLictacion.setFechaDOUE("FechaDOUELictacion");
		publicidadLictacion.setFechaOtros("OtrosLictacion");
		publicidadLictacion.setFechaPerfil("Fecha PerfilLictacion");	
		publicidad.setPublicidadLicitacion(publicidadLictacion);
		contrato.setPublicidad(publicidad);
		
		contrato.setReferencia("referencia");
		contrato.setRegArmonizada("RegArmonizada");
		contrato.setSistAdjudicacion(SistemaAdjudicacion.A);
		contrato.setSistemaDinamico("SistemaDinamico");
		contrato.setSubastaElectronica("SubastaElectronica");
		contrato.setTipoContrato(TipoContrato.A);
		contrato.setTmpProrroga(new BigDecimal(cal.getTimeInMillis()));
		contrato.setTramitacion(Tramitacion.E);
		
		relacionContratos.getContrato().add(contrato);
		
		try {
			//tambien podemos poner el nombre de la clase
			//JAXBContext jaxbContext = JAXBContext.newInstance(Libro.class);
			JAXBContext jaxbContext = JAXBContext.newInstance("es.dipucr.jaxb.tribunalcuentas.beans");
			Marshaller marshaller = jaxbContext.createMarshaller();
			//indicamos que queremos formateada nuestra salida (con enters y tabs)
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//indicamos el objeto que escribiremos a XML y la salida (puede ser un objeto tipo FILE)
			marshaller.marshal(relacionContratos, System.out);
		 } catch (JAXBException e) {
			e.printStackTrace();
		 }

	}

}
