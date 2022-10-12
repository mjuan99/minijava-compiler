//[Error:<=|8]

class A{
    public int a;
    public int b;
    boolean m1(){
        return a > 5 && (b < 4) || (a >= b) && (7 <= 10);
        return a > 5 && (b < 4) || (a >= b) && ('7' <= 10);
    }

    static void main(){}
}