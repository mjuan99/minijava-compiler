//[Error:a1|12]

class A{
    A a(){}
    public B b;
    public A a1;
    A m1(){
        a();
        B.a();
        new A();
        B.a().a();
        new A().a().b.a().a1;
    }

    static void main(){}
}

class B{
    static A a(){}
}