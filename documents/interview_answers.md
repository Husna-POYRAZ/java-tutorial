# Mülakat Cevapları — 100 Gün

---

## Java

**Gün 1 — HashMap vs ConcurrentHashMap**

`HashMap` thread-safe değildir. Birden fazla thread aynı anda `put()` çağırırsa, özellikle resize sırasında infinite loop veya veri kaybı yaşanabilir. `ConcurrentHashMap` Java 8 itibarıyla segment-based locking yerine `CAS (Compare-And-Swap)` + `synchronized` block kombinasyonunu kullanır. Bucket başına lock tutarak yüksek concurrency sağlar. Okuma işlemleri tamamen lock-free çalışır. `null` key/value kabul etmez.

```java
// Thread-safe değil
Map<String, Integer> map = new HashMap<>();

// Thread-safe, yüksek concurrency
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```

---

**Gün 2 — volatile vs synchronized**

`volatile` yalnızca görünürlük (visibility) garantisi verir: bir thread'in yazdığı değer, diğer thread'ler tarafından anında görülür. CPU cache'inin atlanmasını sağlar. Ancak atomikliği garanti etmez; `count++` gibi compound işlemlerde yeterli değildir. `synchronized` hem görünürlük hem atomiklik sağlar, bir blok ya da metodu tek thread'e kilitler. Basit flag değişkenleri için `volatile`, compound işlemler için `synchronized` veya `AtomicInteger` tercih edilir.

---

**Gün 3 — String, StringBuilder, StringBuffer**

`String` immutable'dır; her concatenation işlemi heap'te yeni nesne oluşturur. JVM string pool sayesinde aynı literal yeniden kullanılır. `StringBuilder` mutable, thread-safe değil, tek thread senaryosunda tercih edilir. `StringBuffer` mutable, her metodu `synchronized`, multi-thread senaryoda kullanılır ama performans maliyeti vardır. Loop içinde string birleştirme yapılıyorsa `StringBuilder` kullanmak O(n²) karmaşıklığını önler.

---

**Gün 4 — Checked vs Unchecked Exception**

Checked exception'lar `Exception`'ı doğrudan extend eder ve compiler tarafından handle edilmesi zorunlu tutulur (`IOException`, `SQLException`). Unchecked exception'lar `RuntimeException`'ı extend eder, handle etmek zorunlu değildir (`NullPointerException`, `IllegalArgumentException`). Özel Unchecked exception yazmak, programlama hatalarını temsil ederken (geçersiz argüman, null kontrol gibi) ve caller'ı her yerde try-catch yazmaktan kurtarmak istendiğinde tercih edilir. Spring, tüm database exception'larını unchecked `DataAccessException`'a çevirir.

---

**Gün 5 — equals() ve hashCode() sözleşmesi**

Sözleşme şudur: `a.equals(b)` true ise `a.hashCode() == b.hashCode()` kesinlikle sağlanmalıdır. Tersi zorunlu değildir. `hashCode()` override edilmeden `equals()` override edilirse, `HashSet`'e iki "eşit" nesne eklenebilir çünkü farklı bucket'lara yerleşir. `HashMap`'te key lookup çalışmaz. IDE veya Lombok `@EqualsAndHashCode` ile bu sözleşmeyi otomatik sağlamak önerilir.

---

**Gün 6 — Comparator vs Comparable**

`Comparable`, sınıfın doğal sırasını tanımlar (`compareTo()` metodunu sınıf içinde implement eder). Bir sınıfın tek bir doğal sırası olabilir. `Comparator` harici bir karşılaştırıcıdır, sınıfın kaynak koduna dokunmadan birden fazla sıralama stratejisi tanımlanabilir. `Stream.sorted(Comparator.comparing(Person::getAge).thenComparing(Person::getName))` gibi zincirleme kullanım mümkündür.

---

**Gün 7 — Optional<T>**

Return type olarak `null` dönebilecek metotlarda kullanılır; caller'ı null check yapmaya zorlar. `Optional.ofNullable()` ile oluşturulur, `.map()`, `.filter()`, `.orElse()`, `.orElseThrow()` ile işlenir. Kullanılmaması gereken yerler: field tipi olarak, method parametresi olarak, `Collection` içinde. `Optional` nesnenin kendisi null olursa yine `NullPointerException` fırlatır; null'u tamamen ortadan kaldırmaz.

---

**Gün 8 — final, finally, finalize**

`final`: değişken (reassign edilemez), metot (override edilemez), sınıf (extend edilemez) için kullanılır. `finally`: try-catch bloğunda her koşulda çalışan blok; kaynak temizleme için kullanılır (try-with-resources tercih edilir). `finalize()`: GC nesneyi toplamadan önce çağırırdı, ancak çağrılacağı garanti değildi, çağrılma zamanı belirsizdi, performans sorunları yaratıyordu. Java 9'da deprecated, Java 18'de kaldırıldı. Yerine `Cleaner` veya try-with-resources kullanılır.

---

**Gün 9 — Stream map, flatMap, filter**

`filter()` predicate'e uymayan elemanları eler. `map()` her elemanı başka bir değere dönüştürür (1-to-1). `flatMap()` her elemanı bir Stream'e dönüştürür ve tüm Stream'leri düzleştirir (1-to-many). Örnek: `List<List<String>>` yapısını `List<String>`'e çevirmek için `flatMap(Collection::stream)` kullanılır. Stream operasyonları lazy'dir; terminal operasyon (`collect`, `forEach`, `count`) çağrılana kadar hiçbir işlem yapılmaz.

---

**Gün 10 — WeakReference, SoftReference, PhantomReference**

`StrongReference`: normal referans; GC toplamaz. `WeakReference`: GC bir sonraki çalışmada toplar; `WeakHashMap`'te key olarak kullanılır. `SoftReference`: heap dolmaya başlayana kadar yaşar; cache implementasyonları için idealdir. `PhantomReference`: nesne toplandıktan sonra `ReferenceQueue`'ya eklenir; temizleme (cleanup) işlemleri için kullanılır, `get()` her zaman null döner.

---

## Spring Boot

**Gün 11 — @Component, @Service, @Repository, @Controller**

Hepsi `@Component`'in stereotip annotasyonlarıdır ve Spring IoC container'ına bean olarak kaydedilmelerini sağlar. Farkları semantik ve teknik olarak ikiye ayrılır. `@Repository`: `PersistenceExceptionTranslationPostProcessor` bean'i devredeyse SQL exception'larını Spring'in `DataAccessException` hiyerarşisine otomatik çevirir. `@Service`: business logic katmanını temsil eder, ekstra teknik özellik taşımaz. `@Controller` / `@RestController`: Spring MVC'ye request mapping yapılacağını belirtir. Anlambilimsel netlik ve AOP proxy'lerin doğru çalışması için doğru annotasyon kullanılmalıdır.

---

**Gün 12 — Injection türleri**

Constructor injection önerilir: bağımlılıklar açıkça görünür, `final` field kullanılabilir, nesne her zaman geçerli durumda oluşur ve mock inject etmek için Spring context'e gerek kalmaz. Field injection (`@Autowired` field üzerinde) test edilebilirliği azaltır, circular dependency tespitini güçleştirir. Setter injection opsiyonel bağımlılıklar için kullanılabilir. Circular dependency'de Spring, constructor injection kullandığında uygulama başlarken exception fırlatır; bu bir uyarıdır ve tasarım yeniden gözden geçirilmelidir.

