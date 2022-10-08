//[Error:null|7]

class A{
    public boolean a;
    boolean m1(){
        return true || (a && false);
        return true || (null && false);
    }

    static void main(){}
}