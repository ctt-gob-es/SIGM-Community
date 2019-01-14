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

/**
 * Bean que almacena los permisos de un usuario sobre un libro.
 * 
 * @author cmorenog
 */
public class ParamBookBean
		extends GenericBean {
	private static final long serialVersionUID = 1L;
	private boolean canQuery;
	private boolean canCreate;
	private boolean canModify;
	private boolean isBookAdmin;
	private boolean readOnly;
	private boolean canOpenReg;
	private boolean canOperationIR;
	private boolean canModifyFchReg;
	private boolean canRegFchReg;
	private boolean canAddPersons;
	private boolean canModifyPersons;
	private boolean canUpdateProtectedFields;
	// Distribución
	private boolean canAcceptRegistersDist;
	private boolean canRejectRegistersDist;
	private boolean canArchiveRegistersDist;
	private boolean canChangeDestRegistersDist;
	private boolean canChangeDestRejectRegistersDist;
	private boolean canDistRegistersDist;
	
	/**
	 * Obtiene el valor del parámetro canQuery.
	 * 
	 * @return canQuery valor del campo a obtener.
	 */
	public boolean isCanQuery() {
		return canQuery;
	}
	
	/**
	 * Guarda el valor del parámetro canQuery.
	 * 
	 * @param canQuery
	 *            valor del campo a guardar.
	 */
	public void setCanQuery(boolean canQuery) {
		this.canQuery = canQuery;
	}
	
	/**
	 * Obtiene el valor del parámetro canCreate.
	 * 
	 * @return canCreate valor del campo a obtener.
	 */
	public boolean isCanCreate() {
		return canCreate;
	}
	
	/**
	 * Guarda el valor del parámetro canCreate.
	 * 
	 * @param canCreate
	 *            valor del campo a guardar.
	 */
	public void setCanCreate(boolean canCreate) {
		this.canCreate = canCreate;
	}
	
	/**
	 * Obtiene el valor del parámetro canModify.
	 * 
	 * @return canModify valor del campo a obtener.
	 */
	public boolean isCanModify() {
		return canModify;
	}
	
	/**
	 * Guarda el valor del parámetro canModify.
	 * 
	 * @param canModify
	 *            valor del campo a guardar.
	 */
	public void setCanModify(boolean canModify) {
		this.canModify = canModify;
	}
	
	/**
	 * Obtiene el valor del parámetro isBookAdmin.
	 * 
	 * @return isBookAdmin valor del campo a obtener.
	 */
	public boolean isBookAdmin() {
		return isBookAdmin;
	}
	
	/**
	 * Guarda el valor del parámetro isBookAdmin.
	 * 
	 * @param isBookAdmin
	 *            valor del campo a guardar.
	 */
	public void setBookAdmin(boolean isBookAdmin) {
		this.isBookAdmin = isBookAdmin;
	}
	
	/**
	 * Obtiene el valor del parámetro readOnly.
	 * 
	 * @return readOnly valor del campo a obtener.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	
	/**
	 * Guarda el valor del parámetro readOnly.
	 * 
	 * @param readOnly
	 *            valor del campo a guardar.
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * Obtiene el valor del parámetro canOpenReg.
	 * 
	 * @return canOpenReg valor del campo a obtener.
	 */
	public boolean isCanOpenReg() {
		return canOpenReg;
	}
	
	/**
	 * Guarda el valor del parámetro canOpenReg.
	 * 
	 * @param canOpenReg
	 *            valor del campo a guardar.
	 */
	public void setCanOpenReg(boolean canOpenReg) {
		this.canOpenReg = canOpenReg;
	}
	
	/**
	 * Obtiene el valor del parámetro canOperationIR.
	 * 
	 * @return canOperationIR valor del campo a obtener.
	 */
	public boolean isCanOperationIR() {
		return canOperationIR;
	}
	
	/**
	 * Guarda el valor del parámetro canOperationIR.
	 * 
	 * @param canOperationIR
	 *            valor del campo a guardar.
	 */
	public void setCanOperationIR(boolean canOperationIR) {
		this.canOperationIR = canOperationIR;
	}
	
	/**
	 * Obtiene el valor del parámetro canModifyFchReg.
	 * 
	 * @return canModifyFchReg valor del campo a obtener.
	 */
	public boolean isCanModifyFchReg() {
		return canModifyFchReg;
	}
	
	/**
	 * Guarda el valor del parámetro canModifyFchReg.
	 * 
	 * @param canModifyFchReg
	 *            valor del campo a guardar.
	 */
	public void setCanModifyFchReg(boolean canModifyFchReg) {
		this.canModifyFchReg = canModifyFchReg;
	}
	
	/**
	 * Obtiene el valor del parámetro canRegFchReg.
	 * 
	 * @return canRegFchReg valor del campo a obtener.
	 */
	public boolean isCanRegFchReg() {
		return canRegFchReg;
	}
	
	/**
	 * Guarda el valor del parámetro canRegFchReg.
	 * 
	 * @param canRegFchReg
	 *            valor del campo a guardar.
	 */
	public void setCanRegFchReg(boolean canRegFchReg) {
		this.canRegFchReg = canRegFchReg;
	}
	
	/**
	 * Obtiene el valor del parámetro canAddPersons.
	 * 
	 * @return canAddPersons valor del campo a obtener.
	 */
	public boolean isCanAddPersons() {
		return canAddPersons;
	}
	
	/**
	 * Guarda el valor del parámetro canAddPersons.
	 * 
	 * @param canAddPersons
	 *            valor del campo a guardar.
	 */
	public void setCanAddPersons(boolean canAddPersons) {
		this.canAddPersons = canAddPersons;
	}
	
	/**
	 * Obtiene el valor del parámetro canModifyPersons.
	 * 
	 * @return canModifyPersons valor del campo a obtener.
	 */
	public boolean isCanModifyPersons() {
		return canModifyPersons;
	}
	
	/**
	 * Guarda el valor del parámetro canModifyPersons.
	 * 
	 * @param canModifyPersons
	 *            valor del campo a guardar.
	 */
	public void setCanModifyPersons(boolean canModifyPersons) {
		this.canModifyPersons = canModifyPersons;
	}
	
	/**
	 * Obtiene el valor del parámetro canUpdateProtectedFields.
	 * 
	 * @return canUpdateProtectedFields valor del campo a obtener.
	 */
	public boolean isCanUpdateProtectedFields() {
		return canUpdateProtectedFields;
	}
	
	/**
	 * Guarda el valor del parámetro canUpdateProtectedFields.
	 * 
	 * @param canUpdateProtectedFields
	 *            valor del campo a guardar.
	 */
	public void setCanUpdateProtectedFields(boolean canUpdateProtectedFields) {
		this.canUpdateProtectedFields = canUpdateProtectedFields;
	}
	
	/**
	 * Obtiene el valor del parámetro canAcceptRegistersDist.
	 * 
	 * @return canAcceptRegistersDist valor del campo a obtener.
	 */
	public boolean isCanAcceptRegistersDist() {
		return canAcceptRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canAcceptRegistersDist.
	 * 
	 * @param canAcceptRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanAcceptRegistersDist(boolean canAcceptRegistersDist) {
		this.canAcceptRegistersDist = canAcceptRegistersDist;
	}
	
	/**
	 * Obtiene el valor del parámetro canRejectRegistersDist.
	 * 
	 * @return canRejectRegistersDist valor del campo a obtener.
	 */
	public boolean isCanRejectRegistersDist() {
		return canRejectRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canRejectRegistersDist.
	 * 
	 * @param canRejectRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanRejectRegistersDist(boolean canRejectRegistersDist) {
		this.canRejectRegistersDist = canRejectRegistersDist;
	}
	
	/**
	 * Obtiene el valor del parámetro canArchiveRegistersDist.
	 * 
	 * @return canArchiveRegistersDist valor del campo a obtener.
	 */
	public boolean isCanArchiveRegistersDist() {
		return canArchiveRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canArchiveRegistersDist.
	 * 
	 * @param canArchiveRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanArchiveRegistersDist(boolean canArchiveRegistersDist) {
		this.canArchiveRegistersDist = canArchiveRegistersDist;
	}
	
	/**
	 * Obtiene el valor del parámetro canChangeDestRegistersDist.
	 * 
	 * @return canChangeDestRegistersDist valor del campo a obtener.
	 */
	public boolean isCanChangeDestRegistersDist() {
		return canChangeDestRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canChangeDestRegistersDist.
	 * 
	 * @param canChangeDestRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanChangeDestRegistersDist(boolean canChangeDestRegistersDist) {
		this.canChangeDestRegistersDist = canChangeDestRegistersDist;
	}
	
	/**
	 * Obtiene el valor del parámetro canChangeDestRejectRegistersDist.
	 * 
	 * @return canChangeDestRejectRegistersDist valor del campo a obtener.
	 */
	public boolean isCanChangeDestRejectRegistersDist() {
		return canChangeDestRejectRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canChangeDestRejectRegistersDist.
	 * 
	 * @param canChangeDestRejectRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanChangeDestRejectRegistersDist(
			boolean canChangeDestRejectRegistersDist) {
		this.canChangeDestRejectRegistersDist = canChangeDestRejectRegistersDist;
	}
	
	/**
	 * Obtiene el valor del parámetro canDistRegistersDist.
	 * 
	 * @return canDistRegistersDist valor del campo a obtener.
	 */
	public boolean isCanDistRegistersDist() {
		return canDistRegistersDist;
	}
	
	/**
	 * Guarda el valor del parámetro canDistRegistersDist.
	 * 
	 * @param canDistRegistersDist
	 *            valor del campo a guardar.
	 */
	public void setCanDistRegistersDist(boolean canDistRegistersDist) {
		this.canDistRegistersDist = canDistRegistersDist;
	}
}