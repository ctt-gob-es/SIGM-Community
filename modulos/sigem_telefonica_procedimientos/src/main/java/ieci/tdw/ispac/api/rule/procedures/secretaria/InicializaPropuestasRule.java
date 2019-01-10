package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class InicializaPropuestasRule implements IRule {

    private static final Logger LOGGER = Logger.getLogger(InicializaPropuestasRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        boolean cierraTramite = true;
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            String numexp = rulectx.getNumExp();
            //----------------------------------------------------------------------------------------------
            
            //Compruebo si han rellenado la pestaña sesion.
            Iterator<IItem> itSesion = ConsultasGenericasUtil.queryEntities(rulectx, "SECR_SESION", "NUMEXP = '" + numexp + "'");
            if(itSesion.hasNext()){
                IItem sesion = itSesion.next();
                
                if (null != sesion){
                    if(StringUtils.isEmpty(sesion.getString("TIPO"))) {
                        rulectx.setInfoMessage("Falta por introducir en la Pestaña Sesión: Tipo");
                        return false;
                    }
                    
                    if(StringUtils.isEmpty(sesion.getString("HORA"))) {
                        rulectx.setInfoMessage("Falta por introducir en la Pestaña Sesión: Hora de celebración");
                        return false;
                    }
                    
                    if(null==sesion.getDate("FECHA")) {
                        rulectx.setInfoMessage("Falta por introducir en la Pestaña Sesión: Fecha de celebración");
                        return false;
                    }
                    
                    if(StringUtils.isEmpty(sesion.getString("NUMCONV"))) {
                        rulectx.setInfoMessage("Falta por introducir en la Pestaña Sesión: Número de convocatoria");
                        return false;
                    }
                }
            }
                        
            StringBuilder mensajePropuestasErroneas = new StringBuilder("Las propuestas que faltan por introducir el extracto son: ");
            boolean mensajeErrorPropuesta = false;
    
            //Antes de empezar se borra la lista de propuestas
            IItemCollection collAllProps = entitiesAPI.getEntities("SECR_PROPUESTA", rulectx.getNumExp());
            Iterator<?> itAllProps = collAllProps.iterator();
            LOGGER.warn("collAllProps " + collAllProps);
            LOGGER.warn("itAllProps " + itAllProps);
            IItem iProp = null;
            
            while(itAllProps.hasNext()) {
                iProp = (IItem)itAllProps.next();
                iProp.delete(cct);
            }
            
            //Obtención de la lista de expedientes relacionados
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, rulectx.getNumExp());
            
            if (expedientesRelacionados.isEmpty()) {
                LOGGER.warn("No se encuentran expedientes relacionados");
                return false;
            }
            
            for (String strExpPropuesta : expedientesRelacionados) {
                LOGGER.warn("strExpPropuesta "+strExpPropuesta);
                
                if(strExpPropuesta != null){
                    //Obtención de la propuesta
                    IItemCollection collProps = entitiesAPI.getEntities("SECR_PROPUESTA", strExpPropuesta);
                    Iterator<?> itProps = collProps.iterator();
                    
                    if (itProps.hasNext()) {                        
                        IItem iPropuesta = (IItem)itProps.next();
                        IItemCollection collExps = entitiesAPI.getEntities("SPAC_EXPEDIENTES", strExpPropuesta);
                        Iterator<?> itExps = collExps.iterator();
                        
                        if (!itExps.hasNext()) {
                            LOGGER.warn("Debe abrir el expediente que se encuentra cerrado: "+strExpPropuesta);
                            mensajePropuestasErroneas.append("Se encuentra el expediente "+strExpPropuesta+ "cerrado. Es necesario que este abierto.");
                            cierraTramite = false;
                        }
                        
                        if(iPropuesta.getString("EXTRACTO")==null){
                            LOGGER.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
                            mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
                            mensajeErrorPropuesta = true;
                            cierraTramite = false;
                        }
                        
                    } else {
                        LOGGER.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
                        mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
                        mensajeErrorPropuesta = true;
                        cierraTramite = false;
                    }
                }
            }
            
            if(mensajeErrorPropuesta) {
                rulectx.setInfoMessage(mensajePropuestasErroneas.toString());
            }
            
        } catch(ISPACRuleException e) {
            LOGGER.error("Error al cargar las propuestas."+e.getMessage(), e);
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            LOGGER.error("Error al cargar las propuestas."+e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        }

        LOGGER.warn(cierraTramite);
        return cierraTramite;
    }

    @SuppressWarnings("unchecked")
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Comprobamos si la sesión no es constitutiva
            String organo = SecretariaUtil.getOrgano(rulectx);
            String sesion = SecretariaUtil.getSesion(rulectx);
            
            if("PLEN".equals(organo) && "CONS".equals(sesion)){
                //Se debería eliminar el expediente relacionado y cerrar el expediente.
                Iterator<IItem> itColllection = ConsultasGenericasUtil.queryEntities(rulectx, "SPAC_EXP_RELACIONADOS", "NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Sesión/Propuesta'");
                
                while (itColllection.hasNext()) {
                    IItem expRelacionados = (IItem) itColllection.next();
                    String numexpPropuesta = expRelacionados.getString("NUMEXP_PADRE");
                    expRelacionados.delete(cct);
                    ExpedientesUtil.cerrarExpediente(cct, numexpPropuesta);
                }
                
            } else {
                StringBuilder mensajePropuestasErroneas = new StringBuilder("Las propuestas que faltan por introducir el extracto son: ");
        
                //Antes de empezar se borra la lista de propuestas
                IItemCollection collAllProps = entitiesAPI.getEntities("SECR_PROPUESTA", rulectx.getNumExp());
                Iterator<IItem> itAllProps = collAllProps.iterator();
                LOGGER.warn("collAllProps "+collAllProps);
                LOGGER.warn("itAllProps "+itAllProps);
                IItem iProp = null;
                
                while(itAllProps.hasNext()) {
                    iProp = (IItem)itAllProps.next();
                    iProp.delete(cct);
                }
                
                //Obtención de la lista de expedientes relacionados
                List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, rulectx.getNumExp());
                
                if (expedientesRelacionados.isEmpty()) {
                    LOGGER.warn("No se encuentran expedientes relacionados");
                    return false;
                }
                
                for (String strExpPropuesta : expedientesRelacionados) {
                    LOGGER.warn("NumExp Propuesta " + strExpPropuesta);
                    
                    if(strExpPropuesta != null){
                    
                        //Obtención de la propuesta
                        IItemCollection collProps = entitiesAPI.getEntities("SECR_PROPUESTA", strExpPropuesta);
                        Iterator<IItem> itProps = collProps.iterator();
                    
                        if (itProps.hasNext()) {                        
                            IItem iPropuesta = (IItem)itProps.next();
                            
                            //Obtención de los datos del expediente
                            IItemCollection collExps = entitiesAPI.getEntities("SPAC_EXPEDIENTES", strExpPropuesta);
                            Iterator<IItem> itExps = collExps.iterator();
                        
                            if (! itExps.hasNext()) {
                                LOGGER.warn("No se encuentra el expediente asociado a la propuesta");
                                return false;
                            }
                            
                            IItem iExpediente = (IItem)itExps.next();
            
                            //Creación de nueva propuesta en la sesión
                            IItem item = entitiesAPI.createEntity("SECR_PROPUESTA",rulectx.getNumExp());
                            item.set("NUMEXP_ORIGEN", strExpPropuesta);
                            
                            String interesado = "";
                            if(iExpediente.getString("IDENTIDADTITULAR")!=null){
                                interesado = iExpediente.getString("IDENTIDADTITULAR");
                            }
                            item.set("INTERESADO", interesado);
                            
                            String origen = "";
                            if(iPropuesta.getString("ORIGEN")!=null){
                                origen = iPropuesta.getString("ORIGEN");
                            }
                            item.set("ORIGEN", origen);
                            
                            String destino = "";
                            if(iPropuesta.getString("DESTINO")!=null){
                                destino = iPropuesta.getString("DESTINO");
                            }
                            item.set("DESTINO", destino);
                            
                            String prioridad = "";
                            if(iPropuesta.getString("PRIORIDAD")!=null){
                                prioridad = iPropuesta.getString("PRIORIDAD");
                            }
                            item.set("PRIORIDAD", prioridad);
                            
                            String prioridadMotivo = "";
                            if(iPropuesta.getString("PRIORIDAD_MOTIVO")!=null){
                                prioridadMotivo = iPropuesta.getString("PRIORIDAD_MOTIVO");
                            }
                            item.set("PRIORIDAD_MOTIVO", prioridadMotivo);
                            
                            String extracto = "";
                            if(iPropuesta.getString("EXTRACTO")!=null){
                                extracto = iPropuesta.getString("EXTRACTO");
                            }
                            item.set("EXTRACTO", extracto);
                            LOGGER.warn("extracto "+extracto);
                            
                            String dictamen = "";
                            if(iPropuesta.getString("DICTAMEN")!=null){
                                dictamen = iPropuesta.getString("DICTAMEN");
                            }
                            item.set("DICTAMEN", dictamen);
                            
                            item.store(cct);
                            
                            LOGGER.warn("inserccion hecha");
                            
                            //Actualizo el estado administrativo
                            String strEstado = SecretariaUtil.getEstadoAdmPropuesta(rulectx);
                            iExpediente.set("ESTADOADM", strEstado);
                            iExpediente.store(cct);
                            
                        } else{
                            LOGGER.warn("No se encuentra el extracto de la propuesta: "+strExpPropuesta);
                            mensajePropuestasErroneas.append(" ' "+strExpPropuesta+" ' ");
                        }
                    }
                }
                
                ordenarPropuestasPorNombreProcedimiento(cct, entitiesAPI, rulectx.getNumExp());
                
            }
            
            return true;
            
        } catch(ISPACRuleException e){
            LOGGER.error("Error al cargar las propuestas "+rulectx.getNumExp()+" ."+e.getMessage(), e);
               throw new ISPACRuleException(e);

        } catch(Exception e) {
            LOGGER.error("Error al cargar las propuestas  "+rulectx.getNumExp()+" ."+e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido inicializar la propuesta s "+rulectx.getNumExp()+" ."+e.getMessage(), e);

        }
    }

    @SuppressWarnings("unchecked")
    private void ordenarPropuestasPorNombreProcedimiento(IClientContext cct, IEntitiesAPI entitiesAPI, String numExpOrganoColegiado) throws ISPACRuleException {
        // Ordenación de las propuestas (de 10 en 10 para permitir insertar luego
        // urgencias entre medias)
        try {
            int nOrden = 10;
            DecimalFormat df = new DecimalFormat("0000");
            
            //Obtengo la primera propuesta que es aprobarcion del acta anterior
            String propuesta= "WHERE NUMEXP='"+numExpOrganoColegiado+"' and origen='0001' ORDER BY NUMEXP_ORIGEN ASC";
            IItemCollection colPropuesta =  entitiesAPI.queryEntities("SECR_PROPUESTA", propuesta);
            Iterator<IItem> itColPropu = colPropuesta.iterator();
            String numexpProAprobActaAnterior = "";
            
            if(itColPropu.hasNext()){
                IItem iPropuesta = (IItem) itColPropu.next();
                numexpProAprobActaAnterior = iPropuesta.getString("NUMEXP_ORIGEN");
                iPropuesta.set("ORDEN", df.format(nOrden));
                iPropuesta.store(cct);
                nOrden = nOrden + 10;
            }
            
            String query = "WHERE NUMEXP IN (SELECT NUMEXP_ORIGEN FROM SECR_PROPUESTA WHERE NUMEXP = '" + numExpOrganoColegiado + "'  AND NUMEXP_ORIGEN != '"+numexpProAprobActaAnterior+"') ORDER BY NOMBREPROCEDIMIENTO ASC";
            LOGGER.warn("query "+query);
            IItemCollection collAllPropsExp = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", query);
            Iterator<IItem> itAllPropsExp = collAllPropsExp.iterator();
            
            while (itAllPropsExp.hasNext()) {
                IItem iPropExp = (IItem) itAllPropsExp.next();
                String expPropuesta = "";
                
                if (iPropExp.getString("NUMEXP") != null){
                    expPropuesta = iPropExp.getString("NUMEXP");
                }
                LOGGER.warn("numexp. "+expPropuesta);
                
                if (StringUtils.isNotEmpty(expPropuesta)) {
                    String strQuery = "WHERE NUMEXP = '" + numExpOrganoColegiado + "' AND NUMEXP_ORIGEN = '" + expPropuesta + "'";
                    LOGGER.warn("strQuery "+strQuery);
                    IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
                    Iterator<IItem> itAllProps = collAllProps.iterator();                    

                    while (itAllProps.hasNext()) {
                        IItem iProp = (IItem) itAllProps.next();
                        iProp.set("ORDEN", df.format(nOrden));
                        iProp.store(cct);
                        LOGGER.warn("nOrden. "+nOrden);
                        nOrden = nOrden + 10;
                    }
                }
            }

        } catch (ISPACException e) {
            LOGGER.error("Error al cargar las propuestas  " + numExpOrganoColegiado + " ." + e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido inicializar la propuesta " + numExpOrganoColegiado + " ." + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}