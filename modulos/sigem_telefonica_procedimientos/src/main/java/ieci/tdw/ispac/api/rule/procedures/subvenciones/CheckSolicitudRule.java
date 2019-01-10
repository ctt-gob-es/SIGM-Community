package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class CheckSolicitudRule implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        boolean ok = true;
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Obtención de la solicitud antes y después de haber sido guardada
            IItem solicitudNew = rulectx.getItem();
            IItem solicitudOld = null; 
            IItemCollection coll = entitiesAPI.getEntities("SUBV_SOLICITUD", rulectx.getNumExp());
            Iterator<?> it = coll.iterator();
            
            if (!it.hasNext()) {
                rulectx.setInfoMessage("No se ha encontrado el número de expediente de la Convocatoria de Subvenciones");
                ok = false;
                
            } else {
                solicitudOld = (IItem)it.next();

                //Comprobación del número de expediente de la convocatoria
                //--------------------------------------------------------
                String strNumexpConv = solicitudNew.getString("NUMEXP_CONV");
                coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", strNumexpConv);
                it = coll.iterator();
                
                if (!it.hasNext()) {
                    rulectx.setInfoMessage("El número de expediente '" + strNumexpConv + "' no se corresponde con una Convocatoria de Subvenciones válida");
                    ok = false;
                }
                
                //Comprobación del estado de la solicitud
                //---------------------------------------
                
                //Obtención de datos
                String strPresentadaOld = solicitudOld.getString("PRESENTADA");
                if (strPresentadaOld==null){
                    strPresentadaOld="NO"; 
                }
                
                String strPresentadaNew = solicitudNew.getString("PRESENTADA");
                if (strPresentadaNew==null){
                    strPresentadaNew="NO";
                }
                
                String strSubsanadaOld = solicitudOld.getString("SUBSANADA");
                if (strSubsanadaOld==null){
                    strSubsanadaOld="NO";
                }
                
                String strSubsanadaNew = solicitudNew.getString("SUBSANADA");
                if (strSubsanadaNew==null){
                    strSubsanadaNew="NO";
                }
                
                String strSeleccionadaOld = solicitudOld.getString("SELECCIONADA");
                if (strSeleccionadaOld==null){
                    strSeleccionadaOld="NO";
                }
                
                String strSeleccionadaNew = solicitudNew.getString("SELECCIONADA");
                if (strSeleccionadaNew==null){
                    strSeleccionadaNew="NO";
                }
                
                String strJustificadaOld = solicitudOld.getString("JUSTIFICADA");
                if (strJustificadaOld==null){
                    strJustificadaOld="NO";
                }
                
                String strJustificadaNew = solicitudNew.getString("JUSTIFICADA");
                if (strJustificadaNew==null){
                    strJustificadaNew="NO";
                }
                
                boolean isPresentadaOld = strPresentadaOld.compareTo("SI") == 0;
                boolean isPresentadaNew = strPresentadaNew.compareTo("SI") == 0;
                boolean isSubsanadaOld = strSubsanadaOld.compareTo("SI") == 0;
                boolean isSubsanadaNew = strSubsanadaNew.compareTo("SI") == 0;
                boolean isSeleccionadaOld = strSeleccionadaOld.compareTo("SI") == 0;
                boolean isSeleccionadaNew = strSeleccionadaNew.compareTo("SI") == 0;
                boolean isJustificadaOld = strJustificadaOld.compareTo("SI") == 0;
                boolean isJustificadaNew = strJustificadaNew.compareTo("SI") == 0;
                String strDefectos = solicitudOld.getString("DEFECTOS");
                boolean tieneDefectos = strDefectos != null && strDefectos.length() > 0 ;
                
                //Cambios de estado incorrectos
                if ( isPresentadaOld != isPresentadaNew && (isSubsanadaOld || isSeleccionadaOld) ) {
                    rulectx.setInfoMessage("Ya no se puede cambiar el valor de 'Presentada'");
                    ok = false;
                    
                } else if ( isSubsanadaOld != isSubsanadaNew && isSeleccionadaOld ) {
                    rulectx.setInfoMessage("Ya no se puede cambiar el valor de 'Subsanada'");
                    ok = false;
                    
                } else if ( isSeleccionadaOld != isSeleccionadaNew && isJustificadaOld ) {
                    rulectx.setInfoMessage("Ya no se puede cambiar el valor de 'Seleccionada'");
                    ok = false;
                    
                } else if ( !isSubsanadaOld && isSubsanadaNew && !isPresentadaOld ) {
                    rulectx.setInfoMessage("No se puede dar por subsanada una solicitud no presentada");
                    ok = false;
                    
                } else if ( !isSeleccionadaOld && isSeleccionadaNew && !isPresentadaOld ) {
                    rulectx.setInfoMessage("No se puede dar por seleccionada una solicitud no presentada");
                    ok = false;
                    
                } else if ( !isSeleccionadaOld && isSeleccionadaNew && !isSubsanadaOld && tieneDefectos ) {
                    rulectx.setInfoMessage("No se puede dar por seleccionada una solicitud con defectos no subsanados");
                    ok = false;
                    
                } else if ( !isJustificadaOld && isJustificadaNew && !isSeleccionadaOld ) {
                    rulectx.setInfoMessage("No se puede dar por justificada una solicitud no seleccionada");
                    ok = false;
                    
                } else if ( !isPresentadaOld && isPresentadaNew && !dentroDelPlazo(rulectx, strNumexpConv) ) {
                    rulectx.setInfoMessage("No se puede dar por presentada una solicitud fuera de plazo. (Ver \"Fecha de Registro\" del expediente de solicitud)");
                    ok = false;
                }
            }
            
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido validar el cambio en la solicitud",e);
        }
        
        return ok;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        boolean ok = true;
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Obtención de la solicitud antes y después de haber sido guardada
            IItem solicitudNew = rulectx.getItem();
            IItem solicitudOld = null; 
            IItemCollection coll = entitiesAPI.getEntities("SUBV_SOLICITUD", rulectx.getNumExp());
            solicitudOld = (IItem)(coll.iterator().next());
        
            //Se guarda en la solicitud una copia del título de la convocatoria
            String strNumexpConvNew = solicitudNew.getString("NUMEXP_CONV");
            coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", strNumexpConvNew);
            IItem convocatoria = (IItem)coll.iterator().next();
            String strTitulo = convocatoria.getString("TITULO");
            solicitudNew.set("TITULO", strTitulo);
            
            //Se convierte la fecha de pago a string para poder usarla
            //cómodamente desde las plantillas de reintegro
            Date fecha = solicitudNew.getDate("FECHA_PAGO");
            
            if (fecha!=null) {
                SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
                String strFecha = dateformat.format(fecha);
                solicitudNew.set("FECHA_PAGO_STR", strFecha);
                
            } else {
                solicitudNew.set("FECHA_PAGO_STR", "");
            }
            
            //Se relacionan los expedientes de convocatoria y solicitud
            String strNumexpConvOld = solicitudOld.getString("NUMEXP_CONV");
            String strQuery = "WHERE NUMEXP_PADRE = '" + strNumexpConvOld + "'" + " AND NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
            coll = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
            Iterator<?> it = coll.iterator();
            
            if (!it.hasNext()) {
                //Añadir la relación
                int entityId = CommonFunctions.getEntityId(rulectx, "SPAC_EXP_RELACIONADOS");
                IItem relacion = entitiesAPI.createEntity(entityId);
                relacion.set("NUMEXP_PADRE", strNumexpConvNew);
                relacion.set("NUMEXP_HIJO", rulectx.getNumExp());
                relacion.set("RELACION", "Solicitud/Convocatoria");
                relacion.store(cct);
                
            } else {
                //Modificar la relación
                IItem relacion = (IItem)it.next();
                relacion.set("NUMEXP_PADRE", strNumexpConvNew);
                relacion.store(cct);
            }
            
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido comprobar la solicitud",e);
        }

        return Boolean.valueOf(ok);
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
    private boolean dentroDelPlazo(IRuleContext rulectx, String numExpConv) throws ISPACRuleException{
        boolean dentroPlazo = true;
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            IItemCollection coll = entitiesAPI.getEntities("TSOL_FECHAS", numExpConv);
            Iterator<?> it = coll.iterator();
            
            if (it.hasNext()) {
                IItem fechas = (IItem)it.next();
                Date plazo = fechas.getDate("LIMITE_SOLICITUDES");
                
                IItem exp = entitiesAPI.getExpedient(rulectx.getNumExp());
                Date presentacion = exp.getDate("FREG");
                
                if (presentacion==null || presentacion.after(plazo)) {
                    dentroPlazo = false;
                }
            }
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }

        return dentroPlazo;
    }
}