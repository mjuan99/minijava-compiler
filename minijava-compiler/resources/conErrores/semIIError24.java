//[Error:!=|9]

class A{
    public A a;
    public B b;
    public Object o;
    boolean m1(){
        return a != o && (a == b);
        return a != "asd" && (a == b);
    }

    static void main(){}
}

class B extends A{}