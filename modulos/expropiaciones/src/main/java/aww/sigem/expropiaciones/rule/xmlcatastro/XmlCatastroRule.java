package aww.sigem.expropiaciones.rule.xmlcatastro;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.expedients.CommonData;
import ieci.tdw.ispac.api.expedients.Expedients;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import aww.sigem.expropiaciones.catastro.entidades.CargaInicial;
import aww.sigem.expropiaciones.catastro.entidades.Expropiado;
import aww.sigem.expropiaciones.catastro.entidades.Finca;
import aww.sigem.expropiaciones.catastro.parser.ExprParser;
import aww.sigem.expropiaciones.rule.test.ExpropiacionesIniciarProcedimientoTestRule;
import aww.sigem.expropiaciones.util.Propiedades;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExcelUtils;

/**
 * Inicializa valores de Finca y Expropiado con los valores del XML de Catastro
 * Ejecutar al terminar la Fase de Inicio de la Expropiación Forzosa
 * 
 */
public class XmlCatastroRule implements IRule {
	
	/** Logger de la clase. */
	private static final Logger logger = 
		Logger.getLogger(ExpropiacionesIniciarProcedimientoTestRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		try{
			
			// Comprobar que se haya anexado un documento
			IItemCollection docsCollection = DocumentosUtil.getDocumentosByCodTramites(rulectx, rulectx.getNumExp(), "excel_expropiaci");
						
			// Se debe anexar algún fichero.
			if (docsCollection==null || docsCollection.toList().size()==0){
				rulectx.setInfoMessage("No se puede iniciar el tramite del expediente "+rulectx.getNumExp()+" ya que no se ha adjuntado el excell de la expropiación");
				return false;
			} 
			
			// Sólo se debe haber anexado un documento.
			if (docsCollection.toList().size()>1){
				rulectx.setInfoMessage("Sólo se debe adjuntar un fichero y que contenga la extensión excell.");
				return false;
			}
	       
        	// Comprobar que se haya anexado un documento
			docsCollection = DocumentosUtil.getDocumentosByCodTramites(rulectx, rulectx.getNumExp(), "xml-catastro");
						
			// Se debe anexar algún fichero.
			if (docsCollection==null || docsCollection.toList().size()==0){
				rulectx.setInfoMessage("No se puede iniciar el tramite del expediente "+rulectx.getNumExp()+" ya que no se ha adjuntado un fichero XML de catastro");
				return false;
			} 
			
			// Sólo se debe haber anexado un documento.
			if (docsCollection.toList().size()>1){
				rulectx.setInfoMessage("Sólo se debe adjuntar un fichero y que contenga la extensión XML.");
				return false;
			}
			
			
				
			return true;			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al comprobar el estado de los documentos adjuntos", e);
	      
	    } 
	}

	
	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			
			//Abrir el XML de Catastro
			//Se pondra como un campo de Texto en SIGEM
			//TODO: Si es demasiado grande, ver si se puede sacar de un fichero adjunto
			HashMap<String, String> expropiadosMap = new HashMap<String, String>();
			HashMap<String, String> expropiadosMapSinDNI = new HashMap<String, String>();
			HashMap<String, String> expropiadosExpMap = new HashMap<String, String>();
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			
			String xmlCatastro;

			
			
			////logger.warn("Obteniendo información de los documentos del trámite");
			IItemCollection taskDocumentosColeccion = DocumentosUtil.getDocumentosByCodTramites(rulectx, rulectx.getNumExp(), "xml-catastro");
			
			Iterator<IItem> itDocumentos = taskDocumentosColeccion.iterator();
			
			if(!itDocumentos.hasNext()){
				//logger.warn("No hay Documentos");
			}
			
