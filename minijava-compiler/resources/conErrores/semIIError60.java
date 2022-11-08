//[Error:super|5]

class A extends B{
    static void m1(){
        super.m2();
    }

    static void main(){}
}

class B{
    void m2(){}
}