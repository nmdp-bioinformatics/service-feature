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

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import org.nmdp.service.feature.Feature;

/**
 * Feature DAO.
 */
public interface FeatureDao extends Transactional<FeatureDao> {

    @SqlQuery("select locus_id from locus where name = :locus or uri = :locus")
    long findLocusId(@Bind("locus") String locus);

    @SqlQuery("select term_id from term where name = :term or accession = :term or uri = :term")
    long findTermId(@Bind("term") String term);

    @SqlQuery("select sequence_id from sequence where sequence = :sequence")
    long findSequenceId(@Bind("sequence") String sequence);

    @Mapper(FeatureMapper.class)
    @SqlQuery("select l.name as locus, t.name as term, f.rank, f.accession, s.sequence as sequence from feature f, locus l, term t, sequence s where f.locus_id = l.locus_id and f.term_id = t.term_id and f.sequence_id = s.sequence_id and f.feature_id = :featureId")
    Feature findFeatureByFeatureId(@Bind("featureId") long featureId);

    @Mapper(FeatureMapper.class)
    @SqlQuery("select l.name as locus, t.name as term, f.rank, f.accession, s.sequence as sequence from feature f, locus l, term t, sequence s where f.locus_id = l.locus_id and f.term_id = t.term_id and f.sequence_id = s.sequence_id and f.locus_id = :locusId and f.term_id = :termId and f.rank = :rank and f.accession = :accession")
    Feature findFeatureByAccession(@Bind("locusId") long locusId, @Bind("termId") long termId, @Bind("rank") int rank, @Bind("accession") long accession);

    @Mapper(FeatureMapper.class)
    @SqlQuery("select l.name as locus, t.name as term, f.rank, f.accession, s.sequence as sequence from feature f, locus l, term t, sequence s where f.locus_id = l.locus_id and f.term_id = t.term_id and f.sequence_id = s.sequence_id and f.locus_id = :locusId and f.term_id = :termId and f.rank = :rank and f.sequence_id = :sequenceId")
    Feature findFeatureBySequenceId(@Bind("locusId") long locusId, @Bind("termId") long termId, @Bind("rank") int rank, @Bind("sequenceId") long sequenceId);

    @SqlQuery("select ifnull(max(accession),0)+1 from feature where locus_id = :locusId and term_id = :termId and rank = :rank and sequence_id = :sequenceId")
    long nextAccession(@Bind("locusId") long locusId, @Bind("termId") long termId, @Bind("rank") int rank, @Bind("sequenceId") long sequenceId);

    @SqlUpdate("insert into sequence (sequence) values (:sequence)")
    long insertSequence(@Bind("sequence") String sequence);

    @SqlUpdate("insert into feature (locus_id, term_id, rank, accession, sequence_id) values (:locusId, :termId, :rank, :accession, :sequenceId)")
    long insertFeature(@Bind("locusId") long locusId, @Bind("termId") long termId, @Bind("rank") int rank, @Bind("accession") long accession, @Bind("sequenceId") long sequenceId);

    @SqlQuery("select last_insert_id()")
    long lastInsertId();
}
