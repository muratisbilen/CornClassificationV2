import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    private String projectName;
    private long serialized_id;
    ArrayList<Maize> samples;

    public Project(){
        this.projectName = "";
        this.samples = new ArrayList<>();
    }

    public Project(String projectName){
        this.projectName = projectName;
        this.samples = new ArrayList<>();
    }

    public Project(String projectName, ArrayList<Maize> samples) {
        this.projectName = projectName;
        this.samples = samples;
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
}
