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

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test for implementations of FeatureService.
 */
public abstract class AbstractFeatureServiceTest {
    protected FeatureService featureService;
    protected abstract FeatureService createFeatureService();

    @Before
    public void setUp() {
        featureService = createFeatureService();
    }

    @Test
    public final void testCreateFeatureService() {
        assertNotNull(featureService);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetFeatureNullLocus() {
        featureService.getFeature(null, "term", 2, 42L);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetFeatureNullTerm() {
        featureService.getFeature("locus", null, 2, 42L);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testGetFeatureInvalidRank() {
        featureService.getFeature("locus", "term", 0, 42L);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testGetFeatureInvalidAccession() {
        featureService.getFeature("locus", "term", 2, 0L);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateFeatureNullLocus() {
        featureService.createFeature(null, "term", 2, "ACTG");
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateFeatureNullTerm() {
        featureService.createFeature("locus", null, 2, "ACTG");
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testCreateFeatureInvalidRank() {
        featureService.createFeature("locus", "term", 0, "ACTG");
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateFeatureNullSequence() {
        featureService.createFeature("locus", "term", 2, null);
    }

    @Test(expected=NullPointerException.class)
    public final void testListFeaturesNullLocus() {
        featureService.listFeatures(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testListFeaturesLocusTermNullLocus() {
        featureService.listFeatures(null, "term");
    }

    @Test(expected=NullPointerException.class)
    public final void testListFeaturesLocusTermNullTerm() {
        featureService.listFeatures("locus", null);
    }

    @Test(expected=NullPointerException.class)
    public final void testListFeaturesLocusTermRankNullLocus() {
        featureService.listFeatures(null, "term", 1);
    }

    @Test(expected=NullPointerException.class)
    public final void testListFeaturesLocusTermRankNullTerm() {
        featureService.listFeatures("locus", null, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testListFeaturesLocusTermRankInvalidRank() {
        featureService.listFeatures("locus", "term", 0);
    }
}
