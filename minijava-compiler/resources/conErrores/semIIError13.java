//[Error:d|7]

class A{
    public A a;
    B b(){return new B();}
    int m1(int a){
        return this.a.b().c.d;
    }

    static void main(){}
}

class B{
    A a(){return new A();}
    public int c;
}