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

 Autor: Junta de AndalucÃ­a

 Derechos de explotaciÃ³n propiedad de la Junta de AndalucÃ­a.

 Ã‰ste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los tÃ©rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la ComisiÃ³n Europea, en su versiÃ³n 1.0. o posteriores.

 Ã‰ste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÃ�A, incluso sin las presuntas garantÃ­as implÃ­citas de USABILIDAD o ADECUACIÃ“N A PROPÃ“SITO 
 CONCRETO. Para mas informaciÃ³n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por algÃºn motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reÃ§u une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

@Entity
@Table(name = "PF_USUARIOS")
public class PfUsersDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cidentifier;
	private String canagram;
	private String dname;
	private String dsurname1;
	private String dsurname2;
	private Boolean lvalid;
	private String ctype;
	private PfProvinceDTO pfProvince;
	private Boolean lvisible;
	private Boolean lshownotifwarning;
	private Boolean lNotifyPush;
	private Portafirmas portafirmas;
	@Transient
	private Boolean validator = Boolean.FALSE;
	@Transient
	private Boolean passer = Boolean.FALSE;

	private Set<PfSignersDTO> pfSigners = new HashSet<PfSignersDTO>(0);
	private Set<PfUsersAuthorizationDTO> pfUsersAuthorizations = new HashSet<PfUsersAuthorizationDTO>(0);
	private Set<PfUsersEmailDTO> pfUsersEmails = new HashSet<PfUsersEmailDTO>(0);
	private Set<PfUsersJobDTO> pfUsersJobs = new HashSet<PfUsersJobDTO>(0);
	private Set<PfApplicationUsersDTO> pfUsersApplications = new HashSet<PfApplicationUsersDTO>(0);
	private Set<PfUsersParameterDTO> pfUsersParameters = new HashSet<PfUsersParameterDTO>(0);
	private Set<PfFiltersDTO> pfFilters = new HashSet<PfFiltersDTO>(0);
	private Set<PfUsersAuthorizationDTO> pfUsers = new HashSet<PfUsersAuthorizationDTO>(0);
	private Set<PfUserTagsDTO> pfTagsUsers = new HashSet<PfUserTagsDTO>(0);
	private Set<PfUsersProfileDTO> pfUsersProfiles = new HashSet<PfUsersProfileDTO>(0);
	private Set<PfProvinceAdminDTO> pfUsersProvinces = new HashSet<PfProvinceAdminDTO>(0);
	private Set<PfMobileUsersDTO> pfUsersMobiles = new HashSet<PfMobileUsersDTO>(0);
	private Set<PfCommentsDTO> pfComments = new HashSet<PfCommentsDTO>(0);
	private Set<PfOthersUserDTO> pfOthersUsers = new HashSet<PfOthersUserDTO>(0);
	private Set<PfUsersRemitterDTO> pfUsersRemitters = new HashSet<PfUsersRemitterDTO>(0);
	private Set<PfHistoricRequestsDTO> pfHistoricRequests = new HashSet<PfHistoricRequestsDTO>(0);
	private Set<PfSessionAttributesDTO> pfSessionAttributes = new HashSet<PfSessionAttributesDTO>(0);
	private Set<PfUsersGroupsDTO> pfUsersGroups = new HashSet<PfUsersGroupsDTO>(0);
	private Set<PfRequestTemplatesDTO> pfRequestTemplates = new HashSet<PfRequestTemplatesDTO>(0);
	private Set<PfUsersCommentDTO> pfUsersComments = new HashSet<PfUsersCommentDTO>(0);
	private Set<PfUsersMessageDTO> pfUsersMessages = new HashSet<PfUsersMessageDTO>(0);
	private Set<PfValidatorApplicationDTO> pfValidatorApplications = new HashSet<PfValidatorApplicationDTO>(0);
	private Set<PfValidatorApplicationDTO> pfValidatorApplicationsUser = new HashSet<PfValidatorApplicationDTO>(0);
	
	private Set<PfUsersDTO> validadores = new HashSet<PfUsersDTO>(0);
	private Set<PfUsersDTO> validadorDe = new HashSet<PfUsersDTO>(0);
	
	private Set<PfUsersDTO> gestores = new HashSet<PfUsersDTO>(0);
	private Set<PfUsersDTO> gestorDe = new HashSet<PfUsersDTO>(0);

    private List<PfUnidadOrganizacionalDTO> pfUnidadOrganizacionalList;

    private Boolean mostrarFirmanteAnterior = Boolean.FALSE;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
