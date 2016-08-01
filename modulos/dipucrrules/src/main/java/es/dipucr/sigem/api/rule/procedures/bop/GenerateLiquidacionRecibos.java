package es.dipucr.sigem.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.beans.XPropertySet;
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
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerateLiquidacionRecibos {

	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(GenerateLiquidacionRecibosRule.class);
	
	//[ecenpri-Felipe Ticket #58 #91]
	protected final String _FUENTE_ANUNCIO = "Calibri";
	protected final int _TAMANIO_CABECERA = 10;
	protected final int _TAMANIO_ANUNCIO = 11;
	protected final String _FUENTE_COSTE = "Courier New";
	protected final int _TAMANIO_COSTE = 10;
	
	protected final String _DATE_FORMAT = "dd/MM/yyyy";
	protected final String _DATE_PRINT_FORMAT = "EEEE d 'de' MMMM 'de' yyyy";
	
	protected String STR_DocFactura = Constants.BOP._DOC_FACTURA;
	protected String STR_DocSinFacturar = Constants.BOP._DOC_ANUNCIOS_SIN_FACTURAR;
	
	//[eCenpri-Felipe #28,#453]
	protected final String _EXTENSION = Constants._EXTENSION_ODT;
	protected final String _MIMETYPE = Constants._MIMETYPE_ODT;

	private OpenOfficeHelper ooHelper = null;

	/**
	 * 
	 * @param rulectx
	 * @param tipoLiquidacion
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object generarLiquidacion(IRuleContext rulectx, String tipoLiquidacion) throws ISPACRuleException {
		return generarLiquidacion(rulectx, tipoLiquidacion, true);
	}
	
	/**
	 * 
	 * @param rulectx
	 * @param tipoLiquidacion
	 * @return
	 * @throws ISPACRuleException
	 */
	public Object previoLiquidacion(IRuleContext rulectx, String tipoLiquidacion) throws ISPACRuleException {
		return generarLiquidacion(rulectx, tipoLiquidacion, false);
	}
	
			
	/**
	 * 
	 * @param rulectx
	 * @param tipoLiquidacion
	 * @param bAsignarNumFactura - Si es falso, se hace un previo, sin asignar números de factura
	 * 							 - Si es verdadero, se realiza la facturación como tal
	 * @return
	 * @throws ISPACRuleException
	 */
	@SuppressWarnings("rawtypes")
	public Object generarLiquidacion(IRuleContext rulectx, String tipoLiquidacion, boolean bAsignarNumFactura) throws ISPACRuleException {

		ClientContext cct = null;
		String strQuery = null;
        IItemCollection collection = null; 
        Iterator it = null;
        IItem item = null;
        String fechaInicio = null;
        String fechaFin = null;
        Date dtFechaFin = null;
        String fechaFinFiltro = null;

        //INICIO [20.04.2010 eCenpri-Felipe; #58 #91]
        SimpleDateFormat df = new SimpleDateFormat(_DATE_FORMAT);
        SimpleDateFormat dfPrint = new SimpleDateFormat(_DATE_PRINT_FORMAT);
		CollectionDAO collectionJoin = null;
		ArrayList<IItem> listaAnuncios = null;
		ArrayList<IItem> listaAnunciosSinCIF = new ArrayList<IItem>();
		IItem itemSolicitud = null;
		IItem itemLiquidacion = null;
		int idSolicitud = Integer.MIN_VALUE;
		String nombreInteresado = null;
		String nombreInteresadoAnterior = null;
		//FIN [20.04.2010 eCenpri-Felipe; #58 #91]
        
		String cifnifInteresado = null;
		String cifnifInteresadoAnterior = null;
		String sumario = null;
		
		double costeTotal = -1;//eCenpri-Felipe; #58 #91
		double coste = -1;
		double costeTotalEntidad = 0;//eCenpri-Felipe; #58 #91
		double costeEntidad = 0;
		int anunciosEntidad = 0;
		
		String numFactura = "";
		
		try{
			//----------------------------------------------------------------------------------------------
			cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------

			//Abrir transacción
			cct.beginTX();

			//Obtenemos la fecha de inicio y de fecha fin para el filtro
	        strQuery = "WHERE NUMEXP = '"+ cct.getStateContext().getNumexp()+"'";
	        collection = entitiesAPI.queryEntities("BOP_LIQUIDACION", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext()){
	        	itemLiquidacion = (IItem)it.next();
	        	
	        	fechaInicio = itemLiquidacion.getString("FECHA_INICIO"); 
	        	fechaFin = itemLiquidacion.getString("FECHA_FIN");
	        	dtFechaFin = df.parse(fechaFin);
	        	cct.setSsVariable("FECHA_FACTURA", dfPrint.format(dtFechaFin));
	        	//[eCenpri-Felipe; #58 #91]. Añadimos un día a la fecha fin para que incluya el día con el between
	        	fechaFinFiltro = fechaFin + " 23:59:59"; //[eCenpri-Felipe #848] Última hora del dÃ­a actual
	        }

	        //[eCenpri-Felipe; #58 #91] - Consulta
	        //Cruzamos las tablas BOP_SOLICITUD y SPAC_EXPEDIENTES
	        //strQuery = "WHERE S.NUMEXP = E.NUMEXP AND E.NIFCIFTITULAR IS NOT NULL AND S.TIPO_FACTURACION = '" + tipoLiquidacion + "' " +
	        strQuery = "WHERE S.NUMEXP = E.NUMEXP AND S.TIPO_FACTURACION = '" + tipoLiquidacion + "' " +
	        	"AND (S.NUM_FACTURA IS NULL OR S.NUM_FACTURA='') " + 
//	        	"AND S.FECHA_PUBLICACION BETWEEN '" + fechaInicio +"' AND '" + fechaFinFiltro + "' ORDER BY E.NIFCIFTITULAR, S.FECHA_PUBLICACION"; //[eCenpri-Felipe #848]
				"AND S.FECHA_PUBLICACION >= '" + fechaInicio +"' AND S.FECHA_PUBLICACION < '" + fechaFinFiltro + "' ORDER BY E.NIFCIFTITULAR, S.FECHA_PUBLICACION";

			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("SPAC_EXPEDIENTES", "E");
			factory.addTable("BOP_SOLICITUD", "S");

			collectionJoin = factory.queryTableJoin(cct.getConnection(), strQuery);
			collectionJoin.disconnect();

			//Recorremos los registros recuperados
			while (collectionJoin.next())
			{
				item = (IItem) collectionJoin.value();
				
				cifnifInteresado = item.getString("E:NIFCIFTITULAR");
				nombreInteresado = item.getString("E:IDENTIDADTITULAR");
				
				//PRIMERA ITERACIÓN
				if (cifnifInteresadoAnterior == null)
				{
					cifnifInteresadoAnterior = cifnifInteresado;
					nombreInteresadoAnterior = nombreInteresado;
					
					if (bAsignarNumFactura){
						// NÃºmero de la nueva factura //eCenpri-Felipe; #58 #91
						numFactura = BopFacturasUtil.getNumFactura(rulectx, tipoLiquidacion);
						//[dipucr-Felipe #1311]
						String descripcion = BopFacturasUtil.getDescripcionByTipoFactura(tipoLiquidacion);
						cct.setSsVariable("NUM_FACTURA", descripcion + " Nº " + numFactura);
					}
					
			        //Cargamos los datos de la entidad en el documento
			        cargarDatosEntidad(item, entitiesAPI, cct);
			        //Inicializamos la lista de anuncios
			        listaAnuncios = new ArrayList<IItem>();
				}
				
				//RESTO DE ITERACIONES
				// [eCenpri-Felipe; #58 #91] - Si el anuncio no tiene CIF se mete en la lista de anuncios sin CIF
				// para sacarlos al final en un informe
				if (null == cifnifInteresado || cifnifInteresado.equals("")){
					listaAnunciosSinCIF.add(item);
				}
				else
				{
					if (!cifnifInteresado.equals(cifnifInteresadoAnterior))
					{
						//eCenpri-Felipe; #58 #91
//						cargarTotales(cct, anunciosEntidad, costeEntidad, costeTotalEntidad);
						cargarTotales(rulectx, anunciosEntidad, costeEntidad); //[eCenpri-Felipe #453]
						
						//[eCenpri-Felipe #474] Insertamos al ayuntamiento como participante para el "Registrar todo"
						ParticipantesUtil.insertarParticipanteByNIF(rulectx, rulectx.getNumExp(), cifnifInteresadoAnterior,
								ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_FISICA, "");
						
						// Crear un nuevo documento de factura
						//INICIO [eCenpri-Felipe #953]
						crearDocumento(rulectx, listaAnuncios, nombreInteresadoAnterior, STR_DocFactura, numFactura);
//						crearDocumento(rulectx, listaAnuncios, nombreInteresadoAnterior, STR_DocFactura);
						//FIN [eCenpri-Felipe #953]
						listaAnuncios.clear();
						borrarDatosSesion(cct);
						
						if (bAsignarNumFactura){
							//Se inserta un registro con el número de factura en la tabla bop_facturas
				        	BopFacturasUtil.insertarFactura(cct, entitiesAPI, numFactura, tipoLiquidacion, "Varios",
				        			cifnifInteresadoAnterior, nombreInteresadoAnterior, costeEntidad, costeTotalEntidad, dtFechaFin);
				        	
							// Número de la nueva factura //eCenpri-Felipe; #58 #91
							numFactura = BopFacturasUtil.getNumFactura(rulectx, tipoLiquidacion);
							//[dipucr-Felipe #1311]
							String descripcion = BopFacturasUtil.getDescripcionByTipoFactura(tipoLiquidacion);
							cct.setSsVariable("NUM_FACTURA", descripcion + " Nº " + numFactura);
						}

						cifnifInteresadoAnterior = cifnifInteresado;
						nombreInteresadoAnterior = nombreInteresado;
						costeTotalEntidad = 0.0;
						costeEntidad = 0.0;//eCenpri-Felipe; #58 #91
						anunciosEntidad = 0;
			        	
				        //Datos de la entidad en sesión
				        cargarDatosEntidad(item, entitiesAPI, cct);
				        //Inicializamos la lista de anuncios
				        listaAnuncios = new ArrayList<IItem>();
					}
					
					sumario = item.getString("S:SUMARIO");
					if (sumario == null) sumario = "";

					coste = item.getDouble("S:COSTE");
					if (coste < 0) coste = 0.0;
					
					//eCenpri-Felipe; #58 #91
					costeTotal = item.getDouble("S:COSTE_TOTAL");
					if (costeTotal < 0) costeTotal = 0.0;

					anunciosEntidad ++;
					costeEntidad = costeEntidad + coste; 
					costeTotalEntidad = costeTotalEntidad + costeTotal;//eCenpri-Felipe; #58 #91
					
					if (bAsignarNumFactura){
						//Metemos el numero de factura en la solicitud
						idSolicitud = item.getInt("S:ID");
						itemSolicitud = entitiesAPI.getEntity("BOP_SOLICITUD", idSolicitud);
						itemSolicitud.set("NUM_FACTURA", numFactura);
						itemSolicitud.store(cct);
					}

		        	//eCenpri-Felipe; #58 #91
					//meterAnuncio(xComponent, numexp, sumario, costeTotal, df.format(fechaPublicacion));
		        	listaAnuncios.add(item);
				}
			}

			//GENERAR EL DOCUMENTO PARA EL ÚLTIMO INTERESADO
			if (cifnifInteresadoAnterior != null)
			{
				//eCenpri-Felipe; #58 #91
				//meterTotal(xComponent, String.valueOf(anunciosEntidad), costeTotalEntidad);
//				cargarTotales(cct, anunciosEntidad, costeEntidad, costeTotalEntidad);
				cargarTotales(rulectx, anunciosEntidad, costeEntidad); //[eCenpri-Felipe #453]
				
				//INICIO [eCenpri-Felipe #474] Insertamos al ayuntamiento como participante para el "Registrar todo"
				ParticipantesUtil.insertarParticipanteByNIF(rulectx, rulectx.getNumExp(), cifnifInteresadoAnterior,
						ParticipantesUtil._TIPO_INTERESADO, ParticipantesUtil._TIPO_PERSONA_FISICA, "");
				
				// Crear un nuevo documento de factura
				//INICIO [eCenpri-Felipe #953]
				crearDocumento(rulectx, listaAnuncios, nombreInteresadoAnterior, STR_DocFactura, numFactura);
//				crearDocumento(rulectx, listaAnuncios, nombreInteresadoAnterior, STR_DocFactura);
				//FIN [eCenpri-Felipe #953]
				listaAnuncios.clear();
				borrarDatosSesion(cct);
				
				if (bAsignarNumFactura){
					//Se inserta un registro con el número de factura en la tabla bop_facturas
		        	BopFacturasUtil.insertarFactura(cct, entitiesAPI, numFactura, tipoLiquidacion, "Varios",
		        			cifnifInteresadoAnterior, nombreInteresadoAnterior, costeEntidad, costeTotalEntidad, dtFechaFin);
				}
			}
			
			//eCenpri-Felipe; #58 #91 - La lista de anuncios sin CIF la imprimimos en el informe
			if (!listaAnunciosSinCIF.isEmpty()){
				//INICIO [eCenpri-Felipe #953]
				crearDocumento(rulectx, listaAnunciosSinCIF, "", STR_DocSinFacturar, "");
//				crearDocumento(rulectx, listaAnunciosSinCIF, "", STR_DocSinFacturar);
				//FIN [eCenpri-Felipe #953]
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
				logger.error(e1.getMessage(), e1);
			}

			throw new ISPACRuleException("Error al obtener la liquidación de cobros de recibos.", e);
		}     
		finally
		{
			try
			{
				this.borrarDatosSesion(cct);
			}
			catch (ISPACException ie)
			{
				throw new ISPACRuleException("Error al borrar las variables de sesión.", ie);
			}
		}
	}
	
	
	/**
	 * Imprime la lista de anuncios de la entidad en el documento pasado como parÃ¡metro
	 * Se posiciona en la lÃ­nea de la tabla de anuncios para empezar a insertar
	 * @param listaAnuncios
	 * @param xComponent
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 * @throws NoSuchElementException 
	 */
	private void imprimirListaAnuncios(ArrayList<IItem> listaAnuncios, XComponent xComponent, boolean bFactura)
			throws ISPACException, NoSuchElementException {
		
		//obtenemos la posición en el archivo, buscando la cadena "[LISTA_ANUNCIOS]" y reemplazando
		XTextDocument xTextDocument = (XTextDocument)UnoRuntime.queryInterface(XTextDocument.class, xComponent);
		XText xText = xTextDocument.getText();
//		XSearchable xSearchable = (XSearchable) UnoRuntime.queryInterface(XSearchable.class, xTextDocument);
		XReplaceable xReplaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, xTextDocument);
//		XSearchDescriptor xSearchDescriptor = (XSearchDescriptor) xSearchable.createSearchDescriptor();
		XReplaceDescriptor xReplaceDescriptor = (XReplaceDescriptor) xReplaceable.createReplaceDescriptor();
//		xSearchDescriptor.setSearchString("[LISTA_ANUNCIOS]");
		xReplaceDescriptor.setSearchString("[LISTA_ANUNCIOS]");
//      Object founded = xSearchable.findFirst(xSearchDescriptor);
		Object founded = xReplaceable.findFirst(xReplaceDescriptor);
        XTextRange xTextRange = (XTextRange) UnoRuntime.queryInterface(XTextRange.class, founded); 
        XTextCursor xTextCursor = xText.createTextCursorByRange(xTextRange);
        
        //Reemplazamos
        xReplaceDescriptor.setReplaceString("");
        xReplaceable.replaceAll(xReplaceDescriptor);
		
	    //Vamos insertando anuncios
        IItem item = null;
		for (int i=0; i<listaAnuncios.size(); i++){
			item = listaAnuncios.get(i);
			boolean bUltimo = (i == listaAnuncios.size() - 1); //[eCenpri-Felipe #453]
			if (bFactura){
				meterAnuncio(xText, xTextCursor, item, bUltimo);
			}
			else{
				meterAnuncioInforme(xText, xTextCursor, item);
			}
		}
		
	}

	/**
	 * Pone en la sesión los valores totales obtenidos de recorrer la lista de anuncios
	 * @param cct
	 * @param anunciosEntidad - NÃºmero de anuncios de la entidad
	 * @param costeEntidad - Coste total sin IVA
	 * @param costeTotalEntidad - Coste total con IVA
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 */
	private void cargarTotales(IRuleContext rulectx, int anunciosEntidad, double costeEntidad) throws ISPACException {
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		cct.setSsVariable("COSTE", formatearCoste(costeEntidad));
		//INICIO [eCenpri-Felipe #453]
//		cct.setSsVariable("COSTE_TOTAL_TABLA", formatearCoste(costeTotalEntidad));
//		cct.setSsVariable("COSTE_IVA", formatearCoste(costeTotalEntidad - costeEntidad));
//		cct.setSsVariable("COSTE_TOTAL", alinearDerecha(formatearCoste(costeTotalEntidad)));
		float iva = BopUtils.getIVAFacturacion(rulectx);
		double ivaTotal = iva * costeEntidad;
		double costeTotalEntidad = costeEntidad + ivaTotal;
		cct.setSsVariable("COSTE_IVA", formatearCoste(ivaTotal));
		cct.setSsVariable("COSTE_TOTAL", alinearDerecha(formatearCoste(costeEntidad)));
		cct.setSsVariable("COSTE_TOTAL_TABLA", formatearCoste(costeTotalEntidad));
		//FIN [eCenpri-Felipe #453]
		cct.setSsVariable("NUM_ANUNCIOS", String.valueOf(anunciosEntidad));
	}

	/**
	 * Carga los datos postales de la entidad (ayuntamiento o diputación) en la sesión
	 * @param numexp
	 * @param entitiesAPI
	 * @param cct
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 */
	private void cargarDatosEntidad(IItem item, IEntitiesAPI entitiesAPI, ClientContext cct) throws ISPACException {
		
    	cct.setSsVariable("NOMBRE", item.getString("E:IDENTIDADTITULAR"));
    	cct.setSsVariable("DIRNOT", item.getString("E:DOMICILIO"));
    	cct.setSsVariable("C_POSTAL", item.getString("E:CPOSTAL"));
    	cct.setSsVariable("LOCALIDAD", item.getString("E:CIUDAD"));
    	cct.setSsVariable("PROVINCIA", item.getString("E:REGIONPAIS"));
    	cct.setSsVariable("CIF", item.getString("E:NIFCIFTITULAR"));
	}
	
	/**
	 * Borra todos los datos de la sesión antes de generar un nuevo documento
	 * @param cct
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 */
	private void borrarDatosSesion(ClientContext cct) throws ISPACException{
		
		cct.deleteSsVariable("NOMBRE");
		cct.deleteSsVariable("DIRNOT");
		cct.deleteSsVariable("C_POSTAL");
		cct.deleteSsVariable("LOCALIDAD");
		cct.deleteSsVariable("PROVINCIA");
		cct.deleteSsVariable("CIF");
		cct.deleteSsVariable("COSTE");
		cct.deleteSsVariable("COSTE_TOTAL");
		cct.deleteSsVariable("COSTE_IVA");
		cct.deleteSsVariable("NUM_ANUNCIOS");
		cct.deleteSsVariable("NUM_FACTURA");
	}

	/**
	 * Mete el texo relativo a un anuncio en el documento y en la posiciÃ³n pasados como parÃ¡metro
	 * @param xComponent - Objeto relativo al documento
	 * @param xSentenceCursor - cursor (posición dentro del documento)
	 * @param numexp
	 * @param sumario
	 * @param coste
	 * @param fechaPublicacion
	 * @param ultimo [eCenpri-Felipe #453]
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 */
	private void meterAnuncio (XText xText, XTextCursor xTextCursor, IItem item, boolean ultimo) throws ISPACException
	{
		SimpleDateFormat df = new SimpleDateFormat(_DATE_FORMAT);
		String numexp = null;
		String sumario = null;
		double costeBase = -1;
		Date fechaPublicacion = null;
		String sFechaPublicacion = null;
		int iva = -1;
		
		try
		{
			//Tomamos los valores
			numexp = item.getString("E:NUMEXP");
			sumario = item.getString("S:SUMARIO");
			if (sumario == null)
			{
				sumario = "";
			}
			//[eCenpri-Felipe #453]
			//Sustituimos el coste total del anuncio por su subtotal (coste base)
//			costeTotal = item.getDouble("S:COSTE_TOTAL");
			costeBase = item.getDouble("S:COSTE");
			if (costeBase < 0)
			{
				costeBase = 0.0;
			}
			
			fechaPublicacion = item.getDate("S:FECHA_PUBLICACION");
			sFechaPublicacion = df.format(fechaPublicacion);

		    //ecenpri-felipe #58 #91
		    XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		    
		    //Cabecera del anuncio
		    xTextProps.setPropertyValue("CharFontName", _FUENTE_ANUNCIO);
		    xTextProps.setPropertyValue("CharHeight", new Float(_TAMANIO_CABECERA));
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
		    //INICIO [eCenpri-Felipe #453]
//		    xText.insertString(xTextCursor, sFechaPublicacion + "\t" + numexp + "\t" + item.getString("S:URGENCIA") + "\t\t", false);
		    //Cabecera
		    String sCabecera = "F.PUBLICACIÓN\tNº EXPEDIENTE\tURGENCIA\tCARACTERES\tPRECIO\tIVA\t\tSUBTOTAL\r";
		    xText.insertString(xTextCursor, sCabecera, false);
		    
		    //Datos del anuncio
		    xTextProps.setPropertyValue("CharHeight", new Float(_TAMANIO_ANUNCIO));
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
			StringBuffer sbDatosAnuncio = new StringBuffer();
			sbDatosAnuncio.append(sFechaPublicacion);
			sbDatosAnuncio.append("\t");
			sbDatosAnuncio.append(numexp);
			sbDatosAnuncio.append("\t");
			sbDatosAnuncio.append(item.getString("S:URGENCIA"));
			sbDatosAnuncio.append("\t");
			sbDatosAnuncio.append(item.getInt("S:CARACTERES"));
			sbDatosAnuncio.append("\t");
			//[eCenpri-Felipe #561] Hasta 4 decimales en el coste caracter
//			sbDatosAnuncio.append(formatearCoste(item.getDouble("S:COSTE_CARACTER")));
			sbDatosAnuncio.append(formatearCosteCaracter(item.getDouble("S:COSTE_CARACTER")));
			sbDatosAnuncio.append("\t");
			iva = (int)(item.getDouble("S:TIPO_IVA_CARACTERES")*100);
			sbDatosAnuncio.append(iva + "%");
			sbDatosAnuncio.append("\t");
			xText.insertString(xTextCursor, sbDatosAnuncio.toString(), false);
		    //FIN [eCenpri-Felipe #453]
		    
		    //Coste- Courier New 10 Negrita
			xTextProps.setPropertyValue("CharFontName", _FUENTE_COSTE);
		    xTextProps.setPropertyValue("CharHeight", new Float(_TAMANIO_COSTE));
		    xText.insertString(xTextCursor, alinearDerecha(formatearCoste(costeBase)), false);
		    
		    //Desglose costes y Sumario- Calibri 11 sin negrita
		    xTextProps.setPropertyValue("CharFontName", _FUENTE_ANUNCIO);
		    xTextProps.setPropertyValue("CharHeight", new Float(_TAMANIO_ANUNCIO));
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
		    //[eCenpri-Felipe #453] Ponemos la palabra Concepto
		    xText.insertString(xTextCursor, "\rConcepto: ", false);
		    
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
		    if (!ultimo){
		    	xText.insertString(xTextCursor, sumario + "\r\r\r", false);
		    }
		    else{
		    	xText.insertString(xTextCursor, sumario + "\r", false);
		    }
		    
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	}
	
	/**
	 * 
	 * @param xComponent
	 * @param xTextCursor
	 * @param item
	 * @throws ISPACException
	 * @author Felipe-ecenpri #58 #91
	 */
	private void meterAnuncioInforme(XText xText, XTextCursor xTextCursor, IItem item) throws ISPACException
	{
		String titular = null;
		
		try{
		    XPropertySet xTextProps = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, xTextCursor);
		    
		    //NÃºmero de expediente
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.BOLD));
		    xText.insertString(xTextCursor, item.getString("E:NUMEXP"), false);
		    
		    //Titular
		    xTextProps.setPropertyValue("CharWeight", new Float(com.sun.star.awt.FontWeight.NORMAL));
		    titular = item.getString("E:IDENTIDADTITULAR");
		    if (null != titular && !titular.equals("")){
		    	xText.insertString(xTextCursor, " - " + titular, false);
		    }
		    xText.insertString(xTextCursor, "\r", false);
		    //Sumario
		    xText.insertString(xTextCursor, item.getString("S:SUMARIO") + "\r\r", false);
		}
		catch(Exception e)
		{
			throw new ISPACException(e);
		}
	
	}
	
	/**
	 * Formatea el coste pasado como parámetro con dos posiciones decimales redondeadas,
	 * el simbolo del euro y alineado a la derecha
	 * @param coste
	 * @return Coste formateado
	 * @author Felipe-ecenpri #58 #91
	 */
	private String formatearCoste(double coste){
		
		DecimalFormat df = new DecimalFormat(Constants.BOP._COSTE_FORMAT);
		String result = df.format(coste) + " €";
		return result;
		
	}
	
	/**
	 * [eCenpri-Felipe #561] 01.02.2012
	 * Formatea el coste carácter pasado como parámetro con hasta CUATRO posiciones 
	 * decimales redondeadas, el simbolo del euro y alineado a la derecha
	 * @param coste
	 * @return Coste formateado
	 */
	private String formatearCosteCaracter(double coste){
		
		DecimalFormat df = new DecimalFormat(Constants.BOP._COSTE_CARACTER_FORMAT);
		String result = df.format(coste) + " €";
		return result;
		
	}
	
	/**
	 * Añade espacios a la cadena pasada como parámetro
	 * @param cadena
	 * @return
	 * @author Felipe-ecenpri #58 #91
	 */
	private String alinearDerecha(String cadena){
		int espacios = 12 - cadena.length();
		for (int i = 0; i < espacios; i++){
			cadena = " " + cadena;
		}
		return cadena;
	}
	
	/**
	 * 
	 * @param rulectx Contexto de la regla
	 * @param listaAnuncios Lista de anuncios de la factura
	 * @param nombreFichero Nombre del fichero
	 * @param nombrePlantilla Nombre de la plantilla
	 * @param numFactura [eCenpri-Felipe #953] Incluir el número de factura
	 * @throws Exception
	 * @author Felipe-ecenpri
	 */
	@SuppressWarnings("rawtypes")
	protected void crearDocumento(IRuleContext rulectx, ArrayList<IItem> listaAnuncios, 
			String nombreFichero, String nombrePlantilla, String numFactura) throws Exception{
		
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
			
			// Crear un nuevo documento de factura
			//[eCenpri-Felipe #453,#28]
			IItem itemDoc = DocumentosUtil.generarDocumento(rulectx, nombrePlantilla, "borrar");
			String strInfoPag = itemDoc.getString("INFOPAG");
	    	file1 = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			ooHelper = OpenOfficeHelper.getInstance();
			xComponent = ooHelper.loadDocument("file://" + file1.getPath());
			
			//AÃ±adimos los anuncios
			boolean bFactura = nombrePlantilla.equals(STR_DocFactura);
			imprimirListaAnuncios(listaAnuncios, xComponent, bFactura);
			
			//Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(_EXTENSION);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			file = new File(fileName);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(),"");
			file1.delete();
			
	        int tpdoc = DocumentosUtil.getTipoDoc(cct, nombrePlantilla, DocumentosUtil.BUSQUEDA_EXACTA, false);
	        
			if (StringUtils.isEmpty(nombreFichero)){
				nombreCompleto = nombrePlantilla;
			}
			else{
				nombreCompleto = nombrePlantilla + " - " + nombreFichero;
			}
			//INICIO [eCenpri-Felipe #953]
			if (StringUtils.isNotEmpty(numFactura)){
				nombreCompleto = nombreCompleto.replace(nombrePlantilla, numFactura);
			}
			//FIN [eCenpri-Felipe #953]
			
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
	        	entitiesAPI.deleteDocument(doc);
	        }
	        ooHelper.dispose();
		}
		catch(Exception e){
			logger.error("Error al crear el documento " + nombreCompleto + "." + e.getMessage());
			throw new Exception("Error al crear el documento " + nombreCompleto + "." + e.getMessage());
		}
	}
	
}
