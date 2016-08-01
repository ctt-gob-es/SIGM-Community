package ieci.tdw.ispac.api.rule.procedures.bop;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Regla para ser utilizada desde una plantilla. Recibe el parámetro CLASIFICACION que puede tomar los siguientes valores:
 * 
 * diputacion
 * ayuntamiento
 * consejeria
 * ministerio
 * otra
 * particular
 * 
 * Devuelve una cadena con las entidades de cada grupo de clasificacion y sus anuncios correspondientes.
 *
 */
public class GetLiquidacionRecibosTagRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		ClientContext cct = null;

		System.out.println("INICIO.");
		
		try{
			//----------------------------------------------------------------------------------------------
			cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			//----------------------------------------------------------------------------------------------

			// Abrir transacción
			cct.beginTX();

			//Obtenemos el parámetro CLASIFICACION de la plantilla
			String clasificacion = rulectx.get("clasificacion");

			System.out.println("- CLASIFICACION: "+clasificacion);

			String listado = null;

			String strQuery = null;
			IItemCollection collection = null;
			Iterator it = null;
			IItem item = null;


			//FECHA_PUBLICACION != NULL

			strQuery = "WHERE CLASIFICACION = '"+ clasificacion +"' AND TIPO_FACTURACION = 'Pago sin crédito' ORDER BY ENTIDAD";
			collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
			it = collection.iterator();
			String entidad = "-1";
			String numexp = null;
			String sumario = null;
			String texto = null;
			String observaciones = null;
			double coste = -1;
			double costeEntidad = 0;
			int anunciosEntidad = 0;
			Date fechaPublicacion = null;
			boolean finIterator = false;
			int numEntidades = 0;

			while (it.hasNext()){

				item = (IItem)it.next();

				while (!finIterator && item.getString("ENTIDAD")!=null && entidad.equalsIgnoreCase(item.getString("ENTIDAD"))){

					listado += "*";

					numexp = item.getString("NUMEXP");
					if (numexp != null){
						listado += numexp + "\n";
					}

					sumario = item.getString("SUMARIO");
					if (sumario != null){
						listado += sumario + "\n";
					}

					texto = item.getString("TEXTO");
					if (texto != null){
						listado += texto + "\n";
					}

					observaciones = item.getString("OBSERVACIONES");
					if (observaciones != null){
						listado += observaciones + "\n";
					}

					coste = item.getDouble("COSTE");
					if (coste >= 0){
						listado += coste + "\n";
					}

					fechaPublicacion = item.getDate("FECHA_PUBLICACION");
					if (fechaPublicacion != null){
						listado += fechaPublicacion + "\n";
					}

					anunciosEntidad++;
					costeEntidad = costeEntidad + coste;

					if (it.hasNext()){
						item = (IItem)it.next();
					}else{
						finIterator = true;
					}

				}

				if (anunciosEntidad > 0){
					if (!finIterator){
						listado += "anunciosEntidad: "+anunciosEntidad+", costeEntidad: "+costeEntidad;
						System.out.println("1----- anunciosEntidad: "+anunciosEntidad + "costeEntidad: "+costeEntidad);
					}
					anunciosEntidad = 0;
					costeEntidad = 0;
				}


				if (!finIterator){
					//Obtener los campos a mostrar en el listado del documento
					entidad = item.getString("ENTIDAD");
					if (entidad != null){

						numEntidades++;
						
						listado = "\n\n- " + entidad + ":\n\n";

						listado += "*";

						numexp = item.getString("NUMEXP");
						if (numexp != null){
							listado += numexp + "\n";
						}

						sumario = item.getString("SUMARIO");
						if (sumario != null){
							listado += sumario + "\n";
						}

						texto = item.getString("TEXTO");
						if (texto != null){
							listado += texto + "\n";
						}

						observaciones = item.getString("OBSERVACIONES");
						if (observaciones != null){
							listado += observaciones + "\n";
						}

						coste = item.getDouble("COSTE");
						if (coste >= 0){
							listado += coste + "\n";
						}

						fechaPublicacion = item.getDate("FECHA_PUBLICACION");
						if (fechaPublicacion != null){
							listado += fechaPublicacion + "\n";
						}

						anunciosEntidad++;
						costeEntidad = costeEntidad + coste;
						System.out.println("2----- anunciosEntidad: "+anunciosEntidad + "costeEntidad: "+costeEntidad);
						//listado += "anunciosEntidad: "+anunciosEntidad+", costeEntidad: "+costeEntidad;

					}

				}
			}
			if (numEntidades>0 && !finIterator){
				listado += "anunciosEntidad: "+anunciosEntidad+", costeEntidad: "+costeEntidad;
				System.out.println("3----- anunciosEntidad: "+anunciosEntidad + "costeEntidad: "+costeEntidad);
			}











			return listado;

		} catch (Exception e) {

			// Si se produce algún error se hace rollback de la transacción
			try {
				cct.endTX(false);
			} catch (ISPACException e1) {
				e1.printStackTrace();
			}

			throw new ISPACRuleException("Error al obtener la liquidación mensual de cobros de recibos.", e);
		}     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
