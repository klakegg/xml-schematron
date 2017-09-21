package net.klakegg.xml.schematron;

import net.klakegg.commons.builder.Property;
import net.sf.saxon.s9api.Processor;

/**
 * @author erlend
 */
interface SaxonProperties {

    Property<Processor> PROCESSOR = Property.create(new Processor(false));

}
