# Java Stream API — 20 Pratik Örnek: CEVAPLAR

---

## Bölüm 1 — Temel İşlemler (1–5)

---

### Cevap 1 — Listeyi filtrelemek

```java
List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Ahmet", "Hüsna <3");

List<String> result = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList());

// [Ali, Ayşe, Ahmet]
```

**Açıklama:**
- `stream()` → koleksiyonu stream'e çevirir.
- `filter(predicate)` → koşulu sağlamayan elemanları eleme işlemidir (intermediate, lazy).
- `collect(Collectors.toList())` → terminal operasyondur; stream'i çalıştırır ve sonucu toplar.

> Java 16+ için `collect(Collectors.toList())` yerine `.toList()` kısaltması kullanılabilir — **unmodifiable** liste döner.

---

### Cevap 2 — Dönüştürme (map)

```java
List<String> names = List.of("ali", "ayşe", "mehmet");

List<String> upper = names.stream()
    .map(String::toUpperCase)   // method reference
    .collect(Collectors.toList());

// [ALI, AYŞE, MEHMET]
```

**Açıklama:**
- `map(Function<T, R>)` her elemanı başka bir değere dönüştürür (1-to-1).
- `String::toUpperCase` → method reference; `s -> s.toUpperCase()` ile eşdeğerdir, daha okunabilir.

---

### Cevap 3 — Koşullu toplama

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Yol 1 — mapToInt + sum (önerilen)
int sum1 = numbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();

// Yol 2 — reduce
int sum2 = numbers.stream()
    .filter(n -> n % 2 == 0)
    .reduce(0, Integer::sum);

// Yol 3 — Collectors.summingInt
int sum3 = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.summingInt(Integer::intValue));

// Üçü de: 30
```

**⚠️ Püf nokta — mapToInt vs reduce:**
- `mapToInt()` → `IntStream` döner; `sum()`, `average()`, `min()`, `max()`, `summaryStatistics()` primitive operasyonları sağlar. Boxing/unboxing overhead'i yoktur.
- `reduce(0, Integer::sum)` → her adımda `Integer` boxing yapar; büyük listede performans kaybı olur.
- Sayısal toplama işlemlerinde her zaman `mapToInt` / `mapToLong` / `mapToDouble` tercih edilmelidir.

---

### Cevap 4 — Sıralama: Ürünleri fiyata göre sırala

```java
List<String> sortedNames = products.stream()
    .sorted(Comparator.comparingDouble(Product::price))
    .map(Product::name)
    .collect(Collectors.toList());

// [Mouse, Chair, Desk, Laptop]
```

**Açıklama:**
- `sorted(Comparator)` → intermediate, stable sort (merge sort tabanlı).
- `Comparator.comparingDouble()` → double field için özel comparator; `comparing()` ile de çalışır ama boxing yapar.
- `.map(Product::name)` → sıralama sonrası sadece isim alanı alınır.

> Büyükten küçüğe için: `.sorted(Comparator.comparingDouble(Product::price).reversed())`

---

### Cevap 5 — İlk eşleşeni bulmak

```java
Optional<Product> found = products.stream()
    .filter(p -> p.price() > 1000)
    .findFirst();

found.ifPresent(p -> System.out.println(p.name())); // Laptop
```

**⚠️ Püf nokta — findFirst() vs findAny():**

| | `findFirst()` | `findAny()` |
|---|---|---|
| Sequential stream | İlk eşleşeni döner | İlk eşleşeni döner (genellikle) |
| Parallel stream | Sıranın ilk eşleşenini döner (daha yavaş) | Herhangi birini döner (daha hızlı) |
| Kullanım | Sıra önemliyse | Paralel + sıra önemsizse |

Paralel stream'de `findFirst()` thread koordinasyonu gerektirdiğinden daha yavaştır. Sıra önemsizse `findAny()` tercih edilmelidir.

---

## Bölüm 2 — Gruplama ve Toplama (6–10)

---

### Cevap 6 — Kategoriye göre ürünleri grupla

```java
Map<String, List<Product>> grouped = products.stream()
    .collect(Collectors.groupingBy(Product::category));

