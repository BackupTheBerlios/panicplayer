// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaOutput.java
//                created: 01.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.IOException;
import java.net.URL;

import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

import b12.panik.util.Logging;

/**
 * This class is used to write media data to a destination.
 */
public class MediaOutput {

    /** The output format. */
    public static final Format FORMAT_RTP_OUTPUT = new AudioFormat(AudioFormat.GSM_RTP, 8000,
            8, 1);

    /**
     * Writes a data source to its destination.
     * 
     * @param source
     *            the source to write.
     * @param destUrl
     *            the destination URL. If the protocol is <i>RTP</i> the
     *            output is encoded in {@linkplain FORMAT_RTP_OUTPUT}, in
     *            order to be effectively sent over the network.
     * @throws MediaIOException
     *             if one of the following errors occur:
     *             <ul>
     *             <li>No data sink was found.</li>
     *             <li>Problem writing to the data sink.</li>
     *             <li>Security violation while accessing <code>destUrl</code>.
     *             </li>
     *             <li>No processor could be created for the data source
     *             (RTP).</li>
     *             <li>No connection to the data source is possible (RTP).
     *             </li>
     *             <li>Encoding failed (RTP).</li>
     *             </ul>
     */
    public void write(DataSource source, URL destUrl) throws MediaIOException {
        String protocol = destUrl.getProtocol();
        debug("write", "protocol=" + protocol);
        if (protocol.equals("rtp")) {
            Processor p = null;
            try {
                // change encoding in order to send using RTP
                p = Manager.createProcessor(source);
            } catch (NoProcessorException e) {
                error("No processor could be created.", e);
            } catch (IOException e) {
                error("Problem connecting to data source.", e);
            }
            if (p == null) {
                error("No processor could be created.");
            }
            // see jmf-guide p. 148/149 for more information

            // block until processor is configured
            p.configure();
            // output content is in RAW format
            p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
            TrackControl[] trackControls = p.getTrackControls();
            boolean encodingOk = false;
            for (int i = 0; i < trackControls.length; i++) {
                // program tracks to output GSM data
                TrackControl c = trackControls[i];
                if (!encodingOk && c instanceof FormatControl) {
                    FormatControl fc = (FormatControl) c;
                    boolean error = fc.setFormat(FORMAT_RTP_OUTPUT) == null;
                    if (error) {
                        // setFormat returned null => disable track control
                        fc.setEnabled(false);
                        debug("write", "disabled track " + fc);
                    } else {
                        encodingOk = true;
                    }
                } else {
                    // no format change possible => disable track
                    c.setEnabled(false);
                    debug("write", "disabled track " + c);
                }
            }

            if (!encodingOk) {
                error("Encoding failed.");
            }

            // all tracks correctly converted to FORMAT_RTP_OUTPUT or disabled
            p.realize();
            DataSource ds = p.getDataOutput();
            writeSimple(ds, destUrl);

        } else {
            writeSimple(source, destUrl);
        }
    }

    /**
     * Writes <code>source</code> to <code>destUrl</code> without further
     * processing.
     * 
     * @param source
     *            the source.
     * @param destUrl
     *            the destination url.
     * @throws MediaIOException
     *             if an error occurs.
     */
    private void writeSimple(DataSource source, URL destUrl) throws MediaIOException {
        DataSink sink;
        MediaLocator dest = new MediaLocator(destUrl);
        try {
            sink = Manager.createDataSink(source, dest);
            sink.open();
            sink.start();
        } catch (NoDataSinkException e) {
            error("No correct data sink found for " + destUrl + ".", e);
        } catch (IOException e) {
            error("IO problem while trying to open/start writing to destination.", e);
        } catch (SecurityException e) {
            error("Security violation while trying to access " + destUrl + ".", e);
        }
    }

    private void debug(String method, String message) {
        Logging.fine("MediaOutput", method, message);
    }

    private void error(String message, Throwable t) throws MediaIOException {
        if (t == null) { throw new MediaIOException(message); }
        throw new MediaIOException(message, t);
    }

    private void error(String message) throws MediaIOException {
        error(message, null);
    }
}