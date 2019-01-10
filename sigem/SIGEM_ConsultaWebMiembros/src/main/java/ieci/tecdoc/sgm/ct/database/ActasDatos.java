package ieci.tecdoc.sgm.ct.database;

import ieci.tecdoc.sgm.ct.database.datatypes.Acta;
import ieci.tecdoc.sgm.ct.database.datatypes.Actas;
import ieci.tecdoc.sgm.ct.database.datatypes.ActasImpl;
import ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta;
import ieci.tecdoc.sgm.ct.database.exception.DbErrorCodes;
import ieci.tecdoc.sgm.ct.database.exception.DbException;
import ieci.tecdoc.sgm.ct.exception.ConsultaCodigosError;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;


public class ActasDatos extends ActasImpl{
	
	private static final Logger logger = Logger.getLogger(ActasDatos.class);
	
	  /**
	   * Constructor de clase
	   */
	  
		public ActasDatos() {
		}

		/**
		   * Constructor de clase
		   * 
		   * @param interesado del que toma las propiedades
		   */
		public ActasDatos(ActasImpl actas) {
			
			this.setDni(actas.getDni());
			this.setNumeroExpediente(actas.getNumeroExpediente());
			this.setActas(actas.getActas());
		}
		
		 public void cargarActasPorNIFMiembro(String entidad)throws ConsultaExcepcion {

					  
					  Connection con = null;
					  Statement stmt = null;
					  Statement stmt_h = null;
				      ResultSet rsetIntegrante = null;
				      ResultSet rsetExpediente = null;
				      ResultSet rsetExpediente_h = null;
				      ResultSet rsetSesion = null;
				      
					  
					  /**
					   * Comprobación del participante si pertenece al procedimiento 'Gestion de integrante' 
					   * comprobando que no haya sido cerrado el expediente y ademas que no haya cesado.
					   * **/
					  
					  StringBuffer sQueryIntegrante = new StringBuffer( "select * " +
					  		"from secr_miembro miembro, spac_expedientes exp " +
					  		"where exp.numexp = miembro.numexp " +
					  		"and exp.fcierre is null " +
					  		"and exp.codprocedimiento='CR-SECR-01' " +
					  		"and exp.nifciftitular='"+this.getDni()+"' " +
					  		//"and exp.nifciftitular='05611617P' " +
					  		"and miembro.fecha_cese is null");
					  
					  logger.warn("sQueryIntegrante "+sQueryIntegrante);
					  
					  StringBuffer sQueryExpediente = new StringBuffer("select doc.numexp, doc.infopag_rde, doc.infopag, doc.ffirma, doc.id from spac_expedientes exp "
					  		+ "join spac_dt_tramites tramiteConv on (exp.numexp = tramiteConv.numexp and (upper(tramiteConv.nombre) like 'ACTA CON DEBATES EN AUDIO' or upper(tramiteConv.nombre) like 'ACTA DE PLENO' or upper(tramiteConv.nombre) like 'ACTA DE PLENO CON AUDIO') and tramiteConv.fecha_cierre is not null) "
					  		+ "join spac_dt_intervinientes inter on exp.numexp = inter.numexp and inter.ndoc ='"+this.getDni()+"' "
					  		+ "join spac_dt_documentos doc on tramiteConv.id_tram_exp = doc.id_tramite "
					  		+ "where exp.nombreprocedimiento='Sesión de Pleno' order by doc.faprobacion");
					  
					  logger.warn("sQueryExpediente "+sQueryExpediente);
					  
					  
					  StringBuffer sQueryExpediente_h = new StringBuffer("select doc.numexp, doc.infopag_rde, doc.infopag, doc.ffirma, doc.id from spac_expedientes_h exp "
					  		    + "join spac_dt_tramites_h tramiteConv on (exp.numexp = tramiteConv.numexp and (upper(tramiteConv.nombre) like 'ACTA CON DEBATES EN AUDIO' or upper(tramiteConv.nombre) like 'ACTA DE PLENO' or upper(tramiteConv.nombre) like 'ACTA DE PLENO CON AUDIO') and tramiteConv.fecha_cierre is not null) "
						  		+ "join spac_dt_intervinientes_h inter on exp.numexp = inter.numexp and inter.ndoc ='"+this.getDni()+"' "
						  		+ "join spac_dt_documentos_h doc on tramiteConv.id_tram_exp = doc.id_tramite "
						  		+ "where exp.nombreprocedimiento='Sesión de Pleno' order by doc.faprobacion");
						  
						  logger.warn("sQueryExpediente "+sQueryExpediente_h);
					  
					  StringBuffer sQuerySesion = new StringBuffer ("select fecha, hora " +
					  		"from secr_sesion " +
					  		"where numexp=?");
					  
					  logger.warn("sQuerySesion "+sQuerySesion);
					  
					  

					  try {
						  con = DBSessionManager.getSessionTramitador(entidad);
						  stmt = con.createStatement ();
						  stmt_h = con.createStatement ();

						  rsetIntegrante = stmt.executeQuery(sQueryIntegrante.toString());

						  if(rsetIntegrante.next()){
							  Actas actas = new Actas();							  
							  rsetExpediente = stmt.executeQuery(sQueryExpediente.toString());
							  rsetExpediente_h = stmt_h.executeQuery(sQueryExpediente_h.toString());
							  
							  boolean expedientesH = false;
							  	  
							  while (rsetExpediente.next()) {
								  Acta acta = new Acta();
								  String numexpActa = rsetExpediente.getString(1);
						          PreparedStatement pstaSesion = con.prepareStatement(sQuerySesion.toString());
						          pstaSesion.setString(1, numexpActa);
						          rsetSesion = pstaSesion.executeQuery();
								  String fecha = "";
								  String hora = "";
						          while (rsetSesion.next()) { 
									DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
									fecha = formatter.format(rsetSesion.getDate(1));
									hora = rsetSesion.getString(2);
									acta.setFechafirma(rsetSesion.getDate(1));
						          }

						          acta.setNumexp(numexpActa);
						          acta.setExtracto(numexpActa+" - "+"Acta "+fecha+ " "+hora);
						          
								  FicheroPropuesta ficheros = new FicheroPropuesta();
								  ficheros.setTitulo("Acta "+fecha+ " "+hora);
								  String guid = rsetExpediente.getString(2);
								  if(guid != null){
									  ficheros.setGuid(guid);
									  ficheros.setFechaFirma(rsetExpediente.getDate(4));
								  }
								  else{
									  ficheros.setGuid(rsetExpediente.getString(3));
								  }
								  ficheros.setId(rsetExpediente.getInt(5));
								  logger.warn("Acta "+fecha+ " "+hora);
								  acta.setDocumentoActa(ficheros);
								  actas.add(acta);								  
								  
								  if(rsetExpediente.isLast()){
									  rsetExpediente = rsetExpediente_h;
									  expedientesH = true;
								  }
							  }
							  
							  //Si es false es que no hay expediente abiertos y estan todos en h por lo tanto no ha entrado en el while anterior
							  if(!expedientesH){
								  while (rsetExpediente_h.next()) {
									  Acta acta = new Acta();
									  String numexpActa = rsetExpediente_h.getString(1);
							          PreparedStatement pstaSesion = con.prepareStatement(sQuerySesion.toString());
							          pstaSesion.setString(1, numexpActa);
							          rsetSesion = pstaSesion.executeQuery();
									  String fecha = "";
									  String hora = "";
							          while (rsetSesion.next()) { 
										DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
										fecha = formatter.format(rsetSesion.getDate(1));
										hora = rsetSesion.getString(2);
										acta.setFechafirma(rsetSesion.getDate(1));
							          }

							          acta.setNumexp(numexpActa);
							          acta.setExtracto(numexpActa+" - "+"Acta "+fecha+ " "+hora);
							          
									  FicheroPropuesta ficheros = new FicheroPropuesta();
									  ficheros.setTitulo("Acta "+fecha+ " "+hora);
									  String guid = rsetExpediente_h.getString(2);
									  if(guid != null){
										  ficheros.setGuid(guid);
										  ficheros.setFechaFirma(rsetExpediente_h.getDate(4));
									  }
									  else{
										  ficheros.setGuid(rsetExpediente_h.getString(3));
									  }
									  ficheros.setId(rsetExpediente_h.getInt(5));
									  logger.warn("Acta "+fecha+ " "+hora);
									  acta.setDocumentoActa(ficheros);
									  actas.add(acta);								  
								  }
							  }
							  
							  Collections.sort(actas, new Comparator<Acta>() {
								  public int compare(Acta p1, Acta p2) {
										// Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
										return p2.getFechafirma().compareTo(p1.getFechafirma());
									}
								  
							  });
							  
							  
							  this.setActas(actas);
							  
						  }			  
					  } catch (Exception e) {
						  throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_INTERESADO_EXPEDIENTE+ " "+ e.getMessage(), e);

					  } finally {
						  try{
							 
							  if(con != null){
								  rsetIntegrante.close();
								  rsetExpediente.close();
								  rsetExpediente_h.close();
								  stmt_h.close();
								  stmt.close();
								  con.close();
							  }
						  }catch(Exception ee){
							  DbException DbEx = new DbException(DbErrorCodes.CT_CERRAR_CONEXION);
							  logger.warn(this, DbEx.getCause());
						  }
					  }
				  }
	  


}
