package es.msssi.sgm.registropresencial.beans.ibatis;

import java.io.Serializable;

/**
 * Bean para la tabla de perfiles.
 * 
 * @author cmorenog
 */
public class Profile  implements Serializable {

    private static final long serialVersionUID = -5286803184764261549L;
    /**
     * valor del identificador del usuario al que se asigna el perfil.
     */
    private int idUser;
    /**
     * valor del identificador del producto al que se asigna el perfil.
     */
    private int idProduct;
    /**
     * valor del identificador del tipo de perfil.
     */
    private int idProfileType;
    
    /**
     * Constructor.
     */
    public Profile (){
	this.idUser = -1;
	this.idProduct = -1;
	this.idProfileType = -1;
    }
    /**
     * Constructor.
     * @param idUser
     * 		idUser.
     * @param idProduct
     * 		idProducto.
     * @param idProfileType
     * 		idPerfil.
     */
    public Profile (int idUser, int idProduct, int idProfileType){
	this.idUser = idUser;
	this.idProduct = idProduct;
	this.idProfileType = idProfileType;
    }
    /**
     * Retorna el valor de idUser.
     * 
     * @return Valor de idUser.
     */
    public int getIdUser() {
        return idUser;
    }
    /**
     * Establece el valor de idUser.
     * 
     * @param idUser
     *            Valor de idUser.
     */
    public void setIdUser(
        int idUser) {
        this.idUser = idUser;
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
     * Retorna el valor de idProfileType.
     * 
     * @return Valor de idProfileType.
     */
    public int getIdProfileType() {
        return idProfileType;
    }
    /**
     * Establece el valor de idProfileType.
     * 
     * @param idProfileType
     *            Valor de idProfileType.
     */
    public void setIdProfileType(
        int idProfileType) {
        this.idProfileType = idProfileType;
    }
    
    
}
