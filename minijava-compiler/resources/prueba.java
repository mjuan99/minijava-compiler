//<digito-ingresado>
//<digito-ingresado>
//<digito-ingresado>

class A{
    public int at1;
    static void main(){
        var asciiDigitsOffset = 48;
        new A().init(System.read() - asciiDigitsOffset).m1().m1();
    }

    A init(int i){
        this.at1 = i;
        this.m1();
        return this;
    }

    A m1(){
        System.printIln(this.at1);
        return this;
    }
}