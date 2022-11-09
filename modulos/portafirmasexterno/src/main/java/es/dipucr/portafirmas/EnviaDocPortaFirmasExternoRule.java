package es.dipucr.portafirmas;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.EntitiesAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.tram.sign.Sigm30SignConnector;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.axis2.databinding.ADBException;
import org.apache.log4j.Logger;

import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Authentication;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.CreateRequest;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.CreateRequestResponse;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.InsertDocument;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.InsertDocumentResponse;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Request;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SendRequest;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SendRequestResponse;
import _0.v2.modify.pfirma.cice.juntadeandalucia.PfirmaException;
import es.dipucr.portafirmas.common.Configuracion;
import es.dipucr.portafirmas.common.CreacionObjetosPortafirmas;
import es.dipucr.portafirmas.common.DipucrFuncionesComunes;
import es.dipucr.portafirmas.common.ServiciosWebPortaFirmasFunciones;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class EnviaDocPortaFirmasExternoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(EnviaDocPortaFirmasExternoRule.class);


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}


	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean terminarTramite = false;
		try {
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();      
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        //----------------------------------------------------------------------------------------------
			
			Iterator<IItem> rellenadoFirmantesContinuacion = ConsultasGenericasUtil.queryEntities(rulectx.getClientContext(), "FIRMA_DOC_MASFIRMANTES", "NUMEXP='"+rulectx.getNumExp()+"'");
			
			Iterator<IItem>  iDocMandado = ConsultasGenericasUtil.queryEntities(rulectx.getClientContext(), "FIRMA_DOC_EXTERNO_IDDOC", "NUMEXP='"+rulectx.getNumExp()+"' AND ID_TRAMITE='"+rulectx.getTaskId()+"'");
			if(rellenadoFirmantesContinuacion.hasNext()){
				if(!iDocMandado.hasNext()){
					
					IItemCollection itCollection = DocumentosUtil.getDocumentosByTramites(rulectx.getClientContext(), rulectx.getNumExp(), rulectx.getTaskId());
					Iterator<IItem> itDoc = itCollection.iterator();
					
					while (itDoc.hasNext()){
						IItem docFirma = itDoc.next();
						if(docFirma.getString("EXTENSION").equals("pdf") || (docFirma.getString("EXTENSION_RDE") != null && docFirma.getString("EXTENSION_RDE").equals("pdf"))){
							terminarTramite = true;
						}
						else{
							rulectx.setInfoMessage("El documento a mandar a firmar debe estar en pdf");
						}
					}
					
					//Comprobar que existe el firmante y si no existe añadirlo.
					
					//boolean usuario = AdministracionUtil.comprobarExisteUsuarioPortaFirmasExterno(rulectx, entitiesAPI);
					
					//boolean cargoUsuario = AdministracionUtil.relacionarCargoConUsuario(rulectx, entitiesAPI);
					
					if(terminarTramite){
						//Comprobar que si al menos uno a puesto un orden el resto lo tiene que tener.
						IItemCollection itColFirmantes = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO", "WHERE NUMEXP='"+rulectx.getNumExp()+"' ORDER BY ORDEN_FIRMA ASC");
						Iterator<IItem> iteratorFirmantes = itColFirmantes.iterator();
						int contador = 1;
						boolean tieneOrden = false;
						while(iteratorFirmantes.hasNext()){
							IItem firmantes = iteratorFirmantes.next();
							String orden = "";
							int valorOrden = -1;
							if(firmantes.getString("ORDEN_FIRMA")!=null) orden = firmantes.getString("ORDEN_FIRMA");
							if(!StringUtils.isEmpty(orden))valorOrden = Integer.parseInt(orden);
							if(valorOrden >= 0){
								tieneOrden = true;
								firmantes.set("ORDEN_FIRMA", contador);
							}
							if(tieneOrden){
								firmantes.set("ORDEN_FIRMA", contador);
							}
							contador++;
							firmantes.store(cct);
						}
					}
					
				}
				else{
					rulectx.setInfoMessage("Ya ha sido mandado este documento al portafirma. Contacte con el administrador.");
				}
			}
			else{
				rulectx.setInfoMessage("Debe introducir en la pestaña 'Firma Documento Final' si una vez que se firme el documento externo deberá ser firmado por alguien de la entidad, por ejemplo, secretario/a.");
			}
			
			
		}catch(ISPACException e) 
		{
			logger.error("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el expediente. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
        }
		
		return terminarTramite;
	}


	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();  
	        EntitiesAPI entitiesAPI = (EntitiesAPI)cct.getAPI().getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			
			Iterator<IItem>  iDocMandado = ConsultasGenericasUtil.queryEntities(rulectx.getClientContext(), "FIRMA_DOC_EXTERNO_IDDOC", "NUMEXP='"+rulectx.getNumExp()+"' AND ID_TRAMITE='"+rulectx.getTaskId()+"'");			
			if(!iDocMandado.hasNext()){
				//String direccionPortaFirmaExterno = DipucrCommonFunctions.getVarGlobal(Configuracion.DIRECCION_PORTAFIRMASEXTERNOMODIFY);
		        String direccionPortaFirmaExterno = ServiciosWebPortaFirmasFunciones.getDireccionSWModify();

				ModifyServiceStub modify = new ModifyServiceStub(direccionPortaFirmaExterno);
				
				Authentication authentication = Configuracion.getAuthenticationModifyPADES(cct);
				
				/**
				 * documentList: Lista de documentos
				 * **/
				IItemCollection itCollection = DocumentosUtil.getDocumentosByTramites(rulectx.getClientContext(), rulectx.getNumExp(), rulectx.getTaskId());
				Iterator<IItem> itDoc = itCollection.iterator();
				
				//No puedo utilizar los datos específicos porque se estan utilizando para insertar un participante.
				//String tipoFirma = TramitesUtil.getDatosEspecificosOtrosDatos(rulectx.getClientContext(), rulectx.getTaskProcedureId());
				String nombreTramite = TramitesUtil.getNombreTramite(cct, rulectx.getTaskId());
				String tipoFirma = "";
				if(nombreTramite.contains("PARALELA")){
					tipoFirma = "PARALELA";
				}
				else{
					tipoFirma = "CASCADA";
				}

				if (itDoc.hasNext()){
					IItem docFirma = itDoc.next();
					/**
					 * Envio Petición
					 * **/
					String name = docFirma.getString("NOMBRE")+".pdf";
					String extension = MimetypeMapping.getMimeType("pdf");
					Request request = CreacionObjetosPortafirmas.getRequest(rulectx, tipoFirma, name, extension);
					
					CreateRequest createRequest6 = new CreateRequest();
					createRequest6.setAuthentication(authentication);
					createRequest6.setRequest(request);
					CreateRequestResponse idPeticion = modify.createRequest(createRequest6);
					if(itCollection!=null){
						logger.warn("Números de documentos a mandar: "+itCollection.toList().size());
					}
					
					logger.warn("Identificador de la petición. "+idPeticion.getRequestId());
						
					if(!iDocMandado.hasNext()){				
					
						String cve = null;
						if(StringUtils.isNotEmpty(docFirma.getString(DocumentosUtil.COD_COTEJO)))cve = docFirma.getString(DocumentosUtil.COD_COTEJO);
						
						
						File pdfFile = null;
						if(docFirma.getString("INFOPAG_RDE")!=null){
							pdfFile = DocumentosUtil.getFile(cct, docFirma.getString("INFOPAG_RDE"), docFirma.getString("NOMBRE") + "-" + docFirma.getKeyInt(), docFirma.getString("EXTENSION_RDE"));
						}
						else{
							if(docFirma.getString("EXTENSION").equals("pdf")){
								pdfFile = DocumentosUtil.getFile(cct, docFirma.getString("INFOPAG"), docFirma.getString("NOMBRE") + "-" + docFirma.getKeyInt(), docFirma.getString("EXTENSION"));
							}
						}
						File fileBandaGris = null;
						if(StringUtils.isEmpty(cve)){
							SignDocument signDocument = new SignDocument(docFirma);
							
//							DipucrSignConnector signConnector = new DipucrSignConnector();
//							ISignConnector signConnector = SignConnectorFactory.getInstance(cct).getSignConnector();
							Sigm30SignConnector signConnector = new Sigm30SignConnector();
							signConnector.initializate(signDocument, rulectx.getClientContext());
							
							String codCotejo = signConnector.addCodVerificacion(signDocument, docFirma.getDate(DocumentosUtil.FAPROBACION), cct);
							
							
							String pathFileTemp = FileTemporaryManager.getInstance().put(pdfFile.getAbsolutePath(), ".pdf");
							
							try {
								DipucrFuncionesComunes.addGrayBand(cct, signDocument.getDocumentType(), pdfFile, pathFileTemp, codCotejo);
								fileBandaGris = new File (FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/" + pathFileTemp);
								
//								IItem itDocConve = DocumentosUtil.getTipoDocByCodigo(cct, "convenio");
//								DocumentosUtil.generaYAnexaDocumento(rulectx, itDocConve.getInt("ID"), "Prueba con banda", fileBandaGris, "PDF");
								
							} catch (Exception e) {
								logger.error("Error al incrustar la banda gris Numexp. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
								throw new ISPACRuleException("Error al incrustar la banda gris Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
							}
						}
						else{
							fileBandaGris = pdfFile;
						}
						
						
						

						/*
						 * Insertar el documento
						 * */
						IItem peticion = entitiesAPI.createEntity("FIRMA_DOC_EXTERNO_IDDOC", rulectx.getNumExp());
						peticion.set("ID_PETICION", idPeticion.getRequestId());
						InsertDocument insertDocument0 = new InsertDocument();
						insertDocument0.setAuthentication(authentication);
						insertDocument0.setDocument(CreacionObjetosPortafirmas.getDocumento(cct, docFirma, fileBandaGris));
						insertDocument0.setRequestId(idPeticion.getRequestId());
						InsertDocumentResponse insertdoc = modify.insertDocument(insertDocument0);
						
						logger.warn("Identificador del documento. "+insertdoc.getDocumentId());
						logger.warn("--. "+insertdoc.getPullParser(null).toString());
						
						peticion.set("ID_DOCUMENTO", insertdoc.getDocumentId());
						peticion.set("ID_FASE", rulectx.getStageId());
						peticion.set("ID_TRAMITE", rulectx.getTaskId());
						peticion.set("ESTADOFIRMA", 0);
						peticion.set("ID_PROC", cct.getAPI().getProcess(rulectx.getNumExp()).getInt("ID"));
						peticion.set("ID_RESPONSABLE", ResponsablesUtil.getRespTramite(rulectx).getUID());
						peticion.set("AVISO_ELECT_ENVIAD", "0");
						peticion.store(cct);
						
						/**
						 * Enviar la solicitud
						 * **/
						logger.warn("Enviando solicitud.");

						SendRequest sendRequest8 = new SendRequest();
						sendRequest8.setAuthentication(authentication);
						sendRequest8.setRequestId(idPeticion.getRequestId());
						SendRequestResponse requestResp = modify.sendRequest(sendRequest8);
						logger.warn("Identificador envio solicitud. "+requestResp.getRequestId());
						logger.warn("--. "+requestResp.getPullParser(null).toString());
			
					
					}				
				}
				else{
					logger.error("Ya ha sido mandado este documento.");
				}	
			}
			else{
				logger.warn("El documento ya se ha mandado al PortaFirmas - Query(FIRMA_DOC_EXTERNO_IDDOC) NUMEXP='"+rulectx.getNumExp()+"' AND ID_TRAMITE='"+rulectx.getTaskId()+"'");
			}
			

		} catch (RemoteException e) {
			logger.error("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
		} catch (PfirmaException e) {
			logger.error("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
		}  catch (ADBException e) {
			logger.error("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
		}catch (ISPACException e) {
			logger.error("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" Error - "+e.getMessage(),e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
