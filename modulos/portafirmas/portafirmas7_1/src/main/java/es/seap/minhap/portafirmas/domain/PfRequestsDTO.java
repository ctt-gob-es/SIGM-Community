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

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de Andaluc&iacute;a

 Derechos de explotaci&oacute;n propiedad de la Junta de Andaluc&iacute;a.

 Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los t&eacute;rminos de la Licencia EUPL European Public License publicada
 por el organismo IDABC de la Comisi&oacute;n Europea, en su versi&oacute;n 1.0. o posteriores.

 Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas garant&iacute;as impl&iacute;citas de USABILIDAD o ADECUACIÓN A PROPÓSITO
 CONCRETO. Para mas informaci&oacute;n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por alg&uacute;n motivo no le es posible visualizarla, puede
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;
import org.springframework.security.core.context.SecurityContextHolder;

import es.seap.minhap.portafirmas.domain.enums.EstadosParaRemitente;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.ListComparador;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Signer;

@Entity
@Table(name = "PF_PETICIONES", uniqueConstraints = @UniqueConstraint(columnNames = "C_HASH"))
public class PfRequestsDTO extends AbstractBaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfApplicationsDTO pfApplication;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String chash;
	private Date fentry;
	private Date fstart;
	private Date fexpiration;
	private String dreference;
	private String dsubject;
	private Boolean lcascadeSign;
	private Boolean lfirstSignerSign;
	private Boolean lOnlyNotifyActionsToRemitter;
	private boolean signModeCorrect = true;
	private PfImportanceLevelsDTO pfImportance;
	private String ltimestamp;
	private Boolean lSignMarked;
	private Date fechaIntentoFirma;
	private PfUsersDTO usuarioBloqueo;
	
	//Invitaciones
	private Boolean laccepted;
	private Boolean linvited;
	private PfInvitedUsersDTO invitedUser;

	private Set<PfRequestsValueDTO> pfRequestsValues = new HashSet<PfRequestsValueDTO>(0);
	private Set<PfDocumentsDTO> pfDocuments = new HashSet<PfDocumentsDTO>(0);

	private Set<PfSignLinesDTO> pfSignsLines = new HashSet<PfSignLinesDTO>(0);

	private Set<PfCommentsDTO> pfComments = new HashSet<PfCommentsDTO>(0);

	private Set<PfUsersRemitterDTO> pfUsersRemitters = new HashSet<PfUsersRemitterDTO>(0);
	private Set<PfNoticeRequestsDTO> pfNoticesRequests = new HashSet<PfNoticeRequestsDTO>(0);

	private Set<PfActionsDTO> pfActions = new HashSet<PfActionsDTO>(0);

	private Set<PfRequestTagsDTO> pfRequestsTags = new HashSet<PfRequestTagsDTO>(0);
	
	private Set<PfHistoricRequestsDTO> pfHistoricRequests = new HashSet<PfHistoricRequestsDTO>(0);
	
	private Set<PfEmailsRequestDTO> pfEmailsRequest = new HashSet<PfEmailsRequestDTO>(0);
	
	// Indica si la petición ha tenido una acción previa de otro usuario (firmada o dado el visto bueno)
	private boolean accionesPrevia = false;

	// Indica si las firmas de la petición han sido validadas o no
	private boolean validada = false;
	
	private PfUsersDTO ultimoFirmante;
	
	private String remitenteInterfazGenerica;
	
	private Long importeFacturaContrato;
	
	public PfRequestsDTO() {
		super();
	}

	public PfRequestsDTO(PfApplicationsDTO pfApplication, String ccreated,
			Date fcreated, String cmodified, Date fmodified, String chash,
			Date fentry, Boolean lcascadeSign, Boolean lfirstSignerSign,
			PfImportanceLevelsDTO pfImportance, String ltimestamp, 
			Boolean lAccepted, Boolean lInvited, PfInvitedUsersDTO invitedUser, 
			Boolean lSignMarked) {
		super();
		this.pfApplication = pfApplication;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.fentry = fentry;
		this.lcascadeSign = lcascadeSign;
		this.lfirstSignerSign = lfirstSignerSign;
		this.pfImportance = pfImportance;
		this.ltimestamp = ltimestamp;
		this.laccepted= lAccepted;
		this.linvited = lInvited;
		this.invitedUser = invitedUser;
		this.lSignMarked = lSignMarked;
	}

	public PfRequestsDTO(PfApplicationsDTO pfApplication, String ccreated,
			Date fcreated, String cmodified, Date fmodified, String chash,
			Date fentry, Date fstart, Date fexpiration, String dreference,
			String dsubject, Boolean lcascadeSign, Boolean lfirstSignerSign,
			PfImportanceLevelsDTO pfImportance, String ltimestamp,
			Set<PfRequestsValueDTO> pfRequestsValues,
			Set<PfDocumentsDTO> pfDocuments, Set<PfSignLinesDTO> pfSignsLines,
			Set<PfCommentsDTO> pfComments, Set<PfUsersRemitterDTO> pfUsersRemitters,
			Set<PfNoticeRequestsDTO> pfNoticesRequests,
			Set<PfActionsDTO> pfActions, Set<PfRequestTagsDTO> pfRequestsTags,
			Set<PfHistoricRequestsDTO> pfHistoricRequests, Boolean lAccepted, 
			Boolean lInvited, PfInvitedUsersDTO invitedUser, Boolean lSignMarked) {
		super();
		this.pfApplication = pfApplication;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.fentry = fentry;
		this.fstart = fstart;
		this.fexpiration = fexpiration;
		this.dreference = dreference;
		this.dsubject = dsubject;
		this.lcascadeSign = lcascadeSign;
		this.lfirstSignerSign = lfirstSignerSign;
		this.pfImportance = pfImportance;
		this.ltimestamp = ltimestamp;
		this.pfRequestsValues = pfRequestsValues;
		this.pfDocuments = pfDocuments;
		this.pfSignsLines = pfSignsLines;
		this.pfComments = pfComments;
		this.pfUsersRemitters = pfUsersRemitters;
		this.pfNoticesRequests = pfNoticesRequests;
		this.pfActions = pfActions;
		this.pfRequestsTags = pfRequestsTags;
		this.pfHistoricRequests = pfHistoricRequests;
		this.laccepted = lAccepted;
		this.linvited = lInvited;
		this.invitedUser = invitedUser;
		this.lSignMarked = lSignMarked;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_PET_GEN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_PET_GEN", sequenceName = "PF_S_PET")
	@Column(name = "X_PETICION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APL_X_APLICACION", nullable = false)
	public PfApplicationsDTO getPfApplication() {
		return this.pfApplication;
	}

	public void setPfApplication(PfApplicationsDTO pfApplication) {
		this.pfApplication = pfApplication;
	}

	@Column(name = "C_HASH", unique = true, nullable = false, length = 10)
	public String getChash() {
		return this.chash;
	}

	public void setChash(String chash) {
		this.chash = chash;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_ENTRADA", nullable = false, length = 7)
	public Date getFentry() {
		return this.fentry;
	}

	public void setFentry(Date fentry) {
		this.fentry = fentry;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_INICIO", length = 7)
	public Date getFstart() {
		return this.fstart;
	}

	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_CADUCIDAD", length = 7)
	public Date getFexpiration() {
		return this.fexpiration;
	}

	public void setFexpiration(Date fexpiration) {
		this.fexpiration = fexpiration;
	}

	@Column(name = "D_REFERENCIA", length = 30)
	public String getDreference() {
		return this.dreference;
	}

	public void setDreference(String dreference) {
		this.dreference = dreference;
	}

//	@Length(max = 255, message = "{errorSubjectLength}")
	@Column(name = "D_ASUNTO", length = 255)
	public String getDsubject() {		
		
		return deleteHtmlCharacters(this.dsubject);
	}
	
	private static String deleteHtmlCharacters(final String text) {
		
		String result = "";
		result = text;
//		result = result.replaceAll("<b>", "");
//		result = result.replaceAll("</b>", "");
//		result = result.replaceAll("<br/>", "\n");
		
		return result; 
	}

	public void setDsubject(String dsubject) {
		this.dsubject = dsubject;
	}

	@Column(name = "L_FIRMA_EN_CASCADA", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLcascadeSign() {
		return this.lcascadeSign;
	}

	public void setLcascadeSign(Boolean lcascadeSign) {
		this.lcascadeSign = lcascadeSign;
	}

	@Column(name = "L_SOLO_NOTIF_ACC_REMITE", nullable = true, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLOnlyNotifyActionsToRemitter() {
		return this.lOnlyNotifyActionsToRemitter;
	}

	public void setLOnlyNotifyActionsToRemitter(Boolean lOnlyNotifyActionsToRemitter) {
		this.lOnlyNotifyActionsToRemitter = lOnlyNotifyActionsToRemitter;
	}
	
	@Column(name = "L_FIRMA_PRIMER_FIRMANTE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLfirstSignerSign() {
		return this.lfirstSignerSign;
	}

	public void setLfirstSignerSign(Boolean lfirstSignerSign) {
		this.lfirstSignerSign = lfirstSignerSign;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NV_IMP_X_IMPORTANCIA", nullable = false)
	public PfImportanceLevelsDTO getPfImportance() {
		return pfImportance;
	}

	public void setPfImportance(PfImportanceLevelsDTO pfImportance) {
		this.pfImportance = pfImportance;
	}

	@Column(name = "L_SELLO_TIEMPO", nullable = false, length = 1)
	public String getLtimestamp() {
		return this.ltimestamp;
	}

	public void setLtimestamp(String ltimestamp) {
		this.ltimestamp = ltimestamp;
	}

	/*
	 * @Column(name = "T_PETICION") @Type(type = "hibernate.StringClobType")
	 * public String getTrequest() { return this.trequest; }
	 * 
	 * public void setTrequest(String trequest) { this.trequest = trequest; }
	 */

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfRequestsValueDTO> getPfRequestsValues() {
		return this.pfRequestsValues;
	}

	public void setPfRequestsValues(Set<PfRequestsValueDTO> pfRequestsValues) {
		this.pfRequestsValues = pfRequestsValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfDocumentsDTO> getPfDocuments() {
		return this.pfDocuments;
	}

	public void setPfDocuments(Set<PfDocumentsDTO> pfDocuments) {
		this.pfDocuments = pfDocuments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfSignLinesDTO> getPfSignsLines() {
		return this.pfSignsLines;
	}

	@Transient
	public List<PfSignLinesDTO> getPfSignsLinesList() {
		List<PfSignLinesDTO> list = new ArrayList<PfSignLinesDTO>();
		list.addAll(pfSignsLines);
		ListComparador c = new ListComparador("signLines", 1);
		Collections.sort(list, c);
		return list;
	}

	public void setPfSignsLines(Set<PfSignLinesDTO> pfSignsLines) {
		this.pfSignsLines = pfSignsLines;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfCommentsDTO> getPfComments() {
		return this.pfComments;
	}

	public void setPfComments(Set<PfCommentsDTO> pfComments) {
		this.pfComments = pfComments;
	}

	@BatchSize(size=50)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfUsersRemitterDTO> getPfUsersRemitters() {
		return this.pfUsersRemitters;
	}

	public void setPfUsersRemitters(Set<PfUsersRemitterDTO> pfUsersRemitters) {
		this.pfUsersRemitters = pfUsersRemitters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfNoticeRequestsDTO> getPfNoticesRequests() {
		return this.pfNoticesRequests;
	}

	public void setPfNoticesRequests(Set<PfNoticeRequestsDTO> pfNoticesRequests) {
		this.pfNoticesRequests = pfNoticesRequests;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfActionsDTO> getPfActions() {
		return this.pfActions;
	}

	public void setPfActions(Set<PfActionsDTO> pfActions) {
		this.pfActions = pfActions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfRequestTagsDTO> getPfRequestsTags() {
		return this.pfRequestsTags;
	}

	public void setPfRequestsTags(Set<PfRequestTagsDTO> pfRequestsTags) {
		this.pfRequestsTags = pfRequestsTags;
	}

	@BatchSize(size=50)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfHistoricRequestsDTO> getPfHistoricRequests() {
		return this.pfHistoricRequests;
	}

	public void setPfHistoricRequests(
			Set<PfHistoricRequestsDTO> pfHistoricRequests) {
		this.pfHistoricRequests = pfHistoricRequests;
	}
	
	//@BatchSize(size=50)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest")
	public Set<PfEmailsRequestDTO> getPfEmailsRequest() {
		return this.pfEmailsRequest;
	}

	public void setPfEmailsRequest(
			Set<PfEmailsRequestDTO> pfEmailsRequest) {
		this.pfEmailsRequest = pfEmailsRequest;
	}

	@Override
	@Column(name = "C_CREADO", nullable = false, length = 20)
	public String getCcreated() {
		return this.ccreated;
	}

	@Override
	@Column(name = "C_MODIFICADO", nullable = false, length = 20)
	public String getCmodified() {
		return this.cmodified;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_CREADO", nullable = false, length = 7)
	public Date getFcreated() {
		return this.fcreated;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_MODIFICADO", nullable = false, length = 7)
	public Date getFmodified() {
		return this.fmodified;
	}

	@Override
	public void setCcreated(String ccreated) {
		this.ccreated = ccreated;
	}

	@Override
	public void setCmodified(String cmodified) {
		this.cmodified = cmodified;
	}

	@Override
	public void setFcreated(Date fcreated) {
		this.fcreated = fcreated;
	}

	@Override
	public void setFmodified(Date fmodified) {
		this.fmodified = fmodified;
	}

	@Override
	@PrePersist
	public void createAuditing() {
		super.createAuditing();
	}

	@Override
	@PreUpdate
	public void updateAuditing() {
		super.updateAuditing();
	}

	@Transient
	public String getRemitters() {
		// TODO: Hacer todo en una funci&oacute;n de utils
		String remitters = "";
		if (this.getPfUsersRemitters() != null
				&& this.getPfUsersRemitters().size() > 0) {
			Iterator<PfUsersRemitterDTO> itRemitters = this
					.getPfUsersRemitters().iterator();
			remitters = Util.getInstance().completeUserName(
					itRemitters.next().getPfUser());
			if (itRemitters.hasNext()) {
				remitters += " ...";
			}
			// remitters v1
		} else if (this.getPfHistoricRequests() != null
				&& this.getPfHistoricRequests().size() > 0) {
			Iterator<PfHistoricRequestsDTO> iterator = this.pfHistoricRequests
					.iterator();
			PfHistoricRequestsDTO histo = null;
			while (iterator.hasNext()) {
				histo = iterator.next();
				if (histo.getChistoricRequest().equals(
						Constants.C_HISTORICREQUEST_REMITTERNAME)) {
					remitters = histo.getThistoricRequest();
				}
			}
		} else {
			remitters = Constants.D_HISTORICREQUEST_UNKNOW;
		}
		return remitters;
	}

	@Transient
	public String getRemittersAll() {
		String remitters = "";
		int counter = 0;
		if (this.getPfUsersRemitters() != null
				&& this.getPfUsersRemitters().size() > 0) {
			Iterator<PfUsersRemitterDTO> itRemitters = this
					.getPfUsersRemitters().iterator();
			while (itRemitters.hasNext()) {
				if (counter > 0) {
					remitters += ", ";
				}
				remitters += Util.getInstance().completeUserName(
						itRemitters.next().getPfUser());
				counter++;
			}
			// remitters v1
		} else if (this.getPfHistoricRequests() != null
				&& this.getPfHistoricRequests().size() > 0) {
			Iterator<PfHistoricRequestsDTO> iterator = this.pfHistoricRequests
					.iterator();
			PfHistoricRequestsDTO histo = null;
			while (iterator.hasNext()) {
				histo = iterator.next();
				if (histo.getChistoricRequest().equals(
						Constants.C_HISTORICREQUEST_REMITTERNAME)) {
					/*
					 * if (counter > 0) { remitters += " ,"; }
					 */

					remitters = histo.getThistoricRequest();
					// counter++;
				}
			}
		} else {
			remitters = Constants.D_HISTORICREQUEST_UNKNOW;
		}

		return remitters;
	}

	@Transient
	public String getSigners() {
		/*
		 * Iterator<PfSignLinesDTO> itSignLines = this.getPfSignsLines()
		 * .iterator(); Iterator<PfSignersDTO> itSigners =
		 * itSignLines.next().getPfSigners() .iterator(); String signers =
		 * Util.getInstance().completeUserName( itSigners.next().getPfUser());
		 * if (itSigners.hasNext() || itSignLines.hasNext()) { signers += "
		 * ..."; }
		 */
		String signers = "";
		Iterator<PfRequestTagsDTO> itReqTags = this.pfRequestsTags.iterator();
		PfRequestTagsDTO reqTag = null;
		int count = 0;
		while (itReqTags.hasNext() && count < 2) {
			reqTag = itReqTags.next();
			if (reqTag.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)
					&& count == 0) {
				signers = Util.getInstance().completeUserName(
						reqTag.getPfUser());
				count++;
			} else if (reqTag.getPfTag().getCtype().equals(
					Constants.C_TYPE_TAG_STATE)
					&& count == 1) {
				signers += " ...";
				count++;
			}
		}
		return signers;
	}

	@Transient
	public List<PfDocumentsDTO> getPfDocumentsList() {
		List<PfDocumentsDTO> list = new ArrayList<PfDocumentsDTO>();
		Iterator<PfDocumentsDTO> iterator = this.pfDocuments.iterator();
		PfDocumentsDTO doc = null;
		while (iterator.hasNext()) {
			doc = iterator.next();
			if (doc.getLsign()) {
				list.add(doc);
			}
		}
		return list;
	}

	@Transient
	public List<PfDocumentsDTO> getPfAttachedDocumentsList(Long idUsuario) {
		List<PfDocumentsDTO> list = new ArrayList<PfDocumentsDTO>();
		Iterator<PfDocumentsDTO> iterator = this.pfDocuments.iterator();
		PfDocumentsDTO doc = null;
		while (iterator.hasNext()) {
			doc = iterator.next();
			if (!doc.getLsign()) {
				if(doc.getUsuXUsuarioAnexa()==null){
					list.add(doc);
				}
				else{
					List<Long> usuariosPermitidos=new ArrayList<Long>();
					for(PfUsersDTO u: doc.getPfUsuariosList()){
						usuariosPermitidos.add(u.getPrimaryKey());
					}
					if(usuariosPermitidos.contains(idUsuario)){
						list.add(doc);
					}
				}
			}
		}
		//ordenamos
		Collections.sort(list);
		
		return list;
	}
	
	@Transient
	public List<PfDocumentsDTO> getPfAttachedDocumentsList() {
		List<PfDocumentsDTO> list = new ArrayList<PfDocumentsDTO>();
		Iterator<PfDocumentsDTO> iterator = this.pfDocuments.iterator();
		PfDocumentsDTO doc = null;
		while (iterator.hasNext()) {
			doc = iterator.next();
			if (!doc.getLsign()) {
				list.add(doc);
			}
		}
		return list;
	}

	@Transient
	public List<PfCommentsDTO> getPfCommentsList() {
		return Util.getInstance().getPfCommentsList(this.pfComments);
	}

	@Transient
	public List<PfHistoricRequestsDTO> getPfHistoricRequestsList() {
		List<PfHistoricRequestsDTO> list = new ArrayList<PfHistoricRequestsDTO>();
		Iterator<PfHistoricRequestsDTO> iterator = this.pfHistoricRequests
				.iterator();
		while (iterator.hasNext()) {
			PfHistoricRequestsDTO histo = iterator.next();
			if (!histo.getChistoricRequest().contains(
					Constants.C_HISTORICREQUEST_REMITTER)) {
				list.add(histo);
			}
		}
		return list;
	}

	public void setPfDocumentsList(List<AbstractBaseDTO> pfDocumentsList) {
	}

	public void setPfAttachedDocumentsList(
			List<AbstractBaseDTO> pfAttachedDocumentsList) {
	}

	public void setPfCommentsList(List<AbstractBaseDTO> list) {
	}

	public void setPfHistoricRequestsList(List<AbstractBaseDTO> list) {
	}

	@Transient
	public void setRemittersAll(String remittersAll) {

	}

	@Transient
	public List<PfRequestTagsDTO> getPfUserTagsList() {

		List<PfRequestTagsDTO> list = new ArrayList<PfRequestTagsDTO>();

		for (Iterator<PfRequestTagsDTO> iterator = this.pfRequestsTags
				.iterator(); iterator.hasNext();) {
			PfRequestTagsDTO aux = iterator.next();
			// TODO Coger sólo la última
			if (Constants.C_TYPE_TAG_USER.equals(aux.getPfTag().getCtype())) {
				list.add(aux);
			}
		}

		ListComparador c = new ListComparador("requestTag", 1);
		Collections.sort(list, c);

		return list;
	}

	@Transient
	public boolean isForwarded() {
		boolean forwarded = false;

		for (Iterator<PfRequestTagsDTO> iterator = this.pfRequestsTags
				.iterator(); iterator.hasNext();) {
			PfRequestTagsDTO aux = iterator.next();
			if (Constants.C_TAG_SYSTEM_FORWARED.equals(aux.getPfTag().getCtag())) {
				forwarded = true;
			}
		}

		return forwarded;
	}

	@Transient
	public boolean isExpired() {
		boolean expired = false;
		if (this.fexpiration != null && this.fexpiration.before(new Date())) {
			expired = true;
		}
		return expired;
	}

	@Transient
	public boolean isSignModeCorrect() {
		return signModeCorrect;
	}

	public void setSignModeCorrect(boolean signModeCorrect) {
		this.signModeCorrect = signModeCorrect;
	}

	@Transient
	public boolean isAccionesPrevia() {
		return accionesPrevia;
	}

	public void setAccionesPrevia(boolean accionesPrevia) {
		this.accionesPrevia = accionesPrevia;
	}

	@Transient
	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	/**
	 * Indica que alguien ha firmado la peticion y nadie la ha rechazado
	 * @return
	 */
	@Transient
	public boolean isAnySignNotRejected() {
		boolean anySigned = false;
		boolean anyRejected = false;
		for (Iterator<PfRequestTagsDTO> it = pfRequestsTags.iterator(); it.hasNext();) {
			PfRequestTagsDTO pfRequestTag = (PfRequestTagsDTO) it.next();
			if(pfRequestTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
				anySigned = true;
			} else if (pfRequestTag.getPfTag().getCtag().equals(Constants.C_TAG_REJECTED)) {
				anyRejected = true;
			}
		}
		return (anySigned && !anyRejected);
	}
	
	/**
	 * Indica que alguien ha firmado la peticion
	 * @return
	 */
	@Transient
	public boolean isAnySign() {
		boolean anySigned = false;
		for (Iterator<PfRequestTagsDTO> it = pfRequestsTags.iterator(); it.hasNext();) {
			PfRequestTagsDTO pfRequestTag = (PfRequestTagsDTO) it.next();
			if(pfRequestTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
				anySigned = true;
			}
		}
		return anySigned;
	}
	
	@Transient
	public boolean isAnyAttachment(){
		List<PfDocumentsDTO> attachmentList = this.getPfAttachedDocumentsList();
		return !attachmentList.isEmpty();
	}
	
	
	/**
	 * Indica si el informe se ha descargado o no
	 * @return
	 */
	@Transient
	public boolean isDownloaded() {
		boolean todosDescargados = true;
		for (Iterator<PfDocumentsDTO> it = pfDocuments.iterator(); it.hasNext();) {
			PfDocumentsDTO pfDocument = (PfDocumentsDTO) it.next();
			boolean tieneInformeElDocumento = !pfDocument.getLsign();
			if(!tieneInformeElDocumento){
				for (Iterator<PfSignsDTO> iter = pfDocument.getPfSigns().iterator(); iter.hasNext();) {
					PfSignsDTO pfSign = (PfSignsDTO) iter.next();
					if(pfSign.getTieneDocumentos().getTieneInforme()==1){
						tieneInformeElDocumento = true;
						break;
					}
				}
			}
			if(!tieneInformeElDocumento){
				todosDescargados = false;
			}
			if(!todosDescargados){
				break;
			}	
		}
		return todosDescargados;
	}
	
	
	/**
	 * Indica si el remitente se encuentra entre los firmantes
	 * @return
	 */
	@Transient
	public boolean isRemitterInSigners(){
		boolean resultado = false;
		PfUsersRemitterDTO user = new PfUsersRemitterDTO();
		for(PfUsersRemitterDTO remitente : this.pfUsersRemitters){
			user = remitente;
		}
		if(!this.getLinvited() || (this.getLinvited() && this.getLaccepted())){
			for(PfSignLinesDTO lineaFirma : this.pfSignsLines){
				for(PfSignersDTO firmante : lineaFirma.getPfSigners()){
					if(firmante.getPfUser().getPrimaryKey() != null && firmante.getPfUser().getPrimaryKey().equals(user.getPfUser().getPrimaryKey())){
						resultado = true;
					}
				}	
			}
		}
		return resultado;
	}
	
	
	/**
	 * Indica si el remitente es el siguiente a firmar
	 * @return
	 */
	@Transient
	public boolean isRemitterNextToSign(){
		boolean resultado = false;
		PfUsersRemitterDTO user = new PfUsersRemitterDTO();
		for(PfUsersRemitterDTO remitente : this.pfUsersRemitters){
			user = remitente;
		}
		
		List<PfSignLinesDTO> lineasDeFirma = getPfSignsLinesList();
		
		for (PfSignLinesDTO signLineDTO : lineasDeFirma) {
			// Se recuperan los firmantes de la línea de firma
			boolean isTerminate = false;
			for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
				Signer signer = new Signer();
				signer.setPfUser(signerDTO.getPfUser());
				PfRequestTagsDTO stateRequestTag = null;
				for(PfRequestTagsDTO rt: this.pfRequestsTags){
					if(Constants.C_TYPE_TAG_STATE.equalsIgnoreCase(rt.getPfTag().getCtype())){
						stateRequestTag = rt;
					}
				}
				String status = stateRequestTag.getPfTag().getCtag();
				isTerminate = status.equals(Constants.C_TAG_SIGNED) || status.equals(Constants.C_TAG_PASSED);
				signer.setStateTag(stateRequestTag.getPfTag());
				if(!isTerminate){
					break;
				}
			}
			
			if(!this.getLinvited() || (this.getLinvited() && this.getLaccepted())){
				if(!isTerminate){
					for(PfSignersDTO signerIt: signLineDTO.getPfSigners()){
						if(signerIt.getPfUser().getPrimaryKey().equals(user.getPfUser().getPrimaryKey())){
							resultado = true;
							break;
						}
					}
					break;
				}
			}
		}
		return resultado;
	}
	
	@Transient
	public List<Long> getListRemmittersIds(){
		List<Long> returnValue = new ArrayList<Long>();
		for(PfUsersRemitterDTO purd: pfUsersRemitters){
			returnValue.add(purd.getPfUser().getPrimaryKey());
		}
		return returnValue;
	}
	
	@Transient
	public Boolean unreadForRemmitter(Long idUserRem){
		boolean retorno = false;
		for(PfUsersRemitterDTO purd: pfUsersRemitters){
			if(purd.getPfUser().getPrimaryKey().equals(idUserRem) && 
					(purd.getEstadoParaRemitente()==null || purd.getEstadoParaRemitente().equalsIgnoreCase(EstadosParaRemitente.PENDIENTE_DE_LECTURA))){
				retorno = true;
				break;
			}
		}
		return retorno;
	}
	
	@Column(name = "L_ACCEPTED", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLaccepted() {
		return this.laccepted;
	}

	public void setLaccepted(Boolean lAccepted) {
		this.laccepted = lAccepted;
	}
	
	@Column(name = "L_INVITED", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLinvited() {
		return this.linvited;
	}

	public void setLinvited(Boolean lInvited) {
		this.linvited = lInvited;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USU_INV")
	public PfInvitedUsersDTO getInvitedUser() {
		return invitedUser;
	}

	public void setInvitedUser(PfInvitedUsersDTO invitedUser) {
		this.invitedUser = invitedUser;
	}

	@Column(name = "L_MARCA_FIRMA", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getlSignMarked() {
		return lSignMarked;
	}

	public void setlSignMarked(Boolean lSignMarked) {
		this.lSignMarked = lSignMarked;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INTENTO_FIRMA", nullable = true, length = 11)
	public Date getFechaIntentoFirma() {
		return fechaIntentoFirma;
	}

	public void setFechaIntentoFirma(Date fechaIntentoFirma) {
		this.fechaIntentoFirma = fechaIntentoFirma;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_BLOQUEO")
	public PfUsersDTO getUsuarioBloqueo() {
		return this.usuarioBloqueo;
	}

	public void setUsuarioBloqueo(PfUsersDTO usuarioBloqueo) {
		this.usuarioBloqueo = usuarioBloqueo;
	}
	
	@Transient
	public PfUsersDTO getUltimoFirmante() {
		return ultimoFirmante;
	}

	public void setUltimoFirmante(PfUsersDTO ultimoFirmante) {
		this.ultimoFirmante = ultimoFirmante;
	}
	
	@Transient
	public String getRemitenteAndUltiFimanteSiProcede() {
		// Se recupera el usuario autenticado
		// Agustin #1406 cambios en la vista de las bandejas, remitente
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		boolean pintarUltimoFirmanteActivo = user.getMostrarFirmanteAnterior();
		if (this.getLcascadeSign() && pintarUltimoFirmanteActivo && this.getUltimoFirmante()!= null) {
			return "["+user.getPfProvince().getCnombre()+"]<br/>Tramitador: "+this.getRemitters() + "<br/>Último firmante: " + this.getUltimoFirmante().getFullName();
		} else if (this.getRemitenteInterfazGenerica()!=null && !"".equals(this.getRemitenteInterfazGenerica())){
			return "["+user.getPfProvince().getCnombre()+"]<br/>Tramitador: "+this.getRemitters() + "<br/>Último firmante: " + this.getRemitenteInterfazGenerica();
		} else {
			return "["+user.getPfProvince().getCnombre()+"]<br/>Tramitador: "+this.getRemitters();
		}
	}

	@Column(name = "IMPORTE_FACTURA_CONTRATO", length = 18)
	public Long getImporteFacturaContrato() {
		return importeFacturaContrato;
	}

	public void setImporteFacturaContrato(Long importeFacturaContrato) {
		this.importeFacturaContrato = importeFacturaContrato;
	}
	
	public String obtenerIconoImporteFacturaContrato() {
		String icono = "";
		String icono_gold = "mf-icon mf-icon-importe-gold ";
		String icono_silver = "mf-icon mf-icon-importe-silver ";
		String icono_bronze = "mf-icon mf-icon-importe-bronze ";
		Long importe = getImporteFacturaContrato();
		if (importe != null){
			 if (importe <= new Long(300000)){
				 return icono_bronze;
			 } else if (importe > new Long(300000) && importe <= new Long(1000000)){
				 return icono_silver;
			 } else if (importe > new Long(1000000)) {
				 return icono_gold;
			 }
		}
		return icono;
	}
	
	@Transient
	public String getRemitenteInterfazGenerica() {
		return remitenteInterfazGenerica;
	}

	public void setRemitenteInterfazGenerica(String remitenteInterfazGenerica) {
		this.remitenteInterfazGenerica = remitenteInterfazGenerica;
	}

}
