//[Error:b|8]

class A{
    public A a;
    B b(){}
    int m1(int a){
        return this.a.b().c;
        return a.b().c;
    }

    static void main(){}
}

class B{
    A a(){}
    public int c;
}