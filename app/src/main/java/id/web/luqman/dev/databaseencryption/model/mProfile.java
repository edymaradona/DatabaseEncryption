package id.web.luqman.dev.databaseencryption.model;

/**
 * Created by Seven7 on 3/5/2017.
 */

public class mProfile {
    private String id;
    private String first_name;
    private String last_name;

    public mProfile(String id, String first_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return getFirst_name() + " " + getLast_name();
    }
}
