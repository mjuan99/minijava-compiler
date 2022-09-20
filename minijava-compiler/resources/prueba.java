class MiClase1 extends MiClase2{}
class MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
class MiClase3 extends MiClase2 implements MiInterfaz1, MiInterfaz2, MiInterfaz3{}
interface MiInterfaz1{}
interface MiInterfaz2 extends MiInterfaz1, MiInterfaz2, MiInterfaz3{}