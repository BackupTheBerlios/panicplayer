// ----------------------------------------------------------------------------
// [b12] Java Source File: EffectConfiguration.java
//                created: 11.01.2004
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;

import b12.panik.io.UrlTrack;
import b12.panik.util.Logging;

/**
 * The properties for the effect.
 * @author kariem
 */
public class EffectConfiguration {

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
                    Logging.warning(Resources.getString("Configuration.error") + e.toString(), //$NON-NLS-1$
                            e);
                }
            }
        }
    }

    private void add(InputProperty prop) {
        if (effectTracks == null) {
            effectTracks = new HashMap();
        }
        effectTracks.put(prop.getAddress(), prop);
    }

    /** Clears all currently loaded tracks from this configuration. Urls remain. */
    public void clearTracks() {
        if (effectTracks != null) {
            for (Iterator i = effectTracks.values().iterator(); i.hasNext(); ) {
                InputProperty ip = (InputProperty) i.next();
                ip.removeTracks();
            }
        }
    }

    /**
     * Loads the configuration from a collection of <code>UrlTrack</code>s.
     * @param tracks a collection of tracks.
     */
    public void load(Collection tracks) {
        if (tracks != null) {
            for (Iterator i = tracks.iterator(); i.hasNext(); ) {
                add((UrlTrack) i.next());
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

    /**
     * Adds a track to this effect configuration.
     * @param track the track to add.
     */
    void add(UrlTrack track) {
        addTrack(track.getUri(), track.isEnabled(), track.getBegin());
    }

    /**
     * Adds a single track to the configuration.
     * @param uri the uri for the track.
     * @return the newly created InputProperty or an InputProperty that was
     *          associated to this uri prior to the call.
     */
    InputProperty addUri(URI uri) {
        if (effectTracks == null) {
            effectTracks = new HashMap();
        }
        InputProperty ip = (InputProperty) effectTracks.get(uri);
        if (ip == null) {
            ip = new InputProperty(uri, 0);
            effectTracks.put(uri, ip);
        }
        return ip;
    }
    
    void addTrack(URI uri, boolean enabled, long trackStart) {
        InputProperty ip = addUri(uri);
        ip.addTrack(enabled, trackStart);
    }

    /**
     * Returns the available URIs (for drag panel) as a collection.
     * @return the available URIs.
     */
    public Collection getURIs() {
        if (effectTracks == null) {
            return null;
        }
        return effectTracks.keySet();
    }
    
    /**
     * Returns the tracks (for drop panel).
     * @return the tracks.
     */
    public Collection getTracks() {
        if (effectTracks == null) {
            return null;
        }
        List tracks = new ArrayList();
        for (Iterator i = effectTracks.values().iterator(); i.hasNext(); ) {
            InputProperty ip = (InputProperty) i.next();
            if (ip.hasTracks()) {
                final UrlTrack[] urlTracks = ip.getUrlTracks();
                for (int j = 0; j < urlTracks.length; j++) {
                    tracks.add(urlTracks[j]);
                }
            }
        }
        return tracks;
    }
}