			String refDoc = null;
			java.sql.Timestamp fDoc = null;
			while(itDocumentos.hasNext()){
				//logger.warn("Documento");
				IItem documento = (IItem) itDocumentos.next();
				java.sql.Timestamp fItem = (java.sql.Timestamp) documento.get("FDOC");
				//logger.warn("FDoc: " + fItem);
				if(fDoc!=null) {
					if(fItem.after(fDoc)) {
						//logger.warn("Este item es mas nuevo");
						fDoc = fItem;
						refDoc = (String) documento.get("INFOPAG");
						//logger.warn("RefDoc: " + refDoc); 
					}
				} else {
					fDoc = fItem;
					refDoc = (String) documento.get("INFOPAG");
					//logger.warn("RefDoc: " + refDoc);
				}
				
			} 
			
			//logger.warn("Creo el outputstream");
			OutputStream osDocumento = new ByteArrayOutputStream();
			//logger.warn("Obteniendo documento y guardándolo en el OutputStream");
			gendocAPI.getDocument(cct, refDoc, osDocumento); 
			//logger.warn("Documento obtenido");
			
			xmlCatastro=osDocumento.toString();
			
			xmlCatastro=xmlCatastro.replaceAll("\\t", "");
			xmlCatastro=xmlCatastro.replaceAll("\\n", "");
			//String xmlCodificado="";			
			
			//Parsear el XML y obtener estructuras para fincas y Expropiados
			ExprParser exprParser = new ExprParser();
			
            SAXParser parser = new SAXParser();
            
            parser.setContentHandler(exprParser);
            parser.setErrorHandler(exprParser);
            //logger.warn("** Parsea el xml");
              //** Parsea el xml
            
            InputSource source;
            source = new InputSource(new ByteArrayInputStream(xmlCatastro.getBytes()));
            
            try{
            	parser.parse(source);
            }
            catch(NullPointerException e){
            	logger.error(e.getMessage(), e);
    			throw new ISPACRuleException("Error. " + e.getMessage(), e);
            }
         
              //** Listado de fincas 
			List<Finca> listadoFincas = exprParser.getListaFincas();
			
			
			anadirSuperficieFincas(rulectx, listadoFincas);
				
			//logger.warn("** Por cada finca");
			//Por cada finca:
			Iterator<Finca> itFincas = listadoFincas.iterator();
			Finca finca = null;			
		
