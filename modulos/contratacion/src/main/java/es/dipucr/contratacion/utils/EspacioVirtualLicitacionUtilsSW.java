package es.dipucr.contratacion.utils;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.terceros.dto.DireccionPostal;
import ieci.tecdoc.sgm.core.services.terceros.dto.Tercero;
import ieci.tecdoc.sgm.core.services.tramitacion.ServicioTramitacion;
import ieci.tecdoc.sgm.core.services.tramitacion.dto.DocumentoExpediente;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.objeto.Adjudicatario;
import es.dipucr.contratacion.objeto.sw.Campo;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.DatosEmpresa;
import es.dipucr.contratacion.objeto.sw.DatosLicitacion;
import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.DepartamentosContacto;
import es.dipucr.contratacion.objeto.sw.DiariosOficiales;
import es.dipucr.contratacion.objeto.sw.Documento;
import es.dipucr.contratacion.objeto.sw.EspacioVirtualLicitacionBean;
import es.dipucr.contratacion.objeto.sw.Garantia;
import es.dipucr.contratacion.objeto.sw.LicitadorBean;
import es.dipucr.contratacion.objeto.sw.Lotes;
import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.contratacion.objeto.sw.SobreElectronico;
import es.dipucr.contratacion.objeto.sw.Solvencia;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.common.utils.SecretariaUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class EspacioVirtualLicitacionUtilsSW {

	public static final Logger LOGGER = Logger.getLogger(EspacioVirtualLicitacionUtilsSW.class);
	
	public static final String DESC = " DESC";
	
	public static void crearLicitadorAdjudicatario(IClientContext cct, Adjudicatario[] adjudicatarios, es.dipucr.contratacion.objeto.Peticion peticPlace) throws ISPACRuleException{
		try{
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			
			IItemCollection procedimientosDelDepartamento = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_CT_PROCEDIMIENTOS, "WHERE COD_PCD = 'LICITADOR'");
			Iterator <?> procsIterator = procedimientosDelDepartamento.iterator();
			int idCtProcedimientoNuevo = 0;
			if(procsIterator.hasNext()){
				IItem procs = (IItem) procsIterator.next();
				idCtProcedimientoNuevo = procs.getInt("ID");
				
				for (int i = 0; i<adjudicatarios.length; i++){					
					Adjudicatario adjudicatario = adjudicatarios[i];
					boolean lote = false;
					String numexp = peticPlace.getExpediente();
					if(StringUtils.isNotEmpty(adjudicatario.getLote())){
						lote = true;
					}
					if(lote){
						IItemCollection otRela = ExpedientesRelacionadosUtil.getExpedientesByRelacion(entitiesAPI, numexp, "'Lote%'");		
						Iterator<IItem> itRela = otRela.iterator();
						while(itRela.hasNext()){
							IItem itemLote = itRela.next();
							if(StringUtils.isNotEmpty(itemLote.getString("NUMEXP_HIJO")))numexp = itemLote.getString("NUMEXP_HIJO");
							DipucrFuncionesComunesSW.creacionLicitador(cct, idCtProcedimientoNuevo, numexp, adjudicatario, peticPlace.getEntidad());
						}						
					}
					else{
						DipucrFuncionesComunesSW.creacionLicitador(cct, idCtProcedimientoNuevo, numexp, adjudicatario, peticPlace.getEntidad());
					}
				}
			}
			
		} catch (ISPACException e) {
			LOGGER.error("Error Entidad " +peticPlace.getEntidad() + "; Expediente "+ peticPlace.getExpediente() +" - " + e.getMessage(),e);
			throw new ISPACRuleException("Error Entidad " +peticPlace.getEntidad() + "; Expediente "+ peticPlace.getExpediente() +" - " + e.getMessage(),e);
		} 
		
	}	
	
	@SuppressWarnings("unchecked")
	public static EspacioVirtualLicitacionBean getEspacioVirtualLicitacionBeanSW(IClientContext cct, String numexp, IItem datosTram) throws ISPACException {
		EspacioVirtualLicitacionBean espacioVirtualLicitacion = new EspacioVirtualLicitacionBean();
		
		try{
			//Num Expediente
			espacioVirtualLicitacion.setNumexp(numexp);
			
			DatosContrato datosContrato = DipucrFuncionesComunesSW.getDatosContrato(cct, numexp);
			if(datosContrato!=null){
				espacioVirtualLicitacion.setDatosContrato(datosContrato);
				//Compruebo que el tipo de contrato no sea Patrimonial
				//Porque si no coge los datos del presupuesto de la entidad datos del contrato
				if(datosContrato.getTipoContrato()!= null && !datosContrato.getTipoContrato().getId().equals("50")){
					Peticion peticion = DipucrFuncionesComunesSW.getPeticion(cct, numexp);
					datosContrato.setPresupuestoConImpuesto(peticion.getPresupuestoConIva());
					datosContrato.setPresupuestoSinImpuesto(peticion.getPresupuestoSinIva());
				}
				else{
					if(datosContrato.getPresupuestoConImpuesto()!= null){
						datosContrato.setPresupuestoConImpuesto(datosContrato.getPresupuestoConImpuesto());
					}
					if(datosContrato.getPresupuestoSinImpuesto()!=null){
						datosContrato.setPresupuestoSinImpuesto(datosContrato.getPresupuestoSinImpuesto());
					}
				}
			}
			
			DiariosOficiales diariosOficiales = DipucrFuncionesComunesSW.getDiariosOficiales(cct, numexp);			
			if(diariosOficiales!=null){
				espacioVirtualLicitacion.setDiariosOficiales(diariosOficiales);			
			}
			
			
			
			DatosEmpresa datosEmpresa = DipucrFuncionesComunesSW.getDatosEmpresa(cct, numexp);
			if(datosEmpresa!=null){
				espacioVirtualLicitacion.setDatosEmpresa(datosEmpresa);
			}			

			DatosLicitacion datosLicitacion = DipucrFuncionesComunesSW.getDatosLicitacion(cct, numexp);
			if(datosLicitacion!=null){
				espacioVirtualLicitacion.setDatosLicitacion(datosLicitacion);
			}
			
			SobreElectronico [] sobreElect = DipucrFuncionesComunesSW.getSobreElec(cct, numexp);
			if(sobreElect!=null){
				espacioVirtualLicitacion.setSobreElectronico(sobreElect);
			}			
			
			Solvencia solvencia = DipucrFuncionesComunesSW.getSolvencia(cct, numexp);
			if(solvencia!=null){
				espacioVirtualLicitacion.setSolvencia(solvencia);
			}
			
			//garantias
			Garantia[] garantia = DipucrFuncionesComunesSW.getGarantias(cct, numexp);
			if(garantia!=null){
				espacioVirtualLicitacion.setGarantia(garantia);
			}
			
			
			//Personal contacto
			DepartamentosContacto personal = DipucrFuncionesComunesSW.getPersonalContacto(cct, numexp);
			if(personal!=null){
				espacioVirtualLicitacion.setDepartamentosContacto(personal);
				if(datosLicitacion.getOrganoAsistencia()!=null){
					if(personal.getPersonalContactoSecretaria()!=null){
						datosLicitacion.getOrganoAsistencia().setInformacionOC(personal.getPersonalContactoSecretaria());
					}
					
				}
			}
			
			
			//Licitadores
			LicitadorBean[] licitadores = DipucrFuncionesComunesSW.getLicitadores(cct, numexp);
			if(licitadores!=null && licitadores.length>0){
				espacioVirtualLicitacion.setLicitadores(licitadores);
			}
			
			Documento docPCEA = getDocumentoPliegos(cct, numexp, "PCEA", datosContrato.getOrganoContratacion());
			if(docPCEA!=null){
				espacioVirtualLicitacion.setDocumentoPCAP(docPCEA);
			}
			Documento docPPT = getDocumentoPliegos(cct, numexp, "PPT", datosContrato.getOrganoContratacion());
			if(docPPT!=null){
				espacioVirtualLicitacion.setDocumentoPPT(docPPT);
			}
			Documento[] docAnexoPliego = DipucrFuncionesComunesSW.docInformacionAdicionalPliego(cct,numexp, datosContrato.getOrganoContratacion());
			if(docAnexoPliego!=null && docAnexoPliego.length>0){
				espacioVirtualLicitacion.setDocAnexoPliegoAdicionales(docAnexoPliego);
			}			
						
			//Lotes
			Lotes lotes = DipucrFuncionesComunesSW.getLotes(cct, numexp);
			if(lotes!=null){
				espacioVirtualLicitacion.setLotes(lotes);
			}
			
			DatosTramitacion datosTramitacion = DipucrFuncionesComunesSW.getDatosTramitacion(cct, numexp, datosTram);
			if(datosTramitacion!=null){
				espacioVirtualLicitacion.setDatosTramitacion(datosTramitacion);
				
				Campo codigoAdjudicacion = DipucrFuncionesComunesSW.getResultadoLicitacion(cct, numexp);
				if(codigoAdjudicacion!=null){
					datosTramitacion.setCodigoAdjudicacion(codigoAdjudicacion);
				}
				
				//AnuncioAdjudicación -> decreto o acuerdo			
				Documento documento = null;
				boolean encontrado = false;
				IItemCollection itCollContrato = ExpedientesRelacionadosUtil.getExpedientesByRelacion(cct.getAPI().getEntitiesAPI(), numexp, ExpedientesRelacionadosUtil.RELACIONPORTAFIRMA);
				if(itCollContrato!=null){
					Iterator<?> itFirmaContrato = itCollContrato.iterator();
					if(itFirmaContrato.hasNext()){
						IItem firmaContrato = (IItem)itFirmaContrato.next();
						String numexpFirmaContrato = null;
						if(StringUtils.isNotEmpty(firmaContrato.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO)))numexpFirmaContrato = firmaContrato.getString(ExpedientesRelacionadosUtil.NUMEXP_HIJO);
						IItemCollection itDoc = DocumentosUtil.getDocumentos(cct, numexpFirmaContrato, "NOMBRE='Documento Firmado'", "FAPROBACION DESC");
						Iterator<IItem> iteratorDoc = itDoc.iterator();
						if(iteratorDoc.hasNext()){	
							IItem itemDocumento = iteratorDoc.next();
							if(itemDocumento!=null){
								documento = DipucrFuncionesComunesSW.getDocumento(cct, itemDocumento, "Contrato", numexp);
								documento.setIdTypeDoc("ACTA_FORM");
								documento.setTypeDoc("Documento de Acta de Formalización");
								encontrado =true;
							}
							
						}
					}
				}
				if(!encontrado){
					IItem acuerdo = null;
					if(datosContrato.getOrganoContratacion().equals("PLEN")){
						String numExpPropuesta = SecretariaUtil.getUltimoNumexpPropuesta(cct, numexp);
						if(StringUtils.isNotEmpty(numExpPropuesta)){
							acuerdo = SecretariaUtil.getDocAcuerdoPlenJGByNumExpPropuesta(cct, numExpPropuesta);
						}
					}
					if(datosContrato.getOrganoContratacion().equals("DEC")){
						String numexpDecreto = DecretosUtil.getUltimoNumexpDecreto(cct, numexp);
						if(StringUtils.isNotEmpty(numexpDecreto)){
							acuerdo = DecretosUtil.getDocDecretoByNumExpDecreto(cct, numexpDecreto);
						}
					}
					
					if(acuerdo!=null){						
						documento = DipucrFuncionesComunesSW.getDocumento(cct, acuerdo, "Acuerdo", numexp);						
						documento.setIdTypeDoc("ACTA_ADJ");
						documento.setTypeDoc("Documento de Acta de Resolución");
					}					
				}
				if(documento!=null){
					documento.setExpedientNumber(numexp);
					documento.setOrganoContratacion(datosContrato.getOrganoContratacion());
					espacioVirtualLicitacion.setDocumentoPublicacion(documento);
				}
					
			}
							
			
			
			
		} catch (ISPACException e){
			LOGGER.error("Error al crear el objeto EspacioVirtualLicitacionBean para el expediente " + numexp + ". " + e.getMessage(), e);
			throw e;
		}
		return espacioVirtualLicitacion;
	}
	
	public static Documento getDocumentoPliegos(IClientContext cct, String numexp, String codTipoDoc, String organoContratacion) throws ISPACRuleException {
		Documento documentoPCAP = null;
		try{
			String nombreTipoDoc = DocumentosUtil.getNombreTipoDocByCod(cct, codTipoDoc);
			
			IItem itemTramite = TramitesUtil.getTramiteByCode(cct, numexp, "pliegos");
			
			String strQuery = TramitesUtil.NOMBRE+" = '" + itemTramite.getString("NOMBRE") + "' AND " + TramitesUtil.NUMEXP + " = '" + numexp + "'";
	        IItemCollection collection = TramitesUtil.getTramites(cct, numexp, strQuery, TramitesUtil.FECHA_CIERRE + DESC);
	        
	        @SuppressWarnings("unchecked")
			Iterator <IItem> it = collection.iterator();
	        if (it.hasNext())
	        {
	        	IItem doc = it.next();
				// obtengo los documentos
				int nidstage = doc.getInt("ID_FASE_PCD");
	        	int idTask = doc.getInt("ID_TRAM_PCD");
				
				String query = "ID_FASE_PCD = " + nidstage + " AND ID_TRAMITE_PCD = " + idTask + " AND FFIRMA IS NOT NULL AND NUMEXP = '" + numexp + "'";
				LOGGER.warn("query " + query);
				IItemCollection docsCollection = DocumentosUtil.getDocumentos(cct, numexp, query, DocumentosUtil.FAPROBACION + DESC);

				Iterator <?> docIterator = docsCollection.iterator();
				if(docIterator.hasNext()){
					boolean encontradoPCEA = false;
					while(docIterator.hasNext()){
						IItem docPres = (IItem) docIterator.next();
						String infoPag = "";
						String extension = "";
						if(docPres.getString(DocumentosUtil.INFOPAG_RDE)!=null){
							infoPag= docPres.getString(DocumentosUtil.INFOPAG_RDE);
							extension = docPres.getString(DocumentosUtil.EXTENSION_RDE);
						}
						else{
							if(docPres.getString(DocumentosUtil.INFOPAG)!= null) infoPag= docPres.getString(DocumentosUtil.INFOPAG);
							extension = docPres.getString(DocumentosUtil.EXTENSION);
						}
						LOGGER.warn("infoPag "+infoPag);
						File fichero = DocumentosUtil.getFile(cct, infoPag, null, null);
						LOGGER.warn("fichero "+fichero);
						
						if(docPres.getString(DocumentosUtil.NOMBRE).equals(nombreTipoDoc) && !encontradoPCEA){
							//"Pliego de Clausulas Económico - Administrativas"
							LOGGER.warn(nombreTipoDoc);
							documentoPCAP= new Documento();
							//documentoPCAP.setMimeCode("application/octet-stream");
							documentoPCAP.setMimeCode(MimetypeMapping.getMimeType(extension));
							if(docPres.getDate(DocumentosUtil.FFIRMA)!=null){
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(docPres.getDate("FFIRMA"));
								documentoPCAP.setFechaFirma(calendar);
							}
							
							FileInputStream fin = null;
							
							try {
								fin = new FileInputStream(fichero);
								long length = fichero.length();
								 
								if (length > Integer.MAX_VALUE) {
									throw new IOException("Tamaño del fichero excesivo: "  + length);
								}
		//						Create the byte array
								byte[] bytes = new byte[(int)length];
								 
		//						Reads the file content
								int offset = 0;
								int numRead = 0;
								while (offset < bytes.length
									   && (numRead=fin.read(bytes, offset, bytes.length-offset)) >= 0) {
									offset += numRead;
								}
								 
		//						Just to check if file was read completely
								if (offset < bytes.length) {
									throw new IOException("No se ha podido leer completamente el fichero " + fichero.getName());
								}
								 
		//						Close the input stream, all file contents are in the bytes variable
								fin.close();
								//documentoPCAP.setContenido(new DataHandler(bytes,"application/octet-stream"));
								documentoPCAP.setContenido(bytes);
								documentoPCAP.setMimeCode(MimetypeMapping.getMimeType(extension));
								
							} catch(IOException e){
								LOGGER.error("Error: " + numexp + ". " + e.getMessage(), e);
								throw new ISPACRuleException("Error: " + numexp + ". " + e.getMessage(), e);
								
							} finally {
								if(null != fin){
									try{
										fin.close();
									} catch(Exception e){
										LOGGER.error("ERROR al cerrar el FileInputStream. " + e.getMessage(), e);
									}
								}
							}
							
							String nombre = docPres.getString(DocumentosUtil.DESCRIPCION);
			 				nombre = DipucrFuncionesComunesSW.limpiarCaracteresEspeciales(nombre);
			 				LOGGER.warn("*****************************"+nombre);
			 				documentoPCAP.setNameDoc(nombre+"."+extension);
			 				
			 				if(codTipoDoc.equals("PCEA")){
			 					documentoPCAP.setIdTypeDoc("DOC_PCAP");
			 				}
			 				else{
			 					documentoPCAP.setIdTypeDoc("DOC_PPT");
			 				}
			 				
			 				documentoPCAP.setExpedientNumber(numexp);
			 				documentoPCAP.setOrganoContratacion(organoContratacion);
							encontradoPCEA = true;
						}
						fichero.delete();		
					}
				}
			}
		} catch (ISPACException e) {
			LOGGER.error("Error numexp " +numexp + " - " + e.getMessage(),e);
			throw new ISPACRuleException("Error numexp " + numexp + " - " + e.getMessage(),e);
		} 
		return documentoPCAP;
	}
}
