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

package es.seap.minhap.portafirmas.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.context.SecurityContextHolder;

import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Util;

@Entity
@Table(name = "PF_ETIQUETAS", uniqueConstraints = @UniqueConstraint(columnNames = "C_ETIQUETA"))
public class PfTagsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String ctag;
	private String ctype;
	private String ccolor;
	private Set<PfUserTagsDTO> pfUsersTags = new HashSet<PfUserTagsDTO>(0);
	private Set<PfRequestTagsDTO> pfRequestsTags = new HashSet<PfRequestTagsDTO>(0);
	private Set<PfActionsDTO> pfActions = new HashSet<PfActionsDTO>(0);
	private Set<PfNoticeRequestsDTO> pfNoticesRequests = new HashSet<PfNoticeRequestsDTO>(0);
	private Set<PfUsersMessageDTO> pfUsersMessages = new HashSet<PfUsersMessageDTO>(0);

	// auxiliary
	private String ctagTranslated;
	private boolean pass = false;

	public PfTagsDTO() {
		super();
	}

	public PfTagsDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String ctag, String ctype) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.ctag = ctag;
		this.ctype = ctype;
	}

	public PfTagsDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String ctag, String ctype,
			Set<PfUserTagsDTO> pfUsersTags,
			Set<PfRequestTagsDTO> pfRequestsTags, Set<PfActionsDTO> pfActions,
			Set<PfNoticeRequestsDTO> pfNoticesRequests) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.ctag = ctag;
		this.ctype = ctype;
		this.pfUsersTags = pfUsersTags;
		this.pfRequestsTags = pfRequestsTags;
		this.pfActions = pfActions;
		this.pfNoticesRequests = pfNoticesRequests;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_ETIQ")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_ETIQ", sequenceName = "PF_S_ETIQ")
	@Column(name = "X_ETIQUETA", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

//	@Length(max = 30, message = "{errorCTagLength}")
	@Column(name = "C_ETIQUETA", unique = true, nullable = false, length = 30)
	public String getCtag() {
		return this.ctag;
	}

	public void setCtag(String ctag) {
		this.ctag = ctag;
	}

//	@NotNull
//	@Length(max = 10, message = "{errorCTagTypeLength}")
	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfTag")
	public Set<PfUserTagsDTO> getPfUsersTags() {
		return this.pfUsersTags;
	}

	public void setPfUsersTags(Set<PfUserTagsDTO> pfUsersTags) {
		this.pfUsersTags = pfUsersTags;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfTag")
	public Set<PfRequestTagsDTO> getPfRequestsTags() {
		return this.pfRequestsTags;
	}

	public void setPfRequestsTags(Set<PfRequestTagsDTO> pfRequestsTags) {
		this.pfRequestsTags = pfRequestsTags;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfTag")
	public Set<PfActionsDTO> getPfActions() {
		return this.pfActions;
	}

	public void setPfActions(Set<PfActionsDTO> pfActions) {
		this.pfActions = pfActions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfTag")
	public Set<PfNoticeRequestsDTO> getPfNoticeRequests() {
		return this.pfNoticesRequests;
	}

	public void setPfNoticeRequests(Set<PfNoticeRequestsDTO> pfNoticesRequests) {
		this.pfNoticesRequests = pfNoticesRequests;
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
	public String getCtagCapitalizedTruncate() {
		String aux = Util.getInstance().capitalizeString(ctag);

		if (aux == null) {
			aux = "";
		}
		if (this.selected) {
			if (aux.length() > 15) {
				aux = aux.substring(0, 10) + "...";
			}
		} else {
			if (aux.length() > 18) {
				aux = aux.substring(0, 13) + "...";
			}
		}
		return aux;
	}

	@Transient
	public String getCtagCapitalized() {
//		if (ctype.equals(Constants.C_TYPE_TAG_STATE)) {
//			return Util.getInstance().capitalizeString(ctagTranslated);
//		} else {
			return Util.getInstance().capitalizeString(ctag);

//		}
	}

	public void setCtagCapitalizedTruncate(String ctag) {
		this.ctag = ctag;
	}

	public void setCtagCapitalized(String ctag) {
		this.ctag = ctag;
	}

	@Transient
	public String getCtagTranslated() {
		return ctagTranslated;
	}

	public void setCtagTranslated(String ctagTranslated) {
		this.ctagTranslated = ctagTranslated;
	}

	@Transient
	public String getCtagNoSpacesNoPoints() {
		return Util.getInstance().removeSpacesPoints(ctag);
	}

	public void setCcolor(String ccolor) {
		this.ccolor = ccolor;
	}

	@Transient
	public String getCcolor() {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
				.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();


		for (PfUserTagsDTO userTag : this.getPfUsersTags()) {
			ccolor = userTag.getCcolor();
			break;
		}
		return ccolor;
	}

	@Transient
	public String getCcolor(Set<PfRequestTagsDTO> requestTags) {
		// Se recupera el usuario autenticado
		
		PfUsersDTO user = null;
		
		for(PfRequestTagsDTO tagReq: requestTags){
			if(tagReq.getPfTag().getPrimaryKey().equals(this.getPrimaryKey())){
				user = tagReq.getPfUser();
				break;
			}
		}
		
		if(user == null){
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
			user = authorization.getUserDTO();
		}

		for (PfUserTagsDTO userTag : this.getPfUsersTags()) {
			if(userTag.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())){
				ccolor = userTag.getCcolor();
				break;
			}

		}
		return ccolor;
	}
	
	@Transient
	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	@Transient
	public String getCtagToLowerCase() {
		String tag = Util.getInstance().removeSpacesPoints(this.ctag);
		return tag.toLowerCase();
	}

	/**
	 * @return the pfUsersMessages
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfTag")
	public Set<PfUsersMessageDTO> getPfUsersMessages() {
		return pfUsersMessages;
	}

	/**
	 * @param pfUsersMessages the pfUsersMessages to set
	 */
	public void setPfUsersMessages(Set<PfUsersMessageDTO> pfUsersMessages) {
		this.pfUsersMessages = pfUsersMessages;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getPrimaryKey() == ((PfTagsDTO) obj).getPrimaryKey();
	}
	
	
}
