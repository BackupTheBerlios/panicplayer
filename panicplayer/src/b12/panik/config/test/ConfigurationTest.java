// ----------------------------------------------------------------------------
// [b12] Java Source File: ConfigurationTest.java
//                created: 26.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config.test;

import junit.framework.TestCase;
import b12.panik.config.Configuration;
import b12.panik.config.ConfigurationException;

/** Tests methods of {@linkplain Configuration}. */
public class ConfigurationTest extends TestCase {

    private static final String FILENAME = ConfigurationTest.class
            .getResource("example-config.xml").toString();

    public final void testConfiguration() {
        Configuration conf = null;
        try {
            conf = new Configuration(FILENAME);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }
        assertNotNull(conf.getEffectProps());
        assertNotNull(conf.getOutputProps());
        assertNotNull(conf.getInputProps());
    }

}