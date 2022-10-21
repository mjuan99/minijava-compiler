//[Error:super|5]

class A extends B{
    void m1(){
        super = new B();
    }

    static void main(){}
}

class B{
    void m2(){}
}