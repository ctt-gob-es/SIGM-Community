package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CrearTramiteInfIntervencionRule
  implements IRule
{
  public static int _ID_PREPARACION = 69;

  public boolean init(IRuleContext rulectx) throws ISPACRuleException {
    return true;
  }

  public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
    return true;
  }

  public Object execute(IRuleContext rulectx)
    throws ISPACRuleException
  {
    try
    {
    	TramitesUtil.crearTramite(_ID_PREPARACION, rulectx);
    }
    catch (Exception e)
    {
      throw new ISPACRuleException(e);
    }

    return null;
  }

  public void cancel(IRuleContext rulectx)
    throws ISPACRuleException
  {
  }
}