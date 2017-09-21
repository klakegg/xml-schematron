package net.klakegg.xml.schematron;

import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erlend
 */
class XsltTransformerStack {

    private final List<XsltTransformer> xsltTransformers = new ArrayList<>();

    private Destination destination;

    private XsltTransformer current;

    public XsltTransformerStack(Destination destination) {
        this.destination = destination;
    }

    public void append(XsltExecutable xsltExecutable) {
        XsltTransformer xsltTransformer = xsltExecutable.load();
        xsltTransformer.setDestination(destination);

        xsltTransformers.add(xsltTransformer);
        destination = xsltTransformer;
        current = xsltTransformer;
    }

    public void transform(Source source) throws SaxonApiException {
        try {
            current.setSource(source);
            current.transform();
        } finally {
            for (XsltTransformer xsltTransformer : xsltTransformers)
                xsltTransformer.close();
            xsltTransformers.clear();
        }
    }
}
