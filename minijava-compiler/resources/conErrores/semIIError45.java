//[Error:m2|7]

class A extends B{
    private String a1;
    int m1(Object a, A b){
        B.m2(4 / 3 + 1, a1);
        B.m2(4 / 3 + 1, new A(), 1, true);
    }
    static void main(){}
}

class B{
    static void m2(int a, String b){}
}