# 🛒 Shopping Carts Service

<br>

## Descripción General

```shopping-carts-service``` es un microservicio central responsable de la gestión de carritos de compras dentro de un sistema de e-commerce distribuido.
Opera **antes del proceso de ventas**, manejando la creación de carritos, la agregación de productos y el cálculo del precio total, coordinándose con otros servicios de forma resiliente.
Este servicio fue diseñado intencionalmente para **evitar carritos inconsistentes**, incluso ante escenarios de fallas parciales.


<br>

## 🧰 Tecnologías

- Java 17


- Spring Boot 4


- Spring Data JPA


- Spring Cloud (Eureka, OpenFeign, LoadBalancer)


- Resilience4j (Circuit Breaker + Retry)


- MySQL


- Swagger / OpenAPI (Springdoc)


- Maven



<br>

## ✨ Funcionalidades Clave

- Crear carritos de compras asociados a un usuario válido


- Asignar productos a un carrito usando **solo código de producto + cantidad**

- Eliminar productos de un carrito de compras


- Calcular y actualizar automáticamente el precio total del carrito


- Acumular cantidades si el producto ya existe en el carrito


- Soportar **múltiples carritos activos por usuario**


- Validar la existencia del usuario antes de crear el carrito


- Enriquecer los productos del carrito con **nombre y precio unitario** vía ```products-service```


- Comunicación resiliente entre servicios usando Circuit Breaker y Retry


- Puede ejecutarse **de forma independiente** o detrás de un **API Gateway**




<br>


## 🔄 Flujo de Negocio (Cómo Funciona)
1. Se recibe una solicitud para crear un carrito de compras con:
   - ```id_user```
   - Lista de productos (```code + quantity```)


2. El servicio valida que el usuario exista (```users-service```)


3. Los detalles del producto (```name, single_price```) se obtienen desde  ```products-service```


4. El carrito:
   - Acumula cantidades si el producto ya existe
   - Agrega nuevos productos si no estaban previamente
   - Remueve productos elegidos


5. El ```total_price``` se calcula y actualiza internamente


6. El ID del carrito de compras se asocia al usuario


7. Si un servicio dependiente no está disponible:
   - La operación falla de forma segura
   - No se persiste ningún carrito inconsistente



<br>

## 📦 Product Input Format

En la solicitud solo se requiere **código de producto y cantidad**.
El precio y el nombre se resuelven internamente mediante lógica de negocio.

```
{
  "id_user": 12,
  "products": [
    { "code": 1, "quantity": 2 },
    { "code": 4, "quantity": 2 }
  ]
}
```


<br>

## 🔗 Dependencias del Servicio

Este servicio tiene dependencias **explícitas e intencionales**:

* **users-service**

   - Validar la existencia del usuario

   - Asociar los IDs de carritos al usuario


* **products-service**

  - Obtener el nombre y el precio unitario del producto

valida existencia de el/los productos


* **eureka-service**

  - Service discovery


* **api-gateway** (optional)

  - Enrutamiento centralizado en una arquitectura completa de microservicios


No existen otras dependencias ocultas o implícitas.

---

<br>

## 🧠 Lo que Aprendí

- Aplicar arquitectura MVC en un entorno real de microservicios

- Diseñar un servicio CRUD enfocado, con límites claros de responsabilidad


- Coordinar consistencia de datos entre múltiples servicios


- Usar Circuit Breaker y Retry para prevenir fallas en cascada


- Comprender cómo se comunican los microservicios más allá de la teoría


- Aunque técnicamente simple, este servicio fue clave para **consolidar principios fundamentales de backend** que escalan en sistemas distribuidos


<br>

## 🚀 Posibles Mejoras (Fuera de Alcance por Ahora)
- Estrategia global de manejo de excepciones


- Autenticación y autorización

- Pruebas unitarias e integrales (Testcontainers / mocks)


- Validación y reserva de stock


- Pagos, promociones, impuestos


Estas funcionalidades fueron excluidas intencionalmente para mantener el servicio enfocado y cohesivo.

<br>

## ▶️ Cómo Ejecutar el Proyecto

### Bases de Datos Requeridas

Antes de iniciar los servicios, deben existir las siguientes bases de datos MySQL:

- ```users_service```

- ```products_service```

- ```shopping_carts_service```

<br>

Cada base de datos es utilizada por su microservicio correspondiente.
**Los servicios no crean las bases de datos automáticamente**

---

<br>

### Servicios Requeridos

Antes de iniciar ```shopping-carts-service```, los siguientes servicios deben estar en ejecución:
- ```users-service```


- ```products-service```


- ```eureka-service```


- ```api-gateway``` (opcional pero recomendado)


Cada servicio debe estar correctamente registrado en Eureka.


---

<br>

## Requisitos
- Java 17


- Maven


- MySQL


- Eureka Server running at:
```
http://localhost:8761
```

<br>

## Ejecutar la Aplicación


Desde la raíz del proyecto:

```
mvn spring-boot:run
```


El servicio se iniciará en:
```
http://localhost:8085
```



<br>

## 🌐 Acceso a la API

<br>

## Con API Gateway (Recomendado)
```
http://localhost:8080/shopping-carts-service/...
```
Este es el enfoque preferido en un entorno completo de microservicios.

<br>

## Sin API Gateway (Standalone)
```
http://localhost:8085/...
```
Útil para desarrollo, pruebas o ejecución aislada.

<br>

## 📘 Documentación de la API (Swagger)

Una vez que el servicio esté en ejecución, Swagger UI estará disponible en:

```
http://localhost:8085/swagger-ui.html
```

Todos los endpoints, esquemas de request/response y ejemplos se generan automáticamente mediante Springdoc OpenAPI.
