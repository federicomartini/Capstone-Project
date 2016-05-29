package app.com.ttins.gettogether.common.gson;



public class Guest {
    Long id;
    String name;
    String photoPath;
    String note;

    public Guest() {}

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

}
