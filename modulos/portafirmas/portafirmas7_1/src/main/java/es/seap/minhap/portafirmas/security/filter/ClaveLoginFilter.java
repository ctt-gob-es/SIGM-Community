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

package es.seap.minhap.portafirmas.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import es.guadaltel.framework.authenticator.exception.AuthenticatorException;
import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.login.impl.ConstantsClave;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.AuthenticationHelper;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;


/**
 * Filtro de acceso cuando la autenticación se hace a través
 * de la plataforma Cl@ve
 * 
 * @author Alfredo Barquero
 *
 */
public class ClaveLoginFilter extends AbstractAuthenticationProcessingFilter {
	private static Logger logger = Logger.getLogger(ClaveLoginFilter.class);

	public ClaveLoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	
	protected ClaveLoginFilter() {
		super("/login/accesoClave");
	}
	
	@Resource(name="claveProperties")
	private Properties claveProperties;
	
	@Autowired
	private AuthenticateBO authenticateBO;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		
		logger.debug("login by clave");		
			
		String version = claveProperties.getProperty(ConstantsClave.CLAVE_VERSION);
		if(version.equals("2"))
		{
			
//			String usuario_logeado=null;
			String nombre = "";
			String apellidos = "";
			String nif="";
			String email = "";
			String primerApellido = "";
			String partialAfirma = "";
			String selectedIdP = "";
			String relayState = "";		
					
			//Conexto de SPProxy
			ServletContext srcServletContext = request.getSession().getServletContext();
	    	ServletContext targetServletContext = srcServletContext.getContext("/SP2");
	    	
	    	
	    	//Como acceder a la sesion actual, lo dejo comentado por si hicera falta, borrando los parametros de proxy se arregla el problema de seguridad
//	    	try {	
//	    	
//	    		usuario_logeado=((PfUsersDTO)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCidentifier();
//	    	
//	    	} catch (Exception e) {
//				logger.error(e.getMessage());				
//			}
	    	   	
	    	
			try {		
		
				try {
					apellidos=(String)  targetServletContext.getAttribute("FamilyName").toString().subSequence(targetServletContext.getAttribute("FamilyName").toString().indexOf("[")+1, targetServletContext.getAttribute("FamilyName").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				try {
					nombre=(String)  targetServletContext.getAttribute("FirstName").toString().subSequence(targetServletContext.getAttribute("FirstName").toString().indexOf("[")+1, targetServletContext.getAttribute("FirstName").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				try {
					nif=(String)  targetServletContext.getAttribute("PersonIdentifier").toString().subSequence(targetServletContext.getAttribute("PersonIdentifier").toString().indexOf("[")+1, targetServletContext.getAttribute("PersonIdentifier").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				try {
					primerApellido=(String)  targetServletContext.getAttribute("FirstSurname").toString().subSequence(targetServletContext.getAttribute("FirstSurname").toString().indexOf("[")+1, targetServletContext.getAttribute("FirstSurname").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				try {
					partialAfirma=(String)  targetServletContext.getAttribute("PartialAfirma").toString().subSequence(targetServletContext.getAttribute("PartialAfirma").toString().indexOf("[")+1, targetServletContext.getAttribute("PartialAfirma").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				try {
					selectedIdP=(String)  targetServletContext.getAttribute("SelectedIdP").toString().subSequence(targetServletContext.getAttribute("SelectedIdP").toString().indexOf("[")+1, targetServletContext.getAttribute("SelectedIdP").toString().indexOf("]"));
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
			
				
				if (nombre == null || apellidos == null ||nif == null ||partialAfirma == null || nif.isEmpty() ){
					
					throw new BadCredentialsException(Constants.MSG_GENERIC_ERROR);

				}
				
				
				else {
					
					String nifRepre = null;
					String nifPersonaJuridica = null;
					String razonSocial = null;
					
					if(!partialAfirma.isEmpty())
					{
					
					   	String strSolicitud = Base64.decodeAsString(partialAfirma);				   	
					   	
					   	try {
					   		nifRepre = dameNifRepresentante(strSolicitud);
					   	  	if(nifRepre!=null)
						   	{
						   		nif = nifRepre;					   		
						   	}
					   	}catch (Exception e) {
					   		logger.error(e.getMessage());
						}		
				   	
					   	try {
					   		nifPersonaJuridica = dameNifPersonaJuridica(strSolicitud);
					   	}catch (Exception e) {
					   		logger.error(e.getMessage());
						}
					   	
					   	try {
					   		razonSocial = dameRazonSocial(strSolicitud);
					   		if(razonSocial!=null)
							{
						   		nombre = razonSocial;
						   		apellidos = "";
							}
					   	}catch (Exception e) {
					   		logger.error(e.getMessage());
						}
					   	
					   	try {
					   		email = dameEmail(strSolicitud);
					   		if(email==null) {
						   		email="";
						   	}
					   	}catch (Exception e) {
					   		logger.error(e.getMessage());
						}
				   	
					}
				   	
				   	logger.debug("Se inicia el proceso de autenticacion de usuario");
					logger.debug("Escribo entre asterísticos el **dni** que recupero de clave");
					logger.debug("**"+nif+"**");
					
					if(null==nif || nif.isEmpty())
					{
						throw new BadCredentialsException("El dni tiene un formato incorrecto. Usuario no válido");
					}
					
					//Borrar parametros del Proxy para que no pueda entrar otra persona en su lugar, si no se borran puede haber un problema de seguridad
					try {
						targetServletContext.removeAttribute("PersonIdentifier");
						targetServletContext.removeAttribute("PartialAfirma");
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

					return authenticateBO.autenticarUsuario(nif);					
					
				}
			
		}
		
		//Si no esta configurada la version 2 dejo el codigo como estaba
		else {
			
				 try {
						STORKAuthnResponse authnResponse = null;
						IPersonalAttributeList personalAttributeList = null;
						
		
						/* Recuperamos la respuesta de la aplicación */
						String samlResponse = request.getParameter(Constants.ATRIBUTO_SAML_RESPONSE);
						if (!StringUtils.isBlank(samlResponse)) {
							/* Decodificamos la respuesta SAML */
							byte[] decSamlToken = PEPSUtil.decodeSAMLToken(samlResponse);
		
							/* Obtenemos la instancia de SAMLEngine */
							STORKSAMLEngine engine = STORKSAMLEngine.getInstance(ConstantsClave.SP_CONF);
		
							/* Validamos el token SAML */
							try {				
								authnResponse = engine.validateSTORKAuthnResponse(decSamlToken,	(String) request.getRemoteHost());
							} catch (STORKSAMLEngineException e) {
								logger.error(e.getMessage());
								throw new BadCredentialsException("No se ha podido validar el token de la SAML Response",e);
							}
		
							
							if (authnResponse.isFail()) {
								throw new BadCredentialsException("Credenciales Erróneas. Usuario no válido.");
							} else {
								/* Recuperamos los atributos */
								personalAttributeList = authnResponse.getPersonalAttributeList();
		
								PersonalAttribute identificadorAttribute = personalAttributeList.get(claveProperties.getProperty(Constants.PROPERTY_IDENTIFICADOR));
								List<String> identificadores = identificadorAttribute.getValue();
								String dni =  identificadores.get(0).split("/")[2];
								
								logger.debug("Se inicia el proceso de autenticacion de usuario");
								logger.debug("Escribo entre asterísticos el dni__**dni**__ que recupero de clave");
								logger.debug("dni__**"+dni+"**__");
								
								if(null==dni || dni.isEmpty())
								{
									throw new BadCredentialsException("El dni tiene un formato incorrecto. Usuario no válido");
								}
								
								return authenticateBO.autenticarUsuario(dni);
							}
						}
					} catch (BadCredentialsException e) {
						throw e;
					} catch (Exception e) {
						logger.error(e);
						throw new BadCredentialsException(Constants.MSG_GENERIC_ERROR);
					}
					throw new BadCredentialsException( "Credenciales Erróneas. Usuario no válido." );
		}
		

	}			
	
	
	private String dameNifRepresentante(String strSolicitud) {
		
		String aux = strSolicitud;
		
		try 
		{		
		
		int indexBeginSignature = aux.lastIndexOf(ConstantsClave.CLAVE_EVIDENCIAS_NIF_REPRE);
		aux = aux.substring(indexBeginSignature, aux.length());
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_NIF_REPRE, "");
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_VALOR, "");
		aux = aux.split("<", 2)[0];
		
		}catch(Exception e) {
			
			return null;
			
		}
		
		return aux;		
	}
	
    private String dameNifPersonaJuridica(String strSolicitud) {
		
		String aux = strSolicitud;
		
		try 
		{		
		
		int indexBeginSignature = aux.lastIndexOf(ConstantsClave.CLAVE_EVIDENCIAS_NIF_CIF);
		aux = aux.substring(indexBeginSignature, aux.length());
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_NIF_CIF, "");
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_VALOR, "");
		aux = aux.split("<", 2)[0];
		
		}catch(Exception e) {
			
			return null;
			
		}
		
		return aux;		
	}
	
	
	private String dameRazonSocial(String strSolicitud) {
		
		String aux = strSolicitud;
		
		try 
		{		
		
		int indexBeginSignature = aux.lastIndexOf(ConstantsClave.CLAVE_EVIDENCIAS_RAZON_SOCIAL);
		aux = aux.substring(indexBeginSignature, aux.length());
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_RAZON_SOCIAL, "");
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_VALOR, "");
		aux = aux.split("<", 2)[0];
		
		}catch(Exception e) {			
			
			return null;
			
		}
		
		return aux;		
	}
	
	
	private String dameEmail(String strSolicitud) {
		
		String aux = strSolicitud;
		
		try 
		{		
		
		int indexBeginSignature = aux.lastIndexOf(ConstantsClave.CLAVE_EVIDENCIAS_EMAIL);
		aux = aux.substring(indexBeginSignature, aux.length());
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_EMAIL, "");
		aux = aux.replaceFirst(ConstantsClave.CLAVE_EVIDENCIAS_VALOR, "");
		aux = aux.split("<", 2)[0];
		
		}catch(Exception e) {			
			
			return null;
			
		}
		
		return aux;		
	}

}