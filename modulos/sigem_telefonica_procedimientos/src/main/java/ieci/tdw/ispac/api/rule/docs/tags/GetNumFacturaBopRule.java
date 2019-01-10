package ieci.tdw.ispac.api.rule.docs.tags;

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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

/**
 * Asigna el número de anuncio a asociar al expediente de BOP - Solicitud de inserción de anuncio.
 * Si el año actual coincide con el año de la tabla de validación lo obtiene incrementando el último número de anuncio utilizado y lo 
 * actualiza en la tabla de validación global de contadores.
 * Si el año actual no coincide con el año de la tabla de validación (año nuevo) asigna el nuevo año en la tabla de validación, reinicia los
 * contadores y asigna el primer número de anuncio del año.
 *
 */
public class GetNumFacturaBopRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(GetNumFacturaBopRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        int longitudNumFactura = 7;
        
        IClientContext cct = null;
        
        try{
            //----------------------------------------------------------------------------------------------
            cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
    
            // Abrir transacción
            cct.beginTX();
            
            String numFactura = null;
            String numFacturaAux = null;
            String strQuery = null;
            IItemCollection collection = null;
            Iterator<?> it = null;
            IItem item = null;
            
            // Se comprueba si ya tiene número de factura el anuncio
            collection = entitiesAPI.getEntities("BOP_SOLICITUD", cct.getStateContext().getNumexp());    
            it = collection.iterator();
            if (it.hasNext()){
                item = (IItem)it.next();
                numFacturaAux = item.getString("NUM_FACTURA");
                
                if (StringUtils.isNotEmpty(numFacturaAux)) {
                    Log.warn("Número de factura ya existente. Se pone el mismo: " + numFacturaAux);
                    return numFacturaAux;
                }
            }

            //Obtenemos el valor del anyo_factura de la tabla de validación global
            strQuery = "WHERE VALOR = 'anyo_factura'";
            collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
            it = collection.iterator();
            int iAnio = -1;
            if (it.hasNext()){
                item = (IItem)it.next();
                iAnio = Integer.parseInt(item.getString("SUSTITUTO"));
            }
            
            if (iAnio > -1){
                //Lo comparamos con el año actual (si fueran distintos hay que reiniciar los contadores ya que acaba de comenzar un nuevo año)
                Calendar gc = new GregorianCalendar();
                int iAnioActual = gc.get(Calendar.YEAR);
                
                if(iAnio == iAnioActual){
                    strQuery = "WHERE VALOR = 'num_factura'";
                    collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
                    it = collection.iterator();
                    String auxNumFactura = null;
                    int iNumFactura =  -1;
                    if (it.hasNext()) {
                        item = (IItem)it.next();
                        iNumFactura = Integer.parseInt(item.getString("SUSTITUTO"))+1;
                        auxNumFactura = String.valueOf(iNumFactura);
                        
                        //Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumFactura dígitos
                        while (auxNumFactura.length() < longitudNumFactura){
                            auxNumFactura = "0"+auxNumFactura;
                        }
                        numFactura = iAnio + "-" + auxNumFactura;
                        //Actualizar el último número de factura utilizado en la tabla de validación global
                        item.set("SUSTITUTO", iNumFactura);
                        item.store(cct);
                    } if (iNumFactura > -1){
                        collection = entitiesAPI.getEntities("BOP_SOLICITUD", cct.getStateContext().getNumexp());    
                        it = collection.iterator();
                        if (it.hasNext()){
                            item = (IItem)it.next();
                            //Actualizar el número de factura en el expediente de Solicitud de anuncio
                            item.set("NUM_FACTURA", numFactura);
                            item.store(cct);
                        }
                    }
                } else {
                    //Reiniciar los contadores ya que acaba de comenzar un nuevo año y posteriormente asignar el primer número de factura
                    //    al expediente actual
                    //Asignar el valor del año nuevo en la tabla de validación
                    item.set("SUSTITUTO", iAnioActual);
                    item.store(cct);
                    
                    //Reiniciar el valor del contador num_factura en la tabla de validación
                    strQuery = "WHERE VALOR = 'num_factura'";
                    collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
                    it = collection.iterator();
                    while (it.hasNext()){
                        item = (IItem)it.next();
                        item.set("SUSTITUTO", 1);
                        item.store(cct);
                    }
                    
                    //Asignar el primer número de factura
                    //Rellenamos con 0s a la izquierda hasta completar la longitud de longitudFactura dígitos
                    String auxNumFactura = "1";
                    while (auxNumFactura.length() < longitudNumFactura){
                        auxNumFactura = "0"+auxNumFactura;
                    }
                    
                    numFactura = iAnioActual + "-" + auxNumFactura;
                    
                    collection = entitiesAPI.getEntities("BOP_SOLICITUD", cct.getStateContext().getNumexp());    
                    it = collection.iterator();
                    if (it.hasNext()){
                        item = (IItem)it.next();
                        item.set("NUM_FACTURA", numFactura);
                        item.store(cct);
                    }
                }
            }
            
            return numFactura;
            
        } catch (Exception e) {
            
            // Si se produce algún error se hace rollback de la transacción
            try {
                cct.endTX(false);
            } catch (ISPACException e1) {
                LOGGER.error("ERROR. " + e1.getMessage(), e1);
            }
            
            throw new ISPACRuleException("Error al obtener el número de anuncio de solicitud.", e);
        }     
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
