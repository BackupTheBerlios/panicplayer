// ----------------------------------------------------------------------------
// [b12] Java Source File: EffectConfiguration.java
//                created: 11.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.net.*;
import java.util.*;

import b12.panik.util.Logging;

/**
 * The properties for the effect.
 * @author kariem
 */
public class EffectConfiguration {

    private static final String TAG_VOICE = "voice"; //$NON-NLS-1$

    List effectTracks;
    Map effectProps;

    /** Creates a new instance of <code>EffectConfiguration</code>. */
    public EffectConfiguration() {
        effectProps = new HashMap();
    }
    
    void load (ParsedObject po) {
        effectProps.putAll(po.getAttributes());        
        if (po.hasChildren()) {
            final List children = po.getChildren();
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                try {
                    final ParsedObject child = (ParsedObject) i.next();
                    if (child.getName().equals(TAG_VOICE)) {
                        add(new InputProperty(child));
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
        for (Iterator i = effectProps.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            po.addAttribute((String) entry.getKey(), (String) entry.getValue());
        }
        // add voices
        for (Iterator i = effectTracks.iterator(); i.hasNext(); ) {
            InputProperty ip = (InputProperty) i.next();
            ip.fillParsedObject(po.getEmptyChild(TAG_VOICE));
        }
    }

    void add(InputProperty prop) {
        if (effectTracks == null) {
            effectTracks = new ArrayList();
        }
        effectTracks.add(prop);
    }

    void addTrack(URL url) throws ConfigurationException {
        try {
            URI uri = new URI(url.toString());
            addTrack(uri);
        } catch (URISyntaxException e) {
            throw new ConfigurationException(e);
        }
    }

    void addTrack(URI uri) {
        add(new InputProperty(uri, 0));
    }
}
