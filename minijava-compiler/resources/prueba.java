interface I1{
    int m1();
}

interface I2 extends I1{
    void m1();
}

class A implements I2/*, I1*/{
    void m1(){}
    static void main(){}
}