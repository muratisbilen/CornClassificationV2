import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Maize implements Serializable {
    private String name;
    private LinkedHashMap<String, String> genotype;
    private LinkedHashMap<String, Double> result;
    private LinkedHashMap<String, LinkedHashMap<String, Double>> lineResults;

    public Maize(String name) {
        this.name = name;
        this.genotype = new LinkedHashMap<>();
        this.result = new LinkedHashMap<>();
        this.lineResults = new LinkedHashMap<>();
    }

    public Maize(String name, LinkedHashMap<String, String> genotype) {
        this.name = name;
        this.genotype = genotype;
        this.result = new LinkedHashMap<>();
        this.lineResults = new LinkedHashMap<>();
    }

    public Maize(String name, LinkedHashMap<String, String> genotype, LinkedHashMap<String, Double> result) {
        this.name = name;
        this.genotype = genotype;
        this.result = result;
        this.lineResults = new LinkedHashMap<>();
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

    public String toString(){
        return(this.name);
    }

    public LinkedHashMap<String, LinkedHashMap<String, Double>> getLineResults() {
        return lineResults;
    }

    public void setLineResults(LinkedHashMap<String, LinkedHashMap<String, Double>> lineResults) {
        this.lineResults = lineResults;
    }
}
