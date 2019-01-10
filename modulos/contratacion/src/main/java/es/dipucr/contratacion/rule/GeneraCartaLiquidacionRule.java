package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.TemplateDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class GeneraCartaLiquidacionRule implements IRule{
	private static final Logger logger = Logger.getLogger(GeneraCartaLiquidacionRule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//----------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
	        //-----------------------------------------------------------------------------
	        
	        Object connectorSession = null;
	        
	        String consulta = "WHERE NUMEXP_PADRE = '"+rulectx.getNumExp()+"' AND RELACION='Petición Contrato'";
			IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consulta);
			Iterator<IItem> it = collection.iterator(); 
			
			String numexpContratacion = "";
			if(it.hasNext()){
				IItem expRelacionados = (IItem)it.next();
	        	numexpContratacion = expRelacionados.getString("NUMEXP_HIJO");
			}
			
			consulta = "WHERE NUMEXP_PADRE = '"+numexpContratacion+"' AND RELACION='Plica'";
			collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", consulta);
			it = collection.iterator(); 
			
			StringBuffer sexpRela = new StringBuffer("");
			
			if(it.hasNext()){
				IItem expRelacionados = (IItem)it.next();
	        	String numexpLicitador = expRelacionados.getString("NUMEXP_HIJO");
	        	sexpRela.append(" NUMEXP= '"+numexpLicitador+"' ");
			}
			while(it.hasNext()){
				IItem expRelacionados = (IItem)it.next();
	        	String numexpLicitador = expRelacionados.getString("NUMEXP_HIJO");
	        	sexpRela.append("OR NUMEXP = '"+numexpLicitador+"' ");
			}
	        
	        if (!sexpRela.equals("")){
	        	consulta= "WHERE ("+sexpRela.toString()+") AND APTO='SI'";
	        	IItemCollection collectionPlica = entitiesAPI.queryEntities("CONTRATACION_PLICA", consulta);
	        	Iterator<IItem> itPlica = collectionPlica.iterator();
	        	//Este es el adjudicado
	        	if(collectionPlica.toList().size()==1){
	        		if(itPlica.hasNext()){
	        			IItem plica = itPlica.next();
		        		String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+plica.getString("NUMEXP")+"' ORDER BY ID";	
		    			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
		    			if (participantes!=null && participantes.toList().size()>=1) {
		    				for (int i=0;i<participantes.toList().size();i++){
								connectorSession = gendocAPI.createConnectorSession();
								IItem participante = (IItem) participantes.toList().get(i);
								if (participante!=null){
						        	// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
									String nombre = "";
						        	if ((String)participante.get("NOMBRE")!=null){
						        		nombre = (String)participante.get("NOMBRE");
						        	}else{
						        		nombre = "";
						        	}
						        	String dirnot = "";
						        	if ((String)participante.get("DIRNOT")!=null){
						        		dirnot = (String)participante.get("DIRNOT");
						        	}else{
						        		dirnot = "";
						        	}
						        	String c_postal = "";
						        	if ((String)participante.get("C_POSTAL")!=null){
						        		c_postal = (String)participante.get("C_POSTAL");
						        	}else{
						        		c_postal = "";
						        	}
						        	String localidad = "";
						        	if ((String)participante.get("LOCALIDAD")!=null){
						        		localidad = (String)participante.get("LOCALIDAD");
						        	}else{
						        		localidad = "";
						        	}
						        	String caut = "";
						        	if ((String)participante.get("CAUT")!=null){
						        		caut = (String)participante.get("CAUT");
						        	}else{
						        		caut = "";
						        	}
						        	String observaciones = "";
						        	if ((String)participante.get("OBSERVACIONES")!=null){
						        		observaciones = (String)participante.get("OBSERVACIONES");
						        	}else{
						        		observaciones = "";
						        	}
						        	String ndoc = "";
						        	if ((String)participante.get("NDOC")!=null){
						        		ndoc = (String)participante.get("NDOC");
						        	}else{
						        		ndoc = "";
						        	}
						        	String recurso = "";
						        	if ((String)participante.get("RECURSO")!=null){
						        		recurso = (String)participante.get("RECURSO");
						        	}else{
						        		recurso = "";
						        	}
						        	
						        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
						        	sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
						        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
						        	if (colRecurso.iterator().hasNext()){
						        		IItem iRecurso = (IItem)colRecurso.iterator().next();
						        		recurso = iRecurso.getString("SUSTITUTO");
						        	}
						        	
						        	cct.setSsVariable("NOMBRE", nombre);
						        	cct.setSsVariable("DIRNOT", dirnot);
						        	cct.setSsVariable("C_POSTAL", c_postal);
						        	cct.setSsVariable("LOCALIDAD", localidad);
						        	cct.setSsVariable("CAUT", caut);
						        	cct.setSsVariable("OBSERVACIONES", observaciones);
						        	cct.setSsVariable("NDOC", ndoc);
						        	cct.setSsVariable("RECURSO", recurso);
						        	
						        	String plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
									String tipoDocumento = "";
									if(StringUtils.isNotEmpty(plantilla)){
										tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
									}
						        	
						        	int documentTypeId = DocumentosUtil.getTipoDoc(cct, tipoDocumento, DocumentosUtil.BUSQUEDA_EXACTA, false);
						        	IItem template = TemplateDAO.getTemplate(cct, plantilla, documentTypeId);
						        	
						        	if(template != null){
										int templateId = template.getInt("ID");
							        	IItem entityTemplate = DocumentosUtil.generarDocumentoDesdePlantilla(rulectx, rulectx.getTaskId(), documentTypeId, templateId, "", "");
							        	String docRef = DocumentosUtil.getInfoPag(rulectx, entityTemplate.getInt("ID"));
							        	String sMimetype = gendocAPI.getMimeType(connectorSession, docRef);
										entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
										String templateDescripcion = plantilla + " - " + cct.getSsVariable("NOMBRE");
										entityTemplate.set("DESCRIPCION", templateDescripcion);
										entityTemplate.set("DESTINO", nombre);
										entityTemplate.store(cct);
						        	}
									
						        	
									// Si todo ha sido correcto borrar las variables de la session
									cct.deleteSsVariable("NOMBRE");
									cct.deleteSsVariable("DIRNOT");
									cct.deleteSsVariable("C_POSTAL");
									cct.deleteSsVariable("LOCALIDAD");
									cct.deleteSsVariable("CAUT");
									cct.deleteSsVariable("OBSERVACIONES");
									cct.deleteSsVariable("NDOC");
									cct.deleteSsVariable("RECURSO");
						        }
		    				}
		    			}
		        	}
	        	}
	        	else{
	        		logger.warn("Exite mas de un Adjudicado");
	        	}
	        }
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la fecha actual",e);
        }

		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
