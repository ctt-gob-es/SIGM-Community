package es.sigem.dipcoruna.desktop.scan.model.config;

public class ScanProfile {
    private String nombreDispositivo;
	private String nombre;
	private Resolucion resolucion;
	private FormatoColor formatoColor;
	private FormatoFichero formatoFichero = FormatoFichero.PDF;
	private boolean duplex;
	private boolean cargaAutomatica;

	public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(final String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public Resolucion getResolucion() {
		return resolucion;
	}

	public void setResolucion(final Resolucion resolucion) {
		this.resolucion = resolucion;
	}

	public FormatoColor getFormatoColor() {
		return formatoColor;
	}

	public void setFormatoColor(final FormatoColor formatoColor) {
		this.formatoColor = formatoColor;
	}

	public FormatoFichero getFormatoFichero() {
		return formatoFichero;
	}

	public void setFormatoFichero(final FormatoFichero formatoFichero) {
		this.formatoFichero = formatoFichero;
	}

    public boolean isDuplex() {
        return duplex;
    }

    public void setDuplex(final boolean duplex) {
        this.duplex = duplex;
    }

    public boolean isCargaAutomatica() {
        return cargaAutomatica;
    }

    public void setCargaAutomatica(final boolean cargaAutomatica) {
        this.cargaAutomatica = cargaAutomatica;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScanProfile other = (ScanProfile) obj;
        if (nombre == null) {
            if (other.nombre != null) {
                return false;
            }
        }
        else if (!nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "ScanProfile [nombreDispositivo=" + nombreDispositivo + ", nombre=" + nombre + ", resolucion=" + resolucion + ", formatoColor=" + formatoColor
				+ ", formatoFichero=" + formatoFichero + ", duplex=" + duplex + ", cargaAutomatica=" + cargaAutomatica + "]";
	}
}
