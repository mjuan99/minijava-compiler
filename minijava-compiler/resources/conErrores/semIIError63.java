//[Error:a2|8]

class A extends B{
    public int a1;
    void m1(){
        var a3 = "asd";
        a1 = super.a3;
        a1 = super.a2;
    }

    static void main(){}
}

class B{
    private int a2;
    public int a3;
    void m2(){}
}