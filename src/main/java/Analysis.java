import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;

public class Analysis {

    public static void main(String[] args){
        String fn = "C:\\Users\\PC7\\Desktop\\Personal_Work\\Hizmetler\\May_Tohumculuk\\Corn_Heterotic_Groups\\Maize-001.xlsx";
        ArrayList<Maize> data = getGenotypes(fn);

        String fn2 = "C:\\Users\\PC7\\Desktop\\Personal_Work\\Hizmetler\\May_Tohumculuk\\Corn_Heterotic_Groups\\Biomarkers.txt";
        LinkedHashMap<String, ArrayList<String>> biomarkers = getBiomarkers(fn2);

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
        }
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

                ArrayList<Integer> pp = which(biomps, biom);
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
        ArrayList<String> list2 = new ArrayList<>();
        list2.addAll(list);

        int i;
        while((i = list2.indexOf(str))>=0){
            result.add(i);
            list2.remove(i);
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
            m.setResult(calculate(m.getGenotype(), biomarkers));
        }else{
            System.out.println("Some of the biomarkers are not present in the input file. The analysis failed.");
            m.setResult(null);
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

    public static ArrayList<Maize> getGenotypes(String filename){
        ArrayList<Maize> samples = new ArrayList<>();
        try{
            Workbook wb = WorkbookFactory.create(new File(filename));
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if(rowIterator.hasNext()){
                Row row = rowIterator.next(); // Header Row
                Iterator<Cell> cellIterator = row.cellIterator();

                int i;
                for(i=0;(i<3 && cellIterator.hasNext());i++){
                    Cell cell = cellIterator.next(); // Headers 'Name', 'Chr' and 'Position'
                }

                if(i<3){
                    System.out.println("Input file you specified is not appropriate!");
                }

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    samples.add(new Maize(cell.getStringCellValue()));
                }
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next(); // Probe set Rows
                Iterator<Cell> cellIterator = row.cellIterator();

                String ps = cellIterator.next().getStringCellValue(); // Probeset Name
                DataFormatter formatter = new DataFormatter();
                String chr = formatter.formatCellValue(cellIterator.next()); // Chromosome
                String pos = formatter.formatCellValue(cellIterator.next()); // Position

                int s = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = cell.getStringCellValue();
                    Maize m = samples.get(s++);
                    m.getGenotype().put(ps,cellValue);
                }
                wb.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return samples;
    }

    public static LinkedHashMap<String, ArrayList<String>> getBiomarkers(String filename){
        LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
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
            ex.printStackTrace();
        }
        return(result);
    }
}
