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

package es.minhap.seap.ws.directorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.ws.AuthenticationWSHelper;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
import es.minhap.seap.ws.directorio.beans.Autenticacion;
import es.minhap.seap.ws.directorio.beans.Usuarios;
import es.minhap.seap.ws.directorio.excepciones.DirectorioException;
import es.minhap.seap.ws.directorio.types.EstadoUsuario;

@WebService(endpointInterface = "es.minhap.seap.ws.directorio.Directorio")
public class DirectorioImpl implements Directorio {

	@Autowired
	private UserAdmBO userBO;

	@Autowired
	private AuthenticationWSHelper authenticationWSHelper;		

	@Override
	public List<Usuarios> obtenerListadoNIFs(Autenticacion autenticacion) throws DirectorioException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		return obtenerListadoNIFsDesde(autenticacion, calendar);
	}

	@Override
	public List<Usuarios> obtenerListadoNIFsDesde(Autenticacion autenticacion, Calendar fechaDesde) throws DirectorioException {
		List<AbstractBaseDTO> usuarios = null;
		try {
			autentica(autenticacion);
			usuarios = userBO.queryListActiveByDate(fechaDesde.getTime());
		} catch (PfirmaException e) {
			throw new DirectorioException(e.getMessage());
		}
		return obtenerUsuarios(usuarios);
	}

	private PfUsersDTO autentica(Autenticacion autenticacion) throws PfirmaException {
		Set<String> perfilesNecesarios = new HashSet<>();
		perfilesNecesarios.add(es.seap.minhap.portafirmas.utils.Constants.C_PROFILES_WEBSERVICE);
		perfilesNecesarios.add(es.seap.minhap.portafirmas.utils.Constants.C_PROFILES_ADMIN);
		Authentication authentication = new Authentication();
		authentication.setUserName(autenticacion.getUsuario());
		authentication.setPassword(autenticacion.getClave());
		return authenticationWSHelper.authenticateWebservice(authentication, perfilesNecesarios);
	}

	private List<Usuarios> obtenerUsuarios(List<AbstractBaseDTO> usuarios) {
		ArrayList<Usuarios> usuariosWeb = new ArrayList<Usuarios>();
		for (AbstractBaseDTO usuarioDTO : usuarios) {
			usuariosWeb.add(convertirUsuario((PfUsersDTO) usuarioDTO));
		}
		return usuariosWeb;
	}

	private Usuarios convertirUsuario(PfUsersDTO usuarioDTO) {
		Usuarios usuarioWeb = new Usuarios();
		usuarioWeb.setDni(usuarioDTO.getCidentifier());
		usuarioWeb.setNombre(usuarioDTO.getDname());
		usuarioWeb.setApellido1(usuarioDTO.getDsurname1());
		usuarioWeb.setApellido2(usuarioDTO.getDsurname2());
		usuarioWeb.setEstado(EstadoUsuario.ALTA);
		return usuarioWeb;
	}
	
	

}
