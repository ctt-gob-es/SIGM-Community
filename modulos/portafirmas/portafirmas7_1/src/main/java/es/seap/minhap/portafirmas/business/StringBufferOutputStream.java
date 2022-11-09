/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.business;
/*
 * Created on Dec 25, 2004
 * 
 * Copyright 2005 CafeSip.org 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *  http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 */

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author amit
 *
 */
public class StringBufferOutputStream extends OutputStream
{
    private StringBuffer textBuffer = null;
    
    public StringBufferOutputStream(StringBuffer textBuffer) {
		super();
		this.textBuffer = textBuffer;
	}

	/**
     * Crea un nuevo objeto StringBuffer y llama al contructor con el par&aacute;metro textBuffer
     */
    public StringBufferOutputStream()
    {
        this(new StringBuffer());
    }

    /**
     * Escribe el byte en la variable textBuffer
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException
    {
        char a = (char)b;
        textBuffer.append(a);
    }
    /**
     * Convierte a cadena los datos del buffer
     * @return la cadena con los datos del buffer
     */
    public String toString()
    {
        return textBuffer.toString();
    }
    /**
     * Borra todos los datos del buffer
     */
    public void clear()
    {
        textBuffer.delete(0, textBuffer.length());
    }
}