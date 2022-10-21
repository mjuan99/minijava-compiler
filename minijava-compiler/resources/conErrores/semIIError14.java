//[Error:b|10]

class A{
    public A a;
    B b(){return new B();}
    int m1(int a){
        return this.a.b().c;
    }
    int m2(int a){
        return a.b().c;
    }

    static void main(){}
}

class B{
    A a(){}
    public int c;
}