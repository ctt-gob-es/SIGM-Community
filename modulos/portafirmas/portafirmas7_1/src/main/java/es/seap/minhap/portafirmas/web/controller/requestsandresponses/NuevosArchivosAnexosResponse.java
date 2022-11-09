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

package es.seap.minhap.portafirmas.web.controller.requestsandresponses;

import java.io.Serializable;

public class NuevosArchivosAnexosResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombreArchivo;
	private String tipoDeArchivo;
	private String usuarioAnexo;
	private String comentarioAnexo;
	private String chash;
	private boolean esTCNYTCNActivo;
	private boolean esFacturae;
	
	
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getTipoDeArchivo() {
		return tipoDeArchivo;
	}
	public void setTipoDeArchivo(String tipoDeArchivo) {
		this.tipoDeArchivo = tipoDeArchivo;
	}
	public String getUsuarioAnexo() {
		return usuarioAnexo;
	}
	public void setUsuarioAnexo(String usuarioAnexo) {
		this.usuarioAnexo = usuarioAnexo;
	}
	public String getComentarioAnexo() {
		return comentarioAnexo;
	}
	public void setComentarioAnexo(String comentarioAnexo) {
		this.comentarioAnexo = comentarioAnexo;
	}
	public NuevosArchivosAnexosResponse(String nombreArchivo, String tipoDeArchivo, String usuarioAnexo,
			String comentarioAnexo) {
		super();
		this.nombreArchivo = nombreArchivo;
		this.tipoDeArchivo = tipoDeArchivo;
		this.usuarioAnexo = usuarioAnexo;
		this.comentarioAnexo = comentarioAnexo;
	}
	
	public NuevosArchivosAnexosResponse(String nombreArchivo, String tipoDeArchivo, String usuarioAnexo,
			String comentarioAnexo, String chash, boolean tcnYActivo, boolean facturae) {
		super();
		this.nombreArchivo = nombreArchivo;
		this.tipoDeArchivo = tipoDeArchivo;
		this.usuarioAnexo = usuarioAnexo;
		this.comentarioAnexo = comentarioAnexo;
		this.chash = chash;
		this.setEsTCNYTCNActivo(tcnYActivo);
		this.setEsFacturae(facturae);
	}
	public NuevosArchivosAnexosResponse() {
		super();
	}
	public String getChash() {
		return chash;
	}
	public void setChash(String chash) {
		this.chash = chash;
	}
	public boolean isEsTCNYTCNActivo() {
		return esTCNYTCNActivo;
	}
	public void setEsTCNYTCNActivo(boolean esTCNYTCNActivo) {
		this.esTCNYTCNActivo = esTCNYTCNActivo;
	}
	public boolean isEsFacturae() {
		return esFacturae;
	}
	public void setEsFacturae(boolean esFacturae) {
		this.esFacturae = esFacturae;
	}

}
