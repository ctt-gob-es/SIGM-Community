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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.isicres.AxSfQueryField;
import com.ieci.tecdoc.common.isicres.Keys;

/**
 * Bean con el formulario de búsqueda de los registros de entrada.
 * 
 * @author cmorenog
 */
public class SearchInputRegisterBean
		extends GenericBean {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(SearchInputRegisterBean.class);
	
	/** combo número de registro. */
	private String fld1Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor número de registro. */
	private String fld1Value;
	/** valor número de registro. */
	private String fld1ValueHasta;
	
	/** combo fecha de registro. */
	private String fld2Select = Keys.QUERY_BETWEEN_TEXT_VALUE;
	/** valor fecha de registro. */
	private List<Date> fld2Value;
	/** valor desde fecha de registro. */
	private Date fld2ValueDesde;
	/** valor hasta fecha de registro. */
	private Date fld2ValueHasta;
	
	/** combo usuario. */
	private String fld3Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor usuario. */
	private String fld3Value;
	
	/** combo Fecha de Trabajo. */
	private String fld4Select = Keys.QUERY_BETWEEN_TEXT_VALUE;
	/** valor Fecha de Trabajo. */
	private List<Date> fld4Value;
	/** valor desde Fecha de Trabajo. */
	private Date fld4ValueDesde;
	/** valor hasta Fecha de Trabajo. */
	private Date fld4ValueHasta;
	
	/** combo Oficina de Registro. */
	private String fld5Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor Oficina de Registro. */
	private String fld5Value;
	
	/** combo Estado. */
	private String fld6Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor Estado. */
	private String fld6Value;
	
	/** combo Origen. */
	private String fld7Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor Origen. */
	private ScrOrg fld7Value;
	
	/** combo Destino. */
	private String fld8Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor Destino. */
	private ScrOrg fld8Value;
	
	/** combo Remitentes. */
	private String fld9Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor Remitentes. */
	private String fld9Value;
	
	/** Numero de registro original. */
	private String fld10Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor de registro original. */
	private String fld10Value;
	/** combo Tipos de Transporte. */
	private String fld14Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** combo Tipos de Transporte. */
	private String fld14Value;
	
	/** combo Numero de Transporte. */
	private String fld15Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor Numero de Transporte. */
	private String fld15Value;
	
	/** combo Tipos de Asunto. */
	private String fld16Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor Tipos de Asunto. */
	private ScrCa fld16Value;
	
	/** combo Resumen. */
	private String fld17Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor Resumen. */
	private String fld17Value;
	
	/** combo Ref.Expediente. */
	private String fld19Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor Ref.Expediente. */
	private String fld19Value;
	
	/** valor Tipo de relación de registros para informes. */
	private int reportTypeValue = 5;
	/** Indicador de inclusión de diligencias. */
	private boolean includeProceedingValue;
	
	/** check es sir. */
	private String fld503Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor fld503. */
	private String fld503Value;
	
	/** combo imp. */
	private String fld1001Select = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor impreso. */
	private String fld1001Value;

	/** combo tipo documento. */
	private String interesadoTipoDocSelect = Keys.QUERY_EQUAL_TEXT_VALUE;
	/** valor impreso. */
	private String interesadoTipoDocValue;

	/** combo comentario. */
	private String fld18Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor comentario. */
	private String fld18Value;
	/** check flag. */
	private String fld1002Select = Keys.QUERY_NOT_EQUAL_TEXT_VALUE;
	/** valor flag. */
	private Boolean fld1002Value;
	/** combo observaciones. */
	private String fld507Select = Keys.QUERY_LIKE_TEXT_VALUE;
	/** valor comentario. */
	private String fld507Value;
	/** check rojo. */
	private String fld504Select = Keys.QUERY_NOT_EQUAL_TEXT_VALUE;
	/** valor rojo. */
	private Boolean fld504Value = true;
	/** check naranjas. */
	private String fld505Select = Keys.QUERY_NOT_EQUAL_TEXT_VALUE;
	/** valor naranjas. */
	private Boolean fld505Value = true;
	/** check verdes. */
	private String fld506Select = Keys.QUERY_NOT_EQUAL_TEXT_VALUE;
	/** valor verdes. */
	private Boolean fld506Value = true;
	
	/**
	 * Mapea un campo del formulario de búsqueda al objeto que el core sigem
	 * requiere para la búsqueda.
	 * 
	 * @param fld
	 *            Campo a mapear.
	 * @param idbook
	 *            libro en sesión.
	 * 
	 * @return objeto mapeado con el campo introducido.
	 *         Devuelve null si el campo es vacio o da un error.
	 */
	@SuppressWarnings("unchecked")
	public AxSfQueryField fieldtoQuery(String fld, Integer idbook) {
		LOG.trace("Entrando en SearchInputRegisterBean.fieldtoQuery()");
		Object valor = null;
		String operador = null;
		AxSfQueryField queryField = null;
		try {
			if ("fld2".equals(fld) || "fld4".equals(fld)) {
				Object valorDesde =
						SearchInputRegisterBean.class
								.getDeclaredField(fld + "ValueDesde").get(this);
				Object valorHasta =
						SearchInputRegisterBean.class
								.getDeclaredField(fld + "ValueHasta").get(this);
				if (valorDesde != null && valorHasta == null) {
					valor =
							SearchInputRegisterBean.class.getDeclaredField(
									fld + "ValueDesde").get(this);
					operador = Keys.QUERY_GREATER_EQUAL_TEXT_VALUE;
				}
				else {
					if (valorDesde != null && valorHasta != null) {
						valor = new ArrayList<Date>();
						((List<Date>) valor).add((Date) valorDesde);
						((List<Date>) valor).add((Date) valorHasta);
						operador = Keys.QUERY_BETWEEN_TEXT_VALUE;
					}
					else {
						if (valorDesde == null && valorHasta != null) {
							valor =
									SearchInputRegisterBean.class.getDeclaredField(
											fld + "ValueHasta").get(this);
							operador = Keys.QUERY_LESSER_EQUAL_TEXT_VALUE;
						}
					}
				}
			}
			else {
				if ("fld16".equals(fld)) {
					try {
						valor =
								((ScrCa) SearchInputRegisterBean.class.getDeclaredField(
										fld + "Value").get(this)).getId();
					}
					catch (NullPointerException e) {
						throw e;
					}
					operador =
							(String) SearchInputRegisterBean.class.getDeclaredField(
									fld + "Select").get(this);
				}
				else {
					if ("fld7".equals(fld) || "fld8".equals(fld)) {
						try {
							valor =
									((ScrOrg) SearchInputRegisterBean.class
											.getDeclaredField(fld + "Value").get(this))
											.getId();
						}
						catch (NullPointerException e) {
							throw e;
						}
						operador =
								(String) SearchInputRegisterBean.class.getDeclaredField(
										fld + "Select").get(this);
					}
					else {
					    if ("fld1002".equals(fld)){
						valor =
								SearchInputRegisterBean.class.getDeclaredField(
										fld + "Value").get(this);
        					    if (valor != null && (Boolean)valor){
        						valor = "1";
        					    }else {
        						valor = null;
        					    }
        						operador =
        								(String) SearchInputRegisterBean.class.getDeclaredField(
        										fld + "Select").get(this);
					    }else { 
						if ("fld504".equals(fld) || "fld505".equals(fld) || "fld506".equals(fld)){
						valor =
							SearchInputRegisterBean.class.getDeclaredField(
									fld + "Value").get(this);
        					    if (valor == null || (valor != null && !(Boolean)valor)){
        						valor = "1";
        					    }else {
        						valor = null;
        					    }
        						operador =
        								(String) SearchInputRegisterBean.class.getDeclaredField(
        										fld + "Select").get(this);
        					    }else {
        						if ("fld1".equals(fld)){
        						    Object valorDesde =
        								SearchInputRegisterBean.class
        										.getDeclaredField(fld + "Value").get(this);
        						    Object valorHasta =
        								SearchInputRegisterBean.class
        										.getDeclaredField(fld + "ValueHasta").get(this);
        						    operador = (String) SearchInputRegisterBean.class.getDeclaredField(
									fld + "Select").get(this);
        						    if (!Keys.QUERY_BETWEEN_TEXT_VALUE.equals(operador)){
        							valor =
									SearchInputRegisterBean.class.getDeclaredField(
											fld + "Value").get(this);
        						    }else {
        							if (valorHasta !=  null && !"".equals(valorHasta)
        								&& valorDesde !=  null && !"".equals(valorDesde)){
                							valor = new ArrayList<String>();
        								((List<String>) valor).add((String) valorDesde);
        								((List<String>) valor).add((String) valorHasta);
        							}
        						    }
        						    
        	        				}else {
                						valor =
                							SearchInputRegisterBean.class.getDeclaredField(
                									fld + "Value").get(this);
                						operador =
                							(String) SearchInputRegisterBean.class.getDeclaredField(
                									fld + "Select").get(this);
        	        				}
        					    }
					    }
					}
				}
			}
			if (valor != null && !"".equals(valor)) {
				queryField = new AxSfQueryField();
				queryField.setBookId(idbook);
				queryField.setFldId(fld);
				queryField.setOperator(operador);
				queryField.setValue(valor);
			}
		}
		catch (Exception e) {
			queryField = null;
		}
		return queryField;
	}
	
	/**
	 * Obtiene el valor del parámetro fld1Select.
	 * 
	 * @return fld1Select valor del campo a obtener.
	 */
	public String getFld1Select() {
		return fld1Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld1Select.
	 * 
	 * @param fld1Select
	 *            valor del campo a guardar.
	 */
	public void setFld1Select(String fld1Select) {
		this.fld1Select = fld1Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld1Value.
	 * 
	 * @return fld1Value valor del campo a obtener.
	 */
	public String getFld1Value() {
		return fld1Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld1Value.
	 * 
	 * @param fld1Value
	 *            valor del campo a guardar.
	 */
	public void setFld1Value(String fld1Value) {
		this.fld1Value = fld1Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2Select.
	 * 
	 * @return fld2Select valor del campo a obtener.
	 */
	public String getFld2Select() {
		return fld2Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld2Select.
	 * 
	 * @param fld2Select
	 *            valor del campo a guardar.
	 */
	public void setFld2Select(String fld2Select) {
		this.fld2Select = fld2Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2Value.
	 * 
	 * @return fld2Value valor del campo a obtener.
	 */
	public List<Date> getFld2Value() {
		return fld2Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld2Value.
	 * 
	 * @param fld2Value
	 *            valor del campo a guardar.
	 */
	public void setFld2Value(List<Date> fld2Value) {
		this.fld2Value = fld2Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2ValueDesde.
	 * 
	 * @return fld2ValueDesde valor del campo a obtener.
	 */
	public Date getFld2ValueDesde() {
		return fld2ValueDesde;
	}
	
	/**
	 * Guarda el valor del parámetro fld2ValueDesde.
	 * 
	 * @param fld2ValueDesde
	 *            valor del campo a guardar.
	 */
	public void setFld2ValueDesde(Date fld2ValueDesde) {
		this.fld2ValueDesde = fld2ValueDesde;
	}
	
	/**
	 * Obtiene el valor del parámetro fld2ValueHasta.
	 * 
	 * @return fld2ValueHasta valor del campo a obtener.
	 */
	public Date getFld2ValueHasta() {
		return fld2ValueHasta;
	}
	
	/**
	 * Guarda el valor del parámetro fld2ValueHasta.
	 * 
	 * @param fld2ValueHasta
	 *            valor del campo a guardar.
	 */
	public void setFld2ValueHasta(Date fld2ValueHasta) {
		this.fld2ValueHasta = fld2ValueHasta;
	}
	
	/**
	 * Obtiene el valor del parámetro fld3Select.
	 * 
	 * @return fld3Select valor del campo a obtener.
	 */
	public String getFld3Select() {
		return fld3Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld3Select.
	 * 
	 * @param fld3Select
	 *            valor del campo a guardar.
	 */
	public void setFld3Select(String fld3Select) {
		this.fld3Select = fld3Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld3Value.
	 * 
	 * @return fld3Value valor del campo a obtener.
	 */
	public String getFld3Value() {
		return fld3Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld3Value.
	 * 
	 * @param fld3Value
	 *            valor del campo a guardar.
	 */
	public void setFld3Value(String fld3Value) {
		this.fld3Value = fld3Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld4Select.
	 * 
	 * @return fld4Select valor del campo a obtener.
	 */
	public String getFld4Select() {
		return fld4Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld4Select.
	 * 
	 * @param fld4Select
	 *            valor del campo a guardar.
	 */
	public void setFld4Select(String fld4Select) {
		this.fld4Select = fld4Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld4Value.
	 * 
	 * @return fld4Value valor del campo a obtener.
	 */
	public List<Date> getFld4Value() {
		return fld4Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld4Value.
	 * 
	 * @param fld4Value
	 *            valor del campo a guardar.
	 */
	public void setFld4Value(List<Date> fld4Value) {
		this.fld4Value = fld4Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld4ValueDesde.
	 * 
	 * @return fld4ValueDesde valor del campo a obtener.
	 */
	public Date getFld4ValueDesde() {
		return fld4ValueDesde;
	}
	
	/**
	 * Guarda el valor del parámetro fld4ValueDesde.
	 * 
	 * @param fld4ValueDesde
	 *            valor del campo a guardar.
	 */
	public void setFld4ValueDesde(Date fld4ValueDesde) {
		this.fld4ValueDesde = fld4ValueDesde;
	}
	
	/**
	 * Obtiene el valor del parámetro fld4ValueHasta.
	 * 
	 * @return fld4ValueHasta valor del campo a obtener.
	 */
	public Date getFld4ValueHasta() {
		return fld4ValueHasta;
	}
	
	/**
	 * Guarda el valor del parámetro fld4ValueHasta.
	 * 
	 * @param fld4ValueHasta
	 *            valor del campo a guardar.
	 */
	public void setFld4ValueHasta(Date fld4ValueHasta) {
		this.fld4ValueHasta = fld4ValueHasta;
	}
	
	/**
	 * Obtiene el valor del parámetro fld5Select.
	 * 
	 * @return fld5Select valor del campo a obtener.
	 */
	public String getFld5Select() {
		return fld5Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld5Select.
	 * 
	 * @param fld5Select
	 *            valor del campo a guardar.
	 */
	public void setFld5Select(String fld5Select) {
		this.fld5Select = fld5Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld5Value.
	 * 
	 * @return fld5Value valor del campo a obtener.
	 */
	public String getFld5Value() {
		return fld5Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld5Value.
	 * 
	 * @param fld5Value
	 *            valor del campo a guardar.
	 */
	public void setFld5Value(String fld5Value) {
		this.fld5Value = fld5Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld6Select.
	 * 
	 * @return fld6Select valor del campo a obtener.
	 */
	public String getFld6Select() {
		return fld6Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld6Select.
	 * 
	 * @param fld6Select
	 *            valor del campo a guardar.
	 */
	public void setFld6Select(String fld6Select) {
		this.fld6Select = fld6Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld6Value.
	 * 
	 * @return fld6Value valor del campo a obtener.
	 */
	public String getFld6Value() {
		return fld6Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld6Value.
	 * 
	 * @param fld6Value
	 *            valor del campo a guardar.
	 */
	public void setFld6Value(String fld6Value) {
		this.fld6Value = fld6Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld7Select.
	 * 
	 * @return fld7Select valor del campo a obtener.
	 */
	public String getFld7Select() {
		return fld7Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld7Select.
	 * 
	 * @param fld7Select
	 *            valor del campo a guardar.
	 */
	public void setFld7Select(String fld7Select) {
		this.fld7Select = fld7Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld7Value.
	 * 
	 * @return fld7Value valor del campo a obtener.
	 */
	public ScrOrg getFld7Value() {
		return fld7Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld7Value.
	 * 
	 * @param fld7Value
	 *            valor del campo a guardar.
	 */
	public void setFld7Value(ScrOrg fld7Value) {
		this.fld7Value = fld7Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld8Select.
	 * 
	 * @return fld8Select valor del campo a obtener.
	 */
	public String getFld8Select() {
		return fld8Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld8Select.
	 * 
	 * @param fld8Select
	 *            valor del campo a guardar.
	 */
	public void setFld8Select(String fld8Select) {
		this.fld8Select = fld8Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld8Value.
	 * 
	 * @return fld8Value valor del campo a obtener.
	 */
	public ScrOrg getFld8Value() {
		return fld8Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld8Value.
	 * 
	 * @param fld8Value
	 *            valor del campo a guardar.
	 */
	public void setFld8Value(ScrOrg fld8Value) {
		this.fld8Value = fld8Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld9Select.
	 * 
	 * @return fld9Select valor del campo a obtener.
	 */
	public String getFld9Select() {
		return fld9Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld9Select.
	 * 
	 * @param fld9Select
	 *            valor del campo a guardar.
	 */
	public void setFld9Select(String fld9Select) {
		this.fld9Select = fld9Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld9Value.
	 * 
	 * @return fld9Value valor del campo a obtener.
	 */
	public String getFld9Value() {
		return fld9Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld9Value.
	 * 
	 * @param fld9Value
	 *            valor del campo a guardar.
	 */
	public void setFld9Value(String fld9Value) {
		this.fld9Value = fld9Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld14Select.
	 * 
	 * @return fld14Select valor del campo a obtener.
	 */
	public String getFld14Select() {
		return fld14Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld14Select.
	 * 
	 * @param fld14Select
	 *            valor del campo a guardar.
	 */
	public void setFld14Select(String fld14Select) {
		this.fld14Select = fld14Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld14Value.
	 * 
	 * @return fld14Value valor del campo a obtener.
	 */
	public String getFld14Value() {
		return fld14Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld14Value.
	 * 
	 * @param fld14Value
	 *            valor del campo a guardar.
	 */
	public void setFld14Value(String fld14Value) {
		this.fld14Value = fld14Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld15Select.
	 * 
	 * @return fld15Select valor del campo a obtener.
	 */
	public String getFld15Select() {
		return fld15Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld15Select.
	 * 
	 * @param fld15Select
	 *            valor del campo a guardar.
	 */
	public void setFld15Select(String fld15Select) {
		this.fld15Select = fld15Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld15Value.
	 * 
	 * @return fld15Value valor del campo a obtener.
	 */
	public String getFld15Value() {
		return fld15Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld15Value.
	 * 
	 * @param fld15Value
	 *            valor del campo a guardar.
	 */
	public void setFld15Value(String fld15Value) {
		this.fld15Value = fld15Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld16Select.
	 * 
	 * @return fld16Select valor del campo a obtener.
	 */
	public String getFld16Select() {
		return fld16Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld16Select.
	 * 
	 * @param fld16Select
	 *            valor del campo a guardar.
	 */
	public void setFld16Select(String fld16Select) {
		this.fld16Select = fld16Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld16Value.
	 * 
	 * @return fld16Value valor del campo a obtener.
	 */
	public ScrCa getFld16Value() {
		return fld16Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld16Value.
	 * 
	 * @param fld16Value
	 *            valor del campo a guardar.
	 */
	public void setFld16Value(ScrCa fld16Value) {
		this.fld16Value = fld16Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld17Select.
	 * 
	 * @return fld17Select valor del campo a obtener.
	 */
	public String getFld17Select() {
		return fld17Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld17Select.
	 * 
	 * @param fld17Select
	 *            valor del campo a guardar.
	 */
	public void setFld17Select(String fld17Select) {
		this.fld17Select = fld17Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld17Value.
	 * 
	 * @return fld17Value valor del campo a obtener.
	 */
	public String getFld17Value() {
		return fld17Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld17Value.
	 * 
	 * @param fld17Value
	 *            valor del campo a guardar.
	 */
	public void setFld17Value(String fld17Value) {
		this.fld17Value = fld17Value;
	}
	
	/**
	 * Obtiene el valor del parámetro fld19Select.
	 * 
	 * @return fld19Select valor del campo a obtener.
	 */
	public String getFld19Select() {
		return fld19Select;
	}
	
	/**
	 * Guarda el valor del parámetro fld19Select.
	 * 
	 * @param fld19Select
	 *            valor del campo a guardar.
	 */
	public void setFld19Select(String fld19Select) {
		this.fld19Select = fld19Select;
	}
	
	/**
	 * Obtiene el valor del parámetro fld19Value.
	 * 
	 * @return fld19Value valor del campo a obtener.
	 */
	public String getFld19Value() {
		return fld19Value;
	}
	
	/**
	 * Guarda el valor del parámetro fld19Value.
	 * 
	 * @param fld19Value
	 *            valor del campo a guardar.
	 */
	public void setFld19Value(String fld19Value) {
		this.fld19Value = fld19Value;
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
	 * Obtiene el valor del parámetro fld10Select.
	 * 
	 * @return fld10Select valor del campo a obtener.
	 */
	public String getFld10Select() {
		return fld10Select;
	}
	/**
	 * Guarda el valor del parámetro fld10Select.
	 * 
	 * @param fld10Select
	 *            valor del campo a guardar.
	 */
	public void setFld10Select(String fld10Select) {
		this.fld10Select = fld10Select;
	}
	/**
	 * Obtiene el valor del parámetro fld10Value.
	 * 
	 * @return fld10Value valor del campo a obtener.
	 */
	public String getFld10Value() {
		return fld10Value;
	}
	/**
	 * Guarda el valor del parámetro fld10Value.
	 * 
	 * @param fld10Value
	 *            valor del campo a guardar.
	 */
	public void setFld10Value(String fld10Value) {
		this.fld10Value = fld10Value;
	}
	/**
	 * Obtiene el valor del parámetro fld503Select.
	 * 
	 * @return fld503Select valor del campo a obtener.
	 */
	public String getFld503Select() {
	    return fld503Select;
	}
	/**
	 * Guarda el valor del parámetro fld503Select.
	 * 
	 * @param fld503Select
	 *            valor del campo a guardar.
	 */
	public void setFld503Select(String fld503Select) {
	    this.fld503Select = fld503Select;
	}
	/**
	 * Obtiene el valor del parámetro fld503Value.
	 * 
	 * @return fld503Value valor del campo a obtener.
	 */
	public String getFld503Value() {
	    return fld503Value;
	}
	/**
	 * Guarda el valor del parámetro fld503Value.
	 * 
	 * @param fld503Value
	 *            valor del campo a guardar.
	 */
	public void setFld503Value(String fld503Value) {
	    this.fld503Value = fld503Value;
	}
	/**
	 * Obtiene el valor del parámetro fld1001Select.
	 * 
	 * @return fld1001Select valor del campo a obtener.
	 */
	public String getFld1001Select() {
	    return fld1001Select;
	}
	/**
	 * Guarda el valor del parámetro fld1001Select.
	 * 
	 * @param fld1001Select
	 *            valor del campo a guardar.
	 */
	public void setFld1001Select(String fld1001Select) {
	    this.fld1001Select = fld1001Select;
	}
	/**
	 * Obtiene el valor del parámetro fld1001Value.
	 * 
	 * @return fld1001Value valor del campo a obtener.
	 */
	public String getFld1001Value() {
	    return fld1001Value;
	}
	/**
	 * Guarda el valor del parámetro fld1001Value.
	 * 
	 * @param fld1001Value
	 *            valor del campo a guardar.
	 */
	public void setFld1001Value(String fld1001Value) {
	    this.fld1001Value = fld1001Value;
	}
	/**
	 * Obtiene el valor del parámetro interesadoTipoDocValue.
	 * 
	 * @return interesadoTipoDocValue valor del campo a obtener.
	 */
	public String getInteresadoTipoDocSelect() {
	    return interesadoTipoDocSelect;
	}
	/**
	 * Guarda el valor del parámetro interesadoTipoDocSelect.
	 * 
	 * @param interesadoTipoDocSelect
	 *            valor del campo a guardar.
	 */
	public void setInteresadoTipoDocSelect(String interesadoTipoDocSelect) {
	    this.interesadoTipoDocSelect = interesadoTipoDocSelect;
	}
	/**
	 * Obtiene el valor del parámetro interesadoTipoDocValue.
	 * 
	 * @return interesadoTipoDocValue valor del campo a obtener.
	 */
	public String getInteresadoTipoDocValue() {
	    return interesadoTipoDocValue;
	}
	/**
	 * Guarda el valor del parámetro interesadoTipoDocValue.
	 * 
	 * @param interesadoTipoDocValue
	 *            valor del campo a guardar.
	 */
	public void setInteresadoTipoDocValue(String interesadoTipoDocValue) {
	    this.interesadoTipoDocValue = interesadoTipoDocValue;
	}
	/**
	 * Obtiene el valor del parámetro fld18Select.
	 * 
	 * @return fld18Select valor del campo a obtener.
	 */
	public String getFld18Select() {
	    return fld18Select;
	}
	/**
	 * Guarda el valor del parámetro fld18Select.
	 * 
	 * @param fld18Select
	 *            valor del campo a guardar.
	 */
	public void setFld18Select(String fld18Select) {
	    this.fld18Select = fld18Select;
	}
	/**
	 * Obtiene el valor del parámetro fld18Value.
	 * 
	 * @return fld18Value valor del campo a obtener.
	 */
	public String getFld18Value() {
	    return fld18Value;
	}
	/**
	 * Guarda el valor del parámetro fld18Value.
	 * 
	 * @param fld18Value
	 *            valor del campo a guardar.
	 */
	public void setFld18Value(String fld18Value) {
	    this.fld18Value = fld18Value;
	}
	/**
	 * Obtiene el valor del parámetro fld1002Select.
	 * 
	 * @return fld1002Select valor del campo a obtener.
	 */
	public String getFld1002Select() {
	    return fld1002Select;
	}
	/**
	 * Guarda el valor del parámetro fld1002Select.
	 * 
	 * @param fld1002Select
	 *            valor del campo a guardar.
	 */
	public void setFld1002Select(String fld1002Select) {
	    this.fld1002Select = fld1002Select;
	}

	/**
	 * Obtiene el valor del parámetro fld1002Value.
	 * 
	 * @return fld1002Value valor del campo a obtener.
	 */
	public Boolean getFld1002Value() {
	    return fld1002Value;
	}
	/**
	 * Guarda el valor del parámetro fld1002Value.
	 * 
	 * @param fld1002Value
	 *            valor del campo a guardar.
	 */
	public void setFld1002Value(Boolean fld1002Value) {
	    this.fld1002Value = fld1002Value;
	}
	/**
	 * Obtiene el valor del parámetro fld507Select.
	 * 
	 * @return fld507Select valor del campo a obtener.
	 */
	public String getFld507Select() {
	    return fld507Select;
	}
	/**
	 * Guarda el valor del parámetro fld507Select.
	 * 
	 * @param fld507Select
	 *            valor del campo a guardar.
	 */
	public void setFld507Select(String fld507Select) {
	    this.fld507Select = fld507Select;
	}
	/**
	 * Obtiene el valor del parámetro fld507Value.
	 * 
	 * @return fld507Value valor del campo a obtener.
	 */
	public String getFld507Value() {
	    return fld507Value;
	}
	/**
	 * Guarda el valor del parámetro fld507Value.
	 * 
	 * @param fld507Value
	 *            valor del campo a guardar.
	 */
	public void setFld507Value(String fld507Value) {
	    this.fld507Value = fld507Value;
	}
	/**
	 * Obtiene el valor del parámetro fld504Select.
	 * 
	 * @return fld504Select valor del campo a obtener.
	 */
	public String getFld504Select() {
	    return fld504Select;
	}
	/**
	 * Guarda el valor del parámetro fld504Select.
	 * 
	 * @param fld504Select
	 *            valor del campo a guardar.
	 */
	public void setFld504Select(String fld504Select) {
	    this.fld504Select = fld504Select;
	}
	/**
	 * Obtiene el valor del parámetro fld504Value.
	 * 
	 * @return fld504Value valor del campo a obtener.
	 */
	public Boolean getFld504Value() {
	    return fld504Value;
	}
	/**
	 * Guarda el valor del parámetro fld504Value.
	 * 
	 * @param fld504Value
	 *            valor del campo a guardar.
	 */
	public void setFld504Value(Boolean fld504Value) {
	    this.fld504Value = fld504Value;
	}
	/**
	 * Obtiene el valor del parámetro fld505Select.
	 * 
	 * @return fld505Select valor del campo a obtener.
	 */
	public String getFld505Select() {
	    return fld505Select;
	}
	/**
	 * Guarda el valor del parámetro fld505Select.
	 * 
	 * @param fld505Select
	 *            valor del campo a guardar.
	 */
	public void setFld505Select(String fld505Select) {
	    this.fld505Select = fld505Select;
	}
	/**
	 * Obtiene el valor del parámetro fld505Value.
	 * 
	 * @return fld505Value valor del campo a obtener.
	 */
	public Boolean getFld505Value() {
	    return fld505Value;
	}
	/**
	 * Guarda el valor del parámetro fld505Value.
	 * 
	 * @param fld505Value
	 *            valor del campo a guardar.
	 */
	public void setFld505Value(Boolean fld505Value) {
	    this.fld505Value = fld505Value;
	}
	/**
	 * Obtiene el valor del parámetro fld506Select.
	 * 
	 * @return fld506Select valor del campo a obtener.
	 */
	public String getFld506Select() {
	    return fld506Select;
	}
	/**
	 * Guarda el valor del parámetro fld506Select.
	 * 
	 * @param fld506Select
	 *            valor del campo a guardar.
	 */
	public void setFld506Select(String fld506Select) {
	    this.fld506Select = fld506Select;
	}
	/**
	 * Obtiene el valor del parámetro fld506Value.
	 * 
	 * @return fld506Value valor del campo a obtener.
	 */
	public Boolean getFld506Value() {
	    return fld506Value;
	}
	/**
	 * Guarda el valor del parámetro fld506Value.
	 * 
	 * @param fld506Value
	 *            valor del campo a guardar.
	 */
	public void setFld506Value(Boolean fld506Value) {
	    this.fld506Value = fld506Value;
	}
	/**
	 * Obtiene el valor del parámetro fld1ValueHasta.
	 * 
	 * @return fld1ValueHasta valor del campo a obtener.
	 */
	public String getFld1ValueHasta() {
	    return fld1ValueHasta;
	}
	/**
	 * Guarda el valor del parámetro fld1ValueHasta.
	 * 
	 * @param fld1ValueHasta
	 *            valor del campo a guardar.
	 */
	public void setFld1ValueHasta(String fld1ValueHasta) {
	    this.fld1ValueHasta = fld1ValueHasta;
	}
	
	
}