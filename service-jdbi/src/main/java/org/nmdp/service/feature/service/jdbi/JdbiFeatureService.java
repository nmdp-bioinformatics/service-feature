/*

    feature-service-jdbi  JDBI feature service.
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
package org.nmdp.service.feature.service.jdbi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.inject.Inject;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.service.FeatureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBI feature service.
 */
@Immutable
final class JdbiFeatureService implements FeatureService {
    private final FeatureDao featureDao;
    private static final Logger logger = LoggerFactory.getLogger(JdbiFeatureService.class);

    @Inject
    JdbiFeatureService(final FeatureDao featureDao) {
        checkNotNull(featureDao);
        this.featureDao = featureDao;
    }

    @Override
    public Feature getFeature(final String locus,
                              final String term,
                              final int rank,
                              final long accession) {

        checkNotNull(locus, "locus must be given");
        checkNotNull(term, "term must be given");
        checkArgument(rank > 0, "rank must be at least 1");
        checkArgument(accession > 0L, "accession must be at least 1L");

        long locusId = featureDao.findLocusId(locus);
        long termId = featureDao.findTermId(term);

        if (logger.isTraceEnabled()) {
            logger.trace("finding feature by locusId " + locusId + " termId " + termId + " rank " + rank + " accession " + accession);
        }
        return featureDao.findFeatureByAccession(locusId, termId, rank, accession);
    }

    @Override
    public Feature createFeature(final String locus,
                                 final String term,
                                 final int rank,
                                 final String sequence) {

        checkNotNull(locus, "locus must be given");
        checkNotNull(term, "term must be given");
        checkArgument(rank > 0, "rank must be at least 1");
        checkNotNull(sequence, "sequence must be given");

        long locusId = featureDao.findLocusId(locus);
        long termId = featureDao.findTermId(term);
        long sequenceId = featureDao.findSequenceId(sequence);

        if (logger.isTraceEnabled()) {
            logger.trace("finding feature by locusId " + locusId + " termId " + termId + " rank " + rank + " sequenceId " + sequenceId);
        }
        Feature f = featureDao.findFeatureBySequenceId(locusId, termId, rank, sequenceId);
        if (f != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("found feature by locusId " + locusId + " termId " + termId + " rank " + rank + " sequenceId " + sequenceId);
            }
            return f;
        }

        // start transaction
        featureDao.begin();

        if (sequenceId < 1L) {
            sequenceId = insertSequence(sequence);
        }
        long accession = featureDao.nextAccession(locusId, termId, rank);

        if (logger.isTraceEnabled()) {
            logger.trace("next accession locusId " + locusId + " termId " + termId + " rank " + rank);
        }
        long featureId = insertFeature(locusId, termId, rank, accession, sequenceId);

        if (logger.isTraceEnabled()) {
            logger.trace("finding feature by featureId " + featureId);
        }
        f = featureDao.findFeatureByFeatureId(featureId);

        // end transaction
        featureDao.commit();

        return f;
    }

    long insertSequence(final String sequence) {
        featureDao.insertSequence(sequence);
        long sequenceId = featureDao.lastInsertId();

        if (logger.isTraceEnabled()) {
            logger.trace("inserted sequence " + sequence + " for sequence id " + sequenceId);
        }
        return sequenceId;
    }

    long insertFeature(final long locusId, final long termId, final int rank, final long accession, final long sequenceId) {
        featureDao.insertFeature(locusId, termId, rank, accession, sequenceId);
        long featureId = featureDao.lastInsertId();

        if (logger.isTraceEnabled()) {
            logger.trace("inserted feature accession " + accession + " for feature id " + featureId);
        }
        return featureId;
    }

    @Override
    public List<Feature> listFeatures(final String locus) {
        checkNotNull(locus, "locus must be given");
        long locusId = featureDao.findLocusId(locus);
        return featureDao.listFeaturesByLocus(locusId);
    }

    @Override
    public List<Feature> listFeatures(final String locus,
                                      final String term) {
        checkNotNull(locus, "locus must be given");
        checkNotNull(term, "term must be given");
        // todo: join in sql query
        long locusId = featureDao.findLocusId(locus);
        long termId = featureDao.findTermId(term);
        return featureDao.listFeaturesByLocusAndTerm(locusId, termId);
    }

    @Override
    public List<Feature> listFeatures(final String locus,
                                      final String term,
                                      final int rank) {
        checkNotNull(locus, "locus must be given");
        checkNotNull(term, "term must be given");
        checkArgument(rank > 0, "rank must be at least 1");
        long locusId = featureDao.findLocusId(locus);
        long termId = featureDao.findTermId(term);
        return featureDao.listFeaturesByLocusTermAndRank(locusId, termId, rank);
    }
}
