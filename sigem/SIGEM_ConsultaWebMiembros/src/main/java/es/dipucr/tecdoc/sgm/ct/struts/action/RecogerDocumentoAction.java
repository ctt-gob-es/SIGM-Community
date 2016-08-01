package es.dipucr.tecdoc.sgm.ct.struts.action;
/*
 *  $Id: RecogerDocumentoAction.java,v 1.2.2.1 2008/02/05 13:31:39 jconca Exp $
 */

import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tecdoc.sgm.base.ftp.FtpConnection;
import ieci.tecdoc.sgm.base.ftp.FtpTransferFns;
import ieci.tecdoc.sgm.core.services.dto.Entidad;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.modulos.buscador.beans.VolumeBean;
import es.dipucr.modulos.buscador.dao.SearchDAO;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;



	/** 
	 * Se sobrescribe el metodo execute de la clase Action
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public class RecogerDocumentoAction extends Action {	
	  private static final Logger logger = Logger.getLogger(RecogerDocumentoAction.class);

	  private static Properties propiedades = new Properties();

	  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception
	  {
	    SearchDAO buscador = null;
	    VolumeBean volumeBean = null;
	    String conInfo = null;
	    String aux = null;
	    String ip = null;
	    String port = null;
	    String user = null;
	    String password = null;
	    String path = null;
	    StringTokenizer st = null;
	    byte[] fichero = null;
	    try
	    {
	      propiedades.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("BuscadorDocs.properties"));
	      
	      //entidad = (String)request.getSession().getAttribute("entidad");
	      Entidad entidad = Misc.obtenerEntidad(request);
	      logger.warn("entidad: " + entidad);
	      logger.warn("entidad: " + entidad.getIdentificador());

	      String infopag = request.getParameter("guid");
	      String nombre = request.getParameter("nombreDoc");
	      
	      logger.warn("infopag: " + infopag + " nombre "+nombre);
	      
	      if (infopag == null) {
	        return null;
	      }

	      buscador = new SearchDAO();
	      volumeBean = buscador.searchVolume(entidad.getIdentificador(), infopag);

	      conInfo = volumeBean.getConInfo();
	      aux = conInfo.substring(conInfo.indexOf("|\"") + 2, conInfo.indexOf("\"|", conInfo.indexOf("|\"")));
	      st = new StringTokenizer(aux, ",");
	      ip = st.nextToken();
	      port = st.nextToken();
	      user = st.nextToken();
	      password = st.nextToken();
	      path = st.nextToken();
	      path = path + "/" + volumeBean.getVolName() + "/" + volumeBean.getLoc();
	      logger.warn("volumeBean.getVolName() "+volumeBean.getVolName());
	      logger.warn("volumeBean.getLoc() "+volumeBean.getLoc());
	      logger.warn("path "+path);

//	      logger.warn("IP: " + ip);
//	      logger.warn("Puerto: " + port);
//	      logger.warn("Usuario: " + user);
//	      logger.warn("Contraseña: " + password);
//	      logger.warn("Path: " + path);

	      password = "sigem";
	      password = propiedades.getProperty("password");

//	      logger.warn("Contraseña desencriptada: " + password);

	      FtpConnection ftp = new FtpConnection();
	      ftp.open(ip, Integer.parseInt(port), user, password);

	      fichero = FtpTransferFns.retrieveFile(ftp, path);
	      logger.warn("fichero.lenght " + fichero.length);

	      String ext = FilenameUtils.getExtension(path);
	      String mimetype = MimetypeMapping.getFileMimeType(path);
	      ServletOutputStream out = response.getOutputStream();
	      response.setHeader("Pragma", "public");
	      response.setHeader("Cache-Control", "max-age=0");
	      response.setContentType(mimetype);
	      response.setHeader("Content-Transfer-Encoding", "binary");
	      
	      logger.warn("path.contains(.tgz) "+path.contains(".tgz"));	     	      
	      if(path.contains(".tgz")){
	    	  path = nombre +".zip";
	      }
	      response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre + "." + ext + "\"");

	      response.setContentLength(fichero.length);
	      try
	      {
	    	  out.write(fichero, 0, fichero.length);
	      }
	      catch (Exception ex)
	      {
	        response.setContentType("text/html");
	        out.write(ex.getCause().getMessage().getBytes());
	        logger.warn("Error en el documento. "+ex.getMessage(), ex);
	      }
	      finally {
	        out.close();
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error("Error al mostrar el documento: " + e.getMessage(), e);
	    }

	    return null;
	  }
	}
