// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackCreator.java
//                created: 09.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.IOException;

import javax.media.*;
import javax.media.Demultiplexer;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

/**
 * @author Jorge De Mar
 */

public class TrackCreator implements Demultiplexer {

    /** @see javax.media.Demultiplexer#getSupportedInputContentDescriptors() */
    public ContentDescriptor[] getSupportedInputContentDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.Demultiplexer#start() */
    public void start() throws IOException {
        // TODO Auto-generated method stub
        
    }

    /** @see javax.media.Demultiplexer#stop() */
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    /** @see javax.media.Demultiplexer#getTracks() */
    public Track[] getTracks() throws IOException, BadHeaderException {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.Demultiplexer#isPositionable() */
    public boolean isPositionable() {
        // TODO Auto-generated method stub
        return false;
    }

    /** @see javax.media.Demultiplexer#isRandomAccess() */
    public boolean isRandomAccess() {
        // TODO Auto-generated method stub
        return false;
    }

    /** @see javax.media.Demultiplexer#setPosition(javax.media.Time, int) */
    public Time setPosition(Time arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.Demultiplexer#getMediaTime() */
    public Time getMediaTime() {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.Demultiplexer#getDuration() */
    public Time getDuration() {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.PlugIn#getName() */
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.PlugIn#open() */
    public void open() throws ResourceUnavailableException {
        // TODO Auto-generated method stub
        
    }

    /** @see javax.media.PlugIn#close() */
    public void close() {
        // TODO Auto-generated method stub
        
    }

    /** @see javax.media.PlugIn#reset() */
    public void reset() {
        // TODO Auto-generated method stub
        
    }

    /** @see javax.media.Controls#getControls() */
    public Object[] getControls() {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.Controls#getControl(java.lang.String) */
    public Object getControl(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /** @see javax.media.MediaHandler#setSource(javax.media.protocol.DataSource) */
    public void setSource(DataSource arg0) throws IOException, IncompatibleSourceException {
        // TODO Auto-generated method stub
        
    }

}
