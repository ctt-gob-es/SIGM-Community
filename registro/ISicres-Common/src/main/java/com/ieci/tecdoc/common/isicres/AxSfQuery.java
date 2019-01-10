package com.ieci.tecdoc.common.isicres;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author LMVICENTE
 * @creationDate 13-may-2004 10:32:18
 * @version
 * @since
 */
public class AxSfQuery implements Serializable {

    /*******************************************************************************************************************
     * Attributes
     ******************************************************************************************************************/

    private Integer bookId = null;
    private int pageSize = 0;
    private Map fields = new TreeMap();
    private String whereField9 = " fdrid in (select id_fdr from scr_regint where unaccent(upper(name)) ";
    private String whereIdarchField9 = "' and id_arch=";
    private String whereOprDependOfIn = " in (select id from scr_orgs where type != 0 start with code=";
    private String whereOprDependOfConnect = " connect by prior id = id_father";
    private String sentenceField9 = null;
    private String sentenceField9OrBetween = "";
    private String orderBy = "";
    
    private String whereDistNotRegister = " fdrid in (select id_fdr from scr_distreg where state";
	private String selectDefWhere2 = null;
	private String whereField1001_01_S = " EXISTS (SELECT 'S' FROM a";
	private String whereField1001_01_N = "  NOT EXISTS (SELECT 'S' FROM a";
	private String whereField1001_02 = "xf X WHERE X.FLDID=1001 AND TEXT = 'S' and '1'=? and X.FDRID=R.FDRID ) " ;
	private String whereField507_01 = " EXISTS (SELECT 'S' FROM SCR_EXREG_IN SE WHERE SE.ID_FDR=R.fdrid AND SE.ID_ARCH=";
	private String whereField507_02 = "  AND UPPER(ID_EXCHANGE_SIR)  ";
	private String whereFieldExtend_01 = " EXISTS (SELECT 'S' FROM  A";
	private String whereFieldExtend_02 = "XF XF WHERE XF.FDRID=R.FDRID AND FLDID= ";
	private String whereFieldExtend_03 = " AND UPPER(XF.TEXT)  ";
	/*******************************************************************************************************************
     * Constructors
     ******************************************************************************************************************/

    public AxSfQuery() {

    }

    public AxSfQuery(Integer bookId) {
        this.bookId = bookId;
    }

    /*******************************************************************************************************************
     * Public methods
     ******************************************************************************************************************/

    public String toXML() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<AxSfQuery bookId='");
        buffer.append(bookId);
        buffer.append("'>");
        for (Iterator it = fields.values().iterator(); it.hasNext();) {
            buffer.append(((AxSfQueryField) it.next()).toXML());
        }
        buffer.append("</AxSfQuery>");

        return buffer.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("AxSfQuery bookId [");
        buffer.append(bookId);
        buffer.append("] pageSize [");
        buffer.append(pageSize);
        buffer.append("] ");
        for (Iterator it = fields.values().iterator(); it.hasNext();) {
            buffer.append("\n\t");
            buffer.append(it.next().toString());
        }
        buffer.append("\n");

