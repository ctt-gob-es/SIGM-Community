package es.msssi.tecdoc.fwktd.dir.ws.object;

import java.io.Serializable;

/**
 * Clase que implementa un criterio de búsqueda para las UO.
 *
 * @author cmorenog
 *
 */
public class CriterioUO implements Serializable{
	private static final long serialVersionUID = 5164807099767853323L;

	/**
	 * Nombre del criterio
	 */
	private CriterioUOEnum nombre = null;

	/**
	 * Operador
	 */
	private OperadorCriterioUOEnum operador = OperadorCriterioUOEnum.EQUAL;

	/**
	 * Valor
	 */
	private String valor = null;

	/**
	 * Constructor.
	 */
	public CriterioUO() {
	}

	/**
	 * Constructor.
	 * @param nombre Nombre del criterio.
	 * @param valor Valor.
	 */
	public CriterioUO(CriterioUOEnum nombre, String valor) {
		this(nombre, OperadorCriterioUOEnum.EQUAL, valor);
	}

	/**
	 * Constructor.
	 * @param nombre Nombre del criterio.
	 * @param operador Operador.
	 * @param valor Valor.
	 */
	public CriterioUO(CriterioUOEnum nombre, OperadorCriterioUOEnum operador, String valor) {
		this();
		setNombre(nombre);
		setOperador(operador);
		setValor(valor);
	}
	/**
	 * Obtiene el valor del parámetro nombre.
	 * 
	 * @return nombre valor del campo a obtener.
	 *
	 */
	public CriterioUOEnum getNombre() {
		return nombre;
	}
	
	/**
	 * Guarda el valor del parámetro nombre.
	 * 
	 * @param nombre
	 *            valor del campo a guardar.
	 */
	public void setNombre(CriterioUOEnum nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Obtiene el valor del parámetro operador.
	 * 
	 * @return operador valor del campo a obtener.
	 *
	 */
	public OperadorCriterioUOEnum getOperador() {
		return operador;
	}
	
	/**
	 * Guarda el valor del parámetro operador.
	 * 
	 * @param operador
	 *            valor del campo a guardar.
	 */
	public void setOperador(OperadorCriterioUOEnum operador) {
		this.operador = operador;
	}
	
	/**
	 * Obtiene el valor del parámetro valor.
	 * 
	 * @return valor valor del campo a obtener.
	 *
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * Guarda el valor del parámetro valor.
	 * 
	 * @param valor
	 *            valor del campo a guardar.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	
}
