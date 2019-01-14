package es.dipucr.sigem.subvenciones.convocatorias.solicitudes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.directwebremoting.util.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.ConfiguracionColumna;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.ConstantesSubvenciones;
import es.dipucr.sigem.api.rule.procedures.ParticipantesSubvencionesUtil;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class ObjetoSolictudConvocatoriaSubvencion {

    private static Logger LOGGER = Logger.getLogger(ObjetoSolictudConvocatoriaSubvencion.class);

    private String numexp = null;
    private IItem expediente = null;
    private IItem solicitud = null;
    private IItem resolucion = null;
    private ParticipantesSubvencionesUtil interesado = null;

    public ObjetoSolictudConvocatoriaSubvencion(IClientContext cct, String numexp){
        try {
            IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();

            this.numexp = numexp;

            if(StringUtils.isNotEmpty(this.numexp)){
                expediente = ExpedientesUtil.getExpediente(cct, this.numexp);

                Iterator<?> solicitudIterator = entitiesAPI.getEntities(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, this.numexp).iterator();
                if(solicitudIterator.hasNext()){
                    solicitud = (IItem) solicitudIterator.next();
                } else {
                    solicitud = entitiesAPI.createEntity(ConstantesSubvenciones.DatosSolicitud.NOMBRE_TABLA, this.numexp);
                }

                Iterator<?> resolucionIterator = entitiesAPI.getEntities(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, this.numexp).iterator();
                if(resolucionIterator.hasNext()){
                    resolucion = (IItem) resolucionIterator.next();
                } else {
                    resolucion = entitiesAPI.createEntity(ConstantesSubvenciones.DatosResolucion.NOMBRE_TABLA, this.numexp);
                }
                
                interesado = null;
            }
        } catch (ISPACException e) {
            LOGGER.error("ERROR. " + e.getMessage(), e);
        }
    }

    public IItem getExpediente() {
        return expediente;
    }

    public void setExpediente(IItem expediente) {
        this.expediente = expediente;
    }

    public IItem getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(IItem solicitud) {
        this.solicitud = solicitud;
    }

    public IItem getResolucion() {
        return resolucion;
    }

    public void setResolucion(IItem resolucion) {
        this.resolucion = resolucion;
    }

    public ParticipantesSubvencionesUtil getInteresado() {
        if(null == interesado){
            interesado = new ParticipantesSubvencionesUtil(getNifCifTitular(), getBeneficiario());
            
            interesado.setRol(ParticipantesUtil._TIPO_INTERESADO);
            interesado.setTipoDireccion(ParticipantesUtil.TIPO_DIRECCION_TELEMATICA);
        }        
        return interesado;
    }

    public void setInteresado(ParticipantesSubvencionesUtil interesado) {
        this.interesado = interesado;
    }

    public String getNumexp() {
        return numexp;
    }

    public void setNumexp(String numexp) {
        this.numexp = numexp;
    }

    public String getBeneficiario() {        
        return getCampoExpediente( ExpedientesUtil.IDENTIDADTITULAR);
    }

    public String getNifCifTitular() {
        return getCampoExpediente( ExpedientesUtil.NIFCIFTITULAR);
    }

    public String getCiudadTitular() {
        return getCampoExpediente( ExpedientesUtil.CIUDAD);
    }

    public String getNREG() {
        return getCampoExpediente( ExpedientesUtil.NREG);
    }

    public String getFechaSolicitud() {
        return getCampoExpediente( ExpedientesUtil.FREG, ConstantesString.FORMATO_FECHA_DD_MM_YYYY);
    }

    public double getImporte1(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE);
    }

    public double getImporte2(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE2);
    }

    public double getImporte3(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.IMPORTE3);
    }

    public double getPuntos1() {
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO1);
    }

    public double getPuntos2() {
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.PUNTOSPROYECTO2);
    }

    public String getCampoExpediente(String campo) {
        return getCampoExpediente(campo, "");
    }
    
    public double getDevolucion(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION);
    }
    
    public double getDevolucion2(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION2);
    }

    public double getDevolucion3(){
        return SubvencionesUtils.getDouble(resolucion, ConstantesSubvenciones.DatosResolucion.DEVOLUCION3);
    }


    public String getCampoExpediente(String campo, String formato) {

        ConfiguracionColumna columna = new ConfiguracionColumna();
        columna.setNombreColumna(campo);
        columna.setFormatter(formato);

        return getCampo(expediente, columna);
    }

    public String getCampoSolicitud(String campo) {
        return getCampoSolicitud(campo, "");
    }

    public String getCampoSolicitud(String campo, String formato) {

        ConfiguracionColumna columna = new ConfiguracionColumna();
        columna.setNombreColumna(campo);
        columna.setFormatter(formato);

        return getCampo(solicitud, columna);
    }

    public String getCampoResolucion(String campo) {
        return getCampoResolucion(campo, "");
    }

    public String getCampoResolucion(String campo, String formato) {

        ConfiguracionColumna columna = new ConfiguracionColumna();
        columna.setNombreColumna(campo);
        columna.setFormatter(formato);        

        return getCampo(resolucion, columna);
    }

    public String getCampo(ConfiguracionColumna atributo) {
        String valor = "";

        if("EXP".equalsIgnoreCase(atributo.getTablaDato())){
            valor = getCampo(expediente, atributo);

        } else if("SOL".equalsIgnoreCase(atributo.getTablaDato())){
            valor = getCampo(solicitud,atributo);

        } else if("RESOL".equalsIgnoreCase(atributo.getTablaDato())){
            valor = getCampo(resolucion, atributo);

        } else {
            valor = "";
        }

        return valor;
    }

    public String getCampo(IItem entidad, ConfiguracionColumna atributo) {
        String valor = "";

        int tipoDato = SubvencionesUtils.getTipoDato(entidad, atributo.getNombreColumna());

        if( SubvencionesUtils.esShort(tipoDato)){
            valor = "" + SubvencionesUtils.getShortAsInt(entidad, atributo.getNombreColumna());

        } else if (SubvencionesUtils.esInt(tipoDato)){
            valor = "" + SubvencionesUtils.getInt(entidad, atributo.getNombreColumna());

        } else if (SubvencionesUtils.esDouble(tipoDato)){
            valor = SubvencionesUtils.getFormattedDouble(entidad, atributo.getNombreColumna(), atributo.getFormatter());

        } else if (SubvencionesUtils.esFloat(tipoDato)){
            valor = SubvencionesUtils.getFormattedFloat(entidad, atributo.getNombreColumna(), atributo.getFormatter());

        } else if (SubvencionesUtils.esString(tipoDato)){
            valor = SubvencionesUtils.getString(entidad, atributo.getNombreColumna());

        } else if (SubvencionesUtils.esFecha(tipoDato)){
            valor = SubvencionesUtils.getFormattedFecha(entidad, atributo.getNombreColumna(), atributo.getFormatter());

        } else {
            valor = SubvencionesUtils.getString(entidad, atributo.getNombreColumna());
        }

        return valor;
    }

    public boolean esAprobado(double importe, boolean esRechazado, boolean esRenuncia) {
        return 0 < importe && !esRechazado && !esRenuncia;
    }

    public boolean esAprobadoGrupo1() {
        return esAprobado(getImporte1(), esRechazadoGrupo1(), esRenunciaGrupo1());
    }

    public boolean esAprobadoGrupo2() {
        return esAprobado(getImporte2(), esRechazadoGrupo2(), esRenunciaGrupo2());
    }

    public boolean esAprobadoGrupo3() {
        return esAprobado(getImporte3(), esRechazadoGrupo3(), esRenunciaGrupo3());
    }

    public boolean esRechazado(String campoRechazo) {
        return StringUtils.isNotEmpty(getCampoResolucion(campoRechazo));
    }

    public boolean esRechazadoGrupo1() {
        return esRechazado(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO);
    }

    public boolean esRechazadoGrupo2() {
        return esRechazado(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO2);
    }
    public boolean esRechazadoGrupo3() {
        return StringUtils.isEmpty(getCampoResolucion(ConstantesSubvenciones.DatosResolucion.MOTIVO_RECHAZO3));
    }

    public boolean esRenuncia( String campoRenuncia) {
        return StringUtils.isNotEmpty(getCampoResolucion(campoRenuncia));
    }
    public boolean esRenunciaGrupo1() {
        return esRenuncia(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA1);
    }
    public boolean esRenunciaGrupo2() {
        return esRenuncia(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA2);
    }
    public boolean esRenunciaGrupo3() {
        return esRenuncia(ConstantesSubvenciones.DatosResolucion.MOTIVO_RENUNCIA3);
    }

    public boolean esAprobado(int grupo) {
        switch (grupo){
            case 1:
                return esAprobadoGrupo1();
            case 2:
                return esAprobadoGrupo2();
            case 3:
                return esAprobadoGrupo3();
            default:
                return false;
        }
    }

    public boolean esRechazado(int grupo) {
        switch (grupo){
        case 1:
            return esRechazadoGrupo1();
        case 2:
            return esRechazadoGrupo2();
        case 3:
            return esRechazadoGrupo3();
        default:
            return false;
        }
    }

    public boolean esRenuncia(int grupo) {
        switch (grupo){
        case 1:
            return esRenunciaGrupo1();
        case 2:
            return esRenunciaGrupo2();
        case 3:
            return esRenunciaGrupo3();
        default:
            return false;
        }
    }

    public void insertaParticipante(IClientContext cct, String numexpConvocatoria) {
        SubvencionesUtils.insertaParticipante(cct, numexpConvocatoria, getInteresado());
    }
    
    /**
     * Comrpueba si exsite el importe y si no hay renuncia/rechazo para ese proyecto/grupo
     * @param tipoImporte
     * @param grupo
     * @return
     * @throws ISPACException
     */
    public boolean hayImporteCorrecto(String tipoImporte, int grupo) throws ISPACException{

        return !esRenuncia(grupo) && !esRechazado(grupo) && !StringUtils.isEmpty(resolucion.getString(tipoImporte));
    }
    
    /**
     * Devuelve el importe si no hay renuncia/rechazo para ese proyecto/grupo
     * Si el importe es vacío o hay renuncia/rechazo para ese proyecto/grupo, devuelve 0
     * @param tipoImporte
     * @param grupo
     * @return
     * @throws ISPACException
     */
    public double getImporteCorrecto(String tipoImporte, int grupo) throws ISPACException{
        
        double importe = 0.0;
        if (hayImporteCorrecto(tipoImporte, grupo)){
             importe = SubvencionesUtils.getDouble(resolucion, tipoImporte);
        }
        return importe;
    }
}
