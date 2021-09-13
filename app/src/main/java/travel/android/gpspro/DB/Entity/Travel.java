package travel.android.gpspro.DB.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Travel_table")
public class Travel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String date;
    private Double star;

    public Travel(String title, String description, String date, Double star) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() { return  date; }

    public Double getStar() { return star; }
}
