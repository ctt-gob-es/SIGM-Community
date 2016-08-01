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
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

import es.dipucr.sigem.api.rule.common.DipucrProperties;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [eCenpri-Felipe #593 #828] 25.07.2012
 * Regla que divide en archivos pdf con cada uno de los anuncios y 
 * actualiza los contadores y números de página tanto del BOP como de los anuncios
 */
public class DividirAnunciosBopRule extends GenerateLiquidacionRecibos implements IRule 
{
	
	public final static String KEY_TEXTO_ANUNCIO = "bop.texto_num_anuncio";
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(DividirAnunciosBopRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Método validate
	 * Validamos que sólo haya un archivo de BOP firmado en el expediente
	 */
	@SuppressWarnings("rawtypes")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			IItemCollection collection = BopUtils.getBopsFirmados(rulectx);
			List listBOP = collection.toList();
			if (listBOP.size() == 0){
				rulectx.setInfoMessage("No hay ningún archivo de B.O.P. firmado.");
				return false;
			}
			else if (listBOP.size() > 1){
				rulectx.setInfoMessage("Hay más de un archivo de B.O.P. firmado.");
				return false;
			}
			else{
				return true;
			}
			
		} catch (ISPACException e) {
			throw new ISPACRuleException("Error al comprobar los archivos de BOP firmados", e);
		}
	}

	/**
	 * Método execute
	 */
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		ClientContext cct = null;
		String strQuery = null;
		StringBuffer sbQuery = null;
        IItemCollection collection = null;
        FileInputStream fisBop = null;
		
		try{
			//-----------------------------------------------------------	
			cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//-----------------------------------------------------------
			
			String numexp = rulectx.getNumExp();
			
			collection = BopUtils.getBopsFirmados(rulectx);
			IItem itemDocBopTramite1 = (IItem) collection.iterator().next();
			
			//INICIO [eCenpri-Felipe #828] 
			//Ponemos el boletín como público para que se pueda acceder mediante URL
			itemDocBopTramite1.set("ESTADO", "PÚBLICO");
			itemDocBopTramite1.store(cct);
			//FIN [eCenpri-Felipe #828]
			
			//Lo copiamos al trámite actual
			String strInfoPag = itemDocBopTramite1.getString("INFOPAG_RDE");
			File fileBop = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			fisBop = new FileInputStream(fileBop);
			
			//Dividimos en anuncios y actualizamos contadores
			PdfReader reader = new PdfReader((InputStream) fisBop);
			
			//[eCenpri-Felipe #593bis] Posibilidad de probar en la máquina de pruebas
			String TEXTO_NUM_ANUNCIO = DipucrProperties.getPropertyNoSingleton(KEY_TEXTO_ANUNCIO);
			if (StringUtils.isEmpty(TEXTO_NUM_ANUNCIO)){
				TEXTO_NUM_ANUNCIO = BopUtils._TEXTO_NUM_ANUNCIO; 
			}
			
			int numPags = reader.getNumberOfPages();
			
			//INICIO [eCenpri-Felipe #302#40]
			//Después de editar el BOP, normalmente cambia el número de páginas
			//Actualizamos los valores de los contadores para que tenga el valor real
			collection = entitiesAPI.getEntities("BOP_PUBLICACION", numexp);
			IItem itemPublicacion = (IItem) collection.iterator().next();
			Date dFechaPublicacion = itemPublicacion.getDate("FECHA");
			int numPagInicialBop = itemPublicacion.getInt("NUM_PAGINA");
			int numPagFinalBop = numPagInicialBop + numPags - 1;
			itemPublicacion.set("NUM_ULTIMA_PAGINA", numPagFinalBop);//#40
			//Para que no se vuelva a ejecutar la regla PrepararBopRule
			cct.setSsVariable("PREPARAR_BOP", "NO");
			itemPublicacion.store(cct);
			//Contador
			strQuery = "WHERE VALOR = 'num_pagina'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        IItem itemContadorPaginas = (IItem) collection.iterator().next();
	        itemContadorPaginas.set("SUSTITUTO", numPagFinalBop);
	        itemContadorPaginas.store(cct);
			//FIN [eCenpri-Felipe #302#40]
			
	        //Separamos en anuncios y vamos actualizando contadores individuales
			int pagInicio = 0;
			int pagFin = 0;
			int pagAnuncio = 0;
			String numAnuncio = null;
			int posNumAnuncio = 0;
			int posFinNumAnuncio = 0;
			IItem itemAnuncio = null;
			PdfTextExtractor pdfTextExtractor = new PdfTextExtractor(reader);			
			
			//Recorremos el documento del BOP
			for (int i=1; i <= numPags; i++){

				String texto = pdfTextExtractor.getTextFromPage(i);
				
				//Primer anuncio
				if (texto.contains(BopUtils._MARCA_FIN_INDICE)){
					pagInicio = i + 1;
				}
				
				//Resto de anuncios
				posNumAnuncio = texto.indexOf(TEXTO_NUM_ANUNCIO);
				if (posNumAnuncio > 0){
					pagFin = i;
					posNumAnuncio += TEXTO_NUM_ANUNCIO.length();
					posFinNumAnuncio = texto.indexOf("\n", posNumAnuncio);
					numAnuncio = texto.substring(posNumAnuncio, posFinNumAnuncio);
					
					//INICIO [eCenpri-Felipe #302#40]
					//Actualizamos el número de página del anuncio
					sbQuery = new StringBuffer();
					sbQuery.append("WHERE FECHA_PUBLICACION = '");
					sbQuery.append(dFechaPublicacion);
					sbQuery.append("' AND NUM_ANUNCIO_BOP = ");
					sbQuery.append(numAnuncio);
					logger.warn("consulta BOP: " + sbQuery.toString());
					collection = entitiesAPI.queryEntities("BOP_SOLICITUD", sbQuery.toString());					
					try{
						itemAnuncio = (IItem) collection.iterator().next();
					}
					catch(Exception ex){
						//[eCenpri-Felipe #905]
						logger.error("Obteniendo el anuncio " + numAnuncio 
								+ " entre las páginas " + pagInicio + " y " + pagFin);
						throw ex;
					}
					pagAnuncio = pagInicio + numPagInicialBop - 1;
					itemAnuncio.set("NUM_PAGINA", pagAnuncio);
					itemAnuncio.store(cct);
					//FIN [eCenpri-Felipe #302#40]
					
					//Creamos el archivos de anuncio asociado al trámite
					String sumario = itemAnuncio.getString("SUMARIO");
					String descripcionArchivo = numAnuncio + " - " + sumario;
					
					File fileAnuncio = FileTemporaryManager.getInstance().newFile("pdf");
					
					PdfUtil.obtenerSeccion(fileBop, fileAnuncio, pagInicio, pagFin);
					
					String tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, Constants.BOP._DOC_ANUNCIO);
					int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
					
					IItem itemDocAnuncio = DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, descripcionArchivo, fileAnuncio, "pdf");
					
					//INICIO [dipucr-Felipe #1088]
					int idDoc = itemDocAnuncio.getKeyInt();
					itemAnuncio.set("ID_DOCUMENTO", idDoc);
					itemAnuncio.store(cct);
					//FIN [dipucr-Felipe #1088]
					
					itemDocAnuncio.set("ESTADO", "PÚBLICO"); //#828 Para que se pueda acceder mediante URL
					itemDocAnuncio.store(cct);
					
					if(fileAnuncio != null && fileAnuncio.exists()) fileAnuncio.delete();
					
					//Ponemos la página inicio del próximo anuncio
					pagInicio = pagFin + 1;
				}
			}
			
			//INICIO [eCenpri-Felipe #302#40]
			//Comprobamos que el último anuncio corresponde con el valor del contador
			strQuery = "WHERE VALOR = 'num_anuncio'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
			IItem itemContadorAnuncios = (IItem) collection.iterator().next();
			int numAnuncioContador = Integer.valueOf(itemContadorAnuncios.getString("SUSTITUTO"));
			if (numAnuncioContador != Integer.valueOf(numAnuncio)){
				rulectx.setInfoMessage("Error: el último anuncio de BOP no se corresponde con " +
						"el valor del contador para el último anuncio generado");
				return false;
			}
			else{//Actualizamos el último anuncio del BOP
				itemPublicacion.set("NUM_ULTIMO_ANUNCIO", numAnuncio);
				itemPublicacion.store(cct);
			}
			cct.deleteSsVariable("PREPARAR_BOP");
			//FIN [eCenpri-Felipe #302#40]
			
			//[eCenpri-Felipe #828] No es necesario duplicar el almacenamiento
			//Copiamos también el BOP al trámite
//			IItem itemDocBopTramite2 = DipucrCommonFunctions.generarDocumentoDevuelveIItem
//				(rulectx, Constants.BOP._DOC_BOP, itemDocBopTramite1.getString("DESCRIPCION"));
//			fisBop = new FileInputStream(fileBop);
//			gendocAPI.attachTaskInputStream(null, rulectx.getTaskId(), itemDocBopTramite2.getKeyInt(),
//					fisBop, (int)fileBop.length(), Constants._MIMETYPE_PDF, Constants.BOP._DOC_BOP);
//			itemDocBopTramite2.set("EXTENSION", Constants._EXTENSION_PDF);
//			itemDocBopTramite2.store(cct);
			
			return null;
		}
		catch (Exception e) {
			throw new ISPACRuleException("Error al dividir los anuncios del BOP" +
					" y actualizar los contadores. " + e.getMessage(), e);
		}
		finally{
			if (null != fisBop){
				try {
					fisBop.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
