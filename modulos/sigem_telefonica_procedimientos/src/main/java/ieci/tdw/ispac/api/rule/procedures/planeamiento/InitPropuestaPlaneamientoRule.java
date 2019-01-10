package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;

public class InitPropuestaPlaneamientoRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(InitPropuestaPlaneamientoRule.class);

    protected String strEntidad = "";
    protected String strExtracto = "";

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
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------

            //Obtiene el expediente de la entidad
            String numexpProp = rulectx.getNumExp();    
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, numexpProp);
            
            if (expedientesRelacionados.isEmpty()) {
                return Boolean.FALSE;
            }
            
            String numexpEnt = expedientesRelacionados.get(0);
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexpEnt);
            Iterator<?> it = col.iterator();
            
            if (!it.hasNext()) {
                return Boolean.FALSE;
            }
            
            IItem entidad = (IItem)it.next();
            //String strArea = entidad.getString("AREA");
            
            //Inicializa los datos de la propuesta
            IItem propuesta = entitiesAPI.createEntity("SECR_PROPUESTA", numexpProp);
            if (propuesta != null) {
                //propuesta.set("ORIGEN", strArea);
                propuesta.set("EXTRACTO", strExtracto);
                propuesta.store(cct);
            }
            
            //Actualiza el campo "estado" de la entidad para
            //que en el formulario se oculten los enlaces de creación de Propuesta/Decreto
            entidad.set("ESTADO", "Propuesta");
            entidad.store(cct);
            
            //Añade el ZIP con el contenido de la propuesta
            //---------------------------------------------

            // Obtener el documento zip "Contenido de la propuesta" del expediente de la entidad
            IItemCollection documentsCollection = entitiesAPI.getDocuments(numexpEnt, "NOMBRE = 'Contenido de la propuesta'", "FDOC DESC");
            IItem contenidoPropuesta = null;
            
            if (documentsCollection!=null && documentsCollection.next()){
                contenidoPropuesta = (IItem)documentsCollection.iterator().next();
            
            } else {
                throw new ISPACInfo("No se ha encontrado el documento de Contenido de la propuesta");
            }
            
            //Obtiene el número de fase de la propuesta
            IItemCollection collExpsAux = entitiesAPI.getEntities(SpacEntities.SPAC_FASES, numexpProp);
            Iterator<?> itExpsAux = collExpsAux.iterator();
            
            if ( !itExpsAux.hasNext()) {
                return Boolean.FALSE;
            }
            
            IItem iExpedienteAux = (IItem)itExpsAux.next();
            int idFase = iExpedienteAux.getInt("ID");

            // Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
            if (contenidoPropuesta!=null){

                // Crear el documento del mismo tipo que el Contenido de la propuesta pero asociado al nuevo expediente de Propuesta
                IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,contenidoPropuesta.getInt("ID_TPDOC"));

                String infopag = contenidoPropuesta.getString(DocumentosUtil.INFOPAG);
                String infopagrde = contenidoPropuesta.getString(DocumentosUtil.INFOPAG_RDE);
                String repositorio = contenidoPropuesta.getString(DocumentosUtil.REPOSITORIO);
                String descripcion = contenidoPropuesta.getString(DocumentosUtil.DESCRIPCION);
                String extension = contenidoPropuesta.getString(DocumentosUtil.EXTENSION);            
                int idPlantilla = contenidoPropuesta.getInt(DocumentosUtil.ID_PLANTILLA);
            
                nuevoDocumento.set(DocumentosUtil.INFOPAG, infopag);
                nuevoDocumento.set(DocumentosUtil.INFOPAG_RDE, infopagrde);
                nuevoDocumento.set(DocumentosUtil.REPOSITORIO, repositorio);
                nuevoDocumento.set(DocumentosUtil.DESCRIPCION, descripcion);
                nuevoDocumento.set(DocumentosUtil.EXTENSION, extension);
                
                if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
                    nuevoDocumento.set(DocumentosUtil.ID_PLANTILLA, idPlantilla);
                }
                
                try {
                    String codVerificacion = contenidoPropuesta.getString(DocumentosUtil.COD_COTEJO);
                    nuevoDocumento.set(DocumentosUtil.COD_COTEJO, codVerificacion);
                    
                } catch(ISPACException e) {
                    LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);
                }

                nuevoDocumento.store(cct);
            }
            
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido inicializar la propuesta.",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}
