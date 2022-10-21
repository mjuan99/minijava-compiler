//[Error:intPrivA|14]

class A{
    private int intPrivA;
    public int intPubA;

    static void main(){}
}

class B extends A{
    private int intPrivB;
    public int intPubB;
    void m1(){
        this.intPubA = this.intPrivB + this.intPubB * this.intPrivA;
    }
}