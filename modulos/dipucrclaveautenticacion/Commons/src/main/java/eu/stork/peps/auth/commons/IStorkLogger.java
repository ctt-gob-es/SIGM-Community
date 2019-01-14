/*
 * This work is Open Source and licensed by the European Commission under the
 * conditions of the European Public License v1.1 
 *  
 * (http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1); 
 * 
 * any use of this file implies acceptance of the conditions of this license. 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package eu.stork.peps.auth.commons;

/**
 * Interface for stork logging.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.10 $, $Date: 2011-02-17 22:44:34 $
 */
public interface IStorkLogger {

    /**
     * Getter for SpApplication.
     *
     * @return The SpApplication value.
     */
    String getSpApplication();

    /**
     * Setter for SpApplication.
     *
     * @param spApplication The SP Application.
     */
    void setSpApplication(String spApplication);

    /**
     * Getter for ProviderName.
     *
     * @return The ProviderName value.
     */
    String getProviderName();

    /**
     * Setter for ProviderName.
     *
     * @param providerName The provider name.
     */
    void setProviderName(String providerName);

    /**
     *
     * Getter for Origin.
     *
     * @return The Origin value.
     *
     */
    String getOrigin();

    /**
     * Setter for Origin.
     *
     * @param origin The origin.
     */
    void setOrigin(String origin);

    /**
     *
     * Getter for QAA Level.
     *
     * @return The QAA Level value.
     *
     */
    int getQaaLevel();

    /**
     * Setter for QAA Level.
     *
     * @param qaaLevel The qaa level.
     */
    void setQaaLevel(int qaaLevel);

    /**
     *
     * Getter for timestamp.
     *
     * @return The timestamp value.
     *
     */
    String getTimestamp();

    /**
     * Setter for timestamp.
     *
     * @param timestamp The request's timestamp.
     */
    void setTimestamp(String timestamp);

    /**
     * Getter for InResponseTo.
     *
     * @return The InResponseTo value.
     */
    String getInResponseTo();

    /**
     * Setter for InResponseTo.
     *
     * @param inResponseTo The Saml's response id.
     */
    void setInResponseTo(String inResponseTo);

    /**
     * Getter for InResponseToSPReq.
     *
     * @return The InResponseToSPReq value.
     */
    String getInResponseToSPReq();

    /**
     * Setter for InResponseToSPRequ.
     *
     * @param inResponseToSPReq The Saml's response id.
     */
    void setInResponseToSPReq(String inResponseToSPReq);

    /**
     * Getter for opType.
     *
     * @return The opType value.
     */
    String getOpType();

    /**
     * Setter for opType.
     *
     * @param opType The operation type.
     */
    void setOpType(String opType);

    /**
     * Getter for destination.
     *
     * @return The destination value.
     */
    String getDestination();

    /**
     * Setter for destinationIp.
     *
     * @param destination The remote IP.
     */
    void setDestination(String destination);

    /**
     * Getter for message or assertion consumer.
     *
     * @return The message or assertion consumer.
     */
    String getMessage();

    /**
     * Setter for message or assertion consumer.
     *
     * @param message or assertion consumer.
     */
    void setMessage(String message);

    /**
     * Getter for country.
     *
     * @return The country value.
     */
    String getCountry();

    /**
     * Setter for country.
     *
     * @param country The country.
     */
    void setCountry(String country);

    /**
     * Getter for samlHash.
     *
     * @return The samlHash value.
     */
    byte[] getSamlHash();

    /**
     * Setter for samlHash.
     *
     * @param samlHash the encrypted SAML token
     */
    void setSamlHash(byte[] samlHash);

    /**
     * Getter for msgId.
     *
     * @return the msgId
     */
    String getMsgId();

    /**
     * Setter for msgId.
     *
     * @param msgId the ID of the originator of this message
     */
    void setMsgId(String msgId);

    /**
     * Getter for sPMsgId.
     *
     * @return the sPMsgId
     */
    String getSPMsgId();

    /**
     * Setter for sPMsgId.
     *
     * @param sPMsgId the ID of the originator of this message
     */
    void setSPMsgId(String sPMsgId);

    /**
     * The format of the returned String must be the following:
     * "requestCounter#ddMMMyyyykk:mm:ss#opType#originIp#originName
     * #destinationIp#destinationName#samlHash#[originatorName#msgId#]"
     *
     * The values enclosed in '[]' only apply when logging responses.
     *
     * @return {@inheritDoc}
     */
    @Override
    String toString();
}
