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

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Feature request.
 */
@ApiModel("Provide locus, term, rank, and sequence to create an enumerated sequence feature.")
@Immutable
public final class FeatureRequest {
    private final String locus;
    private final String term;
    private final int rank;
    private final String sequence;

    @JsonCreator
    public FeatureRequest(final @JsonProperty("locus") String locus,
                          final @JsonProperty("term") String term,
                          final @JsonProperty("rank") int rank,
                          final @JsonProperty("sequence") String sequence) {

        this.locus = locus;
        this.term = term;
        this.rank = rank;
        this.sequence = sequence;
    }


    @ApiModelProperty(value="locus name or URI", required=true)
    public String getLocus() {
        return locus;
    }

    @ApiModelProperty(value="Sequence Ontology (SO) term name, accession, or URI", required=true)
    public String getTerm() {
        return term;
    }

    @ApiModelProperty(value="feature rank, must be at least 1", required=true)
    public int getRank() {
        return rank;
    }

    @ApiModelProperty("feature sequence, in DNA alphabet")
    public String getSequence() {
        return sequence;
    }
}
