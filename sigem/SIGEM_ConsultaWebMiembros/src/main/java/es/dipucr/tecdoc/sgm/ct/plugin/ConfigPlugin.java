package es.dipucr.tecdoc.sgm.ct.plugin;
/*
 *  $Id: ConfigPlugin.java,v 1.2.2.1 2008/02/05 13:31:39 jconca Exp $
 */
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

/**
 * @deprecated
 */
public class ConfigPlugin extends BasePlugin
{

  private static Logger logger          = Logger.getLogger(ConfigPlugin.class);

  /*
   * Atributos por defecto del plugin.
   */
  //private ModuleConfig  m_config        = null;
  private ActionServlet m_servlet       = null;

  /*
   * Atributos específicos de la aplicación
   */
  private String m_redirAutenticacion = null;
  private String m_dirDesconectar = null;
  
  /*
   * Métodos 
   */
  public String getRedirAutenticacion() {
	return m_redirAutenticacion;
  }

  public String getDirDesconectar() {
	return m_dirDesconectar;
  }
  
  public void setRedirAutenticacion(String redirAutenticacion) {
	this.m_redirAutenticacion = redirAutenticacion;
  }
  
  public void setDirDesconectar(String dirDesconectar) {
	this.m_dirDesconectar = dirDesconectar;
  }
  
/*
   * (non-Javadoc)
   * 
   * @see org.apache.struts.action.PlugIn#destroy()
   */
  public void destroy()
  {
    m_servlet.getServletContext().removeAttribute("redirAutenticacion");
    m_servlet.getServletContext().removeAttribute("dirDesconectar");
    
    m_servlet = null;
    //m_config = null;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
   *      org.apache.struts.config.ModuleConfig)
   */
  public void init(ActionServlet servlet, ModuleConfig config)
        throws ServletException
  {

    //this.m_config = config;
    this.m_servlet = servlet;
    
    m_servlet.getServletContext().setAttribute("redirAutenticacion", this.m_redirAutenticacion);
    m_servlet.getServletContext().setAttribute("dirDesconectar", this.m_dirDesconectar);
    
    if (logger.isDebugEnabled())
    {
      logger.debug("Defs.PLUGIN_REDIRAUTENTICACION: " + this.m_redirAutenticacion);
      logger.debug("Defs.PLUGIN_DIRDESCONECTAR: " + this.m_dirDesconectar);
    }
  }
}