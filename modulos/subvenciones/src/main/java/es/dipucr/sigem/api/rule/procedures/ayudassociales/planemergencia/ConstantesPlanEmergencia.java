package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;



public class ConstantesPlanEmergencia {
    
    public final static String ALIMENTACION = "ALIMENTACION";
    public final static String EXCEPCIONAL = "EXCEPCIONAL";
    public final static String COMEDOR = "COMEDOR";
    public final static String LIBROS = "LIBROS";
    
    public final static String TRIMESTRE = "TRIMESTRE";
    
    public final static String PRIMER_TRIMESTRE = "PRIMER TRIMESTRE";
    public final static String SEGUNDO_TRIMESTRE = "SEGUNDO TRIMESTRE";
    public final static String TERCER_TRIMESTRE = "TERCER TRIMESTRE";
    public final static String CUARTO_TRIMESTRE = "CUARTO TRIMESTRE";
    
    public final static String CORREOS_PLAN_EMERGENCIA = "CORREOS_PLAN_EMERGENCIA";
    
    public final static String RELACION_CONVOCATORIA = "Solicitud Servicios Sociales: Plan Emergencia";
    public final static String RELACION_SOLICITUDES = "Solicitudes Trimestrales Plan de Emergencia";

    /**
     * Mapeo de las tablas
     */
    public interface DpcrSERSOPlanEmer{
        String NOMBRE_TABLA = "DPCR_SERSO_PLAN_EMER";
        
        String NIF = "NIF";
        String NOMBRE = "NOMBRE";
        
        String NOMBRESOLICITANTE = "NOMBRESOLICITANTE";
        String DOCUMENTOIDENTIDAD = "DOCUMENTOIDENTIDAD";
        
        String TIPOAYUDA = "TIPOAYUDA";
        String DESCRIPCION_TIPOAYUDA = "DESCRIPCION_TIPOAYUDA";
        String CONVOCATORIA = "CONVOCATORIA";
        String CIUDAD = "CIUDAD";

        String NFAMILIAR = "NFAMILIAR";
        String MENORES3ANIOS = "MENORES3ANIOS";
        String NUMMENORES = "NUMMENORES";
        
        String PROPUESTA1_IMPORTE = "PROPUESTA1_IMPORTE";
        String PROPUESTA2_IMPORTE = "PROPUESTA2_IMPORTE";
        
        String ESTRABAJADOR = "ESTRABAJADOR";
    }
     
    public interface SERSOPlanEmerConcesion {
        String NOMBRE_TABLA= "SERSO_PLAN_EMER_CONCESION";   
        String PORMIEMBROS = "PORMIEMBROS";
        String PORMENORES = "PORMENORES";
        String TOTALMES = "TOTALMES";
        String PROPUESTA11 = "PROPUESTA11";
        String PROPUESTA12 = "PROPUESTA12";
        String PROPUESTA13 = "PROPUESTA13";
        String TOTAL11 = "TOTAL11";
        String TOTAL12 = "TOTAL12";
        String TOTALCONCEDIDO = "TOTALCONCEDIDO";
        String TOTALSEMESTRE1 = "TOTALSEMESTRE1";
        String MAXIMOSEMESTRE = "MAXIMOSEMESTRE";
        String NUMAYUDA1 = "NUMAYUDA1";
        String NUMEXC1 = "NUMEXC1";
    
        String PROPUESTA21 = "PROPUESTA21";
        String PROPUESTA22 = "PROPUESTA22";
        String PROPUESTA23 = "PROPUESTA23";
        String TOTAL21 = "TOTAL21";
        String TOTAL22 = "TOTAL22";
        String PORMIEMBROS2 = "PORMIEMBROS2";
        String PORMENORES2 = "PORMENORES2";
        String TOTALMES2 = "TOTALMES2";
        String TOTALSEMESTRE2 = "TOTALSEMESTRE2";
        String TOTALCONCEDIDO2 = "TOTALCONCEDIDO2";
        String MAXIMOSEMESTRE2 = "MAXIMOSEMESTRE2";
        String NUMAYUDA2 = "NUMAYUDA2";
        String NUMEXC2 = "NUMEXC2";
    
        String PORMIEMBROS3 = "PORMIEMBROS3";
        String PORMENORES3 = "PORMENORES3";
        String TOTALMES3 = "TOTALMES3";
        String PROPUESTA31 = "PROPUESTA31";
        String PROPUESTA32 = "PROPUESTA32";
        String PROPUESTA33 = "PROPUESTA33";
        String TOTAL31 = "TOTAL31";
        String TOTAL32 = "TOTAL32";
        String TOTALSEMESTRE3 = "TOTALSEMESTRE3";
        String TOTALCONCEDIDO3 = "TOTALCONCEDIDO3";
        String MAXIMOSEMESTRE3 = "MAXIMOSEMESTRE3";
        String NUMAYUDA3 = "NUMAYUDA3";
        String NUMEXC3 = "NUMEXC3";
    
