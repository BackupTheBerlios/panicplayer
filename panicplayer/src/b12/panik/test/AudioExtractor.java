// ----------------------------------------------------------------------------
// [b12] Java Source File: AudioExtractor.java
//                created: 20.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------

package b12.panik.test;

import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;

public class AudioExtractor implements ControllerListener, DataSinkListener {
    Processor p = null;
    DataSink ds = null;
    String inputFile, outputFile;
    public static void main(String[] arg) {
        AudioExtractor conv = new AudioExtractor();
        conv.init(arg[0], arg[1]);
    }
    public void init(String in, String out) {
        this.inputFile = in;
        this.outputFile = out;
        try {
            p = Manager.createProcessor(new MediaLocator(inputFile));
            p.addControllerListener(this);
            p.configure();
        } catch (Exception e) {
        }
    }
    public synchronized void dataSinkUpdate(DataSinkEvent event) {
        if (event instanceof DataSinkErrorEvent) {
            System.out.println(
                "Data sink exception: "
                    + ((DataSinkErrorEvent) event).toString());
        }
    }
    public synchronized void controllerUpdate(ControllerEvent event) {
        if (event instanceof ConfigureCompleteEvent) {
            AudioFormat outFormat = new AudioFormat(AudioFormat.MPEG);
            TrackControl[] tracks = p.getTrackControls();
            boolean found = false;
            for (int i = 0; i < tracks.length; i++) {
                Format trackFormat = tracks[i].getFormat();
                if (!found && trackFormat instanceof AudioFormat) {
                    Codec[] audioConversion =
                        new Codec[] {
                             new com.sun.media.codec.audio.mpa.NativeEncoder()};
                    try {
                        tracks[i].setCodecChain(audioConversion);
                        tracks[i].setFormat(outFormat);
                        p.setContentDescriptor(
                            new ContentDescriptor("audio.mpeg"));
                    } catch (Exception e) {
                    }
                    found = true;
                } else
                    tracks[i].setEnabled(false);
            }
            if (found)
                p.realize();
        } else if (event instanceof RealizeCompleteEvent) {
            try {
                ds =
                    Manager.createDataSink(
                        p.getDataOutput(),
                        new MediaLocator(outputFile));
                ds.addDataSinkListener(this);
                ds.open();
                ds.start();
                p.start();
            } catch (Exception e) {
            }
        } else if (event instanceof EndOfMediaEvent) {
            p.stop();
            p.deallocate();
            try {
                ds.stop();
            } catch (Exception e) {
            }
            ds.close();
        }
    }
}