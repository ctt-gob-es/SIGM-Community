package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
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

public class GenerateAnuncioBopRule implements IRule {
    
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(GenerateAnuncioBopRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        Iterator<?> it = null;
        IItemCollection collection = null;
        IItem item = null;
        String strQuery = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        Date fechaPub = null;
        Date fechaBop = null;
        
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            // Obtenemos los documentos de solicitud de anuncio con la fecha de publicación
            // en cuestión
            IItem datosAnuncio = rulectx.getItem();
            
            // Fecha del último BOP
            fechaPub = datosAnuncio.getDate("FECHA_PUBLICACION");
            LOGGER.warn("fecha: " + df.format(fechaPub));
            strQuery = "WHERE VALOR = 'fecha_ultimo_bop'";
            collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
            it = collection.iterator();
            
            if (it.hasNext()){
                item = (IItem)it.next();
                fechaBop = df2.parse(item.getString("SUSTITUTO"));
            }
            
            if (!fechaPub.after(fechaBop)) {
                datosAnuncio.set("FECHA_PUBLICACION", "");
                LOGGER.warn("La fecha del BOP debe ser superior a la del último BOP publicado.");
                throw new ISPACInfo("La fecha del BOP debe ser superior a la del último BOP publicado.");
            }
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido generar el anuncio para el BOP",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}
