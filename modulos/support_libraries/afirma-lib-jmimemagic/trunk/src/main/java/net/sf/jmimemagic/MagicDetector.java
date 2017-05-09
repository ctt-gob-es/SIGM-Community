/*
jMimeMagic(TM) is a Java library for determining the content type of files or
streams.

Copyright (C) 2004 David Castro

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

For more information, please email arimus@users.sourceforge.net
*/
package net.sf.jmimemagic;

import java.io.File;
import java.util.Map;

/** Detector gen&eacute;rico de tipo de datos. */
public interface MagicDetector {

    /** Get the short name of this detector.
     * @return Short name of this detector. */
    String getName();

    /** Get the display name of this detector.
     * @return Display name of this detector. */
    String getDisplayName();

    /** Get the version of this plugin.
     * @return Version of this plugin. */
    String getVersion();

    /** Gget a list of types this detector handles.
     * @return List of types this detector handles. */
    String[] getHandledTypes();

    /** Gget a list of file extensions this detector typically deals with.
     * @return List of file extensions this detector typically deals with. */
    String[] getHandledExtensions();

    /** Process the stream and return all matching content types.
     * @param data
     * @param offset
     * @param length
     * @param bitmask
     * @param comparator
     * @param mimeType
     * @param params
     * @return Matching content types. */
    String[] process(byte[] data, int offset, int length, long bitmask, char comparator,
        String mimeType, Map<String, String> params);

    /** Process the file and return all matching content types.
     * @param file
     * @param offset
     * @param length
     * @param bitmask
     * @param comparator
     * @param mimeType
     * @param params
     * @return Matching content types. */
    String[] process(File file, int offset, int length, long bitmask, char comparator,
        String mimeType, Map<String, String> params);
}
