package de.eliteschw31n.octoprint.commands;

import de.eliteschw31n.octoprint.OctoPrintInstance;
import de.eliteschw31n.octoprint.util.OctoPrintHttpRequest;

/**
 * Command classes (REST endpoints) extend this class to provide functionality to the library
 *
 * @author rweber
 */
public abstract class OctoPrintCommand {
    protected OctoPrintInstance g_comm = null;
    protected String g_base = null;

    public OctoPrintCommand(OctoPrintInstance requestor, String base) {
        g_comm = requestor;
        g_base = base;
    }

    protected OctoPrintHttpRequest createRequest() {
        return this.createRequest("");
    }

    protected OctoPrintHttpRequest createRequest(String loc) {

        String urlPath = g_base;

        if (loc != null && !loc.isEmpty()) {
            urlPath = urlPath + "/" + loc;
        }

        OctoPrintHttpRequest request = new OctoPrintHttpRequest(urlPath);

        return request;
    }
}
