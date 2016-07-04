/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util.sigm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.ieci.tecdoc.common.entity.AxXfEntity;
import com.ieci.tecdoc.common.utils.BBDDUtils;

import es.msssi.sigm.core.util.Constants;

public class AxXfEntityMSSSI extends AxXfEntity {

	protected static String FINDBYFRDID_SENTENCE = "SELECT fldid FROM {0} WHERE fdrId=? AND fldid >= "+Constants.FLDID_CAMPO_EXTENDIDO_RP_MSSSI;


	public List<Integer> findByFrdid(Integer fdrid, String entidad, String type) throws Exception {
 
		List<Integer> result = new ArrayList<Integer>();
		BBDDUtils bbddUtils = new BBDDUtils();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = bbddUtils.getConnection(entidad);
			String sentence = MessageFormat.format(FINDBYFRDID_SENTENCE,
					new String[] { "A"+ type +"XF" });
			ps = con.prepareStatement(sentence);
			ps.setInt(1, fdrid);
			rs = ps.executeQuery();
			while(rs.next()){
				int fldid = rs.getInt(1);
				result.add(fldid);
			}
			
		} catch (SQLException ex) {
			log.error("Error en método findByFrdid. FRDID ["
					+ fdrid + "]", ex);
			throw new Exception(ex);
		} catch (NamingException ex) {
			log.error("Error en método findByFrdid. FRDID ["
					+ fdrid + "]", ex);
			throw new Exception(ex);
		} finally {
			bbddUtils.close(rs);
			bbddUtils.close(ps);
			bbddUtils.close(con);
		}

		return result;
	}
	
}
