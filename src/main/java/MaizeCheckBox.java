import javax.swing.*;
import java.util.ArrayList;

public class MaizeCheckBox extends JCheckBox {
    private Maize sample;

    public MaizeCheckBox(Maize sample){
        super(sample.getName());
        this.sample = sample;
    }

    public Maize getSample() {
        return sample;
    }

    public void setSample(Maize sample) {
        this.sample = sample;
        setText(sample.getName());
    }

    public String toString(){
        return(this.sample.getName());
    }
}
