// ----------------------------------------------------------------------------
// [MM1 - Massenpanik]
//       Java Source File: IFormat.java
//                  $Date: 2004/01/17 11:13:42 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import javax.media.format.AudioFormat;

/**
 * Default values for audio processing.
 * @author kariem
 */
interface IFormat {

    /** default sample rate */
    static final double SAMPLE_RATE = 11025;
    
    /** Audio formats used for input */
    static final AudioFormat[] FORMATS = new AudioFormat[]{
            new AudioFormat(AudioFormat.LINEAR, SAMPLE_RATE, 8, 1, AudioFormat.BIG_ENDIAN,
                    AudioFormat.UNSIGNED)}/*,
     new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1, AudioFormat.BIG_ENDIAN,
     AudioFormat.UNSIGNED)}*/;}
