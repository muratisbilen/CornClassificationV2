public class ProjectItem {
    private String projectName;
    private long serialized_id;
    private String date;
    private String filename;
    private int sampleNumber;

    public ProjectItem(String projectName, long serialized_id, String date, String filename, int sampleNumber) {
        this.projectName = projectName;
        this.serialized_id = serialized_id;
        this.date = date;
        this.filename = filename;
        this.sampleNumber = sampleNumber;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSampleNumber() {
        return sampleNumber;
    }

    public void setSampleNumber(int sampleNumber) {
        this.sampleNumber = sampleNumber;
    }

    public String toString(){
        return projectName;
    }
}