---

**Gün 13 — @Transactional**

Spring, bu annotasyonu gördüğünde AOP proxy üzerinden transaction açar, metot sonunda commit, exception durumunda rollback yapar. `propagation`: REQUIRED (varsayılan, mevcut transaction'a katıl), REQUIRES_NEW (her zaman yeni transaction aç), NESTED, SUPPORTS vb. `isolation`: READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE. Dikkat edilmesi gereken: aynı sınıf içinden çağrı proxy'yi bypass eder (self-invocation), transaction çalışmaz. Private metotlarda proxy çalışmaz. `@Transactional(readOnly = true)` Hibernate flush'u devre dışı bırakarak performansı artırır.

---

**Gün 14 — application.properties vs YAML, @ConfigurationProperties**

YAML hiyerarşik yapıları daha okunabilir gösterir, liste tanımlamak daha kolaydır. Properties düz formattır, bazı araçlarla daha uyumludur. `@ConfigurationProperties(prefix = "app.mail")` ile belirli bir prefix altındaki tüm değerler POJO'ya bağlanır, tip güvenliği ve IDE desteği sağlanır. `@Value` tek değer için uygundur ama fazla değer için bakımı zorlaşır. `@Validated` ile birlikte kullanılırsa başlangıçta geçersiz config yakalanır.

---

**Gün 15 — Spring Boot Actuator**

`/actuator/health`: uygulama ve bağımlılıkların (DB, Kafka, disk) sağlık durumu. Kubernetes liveness/readiness probe için kullanılır. `/actuator/metrics`: JVM, HTTP, custom metric bilgileri. Prometheus scraping ile entegre edilir. `/actuator/env`: tüm ortam değişkenlerini ve config değerlerini gösterir; Production'da kapatılmalı çünkü hassas bilgi içerebilir. `/actuator/loggers`: runtime'da log seviyesini değiştirmeyi sağlar. Best practice: production'da sadece `health` ve `prometheus` endpoint'leri açık tutulur, diğerleri güvence altına alınır veya devre dışı bırakılır.

---

**Gün 16 — @Bean vs @Component**

`@Component` (ve türevleri) class path scanning ile otomatik tespit edilir; kendi yazdığın sınıflarda kullanılır. `@Bean` ise bir `@Configuration` sınıfı içinde metot üzerine konur; third-party kütüphane sınıfları (Jackson `ObjectMapper`, `RestTemplate`) gibi kaynak koduna erişilemeyen sınıfları Spring context'e eklemek için kullanılır. `@Bean` metodunun dönüş tipi Spring'e bean türünü söyler; metot adı varsayılan bean adıdır.

---

**Gün 17 — @Profile**

`@Profile("dev")` annotasyonu ile bir bean yalnızca belirtilen profilde aktif edilir. `application-dev.yml`, `application-prod.yml` gibi profil-özgü config dosyaları otomatik yüklenir. Aktif profil `spring.profiles.active` ile set edilir: JVM argument, env variable veya config dosyası ile. Test sınıflarında `@ActiveProfiles("test")` kullanılır. `@Profile("!prod")` şeklinde negasyon da desteklenir.

---

**Gün 18 — @ControllerAdvice, @ExceptionHandler**

`@ControllerAdvice`, tüm controller'lar için global exception handling sağlar. `@ExceptionHandler(ResourceNotFoundException.class)` ile belirli exception türleri yakalanır ve uygun HTTP response dönülür. `ResponseEntityExceptionHandler`'ı extend ederek Spring MVC'nin varsayılan exception'larını da handle etmek mümkündür. `@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`. Her exception için `ProblemDetail` (RFC 7807) formatında standart hata response üretmek best practice'tir.

---

**Gün 19 — @RestController vs @Controller, ResponseEntity**

`@Controller` view name döner (Thymeleaf vb.). `@RestController` = `@Controller` + `@ResponseBody`; her metot return değeri doğrudan response body'ye serialization yapar. `ResponseEntity<T>` HTTP status code, header ve body üzerinde tam kontrol sağlar. Sadece body yeterliyse doğrudan `T` döndürülebilir; status veya header kontrolü gerektiğinde `ResponseEntity` kullanılır.

---

**Gün 20 — CommandLineRunner vs ApplicationRunner**

İkisi de `ApplicationContext` tamamen yüklendikten sonra, uygulama başlarken çalışır. Fark: `CommandLineRunner.run(String... args)` raw string array alır. `ApplicationRunner.run(ApplicationArguments args)` parse edilmiş argument nesnesi sunar; `--key=value` formatındaki argümanlara isimle erişilebilir. Database seed işlemleri, cache ısınma, başlangıç kontrolleri için kullanılır. `@Order` ile birden fazla runner sıralanabilir.

---

## Hibernate / JPA

**Gün 21 — LAZY vs EAGER, N+1 Problemi**

`EAGER` ilişki her sorgulandığında bağlı entity'ler de anında yüklenir; gereksiz veri çekilebilir. `LAZY` ilişki yalnızca erişildiğinde yüklenir; varsayılan tercih edilendir. N+1 problemi: 10 `Order` çekildiğinde her birinin `Customer`'ı için ayrı SQL atılmasıdır (1 + 10 = 11 sorgu). Çözümler: `JOIN FETCH` ile JPQL sorgusu yazmak, `@EntityGraph`, `@BatchSize` veya Hibernate'in `FetchMode.SUBSELECT` kullanımı.

```java
// N+1'i önlemek için JOIN FETCH
@Query("SELECT o FROM Order o JOIN FETCH o.customer")
List<Order> findAllWithCustomer();
```

---

**Gün 22 — cascade ve orphanRemoval**

`cascade = CascadeType.ALL` parent üzerindeki persist, merge, remove gibi işlemlerin child'lara da yansımasını sağlar. `CascadeType.PERSIST` yalnızca kaydetmeyi yayar. `orphanRemoval = true` ise parent'tan referansı kaldırılan child entity otomatik silinir. Fark: `cascade = REMOVE` parent silinince child silinir; `orphanRemoval = true` parent silinmeden child listeden çıkarılınca da child silinir. İkisi birlikte kullanıldığında tam yaşam döngüsü kontrolü sağlanır.

---

**Gün 23 — EntityManager Lifecycle**

`Transient`: new ile oluşturulmuş, context bilmiyor, DB'de yok. `Managed` (Persistent): context takip ediyor; dirty checking aktif, transaction sonunda flush edilir. `Detached`: context kapandı veya `evict()` çağrıldı; değişiklikler takip edilmiyor. `Removed`: `remove()` çağrıldı; transaction commit'inde DELETE atılır. `merge()` detached entity'yi tekrar managed yapar.

---

**Gün 24 — JPQL vs Native Query**

JPQL entity adları ve field adları üzerinden çalışır; DB'den bağımsız, Hibernate dialect'e göre SQL'e çevrilir. Native query doğrudan SQL'dir; DB-specific özellikler kullanılabilir ama portabilite azalır. `@Query(nativeQuery = true)` ile Native query yazılır. `@NamedQuery` sınıf seviyesinde tanımlanır, uygulama başlangıcında parse edilir (hata erken yakalanır); `@Query` repository metodunda tutulur, okunması daha kolaydır. Karmaşık raporlama sorguları için Native veya QueryDSL tercih edilir.

---

**Gün 25 — L1 ve L2 Cache**

L1 (First-Level) cache `EntityManager` kapsamındadır; aynı transaction içinde aynı entity iki kez sorgulanırsa DB'ye ikinci kez gidilmez. Otomatik, devre dışı bırakılamaz. L2 (Second-Level) cache uygulama kapsamındadır; farklı transaction'lar arasında paylaşılır. Ehcache, Caffeine gibi provider gerekir. `@Cacheable` ve `@org.hibernate.annotations.Cache` ile aktif edilir. `@Version` veya sık güncellenen entity'lerde L2 cache stale data riskine yol açabilir.

---

**Gün 26 — @Version ve Locking**

`@Version` alanı (int veya Long) Hibernate tarafından otomatik yönetilir. Update sırasında version eşleşmezse `OptimisticLockException` fırlatılır. Optimistic locking: çakışma beklenmediğinde, lock almadan işlem yapılır; sonunda kontrol edilir. Yüksek read, düşük conflict senaryoları için uygundur. Pessimistic locking (`LockModeType.PESSIMISTIC_WRITE`): satır DB seviyesinde kilitlenir; çakışma yüksekse tercih edilir ama deadlock riski taşır.

---

**Gün 27 — @Inheritance Stratejileri**

`SINGLE_TABLE`: tüm hiyerarşi tek tabloda, ayrımcı sütun (discriminator) ile. En hızlı sorgular, ancak alt sınıf sütunları nullable olur, tablo genişler. `JOINED`: her sınıf ayrı tablo, join ile birleştirilir. Normalize, ancak sorgular JOIN gerektirir. `TABLE_PER_CLASS`: her konkrit sınıf ayrı tam tablo, polimorfik sorgularda UNION kullanılır. Tercih: basit hiyerarşi için `SINGLE_TABLE`, normalized tasarım için `JOINED`.

---

**Gün 28 — JpaRepository vs CrudRepository**

`CrudRepository`: temel CRUD operasyonları (`save`, `findById`, `delete`, `findAll`). `PagingAndSortingRepository`: `CrudRepository` + pagination ve sorting desteği. `JpaRepository`: `PagingAndSortingRepository` + `flush()`, `saveAndFlush()`, `deleteInBatch()` gibi JPA-specific metodlar. Genelde `JpaRepository` tercih edilir çünkü en kapsamlıdır. Spring Data, method isimden otomatik sorgu üretir: `findByEmailAndActiveTrue()`.

---

**Gün 29 — @Embeddable ve @Embedded**

`@Embeddable` ile işaretlenen sınıf kendi tablosu olmayan bir value object'tir. `@Embedded` ile bu nesne başka bir entity içine gömülür; sütunlar owner entity'nin tablosuna eklenir. DDD'deki Value Object pattern'ini JPA'da uygulamanın yoludur. `Address` sınıfı `Person` entity'sine gömülürse `person` tablosunda `address_street`, `address_city` sütunları oluşur. `@AttributeOverride` ile sütun adları özelleştirilebilir.

---

**Gün 30 — Flyway vs Liquibase**

Flyway SQL veya Java tabanlı migration script'leri kullanır; `V1__create_users.sql` naming convention ile versiyon sıralaması yapar. Sade ve öğrenmesi kolaydır. Liquibase XML, YAML, JSON veya SQL formatında changeset kullanır; rollback desteği yerleşik olarak gelir. Production'da best practice: migration'lar version control'de tutulur, `repair` ve `validate` komutları pipeline'a eklenir, Spring Boot ile her ikisi de `spring.flyway.*` veya `spring.liquibase.*` konfigürasyonu ile entegre olur.

---

## Kafka

**Gün 31 — Topic, Partition, Offset, Consumer Group**

`Topic`: mesajların kategorilere göre gruplandığı kanal. `Partition`: topic'in paralel işlenebilen alt bölümleri; her partition bir broker'da leader, diğerlerinde replica olarak tutulur. `Offset`: bir partition içindeki mesajın sıra numarası; consumer ilerledikçe artar. `Consumer Group`: aynı topic'i birlikte tüketen consumer'lar; her partition bir gruptaki yalnızca bir consumer'a atanır. Bu sayede paralel işleme sağlanır. Consumer sayısı partition sayısını geçerse fazla consumer'lar boşta kalır.

---

**Gün 32 — Delivery Semantics**

`At-most-once`: mesaj kaybolabilir ama hiç duplike olmaz. Offset önce commit edilir. `At-least-once`: mesaj en az bir kez işlenir ama duplike olabilir. Offset başarıdan sonra commit edilir. `Exactly-once`: mesaj tam bir kez işlenir. Kafka Transactions API (`idempotent producer` + `transactional.id`) ile sağlanır, ancak karmaşıklık ve latency artar. Çoğu sistemde at-least-once + idempotent consumer tercih edilir çünkü exactly-once her durumda uygulanamaz (harici sistemler).

---

**Gün 33 — acks Konfigürasyonu**

`acks=0`: producer yanıt beklemez, en yüksek throughput, mesaj kaybı riski var. `acks=1`: leader broker'dan onay gelir, leader çökerse kayıp olabilir. `acks=all` (veya `-1`): tüm ISR broker'larından onay beklenir, en yüksek durability. `min.insync.replicas` ile birlikte kullanılır. Finansal işlemler için `acks=all + min.insync.replicas=2`, yüksek throughput log senaryoları için `acks=1` tercih edilir.

---

**Gün 34 — auto.offset.reset**

Consumer group'un ilk kez bir partition'ı okuduğunda (henüz commit edilmiş offset yok) nereden başlayacağını belirtir. `earliest`: partition'ın başından, tüm geçmiş mesajları okur. `latest`: subscription anından itibaren yeni mesajları okur. `none`: commit edilmiş offset yoksa exception fırlatır. Yeni bir consumer group deploy edildiğinde `earliest` geçmişi işlemek için, `latest` sadece yeni olayları işlemek için seçilir.

---

**Gün 35 — Replication Factor ve ISR**

`replication.factor=3` bir partition'ın 3 broker'da kopyalanacağını belirtir. Leader tüm okuma/yazmaları alır. `ISR (In-Sync Replicas)`: leader ile senkron durumda olan replica'ların listesi; geride kalan replica ISR'dan düşer. `acks=all` ile yazmak: ISR'daki tüm replica'ların onaylamasını bekler. Bir broker çöktüğünde Kafka ISR içinden yeni leader seçer. Veri kaybı olmaması için `replication.factor >= 3` ve `min.insync.replicas=2` önerilir.

---

**Gün 36 — Mesaj Sıralaması**

Kafka, sıralama garantisini yalnızca aynı partition içinde verir. Farklı partition'lardaki mesajların sırası garanti değildir. Aynı entity'ye ait mesajların sıralı işlenmesi için aynı key kullanılır; aynı key her zaman aynı partition'a gider (default partitioner: key hash). Sıralama kritikse topic'in tek partition'lı olması gerekir, ancak bu paralellliği yok eder. `enable.idempotence=true` ile producer retry'da aynı mesajı iki kez yazmaz.

---

**Gün 37 — Spring Kafka: KafkaTemplate, @KafkaListener, @RetryableTopic**

```java
// Producer
kafkaTemplate.send("orders", order.getId().toString(), order);

// Consumer
@KafkaListener(topics = "orders", groupId = "order-service")
public void consume(Order order) { ... }

// Retry + DLT
@RetryableTopic(attempts = "3", backoff = @Backoff(delay = 1000))
@KafkaListener(topics = "orders")
public void consume(Order order) { ... }
```

`@RetryableTopic` başarısız mesajları `orders-retry-0`, `orders-retry-1` gibi topic'lere yönlendirir, tüketim başarısız kalırsa `orders-dlt` topic'ine gönderir.

---

**Gün 38 — Log Compaction**

Normal topic'lerde eski mesajlar retention süresi dolunca silinir. Compacted topic'te her key'in yalnızca son değeri tutulur; eski değerler temizlenir. Tombstone (null value): key silindiğini bildirir. Kullanım senaryosu: Kafka'yı changelog store olarak kullanmak, consumer yeniden başladığında tam state'i okumak. Örnek: kullanıcı profil güncellemelerinde yalnızca son profil bilgisi saklanır. `cleanup.policy=compact` ile aktif edilir.

---

**Gün 39 — Kafka Streams vs Consumer API**

Consumer API düşük seviyeli bir API'dır; partition atama, offset yönetimi, rebalance handling manuel yapılır. Kafka Streams, Consumer API üzerine inşa edilmiş high-level bir DSL/stream processing kütüphanesidir. `KStream`, `KTable`, `KGroupedStream` gibi abstraction'lar, windowing, join, aggregation işlemleri sağlar. Basit mesaj tüketimi için Consumer API yeterlidir. Stateful stream processing, aggregation, join gerektiğinde Kafka Streams tercih edilir. KSQL/ksqlDB de Kafka Streams üzerine kuruludur.

---

**Gün 40 — Dead Letter Topic**

İşlenemeyen (poison) mesajlar normal topic'te kaldıkça partition bloke olur. DLT, bu mesajların ayrı bir topic'e taşınarak akışın devam etmesini sağlar. Spring Kafka'da `@RetryableTopic` ile otomatik DLT yapılandırılır. DLT'deki mesajlar için monitoring alarm kurulur ve operasyonel süreç belirlenir: manuel inceleme, re-process etme veya atma. Header'lara hata nedeni, retry sayısı ve orijinal topic bilgisi yazılır.

---

## Event-Driven Architecture

**Gün 41 — EDA Nedir?**

Sistemin bileşenleri birbirine direkt çağrı (request-response) yerine event yayarak iletişim kurar. Üretici (producer) event'i publish eder ve tüketicinin (consumer) kim olduğunu bilmez. Avantajlar: loose coupling, asenkron işleme, kolay genişletilebilirlik. Dezavantajlar: debug ve tracing güçlüğü, eventual consistency, schema evolution karmaşıklığı. Request-response, anlık yanıt gerektiren (API call) durumlarda; EDA, fire-and-forget, paralel işleme veya system decoupling gereken durumlarda tercih edilir.

---

**Gün 42 — Domain Event vs Integration Event**

`Domain Event`: bounded context içinde olan anlamlı bir iş olayıdır. `OrderPlaced`, `PaymentCompleted`. Aggregate tarafından üretilir. `Integration Event`: farklı bounded context'ler (servisler) arasında yayılan olaydır; genellikle domain event'ten türetilir ama daha stabil bir kontrat taşır. Integration event'ler versiyonlanır ve schema değişimlerine karşı korunur. Domain event'ler servis içinde kullanılırken integration event'ler Kafka gibi message broker üzerinden yayınlanır.

---

**Gün 43 — Event Sourcing**

Sistemin durumu state snapshot olarak değil, yaşanan olayların (event) sırası olarak saklanır. Mevcut state, event'ler replay edilerek elde edilir. Avantajlar: tam audit log, geçmiş state'e gidebilme, temporal query. Dezavantajlar: sorgulama karmaşıklığı (CQRS ile çözülür), event schema evolution, eventual consistency. Her şeyi event sourcing ile yapmak gerekmez; yüksek audit ihtiyacı olan, geçmişin önemli olduğu domain'lerde (finans, sigorta, e-ticaret) değer üretir.

---

**Gün 44 — CQRS**

Command ve query modellerini birbirinden ayırır. Command (yazma) tarafı iş kurallarını uygular, event'ler üretir. Query (okuma) tarafı denormalize, okuma için optimize edilmiş view kullanır. Bu view event'lerden beslenerek güncellenir. Event Sourcing olmadan da kullanılabilir. Avantaj: okuma ve yazma ayrı ayrı scale edilebilir, okuma modeli query'ye göre optimize edilebilir. Dezavantaj: eventual consistency, iki model arasındaki senkronizasyon karmaşıklığı.

---

**Gün 45 — Outbox Pattern**

Problem: DB'ye yazma ve Kafka'ya publish etme atomik değildir. DB'ye yazdıktan sonra uygulama çöktüğünde event kaybolur. Çözüm: event'i aynı DB transaction'ı içinde `outbox` tablosuna yaz. Ayrı bir process (CDC tool: Debezium, veya scheduled poller) bu tabloyu okur ve Kafka'ya publish eder. Atomiklik DB transaction ile garanti edilir. Debezium, transaction log (WAL) değişikliklerini okuyarak outbox'tan Kafka'ya güvenilir şekilde iletir.

---

**Gün 46 — Idempotent Consumer**

Aynı mesajın birden fazla kez işlenmesi idempotency ile önlenir. Her event'e benzersiz ID atanır (`UUID`). Consumer, işlemeden önce bu ID'nin daha önce işlenip işlenmediğini kontrol eder (Redis set, DB tablosu). İşlendiyse atlar. At-least-once delivery semantiğinde idempotency zorunludur. Örnek: ödeme event'i iki kez gelirse ikinci kez çift para çekilmemeli; idempotency key ile kontrol edilir.

---

**Gün 47 — Event Schema Evolution**

Avro veya Protobuf ile şema tanımlanır, Schema Registry üzerinden versiyonlanır. `Backward compatibility`: yeni consumer eski mesajı okuyabilir (yeni field eklendi, optional). `Forward compatibility`: eski consumer yeni mesajı okuyabilir (field kaldırıldı). `Full compatibility`: ikisi de. Kırıcı değişiklikler (field type değiştirme, zorunlu field silme) yeni topic oluşturmayı gerektirir. JSON kullanılıyorsa evolution manuel yönetilir; Schema Registry olmadan güvenli evolution zordur.

---

**Gün 48 — Eventual Consistency**

Dağıtık sistemlerde tüm node'ların aynı anda tutarlı olması garanti edilemez; belirli bir zaman sonra tutarlı olunur. Kullanıcıya yansıtma stratejileri: "İşleminiz alındı, kısa süre içinde tamamlanacak" mesajı, optimistic UI update (event alındı sayılarak UI güncellenir), polling veya WebSocket ile state takibi. Tasarım seviyesinde: compensating transaction, saga pattern ile yanlış durumlar düzeltilir.

---

**Gün 49 — Choreography vs Orchestration Saga**

`Choreography`: her servis event'e tepki vererek bir sonraki adımı tetikler. Merkezi koordinatör yoktur. Avantaj: loose coupling. Dezavantaj: iş akışını takip etmek zordur, event döngüsü riski. `Orchestration`: merkezi bir saga orchestrator (genellikle ayrı bir servis veya state machine) adımları yönetir. Avantaj: akış tek yerde görünür, hata yönetimi kolay. Dezavantaj: orchestrator tüm servisleri bilir, coupling artar. Karmaşık, uzun-süreli iş akışlarında orchestration tercih edilir.

---

**Gün 50 — Distributed Tracing ve Correlation ID**

Her istek veya event için benzersiz `traceId` üretilir. Alt işlemlere `spanId` atanır. Bu ID'ler HTTP header'larında (`X-B3-TraceId`) veya Kafka message header'larında taşınır. Micrometer Tracing (eski Spring Cloud Sleuth) bu propagation'ı otomatik yapar. Zipkin veya Jaeger, span'leri toplar ve görselleştirir. Bir kullanıcı isteğinin 5 servis üzerinden izini sürmek mümkün hale gelir. MDC (Mapped Diagnostic Context) ile traceId log satırlarına eklenir.

---

## Unit Testing

**Gün 51 — Unit Test vs Integration Test, Test Pyramid**

Unit test: tek bir sınıfı veya metodu izole ederek test eder; dış bağımlılıklar mock'lanır; hızlı çalışır. Integration test: birden fazla bileşeni gerçek bağımlılıklarla (DB, Kafka) test eder; yavaş. Test piramidi: tabanda çok sayıda hızlı unit test, ortada daha az integration test, tepede az sayıda E2E test. Ters piramit (çok E2E, az unit) maliyet ve yavaşlık üretir. Mikroservislerde "test diamond" önerilir: unit + component test ağır basın.

---

**Gün 52 — mock() vs spy() vs @InjectMocks**

`mock(Service.class)`: tüm metodları stub olan sahte nesne oluşturur; gerçek kod çalışmaz. `spy(new Service())`: gerçek nesneyi sarmalar; stub tanımlanmayan metodlar gerçekten çalışır. `@InjectMocks`: test edilen sınıfı oluşturur ve `@Mock`/`@Spy` ile işaretlenen bağımlılıkları inject eder. `spy()` kısmen gerçek davranış, kısmen mock gerektiğinde kullanılır; ancak tercihen gerçek obje inject edilmesi (constructor injection) daha temizdir.

---

**Gün 53 — @Mock vs @MockBean**

`@Mock` (Mockito): Spring context olmadan çalışır; saf unit test. `@MockBean` (Spring Boot Test): Spring context ayağa kalkar, gerçek bean'in yerine mock koyar. `@MockBean` kullanmak context başlatacağından yavaştır; yalnızca gerçekten context gerektiğinde (controller layer, security filter vb.) tercih edilir. `@WebMvcTest` + `@MockBean` kombinasyonu controller katmanı için idealdir.

---

**Gün 54 — verify() ve Behaviour Verification**

State verification: metodun sonucunu (return değeri, DB kaydı) test eder. Behaviour verification: belirli bir metodun kaç kez ve hangi argümanlarla çağrıldığını test eder. `verify(mailService).send(any(Email.class))`, `verify(repo, times(1)).save(user)`. Aşırı `verify()` kullanımı, implementasyon detaylarına bağımlı ve kırılgan test üretir. Return değeri test edilebiliyorsa state verification tercih edilir.

---

**Gün 55 — ArgumentCaptor**

```java
ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
verify(orderRepo).save(captor.capture());
Order saved = captor.getValue();
assertThat(saved.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
```

Mock'a geçilen argümanın içeriğini yakalamak için kullanılır. `any()` ile doğrulama yeterli olmadığında, geçilen nesnenin iç durumunu assert etmek gerektiğinde tercih edilir.

---

**Gün 56 — Parameterized Test**

```java
@ParameterizedTest
@CsvSource({
    "admin@test.com, true",
    "invalid-email, false",
    ", false"
})
void validateEmail(String email, boolean expected) {
    assertThat(emailValidator.isValid(email)).isEqualTo(expected);
}
```

Aynı test mantığını farklı girdilerle çalıştırmak için kullanılır. `@ValueSource`, `@MethodSource`, `@EnumSource` diğer veri kaynakları. Test kod tekrarını azaltır, edge case'leri sistematik kapsar.

---

**Gün 57 — Code Coverage**

Line coverage: çalıştırılan satır yüzdesi. Branch coverage: if/else dallanmaları. %100 coverage her zaman yeterli değildir; yanlış assert, gereksiz test veya test edilmeyen behavior gizlenebilir. Coverage bir taban belirler (ör. %80), alt limitin altına düşülmemesi için pipeline'a eklenir. Mutation testing (PIT) daha güçlü kalite göstergesidir: küçük kod değişikliklerinde testlerin fail etmesi beklenir.

---

**Gün 58 — JUnit 5 Lifecycle Metodları**

`@BeforeAll`: tüm testlerden önce bir kez çalışır, `static` olmalıdır. `@BeforeEach`: her test metodundan önce çalışır. `@AfterEach`: her test metodundan sonra çalışır. `@AfterAll`: tüm testlerden sonra bir kez çalışır, `static` olmalıdır. `@TestInstance(Lifecycle.PER_CLASS)` ile static zorunluluğu kalkar. DB temizleme genellikle `@AfterEach`'te yapılır.

---

**Gün 59 — Test Double'lar**

`Dummy`: metoda geçilir ama kullanılmaz; null ya da boş nesne. `Stub`: çağrıldığında önceden tanımlanmış değer döner. `Fake`: gerçek implementasyon ama üretim için uygun değil (in-memory DB). `Mock`: beklentiler önceden tanımlanır, doğrulama yapılır. `Spy`: gerçek nesne üzerine wrap, bazı metodlar override edilir. Mockito'nun `mock()` hem Stub hem Mock davranışı sağlar.

---

**Gün 60 — TDD ve Red-Green-Refactor**

`Red`: önce başarısız test yaz. `Green`: testi geçirecek en minimal kodu yaz. `Refactor`: davranışı bozmadan kodu iyileştir; testler güvence ağı. Avantajlar: tasarım test edilebilirlik etrafında şekillenir, regression güvencesi, gereksiz kod yazılmaz. Dezavantajlar: başlangıçta yavaş hissettirirr, belirsiz gereksinim veya exploration fazında zorlaşır. "Test-first" zihniyet, API tasarımını tüketen bakış açısından başlatır.

---

## Component / Integration Testing

**Gün 61 — @SpringBootTest ve webEnvironment**

`MOCK` (varsayılan): gerçek servlet container çalışmaz, MockMvc ile test yapılır; hızlıdır. `RANDOM_PORT`: gerçek HTTP server rastgele port'ta başlar; `TestRestTemplate` veya `WebTestClient` ile test edilir. `DEFINED_PORT`: `server.port`'taki port kullanılır; port çakışması riski var. Tam context gerektiğinde `@SpringBootTest`, yalnızca web katmanı gerektiğinde `@WebMvcTest` tercih edilir.

---

**Gün 62 — MockMvc vs WebTestClient**

`MockMvc`: servlet tabanlı, Spring MVC ile çalışır, synchronous. Uygulama çalışmadan, mock servlet container ile test yapar. `WebTestClient`: reactive ve non-reactive uygulamalar için; `RANDOM_PORT` ile gerçek HTTP üzerinden veya `WebFlux` context ile çalışır; fluent API. Servis henüz ayağa kalkmadan hızlı controller testi için `MockMvc`, gerçek HTTP round-trip için `WebTestClient` tercih edilir.

---

**Gün 63 — Testcontainers**

```java
@Container
static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
    .withDatabaseName("testdb");

@DynamicPropertySource
static void props(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", postgres::getJdbcUrl);
}
```

Test sırasında gerçek Docker container başlatır. H2 in-memory DB'nin üretemeyeceği DB-specific davranışları (JSON sütunlar, stored procedure, constraint) test edebilmeyi sağlar. `@Container` + `@DynamicPropertySource` kombinasyonu JUnit 5 ile entegre çalışır.

---

**Gün 64 — @DataJpaTest**

Yalnızca JPA katmanını yükler (entity, repository); web, service katmanları yüklenmez. Varsayılan olarak H2 in-memory DB kullanır ve her test rollback edilir. `@AutoConfigureTestDatabase(replace = NONE)` ile gerçek DB (veya Testcontainers) kullanılabilir. Repository metodlarının doğru sonuç döndürdüğünü test etmek için hızlı ve odaklı bir çözümdür.

---

**Gün 65 — @WebMvcTest**

Yalnızca MVC katmanını yükler: `@Controller`, `@ControllerAdvice`, `Filter`, `WebMvcConfigurer`. Service ve repository bean'leri yüklenmez; `@MockBean` ile sağlanmalıdır. HTTP request/response, validation, serialization, exception handling testleri için idealdir. `@SpringBootTest` + `MockMvc`'den çok daha hızlıdır çünkü context minimumdur.

---

**Gün 66 — @EmbeddedKafka**

```java
@EmbeddedKafka(partitions = 1, topics = {"orders"})
@SpringBootTest
class OrderConsumerTest {
    @Autowired KafkaTemplate<String, Order> template;

    @Test
    void shouldConsumeOrder() {
        template.send("orders", new Order(...));
        // await + assert
    }
}
```

Gerçek Kafka broker'ı olmadan, test sürecinde in-memory Kafka başlatır. Producer-consumer entegrasyonunu, retry ve DLT davranışını test etmek için kullanılır.

---

**Gün 67 — Consumer Driven Contract Testing (Pact)**

Consumer (frontend/microservice) beklentisini "contract" olarak tanımlar ve Pact Broker'a yükler. Provider (API), bu contract'ı kendi pipeline'ında verify eder. Tam E2E test yerine geçer; servisler birbirinden bağımsız test edilebilir. Kırıcı API değişikliklerini erken yakalar. Spring Boot ile `@PactTestFor` provider-side verification için kullanılır.

---

**Gün 68 — WireMock**

```java
stubFor(get(urlEqualTo("/payment/validate"))
    .willReturn(aResponse().withStatus(200).withBody("{\"valid\":true}")));
```

Harici HTTP servislerin test ortamında simüle edilmesini sağlar. Timeout, 500 error, yavaş yanıt gibi hata senaryoları kolayca üretilir. `@WireMockTest` (JUnit 5 extension) ile Spring Boot entegrasyonu sağlanır. Gerçek servise bağımlı olmadan resilience senaryoları test edilebilir.

---

**Gün 69 — Test'te @Transactional Rollback**

Test metoduna `@Transactional` konulduğunda Spring, test tamamlandığında transaction'ı otomatik rollback eder. Bu sayede testler birbirini etkilemez. Dikkat: `@Transactional` test metodunda açıktır; `REQUIRES_NEW` ile açılan iç transaction'lar rollback edilmez. Testcontainers ile gerçek DB kullanıldığında da bu davranış geçerlidir. `@Commit` ile rollback devre dışı bırakılabilir (nadir kullanım).

---

**Gün 70 — E2E vs Component Test**

E2E test: tüm sistemin baştan sona çalışmasını test eder; gerçek dış bağımlılıklar dahildir; yavaş, pahalı, kırılgan. Component test: tek bir mikroservisi dış bağımlılıkları mock/stub ile izole ederek test eder; hızlıdır, CI'da koşabilir. Mikroservis mimarisinde test piramidi şöyle şekillenebilir: %50 unit, %40 component/integration, %10 E2E. E2E'ye çok yatırım yapmak yavaş pipeline ve flaky test sorununa yol açar.

---

## Microservices

**Gün 71 — Service Discovery**

`Client-side discovery`: client, service registry'den (Eureka) adresi kendisi alır ve doğrudan çağırır. Load balancing client tarafında yapılır. `Server-side discovery`: client load balancer'a (AWS ALB, Kubernetes Service) gider; nereye gideceğini bilmez. Kubernetes'te DNS tabanlı server-side discovery yerleşik gelir; Eureka Kubernetes ortamında genellikle gereksizdir.

---

**Gün 72 — API Gateway ve BFF**

API Gateway tek giriş noktasıdır: routing, auth, rate limiting, SSL termination, logging. İstemci her servisi ayrı ayrı çağırmaz. `BFF (Backend for Frontend)`: farklı client tipleri (mobile, web, partner) için ayrı gateway'ler. Her BFF kendi client'ının ihtiyacına göre response şekillendirir. Web BFF daha zengin veri dönerken mobile BFF daha az veri, daha küçük payload döner. Ekip başına BFF pattern'i (micro-frontend) olarak da kullanılır.

---

**Gün 73 — Circuit Breaker**

`CLOSED`: normal durum, tüm istekler geçer. Hata oranı eşiği aşılırsa `OPEN`'a geçer. `OPEN`: istekler engellenir, fallback döner; servis yenilenmesi için beklenir. `HALF_OPEN`: belirli sayıda istek geçirilir; başarılıysa `CLOSED`'a döner, değilse `OPEN` kalır. Resilience4j Spring Boot Starter ile `@CircuitBreaker` annotasyonu kullanılır. Timeout, retry ve bulkhead ile birlikte layered resilience oluşturulur.

---

**Gün 74 — Distributed Transaction**

`2PC (Two-Phase Commit)`: koordinatör tüm katılımcıları önce prepare, sonra commit eder. Senkron, bloke edici, koordinatör single point of failure. Mikroservislerde genellikle uygun değildir. `Saga Pattern`: transaction küçük lokal transaction'lara bölünür; hata durumunda compensating transaction (geri alma adımları) çalıştırılır. Asenkron, daha yüksek availability, ancak eventual consistency. Saga tercih sebebi: servisler bağımsız, farklı DB kullanıyor olabilir.

---

**Gün 75 — Bulkhead Pattern**

Bir servisin kaynak sorunun tüm sistemi etkilememesi için kaynakların izole edilmesidir. `Thread pool isolation`: her bağımlılık için ayrı thread pool; bir havuz dolsa diğerleri çalışmaya devam eder (Hystrix). `Semaphore isolation`: concurrent çağrı sayısını sınırlar; daha az overhead. Resilience4j `@Bulkhead` annotasyonu ile yapılandırılır. Kritik ve kritik olmayan işlemler ayrı havuzlarda tutulur.

---

**Gün 76 — Mikroservislerde Auth**

`Gateway Auth`: JWT gateway'de doğrulanır, içerideki servisler güvenir. Servisler arası çağrılarda JWT veya mTLS kullanılır. `JWT propagation`: HTTP `Authorization: Bearer <token>` header'ı downstream servislere taşınır; Spring Security `SecurityContext`'ten okunur. `OAuth2 Resource Server`: her servis token'ı kendi doğrulayabilir. Service-to-service için `client_credentials` grant type + scope kontrolü kullanılır.

---

**Gün 77 — Service Mesh**

Sidecar proxy (Envoy) her pod'a eklenir; network trafiği uygulamadan bağımsız olarak yönetilir: mTLS, circuit breaking, retry, observability. Uygulama koduna dokunmadan cross-cutting concerns sağlanır. API Gateway'den farkı: gateway kuzey-güney (dış-iç) trafiği yönetirken service mesh doğu-batı (servisler arası) trafiği yönetir. İkisi birlikte kullanılır. Karmaşıklığı artırır; küçük sistemler için Kubernetes native networking yeterlidir.

---

**Gün 78 — Sync vs Async İletişim**

Sync (REST/gRPC): anlık yanıt gerektiğinde (ödeme onayı, auth kontrolü), basit implementation, kolay debug. Dezavantaj: caller bloklenir, downstream çökerse caller etkilenir. Async (Kafka/RabbitMQ): fire-and-forget, producer consumer'ın hızına uymak zorunda değil, loose coupling. Dezavantaj: eventual consistency, daha karmaşık error handling. Karar kriteri: yanıt anında gerekli mi? Caller bloklansa sorun olur mu? Servisler arasında temporal decoupling değer katıyor mu?

---

**Gün 79 — Database Per Service**

Her servis kendi DB'sine sahiptir; diğer servislerin DB'sine direkt erişemez. Avantaj: servisler bağımsız, farklı DB teknolojileri seçilebilir, schema değişikliği diğer servisleri etkilemez. `Shared database` anti-pattern: iki servis aynı tabloyu paylaşırsa ayrı deploy edilemez, schema değişikliği her iki servisi kırar. Veri paylaşımı API veya event üzerinden yapılır. Raporlama için read-only replica veya data warehouse kullanılır.

---

**Gün 80 — Strangler Fig Pattern**

Monoliti yıkmadan yavaş yavaş parça parça mikroservise taşıma stratejisi. API Gateway veya facade önüne konur; yeni özellikler mikroservis olarak yazılır, eski özellikler monolitten yavaşça taşınır. Monolith zamanla "boğulur" (strangled). Risk: yıllarca iki sistemin birlikte çalışması gerekebilir. Alternatif: "Big Bang" rewrite — riskli, önerilmez. Strangler Fig kademeli, geri dönülebilir, production kesintisi minimumda.

---

## DevOps

**Gün 81 — CI/CD**

`CI (Continuous Integration)`: her commit'te otomatik build ve test çalışır; entegrasyon sorunları erken yakalanır. `CD (Continuous Delivery)`: her değişiklik production'a deploy'a hazır hale getirilir; deploy manuel onay gerektirebilir. `Continuous Deployment`: her geçen test sonrası otomatik production'a çıkar; onay gerektirmez. Tipik pipeline: checkout → build → unit test → integration test → Docker image build → push registry → deploy staging → smoke test → deploy prod.

---

**Gün 82 — Docker Kavramları**

`Image`: dosya sistemi katmanlarından oluşan immutable şablon. `Container`: image'den oluşturulan çalışan process. `COPY`: dosyaları build context'ten image'e kopyalar; güvenli. `ADD`: COPY + URL indirme + tar çıkarma; gereksiz özellikler güvenlik riski taşır; tercih edilmez. `CMD`: çalışacak default komutu belirler; override edilebilir. `ENTRYPOINT`: container'ın ana executable'ını belirler; her zaman çalışır, `CMD` argüman olarak eklenir.

---

**Gün 83 — Kubernetes Temel Kavramlar**

`Pod`: bir veya daha fazla container içeren en küçük deploy birimi; geçici, IP'si değişir. `Deployment`: pod'ların desired state'ini yönetir; rolling update, rollback sağlar. `Service`: pod'ların önünde stabil IP/DNS sağlar; `ClusterIP` (iç), `NodePort` (dış erişim için), `LoadBalancer` (cloud LB). `Ingress`: HTTP/HTTPS routing kuralları; host/path bazlı yönlendirme, TLS termination.

---

**Gün 84 — Liveness vs Readiness Probe**

`Readiness probe`: servis trafik almaya hazır mı? Başarısız olursa Service endpoint'ten çıkarılır; trafik gelmez ama pod çalışmaya devam eder. `Liveness probe`: servis hala canlı mı? Başarısız olursa pod yeniden başlatılır. Yanlış yapılandırma: liveness probe fazla agresif ayarlanırsa normal yavaşlamada pod sürekli restart eder (crash loop). Startup probe: yavaş başlayan uygulamalarda ilk başlangıç süresini vermek için kullanılır.

---

**Gün 85 — Blue-Green vs Canary Deployment**

`Blue-Green`: iki özdeş ortam (blue=mevcut, green=yeni). Traffic switch ile anlık geçiş; rollback koloy. Maliyet: iki ortam aynı anda çalışır. `Canary`: yeni sürüm trafiğin küçük yüzdesine (%5-10) yönlendirilir, metrikler izlenir, sorun yoksa kademeli artırılır. Risk erken yakalanır, tam rollout öncesi gerçek kullanıcıyla test edilir. Feature flag ile canary release yapılabilir. Kubernetes'te Argo Rollouts bu stratejileri otomatize eder.

---

**Gün 86 — ConfigMap ve Secret Yönetimi**

`ConfigMap`: non-sensitive konfigürasyon (url, port, feature flag). Env variable veya dosya olarak mount edilir. `Secret`: sensitive veri (şifre, token); base64 encode edilir ama bu şifreleme değildir. Best practice: Secret'ları Git'te tutma, Vault (HashiCorp) veya cloud KMS (AWS Secrets Manager, Azure Key Vault) ile yönet. External Secrets Operator, vault'taki secret'ları Kubernetes Secret'a senkronize eder. RBAC ile Secret erişimi sınırlandırılır.

---

**Gün 87 — Horizontal Pod Autoscaler (HPA)**

CPU ve memory gibi resource metric'lere veya custom metric'lere göre replica sayısını otomatik ayarlar. `kubectl autoscale deployment api --cpu-percent=70 --min=2 --max=10`. Custom metric için Prometheus Adapter veya KEDA kullanılır: Kafka topic'teki mesaj sayısına göre consumer pod scale edilebilir. `Vertical Pod Autoscaler (VPA)` ise CPU/memory request'lerini otomatik ayarlar; HPA ile birlikte dikkatli kullanılmalıdır.

---

**Gün 88 — GitOps**

Git repository, istenen sistem durumunun tek kaynağı olarak kullanılır. `Push-based`: CI pipeline değişikliği cluster'a push eder; CI'nın cluster credential'larına sahip olması gerekir. `Pull-based`: cluster içinde bir agent (ArgoCD, Flux) Git'i izler, fark varsa otomatik uygular. ArgoCD, Git'teki state ile cluster'daki state arasındaki drift'i görselleştirir ve sync eder. Avantaj: audit trail Git'te, rollback `git revert`, cluster credential'ları dışarıya çıkmaz.

---

**Gün 89 — Helm Chart**

Kubernetes manifest'lerini parametrize edilmiş şablonlar olarak paketler. `values.yaml` ile ortama göre farklı konfigürasyon (replica sayısı, image tag, resource limit). `helm install`, `helm upgrade`, `helm rollback` ile yaşam döngüsü yönetilir. Artifact Hub'da hazır chart'lar mevcuttur (PostgreSQL, Redis). `Helmfile` birden fazla chart'ı birlikte yönetir. Dezavantaj: Go template sözdizimi karmaşıklaşabilir; büyük chart'larda Kustomize veya Jsonnet alternatif olarak değerlendirilir.

---

**Gün 90 — Observability'nin Üç Sütunu**

`Logs`: olayların metin kaydı; structured logging (JSON) ile filtreleme kolaylaşır. ELK (Elasticsearch + Logstash + Kibana) veya Loki + Grafana. `Metrics`: sayısal ölçümler, zaman serisi (request rate, latency, error rate). Prometheus scrape eder, Grafana görselleştirir. `Traces`: bir isteğin tüm servislerdeki yolculuğu; span'ler ve latency breakdown. Jaeger veya Zipkin. Three pillars birbirini tamamlar: metric alarm tetikler → log filtrelenir → trace ile root cause bulunur.

---

## Architectural Decisions

**Gün 91 — CAP Teoremi**

Dağıtık sistemler üç özellikten yalnızca ikisini garanti edebilir: `Consistency` (her okuma en son yazmayı görür), `Availability` (her istek yanıt alır, hata olmayabilir), `Partition Tolerance` (ağ bölünmesinde sistem çalışır). Ağ bölünmesi pratikte kaçınılmazdır; gerçek seçim C vs A arasındadır. `CP` sistem (ZooKeeper, HBase): bölünmede availability'yi feda eder. `AP` sistem (Cassandra, DynamoDB): bölünmede tutarsız veri dönebilir. PACELC teoremi, normal koşulda latency-consistency trade-off'unu da modeller.

---

**Gün 92 — Mimari Seçim ve Conway's Law**

Conway's Law: sistemin mimarisi, onu üreten organizasyonun iletişim yapısını yansıtır. 5 kişilik ekip için mikroservis operasyonel yük üretir, monolith daha uygundur. Modular monolith: tek deploy ama net module sınırları; refactoring daha kolay, team boundary'leri izin verirse mikroservise evrilebilir. Ekip bağımsız deploy, bağımsız scale ve farklı teknoloji kullanmak istiyorsa mikroservis değer üretir. "Microservices by default" anti-pattern'dir.

---

**Gün 93 — Sync vs Async İletişim Kararı**

Anlık yanıt gerekiyorsa (ödeme sonucu, auth): sync. Caller'ın beklememesi gerekiyorsa veya downstream servisin hızı farklıysa: async. Sistemler arasında temporal decoupling değerliyse (sipariş servisi bildirim servisinin ayakta olmasına bağımlı olmamalı): async. Ordering garanti edilmesi gerekiyor ama sync uygunsuzsa: Kafka. Basit bildirim, fire-and-forget: RabbitMQ veya Kafka. Sync timeout'ları ve retry'lar karmaşıklık ekler; async ise eventually consistent tasarım gerektirir.

---

**Gün 94 — EDA ne zaman, Request-Response ne zaman?**

EDA: birden fazla servisin aynı olaya tepki vermesi gerektiğinde (fan-out), iş akışı asenkron yürütülebiliyorsa, servisler arasında loose coupling kritikse, audit log veya event replay değerliyse. Request-Response: anlık yanıt zorunluysa, iş akışı senkron ve basitse, tek tüketici varsa, debug ve tracing basit tutulmak isteniyorsa. Hibrit yaklaşım yaygındır: kullanıcı isteği sync, arka planda işlemler async.

---

**Gün 95 — API Versioning Stratejileri**

`URL versioning` (`/api/v1/orders`): açık, kolay cache, RESTful değil ama yaygın ve pratik. `Header versioning` (`Accept: application/vnd.company.v2+json`): URL temiz kalır, ancak test ve tarayıcı erişimi zorlaşır. `Query param` (`?version=2`): kolay ama URL'i kirletir. `Media type`: en RESTful ama en karmaşık. Öneri: breaking change varsa yeni versiyon, non-breaking change (field ekleme) için mevcut versiyonda backward compatible değişiklik. Deprecation politikası önceden duyurulmalıdır.

---

**Gün 96 — Eventual Consistency Kabul Edilemediğinde**

Strong consistency gerektiren durumlar için: tek servis içinde ACID transaction (bounded context tasarımını gözden geçir), 2PC (sınırlı senaryolarda), Saga + compensating transaction ile "best effort" tutarlılık. Mimari kararlar: consistency gerektiren verileri aynı bounded context'e taşı; farklı servislere yayılmış ACID ihtiyacı tasarım hatasının işareti olabilir. Kullanıcıya optimistic lock hatası veya retry mekanizması sunulabilir. Consensus protocol (Raft, Paxos) ile strong consistency—ancak latency artar.

---

**Gün 97 — Caching Stratejileri**

`Cache-aside` (Lazy Loading): uygulama önce cache'e bakar, miss ise DB'den okur, cache'e yazar. En yaygın. `Write-through`: yazma cache ve DB'ye eş zamanlı yapılır; tutarlı ama her yazma maliyetli. `Write-behind` (Write-back): yazma önce cache'e, sonra async DB'ye. Hızlı ama kayıp riski. `Read-through`: cache miss'te cache kendi DB'den okur. Cache invalidation: TTL tabanlı, event-driven (DB'ye yazılınca cache temizle), versioned key. "There are only two hard things in CS: cache invalidation and naming things."

---

**Gün 98 — ADR (Architecture Decision Record)**

Mimari kararları, bağlamı ve gerekçesiyle birlikte belgeler. Format: başlık, status (proposed/accepted/deprecated), context (neden bu karar?), decision (ne seçildi?), consequences (sonuçlar, trade-off'lar). Git repository'de `docs/adr/` altında tutulur; kod ile birlikte versiyonlanır. Neden önemli: yeni ekip üyeleri kararların gerekçesini anlar, zaman içinde karar revisit edilebilir, tekrar eden tartışmalar önlenir.

---

## Design Patterns

**Gün 99 — Repository Pattern**

Domain logic ile data access logic'ini birbirinden ayırır. Domain katmanı repository interface'ini bilir; implementasyonu bilmez. Spring Data JPA, interface tanımından otomatik implementasyon üretir: `findByEmailAndActiveTrue()` → SQL. Doğrudan `EntityManager` kullanmak yerine Repository tercih edilmesi: soyutlama sağlar, test sırasında mock injection kolaylaşır, persistence teknolojisi değiştiğinde domain katmanı etkilenmez.

---

**Gün 100 — Strategy vs Template Method Pattern**

`Strategy`: algoritmanın tamamı interface olarak soyutlanır; runtime'da farklı implementasyon inject edilir. Open/Closed Principle'ı destekler. Spring'de: farklı ödeme sağlayıcıları (`PayPalStrategy`, `StripeStrategy`) aynı interface'den gelir; `@Qualifier` veya map-based injection ile seçilir.

`Template Method`: algoritmanın iskelet adımları abstract sınıfta tanımlanır, alt sınıflar belirli adımları override eder. Adım sırası sabittir. Spring'de: `AbstractController`, `JdbcTemplate` (`execute()` metodu şablonu yönetir, spesifik SQL caller'dan gelir).

```java
// Strategy — Spring örneği
Map<String, PaymentStrategy> strategies; // bean name → impl
strategies.get("paypal").pay(amount);

// Template Method — JdbcTemplate
jdbcTemplate.query("SELECT * FROM orders", rs -> {
    // bu lambda bir "template step"
});
```

Fark özeti: Strategy tüm algoritmayı değiştirir; Template Method algoritmanın içindeki bazı adımları değiştirir.
