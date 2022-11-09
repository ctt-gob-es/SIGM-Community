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

package es.seap.minhap.portafirmas.business.ws;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.mpt.dsic.inside.ws.service.ApplicationLogin;
import es.mpt.dsic.inside.ws.service.DatosEntradaFichero;
import es.mpt.dsic.inside.ws.service.DatosSalida;
import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;

@Service
public class EeutilFirmaBO {
	
	@Autowired
	private EeUtilConfigManager eeUtilConfigManager;
	
	@Autowired
	private MailToAdminBO mailToAdminBO;

	Logger log = Logger.getLogger(EeutilFirmaBO.class);
	
	public byte[] firmarEnServidor(byte[] archivo) throws IOException, EeutilException{
		
		byte[] retorno = null;
        
        byte[] bytes = archivo;
        
		Map<String, String> params = null;
		
		// Carga de la configuración
		params = eeUtilConfigManager.cargarConfiguracion (eeUtilConfigManager.configuracionPorDefecto());
		if (params == null) {
			log.error("Se ha producido un error cargando la configuración del servicio eeutil en la llamada a firmarEnServidor de EeutilFirma");
			//mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILFIRMA);
			throw new EeutilException ("No se han encontrado parametros de configuracion de EeutilFirma");
		}
		
        ApplicationLogin aplicacionInfo = createApplicationLogin(params.get(Constants.C_PARAMETER_EEUTIL_FIRMA_USER), params.get(Constants.C_PARAMETER_EEUTIL_FIRMA_PASSWORD));

        DatosEntradaFichero datosEntradaFichero = new DatosEntradaFichero();

        datosEntradaFichero.setAlgoritmoFirma("SHA256withRSA");
        datosEntradaFichero.setCofirmarSiFirmado(false);
        datosEntradaFichero.setContenido(bytes);
        datosEntradaFichero.setModoFirma("IMPLICIT");
        datosEntradaFichero.setFormatoFirma("PAdES");

        java.net.URL urlWsdlServicio = new URL(params.get(Constants.C_PARAMETER_EEUTIL_FIRMA_URL));
        
        es.mpt.dsic.inside.ws.service.impl.EeUtilFirmarService port;
		try {
			es.mpt.dsic.inside.ws.service.impl.EeUtilFirmarServiceImplService service = new es.mpt.dsic.inside.ws.service.impl.EeUtilFirmarServiceImplService(urlWsdlServicio);
			port = service.getEeUtilFirmarServiceImplPort();
		} catch (Throwable t) {
			log.error("Se ha producido un error obteniendo el servicio eeutil en la llamada a firmarEnServidor de EeutilFirma", t);
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_CREAR_SERVICIO_UTILFIRMA, t);
			throw new EeutilException ("No se ha podido obtener el stub de EeutilFirma", t);
		}

        DatosSalida ds;
		try {
			ds = port.firmaFichero(aplicacionInfo, datosEntradaFichero);
		} catch (Throwable e) {
			mailToAdminBO.sendMailToAdmin(Constants.EEUTIL_FIRMA_FICHERO, e);
			log.error("Se ha producido un error en la llamada a firmaFichero de EeutilFirma" , e);
			throw new EeutilException("Se ha producido un error en la llamada a firmaFichero de EeutilFirma" + e.getMessage());
		}
        
        if("ok".equalsIgnoreCase(ds.getEstado())){
        	retorno = ((es.mpt.dsic.inside.ws.service.ResultadoFirmaFichero)ds.getDatosResultado()).getFirma();
        }
        else{
        	throw new EeutilException ("Ha fallado realizando la firma en servidor del informe de firma, error en llamada firmaFichero de EeutilFirma");
        }
        
        return retorno;
	}
	
	private ApplicationLogin createApplicationLogin (String idaplicacion, String password) throws EeutilException{
		if (idaplicacion == null) {
			throw new EeutilException ("El usuario de EeutilFirma es nulo");
		}
		if (password == null) {
			throw new EeutilException ("El password de EeutilFirma es nulo");
		}
		ApplicationLogin appInfo = new ApplicationLogin();
		appInfo.setIdaplicacion(idaplicacion);
		appInfo.setPassword(password);
        return appInfo;
	}
	
}
