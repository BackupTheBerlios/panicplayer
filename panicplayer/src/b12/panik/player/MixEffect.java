// ----------------------------------------------------------------------------
// [b12] Java Source File: MixEffect.java
//                created: 29.10.2003
//              $Revision: 1.20 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.media.*;
import javax.media.format.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;

import b12.panik.io.IOUtils;
import b12.panik.io.UrlTrack;
import b12.panik.ui.TracksPanel;
import b12.panik.util.*;

/**
 * This is the mixing effect. This class is used to mix different tracks. The
 * background track is the input parameter for the method.
 * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from
 * the outside, so that the <code>MixEffect</code> is capable of modifying
 * the input buffer correctly.
 */
public class MixEffect implements Codec, TrackManager {

    // the arrays for input and output formats
    /** input formats */
    public static final AudioFormat[] FORMATS_INPUT = IFormat.FORMATS;
    /** output formats */
    public static final AudioFormat[] FORMATS_OUTPUT = IFormat.FORMATS;

    private Format currentFormat;
    private String effectName = "PanicPlayerEffect";

    //the number of tracks that can be played simultaneous
    private int maxConcurrentTracks = 4;

    private SortedSet offsetedArrays;

    /*    private Set playedOffsetedArrays;
     private SortedSet nonPlayedOffsetedArrays;
     private OffsetedArray currentOffsetedArray;
     private OffsetedArray currentOffsetedArrayToPlay;
     private Set offsetedArraysToAdd;
     private SortedSet tempNonPlayedOffsetedArrays;
     private SortedSet tempPlayedOffsetedArrays;
     */
    /**	Time in ms per 1000 byte. */
    private double timePer1000Byte;

    private boolean initialisationRealized = false;

    private long currentBeginIndex;
    private long currentEndIndex;
    //    private Iterator iterator;

    private final Set urlTracks;

    private byte[] currentData;

    final TracksPanel tracksPanel;

    /**
     * Creates a new instance of <code>MixEffect</code>.
     */
    public MixEffect() {
        urlTracks = new TreeSet(new TrackComparator());
        // initialize component
        tracksPanel = new TracksPanel();
        tracksPanel.setTrackManager(this);
        //setTimePer1000Byte();
    }

    /**
     * Set the number of maximal simultaneous tracks.
     * @param nb the desired number. 
     */
    public void setMaxConcurrentTracks(int nb) {
        maxConcurrentTracks = nb;
    }

    /**
     * Return the number of maximal simultaneous tracks.
     * @return the number of simultaneous tracks.
     */
    public int getMaxConcurrentTracks() {
        return maxConcurrentTracks;
    }

    /**
     * Adds a single input <code>UrlTrack</code> to the effect. This track will
     * not be managed.
     * @param track the track to be added.
     */
    void addInputUrlTrack(UrlTrack track) {
        tracksPanel.addTrack(track);
    }
    
    /**
     * Adds a track to this effect, which will be managed by the effect, if the
     * track is enabled.
     * @param track the track to be added.
     * @throws ConstraintsException
     */
    public void addTrack(UrlTrack track) throws ConstraintsException {
        urlTracks.add(track);
        System.out.println("track added");
        initialisationRealized = false;
    }

    //
    // Codec implementation methods
    //
    /** @see Codec#getSupportedInputFormats() */
    public Format[] getSupportedInputFormats() {
        return FORMATS_INPUT;
    }

    /** @see Codec#getSupportedOutputFormats(Format) */
    public Format[] getSupportedOutputFormats(Format input) {
        return FORMATS_OUTPUT;
    }

    /** @see Codec#setInputFormat(Format) */
    public Format setInputFormat(Format format) {
        for (int i = 0; i < FORMATS_INPUT.length; i++) {
            if (FORMATS_INPUT[i].matches(format)) {
                currentFormat = format;
                break;
            }
        }
        Logging.fine("MixEffect: current input format set to " + currentFormat);
        return currentFormat;
    }

    /** @see Codec#setOutputFormat(Format) */
    public Format setOutputFormat(Format format) {
        for (int i = 0; i < FORMATS_OUTPUT.length; i++) {
            if (FORMATS_OUTPUT[i].matches(format)) {
                currentFormat = format;
                break;
            }
        }
        Logging.fine("MixEffect: current output format set to " + currentFormat);
        return currentFormat;
    }

