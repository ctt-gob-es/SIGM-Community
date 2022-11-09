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

import java.util.Date;

public class PfUserMessageDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private String dsubject;
	private String ttext;
	private Date fstart;
	private Date fexpiration;
	private Long userMessagePk;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String ctag;


	public PfUserMessageDTO() {
		super();
	}


	/**
	 * @return the primaryKey
	 */
	public Long getPrimaryKey() {
		return primaryKey;
	}


	/**
	 * @param primaryKey the primaryKey to set
	 */
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}


	/**
	 * @return the ttext
	 */
	public String getTtext() {
		return ttext;
	}


	/**
	 * @param ttext the ttext to set
	 */
	public void setTtext(String ttext) {
		this.ttext = ttext;
	}


	/**
	 * @return the fstart
	 */
	public Date getFstart() {
		return fstart;
	}


	/**
	 * @param fstart the fstart to set
	 */
	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}


	/**
	 * @return the fexpiration
	 */
	public Date getFexpiration() {
		return fexpiration;
	}


	/**
	 * @param fexpiration the fexpiration to set
	 */
	public void setFexpiration(Date fexpiration) {
		this.fexpiration = fexpiration;
	}


	/**
	 * @return the userMessagePk
	 */
	public Long getUserMessagePk() {
		return userMessagePk;
	}


	/**
	 * @param userMessagePk the userMessagePk to set
	 */
	public void setUserMessagePk(Long userMessagePk) {
		this.userMessagePk = userMessagePk;
	}


	/**
	 * @return the ccreated
	 */
	public String getCcreated() {
		return ccreated;
	}


	/**
	 * @param ccreated the ccreated to set
	 */
	public void setCcreated(String ccreated) {
		this.ccreated = ccreated;
	}


	/**
	 * @return the fcreated
	 */
	public Date getFcreated() {
		return fcreated;
	}


	/**
	 * @param fcreated the fcreated to set
	 */
	public void setFcreated(Date fcreated) {
		this.fcreated = fcreated;
	}


	/**
	 * @return the cmodified
	 */
	public String getCmodified() {
		return cmodified;
	}


	/**
	 * @param cmodified the cmodified to set
	 */
	public void setCmodified(String cmodified) {
		this.cmodified = cmodified;
	}


	/**
	 * @return the fmodified
	 */
	public Date getFmodified() {
		return fmodified;
	}


	/**
	 * @param fmodified the fmodified to set
	 */
	public void setFmodified(Date fmodified) {
		this.fmodified = fmodified;
	}


	/**
	 * @return the dsubject
	 */
	public String getDsubject() {
		return dsubject;
	}


	/**
	 * @param dsubject the dsubject to set
	 */
	public void setDsubject(String dsubject) {
		this.dsubject = dsubject;
	}


	/**
	 * @return the ctag
	 */
	public String getCtag() {
		return ctag;
	}


	/**
	 * @param ctag the ctag to set
	 */
	public void setCtag(String ctag) {
		this.ctag = ctag;
	}


	
}
