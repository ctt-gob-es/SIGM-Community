package es.dipucr.modulos.buscador.beans;

import org.apache.struts.action.ActionForm;

public class SearchForm extends ActionForm
{
  private static final long serialVersionUID = 1L;
  private String cotejo1;
  private String cotejo2;
  private String cotejo3;
  private String cotejo4;
  private String nombre;

  public String getCotejo1()
  {
    return this.cotejo1;
  }
  public void setCotejo1(String cotejo1) {
    this.cotejo1 = cotejo1;
  }
  public String getCotejo2() {
    return this.cotejo2;
  }
  public void setCotejo2(String cotejo2) {
    this.cotejo2 = cotejo2;
  }
  public String getCotejo3() {
    return this.cotejo3;
  }
  public void setCotejo3(String cotejo3) {
    this.cotejo3 = cotejo3;
  }
  public String getCotejo4() {
    return this.cotejo4;
  }
  public void setCotejo4(String cotejo4) {
    this.cotejo4 = cotejo4;
  }
  public String getNombre() {
    return this.nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
}
