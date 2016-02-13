/*

    feature-dropwizard  Feature dropwizard.
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
package org.nmdp.service.feature.dropwizard;

import com.codahale.metrics.health.HealthCheck;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.SerializationFeature;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.wordnik.swagger.config.SwaggerConfig;

import com.wordnik.swagger.model.ApiInfo;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.nmdp.service.common.dropwizard.CommonServiceApplication;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.resource.UserInputExceptionMapper;
import org.nmdp.service.feature.resource.ExceptionMapperModule;
import org.nmdp.service.feature.service.impl.FeatureServiceModule;

import org.nmdp.service.feature.resource.FeatureMixIn;
import org.nmdp.service.feature.resource.FeatureResource;

/**
 * Feature application.
 */
@Immutable
public final class FeatureApplication extends CommonServiceApplication<FeatureConfiguration> {

    @Override
    public String getName() {
        return "features";
    }

    @Override
    public void initializeService(final Bootstrap<FeatureConfiguration> bootstrap) {
        // empty
    }

    @Override
    public void runService(final FeatureConfiguration configuration, final Environment environment) throws Exception {
        Injector injector = Guice.createInjector(new FeatureServiceModule(), new ExceptionMapperModule());

        environment.healthChecks().register("feature", new HealthCheck() {
                @Override
                protected Result check() throws Exception {
                    return Result.healthy();
                }
            });

        environment.jersey().register(injector.getInstance(FeatureResource.class));
        environment.jersey().register(injector.getInstance(UserInputExceptionMapper.class));

        environment.getObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .addMixInAnnotations(Feature.class, FeatureMixIn.class);
    }

    @Override
    public void configureSwagger(final SwaggerConfig config) {
        config.setApiVersion("1.0");
        config.setApiInfo(new ApiInfo("Feature service",
                                      "Enumerated sequence feature service.",
                                      null,
                                      null,
                                      "GNU Lesser General Public License (LGPL), version 3 or later",
                                      "http://www.gnu.org/licenses/lgpl.html"));
    }


    /**
     * Main.
     *
     * @param args command line arguments
     * @throws Exception if an error occurs
     */
    public static void main(final String[] args) throws Exception {
        new FeatureApplication().run(args);
    }
}
