/*

    feature-dropwizard-jdbi  Feature dropwizard JDBI.
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
package org.nmdp.service.feature.dropwizard.jdbi;

import javax.annotation.concurrent.Immutable;

import com.fasterxml.jackson.databind.SerializationFeature;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.wordnik.swagger.config.SwaggerConfig;

import com.wordnik.swagger.model.ApiInfo;

import io.dropwizard.Application;

import io.dropwizard.jdbi.DBIFactory;

import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.nmdp.service.common.dropwizard.CommonServiceApplication;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.service.jdbi.FeatureDao;
import org.nmdp.service.feature.service.jdbi.JdbiFeatureServiceModule;

import org.nmdp.service.feature.resource.FeatureMixIn;
import org.nmdp.service.feature.resource.FeatureResource;

import org.skife.jdbi.v2.DBI;

/**
 * Feature JDBI application.
 */
@Immutable
public final class FeatureJdbiApplication extends CommonServiceApplication<FeatureJdbiConfiguration> {

    @Override
    public String getName() {
        return "features";
    }

    @Override
    public void initializeService(final Bootstrap<FeatureJdbiConfiguration> bootstrap) {
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void runService(final FeatureJdbiConfiguration configuration, final Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final FeatureDao featureDao = jdbi.onDemand(FeatureDao.class);

        Injector injector = Guice.createInjector(new JdbiFeatureServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(FeatureDao.class).toInstance(featureDao);
                }
            });

        environment.jersey().register(injector.getInstance(FeatureResource.class));

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
        new FeatureJdbiApplication().run(args);
    }
}
