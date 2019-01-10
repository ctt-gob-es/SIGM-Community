package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;


public class ConstantesPlanEmergencia {
    
    public static final String ALIMENTACION = "ALIMENTACION";
    public static final String EXCEPCIONAL = "EXCEPCIONAL";
    public static final String COMEDOR = "COMEDOR";
    public static final String LIBROS = "LIBROS";
    
    public static final String TRIMESTRE = "TRIMESTRE";
    
    public static final String PRIMER_TRIMESTRE = "PRIMER TRIMESTRE";
    public static final String SEGUNDO_TRIMESTRE = "SEGUNDO TRIMESTRE";
    public static final String TERCER_TRIMESTRE = "TERCER TRIMESTRE";
    public static final String CUARTO_TRIMESTRE = "CUARTO TRIMESTRE";
    
    public static final String CORREOS_PLAN_EMERGENCIA = "CORREOS_PLAN_EMERGENCIA";
    
    public static final String RELACION_CONVOCATORIA = "Solicitud Servicios Sociales: Plan Emergencia";
    public static final String RELACION_SOLICITUDES = "Solicitudes Trimestrales Plan de Emergencia";

    /**
     * Mapeo de las tablas
     */
    public final class DpcrSERSOPlanEmer{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_PLAN_EMER";
        
        public static final String NUMEXP = "NUMEXP";
        
        public static final String NIF = "NIF";
        public static final String NOMBRE = "NOMBRE";
        
        public static final String NOMBRESOLICITANTE = "NOMBRESOLICITANTE";
        public static final String DOCUMENTOIDENTIDAD = "DOCUMENTOIDENTIDAD";
        
        public static final String TIPOAYUDA = "TIPOAYUDA";
        public static final String DESCRIPCION_TIPOAYUDA = "DESCRIPCION_TIPOAYUDA";
        public static final String CONVOCATORIA = "CONVOCATORIA";
        public static final String CIUDAD = "CIUDAD";
        public static final String TRIMESTRE = "TRIMESTRE";

        public static final String NFAMILIAR = "NFAMILIAR";
        public static final String MENORES3ANIOS = "MENORES3ANIOS";
        public static final String NUMMENORES = "NUMMENORES";
        
        public static final String PROPUESTA1_IMPORTE = "PROPUESTA1_IMPORTE";
        public static final String PROPUESTA2_IMPORTE = "PROPUESTA2_IMPORTE";
        
        public static final String ESTRABAJADOR = "ESTRABAJADOR";
        
        private DpcrSERSOPlanEmer(){
        }
    }
     
    public final class SERSOPlanEmerConcesion {
        public static final String NOMBRE_TABLA= "SERSO_PLAN_EMER_CONCESION";   
        public static final String PORMIEMBROS = "PORMIEMBROS";
        public static final String PORMENORES = "PORMENORES";
        public static final String TOTALMES = "TOTALMES";
        public static final String PROPUESTA11 = "PROPUESTA11";
        public static final String PROPUESTA12 = "PROPUESTA12";
        public static final String PROPUESTA13 = "PROPUESTA13";
        public static final String TOTAL11 = "TOTAL11";
        public static final String TOTAL12 = "TOTAL12";
        public static final String TOTALCONCEDIDO = "TOTALCONCEDIDO";
        public static final String TOTALSEMESTRE1 = "TOTALSEMESTRE1";
        public static final String MAXIMOSEMESTRE = "MAXIMOSEMESTRE";
        public static final String NUMAYUDA1 = "NUMAYUDA1";
        public static final String NUMEXC1 = "NUMEXC1";
    
        public static final String PROPUESTA21 = "PROPUESTA21";
        public static final String PROPUESTA22 = "PROPUESTA22";
        public static final String PROPUESTA23 = "PROPUESTA23";
        public static final String TOTAL21 = "TOTAL21";
        public static final String TOTAL22 = "TOTAL22";
        public static final String PORMIEMBROS2 = "PORMIEMBROS2";
        public static final String PORMENORES2 = "PORMENORES2";
        public static final String TOTALMES2 = "TOTALMES2";
        public static final String TOTALSEMESTRE2 = "TOTALSEMESTRE2";
        public static final String TOTALCONCEDIDO2 = "TOTALCONCEDIDO2";
        public static final String MAXIMOSEMESTRE2 = "MAXIMOSEMESTRE2";
        public static final String NUMAYUDA2 = "NUMAYUDA2";
        public static final String NUMEXC2 = "NUMEXC2";
    
