// ----------------------------------------------------------------------------
// [b12] Java Source File: MixEffect.java
//                created: 29.10.2003
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.media.*;
import javax.media.format.AudioFormat;

import b12.panik.util.Logging;
import b12.panik.util.TrackComparator;

/**
 * This is the mixing effect. This class is used to mix different tracks. The
 * background track is the input parameter for the method.
 * {@linkplain #process(Buffer, Buffer)}. The other tracks have to be set from
 * the outside, so that the <code>MixEffect</code> is capable of modifying
 * the input buffer correctly.
 */
public class MixEffect implements Codec {

   // two audio formats
   private static final AudioFormat[] FORMATS = new AudioFormat[]{
         new AudioFormat(AudioFormat.LINEAR, 11025, 8, 1, AudioFormat.BIG_ENDIAN,
               AudioFormat.UNSIGNED),
         new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1, AudioFormat.BIG_ENDIAN,
               AudioFormat.UNSIGNED)};

   // the arrays for input and output formats
   /** input formats */
   public static final AudioFormat[] FORMATS_INPUT = FORMATS;

   /** output formats */
   public static final AudioFormat[] FORMATS_OUTPUT = FORMATS;

   private Format currentFormat;
   
   private String effectName = "PanicPlayerEffect";

   /**
    * Contains a sorted set of tracks that have to be mixed into the input.
    * The list is sorted by the starting time of the tracks.
    */
   SortedSet tracks;

   private byte[] currentData;

   /**
    * Creates a new instance of <code>MixEffect</code>.
    */
   public MixEffect() {
      // should be sorted automatically
      tracks = new TreeSet(new TrackComparator());
   }

   /**
    * Adds a single input <code>Track</code> to the effect.
    * @param track the track to be added. 
    */
   void addInputTrack(Track track) {
      tracks.add(track);
   }

   /**
    * Adds a <code>Collection</code> of <code>Track</code> objects to the
    * effect.
    * @param newTracks the new <code>Track</code>s to be added.
    * @return <code>true</code> if this list changed as a result of the call.
    */
   boolean addInputTracks(Collection newTracks) {
      return tracks.addAll(newTracks);
      // return true;
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
      for (int i = 0; i < FORMATS.length; i++) {
         if (FORMATS[i].matches(format)) {
            currentFormat = format;
            break;
         }
      }
      Logging.fine("MixEffect: current input format set to " + currentFormat);
      return currentFormat;
   }

   /** @see Codec#setOutputFormat(Format) */
   public Format setOutputFormat(Format format) {
      for (int i = 0; i < FORMATS.length; i++) {
         if (FORMATS[i].matches(format)) {
            currentFormat = format;
            break;
         }
      }
      Logging.fine("MixEffect: current output format set to " + currentFormat);
      return currentFormat;
   }

   /** @see Codec#process(Buffer, Buffer) */
   public int process(Buffer inputBuffer, Buffer outputBuffer) {
      // TODO Implement method
      // use input.getTimeStamp() to obtain the buffers position
      // add information from the tracks at this position up to the duration
      // input.getLength().
      System.out.println("processing(ing)");

      this.currentData = (byte[]) inputBuffer.getData();

      outputBuffer.copy(inputBuffer);
      return BUFFER_PROCESSED_OK;
   }

   /** @see PlugIn#getName() */
   public String getName() {
      // TODO Implement method
      return effectName;
   }

   /** @see PlugIn#open() */
   public void open() throws ResourceUnavailableException {
      // TODO Implement method
      // see if sound is available, and if output on sound is possible
   }

   /** @see PlugIn#close() */
   public void close() {
      // TODO Implement method
   }

   /** @see PlugIn#reset() */
   public void reset() {
      // TODO Implement method
   }

   /** @see Controls#getControls() */
   public Object[] getControls() {
      // TODO Implement method
      return new Control[0];
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

   public byte[] currentData() {
      return currentData;
   }
}