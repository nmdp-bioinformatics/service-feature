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

import static org.dishevelled.compress.Readers.reader;
import static org.dishevelled.compress.Writers.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
import org.dishevelled.commandline.argument.StringArgument;

import org.nmdp.service.feature.Feature;

import org.nmdp.service.feature.client.EndpointUrl;
import org.nmdp.service.feature.client.FeatureService;
import org.nmdp.service.feature.client.FeatureServiceModule;

import org.nmdp.service.feature.resource.FeatureRequest;

/**
 * Feature tools.
 */
public final class FeatureTools implements Callable<Integer> {
    private final File inputFile;
    private final File outputFile;
    private final FeatureService featureService;
    static final String DEFAULT_ENDPOINT_URL = "http://localhost:8080/";
    static final String USAGE = "feature-tools -u " + DEFAULT_ENDPOINT_URL + " -i requests.txt -o features.txt";

    public FeatureTools(final String endpointUrl, final File inputFile, final File outputFile) {
        checkNotNull(endpointUrl);

        Injector injector = Guice.createInjector(new FeatureServiceModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(String.class).annotatedWith(EndpointUrl.class).toInstance(endpointUrl);
                }
            });

        this.inputFile = inputFile;
        this.outputFile = outputFile;
        featureService = injector.getInstance(FeatureService.class);
    }

    @Override
    public Integer call() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = reader(inputFile);
            writer = writer(outputFile);

            int lineNumber = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] tokens = line.split("\t");
                if (tokens.length < 4) {
                    throw new IOException("invalid input file format at line " + lineNumber + ", expected at least 4 tab-separated columns found " + tokens.length);
                }
                // fourth column can be either accession or sequence
                if (tokens[3].matches("^[0-9]+")) {
                    // locus, term, rank, accession
                    writeFeature(featureService.getFeature(tokens[0], tokens[1], Integer.parseInt(tokens[2]), Long.parseLong(tokens[3])), writer);
                }
                else {
                    // locus, term, rank, sequence
                    writeFeature(featureService.createFeature(new FeatureRequest(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3])), writer);
                }
                lineNumber++;
            }

            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
            try {
                writer.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }

    static void writeFeature(final Feature feature, final PrintWriter writer) {
        if (feature != null) {
            writer.println(Joiner.on("\t").join(feature.getLocus(), feature.getTerm(), feature.getRank(), feature.getAccession(), feature.getSequence()));
        }
    }

    public static void main(final String args[]) {
        Switch about = new Switch("a", "about", "display about message");
        Switch help = new Switch("h", "help", "display help message");
        StringArgument endpointUrl = new StringArgument("u", "endpoint-url", "endpoint URL, default " + DEFAULT_ENDPOINT_URL, false);
        FileArgument inputFile = new FileArgument("i", "input-file", "input file of tab-delimited feature requests, default stdin", false);
        FileArgument outputFile = new FileArgument("o", "output-file", "output file of tab-delimited features, default stdout", false);

        ArgumentList arguments = new ArgumentList(about, help, endpointUrl, inputFile, outputFile);
        CommandLine commandLine = new CommandLine(args);

        FeatureTools featureTools = null;
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

            featureTools = new FeatureTools(endpointUrl.getValue(DEFAULT_ENDPOINT_URL), inputFile.getValue(), outputFile.getValue());
        }
        catch (CommandLineParseException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
        try {
            System.exit(featureTools.call());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
