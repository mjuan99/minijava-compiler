//[Error:=|12]

class A{
    public int a1;
    public char a2;
    public String a3;
    public A a4;

    int m1(){
        a1 = a2;
        a3 = a2;
        a3 = a4;
        a2 = a1;
    }

    static void main(){}
}