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

package es.seap.minhap.portafirmas.web.beans;

import java.io.Serializable;

public class UploadFileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 604423229399076050L;
	
	private boolean success;

	private String applicationError;
	private String applicationErrorDescription;
	
	private String validacionTamañoOk;
	private String  validacionTamanioDescription;
	
	private String validacionPDFAOk;
	private String validacionPDFADescription;
	
	private String validacionVersionPDFOk;
	private String validacionVersionPDFDescripcion;
	
	private String validacionFirmaOk;
	private String validacionFirmaDescription;
	
	private Integer id;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getApplicationError() {
		return applicationError;
	}
	public void setApplicationError(String applicationError) {
		this.applicationError = applicationError;
	}
	public String getApplicationErrorDescription() {
		return applicationErrorDescription;
	}
	public void setApplicationErrorDescription(String applicationErrorDescription) {
		this.applicationErrorDescription = applicationErrorDescription;
	}
	public String getValidacionTamañoOk() {
		return validacionTamañoOk;
	}
	public void setValidacionTamañoOk(String validacionTamañoOk) {
		this.validacionTamañoOk = validacionTamañoOk;
	}
	public String getValidacionTamanioDescription() {
		return validacionTamanioDescription;
	}
	public void setValidacionTamanioDescription(String validacionTamanioDescription) {
		this.validacionTamanioDescription = validacionTamanioDescription;
	}
	public String getValidacionPDFAOk() {
		return validacionPDFAOk;
	}
	public void setValidacionPDFAOk(String validacionPDFAOk) {
		this.validacionPDFAOk = validacionPDFAOk;
	}
	public String getValidacionPDFADescription() {
		return validacionPDFADescription;
	}
	public void setValidacionPDFADescription(String validacionPDFADescription) {
		this.validacionPDFADescription = validacionPDFADescription;
	}
	public String getValidacionVersionPDFOk() {
		return validacionVersionPDFOk;
	}
	public void setValidacionVersionPDFOk(String validacionVersionPDFOk) {
		this.validacionVersionPDFOk = validacionVersionPDFOk;
	}
	public String getValidacionVersionPDFDescripcion() {
		return validacionVersionPDFDescripcion;
	}
	public void setValidacionVersionPDFDescripcion(String validacionVersionPDFDescripcion) {
		this.validacionVersionPDFDescripcion = validacionVersionPDFDescripcion;
	}
	public String getValidacionFirmaOk() {
		return validacionFirmaOk;
	}
	public void setValidacionFirmaOk(String validacionFirmaOk) {
		this.validacionFirmaOk = validacionFirmaOk;
	}
	public String getValidacionFirmaDescription() {
		return validacionFirmaDescription;
	}
	public void setValidacionFirmaDescription(String validacionFirmaDescription) {
		this.validacionFirmaDescription = validacionFirmaDescription;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
