package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dgpe.codice.common.cbclib.EmbeddedDocumentBinaryObjectType;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfCopy;

import es.dipucr.contratacion.client.beans.AnuncioPrevioBean;
import es.dipucr.contratacion.client.beans.PublicacionesOficialesBean;
import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.common.ServiciosWebContratacionFunciones;
import es.dipucr.contratacion.objeto.BOP;
import es.dipucr.contratacion.objeto.DatosContrato;
import es.dipucr.contratacion.objeto.DatosLicitacion;
import es.dipucr.contratacion.objeto.DiariosOficiales;
import es.dipucr.contratacion.objeto.Peticion;
import es.dipucr.contratacion.objeto.Solvencia;
import es.dipucr.contratacion.resultadoBeans.PublicationResult;
import es.dipucr.contratacion.resultadoBeans.Resultado;
import es.dipucr.contratacion.resultadoBeans.VisualizationResult;
import es.dipucr.contratacion.services.PlataformaContratacionProxy;
import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class AnuncioPrevioRule implements IRule{
	
	public static final Logger logger = Logger
	.getLogger(AnuncioPrevioRule.class);

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
				
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			// --------------------------------------------------------------------
				ClientContext cct = (ClientContext) rulectx.getClientContext();
				IInvesflowAPI invesFlowAPI = cct.getAPI();
				IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
				IGenDocAPI genDocAPI = cct.getAPI().getGenDocAPI();
				Object connectorSession = genDocAPI.createConnectorSession();
			// --------------------------------------------------------------------
			
			
		
			PlataformaContratacionProxy platContratacion = new PlataformaContratacionProxy(ServiciosWebContratacionFunciones.getDireccionSW());
			
			AnuncioPrevioBean analisisPrevio = new AnuncioPrevioBean();
			
			//Num Expediente
			analisisPrevio.setNumexp(rulectx.getNumExp());
			
			DatosContrato datContrato = DipucrFuncionesComunes.getDatosContrato(rulectx, rulectx.getNumExp());
			if(datContrato!=null){
				analisisPrevio.setObjetoContrato(datContrato.getObjetoContrato());
				analisisPrevio.setValorEstimadoContrato(datContrato.getValorEstimadoContrato());
				analisisPrevio.setProcContratacion(datContrato.getProcedimientoContratacion());
				analisisPrevio.setTipoContrato(datContrato.getTipoContrato());
				analisisPrevio.setSubTipoContrato(datContrato.getSubTipoContrato());
				analisisPrevio.setTipoTramitacion(datContrato.getTipoTramitacion());
				analisisPrevio.setTramitacionGasto(datContrato.getTramitacionGasto());
				analisisPrevio.setCpv(datContrato.getCpv());
			}
			
//			DatosTramitacion plazoPresentacionProp = DipucrFuncionesComunes.getDatosTramitacion(rulectx);
//			analisisPrevio.setF_term_pazo_presen_prop(plazoPresentacionProp.getTerminacionPlazoPresent());
//			analisisPrevio.setFechaPresentacionSolcitudesParticipacion(plazoPresentacionProp.getFechaPresentacionSolicitudesParticipacion());
			
			Peticion peticion = DipucrFuncionesComunes.getPeticion(rulectx);
			analisisPrevio.setPresupuestoConIva(peticion.getPresupuestoConIva());
			analisisPrevio.setPresupuestoSinIva(peticion.getPresupuestoSinIva());
			
			Solvencia solvencia = DipucrFuncionesComunes.getSolvencia(rulectx);
			analisisPrevio.setSolvenciaEconomica(solvencia.getSolvenciaEconomica());
			analisisPrevio.setSolvenciaTecn(solvencia.getSolvenciaTecn());
			
			
			PublicacionesOficialesBean publicacionesOficiales = null;			
			DiariosOficiales diariosOficiales =  DipucrFuncionesComunes.getDiariosOficiales(rulectx, "AnuncioPrevioRule");
			BOP bop = DipucrFuncionesComunes.getBOP(rulectx);			
			if(diariosOficiales !=null || bop!=null){
				publicacionesOficiales = new PublicacionesOficialesBean();
				if(diariosOficiales.getDoue() !=null){
					publicacionesOficiales.setEnviarDOUE(diariosOficiales.getDoue().isPublicarDOUE());
				}
				if(diariosOficiales.getBoe() !=null){
					publicacionesOficiales.setEnviarBOE(diariosOficiales.getBoe().isPublicarBOE());
				}
				if(bop!=null){
					publicacionesOficiales.setNombreOtrosDiarios(bop.getNombreBOP());
					publicacionesOficiales.setFechaPubOtrosDiarios(bop.getFechaPublicacion());
					publicacionesOficiales.setPublishURLOtrosDiarios(bop.getUrlPublicacion());
				}
				
				analisisPrevio.setDiarios(publicacionesOficiales);
			}
			
			
			
			DatosLicitacion datosLicitacion= DipucrFuncionesComunes.getDatosLicitacion(rulectx);
			analisisPrevio.setTipoPresentacionOferta(datosLicitacion.getTipoPresentacionOferta());
			analisisPrevio.setFundacionPrograma(datosLicitacion.getFundacionPrograma());
			
			try {
				String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
				
				//String publishedByUser = UsuariosUtil.getDni(cct);
				//String publishedByUser = "99001215S";
				String publishedByUser = DipucrCommonFunctions.getVarGlobal("PUBLISHEDBYUSER");
				if(publishedByUser==null || publishedByUser.equals("")){
					publishedByUser = UsuariosUtil.getDni(cct);
				}

				//Petición
				Resultado resultadoAnalisisPrevio = platContratacion.envioAnalisisPrevio(entidad, analisisPrevio, publishedByUser);
				
				PublicationResult result = resultadoAnalisisPrevio.getPublicacion();
				if(result.getResultCode().equals("OK")){
					VisualizationResult docResultado =  resultadoAnalisisPrevio.getVisualizacion();
					if(docResultado!=null){
						
						if (docResultado.getResultCode().equals("OK")) {
							
							DipucrFuncionesComunes.imprimelogsVisualizacion(docResultado);
							
							EmbeddedDocumentBinaryObjectType documento = docResultado.getDocument();

							// Anuncio de Informacion Previa
							String STR_nombreTPdoc = "Anuncio de Informacion Previa";
							String strQuery = "WHERE NOMBRE = '" + STR_nombreTPdoc+ "'";
							IItemCollection collectionTPDOC = entitiesAPI.queryEntities("SPAC_CT_TPDOC",strQuery);
							@SuppressWarnings("unchecked")
							Iterator<IItem> itTPDoc = collectionTPDOC.iterator();
							int tpdoc = 0;
							if (itTPDoc.hasNext()) {
								IItem tpd = (IItem) itTPDoc.next();
								tpdoc = tpd.getInt("ID");
							}
							int taskId = rulectx.getTaskId();
							IItem newdoc = genDocAPI.createTaskDocument(
									taskId, tpdoc);

							String rutaFileName = FileTemporaryManager
									.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance()
											.newFileName(".pdf");
							FileOutputStream fos = null;
							try {
								fos = new FileOutputStream(rutaFileName);
							} catch (IOException e) {
								logger.error(e.getMessage(), e);
								throw new ISPACRuleException(e);
							}
							fos.write(documento.getValue());
							fos.close();
							fos.flush();

							File file = new File(rutaFileName);

							FileInputStream in = new FileInputStream(file);
							int docId = newdoc.getInt("ID");
							IItem entityDoc = genDocAPI
									.attachTaskInputStream(connectorSession, taskId, docId, in, (int) file.length(), documento.getMimeCode(), "Anuncio de Informacion Previa");
							entityDoc.set("EXTENSION", "pdf");
							entityDoc.set("FFIRMA", new Date());
							entityDoc.store(cct);
							file.delete();
							
							DipucrFuncionesComunes.imprimeResultadoAnalisisPrevio(resultadoAnalisisPrevio, rulectx, 
									"Información de Plataforma Contratación", "Anuncio de Informacion Previa");
						}
					}
				}

				else{
					/**
					 * ALMACENAR LA INFORMACIÓN EN UN DOCUMENTO CON LOS ERRORES.
					 * **/
					File ficheroError = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/documentoErrorContratacion.pdf");
					
					try {
						
						// A partir del objeto File creamos el fichero
						// físicamente
						if (ficheroError.createNewFile()) {
							
							logger.warn("El fichero se ha creado correctamente");
							Document documentErrorComp = new Document();
							PdfCopy.getInstance(documentErrorComp,new FileOutputStream(ficheroError));
							documentErrorComp.open();
							
							
							
							
							DipucrFuncionesComunes.imprimelogs(result);
							
							DipucrFuncionesComunes.imprimedoc(result, documentErrorComp, "Anuncio Previo", rulectx);
							
							documentErrorComp.close();
							
							if (ficheroError != null) {
								IItemCollection tipDoc = entitiesAPI.queryEntities("SPAC_CT_TPDOC","WHERE NOMBRE='Error'");
								@SuppressWarnings("unchecked")
								Iterator<IItem> tipDocIterator = tipDoc.iterator();
								int idTipDoc = 0;
								if (tipDocIterator.hasNext())
									idTipDoc  = ((IItem) tipDocIterator.next()).getInt("ID");
								IItem entityDocument = genDocAPI.createTaskDocument(rulectx.getTaskId(), idTipDoc);
								entityDocument.set("EXTENSION", Constants._EXTENSION_PDF);
								entityDocument.set("DESCRIPCION","Error");
								entityDocument.store(cct);
								cct.endTX(true);
								int documentIdComparece = entityDocument.getKeyInt();
								InputStream inError = new FileInputStream(ficheroError);

								genDocAPI.attachTaskInputStream(connectorSession, rulectx.getTaskId(), documentIdComparece, inError,
										(int) ficheroError.length(), "application/pdf", "Error");
								
								inError.close();
							}

						} else{
							logger.warn("No ha podido ser creado el fichero");
						}
						
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. ",e);
					} catch (DocumentException e) {
						logger.error(e.getMessage(), e);
						throw new ISPACRuleException("Error. ",e);
					}
					finally{
						ficheroError.delete();
					}
				}

			} catch (RemoteException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new ISPACRuleException("Error. ",e);
			}
		}
		catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		logger.warn("TODO CORRECTOOOOO!!!!!!!!!!!!!!!!!!!!");
		
		return new Boolean(true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
