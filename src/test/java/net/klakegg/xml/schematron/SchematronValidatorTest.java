package net.klakegg.xml.schematron;

import org.junit.Test;

/**
 * @author erlend
 */
public class SchematronValidatorTest {

    @Test
    public void simple() throws Exception {
        SchematronValidator schematronValidator = SchematronValidator.builder()
                .set(SchematronCompiler.STEP1_PERFORM, false)
                .set(SchematronCompiler.STEP2_PERFORM, false)
                .build();

        schematronValidator.validate(
                "src/test/resources/PAYMENT-MD.sch",
                "src/test/resources/Metadata.xml",
                System.out);
    }
}
