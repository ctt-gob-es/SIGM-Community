package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CheckConvocatoriaRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(CheckConvocatoriaRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        
        try {
            IItem conv = rulectx.getItem();
            String numAcuerdoAprobacion = conv.getString("NUM_ACUERDO_APROBACION");
            String numDecretoAprobacion = conv.getString("NUM_DECRETO_APROBACION");
        
            if (numAcuerdoAprobacion!=null && numAcuerdoAprobacion.length() > 0) {
                String strRes = validarAcuerdo(rulectx, numAcuerdoAprobacion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
                
            } else if (numDecretoAprobacion!=null && numDecretoAprobacion.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoAprobacion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }

            String numAcuerdoResolucion = conv.getString("NUM_ACUERDO_RESOLUCION");
            String numDecretoResolucion = conv.getString("NUM_DECRETO_RESOLUCION");
            
            if (numAcuerdoResolucion!=null && numAcuerdoResolucion.length() > 0) {
                String strRes = validarAcuerdo(rulectx, numAcuerdoResolucion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            
            } else if (numDecretoResolucion!=null && numDecretoResolucion.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoResolucion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido validar el cambio en la convocatoria",e);
        }
        
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        boolean ok = true;
        
        try {
            IItem conv = rulectx.getItem();

            //Se convierte el código de área a string para poder usarlo
            //cómodamente desde las plantillas de reintegro
            String strValor = conv.getString("AREA");
            
            if (strValor != null) {
                String strArea = "";
                IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
                String strQuery = "WHERE VALOR = '" + strValor + "'";
                IItemCollection coll = entitiesAPI.queryEntities("SECR_VLDTBL_AREAS", strQuery);
                Iterator<?> it = coll.iterator();
                
                if (it.hasNext()) {
                    IItem item = (IItem)it.next();
                    strArea = item.getString("SUSTITUTO");
                }
                
                conv.set("AREA_STR", strArea);
            }
            
            //Reviso los números de acuerdo
            String numAcuerdoAprobacion = conv.getString("NUM_ACUERDO_APROBACION");
            String numDecretoAprobacion = conv.getString("NUM_DECRETO_APROBACION");
            
            if (numAcuerdoAprobacion!=null && numAcuerdoAprobacion.length() > 0) {
                tratarAcuerdo(rulectx, numAcuerdoAprobacion, true);
                
            } else if (numDecretoAprobacion!=null && numDecretoAprobacion.length() > 0) {
                tratarDecreto(rulectx, numDecretoAprobacion, true);
            }

            String numAcuerdoResolucion = conv.getString("NUM_ACUERDO_RESOLUCION");
            String numDecretoResolucion = conv.getString("NUM_DECRETO_RESOLUCION");
            
            if (numAcuerdoResolucion!=null && numAcuerdoResolucion.length() > 0) {
                tratarAcuerdo(rulectx, numAcuerdoResolucion, false);
                
            } else if (numDecretoResolucion!=null && numDecretoResolucion.length() > 0) {
                tratarDecreto(rulectx, numDecretoResolucion, false);
            }
            
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido comprobar la convocatoria",e);
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
            
            String strQuery = "WHERE ANIO="+year+" AND NUMERO_DECRETO="+numero;
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
    
    private void tratarAcuerdo(IRuleContext rulectx, String numAcuerdo, boolean isAprobacion) throws ISPACRuleException {
        try {
            String strModo = "MODO";
            String strFecha = "FECHA";
            
            if (!isAprobacion){
                strModo = "MODO_RESOLUCION";
            }
            if (!isAprobacion){
                strFecha = "FECHA_RESOLUCION";
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
            
            IItem conv = rulectx.getItem();
            
            if (organo!=null && organo.compareTo("PLEN")==0) {
                conv.set(strModo,"Acuerdo del Pleno Corporativo");
                
            } else if (organo!=null && organo.compareTo("JGOB")==0) {
                conv.set(strModo,"Acuerdo de la Junta de Gobierno");
                
            } else {
                conv.set(strModo,"Acuerdo");
            }
            
            Date fecha = sesion.getDate("FECHA");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            conv.set(strFecha, dateformat.format(fecha));
            
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy", new Locale("es"));
            String strYear = df2.format(fecha);
            
            if (isAprobacion) {
                conv.set("ANIO", strYear);
                
            } else {
                conv.set("ANIO_RESOLUCION", strYear);
            }
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }

    private void tratarDecreto(IRuleContext rulectx, String numDecreto, boolean isAprobacion) throws ISPACRuleException {
        try {
            String strModo = "MODO";
            String strFecha = "FECHA";
            
            if (!isAprobacion) {
                strModo = "MODO_RESOLUCION";
            }
            if (!isAprobacion) {
                strFecha = "FECHA_RESOLUCION";
            }

            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            String year = numDecreto.substring(0, 4);
            String numero = numDecreto.substring(5);
            String strQuery = "WHERE ANIO="+year+" AND NUMERO_DECRETO="+numero;
            
            IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
            Iterator<?> it = coll.iterator();
            IItem decreto = (IItem)it.next();
            
            IItem conv = rulectx.getItem();
            
            conv.set(strModo,"Decreto de la Presidencia");
            Date fecha = decreto.getDate("FECHA_DECRETO");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            conv.set(strFecha, dateformat.format(fecha));
            
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy", new Locale("es"));
            String strYear = df2.format(fecha);
            
            if (isAprobacion) {
                conv.set("ANIO", strYear);
                
            } else {
                conv.set("ANIO_RESOLUCION", strYear);
            }
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }
}