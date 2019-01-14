/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class InstanceConfiguration.
 * 
 * @author fjquevedo
 */
public class InstanceEngine {

    /** The configuration. */
    private List<ConfigurationEngine> configuration = new ArrayList<ConfigurationEngine>();

    /** The name. */
    private String name;

    /**
     * Gets the parameters.
     * 
     * @return the parameters
     */
    public final List<ConfigurationEngine> getConfiguration() {
	return this.configuration;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public final String getName() {
	return name;
    }

    /**
     * Sets the parameters.
     * 
     * @param newConfiguration the new parameters
     */
    public final void setConfiguration(final List<ConfigurationEngine> newConfiguration) {
	this.configuration = newConfiguration;
    }

    /**
     * Sets the name.
     * 
     * @param newName the new name
     */
    public final void setName(final String newName) {
	this.name = newName;
    }

}
