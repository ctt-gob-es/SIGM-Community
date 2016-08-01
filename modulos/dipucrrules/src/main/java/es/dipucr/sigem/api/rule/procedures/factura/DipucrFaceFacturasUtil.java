package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.dipucr.factura.services.factura.FacturaWSProxy;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;


/**
 * #1101 Procedimiento SIGEM para Factur@-FACe
 * Métodos y constantes comunes de facturación
 * @author dipucr-Felipe
 * @since 06.05.2014
 */
public class DipucrFaceFacturasUtil
{
	/**
     * Log.
     */
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(DipucrFaceFacturasUtil.class);
	
	/**
	 * Departamento de compras
	 */
	public static final String DEP_COMPRAS = "CONTRA";
	
	/**
	 * Tipo de documento "Conformación factura"
	 */
	public static final String DOCUMENTO_FACTURA = "Conformación Factura";
	public static final String COD_DOC_FACTURA = "EFACT_CONFORMA";
	
	/**
	 * Códigos de los trámites
	 */
	public static final String COD_TRAM_REVISION1 = "EFACT_REV1";
	public static final String COD_TRAM_REVISION2 = "EFACT_REV2";
	public static final String COD_TRAM_REVISION3 = "EFACT_REV3"; //[dipucr-Felipe #1151]
	public static final String COD_TRAM_FIRMA = "EFACT_FIRMA";
	public static final String COD_TRAM_ANULACION = "EFACT_ANULACION";
	
	/**
	 * Recupera la entidad de factura
	 * @param context
	 * @param numExp
	 * @return
	 * @throws ISPACException
	 */
	public static IItem getFacturaEntity(IClientContext context, String numExp)
			throws ISPACException {
		try {
			IInvesflowAPI invesflowAPI = context.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			@SuppressWarnings("rawtypes")
			List facturas = entitiesAPI.queryEntities("FACTURAE", 
					String.format("WHERE NUMEXP = '%s'", new Object[] { numExp })).toList();

			if (facturas.size() != 1)
				throw new IllegalArgumentException(
						"El expediente no se ha creado con los datos de la factura "
						+ "y el adjunto (mediante los datos especificos)");
			return (IItem) facturas.get(0);
		} catch (Throwable e) {
			throw new ISPACException("Error al obtener la entidad EFACTURA", e);
		}
	}
	
	/**
	 * Recupera la entidad de anulación de factura
	 * @param context
	 * @param numExp
	 * @return
	 * @throws ISPACException
	 */
	public static IItem getAnulacionEntity(IClientContext context, String numExp)
			throws ISPACException {
		try {
			IInvesflowAPI invesflowAPI = context.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

			@SuppressWarnings("rawtypes")
			List facturas = entitiesAPI.queryEntities("ANULACION_FACTURAE", 
					String.format("WHERE NUMEXP = '%s'", new Object[] { numExp })).toList();

			if (facturas.size() != 1)
				throw new IllegalArgumentException(
						"El expediente no se ha creado con los datos de anulación de la factura");
			return (IItem) facturas.get(0);
		} catch (Throwable e) {
			throw new ISPACException("Error al obtener la entidad EFACTURA", e);
		}
	}
	
	/**
	 * Cambia el estado de la factura en FACe
	 * @param nreg
	 * @param codEstado
	 * @param subtipo
	 * @param motivo
	 * @throws ISPACException 
	 */
	public static void cambiarEstadoFactura
		(IClientContext cct, String nreg, String codEstado, int subtipo, String motivo) throws ISPACException
	{
		try{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			FacturaWSProxy ws = new FacturaWSProxy();
			ws.cambiarEstadoFactura(idEntidad, nreg, codEstado, subtipo, motivo);
		}
		catch(Exception ex){
			throw new ISPACException("Error al llamar al servicio de cambiarEstadoFactura", ex);
		}
	}
	
	/**
	 * [dipucr-Felipe #1303]
	 * Envía el documento de factura firmado correspondiente
	 * @param nreg
	 * @param itemDocumento
	 * @throws ISPACException 
	 */
	public static void enviarFacturaFirmada
		(IClientContext cct, String nreg, IItem itemDocumento) throws ISPACException
	{
		try{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			String strInfopag = itemDocumento.getString("INFOPAG_RDE");
			File file =  DocumentosUtil.getFile(cct, strInfopag, null, null);
			
			byte[] data = new byte[(int) file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
		    fileInputStream.read(data);
		    fileInputStream.close();
			
			FacturaWSProxy ws = new FacturaWSProxy();
			ws.enviarFacturaFirmada(idEntidad, nreg, data);
		}
		catch(Exception ex){
			throw new ISPACException("Error al llamar al servicio de enviarFacturaFirmada", ex);
		}
	}
	
	/**
	 * Cambia el estado de la factura en FACe
	 * @param nreg
	 * @param codEstado
	 * @param motivo
	 * @throws ISPACException 
	 */
//	public static void cambiarEstadoFactura
//		(IClientContext cct, String nreg, String codEstado, String motivo) throws ISPACException
//	{
//		cambiarEstadoFactura(cct, nreg, codEstado, 0, motivo);
//	}
	
	/**
	 * Cambia el estado de anulación de la factura en FACe
	 * @param nreg
	 * @param codEstado
	 * @param motivo
	 * @throws ISPACException 
	 */
	public static void cambiarEstadoAnulacionFactura
		(IClientContext cct, String nreg, String codEstado, String motivo) throws ISPACException
	{
		try{
			String idEntidad = EntidadesAdmUtil.obtenerEntidad(cct);
			
			FacturaWSProxy ws = new FacturaWSProxy();
			ws.cambiarEstadoAnulacionFactura(idEntidad, nreg, codEstado, motivo);
		}
		catch(Exception ex){
			throw new ISPACException("Error al llamar al servicio de cambiarEstadoAnulacionFactura", ex);
		}
	}
	
	/**
	 * Devuelve el resumen de la factura para incluir en el registro y en
	 * el asunto del expediente.
	 * @param itemFactura
	 * @return
	 * @throws ISPACException 
	 */
	public static String getResumenFactura(IItem itemFactura) throws ISPACException{
		
		String numSerie = itemFactura.getString("SERIE");
		String numFactura = itemFactura.getString("NUMERO");
		String numFacturaCompleta = "";
		if (!StringUtils.isEmpty(numSerie)){
			numFacturaCompleta = numSerie + "/" + numFactura;
		}
		else{
			numFacturaCompleta = numFactura;
		}
		String proveedor = itemFactura.getString("NOMBRE_PROVEEDOR");
		String importe = String.valueOf(itemFactura.getDouble("IMPORTE"));
		String nregFace = itemFactura.getString("NREG_FACE");
		String nregRcf = itemFactura.getString("RCF_NREG");
		
		//Texto del resumen
		StringBuffer sbResumen = new StringBuffer();
		sbResumen.append(proveedor);
		sbResumen.append(". Nº factura: ");
		sbResumen.append(numFacturaCompleta);
		sbResumen.append(". Importe: ");
		sbResumen.append(importe);
		sbResumen.append(" euros. Nº Face: ");
		sbResumen.append(nregFace);
		sbResumen.append(". Nº Contable: ");
		sbResumen.append(nregRcf);
		sbResumen.append(".");
		
		return sbResumen.toString();
	}
}
