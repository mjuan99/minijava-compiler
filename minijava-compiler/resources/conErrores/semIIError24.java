//[Error:!=|11]

class A{
    public A a;
    public B b;
    public Object o;
    boolean m1(){
        return a != o && (a == b);
    }
    boolean m2(){
        return a != "asd" && (a == b);
    }

    static void main(){}
}

class B extends A{}