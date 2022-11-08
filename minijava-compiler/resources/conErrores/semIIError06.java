//[Error:c|7]

class A{
    public A a;
    public B b;
    int m1(){
        return a.b.a.c.b;
    }

    static void main(){}
}

class B{
    public A a;
    public int c;
}