    /** @see Codec#process(Buffer, Buffer) */
    public int process(Buffer inputBuffer, Buffer outputBuffer) {
        //System.out.println("...");
        //printShortedBuffer(inputBuffer);
        final long timeStamp = inputBuffer.getTimeStamp();
        // calculate number of bytes from zero
        /* System.out.println("temps en ns: "+processor.getMediaNanoseconds());
         double temp=(double) ((processor.getMediaNanoseconds())/(((double) (1000))*timePer1000Byte));
         long timeInByte=(long) Math.floor(temp);
         
         System.out.println("temps en byte: "+timeInByte); */

        int inputLength = (inputBuffer.getLength());
        if (!initialisationRealized) {
            //setTimePer1000Byte();

            //test
            /*	try {
             //                    URI urlAah =
             //                        new File(
             //                        "file:///D:/Kariem/Uni/ws0304/mm1ue/project/massenpanik2/sounds/aaaa2.25.11k.wav").toURI();
             URI urlAah =
             new File("sounds/aaaa2.25.11k.wav").toURI();
             
             URI urlOoh =
             new File("sounds/oooo2.5.11k.wav").toURI();
             URI urlIih =
             new File("sounds/iiii2.5.11k.wav").toURI();
             
             //             URL urlAah =
             //             new java.net.URL(
             //             "file:///D:/Kariem/Uni/ws0304/mm1ue/project/massenpanik2/sounds/aaaa2.25.11k.wav");
             //             URL urlOoh =
             //             new java.net.URL(
             //             "file:///D:/Kariem/Uni/ws0304/mm1ue/project/massenpanik2/sounds/oooo2.5.11k.wav");
             //             URL urlIih =
             //             new java.net.URL(
             //             "file:///D:/Kariem/Uni/ws0304/mm1ue/project/massenpanik2/sounds/iiii2.5.11k.wav");

             UrlTrack trackAah = new UrlTrack(urlAah, 500);
             urlTracks.add(trackAah);
             UrlTrack trackOoh = new UrlTrack(urlOoh, 3000);
             urlTracks.add(trackOoh);
             UrlTrack trackIih = new UrlTrack(urlIih, 6000);
             urlTracks.add(trackIih);
             UrlTrack trackOoh2=new UrlTrack(urlOoh,2000);
             urlTracks.add(trackOoh2);
             UrlTrack trackIih2 = new UrlTrack(urlIih, 1000);
             urlTracks.add(trackIih2);
             } catch (Exception e) {
             System.out.println("********pb d url*************");
             } */
            //fin test
            initialiseOffsetedArrays();
            currentBeginIndex = -inputLength;
            /*            playedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
             if (!(offsetedArrays.isEmpty())) {
             nonPlayedOffsetedArrays = new TreeSet(offsetedArrays);
             }
             */
            initialisationRealized = true;
        }

        if ((inputLength == 0) || (offsetedArrays.isEmpty())) {
            outputBuffer.copy(inputBuffer);
            return BUFFER_PROCESSED_OK;
        }

        ///currentBeginIndex += inputLength;
        double byteOffset = timeStamp / 1000 / timePer1000Byte - inputLength;
        currentBeginIndex = (long) byteOffset;
        currentEndIndex = currentBeginIndex + inputLength;

        /*        if (!(nonPlayedOffsetedArrays.isEmpty())) {
         iterator = nonPlayedOffsetedArrays.iterator();
         currentOffsetedArray = (OffsetedArray) iterator.next();
         } else {
         currentOffsetedArray = null;
         }

         tempNonPlayedOffsetedArrays = new TreeSet(nonPlayedOffsetedArrays);

         while ((currentOffsetedArray != null)
         && (currentOffsetedArray.getStartIndex() <= currentEndIndex)) {
         playedOffsetedArrays.add(currentOffsetedArray);
         tempNonPlayedOffsetedArrays.remove(currentOffsetedArray);
         if (iterator.hasNext()) {
         currentOffsetedArray = (OffsetedArray) iterator.next();
         } else {
         currentOffsetedArray = null;
         }
         }

         nonPlayedOffsetedArrays = new TreeSet(tempNonPlayedOffsetedArrays);
         offsetedArraysToAdd = new TreeSet(new OffsetedArrayComparator());
         offsetedArraysToAdd.add(new OffsetedArray(inputBuffer, currentBeginIndex,
         maxConcurrentTracks));

         tempPlayedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
         tempPlayedOffsetedArrays.addAll(playedOffsetedArrays);
         currentOffsetedArray = null;
         for (Iterator i = playedOffsetedArrays.iterator(); i.hasNext(); ) {
         currentOffsetedArray = (OffsetedArray) i.next();
         currentOffsetedArrayToPlay = new OffsetedArray(currentOffsetedArray,
         currentBeginIndex, currentEndIndex);

         if (currentOffsetedArrayToPlay.getDurationIndex() == 0) {
         tempPlayedOffsetedArrays.remove(currentOffsetedArray);
         } else {
         offsetedArraysToAdd.add(currentOffsetedArrayToPlay);
         }
         }
         playedOffsetedArrays = new TreeSet(new OffsetedArrayComparator());
         playedOffsetedArrays.addAll(tempPlayedOffsetedArrays);
         */
        Set offsetedArraysToAdd = new TreeSet(new OffsetedArrayComparator());

        for (Iterator i = offsetedArrays.iterator(); i.hasNext(); ) {
            OffsetedArray osa = (OffsetedArray) i.next();
            final long osaStart = osa.getStartIndex();
            if (osaStart <= currentBeginIndex) {
                if (osaStart + osa.getDurationIndex() > currentBeginIndex) {
                    offsetedArraysToAdd.add(osa);
                }
            } else if (osaStart > currentBeginIndex && osaStart < currentEndIndex) {
                offsetedArraysToAdd.add(osa);
            }
        }

        // add current buffer
        OffsetedArray main = new OffsetedArray(inputBuffer, currentBeginIndex, false);
        main.name = "main";
        offsetedArraysToAdd.add(main);

        System.out.println("---");

        final int size = offsetedArraysToAdd.size();
        if (size > 0) {
            // change gain of all tracks which are going to be added
            for (Iterator i = offsetedArraysToAdd.iterator(); i.hasNext(); ) {
                OffsetedArray os = (OffsetedArray) i.next();
                os.multiply(1.0/size);
            }
        }

        OffsetedArray outputArray = addOffsetedArrays(offsetedArraysToAdd);
        outputArray.setSizeTo(((byte[]) inputBuffer.getData()).length);

        outputBuffer.copy(inputBuffer);

        outputArray.putData(outputBuffer);
        //        System.out.print("out: ");
        //        printShortedBuffer(outputBuffer);
        return BUFFER_PROCESSED_OK;
    }

