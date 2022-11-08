//[Error:m3|7]

class A{
    public I a1;
    int m1(){
        a1.m2();
        a1.m3();
    }
    static void main(){}
}

interface I{
    int m2();
}