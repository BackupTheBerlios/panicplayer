// ----------------------------------------------------------------------------
// [b12] Java Source File: ConfigurationTest.java
//                created: 26.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

/** Tests methods of {@linkplain Configuration}. */
public class ConfigurationTest extends TestCase {

    private static final String FILENAME = ConfigurationTest.class
            .getResource("example-config.xml").toString();
    private static final String FILENAME2 = ConfigurationTest.class
            .getResource("example-config1.xml").toString();

    public final void testConfiguration() {
        Configuration conf = null;
        try {
            conf = new Configuration(FILENAME);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }
        assertNotNull(conf.getEffectProps());
        assertNotNull(conf.getOutputProps());
        assertNotNull(conf.getInputProperty());
    }

    
    public final void testParsedObject()  {
        Configuration conf = null;
        try {
            conf = new Configuration(FILENAME2);
            File f = new File("output.xml");
            conf.writeTo(f);
        } catch (ConfigurationException e) {
            fail(e.toString());
        } catch (IOException e) {
            fail(e.toString());
        }
    }
}