//[Error:a3|9]

class A{
    private int a1;
    private B a2;

    void m1(){
        this.a1 = a2.m2().a1;
        this.a1 = a2.a3;
    }

    static void main(){}
}

class B{
    private int a3;
    A m2(){}
}