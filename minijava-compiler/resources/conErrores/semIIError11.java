//[Error:d|7]

class A{
    public A a;
    B b(){}
    int m1(){
        return a.a.b().c().d;
    }

    static void main(){}
}

class B{
    A a(){}
    int c(){}
}