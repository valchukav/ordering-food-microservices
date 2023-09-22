# ordering-food-microservices
Spring Boot 2.7, Spring Data JPA, Kafka, Docker, PostgreSQL

## Description

This project is designed for educational purposes and represents food ordering system with 4 main services: 
- Customer service,
- Order service,
- Payment service,
- Restaurant service.

Services communicate with each other through Kafka in accordance to CQRS, Saga and Outbox patterns. The flow is described in the next steps:

1. The REST controller of the Order service processes an order creation request, which specifies the customer ID, restaurant ID and other order details such as delivery address and needed products. Then Order service validates the orded and in case of success publishes PaymentRequestAvroModel for Payment service (through "payment-request" topic).
2. PaymentRequestKafkaListener from Payment service consumes this message and provides its own validation of credit entry and credit history of the customer. After that Payment service publishes PaymentResponseAvroModel in PAID or CANCELLED status for Order service (through "payment-response" topic).
3. In case of PAID status Order service processes the order further (changing internal status) and publishes RestaurantApprovalRequestAvroModel for Restaurant service (through "restaurant-approval-request" topic).
4. RestaurantApprovalRequestKafkaListener from Restaurant service consumes this message and provides its own validation of restaurant and products availability (restaurant or product may be disabled). After that Restaurant service publishes RestaurantApprovalResponseAvroModel in APPROVED or REJECTED status for Order service (through "restaurant-approval-response" topic).
5. Based on the response received from the Restaurant service, the Order service changes the order status and updates the value in the database. In any case Order service will return order tracking ID by which user can receive information about the order status and failure messages (GET endpoint in Order service).

The consistency of the system is maintained by Saga and Outbox patterns: in each step services update outbox tables and in case of failure scenarios provide compensating transactions. Outbox tables are tracked and cleaned up by scheduled tasks.

Note that if there is no customer in database it should be created through the POST endpoint in Customer service. Customer service will publish Kafka message ("customer" topic) and Order service will update its own customers database table. Restaurants, products, and credit entries should be added in database manually, there are no REST endpoints in Restaurant and Payment services for this purpose.

## Deployment

This project may be deployed using docker compose file (https://github.com/valchukav/ordering-food-microservices/blob/master/infrastructure/docker-compose/docker-compose.yml). 

Maven `mvn clean install` command will create local Docker images.

Note that integration tests need locally installed PostgreSQL (check application.yml files in each service (container modules)).

## Tests

The main logic of this project is covered by unit and integration tests using JUnit 5, AssertJ, Mockito and MockMVC.

##
Based on course: https://www.udemy.com/course/microservices-clean-architecture-ddd-saga-outbox-kafka-kubernetes/
