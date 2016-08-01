package es.dipucr.portafirmas;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import _0.v2.query.pfirma.cice.juntadeandalucia.PfirmaException;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Comment;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.CommentList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Document;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DocumentList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadDocumentResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadSignResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.GetCVSResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.ImportanceLevel;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Parameter;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.ParameterList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequestResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.RemitterList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Request;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.RequestStatus;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.SignLine;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.SignLineList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.SignType;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Signature;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Signer;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.SignerList;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.State;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.TimestampInfo;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.User;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.UserJob;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ApplicationLogin;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.CSVInfo;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ContenidoInfo;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.CopiaInfo;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.FirmaInfo;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCSV;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCSVE;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCSVResponse;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCSVResponseE;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCopiaFirma;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCopiaFirmaE;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCopiaFirmaResponse;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.GenerarCopiaFirmaResponseE;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.InformacionFirmas_type0;
import es.mpt.dsic.inside.ws.service.impl.EeUtilServiceImplServiceStub.ListaFirmaInfo;
import es.mpt.dsic.inside.ws.service.impl.InSideException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfCopy;

import es.dipucr.portafirmas.client.PortaFirmasConsultaClient;
import es.dipucr.portafirmas.common.Configuracion;
import es.dipucr.portafirmas.common.ServiciosWebPortaFirmasFunciones;
import es.dipucr.portafirmas.dao.procedure.FirmaDocExternoInformSDAO;
import es.dipucr.sigem.api.rule.common.comparece.CompareceConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class RecibirDocPortaFirmasExternoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(RecibirDocPortaFirmasExternoRule.class);
	
	/**
	 * Ruta por defecto de la imagen del logo dipu
	 * /home/sigem/SIGEM/conf/SIGEM_Tramitacion
	 */
    private static Font fuenteNegra12 = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK);
    private static Font fuenteNegraNormal12 = new Font(Font.TIMES_ROMAN, 12, Font.NORMAL, Color.BLACK);
    private static Font fuenteNegra25 = new Font(Font.TIMES_ROMAN, 15, Font.BOLD, Color.BLUE);

 


	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean cerrar = false;
		try{
			/*************************************************************************************/
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IEntitiesAPI entitiesAPI =  cct.getAPI().getEntitiesAPI();
			/**************************************************************************************/
			
			IItem itTramite = TramitesUtil.getTramiteByCode(rulectx, "gen-doc-firmar");
			
			IItemCollection itColTramites = TramitesUtil.queryTramites(cct, "WHERE ID_TRAM_CTL="+itTramite.getInt("ID"));
			Iterator<IItem> itTramites = itColTramites.iterator();
			StringBuffer queryTramite = new StringBuffer("");
			queryTramite.append("(ID_TRAMITE='"+ itTramites.next().getInt("ID_TRAM_EXP")+"'");
			while(itTramites.hasNext()){
				IItem tramite = itTramites.next();
				queryTramite.append(" OR ID_TRAMITE='"+tramite.getInt("ID_TRAM_EXP")+"'");
			}
			queryTramite.append(")");
			
			//Obtengo el identificador de la peticion
			String query = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND ID_FASE='"+rulectx.getStageId()+"' AND "+queryTramite.toString();
			IItemCollection itColPeticion = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO_IDDOC", query);
			Iterator<IItem> itPeticion = itColPeticion.iterator();
			while(itPeticion.hasNext()){

				IItem peticion = itPeticion.next();		
				String direccionPortaFirmaExternoConsulta = ServiciosWebPortaFirmasFunciones.getDireccionSWConsulta();
				PortaFirmasConsultaClient consulta = new PortaFirmasConsultaClient(direccionPortaFirmaExternoConsulta);
				Authentication authentication = new Authentication();		
				String userName = "DIPUCR_WS_PADES";
				authentication.setUserName(userName);
				String password = "DIPUCR_WS_PADES";
				authentication.setPassword(password);
				
				QueryRequestResponse informacion = consulta.recuperaDetallePeticion(authentication, peticion.getString("ID_PETICION"));
				String valorPet = informacion.getRequest().getRequestStatus().getValue();
				if (!valorPet.equals("EN PROCESO")) {
					cerrar = true;
				}
			}
		}catch (ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (AxisFault e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (PfirmaException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return cerrar;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*************************************************************************************/
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IEntitiesAPI entitiesAPI =  cct.getAPI().getEntitiesAPI();
			/**************************************************************************************/
			IItem itTramite = TramitesUtil.getTramiteByCode(rulectx, "gen-doc-firmar");
			
			IItemCollection itColTramites = TramitesUtil.queryTramites(cct, "WHERE ID_TRAM_CTL="+itTramite.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'");
			Iterator<IItem> itTramites = itColTramites.iterator();
			StringBuffer queryTramite = new StringBuffer("");
			queryTramite.append("(ID_TRAMITE='"+ itTramites.next().getInt("ID_TRAM_EXP")+"'");
			while(itTramites.hasNext()){
				IItem tramite = itTramites.next();
				queryTramite.append(" OR ID_TRAMITE='"+tramite.getInt("ID_TRAM_EXP")+"'");
			}
			queryTramite.append(")");
			
			//Obtengo el identificador de la peticion
			String query = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND ID_FASE='"+rulectx.getStageId()+"' AND "+queryTramite.toString();
			IItemCollection itColPeticion = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO_IDDOC", query);
			Iterator<IItem> itPeticion = itColPeticion.iterator();
			while(itPeticion.hasNext()){
				IItem peticion = itPeticion.next();		
				String direccionPortaFirmaExternoConsulta = ServiciosWebPortaFirmasFunciones.getDireccionSWConsulta();				
				PortaFirmasConsultaClient consulta = new PortaFirmasConsultaClient(direccionPortaFirmaExternoConsulta);
				Authentication authentication = new Authentication();		
				String userName = "DIPUCR_WS_PADES";
				authentication.setUserName(userName);
				String password = "DIPUCR_WS_PADES";
				authentication.setPassword(password);
				DownloadSignResponse downloadSign = consulta.recuperaDocumentosBySolicitud(authentication, peticion.getString("ID_DOCUMENTO"));
				
				if(downloadSign!=null){
					Signature signature = downloadSign.getSignature();
					if(signature.getSign()==true){
						DataHandler dh = null;
						if(signature.getContent()!=null) dh = signature.getContent();
						if(dh!=null){
							
							String ruta = FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_PDF);
							InputStream ie = dh.getInputStream();
							int documentId = DocumentosUtil.getIdTipoDocByCodigo(cct, "doc-Firmado");
							File documento = new File(ruta);
							if (documento.createNewFile()) {
								OutputStream os1 = new FileOutputStream(documento);
								byte[] buffer = new byte[1024];
								int numRead;
								while ( (numRead = ie.read(buffer) ) != -1) {
							          os1.write(buffer, 0, numRead);
							      }

								os1.close();
							}
							
							//Código de verificación electrónica de la firma digital.							
							GetCVSResponse responseCVS = consulta.devolverCVS(authentication, signature);							
							IItem docTramite = DocumentosUtil.generaYAnexaDocumento(rulectx, documentId, "", documento, Constants._EXTENSION_PDF);
							docTramite.set("COD_VERIFICACION", responseCVS.getCvs());
							docTramite.set("COD_COTEJO", responseCVS.getCvs());
							//Obtener el nombre del documento
							docTramite.set("DESCRIPCION", docTramite.getString("NOMBRE"));
							docTramite.store(cct);
							ie.close();
							if(documento != null && documento.exists())documento.delete();
							obtenerInformacionPeticion(authentication, signature, consulta, rulectx, peticion.getString("ID_PETICION"));
							peticion.set("ESTADOFIRMA", 1);
							peticion.store(cct);
						}
					}
				}
			}
			
			
			
		}catch (ISPACRuleException e){
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (AxisFault e) {
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (RemoteException e) {
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (PfirmaException e) {
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error en el numExp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}

		return new Boolean(true);
	}


	@SuppressWarnings("unchecked")
	private void obtenerInformacionPeticion(Authentication authentication, Signature signature, PortaFirmasConsultaClient consulta, IRuleContext rulectx, String idPeticion) throws ISPACRuleException {
		
		try {
			
			/********************************************************************/
			ClientContext cct = (ClientContext)rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			/********************************************************************/
			
			//Montamos el documento
			File fileDoc = new File(FileTemporaryManager.getInstance().getFileTemporaryPath()+ "/"+ FileTemporaryManager.getInstance().newFileName("."+Constants._EXTENSION_PDF));
			com.lowagie.text.Document documentJustificante = new com.lowagie.text.Document();

			PdfCopy.getInstance(documentJustificante, new FileOutputStream(fileDoc));
			
			documentJustificante.open();
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			String imageLogoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_LOGO_PATH_DIPUCR);
			String imageFondoPath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_FONDO_PATH_DIPUCR);
			String imagePiePath = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entidad + File.separator, "/SIGEM_TramitacionWeb") + CompareceConfiguration.getInstanceNoSingleton(entidad).getProperty(CompareceConfiguration.IMAGE_PIE_PATH_DIPUCR);


			File logoURL = new File(imageLogoPath);
			if (logoURL != null) {
				Image logo = Image.getInstance(imageLogoPath);
				logo.scalePercent(50);
				documentJustificante.add(logo);
			}
			File fondoURL = new File(imageFondoPath);
			if(fondoURL != null){
				Image fondo = Image.getInstance(imageFondoPath);
				fondo.setAbsolutePosition(250, 50);
				fondo.scalePercent(70);
				documentJustificante.add(fondo);
				
			}

			File pieURL = new File(imagePiePath);
			if(pieURL != null){
				Image pie = Image.getInstance(imagePiePath);
				pie.setAbsolutePosition(documentJustificante.getPageSize().getWidth() - 550, 15);
				pie.scalePercent(80);
				documentJustificante.add(pie);
				
			}

			documentJustificante.add(new Phrase("\n\n"));
			
			Paragraph parrafoHoja = new Paragraph();
			 
	        // Agregamos una linea en blanco
	        agregarLineasEnBlanco(parrafoHoja, 1);
			
			parrafoHoja.add(new Paragraph("JUSTIFICANTE DE FIRMA", fuenteNegra25) );
			parrafoHoja.add(new Phrase("\n\n\n"));

			documentJustificante.add(parrafoHoja);
			
			IItem informacionEntidad = entitiesAPI.createEntity("FIRMA_DOC_EXTERNO_INFORM", rulectx.getNumExp());
			informacionEntidad.set("TRAMITE", rulectx.getTaskId());
			informacionEntidad.set("FASE", rulectx.getStageId());
			//Código de verificación electrónica de la firma digital.
			
			GetCVSResponse responseCsv = consulta.devolverCVS(authentication, signature);
			if(responseCsv!=null){
				String sCvs = responseCsv.getCvs();
				informacionEntidad.set("CVS", sCvs);
				documentJustificante.add(new Phrase("Código de Verificación Electrónica: ", fuenteNegra12));
				documentJustificante.add(new Phrase(sCvs, fuenteNegraNormal12));
				documentJustificante.add(new Phrase("\n"));
			}		
			
			/**
			 * El elemento de tipo request, representa las peticiones que se mandan a los usuarios para su firma.
			 * **/
			QueryRequestResponse informacion = consulta.recuperaDetallePeticion(authentication, idPeticion);
			Request request = informacion.getRequest();
			if(request!=null){
				
				
				//Identificador de la aplicación que envía la solicitud
				String aplicacion = request.getApplication();
				informacionEntidad.set("APPLICATION", aplicacion);
				documentJustificante.add(new Phrase("Identificador de la aplicación que envía la solicitud: ", fuenteNegra12));
				documentJustificante.add(new Phrase(aplicacion, fuenteNegraNormal12));
				documentJustificante.add(new Phrase("\n"));
				
				//Fecha sin hora en la que la petición entra en el portafirmas
				Calendar fEntrada = request.getFentry();
				if(fEntrada!=null){
					informacionEntidad.set("FENTRY", fEntrada.getTime());
					documentJustificante.add(new Phrase("Fecha en la que la petición entra en el portafirmas: ", fuenteNegra12));
					documentJustificante.add(new Phrase(FechasUtil.getFormattedDateTimeForQuery(fEntrada.getTime()), fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}				
				
				//Fecha a partir de la cual la petición deja de ser válida. La escribe el remitente
				Calendar fExp = request.getFexpiration();
				if(fExp!=null){
					informacionEntidad.set("FEXPIRATION", fExp.getTime());
					documentJustificante.add(new Phrase("Fecha a partir de la cual la petición deja de ser válida: ", fuenteNegra12));
					documentJustificante.add(new Phrase(FechasUtil.getFormattedDateTimeForQuery(fExp.getTime()), fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}
				
				//Fecha a partir de la cual la petición es válida. La escribe el remitente
				Calendar fStart = request.getFstart();
				if(fStart!=null){
					informacionEntidad.set("FSTART", fStart.getTime());
					documentJustificante.add(new Phrase("Fecha a partir de la cual la petición es válida: ", fuenteNegra12));
					documentJustificante.add(new Phrase(FechasUtil.getFormattedDateTimeForQuery(fStart.getTime()), fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}				
				
				//Identificador de la solicitud. Lo genera el portafirmas cuando se crea la
				//solicitud mendiante createRequest
				String identificador = request.getIdentifier();
				informacionEntidad.set("IDENTIFIER", identificador);
				documentJustificante.add(new Phrase("Identificador de la solicitud: ", fuenteNegra12));
				documentJustificante.add(new Phrase(identificador, fuenteNegraNormal12));
				documentJustificante.add(new Phrase("\n"));
				
				//Campo que contiene una referencia que podrá ser vista por el firmante de la
				//aplicación. Esta referencia no se puede usar para identificar la solicitud a través de los
				//webservices. Hay que usar el campo identifier
				//String numexp = request.getReference();
				
				//Campo de conveniencia devuelto por el portafirmas indicando el estado
				//de la solicitud.
				RequestStatus requestStatus = request.getRequestStatus();
				if(requestStatus!=null){
					String estado = requestStatus.getValue();
					informacionEntidad.set("REQUESTSTATUS", estado);
					documentJustificante.add(new Phrase("Estado de la solicitud: ", fuenteNegra12));
					documentJustificante.add(new Phrase(estado, fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}
				
				//Indica si la firma se realiza en paralelo o cascada
				SignType signType = request.getSignType();
				if(signType!=null){
					String formaFirma = signType.getValue();
					informacionEntidad.set("SIGNTYPE", formaFirma);
					documentJustificante.add(new Phrase("Forma de realización de la firma: ", fuenteNegra12));
					documentJustificante.add(new Phrase(formaFirma, fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}				
				
				//Asunto de la solicitud.
				String subject = request.getSubject();
				informacionEntidad.set("SUBJECT", subject);
				documentJustificante.add(new Phrase("Asunto de la solicitud: ", fuenteNegra12));
				documentJustificante.add(new Phrase(subject, fuenteNegraNormal12));
				documentJustificante.add(new Phrase("\n"));
				
				//Contenido del cuerpo del mensaje
				String text = request.getText();
				informacionEntidad.set("TEXT", text);
				documentJustificante.add(new Phrase("Contenido del cuerpo del mensaje: ", fuenteNegra12));
				documentJustificante.add(new Phrase(text, fuenteNegraNormal12));
				documentJustificante.add(new Phrase("\n"));
				
				//Nivel de importancia de la petición
				ImportanceLevel imporLevel = request.getImportanceLevel();
				if(imporLevel!=null){
					String nivel = imporLevel.getLevelCode();
					String descripcion = imporLevel.getDescription();
					informacionEntidad.set("IMPORTANCELEVEL", nivel+" - "+descripcion);
					documentJustificante.add(new Phrase("Nivel de importancia de la petición: ", fuenteNegra12));
					documentJustificante.add(new Phrase(nivel, fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
					documentJustificante.add(new Phrase(descripcion, fuenteNegraNormal12));
					documentJustificante.add(new Phrase("\n"));
				}
				
				//Información sobre si se quiere que la petición tenga sello de tiempo
				//TimestampInfo timestampInfo = request.getTimestampInfo();
				//informacionEntidad.set("TIMESTAMPINFO", timestampInfo.getAddTimestamp()+");
				
				informacionEntidad.store(cct);

				int id = informacionEntidad.getInt("ID");

				DbCnt cnt = cct.getConnection();
				
				//Lista de comentarios que los usuarios han ido añadiendo a la petición
				CommentList commentList = request.getCommentList();
				if(commentList!=null){
					Comment[] vComentarios = commentList.getComment();
					if(vComentarios!=null && vComentarios.length>0){
						documentJustificante.add(new Phrase("Lista de comentarios que los usuarios han ido añadiendo a la petición: ", fuenteNegra12));
					}					
					for (int i = 0; vComentarios!=null && i < vComentarios.length; i++) {
						
						Comment comment = vComentarios[i];
						//Fecha de última modificación
						Calendar fUltimaMod = comment.getFmodify();
						//Asunto del comentario
						String asunto = comment.getSubject();
						//Texto del comentario
						String textoCOm = comment.getTextComment();
						//Usuario que escribió el comentario.
						UserJob usuario = comment.getUser();
						//Usuario que escribió el comentario.
						String dniUsuairo = usuario.getIdentifier();
						
						IItemCollection itCollect = ParticipantesUtil.getParticipantes(cct, rulectx.getNumExp(), "(NDOC = '"+dniUsuairo+"')", "");
						Iterator<IItem> iterPar = itCollect.iterator();
						String nombre = "";
						while(iterPar.hasNext()){
							IItem partic = iterPar.next();
							if(partic.getString("NOMBRE")!=null) nombre = partic.getString("NOMBRE");
						}
						
						documentJustificante.add(new Phrase(dniUsuairo+" - "+nombre+": "+asunto+", "+textoCOm+", "+FechasUtil.getFormattedDateTimeForQuery(fUltimaMod.getTime()), fuenteNegraNormal12));
						documentJustificante.add(new Phrase("\n"));
						
						FirmaDocExternoInformSDAO pcftdao = new FirmaDocExternoInformSDAO(cnt);
						pcftdao.createNew(cnt);
						pcftdao.set("FIELD", "COMMENTLIST");
						pcftdao.set("REG_ID", id);
						pcftdao.set("VALUE", dniUsuairo+" - "+asunto+" - "+textoCOm+" - "+fUltimaMod);
						pcftdao.store(cnt);
					}
				}
				
				
				//Lista de documentos. El tipo documentList contiene varios elementos de
				//tipo document, que representan los datos de un documento. Cuando se recupera una petición, los elementos de tipo document no
				//incluyen el contenido del mismo
				DocumentList documentoList = request.getDocumentList();
				if(documentoList!=null){
					Document [] vdoc = documentoList.getDocument();
					if(vdoc!=null && vdoc.length>0){
						documentJustificante.add(new Phrase("Lista de documentos: ", fuenteNegra12));
					}
					for (int i = 0; vdoc!=null && i < vdoc.length; i++) {
						Document document = vdoc[i];
						String nombre = document.getName();
						boolean firmado = document.getSign();
						String strinFirm = "";
						if(firmado){
							strinFirm="Documento firmado";
						}
						else{
							strinFirm="Documento sin firma";
						}
						
						documentJustificante.add(new Phrase(nombre+" - "+strinFirm, fuenteNegraNormal12));
						documentJustificante.add(new Phrase("\n"));
						
						
						FirmaDocExternoInformSDAO pcftdao = new FirmaDocExternoInformSDAO(cnt);
						pcftdao.createNew(cnt);
						pcftdao.set("FIELD", "DOCUMENTLIST");
						pcftdao.set("REG_ID", id);
						pcftdao.set("VALUE", nombre+" - "+firmado);
						pcftdao.store(cnt);
					}
				}

				
				//Indica los cambios de estado en los que se ha de enviar una notificación al
				//remitente de la petición
				//NoticeList noticeList = request.getNoticeList();
				
				//Lista de parámetros de la solicitud. Este campo existe en previsión de
				//usos futuros, pero actualmente no se utiliza
				ParameterList parameterList = request.getParameterList();
				if(parameterList!=null){
					Parameter [] vParam = parameterList.getParameter();
					if(vParam!=null && vParam.length>0){
						documentJustificante.add(new Phrase("Lista de parámetros de la solicitud: ", fuenteNegra12));
					}
					for (int i = 0; vParam!=null && i < vParam.length; i++) {
						Parameter parameter = vParam[i];
						String ident = parameter.getIdentifier();
						String value = parameter.getValue();
						
						documentJustificante.add(new Phrase(ident+" - "+value, fuenteNegraNormal12));
						documentJustificante.add(new Phrase("\n"));
						
						FirmaDocExternoInformSDAO pcftdao = new FirmaDocExternoInformSDAO(cnt);
						pcftdao.createNew(cnt);
						pcftdao.set("FIELD", "PARAMETERLIST");
						pcftdao.set("REG_ID", id);
						pcftdao.set("VALUE", ident+" - "+value);
						pcftdao.store(cnt);
					}
				}
				
				//Lista de remitentes de la solicitud
				/**
				 * El objeto remitterList nos permite indicar quienes son las personas que remiten una petición. Los
				 * remitentes deben ser usuarios válidos de la aplicación. Aunque el modelo de datos permite poner varios
				 * remitentes, la aplicación actualmente limita el número a 1 remitente, que además debe ser la misma
				 * aplicación que se pone como origen de la petición
				 * **/
				RemitterList remitterList = request.getRemitterList();
				if(remitterList!=null){
					User[] vUser = remitterList.getUser();
					if(vUser!=null && vUser.length>0){
						documentJustificante.add(new Phrase("Lista de remitentes de la solicitud: ", fuenteNegra12));
					}
					for (int i = 0; vUser!=null && i < vUser.length; i++) {
						User user = vUser[i];
						String ident = user.getIdentifier();
						String nombre = user.getName();
						String apell1 = user.getSurname1();
						String apell2 = user.getSurname2();
						
						documentJustificante.add(new Phrase(ident+" - "+nombre, fuenteNegraNormal12));
						documentJustificante.add(new Phrase("\n"));
						
						FirmaDocExternoInformSDAO pcftdao = new FirmaDocExternoInformSDAO(cnt);
						pcftdao.createNew(cnt);
						pcftdao.set("FIELD", "REMITTERLIST");
						pcftdao.set("REG_ID", id);
						pcftdao.set("VALUE", ident+" - "+nombre+" "+apell1+" "+apell2);
						pcftdao.store(cnt);
					}
				}
				
				//Lista de líneas de firma de la solicitud
				SignLineList signLineList = request.getSignLineList();
				if(signLineList!=null){
					SignLine [] vSignLine = signLineList.getSignLine();
					if(vSignLine!=null && vSignLine.length>0){
						documentJustificante.add(new Phrase("Lista de líneas de firma de la solicitud: ", fuenteNegra12));
					}
					for (int i = 0; vSignLine!=null && i < vSignLine.length; i++) {
						SignLine signLine = vSignLine[i];
						//Lista de personas y cargos que componen la linea de firma
						SignerList signerList = signLine.getSignerList();
						if(signerList!=null){
							Signer [] vSigner = signerList.getSigner();
							
							for (int j = 0; vSigner!=null && j < vSigner.length; j++) {
								Signer signer = vSigner[j];
								//Indica la fecha del último cambio de estado. Este campo sólo está relleno cuando se recibe
								//información desde el servidor de portafirma, y será ignorado si se envía para mdoficiar una
								//petición
								Calendar fecha = signer.getFstate();
								//Indica el estado de la linea de firma a la que pertenece el firmante. Este campo sólo está
								//relleno cuando se recibe información desde el servidor y será ignorado si se envía para mdoficiar
								//una petición.
								State state = signer.getState();
								String ident = "";
								if(state!=null){
									ident = state.getIdentifier();
								}
								//userJob es un elemento que puede representar tanto a un usuario de la aplicación como a
								//un cargo.
								UserJob userJob = signer.getUserJob();
								String identFirmante = "";
								if(userJob!=null){
									identFirmante = userJob.getIdentifier();
								}
								
								String nombreFirmante = "";
								IItemCollection itParti = ParticipantesUtil.getParticipantes(cct, rulectx.getNumExp(), "(NDOC = '"+identFirmante+"')", "");
								Iterator<IItem> iteratorParti = itParti.iterator();
								while(iteratorParti.hasNext()){
									IItem partic = iteratorParti.next();
									nombreFirmante = partic.getString("NOMBRE");
								}
								
								documentJustificante.add(new Phrase("\n"));
								documentJustificante.add(new Phrase("* "+identFirmante +": "+nombreFirmante+" - "+FechasUtil.getFormattedDateTimeForQuery(fecha.getTime())+" - "+ident, fuenteNegraNormal12));
								documentJustificante.add(new Phrase("\n"));
								
								FirmaDocExternoInformSDAO pcftdao = new FirmaDocExternoInformSDAO(cnt);
								pcftdao.createNew(cnt);
								pcftdao.set("FIELD", "SIGNLINELIST");
								pcftdao.set("REG_ID", id);
								pcftdao.set("VALUE", identFirmante+" - "+FechasUtil.getFormattedDateTimeForQuery(fecha.getTime())+" - "+ident);
								pcftdao.store(cnt);
							}
						}
						//signLineType: Tipo de linea de firma. Acepta 2 valores:
						//	FIRMA: La linea debe ser aceptada y firmada.
						//	VISTOBUENO: La linea debe ser aceptada.
						/*SignLineType lineType = signLine.getType();
						if(lineType!=null){
							String valorFirma = lineType.getValue();
						}*/
					}
				}
				documentJustificante.close();
				
				if (fileDoc != null) {
					int documentId = DocumentosUtil.getIdTipoDocByCodigo(cct, "justif-petic");
					IItem docTramite = DocumentosUtil.generaYAnexaDocumento(rulectx, documentId, "Informe", fileDoc, "pdf");
					docTramite.store(cct);
				}
				
			}
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (PfirmaException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}		
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	 private static void agregarLineasEnBlanco(Paragraph parrafo, int nLineas) 
	    {
	        for (int i = 0; i < nLineas; i++) 
	            parrafo.add(new Paragraph(" "));
	    }

}












