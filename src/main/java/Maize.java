import java.util.LinkedHashMap;

public class Maize {
    private String name;
    private LinkedHashMap<String, String> genotype;
    private LinkedHashMap<String, Double> result;

    public Maize(String name) {
        this.name = name;
        this.genotype = new LinkedHashMap<>();
        this.result = new LinkedHashMap<>();
    }

    public Maize(String name, LinkedHashMap<String, String> genotype) {
        this.name = name;
        this.genotype = genotype;
        this.result = new LinkedHashMap<>();
    }

    public Maize(String name, LinkedHashMap<String, String> genotype, LinkedHashMap<String, Double> result) {
        this.name = name;
        this.genotype = genotype;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, String> getGenotype() {
        return genotype;
    }

    public void setGenotype(LinkedHashMap<String, String> genotype) {
        this.genotype = genotype;
    }

    public LinkedHashMap<String, Double> getResult() {
        return result;
    }

    public void setResult(LinkedHashMap<String, Double> result) {
        this.result = result;
    }
}
