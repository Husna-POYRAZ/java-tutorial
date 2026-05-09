# Mülakat Soruları — 100 Gün

> Her konu alanından günlük 1 soru. Toplam 10 kategori × 10 gün = 100 soru.

---

## Java

**Gün 1.**
`HashMap` ile `ConcurrentHashMap` arasındaki fark nedir? Thread-safety açısından `ConcurrentHashMap`'in iç yapısı nasıl çalışır?

**Gün 2.**
Java'da `volatile` keyword'ü ne işe yarar? `synchronized` ile farkı nedir? Hangi durumda hangisi tercih edilmelidir?

**Gün 3.**
`String`, `StringBuilder` ve `StringBuffer` farkları nelerdir? `String`'in immutable olması JVM'de nasıl bir avantaj sağlar?

**Gün 4.**
Java'da Checked Exception ile Unchecked Exception arasındaki fark nedir? `RuntimeException` alt sınıfı yazmak ne zaman mantıklıdır?

**Gün 5.**
`equals()` ve `hashCode()` sözleşmesi (contract) nedir? Bu iki metodu override etmediğinde `HashSet` veya `HashMap`'te ne gibi sorunlar yaşanır?

**Gün 6.**
Java'da `Comparator` ile `Comparable` arasındaki fark nedir? `Stream.sorted()` ile nasıl kullanılır?

**Gün 7.**
`Optional<T>` ne zaman kullanılmalı, ne zaman kullanılmamalıdır? `NullPointerException`'ı tamamen ortadan kaldırır mı?

**Gün 8.**
Java'daki `final`, `finally`, `finalize` arasındaki fark nedir? `finalize()` neden deprecated oldu?

**Gün 9.**
Java Stream API'deki `map()`, `flatMap()` ve `filter()` işlemleri nasıl çalışır? Lazy evaluation ne anlama gelir?

**Gün 10.**
Java'da `WeakReference`, `SoftReference` ve `PhantomReference` nedir? Garbage Collector ile ilişkileri nasıldır?

---

## Spring Boot

**Gün 11.**
`@Component`, `@Service`, `@Repository` ve `@Controller` arasındaki fark nedir? Hepsi `@Component`'ten türüyorsa neden ayrı annotasyonlar var?

**Gün 12.**
Spring Boot'ta `@Autowired`, constructor injection ve setter injection arasındaki farklar nelerdir? Hangi yaklaşım neden önerilir?

**Gün 13.**
`@Transactional` annotasyonu nasıl çalışır? `propagation` ve `isolation` parametreleri ne işe yarar?

**Gün 14.**
Spring Boot'ta `application.properties` ile `application.yml` farkı nedir? `@ConfigurationProperties` ne işe yarar?

**Gün 15.**
Spring Boot Actuator nedir? `health`, `metrics`, `env` endpoint'leri ne sağlar? Production'da hangileri açık bırakılmalıdır?

**Gün 16.**
`@Bean` ile `@Component` arasındaki fark nedir? Third-party kütüphane sınıflarını Spring context'e nasıl eklersin?

**Gün 17.**
Spring Boot'ta `@Profile` annotasyonu nasıl kullanılır? Dev, test ve prod ortamları için farklı bean konfigürasyonu nasıl yapılır?

**Gün 18.**
Spring Boot'ta exception handling nasıl merkezileştirilir? `@ControllerAdvice` ve `@ExceptionHandler` nasıl çalışır?

**Gün 19.**
`@RestController` ile `@Controller` arasındaki fark nedir? `ResponseEntity<T>` ne zaman kullanılmalıdır?

**Gün 20.**
Spring Boot'ta `CommandLineRunner` ve `ApplicationRunner` nedir? Uygulama başlarken bazı işlemleri nasıl çalıştırırsın?

---

## Hibernate / JPA

**Gün 21.**
JPA'da `FetchType.LAZY` ile `FetchType.EAGER` farkı nedir? N+1 problemi nedir ve nasıl çözülür?

