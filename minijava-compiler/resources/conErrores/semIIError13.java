//[Error:d|7]

class A{
    public A a;
    B b(){}
    int m1(int a){
        return this.a.b().c.d;
    }

    static void main(){}
}

class B{
    A a(){}
    public int c;
}