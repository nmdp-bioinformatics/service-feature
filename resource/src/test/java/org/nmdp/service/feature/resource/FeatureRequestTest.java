/*

    feature-resource  Feature resources.
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
package org.nmdp.service.feature.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for FeatureRequest.
 */
public final class FeatureRequestTest {
    private FeatureRequest featureRequest;

    @Before
    public void setUp() {
        featureRequest = new FeatureRequest("locus", "term", 2, 42L, "ACGT");
    }

    @Test
    public void testConstructor() {
        assertNotNull(featureRequest);
    }

    @Test
    public void testFeatureRequest() {
        assertEquals("locus", featureRequest.getLocus());
        assertEquals("term", featureRequest.getTerm());
        assertEquals(2, featureRequest.getRank());
        assertEquals(42L, featureRequest.getAccession());
        assertEquals("ACGT", featureRequest.getSequence());
    }
}
