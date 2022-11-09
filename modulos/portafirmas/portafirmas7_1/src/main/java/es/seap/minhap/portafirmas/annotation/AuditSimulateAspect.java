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

package es.seap.minhap.portafirmas.annotation;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfAuditoriaDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;

@Aspect
@Component

@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AuditSimulateAspect {

	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private BaseDAO baseDAO;
	
	/**
	 * Metodo del aspecto que se ejecutara despues de encontrar una anotacion de tipo @AuditSimulateAnnotation en un metodo.
	 * @param jp joinpont.
	 * @param auditable la anotacion.
	 */
	@After(value = "@annotation(auditable))", argNames = "auditable")
	public void logAuditActivity(JoinPoint jp, AuditSimulateAnnotation auditable) {
//		log.error("Incio Metodo logAuditActivity");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();

			if (authorization != null) {
				// escribimos en la traza de auditoria simulada.
				if (authorization.isUserSimulado()) {
					
					PfUsersDTO oUserOriginalDTO = authorization.getUserOriginal();
					PfUsersDTO oUsersSimuladoDTO = authorization.getUserDTO();

					String parametrosAudit = obtenerInformacionAdicionalAuditoria512c(jp, auditable);
					
					
					String nombreOperacion= podarContenidoTamanoEspecificado(auditable.nombreOperacion(),256);

					
					String nameClassApuntado=podarContenidoTamanoEspecificado(jp.getTarget().getClass().getName(),256);
					String nameMethodApuntado=podarContenidoTamanoEspecificado(((MethodSignature)jp.getSignature()).getMethod().getName(),256);
					
					PfAuditoriaDTO pfAudDTO= new PfAuditoriaDTO(oUserOriginalDTO.getPrimaryKey(), oUsersSimuladoDTO.getPrimaryKey(), nombreOperacion, nameClassApuntado, nameMethodApuntado,parametrosAudit);
					
					baseDAO.insertOrUpdate(pfAudDTO);

					//System.out.println("HOLA SOY USUARIO SIMULADO me puedes AUDITAR" + "ORIGINAL==>"
					//		+ oUserOriginalDTO.getCidentifier() + "||SIMULADO==>" + oUsersSimuladoDTO.getCidentifier());

				} else {
					PfUsersDTO oUserOriginalDTO = authorization.getUserOriginal();
					//System.out.println("ADIOS SOY USUARIO SIN SIMULAR: " + oUserOriginalDTO.getCidentifier());
				}

			}

		}
//		log.error("Fin Metodo logAuditActivity");

	}

	/***
	 * Metodo para obtener la informacion de parametrizacion. Si es mayor que 512 caracteres, se poda al final.
	 * @param jp joinpoint.
	 * @param auditable anotacion.
	 * @return valor de los parametros.
	 */
	private String obtenerInformacionAdicionalAuditoria512c(JoinPoint jp, AuditSimulateAnnotation auditable) {

		StringBuilder strB = new StringBuilder();

		MethodSignature methodSignature = (MethodSignature) jp.getSignature();
		Method method = methodSignature.getMethod();
		// AuditSimulateAnnotation annotation =
		// method.getAnnotation(AuditSimulateAnnotation.class);
		AuditSimulateMapping[] aDatosAnnotation = auditable.datosSimulado();

		// datos del metodo.
		String[] parameterNames = methodSignature.getParameterNames();
		Class[] parameterTypes = methodSignature.getParameterTypes();
		Object[] argValues = jp.getArgs();

		for (int c = 0; c < aDatosAnnotation.length; c++) {
			// datos de la anotacion.
			String paramName = aDatosAnnotation[c].paramName();
			Class paramType = aDatosAnnotation[c].paramType();
			String attrName = aDatosAnnotation[c].attrName();
			Class attrType = aDatosAnnotation[c].attrType();

			if (attrName.equals("")) {

				int cc = 0;
				for (String parameterName : parameterNames) {
					if (parameterName.equals(paramName)) {
						strB.append(parameterName + "=" + argValues[cc] + ";");
					}
					cc++;
				}
			} else {
				// preparado para ello, attrName, attrParam
				// TODO throw new Exception("NO IMPLEMENTADO TODAVIA, EL TIPO DE PARAMETRO A
				// DEVOLVER TIENE QUE SE UN TIPO PRIMITIVO Y NO UN OBJETO");
			}

		}
		
		
		String content= strB.toString();

		return podarContenidoTamanoEspecificado(content, 512);
	}
	
	
	
	private String podarContenidoTamanoEspecificado(String contenido, int longitudMaxima)
	{
		String result = null;
		
		if(contenido != null && contenido.length()>longitudMaxima)
		{
			//podamos al valor de contenido menos 5 caracteres y añadimos ...
			result=contenido.substring(0,longitudMaxima-5).intern().concat(" ...");
			log.warn("El espacio para la parametrizacion ha superado el espacio maximo de almacenaje en tabla"+ longitudMaxima);
		}
		else
		{
			result = contenido;
		}
		
		return result;
	}

}
