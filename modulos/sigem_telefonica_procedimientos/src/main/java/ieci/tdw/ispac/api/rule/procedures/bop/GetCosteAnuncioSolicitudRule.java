package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
//import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
//import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

/**
 * Retorna el número de anuncio a asociar al expediente de BOP - Solicitud de inserción de anuncio
 *
 */
public class GetCosteAnuncioSolicitudRule implements IRule {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GetCosteAnuncioSolicitudRule.class);
	
	/** Objeto properties con los ids necesarios */
	private static Properties props = new Properties();
	
	/** Path para acceder al fichero de propiedades */
	private static final String PATH_PROPERTIES_FILE = "rules.properties";
	
	protected String STR_DocAnuncio = "BOP - Anuncio";
	private OpenOfficeHelper ooHelper = null;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		float costeAnuncio = 0;
		float costeAnuncioTotal = 0;
		float costeCaracter = 0;
		float costeUrgencia = 0;
		float iva = 0;
		float costeIva = 0;
		float costeTotal = 0;
		String urgencia = null;
		int numCaracteres = 0;
//		int numPaginas = 1;
		
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
	        
	        //Obtenemos los costes del fichero de propiedades
			props.load(this.getClass().getClassLoader().getResourceAsStream(PATH_PROPERTIES_FILE));
			costeCaracter = Float.parseFloat(props.getProperty("costeCaracter"));
	        logger.warn("Coste por caracter = " + costeCaracter);
			costeUrgencia = Float.parseFloat(props.getProperty("costeUrgencia"));
	        logger.warn("Coste de la urgencia = " + costeUrgencia);
			iva = Float.parseFloat(props.getProperty("iva"));
	        logger.warn("IVA aplicado = " + iva);
			
	        strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
	        logger.warn("Query: " + strQuery);
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext()){
	        	item = (IItem)it.next();
		        //Ver cuántos caracteres tiene el documento del anuncio
	        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, STR_DocAnuncio);
				logger.warn("InfoPag del anuncio: " + strInfoPag);
	        	File file1 = CommonFunctions.getFile(rulectx, strInfoPag);
//	    		String cnt = "uno:socket,host=localhost,port=8100;urp;StarOffice.NamingService";
//	    		String cnt = "uno:socket,host=10.12.200.55,port=8100;urp;StarOffice.NamingService";
//	    		ooHelper = OpenOfficeHelper.getInstance(cnt);
	    		ooHelper = OpenOfficeHelper.getInstance();
				logger.warn("Fichero: " + file1.getPath());
	    		XComponent xComponent = ooHelper.loadDocument("file://" + file1.getPath());
	    		
	        	numCaracteres = CommonFunctions.getNumeroCaracteres(xComponent).intValue(); 
		        logger.warn("Número de caracteres: " + (numCaracteres - 63));
//	        	numPaginas = CommonFunctions.getNumPages(xComponent); 
//		        logger.warn("Número de páginas: " + numPaginas);
	        	urgencia = item.getString("URGENCIA");
		        logger.warn("Urgencia: " + urgencia);
	        	
		        // Quitamos los caracteres de la cabecera
//	        	costeAnuncio = (numCaracteres - (numPaginas * 63)) * costeCaracter;
	        	costeAnuncio = (numCaracteres - 63) * costeCaracter;
	        	costeAnuncioTotal = costeAnuncio;
	        	
	        	if ((urgencia != null) && urgencia.equals("Urgente"))
	        	{
	        		costeAnuncioTotal = costeAnuncio + (costeAnuncio * costeUrgencia);
	        	}
	        	
	        	item.set("COSTE", String.valueOf(costeAnuncioTotal));
	        	logger.warn("Coste = " + costeAnuncioTotal);
	        	
	        	costeIva = costeAnuncioTotal * iva;
	        	item.set("COSTE_IVA", String.valueOf(costeIva));
	        	logger.warn("Coste del IVA = " + costeIva);
	        	
	        	costeTotal = costeAnuncioTotal + costeIva;
	        	item.set("COSTE_TOTAL", String.valueOf(costeTotal));
	        	logger.warn("Coste total = " + costeTotal);
	        	
	        	item.store(cct);
	        }
	        
	        logger.warn("Coste del anuncio de la solicitud = " + costeAnuncioTotal);
	        return numAnuncioSolicitud;
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				e1.printStackTrace();
			}
	    	
	        throw new ISPACRuleException("Error al obtener el coste del anuncio de solicitud.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
//	private int numPalabras(String texto)
//	{
//		int numPalabras = 0;
//		
//		if (texto != null && !texto.equals(""))
//		{
//			StringTokenizer st = new StringTokenizer(texto);
//			numPalabras = st.countTokens();
//		}
//		
//		return numPalabras;
//	}

//	private IItem getDocument(IRuleContext rulectx, IItem acta) throws ISPACException
//	{
//		IItem doc = null;
//		try
//		{
//			IClientContext cct = rulectx.getClientContext();
//			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
//			String strId = acta.getString("IDDOC");
//			if ((strId == null) || strId.equals(""))
//			{
//				return null;
//			}
//			String strQuery = "WHERE ID='"+strId+"'";
//			IItemCollection docs = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
//			Iterator it = docs.iterator();
//			if (it.hasNext())
//			{
//				doc = (IItem)it.next();
//			}
//		}
//		catch(Exception e)
//		{
//			throw new ISPACException(e);
//		}
//		return doc;
//	}
}
