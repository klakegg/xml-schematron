package net.klakegg.xml.schematron;

import org.junit.Test;

import java.io.File;

/**
 * @author erlend
 */
public class SchematronCompilerTest {

    @Test
    public void simple() throws Exception {
        File file = new File(getClass().getResource("/PAYMENT-MD.sch").toURI());

        SchematronCompiler schematronCompiler = SchematronCompiler.builder()
                .set(SchematronCompiler.STEP3_PERFORM, false)
                .build();
        schematronCompiler.compile(file, System.out);
    }
}
