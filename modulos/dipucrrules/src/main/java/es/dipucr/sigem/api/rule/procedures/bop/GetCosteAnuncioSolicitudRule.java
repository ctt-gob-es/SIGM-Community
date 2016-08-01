package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.bop.BOPConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EdicionDocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * Retorna el número de anuncio a asociar al expediente de BOP - Solicitud de inserción de anuncio
 *
 */
public class GetCosteAnuncioSolicitudRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GetCosteAnuncioSolicitudRule.class);
	
	/** Objeto properties con los ids necesarios */
	private static Properties props = new Properties();
	
	protected String STR_DocAnuncio = Constants.BOP._DOC_ANUNCIO;
	private OpenOfficeHelper ooHelper = null;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		float costeCaracter = 0;
		float costeUrgencia = 0;
		float costeBaseCaracteres = 0;
		float costeIVACaracteres = 0;
		float costeTotalCaracteres = 0;
		float costeBaseOtrosConceptos = 0;
		float costeIVAOtrosConceptos = 0;
		float costeTotalOtrosConceptos = 0;
		float iva = 0;
		float costeBaseAnuncio = 0;
		float costeIvaAnuncio = 0;
		float costeTotalAnuncio = 0;
		String urgencia = null;
		int numCaracteres = 0;
//		int numPaginas = 0; //[eCenpri-Felipe #970] AL FINAL NO NECESARIO
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        /////TRANSACTION
	        
			// Abrir transacción
	        cct.beginTX();
	        
	        String numAnuncioSolicitud = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        String numexp = null;
	        
	        //Obtenemos el número de expediente
	        numexp = cct.getStateContext().getNumexp();
	        	        
	        //Obtenemos los costes del fichero de propiedades
	        BOPConfiguration bopConfiguracion = BOPConfiguration.getInstance();
			costeCaracter = Float.parseFloat(bopConfiguracion.get(BOPConfiguration.COSTE_CARACTER));
			costeUrgencia = Float.parseFloat(bopConfiguracion.get(BOPConfiguration.COSTE_URGENCIA));
			//iva = Float.parseFloat(props.getProperty("iva"));
	        iva = getIVAFacturacion(rulectx);
			
	        strQuery = "WHERE NUMEXP = '"+ numexp +"'";
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext()){
	        	item = (IItem)it.next();
		        //Ver cuántos caracteres tiene el documento del anuncio
		        //[eCenpri-Felipe #970]
	        	//Obtenemos los caracteres de la plantilla de anuncio (por página) para restarlos al total
	        	int numCaracteresCabeceraPie = obtenerNumCaracteresPlantilla(rulectx, cct);
	        	
	        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, STR_DocAnuncio);
	        	File file1 = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);
	    		ooHelper = OpenOfficeHelper.getInstance();
	    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
	    		
	        	numCaracteres = EdicionDocumentosUtil.getNumeroCaracteres(xComponent).intValue();
//	        	numPaginas = CommonFunctions.getNumPages(xComponent); //[eCenpri-Felipe #970] NO NECESARIO
	        	//Quitamos los caracteres de la cabecera y pie (multiplicar por el número de páginas)
