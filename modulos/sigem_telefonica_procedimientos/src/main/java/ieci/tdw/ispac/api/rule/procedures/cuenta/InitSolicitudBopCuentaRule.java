package ieci.tdw.ispac.api.rule.procedures.cuenta;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
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

public class InitSolicitudBopCuentaRule implements IRule {

    private static final Logger LOGGER = Logger.getLogger(InitSolicitudBopCuentaRule.class);
    
    protected String strEntidad = "";
    protected String strBOPEntidad = "";
    protected String strBOPUrgencia = "";
    protected String strBOPSumario = "";
    protected String strBOPObservaciones = "";

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
            String numexpSolicitud = rulectx.getNumExp();    
            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, numexpSolicitud);
            
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

            //Actualiza el campo "estado" de la entidad para
            //que en el formulario se oculte el enlace de creación del expediente
            entidad.set("ESTADO", "Creado");
            entidad.store(cct);
            
            //Inicializa los datos de la solicitud BOP
            IItem solicitud = entitiesAPI.createEntity("BOP_SOLICITUD", numexpSolicitud);
            if (solicitud != null) {
                solicitud.set("ENTIDAD", strBOPEntidad);
                solicitud.set("URGENCIA", strBOPUrgencia);
                solicitud.set("SUMARIO", strBOPSumario);
                solicitud.set("OBSERVACIONES", strBOPObservaciones);
                solicitud.store(cct);
            }
            
            //Añade el anuncio en formato DOC
            //-------------------------------

            // Obtener el documento "Anuncio" del expediente de la entidad
            IItemCollection documentsCollection = entitiesAPI.getDocuments(numexpEnt, "NOMBRE='Anuncio'", "FDOC DESC");
            IItem docAnuncio = null;
            
            if (documentsCollection!=null && documentsCollection.next()){
                docAnuncio = (IItem)documentsCollection.iterator().next();
            
            } else {
                throw new ISPACInfo("No se ha encontrado el documento de Anuncio");
            }
            
            //Obtiene el número de fase de la solicitud
            IItemCollection collExpsAux = entitiesAPI.getEntities("SPAC_FASES", numexpSolicitud);
            Iterator<?> itExpsAux = collExpsAux.iterator();
            
            if ( !itExpsAux.hasNext()) {
                return Boolean.FALSE;
            }
            
            IItem iExpedienteAux = (IItem)itExpsAux.next();
            int idFase = iExpedienteAux.getInt("ID");

            // Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
            if (docAnuncio!=null){

                // Crear el documento asociado al nuevo expediente de Solicitud BOP
                // Debe ser de tipo "Anexo a Solicitud" y extension "DOC"
                int idDocSolicitud = DocumentosUtil.getIdTipoDocByNombre(cct, "Anexo a Solicitud");
                
                if (idDocSolicitud != -1 && docAnuncio.getString(DocumentosUtil.EXTENSION).toLowerCase().compareTo("doc")==0){
                    IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,idDocSolicitud);
    
                    String infopag = docAnuncio.getString(DocumentosUtil.INFOPAG);
                    String infopagrde = docAnuncio.getString(DocumentosUtil.INFOPAG_RDE);
                    String repositorio = docAnuncio.getString(DocumentosUtil.REPOSITORIO);
                    String descripcion = docAnuncio.getString(DocumentosUtil.DESCRIPCION);
                    String extension = "DOC"; //La extensión debe ser así, DOC en mayúsculas.            
                    int idPlantilla = docAnuncio.getInt(DocumentosUtil.ID_PLANTILLA);
                
                    nuevoDocumento.set(DocumentosUtil.INFOPAG, infopag);
                    nuevoDocumento.set(DocumentosUtil.INFOPAG_RDE, infopagrde);
                    nuevoDocumento.set(DocumentosUtil.REPOSITORIO, repositorio);
                    nuevoDocumento.set(DocumentosUtil.DESCRIPCION, descripcion);
                    nuevoDocumento.set(DocumentosUtil.EXTENSION, extension);
                    
                    if (String.valueOf(idPlantilla)!=null && String.valueOf(idPlantilla).trim().length()!=0){
                        nuevoDocumento.set(DocumentosUtil.ID_PLANTILLA, idPlantilla);
                    }
                    
                    try {
                        String codVerificacion = docAnuncio.getString(DocumentosUtil.COD_COTEJO);
                        nuevoDocumento.set(DocumentosUtil.COD_COTEJO, codVerificacion);
                        
                    } catch(ISPACException e) {
                        LOGGER.info("No mostraba nada, solo captura el posible error: " + e.getMessage(), e);
                    }
    
                    nuevoDocumento.store(cct);
                }
            }
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido inicializar la solicitud BOP.",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}
