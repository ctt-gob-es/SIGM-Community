/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filtro que cambia el character encoding a UTF-8.
 * 
 * @author jortizs
 * */
public class CharacterEncodingFilter implements Filter {

    /**
     * Método que cambia el encoding de la petición y la respuesta.
     * 
     * @param req
     *            request.
     * @param resp
     *            response.
     * @param chain
     *            chain.
     * @throws IOException
     *             error de entrada/salida.
     * @throws ServletException
     *             excepción en el servlet.
     */
    public void doFilter(
	ServletRequest req, ServletResponse resp, FilterChain chain)
	throws IOException, ServletException {
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");
	chain.doFilter(
	    req, resp);
    }

    /**
     * Método de inicio del filtro.
     * 
     * @param filterConfig
     *            Configuración del filtro.
     * @throws ServletException
     *             excepción en el servlet.
     */
    public void init(
	FilterConfig filterConfig)
	throws ServletException {

    }

    /**
     * Método de fin del filtro.
     */
    public void destroy() {

    }
}