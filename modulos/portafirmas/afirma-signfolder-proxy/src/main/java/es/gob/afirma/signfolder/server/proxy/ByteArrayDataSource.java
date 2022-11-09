/*
 * Copyright 2005-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 * Copia de la clase "com.sun.xml.internal.ws.util.ByteArrayDataSource" de SunMicrosystems.
 *
 */
package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 * {@link DataSource} backed by a byte buffer.
 *
 * @author Kohsuke Kawaguchi
 */
public final class ByteArrayDataSource implements DataSource {

	private final String contentType;
	private final byte[] buf;
	private final int start;
	private final int len;

	/**
	 * Crea la fuente de datos.
	 * @param buf Datos de origen.
	 * @param contentType Tipo de datos.
	 */
	public ByteArrayDataSource(final byte[] buf, final String contentType) {
		this(buf, 0, buf.length, contentType);
	}

	/**
	 * Crea la fuente de datos.
	 * @param buf Datos de origen.
	 * @param length Longitud de los datos.
	 * @param contentType Tipo de datos.
	 */
	public ByteArrayDataSource(final byte[] buf, final int length, final String contentType) {
		this(buf, 0, length, contentType);
	}

	/**
	 * Crea la fuente de datos.
	 * @param buf Datos de origgen.
	 * @param start &Iacute;ndice a partir del cual comienzan los datos.
	 * @param length Longitud de los datos.
	 * @param contentType Tipo de datos.
	 */
	public ByteArrayDataSource(final byte[] buf, final int start, final int length, final String contentType) {
		this.buf = buf;
		this.start = start;
		this.len = length;
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		if (this.contentType == null)
			return "application/octet-stream"; //$NON-NLS-1$
			return this.contentType;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.buf, this.start, this.len);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}
}