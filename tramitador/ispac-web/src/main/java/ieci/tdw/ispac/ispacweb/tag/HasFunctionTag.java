package ieci.tdw.ispac.ispacweb.tag;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.utils.CollectionUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.security.UserCredentials;
import ieci.tdw.ispac.ispacweb.security.UserCredentialsHelper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

import org.apache.log4j.Logger;


/**
 * Tag para comprobar si el usuario tiene una función asignada.
 * 
 * Ejemplos:
 * 
 *	<ispac:hasFunction var="autorizado" functions="FUNCION1, FUNCION2"/>
 *
 *	<ispac:hasFunction functions="FUNCION1, FUNCION2">
 *		<p>El usuario tiene la función adecuada.</p>
 *	</ispac:hasFunction>
 * 
 */
public class HasFunctionTag extends ConditionalTagSupport {

	private static final long serialVersionUID = 1573228852224900695L;
	
	private static final Logger LOGGER = Logger.getLogger(HasFunctionTag.class);

	protected String functions = null;

	/**
	 * Constructor.
	 */
	public HasFunctionTag() {
		super();
	}

	/**
	 * Releases any resources this ConditionalTagSupport may have (or inherit).
	 */
	public void release() {
		super.release();
		setFunctions(null);
	}

	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	/**
	 * Subclasses implement this method to compute the boolean result of the
	 * conditional action. This method is invoked once per tag invocation by
	 * doStartTag().
	 * 
	 * @return a boolean representing the condition that a particular subclass
	 *         uses to drive its conditional logic.
	 * @throws JspTagException
	 */
	protected boolean condition() throws JspTagException {
		
		boolean result = false;

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		try {
			
			List<String> functionList = getFunctionList();
			if (!CollectionUtils.isEmpty(functionList)) {
				UserCredentials credentials = UserCredentialsHelper.getUserCredentials(request, null);

				// [Dipucr-Manu Ticket #478] - INICIO - ALSIGM3 Nueva opción Repositorio Común
				//Esto es un apaño hasta que se decida si los administradores pueden modificar el repositorio común, de momento no pueden, sólo los usuarios privilegiados para ello.
				//Cuado se decida se deja como estaba y ya funciona tanto para usuarios privilegiados como para administradores.
				List<String> permisoEspecial = new ArrayList<String>();
				permisoEspecial.add("FUNC_INV_REPOSITORIO_COMUN_EDIT");
				
				if(functionList.contains("FUNC_INV_REPOSITORIO_COMUN_EDIT")){
					if(credentials.containsAnyFunction(permisoEspecial)){
						result = credentials.isCatalogAdministrator() || credentials.containsAnyFunction(functionList);
					}
					else{
						result = false;
					}
				}
				else{
					result = credentials.isCatalogAdministrator() || credentials.containsAnyFunction(functionList);
				}
			}
			// [Dipucr-Manu Ticket #478] - FIN - ALSIGM3 Nueva opción Repositorio Común
		} catch (ISPACException e) {
			LOGGER.error("Error al comprobar las funciones del usuario", e);
			throw new JspTagException(e.toString());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> getFunctionList() {
		return CollectionUtils.createList(StringUtils.split(getFunctions(), ","));
	}
}
