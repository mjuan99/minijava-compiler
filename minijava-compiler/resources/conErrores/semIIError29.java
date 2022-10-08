//[Error:=|11]

class A{
    public int a;
    public A a1;
    int m1(){}
    A m3(){}
    void m2(int b){
        m3().a1.a = 7 * 2;
        a1.m3().a = a - 7;
        a1.m1() = 7;
    }

    static void main(){}
}