        return buffer.toString();
    }

    /**
     * @return Returns the querySize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param querySize
     *            The querySize to set.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return Returns the bookId.
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * @param bookId
     *            The bookId to set.
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * @return Returns the fields.
     */
    public Collection getFields() {
        return fields.values();
    }

    
    public void addField(AxSfQueryField field) {
        if (field != null) {
            fields.put(new Integer(fields.size()+1), field);
        }
    }

    /*******************************************************************************************************************
     * Protected methods
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Private methods
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Inner classes
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     * Test brench
     ******************************************************************************************************************/
	/**
	 * @return Returns the whereField9.
	 */
	public String getWhereField9() {
		return whereField9;
	}
	/**
	 * @return Returns the sentenceField9.
	 */
	public String getSentenceField9() {
		return sentenceField9;
	}
	/**
	 * @param sentenceField9 The sentenceField9 to set.
	 */
	public void setSentenceField9(String valueField) {
		this.sentenceField9 = "'" + valueField + whereIdarchField9;;
	}
	/**
	 * @return Returns the sentenceField9OrBetween.
	 */
	public String getSentenceField9OrBetween() {
		return sentenceField9OrBetween;
	}
	/**
	 * @param sentenceField9OrBetween The sentenceField9OrBetween to set.
	 */
	public void initializeSentenceField9OrBetween() {
			sentenceField9OrBetween = "";
	}
	public void setSentenceField9OrBetween(String valueField, String orOrBetween) {
		if (sentenceField9OrBetween.equals("")){
			sentenceField9OrBetween = "'" + valueField + "'";
		} else {
			if (orOrBetween.equals("OR")){
				sentenceField9OrBetween = sentenceField9OrBetween + " OR " + "name='" + valueField;
			} else {
				sentenceField9OrBetween = sentenceField9OrBetween + " and " + "'" + valueField;
			}
		}
	}
	/**
	 * @return Returns the whereIdarchField9.
	 */
	public String getWhereIdarchField9() {
		return whereIdarchField9;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = " ORDER BY " + orderBy;
	}

	/**
	 * @return the whereOprDependOfIn
	 */
	public String getWhereOprDependOfIn() {
		return whereOprDependOfIn;
	}

	/**
	 * @return the whereOprDependOfConnect
	 */
	public String getWhereOprDependOfConnect() {
		return whereOprDependOfConnect;
	}
	
	/**
	 * @return the whereDistNotRegister
	 */
	public String getWhereDistNotRegister() {
		return whereDistNotRegister;
	}

	/**
	 * @param whereDistNotRegister the whereDistNotRegister to set
	 */
	public void setWhereDistNotRegister(String whereDistNotRegister) {
		this.whereDistNotRegister = whereDistNotRegister;
	}

	/**
	 * @return the selectDefWhere2
	 */
	public String getSelectDefWhere2() {
		return selectDefWhere2;
	}

	/**
	 * @param selectDefWhere2 the selectDefWhere2 to set
	 */
	public void setSelectDefWhere2(String selectDefWhere2) {
		this.selectDefWhere2 = selectDefWhere2;
	}
	/**
	 * @return the whereField1001_01_S
	 */
	public String getWhereField1001_01_S() {
	    return whereField1001_01_S;
	}
	/**
	 * @param whereField1001_01_S the whereField1001_01_S to set
	 */
	public void setWhereField1001_01_S(String whereField1001_01_S) {
	    this.whereField1001_01_S = whereField1001_01_S;
	}
	/**
	 * @return the whereField1001_02
	 */
	public String getWhereField1001_02() {
	    return whereField1001_02;
	}
	/**
	 * @param whereField1001_02 the whereField1001_02 to set
	 */
	public void setWhereField1001_02(String whereField1001_02) {
	    this.whereField1001_02 = whereField1001_02;
	}
	
	/**
	 * @return the whereField1001_01_N
	 */
	public String getWhereField1001_01_N() {
	    return whereField1001_01_N;
	}
	/**
	 * @param whereField1001_02_N the whereField1001_01_N to set
	 */
	public void setWhereField1001_01_N(String whereField1001_01_N) {
	    this.whereField1001_01_N = whereField1001_01_N;
	}

	public String getWhereField507_01() {
	    return whereField507_01;
	}

	public void setWhereField507_01(String whereField507_01) {
	    this.whereField507_01 = whereField507_01;
	}

	public String getWhereField507_02() {
	    return whereField507_02;
	}

	public void setWhereField507_02(String whereField507_02) {
	    this.whereField507_02 = whereField507_02;
	}

	public String getWhereFieldExtend_01() {
	    return whereFieldExtend_01;
	}

	public void setWhereFieldExtend_01(String whereFieldExtend_01) {
	    this.whereFieldExtend_01 = whereFieldExtend_01;
	}

	public String getWhereFieldExtend_02() {
	    return whereFieldExtend_02;
	}

	public void setWhereFieldExtend_02(String whereFieldExtend_02) {
	    this.whereFieldExtend_02 = whereFieldExtend_02;
	}

	public String getWhereFieldExtend_03() {
	    return whereFieldExtend_03;
	}

	public void setWhereFieldExtend_03(String whereFieldExtend_03) {
	    this.whereFieldExtend_03 = whereFieldExtend_03;
	}
	
}

