/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.BeanUtils;

import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.advice.client.WSConstants;
import es.seap.minhap.portafirmas.ws.bean.Comment;
import es.seap.minhap.portafirmas.ws.bean.CommentList;
import es.seap.minhap.portafirmas.ws.bean.Document;
import es.seap.minhap.portafirmas.ws.bean.DocumentList;
import es.seap.minhap.portafirmas.ws.bean.DocumentType;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJob;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUser;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobAssociated;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobAssociatedList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobInfo;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.ImportanceLevel;
import es.seap.minhap.portafirmas.ws.bean.ImportanceLevelList;
import es.seap.minhap.portafirmas.ws.bean.Job;
import es.seap.minhap.portafirmas.ws.bean.JobList;
import es.seap.minhap.portafirmas.ws.bean.Parameter;
import es.seap.minhap.portafirmas.ws.bean.ParameterList;
import es.seap.minhap.portafirmas.ws.bean.RemitterList;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.Seat;
import es.seap.minhap.portafirmas.ws.bean.SeatList;
import es.seap.minhap.portafirmas.ws.bean.SignLine;
import es.seap.minhap.portafirmas.ws.bean.SignLineList;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.bean.SignatureSerializable;
import es.seap.minhap.portafirmas.ws.bean.Signer;
import es.seap.minhap.portafirmas.ws.bean.SignerList;
import es.seap.minhap.portafirmas.ws.bean.State;
import es.seap.minhap.portafirmas.ws.bean.StateList;
import es.seap.minhap.portafirmas.ws.bean.TimestampInfo;
import es.seap.minhap.portafirmas.ws.bean.User;
import es.seap.minhap.portafirmas.ws.bean.UserList;

public class WSUtil {
	
	private static Set<String> WS_PROFILES;
	private static Set<String> WS_USER_MANAGER_PROFILES;

	public synchronized static Set<String> getWsProfiles () {
		if (WS_PROFILES == null) {
			WS_PROFILES = new HashSet<>();
			WS_PROFILES.add(es.seap.minhap.portafirmas.utils.Constants.C_PROFILES_WEBSERVICE);
		}
		return WS_PROFILES;
	}
	
	public synchronized static Set<String> getWsUserManagerProfiles () {
		if (WS_USER_MANAGER_PROFILES == null) {
			WS_USER_MANAGER_PROFILES = new HashSet<>();
			WS_USER_MANAGER_PROFILES.add(es.seap.minhap.portafirmas.utils.Constants.C_PROFILES_WEBSERVICE);
			WS_USER_MANAGER_PROFILES.add(Constants.C_PROFILES_ADMIN_PROVINCE);
		}
		return WS_USER_MANAGER_PROFILES;
	}
	/**
	 * Transforma un objeto de tipo de documento bean en un objeto de tipo documento de negocio
	 * @param documentType el tipo de documento
	 * @param applicationDTO la aplicaci&oacute;n
	 * @return el objeto de tipo documento de negocio
	 */
	public static PfDocumentTypesDTO documentTypeToPfDocumentTypeDTO(
			DocumentType documentType, PfApplicationsDTO applicationDTO) {
		PfDocumentTypesDTO documentTypeDTO = new PfDocumentTypesDTO();
		documentTypeDTO.setCdocumentType(documentType.getIdentifier());
		documentTypeDTO.setDdocumentType(documentType.getDescription());
		documentTypeDTO.setLvalid(documentType.getValid());
		documentTypeDTO.setPfApplication(applicationDTO);

		return documentTypeDTO;
	}
	
	public static Signature signatureSerializableToSignature(SignatureSerializable signatureSerializable) {
		Signature signature = new Signature();
		BeanUtils.copyProperties(signatureSerializable, signature);
		
		if (signatureSerializable.getContentBytes() != null) {
			DataHandler content = new DataHandler(new ByteArrayDataSource(signatureSerializable.getContentBytes(), signatureSerializable.getMimeType()));
			signature.setContent(content);
		}
			
		return signature;
	}	

