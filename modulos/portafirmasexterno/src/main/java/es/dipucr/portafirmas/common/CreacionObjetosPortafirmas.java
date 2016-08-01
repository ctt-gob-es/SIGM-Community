package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;

import org.apache.log4j.Logger;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.AssignJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.EnhancedJob;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.EnhancedJobList;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.EnhancedUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.EnhancedUserJobInfo;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.EnhancedUserList;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.InsertEnhancedUsers;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Job;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Seat;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.SeparateJobToUser;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.UpdateEnhancedJobs;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.User;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Document;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.DocumentType;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.ImportanceLevel;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.RemitterList;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Request;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SignLine;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SignLineList;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SignLineType;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SignType;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Signer;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.SignerList;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.TimestampInfo;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.UserJob;

import com.sun.istack.ByteArrayDataSource;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class CreacionObjetosPortafirmas {
	
	private static final Logger logger = Logger.getLogger(CreacionObjetosPortafirmas.class);
	
public static InsertEnhancedJobs crearObjetoInsertEnhancedJobs(Authentication authentication, Seat seat, Job job){
		
		
		InsertEnhancedJobs usuariosAlta = new InsertEnhancedJobs();
		usuariosAlta.setAuthentication(authentication);
		
		EnhancedJobList jobAlta = new EnhancedJobList();
		EnhancedJob enhancedJob = new EnhancedJob();
		EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo();			
		//seat: Sede del usuario o el cargo. Ver 6.18
		//Obtener la sede
		//obtenerSede();
		
		enhancedUserJobInfo.setSeat(seat);
		//valid: True si el usuario o cargo está vigente, false en caso contrario.
		enhancedUserJobInfo.setValid(true);
		//visibleOtherSeats: True si el usuario es visible para usuarios de otras sedes
		enhancedUserJobInfo.setVisibleOtherSeats(false);	
		
		//enhancedUserJobInfo: Contiene información de la sede y la vigencia del cargo. Ver 6.20
		enhancedJob.setEnhancedUserJobInfo(enhancedUserJobInfo);
		
		/**
		 * Cargos: Definen cargos que pueden ocupar los usuarios. Cada usuario puede ocupar un único
		 * cargo en un momento determinado. Cuando se envía una petición a un cargo, esta es recibida por
		 * todos los usuarios que ocupan dicho cargo.
		 * **/
		EnhancedJob[] vEnhancedJob = new EnhancedJob[1];
		
		//job: Contiene información del cargo. Ver 6.3
		enhancedJob.setJob(job);
		vEnhancedJob[0] = enhancedJob;
		jobAlta.setEnhancedJob(vEnhancedJob);
		
		//Listado de usuarios a insertar
		usuariosAlta.setEnhancedJobList(jobAlta);
		
		return usuariosAlta;
	}
	
	
	public static UpdateEnhancedJobs crearObjetoUpdateEnhancedJobsResponse(Authentication authentication, Seat seat, Job job){

		UpdateEnhancedJobs jobModi = new UpdateEnhancedJobs();
		jobModi.setAuthentication(authentication);

		EnhancedJobList jobAlta = new EnhancedJobList();
		EnhancedJob enhancedJob = new EnhancedJob();
		EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo();
		// seat: Sede del usuario o el cargo. Ver 6.18
		// Obtener la sede
		// obtenerSede();

		enhancedUserJobInfo.setSeat(seat);
		// valid: True si el usuario o cargo está vigente, false en caso
		// contrario.
		enhancedUserJobInfo.setValid(true);
		// visibleOtherSeats: True si el usuario es visible para usuarios de
		// otras sedes
		enhancedUserJobInfo.setVisibleOtherSeats(false);

		// enhancedUserJobInfo: Contiene información de la sede y la vigencia
		// del cargo. Ver 6.20
		enhancedJob.setEnhancedUserJobInfo(enhancedUserJobInfo);

		/**
		 * Cargos: Definen cargos que pueden ocupar los usuarios. Cada usuario
		 * puede ocupar un único cargo en un momento determinado. Cuando se
		 * envía una petición a un cargo, esta es recibida por todos los
		 * usuarios que ocupan dicho cargo.
		 * **/
		EnhancedJob[] vEnhancedJob = new EnhancedJob[1];

		// job: Contiene información del cargo. Ver 6.3
		enhancedJob.setJob(job);
		vEnhancedJob[0] = enhancedJob;
		jobAlta.setEnhancedJob(vEnhancedJob);

		// Listado de usuarios a insertar
		jobModi.setEnhancedJobList(jobAlta);

		return jobModi;
	}
	
	
	
	public static AssignJobToUser crearObjetoAssignJobToUser (Authentication authentication, String cargo, String dni){
		AssignJobToUser assignJobToUser = new AssignJobToUser();
		assignJobToUser.setAuthentication(authentication);
		assignJobToUser.setFstart(new Date());
		//Calendar cal = Calendar.getInstance();
		//assignJobToUser.setFend(cal.getTime());
		assignJobToUser.setJobIdentifier(cargo);
		assignJobToUser.setUserIdentifier(dni);
		return assignJobToUser;
	}
	
	public static InsertEnhancedUsers crearObjetoInsertEnhancedUsers(Authentication authentication, Seat seat, User user){
		
		
		InsertEnhancedUsers usuariosAlta = new InsertEnhancedUsers();
		usuariosAlta.setAuthentication(authentication);
		
		EnhancedUserList usuarAlta = new EnhancedUserList();
		EnhancedUser enhancedUser = new EnhancedUser();
		EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo();			
		//seat: Sede del usuario o el cargo. Ver 6.18
		//Obtener la sede
		//obtenerSede();

		enhancedUserJobInfo.setSeat(seat);
		//valid: True si el usuario o cargo está vigente, false en caso contrario.
		enhancedUserJobInfo.setValid(true);
		//visibleOtherSeats: True si el usuario es visible para usuarios de otras sedes
		enhancedUserJobInfo.setVisibleOtherSeats(false);	
		
		//enhancedUserJobInfo: Contiene información de la sede y la vigencia del cargo. Ver 6.20
		enhancedUser.setEnhancedUserJobInfo(enhancedUserJobInfo);
		
		/**
		 * Cargos: Definen cargos que pueden ocupar los usuarios. Cada usuario puede ocupar un único
		 * cargo en un momento determinado. Cuando se envía una petición a un cargo, esta es recibida por
		 * todos los usuarios que ocupan dicho cargo.
		 * **/
		EnhancedUser[] vEnhancedUser = new EnhancedUser[1];
		
		//job: Contiene información del cargo. Ver 6.3
		enhancedUser.setUser(user);

		vEnhancedUser[0] = enhancedUser;
		usuarAlta.setEnhancedUser(vEnhancedUser);
		
		//Listado de usuarios a insertar
		usuariosAlta.setEnhancedUserList(usuarAlta);
		
		return usuariosAlta;
	}

	@SuppressWarnings("unchecked")
	public static Request getRequest(IRuleContext rulectx) throws ISPACRuleException {
		Request request = new Request();
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();      
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();		
	        //----------------------------------------------------------------------------------------------
		
		
			/**
			 * Peticiones. peticiones que se mandan a los usuarios para su firma
			 * identifier: Identificador de la solicitud. Lo genera el portafirmas cuando se crea la solicitud mendiante createRequest
			 * subject: Asunto de la solicitud. Campo obligatorio
			 * fentry: Fecha sin hora en la que la petición entra en el portafirmas
			 * fstart: Fecha a partir de la cual la petición es válida. La escribe el remitente
			 * fexpiration: Fecha a partir de la cual la petición deja de ser válida. La escribe el remitente
			 * reference: Campo que contiene una referencia que podrá ser vista por el firmante de la aplicación. 
			 * 		Esta referencia no se puede usar para identificar la solicitud a través de los webservices. Hay que usar el campo identifier.
			 * text: Contenido del cuerpo del mensaje. Admite formato HTML.
			 * signType: Indica si la firma se realiza en paralelo o cascada. Los tipos de firma se definen en el punto 6.6
			 * application: Identificador de la aplicación que envía la solicitud. Para poder crear,
			 * 		modificar o leer una solicitud, la aplicación debe colgar de una aplicación padre que
			 * 		coincide con el nombre del usuario autenticado. Por ejemplo, si la petición la manda el 
			 * 		usuario ACCEDA a la aplicación ACCEDA_TRAMITE1, debe existir en el portafirmas 
			 * 		una aplicación llamada ACCEDA de la que cuelgue una subaplicación llamada ACCEDA_TRAMITE1.
			 * importanceLevel: Nivel de importancia de la petición. Se define en el punto
			 * documentList: Lista de documentos. El tipo documentList contiene varios elementos de
			 * 		tipo document, que representan los datos de un documento. El tipo document se define
			 * en el punto 6.4. Cuando se recupera una petición, los elementos de tipo document no incluyen el contenido del mismo.
			 * signLineList: Lista de líneas de firma de la solicitud. Se define en el punto 6.7
			 * remitterList: Lista de remitentes de la solicitud. Se define en el punto 6.8
			 * parameterList: Lista de parámetros de la solicitud. Este campo existe en previsión de usos futuros, pero actualmente no se utiliza.
			 * noticeList: Indica los cambios de estado en los que se ha de enviar una notificación al
			 * 		remitente de la petición. Elelemento noticeList se define en punto 6.9.
			 * actionList: OPCIONAL. SIGNIFICADO DESCONOCIDO
			 * requestStatus: Campo de conveniencia devuelto por el portafirmas indicando el estado 
			 * 		de la solicitud. Se define en el punto 6.13. Este campo se ignorará cuando se cree o actualice una petición
			 * commentList: Lista de comentarios que los usuarios han ido añadiendo a la petición. Se define en el punto 6.14
			 * timestampInfo: Información sobre si se quiere que la petición tenga sello de tiempo. Ver punto 6.15
			 * **/
			
			IItem expediente = ExpedientesUtil.getExpediente(cct, rulectx.getNumExp());
			
			request.setSubject(expediente.getString("ASUNTO"));
			
			/**
			 * Identificador de la aplicación que envía la solicitud.
			 * **/
			//request.setApplication("DIPUCR_WS");//firma xades
			request.setApplication(Configuracion.getAplicacionPADES());
			
			request.setFstart(Calendar.getInstance());
			
			//Calendar cal = Calendar.getInstance();
			//cal.set(2014, Calendar.JULY, 30);
			//request.setFexpiration(cal);
			
			request.setReference(rulectx.getNumExp());
			
			request.setText(expediente.getString("NOMBREPROCEDIMIENTO")+" - "+expediente.getString("ASUNTO"));
			
			
			/**
			 * noticeList: Indica los cambios de estado en los que se ha de enviar una notificación al
			 * 		remitente de la petición
			 * **/
			/*NoticeList noticeList = new NoticeList();
			State[] vState = new State [8];
			State estado = new State();
			estado.setIdentifier("FIRMADO");
			vState[0] = estado;
			State estado1 = new State();
			estado1.setIdentifier("VISTOBUENO");
			vState[1] = estado1;
			State estado2 = new State();
			estado2.setIdentifier("DEVUELTO");
			vState[2] = estado2;
			State estado3 = new State();
			estado3.setIdentifier("CADUCADO");
			vState[3] = estado3;
			State estado4 = new State();
			estado4.setIdentifier("EN ESPERA");
			vState[4] = estado;
			State estado5 = new State();
			estado5.setIdentifier("LEIDO");
			vState[5] = estado5;
			State estado6 = new State();
			estado6.setIdentifier("NUEVO");
			vState[6] = estado6;
			State estado7 = new State();
			estado7.setIdentifier("RETIRADO");
			vState[7] = estado7;			
			noticeList.setState(vState);
			request.setNoticeList(noticeList);*/
			
			/**
			 * timestampInfo: Información sobre si se quiere que la petición tenga sello de tiempo
			 * **/
			TimestampInfo timestampInfo = new TimestampInfo();
			timestampInfo.setAddTimestamp(true);
			request.setTimestampInfo(timestampInfo);
			
			
			/**
			 * Cascada: En la firma en cascada, las diversas lineas de firma, se deben firmar o dar el visto bueno
			 * en el orden en que están en la petición. Si hubiera varias personas en una misma linea, lo podrá
			 * firmar/validar cualquiera de ellas.
			 * Paralelo: En la firma en paralelo, las diversas lineas de firma, se pueden firmar o dar el visto
			 * bueno en cualquier orden idependietemente de como estén en la petición. Si hubiera varias
			 * personas en una misma linea, lo podrá firmar/validar cualquiera de ellas. Si una persona estuviese
			 * en más de una linea, deberá firmar/validar en primer lugar la primera de ellas, aunque eso no
			 * impide que otros usuarios puedan firmar otras lineas.
			 * Primer firmante: Este tipo de firma es igual que la firma en paralelo salvo que un usuario que
			 * esté en 2 lineas de firma, sólo podrá firmar una de ellas
			 * **/
	
			request.setSignType(SignType.value1);			
			
			/**
			 * importanceLevel: Nivel de importancia de la petición
			 * **/
			ImportanceLevel importanceLevel = new ImportanceLevel();
			importanceLevel.setLevelCode("2");
			importanceLevel.setDescription("Alta");
			request.setImportanceLevel(importanceLevel);
			
			IItemCollection itColFirmantes = entitiesAPI.queryEntities("FIRMA_DOC_EXTERNO", "WHERE NUMEXP='"+rulectx.getNumExp()+"' ORDER BY ORDEN_FIRMA ASC");
			Iterator<IItem> iteratorFirmantes = itColFirmantes.iterator();
			/**
			 * signLineList: Lista de líneas de firma de la solicitud.
			 * **/
			SignLineList signlinelist = new SignLineList();
			/**
			 * Si se meten todos los firmantes en este objeto van a firmar sin orden
			 * **/
			SignerList signerlist = new SignerList ();
			boolean noOrdenFirma = false;
			while(iteratorFirmantes.hasNext()){
				
				IItem firmante = iteratorFirmantes.next();
				String dniNombre = firmante.getString("DNI");
				String [] vdniNombre = dniNombre.split(" - ");
				String dni = "";
				if(vdniNombre.length > 0){
					dni = vdniNombre[0];
				}
				
				if(firmante.getString("ORDEN_FIRMA")==null){
					noOrdenFirma = true;
					Signer signer = new Signer();			
					UserJob userJob1 = new UserJob();
					userJob1.setIdentifier(dni);
					signer.setUserJob(userJob1);
					signerlist.addSigner(signer);	
				}
				else{
					Signer signer = new Signer();			
					UserJob userJob2 = new UserJob();
					userJob2.setIdentifier(dni);
					signer.setUserJob(userJob2);
					
					SignLine signLine2 = new SignLine();
					SignerList signerlist2 = new SignerList ();
					signerlist2.addSigner(signer);	
					signLine2.setSignerList(signerlist2);
					signLine2.setType(SignLineType.FIRMA);
					signlinelist.addSignLine(signLine2);
				}
			}
			if(noOrdenFirma){
				SignLine signLine1 = new SignLine();
				signLine1.setSignerList(signerlist);
				signLine1.setType(SignLineType.FIRMA);
				signlinelist.addSignLine(signLine1);
			}	
			
			
			
			//SignLineType Tipo de linea de firma. Acepta dos valores -> FIRMA: La linea debe ser aceptada y firmada.
			//															VISTOBUENO: La linea debe ser aceptada.
			request.setSignLineList(signlinelist);
			
			
			_0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.User usuario = Configuracion.getRemitentePeticionPADES();
			RemitterList remitterList = new RemitterList();
			_0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.User[] user = new _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.User[1];
			user[0] = usuario;
			remitterList.setUser(user);
			request.setRemitterList(remitterList);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return request;
	}
	
	public static SeparateJobToUser crearObjetoSeparateJobToUser(Authentication authentication, String cargo, String dni){
		SeparateJobToUser separateJobToUser = new SeparateJobToUser();
		separateJobToUser.setAuthentication(authentication);
		separateJobToUser.setJobIdentifier(cargo);
		separateJobToUser.setUserIdentifier(dni);
		return separateJobToUser;
	}

	public static Document getDocumento(ClientContext cct, IItem docFirma) throws ISPACRuleException {
		Document doc = new Document();
		try{
			//Nombre del documento (nombre del fichero)
			doc.setName(docFirma.getString("NOMBRE")+"."+docFirma.getString("EXTENSION"));
			//Tipo mime del documento
			doc.setMime(MimetypeMapping.getMimeType(docFirma.getString("EXTENSION")));
			//Tipo de documento. Los tipos de documentos se definen en la aplicación, e indican que tipo de contenido tiene el documento: Informe, petición, convenio,
			DocumentType documentType = new DocumentType();
			documentType.setIdentifier(docFirma.getInt("ID")+"");
			documentType.setDescription(docFirma.getString("DESCRIPCION"));
			documentType.setValid(true);
			doc.setDocumentType(documentType);		
			
			File pdfFile = null;
			if(docFirma.getString("INFOPAG_RDE")!=null){
				pdfFile = DocumentosUtil.getFile(cct, docFirma.getString("INFOPAG_RDE"), docFirma.getString("NOMBRE"), docFirma.getString("EXTENSION_RDE"));
			}
			else{
				if(docFirma.getString("EXTENSION").equals("pdf")){
					pdfFile = DocumentosUtil.getFile(cct, docFirma.getString("INFOPAG"), docFirma.getString("NOMBRE"), docFirma.getString("EXTENSION"));
				}
			}
			//pdfFile = new File("C:\\Teresa\\borrar\\Pap.pdf");
			byte[] data = new byte[(int) pdfFile.length()];
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			FileInputStream fileInputStream = new FileInputStream(pdfFile);
			int count = 0;
			while (fileInputStream.read(data) != -1) {
				byteArrayOutputStream.write(data, 0, count);
			}
			fileInputStream.close();
			byteArrayOutputStream.close();
			// FileDataSource fDataSource = new FileDataSource(file);
			DataHandler contenido = new DataHandler(new ByteArrayDataSource(data,MimetypeMapping.getMimeType(docFirma.getString("EXTENSION"))));
						
	
			doc.setContent(contenido);
			
			doc.setSign(true);
		}catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}
		return doc;
	}

	
}
