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

import java.io.Serializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Bean que guarda la configuracion del usuario.
 * 
 * @author cmorenog
 */
public class Configuration implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idUser = null;
    private String nameDepartament = null;
    private boolean emailcheck = false;
    private boolean emailcheckrechazadas = false;
    private boolean emailcheckintercambioreg = false;
    private String copyField = null;
    private String[] fieldInputSelected = null;
    private String[] fieldOutputSelected = null;

    /**
     * Constructor.
     */
    public Configuration() {
    }

    /**
     * Obtiene el valor del parámetro emailcheck.
     * 
     * @return emailcheck valor del campo a obtener.
     */
    public boolean getEmailcheck() {
	return emailcheck;
    }

    /**
     * Guarda el valor del parámetro emailcheck.
     * 
     * @param emailcheck
     *            valor del campo a guardar.
     */
    public void setEmailcheck(boolean emailcheck) {
	this.emailcheck = emailcheck;
    }
    
    /**
     * Obtiene el valor del parámetro emailcheck.
     * 
     * @return emailcheck valor del campo a obtener.
     */
    public boolean isEmailcheck() {
	return emailcheck;
    }

    /**
     * Obtiene el valor del parámetro emailcheck.
     * 
     * @return emailcheck valor del campo a obtener.
     */
    public Integer getEmailcheckInt() {
	int result = 0;
	if (emailcheck){
	    result = 1;
	}
	return result;
    }
    
    /**
     * Guarda el valor del parámetro emailcheck.
     * 
     * @param emailcheck
     *            valor del campo a guardar.
     */
    public void setEmailcheckInt(Integer emailcheck) {
	if (emailcheck == null || new Integer(0).equals(emailcheck)){
	    this.emailcheck = false;
	} else {
	    this.emailcheck = true;
	}
    }
    
    
    /**
     * Obtiene el valor del parámetro emailcheckrechazadas.
     * 
     * @return emailcheckrechazadas valor del campo a obtener.
     */
    public boolean getEmailcheckrechazadas() {
	return emailcheckrechazadas;
    }
    
    /**
     * Guarda el valor del parámetro emailcheckrechazadas.
     * 
     * @param emailcheckrechazadas
     *            valor del campo a guardar.
     */
    public void setEmailcheckrechazadas(boolean emailcheckrechazadas) {
	this.emailcheckrechazadas = emailcheckrechazadas;
    }

    /**
     * Obtiene el valor del parámetro emailcheckrechazadas.
     * 
     * @return emailcheckrechazadas valor del campo a obtener.
     */
    public boolean isEmailcheckrechazadas() {
	return emailcheckrechazadas;
    }
    
    /**
     * Guarda el valor del parámetro emailcheckrechazadas.
     * 
     * @param emailcheckrechazadas
     *            valor del campo a guardar.
     */
    public void setEmailcheckrechazadasInt(Integer emailcheckrechazadas) {
		if (emailcheckrechazadas == null || new Integer(0).equals(emailcheckrechazadas)){
		    this.emailcheckrechazadas = false;
		} else {
		    this.emailcheckrechazadas = true;
		}
    }
    
    
    /**
     * Obtiene el valor del parámetro emailcheckrechazadas.
     * 
     * @return emailcheckrechazadas valor del campo a obtener.
     */
    public Integer getEmailcheckrechazadasInt() {
		int result = 0;
		if (emailcheckrechazadas) {
		    result = 1;
		}
		return result;
    }
    
    /**
     * Obtiene el valor del parámetro emailcheckintercambioreg.
     * 
     * @return emailcheckintercambioreg valor del campo a obtener.
     */
    public boolean getEmailcheckintercambioreg() {
	return emailcheck;
    }

    /**
     * Guarda el valor del parámetro emailcheckintercambioreg.
     * 
     * @param emailcheckintercambioreg
     *            valor del campo a guardar.
     */
    public void setEmailcheckintercambioreg(boolean emailcheckintercambioreg) {
	this.emailcheckintercambioreg = emailcheckintercambioreg;
    }
    
    /**
     * Obtiene el valor del parámetro emailcheckintercambioreg.
     * 
     * @return emailcheckintercambioreg valor del campo a obtener.
     */
    public boolean isEmailcheckintercambioreg() {
	return emailcheckintercambioreg;
    }

    /**
     * Obtiene el valor del parámetro emailcheckintercambioreg.
     * 
     * @return emailcheckintercambioreg valor del campo a obtener.
     */
    public Integer getEmailcheckintercambioregInt() {
	int result = 0;
	if (emailcheckintercambioreg){
	    result = 1;
	}
	return result;
    }
    
    /**
     * Guarda el valor del parámetro emailcheckintercambioreg.
     * 
     * @param emailcheckintercambioreg
     *            valor del campo a guardar.
     */
    public void setEmailcheckintercambioregInt(Integer emailcheckintercambioreg) {
	if (emailcheckintercambioreg == null || new Integer(0).equals(emailcheckintercambioreg)){
	    this.emailcheckintercambioreg = false;
	} else {
	    this.emailcheckintercambioreg = true;
	}
    }
    

    /**
     * Obtiene el valor del parámetro copyField.
     * 
     * @return copyField valor del campo a obtener.
     */
    public String getCopyField() {
	return copyField;
    }

    /**
     * Guarda el valor del parámetro copyField.
     * 
     * @param copyField
     *            valor del campo a guardar.
     */
    public void setCopyField(String copyField) {
	this.copyField = copyField;
    }

    /**
     * Obtiene el valor del parámetro idUser.
     * 
     * @return idUser valor del campo a obtener.
     */
    public Integer getIdUser() {
	return idUser;
    }

    /**
     * Guarda el valor del parámetro idUser.
     * 
     * @param idUser
     *            valor del campo a guardar.
     */
    public void setIdUser(Integer idUser) {
	this.idUser = idUser;
    }

    /**
     * Obtiene el valor del parámetro nameDepartament.
     * 
     * @return nameDepartament valor del campo a obtener.
     */
    public String getNameDepartament() {
	return nameDepartament;
    }

    /**
     * Guarda el valor del parámetro nameDepartament.
     * 
     * @param nameDepartament
     *            valor del campo a guardar.
     */
    public void setNameDepartament(String nameDepartament) {
	this.nameDepartament = nameDepartament;
    }

    /**
     * Obtiene el valor del parámetro fieldInputSelected.
     * 
     * @return fieldInputSelected valor del campo a obtener.
     */
    public String[] getFieldInputSelected() {
	return fieldInputSelected;
    }

    /**
     * Guarda el valor del parámetro fieldInputSelected.
     * 
     * @param fieldInputSelected
     *            valor del campo a guardar.
     */
    public void setFieldInputSelected(String[] fieldInputSelected) {
	this.fieldInputSelected = fieldInputSelected;
    }

    /**
     * Obtiene el valor del parámetro fieldOutputSelected.
     * 
     * @return fieldOutputSelected valor del campo a obtener.
     */
    public String[] getFieldOutputSelected() {
	return fieldOutputSelected;
    }

    /**
     * Guarda el valor del parámetro fieldOutputSelected.
     * 
     * @param fieldOutputSelected
     *            valor del campo a guardar.
     */
    public void setFieldOutputSelected(String[] fieldOutputSelected) {
	this.fieldOutputSelected = fieldOutputSelected;
    }

    @SuppressWarnings("unchecked")
    public void convertArraytoJSON() {
	JSONObject obj = new JSONObject();
	JSONArray listInputReg = new JSONArray();
	if (this.fieldInputSelected != null && this.fieldInputSelected.length > 0) {
	    for (int i = 0; i < this.fieldInputSelected.length; i++) {
		listInputReg.add(this.fieldInputSelected[i]);
	    }
	}
	obj.put("inputReg", listInputReg);

	JSONArray listOutputReg = new JSONArray();
	if (this.fieldOutputSelected != null && this.fieldOutputSelected.length > 0) {
	    for (int i = 0; i < this.fieldOutputSelected.length; i++) {
		listOutputReg.add(this.fieldOutputSelected[i]);
	    }
	}
	obj.put("outputReg", listOutputReg);
	this.copyField = obj.toJSONString();
    }

    public void convertJSONtoArray() {
	JSONParser parser = new JSONParser();
	JSONObject obj = new JSONObject();
	String[] aInputReg = null;
	String[] aOutputReg = null;

	if (this.copyField != null) {
	    try {
		obj = (JSONObject) parser.parse(this.copyField);

		JSONArray listInputReg = (JSONArray) obj.get("inputReg");

		if (!listInputReg.isEmpty()) {
		    aInputReg = new String[listInputReg.size()];
		    for (int i = 0; i < listInputReg.size(); i++) {
			aInputReg[i] = (String) listInputReg.get(i);
		    }
		}

		JSONArray listOutputReg = (JSONArray) obj.get("outputReg");

		if (!listOutputReg.isEmpty()) {
		    aOutputReg = new String[listOutputReg.size()];
		    for (int i = 0; i < listOutputReg.size(); i++) {
			aOutputReg[i] = (String) listOutputReg.get(i);
		    }
		}
	    }
	    catch (ParseException e) {
	    }
	}
	this.fieldInputSelected = aInputReg;
	this.fieldOutputSelected = aOutputReg;
    }
}