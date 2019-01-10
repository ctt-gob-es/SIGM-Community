package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class CierreSesionRule implements IRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Se obtiene la lista de propuestas de la sesión
            IItemCollection collAllProps = entitiesAPI.getEntities("SECR_PROPUESTA", rulectx.getNumExp());            
            Iterator<?> itAllProps = collAllProps.iterator();
            IItem iProp = null;
            
            while(itAllProps.hasNext()) {
                iProp = (IItem)itAllProps.next();
                cierraPropuesta(rulectx, iProp);
            }

            //Ahora lo mismo pero con las urgencias
            collAllProps = entitiesAPI.getEntities("SECR_URGENCIAS", rulectx.getNumExp());
            itAllProps = collAllProps.iterator();
            iProp = null;
            
            while(itAllProps.hasNext()) {
                iProp = (IItem)itAllProps.next();
                cierraPropuesta(rulectx, iProp);
            }
            
            //Se eliminan las relaciones con esta sesión de gobierno
            String strQuery = "WHERE NUMEXP_HIJO = '" + rulectx.getNumExp() + "'";
            entitiesAPI.deleteEntities("SPAC_EXP_RELACIONADOS", strQuery);
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido cerrar la sesión",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
    private void cierraPropuesta(IRuleContext rulectx, IItem iProp) throws ISPACRuleException {
        try {
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            ITXTransaction tx = invesFlowAPI.getTransactionAPI();
    
            //Actualizamos el expediente de origen
            String numExpOrigen = iProp.getString("NUMEXP_ORIGEN");
            IItemCollection collProps = entitiesAPI.getEntities("SECR_PROPUESTA", numExpOrigen);
            Iterator<?> itProps = collProps.iterator();
            IItem iPropOrigen = null;
            
            if ( itProps.hasNext()) {
                iPropOrigen = (IItem)itProps.next();
                iPropOrigen.set("NOTAS", iProp.getString("NOTAS"));
                iPropOrigen.set("DEBATE", iProp.getString("DEBATE"));
                iPropOrigen.set("ACUERDOS", iProp.getString("ACUERDOS"));
                iPropOrigen.set("DICTAMEN", iProp.getString("DICTAMEN"));
                iPropOrigen.set("N_SI", iProp.getString("N_SI"));
                iPropOrigen.set("N_NO", iProp.getString("N_NO"));
                iPropOrigen.set("N_ABS", iProp.getString("N_ABS"));
                iPropOrigen.store(cct);
            }
            
            //Actualizamos el estado administrativo
            IItem iExpediente = ExpedientesUtil.getExpediente(cct, numExpOrigen);

            if (null != iExpediente) {
                String strOrgano = SecretariaUtil.getOrganoSesion(rulectx, null);
                
                if ( strOrgano.compareTo("COMI")==0 || strOrgano.compareTo("MESA")==0 ) {
                    //En Comisiones Informativas y Mesa de Contratación
                    //no cerramos las propuestas sino que las etiquetamos como "Dictaminada"
                    iExpediente.set(ExpedientesUtil.ESTADOADM, "SEC_DC");
                    iExpediente.store(cct);
                    
                } else {
                    //En Pleno y Junta de Gobierno etiquetamos
                    //como "Archivada" y luego cerramos el expediente
                    iExpediente.set(ExpedientesUtil.ESTADOADM, "SEC_AR");
                    iExpediente.store(cct);
                    int id = iExpediente.getInt("ID");
                    tx.closeProcess(id);
                }
            }
            
        } catch(ISPACRuleException e){
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido cerrar la propuesta. Compruebe que no quedan trámites abiertos en las propuestas.",e);
        }
    }
}