			while (itFincas.hasNext()) {
				finca = (Finca) itFincas.next();
				if(finca!=null){
	
					// Habra que recorrer las subparcelas que tiene la finca y
					// buscar la de mayor tamaño.
	
					// logger.warn("**** Abrir un expediente de finca en Sigem");
					// Abrir un expediente de finca en Sigem
					Expedients expedientsAPI = new Expedients();
					// Lista de interesados
					List interested = new ArrayList();
	
					// Obtener el número de registro de algún sitio (puede ser del
					// expediente padre)
					// En expropiaciones no va a haber registro
					String numregistro = null;
	
					// logger.warn("**** Datos específicos del procedimiento");
					CommonData commonData = new CommonData("1", Propiedades.getString("codExpropiacionFinca"), numregistro, new Date(), interested);
					// Datos específicos del procedimiento
					String especificDataXML = "<?xml version=\"1.0\" encoding=\"iso-8859-15\" ?><datos_especificos></datos_especificos>";
	
					// Lista de documentos del expediente
					List documents = new ArrayList();
	
					String idExpCreado = expedientsAPI.initExpedient(commonData, especificDataXML, documents, null);
					// logger.warn("**** Id del Expediente de Finca creado: " +
					// idExpCreado);
					// RElación: "Expropiacion/Finca"
					// Relacionar el expediente de Finca con el expediente de
					// Expropiación
					IItem relacionExpedientes = entitiesAPI.createEntity("SPAC_EXP_RELACIONADOS", "");
					relacionExpedientes.set("NUMEXP_PADRE", rulectx.getNumExp());
					relacionExpedientes.set("NUMEXP_HIJO", idExpCreado);
					relacionExpedientes.set("RELACION", "Finca/Expropiacion");
					relacionExpedientes.store(rulectx.getClientContext());
	
					// Grabar los datos de la Finca
					IItem fincaItem = entitiesAPI.createEntity("EXPR_FINCAS", "");
					fincaItem.set("NUMEXP", idExpCreado);
					fincaItem.set("NUM_PARCELA", finca.getNum_parcela());
					fincaItem.set("NUM_POLIGONO", finca.getNum_poligono());
					// logger.warn("NUM_POLIGONO= " + finca.getNum_poligono());
					/**
					 * #[Teresa - Ticket 200] INICIO SIGEM expropiaciones Modificar
					 * los listados para que aparezcan los metros a ocupar por
					 * propietario
					 * **/
					// fincaItem.set("MUNICIPIO", finca.getCod_mun());
					fincaItem.set("MUNICIPIO", finca.getMunicipio());
					/**
					 * #[Teresa - Ticket 200] FIN SIGEM expropiaciones Modificar los
					 * listados para que aparezcan los metros a ocupar por
					 * propietario
					 * **/
					fincaItem.set("APROVECHAMIENTO", finca.getAprovechamiento());
	
					// Al insertar teniendo en cuenta los decimales sup_parcela
					// contiene una cadena vacia.
					if(!StringUtils.isEmpty(finca.getSup_parcela()) &&  !finca.getSup_parcela().equals("")){
						double valor = 0.0;
						try{
							valor = Double.parseDouble(finca.getSup_parcela());
						}catch (NumberFormatException e){
							
						}

						fincaItem.set("SUP_PARCELA", valor);
					}
					if(!StringUtils.isEmpty(finca.getSuperficieExpr()) &&  !finca.getSuperficieExpr().equals("")){
						double valorExp = 0.0;
						try{
							valorExp = Double.parseDouble(finca.getSuperficieExpr());
						}catch (NumberFormatException e){
							
						}
						fincaItem.set("SUP_EXPROPIADA_UTIL", valorExp);
					}
					if(!StringUtils.isEmpty(finca.getSuperficieExpr()) &&  !finca.getSuperficieExpr().equals("")){
						double valorExp = 0.0;
						try{
							valorExp = Double.parseDouble(finca.getSuperficieExpr());
						}catch (NumberFormatException e){
							
						}
						fincaItem.set("SUP_EXPROPIADA", valorExp);
					}
	
					fincaItem.set("PROVINCIA", finca.getProvincia());
					fincaItem.set("CLASE_FINCA", finca.getClase_finca());
					fincaItem.set("DELEGACION_MEH", finca.getDelegacion_meh());
					fincaItem.set("COD_MUN", finca.getCod_mun());
					fincaItem
							.set("PARCELA_CATASTRAL", finca.getParcela_catastral());
					fincaItem.set("NATURALEZA_INMUEBLE",
							finca.getNaturaleza_inmueble());
					fincaItem.set("NUMERO_CARGO", finca.getNumero_cargo());
					fincaItem
							.set("CARACTER_CONTROL1", finca.getCaracter_control1());
					fincaItem
							.set("CARACTER_CONTROL2", finca.getCaracter_control2());
					fincaItem.set("USO_INMUEBLE", finca.getUso_inmueble());
					fincaItem.set("SUPERFICIE_CONSTRUIDA",
							finca.getSuperficie_construida());
					fincaItem.set("DOMICILIO_TRIBUTARIO",
							finca.getDomicilio_tributario());
					fincaItem.set("LOCALIZACION_FINCA",
							finca.getLocalizacion_finca());
					fincaItem.set("SUBPARCELAS", finca.getSubparcelas());
					fincaItem.set("ELEMENTOS_CONSTRUCTIVOS",
							finca.getElementos_constructivos());
					fincaItem.set("ANO_CATASTRAL", finca.getAno_catastral());
					fincaItem.set("VALOR_CATASTRAL", finca.getValor_catastral());
					fincaItem.set("VALOR_CATASTRAL_SUELO",
							finca.getValor_catastral_suelo());
					fincaItem.set("VALOR_CATASTRAL_CONSTRUCCION",
							finca.getValor_catastral_construccion());
					fincaItem.set("COEFICIENTE_INMUEBLE",
							finca.getCoeficiente_inmueble());
					// TODO
	
					// logger.warn("**** Finca Sigem: " +
					// fincaItem.toMap().toString());
					fincaItem.store(rulectx.getClientContext());
	
					// Asunto del Expediente de Finca: información del campo
					// localizacion_finca
					IItem expFinca = entitiesAPI.getExpedient(idExpCreado);
					String asuntoF = "Finca a Expropiar - ";
					// Inicializar propiedades de la entidad del expediente
					// logger.warn(asuntoF + finca.getLocalizacion_finca());
					expFinca.set("ASUNTO", asuntoF + finca.getLocalizacion_finca());
					expFinca.store(rulectx.getClientContext());
	
					// -- TODO: REVISAR Relacion "Expropiacion/Expropiado"
	
					// logger.warn("**** Relacion Finca/Expropiado");
					// Relacion "Finca/Expropiado"
					List<Expropiado> listaExpropiados = finca.getListaExpropiados();
					Iterator<Expropiado> itExpropiados = listaExpropiados.iterator();
					while (itExpropiados.hasNext()) {

						Expropiado exprop = (Expropiado) itExpropiados.next();
						String idExpExpropiado = null;
						if (exprop.getNdoc() != null) {
							// validacion
							if (!expropiadosMap.containsKey(exprop.getNdoc().trim())) {
								commonData = new CommonData("1", Propiedades.getString("codExpropiados"),numregistro, new Date(), interested);
								idExpExpropiado = expedientsAPI.initExpedient(commonData, especificDataXML,documents, null);
	
							} else {
								idExpExpropiado = (String) expropiadosMap.get(exprop.getNdoc().trim());
							}
						}
						else{
							if (!expropiadosMapSinDNI.containsKey(exprop.getNombre().trim())) {
								commonData = new CommonData("1", Propiedades.getString("codExpropiados"),numregistro, new Date(), interested);
								idExpExpropiado = expedientsAPI.initExpedient(commonData, especificDataXML,documents, null);
	
							} else {
								idExpExpropiado = (String) expropiadosMapSinDNI.get(exprop.getNombre().trim());
							}
						}

						// Relación Expropiado/Finca

						relacionExpedientes = entitiesAPI.createEntity("SPAC_EXP_RELACIONADOS", "");
						relacionExpedientes.set("NUMEXP_PADRE", idExpCreado);
						relacionExpedientes.set("NUMEXP_HIJO",idExpExpropiado);
						relacionExpedientes.set("RELACION","Expropiado/Finca");
						relacionExpedientes.store(rulectx.getClientContext());

						// Relación Expropiado/Expropiacion

						String relacionExp = rulectx.getNumExp()
								+ idExpExpropiado;

						if (!expropiadosExpMap.containsKey(relacionExp
								.trim())) {
							relacionExpedientes = entitiesAPI.createEntity("SPAC_EXP_RELACIONADOS", "");
							relacionExpedientes.set("NUMEXP_PADRE",rulectx.getNumExp());
							relacionExpedientes.set("NUMEXP_HIJO",idExpExpropiado);
							relacionExpedientes.set("RELACION","Expropiado/Expropiacion");
							relacionExpedientes.store(rulectx.getClientContext());
							expropiadosExpMap.put(relacionExp.trim(),
									idExpExpropiado);
						}
						if (exprop.getNdoc() != null) {
							if (!expropiadosMap.containsKey(exprop.getNdoc().trim())) {
	
								// Grabar los datos de cada inteviniente
								IItem item = entitiesAPI.createEntity("SPAC_DT_INTERVINIENTES", "");
								item.set("NUMEXP", idExpExpropiado);
	
								logger.info("Interviniente: "+ exprop.getNdoc());
	
								item.set("NDOC", exprop.getNdoc());
								item.set("NOMBRE", exprop.getNombre());
								item.set("DIRNOT", exprop.getDirnot());
								item.set("CAUT", exprop.getCaut());
								item.set("LOCALIDAD", exprop.getLocalidad());
								item.set("C_POSTAL", exprop.getC_postal());
	
								item.store(rulectx.getClientContext());
							}
						}
						else{
							if (!expropiadosMapSinDNI.containsKey(exprop.getNombre().trim())) {
	
								// Grabar los datos de cada inteviniente
								IItem item = entitiesAPI.createEntity("SPAC_DT_INTERVINIENTES", "");
								item.set("NUMEXP", idExpExpropiado);
	
								logger.info("Interviniente: "+ exprop.getNombre());
	
								item.set("NDOC", "");
								item.set("NOMBRE", exprop.getNombre());
								item.set("DIRNOT", exprop.getDirnot());
								item.set("CAUT", exprop.getCaut());
								item.set("LOCALIDAD", exprop.getLocalidad());
								item.set("C_POSTAL", exprop.getC_postal());
	
								item.store(rulectx.getClientContext());
							}
						}
						// Grabar la relacion finca/propietario.
						IItem propietarioItem = entitiesAPI.createEntity(
								"EXPR_FINCA_EXPROPIADO_PAGO", "");
						propietarioItem.set("NUMEXP", idExpCreado);
						propietarioItem.set("NUMEXP_EXPROPIADO",
								idExpExpropiado);
						propietarioItem.set("PORCENTAJE_PROP",
								exprop.getPde());
						propietarioItem.store(rulectx.getClientContext());

						fincaItem.store(rulectx.getClientContext());

						// Asunto del Expediente de Expropiado: Nombre
						IItem expProp = entitiesAPI
								.getExpedient(idExpExpropiado);
						String asuntoP = "Expropiado: ";
						// Inicializar propiedades de la entidad del
						// expediente
						// logger.warn(asuntoP + exprop.getNombre());
						expProp.set("ASUNTO", asuntoP + exprop.getNombre());
						expProp.store(rulectx.getClientContext());
						if (exprop.getNdoc() != null) {
							if (!expropiadosMap.containsKey(exprop.getNdoc().trim())) {
								expropiadosMap.put(exprop.getNdoc().trim(),idExpExpropiado);
							}
						}
						else{
							if (!expropiadosMapSinDNI.containsKey(exprop.getNombre().trim())) {
								expropiadosMapSinDNI.put(exprop.getNombre().trim(),idExpExpropiado);
							}
						}

					}
				}
			}
			
