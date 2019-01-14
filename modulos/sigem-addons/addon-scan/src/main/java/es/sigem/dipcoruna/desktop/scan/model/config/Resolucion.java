package es.sigem.dipcoruna.desktop.scan.model.config;

public enum Resolucion {
	ALTA (400), MEDIA (300), BAJA (200);

	//Dots Per Inch
	private final int dpi;

	private Resolucion (final int dpi) {
		this.dpi = dpi;
	}

	public int getDpi() {
		return dpi;
	}
}