// {"Electronics": [Laptop, Mouse, Monitor], "Furniture": [Desk, Chair]}
```

**⚠️ Püf nokta — groupingBy vs partitioningBy:**

| | `groupingBy` | `partitioningBy` |
|---|---|---|
| Key tipi | Herhangi bir tür | Yalnızca `Boolean` (true/false) |
| Grup sayısı | N adet | Her zaman 2 |
| Kullanım | Kategori, department vb. | İki gruba bölme (aktif/pasif) |

```java
// partitioningBy örneği
Map<Boolean, List<Product>> partitioned = products.stream()
    .collect(Collectors.partitioningBy(p -> p.price() > 2000));
// {true: [Laptop, Monitor, Desk], false: [Mouse, Chair]}
```

---

### Cevap 7 — Gruplama + sayma

```java
Map<String, Long> countByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::category,
        Collectors.counting()       // downstream collector
    ));

// {"Electronics": 3, "Furniture": 2}
```

**Açıklama:**
- `groupingBy(classifier, downstream)` ikinci argüman her grup üzerinde uygulanacak collector'dır.
- `Collectors.counting()` → `Long` döner.
- Diğer downstream collector'lar: `summingInt`, `averagingDouble`, `joining`, `toList`, `maxBy`, `minBy`.

---

### Cevap 8 — Gruplama + ortalama fiyat

```java
Map<String, Double> avgPriceByCategory = products.stream()
    .collect(Collectors.groupingBy(
        Product::category,
        Collectors.averagingDouble(Product::price)
    ));

// {"Electronics": 6583.33, "Furniture": 2250.0}
```

**Bonus — IntSummaryStatistics ile tam istatistik:**
```java
Map<String, DoubleSummaryStatistics> statsMap = products.stream()
    .collect(Collectors.groupingBy(
        Product::category,
        Collectors.summarizingDouble(Product::price)
    ));

// Her kategori için count, sum, min, max, average bir arada
statsMap.get("Electronics").getAverage(); // 6583.33
statsMap.get("Electronics").getMax();     // 15000.0
```

---

### Cevap 9 — flatMap: Tüm ürünleri düzleştir

```java
List<String> allProductNames = orders.stream()
    .flatMap(order -> order.products().stream())  // List<Product> → Stream<Product>
    .map(Product::name)
    .collect(Collectors.toList());

// [Laptop, Mouse, Desk, Chair, Monitor]
```

**⚠️ Püf nokta — map vs flatMap:**

```java
// map() kullansaydık:
Stream<List<Product>> nestedStream = orders.stream()
    .map(Order::products);
// → Stream<List<Product>>, iç içe geçmiş stream — kullanışsız

// flatMap() her List<Product>'ı açar ve tek bir Stream<Product> elde edilir
Stream<Product> flatStream = orders.stream()
    .flatMap(o -> o.products().stream());
// → Stream<Product>, düzleştirilmiş
```

`flatMap` şu zinciri otomatik yapar: her elemana `map` uygula → sonuçları tek stream'de birleştir (flatten).

---

### Cevap 10 — flatMap + filter: Gönderilmiş siparişlerin Electronics ürünleri

```java
List<Product> result = orders.stream()
    .filter(Order::shipped)                              // gönderilmiş siparişler
    .flatMap(order -> order.products().stream())         // ürünleri düzleştir
    .filter(p -> p.category().equals("Electronics"))     // Electronics filtresi
    .collect(Collectors.toList());

// [Laptop, Mouse, Monitor]
```

**Not:** `filter` → `flatMap` → `filter` zinciri lazy çalışır; her eleman pipeline'dan tek tek geçer. Ara listeler oluşturulmaz.

---

## Bölüm 3 — İleri Düzey Zincirler (11–15)

---

### Cevap 11 — Distinct + sorted + limit

```java
List<Integer> numbers = List.of(5, 3, 1, 3, 7, 1, 9, 5, 2, 8, 2);

List<Integer> result = numbers.stream()
    .distinct()        // tekrarları kaldır: [5, 3, 1, 7, 9, 2, 8]
    .sorted()          // küçükten büyüğe: [1, 2, 3, 5, 7, 8, 9]
    .limit(5)          // ilk 5: [1, 2, 3, 5, 7]
    .collect(Collectors.toList());

