package ieci.tdw.ispac.api.rule.procedures.negociado;

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


/**
 * 
 * @author teresa
 * @date 03/03/2010
 * @propósito Inicializa el expediente de Solicitud de Inserción del Anuncio en el BOP asociado al expediente de Convocatoria Negociado
 * de contratación actual.
 * Asocia el documento DOC de Anexo a Solicitud al expediente de Solicitud de Inserción del Anuncio en el BOP relacionado.
 * Es el mismo fichero físico DOC en el repositorio de documentos
 */
public class InitAnuncioNegRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(InitAnuncioNegRule.class);

    protected String strEntidad = "SGN_NEGOCIADO";
    //protected String strExtracto = "";
    //protected String strArea = "";
    protected String strEntidadAnuncio = "Contratación y compras";
    protected String strSumario = "Solicitud de anuncio generada desde el procedimiento de " +
                                        "Convocatoria de Negociado de Contratación Con Publicidad";
    protected String strObservaciones = "Solicitud de anuncio generada desde el procedimiento de " +
                                            "Convocatoria de Negociado de Contratación Con Publicidad";
    protected String strUrgencia = "Normal";
    protected String strAnexoSolicitud = "Anexo a Solicitud";
    
    /** Logger de la clase. */

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
            IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
            //----------------------------------------------------------------------------------------------
            
            //Obtención del expediente de anuncio relacionado
            String strExpAnuncio = rulectx.getNumExp();

            List<String> expedientesRelacionados = ExpedientesRelacionadosUtil.getExpRelacionadosPadres(entitiesAPI, strExpAnuncio);
            
            if (expedientesRelacionados.isEmpty()) {                
                LOGGER.warn("No se encuentra el expediente de propuesta relacionado");
                return Boolean.FALSE;
            }
            
            String strExpNegociado = expedientesRelacionados.get(0);
            
            //Inicializar los datos del anuncio
            IItemCollection col = entitiesAPI.getEntities(strEntidad, strExpNegociado);
            Iterator<?> it = col.iterator();
            
            if (!it.hasNext()) {
                return Boolean.FALSE;
            }
            
            IItem entidad = (IItem)it.next();
            //String strArea = entidad.getString("AREA");
            IItem anuncio = entitiesAPI.createEntity("BOP_SOLICITUD", strExpAnuncio);
            
            if (anuncio != null) {
                anuncio.set("ENTIDAD", strEntidadAnuncio);
                anuncio.set("SUMARIO", strSumario);
                anuncio.set("OBSERVACIONES", strObservaciones);
                anuncio.set("URGENCIA", strUrgencia);
                anuncio.store(cct);
            }
            
            //Actualizar el campo "estado" de la entidad para
            //que en el formulario se oculte el enlace de creación de Anuncio
            entidad.set("ESTADO", "Anuncio");
            entidad.store(cct);
            
            //Añadir el DOC con el contenido del anuncio
            //---------------------------------------------

            // Obtener el documento DOC "Anuncio de licitación" del expediente de Convocatoria de contratación
            IItemCollection documentsCollection = entitiesAPI.getDocuments(strExpNegociado, "NOMBRE = 'Anuncio de licitación'", "FDOC DESC");
            IItem docAnuncio = null;
            
            if (documentsCollection!=null && documentsCollection.next()){
                docAnuncio = (IItem)documentsCollection.iterator().next();
                
            } else {
                throw new ISPACInfo("No se ha encontrado el documento de Anuncio de licitación");
            }
            
            //Obtener la fase del expediente de Anuncio a partir de su NUMEXP en la tabla de spac_fases
            IItemCollection collExpsAux = entitiesAPI.getEntities(SpacEntities.SPAC_FASES, strExpAnuncio);
            Iterator<?> itExpsAux = collExpsAux.iterator();
            
            if (! itExpsAux.hasNext()) {
                LOGGER.warn("No se encuentra el expediente asociado de propuesta con numexp: "+strExpAnuncio);
                return Boolean.FALSE;
            }
            
            IItem iExpedienteAux = (IItem)itExpsAux.next();
            int idFase = iExpedienteAux.getInt("ID");
            
            // Copiar los valores de los campos INFOPAG - DESCRIPCION - EXTENSION - ID_PLANTILLA
            if (docAnuncio!=null) {
                // Crear el documento del mismo tipo que el Contenido de la propuesta pero asociado al nuevo expediente de Propuesta
                //IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase,docAnuncio.getInt("ID_TPDOC"));
                IItem nuevoDocumento = (IItem)genDocAPI.createStageDocument(idFase, DocumentosUtil.getIdTipoDocByNombre(cct, strAnexoSolicitud));

                String infopag = docAnuncio.getString(DocumentosUtil.INFOPAG);
                String infopagrde = docAnuncio.getString(DocumentosUtil.INFOPAG_RDE);
                String repositorio = docAnuncio.getString(DocumentosUtil.REPOSITORIO);
                String descripcion = docAnuncio.getString(DocumentosUtil.DESCRIPCION);
                String extension = docAnuncio.getString(DocumentosUtil.EXTENSION);            
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
                    LOGGER.info("No mostraba nada, solo muesta el mensaje. El error es: " + e.getMessage(), e);
                }
                
                nuevoDocumento.store(cct);
            }
            
        }catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
