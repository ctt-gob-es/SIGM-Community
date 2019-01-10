package ieci.tecdoc.sgm.ct.database;

import ieci.tecdoc.sgm.ct.database.datatypes.FicheroPropuesta;
import ieci.tecdoc.sgm.ct.database.datatypes.Propuesta;
import ieci.tecdoc.sgm.ct.database.datatypes.PropuestaImpl;
import ieci.tecdoc.sgm.ct.database.datatypes.Propuestas;
import ieci.tecdoc.sgm.ct.database.exception.DbErrorCodes;
import ieci.tecdoc.sgm.ct.database.exception.DbException;
import ieci.tecdoc.sgm.ct.exception.ConsultaCodigosError;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.apache.log4j.Logger;

public class PropuestaDatos extends PropuestaImpl implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InteresadoDatos.class);
	  protected boolean isDebugeable = true;
	  
	  /**
	   * Constructor de clase
	   */
	  
		public PropuestaDatos() {
		}

		/**
		   * Constructor de clase
		   * 
		   * @param interesado del que toma las propiedades
		   */
		public PropuestaDatos(PropuestaImpl propuesta) {
			
			this.setDni(propuesta.getDni());
			this.setNumeroExpediente(propuesta.getNumeroExpediente());
			this.setDocumentosPropuesta(propuesta.getDocumentosPropuesta());
		}
	  
	  
	  /**
	   * [Ticket #431 TCG] Realiza la consulta por NIF, comprobando si es miembro y el expediente esta abierto.
	   *
	   * @param dbCon Objeto conexión a base de datos.
	   * @throws DbException Si se produce algún error.
	   */
	public void cargarPropuestas(String entidad, String numexp)throws ConsultaExcepcion {

		  if (isDebugeable)
			  logger.debug("cargarExpedientesPorNIF >> NIF: " + this.dni);
		  
		  Connection con = null;
		  Statement stmt = null;
	      ResultSet rsetIntegrante = null;
	      ResultSet rsetPropuesta = null;
	      
	      Statement stmtPropUrgencia = null; 
		  
		  /**
		   * Comprobación del participante si pertenece al procedimiento 'Gestion de integrante' 
		   * comprobando que no haya sido cerrado el expediente y ademas que no haya cesado.
		   * **/
		  
	      /*StringBuffer sQueryPropuestas = new StringBuffer("select exp_rel.numexp_padre, exp.asunto " +
	      		"from spac_exp_relacionados exp_rel, spac_expedientes exp " +
	      		"where exp_rel.numexp_hijo='"+numexp+"' and exp_rel.relacion LIKE '%Propuesta' and exp.numexp=exp_rel.numexp_padre");*/
	      
		  /*StringBuffer sQueryPropuestas = new StringBuffer( "select exp_rel.numexp_padre, propu.extracto " +
		  		"from spac_exp_relacionados exp_rel, secr_propuesta propu " +
		  		"where exp_rel.numexp_hijo='"+numexp+"' and " +
		  		"exp_rel.numexp_padre=propu.numexp and " +
		  		"exp_rel.relacion LIKE '%Propuesta' " +
		  		"order by exp_rel.id");*/
	      
	      /*StringBuffer sQueryPropuestas = new StringBuffer("select distinct(rel.numexp_padre), prop.extracto, exp.asunto, prop.orden " +
	      		"from spac_expedientes exp, spac_exp_relacionados rel " +
	      		"left join secr_propuesta prop " +
	      		"on rel.numexp_padre = prop.numexp where rel.numexp_hijo = '"+numexp+"' " +
	      		"and ((prop.orden is not null and prop.numexp_origen = rel.numexp_hijo) or " +
	      		"(prop.orden is null)) and exp.numexp = rel.numexp_padre order by prop.orden");*/
	      
	      
	      StringBuffer sQueryPropuestas = 	 new StringBuffer("select numexp_origen, extracto, extracto, orden from secr_propuesta where numexp='"+numexp+"' order by orden");	
		  
	      /**Ticket #767# [Teresa] SIGEM Orgános Colegiados ordenar las propuestas por el orden de la convocatoria.**/
	      /**StringBuffer sQueryPropuestas = new StringBuffer( "select numexp_origen, extracto from secr_propuesta where " +
	      		"numexp = '"+numexp+"' order by orden");**/
		  logger.warn("sQueryPropuestas"+sQueryPropuestas);
		  
		  //Para cada propuesta
		  StringBuffer sQueryProp = new StringBuffer("select descripcion, infopag_rde, infopag, ffirma, id " +
		  		"from spac_dt_documentos " +
		  		"where (faprobacion IS not null or extension IN ('pdf', 'zip', 'mp3', 'MP3', 'mp4')) and numexp=? order by descripcion");
		  logger.warn("sQueryProp"+sQueryProp);
		  boolean urgencias = false;

		  try {
			  
			  con = DBSessionManager.getSessionTramitador(entidad);
			  stmt = con.createStatement ();
			  stmtPropUrgencia = con.createStatement ();
			  rsetIntegrante = stmt.executeQuery(sQueryPropuestas.toString());
			  if(this.propuestas == null){
				  this.propuestas = new Propuestas();
		       }
			  
			  while (rsetIntegrante.next()){				  
				  PreparedStatement pstaPropuesta = con.prepareStatement(sQueryProp.toString());
				  String numexpPropuestaRela = "";
				  if(!urgencias){
					  numexpPropuestaRela = rsetIntegrante.getString(1);
				  }
				  else{
					  numexpPropuestaRela = rsetIntegrante.getString(5);
				  }
				  //Compruebo que esa propuesta no sea de urgencia
				  /*StringBuffer sPropuestaUrgencia = new StringBuffer("select * " +
						  "from secr_propuesta " +
						  "where numexp='"+numexp+"' and numexp_origen='"+numexpPropuestaRela+"'");	*/		  
				 
				  
				  pstaPropuesta.setString(1, numexpPropuestaRela);
				  logger.warn("numexpPropuestaRela "+numexpPropuestaRela);
				  
				  Propuesta propuesta = new Propuesta();
				  
				  String extracto = "";
				  if(rsetIntegrante.getString(2)!=null){
					  extracto = rsetIntegrante.getString(2);
				  }
				  else{
					  extracto = rsetIntegrante.getString(3);
				  }

				  logger.warn("extracto "+extracto);				  
				  


				  if(!urgencias){
					  propuesta.setExtracto(extracto);
				  }
				  else{
					  propuesta.setExtracto("--------------------POSIBLE URGENCIA------------------\n\n\t"+extracto);
				  }
  
				  propuesta.setNumexp(numexpPropuestaRela);
				 
				  
				  rsetPropuesta = pstaPropuesta.executeQuery();
				  Vector propuestasDocumentos = new Vector();
				  while (rsetPropuesta.next()) {
					  logger.warn("extracto. "+rsetPropuesta.getString(1));
					  FicheroPropuesta ficheros = new FicheroPropuesta();
					  String nombrePropuesta = limpiarCaracteresEspeciales(rsetPropuesta.getString(1));
					  if(nombrePropuesta.length()>=50){
						  ficheros.setTitulo(nombrePropuesta.substring(0, 50));
					  }
					  else{
						  ficheros.setTitulo(nombrePropuesta);
					  }
					  
					  String guid = rsetPropuesta.getString(2);
					  if(guid != null){
						  ficheros.setGuid(guid);
						  ficheros.setFechaFirma(rsetPropuesta.getDate(4));
					  }
					  else{
						  ficheros.setGuid(rsetPropuesta.getString(3));
					  }
					  ficheros.setId(rsetPropuesta.getInt(5));
					  propuestasDocumentos.add(ficheros);		          
				  }
				  
				  
				  comprobarExpContratacion(con, propuestasDocumentos, numexpPropuestaRela);			 
				  
				  
				  propuesta.setDocumentosPropuestas(propuestasDocumentos);
				  this.propuestas.add(propuesta); 
				  
				  logger.warn("rsetIntegrante.isLast() "+rsetIntegrante.isLast());
				  if(rsetIntegrante.isLast() && !urgencias){
					  urgencias = true;
					  sQueryPropuestas = 	 new StringBuffer("select numexp_origen, extracto, extracto, orden, numexp from secr_propuesta where numexp in "
					  		+ "( select numexp_padre from spac_exp_relacionados where numexp_hijo='"+numexp+"' and numexp_padre not in ("
					  		+ "select numexp_origen from secr_propuesta where numexp='"+numexp+"' order by orden))");
					  logger.warn("sPropuestaUrgencia "+sQueryPropuestas.toString());
					  rsetIntegrante = stmt.executeQuery(sQueryPropuestas.toString());					  
				  }
				  
			  }			  
		  } catch (Exception e) {
			  throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTE +" - "+ e.getMessage(), e);

		  } finally {
			  try{
				 
				  if(con != null)
					  con.close();
				  
			  }catch(Exception ee){
				  DbException DbEx = new DbException(DbErrorCodes.CT_CERRAR_CONEXION, ee);
				  logger.error(this, DbEx.getCause());
				  throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_EXPEDIENTE +" - "+ ee.getMessage(), ee);
			  }
		  }
	  }
	
	public static String limpiarCaracteresEspeciales(String input) {

		 // Cadena de caracteres original a sustituir.
		 String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
		 // Cadena de caracteres ASCII que reemplazarán los originales.
		 String ascii    = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
		 
		 /*input = input.replace(" ", "");
		 input = input.replace("º", "");
		 input = input.replace("-", "");
		 input = input.replace("_", "");
		 input = input.replace("(", "");
		 input = input.replace(")", "");
		 input = input.replace(".", "");*/
		 
		 input = input.replace("'", "");//[dipucr-Felipe #751] Comilla simple rompe enlace javascript
		 String output = input;
	
		 for (int i=0; i<original.length(); i++) {
			 // Reemplazamos los caracteres especiales.
			 output = output.replace(original.charAt(i), ascii.charAt(i));
		 }
		 return output;
	}

	private void comprobarExpContratacion(Connection con, Vector propuestasDocumentos, String numexpPropuestaRela) throws ConsultaExcepcion {
		/**
		   * [Ticket #943# Teresa INICIO]
		   * CONTRATACION-SIGEM Portal del diputado añadir documento de la Petición de Contratación
		   * **/
		  try{
			  //Compruebo que la propuesta sea de un procedimiento de contratación
			  //y si es así obtengo los documentos de la petición de contratación
			  String sql = "select id_padre, id from spac_ct_procedimientos where id in (select id_pcd from spac_expedientes where numexp in " +
			  		"(select numexp_padre from spac_exp_relacionados where numexp_hijo='"+numexpPropuestaRela+"'));";
			  
			 
			  Statement stmtProcCont = con.createStatement ();
			  ResultSet rsetCTProcedimiento = stmtProcCont.executeQuery(sql);
			  
			  if(rsetCTProcedimiento.next()){
				  int id_padre_procedimiento = rsetCTProcedimiento.getInt(1);
				  //444 -> "Procedimiento de Contratación"
				  //Pruebas 255 -> "Procedimiento Genérico de Contratación"
				  if(id_padre_procedimiento == 444){
					  //Obtengo el número de la propuesta de contratacion
					  StringBuffer queryPropuestaContrat = new StringBuffer("select numexp_padre from spac_exp_relacionados where numexp_hijo in " +
					  		"(select numexp_padre from spac_exp_relacionados where numexp_hijo='"+numexpPropuestaRela+"')");
					  
					  Statement stmtPropCont = con.createStatement ();
					  ResultSet rsetPropContr = stmtPropCont.executeQuery(queryPropuestaContrat.toString());
					  if(rsetPropContr.next()){
						  //Número de la propuesta contratacion
						  String numexpPropCont = rsetPropContr.getString(1);
						  
						  //obtengo datos del trámite Informe Necesidad Contrato para obtener los documentos
						  StringBuffer queryTramProcCont = new StringBuffer("select id_fase, id_tramite_bpm from spac_p_tramites where NOMBRE = 'Informe Necesidad Contrato' " +
						  		"and id_pcd IN (select id from spac_ct_procedimientos where id in " +
						  		"(select id_pcd from spac_expedientes where numexp in " +
						  		"(select numexp_padre from spac_exp_relacionados where numexp_hijo in " +
						  		"(select numexp_padre from spac_exp_relacionados where numexp_hijo='"+numexpPropuestaRela+"'))))");
						  
						  Statement stmtTramProcCont = con.createStatement ();
						  ResultSet rsetTramProcCont = stmtTramProcCont.executeQuery(queryTramProcCont.toString());
						  
						  if(rsetTramProcCont.next()){
							  int id_fase = rsetTramProcCont.getInt(1);
							  String id_tramite = rsetTramProcCont.getString(2);
							  
							  //con los datos del tramite y el número de expediente de la propuesta obtengo los documentos
							  //que van a ir al portal del diputado.
							  
							  StringBuffer queryDocPropCont = new StringBuffer("select descripcion, infopag_rde, infopag, ffirma, id from spac_dt_documentos where " +
							  		"NUMEXP='"+numexpPropCont+"' AND ID_FASE_PCD="+id_fase+" AND ID_TRAMITE_PCD="+id_tramite+" AND NOMBRE!='Informe Necesidad Contrato' " +
							  		"AND NOMBRE!='Pliego de Prescripciones Técnicas'");
							  
							  Statement stmtDocPropCont = con.createStatement();
							  ResultSet rsetDocPetCont = stmtDocPropCont.executeQuery(queryDocPropCont.toString());
							  
							  while(rsetDocPetCont.next()){
								  FicheroPropuesta ficheros = new FicheroPropuesta();
								  ficheros.setTitulo(rsetDocPetCont.getString(1));
								  String guid = rsetDocPetCont.getString(2);
								  if(guid != null){
									  ficheros.setGuid(guid);
									  ficheros.setFechaFirma(rsetDocPetCont.getDate(4));
								  }
								  else{
									  ficheros.setGuid(rsetDocPetCont.getString(3));
								  }
								  ficheros.setId(rsetDocPetCont.getInt(5));
								  propuestasDocumentos.add(ficheros);
							  }
						  }
						  if(rsetTramProcCont != null) rsetTramProcCont.close();
						  if(stmtTramProcCont != null) stmtTramProcCont.close();
					  }
					  if(stmtPropCont != null) stmtPropCont.close();
					  if(rsetPropContr != null ) rsetPropContr.close();
				  }
			  }
			  
			  if (stmtProcCont!=null) stmtProcCont.close();
			  if(rsetCTProcedimiento != null) rsetCTProcedimiento.close();
			  
			  /**
			   * [Ticket #943# Teresa FIN]
			   * CONTRATACION-SIGEM Portal del diputado añadir documento de la Petición de Contratación
			   * **/
		  }catch(SQLException e){
			  throw new ConsultaExcepcion(ConsultaCodigosError.EC_CARGAR_INTERESADO_EXPEDIENTE+ "numexpPropuestaRela "+numexpPropuestaRela+" - "+e.getMessage(), e);
		  }
	}
}