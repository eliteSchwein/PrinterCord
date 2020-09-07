package de.eliteschw31n.octoprint.model;

public enum FileType {
    MODEL, MACHINECODE, FOLDER;

    public static FileType findType(String t) {
        FileType result = null;

        if (t.equals(FileType.FOLDER.toString())) {
            result = FileType.FOLDER;
        } else if (t.equals(FileType.MACHINECODE.toString())) {
            result = FileType.MACHINECODE;
        } else {
            result = FileType.MODEL;
        }

        return result;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
