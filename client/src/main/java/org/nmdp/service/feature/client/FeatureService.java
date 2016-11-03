/*

    feature-client  Feature client.
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
package org.nmdp.service.feature.client;

import java.util.List;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.resource.FeatureRequest;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.POST;
import retrofit.http.Query;

import rx.Observable;

/**
 * Feature service client.
 */
public interface FeatureService {

    @GET("/features")
    Feature getFeature(@Query("locus") String locus,
                       @Query("term") String term,
                       @Query("rank") int rank,
                       @Query("accession") long accession);

    @POST("/features")
    Feature createFeature(@Body FeatureRequest featureRequest);

    @POST("/features")
    Observable<Feature> createFeatureObservable(@Body FeatureRequest featureRequest);

    @GET("/features/{locus}")
    List<Feature> listFeatures(@Path("locus") String locus);

    @GET("/features/{locus}/{term}")
    List<Feature> listFeatures(@Path("locus") String locus,
                               @Path("term") String term);

    @GET("/features/{locus}/{term}/{rank}")
    List<Feature> listFeatures(@Path("locus") String locus,
                               @Path("term") String term,
                               @Path("rank") int rank);
}
