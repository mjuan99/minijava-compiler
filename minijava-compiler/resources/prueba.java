//size = 13
//1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13

class A{
    public String at1;
    static void main(){
        var a = 54321;
        var b = 56789;
        System.printIln(reverse(a));
        System.printIln(reverse(b));
    }

    static int reverse(int i){
        if(i < 10) {
            return i;
        }
        else {
            var i2reversed = reverse(i / 10);
            System.printS("i2reversed = ");
            System.printIln(i2reversed);
            var i2reversedTimes10 = i2reversed * 10;
            System.printS("i2reversedTimes10 = ");
            System.printIln(i2reversedTimes10);
            var lastDigit = i % 10;
            System.printS("lastDigit = ");
            System.printIln(lastDigit);
            var ireversed = i2reversedTimes10 + lastDigit;
            System.printS("ireversed = ");
            System.printIln(ireversed);
            return ireversed;
        }
    }
}