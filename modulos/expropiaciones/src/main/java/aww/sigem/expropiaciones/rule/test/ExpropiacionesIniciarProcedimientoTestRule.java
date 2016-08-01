package aww.sigem.expropiaciones.rule.test;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.expedients.CommonData;
import ieci.tdw.ispac.api.expedients.Expedients;
import ieci.tdw.ispac.api.expedients.InterestedPerson;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Inicializa valores de Finca al iniciar el expediente
 */
public class ExpropiacionesIniciarProcedimientoTestRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesIniciarProcedimientoTestRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext 	rulectx) throws ISPACRuleException {
		try {
					
			Expedients expedientsAPI = new Expedients();		
			
			logger.warn("Lanzando un nuevo procedimiento de PruebaReglas");
			
			//Lista de interesados
			List interested = new ArrayList();
			
			//Obtener el número de registro de algún sitio (puede ser del expediente padre)
			//En expropiaciones no va a haber registro
			String numregistro = null;
			
			//Se abre un nuevo Expediente de tipo PruebaReglas
			CommonData commonData = new CommonData("1", "PCD-46", numregistro, new Date(), interested);
	        // Datos específicos del procedimiento
	        String especificDataXML = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?><datos_especificos></datos_especificos>";
			
	        // Lista de documentos del expediente
	        List documents = new ArrayList();
			
	        String idExpCreado = expedientsAPI.initExpedient(commonData, especificDataXML, documents, null);
	        logger.warn("Id del Expediente creado: " + idExpCreado);
	        
	        //Relacionar el expediente recien creado con el padre
	           	 
	        
	        //SPAC_TBL_007 - Lista de tipos de relaciones
	        //SPAC_EXP_RELACIONADOS - Esta tabla no es una entidad pública en SIGEM (la he visto en pgAdmin)
	        
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        
	        IItem relacionExpedientes = entitiesAPI.createEntity("SPAC_EXP_RELACIONADOS", "");
	        relacionExpedientes.set("NUMEXP_PADRE", rulectx.getNumExp());
	        relacionExpedientes.set("NUMEXP_HIJO", idExpCreado);
	        relacionExpedientes.set("RELACION", "PruebaReglas Padre/Hijo");        
	        relacionExpedientes.store(rulectx.getClientContext());
	        
	        //Inicializar datos del expediente recién creado (Participantes)
	        
	        
			IItemCollection itemCollection = entitiesAPI.getEntities("SPAC_DT_INTERVINIENTES", rulectx.getNumExp());
			
			
			if (itemCollection!=null && itemCollection.toList().size()>=1) {
				throw new ISPACRuleException("Se ha producido un error. Se han encontrado varios registros para la entidad SPAC_DT_INTERVINIENTES");
			}else if (itemCollection!=null && itemCollection.toList().size()==0) {				
				IItem item = entitiesAPI.createEntity("SPAC_DT_INTERVINIENTES","");
				item.set("NUMEXP", idExpCreado);
				item.set("NDOC", "12345678Z");				
				item.store(rulectx.getClientContext());
		        
				IItem item2 = entitiesAPI.createEntity("SPAC_DT_INTERVINIENTES","");
				item2.set("NUMEXP", idExpCreado);
				item2.set("NDOC", "00000001A");
				item2.store(rulectx.getClientContext());
			}
		        
			return null;
		} catch (Exception e) {
			throw new ISPACRuleException(e);
		}
	}

	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
    //TODO:Borrar - Codigo que muestra como añadir interesados y documentos
	private static void initExpedient2(int idPcd) throws Exception {
		
        Expedients expedientsAPI = new Expedients();
        
        // Lista de interesados del expediente
        List interested = new ArrayList();
        
        InterestedPerson interestedPerson1 = new InterestedPerson(null,"N", "06565893A", "José López López", "jll@hotmail.com");
        InterestedPerson interestedPerson2 = new InterestedPerson(null,"N", "09461393B", "Manuel Álvarez López", "Cervantes 5 - 2ºA", "33005", "Oviedo", "Asturias / España");
        InterestedPerson interestedPerson3 = new InterestedPerson(null,"N", "03242544C", "Luis Álvarez Gómez", "Asturias 23 - 5ºE", "33012", "Gijón", "Asturias / España", "lag@hotmail.com", InterestedPerson.IND_TELEMATIC, "999999999", "666666666");
        InterestedPerson interestedPerson4 = new InterestedPerson(null,"S", "12345678A", "Pablo Alonso García", "paglo_g_a@gmail.com");
        interested.add(interestedPerson1);
        interested.add(interestedPerson2);
        interested.add(interestedPerson3);
        interested.add(interestedPerson4);
        
        CommonData commonData = null;
        String especificDataXML = null;
        
        //Procedimiento de Quejas, Reclamaciones y Sugerencias
        //==================================================
        // Datos comunes del expediente
        if (idPcd == 3){
	        commonData = new CommonData("1", "PCD-3", "200700000001", new Date(), interested);
	        // Datos específicos del procedimiento
	        especificDataXML = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?><datos_especificos><asunto_queja>Ruidos</asunto_queja><cod_organo>0001</cod_organo><descr_organo>Servicio de Atención al Ciudadano</descr_organo></datos_especificos>";
        } else 
        //Procedimiento de Subvenciones
        //==================================================
       	if (idPcd == 4){
	        commonData = new CommonData("1", "PCD-4", "200700000001", new Date(), interested);
	        // Datos específicos del procedimiento
	        especificDataXML = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?><datos_especificos><tipo_subvencion>001</tipo_subvencion><resumen_proyecto>Resumen</resumen_proyecto></datos_especificos>";
        	
        }
        //Procedimiento de Obras
        //==================================================
       	else {
	        commonData = new CommonData("1", "PCD-5", "200700000001", new Date(), interested);
	        // Datos específicos del procedimiento
	        especificDataXML = "<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?><datos_especificos><ubicacion_inmueble>Uria, 21</ubicacion_inmueble><descripcion_obras>Reforma Integral</descripcion_obras></datos_especificos>";
        }
        
        // Lista de documentos del expediente
        List documents = new ArrayList();
        
        // Iniciar el expediente
        expedientsAPI.initExpedient(commonData, especificDataXML, documents);
	}


}
