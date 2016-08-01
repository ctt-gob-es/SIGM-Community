package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;

/**
 * Regla para ser utilizada desde una plantilla. Recibe el parámetro CLASIFICACION que puede tomar los siguientes valores:
 * 
 * diputacion
 * ayuntamiento
 * consejeria
 * ministerio
 * otra
 * particular
 * 
 * Devuelve una cadena con las entidades de cada grupo de clasificacion y sus anuncios correspondientes.
 *
 */
public class GenerateLiquidacionRecibosRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GenerateLiquidacionRecibosRule.class);
	
	protected String STR_DocFactura = "BOP - Factura";

	private OpenOfficeHelper ooHelper = null;

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		ClientContext cct = null;
		String strQuery = null;
        IItemCollection collection = null;
        Iterator it = null;
        IItem item = null;
        String fechaInicio = null;
        String fechaFin = null;

		String entidad = null;
		String entidadAnterior = null;
		String numexp = null;
		String sumario = null;
		double coste = -1;
		double costeEntidad = 0;
		int anunciosEntidad = 0;
		Date fechaPublicacion = null;
		XComponent xComponent = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		String cnt = "uno:socket,host=10.12.200.55,port=8100;urp;StarOffice.NamingService";
		
		File file = null;
		File file1 = null;
		
		String numFactura = null;

		logger.warn("INICIO.");
		
		try{
			//----------------------------------------------------------------------------------------------
			cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			//----------------------------------------------------------------------------------------------

			// Abrir transacción
			cct.beginTX();

			//Obtenemos el parámetro CLASIFICACION de la plantilla
//			String clasificacion = rulectx.get("clasificacion");
//
//			logger.warn("- CLASIFICACION: " + clasificacion);

	        strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
	        logger.warn("Query: " + strQuery);
	        collection = entitiesAPI.queryEntities("BOP_LIQUIDACION", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext()){
	        	item = (IItem)it.next();
	        	
	        	fechaInicio = item.getString("FECHA_INICIO"); 
	        	fechaFin = item.getString("FECHA_FIN"); 
	        }

			//FECHA_PUBLICACION != NULL

			strQuery = "WHERE TIPO_FACTURACION = 'Pago con crédito' AND (NUM_FACTURA IS NULL OR NUM_FACTURA='') AND FECHA_PUBLICACION BETWEEN '" + fechaInicio +"' AND '" + fechaFin + "' ORDER BY ENTIDAD";
			collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
			it = collection.iterator();
			
			while (it.hasNext())
			{
				item = (IItem)it.next();
				
				entidad = item.getString("ENTIDAD");
				logger.warn("Entidad: " + entidad);
				
				if (entidadAnterior == null)
				{
					entidadAnterior = entidad;
					
					// Número de la nueva factura
					numFactura = getNumFactura(rulectx);
					cct.setSsVariable("NUM_FACTURA", numFactura);

					// Crear un nuevo documento de factura
			        CommonFunctions.generarDocumento(rulectx, STR_DocFactura, STR_DocFactura, "borrar");
		        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, STR_DocFactura + " - borrar");
					logger.warn("InfoPag del documento generado: " + strInfoPag);
		        	file1 = CommonFunctions.getFile(rulectx, strInfoPag);
//		    		ooHelper = OpenOfficeHelper.getInstance(cnt);
		    		ooHelper = OpenOfficeHelper.getInstance();
		    		xComponent = ooHelper.loadDocument("file://" + file1.getPath());
				}
				
				if (entidad!=null)
				{
					if (!entidad.equals(entidadAnterior))
					{
						meterTotal(xComponent, String.valueOf(anunciosEntidad), String.valueOf(costeEntidad));
						
			    		//Guarda el resultado en repositorio temporal
						String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
						fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
						file = new File(fileName);
			    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
			    		file1.delete();
			    		
			    		//Guarda el resultado en gestor documental
						strQuery = "WHERE NOMBRE = '" + STR_DocFactura + "'";
						logger.warn("Query: " + strQuery);
				        collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
				        it = collection.iterator();
				        int tpdoc = 0;
				        if (it.hasNext())
				        {
				        	IItem tpd = (IItem)it.next();
				        	tpdoc = tpd.getInt("ID");
							logger.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
				        }
			    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
			    		FileInputStream in = new FileInputStream(file);
			    		int docId = newdoc.getInt("ID");
			    		Object connectorSession = gendocAPI.createConnectorSession();
			    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", STR_DocFactura + " - " + entidadAnterior);
			    		entityDoc.set("EXTENSION", "doc");
			    		entityDoc.store(cct);
			    		file.delete();
			    		//Borra los documentos intermedios del gestor documental
						strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '%borrar%'";
				        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
				        it = collection.iterator();
				        while (it.hasNext())
				        {
				        	IItem doc = (IItem)it.next();
				        	logger.warn("Borrar (ID): " + doc.getString("ID"));
				        	logger.warn("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
				        	logger.warn("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
				        	logger.warn("Borrar (DESCRIPCION): " + doc.getString("DESCRIPCION"));
				        	entitiesAPI.deleteDocument(doc);
				        }

						entidadAnterior = entidad;
						costeEntidad = 0.0;
						anunciosEntidad = 0;

						// Número de la nueva factura
						numFactura = getNumFactura(rulectx);
						cct.setSsVariable("NUM_FACTURA", numFactura);

						// Crear un nuevo documento de factura
				        CommonFunctions.generarDocumento(rulectx, STR_DocFactura, STR_DocFactura, "borrar");
			        	String strInfoPag = CommonFunctions.getInfoPag(rulectx, STR_DocFactura + " - borrar");
						logger.warn("InfoPag del documento generado: " + strInfoPag);
			        	file1 = CommonFunctions.getFile(rulectx, strInfoPag);
//			    		ooHelper = OpenOfficeHelper.getInstance(cnt);
			    		ooHelper = OpenOfficeHelper.getInstance();
			    		xComponent = ooHelper.loadDocument("file://" + file1.getPath());
					}

					logger.warn("Metiendo anuncio en la factura...");

					numexp = item.getString("NUMEXP");
					logger.warn("Número de expediente: " + numexp);

					sumario = item.getString("SUMARIO");
					if (sumario == null)
					{
						sumario = "";
					}
					logger.warn("Sumario: " + sumario);

					coste = item.getDouble("COSTE");
					if (coste < 0)
					{
						coste = 0.0;
					}
					logger.warn("Coste: " + coste);

					fechaPublicacion = item.getDate("FECHA_PUBLICACION");
					logger.warn("Fecha de publicación: " + fechaPublicacion);
					
					anunciosEntidad ++;
					costeEntidad = costeEntidad + coste;

					item.set("NUM_FACTURA", numFactura);
					logger.warn("Número de factura: " + numFactura);
		        	item.store(cct);

					meterAnuncio(xComponent, numexp, sumario, String.valueOf(coste), df.format(fechaPublicacion));
				}
			}

			if (entidadAnterior != null)
			{
				meterTotal(xComponent, String.valueOf(anunciosEntidad), String.valueOf(costeEntidad));

				//Guarda el resultado en repositorio temporal
				String fileName = FileTemporaryManager.getInstance().newFileName(".doc");
				fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				file = new File(fileName);
	    		OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"MS Word 97");
	    		file1.delete();
	    		
	    		//Guarda el resultado en gestor documental
				strQuery = "WHERE NOMBRE = '" + STR_DocFactura + "'";
				logger.warn("Query: " + strQuery);
		        collection = entitiesAPI.queryEntities("SPAC_CT_TPDOC", strQuery);
		        it = collection.iterator();
		        int tpdoc = 0;
		        if (it.hasNext())
		        {
		        	IItem tpd = (IItem)it.next();
		        	tpdoc = tpd.getInt("ID");
					logger.warn("Documento guardado en el gestor documental (ID): " + tpdoc);
		        }
	    		IItem newdoc = gendocAPI.createTaskDocument(rulectx.getTaskId(), tpdoc);
	    		FileInputStream in = new FileInputStream(file);
	    		int docId = newdoc.getInt("ID");
	    		Object connectorSession = gendocAPI.createConnectorSession();
	    		IItem entityDoc = gendocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), docId, in, (int)file.length(), "application/msword", STR_DocFactura + " - " + entidadAnterior);
	    		entityDoc.set("EXTENSION", "doc");
	    		entityDoc.store(cct);
	    		file.delete();
	    		//Borra los documentos intermedios del gestor documental
				strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND DESCRIPCION LIKE '%borrar%'";
		        collection = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery);
		        it = collection.iterator();
		        while (it.hasNext())
		        {
		        	IItem doc = (IItem)it.next();
		        	logger.warn("Borrar (ID): " + doc.getString("ID"));
		        	logger.warn("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
		        	logger.warn("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
		        	logger.warn("Borrar (DESCRIPCION): " + doc.getString("DESCRIPCION"));
		        	entitiesAPI.deleteDocument(doc);
		        }
			}
			
        	return new Boolean(true);
		} 
		catch (Exception e)
		{
			// Si se produce algún error se hace rollback de la transacción
			try
			{
				cct.endTX(false);
			}
			catch (ISPACException e1) 
			{
				e1.printStackTrace();
			}

			throw new ISPACRuleException("Error al obtener la liquidación de cobros de recibos.", e);
		}
		finally
		{
			try
			{
				cct.deleteSsVariable("NUM_FACTURA");
			}
			catch (ISPACException ie)
			{
				throw new ISPACRuleException("Error al borrar la variable de sesión NUM_FACTURA.", ie);
			}
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	private void meterAnuncio (XComponent xComponent, String numexp, String sumario, String coste, String fechaPublicacion) throws ISPACException
	{
		try
		{
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

		    logger.warn("Anuncio: " + fechaPublicacion + "\t" + numexp + "\t" + sumario + "\t\t" + coste);
			xText.insertString(xTextCursor, "\r" + fechaPublicacion + "\t" + numexp + "\t" + sumario + "\t\t" + coste, false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}

	private void meterTotal (XComponent xComponent, String numAnuncios, String costeTotal) throws ISPACException
	{
		try
		{
		    XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		    XText xText = xTextDocument.getText();
		    XTextCursor xTextCursor = xText.createTextCursor();
		    xTextCursor.gotoRange(xText.getEnd(),false);

		    Log.warn("\t\tNúmero de anuncios: " + numAnuncios + "\t\tTOTAL: " + costeTotal);
			xText.insertString(xTextCursor, "\r\r\r\t\tNúmero de anuncios: " + numAnuncios + "\t\tTOTAL: " + costeTotal, false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	private String getNumFactura(IRuleContext rulectx) throws ISPACRuleException 
	{
		int longitudNumFactura = 7;
		
		ClientContext cct = null;
		
        try{
			//----------------------------------------------------------------------------------------------
	        cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
			// Abrir transacción
	        cct.beginTX();
	        
	        String numFactura = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
	        
	        //Obtenemos el valor del anyo_factura de la tabla de validación global
	        strQuery = "WHERE VALOR = 'anyo_factura'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        it = collection.iterator();
	        int iAnio = -1;
	        if (it.hasNext()){
	        	item = (IItem)it.next();
	        	iAnio = Integer.parseInt(item.getString("SUSTITUTO"));
	        }
	        
	        if (iAnio > -1){
		        //Lo comparamos con el año actual (si fueran distintos hay que reiniciar los contadores ya que acaba de comenzar un nuevo año)
		        Calendar gc = new GregorianCalendar();
		        int iAnioActual = gc.get(Calendar.YEAR);
		        
		        if(iAnio == iAnioActual){
			        strQuery = "WHERE VALOR = 'num_factura'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        String auxNumFactura = null;
			        int iNumFactura =  -1;
			        if (it.hasNext())
			        {
			        	item = (IItem)it.next();
			        	iNumFactura = Integer.parseInt(item.getString("SUSTITUTO"))+1;
			        	auxNumFactura = String.valueOf(iNumFactura);
			        	
			        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudNumFactura dígitos
			        	while (auxNumFactura.length() < longitudNumFactura)
			        	{
			        		auxNumFactura = "0"+auxNumFactura;
			        	}
			        	numFactura = iAnio + "-" + auxNumFactura;
			        	//Actualizar el último número de factura utilizado en la tabla de validación global
			        	item.set("SUSTITUTO", iNumFactura);
			        	item.store(cct);
			        }
		        }
		        else
		        {
		        	//Reiniciar los contadores ya que acaba de comenzar un nuevo año y posteriormente asignar el primer número de factura
		        	//	al expediente actual
		        	//Asignar el valor del año nuevo en la tabla de validación
		        	item.set("SUSTITUTO", iAnioActual);
		        	item.store(cct);
		        	
			        //Reiniciar el valor del contador num_factura en la tabla de validación
			        strQuery = "WHERE VALOR = 'num_factura'";
			        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			        it = collection.iterator();
			        while (it.hasNext()){
			        	item = (IItem)it.next();
		        		item.set("SUSTITUTO", 1);
		        		item.store(cct);
			        }
			        
			        //Asignar el primer número de factura
		        	//Rellenamos con 0s a la izquierda hasta completar la longitud de longitudFactura dígitos
		        	String auxNumFactura = "1";
			        while (auxNumFactura.length() < longitudNumFactura){
		        		auxNumFactura = "0"+auxNumFactura;
		        	}
			        
			        numFactura = iAnioActual + "-" + auxNumFactura;
		        }
		        
	        }
	        
	        return numFactura;
	        
	    } catch (Exception e) {
	    	
			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				e1.printStackTrace();
			}
	    	
	        throw new ISPACRuleException("Error al obtener el número de anuncio de solicitud.", e);
	    }     
	}
}
