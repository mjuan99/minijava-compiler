//prueba de herencia de interfaces diamante

interface MiInterfaz1 extends MiInterfaz2, MiInterfaz3{}

interface MiInterfaz2 extends MiInterfaz3{}

interface MiInterfaz3{}

interface MiInterfaz4 extends MiInterfaz5, MiInterfaz6{}

interface MiInterfaz5 extends MiInterfaz7{}

interface MiInterfaz6 extends MiInterfaz7{}

interface MiInterfaz7{}

class A{static void main(){}}