    OffsetedArray addOffsetedArrays(Set offsetedArraysSet) {
        System.out.println("Adding offset arrays");
        OffsetedArray offsetedArrayResult = null;
        //        int i = 1;
        for (Iterator iter = offsetedArraysSet.iterator(); iter.hasNext(); ) {
            OffsetedArray currentOffsetedArray = (OffsetedArray) iter.next();
            System.out.println("  adding " + currentOffsetedArray);
            offsetedArrayResult = OffsetedArray.addOffsetedArray(offsetedArrayResult,
                    currentOffsetedArray);
            //            i++;
        }
        return offsetedArrayResult;
    }

    /**
     * Prepare the effect to restart.
     *
     */
    public void prepareToRestart() {
        initialisationRealized = false;
        //    	currentBeginIndex=0;    	
    }

    /**
     * Set the time in nanosecond per 1000 byte.
     * @param wanted the time per 1000 byte wanted
     */
    public void setTimePer1000Byte(double wanted) {
        timePer1000Byte = wanted; //90.70;
    }

    /**
     * Display a sampler (1 byte on 800) of a buffer.
     * @param buffer the buffer to be displayeded.
     */
    public void printShortedBuffer(Buffer buffer) {
        byte[] dataIn = ((byte[]) buffer.getData());
        int length = buffer.getLength();
        for (int i = 0; i < length; i = i + 800) {
            System.out.print(dataIn[i + buffer.getOffset()] + " ");
        }
        System.out.println("");
    }

    /** @see PlugIn#getName() */
    public String getName() {
        // TODO Implement method
        return effectName;
    }

    /** @see PlugIn#open() */
    public void open() {
        // TODO Implement method
        // see if sound is available, and if output on sound is possible
    }

    /** @see PlugIn#close() */
    public void close() {
        // TODO Implement method
    }

    /** @see PlugIn#reset() */
    public void reset() {
        // do nothing
    }
    
    /** Resets the mixeffect to its initial state. */
    public void resetVisuals() {
        urlTracks.clear();
        tracksPanel.reset();
    }

    /** @see Controls#getControls() */
    public Object[] getControls() {
        // TODO Implement method
        return new Control[0];
    }

    /**
     * Returns the interface component for this effect.
     * @return the component for user interaction.
     */
    public JComponent getComponent() {
        return tracksPanel;
    }

    /**
     * Sets the main track for this effect. This method is important for the
     * rendering component in order to scale its contents correctlz. The
     * track's data is processed in the process method.
     * @param url the url.
     */
    public void setMainTrack(final URL url) {
        // return immediately after call to this method. Thread does the rest.
        Thread lengthUpdater = new Thread("Length Updater") {
            /** @see java.lang.Thread#run() */
            public void run() {
                if (this == Thread.currentThread()) {
                    try {
                        double seconds = IOUtils.getTrackLength(url); 
                        tracksPanel.setLength(seconds);
                        setTimePer1000Byte(IOUtils.getTimePer1000Byte(url));
                    } catch (UnsupportedAudioFileException e) {
                        Logging.severe("Format not supported by audio system.", e);
                    } catch (IOException e) {
                        Logging.severe("Audio file could not be found.", e);
                    }
                }
            }
        };
        initialisationRealized = false;
        lengthUpdater.start();
    }

    /** @see Controls#getControl(String) */
    public Object getControl(String controlType) {
        // TODO Implement method
        try {
            Class cls = Class.forName(controlType);
            Object cs[] = getControls();
            for (int i = 0; i < cs.length; i++) {
                if (cls.isInstance(cs[i]))
                    return cs[i];
            }
            return null;
        } catch (Exception e) { // no such controlType or such control
            return null;
        }
    }

    /**
     * Returns the current data.
     * @return the currently processed data.
     */
    public byte[] currentData() {
        return currentData;
    }

    /** Initialise the OffsetedArrays by reading the urlTracks. */
    public void initialiseOffsetedArrays() {
        offsetedArrays = new TreeSet(new OffsetedArrayComparator());
        for (Iterator iter = urlTracks.iterator(); iter.hasNext(); ) {
            UrlTrack currentUrlTrack = (UrlTrack) iter.next();
            offsetedArrays.add(new OffsetedArray(currentUrlTrack, timePer1000Byte));
        }
    }

}
