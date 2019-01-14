/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.sun.istack.Nullable;

/**
 * Bean correspondiente a un registro de salida.
 * 
 * @author cmorenog
 */
public class OutputRegisterBean extends GenericBean {
	private static final long serialVersionUID = 1L;

	/**
	 * Correspondencia de campos internos con valores externos: <br />
	 * - <i>fdrid</i>: Id. <br />
	 * - <i>fld1</i>: Número de registro. <br />
	 * - <i>fld2</i>: Fecha de registro. <br />
	 * - <i>fld3</i>: Usuario. <br />
	 * - <i>fld4</i>: Fecha de trabajo. <br />
	 * - <i>fld5</i>: Oficina de Registro. <br />
	 * - <i>fld5Name</i>: Nombre oficina de Registro. <br />
	 * - <i>fld6</i>: Estado. <br />
	 * - <i>fld6Name</i>: Nombre estado. <br />
	 * - <i>fld7</i>: Origen. <br />
	 * - <i>fld7Name</i>: Nombre origen. <br />
	 * - <i>fld8</i>: Destino. <br />
	 * - <i>fld8Name</i>: Nombre destino. <br />
	 * - <i>fld9</i>: Remitentes. <br />
	 * - <i>fld10</i>: Tipos de transporte. <br />
	 * - <i>fld10</i>: Nombre de transporte. <br />
	 * - <i>fld11</i>: Número de transporte. <br />
	 * - <i>fld12</i>: Tipo de asunto. <br />
	 * - <i>fld13</i>: Resumen. <br />
	 * - <i>fld14</i>: Comentarios. <br />
	 * - <i>fld21</i>: Número de registro de oficina (WS). <br />
	 * - <i>fld22</i>: Número de registro general (WS). - <i>fld501</i>: expone.
	 * - <i>fld502</i>: solicita. - <i>fld507</i>: observaciones.
	 */
	// id
	private Integer fdrid;
	// número de registro
	private String fld1;
	// fecha de registro
	private Date fld2;
	// usuario
	private String fld3;
	// fecha de trabajo
	private Date fld4;
	// Oficina de Registro
	private ScrOfic fld5 = null;
	// Nombre oficina de registro
	private String fld5Name = null;
	// Estado
	private String fld6 = null;
	// Nombre estado
	private String fld6Name = null;
	// Origen
	private ScrOrg fld7 = null;
	// Nombre origen
	private String fld7Name = null;
	// Destino
	private ScrOrg fld8 = null;
	// Nombre destino
	private String fld8Name = null;
	// Remitentes
	private String fld9 = null;
	// Tipos de Transporte
	private String fld10 = null;
	// Nombre Tipos de Transporte
	private String fld10Name = null;
	// Numero de Transporte
	private String fld11 = null;
	// Tipo de asunto
	private ScrCa fld12 = null;
	// Nombre de asunto
	private String fld12Name = null;
	// Resumen
	private String fld13 = null;
	// Comentarios
	private String fld14 = null;
	// Número de registro de oficina (WS)
	private String fld21 = null;
	// Número de registro general (WS)
	private String fld22 = null;
	// Indicador de si es un registro de intercambio.
	private Integer fld503 = null;
	// Indicador de si tiene doc.fisica requerida.
	private Integer fld504 = null;
	// Indicador de si tiene doc.fisica complementaria.
	private Integer fld505 = null;
	// Indicador de si no tiene doc.fisica.
	private Integer fld506 = null;
	// Indicador de si se ha repetido.
	private Integer fld1002 = null;
	// expone.
	private String fld501 = null;
	// solicita.
	private String fld502 = null;
	// observaciones.
	private String fld507 = null;
	private HashMap<String, Object> fieldsMod = new HashMap<String, Object>();
	private Interesado interesado = new Interesado();
	private List<Interesado> interesados = new ArrayList<Interesado>();
	// obligatoriedad del númro de registro
	private Integer fld1003 = null;
	// ultimo acuse
	private String fld1004 = null;
	
	/**
	 * Obtiene el valor del parámetro fdrid.
	 * 
	 * @return fdrid valor del campo a obtener.
	 */
	public Integer getFdrid() {
		return fdrid;
	}

