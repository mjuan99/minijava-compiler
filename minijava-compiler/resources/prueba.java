class A{
    public A a;
    public B b;
    int m1(){
        return a.c.b.c;
    }

    static void main(){}
}

class B{
    public int c;
}