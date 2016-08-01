package ieci.tdw.ispac.ispacweb.api.impl.states;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class ProcessListState extends BaseState {

	private static final String FIELD_IDPCD = "ID_PCD";

	public ProcessListState(String stateticket) throws ISPACException {
		super(stateticket);
	} 
	

	public ProcessListState (StateContext stateContext) {
		super(stateContext);
	}
	public ProcessListState (int state, Map params,
			IClientContext cct) throws ISPACException {
		String[] stagePcdIdstr = (String[])params.get(ManagerState.PARAM_STAGEPCDID);
		int stagePcdId = 0;
		
		//[eCenpri-Manu Ticket #131] - INICIO - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
		int iAnio = 0;

		try
        {
            if (stagePcdIdstr != null)
            	stagePcdId = Integer.parseInt(stagePcdIdstr[stagePcdIdstr.length-1]);
        } catch (NumberFormatException e)
        {
            throw new ISPACException("ProcessListState: Parámetros incorrectos.",e);
        }
		
		//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
		String[] anios = (String[]) params.get("anio");
		if(anios != null){
			String anio = anios[0];
			if(StringUtils.isNotEmpty(anio) && StringUtils.isNumeric(anio)){
				iAnio = Integer.parseInt(anio);
			}
		}
		
		getStateContext(state, stagePcdId, iAnio, cct);
		//[eCenpri-Manu Ticket #131] - FIN - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
	}

	/**
	 * Obtiene el stateContext correcto
	 * @param state identificador del estado
	 * @param stagePcdId identificador de la fase en el procedimiento
	 * @param iAnio Filtro de año de inicio de expediente
	 * @param cct ClientContext
	 * @throws ISPACException
	 */
	private void getStateContext(int state,
			int stagePcdId, int iAnio, IClientContext cct) throws ISPACException {
		IItem item = cct.getAPI().getProcedureStage(stagePcdId);
		int pcdId = item.getInt(FIELD_IDPCD);
		mStateContext = new StateContext();
		mStateContext.setState(state);
		mStateContext.setPcdId(pcdId);
		mStateContext.setStagePcdId(stagePcdId);

		//[eCenpri-Manu Ticket #131] - ALSIGM3 Filtrar el área de trabajo por año de inicio de expediente.
		mStateContext.setAnio(iAnio);
	}

	public void exit() {
	}

	public void refresh() {
	}

	public void enter() {
	}

	public String getLabel() {
		return ManagerState.LABEL_PROCESSLIST;
	}

	public String getQueryString() {
		return "?" + ManagerState.PARAM_STAGEPCDID + "=" + getStagePcdId();
	}

	public boolean equals(IState iState) {
		boolean result = super.equals(iState);
		if (result) {
			if (getStagePcdId() != iState.getStagePcdId())
				result = false;
		}
		return result;
	}
}
