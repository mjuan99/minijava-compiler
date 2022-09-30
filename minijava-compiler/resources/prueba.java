class A{
    private int a1;
    private String a2;
    private boolean a3; //TODO QUE PASA CUANDO HEREDAN ESTE
}

class B extends A{
    public int a1;
    public char a2;
}
interface I1{int m1();}
class C extends B implements I1{
    public int a1;
    void m1(){}
    static int main(){}
}