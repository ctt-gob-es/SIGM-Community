package es.dipucr.modulos.buscador.beans;

public class SearchBean
{
  private String codCotejo;
  private String nombre;
  private String numExp;
  private String fechaDoc;
  private String tpReg;
  private String id;
  private String infopag;
  private String nreg;
  private String freg;
  private String origen;
  private String destino;

  public SearchBean()
  {
    this.codCotejo = null;
    this.nombre = null;
    this.numExp = null;
    this.fechaDoc = null;
    this.tpReg = null;

    this.nreg = null;
    this.freg = null;
    this.origen = null;
    this.destino = null;
  }

  public String getCodCotejo()
  {
    return this.codCotejo;
  }

  public void setCodCotejo(String codCotejo) {
    this.codCotejo = codCotejo;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getNumExp() {
    return this.numExp;
  }

  public void setNumExp(String numExp) {
    this.numExp = numExp;
  }

  public String getFechaDoc() {
    return this.fechaDoc;
  }

  public void setFechaDoc(String fechaDoc) {
    this.fechaDoc = fechaDoc;
  }

  public String getTpReg() {
    return this.tpReg;
  }

  public void setTpReg(String tpReg) {
    this.tpReg = tpReg;
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getInfopag() {
    return this.infopag;
  }

  public void setInfopag(String infopag) {
    this.infopag = infopag;
  }

  public String getNreg() {
    return this.nreg;
  }

  public void setNreg(String nreg) {
    this.nreg = nreg;
  }

  public String getFreg() {
    return this.freg;
  }

  public void setFreg(String freg) {
    this.freg = freg;
  }

  public String getOrigen() {
    return this.origen;
  }

  public void setOrigen(String origen) {
    this.origen = origen;
  }

  public String getDestino() {
    return this.destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }
}