//prueba super

class A extends B{
    public String a1;
    void m1(){
        super.a1 = 5;
        super.m2();
        var a = 3;
        super.a.a.a = new A();
    }

    static void main(){}
}

class B{
    public int a1;
    public A a;
    void m2(){}
}