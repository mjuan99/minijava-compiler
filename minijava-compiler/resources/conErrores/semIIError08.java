//[Error:c|8]

class A{
    public A a1;
    A a(){return new A();}
    B b(){return new B();}
    int m1(){
        return a1.b().a().c().b();
    }

    static void main(){}
}

class B{
    A a(){return new A();}
    int c(){return 1;}
}