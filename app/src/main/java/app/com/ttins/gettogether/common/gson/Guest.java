package app.com.ttins.gettogether.common.gson;



public class Guest {
    Long id;
    String note;

    public Guest(Long id, String note) {
        this.id = id;
        this.note = note;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return this.note;
    }

}
