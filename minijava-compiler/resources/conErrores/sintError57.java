//[Error:MiInterfaz2|16]
//prueba multideteccion declaracion de clases e interfaces

MiClase {}
class miClase {}
interface miInterfaz{}
class MiClase MiClase2 {} // ESTO DA ERRORES RAROS POR LOS SIGUIENTES
interface MiInterfaz MiInterfaz2 {}
class MiClase extends miClase2 {}
class MiClase extends MiClase2, MiClase3 {}
class MiClase extends MiClase2 MiClase3 {}
class MiClase extends MiClase2 implements miInterfaz {}
interface MiInterfaz extends miInterfaz2 {}
class MiClase implements MiInterfaz1, miInterfaz2 {}
interface MiInterfaz extends MiInterfaz2, miInterfaz3 {}
class MiClase implements MiInterfaz1 MiInterfaz2 {}
interface MiInterfaz extends MiInterfaz1 MiInterfaz2 {}
class MiClase
class MiClase {}
interface MiInterfaz
interface MiInterfaz {}