//	        	numCaracteres = numCaracteres - (numPaginas * numCaracteresCabeceraPie);
	        	numCaracteres = numCaracteres - numCaracteresCabeceraPie;
	        	item.set("CARACTERES", numCaracteres);
	        	urgencia = item.getString("URGENCIA");
	        	
		        //Calculamos el coste
	        	//[ecenpri-Felipe Ticket#21] El coste urgencia se almacena en términos de porcentaje
	        	//Actualmente es 1, por lo que se le suma el 100%, es decir, es el doble
	        	if ((urgencia != null) && urgencia.equals("Urgente")){
	        		costeCaracter += (costeUrgencia * costeCaracter);
	        	}
	        	item.set("COSTE_CARACTER", costeCaracter);
	        	
	        	//Coste base en función del número de caracteres
		        costeBaseCaracteres = numCaracteres * costeCaracter;
		        item.set("COSTE_BASE_CARACTERES", costeBaseCaracteres);
		        item.set("TIPO_IVA_CARACTERES", iva);
		        costeIVACaracteres = costeBaseCaracteres * iva;
		        item.set("COSTE_IVA_CARACTERES", costeIVACaracteres);
		        costeTotalCaracteres = costeBaseCaracteres + costeIVACaracteres;
		        item.set("COSTE_TOTAL_CARACTERES", costeTotalCaracteres);
		        
		        //Coste por otros conceptos
		        costeBaseOtrosConceptos = item.getFloat("COSTE_BASE_OTROS_CONCEPTOS");
		        if(costeBaseOtrosConceptos > 0){
		        	item.set("TIPO_IVA_OTROS_CONCEPTOS", iva);
		        	costeIVAOtrosConceptos = costeBaseOtrosConceptos * iva;
			        item.set("COSTE_IVA_OTROS_CONCEPTOS", costeIVAOtrosConceptos);
			        costeTotalOtrosConceptos = costeBaseOtrosConceptos + costeIVAOtrosConceptos;
			        item.set("COSTE_TOTAL_OTROS_CONCEPTOS", costeTotalOtrosConceptos);
		        }
		        else{
		        	costeBaseOtrosConceptos = 0;
		        }
		        
		        //Costes totales
	        	costeBaseAnuncio = costeBaseCaracteres + costeBaseOtrosConceptos;
	        	item.set("COSTE", costeBaseAnuncio);
	        	
	        	costeIvaAnuncio = costeIVACaracteres + costeIVAOtrosConceptos;
	        	item.set("COSTE_IVA", costeIvaAnuncio);
	        	
	        	costeTotalAnuncio = costeTotalCaracteres + costeTotalOtrosConceptos;
	        	//Debe ser igual que -> costeTotalAnuncio = costeBaseAnuncio + costeIvaAnuncio;
	        	item.set("COSTE_TOTAL", costeTotalAnuncio);
	        	
	        	item.store(cct);
	        	
	        	ooHelper.dispose();
	        }
	        
	        return numAnuncioSolicitud;
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error(e1.getMessage(), e1);
			}
	    	
	        throw new ISPACRuleException("Error al obtener el coste del anuncio de solicitud.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	
	/**
	 * Método que accede a la tabla CONT_VLDTBL_PORCENTAJESIVA para obtener el IVA actual,
	 * concretamente al registro con id=3 y columna valor
	 * @param rulectx
	 * @return
	 * @throws ISPACException
	 * @author Felipe - ecenpri
	 * @throws ISPACRuleException 
	 */
	protected float getIVAFacturacion(IRuleContext rulectx) throws ISPACRuleException{
		float iva = 0;
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			IItem item = entitiesAPI.getEntity("CONT_VLDTBL_PORCENTAJESIVA", 3);
			iva = Float.valueOf(item.getString("VALOR")) / 100;
		} catch (ISPACException e) {
			logger.error("Error al obtener el IVA. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el IVA. " + e.getMessage(), e);
		}
		return iva;
	}

	/**
	 * [eCenpri-Felipe #970]
	 * Recupera el número de caracteres (cabecera y pie) de la plantilla tipo de anuncios
	 * @param rulectx
	 * @param cct 
	 * @return
	 * @throws Exception 
	 */
	private int obtenerNumCaracteresPlantilla(IRuleContext rulectx, ClientContext cct) throws Exception {
		
		int numCaracteres = 0;

		//Generamos un documento de plantilla vacío
		IItem itemDocPlantillaVacia = DocumentosUtil.generarDocumento(rulectx, STR_DocAnuncio, "borrar");
		String strInfoPag = itemDocPlantillaVacia.getString("INFOPAG");
		File file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);

		//Recuperamos el número de caracteres
		ooHelper = OpenOfficeHelper.getInstance();
		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
		numCaracteres = EdicionDocumentosUtil.getNumeroCaracteres(xComponent).intValue();
		
		//Borramos el documento
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		entitiesAPI.deleteDocument(itemDocPlantillaVacia);
		
		return numCaracteres;
	}
}
