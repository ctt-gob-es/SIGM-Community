package es.dipucr.tablonEdictalUnico.rule;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CargarDatosAntiguaRecaudacionTablonEdictalBOERule implements IRule{
	
	public static final Logger logger = Logger.getLogger(CargarDatosAntiguaRecaudacionTablonEdictalBOERule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //------------------------------------------------------------------
	        
	        String refDoc = null;
			java.sql.Timestamp fDoc = null;
	        
		
			IItem tramExcel = TramitesUtil.getTramiteByCode(rulectx, "fichAntgRecauda");
			
			String consulta = "WHERE ID_TRAM_CTL = "+tramExcel.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'";
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iterator = tramspacDtTramite.iterator();
			IItem itTramitedt = iterator.next();
			
			IItemCollection taskDocumentosColeccion = DocumentosUtil.getDocumentosByTramites(rulectx, rulectx.getNumExp(), itTramitedt.getInt("ID_TRAM_EXP"));
			
			Iterator<IItem> itDocumentos = taskDocumentosColeccion.iterator();
			while(itDocumentos.hasNext()){
				//logger.warn("Documento");
				IItem documento = (IItem) itDocumentos.next();
				java.sql.Timestamp fItem = (java.sql.Timestamp) documento.get("FDOC");
				//logger.warn("FDoc: " + fItem);
				if(fDoc!=null) {
					if(fItem.after(fDoc)) {
						fDoc = fItem;
						refDoc = documento.getString("INFOPAG");
					}
				} else {
					fDoc = fItem;
					refDoc = (String) documento.getString("INFOPAG");
				}
				File fichero = DocumentosUtil.getFile(cct, refDoc, documento.getString("NOMBRE"), documento.getString("EXTENSION"));
				FileReader fileRea = new FileReader(fichero);
				BufferedReader readbuffer = new BufferedReader(fileRea);			
				String strRead = "";
				System.out.println ("Entrando") ;
				IItem datosParrafoCabecera = entitiesAPI.createEntity("TABLON_EDICTAL_BOE_PARRAFOS", rulectx.getNumExp());
				datosParrafoCabecera.set("PARRAFO", "----------CABECERA----------");
				datosParrafoCabecera.store(cct);
				StringBuffer parrafo = new StringBuffer("");
				while (!((strRead = readbuffer.readLine()) == null)) {
					if(strRead.equals(" ")){
						IItem datosParrafoPie = entitiesAPI.createEntity("TABLON_EDICTAL_BOE_PARRAFOS", rulectx.getNumExp());
						datosParrafoPie.set("PARRAFO", parrafo.toString()+"\n	");
						datosParrafoPie.store(cct);
						parrafo = new StringBuffer("");
					}
					else{
						parrafo.append(strRead+"\n");
					} 
				}
				IItem datosParrafoPie = entitiesAPI.createEntity("TABLON_EDICTAL_BOE_PARRAFOS", rulectx.getNumExp());
				datosParrafoPie.set("PARRAFO", "-----------PIE-----------");
				datosParrafoPie.store(cct);
				System.out.println ("Cerrando") ;
				readbuffer.close();
				fichero = null;
				fileRea.close();
			}
		}catch(ISPACException e) 
		{
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
        } catch (FileNotFoundException e) {
        	logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		} 

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
