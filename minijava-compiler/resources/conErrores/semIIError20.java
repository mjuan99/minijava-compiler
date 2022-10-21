//[Error:/|10]

class A{
    public int a;
    public int b;
    int m1(){
        return a + b * (3 / 5) % 8;
    }
    int m2(){
        return a + b * (3 / true) % 8;
    }

    static void main(){}
}