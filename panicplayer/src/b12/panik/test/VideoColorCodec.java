// ----------------------------------------------------------------------------
// [b12] Java Source File: VideoColorCodec.java
//                created: 20.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------

package b12.panik.test;

import java.awt.Dimension;

import javax.media.*;
import javax.media.format.RGBFormat;

public class VideoColorCodec implements Codec {
    private double percentRed = 0.5, percentGreen = 0.5, percentBlue = 0.5;
    protected RGBFormat[] supportedInputFormats =
        new RGBFormat[] { new RGBFormat()};
    protected RGBFormat[] supportedOutputFormats =
        new RGBFormat[] { new RGBFormat()};
    private RGBFormat inputFormat = supportedInputFormats[0],
        outputFormat = supportedOutputFormats[0];
    Object[] control = new Object[] { new VideoColorControl()};
    public Format[] getSupportedInputFormats() {
        return (supportedInputFormats);
    } // VEREINFACHT!!!
    public Format[] getSupportedOutputFormats(Format format) {
        if (format instanceof RGBFormat)
            return (supportedOutputFormats);
        else
            return (new Format[] {
        });
    }

    public Format setInputFormat(Format format) { // VEREINFACHT!!!
        if (format instanceof RGBFormat)
            inputFormat = (RGBFormat) format;
        return (inputFormat);
    }
    public Format setOutputFormat(Format format) { // VEREINFACHT!!!
        if (format instanceof RGBFormat)
            outputFormat = (RGBFormat) format;
        return (outputFormat);
    }
    public String getName() {
        return (new String("Video color manipulation codec."));
    }
    public void open() throws ResourceUnavailableException {
    }
    public void close() {
    }
    public void reset() {
    }
    public Object[] getControls() {
        return ((Object[]) control);
    }
    public Object getControl(String str) {
        if (str.equals("VideoColorControl"))
            return ((Object) control[0]);
        else
            return (null);
    }
    public int process(Buffer in, Buffer out) {
        int[] inFrame = (int[]) in.getData(),
            // VEREINFACHT: Prüfen mit Format.getDataType()!
    outFrame;

        RGBFormat inputFormat = (RGBFormat) in.getFormat();
        // könnte man gleich casten!
        out.copy(in);
        outFrame = (int[]) out.getData();
        Dimension frameSize = inputFormat.getSize();
        int gMask = inputFormat.getGreenMask(),
            bMask = inputFormat.getBlueMask(),
            rMask = inputFormat.getRedMask();

        int row, col, inPixel;
        for (row = 0; row < frameSize.height; row++)
            for (col = 0; col < frameSize.width; col++) {
                inPixel = inFrame[row * frameSize.width + col];
                // Annahme: basiert auf RGBFormat!
                outFrame[row * frameSize.width + col] =
                    (int) (((inPixel & gMask) * percentGreen)) & gMask;
                // VEREINFACHT: Überlauf!
            }
        return (BUFFER_PROCESSED_OK);
    }

    private class VideoColorControl {
        private final byte BIN_RED = 0;
		private final byte BIN_GREEN = 1;
		private final byte BIN_BLUE = 2;
		public java.awt.Component getControlComponent() {
            return (null);
        }
        public double getPercent(byte colorBin) {
            switch (colorBin) {
                case BIN_RED :
                    return (percentRed);
                case BIN_GREEN :
                    return (percentGreen);
                case BIN_BLUE :
                    return (percentBlue);
            }
            return (-1.0);
        }
        public void setPercent(byte colorBin, double value) {
            switch (colorBin) {
                case BIN_RED :
                    percentRed = value;
                case BIN_GREEN :
                    percentGreen = value;
                case BIN_BLUE :
                    percentBlue = value;
            }
        }
    }
}