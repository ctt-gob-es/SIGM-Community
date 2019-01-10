package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.openoffice.OpenOfficeHelper;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sun.star.lang.XComponent;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;

public class GeneratePlenoRule implements IRule {

	private OpenOfficeHelper ooHelper = null;
	private static final Logger logger = Logger
			.getLogger(GeneratePlenoRule.class);

	protected String STR_prefijo = "";
	protected String STR_nombreTramite = "";
	protected String STR_nombreCabecera = "";
	protected String STR_propuestaBorrador = "";
	protected String STR_urgencias = "";
	protected String STR_nombreRuegos = "";
	protected String STR_nombrePie = "";
	protected String STR_informes = "";
	protected String STR_constitutiva = "";
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
			// ----------------------------------------------------------------------------------------------
			cargaDoc(rulectx);
			
			logger.warn("STR_prefijo_"+STR_prefijo);
			logger.warn("STR_nombreTramite_"+STR_nombreTramite);
			logger.warn("STR_nombreCabecera_"+STR_nombreCabecera);
			logger.warn("STR_propuestaBorrador_"+STR_propuestaBorrador);
			logger.warn("STR_urgencias_"+STR_urgencias);
			logger.warn("STR_nombreRuegos_"+STR_nombreRuegos);
			logger.warn("STR_nombrePie_"+STR_nombrePie);
			
			XComponent xComponent = null;
			
