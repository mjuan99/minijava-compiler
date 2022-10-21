//[Error:return|9]

class A{
    public A a;
    B b(){return new B();}
    A m1(){
        return C.a();
        return C.a().a;
        return C.a().b();
    }

    static void main(){}
}

class B{
    A a(){return new A();}
    public int c;
}

class C{
    static A a(){return new A();}
}