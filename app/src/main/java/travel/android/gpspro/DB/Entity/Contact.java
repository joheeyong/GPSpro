package travel.android.gpspro.DB.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private int idd;
    private String profileURL;

    @Ignore
    public Contact() { }

    @Ignore
    public Contact(long id, String name, int idd, String profileURL) {
        this.idd = idd;
        this.id = id;
        this.name = name;
        this.profileURL = profileURL;
    }

    public Contact(String name, int idd, String profileURL) {
        this.idd = idd;
        this.name = name;
        this.profileURL = profileURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

//    public void setProfileURL(String profileURL) {
//        this.profileURL = profileURL;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdd() {
        return idd;
    }

    public void setIdd(int idd) {
        this.idd = idd;
    }
}
