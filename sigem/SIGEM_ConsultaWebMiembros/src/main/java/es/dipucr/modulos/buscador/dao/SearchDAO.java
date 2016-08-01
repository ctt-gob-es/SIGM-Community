package es.dipucr.modulos.buscador.dao;

import es.dipucr.modulos.buscador.actions.SearchResultAction;
import es.dipucr.modulos.buscador.beans.SearchBean;
import es.dipucr.modulos.buscador.beans.VolumeBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class SearchDAO
{
  private static final Logger logger = Logger.getLogger(SearchResultAction.class);

  public SearchBean searchDocument(String entidad, String codCotejo)
  {
    Connection conn = null;
    Statement stmt = null;
    ResultSet jdbcRS = null;
    String stmtText = "";
    String ent = null;
    SearchBean searchBean = new SearchBean();
    Context contextName = null;
    try
    {
      contextName = new InitialContext();
      ent = "java:comp/env/jdbc/tramitadorDS_" + entidad;
      logger.debug("Conectando a la fuente de datos: " + ent);
      DataSource ds = (DataSource)contextName.lookup(ent);
      conn = ds.getConnection();
      stmt = conn.createStatement();

      stmtText = "SELECT nombre, numexp, fdoc, tp_reg, id, infopag_rde, nreg, freg, origen, destino FROM spac_dt_documentos WHERE cod_cotejo ='" + 
        codCotejo + "' " +
        		" UNION ALL " +        
        		" SELECT nombre, numexp, fdoc, tp_reg, id, infopag_rde, nreg, freg, origen, destino FROM spac_dt_documentos_h WHERE cod_cotejo ='" + 
        codCotejo + "'  ORDER BY id DESC";

      if (logger.isDebugEnabled()) {
        logger.debug("stmtText: " + stmtText);
      }
      jdbcRS = stmt.executeQuery(stmtText);

      if (jdbcRS.next()) {
        searchBean.setCodCotejo(codCotejo);
        searchBean.setNombre(jdbcRS.getString(1));
        searchBean.setNumExp(jdbcRS.getString(2));
        searchBean.setFechaDoc(jdbcRS.getString(3));
        searchBean.setTpReg(jdbcRS.getString(4));
        searchBean.setId(jdbcRS.getString(5));
        searchBean.setInfopag(jdbcRS.getString(6));

        searchBean.setNreg(jdbcRS.getString(7));
        searchBean.setFreg(jdbcRS.getString(8));
        searchBean.setOrigen(jdbcRS.getString(9));
        searchBean.setDestino(jdbcRS.getString(10));
      }

      jdbcRS.close();
      stmt.close();
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    finally
    {
      try
      {
        if (conn != null)
        {
          conn.close();
          if (logger.isDebugEnabled())
          {
            logger.debug("Sesión cerrada.");
          }
        }
        if (jdbcRS != null) jdbcRS.close();
        if (stmt != null) stmt.close();
        if (contextName != null) contextName.close();

      }
      catch (Exception ee)
      {
        logger.error(ee);
      }

    }

    return searchBean;
  }

  public VolumeBean searchVolume(String entidad, String infopag)
  {
    Connection conn = null;
    Statement stmt = null;
    ResultSet jdbcRS = null;
    String stmtText = "";
    String ent = null;
    VolumeBean volumeBean = new VolumeBean();
    Context contextName = null;
    String extid1 = null;
    String extid2 = null;
    String volid = null;
    String repid = null;
    try
    {
      extid1 = infopag.substring(infopag.indexOf("<archive>") + 9, infopag.indexOf("</archive>"));
      extid2 = infopag.substring(infopag.indexOf("<folder>") + 8, infopag.indexOf("</folder>"));
      logger.debug("Archive: " + extid1);
      logger.debug("Folder: " + extid2);

      contextName = new InitialContext();
      ent = "java:comp/env/jdbc/registroDS_" + entidad;
      logger.debug("Conectando a la fuente de datos: " + ent);
      DataSource ds = (DataSource)contextName.lookup(ent);
      conn = ds.getConnection();

      stmt = conn.createStatement();

      stmtText = "SELECT volid, loc, filesize FROM ivolfilehdr WHERE extid1 ='" + extid1 + "' AND extid2 ='" + extid2 + "'";

      if (logger.isDebugEnabled()) {
        logger.debug("stmtText: " + stmtText);
      }
      jdbcRS = stmt.executeQuery(stmtText);

      if (jdbcRS.next()) {
        volid = jdbcRS.getString(1);
        volumeBean.setLoc(jdbcRS.getString(2));
        volumeBean.setFilesize(Integer.parseInt(jdbcRS.getString(3)));
      }
      jdbcRS.close();
      stmt.close();

      stmt = conn.createStatement();

      stmtText = "SELECT name, repid FROM ivolvolhdr WHERE id ='" + volid + "'";

      if (logger.isDebugEnabled()) {
        logger.debug("stmtText: " + stmtText);
      }
      jdbcRS = stmt.executeQuery(stmtText);

      if (jdbcRS.next()) {
        volumeBean.setVolName(jdbcRS.getString(1));
        repid = jdbcRS.getString(2);
      }
      jdbcRS.close();
      stmt.close();

      stmt = conn.createStatement();

      stmtText = "SELECT info FROM ivolrephdr WHERE id ='" + repid + "'";

      if (logger.isDebugEnabled()) {
        logger.debug("stmtText: " + stmtText);
      }
      jdbcRS = stmt.executeQuery(stmtText);

      if (jdbcRS.next()) {
        volumeBean.setConInfo(jdbcRS.getString(1));
      }
      jdbcRS.close();
      stmt.close();
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    finally
    {
      try
      {
        if (conn != null)
        {
          conn.close();
          if (logger.isDebugEnabled())
          {
            logger.debug("Sesión cerrada.");
          }
        }
        if (jdbcRS != null) jdbcRS.close();
        if (stmt != null) stmt.close();
        if (contextName != null) contextName.close();

      }
      catch (Exception ee)
      {
        logger.error(ee);
      }

    }

    return volumeBean;
  }
}
