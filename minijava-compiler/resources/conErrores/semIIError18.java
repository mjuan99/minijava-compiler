//[Error:b|9]

class A{
    public A a;
    B b(){}
    A m1(){
        return C.a();
        return C.a().a;
        return C.a().b();
    }

    static void main(){}
}

class B{
    A a(){}
    public int c;
}

class C{
    static A a(){}
}