**Gün 22.**
Hibernate'te `@OneToMany` ilişkisinde `cascade` ve `orphanRemoval` parametreleri ne işe yarar?

**Gün 23.**
`EntityManager`'ın lifecycle durumları nelerdir? `transient`, `managed`, `detached` ve `removed` durumları ne anlama gelir?

**Gün 24.**
JPQL ile Native Query arasındaki fark nedir? `@Query` annotasyonu ile `@NamedQuery` farkı nedir?

**Gün 25.**
Hibernate'te birinci seviye (L1) ve ikinci seviye (L2) cache nedir? `@Cacheable` nasıl kullanılır?

**Gün 26.**
`@Version` annotasyonu ne işe yarar? Optimistic locking ile pessimistic locking arasındaki fark nedir?

**Gün 27.**
Hibernate'te `@Inheritance` stratejileri nelerdir? `SINGLE_TABLE`, `JOINED` ve `TABLE_PER_CLASS` ne zaman tercih edilir?

**Gün 28.**
Spring Data JPA'da `JpaRepository`, `CrudRepository` ve `PagingAndSortingRepository` arasındaki fark nedir?

**Gün 29.**
Hibernate'te `@Embeddable` ve `@Embedded` ne işe yarar? Value object pattern ile ilişkisi nedir?

**Gün 30.**
Database migration için Flyway ile Liquibase arasındaki farklar nelerdir? Production ortamında migration nasıl yönetilir?

---

## Kafka

**Gün 31.**
Kafka'da `topic`, `partition`, `offset` ve `consumer group` kavramları nedir? Aralarındaki ilişkiyi açıkla.

**Gün 32.**
Kafka'da `at-least-once`, `at-most-once` ve `exactly-once` delivery semantikleri nedir? Her birinin trade-off'ları nelerdir?

**Gün 33.**
Kafka'da `acks=0`, `acks=1` ve `acks=all` konfigürasyonları arasındaki fark nedir? Hangisi ne zaman kullanılır?

**Gün 34.**
Kafka Consumer'da `auto.offset.reset` parametresi ne işe yarar? `earliest` ile `latest` arasındaki fark nedir?

**Gün 35.**
Kafka'da `replication factor` ve `ISR (In-Sync Replicas)` kavramları nedir? Broker çöktüğünde ne olur?

**Gün 36.**
Kafka'da mesaj sıralaması garantisi var mıdır? Partition içinde ve partition'lar arasındaki sıralama nasıl çalışır?

**Gün 37.**
`KafkaTemplate` ile `@KafkaListener` Spring'de nasıl kullanılır? Error handling için `@RetryableTopic` nasıl yapılandırılır?

**Gün 38.**
Kafka'da `compaction` nedir? Log compacted topic ne zaman kullanılır?

**Gün 39.**
Kafka Streams ile Kafka Consumer API arasındaki fark nedir? Ne zaman hangisi tercih edilmelidir?

**Gün 40.**
Kafka'da `Dead Letter Topic (DLT)` nedir? Poison message sorunu nasıl handle edilir?

---

## Event-Driven Architecture

**Gün 41.**
Event-Driven Architecture (EDA) nedir? Request-Response mimarisi ile temel farkları nelerdir?

**Gün 42.**
Domain Event ile Integration Event arasındaki fark nedir? Her ikisini de aynı sistemde nasıl kullanırsın?

**Gün 43.**
Event Sourcing nedir? Geleneksel CRUD tabanlı yaklaşımdan farkı ve trade-off'ları nelerdir?

**Gün 44.**
CQRS (Command Query Responsibility Segregation) pattern'i nedir? Event Sourcing ile birlikte nasıl kullanılır?

**Gün 45.**
Outbox Pattern nedir? Database transaction ile event publish işlemini atomik yapmak için nasıl kullanılır?

**Gün 46.**
Idempotent consumer nedir? Aynı event'in birden fazla kez işlenmesi (duplicate) nasıl önlenir?

**Gün 47.**
Event schema evolution nasıl yönetilir? Backward ve forward compatibility için hangi stratejiler kullanılır?

