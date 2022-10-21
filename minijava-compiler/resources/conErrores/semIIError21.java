//[Error:&&|9]

class A{
    public boolean a;
    boolean m1(){
        return true || (a && false);
    }
    boolean m2(){
        return true || (null && false);
    }

    static void main(){}
}