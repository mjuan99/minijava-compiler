//[Error:!|9]

class A{
    public int a1;
    public boolean a2;
    void m1(){
        a1 = -5;
        a2 = !(+3 == -3);
        a1 = !'c';
    }

    static void main(){}
}