**Gün 48.**
Event-Driven sistemlerde eventual consistency nedir? Kullanıcıya nasıl yansıtılır?

**Gün 49.**
Choreography tabanlı saga ile orchestration tabanlı saga arasındaki fark nedir? Hangi durum hangisini gerektirir?

**Gün 50.**
Event-Driven sistemlerde distributed tracing nasıl yapılır? Correlation ID propagation nasıl sağlanır?

---

## Unit Testing

**Gün 51.**
Unit test ile integration test arasındaki fark nedir? Test piramidi (test pyramid) ne anlama gelir?

**Gün 52.**
Mockito'da `mock()`, `spy()` ve `@InjectMocks` arasındaki fark nedir? Ne zaman hangisi kullanılır?

**Gün 53.**
`@Mock` ile `@MockBean` arasındaki fark nedir? Spring context'in ne zaman ayağa kaldırılması gerekir?

**Gün 54.**
Test'te `verify()` ne işe yarar? Behaviour verification ile state verification arasındaki fark nedir?

**Gün 55.**
`ArgumentCaptor` nedir? Bir metoda geçilen argümanı nasıl test edersin?

**Gün 56.**
Parameterized test nedir? JUnit 5'te `@ParameterizedTest` ile `@CsvSource` nasıl kullanılır?

**Gün 57.**
Code coverage metriği ne anlama gelir? %100 coverage her zaman yeterli midir? Branch coverage ile line coverage farkı nedir?

**Gün 58.**
`@BeforeEach`, `@AfterEach`, `@BeforeAll` ve `@AfterAll` ne işe yarar? Test lifecycle nasıl yönetilir?

**Gün 59.**
Test double'lar nelerdir? Dummy, Fake, Stub, Mock ve Spy arasındaki farkları açıkla.

**Gün 60.**
TDD (Test Driven Development) nedir? Red-Green-Refactor döngüsü nasıl uygulanır?

---

## Component / Integration Testing

**Gün 61.**
`@SpringBootTest` annotasyonu ne yapar? `webEnvironment` parametresinin `MOCK`, `RANDOM_PORT` ve `DEFINED_PORT` seçenekleri ne anlama gelir?

**Gün 62.**
`MockMvc` ile `WebTestClient` arasındaki fark nedir? REST endpoint'lerini test etmek için hangisi tercih edilir?

**Gün 63.**
Testcontainers nedir? Real bir PostgreSQL ile test yazmak için nasıl kullanılır?

**Gün 64.**
`@DataJpaTest` annotasyonu ne yapar? Repository katmanını izole etmek için neden kullanılır?

**Gün 65.**
`@WebMvcTest` annotasyonu ne yapar? Controller katmanını izole etmek için nasıl kullanılır?

**Gün 66.**
Spring Boot'ta `@EmbeddedKafka` ile Kafka consumer/producer testi nasıl yazılır?

**Gün 67.**
Consumer Driven Contract Testing (CDC) nedir? Pact framework nasıl çalışır?

**Gün 68.**
`WireMock` nedir? Dış servis bağımlılıklarını test ortamında nasıl simüle edersin?

**Gün 69.**
Database test'lerinde `@Transactional` annotasyonu test metoduna konulduğunda ne olur? Rollback davranışı nasıl çalışır?

**Gün 70.**
End-to-end test ile component test arasındaki fark nedir? Mikroservis mimarisinde hangi test seviyesine ne kadar yatırım yapılmalıdır?

---

## Microservices

**Gün 71.**
Service Discovery nedir? Client-side ile server-side discovery arasındaki fark nedir?

**Gün 72.**
API Gateway nedir? BFF (Backend for Frontend) pattern'i ne zaman kullanılır?

**Gün 73.**
Circuit Breaker pattern'i nedir? `CLOSED`, `OPEN` ve `HALF_OPEN` durumları nasıl çalışır?

**Gün 74.**
Mikroservislerde distributed transaction yönetimi için hangi yaklaşımlar kullanılır? 2PC ile Saga Pattern'in farkı nedir?

