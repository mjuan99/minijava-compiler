//[Error:a1|14]

class A{
    private int a1;
    public int a2;

    static void main(){}
}

class B extends A{
    private int a3;
    public int a4;
    void m1(){
        this.a2 = this.a3 + this.a4 * this.a1;
    }
}