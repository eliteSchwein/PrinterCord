package de.eliteschw31n.octoprint.commands;

import de.eliteschw31n.octoprint.OctoPrintInstance;
import de.eliteschw31n.octoprint.model.ConnectionState;
import de.eliteschw31n.octoprint.util.JSONUtils;
import de.eliteschw31n.octoprint.util.OctoPrintHttpRequest;
import org.json.simple.JsonObject;

/**
 * Implementation of commands found under the Connection (http://docs.octoprint.org/en/master/api/connection.html) endpoint.
 *
 * @author rweber
 */
public class ConnectionCommand extends OctoPrintCommand {

    public ConnectionCommand(OctoPrintInstance requestor) {
        super(requestor, "connection");
    }

    private boolean connectionCommand(final String type) {
        OctoPrintHttpRequest request = this.createRequest();

        //set request type and command to send
        request.setType("POST");
        request.addParam("command", type);

        return g_comm.executeUpdate(request);
    }

    public ConnectionState getCurrentState() {
        ConnectionState result = null;

        JsonObject json = g_comm.executeQuery(this.createRequest());

        if (json != null && json.containsKey("current")) {
            result = JSONUtils.createObject((JsonObject) json.get("current"), ConnectionState.class.getName());
        }

        return result;
    }

    public boolean connect() {
        return this.connectionCommand("connect");
    }

    public boolean disconnect() {
        return this.connectionCommand("disconnect");
    }

}
