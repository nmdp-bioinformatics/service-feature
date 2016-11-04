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

import java.util.List;

import org.nmdp.service.feature.Feature;

/**
 * Feature service.
 */
public interface FeatureService {

    /**
     * Return the feature for the specified locus, term, rank, and accession, if any.
     *
     * @param locus locus, must not be null
     * @param term term, must not be null
     * @param rank rank, must be at least <code>1</code>
     * @param accession accession, must be at least <code>1L</code>
     * @return the feature for the specified accession, locus, term, rank, and accession, or <code>null</code>
     *    if no such feature exists
     */
    Feature getFeature(String locus, String term, int rank, long accession);

    /**
     * Create and return the feature for the specified locus, term, rank, and sequence.
     *
     * @param locus locus, must not be null
     * @param term term, must not be null
     * @param rank rank, must be at least <code>1</code>
     * @param sequence sequence, in DNA alphabet, must not be null
     * @return the feature for the specified locus, term, rank, and sequence
     */
    Feature createFeature(String locus, String term, int rank, String sequence);

    /**
     * List the features at the specified locus, if any.
     *
     * @param locus locus, must not be null
     * @return a list of features at the specified locus or an empty list
     *    if no such features exist
     */
    List<Feature> listFeatures(String locus);

    /**
     * List the features at the specified locus and term, if any.
     *
     * @param locus locus, must not be null
     * @param term term, must not be null
     * @return a list of features at the specified locus and term, or an empty list
     *    if no such features exist
     */
    List<Feature> listFeatures(String locus, String term);

    /**
     * List the features at the specified locus, term, and rank, if any.
     *
     * @param locus locus, must not be null
     * @param term term, must not be null
     * @param rank rank, must be at least <code>1</code>
     * @return a list of features at the specified locus, term, and rank, or an empty list
     *    if no such features exist
     */
    List<Feature> listFeatures(String locus, String term, int rank);
}
