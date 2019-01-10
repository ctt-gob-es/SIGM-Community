package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.ObjectDAO;
import ieci.tdw.ispac.ispaclib.dao.historico.TXHitoHDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class HitosUtils {

	private static Logger logger = Logger.getLogger(HitosUtils.class);
	
	public static IItemCollection getHitos(IClientContext cct, String numexp) throws ISPACException{
		IItemCollection resultado = null;
		int procedureId = Integer.MIN_VALUE;
		
		try{
			DbCnt cnt = cct.getConnection();
			resultado = TXHitoDAO.getMilestones(cnt, numexp).disconnect();
			
			if(resultado == null || resultado.toList().size() == 0){
				cnt = cct.getConnection();
				resultado = TXHitoHDAO.getMilestones(cnt, numexp).disconnect();
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar los hitos del expediente: " + numexp + "(procedureId=" + procedureId + "). " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los hitos del expediente: " + numexp + "(procedureId=" + procedureId + "). " + e.getMessage(), e);
		}

		return resultado;
	}
	
	public static IItemCollection getHitos(IClientContext cct, int procedureId) throws ISPACException{
		IItemCollection resultado = null;
		
		try{
			DbCnt cnt = cct.getConnection();
			resultado = TXHitoDAO.getMilestones(cnt, procedureId).disconnect();
			
			if(resultado == null || resultado.toList().size() == 0){
				cnt = cct.getConnection();
				resultado = TXHitoHDAO.getMilestones(cnt, procedureId).disconnect();
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar los hitos del expediente con id: " + procedureId + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los hitos del expediente con id: " + procedureId + ". " + e.getMessage(), e);
		}

		return resultado;
	}
	
	public static IItemCollection getHitos(IClientContext cct, int procedureId, String query) throws ISPACException{
		IItemCollection resultado = null;
		
		try{
			DbCnt cnt = cct.getConnection();
			resultado = TXHitoDAO.getMilestones(cnt, procedureId, query).disconnect();
			
			if(resultado == null || resultado.toList().size() == 0){
				cnt = cct.getConnection();
				resultado = TXHitoHDAO.getMilestones(cnt, procedureId, query).disconnect();
			}
		}
		catch(Exception e){
			logger.error("Error al recuperar los hitos del expediente con id: " + procedureId + ". " + e.getMessage(), e);
			throw new ISPACException("Error al recuperar los hitos del expediente con id: " + procedureId + ". " + e.getMessage(), e);
		}

		return resultado;
	}
	
	/**
	 * Copia los hitos del expediente indicado en la tabla pasada por parámetro
	 * 
	 */
	public static boolean copiaHitos(IClientContext cct, String tabla, IItem hito_viejo, String numexp) throws Exception{
		
		ObjectDAO hito_nuevo = null;
		
		DbCnt cnt = cct.getConnection();
		if(Constants.TABLASBBDD.SPAC_HITOS.equals(tabla))
			hito_nuevo = new TXHitoDAO(cnt);
		else
		    hito_nuevo = new TXHitoHDAO(cnt);
		
		hito_nuevo.createNew(cnt);		
		hito_nuevo.set("ID", hito_viejo.getInt("ID"));
		hito_nuevo.set("ID_EXP", hito_viejo.getInt("ID_EXP"));
		hito_nuevo.set("ID_EXP", hito_viejo.getInt("ID_EXP"));
		hito_nuevo.set("ID_FASE", hito_viejo.getInt("ID_FASE"));
		hito_nuevo.set("ID_TRAMITE", hito_viejo.getInt("ID_TRAMITE"));
		hito_nuevo.set("HITO", hito_viejo.getInt("HITO"));
		hito_nuevo.set("FECHA", hito_viejo.getDate("FECHA"));
		hito_nuevo.set("FECHA_LIMITE", hito_viejo.getDate("FECHA_LIMITE"));
		hito_nuevo.set("INFO", hito_viejo.getString("INFO"));
		hito_nuevo.set("AUTOR", hito_viejo.getString("AUTOR"));
		hito_nuevo.set("DESCRIPCION", hito_viejo.getString("DESCRIPCION"));
		if(hito_viejo.getInt("ID_INFO") != Integer.MIN_VALUE)
			hito_nuevo.set("ID_INFO", hito_viejo.getInt("ID_INFO"));
		
		try{
			hito_nuevo.store(cct);
		}
		catch(Exception e){
			try{
				hito_nuevo.createNew(cnt);		
				hito_nuevo.set("ID_EXP", hito_viejo.getInt("ID_EXP"));
				hito_nuevo.set("ID_EXP", hito_viejo.getInt("ID_EXP"));
				hito_nuevo.set("ID_FASE", hito_viejo.getInt("ID_FASE"));
				hito_nuevo.set("ID_TRAMITE", hito_viejo.getInt("ID_TRAMITE"));
				hito_nuevo.set("HITO", hito_viejo.getInt("HITO"));
				hito_nuevo.set("FECHA", hito_viejo.getDate("FECHA"));
				hito_nuevo.set("FECHA_LIMITE", hito_viejo.getDate("FECHA_LIMITE"));
				hito_nuevo.set("INFO", hito_viejo.getString("INFO"));
				hito_nuevo.set("AUTOR", hito_viejo.getString("AUTOR"));
				hito_nuevo.set("DESCRIPCION", hito_viejo.getString("DESCRIPCION"));
				if(hito_viejo.getInt("ID_INFO") != Integer.MIN_VALUE)
					hito_nuevo.set("ID_INFO", hito_viejo.getInt("ID_INFO"));
				
				hito_nuevo.store(cct);
				return true;
			}
			catch(Exception e1){
				logger.warn("No insertamos el hito en historicos. " + e1.getMessage(), e1);
				return false;
			}
		} finally {
			cct.releaseConnection(cnt);
		}
		
		return true;
	}
}
