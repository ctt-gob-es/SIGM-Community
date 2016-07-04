/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;


public class Validador
{
    public static final int TAM_NIF = 9;
    public static final int NIF_ERROR = -1;
    public static final int NIF_ERROR_TAMANO = -2;
    public static final int NIF_ERROR_BLANCOS = -3;
    public static final int NIF_ERROR_CARACTERES = -4;
    public static final int NIF_ERROR_3LETRAS = -5;
    public static final int NIF_ERROR_DATOSENTRADA = -6;
    public static final int CIF_ERROR_DC = -10;
    public static final int NIF_ERROR_DC = -11;
    public static final int NIF_ERROR_NUM = -12;
    public static final int NIF_ERROR_DOSNUM = -13;
    public static final int DNI_ERROR_MAX = -20;
    public static final int DNI_ERROR_VALOR = -21;
    public static final int DNI_OK = 0;
    public static final int NIF_OK = 1;
    public static final int NIF_NORESIDENTES = 2;
    public static final int NIF_MENORES14ANOS = 3;
    public static final int NIF_EXTRANJEROS = 4;
    public static final int CIF_OK = 20;
    public static final int CIF_EXTRANJERO_OK = 21;
    public static final int CIF_ORGANIZACION_OK = 22;
    public static final int CIF_NORESIDENTES_OK = 23;
    private static final char Numeros[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private static final char Letras[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 
        'W', 'X', 'Y', 'Z'
    };
    private static final char LetrasNIF[] = {
        'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 
        'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 
        'C', 'K', 'E'
    };
    private static final char Letras2CIF[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'
    };
    private static final char LetrasCIF[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'U', 
        'V'
    };
    public static final char LetrasCIFORG_Y_EXTR[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'P', 'Q', 'S', 
        'N', 'W', 'R'
    };
    private static final char LetrasREGATRIBRENTAS[] = {
        'E', 'G', 'H', 'J', 'U', 'V'
    };
    private static final char LetrasCIFEXT[] = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'N', 'W'
    };
    private static final char LetrasNIFEXT[] = {
        'X', 'Y', 'Z'
    };

    public Validador()
    {
    }

    public int checkNif(String s)
    {
        if(s == null || s.length() != 9)
            return -1;
        else
            return vNif(s);
    }

    public int vNif(String s)
    {
        Object obj = null;
        boolean flag = false;
        int k1 = 0;
        long l1 = 0L;
        long l5 = 0L;
        s = s.trim();
        if(s.length() != 9)
            return -2;
        char c = '\0';
        char c1 = '\0';
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        char ac[] = s.toCharArray();
        for(int i = 0; i < 9; i++)
        {
            char c2 = ac[i];
            int i3 = Character.getType(c2);
            if(i3 == 1 && i2 == 0)
            {
                i2++;
                c = c2;
                j2 = i;
                continue;
            }
            if(i3 == 1 && i2 == 1)
            {
                i2++;
                c1 = c2;
                k2 = i;
                continue;
            }
            if(i3 == 1 && i2 == 2)
                return -5;
            if(i3 != 9)
                return -4;
        }

        if(i2 == 0)
        {
            if(ac[0] != '0')
                return -20;
            String s3 = s.substring(1);
            return !s3.equals("11111111") && !s3.equals("22222222") && !s3.equals("33333333") && !s3.equals("44444444") && !s3.equals("55555555") && !s3.equals("66666666") && !s3.equals("77777777") && !s3.equals("88888888") && !s3.equals("99999999") && !s3.equals("00000000") ? 0 : -21;
        }
        if(i2 == 1 && caracEnCad(LetrasCIF, ac[j2]) && j2 == 0 && Character.isDigit(ac[8]))
        {
            int j;
            for(j = 1; j < 8; j++)
            {
                if(j == 2 || j == 4 || j == 6)
                {
                    k1 += ac[j] - 48;
                    continue;
                }
                int i1 = (ac[j] - 48) * 2;
                if(i1 > 9)
                    i1 -= 9;
                k1 += i1;
            }

            k1 = 10 - k1 % 10;
            if(k1 == 10)
                k1 = 0;
            if(k1 == ac[j] - 48)
                return !caracEnCad(LetrasREGATRIBRENTAS, ac[j2]) ? 20 : 23;
            else
                return -10;
        }
        if(i2 == 2 && caracEnCad(LetrasCIFORG_Y_EXTR, ac[j2]) && j2 == 0 && k2 == 8 && caracEnCad(Letras2CIF, ac[k2]))
        {
            for(int k = 1; k < 8; k++)
            {
                if(k == 2 || k == 4 || k == 6)
                {
                    k1 += ac[k] - 48;
                    continue;
                }
                int j1 = (ac[k] - 48) * 2;
                if(j1 > 9)
                    j1 -= 9;
                k1 += j1;
            }

            k1 = 10 - k1 % 10;
            if(Letras2CIF[k1 - 1] == ac[k2])
                return !caracEnCad(LetrasCIFEXT, ac[j2]) ? 22 : 21;
            else
                return -10;
        }
        if(i2 == 1 && caracEnCad(Letras, ac[8]) && caracEnCad(LetrasNIF, ac[j2]) && j2 == 8)
        {
            StringBuffer stringbuffer = new StringBuffer(s.substring(0, j2));
            long l2 = Long.parseLong(stringbuffer.toString());
            long l6 = l2 % 23L;
            if(l6 + 1L > 23L)
                return -12;
            if(c == LetrasNIF[(int)l6])
                return !s.equals("00000001R") && !s.equals("00000000T") && !s.equals("99999999R") ? 1 : -1;
            else
                return -11;
        }
        if(i2 == 2 && (ac[0] == 'K' || ac[0] == 'L' || ac[0] == 'M') && caracEnCad(LetrasNIF, ac[k2]) && k2 == 8)
        {
            String s1 = s.substring(1, 3);
            if(!caracEnCad(Numeros, s1.charAt(0)) || !caracEnCad(Numeros, s1.charAt(1)))
                return -13;
            int l = Integer.parseInt(s1);
            if(l < 1 || l > 56)
                return -1;
            s1 = s.substring(1, k2);
            long l3 = Long.parseLong(s1);
            long l7 = l3 % 23L;
            l7++;
            if(l7 > 23L)
                return -12;
            return c1 != LetrasNIF[(int)(l7 - 1L)] ? -11 : 2;
        }
        if(s.equals("X0000000T"))
            return -1;
        if(i2 == 2 && caracEnCad(LetrasNIFEXT, ac[0]) && caracEnCad(LetrasNIF, ac[k2]) && k2 == 8)
        {
            String s2 = s.substring(1, k2);
            long l4 = Long.parseLong(s2);
            if(ac[0] == 'Y')
                l4 += 0x989680L;
            else
            if(ac[0] == 'Z')
                l4 += 0x1312d00L;
            long l8 = l4 % 23L;
            l8++;
            if(l8 > 23L)
                return -12;
            return c1 != LetrasNIF[(int)(l8 - 1L)] ? -11 : 4;
        } else
        {
            return -1;
        }
    }

    private boolean caracEnCad(char ac[], char c)
    {
        boolean flag = false;
        int i = ac.length;
        int j = 0;
        do
        {
            if(j >= i)
                break;
            if(ac[j] == c)
            {
                flag = true;
                break;
            }
            j++;
        } while(true);
        return flag;
    }


}