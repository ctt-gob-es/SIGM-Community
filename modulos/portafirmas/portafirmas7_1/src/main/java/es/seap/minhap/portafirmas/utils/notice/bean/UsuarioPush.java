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

package es.seap.minhap.portafirmas.utils.notice.bean;

import java.io.Serializable;

public class UsuarioPush implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Nombre del usuario
	private String docUsuario;
    private String idExterno;
    //DNI del usuario
	private String idUsuario;

	
	
	@Override
	public boolean equals(Object obj) {
		    if (obj == this)
		    {
		        return true;
		    }
		    if (obj == null)
		    {
		        return false;
		    }
		    if (obj instanceof UsuarioPush)
		    {
		    	UsuarioPush other = (UsuarioPush)obj;
		        return other.getIdUsuario().equals(getIdUsuario()) &&
		                other.getDocUsuario().equals(getDocUsuario()) &&
		                other.getIdExterno().equals(getIdExterno());

		    }
		    else
		    {
		        return false;
		    }
	}



	public String getDocUsuario() {
		return docUsuario;
	}



	public void setDocUsuario(String docUsuario) {
		this.docUsuario = docUsuario;
	}



	public String getIdExterno() {
		return idExterno;
	}



	public void setIdExterno(String idExterno) {
		this.idExterno = idExterno;
	}



	public String getIdUsuario() {
		return idUsuario;
	}



	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
}
