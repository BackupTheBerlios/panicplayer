/*
 * Created on 10 janv. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package b12.panik.io;

import java.net.URL;

/**
 * @author Teo
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UrlTrack {
	private URL url; //url des tracks
	private long begin; //begin in ns des tracks
	
	public UrlTrack(URL wantedUrl,long wantedBegin) {
		url=wantedUrl;
		begin=wantedBegin;
	}
}
