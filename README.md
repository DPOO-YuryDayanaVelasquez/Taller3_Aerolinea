# Taller #3 - Aerolínea

Implementación del Taller #3 de DPOO - Aerolínea, aplicando conceptos de
Programación Orientada a Objetos, herencia, manejo de excepciones y
persistencia mediante archivos JSON.

## Decisión técnica

Durante las pruebas de persistencia se identificó una inconsistencia en el
skeleton proporcionado: al cargar los tiquetes desde JSON, estos se
reconstruían y asociaban correctamente con el cliente, pero no quedaban
registrados dentro del vuelo correspondiente.

Debido a que `Vuelo` no contaba originalmente con un método para registrar
un tiquete existente, se agregó `registrarTiquete(Tiquete tiquete)` y se
utilizó durante la carga de persistencia.

Esta modificación permite reconstruir correctamente las relaciones entre
vuelos, clientes y tiquetes, sin alterar el comportamiento de la venta
normal de tiquetes.

La solución fue validada mediante un round-trip de persistencia en procesos
JVM separados, verificando cantidades, asociaciones, tarifas, códigos,
estado de uso y saldo pendiente.