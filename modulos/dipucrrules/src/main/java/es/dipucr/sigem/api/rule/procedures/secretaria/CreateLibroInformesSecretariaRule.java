package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.join.TableJoinFactoryDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * [dipucr-Felipe #869]
 * Crea el libro de informes de secretaría
 */
public class CreateLibroInformesSecretariaRule extends CreateLibroDocumentosRule {

    private static final Logger LOGGER = Logger.getLogger(CreateLibroInformesSecretariaRule.class);
    
    public static final String PLANTILLA_LIBRO = "Libro de Informes de Secretaría";
	public static final String DOC_BUSCAR = "Informe de Secretaría"; 
	
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
    	plantillaLibro = PLANTILLA_LIBRO;
        return true;
    }

	@Override
	protected ArrayList<String> getListDocumentos(IRuleContext rulectx) throws ISPACRuleException {

		try{
        	IClientContext cct = rulectx.getClientContext();
            
            StringBuffer sbQuery = new StringBuffer();
            sbQuery.append(" WHERE INF.NUMEXP = DOC.NUMEXP");
            sbQuery.append(" AND DOC.DESCRIPCION = '" + DOC_BUSCAR + "'");
            sbQuery.append(" AND DOC.ESTADOFIRMA = '" + SignStatesConstants.FIRMADO + "'");
            sbQuery.append(" AND DOC.FDOC >= DATE('" + fechaInicio + "')");
            sbQuery.append(" AND DOC.FDOC < DATE('" + FechasUtil.addDias(fechaFin, 1) + "')");
            sbQuery.append(" ORDER BY INF.ANIO, INF.NUMERO");

			TableJoinFactoryDAO factory = new TableJoinFactoryDAO();
			factory.addTable("INFORME_SECR", "INF");
			factory.addTable("SPAC_DT_DOCUMENTOS", "DOC");

			DbCnt cnt = cct.getConnection();
			CollectionDAO collectionJoin = factory.queryTableJoin(cnt, sbQuery.toString());
			collectionJoin.disconnect();
			cct.releaseConnection(cnt);
            
            ArrayList<String> listInformes = new ArrayList<String>();
            
            while (collectionJoin.next()){
            	
            	IItem itemDocumento = collectionJoin.value();
            	String infopagRde = itemDocumento.getString("DOC:INFOPAG_RDE");
            	listInformes.add(infopagRde);
            }
            
            return listInformes;
            
        } catch(Exception e){
        	String error = "Error al recuperar los documentos del libro de informes de Secretaría";
        	LOGGER.error(error, e);
            throw new ISPACRuleException(error, e);
        }
	}    
}
