//[Error:a2|7]

class A{
    private I a1;
    int m1(){
        a1.m2();
        a1.a2;
    }
    static void main(){}
}

interface I{
    int m2();
}