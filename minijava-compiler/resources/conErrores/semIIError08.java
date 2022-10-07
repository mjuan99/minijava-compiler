//[Error:c|8]

class A{
    public A a1;
    A a(){}
    B b(){}
    int m1(){
        return a1.b().a().c().b();
    }

    static void main(){}
}

class B{
    A a(){}
    int c(){}
}