package es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

import es.ieci.tecdoc.fwktd.sir.core.types.EstadoAsientoRegistralEnum;


/**
 * Enumerado de estado de intercambio de registro de salida dentro de isicres.
 *
 *
 */

public final class EstadoIntercambioRegistralSalidaEnumVO extends ValuedEnum {
	   /**
	 * 
	 */
	private static final long serialVersionUID = -3752212132916841043L;

	public static final int PENDIENTE_ENVIO_VALUE = EstadoAsientoRegistralEnum.PENDIENTE_ENVIO.getValue();

	public static final int ENVIADO_VALUE = EstadoAsientoRegistralEnum.ENVIADO.getValue();		

	  public static final int ENVIADO_Y_ERROR_VALUE = EstadoAsientoRegistralEnum.ENVIADO_Y_ERROR.getValue();

	  public static final int DEVUELTO_VALUE = EstadoAsientoRegistralEnum.DEVUELTO.getValue();

	  public static final int ACEPTADO_VALUE = EstadoAsientoRegistralEnum.ACEPTADO.getValue();

	  public static final int ANULADO_VALUE = EstadoAsientoRegistralEnum.ANULADO.getValue();

	  public static final int VALIDADO_VALUE = EstadoAsientoRegistralEnum.VALIDADO.getValue();
	  
	  public static final int REENVIADO_VALUE = EstadoAsientoRegistralEnum.REENVIADO.getValue();
	  
	  public static final int RECTIFICADO_VALUE = 999;

	  private static final java.lang.String PENDIENTE_ENVIO_STRING = "Pendiente de envío";

	  private static final java.lang.String ENVIADO_STRING = "Enviado";

	  private static final java.lang.String ENVIADO_Y_ERROR_STRING = "Enviado y ERROR";

	  private static final java.lang.String DEVUELTO_STRING = "Devuelto";
	  
	  private static final java.lang.String ACEPTADO_STRING = "Aceptado";
	  
	  private static final java.lang.String ANULADO_STRING = "Anulado";
	  
	  private static final java.lang.String VALIDADO_STRING = "Validado";
	 
	  private static final java.lang.String REENVIADO_STRING = "Reenviado";
	
	  private static final java.lang.String RECTIFICADO_STRING = "Rectificado";
	  
	  public static final EstadoIntercambioRegistralSalidaEnumVO  PENDIENTE  = new EstadoIntercambioRegistralSalidaEnumVO( PENDIENTE_ENVIO_STRING, PENDIENTE_ENVIO_VALUE );
	   public static final EstadoIntercambioRegistralSalidaEnumVO  ENVIADO  = new EstadoIntercambioRegistralSalidaEnumVO( ENVIADO_STRING, ENVIADO_VALUE );
	   public static final EstadoIntercambioRegistralSalidaEnumVO  ENVIADO_ERROR  = new EstadoIntercambioRegistralSalidaEnumVO(ENVIADO_Y_ERROR_STRING, ENVIADO_Y_ERROR_VALUE );
	   public static final EstadoIntercambioRegistralSalidaEnumVO  ANULADO  = new EstadoIntercambioRegistralSalidaEnumVO( ANULADO_STRING, ANULADO_VALUE );
	   public static final EstadoIntercambioRegistralSalidaEnumVO  ACEPTADO = new EstadoIntercambioRegistralSalidaEnumVO( ACEPTADO_STRING, ACEPTADO_VALUE);
	   public static final EstadoIntercambioRegistralSalidaEnumVO  DEVUELTO = new EstadoIntercambioRegistralSalidaEnumVO( DEVUELTO_STRING, DEVUELTO_VALUE);	   
	   public static final EstadoIntercambioRegistralSalidaEnumVO  VALIDADO = new EstadoIntercambioRegistralSalidaEnumVO( VALIDADO_STRING, VALIDADO_VALUE);	   
	   public static final EstadoIntercambioRegistralSalidaEnumVO  REENVIADO = new EstadoIntercambioRegistralSalidaEnumVO( REENVIADO_STRING, REENVIADO_VALUE);	   
	   public static final EstadoIntercambioRegistralSalidaEnumVO  RECTIFICADO = new EstadoIntercambioRegistralSalidaEnumVO( RECTIFICADO_STRING, RECTIFICADO_VALUE);	   
	   
	   private EstadoIntercambioRegistralSalidaEnumVO(String name, int value) {
	     super( name, value );
	   }
	 
	   public static EstadoIntercambioRegistralSalidaEnumVO getEnum(String value) {
	     return (EstadoIntercambioRegistralSalidaEnumVO) getEnum(EstadoIntercambioRegistralSalidaEnumVO.class, value);
	   }
	 
	   public static EstadoIntercambioRegistralSalidaEnumVO getEnum(int value) {
	     return (EstadoIntercambioRegistralSalidaEnumVO) getEnum(EstadoIntercambioRegistralSalidaEnumVO.class, value);
	   }
	 
	   public static Map getEnumMap() {
	     return getEnumMap(EstadoIntercambioRegistralSalidaEnumVO.class);
	   }
	 
	   public static List getEnumList() {
	     return getEnumList(EstadoIntercambioRegistralSalidaEnumVO.class);
	   }
	 
	   public static Iterator iterator() {
	     return (Iterator) iterator(EstadoIntercambioRegistralSalidaEnumVO.class);
	   }

	 }