	// public static List<AbstractBaseDTO> documentListToPfDocumentDTOList(
	// DocumentTypeList documentTypeList, PfApplicationsDTO applicationDTO) {
	// List<AbstractBaseDTO> list = null;
	// if (documentTypeList != null) {
	// list = new ArrayList<AbstractBaseDTO>();
	// Iterator<DocumentType> it = documentTypeList.getDocumentType()
	// .iterator();
	// PfDocumentTypesDTO documentTypeDTO = null;
	// DocumentType documentType = null;
	// while (it.hasNext()) {
	// documentType = it.next();
	// documentTypeDTO = new PfDocumentTypesDTO();
	// documentTypeDTO.setCdocumentType(documentType.getIdentifier());
	// documentTypeDTO.setDdocumentType(documentType.getDescription());
	// documentTypeDTO.setLvalid(documentType.getValid());
	// documentTypeDTO.setPfApplication(applicationDTO);
	// list.add(documentTypeDTO);
	// }
	// }
	//
	// return list;
	// }

	public static UserList pfUsersDTOListToUserList(
			List<AbstractBaseDTO> userListDTO) {
		UserList list = null;
		if (userListDTO != null) {
			list = new UserList();
			Iterator<AbstractBaseDTO> it = userListDTO.iterator();
			PfUsersDTO userDTO = null;
			User user = null;
			while (it.hasNext()) {
				userDTO = (PfUsersDTO) it.next();
				user = userDTOToUser(userDTO);

				list.getUser().add(user);
			}
		}

		return list;
	}
	
	public static EnhancedUserList pfUsersDTOListToEnhancedUserList (List<AbstractBaseDTO> userListDTO) {
		EnhancedUserList list = new EnhancedUserList();
		if (userListDTO != null) {			
			Iterator<AbstractBaseDTO> it = userListDTO.iterator();
			PfUsersDTO userDTO = null;
			EnhancedUser userEnhanced = null;
			while (it.hasNext()) {
				userDTO = (PfUsersDTO) it.next();
				userEnhanced = userDTOToEnhancedUser(userDTO);

				list.getEnhancedUser().add(userEnhanced);
			}
		}

		return list;
	}
	
	public static EnhancedJobList pfUsersDTOListToEnhancedJobList (List<AbstractBaseDTO> userListDTO) {
		EnhancedJobList list = new EnhancedJobList();
		if (userListDTO != null) {		
			Iterator<AbstractBaseDTO> it = userListDTO.iterator();
			PfUsersDTO userDTO = null;
			EnhancedJob jobEnhanced = null;
			while (it.hasNext()) {
				userDTO = (PfUsersDTO) it.next();
				jobEnhanced = userDTOToEnhancedJob(userDTO);

				list.getEnhancedJob().add(jobEnhanced);
			}
		}

		return list;
	}

