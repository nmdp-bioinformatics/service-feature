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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.nmdp.service.feature.service.DnaAlphabet.isDna;

import org.junit.Test;

/**
 * Unit test for DnaAlphabet.
 */
public abstract class DnaAlphabetTest {

    @Test
    public final void testIsDnaNull() {
        assertFalse(isDna(null));
    }

    @Test
    public final void testIsDnaEmpty() {
        assertFalse(isDna(""));
    }

    @Test
    public final void testIsDna() {
        assertFalse(isDna("not dna"));
        assertFalse(isDna("ACTG "));
        assertFalse(isDna("AC-TG"));
        assertFalse(isDna("ACTGNNN"));

        assertTrue(isDna("A"));
        assertTrue(isDna("C"));
        assertTrue(isDna("T"));
        assertTrue(isDna("G"));
        assertTrue(isDna("AC"));
        assertTrue(isDna("ACTG"));
        assertTrue(isDna("AAAATTTTCCCCGGGG"));
    }
}
