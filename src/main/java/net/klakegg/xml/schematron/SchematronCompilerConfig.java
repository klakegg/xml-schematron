package net.klakegg.xml.schematron;

import net.klakegg.commons.builder.Property;
import net.klakegg.xml.schematron.api.ResourceFolder;

/**
 * @author erlend
 */
interface SchematronCompilerConfig extends SaxonProperties {

    Property<String> STEP1_FILENAME = Property.create("iso_dsdl_include.xsl");

    Property<Boolean> STEP1_PERFORM = Property.create(true);

    Property<String> STEP2_FILENAME = Property.create("iso_abstract_expand.xsl");

    Property<Boolean> STEP2_PERFORM = Property.create(true);

    Property<String> STEP3_FILENAME = Property.create("iso_svrl_for_xslt2.xsl");

    Property<Boolean> STEP3_PERFORM = Property.create(true);

    Property<ResourceFolder> RESOURCE_FOLDER = Property.create(ResourceFolder.of("/xml/schematron/"));

}
