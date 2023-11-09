# springboot1_23-24

Preguntas API usuario-producto:

2. Comprobar si un producto existe antes de crearlo. Si existe devolver un error de producto existente. Esto impide tener un mismo producto con varios ID. ¿Sería deseable tener un producto repetido? Si es así, ¿como lo solucionarías?

    Si, en el supuesto caso de rebajar el precio de un producto, este puede duplicarse aplicandole el nuevo precio y una vez acabe la oferta eliminarlo. Podria solucionarse creando el nuevo producto con un id similar siguiendo el siguiente patron: para un duplicado del producto 1 podria crearse con el id 101 con el mismo nombre pero diferente precio.

<br>

4. DTO. Estudiar si sería deseable usar DTO en el caso de tener que añadir Mappings para la relación entre usuario y producto. Razones para usar y para no usar DTO.

    Si, es deseable usar el DTO, de esta forma se puede controlar mejor lo que se muestra en las peticiones como por ejemplo las iteraciones al mostar los productos y que no se cree un bucle al relacionarlo con los usuarios.

<br>

- Hecho por Víctor Jiménez Corada 2º DAW