**Gün 75.**
`Bulkhead` pattern'i nedir? Thread pool isolation ile semaphore isolation arasındaki fark nedir?

**Gün 76.**
Mikroservislerde authentication ve authorization nasıl yönetilir? JWT token propagation nasıl sağlanır?

**Gün 77.**
Service mesh nedir? Sidecar proxy (Istio, Linkerd) ne sağlar? API Gateway ile farkı nedir?

**Gün 78.**
Mikroservislerde inter-service communication için synchronous (REST/gRPC) ile asynchronous (Kafka) yaklaşımlarının trade-off'ları nelerdir?

**Gün 79.**
Mikroservislerde `database per service` pattern'i neden önemlidir? Shared database anti-pattern neden sorun yaratır?

**Gün 80.**
Strangler Fig pattern'i nedir? Monoliti mikroservise dönüştürme sürecinde nasıl kullanılır?

---

## DevOps

**Gün 81.**
CI/CD pipeline nedir? Continuous Integration ile Continuous Deployment arasındaki fark nedir?

**Gün 82.**
Docker'da `image` ile `container` arasındaki fark nedir? `COPY` ile `ADD`, `CMD` ile `ENTRYPOINT` arasındaki farklar nelerdir?

**Gün 83.**
Kubernetes'te `Pod`, `Deployment`, `Service` ve `Ingress` kavramları nedir? Aralarındaki ilişkiyi açıkla.

**Gün 84.**
Kubernetes'te `liveness probe` ile `readiness probe` arasındaki fark nedir? Yanlış yapılandırma nasıl sorun yaratır?

**Gün 85.**
Blue-Green deployment ile Canary deployment arasındaki fark nedir? Rollback stratejileri nasıl planlanır?

**Gün 86.**
`ConfigMap` ve `Secret` Kubernetes'te nasıl kullanılır? Secret yönetimi için best practice nedir?

**Gün 87.**
Horizontal Pod Autoscaler (HPA) nasıl çalışır? CPU ve custom metric'e göre scale nasıl yapılandırılır?

**Gün 88.**
GitOps nedir? ArgoCD veya Flux ile nasıl uygulanır? Push-based ile pull-based deployment farkı nedir?

**Gün 89.**
`Helm chart` nedir? Kubernetes manifest'leri yönetmek için neden kullanılır?

**Gün 90.**
Observability'nin üç sütunu nedir? Logs, metrics ve traces arasındaki fark nedir? Hangi araçlar hangi ihtiyaca karşılık gelir?

---

## Architectural Decisions

**Gün 91.**
CAP teoremi nedir? CP ve AP sistemleri arasındaki trade-off nasıl değerlendirilir?

**Gün 92.**
Monolith, modular monolith ve mikroservis arasında nasıl karar verilir? Conway's Law bu kararı nasıl etkiler?

**Gün 93.**
Synchronous ve asynchronous iletişim arasında karar verirken hangi faktörler belirleyicidir?

**Gün 94.**
Event-driven mimari ne zaman tercih edilmeli, ne zaman request-response yeterlidir?

**Gün 95.**
API versioning stratejileri nelerdir? URL versioning, header versioning ve media type versioning'in trade-off'ları nelerdir?

**Gün 96.**
`Eventual consistency` kabul edilemez olduğunda hangi mimari kararlar alınabilir?

**Gün 97.**
Caching stratejileri nelerdir? Cache-aside, write-through ve write-behind arasındaki fark nedir? Cache invalidation nasıl yönetilir?

**Gün 98.**
ADR (Architecture Decision Record) nedir? Neden yazılır ve nasıl yapılandırılır?

---

## Design Patterns

**Gün 99.**
Repository pattern nedir? Spring Data JPA bunu nasıl uygular? Doğrudan `EntityManager` kullanmak yerine neden tercih edilir?

**Gün 100.**
Strategy pattern ile Template Method pattern arasındaki fark nedir? Spring'de gerçek hayat kullanım örneği nedir?