			return null;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		} catch (SAXException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	private void anadirSuperficieFincas(IRuleContext rulectx, List<Finca> listadoFincas) throws ISPACRuleException {
		
		try{
			//Obtengo el documento

			IItemCollection itColExcel = DocumentosUtil.getDocumentosByCodTramites(rulectx, rulectx.getNumExp(), "excel_expropiaci");
			Iterator<IItem> itExcel = itColExcel.iterator();
			File fileExcel = null;
			if (itExcel.hasNext()) {
				IItem itdocExel = itExcel.next();
				String infopag = itdocExel.getString("INFOPAG");
				String extension = itdocExel.getString("EXTENSION");
				fileExcel = DocumentosUtil.getFile(rulectx.getClientContext(), infopag, "", extension);
			}
			if(fileExcel!=null){
				//En este momento es cuando voy a buscar en el excell inicial y obtengo la superficie para almacenarla en la finca
				Iterator<Finca> itFincas = listadoFincas.iterator();
				Finca finca = null;
						
			
				while (itFincas.hasNext()) {
					finca = (Finca) itFincas.next();
					CargaInicial datosInicial = finca.getDatosInicial();
					// File fileExcel = new File("pruebaBuena.xls");
					int[] num_columna = { 0, 1, 2, 3, 4 };
					String[] valor_busqueda = { datosInicial.getRc(),datosInicial.getProvincia()+".0", datosInicial.getMunicipio()+".0",datosInicial.getPoligono()+".0", datosInicial.getParcela()+".0" };
					int fila = ExcelUtils.findRowbyColumnVector(fileExcel, 0,num_columna, valor_busqueda);
					String valorSuperficie = ExcelUtils.findValueBySheetRowColumn(fileExcel,0, 5, fila);
					finca.setSuperficieExpr(valorSuperficie);
				}
			}
			
		}catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. " + e.getMessage(), e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
