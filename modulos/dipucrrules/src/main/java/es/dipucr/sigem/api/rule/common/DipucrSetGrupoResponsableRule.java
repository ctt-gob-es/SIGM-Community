package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.invesdoc.directory.InvesDocEntry;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Setea el grupo responsable del procedimiento en función
 * en función del usuario que lo inicia
 * @author Felipe
 *
 */
public class DipucrSetGrupoResponsableRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			String numexp = rulectx.getNumExp();
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IRespManagerAPI respAPI = invesflowAPI.getRespManagerAPI();
			
			String uidUsuario = ((ClientContext) cct).getUser().getUID();
			
			IItem itemExpediente = entitiesAPI.getExpedient(numexp);
			
			//Grupos que tienen permisos en el expediente
			int idPcd = itemExpediente.getInt("ID_PCD");
			IItemCollection colPermisos = respAPI.getPermissions(idPcd);
			@SuppressWarnings("unchecked")
			List<IItem> listPermisos = colPermisos.toList();
			
			IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();
			String uidGrupoUsuario = null;
			IItem itemPermiso = null;
			
			//Recorremos la lista de grupos para localizar a cúal de los grupos pertenece el usuario
			for (int i = 0; i < listPermisos.size() && StringUtils.isEmpty(uidGrupoUsuario); i++){
				
				itemPermiso = listPermisos.get(i);
				String uidGrupo = itemPermiso.getString("UID_USR");
				@SuppressWarnings("unchecked")
				Set<InvesDocEntry> setUsuariosGrupo = directory.getMembersFromUID(uidGrupo);
				InvesDocEntry[] arrUsuariosGrupo = new InvesDocEntry[setUsuariosGrupo.size()];
				arrUsuariosGrupo = setUsuariosGrupo.toArray(arrUsuariosGrupo);
				InvesDocEntry entry = null;
				
				//Recorremos los usuarios del grupo
				for (int j = 0; j < arrUsuariosGrupo.length && StringUtils.isEmpty(uidGrupoUsuario); j++){
					entry = arrUsuariosGrupo[j];
					if (uidUsuario.equals(entry.getUID())){
						uidGrupoUsuario = uidGrupo;
					}
				}
			}
			
			//Si hemos encontrado el grupo del usuario, cambiamos los permisos
			if (!StringUtils.isEmpty(uidGrupoUsuario)){

				IProcess itemProcess = invesflowAPI.getProcess(numexp);
				itemProcess.set("ID_RESP", uidGrupoUsuario);
				itemProcess.store(cct);
				
				IStage itemFase = invesflowAPI.getStage(rulectx.getStageId());
				itemFase.set("ID_RESP", uidGrupoUsuario);
				itemFase.store(cct);
			}
			
		}
		catch (Exception ex){
			throw new ISPACRuleException
				("Error al setear el nuevo responsable del procedimiento", ex);
		}
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
