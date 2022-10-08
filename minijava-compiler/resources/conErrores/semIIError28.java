//[Error:m1|11]

class A{
    public int a;
    int m1(){}
    void m2(int b){
        var c = 3;
        new A().a = 7 * 2;
        b = a - 7;
        c = new A().a * b + c;
        m1() = 7;
    }

    static void main(){}
}