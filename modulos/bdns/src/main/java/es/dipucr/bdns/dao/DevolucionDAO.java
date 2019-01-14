package es.dipucr.bdns.dao;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.dipucr.bdns.objetos.Devolucion;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

public class DevolucionDAO {

	public static String TABLENAME = "BDNS_IGAE_DEVOLUCION";
	
	/**
	 * Crea un objeto de tipo Devolucion
	 * @param numexpConvocatoria
	 * @param cifBeneficiario
	 * @param idConvocatoria
	 * @param refConcesion
	 * @param datosOperacionSical
	 * @return
	 * @throws Exception
	 */
	public Devolucion newObjectNotPersist(String numexpConvocatoria, String cifBeneficiario,
			String idConvocatoria, String refConcesion, String numOperacion, String fechaOperacion, String importe) throws Exception {
		
		Devolucion devolucion = new Devolucion();
		devolucion.setIdConvocatoria(idConvocatoria);
		devolucion.setNumexpConvocatoria(numexpConvocatoria);
		devolucion.setReferenciaConcesion(refConcesion);
		devolucion.setCifBeneficiario(cifBeneficiario);
		devolucion.setReferenciaDevolucion(numOperacion);
		devolucion.setFechaDevolucion(FechasUtil.convertToDate(fechaOperacion));
		devolucion.setImporteDevolucion((Double.valueOf(importe.replace(",", "."))));
		return devolucion;
	}
	
	/**
	 * Comprueba si una devolucion existe ya en la BBDD
	 * @param entitiesAPI
	 * @param devolucion
	 * @return
	 * @throws ISPACException
	 */
	public boolean existsBBDD(IEntitiesAPI entitiesAPI, Devolucion devolucion) throws ISPACException {
		
		StringBuffer sbQueryDevolucion = new StringBuffer();
		sbQueryDevolucion.append("WHERE NUMEXP='" + devolucion.getNumexpConvocatoria() + "'");
		sbQueryDevolucion.append(" AND ID_CONVOCATORIA='" + devolucion.getIdConvocatoria() + "'");
		sbQueryDevolucion.append(" AND CIF_BENEFICIARIO='" + devolucion.getCifBeneficiario() + "'");
		sbQueryDevolucion.append(" AND REFERENCIA_DEVOLUCION='" + devolucion.getReferenciaDevolucion() + "'");
		
		IItemCollection colDevoluciones = entitiesAPI.queryEntities(TABLENAME, sbQueryDevolucion.toString());
		boolean bExisteDevolucion = (colDevoluciones.iterator().hasNext());
		return bExisteDevolucion;
	}
	
	/**
	 * Almacena la devolucion en la BBDD
	 * @param cct
	 * @param devolucion
	 * @throws ISPACException
	 */
	public void store(ClientContext cct, Devolucion devolucion) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem itemDevolucion = entitiesAPI.createEntity(TABLENAME, devolucion.getNumexpConvocatoria());
		itemDevolucion.set("ID_CONVOCATORIA", devolucion.getIdConvocatoria());
		itemDevolucion.set("CIF_BENEFICIARIO", devolucion.getCifBeneficiario());
		itemDevolucion.set("REFERENCIA_DEVOLUCION", devolucion.getReferenciaDevolucion());
		itemDevolucion.set("IMPORTE", devolucion.getImporteDevolucion());
		itemDevolucion.set("IMPORTE_INTERESES", devolucion.getImporteIntereses());
		itemDevolucion.set("FECHA_DEVOLUCION", devolucion.getFechaDevolucion());
		itemDevolucion.set("CODIGO_CAUSA", devolucion.getCodigoCausa());
		itemDevolucion.set("CAUSA", devolucion.getCausa());
		
		itemDevolucion.store(cct);
	}
	
	/**
	 * Devuelve las devoluciones de una concesiom
	 * @param cct
	 * @param idConvocatoria
	 * @param cifBeneficiario
	 * @return
	 * @throws ISPACException 
	 */
	public List<Devolucion> getDevolucionesByConcesion(IClientContext cct, String idConvocatoria, String cifBeneficiario) throws ISPACException{
		
		ArrayList<Devolucion> listDevoluciones = new ArrayList<Devolucion>();
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		StringBuffer sbQueryDevoluciones = new StringBuffer();
		sbQueryDevoluciones.append(" WHERE ID_CONVOCATORIA='" + idConvocatoria + "'");
		sbQueryDevoluciones.append(" AND CIF_BENEFICIARIO='" + cifBeneficiario + "'");
		IItemCollection colDevolucions = entitiesAPI.queryEntities(TABLENAME, sbQueryDevoluciones.toString());
		@SuppressWarnings("rawtypes")
		Iterator it = colDevolucions.iterator();
		
		while(it.hasNext()){
			IItem itemDevolucion = (IItem) it.next();
			
			Devolucion devolucion = new Devolucion
			(
					idConvocatoria,
					itemDevolucion.getString("NUMEXP"),
					cifBeneficiario,
					null,
					itemDevolucion.getString("REFERENCIA_DEVOLUCION"),
					itemDevolucion.getDouble("IMPORTE"),
					itemDevolucion.getDouble("IMPORTE_INTERESES"),
					itemDevolucion.getDate("FECHA_DEVOLUCION"),
					itemDevolucion.getString("CODIGO_CAUSA"), 
					itemDevolucion.getString("CAUSA")
			);
			
			listDevoluciones.add(devolucion);
		}
		
		return listDevoluciones;
	}

}
