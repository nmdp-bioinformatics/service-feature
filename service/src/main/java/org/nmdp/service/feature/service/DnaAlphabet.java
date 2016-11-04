/*

    feature-service  Feature service.
    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.service.feature.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DNA alphabet.
 */
public final class DnaAlphabet {
    private static final Pattern DNA = Pattern.compile("^[ACTG]*$");

    /**
     * Return true if the specified sequence contains only members of the DNA alphabet [A,C,T,G].
     *
     * @param sequence sequence
     * @return true if the specified sequence contains only members of the DNA alphabet [A,C,T,G]
     */
    public static boolean isDna(final String sequence) {
        Matcher matcher = DNA.matcher(sequence);
        return matcher.matches();
    }
}
