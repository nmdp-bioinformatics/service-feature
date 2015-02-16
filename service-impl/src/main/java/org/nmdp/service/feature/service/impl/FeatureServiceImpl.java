/*

    feature-service-impl  Feature service impl.
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
package org.nmdp.service.feature.service.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.Immutable;

import org.dishevelled.multimap.TernaryKeyMap;
import org.dishevelled.multimap.QuaternaryKeyMap;

import org.dishevelled.multimap.impl.TernaryKeyMaps;
import org.dishevelled.multimap.impl.QuaternaryKeyMaps;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.service.FeatureService;

/**
 * In-memory implementation of FeatureService.
 */
@Immutable
final class FeatureServiceImpl implements FeatureService {
    private final QuaternaryKeyMap<String, String, Integer, Long, Feature> features = QuaternaryKeyMaps.createQuaternaryKeyMap();
    private final TernaryKeyMap<String, String, Integer, SequencesToFeatures> sequencesToFeatures = TernaryKeyMaps.createTernaryKeyMap();

    FeatureServiceImpl() {
        // empty
    }

    @Override
    public Feature getFeature(final String locus,
                              final String term,
                              final int rank,
                              final long accession) {

        checkNotNull(locus);
        checkNotNull(term);
        checkArgument(rank > 0, "rank must be at least 1");
        checkArgument(accession > 0L, "accession must be at least 1L");
        return features.get(locus, term, rank, accession);
    }

    @Override
    public Feature createFeature(final String locus,
                                 final String term,
                                 final int rank,
                                 final String sequence) {

        checkNotNull(locus);
        checkNotNull(term);
        checkArgument(rank > 0, "rank must be at least 1");
        checkNotNull(sequence);
        if (!sequencesToFeatures.containsKey(locus, term, rank)) {
            sequencesToFeatures.put(locus, term, rank, new SequencesToFeatures());
        }
        return sequencesToFeatures.get(locus, term, rank).createFeature(locus, term, rank, sequence);
    }

    /**
     * Mapping of sequences to features.
     */
    final class SequencesToFeatures {
        private final AtomicLong ids;
        private final Set<String> sequences;
        private final Map<String, Feature> sequencesToFeatures;

        SequencesToFeatures() {
            ids = new AtomicLong(1L);
            sequences = new HashSet<String>();
            sequencesToFeatures = new HashMap<String, Feature>();
        }        

        Feature createFeature(final String locus,
                              final String term,
                              final int rank,
                              final String sequence) {

            if (sequences.contains(sequence)) {
                return sequencesToFeatures.get(sequence);
            }
            long accession = ids.getAndIncrement();
            Feature feature = new Feature(locus, term, rank, accession, sequence);
            sequences.add(sequence);
            sequencesToFeatures.put(sequence, feature);
            features.put(locus, term, rank, accession, feature); // causes this inner class not to be static
            return feature;
        }
    }
}
