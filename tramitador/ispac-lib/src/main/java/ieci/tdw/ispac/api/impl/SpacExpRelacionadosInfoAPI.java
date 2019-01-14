package ieci.tdw.ispac.api.impl;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.ISpacExpRelacionadosInfoAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.procedure.PSpacExpRelacionadosInfoDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class SpacExpRelacionadosInfoAPI implements ISpacExpRelacionadosInfoAPI {
	
	protected Logger LOGGER = Logger.getLogger(SpacExpRelacionadosInfoAPI.class);
	
	/**
	 * Contexto de cliente.
	 */
    private ClientContext mcontext;

    /**
     * Constructor.
     * @param context Contexto de cliente
     */
    public SpacExpRelacionadosInfoAPI(ClientContext context) {
        mcontext = context;
    }

	public ClientContext getClientContext() {
		return mcontext;
	}
	
	public IItem getInfoSpacExpRelacionaPadreHijo(String numexp_padre, String numexp_hijo) throws ISPACRuleException{
		ItemBean resultado = null;
		try {
			PSpacExpRelacionadosInfoDAO expRela = PSpacExpRelacionadosInfoDAO.getInfoSpacExpRelacionaPadreHijo(mcontext.getConnection(), numexp_padre, numexp_hijo);
			if(expRela!=null){
				resultado = new ItemBean();
				if(expRela.getId() >0) resultado.setProperty("ID", expRela.getId());
				if(StringUtils.isNotEmpty(expRela.getNumexpPadre()))resultado.setProperty("NUMEXP_PADRE", expRela.getNumexpPadre());
				if(StringUtils.isNotEmpty(expRela.getNumexpHijo()))resultado.setProperty("NUMEXP_HIJO", expRela.getNumexpHijo());
				if(expRela.getIdTramitePadre() >0) resultado.setProperty("ID_TRAMITE_PADRE", expRela.getIdTramitePadre());
				if(expRela.getIdTramiteHijo() >0) resultado.setProperty("ID_TRAMITE_HIJO", expRela.getIdTramiteHijo());
				if(StringUtils.isNotEmpty(expRela.getObservaciones()))resultado.setProperty("OBSERVACIONES", expRela.getObservaciones());
				return resultado.getItem();
			}
		} catch (ISPACException e) {
			LOGGER.error(" Error - "+e.getMessage(),e);
			throw new ISPACRuleException(" Error - "+e.getMessage(),e);
		}
		return null;
	}
	
	public IItem getInfoSpacExpRelacionaPadreIdTramitePadre(String numexp_padre, int id_tramite_padre) throws ISPACRuleException{
		try {
			IItem expRela = PSpacExpRelacionadosInfoDAO.getInfoSpacExpRelacionaPadreIdTramitePadre(mcontext.getConnection(), numexp_padre, id_tramite_padre);
			if(expRela!=null){
				return expRela;
			}
		} catch (ISPACException e) {
			LOGGER.error(" Error - "+e.getMessage(),e);
			throw new ISPACRuleException(" Error - "+e.getMessage(),e);
		}
		return null;
	}
	
	public IItem getInfoSpacExpRelacionaHijoIdTramiteHijo(String numexp_hijo, int id_tramite_hijo) throws ISPACRuleException{
		try {
			IItem expRela = PSpacExpRelacionadosInfoDAO.getInfoSpacExpRelacionaHijoIdTramiteHijo(mcontext.getConnection(), numexp_hijo, id_tramite_hijo);
			if(expRela!=null){
				return expRela;
			}
		} catch (ISPACException e) {
			LOGGER.error(" Error - "+e.getMessage(),e);
			throw new ISPACRuleException(" Error - "+e.getMessage(),e);
		}
		return null;
	}
	
	public void addSpacExpRelacionadosInfo(String numexp_padre, String numexp_hijo, int id_tramite_padre, int id_tramite_hijo, String observaciones) throws ISPACRuleException{
		DbCnt cnt = null;
		try {
			cnt = mcontext.getConnection();
			PSpacExpRelacionadosInfoDAO.insert(cnt, numexp_padre, numexp_hijo, id_tramite_padre, id_tramite_hijo, observaciones);
		} catch (ISPACException e) {
			LOGGER.error(" Error - "+e.getMessage(),e);
			throw new ISPACRuleException(" Error - "+e.getMessage(),e);
		} finally {
			mcontext.releaseConnection(cnt);
		}
	}
	
	public void deleteSpacExpRelacionadosInfoByPadreHijo(int identificadorSpacExpRelacInfo) throws ISPACRuleException{
		DbCnt cnt = null;
		try {
			cnt = mcontext.getConnection();
			if(identificadorSpacExpRelacInfo>0){
				PSpacExpRelacionadosInfoDAO.delete(cnt, identificadorSpacExpRelacInfo);
			}
		} catch (ISPACException e) {
			LOGGER.error(" Error - "+e.getMessage(),e);
			throw new ISPACRuleException(" Error - "+e.getMessage(),e);
		} finally {
			mcontext.releaseConnection(cnt);
		}
	}
}
