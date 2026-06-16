# 🥭 Frutimonchis Backend

Sistema Backend desarrollado con **Spring Boot** y **PostgreSQL** para la gestión de inventario, productos, compras, ventas, clientes y proveedores mediante una API REST.

---

## 📖 Descripción

Frutimonchis Backend es una aplicación orientada a la administración de procesos comerciales e inventario. Implementa una arquitectura multicapa basada en Spring Boot y expone una API REST que permite gestionar productos, clientes, proveedores, compras, ventas y movimientos de inventario con persistencia en PostgreSQL.

---

## ✨ Características principales

* API REST desarrollada con Spring Boot.
* Persistencia de datos en PostgreSQL mediante Spring Data JPA.
* Arquitectura multicapa (Controller, Service, Repository, Entity y DTO).
* Documentación y pruebas de endpoints mediante Swagger/OpenAPI.
* Gestión de inventario, compras y ventas.
* Administración de clientes y proveedores.

---

## 🛠️ Tecnologías utilizadas

* Java 21
* Spring Boot 3
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Swagger / OpenAPI

---

## 🏗️ Arquitectura

```text
src/main/java
│
├── controller
├── service
├── repository
├── entity
├── dto
└── config
```

---

## 📋 Funcionalidades

### Gestión de Productos

* Crear productos
* Consultar productos
* Actualizar productos
* Eliminar productos
* Gestión de categorías

### Gestión de Clientes

* Registrar clientes
* Consultar clientes
* Actualizar clientes
* Eliminar clientes

### Gestión de Proveedores

* Registrar proveedores
* Administrar cuentas por pagar

### Gestión de Compras

* Registrar compras
* Gestionar detalles de compra
* Controlar estados de compra

### Gestión de Ventas

* Registrar ventas
* Gestionar detalles de venta
* Soportar procesos de facturación

### Inventario

* Control de stock
* Movimientos de inventario
* Historial de operaciones

---

## 🗄️ Base de datos

Principales entidades del sistema:

* categorias
* productos
* clientes
* proveedores
* compras
* compra_detalles
* ventas
* venta_detalles
* movimientos_inventario

---

## 📸 Capturas

### Swagger UI

![Swagger](screenshots/swagger-home.png)

### Productos

![Productos](screenshots/swagger-productos.png)

### Compras

![Compras](screenshots/swagger-compras.png)

### Ventas

![Ventas](screenshots/swagger-ventas.png)

### Base de datos PostgreSQL

![Database](screenshots/database.png)

---

## 🚀 Instalación

### Requisitos previos

* Java 21
* Maven
* PostgreSQL

### Clonar el proyecto

```bash
git clone https://github.com/Ing-MairaAlejandraRangel/frutimonchis-backend.git
```

### Ejecutar la aplicación

```bash
mvn spring-boot:run
```

---

## 📚 Documentación de la API

Una vez iniciada la aplicación, la documentación puede consultarse desde Swagger/OpenAPI en la ruta configurada por el proyecto (por ejemplo `http://localhost:8090/swagger-ui/index.html`).

---

## ✅ Estado del proyecto

Proyecto funcional y desarrollado con fines académicos y de portafolio profesional.

Incluye:

* ✔ API REST
* ✔ Persistencia en PostgreSQL
* ✔ Gestión de inventario
* ✔ Gestión de compras y ventas
* ✔ Administración de clientes y proveedores
* ✔ Documentación con Swagger

---

# 👩‍💻 Autora

**Maira Alejandra Rangel Murillo**
**Ingeniera de Sistemas**

Apasionada por el desarrollo de software y la creación de soluciones tecnológicas orientadas a la automatización, la gestión de datos y el desarrollo de aplicaciones web y móviles.

**Áreas de interés:**

* 💻 Desarrollo Full Stack
* ☕ Desarrollo Backend con Java y Spring Boot
* 🌐 Desarrollo Web con JavaScript y Node.js
* 📱 Desarrollo de aplicaciones Android
* 🗄️ Bases de Datos SQL (PostgreSQL y MySQL)
* 🔗 Diseño e implementación de APIs REST
* 🧪 Aseguramiento de la calidad (QA) y pruebas de software

Este proyecto fue desarrollado con fines académicos y forma parte de mi portafolio profesional como Ingeniera de Sistemas.

