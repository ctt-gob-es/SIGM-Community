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

package es.seap.minhap.portafirmas.utils.notice.message;

import java.util.List;

import es.seap.minhap.portafirmas.utils.notice.bean.UsuarioPush;

public class PushNoticeMessage implements NoticeMessage {

	private static final long serialVersionUID = 1L;

	private String titulo;
	private String cuerpo;
	private String icono;
	private String sonido;
	private List<UsuarioPush> destinatarios;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getSonido() {
		return sonido;
	}

	public void setSonido(String sonido) {
		this.sonido = sonido;
	}

	public List<UsuarioPush> getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(List<UsuarioPush> destinatarios) {
		this.destinatarios = destinatarios;
	}

	@Override
	public List<String> getReceivers() {
		return null;
	}

	@Override
	public void setReceivers(List<String> receivers) {
	}

}
