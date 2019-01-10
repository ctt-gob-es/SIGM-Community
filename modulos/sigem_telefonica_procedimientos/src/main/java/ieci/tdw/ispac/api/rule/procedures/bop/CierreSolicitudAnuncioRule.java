package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class CierreSolicitudAnuncioRule implements IRule {
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(CierreSolicitudAnuncioRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        IItemCollection collAnuncios = null;
        IItem itemAnuncio = null;
        IItem itemProceso = null;

        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            ITXTransaction txAPI = invesFlowAPI.getTransactionAPI();

            // Se obtiene la fecha de publicación
            IItemCollection bops = entitiesAPI.getEntities("BOP_PUBLICACION", rulectx.getNumExp());
            Iterator<?> it = bops.iterator();
            IItem bop = null;
            
            if(it.hasNext()) {
                bop = (IItem)it.next();
            }
            
            String fecha = bop.getString("FECHA");
            Date fechaPub = bop.getDate("FECHA"); 
            LOGGER.warn("Fecha: " + fecha);

            //Se obtiene la lista de solicitudes de anuncio para dicho BOP
            String strQuery = "WHERE fecha_publicacion = '" + df.format(fechaPub) + "'";
            LOGGER.warn("Query: " + strQuery);
            collAnuncios = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
            it = collAnuncios.iterator();
            
            //Recorremos la lista de anuncios publicados en el BOP actual
            while (it.hasNext()) {
                itemAnuncio = (IItem)it.next();
                String numexpToClose = itemAnuncio.getString("NUMEXP");
                LOGGER.warn("Número de expediente que se va a cerrar: " + numexpToClose);

                //Obtenemos el proceso
                //[eCenpri-Felipe #893] Se estaba cogiendo el id del expediente en vez del id del proceso
                //Se perdión la sincronización, al ejecutar la instrucción closeProcess se produce un error
                itemProceso = invesFlowAPI.getProcess(numexpToClose);
                int idProc = itemProceso.getInt("ID");
                
                // Cerramos los trámites abiertos del expediente
                IItemCollection collTramites = entitiesAPI.getEntities("SPAC_TRAMITES", numexpToClose);
                Iterator<?> it3 = collTramites.iterator();
                IItem itemTramite = null;

                //[eCenpri-Felipe #484] Si por error se ha creado más de un trámite, se cierran
                while (it3.hasNext()) {
                    itemTramite = (IItem)it3.next();
                    int idTram = itemTramite.getInt("ID");
                    LOGGER.warn("Cerrando el trámite con ID = " + idTram);
                    txAPI.closeTask(idTram);
                }
                
                IItemCollection fases = invesFlowAPI.getStagesProcess(idProc);
                Iterator<?> it4 = fases.iterator();
                IItem fase = null;
                
                if (it4.hasNext()) {
                    fase = (IItem)it4.next();
                    int idFase = fase.getInt("ID");
                    LOGGER.warn("Cerrando la fase con ID = " + idFase);
                    txAPI.closeStage(idFase);
                }
                
                LOGGER.warn("Cerrando el proceso de solicitud de anuncio con ID = " + idProc);
                txAPI.closeProcess(idProc);
            }

            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido cerrar la sesión",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
