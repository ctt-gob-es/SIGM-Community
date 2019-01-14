package es.dipucr.sigem.api.rule.procedures.lanza;

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

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.bop.BOPConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EdicionDocumentosUtil;

public class GetCosteAnuncioLanzaRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GetCosteAnuncioLanzaRule.class);
	
	protected String NOMBRE_DOC_ANUNCIO = "LANZA - Anuncio";
	private OpenOfficeHelper ooHelper = null;
	private static int TIPO_IVA = 4;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		double costeCaracter = 0;
		double iva = 0;
		double costeBase = 0;
		double costeIVA = 0;
		double costeTotal = 0;
		int numCaracteres = 0;
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        String numexp = cct.getStateContext().getNumexp();
	        //----------------------------------------------------------------------------------------------
	        
			// Abrir transacción
	        cct.beginTX();
	        
	        String strQuery = null;
	        IItemCollection collection = null;
	        IItem item = null;
	        	        
	        //Obtenemos los costes del fichero de propiedades
	        BOPConfiguration bopConfiguracion = BOPConfiguration.getInstance();
			costeCaracter = Float.parseFloat(bopConfiguracion.get(BOPConfiguration.COSTE_CARACTER));
	        iva = getIVAFacturacion(rulectx);
			
	        strQuery = "WHERE NUMEXP = '"+ numexp +"'";
	        collection = entitiesAPI.queryEntities("LANZA_SOLICITUD", strQuery);
	        item = (IItem) collection.iterator().next();
	        	
	        //Obtenemos los caracteres de la plantilla de anuncio (por página) para restarlos al total
        	int numCaracteresCabeceraPie = obtenerNumCaracteresPlantilla(rulectx, cct);
        	
        	String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, NOMBRE_DOC_ANUNCIO);
        	File file1 = DocumentosUtil.getFile(rulectx.getClientContext(), strInfoPag, null, null);
    		ooHelper = OpenOfficeHelper.getInstance();
    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
    		
        	numCaracteres = EdicionDocumentosUtil.getNumeroCaracteres(xComponent).intValue();
        	numCaracteres = numCaracteres - numCaracteresCabeceraPie;
        	item.set("NUM_CARACTERES", numCaracteres);
        	item.set("COSTE_CARACTER", costeCaracter);
        	
        	//Coste base en función del número de caracteres
	        costeBase = numCaracteres * costeCaracter;
	        item.set("COSTE_BASE", costeBase);
	        costeIVA = costeBase * iva;
	        item.set("COSTE_IVA", costeIVA);
	        costeTotal = costeBase + costeIVA;
	        item.set("COSTE_TOTAL", costeTotal);
	        	        	
        	item.store(cct);
        	
	        
	        return new Boolean(true);
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				logger.error(e1.getMessage(), e1);
			}
	    	
	        throw new ISPACRuleException("Error al obtener el coste del anuncio de solicitud.", e);
	    } finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}    
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	
	/**
	 * Método que accede a la tabla CONT_VLDTBL_PORCENTAJESIVA para obtener el IVA actual,
	 * concretamente al registro con id=4 y columna valor
	 * @param rulectx
	 * @return
	 */
	protected float getIVAFacturacion(IRuleContext rulectx) throws ISPACRuleException{
		float iva = 0;
		try {
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
			IItem item = entitiesAPI.getEntity("CONT_VLDTBL_PORCENTAJESIVA", TIPO_IVA);
			iva = Float.valueOf(item.getString("VALOR")) / 100;
		} catch (ISPACException e) {
			logger.error("Error al obtener el IVA. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el IVA. " + e.getMessage(), e);
		}
		return iva;
	}

	/**
	 * Recupera el número de caracteres (cabecera y pie) de la plantilla tipo de anuncios
	 * @param rulectx
	 * @param cct 
	 * @return
	 * @throws Exception 
	 */
	private int obtenerNumCaracteresPlantilla(IRuleContext rulectx, ClientContext cct) throws Exception {
		
		int numCaracteres = 0;

		//Generamos un documento de plantilla vacío
		IItem itemDocPlantillaVacia = DocumentosUtil.generarDocumento(rulectx, NOMBRE_DOC_ANUNCIO, "borrar");
		String strInfoPag = itemDocPlantillaVacia.getString("INFOPAG");
		File file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);

		//Recuperamos el número de caracteres
		ooHelper = OpenOfficeHelper.getInstance();
		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
		numCaracteres = EdicionDocumentosUtil.getNumeroCaracteres(xComponent).intValue();
		
		if(null != ooHelper){
			ooHelper.dispose();
		}

		//Borramos el documento
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		entitiesAPI.deleteDocument(itemDocPlantillaVacia);
		
		return numCaracteres;
	}
}