// [1, 2, 3, 5, 7]
```

**Operasyon sırası önemlidir:**
- `distinct()` → `sorted()` → `limit()` : önce tekrarlar çıkar, sonra sırala, sonra kes — verimli.
- `sorted()` → `distinct()` → `limit()` : önce tüm listeyi sırala (pahalı), sonra tekrar çıkar — verimsiz.

---

### Cevap 12 — reduce: Toplam stok değeri

```java
double totalStockValue = products.stream()
    .mapToDouble(p -> p.price() * p.stock())
    .sum();
// 229000.0

// reduce() ile:
double totalStockValue2 = products.stream()
    .reduce(0.0,
        (acc, p) -> acc + p.price() * p.stock(),   // accumulator
        Double::sum);                                // combiner (paralel için)
// 229000.0
```

**⚠️ Püf nokta — reduce(identity, acc) vs reduce(acc):**

```java
// 2 argümanlı: identity değeri var → T döner (Optional değil)
int sum = numbers.stream().reduce(0, Integer::sum);  // boş liste → 0

// 1 argümanlı: identity yok → Optional<T> döner
Optional<Integer> sum2 = numbers.stream().reduce(Integer::sum); // boş liste → Optional.empty()
```

Identity değeri yoksa boş liste için varsayılan döndürülemez, bu yüzden `Optional` kullanılır. Identity değeri yanlış seçilirse (örneğin çarpma için 0 yerine 1 seçilmeli) hatalı sonuç üretilir.

---

### Cevap 13 — Collecting to Map

```java
Map<String, Double> salaryMap = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,    // key extractor
        Employee::salary   // value extractor
    ));

// {"Ali": 15000, "Ayşe": 18000, "Mehmet": 12000, "Hüsna <3": 13500, "Can": 20000}
```

**⚠️ Püf nokta — Duplicate key sorunu:**

```java
// İki kişi aynı ismi taşısaydı:
employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Employee::salary
    ));
// → IllegalStateException: Duplicate key Ali

// Çözüm: merge function ile çakışmayı yönet
Map<String, Double> safeMap = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Employee::salary,
        (existing, replacement) -> existing   // ikincisini yoksay
    ));

// Sıralı map döndürmek için LinkedHashMap
Map<String, Double> linkedMap = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Employee::salary,
        (e, r) -> e,
        LinkedHashMap::new
    ));
```

---

### Cevap 14 — Her departmandaki en yüksek maaşlı çalışan

```java
Map<String, Optional<Employee>> topEarners = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.maxBy(Comparator.comparingDouble(Employee::salary))
    ));

// {"Engineering": Optional[Can(20000)], "HR": Optional[Hüsna <3(13500)]}
```

**Optional olmadan sonuç için:**
```java
Map<String, Employee> topEarnersUnwrapped = employees.stream()
    .collect(Collectors.toMap(
        Employee::department,
        e -> e,
        BinaryOperator.maxBy(Comparator.comparingDouble(Employee::salary))
    ));
```

**Açıklama:** `maxBy` downstream collector olduğu için `Optional` döner — grup boş olabilir. `toMap` + merge function yönteminde ise `BinaryOperator` ile iki değer arasında max seçilir, `Optional` olmaz.

---

### Cevap 15 — String birleştirme: Joining

```java
String engineeringNames = employees.stream()
    .filter(e -> e.department().equals("Engineering"))
    .map(Employee::name)
    .sorted()                               // alfabetik sıra için
    .collect(Collectors.joining(", "));     // delimiter

// "Ali, Ayşe, Can"

// Prefix ve suffix ile:
String withBrackets = employees.stream()
    .filter(e -> e.department().equals("Engineering"))
    .map(Employee::name)
    .collect(Collectors.joining(", ", "[", "]"));

