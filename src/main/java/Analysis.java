import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class Analysis {
    static MainPanel mp;
    public static void main(String[] args) throws Exception{
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

        Connection c = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");

        System.out.println("Connection succesful");

        Statement st = c.createStatement();
        String com = "CREATE TABLE IF NOT EXISTS `Projects` (" +
                "`serialized_id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "`object_name` varchar(20) default NULL, " +
                "`file_name` varchar(20) default NULL, " +
                "`project_date` varchar(20) default NULL, " +
                "`number_of_samples` INTEGER default NULL, " +
                "`serialized_object` blob" +
                ");";

        //st.executeUpdate("DROP TABLE Projects;");
        st.executeUpdate(com);

        c.close();

        JFrame jf = new JFrame();
        mp = new MainPanel();

        InputStream is = Analysis.class.getClassLoader().getResourceAsStream("may-logo.png");
        ImageIcon img = new ImageIcon(ImageIO.read(is));
        Image im = img.getImage();
        img.setImage(im);
        //Image im2 = im.getScaledInstance(im.getWidth(null)/10, im.getHeight(null)/10, Image.SCALE_SMOOTH);
        Image im2 = im.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        img.setImage(im2);
        mp.getLogoLabel().setIcon(img);

        jf.add(mp.getMainPanel());
        jf.setSize(1000,800);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setVisible(true);


        /*inputFile = "C:\\Users\\PC7\\Desktop\\Personal_Work\\Hizmetler\\May_Tohumculuk\\Corn_Heterotic_Groups\\Maize-001.xlsx";
        ArrayList<Maize> data = getGenotypes(inputFile);

        biomarkerFile = "Biomarkers.txt";
        LinkedHashMap<String, ArrayList<String>> biomarkers = getBiomarkers(biomarkerFile);

        for(Maize m : data){
            analyze(m,biomarkers);
        }

        for(Maize m : data){

            Set<String> ks = m.getResult().keySet();
            ArrayList<String> ks2 = new ArrayList<>();
            ks2.addAll(ks);
            String s = ks2.get(0) + ": " + Math.round(10000*m.getResult().get(ks2.get(0)))/100.0+"%";
            for(int i=1;i<ks2.size();i++){
                s += " "+ks2.get(i) +": "+ Math.round(10000*m.getResult().get(ks2.get(i)))/100.0+"%";
            }
            System.out.println(m.getName()+": "+s);
        }*/
    }

    public static LinkedHashMap<String, Double> calculate(LinkedHashMap<String,String> genotypes,LinkedHashMap<String,ArrayList<String>> biomarkers){
        LinkedHashMap<String, Double> results = new LinkedHashMap<>();

        int unk = 0;
        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Double> oscores = new ArrayList<>();

        Set<String> biomset = biomarkers.keySet();
        ArrayList<String> biomps = new ArrayList<>();
        biomps.addAll(biomset);

        ArrayList<String> classes = new ArrayList<>();

        for(String s : biomps){
            classes.add(biomarkers.get(s).get(0));
        }

        ArrayList<String> pops = unique(classes);

        for(int j=0;j<pops.size();j++){
            ArrayList<Integer> pos = which(classes, pops.get(j));
            int sc = 0;
            int sco = 0;
            for(int k=0;k<pos.size();k++){
                String biom = biomps.get(pos.get(k));
                String ee = biomarkers.get(biom).get(1);
                String oo = biomarkers.get(biom).get(2);

                ArrayList<String> geno = new ArrayList<>(Arrays.asList(genotypes.get(biom).split("")));

                if(geno.get(0).equals(ee)){
                    sc = sc+1;
                }else if(geno.get(0).equals(oo)){
                    sco = sco+1;
                }else{
                    unk = unk+1;
                }

                if(geno.get(1).equals(ee)){
                    sc = sc+1;
                }else if(geno.get(1).equals(oo)){
                    sco = sco+1;
                }else{
                    unk = unk+1;
                }
            }

            scores.add(1.0*sc/(2*pos.size()));
            oscores.add(1.0*sco/(2*pos.size()));
        }
        ArrayList<Double> scores2 = new ArrayList<>();
        scores2.addAll(scores);

        for(int j=0;j<scores.size();j++){
            ArrayList<Double> sr = new ArrayList<>();
            sr.addAll(scores);
            sr.remove(j);

            ArrayList<Double> mult = new ArrayList<>();
            double srsum = doubleSum(sr);
            if(srsum!=0){
                mult = doubleArrayDivide(sr,srsum);
                int multcount = 0;
                for(int h=0;h<scores2.size();h++){
                    if(h!=j){
                        scores2.set(h,scores2.get(h)+mult.get(multcount++)*oscores.get(j));
                    }
                }
            }else{
                for(int h=0;h<sr.size();h++){
                    sr.set(h,sr.get(h)+1);
                }
                srsum = doubleSum(sr);
                mult = doubleArrayDivide(sr,srsum);
                int multcount = 0;
                for(int h=0;h<scores2.size();h++){
                    if(h!=j){
                        scores2.set(h,scores2.get(h)+mult.get(multcount++)*oscores.get(j));
                    }
                }
            }
        }

        scores2.add(1.0*unk/(2*biomps.size()));
        ArrayList<Double> scores3 = doubleArrayDivide(scores2,doubleSum(scores2));
        pops.add("Unknown");
        for(int i=0;i<scores3.size();i++){
            results.put(pops.get(i),scores3.get(i));
        }

        return results;
    }

    public static LinkedHashMap<String, Double> calculateLine(LinkedHashMap<String,String> genotypes,LinkedHashMap<String, ArrayList<String>> biomarkers){
        LinkedHashMap<String, Double> results = new LinkedHashMap<>();

        int unk = 0;
        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Double> oscores = new ArrayList<>();

        Set<String> biomset = biomarkers.keySet();
        ArrayList<String> biomps = new ArrayList<>();
        biomps.addAll(biomset);

        ArrayList<String> classes = new ArrayList<>();

        for(String s : biomps){
            classes.add(biomarkers.get(s).get(0));
        }

        ArrayList<String> pops = unique(classes);

        for(int j=0;j<pops.size();j++){
            ArrayList<Integer> pos = which(classes, pops.get(j));
            int sc = 0;
            int sco = 0;
            for(int k=0;k<pos.size();k++){
                String biom = biomps.get(pos.get(k));
                String ee = biomarkers.get(biom).get(1);
                String oo = biomarkers.get(biom).get(2);

                String geno = genotypes.get(biom);
                if(geno.equals(ee)){
                    sc = sc+1;
                }else if(geno.equals(oo)){
                    sco = sco+1;
                }else{
                    unk = unk+1;
                }
            }

            scores.add(1.0*sc/(pos.size()));
            oscores.add(1.0*sco/(pos.size()));
        }

        ArrayList<Double> scores2 = new ArrayList<>();
        scores2.addAll(scores);

        for(int j=0;j<scores.size();j++){
            ArrayList<Double> sr = new ArrayList<>();
            sr.addAll(scores);
            sr.remove(j);

            ArrayList<Double> mult = new ArrayList<>();
            double srsum = doubleSum(sr);
            if(srsum!=0){
                mult = doubleArrayDivide(sr,srsum);
                int multcount = 0;
                for(int h=0;h<scores2.size();h++){
                    if(h!=j){
                        scores2.set(h,scores2.get(h)+mult.get(multcount++)*oscores.get(j));
                    }
                }
            }else{
                for(int h=0;h<sr.size();h++){
                    sr.set(h,sr.get(h)+1);
                }
                srsum = doubleSum(sr);
                mult = doubleArrayDivide(sr,srsum);
                int multcount = 0;
                for(int h=0;h<scores2.size();h++){
                    if(h!=j){
                        scores2.set(h,scores2.get(h)+mult.get(multcount++)*oscores.get(j));
                    }
                }
            }
        }

        scores2.add(1.0*unk/(biomps.size()));
        ArrayList<Double> scores3 = doubleArrayDivide(scores2,doubleSum(scores2));
        pops.add("Unknown");
        for(int i=0;i<scores3.size();i++){
            results.put(pops.get(i),scores3.get(i));
        }

        return results;
    }

    public static ArrayList<Double> doubleArraySum(ArrayList<Double> a1, ArrayList<Double> a2){
        ArrayList<Double> result = new ArrayList<>();

        for(int i=0;i<a1.size();i++){
            result.add(a1.get(i)+a2.get(i));
        }

        return result;
    }

    public static ArrayList<Double> doubleArrayMultiply(ArrayList<Double> arr, double x){
        ArrayList<Double> result = new ArrayList<>();

        for(double xx : arr){
            result.add(xx*x);
        }

        return result;
    }

    public static ArrayList<Double> doubleArrayDivide(ArrayList<Double> arr, double x){
        ArrayList<Double> result = new ArrayList<>();

        for(double xx : arr){
            result.add(xx/x);
        }

        return result;
    }

    public static double doubleSum(ArrayList<Double> sc){
        double x = 0;

        for(double xx : sc){
            x += xx;
        }

        return x;
    }

    public static ArrayList<Integer> which(ArrayList<String> list, String str){
        ArrayList<Integer> result = new ArrayList<>();

        for(int k=0;k<list.size();k++){
            if(list.get(k).equals(str)){
                result.add(k);
            }
        }

        return result;
    }

    public static ArrayList<String> unique(ArrayList<String> x){
        ArrayList<String> result = new ArrayList<>();

        for(String s : x){
            int ind = result.indexOf(s);
            if(ind<0){
                result.add(s);
            }
        }
        return result;
    }

    public static void analyze(Maize m, LinkedHashMap<String, ArrayList<String>> biomarkers){
        //Check if all the biomarkers are present in the input file.
        boolean check = checkBiomarkerAvailability(m.getGenotype(),biomarkers);

        if(check){
            LinkedHashMap<String, Double> res = calculate(m.getGenotype(), biomarkers);
            m.setResult(res);

            ArrayList<String> keys = new ArrayList<>(res.keySet());
            for(String s: keys){
                if(res.get(s)>0 && !s.equals("Unknown")){
                    analyzeLines(m,s);
                }
            }
        }else{
            System.out.println("Some of the biomarkers are not present in the input file. The analysis failed.");
            m.setResult(null);
        }
    }

    public static void analyzeLines(Maize m, String hetgroup){
        LinkedHashMap<String, ArrayList<String>> biomarkers = getBiomarkers(hetgroup+"_Lines_Biomarkers.txt");
        mp.getTp().setText(mp.getTp().getText()+"\n"+hetgroup+" hatları için biyobelirteçler okundu. Toplamda \n-- "+biomarkers.size()+" adet biyobelirtec tespit edildi.");

        //Check if all the biomarkers are present in the input file.
        boolean check = checkBiomarkerAvailability(m.getGenotype(),biomarkers);

        if(check){
            LinkedHashMap<String, Double> res = calculateLine(m.getGenotype(),biomarkers);
            m.getLineResults().put(hetgroup,res);
        }else{
            System.out.println("Some of the biomarkers are not present in the input file. The analysis failed.");
            mp.getTp().setText(mp.getTp().getText()+"\n"+"Örneklerin genotip dosyasında "+hetgroup+" heterotik grubunun hatlarına ait bazı biyobelirteçler için bilgi bulunmadığından dolayı analiz yapılamamıştır");
            m.getLineResults().put(hetgroup,null);
        }
    }

    public static boolean checkBiomarkerAvailability(LinkedHashMap<String,String> genotypes,LinkedHashMap<String,ArrayList<String>> biomarkers){
        boolean check = true;

        Set<String> genokeys0 = genotypes.keySet();
        Set<String> biomkeys0 = biomarkers.keySet();

        ArrayList<String> genokeys = new ArrayList<>();
        genokeys.addAll(genokeys0);

        ArrayList<String> biomkeys = new ArrayList<>();
        biomkeys.addAll(biomkeys0);

        check = compareStrings(genokeys,biomkeys);

        return check;
    }

    public static boolean compareStrings(ArrayList<String> list1, ArrayList<String> list2){
        boolean check = true;

        for(String s : list2){
            int ind = list1.indexOf(s);
            if(ind < 0){
                return(false);
            }
        }

        return check;
    }

    public static LinkedHashMap<String, ArrayList<String>> getBiomarkers(String filename){
        LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
        try{
            InputStream is = MainPanel.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = br.readLine(); //Header
            while((s=br.readLine())!=null){

                String[] ss = s.split("\t");
                String bm = ss[0]; //Biomarker probeset
                String lab = ss[1]; // Population
                String ee = ss[2]; // Effect Allele
                String oo = ss[3]; // Other Allele

                ArrayList<String> vals = new ArrayList<>();
                vals.add(lab);
                vals.add(ee);
                vals.add(oo);

                result.put(bm,vals);
            }
            return result;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Biyobelirteç dosyasındaki bir heterotik grup için hat biyobelirteç dosyası bulunmamaktadır.");
            mp.getTp().setText(mp.getTp().getText()+"\n"+"Biyobelirteç dosyasındaki bir heterotik grup için hat biyobelirteç dosyası bulunmamaktadır.");
            ex.printStackTrace();
        }
        return(result);
    }
}
