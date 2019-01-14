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

import java.util.Date;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;

/**
 * Bean correspondiente a una fila en el buscador de registro de entrada.
 * 
 * @author cmorenog
 */
public class RowSearchInputRegisterBean
		extends GenericBean {
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
	 * - <i>fld10</i>: Número de Registro original. <br />
	 * - <i>fld11</i>: Tipo de Registro original. <br />
	 * - <i>fld12</i>: Fecha de Registro original. <br />
	 * - <i>fld13</i>: Registro original. <br />
	 * - <i>fld13Name</i>: Nombre Registro original. <br />
	 * - <i>fld14</i>: Tipos de transporte. <br />
	 * - <i>fld15</i>: Número de transporte. <br />
	 * - <i>fld16</i>: Tipo de asunto. <br />
	 * - <i>fld16Name</i>: Nombre de asunto. <br />
	 * - <i>fld17</i>: Resumen. <br />
	 * - <i>fld19</i>: Referencia al Expediente.
	 * - <i>fld503</i>: Si es SIR.
	 * - <i>fld504</i>: Si tiene documentacion fisica requerida.
	 * - <i>fld505</i>: Si tiene documentacion fisica complementaria.
	 * - <i>fld506</i>: Si no tiene documentacion.
	 * - <i>fld1001</i>: Esta impreso.
	 * - <i>fld1002</i>: flag.
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
	// número de Registro Original
	private String fld10 = null;
	// Tipo de Registro Original
	private Integer fld11 = null;
	// Fecha de Registro Original
	private Date fld12 = null;
	// Registro original
	private ScrOrg fld13 = null;
	// Nombre registro original
	private String fld13Name = null;
	// Tipos de Transporte
	private String fld14 = null;
	// Numero de Transporte
	private String fld15 = null;
	// Tipo de asunto
	private ScrCa fld16 = null;
	// Nombre de asunto
	private String fld16Name = null;
	// Resumen
	private String fld17 = null;
	// Referencia al Expediente
	private String fld19 = null;
	private String fld503 = null;
	private String fld504 = null;
	private String fld505 = null;
	private String fld506 = null;
	private String fld1001 = null;
	private String fld1002 = null;
	private String fld1004 = null;
	private Integer stateDis = null;
	private Integer stateOutput = null;
	
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
		this.fld2 = fld2;
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
		this.fld7 = fld7;
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
		this.fld8 = fld8;
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
		this.fld9 = fld9;
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
		this.fld10 = fld10;
	}
	
	/**
	 * Obtiene el valor del parámetro fld11.
	 * 
	 * @return fld11 valor del campo a obtener.
	 */
	public Integer getFld11() {
		return fld11;
	}
	
	/**
	 * Guarda el valor del parámetro fld11.
	 * 
	 * @param fld11
	 *            valor del campo a guardar.
	 */
	public void setFld11(Integer fld11) {
		this.fld11 = fld11;
	}
	
	/**
	 * Obtiene el valor del parámetro fld12.
	 * 
	 * @return fld12 valor del campo a obtener.
	 */
	public Date getFld12() {
		return fld12;
	}
	
	/**
	 * Guarda el valor del parámetro fld12.
	 * 
	 * @param fld12
	 *            valor del campo a guardar.
	 */
	public void setFld12(Date fld12) {
		this.fld12 = fld12;
	}
	
	/**
	 * Obtiene el valor del parámetro fld13.
	 * 
	 * @return fld13 valor del campo a obtener.
	 */
	public ScrOrg getFld13() {
		return fld13;
	}
	
	/**
	 * Guarda el valor del parámetro fld13.
	 * 
	 * @param fld13
	 *            valor del campo a guardar.
	 */
	public void setFld13(ScrOrg fld13) {
		this.fld13 = fld13;
	}
	
	/**
	 * Obtiene el valor del parámetro fld13Name.
	 * 
	 * @return fld13Name valor del campo a obtener.
	 */
	public String getFld13Name() {
		return fld13Name;
	}
	
	/**
	 * Guarda el valor del parámetro fld13Name.
	 * 
	 * @param fld13Name
	 *            valor del campo a guardar.
	 */
	public void setFld13Name(String fld13Name) {
		this.fld13Name = fld13Name;
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
		this.fld14 = fld14;
	}
	
	/**
	 * Obtiene el valor del parámetro fld15.
	 * 
	 * @return fld15 valor del campo a obtener.
	 */
	public String getFld15() {
		return fld15;
	}
	
	/**
	 * Guarda el valor del parámetro fld15.
	 * 
	 * @param fld15
	 *            valor del campo a guardar.
	 */
	public void setFld15(String fld15) {
		this.fld15 = fld15;
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
	 * Obtiene el valor del parámetro fld16Name.
	 * 
	 * @return fld16Name valor del campo a obtener.
	 */
	public String getFld16Name() {
		return fld16Name;
	}
	
	/**
	 * Guarda el valor del parámetro fld16Name.
	 * 
	 * @param fld16Name
	 *            valor del campo a guardar.
	 */
	public void setFld16Name(String fld16Name) {
		this.fld16Name = fld16Name;
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
	 * Obtiene el valor del parámetro fld19.
	 * 
	 * @return fld19 valor del campo a obtener.
	 */
	public String getFld19() {
		return fld19;
	}
	
	/**
	 * Guarda el valor del parámetro fld19.
	 * 
	 * @param fld19
	 *            valor del campo a guardar.
	 */
	public void setFld19(String fld19) {
		this.fld19 = fld19;
	}
	/**
	 * Obtiene el valor del parámetro fld503.
	 * 
	 * @return fld503 valor del campo a obtener.
	 */
	public String getFld503() {
	    return fld503;
	}
	/**
	 * Guarda el valor del parámetro fld503.
	 * 
	 * @param fld503
	 *            valor del campo a guardar.
	 */
	public void setFld503(String fld503) {
	    this.fld503 = fld503;
	}
	/**
	 * Obtiene el valor del parámetro fld504.
	 * 
	 * @return fld504 valor del campo a obtener.
	 */
	public String getFld504() {
	    return fld504;
	}
	/**
	 * Guarda el valor del parámetro fld504.
	 * 
	 * @param fld504
	 *            valor del campo a guardar.
	 */
	public void setFld504(String fld504) {
	    this.fld504 = fld504;
	}
	/**
	 * Obtiene el valor del parámetro fld505.
	 * 
	 * @return fld505 valor del campo a obtener.
	 */
	public String getFld505() {
	    return fld505;
	}
	/**
	 * Guarda el valor del parámetro fld505.
	 * 
	 * @param fld505
	 *            valor del campo a guardar.
	 */
	public void setFld505(String fld505) {
	    this.fld505 = fld505;
	}
	/**
	 * Obtiene el valor del parámetro fld506.
	 * 
	 * @return fld506 valor del campo a obtener.
	 */
	public String getFld506() {
	    return fld506;
	}
	/**
	 * Guarda el valor del parámetro fld506.
	 * 
	 * @param fld506
	 *            valor del campo a guardar.
	 */
	public void setFld506(String fld506) {
	    this.fld506 = fld506;
	}
	/**
	 * Obtiene el valor del parámetro fld1001.
	 * 
	 * @return fld1001 valor del campo a obtener.
	 */
	public String getFld1001() {
	    return fld1001;
	}
	/**
	 * Guarda el valor del parámetro fld1001.
	 * 
	 * @param fld1001
	 *            valor del campo a guardar.
	 */
	public void setFld1001(String fld1001) {
	    this.fld1001 = fld1001;
	}
	/**
	 * Obtiene el valor del parámetro fld1002.
	 * 
	 * @return fld1002 valor del campo a obtener.
	 */
	public String getFld1002() {
	    return fld1002;
	}
	/**
	 * Guarda el valor del parámetro fld1002.
	 * 
	 * @param fld1002
	 *            valor del campo a guardar.
	 */
	public void setFld1002(String fld1002) {
	    this.fld1002 = fld1002;
	}
	/**
	 * Obtiene el valor del parámetro fld1004.
	 * 
	 * @return fld1004 valor del campo a obtener.
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
	    this.fld1004 = fld1004;
	}
	/**
	 * Obtiene el valor del parámetro stateDis.
	 * 
	 * @return stateDis valor del campo a obtener.
	 */
	public Integer getStateDis() {
	    return stateDis;
	}
	/**
	 * Guarda el valor del parámetro stateDis.
	 * 
	 * @param stateDis
	 *            valor del campo a guardar.
	 */
	public void setStateDis(Integer stateDis) {
	    this.stateDis = stateDis;
	}

	/**
	 * Obtiene el valor del parámetro stateOutput.
	 * 
	 * @return stateOutput valor del campo a obtener.
	 */
	public Integer getStateOutput() {
	    return stateOutput;
	}
	/**
	 * Guarda el valor del parámetro stateOutput.
	 * 
	 * @param stateOutput
	 *            valor del campo a guardar.
	 */
	public void setStateOutput(Integer stateOutput) {
	    this.stateOutput = stateOutput;
	}

}