// "[Ali, Ayşe, Can]"
```

**⚠️ Püf nokta — Collectors.joining() vs String.join():**

| | `Collectors.joining()` | `String.join()` |
|---|---|---|
| Giriş | `Stream<String>` | `Iterable<String>` veya varargs |
| Pipeline içinde kullanım | ✅ Doğrudan entegre | ❌ Önce `collect(toList())` gerekir |
| Prefix/Suffix desteği | ✅ | ❌ |
| Kullanım yeri | Stream zincirine dahil | Hazır koleksiyonda hızlı birleştirme |

---

## Bölüm 4 — Karmaşık Senaryolar (16–20)

---

### Cevap 16 — Çoklu kriter sıralama

```java
List<Employee> sorted = employees.stream()
    .sorted(
        Comparator.comparing(Employee::department)              // önce departman (A-Z)
            .thenComparing(Comparator.comparingDouble(Employee::salary).reversed())  // sonra maaş (büyükten küçüğe)
    )
    .collect(Collectors.toList());

// Engineering → Can(20000), Ayşe(18000), Ali(15000)
// HR          → Hüsna <3(13500), Mehmet(12000)
```

**⚠️ Püf nokta — .reversed() kapsamı:**

```java
// YANLIŞ — reversed() tüm zinciri etkiler
Comparator.comparing(Employee::department)
    .thenComparingDouble(Employee::salary)
    .reversed();
// → department da ters sıralanır!

// DOĞRU — sadece salary kısmını reverse et
Comparator.comparing(Employee::department)
    .thenComparing(
        Comparator.comparingDouble(Employee::salary).reversed()
    );
```

`.reversed()` çağrıldığı comparator'ın tamamını tersine çevirir. Sadece belirli kriteri ters çevirmek için o kriteri ayrı `Comparator` olarak oluşturup `thenComparing`'e geçirmek gerekir.

---

### Cevap 17 — Nested groupingBy

```java
Map<String, Map<String, List<Employee>>> nestedGroup = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,               // birinci kriter: departman
        Collectors.groupingBy(              // downstream = ikinci gruplama
            e -> e.age() < 30 ? "Junior" : "Senior"
        )
    ));

// {"Engineering" → {"Junior": [Ali], "Senior": [Ayşe, Can]},
//  "HR"          → {"Junior": [Mehmet], "Senior": [Hüsna <3]}}
```

**Okunabilirlik için yardımcı metot:**
```java
private static String ageGroup(Employee e) {
    return e.age() < 30 ? "Junior" : "Senior";
}

// Kullanım:
.collect(Collectors.groupingBy(Employee::department,
         Collectors.groupingBy(Main::ageGroup)));
```

---

### Cevap 18 — Partition + toplam maaş

```java
Map<Boolean, Double> totalSalaryByPartition = employees.stream()
    .collect(Collectors.partitioningBy(
        e -> e.salary() > 15000,                    // true: yüksek, false: düşük
        Collectors.summingDouble(Employee::salary)  // her partition için toplam
    ));

// {true: 53000.0 (Ayşe:18000 + Can:20000 + Hüsna <3:13500... )
// Dikkat: Hüsna <3 13500 < 15000 → false grubunda}
// {true: 38000.0 (Ayşe:18000 + Can:20000), false: 40500.0 (Ali:15000 + Mehmet:12000 + Hüsna <3:13500)}
```

**Not:** 15000 > 15000 koşulu `false` olduğundan Ali false grubundadır. `>=` için `e.salary() >= 15000` kullanılmalıdır.

---

### Cevap 19 — Lazy evaluation tuzağı

**Çıktı:**
```
Stream oluşturuldu
filter: Ali
map: Ali
filter: Ayşe
map: Ayşe
filter: Mehmet
filter: Ahmet
map: Ahmet
Collect tamamlandı: [ALI, AYŞE, AHMET]
```

**Açıklama — iki kritik davranış:**

**1. Lazy evaluation:**
`stream()`, `filter()`, `map()` çağrıları sadece pipeline'ı tanımlar; hiçbir kod çalışmaz. `"Stream oluşturuldu"` terminal operasyondan önce yazdırılır çünkü stream henüz işlenmemiştir. Terminal operasyon (`collect()`) çağrılınca pipeline devreye girer.

**2. Vertical (depth-first) işleme:**
Her eleman pipeline'ı baştan sona geçer; sonra sıradaki eleman alınır. "Ali" için önce filter, sonra map çalışır; ardından "Ayşe" için aynı süreç tekrar eder. Horizontal (breadth-first) değildir: önce tüm liste filter'dan geçmez, sonra map'e girmez.

Bu davranış şu avantajı sağlar: `findFirst()` ile pipeline, ilk eşleşenden sonra durabilir — tüm liste işlenmez.

```java
// Short-circuit örneği: "Ayşe" bulununca durur
Optional<String> first = names.stream()
    .filter(n -> n.startsWith("A"))
    .map(String::toUpperCase)
    .findFirst();
