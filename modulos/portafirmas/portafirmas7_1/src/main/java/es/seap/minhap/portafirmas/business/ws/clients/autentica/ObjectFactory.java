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


package es.seap.minhap.portafirmas.business.ws.clients.autentica;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the clientesAutentica package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: clientesAutentica
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUsers }
     * 
     */
    public GetUsers createGetUsers() {
        return new GetUsers();
    }

    /**
     * Create an instance of {@link WebUser }
     * 
     */
    public WebUser createWebUser() {
        return new WebUser();
    }

    /**
     * Create an instance of {@link AdminUser }
     * 
     */
    public AdminUser createAdminUser() {
        return new AdminUser();
    }

    /**
     * Create an instance of {@link LdapUser }
     * 
     */
    public LdapUser createLdapUser() {
        return new LdapUser();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
    }

    /**
     * Create an instance of {@link IsUser }
     * 
     */
    public IsUser createIsUser() {
        return new IsUser();
    }

    /**
     * Create an instance of {@link IsUserResponse }
     * 
     */
    public IsUserResponse createIsUserResponse() {
        return new IsUserResponse();
    }

    /**
     * Create an instance of {@link AddUser }
     * 
     */
    public AddUser createAddUser() {
        return new AddUser();
    }

    /**
     * Create an instance of {@link LdapPositionWS }
     * 
     */
    public LdapPositionWS createLdapPositionWS() {
        return new LdapPositionWS();
    }

    /**
     * Create an instance of {@link AddUserResponse }
     * 
     */
    public AddUserResponse createAddUserResponse() {
        return new AddUserResponse();
    }

    /**
     * Create an instance of {@link ModifyUser }
     * 
     */
    public ModifyUser createModifyUser() {
        return new ModifyUser();
    }

    /**
     * Create an instance of {@link UpdateUser }
     * 
     */
    public UpdateUser createUpdateUser() {
        return new UpdateUser();
    }

    /**
     * Create an instance of {@link ModifyUserResponse }
     * 
     */
    public ModifyUserResponse createModifyUserResponse() {
        return new ModifyUserResponse();
    }

    /**
     * Create an instance of {@link Authentication }
     * 
     */
    public Authentication createAuthentication() {
        return new Authentication();
    }

    /**
     * Create an instance of {@link AuthenticationResponse }
     * 
     */
    public AuthenticationResponse createAuthenticationResponse() {
        return new AuthenticationResponse();
    }

    /**
     * Create an instance of {@link GetAdministrativeSituation }
     * 
     */
    public GetAdministrativeSituation createGetAdministrativeSituation() {
        return new GetAdministrativeSituation();
    }

    /**
     * Create an instance of {@link GetAdministrativeSituationResponse }
     * 
     */
    public GetAdministrativeSituationResponse createGetAdministrativeSituationResponse() {
        return new GetAdministrativeSituationResponse();
    }

    /**
     * Create an instance of {@link GetTypeUser }
     * 
     */
    public GetTypeUser createGetTypeUser() {
        return new GetTypeUser();
    }

    /**
     * Create an instance of {@link GetTypeUserResponse }
     * 
     */
    public GetTypeUserResponse createGetTypeUserResponse() {
        return new GetTypeUserResponse();
    }

    /**
     * Create an instance of {@link GetListUnitOrganization }
     * 
     */
    public GetListUnitOrganization createGetListUnitOrganization() {
        return new GetListUnitOrganization();
    }

    /**
     * Create an instance of {@link GetListUnitOrganizationResponse }
     * 
     */
    public GetListUnitOrganizationResponse createGetListUnitOrganizationResponse() {
        return new GetListUnitOrganizationResponse();
    }

    /**
     * Create an instance of {@link GetListUnitManagementCenter }
     * 
     */
    public GetListUnitManagementCenter createGetListUnitManagementCenter() {
        return new GetListUnitManagementCenter();
    }

    /**
     * Create an instance of {@link GetListUnitManagementCenterResponse }
     * 
     */
    public GetListUnitManagementCenterResponse createGetListUnitManagementCenterResponse() {
        return new GetListUnitManagementCenterResponse();
    }

    /**
     * Create an instance of {@link GetListUnitTargetCenter }
     * 
     */
    public GetListUnitTargetCenter createGetListUnitTargetCenter() {
        return new GetListUnitTargetCenter();
    }

    /**
     * Create an instance of {@link GetListUnitTargetCenterResponse }
     * 
     */
    public GetListUnitTargetCenterResponse createGetListUnitTargetCenterResponse() {
        return new GetListUnitTargetCenterResponse();
    }

    /**
     * Create an instance of {@link GetUnits }
     * 
     */
    public GetUnits createGetUnits() {
        return new GetUnits();
    }

    /**
     * Create an instance of {@link GetUnitsResponse }
     * 
     */
    public GetUnitsResponse createGetUnitsResponse() {
        return new GetUnitsResponse();
    }

    /**
     * Create an instance of {@link GetUserUnits }
     * 
     */
    public GetUserUnits createGetUserUnits() {
        return new GetUserUnits();
    }

    /**
     * Create an instance of {@link GetUserUnitsResponse }
     * 
     */
    public GetUserUnitsResponse createGetUserUnitsResponse() {
        return new GetUserUnitsResponse();
    }

}
