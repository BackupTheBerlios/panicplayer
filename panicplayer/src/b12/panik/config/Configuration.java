// ----------------------------------------------------------------------------
// [b12] Java Source File: Configuration.java
//                created: 26.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.util.Iterator;
import java.util.Properties;

/**
 * Used to configure the player with all its properties. This class provides
 * public accessors to override default properties and may load its setting
 * from an XML file.
 */
public class Configuration {

    Properties inputProps, outputProps, effectProps;

    public Configuration(String uri) throws ConfigurationException {
        ParsedObject po = null;
        try {
            po = ParsedObject.loadFromURI(uri);
        } catch (Exception e) {
            throw new ConfigurationException("Error in Configuration: ", e);
        }

        for (Iterator i = po.getChildren().iterator(); i.hasNext(); ) {
            ParsedObject child = (ParsedObject) i.next();
            String name = child.getName();
            if (name.equals("input")) {
                inputProps = parseProps(child);
            } else if (name.equals("output")) {
                outputProps = parseProps(child);
            } else if (name.equals("effect")) {
                effectProps = parseProps(child);
            }
        }
    }

    Properties parseProps(ParsedObject po) {
        Properties props = new Properties();
        props.putAll(po.getAttributes());
        System.out.println("created props for " + po.getName());
        System.out.println(props);
        return props;
    }

    /*
     * simple getters and setters 
     */
    
    /**
	 * Returns the effect properties.
	 * 
	 * @return the effect properties.
	 */
    public Properties getEffectProps() {
        return effectProps;
    }
    /**
	 * Sets the effect's properties.
	 * 
	 * @param effectProps
	 *            The effect's properties.
	 */
    public void setEffectProps(Properties effectProps) {
        this.effectProps = effectProps;
    }
    /**
	 * Returns the input properties.
	 * 
	 * @return the input properties.
	 */
    public Properties getInputProps() {
        return inputProps;
    }
    /**
	 * Sets the input properties.
	 * 
	 * @param inputProps
	 *            The input properties.
	 */
    public void setInputProps(Properties inputProps) {
        this.inputProps = inputProps;
    }
    /**
	 * Returns the output properties.
	 * 
	 * @return the output properties.
	 */
    public Properties getOutputProps() {
        return outputProps;
    }
    /**
	 * Sets the output properties.
	 * 
	 * @param outputProps
	 *            The output properties.
	 */
    public void setOutputProps(Properties outputProps) {
        this.outputProps = outputProps;
    }
}