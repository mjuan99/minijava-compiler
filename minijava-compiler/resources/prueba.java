//[Error:b|10]

class A{
    public A a;
    public B b;
    public C c;
    public Object o;
    boolean m1(){
        return a != o && (a == b);
        return c != b && (a == b);
    }

    static void main(){}
}

class B extends A{}

class C{}