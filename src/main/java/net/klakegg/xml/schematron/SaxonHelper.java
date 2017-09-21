package net.klakegg.xml.schematron;

import net.sf.saxon.s9api.XdmDestination;
import net.sf.saxon.s9api.XdmNode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author erlend
 */
interface SaxonHelper {

    static InputStream xdmToInputStream(XdmDestination xdmDestination) {
        return xdmToInputStream(xdmDestination.getXdmNode());
    }

    static InputStream xdmToInputStream(XdmNode xdmNode) {
        return new ByteArrayInputStream(xdmToBytes(xdmNode));
    }

    static byte[] xdmToBytes(XdmDestination xdmDestination) {
        return xdmToBytes(xdmDestination.getXdmNode());
    }

    static byte[] xdmToBytes(XdmNode xdmNode) {
        return xdmNode.toString().getBytes(StandardCharsets.UTF_8);
    }
}