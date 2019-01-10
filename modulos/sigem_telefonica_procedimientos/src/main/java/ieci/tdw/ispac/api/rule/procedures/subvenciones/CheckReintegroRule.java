package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class CheckReintegroRule implements IRule {
    
    private static Logger LOGGER = Logger.getLogger(CheckReintegroRule.class);

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

            //Obtención del reintegro antes y después de haber sido guardado
            IItem reintegroNew = rulectx.getItem();
            IItemCollection coll = entitiesAPI.getEntities("SUBV_REINTEGRO", rulectx.getNumExp());
            Iterator<?> it = coll.iterator();
            
            if (!it.hasNext()) {
                rulectx.setInfoMessage("No se ha encontrado el número de expediente del Reintegro de Subvenciones");
                ok = false;
                
            } else {
                //Comprobación del número de expediente de la solicitud
                String strNumexpSolNew = reintegroNew.getString("NUMEXP_SOL");
                coll = entitiesAPI.getEntities("SUBV_SOLICITUD", strNumexpSolNew);
                it = coll.iterator();
                
                if (!it.hasNext()) {
                    rulectx.setInfoMessage("El número de expediente '"+strNumexpSolNew+"' no se corresponde con una Solicitud de Subvenciones válida");
                    ok = false;
                }
            }
            
            //Comprobación del acuerdo/decreto de inicio
            IItem reintegro = rulectx.getItem();
            String numAcuerdoIni = reintegro.getString("NUM_ACUERDO_INI");
            String numDecretoIni = reintegro.getString("NUM_DECRETO_INI");
            
            if (numAcuerdoIni!=null && numAcuerdoIni.length() > 0) {
                String strRes = validarAcuerdo(rulectx, numAcuerdoIni);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
                
            } else if (numDecretoIni!=null && numDecretoIni.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoIni);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }

            //Comprobación del acuerdo/decreto de resolución
            String numAcuerdoFin = reintegro.getString("NUM_ACUERDO_FIN");
            String numDecretoFin = reintegro.getString("NUM_DECRETO_FIN");
            
            if (numAcuerdoFin!=null && numAcuerdoFin.length() > 0) {
                String strRes = validarAcuerdo(rulectx, numAcuerdoFin);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
                
            } else if (numDecretoFin!=null && numDecretoFin.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoFin);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }
        } catch(ISPACRuleException e){
               throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido validar el cambio en la entidad de reintegro",e);
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

            IItem reintegroNew = rulectx.getItem();

            //Solicitud
            String strNumexpSolNew = reintegroNew.getString("NUMEXP_SOL");
            IItemCollection coll = entitiesAPI.getEntities("SUBV_SOLICITUD", strNumexpSolNew);
            IItem solicitud = (IItem)coll.iterator().next();

            //Convocatoria
            String strNumexpConv = solicitud.getString("NUMEXP_CONV");
            coll = entitiesAPI.getEntities("SUBV_CONVOCATORIA", strNumexpConv);
            IItem convocatoria = (IItem)coll.iterator().next();

            //Solicitante
            coll = entitiesAPI.getEntities("SUBV_ENTIDAD", strNumexpSolNew);
            IItem entidad = (IItem)coll.iterator().next();

            //Se guarda una copia de los datos de la solicitud
            String strNombre = entidad.getString("NOMBRE");
            reintegroNew.set("NOMBRE", strNombre);
            String strTitulo = solicitud.getString("PROYECTO");
            reintegroNew.set("TITULO", strTitulo);
            String strArea = convocatoria.getString("AREA");
            reintegroNew.set("AREA", strArea);
            
            //Se relacionan los expedientes de reintegro y solicitud
            coll = entitiesAPI.getEntities("SUBV_REINTEGRO", rulectx.getNumExp());
            IItem reintegroOld = (IItem)(coll.iterator().next());
            String strNumexpSolOld = reintegroOld.getString("NUMEXP_SOL");

            if ( (strNumexpSolOld==null || strNumexpSolOld.length() == 0) && strNumexpSolNew.length() > 0 ) {
                //Añadir la relación
                int entityId = CommonFunctions.getEntityId(rulectx, "SPAC_EXP_RELACIONADOS");
                IItem relacion = entitiesAPI.createEntity(entityId);
                relacion.set("NUMEXP_PADRE", strNumexpSolNew);
                relacion.set("NUMEXP_HIJO", rulectx.getNumExp());
                relacion.set("RELACION", "Reintegro/Solicitud");
                relacion.store(cct);
                
            } else if ( strNumexpSolOld.length() > 0 && (strNumexpSolNew==null || strNumexpSolNew.length() == 0) ) {
                //Eliminar la relación
                String strQuery = "WHERE NUMEXP_PADRE = '" + strNumexpSolOld + "'" + " AND NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
                entitiesAPI.deleteEntities("SPAC_EXP_RELACIONADOS", strQuery);
                
            } else if ( strNumexpSolOld.compareTo(strNumexpSolNew) != 0) {
                //Modificar la relación
                String strQuery = "WHERE NUMEXP_PADRE = '" + strNumexpSolOld + "'" + " AND NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
                coll = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
                Iterator<?> it = coll.iterator();
                
                if (it.hasNext()) {
                    IItem relacion = (IItem)it.next();
                    relacion.set("NUMEXP_PADRE", strNumexpSolNew);
                    relacion.store(cct);
                }
            }
            
            //Reviso los números de acuerdo
            String numAcuerdoIni = reintegroNew.getString("NUM_ACUERDO_INI");
            String numDecretoIni = reintegroNew.getString("NUM_DECRETO_INI");
            
            if (numAcuerdoIni!=null && numAcuerdoIni.length() > 0) {
                tratarAcuerdo(rulectx, numAcuerdoIni, true);
                
            } else if (numDecretoIni!=null && numDecretoIni.length() > 0) {
                tratarDecreto(rulectx, numDecretoIni, true);
            }

            String numAcuerdoFin = reintegroNew.getString("NUM_ACUERDO_FIN");
            String numDecretoFin = reintegroNew.getString("NUM_DECRETO_FIN");
            
            if (numAcuerdoFin!=null && numAcuerdoFin.length() > 0) {
                tratarAcuerdo(rulectx, numAcuerdoFin, false);
                
            } else if (numDecretoFin!=null && numDecretoFin.length() > 0) {
                tratarDecreto(rulectx, numDecretoFin, false);
            }
            
        } catch(ISPACRuleException e) {
               throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido comprobar la solicitud",e);
        }
        
        return Boolean.valueOf(ok);
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
    private String validarAcuerdo(IRuleContext rulectx, String numAcuerdo) throws ISPACRuleException {
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            if (numAcuerdo.length() < 6) {
                return "El formato del número de acuerdo no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
            }
            
            String year = numAcuerdo.substring(0, 4);
            String numero = numAcuerdo.substring(5);
            
            try {
                Integer.parseInt(year);
                Integer.parseInt(numero);
                
            } catch(NumberFormatException e) {
                LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);
                return "El formato del número de acuerdo no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
            }
            
            String strQuery = "WHERE YEAR = " + year + " AND NUMERO = " + numero;
            IItemCollection coll = entitiesAPI.queryEntities("SECR_ACUERDO", strQuery);
            Iterator<?> it = coll.iterator();
            
            if (!it.hasNext()) {
                return "No se encuentra el acuerdo con número " + numAcuerdo;
            }
            
            IItem acuerdo = (IItem)it.next();
            String numexpSesion = acuerdo.getString("NUMEXP");
            coll = entitiesAPI.getEntities("SECR_SESION", numexpSesion);
            it = coll.iterator();
            
            if (!it.hasNext()) {
                return "No se encuentra la sesión de gobierno asociada al acuerdo " + numAcuerdo;
            }
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
        
        return null;
    }
    
    private String validarDecreto(IRuleContext rulectx, String numDecreto) throws ISPACRuleException {
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            if (numDecreto.length() < 6) {
                return "El formato del número de decreto no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
            }
            
            String year = numDecreto.substring(0, 4);
            String numero = numDecreto.substring(5);
            
            try {
                Integer.parseInt(year);
                Integer.parseInt(numero);
                
            } catch(NumberFormatException e) {
                LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);
                return "El formato del número de decreto no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
            }
            
            String strQuery = "WHERE ANIO = " + year + " AND NUMERO_DECRETO = " + numero;
            IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
            Iterator<?> it = coll.iterator();
            
            if (!it.hasNext()) {
                return "No se encuentra el decreto con número " + numDecreto;
            }
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
        
        return null;
    }
    
    private void tratarAcuerdo(IRuleContext rulectx, String numAcuerdo, boolean isInicio) throws ISPACRuleException {
        
        try {
            String strModo = "MODO_INI";
            String strFecha = "FECHA_INI";
            
            if (!isInicio) {
                strModo = "MODO_FIN";
            }
            if (!isInicio) {
                strFecha = "FECHA_FIN";
            }
            
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            String year = numAcuerdo.substring(0, 4);
            String numero = numAcuerdo.substring(5);
            String strQuery = "WHERE YEAR = " + year + " AND NUMERO = " + numero;
            
            IItemCollection coll = entitiesAPI.queryEntities("SECR_ACUERDO", strQuery);
            Iterator<?> it = coll.iterator();
            IItem acuerdo = (IItem)it.next();            
            String numexpSesion = acuerdo.getString("NUMEXP");
            
            coll = entitiesAPI.getEntities("SECR_SESION", numexpSesion);
            it = coll.iterator();
            IItem sesion = (IItem)it.next();
            String organo = sesion.getString("ORGANO");
            
            IItem reintegro = rulectx.getItem();
            
            if (organo!=null && organo.compareTo("PLEN")==0) {
                reintegro.set(strModo,"Acuerdo del Pleno Corporativo");
                
            } else if (organo!=null && organo.compareTo("JGOB")==0) {
                reintegro.set(strModo,"Acuerdo de la Junta de Gobierno");
                
            } else {
                reintegro.set(strModo,"Acuerdo");
            }
            
            Date fecha = sesion.getDate("FECHA");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            reintegro.set(strFecha, dateformat.format(fecha));
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }

    private void tratarDecreto(IRuleContext rulectx, String numDecreto, boolean isInicio) throws ISPACRuleException {
        
        try {
            String strModo = "MODO_INI";
            String strFecha = "FECHA_INI";
            
            if (!isInicio) {
                strModo = "MODO_FIN";
            }
            if (!isInicio) {
                strFecha = "FECHA_FIN";
            }

            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            String year = numDecreto.substring(0, 4);
            String numero = numDecreto.substring(5);
            String strQuery = "WHERE ANIO = " + year + " AND NUMERO_DECRETO = " + numero;
            IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
            Iterator<?> it = coll.iterator();
            IItem decreto = (IItem)it.next();
            
            IItem reintegro = rulectx.getItem();
            
            reintegro.set(strModo,"Decreto de la Presidencia");
            Date fecha = decreto.getDate("FECHA_DECRETO");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            reintegro.set(strFecha, dateformat.format(fecha));
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }
}