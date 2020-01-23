package es.dipucr.contratacion.objeto.sw;

public class Lotes{
    private String detallePosibilidadAdjudicacion;
    private Lote[] lotes;
    private int numeroMaximoAdjudicacion;
    private int numeroMaximoPresentacion;
    private Campo seDebeOfertar;
    private boolean tieneLotes;
    private int numLotes;
	public String getDetallePosibilidadAdjudicacion() {
		return detallePosibilidadAdjudicacion;
	}
	public void setDetallePosibilidadAdjudicacion(
			String detallePosibilidadAdjudicacion) {
		this.detallePosibilidadAdjudicacion = detallePosibilidadAdjudicacion;
	}
	public Lote[] getLotes() {
		return lotes;
	}
	public void setLotes(Lote[] lotes) {
		this.lotes = lotes;
	}
	public int getNumeroMaximoAdjudicacion() {
		return numeroMaximoAdjudicacion;
	}
	public void setNumeroMaximoAdjudicacion(int numeroMaximoAdjudicacion) {
		this.numeroMaximoAdjudicacion = numeroMaximoAdjudicacion;
	}
	public int getNumeroMaximoPresentacion() {
		return numeroMaximoPresentacion;
	}
	public void setNumeroMaximoPresentacion(int numeroMaximoPresentacion) {
		this.numeroMaximoPresentacion = numeroMaximoPresentacion;
	}
	public Campo getSeDebeOfertar() {
		return seDebeOfertar;
	}
	public void setSeDebeOfertar(Campo seDebeOfertar) {
		this.seDebeOfertar = seDebeOfertar;
	}
	public boolean isTieneLotes() {
		return tieneLotes;
	}
	public void setTieneLotes(boolean tieneLotes) {
		this.tieneLotes = tieneLotes;
	}
	public int getNumLotes() {
		return numLotes;
	}
	public void setNumLotes(int numLotes) {
		this.numLotes = numLotes;
	}

}
