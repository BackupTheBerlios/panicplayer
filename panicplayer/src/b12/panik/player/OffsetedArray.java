// ----------------------------------------------------------------------------
// [b12] Java Source File: OffsetedArray.java
//                created: 09.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import javax.media.Buffer;

/**
 * 
 * @author kariem
 */
public class OffsetedArray {
    private long startTime;
    private long duration;
    private byte[] content;
    
    public OffsetedArray(Buffer buffer,long begin) {
        
    }
    
    public OffsetedArray(OffsetedArray source,long begin,long end) {
        
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getDuration() {
        return duration;
    }
    
    
    
    
}
