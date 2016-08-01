package es.dipucr.modulos.buscador.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.modulos.buscador.beans.SearchBean;
import es.dipucr.modulos.buscador.beans.SearchForm;
import es.dipucr.modulos.buscador.dao.SearchDAO;

public class SearchResultAction extends Action
{
  private static final Logger logger = Logger.getLogger(SearchResultAction.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String codCotejo = null;
    String entidad = null;
    SearchDAO buscador = null;
    SearchForm searchForm = null;
    SearchBean searchBean = null;
    try
    {
      buscador = new SearchDAO();

      searchForm = (SearchForm)form;

      entidad = (String)request.getSession().getAttribute("entidad");

      codCotejo = searchForm.getCotejo1() + searchForm.getCotejo2() + searchForm.getCotejo3() + searchForm.getCotejo4();
      logger.debug("Buscando el documento con el código de cotejo " + codCotejo);
      searchBean = buscador.searchDocument(entidad, codCotejo);

      request.setAttribute("searchBean", searchBean);

      if (searchBean.getInfopag() != null)
      {
        request.getSession().setAttribute("infopag", new String(searchBean.getInfopag()));
      }

      return mapping.findForward("success");
    }
    catch (Exception e)
    {
      logger.error("Se ha producido un error inesperado", e.fillInStackTrace());
    }return mapping.findForward("error");
  }
}
