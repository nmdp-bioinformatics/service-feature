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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.service.FeatureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Feature resource.
 */
@Path("/features")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value="Features", description="Create and retrieve enumerated sequence features.")
@Immutable
public final class FeatureResource {
    private final FeatureService featureService;
    private static final Logger logger = LoggerFactory.getLogger(FeatureResource.class);

    @Inject
    public FeatureResource(final FeatureService featureService) {
        checkNotNull(featureService);
        this.featureService = featureService;
    }

    @GET
    @ApiOperation(value="Retrieve an enumerated sequence feature", response=Feature.class)
    public Feature getFeature(final @QueryParam("locus") @ApiParam("locus name or URI") String locus,
                              final @QueryParam("term") @ApiParam("Sequence Ontology (SO) term name, accession, or URI") String term,
                              final @QueryParam("rank") @ApiParam("feature rank, must be at least 1") int rank,
                              final @QueryParam("accession") @ApiParam("accession, must be at least 1") long accession) {

        if (logger.isTraceEnabled()) {
            logger.trace("getFeature locus " + locus + " term " + term + " rank " + rank + " accession " + accession);
        }

        // todo: returning null here sends HTTP 204 No Content which causes trouble for Retrofit
        return featureService.getFeature(locus, term, rank, accession);
    }

    @POST
    @ApiOperation(value="Create an enumerated sequence feature", response=Feature.class)
    @ApiImplicitParam(paramType="body", dataType="FeatureRequest")
    public Feature createFeature(final FeatureRequest featureRequest) {
        checkNotNull(featureRequest);

        if (logger.isTraceEnabled()) {
            logger.trace("createFeature locus " + featureRequest.getLocus() + " term " + featureRequest.getTerm() + " rank " + featureRequest.getRank() + " sequence " + featureRequest.getSequence());
        }
        return featureService.createFeature(featureRequest.getLocus(), featureRequest.getTerm(), featureRequest.getRank(), featureRequest.getSequence());
    }
}
