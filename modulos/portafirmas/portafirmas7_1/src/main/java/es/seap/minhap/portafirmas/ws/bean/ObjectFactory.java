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

package es.seap.minhap.portafirmas.ws.bean;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * Provides methods to create new instances of schema derived classes for
 * package: juntadeandalucia.cice.pfirma.type.v2
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: juntadeandalucia.cice.pfirma.type.v2
	 * 
	 */
	public ObjectFactory() {
	}

    /**
     * Create an instance of {@link DownloadSign }
     * 
     */
    public Authentication createAuthentication() {
        return new Authentication();
    }

    /**
     * Create an instance of {@link QueryJobs }
     * 
     */
    public Signature createSignature() {
        return new Signature();
    }
	
	/**
	 * Create an instance of {@link Document }
	 * 
	 * @return possible object is {@link Document }
	 * 
	 */
	public Document createDocument() {
		return new Document();
	}

	/**
	 * Create an instance of {@link UserList }
	 * 
	 * @return possible object is {@link UserList }
	 */
	public UserList createUserList() {
		return new UserList();
	}

	/**
	 * Create an instance of {@link State }
	 * 
	 * @return possible object is {@link State }
	 */
	public State createState() {
		return new State();
	}

	/**
	 * Create an instance of {@link Signer }
	 * 
	 * @return possible object is {@link Signer }
	 */
	public Signer createSigner() {
		return new Signer();
	}

	/**
	 * Create an instance of {@link ExceptionInfo }
	 * 
	 * @return possible object is {@link ExceptionInfo }
	 */
	public ExceptionInfo createExceptionInfo() {
		return new ExceptionInfo();
	}

	/**
	 * Create an instance of {@link SignLine }
	 * 
	 * @return possible object is {@link SignLine }
	 */
	public SignLine createSignLine() {
		return new SignLine();
	}

	/**
	 * Create an instance of {@link Parameter }
	 * 
	 * @return possible object is {@link Parameter }
	 */
	public Parameter createParameter() {
		return new Parameter();
	}

	/**
	 * Create an instance of {@link User }
	 * 
	 * @return possible object is {@link User }
	 */
	public User createUser() {
		return new User();
	}

	/**
	 * Create an instance of {@link DocumentType }
	 * 
	 * @return possible object is {@link DocumentType }
	 */
	public DocumentType createDocumentType() {
		return new DocumentType();
	}

	/**
	 * Create an instance of {@link ImportanceLevel }
	 * 
	 * @return possible object is {@link ImportanceLevel }
	 */
	public ImportanceLevel createImportanceLevel() {
		return new ImportanceLevel();
	}

	/**
	 * Create an instance of {@link ParameterList }
	 * 
	 * @return possible object is {@link ParameterList }
	 */
	public ParameterList createParameterList() {
		return new ParameterList();
	}

	/**
	 * Create an instance of {@link SignerList }
	 * 
	 * @return possible object is {@link SignerList }
	 */
	public SignerList createSignerList() {
		return new SignerList();
	}

	/**
	 * Create an instance of {@link RemitterList }
	 * 
	 * @return possible object is {@link RemitterList }
	 */
	public RemitterList createRemitterList() {
		return new RemitterList();
	}

	/**
	 * Create an instance of {@link SignLineList }
	 * 
	 * @return possible object is {@link SignLineList }
	 */
	public SignLineList createSignLineList() {
		return new SignLineList();
	}

	/**
	 * Create an instance of {@link Request }
	 * 
	 * @return possible object is {@link Request }
	 */
	public Request createRequest() {
		return new Request();
	}

	/**
	 * Create an instance of {@link Job }
	 * 
	 * @return possible object is {@link Job }
	 */
	public Job createJob() {
		return new Job();
	}

	/**
	 * Create an instance of {@link DocumentList }
	 * 
	 * @return possible object is {@link DocumentList }
	 */
	public DocumentList createDocumentList() {
		return new DocumentList();
	}

	/**
	 * Create an instance of {@link ImportanceLevelList }
	 * 
	 * @return possible object is {@link ImportanceLevelList }
	 */
	public ImportanceLevelList createImportanceLevelList() {
		return new ImportanceLevelList();
	}

	/**
	 * Create an instance of {@link Comment }
	 * 
	 * @return possible object is {@link Comment }
	 */
	public Comment createComment() {
		return new Comment();
	}

	/**
	 * Create an instance of {@link CommentList }
	 * 
	 * @return possible object is {@link CommentList }
	 */
	public CommentList createCommentList() {
		return new CommentList();
	}
	
	/**
	 * Create an instance of {@link Action }
	 * 
	 * @return possible object is {@link Action }
	 */
	public Action createAction() {
		return new Action();
	}

	/**
	 * Create an instance of {@link ActionList }
	 * 
	 * @return possible object is {@link ActionList }
	 */
	public ActionList createActionList() {
		return new ActionList();
	}

	/**
	 * Create an instance of {@link NoticeList }
	 * 
	 * @return possible object is {@link NoticeList }
	 */
	public NoticeList createNoticeList() {
		return new NoticeList();
	}

}
