package de.eliteschw31n.guildinstance.util;

public class AdminRole {
    private final boolean role;
    private final long id;

    public AdminRole(final boolean role, final long id) {
        this.id = id;
        this.role = role;
    }

    public boolean isRole() {
        return role;
    }

    public long getId() {
        return id;
    }
}