	/**
	 * Guarda el valor del parámetro fdrid.
	 * 
	 * @param fdrid
	 *            valor del campo a guardar.
	 */
	public void setFdrid(Integer fdrid) {
		// MQE
		if (null != fdrid && fdrid.equals(0)) {
			fdrid = null;
		}
		this.fdrid = fdrid;
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
	 * Obtiene el valor del parámetro fld2.
	 * 
	 * @return fld2 valor del campo a obtener.
	 */
	public Date getFld2() {
		return fld2;
	}

	/**
	 * Guarda el valor del parámetro fld2.
	 * 
	 * @param fld2
	 *            valor del campo a guardar.
	 */
	public void setFld2(Date fld2) {
		if (!equal(this.fld2, fld2)) {
			this.fld2 = fld2;
			fieldsMod.put("fld2", fld2);
		}
	}

	/**
	 * Obtiene el valor del parámetro fld3.
	 * 
	 * @return fld3 valor del campo a obtener.
	 */
	public String getFld3() {
		return fld3;
	}

	/**
	 * Guarda el valor del parámetro fld3.
	 * 
	 * @param fld3
	 *            valor del campo a guardar.
	 */
	public void setFld3(String fld3) {
		this.fld3 = fld3;
	}

	/**
	 * Obtiene el valor del parámetro fld4.
	 * 
	 * @return fld4 valor del campo a obtener.
	 */
	public Date getFld4() {
		return fld4;
	}

	/**
	 * Guarda el valor del parámetro fld4.
	 * 
	 * @param fld4
	 *            valor del campo a guardar.
	 */
	public void setFld4(Date fld4) {
		this.fld4 = fld4;
	}

	/**
	 * Obtiene el valor del parámetro fld5.
	 * 
	 * @return fld5 valor del campo a obtener.
	 */
	public ScrOfic getFld5() {
		return fld5;
	}

	/**
	 * Guarda el valor del parámetro fld5.
	 * 
	 * @param fld5
	 *            valor del campo a guardar.
	 */
	public void setFld5(ScrOfic fld5) {
		this.fld5 = fld5;
	}

	/**
	 * Obtiene el valor del parámetro fld5Name.
	 * 
	 * @return fld5Name valor del campo a obtener.
	 */
	public String getFld5Name() {
		return fld5Name;
	}

	/**
	 * Guarda el valor del parámetro fld5Name.
	 * 
	 * @param fld5Name
	 *            valor del campo a guardar.
	 */
	public void setFld5Name(String fld5Name) {
		this.fld5Name = fld5Name;
	}

	/**
	 * Obtiene el valor del parámetro fld6.
	 * 
	 * @return fld6 valor del campo a obtener.
	 */
	public String getFld6() {
		return fld6;
	}

	/**
	 * Guarda el valor del parámetro fld6.
	 * 
	 * @param fld6
	 *            valor del campo a guardar.
	 */
	public void setFld6(String fld6) {
		this.fld6 = fld6;
	}

	/**
	 * Obtiene el valor del parámetro fld6Name.
	 * 
	 * @return fld6Name valor del campo a obtener.
	 */
	public String getFld6Name() {
		return fld6Name;
	}

	/**
	 * Guarda el valor del parámetro fld6Name.
	 * 
	 * @param fld6Name
	 *            valor del campo a guardar.
	 */
	public void setFld6Name(String fld6Name) {
		this.fld6Name = fld6Name;
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
		if (!equal(this.fld7, fld7)) {
			this.fld7 = fld7;
			if (fld7 == null) {
				fieldsMod.put("fld7", fld7);
			} else {
				fieldsMod.put("fld7", fld7.getId());
			}
		}
	}

	/**
	 * Obtiene el valor del parámetro fld7Name.
	 * 
	 * @return fld7Name valor del campo a obtener.
	 */
	public String getFld7Name() {
		return fld7Name;
	}

	/**
	 * Guarda el valor del parámetro fld7Name.
	 * 
	 * @param fld7Name
	 *            valor del campo a guardar.
	 */
	public void setFld7Name(String fld7Name) {
		this.fld7Name = fld7Name;
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
		if (!equal(this.fld8, fld8)) {
			this.fld8 = fld8;
			if (fld8 == null) {
				fieldsMod.put("fld8", fld8);
			} else {
				fieldsMod.put("fld8", fld8.getId());
			}
		}
	}

	/**
	 * Obtiene el valor del parámetro fld8Name.
	 * 
	 * @return fld8Name valor del campo a obtener.
	 */
	public String getFld8Name() {
		return fld8Name;
	}

	/**
	 * Guarda el valor del parámetro fld8Name.
	 * 
	 * @param fld8Name
	 *            valor del campo a guardar.
	 */
	public void setFld8Name(String fld8Name) {
		this.fld8Name = fld8Name;
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
		if (!equal(this.fld9, fld9)) {
			this.fld9 = fld9;
			fieldsMod.put("fld9", fld9);
		}
	}

	/**
	 * Obtiene el valor del parámetro fld10.
	 * 
	 * @return fld10 valor del campo a obtener.
	 */
	public String getFld10() {
		return fld10;
	}

	/**
	 * Guarda el valor del parámetro fld10.
	 * 
	 * @param fld10
	 *            valor del campo a guardar.
	 */
	public void setFld10(String fld10) {
		if (!equal(this.fld10, fld10)) {
			this.fld10 = fld10;
			fieldsMod.put("fld10", fld10);
		}
	}

	/**
	 * Obtiene el valor del parámetro fld11.
	 * 
	 * @return fld11 valor del campo a obtener.
	 */
	public String getFld11() {
		return fld11;
	}

	/**
	 * Guarda el valor del parámetro fld11.
	 * 
	 * @param fld11
	 *            valor del campo a guardar.
	 */
	public void setFld11(String fld11) {
		if (!equal(this.fld11, fld11)) {
			this.fld11 = fld11;
			fieldsMod.put("fld11", fld11);
		}
	}

	/**
	 * Obtiene el valor del parámetro fld12.
	 * 
	 * @return fld12 valor del campo a obtener.
	 */
	public ScrCa getFld12() {
		return fld12;
	}

	/**
	 * Guarda el valor del parámetro fld12.
	 * 
	 * @param fld12
	 *            valor del campo a guardar.
	 */
	public void setFld12(ScrCa fld12) {
		if (!equal(this.fld12, fld12)) {
			this.fld12 = fld12;
			if (fld12 == null) {
				fieldsMod.put("fld12", fld12);
			} else {
				fieldsMod.put("fld12", fld12.getId());
			}
		}
	}

	/**
	 * Obtiene el valor del parámetro fld12Name.
	 * 
	 * @return fld12Name valor del campo a obtener.
	 */
	public String getFld12Name() {
		return fld12Name;
	}

	/**
	 * Guarda el valor del parámetro fld12Name.
	 * 
	 * @param fld12Name
	 *            valor del campo a guardar.
	 */
	public void setFld12Name(String fld12Name) {
		this.fld12Name = fld12Name;
	}

	/**
	 * Obtiene el valor del parámetro fld13.
	 * 
	 * @return fld13 valor del campo a obtener.
	 */
	public String getFld13() {
		return fld13;
	}

	/**
	 * Guarda el valor del parámetro fld13.
	 * 
	 * @param fld13
	 *            valor del campo a guardar.
	 */
	public void setFld13(String fld13) {
		if (!equal(this.fld13, fld13)) {
			this.fld13 = fld13;
			fieldsMod.put("fld13", fld13);
		}
	}

	/**
	 * Obtiene el valor del parámetro fld14.
	 * 
	 * @return fld14 valor del campo a obtener.
	 */
	public String getFld14() {
		return fld14;
	}

	/**
	 * Guarda el valor del parámetro fld14.
	 * 
	 * @param fld14
	 *            valor del campo a guardar.
	 */
	public void setFld14(String fld14) {
		if (!equal(this.fld14, fld14)) {
			this.fld14 = fld14;
			fieldsMod.put("fld14", fld14);
		}
	}

	/**
	 * Obtiene el valor del parámetro fieldsMod.
	 * 
	 * @return fieldsMod valor del campo a obtener.
	 */
	public HashMap<String, Object> getFieldsMod() {
		return fieldsMod;
	}

	/**
	 * Guarda el valor del parámetro fieldsMod.
	 * 
	 * @param fieldsMod
	 *            valor del campo a guardar.
	 */
	public void setFieldsMod(HashMap<String, Object> fieldsMod) {
		this.fieldsMod = fieldsMod;
	}

	/**
	 * Obtiene el valor del parámetro interesado.
	 * 
	 * @return interesado valor del campo a obtener.
	 */
	public Interesado getInteresado() {
		return interesado;
	}

	/**
	 * Guarda el valor del parámetro interesado.
	 * 
	 * @param interesado
	 *            valor del campo a guardar.
	 */
	public void setInteresado(Interesado interesado) {
		this.interesado = interesado;
	}

	/**
	 * Obtiene el valor del parámetro interesados.
	 * 
	 * @return interesados valor del campo a obtener.
	 */
	public List<Interesado> getInteresados() {
		return interesados;
	}

	/**
	 * Guarda el valor del parámetro interesados.
	 * 
	 * @param interesados
	 *            valor del campo a guardar.
	 */
	public void setInteresados(List<Interesado> interesados) {
		this.interesados = interesados;
	}
	
	/**
	 * Obtiene el valor del parámetro fld21.
	 * 
	 * @return fld21 valor del campo a obtener.
	 */
	public String getFld21() {
		return fld21;
	}

	/**
	 * Guarda el valor del parámetro fld21.
	 * 
	 * @param fld21
	 *            valor del campo a guardar.
	 */
	public void setFld21(String fld21) {
		this.fld21 = fld21;
	}

	/**
	 * Obtiene el valor del parámetro fld22.
	 * 
	 * @return fld22 valor del campo a obtener.
	 */
	public String getFld22() {
		return fld22;
	}

	/**
	 * Guarda el valor del parámetro fld22.
	 * 
	 * @param fld22
	 *            valor del campo a guardar.
	 */
	public void setFld22(String fld22) {
		this.fld22 = fld22;
	}

	/**
	 * Guarda el valor del parámetro fld503.
	 * 
	 * @param fld503
	 *            valor del campo a guardar.
	 */
	public Integer getFld503() {
		return fld503;
	}

	/**
	 * Guarda el valor del parámetro fld503.
	 * 
	 * @param fld503
	 *            valor del campo a guardar.
	 */
	public void setFld503(Integer fld503) {
		if (!equal(this.fld503, fld503)) {
			this.fld503 = fld503;
			fieldsMod.put("fld503", fld503);
		}
	}

	/**
	 * Guarda el valor del parámetro fld504.
	 * 
	 * @param fld504
	 *            valor del campo a guardar.
	 */
	public Integer getFld504() {
		return fld504;
	}

	/**
	 * Guarda el valor del parámetro fld504.
	 * 
	 * @param fld504
	 *            valor del campo a guardar.
	 */
	public void setFld504(Integer fld504) {
		if (!equal(this.fld504, fld504)) {
			this.fld504 = fld504;
			fieldsMod.put("fld504", fld504);
		}
	}

	/**
	 * Guarda el valor del parámetro fld505.
	 * 
	 * @param fld505
	 *            valor del campo a guardar.
	 */
	public Integer getFld505() {
		return fld505;
	}

	/**
	 * Guarda el valor del parámetro fld505.
	 * 
	 * @param fld505
	 *            valor del campo a guardar.
	 */
	public void setFld505(Integer fld505) {
		if (!equal(this.fld505, fld505)) {
			this.fld505 = fld505;
			fieldsMod.put("fld505", fld505);
		}
	}

	/**
	 * Guarda el valor del parámetro fld506.
	 * 
	 * @param fld506
	 *            valor del campo a guardar.
	 */
	public Integer getFld506() {
		return fld506;
	}

	/**
	 * Guarda el valor del parámetro fld506.
	 * 
	 * @param fld506
	 *            valor del campo a guardar.
	 */
	public void setFld506(Integer fld506) {
		if (!equal(this.fld506, fld506)) {
			this.fld506 = fld506;
			fieldsMod.put("fld506", fld506);
		}
	}

	/**
	 * Guarda el valor del parámetro fld10Name.
	 * 
	 * @param fld10Name
	 *            valor del campo a guardar.
	 */
	public String getFld10Name() {
		return fld10Name;
	}

	/**
	 * Guarda el valor del parámetro fld10Name.
	 * 
	 * @param fld10Name
	 *            valor del campo a guardar.
	 */
	public void setFld10Name(String fld10Name) {
		this.fld10Name = fld10Name;
	}

	/**
	 * Comprueba si el campo a guardar es igual que el que había guardado ya.
	 * 
	 * @param a
	 *            Campo a guardar.
	 * @param b
	 *            Campo guardado.
	 * 
	 * @return booleano a <i>true</i> si son iguales y <i>false</i> si no lo
	 *         son.
	 */
	private boolean equal(@Nullable Object a, @Nullable Object b) {
		return a == b || (a != null && a.equals(b) || (a == null && b != null && b instanceof java.lang.String && "".equals(b)));
	}

	/**
	 * Guarda el valor del parámetro fld1002.
	 * 
	 * @param fld1002
	 *            valor del campo a guardar.
	 */
	public Integer getFld1002() {
		return fld1002;
	}

	/**
	 * Guarda el valor del parámetro fld1002.
	 * 
	 * @param fld1002
	 *            valor del campo a guardar.
	 */
	public void setFld1002(Integer fld1002) {
		if (!equal(this.fld1002, fld1002)) {
			this.fld1002 = fld1002;
			fieldsMod.put("fld1002", fld1002);
		}
	}

	/**
	 * Guarda el valor del parámetro fld501.
	 * 
	 * @param fld501
	 *            valor del campo a guardar.
	 */
	public String getFld501() {
		return fld501;
	}

	/**
	 * Guarda el valor del parámetro fld501.
	 * 
	 * @param fld501
	 *            valor del campo a guardar.
	 */
	public void setFld501(String fld501) {
		if (!equal(this.fld501, fld501)) {
			this.fld501 = fld501;
			fieldsMod.put("fld501", fld501);
		}
	}

	/**
	 * Guarda el valor del parámetro fld502.
	 * 
	 * @param fld502
	 *            valor del campo a guardar.
	 */
	public String getFld502() {
		return fld502;
	}

	/**
	 * Guarda el valor del parámetro fld502.
	 * 
	 * @param fld502
	 *            valor del campo a guardar.
	 */
	public void setFld502(String fld502) {
		if (!equal(this.fld502, fld502)) {
			this.fld502 = fld502;
			fieldsMod.put("fld502", fld502);
		}
	}

	/**
	 * Guarda el valor del parámetro fld507.
	 * 
	 * @param fld507
	 *            valor del campo a guardar.
	 */
	public String getFld507() {
		return fld507;
	}

	/**
	 * Guarda el valor del parámetro fld507.
	 * 
	 * @param fld507
	 *            valor del campo a guardar.
	 */
	public void setFld507(String fld507) {
		if (!equal(this.fld507, fld507)) {
			this.fld507 = fld507;
			fieldsMod.put("fld507", fld507);
		}
	}

	/**
	 * Guarda el valor del parámetro fld1003.
	 * 
	 * @param fld1003
	 *            valor del campo a guardar.
	 */
	public Integer getFld1003() {
		return fld1003;
	}

	/**
	 * Guarda el valor del parámetro fld1003.
	 * 
	 * @param fld1003
	 *            valor del campo a guardar.
	 */
	public void setFld1003(Integer fld1003) {
		if (!equal(this.fld1003, fld1003)) {
			this.fld1003 = fld1003;
			fieldsMod.put("fld1003", fld1003);
		}
	}

	/**
	 * Guarda el valor del parámetro fld1004.
	 * 
	 * @param fld1004
	 *            valor del campo a guardar.
	 */
	public String getFld1004() {
		return fld1004;
	}

	/**
	 * Guarda el valor del parámetro fld1004.
	 * 
	 * @param fld1004
	 *            valor del campo a guardar.
	 */
	public void setFld1004(String fld1004) {
		if (!equal(this.fld1004, fld1004)) {
			this.fld1004 = fld1004;
			fieldsMod.put("fld1004", fld1004);
		}
	}
}