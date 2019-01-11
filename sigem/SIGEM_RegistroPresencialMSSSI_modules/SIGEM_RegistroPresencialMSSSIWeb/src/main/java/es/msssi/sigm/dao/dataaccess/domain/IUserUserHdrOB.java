package es.msssi.sigm.dao.dataaccess.domain;

import java.util.Date;

public class IUserUserHdrOB {
  private long id;
  private int flag;
  private Date obligation_date;
  private String app;

  /**
   * Constructor.
   * 
   */
  public IUserUserHdrOB() {
  }

  public IUserUserHdrOB(long id, int flag, Date obligation_date, String app) {
    this.id = id;
    this.flag = flag;
    this.obligation_date = obligation_date;
    this.app = app;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getFlag() {
    return flag;
  }

  public void setFlag(int flag) {
    this.flag = flag;
  }

  public Date getObligation_Date() {
    return obligation_date;
  }

  public void setObligation_Date(Date obligation_date) {
    this.obligation_date = obligation_date;
  }

  public String getApp() {
    return app;
  }

  public void setApp(String app) {
    this.app = app;
  }
}