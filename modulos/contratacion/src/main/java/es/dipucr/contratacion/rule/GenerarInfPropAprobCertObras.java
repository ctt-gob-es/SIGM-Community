package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class GenerarInfPropAprobCertObras extends DipucrAutoGeneraDocIniTramiteRule {
	private static final Logger logger = Logger.getLogger(GenerarInfPropAprobCertObras.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - " + this.getClass().getName());

		try{
			IClientContext cct = rulectx.getClientContext();
			
			plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
			
			if(StringUtils.isNotEmpty(plantilla)){
				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
			}

		}
		catch(ISPACException e){
			logger.error("Error al generar el documento. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el documento. " + e.getMessage(), e);
		}
		logger.info("FIN - " + this.getClass().getName());
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void setSsVariables(IClientContext cct, IRuleContext rulectx) {
		try {
			IItemCollection exp_relacionadosCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION like 'Certificación Obra%' ORDER BY ID DESC");
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        
	        String numexpPeticionContratacion = "";
	        if (exp_relacionadosIterator.hasNext()){
	        	numexpPeticionContratacion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_PADRE");       	
	        }
	        
			
	
			
			 //Obtenemos los expedientes relacionados y aprobados, ordenados por ayuntamiento
	         exp_relacionadosCollection = cct.getAPI().getEntitiesAPI().queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='"+numexpPeticionContratacion+"' AND RELACION='Petición Contrato' ORDER BY ID DESC");
	         exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        
	        String numexpContratacion = "";
	        if (exp_relacionadosIterator.hasNext()){
	        	numexpContratacion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_HIJO");       	
	        }
	        
	        //datos del adjudicatario
			IItemCollection expedientesCollection = cct.getAPI().getEntitiesAPI().queryEntities("CONTRATACION_DATOS_TRAMIT", "WHERE NUMEXP='"+numexpContratacion+"'");
		   	Iterator<IItem> expedientesIterator = expedientesCollection.iterator();
		   	
		   	if(expedientesIterator.hasNext()){
		   		IItem datosTram = expedientesIterator.next();
		   		String empresa = datosTram.getString("EMP_ADJ_CONT");
		   		cct.setSsVariable("EMPRESA_ADJUDICATARIA", empresa);
		   		
		   		String nif = datosTram.getString("NIF_ADJUDICATARIA");
		   		cct.setSsVariable("EMPRESA_ADJUDICATARIA_CIF",nif);
		   		
		   		String importeIVA = datosTram.getString("IMP_ADJ_CONIVA");
		   		cct.setSsVariable("EMPRESA_ADJUDICATARIA_IMPORTE_CONIVA", importeIVA);
		   		
		   		String importeSinIVA = datosTram.getString("IMP_ADJ_SINIVA");
		   		cct.setSsVariable("EMPRESA_ADJUDICATARIA_IMPORTE_SINIVA", importeSinIVA);
		   	}
		   	
		  //datos de la aplicación presupuestaría
		   	IItemCollection expedientesCollectionDatTramit = cct.getAPI().getEntitiesAPI().queryEntities("CONTRATACION_DATOS_LIC", "WHERE NUMEXP='"+numexpContratacion+"'");
		   	Iterator<IItem> expedientesIteratorDatTramit = expedientesCollectionDatTramit.iterator();
		   	
		   	if(expedientesIteratorDatTramit.hasNext()){
		   		IItem datosTramit = expedientesIteratorDatTramit.next();
		   		int id = datosTramit.getInt("ID");
		   		
		   		String consulta="SELECT VALUE FROM CONTRATACION_DATOS_LIC_S WHERE REG_ID = "+id+" AND FIELD = 'APLICAPRESUP'";
		        ResultSet datosApli = cct.getConnection().executeQuery(consulta).getResultSet();
		        String value = "";
		        StringBuffer stAplic = new StringBuffer("");
	        	while(datosApli.next()){
	        		
	          		if (datosApli.getString("VALUE")!=null) value = datosApli.getString("VALUE"); else value="";
	          		String [] sAplicacion = value.split("-");
		        	if(sAplicacion.length > 0 && sAplicacion.length==3){
		        		stAplic.append("- Aplicación Presupuestaria: "+sAplicacion[0]+", ");
		        		stAplic.append("Anualidad: "+sAplicacion[1]+", ");
		        		stAplic.append("Importe: "+sAplicacion[2]+"\n");
		        	}
	          	}
	        	if(!stAplic.equals("")){
	        		cct.setSsVariable("PARTIDA_PRESUPUESTARIA_IMPORTE", stAplic.toString());
	        	}
		   	}
		   	
		   	//Datos del contrato
			IItemCollection expedientesCollectionDatContrato = cct.getAPI().getEntitiesAPI().queryEntities("CONTRATACION_DATOS_CONTRATO", "WHERE NUMEXP='"+numexpContratacion+"'");
		   	Iterator<IItem> expedientesIteratorDatContrat = expedientesCollectionDatContrato.iterator();
		   	if(expedientesIteratorDatContrat.hasNext()){
		   		IItem datosContrato = expedientesIteratorDatContrat.next();
		   		String tipoContrato = datosContrato.getString("TIPO_CONTRATO");
				if (tipoContrato != "") {
					String[] v_cod_tipo_contrato = tipoContrato.split(" - ");
					if (v_cod_tipo_contrato.length > 0) {
						tipoContrato = v_cod_tipo_contrato[1];
					}

				}
				cct.setSsVariable("TIPO_CONTRATO", tipoContrato);
				
		   		String objetoContrato = datosContrato.getString("OBJETO_CONTRATO");
		   		cct.setSsVariable("OBJETO_CONTRATO", objetoContrato);
		   		
		   		String ncontrato = datosContrato.getString("NCONTRATO");
		   		cct.setSsVariable("NCONTRATO", ncontrato);
		   		
		   	}
		   	
		   	//CONTRATACION PETICION
		   	IItemCollection expedientesCollectionContratoPeticion = cct.getAPI().getEntitiesAPI().queryEntities("CONTRATACION_PETICION", "WHERE NUMEXP='"+numexpContratacion+"'");
		   	Iterator<IItem> expedientesIteratorContratoPet = expedientesCollectionContratoPeticion.iterator();
		   	if(expedientesIteratorContratoPet.hasNext()){
		   		IItem contratoPet = expedientesIteratorContratoPet.next();
		   		String servResp = contratoPet.getString("SERVICIO_RESPONSABLE");
		   		cct.setSsVariable("SERVRESP", servResp);
		   		String iva = contratoPet.getString("IVA");
		   		cct.setSsVariable("IVA", iva);
		   	}
		   	
		        	 
			

		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void deleteSsVariables(IClientContext cct) {
		try {
			cct.deleteSsVariable("EMPRESA_ADJUDICATARIA");
			cct.deleteSsVariable("EMPRESA_ADJUDICATARIA_CIF");
			cct.deleteSsVariable("EMPRESA_ADJUDICATARIA_IMPORTE");
			cct.deleteSsVariable("PARTIDA_PRESUPUESTARIA_IMPORTE");
			cct.deleteSsVariable("TIPO_CONTRATO");
			cct.deleteSsVariable("OBJETO_CONTRATO");
			cct.deleteSsVariable("NCONTRATO");
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}