// Sadece Ali ve Ayşe işlenir; Mehmet, Ahmet, Hüsna <3 işlenmez
```

---

### Cevap 20 — Gerçek dünya: Sipariş raporu

**Yol 1 — İki ayrı stream (basit ama stream iki kez tüketilir):**
```java
List<Product> shippedProducts = orders.stream()
    .filter(Order::shipped)
    .flatMap(o -> o.products().stream())
    .collect(Collectors.toList());

double total = shippedProducts.stream()
    .mapToDouble(Product::price)
    .sum();

Optional<Product> mostExpensive = shippedProducts.stream()
    .max(Comparator.comparingDouble(Product::price));

System.out.println("Toplam tutar  : " + total);
System.out.println("En pahalı ürün: " + mostExpensive.map(Product::name).orElse("Yok")
                   + " (" + mostExpensive.map(Product::price).orElse(0.0) + ")");
// Toplam tutar  : 25250.0
// En pahalı ürün: Laptop (15000.0)
```

**⚠️ Püf nokta — Collectors.teeing() ile tek geçiş (Java 12+):**
```java
record Report(double total, Optional<Product> mostExpensive) {}

Report report = orders.stream()
    .filter(Order::shipped)
    .flatMap(o -> o.products().stream())
    .collect(Collectors.teeing(
        Collectors.summingDouble(Product::price),                          // collector 1: toplam
        Collectors.maxBy(Comparator.comparingDouble(Product::price)),      // collector 2: max
        Report::new                                                        // merger: ikisini birleştir
    ));

System.out.println("Toplam tutar  : " + report.total());
System.out.println("En pahalı ürün: " + report.mostExpensive()
                       .map(p -> p.name() + " (" + p.price() + ")")
                       .orElse("Yok"));
// Toplam tutar  : 25250.0
// En pahalı ürün: Laptop (15000.0)
```

**`Collectors.teeing()` nedir?**
Aynı stream'i iki farklı collector'a "tee" (Y-bağlantısı gibi) yönlendirir; her collector kendi sonucunu üretir ve sonunda bir `BiFunction` ikisini birleştirir. Stream yalnızca **bir kez** tüketilir. `sum + count + max` gibi birden fazla sonuç gerektiren durumlarda `DoubleSummaryStatistics` de kullanılabilir:

```java
DoubleSummaryStatistics stats = orders.stream()
    .filter(Order::shipped)
    .flatMap(o -> o.products().stream())
    .mapToDouble(Product::price)
    .summaryStatistics();

stats.getSum();  // 25250.0
stats.getMax();  // 15000.0
stats.getCount(); // 5
stats.getMin();  // 250.0
```

---

## Özet: Stream API Genel Püf Noktaları

| Konu | Doğru Kullanım | Kaçınılacak |
|---|---|---|
| Sayısal işlem | `mapToInt().sum()` | `map().reduce(0, Integer::sum)` |
| Null güvenlik | `.filter(Objects::nonNull)` | NPE riskli işlemler |
| Duplicate key | `toMap` + merge function | Bare `toMap` (exception riski) |
| Reversed sort | Kriter bazlı `Comparator.reversed()` | Zincir sonunda `.reversed()` |
| Çok sonuç | `teeing()` veya `summaryStatistics()` | Stream'i iki kez tüketmek |
| Lazy faydalanma | `findFirst()` + `filter()` | Tüm listeyi topla, sonra al |
| Büyük liste | `parallelStream()` + `findAny()` | `findFirst()` ile paralel |
| String birleştirme | `Collectors.joining()` | `+=` loop veya `String.join()` + toList |