//    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name = "PF_ADMINISTRADOR_ORGANISMO", joinColumns = {
            @JoinColumn(name = "ID_USUARIO", referencedColumnName = "X_USUARIO")}, inverseJoinColumns = {
            @JoinColumn(name = "ID_ORGANISMO", referencedColumnName = "ID")})        
	public List<PfUnidadOrganizacionalDTO> getPfUnidadOrganizacionalList() {
		return pfUnidadOrganizacionalList;
	}

	public void setPfUnidadOrganizacionalList(List<PfUnidadOrganizacionalDTO> pfUnidadOrganizacionalList) {
		this.pfUnidadOrganizacionalList = pfUnidadOrganizacionalList;
	}

	public PfUsersDTO() {
		super();
	}

	public PfUsersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cidentifier, String canagram,
			Boolean lvalid, String ctype, PfProvinceDTO pfProvince, 
			Boolean lvisible, Boolean lshownotifwarning, Boolean lNotifyPush) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cidentifier = cidentifier;
		this.canagram = canagram;
		this.lvalid = lvalid;
		this.ctype = ctype;
		this.pfProvince = pfProvince;
		this.lvisible = lvisible;
		this.lshownotifwarning = lshownotifwarning;
		this.lNotifyPush = lNotifyPush;
	}

	public PfUsersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cidentifier, String canagram, String dname,
			String dsurname1, String dsurname2, Boolean lvalid, String ctype,
			PfProvinceDTO pfProvince,
			Boolean lvisible, Boolean lshownotifwarning,
			Set<PfSignersDTO> pfSigners,
			Set<PfUsersAuthorizationDTO> pfUsersAuthorizations,
			Set<PfUsersEmailDTO> pfUsersEmails, Set<PfUsersJobDTO> pfUsersJobs,
			Set<PfApplicationUsersDTO> pfUsersApplications,
			Set<PfUsersParameterDTO> pfUsersParameters,
			Set<PfFiltersDTO> pfFilters, Set<PfUsersAuthorizationDTO> pfUsers,
			Set<PfUserTagsDTO> pfTagsUsers,
			Set<PfUsersProfileDTO> pfUsersProfiles,
			Set<PfProvinceAdminDTO> pfUsersProvinces,
			Set<PfMobileUsersDTO> pfUsersMobiles,
			Set<PfCommentsDTO> pfComments, Set<PfOthersUserDTO> pfOthersUsers,
			Set<PfUsersRemitterDTO> pfUsersRemitters,
			Set<PfHistoricRequestsDTO> pfHistoricRequests,
			Set<PfSessionAttributesDTO> pfSessionAttributes,
			Set<PfUsersGroupsDTO> pfUsersGroups,
			Set<PfRequestTemplatesDTO> pfRequestTemplates, Boolean lNotifyPush) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cidentifier = cidentifier;
		this.canagram = canagram;
		this.dname = dname;
		this.dsurname1 = dsurname1;
		this.dsurname2 = dsurname2;
		this.lvalid = lvalid;
		this.ctype = ctype;
		this.pfProvince = pfProvince;
		this.lvisible = lvisible;
		this.lshownotifwarning = lshownotifwarning;
		this.pfSigners = pfSigners;
		this.pfUsersAuthorizations = pfUsersAuthorizations;
		this.pfUsersEmails = pfUsersEmails;
		this.pfUsersJobs = pfUsersJobs;
		this.pfUsersApplications = pfUsersApplications;
		this.pfUsersParameters = pfUsersParameters;
		this.pfFilters = pfFilters;
		this.pfUsers = pfUsers;
		this.pfTagsUsers = pfTagsUsers;
		this.pfUsersProfiles = pfUsersProfiles;
		this.pfUsersProvinces = pfUsersProvinces;
		this.pfUsersMobiles = pfUsersMobiles;
		this.pfComments = pfComments;
		this.pfOthersUsers = pfOthersUsers;
		this.pfUsersRemitters = pfUsersRemitters;
		this.pfHistoricRequests = pfHistoricRequests;
		this.pfSessionAttributes = pfSessionAttributes;
		this.pfUsersGroups = pfUsersGroups;
		this.pfRequestTemplates = pfRequestTemplates;
		this.lNotifyPush = lNotifyPush;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_USU")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_USU", sequenceName = "PF_S_USU")
	@Column(name = "X_USUARIO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

//	@Length(max = 30, message = "{errorCIdentifierLength}")
	@Column(name = "C_IDENTIFICADOR", nullable = false, length = 30)
	public String getCidentifier() {
		return this.cidentifier;
	}

	public void setCidentifier(String cidentifier) {
		this.cidentifier = cidentifier;
	}

//	@Length(max = 50, message = "{errorDAnagramLength}")
	@Column(name = "C_ANAGRAMA", length = 50)
	public String getCanagram() {
		return this.canagram;
	}

	public void setCanagram(String canagram) {
		this.canagram = canagram;
	}

//	@Length(max = 50, message = "{errorDUserNameLength}")
	@Column(name = "D_NOMBRE", length = 50)
	public String getDname() {
		return this.dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

//	@Length(max = 50, message = "{errorDUserSurnameLength}")
	@Column(name = "D_APELL1", length = 50)
	public String getDsurname1() {
		return this.dsurname1;
	}

	public void setDsurname1(String dsurname1) {
		this.dsurname1 = dsurname1;
	}

//	@Length(max = 50, message = "{errorDUserSurnameLength}")
	@Column(name = "D_APELL2", length = 50)
	public String getDsurname2() {
		return this.dsurname2;
	}

	public void setDsurname2(String dsurname2) {
		this.dsurname2 = dsurname2;
	}

	@Column(name = "L_VIGENTE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLvalid() {
		return this.lvalid;
	}

	public void setLvalid(Boolean lvalid) {
		this.lvalid = lvalid;
	}

	@Column(name = "L_ALERTA_NOTIF", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLshownotifwarning() {
		return lshownotifwarning;
	}

	public void setLshownotifwarning(Boolean lshownotifwarning) {
		this.lshownotifwarning = lshownotifwarning;
	}
	
	@Column(name = "L_NOTIFY_PUSH",  length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLNotifyPush() {
		return lNotifyPush;
	}

	public void setLNotifyPush(Boolean lNotifyPush) {
		this.lNotifyPush = lNotifyPush;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PORTAFIRMAS", nullable = false)
	public Portafirmas getPortafirmas() {
		return portafirmas;
	}
	
	public void setPortafirmas(Portafirmas portafirmas) {
		this.portafirmas = portafirmas;
	}

	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROV_X_PROVINCIA", nullable = true)
	public PfProvinceDTO getPfProvince() {
		return pfProvince;
	}

	public void setPfProvince(PfProvinceDTO pfProvince) {
		this.pfProvince = pfProvince;
	}
	
	@Column(name = "L_VISIBLE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLvisible() {
		return this.lvisible;
	}

	public void setLvisible(Boolean lvisible) {
		this.lvisible = lvisible;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfSignersDTO> getPfSigners() {
		return this.pfSigners;
	}

	public void setPfSigners(Set<PfSignersDTO> pfSigners) {
		this.pfSigners = pfSigners;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfAuthorizedUser")
	public Set<PfUsersAuthorizationDTO> getPfUsersAuthorizations() {
		return this.pfUsersAuthorizations;
	}

	public void setPfUsersAuthorizations(
			Set<PfUsersAuthorizationDTO> pfUsersAuthorizations) {
		this.pfUsersAuthorizations = pfUsersAuthorizations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersEmailDTO> getPfUsersEmails() {
		return this.pfUsersEmails;
	}

	public void setPfUsersEmails(Set<PfUsersEmailDTO> pfUsersEmails) {
		this.pfUsersEmails = pfUsersEmails;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersJobDTO> getPfUsersJobs() {
		return this.pfUsersJobs;
	}

	public void setPfUsersJobs(Set<PfUsersJobDTO> pfUsersJobs) {
		this.pfUsersJobs = pfUsersJobs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfApplicationUsersDTO> getPfUsersApplications() {
		return this.pfUsersApplications;
	}

	public void setPfUsersApplications(
			Set<PfApplicationUsersDTO> pfUsersApplications) {
		this.pfUsersApplications = pfUsersApplications;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersParameterDTO> getPfUsersParameters() {
		return this.pfUsersParameters;
	}

	public void setPfUsersParameters(Set<PfUsersParameterDTO> pfUsersParameters) {
		this.pfUsersParameters = pfUsersParameters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfFiltersDTO> getPfFilters() {
		return this.pfFilters;
	}

	public void setPfFilters(Set<PfFiltersDTO> pfFilters) {
		this.pfFilters = pfFilters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersAuthorizationDTO> getPfUsers() {
		return this.pfUsers;
	}

	public void setPfUsers(Set<PfUsersAuthorizationDTO> pfUsers) {
		this.pfUsers = pfUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUserTagsDTO> getPfTagsUsers() {
		return this.pfTagsUsers;
	}

	public void setPfTagsUsers(Set<PfUserTagsDTO> pfTagsUsers) {
		this.pfTagsUsers = pfTagsUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersProfileDTO> getPfUsersProfiles() {
		return this.pfUsersProfiles;
	}

	public void setPfUsersProfiles(Set<PfUsersProfileDTO> pfUsersProfiles) {
		this.pfUsersProfiles = pfUsersProfiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfProvinceAdminDTO> getPfUsersProvinces() {
		return this.pfUsersProvinces;
	}

	public void setPfUsersProvinces(Set<PfProvinceAdminDTO> pfUsersProvinces) {
		this.pfUsersProvinces = pfUsersProvinces;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfMobileUsersDTO> getPfUsersMobiles() {
		return this.pfUsersMobiles;
	}

	public void setPfUsersMobiles(Set<PfMobileUsersDTO> pfUsersMobiles) {
		this.pfUsersMobiles = pfUsersMobiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfCommentsDTO> getPfComments() {
		return this.pfComments;
	}

	public void setPfComments(Set<PfCommentsDTO> pfComments) {
		this.pfComments = pfComments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfOthersUserDTO> getPfOthersUsers() {
		return this.pfOthersUsers;
	}

	public void setPfOthersUsers(Set<PfOthersUserDTO> pfOthersUsers) {
		this.pfOthersUsers = pfOthersUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersRemitterDTO> getPfUsersRemitters() {
		return this.pfUsersRemitters;
	}

	public void setPfUsersRemitters(Set<PfUsersRemitterDTO> pfUsersRemitters) {
		this.pfUsersRemitters = pfUsersRemitters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfHistoricRequestsDTO> getPfHistoricRequests() {
		return this.pfHistoricRequests;
	}

	public void setPfHistoricRequests(
			Set<PfHistoricRequestsDTO> pfHistoricRequests) {
		this.pfHistoricRequests = pfHistoricRequests;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersGroupsDTO> getPfUsersGroups() {
		return pfUsersGroups;
	}

	public void setPfUsersGroups(
			Set<PfUsersGroupsDTO> pfUsersGroups) {
		this.pfUsersGroups = pfUsersGroups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfRequestTemplatesDTO> getPfRequestTemplates() {
		return pfRequestTemplates;
	}

	public void setPfRequestTemplates(Set<PfRequestTemplatesDTO> pfRequestTemplates) {
		this.pfRequestTemplates = pfRequestTemplates;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfSessionAttributesDTO> getPfSessionAttributes() {
		return pfSessionAttributes;
	}

	public void setPfSessionAttributes(
			Set<PfSessionAttributesDTO> pfSessionAttributes) {
		this.pfSessionAttributes = pfSessionAttributes;
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
	public List<PfUserTagsDTO> getPfTagsUsersList() {

		List<PfUserTagsDTO> list = new ArrayList<PfUserTagsDTO>();
		for (Iterator<PfUserTagsDTO> iterator = pfTagsUsers.iterator(); iterator
				.hasNext();) {
			PfUserTagsDTO aux = iterator.next();
			list.add(aux);
		}
		return list;
	}

	/**
	 * Recupera el nombre completo del usuario
	 * @return el nombre completo del usuario
	 * @see es.seap.minhap.portafirmas.utils.Util#completeUserName(PfUsersDTO)
	 */
	@Transient
	public String getFullName() {
		return Util.getInstance().completeUserName(this);
	}

	/**
	 * Recupera el nombre completo del usuario con la provincia
	 * @return el nombre completo del usuario con la provincia
	 * @see es.seap.minhap.portafirmas.utils.Util#completeUserNameWithprovince(PfUsersDTO)
	 */
	@Transient
	public String getFullNameWithProvince() {
		return Util.getInstance().completeUserNameWithProvince(this);
	}
	

	/**
	 * Especifica si un usuario es de tipo job o no
	 * @return si es de tipo job o no
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_TYPE_USER_JOB
	 */
	@Transient
	public boolean isJob() {
		return Constants.C_TYPE_USER_JOB.equals(this.getCtype());
	}
	
	/**
	 * Especifica si un usuario es de tipo aplicacion o no
	 * @return si es de tipo aplicacion o no
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_TYPE_USER_APLI
	 */
	@Transient
	public boolean isAplicacion() {
		return Constants.C_TYPE_USER_APLI.equals(this.getCtype());
	}
	
	/**
	 * Especifica si un usuario es de tipo usuario o no
	 * @return si es de tipo usuario o no
	 * @see es.seap.minhap.portafirmas.utils.Constants#C_TYPE_USER_USER
	 */
	@Transient
	public boolean isUsuario() {
		return Constants.C_TYPE_USER_USER.equals(this.getCtype());
	}


	@Transient
	public PfUsersDTO getValidJob() {
		return Util.getInstance().getUserValidJob(this);
	}
	
	@Transient
	public PfUsersDTO getUserOfJob() {
		return Util.getInstance().getUserOfJob(this);
	}


	@Transient
	public List<String> getUserTagsAsList() {
		List<String> result = new ArrayList<String>();
		for (Iterator<PfUserTagsDTO> iterator = this.pfTagsUsers.iterator(); iterator
				.hasNext();) {
			PfUserTagsDTO aux = (PfUserTagsDTO) iterator.next();
			result.add(aux.getPfTag().getCtag());
		}
		return result;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	    name="PF_USUARIOS_VALIDADORES",
	    joinColumns=@JoinColumn(name="USU_X_USUARIO", nullable=false),
	    inverseJoinColumns=@JoinColumn(name="USU_X_USUARIO_VALIDADOR", nullable=false)
	)    
	public Set<PfUsersDTO> getValidadores() {
		return validadores;
	}

	public void setValidadores(Set<PfUsersDTO> validadores) {
		this.validadores = validadores;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	    name="PF_USUARIOS_VALIDADORES",
	    joinColumns=@JoinColumn(name="USU_X_USUARIO_VALIDADOR", nullable=false),
	    inverseJoinColumns=@JoinColumn(name="USU_X_USUARIO", nullable=false)
	)    
	public Set<PfUsersDTO> getValidadorDe() {
		return validadorDe;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersCommentDTO> getPfUsersComments() {
		return this.pfUsersComments;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	    name="PF_USUARIOS_GESTORES",
	    joinColumns=@JoinColumn(name="USU_X_USUARIO", nullable=false),
	    inverseJoinColumns=@JoinColumn(name="USU_X_USUARIO_GESTOR", nullable=false)
	)    
	public Set<PfUsersDTO> getGestores() {
		return gestores;
	}

	public void setGestores(Set<PfUsersDTO> gestores) {
		this.gestores = gestores;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
	    name="PF_USUARIOS_GESTORES",
	    joinColumns=@JoinColumn(name="USU_X_USUARIO_GESTOR", nullable=false),
	    inverseJoinColumns=@JoinColumn(name="USU_X_USUARIO", nullable=false)
	)    
	public Set<PfUsersDTO> getGestorDe() {
		return gestorDe;
	}
	
	public void setGestorDe(Set<PfUsersDTO> gestorDe) {
		this.gestorDe = gestorDe;
	}
	
	
	public void setPfUsersComments(Set<PfUsersCommentDTO> pfUsersComments) {
		this.pfUsersComments = pfUsersComments;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser")
	public Set<PfUsersMessageDTO> getPfUsersMessages() {
		return this.pfUsersMessages;
	}

	public void setPfUsersMessages(Set<PfUsersMessageDTO> pfUsersMessages) {
		this.pfUsersMessages = pfUsersMessages;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfUser", cascade = CascadeType.ALL)
	public Set<PfValidatorApplicationDTO> getPfValidatorApplications() {
		return this.pfValidatorApplications;
	}

	public void setPfValidatorApplications(Set<PfValidatorApplicationDTO> pfValidatorApplications) {
		this.pfValidatorApplications = pfValidatorApplications;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfValidatorUser", cascade = CascadeType.ALL)
	public Set<PfValidatorApplicationDTO> getPfValidatorApplicationsUser() {
		return this.pfValidatorApplicationsUser;
	}

	public void setPfValidatorApplicationsUser(Set<PfValidatorApplicationDTO> pfValidatorApplicationsUser) {
		this.pfValidatorApplicationsUser = pfValidatorApplicationsUser;
	}

	public void setValidadorDe(Set<PfUsersDTO> validadorDe) {
		this.validadorDe = validadorDe;
	}
	@Transient
	public Boolean getValidator() {
		return validator;
	}
	@Transient
	public void setValidator(Boolean validator) {
		this.validator = validator;
	}
	@Transient
	public Boolean getPasser() {
		return passer;
	}
	@Transient
	public void setPasser(Boolean passer) {
		this.passer = passer;
	}
	
	@Transient
	public boolean isAdministrator() {
		boolean returnValue = false;
		List<String> perfilesAdmin = Util.profilesAdmin();
		for(PfUsersProfileDTO it: this.pfUsersProfiles){
			if(perfilesAdmin.contains(it.getPfProfile().getCprofile())){
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}
	
	@Transient
	public String getLdapUser(){
		for(PfUsersParameterDTO param : pfUsersParameters){
			if( Constants.USUARIO_LDAP_IDATRIBUTO.equalsIgnoreCase(param.getPfParameter().getCparameter()) ){
				return param.getTvalue();
			}
		}
		return "";
	}

	@Transient
	public Set<PfProvinceDTO> getOrganismos(){
		Set<PfProvinceDTO> returnValue = new HashSet<PfProvinceDTO>();
		for(PfProvinceAdminDTO it: pfUsersProvinces){
			if(it.getPfProvince().getOrganismo()==null){
				returnValue.add(it.getPfProvince());
			}
		}
		return returnValue;
	}
	
	@Transient
	public Set<Long> getIdsOrganismos(){
		Set<Long> returnValue = new HashSet<Long>();
		for(PfProvinceAdminDTO it: pfUsersProvinces){
			if(it.getPfProvince().getOrganismo()==null){
				returnValue.add(it.getPfProvince().getPrimaryKey());
			}
		}
		return returnValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PfUsersDTO other = (PfUsersDTO) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
	
	@Column(name = "L_MOSTRAR_FIRMANTE_ANTERIOR", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getMostrarFirmanteAnterior() {
		return mostrarFirmanteAnterior;
	}

	public void setMostrarFirmanteAnterior(Boolean mostrarFirmanteAnterior) {
		this.mostrarFirmanteAnterior = mostrarFirmanteAnterior;
	}

	
}
