package es.dipucr.contratacion.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import es.dipucr.contratacion.objeto.sw.BOP;
import es.dipucr.contratacion.objeto.sw.Campo;
import es.dipucr.contratacion.objeto.sw.Clasificacion;
import es.dipucr.contratacion.objeto.sw.DatosContrato;
import es.dipucr.contratacion.objeto.sw.DatosEmpresa;
import es.dipucr.contratacion.objeto.sw.DatosLicitacion;
import es.dipucr.contratacion.objeto.sw.DatosTramitacion;
import es.dipucr.contratacion.objeto.sw.DiariosFechaOficiales;
import es.dipucr.contratacion.objeto.sw.DiariosOficiales;
import es.dipucr.contratacion.objeto.sw.Documento;
import es.dipucr.contratacion.objeto.sw.EspacioVirtualLicitacionBean;
import es.dipucr.contratacion.objeto.sw.Garantia;
import es.dipucr.contratacion.objeto.sw.LicitadorBean;
import es.dipucr.contratacion.objeto.sw.OrganoAsistencia;
import es.dipucr.contratacion.objeto.sw.PersonalContacto;
import es.dipucr.contratacion.objeto.sw.Peticion;
import es.dipucr.contratacion.objeto.sw.PublicacionesOficialesBean;
import es.dipucr.contratacion.objeto.sw.SobreElectronico;
import es.dipucr.contratacion.objeto.sw.Solvencia;
import es.dipucr.contratacion.objeto.sw.common.DipucrFuncionesComunesSW;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class EspacioVirtualLicitacionUtilsSW {

	public static final Logger LOGGER = Logger.getLogger(EspacioVirtualLicitacionUtilsSW.class);
	
	public static final String DESC = " DESC";
	
	public static EspacioVirtualLicitacionBean getEspacioVirtualLicitacionBeanSW(IClientContext cct, String numexp, IItem datosTram) throws ISPACRuleException {
		EspacioVirtualLicitacionBean anuncioLicitacion = new EspacioVirtualLicitacionBean();
		
		try{
			//Num Expediente
			anuncioLicitacion.setNumexp(numexp);
			
			DatosContrato datContrato = DipucrFuncionesComunesSW.getDatosContrato(cct, numexp);
			if(datContrato!=null){
				anuncioLicitacion.setObjetoContrato(datContrato.getObjetoContrato());
				anuncioLicitacion.setProcContratacion(datContrato.getProcedimientoContratacion());
				anuncioLicitacion.setTipoContrato(datContrato.getTipoContrato());
				anuncioLicitacion.setSubTipoContrato(datContrato.getSubTipoContrato());
				anuncioLicitacion.setOrganoContratacion(datContrato.getOrganoContratacion());
				anuncioLicitacion.setTipoTramitacion(datContrato.getTipoTramitacion());
				anuncioLicitacion.setTramitacionGasto(datContrato.getTramitacionGasto());
				anuncioLicitacion.setCpv(datContrato.getCpv());
				anuncioLicitacion.setLugarEjecucionContrato(datContrato.getProvinciaContrato());
				anuncioLicitacion.setValorEstimadoContrato(datContrato.getValorEstimadoContrato());
			}
			
			DiariosFechaOficiales diariosOficiales = DipucrFuncionesComunesSW.getFechaDiariosOficiales(cct, numexp);
			if(diariosOficiales!=null){
				if(diariosOficiales.getContratoSujetoRegArmon()!=null){
					anuncioLicitacion.setContratoSujetoRegArmon(diariosOficiales.getContratoSujetoRegArmon());
				}
			}
			
			DatosTramitacion datosTramitacion = DipucrFuncionesComunesSW.getDatosTramitacion(cct, numexp, datosTram);
			if(datosTramitacion!=null){
				if(datosTramitacion.getPresentacionOfertas()!=null){
					anuncioLicitacion.setPresentacionOfertas(datosTramitacion.getPresentacionOfertas());
					anuncioLicitacion.setFechaPresentacionSolcitudesParticipacion(datosTramitacion.getPresentacionOfertas().getEndCalendar());
					anuncioLicitacion.setDuracionContrato(datosTramitacion.getDuracionContrato());
				}
			}
			
			//Compruebo que el tipo de contrato no sea Patrimonial
			//Porque si no coge los datos del presupuesto de la entidad datos del contrato
			if(!datContrato.getTipoContrato().getId().equals("50")){
				Peticion peticion = DipucrFuncionesComunesSW.getPeticion(cct, numexp);
				anuncioLicitacion.setPresupuestoConIva(peticion.getPresupuestoConIva());
				anuncioLicitacion.setPresupuestoSinIva(peticion.getPresupuestoSinIva());
			}
			else{
				String [] presupuesto =  DipucrFuncionesComunes.getDatosContratoPresupuesto(cct, numexp);
				if(presupuesto!=null && presupuesto.length==2){
					anuncioLicitacion.setPresupuestoConIva(presupuesto[0]);
					anuncioLicitacion.setPresupuestoSinIva(presupuesto[1]);
				}
			}
			
			DatosEmpresa datEmpresa = DipucrFuncionesComunesSW.getDatosEmpresa(cct, numexp);
			if(datEmpresa.getClasificacion()!=null && datEmpresa.getClasificacionEvidence()!=null){
				Clasificacion clasificacion = new Clasificacion();
				clasificacion.setClasificacion(datEmpresa.getClasificacion());
				clasificacion.setClasificacionAcreditarRequisito(datEmpresa.getClasificacionEvidence());
				anuncioLicitacion.setClasificacion(clasificacion);
			}
			
			
			
			anuncioLicitacion.setCondLicit(datEmpresa.getCondLicit());
			anuncioLicitacion.setReqDecl(datEmpresa.getTipoDeclaracion());
			
			DatosLicitacion datosLicitacion= DipucrFuncionesComunesSW.getDatosLicitacion(cct, numexp);
			anuncioLicitacion.setTipoPresentacionOferta(datosLicitacion.getTipoPresentacionOferta());
			OrganoAsistencia organoAsistencia = datosLicitacion.getOrganoAsistencia();
			anuncioLicitacion.setApliPesu(datosLicitacion.getAplicacionPres());
			//Falta por introducir la entidad 'Criterios de adjudicación'
			anuncioLicitacion.setCriterios(datosLicitacion.getCritAdj());
			anuncioLicitacion.setVarOfert(datosLicitacion.getVariantes());
			anuncioLicitacion.setFundacionPrograma(datosLicitacion.getFundacionPrograma());
			anuncioLicitacion.setFormulaRevisionPrecios(datosLicitacion.getRevisionPrecios());
		
			PublicacionesOficialesBean publicacionesOficiales = null;			
			DiariosOficiales diariosOficialesAnuncio =  DipucrFuncionesComunesSW.getDiariosOficiales(cct, numexp, "AnuncioLicitacionRule");
			BOP bop = DipucrFuncionesComunesSW.getBOP(cct, numexp);			
			if(diariosOficialesAnuncio !=null || bop!=null){
				publicacionesOficiales = new PublicacionesOficialesBean();
				if(diariosOficialesAnuncio.getDoue() !=null){
					publicacionesOficiales.setEnviarDOUE(diariosOficialesAnuncio.getDoue().isPublicarDOUE());
				}
				if(diariosOficialesAnuncio.getBoe() !=null){
					publicacionesOficiales.setEnviarBOE(diariosOficialesAnuncio.getBoe().isPublicarBOE());
				}
				if(bop!=null){
					publicacionesOficiales.setNombreOtrosDiarios(bop.getNombreBOP());
					publicacionesOficiales.setFechaPubOtrosDiarios(bop.getFechaPublicacion());
					publicacionesOficiales.setPublishURLOtrosDiarios(bop.getUrlPublicacion());
				}
				
				anuncioLicitacion.setDiarios(publicacionesOficiales);
			}
			
			SobreElectronico [] sobreElect = DipucrFuncionesComunesSW.getSobreElec(cct, numexp);
			anuncioLicitacion.setSobreElect(sobreElect);
			
			Solvencia solvencia = DipucrFuncionesComunesSW.getSolvencia(cct, numexp);
			anuncioLicitacion.setSolvenciaEconomica(solvencia.getSolvenciaEconomica());
			anuncioLicitacion.setSolvenciaTecn(solvencia.getSolvenciaTecn());	
			
			//garantias
			Garantia[] garantia = DipucrFuncionesComunesSW.getGarantias(cct, numexp);
			anuncioLicitacion.setGarantia(garantia);
			
			//Personal contacto
			PersonalContacto[] persCon = DipucrFuncionesComunesSW.getPersonalContacto(cct, numexp);
			anuncioLicitacion.setPersonalContactoContratacion(persCon[0]);
			anuncioLicitacion.setPersonalContactoSecretaria(persCon[1]);
			if(organoAsistencia!=null){
				if(persCon[2]!=null){
					organoAsistencia.setInformacionOC(persCon[2]);
				}				
				anuncioLicitacion.setOrganoAsistencia(organoAsistencia);
			}
			
			//Licitadores
			LicitadorBean[] licitadores = DipucrFuncionesComunesSW.getLicitadores(cct, numexp);
			anuncioLicitacion.setLicitadores(licitadores);
			Documento docPCEA = getDocumentoPliegos(cct, numexp, "PCEA", datContrato.getOrganoContratacion());
			if(docPCEA!=null){
				anuncioLicitacion.setDocumentoPCAP(docPCEA);
			}
			Documento docPPT = getDocumentoPliegos(cct, numexp, "PPT", datContrato.getOrganoContratacion());
			if(docPPT!=null){
				anuncioLicitacion.setDocumentoPPT(docPPT);
			}
			anuncioLicitacion.setDocAdicionales(DipucrFuncionesComunesSW.docInformacionAdicionalPliego(cct,numexp, datContrato.getOrganoContratacion()));
			
			
		} catch (ISPACException e){
			LOGGER.error("Error al crear el objeto EspacioVirtualLicitacionBean para el expediente " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error numexp " + numexp + " - " + e.getMessage(),e);
		}
		return anuncioLicitacion;
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
			 				nombre = DipucrFuncionesComunes.limpiarCaracteresEspeciales(nombre);
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
