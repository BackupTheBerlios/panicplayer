// ----------------------------------------------------------------------------
// [b12] Java Source File: Configuration.java
//                created: 26.10.2003
//              $Revision: 1.12 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import b12.panik.io.MediaIOException;
import b12.panik.io.UrlTrack;
import b12.panik.player.PanicAudioPlayer;
import b12.panik.util.ConstraintsException;
import b12.panik.util.Logging;

/**
 * <p>Used to configure the player with all its properties. This class provides
 * public accessors to override default properties and may load its setting
 * from an XML file.</p>
 * 
 * <p>The configuration is a singular object.</p>
 */
public class Configuration {

    private static final String DEFAULT_CONF_FILE = Resources
            .getString("Configuration.default.file");

    private static final String TAG_ROOT = "panicplayer-conf"; //$NON-NLS-1$
    private static final String TAG_INPUT = "input"; //$NON-NLS-1$
    private static final String TAG_OUTPUT = "output"; //$NON-NLS-1$
    private static final String TAG_EFFECT = "effect"; //$NON-NLS-1$

    private static Configuration instance;

    InputProperty inputProperty;
    Properties outputProps;
    private PanicAudioPlayer player;

    private boolean mainTrackLoaded;
    private final EffectConfiguration effectConfig;

    private Configuration() {
        effectConfig = new EffectConfiguration();
    }
    
    /**
     * Resets the configuration to an initial state. 
     */
    public void resetConfig() {
        player.reset();
        effectConfig.reset();
    }
    

    /**
     * Creates a new instance of <code>Configuration</code>.
     *
     * @param uri the address.
     * @throws ConfigurationException if an error occured in the configuration.
     */
    Configuration(String uri) throws ConfigurationException {
        this();
        if (uri != null) {
            loadConfig(uri);
        }
    }

    /**
     * Returns the current configuration.
     * @return the current configuration.
     */
    public static Configuration getConfiguration() {
        if (instance == null) {
            File f = new File(DEFAULT_CONF_FILE);

            try {
                String uri = f.exists() ? f.toString() : null;
                instance = new Configuration(uri);
            } catch (ConfigurationException e) {
                Logging.severe(
                        "Configuration could not be loaded, switching to empty configuration.",
                        e);
                instance = new Configuration();
            }
        }
        return instance;
    }

    private void loadConfig(String uri) throws ConfigurationException {
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
                effectConfig.load(child);
            }
        }
    }

    /** Parses the input properties. */
    private void parseInput(ParsedObject po) {
        try {
            inputProperty = InputProperty.parseInputProperty(po);
        } catch (MalformedURLException e) {
            Logging.warning(Resources.getString("Configuration.error"), e); //$NON-NLS-1$
        }
    }

    Properties parseOutput(ParsedObject po) {
        Properties props = new Properties();
        props.putAll(po.getAttributes());
        return props;
    }

    /**
     * Saves the configuration to the default configuration file.
     *
     * @throws IOException on IO error.
     */
    public void save() throws IOException {
        writeTo(new File(DEFAULT_CONF_FILE));
    }

    /**
     * Writes the configuration to the given file.
     *
     * @param f the file.
     * @throws IOException on IO error.
     */
    public void writeTo(File f) throws IOException {
        try {
            FileWriter writer = new FileWriter(f);
            XmlParser.writeParsedObject(getParsedObject(), writer, true);
            writer.close();
        } catch (ParserConfigurationException e) {
            throw new IOException(Resources.getString("Configuration.xmlerror")
                    + e.getMessage()); //$NON-NLS-1$
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
        ParsedObject poEffect = root.getEmptyChild(TAG_EFFECT);
        effectConfig.saveTo(poEffect);

        return root;
    }

    /*
     * 
     * simple getters and setters
     * 
     */

    /**
     * Returns the effect configuration.
     *
     * @return the effect properties.
     */
    public EffectConfiguration getEffectConf() {
        return effectConfig;
    }

    /**
     * Returns the input properties.
     *
     * @return the input properties.
     */
    public InputProperty getInputProperty() {
        return inputProperty;
    }

    /**
     * Sets the input property.
     *
     * @param ip the new input property.
     */
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
     * @param outputProps The output properties.
     */
    public void setOutputProps(Properties outputProps) {
        this.outputProps = outputProps;
    }

    /**
     * Loads a sound file.
     * @param f the file to be loaded.
     * @throws MediaIOException if an error occured while loading the sound
     *          file. See {@linkplain #loadMainTrack(URL)} for
     *          more information.
     */
    public void loadMainSoundFile(File f) throws MediaIOException {
        URL url;
        try {
            url = f.toURL();
            loadMainTrack(url);
        } catch (IOException e) {
            throw new MediaIOException(e);
        }
    }

    /**
     * Returns whether the main track is already available.
     * @return <code>true</code> if a main track is available,
     *          <code>false</code> otherwise.
     */
    public boolean isMainTrackLoaded() {
        return mainTrackLoaded;
    }

    /**
     * Loads the sound with the given URL into the configuration.
     * @param url the url.
     * @throws MediaIOException if an error occurred while creating the
     *          player.
     */
    public void loadMainTrack(URL url) throws MediaIOException {
        setInputProperty(new InputProperty(new File(url.toString()), 0));
        player.setMainTrack(url);
        mainTrackLoaded = true;
    }

    /**
     * Adds a single track to the configuration.
     * @param uri the uri to load.
     * @return the created track.
     * @throws MediaIOException if an IO error occurs, or the URL could not
     *          be correctly converted.
     */
    public UrlTrack addTrack(URI uri) throws MediaIOException {
        try {
            UrlTrack track = player.addTrack(uri);
            effectConfig.addTrack(uri, 0);
            // effect config should update track behaviour
            //track.setTrackListener(effectConfig);
            return track;
        } catch (IOException e) {
            throw new MediaIOException(e);
        } catch (ConstraintsException e) {
            throw new MediaIOException(e);
        }
    }

    /**
     * Sets the player for this configuration.
     * @param player the player.
     */
    public void setPlayer(PanicAudioPlayer player) {
        this.player = player;
    }
}