//[Error:}|30]

class A{
    int m1(){
        var x = 7;
        while(x < 5)
            return 2;
        ;;
        m1();
        if(x != x)
            return 7;
        else{
            x += 3;
            x = x * x;
            return x;
        }
    }
    int m2(){
        var x = 7;
        while(x < 5)
            return 2;
        if(x != x)
            return 7;
        else{
            x += 3;
            x = x * x;
        }
        ;;
        m1();
    }

    static void main(){}
}