	public static SeatList pfProvinceDTOListToSeatList (List<AbstractBaseDTO> provinceListDTO) {
		SeatList list = new SeatList ();
		if (provinceListDTO != null) {
			Iterator<AbstractBaseDTO> it = provinceListDTO.iterator();
			PfProvinceDTO provinceDTO = null;
			Seat seat = null;
			while (it.hasNext()) {
				provinceDTO = (PfProvinceDTO) it.next();
				seat = provinceDTOToSeat(provinceDTO);
				list.getSeat().add(seat);
			}
		}
		return list;
	}

	
	public static Request pfRequestDTOToRequest(PfRequestsDTO requestDTO, String text, Map<String, String> mapaParametros, TagBO tagBO) {	
			
		Request request = pfRequestDTOToRequestSinStatus (requestDTO, text, mapaParametros, tagBO);
		if (request != null) {		
			String requestStatus = WSConstants.REQUEST_STATE_ACCEPTED;
			for (SignLine signLine: request.getSignLineList().getSignLine()) {
				boolean lineaFirmadaVistoBueno = false;
				boolean lineaRechazada = false;
				boolean lineaCaducada = false;
				boolean lineaRetirada = false;
				for (Signer signer: signLine.getSignerList().getSigner()) {
					if (signer != null) {
						String state = signer.getState().getIdentifier();
						if (Constants.C_TAG_SIGNED.equals(state) || Constants.C_TAG_PASSED.equals(state)) {
							lineaFirmadaVistoBueno = true;
						} else if (Constants.C_TAG_REJECTED.equals(state)) {
							lineaRechazada = true;
						} else if (Constants.C_TAG_EXPIRED.equals(state)) {
							lineaCaducada = true;						
						} else if (Constants.C_TAG_REMOVED.equals(state)) {
							lineaRetirada = true;						
						}
					}
				}
				if (lineaRechazada) {
					requestStatus = WSConstants.REQUEST_STATE_REJECTED;
					break;
				} else if (lineaCaducada) {
					requestStatus = WSConstants.REQUEST_STATE_EXPIRED;
					break;		
				} else if (lineaRetirada) {
					requestStatus = WSConstants.REQUEST_STATE_REMOVED;
					break;
				} else if (!lineaFirmadaVistoBueno) {
					requestStatus = WSConstants.REQUEST_STATE_AWAITING;
				}
			}
			request.setRequestStatus(requestStatus);	
			
		}

		return request;
	}	
	
	
	
		
	private static Request pfRequestDTOToRequestSinStatus (PfRequestsDTO requestDTO, String text, Map<String, String> mapaParametros, TagBO tagBO) {
		Request request = null;
		
		if (requestDTO != null) {
			request = new Request();
			request.setApplication(requestDTO.getPfApplication().getCapplication());
			request.setDocumentList(pfDocumentDTOSetToDocumentList(requestDTO.getPfDocuments()));
			request.setFentry(dateToGregorian(requestDTO.getFentry()));
			request.setFexpiration(dateToGregorian(requestDTO.getFexpiration()));
			request.setFstart(dateToGregorian((requestDTO.getFstart())));
			request.setIdentifier(requestDTO.getChash());
			// TODO: Esto de momento queda sin rellenar ...
			request.setParameterList(new ParameterList());
			request.setReference(requestDTO.getDreference());
			request.setRemitterList(pfRemitterDTOSetToRemitterList(requestDTO.getPfUsersRemitters()));
			request.setSignLineList(pfSignLineDTOSetToSignLineList(requestDTO, tagBO));
			// TEMPORAL: Comprueba si la aplicación se ha actualizado con los cambios del servicio de respuesta
			// para decidir si se envía o no la lista de comentarios
			// Obtengo el contexto de Spring

			String versionAnterior = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_VERSION_ANTERIOR);
			if (!Constants.C_YES.equals(versionAnterior)) {
				request.setCommentList(pfCommentSetDTOToCommentList(requestDTO.getPfComments()));
			}
			
			if (requestDTO.getLfirstSignerSign()) {
				request.setSignType(Constants.FIRMA_PRIMER_FIRMANTE);
			} else if (requestDTO.getLcascadeSign()) {
				request.setSignType(Constants.FIRMA_CASCADA);
			} else {
				request.setSignType(Constants.FIRMA_PARALELA);
			}
			request.setSubject(requestDTO.getDsubject());
			request.setText(text);

			// Timestamp //
			TimestampInfo timestampInfo = new TimestampInfo();
			if (requestDTO.getLtimestamp().equals(Constants.C_YES)) {
				timestampInfo.setAddTimestamp(true);
				request.setTimestampInfo(timestampInfo);
			} else if (requestDTO.getLtimestamp().equals(Constants.C_NOT)) {
				timestampInfo.setAddTimestamp(false);
				request.setTimestampInfo(timestampInfo);
			}
		}
		return request;
	}
	
	
	public static DocumentTypeList pfDocumentTypeDTOListToDocumentTypeList(List<PfDocumentTypesDTO> documentTypeListDTO) {
		DocumentTypeList list = null;
		if (documentTypeListDTO != null) {
			list = new DocumentTypeList();
			for (PfDocumentTypesDTO documentTypeDTO : documentTypeListDTO) {
				DocumentType documentType = documentTypeDTOToDocumentType(documentTypeDTO);
				list.getDocumentType().add(documentType);
			}
		}

		return list;
	}

	public static ImportanceLevelList pfImportanceLevelListToImportanceLevelList(List<PfImportanceLevelsDTO> listDTO) {
		ImportanceLevelList list = null;
		if (listDTO != null && !listDTO.isEmpty()) {
			list = new ImportanceLevelList();
			for (PfImportanceLevelsDTO levelDTO : listDTO) {
				ImportanceLevel level = new ImportanceLevel();
				level.setLevelCode(levelDTO.getCcodigonivel());
				level.setDescription(levelDTO.getCdescription());
				list.getImportanceLevels().add(level);
			}
		}
		return list;
	}

	public static JobList pfUsersDTOListToJobList(
			List<AbstractBaseDTO> userListDTO) {
		JobList list = null;
		if (userListDTO != null) {
			list = new JobList();
			Iterator<AbstractBaseDTO> it = userListDTO.iterator();
			PfUsersDTO userDTO = null;
			Job job = null;
			while (it.hasNext()) {
				userDTO = (PfUsersDTO) it.next();
				job = new Job();
				job.setIdentifier(userDTO.getCidentifier());
				job.setDescription(userDTO.getDname());

				list.getJob().add(job);
			}
		}

		return list;
	}

	public static StateList pfTagsDTOListToStateList(
			List<AbstractBaseDTO> userListDTO) {
		StateList list = null;
		if (userListDTO != null) {
			list = new StateList();
			Iterator<AbstractBaseDTO> it = userListDTO.iterator();
			PfTagsDTO tagDTO = null;
			State state = null;
			while (it.hasNext()) {
				tagDTO = (PfTagsDTO) it.next();
				state = new State();
				state.setIdentifier(tagDTO.getCtag());

				list.getState().add(state);
			}
		}

		return list;
	}

	public static XMLGregorianCalendar dateToGregorian(Date date) {
		XMLGregorianCalendar result = null;
		DatatypeFactory dataTypeFactory;
		if (date != null) {
			try {
				dataTypeFactory = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
				throw new RuntimeException(e);
			}
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(date);
			result = dataTypeFactory.newXMLGregorianCalendar(gc);
		}

		return result;
	}

	public static DocumentList pfDocumentDTOSetToDocumentList(
			Set<PfDocumentsDTO> documentSetDTO) {
		DocumentList list = null;

		if (documentSetDTO != null) {
			list = new DocumentList();
			Iterator<PfDocumentsDTO> it = documentSetDTO.iterator();
			PfDocumentsDTO documentDTO = null;
			Document document = null;
			while (it.hasNext()) {
				documentDTO = (PfDocumentsDTO) it.next();
				document = documentDTOToDocument(documentDTO);

				list.getDocument().add(document);
			}
		}

		return list;
	}

	// TODO: Depende si se quieren publicar o no, faltan los datos de
	// notificaci&oacute;n de los remitentes
	public static RemitterList pfRemitterDTOSetToRemitterList(
			Set<PfUsersRemitterDTO> remitterSetDTO) {
		RemitterList list = null;

		if (remitterSetDTO != null) {
			list = new RemitterList();
			Iterator<PfUsersRemitterDTO> it = remitterSetDTO.iterator();
			PfUsersRemitterDTO remitterDTO = null;
			User remitter = null;
			while (it.hasNext()) {
				remitterDTO = (PfUsersRemitterDTO) it.next();
				remitter = userDTOToUser(remitterDTO.getPfUser());

				list.getUser().add(remitter);
			}
		}

		return list;
	}

		
	public static SignLineList pfSignLineDTOSetToSignLineList(
			PfRequestsDTO requestDTO, TagBO tagBO) {
		SignLineList list = null;
		
		List<PfSignLinesDTO> signLineListDTO = requestDTO.getPfSignsLinesList();
		
		if (signLineListDTO != null) {
			list = new SignLineList();
			PfSignLinesDTO signLineDTO = null;
			SignLine signLine = null;
			for (int i=0; i<signLineListDTO.size(); i++) {
				signLineDTO = (PfSignLinesDTO) signLineListDTO.get(i);
				signLine = signLineDTOToSignLine(requestDTO, signLineDTO, tagBO);
				list.getSignLine().add(signLine);
			}
			
		}

		return list;
	}
	
	/*public static PfUsersDTO enhancedUserToUserDTO (EnhancedUser enhancedUser) {
		PfUsersDTO userDTO = new PfUsersDTO ();
		userDTO.setCtype("USUARIO");
		userDTO.setCidentifier(enhancedUser.getUser().getIdentifier().trim());
		userDTO.setDname(enhancedUser.getUser().getName().trim());
		userDTO.setDsurname1(enhancedUser.getUser().getSurname1());
		userDTO.setDsurname2(enhancedUser.getUser().getSurname2());
		userDTO.setLvalid(enhancedUser.getEnhancedUserJobInfo().isValid());
		return userDTO;
	}*/

	public static void fillUserDTOWithEnhancedUser (EnhancedUser enhancedUser, PfUsersDTO userDTO) {
		userDTO.setCtype("USUARIO");
		userDTO.setCidentifier(enhancedUser.getUser().getIdentifier().trim());
		userDTO.setDname(enhancedUser.getUser().getName().trim());
		userDTO.setDsurname1(enhancedUser.getUser().getSurname1());
		userDTO.setDsurname2(enhancedUser.getUser().getSurname2());
		userDTO.setLvalid(enhancedUser.getEnhancedUserJobInfo().isValid());
		if (userDTO.getLvalid()==null) userDTO.setLvalid(true);
		userDTO.setLvisible(enhancedUser.getEnhancedUserJobInfo().isVisibleOtherSeats());
		if (userDTO.getLvisible()==null) userDTO.setLvisible(true);
	}
	
	public static void fillUserDTOWithEnhancedJob (EnhancedJob enhancedJob, PfUsersDTO userDTO) {
		userDTO.setCtype("CARGO");
		userDTO.setCidentifier(enhancedJob.getJob().getIdentifier().trim());
		userDTO.setDname(enhancedJob.getJob().getDescription().trim());		
		userDTO.setLvalid(enhancedJob.getEnhancedUserJobInfo().isValid());
		userDTO.setLvisible(enhancedJob.getEnhancedUserJobInfo().isVisibleOtherSeats());
	}
	private static User userDTOToUser(PfUsersDTO userDTO) {
		User user = new User();
		user.setIdentifier(userDTO.getCidentifier());
		user.setName(userDTO.getDname());
		user.setSurname1(userDTO.getDsurname1());
		user.setSurname2(userDTO.getDsurname2());
		return user;
	}

	private static Job userDTOToJob(PfUsersDTO userDTO) {
		Job job = new Job();
		job.setIdentifier(userDTO.getCidentifier());
		job.setDescription(userDTO.getDname());		
		return job;
	}
	
	private static Seat provinceDTOToSeat (PfProvinceDTO province) {
		Seat seat = new Seat ();
		seat.setCode(province.getCcodigoprovincia());
		seat.setDescription(province.getCnombre());
		return seat;
	}
	
	private static EnhancedUserJobInfo userDTOToEnhancedUserJobInfo (PfUsersDTO userDTO) {
		EnhancedUserJobInfo info = new EnhancedUserJobInfo();
		if (userDTO.getPfProvince() != null) {
			info.setSeat(provinceDTOToSeat(userDTO.getPfProvince()));
		}
		info.setValid(userDTO.getLvalid());
		info.setVisibleOtherSeats(userDTO.getLvisible());
		
		Set<PfUsersParameterDTO> paramsDTO = userDTO.getPfUsersParameters();
		if (paramsDTO.size() > 0) {			
			String ldap = getParameterValue (Constants.LOGIN_LDAP_IDATTRIBUTE, paramsDTO.iterator());
			if (ldap != null && !"".contentEquals(ldap)) {
				ParameterList paramList = new ParameterList ();
				Parameter param = new Parameter();
				param.setIdentifier("identificador.ldap");
				param.setValue(ldap);
				paramList.getParameter().add(param);
				info.setParameterList(paramList);
			}
			
		}
		
		return info;
	}
	
	private static String getParameterValue (String idParameter, Iterator<PfUsersParameterDTO> params) {
		boolean found = false;
		String value = null;
		while (!found && params.hasNext()) {
			PfUsersParameterDTO userParamDTO = params.next();
			if (userParamDTO.getPfParameter().getCparameter().contentEquals(idParameter)) {
				value = userParamDTO.getTvalue();
				found = true;
			}
		}
		return value;
	}
	
	public static EnhancedUser userDTOToEnhancedUser(PfUsersDTO userDTO) {
		EnhancedUser enhancedUser = new EnhancedUser();		
		enhancedUser.setUser(userDTOToUser(userDTO));
		enhancedUser.setEnhancedUserJobInfo(userDTOToEnhancedUserJobInfo (userDTO));
		
				
		return enhancedUser;		
	}
	
	public static EnhancedJob userDTOToEnhancedJob (PfUsersDTO userDTO) {
		EnhancedJob enhancedJob = new EnhancedJob();		
		enhancedJob.setJob(userDTOToJob(userDTO));
		enhancedJob.setEnhancedUserJobInfo(userDTOToEnhancedUserJobInfo (userDTO));
		return enhancedJob;		
	}
	
	public static EnhancedUserJobAssociated userJobDTOToEnhancedUserJobAssociated (PfUsersJobDTO usersJobDTO) {
		
		EnhancedUserJobAssociated enhancedUserJobAssociated = new EnhancedUserJobAssociated ();
		enhancedUserJobAssociated.setEnhancedUser(userDTOToEnhancedUser (usersJobDTO.getPfUser()));
		enhancedUserJobAssociated.setEnhancedJob(userDTOToEnhancedJob (usersJobDTO.getPfUserJob()));
		if (usersJobDTO.getFstart() != null) {
			enhancedUserJobAssociated.setFstart(dateToGregorian(usersJobDTO.getFstart()));
		}
		if (usersJobDTO.getFend() != null) {
			enhancedUserJobAssociated.setFend(dateToGregorian(usersJobDTO.getFend()));
		}
		return enhancedUserJobAssociated;
	}
	
	public static EnhancedUserJobAssociatedList userJobDTOToEnhancedUserJobAssociatedList (List<?> usersJobDTOList) {
		EnhancedUserJobAssociatedList enhancedUserJobAssociatedList = new EnhancedUserJobAssociatedList();
		
		for (Object obj : usersJobDTOList) {
			enhancedUserJobAssociatedList.getEnhancedUserJobAssociated().add(userJobDTOToEnhancedUserJobAssociated ( (PfUsersJobDTO) obj));			
		}
		
		return enhancedUserJobAssociatedList;
		
	}
 
	private static SignLine signLineDTOToSignLine(PfRequestsDTO requestDTO,
			PfSignLinesDTO signLineDTO, TagBO tagBO) {
		SignLine signLine = new SignLine();

		signLine.setSignerList(pfSignerSetDTOToSignerList(requestDTO, signLineDTO, tagBO));
		signLine.setType(signLineDTO.getCtype());
		return signLine;
	}

	private static SignerList pfSignerSetDTOToSignerList(PfRequestsDTO requestDTO, PfSignLinesDTO signLineDTO, TagBO tagBO) {
		SignerList list = null;
		Set<PfSignersDTO> signerSetDTO = signLineDTO.getPfSigners();
		if (signerSetDTO != null) {
			list = new SignerList();
			Iterator<PfSignersDTO> it = signerSetDTO.iterator();
			PfSignersDTO signerDTO = null;
			Signer signer = null;
			while (it.hasNext()) {
				signerDTO = (PfSignersDTO) it.next();
				PfRequestTagsDTO reqTag = tagBO.queryStateUserSignLine(requestDTO, signerDTO.getPfUser(), signLineDTO);
				signer = signerDTOToSigner(requestDTO, signerDTO, reqTag);

				list.getSigner().add(signer);
			}
		}

		return list;
	}

	private static Signer signerDTOToSigner(PfRequestsDTO requestDTO,
			PfSignersDTO signerDTO, PfRequestTagsDTO reqTag) {
		Signer signer = signerFromRequestUser(requestDTO, signerDTO, reqTag);

		return signer;
	}

	private static Signer signerFromRequestUser(PfRequestsDTO reqDTO, PfSignersDTO signer, PfRequestTagsDTO reqTag) {
		Signer signerResult = null;
		if (reqDTO.getPfRequestsTags().size() > 0) {
			// the tag is joint to his owner
			if (reqTag.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE) && reqTag.getPfUser().getPrimaryKeyString().equals(signer.getPfUser().getPrimaryKeyString())) {
				signerResult = new Signer();
				if (reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
					// TODO: Sacar esta fecha de la firma y no del cambio de
					// estado.
					signerResult.setFstate(dateToGregorian(reqTag.getFmodified()));
				} else {
					signerResult.setFstate(dateToGregorian(reqTag.getFmodified()));
				}
				State state = new State();
				state.setIdentifier(reqTag.getPfTag().getCtag());
				signerResult.setState(state);

				if (signer.getPfUser().getCtype().equals(
						Constants.C_TYPE_USER_USER)) {
					signerResult.setUserJob(userDTOToUser(signer.getPfUser()));
				} else {
					// TODO: Duda: en el caso de que est&eacute; firmado se deber&iacute;a
					// rellenar con el puesto de trabajo al que se mand&oacute;
					// originalmente o con la persona exacta que lo ha
					// firmado? De momento, puesto de trabajo
					signerResult
							.setUserJob(userDTOToJob(signer.getPfUser()));
				}
			}
		}
		return signerResult;
	}

	private static Document documentDTOToDocument(PfDocumentsDTO docDTO) {
		Document doc = new Document();
		DocumentType documentType = documentTypeDTOToDocumentType(docDTO
				.getPfDocumentType());
		doc.setDocumentType(documentType);
		doc.setMime(docDTO.getDmime());
		doc.setName(docDTO.getDname());
		doc.setSign(docDTO.getLsign());
		doc.setIdentifier(docDTO.getChash());
		return doc;
	}

	private static DocumentType documentTypeDTOToDocumentType(
			PfDocumentTypesDTO documentTypeDTO) {
		DocumentType documentType = new DocumentType();
		documentType.setDescription(documentTypeDTO.getDdocumentType());
		documentType.setIdentifier(documentTypeDTO.getCdocumentType());
		documentType.setValid(documentTypeDTO.getLvalid());
		return documentType;
	}
	
	private static Comment commentDTOToComment(PfCommentsDTO commentDTO) {
		Comment com = new Comment();
		
		if (commentDTO.getPfUser().getCtype().equals(Constants.C_TYPE_USER_USER)) {
			com.setUser((userDTOToUser(commentDTO.getPfUser())));
		} else {
			com.setUser(userDTOToJob(commentDTO.getPfUser()));			
		}
		
		com.setSubject(commentDTO.getDsubject());
		com.setTextComment(commentDTO.getTcomment());
		com.setFmodify(dateToGregorian(commentDTO.getFmodified()));
		return com;
	}
	
	private static CommentList pfCommentSetDTOToCommentList(
			Set<PfCommentsDTO> commentSetDTO) {
		CommentList list = null;

		if (commentSetDTO != null) {
			list = new CommentList();
			Iterator<PfCommentsDTO> it = commentSetDTO.iterator();
			PfCommentsDTO commentDTO = null;
			Comment comment = null;
			while (it.hasNext()) {
				commentDTO = (PfCommentsDTO) it.next();
				comment = commentDTOToComment(commentDTO);
				list.getComment().add(comment);
			}
		}

		return list;
	}

}
