package es.dipucr.tablonEdictalUnico.quartz.bean;

import java.util.Date;

public class Aviso {
	
	int id_aviso;
	int id_proc;
	int id_resp;
	int tipo_destinatario;
	int id_destinatario;
	String fecha;
	String id_expediente;
	int estado_aviso;
	String mensaje;
	String tipo_aviso;
	int id_mproc;
	String uid_resp;
	String uid_destinatario;
	
	
	public int getId_aviso() {
		return id_aviso;
	}
	public void setId_aviso(int idAviso) {
		id_aviso = idAviso;
	}
	public int getId_proc() {
		return id_proc;
	}
	public void setId_proc(int idProc) {
		id_proc = idProc;
	}
	public int getId_resp() {
		return id_resp;
	}
	public void setId_resp(int idResp) {
		id_resp = idResp;
	}
	public int getTipo_destinatario() {
		return tipo_destinatario;
	}
	public void setTipo_destinatario(int tipoDestinatario) {
		tipo_destinatario = tipoDestinatario;
	}
	public int getId_destinatario() {
		return id_destinatario;
	}
	public void setId_destinatario(int idDestinatario) {
		id_destinatario = idDestinatario;
	}	
	public String getId_expediente() {
		return id_expediente;
	}
	public void setId_expediente(String idExpediente) {
		id_expediente = idExpediente;
	}
	public int getEstado_aviso() {
		return estado_aviso;
	}
	public void setEstado_aviso(int estadoAviso) {
		estado_aviso = estadoAviso;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public String getTipo_aviso() {
		return tipo_aviso;
	}
	public void setTipo_aviso(String tipoAviso) {
		tipo_aviso = tipoAviso;
	}
	public int getId_mproc() {
		return id_mproc;
	}
	public void setId_mproc(int idMproc) {
		id_mproc = idMproc;
	}
	public String getUid_resp() {
		return uid_resp;
	}
	public void setUid_resp(String uidResp) {
		uid_resp = uidResp;
	}
	public String getUid_destinatario() {
		return uid_destinatario;
	}
	public void setUid_destinatario(String uidDestinatario) {
		uid_destinatario = uidDestinatario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
}
