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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the juntadeandalucia.cice.pfirma.type.v2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UserSurname1_QNAME = new QName("", "surname1");
    private final static QName _UserSurname2_QNAME = new QName("", "surname2");
    private final static QName _UserName_QNAME = new QName("", "name");
    private final static QName _CsvJustificanteContent_QNAME = new QName("", "content");
    private final static QName _CsvJustificanteMime_QNAME = new QName("", "mime");
    private final static QName _CsvJustificanteCsv_QNAME = new QName("", "csv");
    private final static QName _RequestRequestStatus_QNAME = new QName("", "requestStatus");
    private final static QName _RequestImportanceLevel_QNAME = new QName("", "importanceLevel");
    private final static QName _RequestNoticeList_QNAME = new QName("", "noticeList");
    private final static QName _RequestText_QNAME = new QName("", "text");
    private final static QName _RequestApplication_QNAME = new QName("", "application");
    private final static QName _RequestDocumentList_QNAME = new QName("", "documentList");
    private final static QName _RequestSubject_QNAME = new QName("", "subject");
    private final static QName _RequestSignType_QNAME = new QName("", "signType");
    private final static QName _RequestActionList_QNAME = new QName("", "actionList");
    private final static QName _RequestTimestampInfo_QNAME = new QName("", "timestampInfo");
    private final static QName _RequestReference_QNAME = new QName("", "reference");
    private final static QName _RequestRemitterList_QNAME = new QName("", "remitterList");
    private final static QName _RequestSignLineList_QNAME = new QName("", "signLineList");
    private final static QName _RequestParameterList_QNAME = new QName("", "parameterList");
    private final static QName _RequestIdentifier_QNAME = new QName("", "identifier");
    private final static QName _RequestCommentList_QNAME = new QName("", "commentList");
    private final static QName _RequestEmailToNotifyList_QNAME = new QName("", "emailToNotifyList");
    private final static QName _SignatureSign_QNAME = new QName("", "sign");
    private final static QName _SignatureSignFormat_QNAME = new QName("", "signFormat");
    private final static QName _EnhancedUserEnhancedUserJobInfo_QNAME = new QName("", "enhancedUserJobInfo");
    private final static QName _EnhancedUserUser_QNAME = new QName("", "user");
    private final static QName _SignLineType_QNAME = new QName("", "type");
    private final static QName _SignLineSignerList_QNAME = new QName("", "signerList");
    private final static QName _SeatDescription_QNAME = new QName("", "description");
    private final static QName _SeatCode_QNAME = new QName("", "code");
    private final static QName _ActionAction_QNAME = new QName("", "action");
    private final static QName _ActionState_QNAME = new QName("", "state");
    private final static QName _DocumentDocumentType_QNAME = new QName("", "documentType");
    private final static QName _DocumentUri_QNAME = new QName("", "uri");
    private final static QName _ImportanceLevelLevelCode_QNAME = new QName("", "levelCode");
    private final static QName _ParameterValue_QNAME = new QName("", "value");
    private final static QName _SignerUserJob_QNAME = new QName("", "userJob");
    private final static QName _EnhancedUserJobAssociatedFend_QNAME = new QName("", "fend");
    private final static QName _EnhancedUserJobInfoValid_QNAME = new QName("", "valid");
    private final static QName _EnhancedUserJobInfoSeat_QNAME = new QName("", "seat");
    private final static QName _EnhancedUserJobInfoVisibleOtherSeats_QNAME = new QName("", "visibleOtherSeats");
    private final static QName _EnhancedJobJob_QNAME = new QName("", "job");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: juntadeandalucia.cice.pfirma.type.v2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Authentication }
     * 
     */
    public Authentication createAuthentication() {
        return new Authentication();
    }

    /**
     * Create an instance of {@link SignerList }
     * 
     */
    public SignerList createSignerList() {
        return new SignerList();
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link Request }
     * 
     */
    public Request createRequest() {
        return new Request();
    }

    /**
     * Create an instance of {@link ExceptionInfo }
     * 
     */
    public ExceptionInfo createExceptionInfo() {
        return new ExceptionInfo();
    }

    /**
     * Create an instance of {@link NoticeList }
     * 
     */
    public NoticeList createNoticeList() {
        return new NoticeList();
    }

    /**
     * Create an instance of {@link ImportanceLevel }
     * 
     */
    public ImportanceLevel createImportanceLevel() {
        return new ImportanceLevel();
    }

    /**
     * Create an instance of {@link DocumentType }
     * 
     */
    public DocumentType createDocumentType() {
        return new DocumentType();
    }

    /**
     * Create an instance of {@link StateList }
     * 
     */
    public StateList createStateList() {
        return new StateList();
    }

    /**
     * Create an instance of {@link State }
     * 
     */
    public State createState() {
        return new State();
    }

    /**
     * Create an instance of {@link EnhancedUserJobAssociatedList }
     * 
     */
    public EnhancedUserJobAssociatedList createEnhancedUserJobAssociatedList() {
        return new EnhancedUserJobAssociatedList();
    }

    /**
     * Create an instance of {@link UserList }
     * 
     */
    public UserList createUserList() {
        return new UserList();
    }

    /**
     * Create an instance of {@link SeatList }
     * 
     */
    public SeatList createSeatList() {
        return new SeatList();
    }

    /**
     * Create an instance of {@link CsvJustificante }
     * 
     */
    public CsvJustificante createCsvJustificante() {
        return new CsvJustificante();
    }

    /**
     * Create an instance of {@link EnhancedUserJobAssociated }
     * 
     */
    public EnhancedUserJobAssociated createEnhancedUserJobAssociated() {
        return new EnhancedUserJobAssociated();
    }

    /**
     * Create an instance of {@link Signer }
     * 
     */
    public Signer createSigner() {
        return new Signer();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link EnhancedJobList }
     * 
     */
    public EnhancedJobList createEnhancedJobList() {
        return new EnhancedJobList();
    }

    /**
     * Create an instance of {@link Seat }
     * 
     */
    public Seat createSeat() {
        return new Seat();
    }

    /**
     * Create an instance of {@link EnhancedJob }
     * 
     */
    public EnhancedJob createEnhancedJob() {
        return new EnhancedJob();
    }

    /**
     * Create an instance of {@link ParameterList }
     * 
     */
    public ParameterList createParameterList() {
        return new ParameterList();
    }

    /**
     * Create an instance of {@link Action }
     * 
     */
    public Action createAction() {
        return new Action();
    }

    /**
     * Create an instance of {@link StringList }
     * 
     */
    public StringList createStringList() {
        return new StringList();
    }

    /**
     * Create an instance of {@link SignLine }
     * 
     */
    public SignLine createSignLine() {
        return new SignLine();
    }

    /**
     * Create an instance of {@link EnhancedUserList }
     * 
     */
    public EnhancedUserList createEnhancedUserList() {
        return new EnhancedUserList();
    }

    /**
     * Create an instance of {@link Signature }
     * 
     */
    public Signature createSignature() {
        return new Signature();
    }

    /**
     * Create an instance of {@link DocumentList }
     * 
     */
    public DocumentList createDocumentList() {
        return new DocumentList();
    }

    /**
     * Create an instance of {@link Job }
     * 
     */
    public Job createJob() {
        return new Job();
    }

    /**
     * Create an instance of {@link ActionList }
     * 
     */
    public ActionList createActionList() {
        return new ActionList();
    }

    /**
     * Create an instance of {@link TimestampInfo }
     * 
     */
    public TimestampInfo createTimestampInfo() {
        return new TimestampInfo();
    }

    /**
     * Create an instance of {@link EnhancedUserJobInfo }
     * 
     */
    public EnhancedUserJobInfo createEnhancedUserJobInfo() {
        return new EnhancedUserJobInfo();
    }

    /**
     * Create an instance of {@link RemitterList }
     * 
     */
    public RemitterList createRemitterList() {
        return new RemitterList();
    }

    /**
     * Create an instance of {@link EnhancedUser }
     * 
     */
    public EnhancedUser createEnhancedUser() {
        return new EnhancedUser();
    }

    /**
     * Create an instance of {@link SignLineList }
     * 
     */
    public SignLineList createSignLineList() {
        return new SignLineList();
    }

    /**
     * Create an instance of {@link DocumentTypeList }
     * 
     */
    public DocumentTypeList createDocumentTypeList() {
        return new DocumentTypeList();
    }

    /**
     * Create an instance of {@link JobList }
     * 
     */
    public JobList createJobList() {
        return new JobList();
    }

    /**
     * Create an instance of {@link Comment }
     * 
     */
    public Comment createComment() {
        return new Comment();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link CommentList }
     * 
     */
    public CommentList createCommentList() {
        return new CommentList();
    }

    /**
     * Create an instance of {@link ImportanceLevelList }
     * 
     */
    public ImportanceLevelList createImportanceLevelList() {
        return new ImportanceLevelList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "surname1", scope = User.class)
    public JAXBElement<String> createUserSurname1(String value) {
        return new JAXBElement<String>(_UserSurname1_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "surname2", scope = User.class)
    public JAXBElement<String> createUserSurname2(String value) {
        return new JAXBElement<String>(_UserSurname2_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name", scope = User.class)
    public JAXBElement<String> createUserName(String value) {
        return new JAXBElement<String>(_UserName_QNAME, String.class, User.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHandler }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "content", scope = CsvJustificante.class)
    @XmlMimeType("application/octet-stream")
    public JAXBElement<DataHandler> createCsvJustificanteContent(DataHandler value) {
        return new JAXBElement<DataHandler>(_CsvJustificanteContent_QNAME, DataHandler.class, CsvJustificante.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mime", scope = CsvJustificante.class)
    public JAXBElement<String> createCsvJustificanteMime(String value) {
        return new JAXBElement<String>(_CsvJustificanteMime_QNAME, String.class, CsvJustificante.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "csv", scope = CsvJustificante.class)
    public JAXBElement<String> createCsvJustificanteCsv(String value) {
        return new JAXBElement<String>(_CsvJustificanteCsv_QNAME, String.class, CsvJustificante.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "requestStatus", scope = Request.class)
    public JAXBElement<RequestStatus> createRequestRequestStatus(RequestStatus value) {
        return new JAXBElement<RequestStatus>(_RequestRequestStatus_QNAME, RequestStatus.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportanceLevel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "importanceLevel", scope = Request.class)
    public JAXBElement<ImportanceLevel> createRequestImportanceLevel(ImportanceLevel value) {
        return new JAXBElement<ImportanceLevel>(_RequestImportanceLevel_QNAME, ImportanceLevel.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoticeList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "noticeList", scope = Request.class)
    public JAXBElement<NoticeList> createRequestNoticeList(NoticeList value) {
        return new JAXBElement<NoticeList>(_RequestNoticeList_QNAME, NoticeList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "text", scope = Request.class)
    public JAXBElement<String> createRequestText(String value) {
        return new JAXBElement<String>(_RequestText_QNAME, String.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "application", scope = Request.class)
    public JAXBElement<String> createRequestApplication(String value) {
        return new JAXBElement<String>(_RequestApplication_QNAME, String.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "documentList", scope = Request.class)
    public JAXBElement<DocumentList> createRequestDocumentList(DocumentList value) {
        return new JAXBElement<DocumentList>(_RequestDocumentList_QNAME, DocumentList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "subject", scope = Request.class)
    public JAXBElement<String> createRequestSubject(String value) {
        return new JAXBElement<String>(_RequestSubject_QNAME, String.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "signType", scope = Request.class)
    public JAXBElement<SignType> createRequestSignType(SignType value) {
        return new JAXBElement<SignType>(_RequestSignType_QNAME, SignType.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActionList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "actionList", scope = Request.class)
    public JAXBElement<ActionList> createRequestActionList(ActionList value) {
        return new JAXBElement<ActionList>(_RequestActionList_QNAME, ActionList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TimestampInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "timestampInfo", scope = Request.class)
    public JAXBElement<TimestampInfo> createRequestTimestampInfo(TimestampInfo value) {
        return new JAXBElement<TimestampInfo>(_RequestTimestampInfo_QNAME, TimestampInfo.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "reference", scope = Request.class)
    public JAXBElement<String> createRequestReference(String value) {
        return new JAXBElement<String>(_RequestReference_QNAME, String.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemitterList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "remitterList", scope = Request.class)
    public JAXBElement<RemitterList> createRequestRemitterList(RemitterList value) {
        return new JAXBElement<RemitterList>(_RequestRemitterList_QNAME, RemitterList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignLineList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "signLineList", scope = Request.class)
    public JAXBElement<SignLineList> createRequestSignLineList(SignLineList value) {
        return new JAXBElement<SignLineList>(_RequestSignLineList_QNAME, SignLineList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParameterList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "parameterList", scope = Request.class)
    public JAXBElement<ParameterList> createRequestParameterList(ParameterList value) {
        return new JAXBElement<ParameterList>(_RequestParameterList_QNAME, ParameterList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = Request.class)
    public JAXBElement<String> createRequestIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CommentList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "commentList", scope = Request.class)
    public JAXBElement<CommentList> createRequestCommentList(CommentList value) {
        return new JAXBElement<CommentList>(_RequestCommentList_QNAME, CommentList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StringList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "emailToNotifyList", scope = Request.class)
    public JAXBElement<StringList> createRequestEmailToNotifyList(StringList value) {
        return new JAXBElement<StringList>(_RequestEmailToNotifyList_QNAME, StringList.class, Request.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = State.class)
    public JAXBElement<String> createStateIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, State.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHandler }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Signature.class)
    @XmlMimeType("application/octet-stream")
    public JAXBElement<DataHandler> createSignatureContent(DataHandler value) {
        return new JAXBElement<DataHandler>(_CsvJustificanteContent_QNAME, DataHandler.class, Signature.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sign", scope = Signature.class)
    public JAXBElement<Boolean> createSignatureSign(Boolean value) {
        return new JAXBElement<Boolean>(_SignatureSign_QNAME, Boolean.class, Signature.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = Signature.class)
    public JAXBElement<String> createSignatureIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, Signature.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignFormat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "signFormat", scope = Signature.class)
    public JAXBElement<SignFormat> createSignatureSignFormat(SignFormat value) {
        return new JAXBElement<SignFormat>(_SignatureSignFormat_QNAME, SignFormat.class, Signature.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = UserJob.class)
    public JAXBElement<String> createUserJobIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, UserJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "enhancedUserJobInfo", scope = EnhancedUser.class)
    public JAXBElement<EnhancedUserJobInfo> createEnhancedUserEnhancedUserJobInfo(EnhancedUserJobInfo value) {
        return new JAXBElement<EnhancedUserJobInfo>(_EnhancedUserEnhancedUserJobInfo_QNAME, EnhancedUserJobInfo.class, EnhancedUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "user", scope = EnhancedUser.class)
    public JAXBElement<User> createEnhancedUserUser(User value) {
        return new JAXBElement<User>(_EnhancedUserUser_QNAME, User.class, EnhancedUser.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignLineType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type", scope = SignLine.class)
    public JAXBElement<SignLineType> createSignLineType(SignLineType value) {
        return new JAXBElement<SignLineType>(_SignLineType_QNAME, SignLineType.class, SignLine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignerList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "signerList", scope = SignLine.class)
    public JAXBElement<SignerList> createSignLineSignerList(SignerList value) {
        return new JAXBElement<SignerList>(_SignLineSignerList_QNAME, SignerList.class, SignLine.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = Seat.class)
    public JAXBElement<String> createSeatDescription(String value) {
        return new JAXBElement<String>(_SeatDescription_QNAME, String.class, Seat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "code", scope = Seat.class)
    public JAXBElement<String> createSeatCode(String value) {
        return new JAXBElement<String>(_SeatCode_QNAME, String.class, Seat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "action", scope = Action.class)
    public JAXBElement<String> createActionAction(String value) {
        return new JAXBElement<String>(_ActionAction_QNAME, String.class, Action.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link State }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "state", scope = Action.class)
    public JAXBElement<State> createActionState(State value) {
        return new JAXBElement<State>(_ActionState_QNAME, State.class, Action.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type", scope = Action.class)
    public JAXBElement<String> createActionType(String value) {
        return new JAXBElement<String>(_SignLineType_QNAME, String.class, Action.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sign", scope = Document.class)
    public JAXBElement<Boolean> createDocumentSign(Boolean value) {
        return new JAXBElement<Boolean>(_SignatureSign_QNAME, Boolean.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataHandler }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "content", scope = Document.class)
    @XmlMimeType("application/octet-stream")
    public JAXBElement<DataHandler> createDocumentContent(DataHandler value) {
        return new JAXBElement<DataHandler>(_CsvJustificanteContent_QNAME, DataHandler.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "documentType", scope = Document.class)
    public JAXBElement<DocumentType> createDocumentDocumentType(DocumentType value) {
        return new JAXBElement<DocumentType>(_DocumentDocumentType_QNAME, DocumentType.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "name", scope = Document.class)
    public JAXBElement<String> createDocumentName(String value) {
        return new JAXBElement<String>(_UserName_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mime", scope = Document.class)
    public JAXBElement<String> createDocumentMime(String value) {
        return new JAXBElement<String>(_CsvJustificanteMime_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "type", scope = Document.class)
    public JAXBElement<String> createDocumentType(String value) {
        return new JAXBElement<String>(_SignLineType_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "uri", scope = Document.class)
    public JAXBElement<String> createDocumentUri(String value) {
        return new JAXBElement<String>(_DocumentUri_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = Document.class)
    public JAXBElement<String> createDocumentIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, Document.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = ImportanceLevel.class)
    public JAXBElement<String> createImportanceLevelDescription(String value) {
        return new JAXBElement<String>(_SeatDescription_QNAME, String.class, ImportanceLevel.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "levelCode", scope = ImportanceLevel.class)
    public JAXBElement<String> createImportanceLevelLevelCode(String value) {
        return new JAXBElement<String>(_ImportanceLevelLevelCode_QNAME, String.class, ImportanceLevel.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "value", scope = Parameter.class)
    public JAXBElement<String> createParameterValue(String value) {
        return new JAXBElement<String>(_ParameterValue_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = Parameter.class)
    public JAXBElement<String> createParameterIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, Parameter.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = Job.class)
    public JAXBElement<String> createJobDescription(String value) {
        return new JAXBElement<String>(_SeatDescription_QNAME, String.class, Job.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link State }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "state", scope = Signer.class)
    public JAXBElement<State> createSignerState(State value) {
        return new JAXBElement<State>(_ActionState_QNAME, State.class, Signer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserJob }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "userJob", scope = Signer.class)
    public JAXBElement<UserJob> createSignerUserJob(UserJob value) {
        return new JAXBElement<UserJob>(_SignerUserJob_QNAME, UserJob.class, Signer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fend", scope = EnhancedUserJobAssociated.class)
    public JAXBElement<XMLGregorianCalendar> createEnhancedUserJobAssociatedFend(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EnhancedUserJobAssociatedFend_QNAME, XMLGregorianCalendar.class, EnhancedUserJobAssociated.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valid", scope = EnhancedUserJobInfo.class)
    public JAXBElement<Boolean> createEnhancedUserJobInfoValid(Boolean value) {
        return new JAXBElement<Boolean>(_EnhancedUserJobInfoValid_QNAME, Boolean.class, EnhancedUserJobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Seat }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "seat", scope = EnhancedUserJobInfo.class)
    public JAXBElement<Seat> createEnhancedUserJobInfoSeat(Seat value) {
        return new JAXBElement<Seat>(_EnhancedUserJobInfoSeat_QNAME, Seat.class, EnhancedUserJobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParameterList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "parameterList", scope = EnhancedUserJobInfo.class)
    public JAXBElement<ParameterList> createEnhancedUserJobInfoParameterList(ParameterList value) {
        return new JAXBElement<ParameterList>(_RequestParameterList_QNAME, ParameterList.class, EnhancedUserJobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "visibleOtherSeats", scope = EnhancedUserJobInfo.class)
    public JAXBElement<Boolean> createEnhancedUserJobInfoVisibleOtherSeats(Boolean value) {
        return new JAXBElement<Boolean>(_EnhancedUserJobInfoVisibleOtherSeats_QNAME, Boolean.class, EnhancedUserJobInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "enhancedUserJobInfo", scope = EnhancedJob.class)
    public JAXBElement<EnhancedUserJobInfo> createEnhancedJobEnhancedUserJobInfo(EnhancedUserJobInfo value) {
        return new JAXBElement<EnhancedUserJobInfo>(_EnhancedUserEnhancedUserJobInfo_QNAME, EnhancedUserJobInfo.class, EnhancedJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Job }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "job", scope = EnhancedJob.class)
    public JAXBElement<Job> createEnhancedJobJob(Job value) {
        return new JAXBElement<Job>(_EnhancedJobJob_QNAME, Job.class, EnhancedJob.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "valid", scope = DocumentType.class)
    public JAXBElement<Boolean> createDocumentTypeValid(Boolean value) {
        return new JAXBElement<Boolean>(_EnhancedUserJobInfoValid_QNAME, Boolean.class, DocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description", scope = DocumentType.class)
    public JAXBElement<String> createDocumentTypeDescription(String value) {
        return new JAXBElement<String>(_SeatDescription_QNAME, String.class, DocumentType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "identifier", scope = DocumentType.class)
    public JAXBElement<String> createDocumentTypeIdentifier(String value) {
        return new JAXBElement<String>(_RequestIdentifier_QNAME, String.class, DocumentType.class, value);
    }

}
