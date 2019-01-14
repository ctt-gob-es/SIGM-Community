package es.dipucr.sigem.api.rule.procedures.personal;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.JasperReportUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.svd.common.DipucrFuncionesComunesSVD;

public class CargaTitulosUniversitariosTitulosNoUnivRule implements IRule {

	public static final Logger LOGGER = Logger.getLogger(CargaTitulosUniversitariosTitulosNoUnivRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String codProcedimiento = "SVDTUWS03#SVDTNUWS03";
		String relacion = "Solicitud Convocatoria";
		String nombreProcedimiento = "Títulos Universitarios#Títulos NO Universitarios";

		try {
			// ----------------------------------------------------------------------------------------------
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			// ----------------------------------------------------------------------------------------------
		
			String fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() +  "/" + FileTemporaryManager.getInstance().newFileName("."+ DocumentosUtil.Extensiones.XML);
			FileWriter fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
		    
		    Iterator<IItem> itPuestosTrabajo = ConsultasGenericasUtil.queryEntities(rulectx, "PERSONAL_PUESTO_TRABAJO", "NUMEXP='" + rulectx.getNumExp() + "'");
		    while(itPuestosTrabajo.hasNext()){
		    	IItem puesto = itPuestosTrabajo.next();
		    	String puestoTrabajo = "";
		    	if(puesto.getString("PUESTOTRABAJO")!=null) puestoTrabajo = puesto.getString("PUESTOTRABAJO");
		    	String [] vPuesto = puestoTrabajo.split(" - ");
		    	String nPuesto = vPuesto[0]+ " - " + vPuesto[1];
		    	sbDatos.append("<puestotrabajo nombre='"+puestoTrabajo+"'>");
		    	
		    	String consulta = "WHERE NUMEXP IN (SELECT NUMEXP FROM PERSONAL_PUESTO_TRABAJO WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"' AND RELACION='"+relacion+"') AND PUESTOTRABAJO = '"+nPuesto+"') ORDER BY IDENTIDADTITULAR ASC";
		    	IItemCollection collAllPropsExp = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", consulta);
				Iterator<IItem> itExpRel = collAllPropsExp.iterator();
				while (itExpRel.hasNext()) {
					IItem expRel = itExpRel.next();
					String solicitudExp = "";
					if (expRel.getString("NUMEXP") != null)
						solicitudExp = expRel.getString("NUMEXP");
					IItemCollection itcParti = ParticipantesUtil.getParticipantes(cct, solicitudExp, "ROL='INT'", "");
					Iterator<IItem> itPartic = itcParti.iterator();
					while (itPartic.hasNext()) {
						IItem participante = itPartic.next();
						String nombre = "";
						if (participante.getString("NOMBRE") != null)
							nombre = participante.getString("NOMBRE");
						String dni = "";
						if (participante.getString("NDOC") != null)
							dni = participante.getString("NDOC");
						if (!dni.isEmpty() && !nombre.isEmpty()) {
							sbDatos.append("<interesado>");
							sbDatos.append("<dni>"+dni+"</dni><nombre>"+nombre+"</nombre>");
							String[] vCodProc = codProcedimiento.split("#");
							String[] vNomProc = nombreProcedimiento.split("#");
							if (vCodProc.length > 0 && vNomProc.length > 0) {							
								for (int i = 0; i < vCodProc.length; i++) {	
									
									Element resultadoServicio = DipucrFuncionesComunesSVD.enviaPeticionInteresadosSinDatosEspecificos(rulectx, vCodProc[i], vNomProc[i], dni, nombre);
									try {
										String literalError = DipucrFuncionesComunesSVD.getTextNode(resultadoServicio, "LiteralError");
										if (!literalError.equals("USUARIO NO AUTORIZADO")) {
											
											Node node  = resultadoServicio.getElementsByTagName("ns1:DatosEspecificos").item(0);
										    Element datosEspecificos = (org.w3c.dom.Element) node;
										    
										    String datosTramitada = DipucrFuncionesComunesSVD.getTextNode(datosEspecificos, "ns1:CodigoEstado");									    
										    //0003->Tramitada
										    if(datosTramitada!=null && datosTramitada.equals("0003")){
										    	
										    	sbDatos.append("<procedimiento nombre='"+vNomProc[i]+"'>");
												sbDatos.append("<titulos>");
										    	List<Element> datosTitulacionList = DipucrFuncionesComunesSVD.evaluateXPathAsList("DatosTitulacion",datosEspecificos);
										    	
										  		for (int j=0; j<datosTitulacionList.size(); j++) {
										  	   		Element element = (Element) datosTitulacionList.get(j);										  	   		
										  	   	    String strExp= "//ns1:DatosTitulacion["+(j+1)+"]/ns1:DatosTitulo/ns1:Titulacion";										  	   	
										  			String titulo = DipucrFuncionesComunesSVD.evaluateString(element,strExp);
										  			sbDatos.append("<titulo>");
													sbDatos.append(titulo);
													sbDatos.append("</titulo>");
										  		}
										  		sbDatos.append("</titulos>");
												sbDatos.append("</procedimiento>");										  		
										    }
										}
									} catch (Exception e) {
										LOGGER.error("Error en el expediente. " + rulectx.getNumExp() + " - "+vCodProc[i]+" - "+vNomProc[i]+" - "+dni+" - "+nombre+" - " + e.getMessage(), e);
										throw new ISPACRuleException("Error en el expediente. " + rulectx.getNumExp() + " - "+vCodProc[i]+" - "+vNomProc[i]+" - "+dni+" - "+nombre+" - " + e.getMessage(), e);
									}									
								}							
							}
							sbDatos.append("</interesado>");
						}					
					}
				}
				sbDatos.append("</puestotrabajo>");		    	
		    }			
			sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			
			File fileXML = new File(fileName);
			int tpdocXML = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "informe");
			String nombreTipoDocXML = DocumentosUtil.getNombreTipoDocByCod(cct, "informe");
			IItem docXML = DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), tpdocXML, nombreTipoDocXML, fileXML, "xml");
			
			docXML.delete(cct);			
			
			File ffilePathJunta = JasperReportUtil.obtenerPdftoXml(rulectx, docXML,  "/listado/puestotrabajo","Estadísticas de Puestos de Trabajo", "PuestoTrabajo.jasper");
			
			IItem entityDocumentParcela = DocumentosUtil.generaYAnexaDocumento(rulectx.getClientContext(), rulectx.getTaskId(), tpdocXML, nombreTipoDocXML,ffilePathJunta, DocumentosUtil.Extensiones.PDF);
			entityDocumentParcela.store(rulectx.getClientContext());
			
			if(ffilePathJunta.exists()){
				ffilePathJunta.delete();
			}
			if(fileXML.exists()){
				fileXML.delete();
			}
			if(fichero!=null){
				fichero.close();
			}

		} catch (ISPACException e) {
			LOGGER.error("Error en el expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("Error en el expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. " + rulectx.getNumExp() + " - " + e.getMessage(), e);
		} 

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
