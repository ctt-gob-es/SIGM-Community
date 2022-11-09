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
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedUserJobAssociated", propOrder = { "enhancedUser", "enhancedJob", "fstart", "fend"})

public class EnhancedUserJobAssociated implements Serializable {

	private static final long serialVersionUID = 3397290788016310665L;
	
	@XmlElement(required = true)
	private EnhancedUser enhancedUser;
	@XmlElement(required = true)
	private EnhancedJob enhancedJob;
	@XmlElement(required = true)
	private XMLGregorianCalendar fstart;
	@XmlElement(required = false)
	private XMLGregorianCalendar fend;
	
	public EnhancedUser getEnhancedUser() {
		return enhancedUser;
	}
	public void setEnhancedUser(EnhancedUser enhancedUser) {
		this.enhancedUser = enhancedUser;
	}
	public EnhancedJob getEnhancedJob() {
		return enhancedJob;
	}
	public void setEnhancedJob(EnhancedJob enhancedJob) {
		this.enhancedJob = enhancedJob;
	}
	public XMLGregorianCalendar getFstart() {
		return fstart;
	}
	public void setFstart(XMLGregorianCalendar fstart) {
		this.fstart = fstart;
	}
	public XMLGregorianCalendar getFend() {
		return fend;
	}
	public void setFend(XMLGregorianCalendar fend) {
		this.fend = fend;
	}
	
	
	

}
