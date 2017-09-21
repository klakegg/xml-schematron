package net.klakegg.xml.schematron;

import net.klakegg.commons.builder.Property;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author erlend
 */
interface SchematronValidatorConfig extends SaxonProperties {

    Property<Path> FOLDER = Property.create(Paths.get("."));

}
