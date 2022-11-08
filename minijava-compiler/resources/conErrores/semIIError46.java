//[Error:m2|7]

class A extends B{
    private B a1;
    int m1(Object a, A b){
        a1.m2(4 / 3 + 1, null);
        a1.m2(4 / 3 + 1, null, true, 'c');
    }
    static void main(){}
}

class B{
    void m2(int a, String b){}
}