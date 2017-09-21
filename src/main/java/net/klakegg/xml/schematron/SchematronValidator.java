package net.klakegg.xml.schematron;

import net.klakegg.commons.builder.Builder;
import net.klakegg.commons.builder.Properties;
import net.klakegg.xml.schematron.lang.SchematronException;
import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author erlend
 */
public class SchematronValidator implements SchematronValidatorConfig {

    private final XsltCompiler xsltCompiler;

    private final Map<String, XsltExecutable> xsltExecutables = new HashMap<>();

    private final SchematronCompiler schematronCompiler;

    private final Properties properties;

    public static Builder<SchematronValidator, RuntimeException> builder() {
        return Builder.of(SchematronValidator::new);
    }

    protected SchematronValidator(Properties properties) {
        this.properties = properties;

        schematronCompiler = new SchematronCompiler(properties);

        xsltCompiler = properties.get(PROCESSOR).newXsltCompiler();
    }

    public void validate(String schFile, String xmlFile, OutputStream outputStream)
            throws IOException, SchematronException {
        XdmDestination destination = new XdmDestination();

        validate(schFile, xmlFile, destination);

        outputStream.write(SaxonHelper.xdmToBytes(destination));
    }

    public void validate(String schFile, String xmlFile, Destination destination)
            throws IOException, SchematronException {
        try {
            XsltTransformerStack xsltTransformerStack = new XsltTransformerStack(destination);
            xsltTransformerStack.append(getXsltExecutable(schFile));
            xsltTransformerStack.transform(new StreamSource(properties.get(FOLDER).resolve(xmlFile).toFile()));
        } catch (SaxonApiException e) {
            throw new SchematronException(e.getMessage(), e);
        }
    }

    private XsltExecutable getXsltExecutable(String file) throws IOException, SchematronException {
        if (!xsltExecutables.containsKey(file)) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                schematronCompiler.compile(properties.get(FOLDER).resolve(file), byteArrayOutputStream);

                xsltExecutables.put(file, xsltCompiler.compile(new StreamSource(
                        new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))));
            } catch (SaxonApiException e) {
                throw new SchematronException(e.getMessage(), e);
            }
        }

        return xsltExecutables.get(file);
    }
}
