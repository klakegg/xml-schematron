package net.klakegg.xml.schematron;

import net.klakegg.commons.builder.Builder;
import net.klakegg.commons.builder.Properties;
import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public class SchematronCompiler implements SchematronCompilerConfig {

    private final XsltCompiler xsltCompiler;

    private final Map<String, XsltExecutable> xsltExecutables = new HashMap<>();

    private final Properties properties;

    public static Builder<SchematronCompiler, RuntimeException> builder() {
        return Builder.of(SchematronCompiler::new);
    }

    protected SchematronCompiler(Properties properties) {
        this.properties = properties;

        xsltCompiler = properties.get(PROCESSOR).newXsltCompiler();
        xsltCompiler.setURIResolver(properties.get(RESOURCE_FOLDER));
    }

    private XsltExecutable getXsltExecutable(String file) throws SchematronException {
        if (!xsltExecutables.containsKey(file)) {
            try (InputStream inputStream = properties.get(RESOURCE_FOLDER).load(file)) {
                xsltExecutables.put(file, xsltCompiler.compile(new StreamSource(inputStream)));
            } catch (IOException | SaxonApiException e) {
                throw new SchematronException(String.format("Unable to load file '%s'.", file), e);
            }
        }

        return xsltExecutables.get(file);
    }

    public void compile(Path path, OutputStream outputStream) throws IOException, SchematronException {
        compile(path.toFile(), outputStream);
    }

    public void compile(File file, OutputStream outputStream) throws IOException, SchematronException {
        compile(file, properties.get(PROCESSOR).newSerializer(outputStream));
    }

    public void compile(Path path, Destination destination) throws SchematronException {
        compile(path.toFile(), destination);
    }

    public void compile(File file, Destination destination) throws SchematronException {
        try {
            XsltTransformerStack xsltTransformerStack = new XsltTransformerStack(destination);

            if (properties.get(STEP3_PERFORM))
                xsltTransformerStack.append(getXsltExecutable(properties.get(STEP3_FILENAME)));

            if (properties.get(STEP2_PERFORM))
                xsltTransformerStack.append(getXsltExecutable(properties.get(STEP2_FILENAME)));

            if (properties.get(STEP1_PERFORM))
                xsltTransformerStack.append(getXsltExecutable(properties.get(STEP1_FILENAME)));

            xsltTransformerStack.transform(new StreamSource(file));
        } catch (SaxonApiException e) {
            throw new SchematronException(e.getMessage(), e);
        }
    }
}
