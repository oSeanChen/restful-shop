# Restful-Shop

This project is a backend API server demo which containing Spring Boot, Spring Security, and JWT (JSON Web Token) for authentication. 
It provides endpoints for managing products, users, and orders.

## Features
- User management
- User authentication with JWT
- CRUD operations for products
- Order creation
- Role-Based Access Control (RBAC) for Product and Order Feature By Spring Security

## Security

This API uses Spring Security for protecting endpoints. JWT is used for stateless authentication.

### Role Management
- BUYER_ROLE
- SELLER_ROLE
- ADMIN_ROLE

- Search products no need to authorize.
- Only BUYER_ROLE can create order.
- Only SELLER_ROLE can create, update and delete the product.
- Admin_ROLE can opertate all feature and check users information.

## API Endpoints

### Product Controller

- `POST /api/products`: Create a new product
- `GET /api/products`: Retrieve all products
- `GET /api/products/{id}`: Retrieve a specific product by ID
- `PUT /api/products/{id}`: Update a product
- `DELETE /api/products/{id}`: Delete a product
- `GET /api/products/search`: Search and sort products with pagination

### User Controller

- `POST /register`: Register a new user
- `POST /login`: Authenticate a user and receive a JWT
- `GET /api/users`: Retrieve all users
- `GET /api/users/{id}`: Retrieve a specific user by ID
- `DELETE /api/users/{id}`: Delete a user

### Order Controller

- `POST /api/users/{userId}/orders`: Create a new order for a specific user

## Authentication

This API uses JWT for authentication. To access protected endpoints, you need to:

1. Register a user using the `/register` endpoint
2. Login using the `/login` endpoint to receive a JWT
3. Include the JWT in the `Authorization` header of subsequent requests

## Pagination and Sorting

The product search endpoint (`/api/products/search`) supports pagination and sorting with the following parameters:

- `productName`: Filter products by name (optional)
- `sortBy`: Field to sort by (default: "id")
- `sortOrder`: Sort order, "asc" or "desc" (default: "asc")
- `page`: Page number (default: 0)
- `limit`: Number of items per page (default: 5)

## Swagger UI
`[http://localhost:8080/v3/api-docs](http://localhost:8080/swagger-ui/index.html)`

## Getting Started

To run this project:

1. Ensure you have Java and Maven installed
2. Clone the repository
3. Configure your database settings in `application.properties` (database scheme can see : /src/main/resources/schema.sql)
4. Run `RestfulShopApplication` to start the server
