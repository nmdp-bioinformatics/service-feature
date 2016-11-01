/*

    feature-tools  Feature tools.
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
package org.nmdp.service.feature.tools;

import static com.google.common.base.Preconditions.checkNotNull;

import static org.dishevelled.compress.Writers.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import java.util.concurrent.Callable;

import com.google.common.base.Joiner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;

import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.IntegerArgument;
import org.dishevelled.commandline.argument.StringArgument;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.client.EndpointUrl;
import org.nmdp.service.feature.client.FeatureService;
import org.nmdp.service.feature.client.FeatureServiceModule;

import org.nmdp.service.feature.resource.FeatureRequest;

/**
 * Export features as FASTA formatted sequences.
 */
public final class FeatureExportFasta implements Callable<Integer> {
    private final String locus;
    private final String term;
    private final Integer rank;
    private final File outputFile;
    private final FeatureService featureService;
    static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    static final String USAGE = "feature-export-fasta -u " + DEFAULT_ENDPOINT_URL + " -l HLA-A -t exon -r 1 -o feature-sequences.fa.gz";

    public FeatureExportFasta(final String endpointUrl, final String locus, final String term, final Integer rank, final File outputFile) {
        checkNotNull(endpointUrl);
        checkNotNull(locus);

        Injector injector = Guice.createInjector(new FeatureServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(EndpointUrl.class).toInstance(endpointUrl);
                }
            });

        this.locus = locus;
        this.term = term;
        this.rank = rank;
        this.outputFile = outputFile;
        featureService = injector.getInstance(FeatureService.class);
    }

    @Override
    public Integer call() {
        PrintWriter writer = null;
        List<Feature> features = null;
        try {
            writer = writer(outputFile);

            if (term == null && rank == null) {
                features = featureService.listFeatures(locus);
            }
            else if (rank == null) {
                features = featureService.listFeatures(locus, term);
            }
            else {
                features = featureService.listFeatures(locus, term, rank);
            }

            if (features == null) {
                throw new RuntimeException("could not list features");
            }

            for (Feature feature : features) {
                String header = ">" + Joiner.on("|").join(feature.getLocus(), feature.getTerm(), feature.getRank(), feature.getAccession());
                writer.println(header);
                writer.println(feature.getSequence());
            }
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }

    public static void main(final String args[]) {
        Switch about = new Switch("a", "about", "display about message");
        Switch help = new Switch("h", "help", "display help message");
        StringArgument endpointUrl = new StringArgument("u", "endpoint-url", "endpoint URL, default " + DEFAULT_ENDPOINT_URL, false);
        StringArgument locus = new StringArgument("l", "locus", "locus name or URI", true);
        StringArgument term = new StringArgument("t", "term", "Sequence Ontology (SO) term name, accession, or URI", false);
        IntegerArgument rank = new IntegerArgument("r", "rank", "feature rank, if specified must be at least 1", false);
        FileArgument outputFile = new FileArgument("o", "output-file", "output file of tab-delimited features, default stdout", false);

        ArgumentList arguments = new ArgumentList(about, help, endpointUrl, locus, term, rank, outputFile);
        CommandLine commandLine = new CommandLine(args);

        FeatureExportFasta exportFasta = null;
        try
        {
            CommandLineParser.parse(commandLine, arguments);
            if (about.wasFound()) {
                About.about(System.out);
                System.exit(0);
            }
            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
                System.exit(0);
            }

            exportFasta = new FeatureExportFasta(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), locus.getValue(), term.getValue(), rank.getValue(), outputFile.getValue());
        }
        catch (CommandLineParseException e) {
            if (about.wasFound()) {
                About.about(System.out);
                System.exit(0);
            }
            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
                System.exit(0);
            }
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        try {
            System.exit(exportFasta.call());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
