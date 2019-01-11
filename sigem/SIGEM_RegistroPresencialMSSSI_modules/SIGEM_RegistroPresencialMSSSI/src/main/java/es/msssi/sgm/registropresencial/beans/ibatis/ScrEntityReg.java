package es.msssi.sgm.registropresencial.beans.ibatis;



public class ScrEntityReg {

   
	/** identifier field */
    private Integer id;

    /** persistent field */
    private String code;

    /** persistent field */
    private String name;

    /**persistent fiel**/
    private Integer idOfi;
 


   

    /** full constructor */
    public ScrEntityReg(Integer id, String code, String name, int idOfic) {
        this.id = id;
        this.code = code;      
        this.name = name;
        
    }

    /** default constructor */
    public ScrEntityReg() {
    }
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   
    
    
   
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    

    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

   

   
    public Integer getIdOfi() {
		return idOfi;
	}

	public void setIdOfi(Integer idOfi) {
		this.idOfi = idOfi;
	}

                               


}
