//[Error:c|7]

class A{
    A a(){return new A();}
    B b(){return new B();}
    int m1(){
        return a().b().a().c().b();
    }

    static void main(){}
}

class B{
    A a(){return new A();}
    int c(){return 1;}
}