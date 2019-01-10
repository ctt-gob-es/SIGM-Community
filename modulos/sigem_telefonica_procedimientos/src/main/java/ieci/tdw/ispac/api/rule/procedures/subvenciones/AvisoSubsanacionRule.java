package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class AvisoSubsanacionRule implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            //Para que al ciudadano le aparezca el aviso de subsanación
            //es necesario que en infotramite haya referencia a los documentos a subsanar
            //Luego no se usan, así que genero una entrada tan sólo para que funcione.
            IItem infotram = entitiesAPI.createEntity("SPAC_INFOTRAMITE", rulectx.getNumExp());
            int taskId = rulectx.getTaskId();
            int pcdId = rulectx.getProcedureId();
            int pStageId = rulectx.getStageProcedureId();
            
            String strQuery = "WHERE ID = '" + taskId + "'";
            IItemCollection tramColl = entitiesAPI.queryEntities("SPAC_TRAMITES", strQuery);
            Iterator<?> it = tramColl.iterator();
            
            if (it.hasNext()) {
                IItem tram = (IItem)it.next();
                int taskCtId = tram.getInt("ID_CTTRAMITE");
                String nombreTram = tram.getString("NOMBRE");

                String strInfo = 
                    "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + 
                    "<documentos>" +
                    "<documento id=\"0\">" + 
                    "<pendiente>SI</pendiente>" + 
                    "<nombre>No determinado</nombre>" +
                    "</documento>"+
                    "</documentos>";
            
                infotram.set("ID_TRAMITE", Integer.valueOf(taskId));
                infotram.set("ID_CT_TRAMITE", Integer.valueOf(taskCtId));
                infotram.set("ID_PCD", Integer.valueOf(pcdId));
                infotram.set("ID_P_FASE", Integer.valueOf(pStageId));
                infotram.set("INFO", strInfo);
            
                infotram.store(cct);

                //Genero automáticamente el documento asociado para que al
                //ciudadano le aparezca en la aplicación de consulta de expedientes.
                if (nombreTram.compareTo("Aviso Subsanación") == 0) {
                    String strNombreDoc = DocumentosUtil.getNombreTipoDocByCod(cct, "Subv-AvSubs");
                    CommonFunctions.generarDocumento(rulectx, strNombreDoc, strNombreDoc, null);
                }
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido preparar el aviso de subsanación",e);
        }
        return Boolean.TRUE;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}