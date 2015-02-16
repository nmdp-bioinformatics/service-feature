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

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.resource.FeatureMixIn;

import retrofit.RestAdapter;

import retrofit.converter.JacksonConverter;

/**
 * Feature service module.
 */
@Immutable
public final class FeatureServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // empty
    }

    @Provides @Singleton
    static FeatureService createFeatureService(@EndpointUrl final String endpointUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixInAnnotations(Feature.class, FeatureMixIn.class);

        return new RestAdapter.Builder()
            .setEndpoint(endpointUrl)
            .setConverter(new JacksonConverter(objectMapper))
            .build().create(FeatureService.class);
    }
}
