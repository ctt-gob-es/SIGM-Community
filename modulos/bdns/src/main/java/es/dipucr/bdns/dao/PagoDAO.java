package es.dipucr.bdns.dao;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.dipucr.bdns.objetos.Pago;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class PagoDAO {

	public static String TABLENAME = "BDNS_IGAE_PAGO";
	
	/**
	 * Crea un objeto de tipo Pago
	 * @param numexpConvocatoria
	 * @param cifBeneficiario
	 * @param idConvocatoria
	 * @param refConcesion
	 * @param datosPagoSical
	 * @return
	 * @throws Exception
	 */
	public Pago newObjectNotPersist(String numexpConvocatoria,String cifBeneficiario, String idConvocatoria, 
			String refConcesion, String numOperacion, String fechaOperacion, String importe) throws Exception {
		
		Pago pago = new Pago();
		pago.setIdConvocatoria(idConvocatoria);
		pago.setNumexpConvocatoria(numexpConvocatoria);
		pago.setReferenciaConcesion(refConcesion);
		pago.setCifBeneficiario(cifBeneficiario);
		pago.setReferenciaPago(numOperacion);
		pago.setFechaPago(FechasUtil.convertToDate(fechaOperacion));
		
		pago.setImportePagado(Double.valueOf(importe.replace(",", ".")));
		pago.setbRetencion(false);//En las subvenciones nunca hay retención (Encarna 29-09-16)
		return pago;
	}

	/**
	 * Comprueba si un pago existe ya en la BBDD
	 * @param entitiesAPI
	 * @param pago
	 * @return
	 * @throws ISPACException
	 */
	public boolean existsBBDD(IEntitiesAPI entitiesAPI, Pago pago)	throws ISPACException {

		StringBuffer sbQueryPago = new StringBuffer();
		sbQueryPago.append("WHERE NUMEXP='" + pago.getNumexpConvocatoria() + "'");
		sbQueryPago.append(" AND ID_CONVOCATORIA='" + pago.getIdConvocatoria() + "'");
		sbQueryPago.append(" AND CIF_BENEFICIARIO='" + pago.getCifBeneficiario() + "'");
		sbQueryPago.append(" AND REFERENCIA_PAGO='" + pago.getReferenciaPago() + "'");
		
		IItemCollection colPagos = entitiesAPI.queryEntities(TABLENAME, sbQueryPago.toString());
		boolean bExistePago = (colPagos.iterator().hasNext());
		return bExistePago;
	}
	
	/**
	 * Almacena el pago en la BBDD
	 * @param cct
	 * @param pago
	 * @throws ISPACException
	 */
	public void store(IClientContext cct, Pago pago) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		IItem itemPago = entitiesAPI.createEntity(TABLENAME, pago.getNumexpConvocatoria());
		itemPago.set("ID_CONVOCATORIA", pago.getIdConvocatoria());
		itemPago.set("CIF_BENEFICIARIO", pago.getCifBeneficiario());
		itemPago.set("REFERENCIA_PAGO", pago.getReferenciaPago());
		itemPago.set("IMPORTE", pago.getImportePagado());
		itemPago.set("FECHA_PAGO", pago.getFechaPago());
		if (pago.isbRetencion()){
			itemPago.set("RETENCION", Constants.VALIDACION.SI);
		}
		else{
			itemPago.set("RETENCION", Constants.VALIDACION.NO);
		}
		itemPago.store(cct);
	}
	
	/**
	 * Devuelve los pagos de una concesiom
	 * @param cct
	 * @param idConvocatoria
	 * @param cifBeneficiario
	 * @return
	 * @throws ISPACException 
	 */
	public List<Pago> getPagosByConcesion(IClientContext cct, String idConvocatoria, String cifBeneficiario) throws ISPACException{
		
		ArrayList<Pago> listPagos = new ArrayList<Pago>();
		
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		StringBuffer sbQueryPagos = new StringBuffer();
		sbQueryPagos.append(" WHERE ID_CONVOCATORIA='" + idConvocatoria + "'");
		sbQueryPagos.append(" AND CIF_BENEFICIARIO='" + cifBeneficiario + "'");
		IItemCollection colPagos = entitiesAPI.queryEntities(TABLENAME, sbQueryPagos.toString());
		@SuppressWarnings("rawtypes")
		Iterator it = colPagos.iterator();
		
		while(it.hasNext()){
			IItem itemPago = (IItem) it.next();
			
			Pago pago = new Pago
			(
					idConvocatoria,
					itemPago.getString("NUMEXP"),
					cifBeneficiario, 
					null,
					itemPago.getString("REFERENCIA_PAGO"),
					itemPago.getDouble("IMPORTE"),
					itemPago.getDate("FECHA_PAGO"),
					(Constants.VALIDACION.SI.equals(itemPago.getString("RETENCION")))
			);
			listPagos.add(pago);
		}
		
		return listPagos;
	}
	
}
