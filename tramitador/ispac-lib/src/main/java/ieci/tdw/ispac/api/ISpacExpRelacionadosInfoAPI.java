package ieci.tdw.ispac.api;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;

public interface ISpacExpRelacionadosInfoAPI {
	
	public void addSpacExpRelacionadosInfo(String numexp_padre, String numexp_hijo, int id_tramite_padre, int id_tramite_hijo, String observaciones) throws ISPACRuleException;
	
	public void deleteSpacExpRelacionadosInfoByPadreHijo(int identificadorSpacExpRelacInfo) throws ISPACRuleException;
	
	public IItem getInfoSpacExpRelacionaPadreIdTramitePadre(String numexp_padre, int id_tramite_padre) throws ISPACRuleException;
	
	public IItem getInfoSpacExpRelacionaPadreHijo(String numexp_padre, String numexp_hijo) throws ISPACRuleException;
	
	public IItem getInfoSpacExpRelacionaHijoIdTramiteHijo(String numexp_hijo, int id_tramite_hijo) throws ISPACRuleException;

}
