//[Error:m1|4]

class A{
    void m1(){
        var x = 7;
        return ;
        x = 3;
        if(x == 3)
            m1();
    }

    static void main(){}
}