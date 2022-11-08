//[Error:intPrivB|9]

class A{
    private int intPrivA;
    private B bPrivA;

    void m1(){
        this.intPrivA = bPrivA.mA().intPrivA;
        this.intPrivA = bPrivA.intPrivB;
    }

    static void main(){}
}

class B{
    private int intPrivB;
    A mA(){}
}