			if(!STR_constitutiva.equals("")){
				DocumentosUtil.generarDocumento(rulectx, STR_constitutiva,null);				
				String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, STR_constitutiva);
				// logger.warn(strInfoPag);
				File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
				ooHelper = OpenOfficeHelper.getInstance();
				xComponent = ooHelper.loadDocument("file://"+ file.getPath());
				file.delete();
			} else{

				// Obtiene la cabecera
				DocumentosUtil.generarDocumento(rulectx, STR_nombreCabecera,null);
				
				String strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, STR_nombreCabecera);
				// logger.warn(strInfoPag);
				File file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
				ooHelper = OpenOfficeHelper.getInstance();
				xComponent = ooHelper.loadDocument("file://"+ file.getPath());
				file.delete();
				// logger.warn("FIN CABECERA");
	
				// Borrador
				List listPropuesta = SecretariaUtil.getPropuestas(rulectx, entitiesAPI);
				Iterator itProps2 = listPropuesta.iterator();
				IItem iProp2 = ((IItem) itProps2.next());
				int orden2 = 1;
	
				cct.setSsVariable("ORDEN", String.valueOf(orden2));
				String extracto = "";
				if (iProp2.get("EXTRACTO") != null)
					extracto = (String) iProp2.get("EXTRACTO");
				else
					extracto = "";
				cct.setSsVariable("EXTRACTO", extracto.toUpperCase());
	
				String numexp_origen = iProp2.getString("NUMEXP_ORIGEN");
	
				String strQuery = "WHERE NUMEXP = '" + numexp_origen + "'";
				IItemCollection collection = entitiesAPI.queryEntities(
						"SECR_PROPUESTA", strQuery);
	
				Iterator it = collection.iterator();
				IItem item = null;
	
				while (it.hasNext()) {
	
					item = ((IItem) it.next());
					String fecha_emision = item.getString("FECHA_EMISION");
					if (fecha_emision != null) {
						SimpleDateFormat dateformat = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
						SimpleDateFormat inputDateformat = new SimpleDateFormat("dd'/'MM'/'yyyy", new Locale("es"));
						fecha_emision = dateformat.format(inputDateformat.parse(fecha_emision));
	
					} else {
						fecha_emision = "";
					}
					cct.setSsVariable("FECHA_EMISION", fecha_emision);
				}
	
				String votacion = "";
				if (iProp2.get("RESULTADO_VOTACION") != null)
					votacion = iProp2.getString("RESULTADO_VOTACION");
				else
					votacion = "";
				if (!votacion.equals("")) {
					votacion = votacion + "\n\n";
				}
				cct.setSsVariable("RESULTADO_VOTACION", votacion);
	
				// creacion del documento
	
				DocumentosUtil.generarDocumento(rulectx,STR_propuestaBorrador, null);
				strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, STR_propuestaBorrador);
				file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
				DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
				file.delete();
	
				cct.deleteSsVariable("ORDEN");
				cct.deleteSsVariable("EXTRACTO");
				cct.deleteSsVariable("FECHA_EMISION");
				cct.deleteSsVariable("RESULTADO_VOTACION");
	
				// GENERACION DE LAS PROPUESTAS
				collection = DocumentosUtil.getDocumentsByNombre(rulectx.getNumExp(), rulectx, "Propuesta");
	
				Vector<IItem> vPropuesta = SecretariaUtil.orderPropuestas(rulectx);
				if (vPropuesta != null && vPropuesta.size()>1) {
	
					for (int i = 1; i < vPropuesta.size(); i++) {
		
						if (vPropuesta.get(i) != null) {
							item = ((IItem) vPropuesta.get(i));
							String descripcion = item.getString("DESCRIPCION");
							strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, descripcion);
							file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
							DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
							file.delete();
						}
					}
					
					//Informes
					if(STR_informes!=""){
						int ordeninforme = listPropuesta.size() + 1;
						cct.setSsVariable("ORDENINFORM", String.valueOf(ordeninforme));
						DocumentosUtil.generarDocumento(rulectx, STR_informes,null);
						strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx,	STR_informes);
						file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
						DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
						file.delete();
						cct.deleteSsVariable("ORDENINFORM");
					}				
					
					int tamano = listPropuesta.size() + 2;
					// GENERACION DE LAS URGENCIAS
					if (!STR_urgencias.equals("")) {
		
						String organo = SecretariaUtil.getOrgano(rulectx);
						// calculo del numero de urgencia
						if (organo.equals("PLEN")) {
							cct.setSsVariable("ORDENPRES", String.valueOf(tamano));
							cct.setSsVariable("ORDENURGEN", String.valueOf(tamano + 1));
						} else {
							cct.setSsVariable("ORDENURGEN", String.valueOf(tamano));
						}
		
						List listUrgencia = SecretariaUtil.getUrgencias(rulectx,entitiesAPI);
		
						if (listUrgencia.size() == 0) {
							if (organo.equals("JGOB")) {
								cct.setSsVariable("HAYURGENCIAS",
										"no se someten a la Junta de Gobierno ninguna cuestion de tal naturaleza.");
							}
							if (organo.equals("PLEN")) {
								cct.setSsVariable("HAYURGENCIAS",
										"no se someten a Pleno ninguna cuestion de tal naturaleza.");
							}
						} else {
							if (organo.equals("JGOB")) {
								cct.setSsVariable(
										"HAYURGENCIAS",
										"se someten a la Junta de Gobierno las siguientes cuestiones de tal naturaleza.");
							}
							if (organo.equals("PLEN")) {
								cct.setSsVariable("HAYURGENCIAS",
										"se someten a Pleno las siguientes cuestiones de tal naturaleza.");
							}
						}
		
						DocumentosUtil.generarDocumento(rulectx, STR_urgencias,null);
						strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx,	STR_urgencias);
						file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
						DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
						file.delete();
		
						cct.deleteSsVariable("ORDENURGEN");
						cct.deleteSsVariable("HAYURGENCIAS");
						if (organo.equals("PLEN")) {
							cct.deleteSsVariable("ORDENPRES");
						}
		
						collection = DocumentosUtil.getDocumentsByNombre(rulectx.getNumExp(), rulectx, "Propuesta Urgencia");
		
						Vector<IItem> vUrgencias = SecretariaUtil.orderUrgencias(collection);
						if (vUrgencias != null) {
		
							for (int i = 0; i < vUrgencias.size(); i++) {
								if (vUrgencias.get(i) != null) {
									item = ((IItem) vUrgencias.get(i));
		
									String descripcion = item.getString("DESCRIPCION");
									strInfoPag = DocumentosUtil
											.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx,
													descripcion);
		
									file = DocumentosUtil
											.getFile(cct, strInfoPag, null, null);
									DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
									file.delete();
								}
							}
						}
					}
					if (!STR_nombreRuegos.equals("")) {
						// logger.warn("GENERACION DE LOS RUEGOS");
						tamano = listPropuesta.size();
						String organo = SecretariaUtil.getOrgano(rulectx);
						// calculo del numero de urgencia
						if (organo.equals("PLEN")) {
							cct.setSsVariable("ORDENRUEGOS", String.valueOf(tamano + 4));
						} else {
							cct.setSsVariable("ORDENRUEGOS", String.valueOf(tamano + 3));
						}
		
						DocumentosUtil.generarDocumento(rulectx, STR_nombreRuegos, null);
						strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx, STR_nombreRuegos);
		
						file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
						DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
						file.delete();
					}
					
				}
	
				// GENERACION DEL PIE DEL ACTA
				DocumentosUtil
						.generarDocumento(rulectx, STR_nombrePie, null);
				strInfoPag = DocumentosUtil.getInfoPagByDescripcion(rulectx.getNumExp(), rulectx,
						STR_nombrePie);
	
				file = DocumentosUtil.getFile(cct, strInfoPag, null, null);
				DipucrCommonFunctions.ConcatenaByFormat(xComponent, "file://" + file.getPath(), extensionEntidad);
				file.delete();
			}

			// Guarda el resultado en repositorio temporal
			String fileName = FileTemporaryManager.getInstance().newFileName(extensionEntidad);
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
			File file = new File(fileName);

			//OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(), "");
			String filter = DocumentosUtil.getFiltroOpenOffice(extensionEntidad);
			OpenOfficeHelper.saveDocument(xComponent,"file://" + file.getPath(), filter);	

			// Guarda el resultado en gestor documental
			int tpdoc = DocumentosUtil.getTipoDoc(cct, STR_prefijo, DocumentosUtil.BUSQUEDA_EXACTA, false);

			DocumentosUtil.generaYAnexaDocumento(rulectx, tpdoc, STR_prefijo, file, extensionEntidad);
			file.delete();

			// Borra los documentos intermedios del gestor documental
			IItemCollection collection = entitiesAPI.getDocuments(rulectx.getNumExp(), "DESCRIPCION LIKE '" + STR_prefijo
									+ " -%' OR DESCRIPCION LIKE 'Borrador de Acta de Pleno -%' OR DESCRIPCION LIKE 'Borrador de Acta de Junta -%'",
							"");
			Iterator<IItem> it = collection.iterator();
			while (it.hasNext()) {
				IItem doc = (IItem) it.next();
				entitiesAPI.deleteDocument(doc);
			}
		} catch (Exception e) {
			logger.error("Numexp."+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Numexp."+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} finally {
			if(null != ooHelper){
	        	ooHelper.dispose();
	        }
		}

		return new Boolean(true);
	}
	
	public boolean cargaDoc(IRuleContext rctx) throws ISPACRuleException {
		
		//logger.warn("GenerateActaJuntaRule o pleno");

		STR_prefijo                 = "Acta de Pleno";
		STR_nombrePie               = STR_prefijo+ " - Pie";
		STR_propuestaBorrador       = STR_prefijo+" - Propuesta - Borrador";
		
		
		String sesion = SecretariaUtil.getSesion(rctx);

		STR_nombreTramite           	= "Carga audio y propuestas";
		if(sesion.equals("ORD")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - Ordi";
			STR_informes                = STR_prefijo+" - Informes - Ordi";
			STR_urgencias				= STR_prefijo+" - Urgencias - Ordi";
			STR_nombreRuegos            = STR_prefijo+" - Ruegos y preguntas - Ordi";
		}
		if(sesion.equals("EXTR")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - Extra";
			STR_informes                = "";
			STR_urgencias				= "";
			STR_nombreRuegos            = "";
			
		}
		if(sesion.equals("EXUR")){
			STR_nombreCabecera 			= STR_prefijo+" - Cabecera - ExtraUrg";
			STR_propuestaBorrador       = STR_prefijo+" - Propuesta - RatificacionUrgencia";
			STR_urgencias				= "";
			STR_informes                = "";
			STR_nombreRuegos            = "";
		}
		if(sesion.equals("CONS")){
			STR_constitutiva 			= "Borrador de Acta de Pleno - Constitutiva";
		}
		
		
		return true;
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
	}

}
