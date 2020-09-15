package de.eliteschw31n.printercordinstance;

import de.eliteschw31n.octoprint.OctoPrintInstance;

public class PrinterCordInstance {
    private final long ownerId;
    private final OctoPrintInstance octoPrintInstance;
    private String webCam;

    public PrinterCordInstance(final OctoPrintInstance octoPrintInstance, final String webCam, final long ownerId) {
        this.octoPrintInstance = octoPrintInstance;
        this.webCam = webCam;
        this.ownerId = ownerId;
    }

    public OctoPrintInstance getOctoPrintInstance() {
        return octoPrintInstance;
    }

    public String getWebCam() {
        return webCam;
    }

    public void setWebcam(String webCam) {
        this.webCam = webCam;
    }

    public long getOwnerId() {
        return ownerId;
    }

}
