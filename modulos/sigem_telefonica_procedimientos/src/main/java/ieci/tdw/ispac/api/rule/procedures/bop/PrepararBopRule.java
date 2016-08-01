package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class PrepararBopRule implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(PrepararBopRule.class);
	
	private final String strDocumento = "BOP - Anuncio";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        IItem doc = null;
        IItem anuncio = null;
        String strId = null;
        String strClasificacion = null;
        String strSumario = null;
        String strTexto = null;
        String strUrgencia = null;
        String strOrden = null;
        String strEntidad = null;
        String strNumexpAnuncio = null; //[eCenpri-Felipe #593]
        int orden = 10;
        IItemCollection docs = null;
        Iterator it = null;
        IItemCollection collection = null;
        IItem item = null;
        int numBop = 0;
        int numPag = 0;
        int numAnuncio = 0;
        String strQuery2 = null;        
        IItemCollection collAllProps2 = null;
        Iterator itAllProps2 = null;
        IItem iProp2 = null;
        String strQuery = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
        Date fechaPub = null;
        Date fechaBop = null;
        IItem clasif = null;
        String clasificacion = null;
        Iterator itClasificacion = null;
        ClientContext cct = (ClientContext) rulectx.getClientContext();
        
        //INICIO [eCenpri-Felipe #302#40]
        //Para que cuando actualicemos la entidad por código (sus contadores)
        //no se vuelva a ejecutar la regla
    	try{
	    	String sPrepararBop = cct.getSsVariable("PREPARAR_BOP");
	    	if (!StringUtils.isEmpty(sPrepararBop) && sPrepararBop.equals("NO")){
	    		return null;
	    	}
    	}
	    catch (Exception e) {
	    	throw new ISPACRuleException("Error al comprobar si se debe ejecutar " +
	    			"la regla PrepararBopRule asociada a la entidad BOP_PUBLICACION", e);
		}
    	//FIN [eCenpri-Felipe #302#40]
        
    	try{
			//----------------------------------------------------------------------------------------------
//	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        //Borramos la lista de anuncios
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllProps = entitiesAPI.queryEntities("BOP_ANUNCIOS", strQuery);
	        Iterator itAllProps = collAllProps.iterator();
        	IItem iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	iProp.delete(cct);
	        }
	        
	        // Obtenemos los documentos de solicitud de anuncio con la fecha de publicación
	        // en cuestión
	        IItem datosBOP = rulectx.getItem();
	        
	        // Fecha del último BOP
	        fechaPub = datosBOP.getDate("FECHA");
	        logger.warn("fecha: " + df.format(fechaPub));
	        strQuery = "WHERE VALOR = 'fecha_ultimo_bop'";
	        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
	        it = collection.iterator();
	        if (it.hasNext()){
	        	item = (IItem)it.next();
	        	fechaBop = df2.parse(item.getString("SUSTITUTO"));
	        }
	        logger.warn("fecha del último BOP: " + df.format(fechaBop));
	        
	        if (fechaPub.after(fechaBop))
	        {
		        // Determinar el número de BOP, el número de la primera página y el número del
		        // primer anuncio
		        
		        // Número de BOP
		        strQuery = "WHERE VALOR = 'num_bop'";
		        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	item = (IItem)it.next();
		        	numBop = Integer.parseInt(item.getString("SUSTITUTO")) + 1;
		        	datosBOP.set("NUM_BOP", numBop);
		        }
	//        	item.set("SUSTITUTO", String.valueOf(numBop));
	//        	item.store(cct);
		        
		        // Número de página
		        strQuery = "WHERE VALOR = 'num_pagina'";
		        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	item = (IItem)it.next();
		        	numPag = Integer.parseInt(item.getString("SUSTITUTO")) + 1;
		        	datosBOP.set("NUM_PAGINA", numPag);
		        }
	//        	item.set("SUSTITUTO", String.valueOf(numPag));
	//        	item.store(cct);
		        
		        // Número de anuncio
		        strQuery = "WHERE VALOR = 'num_anuncio'";
		        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CONTADORES", strQuery);
		        it = collection.iterator();
		        if (it.hasNext()){
		        	item = (IItem)it.next();
		        	numAnuncio = Integer.parseInt(item.getString("SUSTITUTO")) + 1;
		        	datosBOP.set("NUM_ANUNCIO", numAnuncio);
		        }
	//        	item.set("SUSTITUTO", String.valueOf(numAnuncio));
	//        	item.store(cct);
		        
		        // Clasificaciones de las entidades
		        strQuery = "ORDER BY ORDEN";
		        collection = entitiesAPI.queryEntities("BOP_VLDTBL_CLASIFICACION", strQuery);
		        itClasificacion = collection.iterator();
		        while (itClasificacion.hasNext()){
		        	clasif = (IItem)itClasificacion.next();
		        	clasificacion = clasif.getString("VALOR");
		        	logger.warn("Clasificación: " + clasificacion);
		        	
			        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='" + clasificacion + "' ORDER BY ENTIDAD";
			        logger.warn("Query: " + strQuery);
			        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
			        it = docs.iterator();
			        while (it.hasNext())
			        {
			        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
			        	doc = (IItem)it.next();
		
			        	// Identificador del documento del anuncio	        	
				        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
				        logger.warn("Query: " + strQuery2);
				        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
				        itAllProps2 = collAllProps2.iterator();
			        	iProp2 = null;
			        	strId = "";
				        while(itAllProps2.hasNext()) {
				        	iProp2 = ((IItem)itAllProps2.next());
				        	strId = iProp2.getString("ID");
				        }
				        
		//	        	strId = doc.getString("ID");
			        	logger.warn("strId: " + strId);
			        	strClasificacion = doc.getString("CLASIFICACION");
			        	logger.warn("strClasificacion: " + strClasificacion);
			        	strEntidad = doc.getString("ENTIDAD");
			        	logger.warn("strEntidad: " + strEntidad);
			        	strSumario = doc.getString("SUMARIO");
			        	logger.warn("strSumario: " + strSumario);
			        	strTexto = doc.getString("TEXTO");
			        	logger.warn("strTexto: " + strTexto);
			        	strUrgencia = doc.getString("URGENCIA");
			        	logger.warn("strUrgencia: " + strUrgencia);
			        	strOrden = Integer.toString(orden);
			        	while (strOrden.length() < 4)
			        	{
			        		strOrden = "0" + strOrden;
			        	}
			        	logger.warn("strOrden: " + strOrden);
			        	//INICIO [eCenpri-Felipe #593]
			        	strNumexpAnuncio = doc.getString("NUMEXP");
			        	logger.warn("strNumexpAnuncio: " + strNumexpAnuncio);
			        	//FIN [eCenpri-Felipe #593]
			        	
			        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
			        	anuncio.set("IDDOC", strId);
			        	anuncio.set("CLASIFICACION", strClasificacion);
			        	anuncio.set("ENTIDAD", strEntidad);
			        	anuncio.set("SUMARIO", strSumario);
			        	anuncio.set("TEXTO", strTexto);
			        	anuncio.set("URGENCIA", strUrgencia);
			        	anuncio.set("ORDEN", strOrden);
			        	anuncio.set("NUMEXP_ANUNCIO", strNumexpAnuncio); //[eCenpri-Felipe #593]
			        	anuncio.store(cct);
			        	orden = orden + 10;
			        }
		        }

		        /*// Anuncios de la Diputación
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Diputación de Ciudad Real' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
	
		        	// Identificador del documento del anuncio	        	
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	logger.warn("strId: " + strId);
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }
		        
		        // Anuncios de los Ayuntamientos
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Ayuntamiento de la provincia de Ciudad Real' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
	
		        	// Identificador del documento del anuncio
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	logger.warn("strId: " + strId);
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }
		        
		        // Anuncios de la Comunidad
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Consejería de la Junta de Comunidades de Castilla la Mancha' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
	
		        	// Identificador del documento del anuncio
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	logger.warn("strId: " + strId);
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }
		        
		        // Anuncios de la Ministerios
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Ministerio' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
	
		        	// Identificador del documento del anuncio
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	logger.warn("strId: " + strId);
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }
		        
		        // Anuncios de la Otras entidades
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Otra entidad' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
	
		        	// Identificador del documento del anuncio
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	logger.warn("strId: " + strId);
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }
		        
		        // Anuncios de la Particulares
		        strQuery = "WHERE fecha_publicacion='" + df.format(fechaPub) + "' AND CLASIFICACION='Particular' ORDER BY ENTIDAD";
		        logger.warn("Query: " + strQuery);
		        docs = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);
		        it = docs.iterator();
		        while (it.hasNext())
		        {
		        	//Generamos una entrada en la lista de anuncios por cada documento encontrado
		        	doc = (IItem)it.next();
		        	strId = doc.getString("ID");
	
		        	// Identificador del documento del anuncio
			        strQuery2 = "WHERE NUMEXP='" + doc.getString("NUMEXP") + "' AND NOMBRE='" + strDocumento + "'";
			        logger.warn("Query: " + strQuery2);
			        collAllProps2 = entitiesAPI.queryEntities("SPAC_DT_DOCUMENTOS", strQuery2);
			        itAllProps2 = collAllProps2.iterator();
		        	iProp2 = null;
		        	strId = "";
			        while(itAllProps2.hasNext()) {
			        	iProp2 = ((IItem)itAllProps2.next());
			        	strId = iProp2.getString("ID");
			        }
			        
	//	        	strId = doc.getString("ID");
		        	strClasificacion = doc.getString("CLASIFICACION");
		        	logger.warn("strClasificacion: " + strClasificacion);
		        	strEntidad = doc.getString("ENTIDAD");
		        	logger.warn("strEntidad: " + strEntidad);
		        	strSumario = doc.getString("SUMARIO");
		        	logger.warn("strSumario: " + strSumario);
		        	strTexto = doc.getString("TEXTO");
		        	logger.warn("strTexto: " + strTexto);
		        	strUrgencia = doc.getString("URGENCIA");
		        	logger.warn("strUrgencia: " + strUrgencia);
		        	strOrden = Integer.toString(orden);
		        	while (strOrden.length() < 4)
		        	{
		        		strOrden = "0" + strOrden;
		        	}
		        	logger.warn("strOrden: " + strOrden);
		        	anuncio = entitiesAPI.createEntity("BOP_ANUNCIOS", rulectx.getNumExp());
		        	anuncio.set("IDDOC", strId);
		        	anuncio.set("CLASIFICACION", strClasificacion);
		        	anuncio.set("ENTIDAD", strEntidad);
		        	anuncio.set("SUMARIO", strSumario);
		        	anuncio.set("TEXTO", strTexto);
		        	anuncio.set("URGENCIA", strUrgencia);
		        	anuncio.set("ORDEN", strOrden);
		        	anuncio.store(cct);
		        	orden = orden + 10;
		        }*/
	        }
	        else
	        {
	        	datosBOP.set("FECHA", "");
	        	logger.warn("La fecha del BOP debe ser superior a la del último BOP publicado.");
	        	throw new ISPACInfo("La fecha del BOP debe ser superior a la del último BOP publicado.");
	        }
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido crear la lista de anuncios del BOP",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
