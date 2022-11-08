//[Error:x|15]

class Main{
    static void main(){
    }
}

class A{
    private int x;
}

class B extends A{
    public int x;
    void m1(){
        x = super.x;
    }
}