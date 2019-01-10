package es.dipucr.sigem.anularCircuitoFirmas.sign;

import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitInstanceDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitMgr;

public class SignCircuitAnular extends SignCircuitMgr {

	private IClientContext mcontext = null;

	public SignCircuitAnular(IClientContext clientContext) {
		super(clientContext);
		this.mcontext = clientContext;
	}

	public boolean anularCircuito(int circuitoId) {
		boolean correcto = false;
		DbCnt cnt = null;
		try {
			cnt = mcontext.getConnection();

			String sql = "WHERE ID_INSTANCIA_CIRCUITO = " + circuitoId;
			CollectionDAO objlist = new CollectionDAO(SignCircuitInstanceDAO.class);
			objlist.delete(cnt, sql);
			correcto = true;
		} catch (Exception e) {
			correcto = false;
		} finally {
			mcontext.releaseConnection(cnt);
		}
		
		return correcto;
	}
}