        public static final String PORMIEMBROS3 = "PORMIEMBROS3";
        public static final String PORMENORES3 = "PORMENORES3";
        public static final String TOTALMES3 = "TOTALMES3";
        public static final String PROPUESTA31 = "PROPUESTA31";
        public static final String PROPUESTA32 = "PROPUESTA32";
        public static final String PROPUESTA33 = "PROPUESTA33";
        public static final String TOTAL31 = "TOTAL31";
        public static final String TOTAL32 = "TOTAL32";
        public static final String TOTALSEMESTRE3 = "TOTALSEMESTRE3";
        public static final String TOTALCONCEDIDO3 = "TOTALCONCEDIDO3";
        public static final String MAXIMOSEMESTRE3 = "MAXIMOSEMESTRE3";
        public static final String NUMAYUDA3 = "NUMAYUDA3";
        public static final String NUMEXC3 = "NUMEXC3";
    
        public static final String PORMIEMBROS4 = "PORMIEMBROS4";
        public static final String PORMENORES4 = "PORMENORES4";
        public static final String TOTALMES4 = "TOTALMES4";
        public static final String PROPUESTA41 = "PROPUESTA41";
        public static final String PROPUESTA42 = "PROPUESTA42";
        public static final String PROPUESTA43 = "PROPUESTA43";
        public static final String TOTAL41 = "TOTAL41";
        public static final String TOTAL42 = "TOTAL42";
        public static final String TOTALSEMESTRE4 = "TOTALSEMESTRE4";
        public static final String TOTALCONCEDIDO4 = "TOTALCONCEDIDO4";
        public static final String MAXIMOSEMESTRE4 = "MAXIMOSEMESTRE4";
        public static final String NUMAYUDA4 = "NUMAYUDA4";
        public static final String NUMEXC4 = "NUMEXC4";
    
        public static final String TOTALTOTAL = "TOTALTOTAL";
        
        private SERSOPlanEmerConcesion(){
        }
    }
    
    public final class DpcrSERSONVales{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_N_VALES";
        
        public static final String NUMEXP = "NUMEXP";
        
        public static final String SEMESTRE1 = "SEMESTRE1";
        public static final String SEMESTRE2 = "SEMESTRE2";
        public static final String SEMESTRE3 = "SEMESTRE3";
        public static final String SEMESTRE4 = "SEMESTRE4";
                
        public static final String SEMESTRE1IMPRESOS = "SEMESTRE1IMPRESOS";
        public static final String SEMESTRE2IMPRESOS = "SEMESTRE2IMPRESOS";
        public static final String SEMESTRE3IMPRESOS = "SEMESTRE3IMPRESOS";
        public static final String SEMESTRE4IMPRESOS = "SEMESTRE4IMPRESOS";
        public static final String MAXSEMESTRE1 = "MAXSEMESTRE1";
        public static final String MAXSEMESTRE2 = "MAXSEMESTRE2";
        public static final String MAXSEMESTRE3 = "MAXSEMESTRE3";
        public static final String MAXSEMESTRE4 = "MAXSEMESTRE4";

        private DpcrSERSONVales(){
        }
    }

    public final class DpcrSERSOPeCantAcum{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_PE_CANT_ACUM";
        
        public static final String PRIMERTRIMESTRE = "PRIMERTRIMESTRE";
        public static final String SEGUNDOTRIMESTRE = "SEGUNDOTRIMESTRE";
        public static final String TERCERTRIMESTRE = "TERCERTRIMESTRE";
        public static final String CUARTOTRIMESTRE = "CUARTOTRIMESTRE";
            
        public static final String TOTALSEMESTRE1 = "TOTALSEMESTRE1";
        public static final String TOTALSEMESTRE2 = "TOTALSEMESTRE2";
        public static final String TOTALSEMESTRE3 = "TOTALSEMESTRE3";
        public static final String TOTALSEMESTRE4 = "TOTALSEMESTRE4";
        
        public static final String TOTALCONCEDIDO = "TOTALCONCEDIDO";
        public static final String TOTALCONCEDIDO2 = "TOTALCONCEDIDO2";
        public static final String TOTALCONCEDIDO3 = "TOTALCONCEDIDO3";
        public static final String TOTALCONCEDIDO4 = "TOTALCONCEDIDO4";
        
        public static final String TOTAL = "TOTAL";
        
        public static final String TRIMESTRAL = "TRIMESTRAL";
        
