// ----------------------------------------------------------------------------
// [b12] Java Source File: InputProperty.java
//                created: 30.11.2003
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import b12.panik.io.UrlTrack;
import b12.panik.util.TimeFormatter;

/**
 * Represents a configurable input. The input consists of a data source and
 * several properties that configure the input's behaviour.
 *
 * @author kariem
 */
public class InputProperty {

    private static final String TAG_TRACK = "track";

    private static final String ATTR_FILE = "file";
    private static final String ATTR_MULTIPLY = "multiply";
    private static final String ATTR_START = "start";
    private static final String ATTR_URI = "uri";

    // offset in seconds
    private long start;
    // multipy sound file
    private int multiply = 1;
    // address
    private URI address;
    // filename
    private File file;
    // tracks
    private List tracks;

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param address the address to use.
     * @param file the file.
     * @param start the offset for the start.
     * @param multiply the multiplier.
     */
    public InputProperty(URI address, File file, long start, int multiply) {
        this.address = address;
        this.start = start;
        this.multiply = multiply;
        this.file = file;
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param address the address to use.
     * @param start the offset for the start.
     * @param multiply the multiplier.
     */
    public InputProperty(URI address, long start, int multiply) {
        this(address, null, start, multiply);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param file the file to use.
     * @param start the offset for the start.
     * @param multiply the multiplier.
     */
    public InputProperty(File file, long start, int multiply) {
        this(file.toURI(), file, start, multiply);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param po the object holding the information.
     * @throws MalformedURLException if an error occured while trying to
     *          convert the string into an address.
     */
    public InputProperty(ParsedObject po) throws MalformedURLException {
        this(getURI(po), getFile(po), getTime(po.getAttribute(ATTR_START)), po.getAttributeInt(
                ATTR_MULTIPLY, 1));
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param file the file.
     * @param start the offset for the start.
     */
    public InputProperty(File file, long start) {
        this(file, start, 1);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param uri the uri.
     * @param start the offset for the start.
     */
    public InputProperty(URI uri, long start) {
        this(uri, start, 1);
    }

    /**
     * Creates a new import property from the given parsed object.
     * @param po the parsed object.
     * @return a newly created input property with the properties retrieved
     *          from <code>po</code>.
     * @throws MalformedURLException if no file and nor URI were specified in
     *          <code>po</code>.
     */
    public static InputProperty parseInputProperty(ParsedObject po)
            throws MalformedURLException {
        URI uri = getURI(po);
        File f = getFile(po);
        long time = getTime(po.getAttribute(ATTR_START));
        int multiply = po.getAttributeInt(ATTR_MULTIPLY, 1);
        InputProperty ip = new InputProperty(uri, f, time, multiply);
        return ip;

    }

    private static File getFile(ParsedObject po) {
        String filename = po.getAttribute(ATTR_FILE);
        if (filename == null) {
            return null;
        }
        return new File(filename);
    }

    private static URI getURI(ParsedObject po) throws MalformedURLException {
        String urlString = po.getAttribute(ATTR_URI);
        if (urlString == null) {
            urlString = po.getAttribute(ATTR_FILE);
            if (urlString == null) {
                // still nothing => throw Exception
                throw new MalformedURLException("no URL specified");
            }
            File f = new File(urlString);
            return f.toURI();
        }
        return URI.create(urlString);
    }

    static long getTime(String s) {
        if (s == null) {
            return 0;
        }
        return TimeFormatter.parseFast(s);
    }

    static String timeToString(long time) {
        return TimeFormatter.format(time);
    }

    /**
     * Fills <code>po</code> with the information from this
     * <code>InputProperty</code>. If filename is set, the resulting
     * <code>ParsedObject</code> will only contain the <i>file</i> attribute,
     * and no <i>url</i>.
     *
     * @param po the object.
     */
    void fillParsedObject(ParsedObject po) {
        if (file != null) {
            // reduce to current directory;
            String dirString = new File(".").getAbsolutePath();
            // without "."
            dirString = dirString.substring(0, dirString.length() - 1);
            String saveFile = file.toString();
            if (saveFile.startsWith(dirString)) {
                // remove current directory to avoid absolute paths in file name
                saveFile = saveFile.substring(dirString.length());
            }
            po.addAttribute(ATTR_FILE, saveFile);
        } else {
            po.addAttribute(ATTR_URI, address.toString());
        }
        if (start != 0) {
            po.addAttribute(ATTR_START, Long.toString(start));
        } else {
            // different tracks are to be added
            if (tracks != null && !tracks.isEmpty()) {
                Iterator i = tracks.iterator();
                while (i.hasNext()) {
                    SimpleTrack st = (SimpleTrack) i.next();
                    st.fillParsedObject(po.addChild(TAG_TRACK));
                }
            }
        }
        if (multiply != 1) {
            po.addAttribute(ATTR_MULTIPLY, Integer.toString(multiply));
        }
    }

    /**
     * Adds a track to this input property.
     * @param enabled whether the track is enabled.
     * @param trackStart the offset start of the track in milliseconds.
     */
    void addTrack(boolean enabled, long trackStart) {
        addTrack(new SimpleTrack(trackStart, enabled));
    }

    private void addTrack(SimpleTrack track) {
        initTrackList();
        if (start > 0) {
            // two separate tracks are now necessary ... add the first track
            SimpleTrack main = new SimpleTrack(start, true);
            tracks.add(main);
            // set start to 0 to indicate that there are internal tracks
            start = 0;
        }
        // add the track intended to be added
        tracks.add(track);
    }

    private void initTrackList() {
        if (tracks == null) {
            tracks = new ArrayList(4);
        }
    }

    /**
     * Returns the URL tracks that are specified by this input property.
     * @return either containing one element or multiple elements.
     */
    UrlTrack[] getUrlTracks() {
        UrlTrack[] urlTracks;
        if (tracks != null && !tracks.isEmpty()) {
            urlTracks = new UrlTrack[tracks.size()];
            int pos = 0;
            for (Iterator i = tracks.iterator(); i.hasNext(); ) {
                SimpleTrack st = (SimpleTrack) i.next();
                urlTracks[pos++] = st.createUrlTrack(address);
            }
        } else {
            urlTracks = new UrlTrack[]{new UrlTrack(address, start)};
        }
        return urlTracks;
    }

    /*
     *
     * simple getters and setters
     *  
     */

    /**
     * Returns the input's address.
     * @return the input's address. If not available the underlying file will
     *          be converted to URI and then returned. 
     */
    public URI getAddress() {
        if (address == null) {
            address = file.toURI();
        }
        return address;
    }
    
    /**
     * Sets the address.
     * @param address The address.
     */
    public void setAddress(URI address) {
        this.address = address;
    }

    /**
     * Returns the multiply.
     * @return the multiply.
     */
    public int getMultiply() {
        return multiply;
    }

    /**
     * Sets the multiply.
     * @param multiply The multiply.
     */
    public void setMultiply(int multiply) {
        this.multiply = multiply;
    }

    /**
     * Returns the offset.
     * @return the offset.
     */
    public long getOffset() {
        return start;
    }

    /**
     * Sets the offset.
     * @param offset The offset.
     */
    public void setOffset(long offset) {
        this.start = offset;
    }

    /**
     * Returns the file.
     * @return the file.
     */
    public File getFilename() {
        return file;
    }

    /**
     * Sets the file.
     * @param file The file.
     */
    public void setFilename(File file) {
        this.file = file;
    }
    
    private static class SimpleTrack {
        private static final String ATTR_ENABLED = "enabled";

        boolean enabled;
        long trackStart;

        /**
         * Creates a new instance of <code>SimpleTrack</code>.
         * @param enabled
         * @param start
         */
        SimpleTrack(long start, boolean enabled) {
            this.trackStart = start;
            this.enabled = enabled;
        }

        void fillParsedObject(ParsedObject po) {
            if (!enabled) {
                po.addAttribute(ATTR_ENABLED, Boolean.toString(enabled));
            }
            po.addAttribute(ATTR_START, InputProperty.timeToString(trackStart));
        }

        static SimpleTrack parseSimpleTrack(ParsedObject po) {
            long startTime = InputProperty.getTime(po.getAttribute(ATTR_START));
            boolean enabledState = po.getAttributeBoolean(ATTR_ENABLED, true);
            return new SimpleTrack(startTime, enabledState);
        }

        UrlTrack createUrlTrack(URI uri) {
            return new UrlTrack(uri, trackStart, enabled);
        }
    }
}