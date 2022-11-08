//prueba de expresiones

class A{
    public int a;
    public boolean b;
    public B c;

    void m1(){
        a = (5 + -3 * +2) / (a % 7);
        b = !b || (a >= 5) && (a != 3 || (new B() == c) && (new B() == new C()));
        b = c != null;
        c = (((new B()).b));
    }

    static void main(){}
}

class B{
    public B b;
}

class C extends B{}