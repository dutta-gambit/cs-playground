
# API Gateway
 - Server side architectural componenet
 - Acts as a intermediary between client and server
 - acts as a single entry point for all the client requests
 - It receives all the client requests and forwards them to the appropriate backend service
 - It can also be used to implement cross-cutting concerns such as authentication, authorization, rate limiting, caching etc.



## API Gateway vs Load Balancer

 - An API gateway is focused on routing requests to the appropriate microservice.
 - A load balancer is focused on distributing requests across a group of backend servers.
 - An API gateway can also be used as a load balancer, but a load balancer cannot be used as an API gateway.




## Why do we need API Gateway?

 - Request Routing
    - Directing incoming client requests to the appropriate backend service

 - Aggregation of Multiple Services
    - Combining responses from multiple backend services into a single response to the client

 - Security Enforcement
    - Implementing security measures such as authentication, authorization and rate limiting

 - Load Balancing 
    - distributing incoming requests across multiple instances of backend services to ensure no single service becomes a bottleneck

 - Caching Responses
    - Storing frequently requested data to reduce latency and decrease load on backend services.

 - Protocol Translation
    - Translating between different protocols used by clients and backend services
    - client might send HTTP requests but backend services communicate using WebSockets or gRPC.

 - Monitoring and Logging 
    - Tracking and recording request and response data for analysis, debugging and performance monitoring

 - Transformation of Request/Response
    - Transforming the request and response data between the client and the backend service

 - API Versioning
    - Managing different versions of APIs to ensure backward compatibility and smooth transitions when updates are made.

 - Rate Limiting and Throttling
    - controlling the number of requests a client can make in a given time frame to protect backend services from being overwhelmed.

 
 - API Monetization
    - Enabling businesses to monetize their APIs by controlling access, usage tiers and billing.

 - Service Discovery Integration
    - Facilitating dynamic discovery of backend services, especially in environments where services are frequently scaled up or down.

 - Circuit Breaker Pattern Implementation
    - Preventing cascading failures by detecting when a backend service is failing and stopping requests to it temporarily.

 - Content Based Routing
    - Routing requests to different backend services based on the content of the request, such as headers, body or query params.

 - SSL Termination
    - Handling SSL/TLS encryption and decryption at the gateway level to offload this resource intensive task from backend services.

 - Policy Enforcement
    - Applying organization policies conistently across all API traffic, such as data validation, request formatting and access controls.

 - Multi Tenancy Support
    - Supporting multiple clients or tenants within a single API infrastructure while ensuring data isolation and customised configurations.

 - A/B Testing and Canary Releases
   - Facilitating controlle testing of new features or services by directing a subset of traffic to differnt backend versions.


 - Localization and Internationalization Support
   - Adapting responses based on the client's locale, such as language prefernces or regional settings.

 - Reducing Client COmplexity
    - Simplifying the client side logic by handling complex operations on the server side through the gateway.