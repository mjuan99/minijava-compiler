//[Error:return|13]

class A{
    public A a;
    B b(){return new B();}
    A m1(){
        return C.a();
    }
    A m2(){
        return C.a().a;
    }
    A m3(){
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