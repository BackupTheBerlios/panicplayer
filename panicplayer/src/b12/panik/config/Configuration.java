// ----------------------------------------------------------------------------
// [b12] Java Source File: Configuration.java
//                created: 26.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import b12.panik.util.Logging;

/**
 * Used to configure the player with all its properties. This class provides
 * public accessors to override default properties and may load its setting
 * from an XML file.
 */
public class Configuration {

    private static final String DEFAULT_CONF_FILE = Resources
            .getString("Configuration.default.file");

    private static final String TAG_ROOT = "panicplayer-conf"; //$NON-NLS-1$
    private static final String TAG_INPUT = "input"; //$NON-NLS-1$
    private static final String TAG_OUTPUT = "output"; //$NON-NLS-1$
    private static final String TAG_EFFECT = "effect"; //$NON-NLS-1$
    private static final String TAG_VOICE = "voice"; //$NON-NLS-1$

    InputProperty inputProperty;
    Properties effectProps;
    List effectInput;

    Properties outputProps;

    /**
     * Creates a new instance of <code>Configuration</code>.
     * @param uri the address.
     * @throws ConfigurationException if an error occured in the configuration.
     */
    public Configuration(String uri) throws ConfigurationException {
        effectInput = new ArrayList();
        effectProps = new Properties();
        if (uri == null) {
            // read from default configuration
            File f = new File(DEFAULT_CONF_FILE);
            if (f.exists()) {
                uri = f.toString();
            }
        }
        if (uri != null) {
            ParsedObject po = null;
            try {
                po = ParsedObject.loadFromURI(uri);
            } catch (Exception e) {
                throw new ConfigurationException(Resources.getString("Configuration.error"), e); //$NON-NLS-1$
            }
            
            for (Iterator i = po.getChildren().iterator(); i.hasNext(); ) {
                final ParsedObject child = (ParsedObject) i.next();
                final String name = child.getName();
                if (name.equals(TAG_INPUT)) {
                    parseInput(child);
                } else if (name.equals(TAG_OUTPUT)) {
                    outputProps = parseOutput(child);
                } else if (name.equals(TAG_EFFECT)) {
                    parseEffect(child);
                }
            }
        }
    }

    /** Parses the input properties. */
    private void parseInput(ParsedObject po) {
        try {
            inputProperty = new InputProperty(po);
        } catch (MalformedURLException e) {
            Logging.warning(Resources.getString("Configuration.error") + e.toString()); //$NON-NLS-1$
        }
    }

    /** Parses the effect properties */
    private void parseEffect(ParsedObject po) {
        effectProps.putAll(po.getAttributes());
        if (po.hasChildren()) {
            final List children = po.getChildren();
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                try {
                    final ParsedObject child = (ParsedObject) i.next();
                    if (child.getName().equals(TAG_VOICE)) {    
                        effectInput.add(new InputProperty(child));
                    }
                } catch (MalformedURLException e) {
                    Logging.warning(Resources.getString("Configuration.error") + e.toString()); //$NON-NLS-1$
                }
            }
        }
    }

    Properties parseOutput(ParsedObject po) {
        Properties props = new Properties();
        props.putAll(po.getAttributes());
        return props;
    }

    /**
     * Saves the configuration to the default configuration file.
     * @throws IOException on IO error.
     */
    public void save() throws IOException {
        writeTo(new File(DEFAULT_CONF_FILE));
    }
    
    /**
     * Writes the configuration to the given file.
     * @param f the file.
     * @throws IOException on IO error.
     */
    public void writeTo(File f) throws IOException {
        try {
            XmlParser.writeParsedObject(getParsedObject(), new FileWriter(f), true);
        } catch (ParserConfigurationException e) {
            throw new IOException(Resources.getString("Configuration.xmlerror") + e.getMessage()); //$NON-NLS-1$
        }
    }

    /** 
     * Creates a ParsedObject from the information in this
     * <code>Configuration</code>
     * 
     * @return a new <code>ParsedObject</code>.
     */
    private ParsedObject getParsedObject() throws ParserConfigurationException {
        ParsedObject root = new ParsedObject(TAG_ROOT);
        
        // fill input
        if (inputProperty != null) {
            inputProperty.fillParsedObject(root.getEmptyChild(TAG_INPUT));
        }
        
        // TODO output

        // fill effect
        // copy properties
        ParsedObject poEffect = root.getEmptyChild(TAG_EFFECT);
        for (Iterator i = effectProps.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            poEffect.addAttribute((String) entry.getKey(), (String) entry.getValue());
        }
        // add voices
        for (Iterator i = effectInput.iterator(); i.hasNext(); ) {
            InputProperty ip = (InputProperty) i.next();
            ip.fillParsedObject(poEffect.getEmptyChild(TAG_VOICE));
        }
        return root;
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

    public void addEffectProperty(InputProperty ip) {
        effectInput.add(ip);
    }

    /**
     * Returns the input properties.
     * 
     * @return the input properties.
     */
    public InputProperty getInputProperty() {
        return inputProperty;
    }

    public void setInputProperty(InputProperty ip) {
        inputProperty = ip;
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