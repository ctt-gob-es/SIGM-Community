package es.dipucr.tablonEdictalUnico.quartz.dao;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.tablonEdictalUnico.commons.Constantes;
import es.dipucr.tablonEdictalUnico.quartz.bean.TablonEdictalBoeDatos;

public class TablonEdictalBoeDatosDAO extends GenericoDAO{
	
	private static final Logger logger = Logger.getLogger(TablonEdictalBoeDatosDAO.class);	
	
	public Vector<TablonEdictalBoeDatos> consultarAnunciosSinRecibir(String entidad) throws ISPACRuleException{
		logger.warn("Entrando en la consulta de anuncios sin Recibir de la entidad. "+entidad);
		Vector<TablonEdictalBoeDatos> anunciosSinEnviar = new Vector<TablonEdictalBoeDatos>();
		try 
		{
			//Connection con = this.getConexion(entidad, Constantes.BBDD_TRAMITADOR);
			Connection con = this.getConexion(entidad, Constantes.BBDD_TRAMITADOR);
			StringBuffer query = new StringBuffer();
			query.append("select * from tablon_edictal_boe_datos where traidoanuncioteu='NO'");
			ResultSet r = null;
			Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			try{
				r = sentencia.executeQuery(query.toString());
				if(r!=null)
				{
					while(r.next())
					{
						TablonEdictalBoeDatos teu = new TablonEdictalBoeDatos();
						teu.setNumexp(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_NUMEXP));
						teu.setEmailIncidencias(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_EMAIL_INCIDENCIAS));
						teu.setFormaPublicacion(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_FORMA_PUBLICACION));
						teu.setDatospersonales(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_DATOSPERSONALES));
						teu.setTipoanuncio(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_TIPOANUNCIO));
						teu.setLgt(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_LGT));
						teu.setProcedimiento(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_PROCEDIMIENTO));
						teu.setLugar(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_LUGAR));
						teu.setCargonombre(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_CARGONOMBRE));
						teu.setFechapublicacion(r.getDate(Constantes.TABLON_EDICTAL_BOE_DATOS_FECHAPUBLICACION));
						teu.setFechafirma(r.getDate(Constantes.TABLON_EDICTAL_BOE_DATOS_FECHAFIRMA));
						teu.setIdentificadorboe(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_IDENTIFICADORBOE));
						teu.setIdentificadoranuncioboe(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_IDENTIFICADORANUNCIOBOE));
						teu.setTraidoanuncioteu(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_TRAIDOANUNCIOTEU));
						teu.setId_proc(r.getInt(Constantes.TABLON_EDICTAL_BOE_DATOS_ID_PROC));
						teu.setUid_destinatario(r.getString(Constantes.TABLON_EDICTAL_BOE_DATOS_UID_DESTINATARIO));
						anunciosSinEnviar.add(teu);					
					}
								
				}			
				if(r!=null)
					r.close();
				r=null;
				if(sentencia!=null)
					sentencia.close();
				sentencia=null;
				if(con!=null)
					con.close();
				con=null;
			}
			catch(SQLException e) {
				logger.error("Error en la entidad. "+entidad+", con el siguiente error. "+e.getMessage(), e);
			}
			
		}catch (Exception e) {
			logger.error("Error en la entidad. "+entidad+", con el siguiente error. "+e.getMessage(), e);
		}
		
		logger.warn("Saliendo de la entidad. "+entidad+"con el numero de anuncios sin enviar. "+anunciosSinEnviar.size());
		return anunciosSinEnviar;
		
	}

}
