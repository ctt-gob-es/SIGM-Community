package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * 
 * @author teresa
 * Comprueba en la entidad SGN_NEGOCIADO que se haya introducido un número de propuesta o de decreto correcto.
 * Si es válido asigna el modo de acuerdo, fecha y año de propuesta o decreto a la entidad. 
 *
 */

public class CheckConvocatoriaNegRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(CheckConvocatoriaNegRule.class);
    
    protected String strAprobacion = "APROBACION";
    protected String strAdjudicacion = "ADJUDICACION";
    protected String strComision = "COMISION";
    protected String strDevolucion = "DEVOLUCION";

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        try {
            IItem conv = rulectx.getItem();
            
            //Acuerdo aprobación
            String numAcuerdoAprobacion = conv.getString("NUM_ACUERDO_APROBACION");
            String numDecretoAprobacion = conv.getString("NUM_DECRETO_APROBACION");
            
            if (numAcuerdoAprobacion!=null && numAcuerdoAprobacion.length() > 0) {
                String strRes = validarPropuesta(rulectx, numAcuerdoAprobacion);
                
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

            //Acuerdo adjudicación
            String numAcuerdoAdjudicacion = conv.getString("NUM_ACUERDO_ADJUDICACION");
            String numDecretoAdjudicacion = conv.getString("NUM_DECRETO_ADJUDICACION");
            
            if (numAcuerdoAdjudicacion!=null && numAcuerdoAdjudicacion.length() > 0) {
                String strRes = validarPropuesta(rulectx, numAcuerdoAdjudicacion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
                
            } else if (numDecretoAdjudicacion!=null && numDecretoAdjudicacion.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoAdjudicacion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }
        
            //Acuerdo comisión
            String numAcuerdoComision = conv.getString("NUM_ACUERDO_COMISION");
            String numDecretoComision = conv.getString("NUM_DECRETO_COMISION");
            
            if (numAcuerdoComision!=null && numAcuerdoComision.length() > 0) {
                String strRes = validarPropuesta(rulectx, numAcuerdoComision);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            } else if (numDecretoComision!=null && numDecretoComision.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoComision);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            }
        
            //Acuerdo devolución
            String numAcuerdoDevolucion = conv.getString("NUM_ACUERDO_DEVOLUCION");
            String numDecretoDevolucion = conv.getString("NUM_DECRETO_DEVOLUCION");
            
            if (numAcuerdoDevolucion!=null && numAcuerdoDevolucion.length() > 0) {
                String strRes = validarPropuesta(rulectx, numAcuerdoDevolucion);
                
                if (strRes != null) {
                    rulectx.setInfoMessage(strRes);
                    return false;
                }
            } else if (numDecretoDevolucion!=null && numDecretoDevolucion.length() > 0) {
                String strRes = validarDecreto(rulectx, numDecretoDevolucion);
                
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
            
            //Reviso los números de acuerdo
            
            //Acuerdo aprobación
            String numAcuerdoAprobacion = conv.getString("NUM_ACUERDO_APROBACION");
            String numDecretoAprobacion = conv.getString("NUM_DECRETO_APROBACION");
            
            if (numAcuerdoAprobacion!=null && numAcuerdoAprobacion.length() > 0) {
                tratarPropuesta(rulectx, numAcuerdoAprobacion, strAprobacion);
                
            } else if (numDecretoAprobacion!=null && numDecretoAprobacion.length() > 0) {
                tratarDecreto(rulectx, numDecretoAprobacion, strAprobacion);
            }

            //Acuerdo adjudicación
            String numAcuerdoAdjudicacion = conv.getString("NUM_ACUERDO_ADJUDICACION");
            String numDecretoAdjudicacion = conv.getString("NUM_DECRETO_ADJUDICACION");

            if (numAcuerdoAdjudicacion!=null && numAcuerdoAdjudicacion.length() > 0) {
                tratarPropuesta(rulectx, numAcuerdoAdjudicacion, strAdjudicacion);
                
            } else if (numDecretoAdjudicacion!=null && numDecretoAdjudicacion.length() > 0) {
                tratarDecreto(rulectx, numDecretoAdjudicacion, strAdjudicacion);
            }
            
            //Acuerdo comisión
            String numAcuerdoComision = conv.getString("NUM_ACUERDO_COMISION");
            String numDecretoComision = conv.getString("NUM_DECRETO_COMISION");
            
            if (numAcuerdoComision!=null && numAcuerdoComision.length() > 0) {
                tratarPropuesta(rulectx, numAcuerdoComision, strComision);
                
            } else if (numDecretoComision!=null && numDecretoComision.length() > 0) {
                tratarDecreto(rulectx, numDecretoComision, strComision);
            }
            
            //Acuerdo devolución
            String numAcuerdoDevolucion = conv.getString("NUM_ACUERDO_DEVOLUCION");
            String numDecretoDevolucion = conv.getString("NUM_DECRETO_DEVOLUCION");
            
            if (numAcuerdoDevolucion!=null && numAcuerdoDevolucion.length() > 0) {
                tratarPropuesta(rulectx, numAcuerdoDevolucion, strDevolucion);
                
            } else if (numDecretoDevolucion!=null && numDecretoDevolucion.length() > 0) {
                tratarDecreto(rulectx, numDecretoDevolucion, strDevolucion);
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido comprobar la convocatoria",e);
        }
        
        return Boolean.valueOf(ok);
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
    private String validarPropuesta(IRuleContext rulectx, String numAcuerdo) throws ISPACRuleException {
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
                LOGGER.info("No mostraba nada, solo devuelve el texto. El error es: " + e.getMessage(), e);
                return "El formato del número de acuerdo no es correcto. Se espera el formato yyyy/n (por ejemplo 2009/15)";
            }
            
            String strQuery = "WHERE YEAR = " + year + " AND NUMERO = "+numero;
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
                LOGGER.info("No mostraba nada, solo devuelve el texto. El error es: " + e.getMessage(), e);
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
    
    private void tratarPropuesta(IRuleContext rulectx, String numAcuerdo, String strTipoAcuerdo) throws ISPACRuleException {
        try {
            String strModo = null;
            String strFecha = null;
            
            if (strTipoAcuerdo.equals(strAprobacion)){
                strModo = "MODO";
                strFecha = "FECHA";
            
            } else if (strTipoAcuerdo.equals(strAdjudicacion)){
                strModo = "MODO_ADJUDICACION";
                strFecha = "FECHA_ADJUDICACION";
            
            } else if (strTipoAcuerdo.equals(strComision)){
                strModo = "MODO_COMISION";
                strFecha = "FECHA_COMISION";
            
            } else if (strTipoAcuerdo.equals(strDevolucion)){
                strModo = "MODO_DEVOLUCION";
                strFecha = "FECHA_DEVOLUCION";
            }
            
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            String year = numAcuerdo.substring(0, 4);
            String numero = numAcuerdo.substring(5);
            String strQuery = "WHERE YEAR = " + year + " AND NUMERO = "+numero;
            
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
                
            } else if (organo!=null && organo.compareTo("JGOV")==0) {
                conv.set(strModo,"Acuerdo de la Junta de Gobierno");
                
            } else {
                conv.set(strModo,"Acuerdo");
            }
            
            Date fecha = sesion.getDate("FECHA");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            conv.set(strFecha, dateformat.format(fecha));
           
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }

    private void tratarDecreto(IRuleContext rulectx, String numDecreto, String strTipoAcuerdo) throws ISPACRuleException {
        try {
            String strModo = null;
            String strFecha = null;
            
            if (strTipoAcuerdo.equals(strAprobacion)){
                strModo = "MODO";
                strFecha = "FECHA";
            
            } else if (strTipoAcuerdo.equals(strAdjudicacion)){
                strModo = "MODO_ADJUDICACION";
                strFecha = "FECHA_ADJUDICACION";
            
            } else if (strTipoAcuerdo.equals(strComision)){
                strModo = "MODO_COMISION";
                strFecha = "FECHA_COMISION";
            
            } else if (strTipoAcuerdo.equals(strDevolucion)){
                strModo = "MODO_DEVOLUCION";
                strFecha = "FECHA_DEVOLUCION";
            }

            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            String year = numDecreto.substring(0, 4);
            String numero = numDecreto.substring(5);
            String strQuery = "WHERE ANIO = " + year + " AND NUMERO_DECRETO = " + numero;
            
            IItemCollection coll = entitiesAPI.queryEntities("SGD_DECRETO", strQuery);
            Iterator<?> it = coll.iterator();
            IItem decreto = (IItem)it.next();
            
            IItem conv = rulectx.getItem();
            conv.set(strModo,"Decreto de la Presidencia");
            Date fecha = decreto.getDate("FECHA_DECRETO");
            SimpleDateFormat dateformat = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", new Locale("es"));
            conv.set(strFecha, dateformat.format(fecha));

        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }
}