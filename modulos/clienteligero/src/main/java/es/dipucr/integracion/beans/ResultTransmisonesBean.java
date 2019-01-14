package es.dipucr.integracion.beans;

import java.util.ArrayList;
import java.util.List;

public class ResultTransmisonesBean {
	
	private List<ResultDatosGenericosBean> transmisionesBeanList = null;
	
	public void ResultTransmisionesBean() {
		this.transmisionesBeanList = new ArrayList<ResultDatosGenericosBean>();
	}

	
	
	
	public List<ResultDatosGenericosBean> getTransmisionesBeanList() {
		return transmisionesBeanList;
	}

	public void setTransmisionesBeanList(
			List<ResultDatosGenericosBean> transmisionesBeanList) {
		this.transmisionesBeanList = transmisionesBeanList;
	}
	
	
	
	

}
