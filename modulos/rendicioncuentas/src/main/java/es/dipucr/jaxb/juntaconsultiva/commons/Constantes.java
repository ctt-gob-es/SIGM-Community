package es.dipucr.jaxb.juntaconsultiva.commons;

public class Constantes {
	public interface  TIPO_ADMINISTRACION{
		public static final String ORGANO_CONSTITUCIONAL = "1";
		public static final String ESTADO = "2";
		public static final String CCAA = "3";
		public static final String ENTIDADLOCAL = "4";
		public static final String OTROS = "5";
	}
	public interface TIPO_ADMINISTRACION_LOCAL{
		public static final String AYUNTAMIENTO = "A";
		public static final String CABILDO_CONSELLINSULAR = "C"; 
		public static final String DIPUTACIONPROVINCIAL_FORAL = "D"; 
		public static final String MANCOMUNIDAD = "M"; 
		public static final String CONSORCIO = "X"; 
	}
	//Diputacion
	public interface ORGANO_CONTRATACION{
		public static final String PLENO = "PLEN";
		public static final String JUNTAGOBIERNO = "JGOB"; 
		public static final String DECRETO = "DEC";
		public static final String COMISIONINFORMATIVA = "COMI";
		public static final String MESACONTRATACION = "MESA";
	}
	public interface ORGANO_CONTRATACION_JUNTACONSULTIVA{
		public static final String PLENO = "1";
		public static final String ALCALDE = "2";
		public static final String JUNTAGOBIERNO = "3"; 
		public static final String PRESIDENTE = "4";
		public static final String OTROS = "9";
	}
	public interface TIPO_CONTRATO{
		public static final String OBRAS = "A";
		public static final String SUMINISTROS = "C"; 
		public static final String SERVICIOS = "E";
	}
	public interface MODALIDAD{
		public static final String CONCESION = "C";
		public static final String GESTIONINTERESADA = "G"; 
		public static final String CONCIERTO = "M";
	}
	
	public interface CARAC_BIENES{
		public static final String ENTREGA_SUCESIVA_PRECIO_UNITARIO = "1";
		public static final String EQUIPOS_SISTEMAS_PROGRAMAS_TIC = "2"; 
		public static final String SUMINISTRO_FABRICACION = "3";
		public static final String OTROS = "4";
	}
	
	public interface TRAMITE{
		public static final String ORDINARIO = "O";
		public static final String URGENTE = "U"; 
		public static final String EMERGENCIA = "E";
	}
	public interface CRITERIOS_ADJUDICACION{
		public static final String OFERTA_MAS_VENTAJOSA = "C";
		public static final String PRECIO_MAS_VENTAJOSO = "S"; 
		public static final String OTRAS = "N";
	}
	
	public interface PROC_ADJUDICACION{
		public static final String ABIERTO = "A";
		public static final String RESTRINGIDO = "R"; 
		public static final String NEGOCIADO = "N";
	}
	
	public interface FORMA_ADJUDICACION{
		public static final String CONCURSO = "C";
		public static final String SUBASTA = "S"; 
		public static final String NEGOCIADA = "N";
	}
	
	public interface MODALIDAD_IMPORTE{
		public static final String CANONGLOBAL = "C";
		public static final String TARIFAS = "T"; 
		public static final String PRECIOSUNITARIOS = "P";
	}
}
