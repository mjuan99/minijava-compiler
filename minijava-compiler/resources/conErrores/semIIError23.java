//[Error:5|9]

class A{
    public A a;
    public A b;
    public Object o;
    boolean m1(){
        return a != o && (a == b);
        return a != 5 && (a == b);
    }

    static void main(){}
}