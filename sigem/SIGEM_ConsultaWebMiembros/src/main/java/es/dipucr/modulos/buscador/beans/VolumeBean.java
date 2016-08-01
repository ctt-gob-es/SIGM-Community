package es.dipucr.modulos.buscador.beans;

public class VolumeBean
{
  String loc;
  int filesize;
  String volName;
  String conInfo;

  public VolumeBean()
  {
    this.loc = null;
    this.filesize = 0;
    this.volName = null;
    this.conInfo = null;
  }

  public String getLoc() {
    return this.loc;
  }

  public void setLoc(String loc) {
    this.loc = loc;
  }

  public int getFilesize() {
    return this.filesize;
  }

  public void setFilesize(int filesize) {
    this.filesize = filesize;
  }

  public String getVolName() {
    return this.volName;
  }

  public void setVolName(String volName) {
    this.volName = volName;
  }

  public String getConInfo() {
    return this.conInfo;
  }

  public void setConInfo(String conInfo) {
    this.conInfo = conInfo;
  }
}
