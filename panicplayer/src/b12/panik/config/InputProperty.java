// ----------------------------------------------------------------------------
// [b12] Java Source File: InputProperty.java
//                created: 30.11.2003
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import b12.panik.util.TimeFormatter;

/**
 * Represents a configurable input. The input consists of a data source and
 * several properties that configure the input's behaviour.
 *
 * @author kariem
 */
public class InputProperty {

    private static final String ATTR_URL = "url";
    private static final String ATTR_FILE = "file";
    private static final String ATTR_OFFSET = "offset";
    private static final String ATTR_MULTIPLY = "multiply";

    // offset in seconds
    private long offset;
    // multipy sound file
    private int multiply = 1;
    // address
    private URI address;
    // filename
    private File file;

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param address the address to use.
     * @param file the file.
     * @param offset the offset.
     * @param multiply the multiplier.
     */
    public InputProperty(URI address, File file, long offset, int multiply) {
        this.address = address;
        this.offset = offset;
        this.multiply = multiply;
        this.file = file;
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param address the address to use.
     * @param offset the offset.
     * @param multiply the multiplier.
     */
    public InputProperty(URI address, long offset, int multiply) {
        this(address, null, offset, multiply);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param file the file to use.
     * @param offset the offset.
     * @param multiply the multiplier.
     */
    public InputProperty(File file, long offset, int multiply) {
        this(file.toURI(), file, offset, multiply);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param po the object holding the information.
     * @throws MalformedURLException if an error occured while trying to
     *          convert the string into an address.
     */
    public InputProperty(ParsedObject po) throws MalformedURLException {
        this(getURI(po), getFile(po), getTime(po.getAttribute(ATTR_OFFSET)), po
                .getAttributeInt(ATTR_MULTIPLY, 1));
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param file the file.
     * @param offset the offset.
     */
    public InputProperty(File file, long offset) {
        this(file, offset, 1);
    }

    /**
     * Creates a new instance of <code>InputProperty</code>.
     * @param uri the uri.
     * @param offset the offset.
     */
    public InputProperty(URI uri, long offset) {
        this(uri, offset, 1);
    }
    
    private static File getFile(ParsedObject po) {
        String filename = po.getAttribute(ATTR_FILE);
        if (filename == null) {
            return null;
        }
        return new File(filename);
    }

    private static URI getURI(ParsedObject po) throws MalformedURLException {
        String urlString = po.getAttribute(ATTR_URL);
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

    private static long getTime(String s) {
        if (s == null) {
            return 0;
        }
        return TimeFormatter.parseFast(s);
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
            po.addAttribute(ATTR_URL, address.toString());
        }
        if (offset != 0) {
            po.addAttribute(ATTR_OFFSET, Long.toString(offset));
        }
        if (multiply != 1) {
            po.addAttribute(ATTR_MULTIPLY, Integer.toString(multiply));
        }
    }

    /**
     * Returns the address.
     * @return the address.
     */
    public URI getAddress() {
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
        return offset;
    }

    /**
     * Sets the offset.
     * @param offset The offset.
     */
    public void setOffset(long offset) {
        this.offset = offset;
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
}
