// ----------------------------------------------------------------------------
// [b12] Java Source File: IOUtils.java
//                created: 29.11.2003
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.sound.sampled.*;

/**
 * This class provides several methods for calls to the IO. Methods to open
 * files are supported by this class.
 *
 * @author kariem
 */
public class IOUtils {

    /**
     * Plays the content of a url.
     *
     * @param url
     *            the url.
     * @throws NoPlayerException
     *             if no player could be created for the resource.
     * @throws IOException
     *             if an IO problem occured while trying to load the resource.
     */
    public static void playSimple(URL url) throws NoPlayerException, IOException {
        getSimplePlayer(url).start();
    }

    /**
     * Returns a simple player for the given resource.
     *
     * @param url
     *            the url.
     * @return a player capable of playing the data found in <code>url</code>.
     * @throws NoPlayerException
     *             if no player could be created.
     * @throws IOException
     *             if an IO problem occured while trying to load the resource.
     */
    public static Player getSimplePlayer(URL url) throws NoPlayerException, IOException {
        return Manager.createPlayer(url);
    }

    /**
     * Creates a track from an URL.
     * @param uri the uri to load the track from.
     * @return the track which is to be loaded.
     * @throws IOException if the audio file at the specified url is not
     *          supported, or the url could not be read.
     */
    public static UrlTrack createTrack(URI uri) throws IOException {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(uri));
            AudioFormat f = stream.getFormat();
            double seconds = stream.getFrameLength() / f.getFrameRate();
            return new UrlTrack(uri, 0, seconds, f);
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Returns the length of a track at the given address.
     * @param trackAddress the address where the audio file is located.
     * @return the time in seconds.
     * 
     * @throws UnsupportedAudioFileException if the audio file is not
     *          supported.
     * @throws IOException if the file could not be found or an error occured
     *          while loading the file.
     */
    public static double getTrackLength(URL trackAddress) throws UnsupportedAudioFileException,
            IOException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(trackAddress);
        AudioFormat f = stream.getFormat();
        return stream.getFrameLength() / f.getFrameRate();
    }

    /**     * Returns the time per 1000 byte of a track at the given address.     * @param trackAddress the address where the audio file is located.     * @return the time in milliseconds.     *      * @throws UnsupportedAudioFileException if the audio file is not     *          supported.     * @throws IOException if the file could not be found or an error occured     *          while loading the file.     */
    public static double getTimePer1000Byte(URL trackAddress)
            throws UnsupportedAudioFileException, IOException {
        AudioInputStream stream = AudioSystem.getAudioInputStream(trackAddress);
        AudioFormat f = stream.getFormat();

        return timePer1000Byte(f.getFrameSize(), f.getFrameRate());
    }
    
    /**
     * Returns the time in milliseconds per 1000 bytes.
     * @param frameSize the frame size
     * @param frameRate the frame rate.
     * @return the time in milliseconds for 1000 bytes.
     */
    public static double timePer1000Byte(int frameSize, double frameRate) {
        return 1E6 / (frameSize * frameRate);      
    }
}
