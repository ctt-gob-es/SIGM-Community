package es.dipucr.integracion.beans;


public class ResultBean {
	

	private String textoSalida = "";

	private ResultAtributosBean resultAtributosBean = null;
	
	private ResultTransmisonesBean resultTransionesBean = null;
	
	public ResultBean() {}


	
	
	
	
	
	
	public ResultAtributosBean getResultAtributosBean() {
		return resultAtributosBean;
	}


	public void setResultAtributosBean(ResultAtributosBean resultAtributosBean) {
		this.resultAtributosBean = resultAtributosBean;
	}

	
	
	

	public ResultTransmisonesBean getResultTransionesBean() {
		return resultTransionesBean;
	}








	public void setResultTransionesBean(ResultTransmisonesBean resultTransionesBean) {
		this.resultTransionesBean = resultTransionesBean;
	}








	public String getTextoSalida() {
		return textoSalida;
	}

	public void setTextoSalida(String textoSalida) {
		this.textoSalida = textoSalida;
	}


	

	
	
}
