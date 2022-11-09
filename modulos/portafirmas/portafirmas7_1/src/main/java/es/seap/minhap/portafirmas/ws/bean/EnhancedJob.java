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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedJob", propOrder = { "job", "enhancedUserJobInfo"})

public class EnhancedJob implements Serializable {

	private static final long serialVersionUID = 4834453483151118596L;

	@XmlElement(required = true)
	private Job job;
	@XmlElement(required = true)
	private EnhancedUserJobInfo enhancedUserJobInfo;
	
	
	
	/**
	 * Gets the value of the job property.
	 * 
	 * @return possible object is {@link Job }
	 * 
	 */
	public Job getJob() {
		return job;
	}

	/**
	 * Sets the value of the job property.
	 * 
	 * @param value
	 *            allowed object is {@link Job }
	 * 
	 */
	public void setJob(Job job) {
		this.job = job;
	}

	/**
	 * Gets the value of the enhancedUserJobInfo property.
	 * 
	 * @return possible object is {@link EnhancedUserJobInfo }
	 * 
	 */
	public EnhancedUserJobInfo getEnhancedUserJobInfo() {
		return enhancedUserJobInfo;
	}

	/**
	 * Sets the value of the enhancedUserJobInfo property.
	 * 
	 * @param value
	 *            allowed object is {@link EnhancedUserJobInfo }
	 * 
	 */
	public void setEnhancedUserJobInfo(EnhancedUserJobInfo enhancedUserJobInfo) {
		this.enhancedUserJobInfo = enhancedUserJobInfo;
	}

}
