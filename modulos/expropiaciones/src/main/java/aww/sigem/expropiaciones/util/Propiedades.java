package aww.sigem.expropiaciones.util;

import java.util.ResourceBundle;

       public class Propiedades {

               private static final ResourceBundle rb = ResourceBundle.getBundle("codigos");
               /**
                * Devuelve el valor de la propiedad ''key''
                * @param key nombre de la propiedad
                * @return
                */
               public static String getString(String key){
                       try{
                               return rb.getString(key);
                       }catch(Exception exc){
                               exc.printStackTrace();
                       }
                       return null;
               }

       }

