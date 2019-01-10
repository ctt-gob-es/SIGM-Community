package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * 
 * @author teresa
 * @date 13/11/2009
 * @propósito Actualiza el campo estado de la entidad para mostrar el enlace de Solicitud de Anuncio del BOP, crea el documento DOC de
 * Anuncio de licitación a partir de su plantilla y lo asocia al trámite actual.
 */
public class CreateDocAnuncioNegRule implements IRule {
    
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(CreateDocAnuncioNegRule.class);

    protected String strEntidad = "SGN_NEGOCIADO";
    //protected String strQueryDocumentos = "" ;    
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
        
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Actualiza el campo estado de la entidad
            //de modo que permita mostrar los enlaces para crear Propuesta/Decreto
            String numexp = rulectx.getNumExp();
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexp);
            Iterator<?> it = col.iterator();
            
            if (it.hasNext()) {
                IItem entidad = (IItem)it.next();
                entidad.set("ESTADO", "Inicio");
                entidad.store(cct);
            }
            
            String strCodTpDoc = "Anuncio lic";
            String strTemplateName = "Anuncio de licitación";
            
            //Generación del documento a partir de la plantilla
            String strTpDocName = DocumentosUtil.getNombreTipoDocByCod(cct, strCodTpDoc);
            CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, null);
            
        } catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
