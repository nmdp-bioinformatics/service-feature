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
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.service.FeatureService;

/**
 * Unit test for FeatureResource.
 */
public final class FeatureResourceTest {
    private Feature feature;
    private FeatureResource featureResource;

    @Mock
    private FeatureService featureService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        feature = new Feature("locus", "term", 2, 42L, "ACGT");
        featureResource = new FeatureResource(featureService);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullFeatureService() {
        new FeatureResource(null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(featureResource);
    }

    @Test
    public void testGetFeatureByQuery() {
        when(featureService.getFeature("locus", "term", 2, 42L)).thenReturn(feature);
        assertEquals(feature, featureResource.getFeatureByQuery("locus", "term", 2, 42L));
    }

    @Test
    public void testGetFeatureByQueryMiss() {
        when(featureService.getFeature("locus", "term", 2, 42L)).thenReturn(null);
        // todo: this should throw a 404
        assertNull(featureResource.getFeatureByQuery("locus", "term", 2, 42L));
    }

    @Test
    public void testGetFeatureByPath() {
        when(featureService.getFeature("locus", "term", 2, 42L)).thenReturn(feature);
        assertEquals(feature, featureResource.getFeatureByPath("locus", "term", 2, 42L));
    }

    @Test
    public void testGetFeatureByPathMiss() {
        when(featureService.getFeature("locus", "term", 2, 42L)).thenReturn(null);
        assertNull(featureResource.getFeatureByPath("locus", "term", 2, 42L));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateFeatureNullFeatureRequest() {
        featureResource.createFeature(null);
    }

    @Test
    public void testCreateFeatureNullSequence() {
        assertNull(featureResource.createFeature(new FeatureRequest("locus", "term", 2, null)));
    }

    @Test
    public void testCreateFeatureValidSequence() {
        when(featureService.createFeature("locus", "term", 2, "ACGT")).thenReturn(feature);
        assertEquals(feature, featureResource.createFeature(new FeatureRequest("locus", "term", 2, "ACGT")));
    }
}
