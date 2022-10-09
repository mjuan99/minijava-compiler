//[Error:m1|10]

class A extends B{
    public int a1;
    private boolean a2;
    int m1(Object a, A b){
        m1(new A(), b);
        m2(7 * 5 + a1, a2 && true, b);
        m3("asd", a3, new B());
        m1(new A(), new B());
    }
    static void main(){}
}

class B extends C{
    char m2(int a, boolean b, B c){}
}

class C{
    public char a3;
    int m3(String a, char b, C c){}
    int m4(){}
}