/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOrg;

/**
 * Bean con el formulario de búsqueda de distribución.
 * 
 * @author cmorenog
 * */
public class SearchDistributionBean
		extends GenericBean {
	
	private static final long serialVersionUID = 1L;
	private static final int INDICE = 9;
	private static final String TXTDELETE = "(1=1)";
	
	/**
	 * tipo distribucion.
	 * 2: salida
	 * 1: entrada
	 * */
	private int type = 1;
	/**
	 * estado.
	 * 0: Nada
	 * 1: Pendiente
	 * 2: Aceptado
	 * 3: Archivado
	 * 4: Rechazado
	 * 5: Redistribuido
	 * 6: Pendiente de distribuir (solo salida)
	 * */
	private Integer state = 1;
	
	/** fecha desde de distribución. */
	private Date distDateDesde;
	/** fecha hasta de distribución. */
	private Date distDateHasta;
	
	/** fecha desde estado. */
	private Date stateDateDesde;
	/** fecha hasta estado. */
	private Date stateDateHasta;
	/** origen de la distribucion. Solo entrada. */
	private String origen;
	/** Tipo origen de la distribucion. Solo entrada. */
	private Integer tipoOrigen;
	/** destino de la distribucion. Solo salida. */
	private String destino;
	/** Tipo destino de la distribucion. Solo salida. */
	private Integer tipoDestino;
	/** Numero registro. */
	private String fld1;
	/** fecha desde registro. */
	private Date fld2Desde;
	/** fecha hasta registro. */
	private Date fld2Hasta;
	/** Origen. */
	private ScrOrg fld7;
	/** Destino. */
	private ScrOrg fld8;
	/** Remitentes. */
	private String fld9;
	/** Tipo de Asunto. */
	private ScrCa fld16;
	/** Resumen. */
	private String fld17;
	/** valor Tipo de relación de registros para informes. */
	private int reportTypeValue = 5;
	/** Indicador de inclusión de diligencias. */
	private boolean includeProceedingValue;	
	
	/**
	 * Obtiene el valor del parámetro type.
	 * 
	 * @return type valor del campo a obtener.
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Guarda el valor del parámetro type.
	 * 
	 * @param type
	 *            valor del campo a guardar.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Obtiene el valor del parámetro state.
	 * 
	 * @return state valor del campo a obtener.
	 */
	public Integer getState() {
		return state;
	}
	
	/**
	 * Guarda el valor del parámetro state.
	 * 
	 * @param state
	 *            valor del campo a guardar.
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	
	/**
	 * Obtiene el valor del parámetro distDateDesde.
	 * 
	 * @return distDateDesde valor del campo a obtener.
	 */
	public Date getDistDateDesde() {
		return distDateDesde;
	}
	
	/**
	 * Guarda el valor del parámetro distDateDesde.
	 * 
	 * @param distDateDesde
	 *            valor del campo a guardar.
	 */
	public void setDistDateDesde(Date distDateDesde) {
		this.distDateDesde = distDateDesde;
	}
	
	/**
	 * Obtiene el valor del parámetro distDateHasta.
	 * 
	 * @return distDateHasta valor del campo a obtener.
	 */
	public Date getDistDateHasta() {
		return distDateHasta;
	}
	
	/**
	 * Guarda el valor del parámetro distDateHasta.
	 * 
	 * @param distDateHasta
	 *            valor del campo a guardar.
	 */
	public void setDistDateHasta(Date distDateHasta) {
		this.distDateHasta = distDateHasta;
	}
	
	/**
	 * Obtiene el valor del parámetro stateDateDesde.
	 * 
	 * @return stateDateDesde valor del campo a obtener.
	 */
	public Date getStateDateDesde() {
		return stateDateDesde;
	}
	
	/**
	 * Guarda el valor del parámetro stateDateDesde.
	 * 
	 * @param stateDateDesde
	 *            valor del campo a guardar.
	 */
	public void setStateDateDesde(Date stateDateDesde) {
		this.stateDateDesde = stateDateDesde;
	}
	
	/**
	 * Obtiene el valor del parámetro stateDateHasta.
	 * 
	 * @return stateDateHasta valor del campo a obtener.
	 */
	public Date getStateDateHasta() {
		return stateDateHasta;
	}
	
	/**
	 * Guarda el valor del parámetro stateDateHasta.
	 * 
	 * @param stateDateHasta
	 *            valor del campo a guardar.
	 */
	public void setStateDateHasta(Date stateDateHasta) {
		this.stateDateHasta = stateDateHasta;
	}
	
	/**
	 * Obtiene el valor del parámetro origen.
	 * 
	 * @return origen valor del campo a obtener.
	 */
	public String getOrigen() {
		return origen;
	}
	
	/**
	 * Guarda el valor del parámetro origen.
	 * 
	 * @param origen
	 *            valor del campo a guardar.
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	/**
	 * Obtiene el valor del parámetro tipoOrigen.
	 * 
	 * @return tipoOrigen valor del campo a obtener.
	 */
	public Integer getTipoOrigen() {
		return tipoOrigen;
	}
	
	/**
	 * Guarda el valor del parámetro tipoOrigen.
	 * 
	 * @param tipoOrigen
	 *            valor del campo a guardar.
	 */
	public void setTipoOrigen(Integer tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	
	/**
	 * Obtiene el valor del parámetro destino.
	 * 
	 * @return destino valor del campo a obtener.
	 */
	public String getDestino() {
		return destino;
	}
	
	/**
	 * Guarda el valor del parámetro destino.
	 * 
	 * @param destino
	 *            valor del campo a guardar.
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	/**
	 * Obtiene el valor del parámetro tipoDestino.
	 * 
	 * @return tipoDestino valor del campo a obtener.
	 */
	public Integer getTipoDestino() {
		return tipoDestino;
	}
	
	/**
	 * Guarda el valor del parámetro tipoDestino.
	 * 
	 * @param tipoDestino
	 *            valor del campo a guardar.
	 */
	public void setTipoDestino(Integer tipoDestino) {
		this.tipoDestino = tipoDestino;
	}
	
	/**
	 * Obtiene el valor del parámetro fld1.
	 * 
	 * @return fld1 valor del campo a obtener.
	 */
	public String getFld1() {
		return fld1;
	}
	
	/**
	 * Guarda el valor del parámetro fld1.
	 * 
	 * @param fld1
	 *            valor del campo a guardar.
	 */
	public void setFld1(String fld1) {
		this.fld1 = fld1;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2Desde.
	 * 
	 * @return fld2Desde valor del campo a obtener.
	 */
	public Date getFld2Desde() {
		return fld2Desde;
	}
	
	/**
	 * Guarda el valor del parámetro fld2Desde.
	 * 
	 * @param fld2Desde
	 *            valor del campo a guardar.
	 */
	public void setFld2Desde(Date fld2Desde) {
		this.fld2Desde = fld2Desde;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2Hasta.
	 * 
	 * @return fld2Hasta valor del campo a obtener.
	 */
	public Date getFld2Hasta() {
		return fld2Hasta;
	}
	
	/**
	 * Guarda el valor del parámetro fld2Hasta.
	 * 
	 * @param fld2Hasta
	 *            valor del campo a guardar.
	 */
	public void setFld2Hasta(Date fld2Hasta) {
		this.fld2Hasta = fld2Hasta;
	}
	
	/**
	 * Obtiene el valor del parámetro fld7.
	 * 
	 * @return fld7 valor del campo a obtener.
	 */
	public ScrOrg getFld7() {
		return fld7;
	}
	
	/**
	 * Guarda el valor del parámetro fld7.
	 * 
	 * @param fld7
	 *            valor del campo a guardar.
	 */
	public void setFld7(ScrOrg fld7) {
		this.fld7 = fld7;
	}
	
	/**
	 * Obtiene el valor del parámetro fld8.
	 * 
	 * @return fld8 valor del campo a obtener.
	 */
	public ScrOrg getFld8() {
		return fld8;
	}
	
	/**
	 * Guarda el valor del parámetro fld8.
	 * 
	 * @param fld8
	 *            valor del campo a guardar.
	 */
	public void setFld8(ScrOrg fld8) {
		this.fld8 = fld8;
	}
	
	/**
	 * Obtiene el valor del parámetro fld9.
	 * 
	 * @return fld9 valor del campo a obtener.
	 */
	public String getFld9() {
		return fld9;
	}
	
	/**
	 * Guarda el valor del parámetro fld9.
	 * 
	 * @param fld9
	 *            valor del campo a guardar.
	 */
	public void setFld9(String fld9) {
		this.fld9 = fld9;
	}
	
	/**
	 * Obtiene el valor del parámetro fld16.
	 * 
	 * @return fld16 valor del campo a obtener.
	 */
	public ScrCa getFld16() {
		return fld16;
	}
	
	/**
	 * Guarda el valor del parámetro fld16.
	 * 
	 * @param fld16
	 *            valor del campo a guardar.
	 */
	public void setFld16(ScrCa fld16) {
		this.fld16 = fld16;
	}
	
	/**
	 * Obtiene el valor del parámetro fld17.
	 * 
	 * @return fld17 valor del campo a obtener.
	 */
	public String getFld17() {
		return fld17;
	}
	
	/**
	 * Guarda el valor del parámetro fld17.
	 * 
	 * @param fld17
	 *            valor del campo a guardar.
	 */
	public void setFld17(String fld17) {
		this.fld17 = fld17;
	}
	
	/**
	 * Obtiene el valor del parámetro reportTypeValue.
	 * 
	 * @return reportTypeValue valor del campo a obtener.
	 */
	public int getReportTypeValue() {
		return reportTypeValue;
	}
	
	/**
	 * Guarda el valor del parámetro reportTypeValue.
	 * 
	 * @param reportTypeValue
	 *            valor del campo a guardar.
	 */
	public void setReportTypeValue(int reportTypeValue) {
		this.reportTypeValue = reportTypeValue;
	}

	/**
	 * Obtiene el valor del parámetro includeProceedingValue.
	 * 
	 * @return includeProceedingValue valor del campo a obtener.
	 */
	public boolean isIncludeProceedingValue() {
		return includeProceedingValue;
	}

	/**
	 * Guarda el valor del parámetro includeProceedingValue.
	 * 
	 * @param includeProceedingValue
	 *            valor del campo a guardar.
	 */
	public void setIncludeProceedingValue(boolean includeProceedingValue) {
		this.includeProceedingValue = includeProceedingValue;
	}
	
	/**
	 * Construye el where de la query de la búsqueda de distribución con los parámetros de
	 * la búsqueda de registro.
	 * 
	 * @return query de consulta
	 *         Devuelve null si no hay criterios de búsqueda de registro.
	 */
	public String fieldsRegtoQuery() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String query = TXTDELETE;
		String select = "SELECT DISTINCT 'S' FROM SCR_REGDIST WHERE ";
		if (!isNullString(this.fld1)) {
			query += " AND FLD1 LIKE '%" + fld1 + "%'";
		}
		// fld 2
		if (!isNull(this.fld2Desde) || !isNull(this.fld2Hasta)) {
			String fechaDesde;
			String fechaHasta;
			if (!isNull(this.fld2Desde) && !isNull(this.fld2Hasta)) {
				fechaDesde = format.format(this.fld2Desde);
				fechaHasta = format.format(this.fld2Hasta);
				query +=
						" AND FLD2 BETWEEN TO_DATE('" + fechaDesde
								+ "','DD-MM-YYYY HH24:MI:SS')"
								+ " AND TO_DATE('" + fechaHasta
								+ "','DD-MM-YYYY HH24:MI:SS')";
			}
			if (!isNull(this.fld2Desde) && isNull(this.fld2Hasta)) {
				fechaDesde = format.format(this.fld2Desde);
				query +=
						" AND FLD2 >= TO_DATE('" + fechaDesde
								+ "','DD-MM-YYYY HH24:MI:SS')";
			}
			if (isNull(this.fld2Desde) && !isNull(this.fld2Hasta)) {
				fechaHasta = format.format(this.fld2Hasta);
				query +=
						" AND FLD2 <= TO_DATE('" + fechaHasta
								+ "','DD-MM-YYYY HH24:MI:SS')";
			}
		}
		if (!isNull(this.fld7)) {
			query += " AND FLD7 =" + this.fld7.getId();
		}
		if (!isNull(this.fld8)) {
			query += " AND FLD8 =" + this.fld8.getId();
		}
		if (!isNullString(this.fld9)) {
		    	query += " AND fdrid in (select id_fdr from scr_regint where " +
		    			" translate(UPPER(name),'ÁÀÂÉÈÊÍÌÎÓÒÔÚÙÛ', 'AAAEEEIIIOOOUUU')" +
		    			" LIKE translate(UPPER('%"+fld9+"%'),'ÁÀÂÉÈÊÍÌÎÓÒÔÚÙÛ', 'AAAEEEIIIOOOUUU') and id_arch=1) ";
			//query += " AND FLD9 LIKE '%" + fld9 + "%'";
		}
		if (!isNull(this.fld16)) {
			query += " AND FLD16 =" + fld16.getId();
		}
		if (!isNullString(this.fld17)) {
			query += " AND FLD17 LIKE '%" + fld17 + "%'";
		}
		
		if (query.equals(TXTDELETE)) {
			query = null;
		}
		else {
			query = select + query.substring(INDICE);
		}
		return query;
	}
	
	/**
	 * Devuelve true si el objeto es nulo.
	 * 
	 * @param param
	 *            objeto.
	 * @return true si es null.
	 */
	private boolean isNull(Object param) {
		boolean result = true;
		if (param != null) {
			result = false;
		}
		return result;
	}
	
	/**
	 * Devuelve true si el string es nulo.
	 * 
	 * @param param
	 *            objeto.
	 * @return true si es null.
	 */
	private boolean isNullString(String param) {
		boolean result = true;
		if (param != null && !"".equals(param)) {
			result = false;
		}
		return result;
	}
	
	/**
	 * Construye el where de la query de la búsqueda de distribución con los parámetros de
	 * la búsqueda de distribución.
	 * 
	 * @return String con el query de la query.
	 *         Devuelve null si no hay criterios de búsqueda de distribución.
	 */
	public String fieldsDisttoQuery() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String query = TXTDELETE;
		if (this.state != null && !this.state.equals(Integer.valueOf(0))) {
			query += " AND STATE =" + String.valueOf(this.state);
		}
		// distDate
		if (this.distDateDesde != null || this.distDateHasta != null) {
			String distDateDesde;
			String distDateHasta;
			if (this.distDateDesde != null && this.distDateHasta != null) {
				distDateDesde = format.format(this.distDateDesde);
				distDateHasta = format.format(this.distDateHasta);
				query +=
						" AND DIST_DATE BETWEEN TO_DATE('" + distDateDesde
								+ "','DD-MM-YYYY HH24:MI')"
								+ " AND TO_DATE('" + distDateHasta
								+ "','DD-MM-YYYY HH24:MI')";
			}
			if (this.distDateDesde != null && this.distDateHasta == null) {
				distDateDesde = format.format(this.distDateDesde);
				query +=
						" AND DIST_DATE >= TO_DATE('" + distDateDesde
								+ "','DD-MM-YYYY HH24:MI')";
			}
			if (this.distDateDesde == null && this.distDateHasta != null) {
				distDateHasta = format.format(this.distDateHasta);
				query +=
						" AND DIST_DATE <= TO_DATE('" + distDateHasta
								+ "','DD-MM-YYYY HH24:MI')";
			}
		}
		// state_date
		if (this.stateDateDesde != null || this.stateDateHasta != null) {
			String stateDateDesde;
			String stateDateHasta;
			if (this.stateDateDesde != null && this.stateDateHasta != null) {
				stateDateDesde = format.format(this.stateDateDesde);
				stateDateHasta = format.format(this.stateDateHasta);
				query +=
						" AND STATE_DATE BETWEEN TO_DATE('" + stateDateDesde
								+ "','DD-MM-YYYY HH24:MI')"
								+ " AND TO_DATE('" + stateDateHasta
								+ "','DD-MM-YYYY HH24:MI')";
			}
			if (this.stateDateDesde != null && this.stateDateHasta == null) {
				stateDateDesde = format.format(this.stateDateDesde);
				query +=
						" AND STATE_DATE >= TO_DATE('" + stateDateDesde
								+ "','DD-MM-YYYY HH24:MI')";
			}
			if (this.stateDateDesde == null && this.stateDateHasta != null) {
				stateDateHasta = format.format(this.stateDateHasta);
				query +=
						" AND STATE_DATE <= TO_DATE('" + stateDateHasta
								+ "','DD-MM-YYYY HH24:MI')";
			}
		}
		
		if (this.type == 1) {
			if (this.origen != null && !this.origen.equals("") && this.tipoOrigen != null) {
				query +=
						" AND (TYPE_ORIG =" + this.tipoOrigen + " AND ID_ORIG = "
								+ this.origen + ") ";
			}
		}
		else {
			if (this.destino != null && !this.destino.equals("")
					&& this.tipoDestino != null) {
				query +=
						" AND (TYPE_DEST =" + this.tipoDestino + " AND ID_DEST = "
								+ this.destino + ") ";
			}
		}
		
		if (query.equals(TXTDELETE)) {
			query = null;
		}
		else {
			query = query.substring(INDICE);
		}
		return query;
	}
}
