class A{
    static void m1(){}
}

interface I{
    int read();
    void m1();
}

class B extends System implements I{
    void m1(){}
}

class C extends A implements I{
    int read(){}
}