        private DpcrSERSOPeCantAcum(){
        }
    }
    
    public final class DpcrSERSOChequesGenerados{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_CHEQUES_GENERADOS";
        public static final String NUMEXP = "NUMEXP";
        public static final String CODIGO = "CODIGO";
        
        private DpcrSERSOChequesGenerados(){
        }
    }
    
    public final class DpcrSERSOTrabSocial{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_TRAB_SOCIAL";
        public static final String DNI = "DNI";
        public static final String EMAIL = "EMAIL";
        
        private DpcrSERSOTrabSocial(){
        }
    }
    
    public final class DpcrSERSOPeConcesionExce{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_PE_CONCESION_EXCE";
        public static final String NFACTURA = "NFACTURA";
        public static final String FECHAFACTURA = "FECHAFACTURA";
        public static final String CONCEPTO = "CONCEPTO";
        public static final String PROVEEDOR = "PROVEEDOR";
        
        private DpcrSERSOPeConcesionExce(){
        }
    }
    
    public final class DpcrSERSOLibrosComedor{
        public static final String NOMBRE_TABLA = "DPCR_SERSO_LIBROS_COMEDOR";
        
        public static final String TOTALLIBROS = "TOTALLIBROS";
        public static final String PAGADO = "PAGADO";
        public static final String CURSO = "CURSO";
        public static final String COSTELIBROS = "COSTELIBROS";
        
        public static final String EMPRESACOMEDOR1 = "EMPRESACOMEDOR1";
        public static final String NOMBRE1 = "NOMBRE1";
        public static final String EMPRESACOMEDOR2 = "EMPRESACOMEDOR2";
        public static final String NOMBRE2 = "NOMBRE2";
        public static final String MESINICIO = "MESINICIO";
        public static final String MESESSOLCOMEDOR = "MESESSOLCOMEDOR";
        public static final String IMPORTETOTALCOMEDOR = "IMPORTETOTALCOMEDOR";
        
        private DpcrSERSOLibrosComedor(){
        }
    }
    
    public final class DpcrSERSOAvisos{

        public static final String INDEXOF_1_SOL = "UNA SOL";
        public static final String TEXTOASUNTO_1_SOL = " - AVISO EL BENEFICIARIO YA TIENE UNA SOLICITUD.";
        
        public static final String TEXTOASUNTO_2_SOL = " - AVISO MÁS DE 2 SOLICITUDES EN UN TRIMESTRE";
        
        public static final String INDEXOF_6_SOL = "6 SOL";
        public static final String TEXTOASUNTO_6_SOL = " - AVISO EL BENEFICIARIO YA TIENE 6 SOLICITUDES.";
        
        public static final String INDEXOF_30_DIAS = "30 DÍAS";
        public static final String TEXTOASUNTO_30_DIAS = " - AVISO 2 SOLICITUDES EN MENOS DE 30 DÍAS";
        
        public static final String INDEXOF_MAXIMO_FAMILIAR_ANUAL = "MÁXIMO FAMILIAR ANUAL";
        public static final String TEXTOASUNTO_MAXIMO_FAMILIAR_ANUAL = " - AVISO EL BENEFICIARIO HA SUPERADO EL MÁXIMO FAMILIAR ANUAL.";
        
        public static final String TEXTOASUNTO_PIDEN_0_EUROS = " - AVISO. Se han solicitado 0 euros.";
        public static final String TEXTOASUNTO_LIMITE_FAMILIAR = " - AVISO. Se ha sobrepasado el límite familiar.";
        public static final String TEXTOASUNTO_LIMITE_MUNICIPIO = " - AVISO. Se ha sobrepasado el límite para el municipio.";
        public static final String TEXTOASUNTO_LIMITE_TRIMESTRAL = " - AVISO. Se ha sobrepasado el límite trimestral.";        
        public static final String TEXTOASUNTO_LIMITE_CONJUNTO_LIBROS_COMEDOR = " - AVISO. Se ha sobrepasado el límite conjunto de LIBROS-COMEDOR para el municipio.";
        public static final String TEXTOASUNTO_AVISO_ERROR_COMPROBACION = " - AVISO. No se ha podido comprobar si se supera el máximo de ambas convocatorias conjuntas, revise que estén indicadas en el mantenimiento de trabajadores sociales.";
        
        private DpcrSERSOAvisos(){
        }
    }

    private ConstantesPlanEmergencia(){
    }
}
