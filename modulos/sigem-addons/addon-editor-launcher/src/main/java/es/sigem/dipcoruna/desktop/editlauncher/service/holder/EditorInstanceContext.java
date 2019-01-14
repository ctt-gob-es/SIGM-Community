package es.sigem.dipcoruna.desktop.editlauncher.service.holder;

public class EditorInstanceContext {    
    private boolean mostrarAvisoGuardado = false;
    private boolean guardarFicheroSinAviso = true;
    private boolean ficheroDirty = false;

    public boolean isMostrarAvisoGuardado() {
        return mostrarAvisoGuardado;
    }

    public void setMostrarAvisoGuardado(boolean mostrarAvisoGuardado) {
        this.mostrarAvisoGuardado = mostrarAvisoGuardado;
    }

    public boolean isGuardarFicheroSinAviso() {
        return guardarFicheroSinAviso;
    }

    public void setGuardarFicheroSinAviso(boolean guardarFicheroSinAviso) {
        this.guardarFicheroSinAviso = guardarFicheroSinAviso;
    }

    public boolean isFicheroDirty() {
        return ficheroDirty;
    }

    public void setFicheroDirty(boolean ficheroDirty) {
        this.ficheroDirty = ficheroDirty;
    }
}
