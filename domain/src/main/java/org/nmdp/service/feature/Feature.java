/*

    feature-domain  Feature domain.
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
package org.nmdp.service.feature;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.annotation.concurrent.Immutable;

/**
 * Feature.
 */
@Immutable
public final class Feature {
    private final String locus;
    private final String term;
    private final int rank;
    private final long accession;
    private final String sequence;
    private final int hashCode;

    public Feature(final String locus,
                   final String term,
                   final int rank,
                   final long accession,
                   final String sequence) {

        checkNotNull(locus);
        checkNotNull(term);
        checkArgument(rank > 0, "rank must be at least 1");
        checkArgument(accession > 0L, "accession must be at least 1L");
        checkNotNull(sequence);

        this.locus = locus;
        this.term = term;
        this.rank = rank;
        this.accession = accession;
        this.sequence = sequence;
        hashCode = Objects.hash(locus, term, rank, accession, sequence);
    }


    public String getLocus() {
        return locus;
    }

    public String getTerm() {
        return term;
    }

    public int getRank() {
        return rank;
    }

    public long getAccession() {
        return accession;
    }

    public String getSequence() {
        return sequence;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Feature)) {
            return false;
        }
        Feature f = (Feature) o;
        return Objects.equals(locus, f.getLocus())
            && Objects.equals(term, f.getTerm())
            && Objects.equals(rank, f.getRank())
            && Objects.equals(accession, f.getAccession())
            && Objects.equals(sequence, f.getSequence());
    }
}
