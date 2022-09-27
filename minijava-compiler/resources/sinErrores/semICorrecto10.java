class A extends String{
    private System a1;
    public Object a2;
    A a3;
    public int a4;
    private B a5;

    int m1(){}
    static void m2(String a){}
    A m5(char a, B b, C c, int d, Object e){}

    A(A a, B b, C c){}
    A(){}
}

interface B{
    int m1(B a);
    B m2(A a);
    void m3();
}

class C extends A implements B{
    public char a1;
    public Object a2;

    int m1(B aa){}
    B m2(A a){}
    static void m2(String x){}
    void m3(){}
    Object m4(){}
    A m5(char a, B b, C c, int d, Object e){}

    static void main(){}

    C(Object o, C c){}
}