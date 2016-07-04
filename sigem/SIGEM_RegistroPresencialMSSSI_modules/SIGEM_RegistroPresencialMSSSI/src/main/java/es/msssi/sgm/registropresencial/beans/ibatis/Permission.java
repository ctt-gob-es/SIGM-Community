package es.msssi.sgm.registropresencial.beans.ibatis;

import java.io.Serializable;

/**
 * Bean para la tabla de permisos.
 * 
 * @author cmorenog
 */
public class Permission implements Serializable {

    private static final long serialVersionUID = -5286803184764261549L;
    
    /**
     * Valor de id permiso.
     */
    private int idPermission;
    /**
     * valor del identificador del destinatario de los permisos.
     */
    private int idDestination;
    /**
     * valor del identificador del producto al que se asigna el permiso.
     */
    private int idProduct;
    /**
     * valor del permiso.
     */
    private int permission;
    
    /**
     * constructor.
     */
    public Permission (){
	this.idPermission = -1;
	this.idDestination = -1;
	this.idProduct = -1;
	this.permission = -1;
    }
    
    /**
     * 
     * @param idPermission
     * 		tipo de destino.
     * @param idDepartament
     * 		id del departamento o usuario.
     * @param idProduct
     * 		id del producto.
     * @param permission
     * 		permisos.
     */
    public Permission(int idPermission, int idDepartament, int idProduct,
	int permission) {
	this.idPermission = idPermission;
	this.idDestination = idDepartament;
	this.idProduct = idProduct;
	this.permission = permission;
    }

    /**
     * Retorna el valor de idPermission.
     * 
     * @return Valor de idPermission.
     */
    public int getIdPermission() {
        return idPermission;
    }
    
    /**
     * Establece el valor de idPermission.
     * 
     * @param idPermission
     *            Valor de idPermission.
     */
    public void setIdPermission(
        int idPermission) {
        this.idPermission = idPermission;
    }
    

    /**
     * Retorna el valor de idProduct.
     * 
     * @return Valor de idProduct.
     */
    public int getIdProduct() {
        return idProduct;
    }
    
    /**
     * Establece el valor de idProduct.
     * 
     * @param idProduct
     *            Valor de idProduct.
     */
    public void setIdProduct(
        int idProduct) {
        this.idProduct = idProduct;
    }
    
    /**
     * Retorna el valor de permission.
     * 
     * @return Valor de permission.
     */
    public int getPermission() {
        return permission;
    }
    
    /**
     * Establece el valor de permission.
     * 
     * @param permission
     *            Valor de permission.
     */
    public void setPermission(
        int permission) {
        this.permission = permission;
    }

    /**
     * Retorna el valor de idDestination.
     * 
     * @return Valor de idDestination.
     */
    public int getIdDestination() {
        return idDestination;
    }

    /**
     * Establece el valor de idDestination.
     * 
     * @param idDestination
     *            Valor de idDestination.
     */
    public void setIdDestination(
        int idDestination) {
        this.idDestination = idDestination;
    }


}
