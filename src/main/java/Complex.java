public class Complex {
    private double real;
    private double imag;
    private double ureal;
    private double uimag;
    private double coef;
    private double x;

    public Complex(){
        this.real = 1;
        this.imag = 1;
        this.coef = Math.sqrt(Math.pow(this.real,2)+Math.pow(this.imag,2));
        this.ureal = this.real/coef;
        this.uimag = this.uimag/coef;
        this.x = Math.acos(this.ureal);
    }
    public Complex(double real, double imag){
        this.real = real;
        this.imag = imag;
        this.coef = Math.sqrt(Math.pow(this.real,2)+Math.pow(this.imag,2));
        this.ureal = this.real/coef;
        this.uimag = this.uimag/coef;
        this.x = Math.acos(this.ureal);
    }

    public Complex(double x){
        this.x = x;
        this.real = Math.cos(x);
        this.imag = Math.sin(x);
        this.coef = 1;
        this.ureal = real;
        this.uimag = imag;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
        this.coef = Math.sqrt(Math.pow(this.real,2)+Math.pow(this.imag,2));
        this.ureal = this.real/coef;
        this.uimag = this.uimag/coef;
        this.x = Math.acos(this.ureal);
    }

    public double getImag() {
        return imag;
    }

    public void setImag(double imag) {
        this.imag = imag;
        this.coef = Math.sqrt(Math.pow(this.real,2)+Math.pow(this.imag,2));
        this.ureal = this.real/coef;
        this.uimag = this.uimag/coef;
        this.x = Math.acos(this.ureal);
    }

    public double getUreal() {
        return ureal;
    }

    public void setUreal(double ureal) {
        this.ureal = ureal;
    }

    public double getUimag() {
        return uimag;
    }

    public double getCoef() {
        return coef;
    }

    public double getX() {
        return x;
    }

    public static Complex sum(Complex... c){
        double real = 0;
        double imag = 0;

        for(int i=0;i<c.length;i++){
            real += c[i].getReal();
            imag += c[i].getImag();
        }
        return(new Complex(real,imag));
    }

    public static Complex multiply(Complex... c){
        double real = 0;
        double imag = 0;

        if(c.length==1){
            return(c[0]);
        }else if(c.length==2){
            return(new Complex(c[0].getReal()*c[1].getReal()-(c[0].getImag()*c[1].getImag()),c[0].getReal()*c[1].getImag()+c[1].getReal()*c[0].getImag()));
        }else{
            Complex c2 = new Complex(c[0].getReal(),c[0].getImag());

            for(int i=1;i<c.length;i++){
                c2 = multiply(c2,c[i]);
            }
            return(c2);
        }
    }

    public void divide(double x){
        setReal(getReal()/x);
        setImag(getImag()/x);
    }

    public void multiply(double x){
        setReal(getReal()*x);
        setImag(getImag()*x);
    }

    public static Complex ln(Complex c){
        return(new Complex(Math.log(c.getCoef()),c.getX()));
    }

    public String toString(){
        return(this.real+" + "+this.imag+"i");
    }
}
