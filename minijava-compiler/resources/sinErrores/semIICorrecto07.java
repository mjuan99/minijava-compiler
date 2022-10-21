//prueba de encadenados y llamados a metodos

class A{
    public A aa;
    B mb(){}
    void m1(A pa){
        new A().aa.mb().ab.ma().mb();
        mb().ab.ma().aa.mb();
        (A.mb2()).ab.ma().aa.mb();
        this.aa.mb().ab.ma().mb();
        aa.aa.mb().ab.ma().mb();
        pa.aa.mb().ab.ma().mb();
        var va = new A();
        va.aa.mb().ab.ma().mb();
    }
    static B mb2(){}
    static void main(){}
}

class B{
    public B ab;
    A ma(){}
}