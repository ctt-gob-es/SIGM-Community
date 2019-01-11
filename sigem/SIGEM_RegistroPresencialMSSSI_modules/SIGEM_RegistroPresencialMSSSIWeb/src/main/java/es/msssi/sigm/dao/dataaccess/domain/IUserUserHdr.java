package es.msssi.sigm.dao.dataaccess.domain;


public class IUserUserHdr  {
    private long id;
    private String name;
    private String password;
    private long deptip;
    private int stat;
    private int flags;
    
    private String iuseruserdata_surname;

    /**
     * Constructor.
     *
     */
    public IUserUserHdr() {
    }
    
	public IUserUserHdr(long id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public IUserUserHdr(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}
 
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getDeptip() {
		return deptip;
	}

	public void setDeptip(long deptip) {
		this.deptip = deptip;
	}

	public int getStat() {
		return stat;
	}

	public void setStat(int stat) {
		this.stat = stat;
	}

  /**
   * @param flags the flags to set
   */
  public void setFlags(int flags) {
    this.flags = flags;
  }

  /**
   * @return the flags
   */
  public int getFlags() {
    return flags;
  }

  /**
   * @param iuseruserdata_surname the iuseruserdata_surname to set
   */
  public void setSurname(String iuseruserdata_surname) {
    this.iuseruserdata_surname = iuseruserdata_surname;
  }

  /**
   * @return the iuseruserdata_surname
   */
  public String getSurname() {
    return iuseruserdata_surname;
  }
}