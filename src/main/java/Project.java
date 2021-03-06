import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Project implements Serializable {
    private String filename;
    private String projectName;
    private long serialized_id;
    private ArrayList<Maize> samples;
    private String projectDate;

    public Project(String filename, String projectName, ArrayList<Maize> samples) {
        this.filename = filename;
        this.projectName = projectName;
        this.samples = samples;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.projectDate = dtf.format(now);
    }

    public ArrayList<Maize> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Maize> samples) {
        this.samples = samples;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public long getSerialized_id() {
        return serialized_id;
    }

    public void setSerialized_id(long serialized_id) {
        this.serialized_id = serialized_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProjectDate() {
        return projectDate;
    }
}
