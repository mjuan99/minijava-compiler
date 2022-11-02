class A{
    public int x;
    static void main(){
        var a = new B();
        a.m1(9);
        System.printIln(a.x);
        System.printIln(a.m0());
    }

    int m0(){
        return x;
    }

    void m1(int i){
        x = i;
    }
}

class B extends A{
    public int x;

    void m1(int i){
        x = i;
        super.x = i;
    }
}