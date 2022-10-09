//[Error:a3|11]

class A extends B implements I1{
    int m1(){
        var v1 = 1;
        v1 = a1;
        v1 = m1();
        var v2 = 'c';
        v2 = a2;
        v2 = m2();
        return a3;
    }
    static void main(){}
}

class B extends C{
    public char a2;
    private B a3;
    char m2(){}
}

class C{
    public int a1;
    private C a2;
    int m1(){}
}