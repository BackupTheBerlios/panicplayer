// ----------------------------------------------------------------------------
// [b12] Java Source File: EffectConfiguration.java
//                created: 11.01.2004
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import javax.media.Track;

import b12.panik.io.TrackPropertyAdapter;
import b12.panik.util.Logging;

/**
 * The properties for the effect.
 * @author kariem
 */
public class EffectConfiguration extends TrackPropertyAdapter {

    private static final String TAG_VOICE = "voice"; //$NON-NLS-1$

    Map effectTracks;
    final Map effectProps;

    /** Creates a new instance of <code>EffectConfiguration</code>. */
    public EffectConfiguration() {
        effectProps = new HashMap();
    }

    /** Resets this configuration to an initial state. */
    public void reset() {
        effectProps.clear();
        if (effectTracks != null) {
            effectTracks.clear();
        }
    }        
    
    void load(ParsedObject po) {
        effectProps.putAll(po.getAttributes());
        if (po.hasChildren()) {
            final List children = po.getChildren();
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                try {
                    final ParsedObject child = (ParsedObject) i.next();
                    if (child.getName().equals(TAG_VOICE)) {
                        add(InputProperty.parseInputProperty(child));
                    }
                } catch (MalformedURLException e) {
                    Logging.warning(Resources.getString("Configuration.error") + e.toString()); //$NON-NLS-1$
                }
            }
        }
    }

    /**
     * Saves this effect configuration to the given parsed object.
     * @param po the destination for the save.
     */
    void saveTo(ParsedObject po) {
        // save properties directly as attributes to po
        for (Iterator i = effectProps.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            po.addAttribute((String) entry.getKey(), (String) entry.getValue());
        }

        if (effectTracks != null) {
            // add voices
            for (Iterator i = effectTracks.values().iterator(); i.hasNext(); ) {
                InputProperty ip = (InputProperty) i.next();
                ip.fillParsedObject(po.getEmptyChild(TAG_VOICE));
            }
        }
    }

    private void add(InputProperty prop) {
        if (effectTracks == null) {
            effectTracks = new HashMap();
        }
        effectTracks.put(prop.getAddress(), prop);
    }

    void addTrack(URI uri, long trackStart) {
        InputProperty ip = null;
        if (effectTracks != null) {
            ip = (InputProperty) effectTracks.get(uri);
            if (ip == null) {
                ip = new InputProperty(uri, trackStart);
            }
            ip.addTrack(true, trackStart);
        } else {
            effectTracks = new HashMap();
            ip = new InputProperty(uri, trackStart);
            effectTracks.put(uri, ip);
        }
    }

    /**
     * Returns the available tracks (for drag panel) as a collection.
     * @return the available tracks.
     */
    public Collection getAvailableTracks() {
        return effectTracks.keySet();
    }

    /** @see TrackPropertyAdapter#propertyChanged(Track, String, Object, Object) */
    public void propertyChanged(Track t, String propertyId, Object oldVal, Object newVal) {
        // TODO implement this to receive track updates.
        super.propertyChanged(t, propertyId, oldVal, newVal);
    }
}