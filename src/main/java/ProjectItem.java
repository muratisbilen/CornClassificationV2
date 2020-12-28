public class ProjectItem {
    private String projectName;
    private long serialized_id;

    public ProjectItem(String projectName, long serialized_id) {
        this.projectName = projectName;
        this.serialized_id = serialized_id;
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

    public String toString(){
        return projectName;
    }
}
