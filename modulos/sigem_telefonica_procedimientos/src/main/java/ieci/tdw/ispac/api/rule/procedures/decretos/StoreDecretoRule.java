package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * 
 * @author diezp
 * @proposito Almacena los valores de la firmas, al terminar el trámite Preparación de las firmas
 * 
 */
public class StoreDecretoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(SetAdmStatusDFRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            //APIs
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            
            // Consulta que obtienen los documentos del trámite actual
            String sqlDocQuery = "ID_TRAMITE = "+rulectx.getTaskId();
            IItemCollection tasksDocuments = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlDocQuery, "");
            
            // Sólo si hay un documento (decreto) adjunto
            if (tasksDocuments!=null && tasksDocuments.toList().size()==1){
                IItem doc = (IItem)tasksDocuments.iterator().next();
                int docId = doc.getInt("ID");

                String sqlQuery = "WHERE ID_DOCUMENTO = " + docId + " ORDER BY ID_PASO ASC";
                IItemCollection ctosFirmaCollection = entitiesAPI.queryEntities("SPAC_CTOS_FIRMA", sqlQuery);
                
                // Comprobar que hay circuito de firmas empezado, y que son dos pasos (presidente y fedatario)
                if(ctosFirmaCollection.next() && ctosFirmaCollection.toList().size()==2){
                    // Datos firmas
                    String presidente = null;
                    Date fechaFirmaPresidente = null;
                    String fedatario = null;
                    Date fechaFirmaFedatario = null;
                    int anio = 0;
                    int numDecreto = 0;
                    
                    IItem ctoFirma1 = (IItem)ctosFirmaCollection.toList().get(0);
                    IItem ctoFirma2 = (IItem)ctosFirmaCollection.toList().get(1);

                    // Si ha firmado el presidente - nombre, fecha firma y estado administrativo = "Firmado por el Presidente"
                    //  + Número de decreto, código cotejo, año, fecha de creación (fecha de firma del decreto)
                    // Firma del presidente - nombre, fecha firma y estado administrativo = "Firmado por el Presidente"
                    if(ctoFirma1!=null && ctoFirma1.getInt("ESTADO")==2){
                        presidente = ctoFirma1.getString("NOMBRE_FIRMANTE");
                        fechaFirmaPresidente = ctoFirma1.getDate("FECHA");
                        
                        // Firma del presidente - estado administrativo = "Firmado por el Presidente"
                        IItem item = entitiesAPI.getExpedient(rulectx.getNumExp());
                        item.set("ESTADOADM","FP");
                        item.store(rulectx.getClientContext()); 
                    }
                    
                    // Firma del fedatario - nombre, fecha firma y estado administrativo = "Firmado por el Fedatario"
                    if(ctoFirma2!=null && ctoFirma2.getInt("ESTADO")==2){
                        fedatario = ctoFirma2.getString("NOMBRE_FIRMANTE");
                        fechaFirmaFedatario = ctoFirma2.getDate("FECHA");
                        
                        // Firma del fedatario - estado administrativo = "Firmado por el Fedatario"
                        IItem item = ExpedientesUtil.getExpediente(rulectx.getClientContext(), rulectx.getNumExp());
                        LOGGER.warn("item.getString(ESTADOADM) "+item.getString("ESTADOADM"));
                        
                        if(!ExpedientesUtil.EstadoADM.RC.equals(item.getString(ExpedientesUtil.ESTADOADM))){
                            item.set(ExpedientesUtil.ESTADOADM, ExpedientesUtil.EstadoADM.DF);
                            item.store(rulectx.getClientContext());
                        }
                    }
                    
                    // Almacenar en la entidad Decreto los usuarios firmantes y las fechas de firma
                    IItemCollection decretos = entitiesAPI.getEntities("SGD_DECRETO", rulectx.getNumExp());
                    
                    // Si aún no se ha creado la entidad Decreto, aunque esto nunca debe de suceder
                    IItem decreto=null;
                    
                    if(decretos==null || decretos.toList().size()==0){
                        decreto = entitiesAPI.createEntity("SGD_DECRETO","");
                        decreto.set("NUMEXP", rulectx.getNumExp());
                    
                    } else if (decretos!=null && decretos.toList().size()==1){
                        // Sólo hay un registro para la entidad Decreto
                        decreto = (IItem)decretos.iterator().next();
                    
                    } else {
                        throw new ISPACRuleException("Error al seleccionar el registro Decreto");
                    }

                    if (presidente!=null){
                        decreto.set("NOMBRE_PRESIDENTE", presidente);
                    }
                    
                    if (fechaFirmaPresidente!=null){
                        decreto.set("FECHA_PRESIDENTE", fechaFirmaPresidente);
                        decreto.set("FECHA_DECRETO", fechaFirmaPresidente);
                        GregorianCalendar gc = new GregorianCalendar();
                        gc.setTime(fechaFirmaPresidente);
                                                    
                        // Con el año, ya podemos saber el número de decreto
                        // Sólo si no se ha introducido anteriormente número de año, introducir año + numDecreto + CodCotejo
                        if (decreto.getInt("NUMERO_DECRETO")<=0){
                            anio = gc.get(Calendar.YEAR);
                            decreto.set("ANIO", anio);
                            
                            String sqlQueryDecreto = "WHERE ANIO = "+anio+" ORDER BY NUMERO_DECRETO DESC";
                            IItemCollection decretosInAnio = entitiesAPI.queryEntities("SGD_DECRETO", sqlQueryDecreto);
                            
                            if (decretosInAnio==null || decretosInAnio.toList().size()==0){
                                numDecreto=1;
                            } else{
                                numDecreto = ((IItem)decretosInAnio.iterator().next()).getInt("NUMERO_DECRETO")+1;
                            }
                            
                            if (numDecreto>0){
                                decreto.set("NUMERO_DECRETO", numDecreto);
                            }
                            
                            if (numDecreto>=0 && fechaFirmaPresidente!=null && anio>=0){
                                DateFormat mDateFormat = new SimpleDateFormat("ddMMyyyy");
                                String codigoCotejo = anio+"/"+numDecreto+"/"+mDateFormat.format(fechaFirmaPresidente);
                                decreto.set(DocumentosUtil.COD_COTEJO, codigoCotejo);
                            }
                        }
                    }
                    
                    if (fedatario!=null){
                        decreto.set("NOMBRE_FEDATARIO", fedatario);
                    }
                    
                    if (fechaFirmaFedatario!=null){
                        decreto.set("FECHA_FEDATARIO", fechaFirmaFedatario);
                    }
                    decreto.store(rulectx.getClientContext());
                }
            }
            
            return null;
            
        } catch(ISPACRuleException e) {
            LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);
            throw new ISPACRuleException(e);
        
        } catch(Exception e){
            throw new ISPACRuleException(e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
