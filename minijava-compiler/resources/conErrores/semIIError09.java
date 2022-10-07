//[Error:c|7]

class A{
    A a(){}
    B b(){}
    int m1(){
        return a().b().a().c().b();
    }

    static void main(){}
}

class B{
    A a(){}
    int c(){}
}