// ----------------------------------------------------------------------------
// [b12] Java Source File: Configuration.java
//                created: 26.10.2003
//              $Revision: 1.15 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import b12.panik.io.MediaIOException;
import b12.panik.io.UrlTrack;
import b12.panik.player.PanicAudioPlayer;
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
    private static String currentConfFile;

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
     * Returns the configuration file.
     * @return Returns the current configuration file.
     */
    public static String getCurrentConfFile() {
        return currentConfFile;
    }

    /**
     * Sets the current configuration file.
     * @param currentConfFile The current configuration file to set.
     */
    public static void setCurrentConfFile(String currentConfFile) {
        Configuration.currentConfFile = currentConfFile;
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
     * Returns the current configuration. If the configuration has not been
     * initialized yet, a new configuration will be created.
     * 
     * @param p the player to be associated with the configuration.
     * 
     * @return the current configuration.
     */
    public static Configuration getConfiguration(PanicAudioPlayer p) {
        if (instance == null) {
            instance = new Configuration();
            try {
                instance.loadConfig(getConfFile());
            } catch (ConfigurationException e) {
                Logging.warning("Configuration could not be loaded, "
                        + "switching to empty configuration.");
            }

            if (p != null) {
                try {
                    instance.setPlayer(p);
                } catch (MediaIOException e) {
                    Logging.severe("MediaIOException - Player properties could not be set.", e);
                } catch (IOException e) {
                    Logging.severe("IOException - Player properties could not be set.", e);
                }
            }
        }
        return instance;
    }

    /**
     * Returns the current configuration.
     * @return the current configuration.
     */
    public static Configuration getConfiguration() {
        return getConfiguration(null);
    }

    /**
     * Returns the current configuration file.
     * @return the current configuration file or <code>null</code>, if no such
     *          file exists and the default configuration file is not existant.
     */
    private static File getConfFile() {
        File confFile;
        if (currentConfFile != null) {
            confFile = new File(currentConfFile);
            if (!confFile.exists()) {
                confFile = new File(DEFAULT_CONF_FILE);
            }
        } else {
            confFile = new File(DEFAULT_CONF_FILE);
        }
        if (confFile.exists()) {
            return confFile;
        }
        return null;
    }

    private void loadConfig(File f) throws ConfigurationException {
        if (f == null || !f.exists()) {
            throw new ConfigurationException("No configuration found at " + f);
        }

        String uri = f.toURI().toString();
        try {
            instance.loadConfig(uri);
        } catch (ConfigurationException e) {
            throw new ConfigurationException("Configuration at " + uri
                    + " could not be loaded.", e);
        }
    }

    /**
     * Loads the configuration with the information found in the given uri.
     * @param uri the location of the new information.
     * @throws ConfigurationException if an error occurred while trying to
     *          read the target.
     */
    public void loadConfig(String uri) throws ConfigurationException {
        Logging.info("Loading configuration from " + uri);
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
            File f = inputProperty.getFile();
            if (f != null) {
                loadMainTrack(f);
            } else {
                String urlString = inputProperty.getAddress().toString();
                loadMainTrack(urlString);
            }
        } catch (MalformedURLException e) {
            Logging.warning(Resources.getString("Configuration.error"), e); //$NON-NLS-1$
        } catch (MediaIOException e) {
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
     * @see #writeTo(Writer)
     */
    public void writeTo(File f) throws IOException {
        FileWriter writer = new FileWriter(f);
        Logging.info("Writing configuration to " + f);
        writeTo(writer);
    }

    /**
     * Writes the configuration to the given writer.
     * @param w the writer.
     * @throws IOException
     */
    public void writeTo(Writer w) throws IOException {
        try {
            XmlParser.writeParsedObject(getParsedObject(), w, true);
            w.close();
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
        effectConfig.clearTracks();
        if (player != null) {
            effectConfig.load(player.getEffectTracks());
        }
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
     * Returns whether the main track is already available.
     * @return <code>true</code> if a main track is available,
     *          <code>false</code> otherwise.
     */
    public boolean isMainTrackLoaded() {
        return mainTrackLoaded;
    }

    /**
     * Loads the sound with the given URL into the configuration.
     * @param urlString the url string.
     * @throws MediaIOException if an error occurred while setting the main
     *          track
     */
    public void loadMainTrack(String urlString) throws MediaIOException {
        setInputProperty(new InputProperty(new File(urlString), 0));
        loadMain(urlString);
    }

    /**
     * Loads the sound file with the given file into the configuration.
     * @param f the file name.
     * @throws MediaIOException if an error occurred while setting the main
     *          track
     */
    public void loadMainTrack(File f) throws MediaIOException {
        setInputProperty(new InputProperty(f, 0));
        loadMain(f.toURI().toString());
    }

    /**
     * Loads the main track from a given URL.
     * @param urlString the location as string.
     * @throws MediaIOException if an error occurred while setting the main
     *          track
     */
    public void loadMainTrackURL(String urlString) throws MediaIOException {
        try {
            setInputProperty(new InputProperty(new URI(urlString), 0));
            loadMain(urlString);
        } catch (URISyntaxException e) {
            throw new MediaIOException(e);
        }
    }

    private void loadMain(String s) throws MediaIOException {
        if (player != null) {
            player.setMainTrack(s);
        }
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
            UrlTrack track = player.addInputTrack(uri);
            effectConfig.addUri(uri);
            return track;
        } catch (IOException e) {
            throw new MediaIOException(e);
        }
    }

    /**
     * Sets the player for this configuration.
     * @param player the player.
     * @throws MediaIOException if an error occured while trying to load 
     *          config properties into player.
     * @throws IOException if an error occured while trying to load an input
     *          track.
     */
    public void setPlayer(PanicAudioPlayer player) throws MediaIOException, IOException {
        this.player = player;
        // load effect configuration into player
        if (inputProperty != null) {
            player.setMainTrack(inputProperty.getAddress().toString(), false);
        }

        // add available uris (drag panel)
        final Collection uris = effectConfig.getURIs();
        if (uris != null && !uris.isEmpty()) {
            for (Iterator i = uris.iterator(); i.hasNext(); ) {
                player.addInputTrack((URI) i.next());
            }
        }

        // add track playlist (drop panel)
        final Collection tracks = effectConfig.getTracks();
        if (tracks != null && !tracks.isEmpty()) {
            for (Iterator i = tracks.iterator(); i.hasNext(); ) {
                player.addTrackForInit((UrlTrack) i.next());
            }
        }
    }
}