        String PORMIEMBROS4 = "PORMIEMBROS4";
        String PORMENORES4 = "PORMENORES4";
        String TOTALMES4 = "TOTALMES4";
        String PROPUESTA41 = "PROPUESTA41";
        String PROPUESTA42 = "PROPUESTA42";
        String PROPUESTA43 = "PROPUESTA43";
        String TOTAL41 = "TOTAL41";
        String TOTAL42 = "TOTAL42";
        String TOTALSEMESTRE4 = "TOTALSEMESTRE4";
        String TOTALCONCEDIDO4 = "TOTALCONCEDIDO4";
        String MAXIMOSEMESTRE4 = "MAXIMOSEMESTRE4";
        String NUMAYUDA4 = "NUMAYUDA4";
        String NUMEXC4 = "NUMEXC4";
    
        String TOTALTOTAL = "TOTALTOTAL";
    }
    
    public interface DpcrSERSONVales{
        String NOMBRE_TABLA = "DPCR_SERSO_N_VALES";
        
        String SEMESTRE1 = "SEMESTRE1";
        String SEMESTRE2 = "SEMESTRE2";
        String SEMESTRE3 = "SEMESTRE3";
        String SEMESTRE4 = "SEMESTRE4";
                
        String SEMESTRE1IMPRESOS = "SEMESTRE1IMPRESOS";
        String SEMESTRE2IMPRESOS = "SEMESTRE2IMPRESOS";
        String SEMESTRE3IMPRESOS = "SEMESTRE3IMPRESOS";
        String SEMESTRE4IMPRESOS = "SEMESTRE4IMPRESOS";
        String MAXSEMESTRE1 = "MAXSEMESTRE1";
        String MAXSEMESTRE2 = "MAXSEMESTRE2";
        String MAXSEMESTRE3 = "MAXSEMESTRE3";
        String MAXSEMESTRE4 = "MAXSEMESTRE4";
    }

    public interface DpcrSERSOPeCantAcum{
        String NOMBRE_TABLA = "DPCR_SERSO_PE_CANT_ACUM";
        
        String PRIMERTRIMESTRE = "PRIMERTRIMESTRE";
        String SEGUNDOTRIMESTRE = "SEGUNDOTRIMESTRE";
        String TERCERTRIMESTRE = "TERCERTRIMESTRE";
        String CUARTOTRIMESTRE = "CUARTOTRIMESTRE";
            
        String TOTALSEMESTRE1 = "TOTALSEMESTRE1";
        String TOTALSEMESTRE2 = "TOTALSEMESTRE2";
        String TOTALSEMESTRE3 = "TOTALSEMESTRE3";
        String TOTALSEMESTRE4 = "TOTALSEMESTRE4";
        
        String TOTALCONCEDIDO = "TOTALCONCEDIDO";
        String TOTALCONCEDIDO2 = "TOTALCONCEDIDO2";
        String TOTALCONCEDIDO3 = "TOTALCONCEDIDO3";
        String TOTALCONCEDIDO4 = "TOTALCONCEDIDO4";
        
        String TOTAL = "TOTAL";
        
        String TRIMESTRAL = "TRIMESTRAL";
    }
    
    public interface DpcrSERSOChequesGenerados{
        String NOMBRE_TABLA = "DPCR_SERSO_CHEQUES_GENERADOS";
        String CODIGO = "CODIGO";
    }
    
    public interface DpcrSERSOTrabSocial{
        String NOMBRE_TABLA = "DPCR_SERSO_TRAB_SOCIAL";
        String DNI = "DNI";
        String EMAIL = "EMAIL";
    }
    
    public interface DpcrSERSOPeConcesionExce{
        String NOMBRE_TABLA = "DPCR_SERSO_PE_CONCESION_EXCE";
        String NFACTURA = "NFACTURA";
        String FECHAFACTURA = "FECHAFACTURA";
        String CONCEPTO = "CONCEPTO";
        String PROVEEDOR = "PROVEEDOR";
    }
    
    public interface DpcrSERSOLibrosComedor{
        String NOMBRE_TABLA = "DPCR_SERSO_LIBROS_COMEDOR";
        
        String TOTALLIBROS = "TOTALLIBROS";
        String PAGADO = "PAGADO";
        String CURSO = "CURSO";
        String COSTELIBROS = "COSTELIBROS";
        
        String EMPRESACOMEDOR1 = "EMPRESACOMEDOR1";
        String NOMBRE1 = "NOMBRE1";
        String EMPRESACOMEDOR2 = "EMPRESACOMEDOR2";
        String NOMBRE2 = "NOMBRE2";
        String MESINICIO = "MESINICIO";
        String MESESSOLCOMEDOR = "MESESSOLCOMEDOR";
        String IMPORTETOTALCOMEDOR = "IMPORTETOTALCOMEDOR";
    }

    private ConstantesPlanEmergencia(){
    }
}
