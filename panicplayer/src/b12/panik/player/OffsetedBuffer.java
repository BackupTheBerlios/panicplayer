// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedBuffer.java
//                created: 09.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import javax.media.Buffer;

/**
 * 
 * @author kariem
 */
public class OffsetedBuffer extends Buffer {
    private Buffer contentBuffer;
    private long offset;
    
    public OffsetedBuffer(Buffer buffer,long offsetWanted) {
        contentBuffer=buffer;
        offset=offsetWanted;
    }
}
