/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.dataaccess.domain;

import java.util.Date;

/**
 * Clase para manejar los datos de la tabla SCR_IREGREPORTSCERT desde Ibatis.
 * 
 * @author jortizs
 */
public class InputRegisterReportsCert {
    // id de libro
    private Integer idBook;
    // id de registro
    private Integer fdrid;
    // timestamp
    private Date timestamp;
    // número de registro
    private String fld1;
    // fecha de registro
    private Date fld2;
    // usuario
    private String fld3;
    // fecha de trabajo
    private Date fld4;
    // Id de la oficina de Registro
    private Integer fld5 = null;
    // Nombre oficina de registro
    private String fld5_text = null;
    // Dirección oficina de registro
    private String fld5_address = null;
    // Ciudad oficina de registro
    private String fld5_city = null;
    // Cod. postal oficina de registro
    private String fld5_zip = null;
    // País (provincia) oficina de registro
    private String fld5_country = null;
    // Teléfono oficina de registro
    private String fld5_telephone = null;
    // Fax oficina de registro
    private String fld5_fax = null;
    // Email oficina de registro
    private String fld5_email = null;
    // Id de estado
    private Integer fld6 = null;
    // Id de Origen
    private Integer fld7 = null;
    // Nombre origen
    private String fld7_text = null;
    // Id de Destino
    private Integer fld8 = null;
    // Nombre destino
    private String fld8_text = null;
    // Remitentes
    private String fld9 = null;
    // número de Registro Original
    private String fld10 = null;
    // Tipo de Registro Original
    private Integer fld11 = null;
    // Fecha de Registro Original
    private Date fld12 = null;
    // Id de Registro original
    private Integer fld13 = null;
    // Nombre registro original
    private String fld13_text = null;
    // Tipos de Transporte
    private String fld14 = null;
    // Numero de Transporte
    private String fld15 = null;
    // Id de asunto
    private Integer fld16 = null;
    // Nombre de asunto
    private String fld16_text = null;
    // Resumen
    private String fld17 = null;
    // Referencia al Expediente
    private String fld19 = null;
    // Fecha del documento
    private Date fld20 = null;

    /**
     * Constructor.
     * 
     */
    public InputRegisterReportsCert() {

    }

    /**
     * Obtiene el valor del parámetro idBook.
     * 
     * @return idBook valor del campo a guardar.
     */
    public Integer getIdBook() {
	return idBook;
    }

    /**
     * Obtiene el valor del parámetro idBook.
     * 
     * @param idBook
     *            valor del campo a obtener.
     */
    public void setIdBook(Integer idBook) {
	this.idBook = idBook;
    }

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
     * Obtiene el valor del parámetro timestamp.
     * 
     * @return timestamp valor del campo a obtener.
     */
    public Date getTimestamp() {
	return timestamp;
    }

    /**
     * Guarda el valor del parámetro timestamp.
     * 
     * @param timestamp
     *            valor del campo a guardar.
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
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
    public Integer getFld5() {
	return fld5;
    }

    /**
     * Guarda el valor del parámetro fld5.
     * 
     * @param fld5
     *            valor del campo a guardar.
     */
    public void setFld5(Integer fld5) {
	this.fld5 = fld5;
    }

    /**
     * Obtiene el valor del parámetro fld5_text.
     * 
     * @return fld5_text valor del campo a obtener.
     */
    public String getFld5_text() {
	return fld5_text;
    }

    /**
     * Guarda el valor del parámetro fld5_text.
     * 
     * @param fld5_text
     *            valor del campo a guardar.
     */
    public void setFld5_text(String fld5_text) {
	this.fld5_text = fld5_text;
    }

    /**
     * Obtiene el valor del parámetro fld5_address.
     * 
     * @return fld5_address valor del campo a obtener.
     */
    public String getFld5_address() {
	return fld5_address;
    }

    /**
     * Guarda el valor del parámetro fld5_address.
     * 
     * @param fld5_address
     *            valor del campo a guardar.
     */
    public void setFld5_address(String fld5_address) {
	this.fld5_address = fld5_address;
    }

    /**
     * Obtiene el valor del parámetro fld5_city.
     * 
     * @return fld5_city valor del campo a obtener.
     */
    public String getFld5_city() {
	return fld5_city;
    }

    /**
     * Guarda el valor del parámetro fld5_city.
     * 
     * @param fld5_city
     *            valor del campo a guardar.
     */
    public void setFld5_city(String fld5_city) {
	this.fld5_city = fld5_city;
    }

    /**
     * Obtiene el valor del parámetro fld5_zip.
     * 
     * @return fld5_zip valor del campo a obtener.
     */
    public String getFld5_zip() {
	return fld5_zip;
    }

    /**
     * Guarda el valor del parámetro fld5_zip.
     * 
     * @param fld5_zip
     *            valor del campo a guardar.
     */
    public void setFld5_zip(String fld5_zip) {
	this.fld5_zip = fld5_zip;
    }

    /**
     * Obtiene el valor del parámetro fld5_country.
     * 
     * @return fld5_country valor del campo a obtener.
     */
    public String getFld5_country() {
	return fld5_country;
    }

    /**
     * Guarda el valor del parámetro fld5_country.
     * 
     * @param fld5_country
     *            valor del campo a guardar.
     */
    public void setFld5_country(String fld5_country) {
	this.fld5_country = fld5_country;
    }

