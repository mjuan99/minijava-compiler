class A{
    static void main(){
    }

    static void print(int i){
        var v1 = 5;
        var v2 = 7;
        System.printIln(i);
    }

    static int duplicar(int i){
        return i * 2;
    }

    int m1(){return 1;}
}

class B extends A{
    void m2(){
        if(1 > 3)
            if(3 > 2)
                return;
    }
    char m3(){return 'c';}
}

class C extends A{
    void m4(){}
    void m5(){}
}

class D extends C{
    void m6(){}
    void m7(){}
}