package es.dipucr.sigem.api.rule.procedures.terceros;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.container.NoSuchElementException;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.api.rule.procedures.bop.GenerateLiquidacionRecibosRule;


/**
 * [dipucr-Felipe 3#592]
 * Regla que actualiza los terceros a partir del excel
 * @author Felipe
 * @since 26.10.2017
 */
public class ActualizarTercerosExcelRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GenerateLiquidacionRecibosRule.class);
	
	public static String _DOC_TABLA_TERCEROS = "Tabla de terceros";
	public static String _DOC_ERRORES = "Carga de terceros - Errores";
	
	private OpenOfficeHelper ooHelper = null;
	protected final String _EXTENSION = Constants._EXTENSION_ODT;
	protected final String _MIMETYPE = Constants._MIMETYPE_ODT;
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación de la fase y el trámite
	 */
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		int linea = Integer.MIN_VALUE;
		
		try{
			
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
			InvesflowAPI invesflowAPI = (InvesflowAPI) cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IThirdPartyAPI thirdpartyAPI = cct.getAPI().getThirdPartyAPI();
			//*********************************************
			
			String numexp = rulectx.getNumExp();
			
			//Obtenemos el documento de justificante
			String strQuery = "NOMBRE = '" + _DOC_TABLA_TERCEROS + "'";
			IItemCollection collection = entitiesAPI.getDocuments(numexp, strQuery, "FDOC DESC");
			IItem itemDocTabla = (IItem)collection.iterator().next();
			String strInfoPag = itemDocTabla.getString("INFOPAG");
			File fileTabla = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			
			//Recuperamos el excel
			List listTerceros = ExcelUtils.getAllBySheet(fileTabla, 0); //Primera y única hoja
			List tercero = null;
			String cifnif, nombre, ape1, ape2, direccion, cpostal, municipio, provincia;
			ArrayList<String> listErrores = new ArrayList<String>();
			
			//Recorremos la lista de terceros
			//Empezamos desde el segundo porque el segundo será la cabecera
			for(int i = 1; i < listTerceros.size(); i++){
				linea = i + 1;
				tercero = (List) listTerceros.get(i);
				
				//cif/nif
				cifnif = (String) tercero.get(2);
				cifnif = cifnif.replaceAll(" ", ""); //Eliminamos espacios en blanco
				
				if (StringUtils.isEmpty(cifnif)){
					listErrores.add(crearError(linea, "NIF ausente", "El campo CIF/NIF es obligatorio"));
					continue;
				}
				
				//Vemos si ya existe
				IThirdPartyAdapter [] arrTerceros = thirdpartyAPI.lookup(cifnif);
				boolean bExisteTercero = (arrTerceros.length > 0);
				
				if (bExisteTercero){
					
					//Nombre
					nombre = (String) tercero.get(3);
					if (StringUtils.isEmpty(nombre)){
						listErrores.add(crearError(linea, cifnif, "El campo NOMBRE es obligatorio"));
						continue;
					}
					
					//Apellido 1
					ape1 = (String) tercero.get(4);
					if (StringUtils.isEmpty(ape1)){
						listErrores.add(crearError(linea, cifnif, "El campo APELLIDO 1 es obligatorio para las personas físicas 'F'"));
						continue;
					}
					
					//Apellido 2
					ape2 = (String) tercero.get(5);
					
					//Dirección
					direccion = (String) tercero.get(6);
					if (StringUtils.isEmpty(direccion)){
						listErrores.add(crearError(linea, cifnif, "El campo DIRECCIÓN es obligatorio"));
						continue;
					}
					
					//C.Postal
					cpostal = (String) tercero.get(7);
					if (StringUtils.isEmpty(cpostal)){
						listErrores.add(crearError(linea, cifnif, "El campo CÓDIGO POSTAL es obligatorio"));
						continue;
					}
					else{
						try{
							cpostal = String.valueOf(Double.valueOf(cpostal).intValue());
						}
						catch (Exception e) {
							listErrores.add(crearError(linea, cifnif, "El campo CÓDIGO POSTAL debe ser de tipo numérico"));
							continue;
						}
					}
					
					//Municipio
					municipio = (String) tercero.get(8);
					if (StringUtils.isEmpty(municipio)){
						listErrores.add(crearError(linea, cifnif, "El campo MUNICIPIO es obligatorio"));
						continue;
					}
					
					//Provincia
					provincia = (String) tercero.get(9);
					if (StringUtils.isEmpty(provincia)){
						listErrores.add(crearError(linea, cifnif, "El campo PROVINCIA es obligatorio"));
						continue;
					}
					
					//Recorremos todos los terceros que tengan ese identificador (NIF) para actualizarlos
					for (IThirdPartyAdapter bbddTercero : arrTerceros){
						
						int idPerson = Integer.valueOf(bbddTercero.getIdExt());
						thirdpartyAPI.updateThirdParty(idPerson, nombre, ape1, ape2, provincia, municipio, cpostal, direccion);
					}
					
				}
			}
			
			//Creamos la lista de errores
			if (listErrores.size() > 0){
			
				crearDocumento(rulectx, listErrores, _DOC_ERRORES, _DOC_ERRORES);				
				rulectx.setInfoMessage("Existen errores en la tabla de terceros");
			}
		}
		catch (Exception e) {
			logger.error("Error no controlado en la actualización de los terceros. Línea " + linea + ". " + e.getMessage(), e);
			throw new ISPACRuleException
				("Error no controlado en la actualización de los terceros. Línea " + linea + ". " + e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * 
	 * @param linea
	 * @param descripcion
	 * @return
	 */
	private String crearError(int linea, String cifnif, String descripcion) {

		StringBuffer error = new StringBuffer()
			.append("Línea ")
			.append(linea)
			.append("\t")
			.append(cifnif)
			.append("\t")
			.append(descripcion);
		
		return error.toString();
	}
	
	/**
	 * 
	 * @param rulectx Contexto de la regla
	 * @param listaErrores Lista de anuncios de la factura
	 * @param nombreFichero Nombre del fichero
	 * @param nombrePlantilla Nombre de la plantilla
	 * @throws Exception
	 * @author Felipe-ecenpri
	 */
	@SuppressWarnings("rawtypes")
	protected void crearDocumento(IRuleContext rulectx, ArrayList<String> listaErrores, 
			String nombreFichero, String nombrePlantilla) throws Exception{
		
		IItemCollection collection = null;
		Iterator it = null;
		XComponent xComponent = null;
		File file = null;
		File file1 = null;
		String nombreCompleto = null;
		
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------
			
			// Crear un nuevo documento de errores
			IItem itemDoc = DocumentosUtil.generarDocumento(rulectx, nombrePlantilla, "borrar");
			String strInfoPag = itemDoc.getString("INFOPAG");
			logger.info("InfoPag del documento generado: " + strInfoPag);
	    	file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//Añadimos los anuncios
			imprimirListaErrores(listaErrores, xComponent);
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(_EXTENSION);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			file1.delete();
			
			//Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, nombrePlantilla, DocumentosUtil.BUSQUEDA_EXACTA, false);

			if (StringUtils.isEmpty(nombreFichero)){
				nombreCompleto = nombrePlantilla;
			}
			else{
				nombreCompleto = nombrePlantilla + " - " + nombreFichero;
			}

			IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, nombreCompleto, file, _EXTENSION);
			
			entityDoc.set("DESTINO", nombreFichero);
			entityDoc.store(cct);
			file.delete();
			
			//Borra los documentos intermedios del gestor documental
	        collection = DocumentosUtil.getDocumentsByDescripcion(rulectx.getNumExp(), rulectx, "borrar");
	        it = collection.iterator();
	        while (it.hasNext())
	        {
	        	IItem doc = (IItem)it.next();
	        	logger.info("Borrar (ID): " + doc.getString("ID"));
	        	logger.info("Borrar (NUMEXP): " + doc.getString("NUMEXP"));
	        	logger.info("Borrar (NOMBRE): " + doc.getString("NOMBRE"));
	        	logger.info("Borrar (DESCRIPCION): " + doc.getString("DESCRIPCION"));
	        	entitiesAPI.deleteDocument(doc);
	        }
		}
		catch(Exception e){
			throw new Exception("Error al crear el documento " + nombreCompleto + "." + e.getMessage());
		}
		finally{
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}
	}
	
	/**
	 * Imprime la lista de anuncios de la entidad en el documento pasado como parámetro
	 * Se posiciona en la línea de la tabla de anuncios para empezar a insertar
	 * @param listaErrores
	 * @param xComponent
	 * @throws ISPACException
	 * @throws NoSuchElementException 
	 */
	private void imprimirListaErrores(ArrayList<String> listaErrores, XComponent xComponent)
			throws ISPACException, NoSuchElementException {
		
		//obtenemos la posición en el archivo, buscando la cadena "[LISTA_ANUNCIOS]" y reemplazando
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		XText xText = xTextDocument.getText();
		XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
		XReplaceDescriptor xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
		xReplaceDescriptor.setSearchString("[LISTA_ERRORES]");
		Object founded = xReplaceable.findFirst(xReplaceDescriptor);
        XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, founded); 
        XTextCursor xTextCursor = xText.createTextCursorByRange(xTextRange);
        
        //Reemplazamos
        xReplaceDescriptor.setReplaceString("");
        xReplaceable.replaceAll(xReplaceDescriptor);
		
	    //Vamos insertando anuncios
        String error = null;
		for (int i=0; i<listaErrores.size(); i++){
			error = listaErrores.get(i);
			xText.insertString(xTextCursor, error + "\n", false);
		}
		
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