    /**
     * Obtiene el valor del parámetro fld5_telephone.
     * 
     * @return fld5_telephone valor del campo a obtener.
     */
    public String getFld5_telephone() {
	return fld5_telephone;
    }

    /**
     * Guarda el valor del parámetro fld5_telephone.
     * 
     * @param fld5_telephone
     *            valor del campo a guardar.
     */
    public void setFld5_telephone(String fld5_telephone) {
	this.fld5_telephone = fld5_telephone;
    }

    /**
     * Obtiene el valor del parámetro fld5_fax.
     * 
     * @return fld5_fax valor del campo a obtener.
     */
    public String getFld5_fax() {
	return fld5_fax;
    }

    /**
     * Guarda el valor del parámetro fld5_fax.
     * 
     * @param fld5_fax
     *            valor del campo a guardar.
     */
    public void setFld5_fax(String fld5_fax) {
	this.fld5_fax = fld5_fax;
    }

    /**
     * Obtiene el valor del parámetro fld5_email.
     * 
     * @return fld5_email valor del campo a obtener.
     */
    public String getFld5_email() {
	return fld5_email;
    }

    /**
     * Guarda el valor del parámetro fld5_email.
     * 
     * @param fld5_email
     *            valor del campo a guardar.
     */
    public void setFld5_email(String fld5_email) {
	this.fld5_email = fld5_email;
    }

    /**
     * Obtiene el valor del parámetro fld6.
     * 
     * @return fld6 valor del campo a obtener.
     */
    public Integer getFld6() {
	return fld6;
    }

    /**
     * Guarda el valor del parámetro fld6.
     * 
     * @param fld6
     *            valor del campo a guardar.
     */
    public void setFld6(Integer fld6) {
	this.fld6 = fld6;
    }

    /**
     * Obtiene el valor del parámetro fld7.
     * 
     * @return fld7 valor del campo a obtener.
     */
    public Integer getFld7() {
	return fld7;
    }

    /**
     * Guarda el valor del parámetro fld7.
     * 
     * @param fld7
     *            valor del campo a guardar.
     */
    public void setFld7(Integer fld7) {
	this.fld7 = fld7;
    }

    /**
     * Obtiene el valor del parámetro fld7_text.
     * 
     * @return fld7_text valor del campo a obtener.
     */
    public String getFld7_text() {
	return fld7_text;
    }

    /**
     * Guarda el valor del parámetro fld7_text.
     * 
     * @param fld7_text
     *            valor del campo a guardar.
     */
    public void setFld7_text(String fld7_text) {
	this.fld7_text = fld7_text;
    }

    /**
     * Obtiene el valor del parámetro fld8.
     * 
     * @return fld8 valor del campo a obtener.
     */
    public Integer getFld8() {
	return fld8;
    }

    /**
     * Guarda el valor del parámetro fld8.
     * 
     * @param fld8
     *            valor del campo a guardar.
     */
    public void setFld8(Integer fld8) {
	this.fld8 = fld8;
    }

    /**
     * Obtiene el valor del parámetro fld8_text.
     * 
     * @return fld8_text valor del campo a obtener.
     */
    public String getFld8_text() {
	return fld8_text;
    }

    /**
     * Guarda el valor del parámetro fld8_text.
     * 
     * @param fld8_text
     *            valor del campo a guardar.
     */
    public void setFld8_text(String fld8_text) {
	this.fld8_text = fld8_text;
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
    public Integer getFld13() {
	return fld13;
    }

    /**
     * Guarda el valor del parámetro fld13.
     * 
     * @param fld13
     *            valor del campo a guardar.
     */
    public void setFld13(Integer fld13) {
	this.fld13 = fld13;
    }

    /**
     * Obtiene el valor del parámetro fld13_text.
     * 
     * @return fld13_text valor del campo a obtener.
     */
    public String getFld13_text() {
	return fld13_text;
    }

    /**
     * Guarda el valor del parámetro fld13_text.
     * 
     * @param fld13_text
     *            valor del campo a guardar.
     */
    public void setFld13_text(String fld13_text) {
	this.fld13_text = fld13_text;
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
    public Integer getFld16() {
	return fld16;
    }

    /**
     * Guarda el valor del parámetro fld16.
     * 
     * @param fld16
     *            valor del campo a guardar.
     */
    public void setFld16(Integer fld16) {
	this.fld16 = fld16;
    }

    /**
     * Obtiene el valor del parámetro fld16_text.
     * 
     * @return fld16_text valor del campo a obtener.
     */
    public String getFld16_text() {
	return fld16_text;
    }

    /**
     * Guarda el valor del parámetro fld16_text.
     * 
     * @param fld16_text
     *            valor del campo a guardar.
     */
    public void setFld16_text(String fld16_text) {
	this.fld16_text = fld16_text;
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
     * Obtiene el valor del parámetro fld20.
     * 
     * @return fld20 valor del campo a obtener.
     */
    public Date getFld20() {
	return fld20;
    }

    /**
     * Guarda el valor del parámetro fld20.
     * 
     * @param fld20
     *            valor del campo a guardar.
     */
    public void setFld20(Date fld20) {
	this.fld20 = fld20;
    }
}