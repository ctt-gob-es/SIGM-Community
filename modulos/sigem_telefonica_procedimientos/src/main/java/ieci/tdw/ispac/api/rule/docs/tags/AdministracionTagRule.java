package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene un listado de documentos a subsanar.
 *
 */
public class AdministracionTagRule implements IRule {
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(AdministracionTagRule.class);
    
    private static final String[] ARRAY_ADMINISTRACIONES_LOCALES = {"DIPUTACIÓN PROVINCIAL", 
        "AYUNTAMIENTOS", 
        "MANCOMUNIDADES"};
    
    private static final String[] ARRAY_ADMINISTRACIONES_AUTONOMICAS = {"PRESIDENCIA DE LA JUNTA", 
        "CONSEJERÍAS", 
        "DELEGACIONES PROVINCIALES"};
    
    private static final String[] ARRAY_ADMINISTRACIONES_ESTATALES = {"JEFATURA DEL ESTADO",
        "PRESIDENCIA DEL GOBIERNO", 
        "MINISTERIOS", 
        "DIRECCIONES GENERALES", 
        "DELEGACIÓN DEL GOBIERNO EN CASTILLA-LA MANCHA", 
        "SUBDELEGACIONES DEL GOBIERNO", 
        "DIRECCIONES PROVINCIALES Y SERVICIOS PERIFÉRICOS"};
    
    private static final String[] ARRAY_ADMINISTRACIONES_JUSTICIA = {"TRIBUNAL SUPREMO", 
        "JUNTA ELECTORAL CENTRAL", 
        "AUDIENCIA NACIONAL", 
        "TRIBUNAL SUPERIOR DE JUSTICIA DE CASTILLA-LA MANCHA", 
        "TRIBUNALES SUPERIORES DE JUSTICIA", 
        "AUDIENCIA PROVINCIAL DE CIUDAD REAL", 
        "AUDIENCIAS PROVINCIALES", 
        "JUNTA ELECTORAL PROVINCIAL", 
        "JUNTAS ELECTORALES DE ZONA",
        "JUZGADOS DE LO SOCIAL",
        "JUZGADOS DE LO PENAL",
        "JUZGADOS DE LO CONTENCIOSO-ADMINISTRATIVO",
        "JUZGADOS DE PRIMERA INSTANCIA E INSTRUCCIÓN",
        "AGRUPACIONES DE JUZGADOS DE PAZ "};
    
    private static final String[] ARRAY_OTRAS_ADMINISTRACIONES = {"OTRAS ADMINISTRACIONES"};
    
    private static final String[] ARRAY_ANUNCIOS_PARTICULARES = {"ANUNCIOS PARTICULARES"};
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            String clasificacion = null;
            String strQuery = null;
            IItemCollection collection = null;
            Iterator<?> it = null;
            IItem item = null;
            String administracion = null;
            
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
    
            LOGGER.warn("Query: " + strQuery);
            collection = entitiesAPI.getEntities("BOP_SOLICITUD", rulectx.getNumExp());    
            it = collection.iterator();
            
            if (it.hasNext()) {
                item = (IItem)it.next();
                
                clasificacion = item.getString("CLASIFICACION");
            }

            if (clasificacion != null) {
                
                if (esAdministracion(clasificacion, ARRAY_ADMINISTRACIONES_LOCALES)) {
                    administracion = "administración local";
                }

                if (esAdministracion(clasificacion, ARRAY_ADMINISTRACIONES_AUTONOMICAS)) {
                    administracion = "administración autonómica";
                }
                
                if (esAdministracion(clasificacion, ARRAY_ADMINISTRACIONES_ESTATALES)) {                    
                    administracion = "administración estatal";
                }
                
                if (esAdministracion(clasificacion, ARRAY_ADMINISTRACIONES_JUSTICIA)) {
                    administracion = "administración de justicia";
                }
                
                if (esAdministracion(clasificacion, ARRAY_OTRAS_ADMINISTRACIONES)) {
                    administracion = "otras administraciones";
                }
                
                if (esAdministracion(clasificacion, ARRAY_ANUNCIOS_PARTICULARES)) {
                    administracion = "anuncios particulares";
                }
                
                if (administracion == null) {
                    administracion = "otras administraciones";
                }
            }

            return administracion;
            
        } catch (Exception e) {
            throw new ISPACRuleException("Error confecionando listado de documentos a subsanar.", e);
        }
    }
    
    private boolean esAdministracion(String clasificacion, String[] arrayAdministraciones){
        List<String> listaAdministraciones = Arrays.asList(arrayAdministraciones);
        return listaAdministraciones.contains(clasificacion);
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
