# Java Stream API — 20 Pratik Örnek

> Basitten karmaşığa sıralanmıştır. "Klasik → Stream" dönüşüm soruları, püf nokta soruları ve ileri seviye zincir sorulardan oluşur.

---

## Hazırlık — Ortak Model Sınıfları

Tüm sorularda aşağıdaki sınıflar kullanılacaktır:

```java
record Product(String name, String category, double price, int stock) {}

record Order(int id, String customerName, List<Product> products, boolean shipped) {}

record Employee(String name, String department, double salary, int age) {}
```

---

## Bölüm 1 — Temel İşlemler (1–5)

---

### Soru 1 — for → stream dönüşümü: Listeyi filtrelemek

Aşağıdaki klasik kodu **Stream API** ile yeniden yaz.

```java
List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Ahmet", "Hüsna <3");

// KLASİK
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("A")) {
        result.add(name);
    }
}
```

**Beklenen çıktı:** `["Ali", "Ayşe", "Ahmet"]`

---

### Soru 2 — for → stream dönüşümü: Dönüştürme (map)

Aşağıdaki klasik kodu **Stream API** ile yeniden yaz.

```java
List<String> names = List.of("ali", "ayşe", "mehmet");

// KLASİK
List<String> upper = new ArrayList<>();
for (String name : names) {
    upper.add(name.toUpperCase());
}
```

**Beklenen çıktı:** `["ALI", "AYŞE", "MEHMET"]`

---

### Soru 3 — if/else → stream: Koşullu toplama

Aşağıdaki kodu Stream ile yeniden yaz.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// KLASİK
int sum = 0;
for (int n : numbers) {
    if (n % 2 == 0) {
        sum += n;
    }
}
// sum = 30
```

**Beklenen çıktı:** `30`

> 💡 **Püf nokta:** `mapToInt` + `sum()` mı, yoksa `reduce()` mı kullanmalısın?

---

### Soru 4 — Sıralama: Ürünleri fiyata göre sırala

```java
List<Product> products = List.of(
    new Product("Laptop", "Electronics", 15000.0, 5),
    new Product("Mouse",  "Electronics",   250.0, 50),
    new Product("Desk",   "Furniture",    3000.0, 10),
    new Product("Chair",  "Furniture",    1500.0, 20)
);
```

Ürünleri **fiyata göre küçükten büyüğe** sırala ve sadece **isimlerini** döndür.

**Beklenen çıktı:** `["Mouse", "Chair", "Desk", "Laptop"]`

---

### Soru 5 — for → stream: İlk eşleşeni bulmak

Aşağıdaki kodu Stream ile yeniden yaz.

```java
List<Product> products = ...; // Soru 4'teki liste

// KLASİK
Product found = null;
for (Product p : products) {
    if (p.price() > 1000) {
        found = p;
        break;
    }
}
```

**Beklenen çıktı:** `Optional<Product>` — fiyatı 1000'den yüksek ilk ürün.

> 💡 **Püf nokta:** `findFirst()` ile `findAny()` arasındaki fark nedir? Paralel stream'de hangisi tercih edilmeli?

---

## Bölüm 2 — Gruplama ve Toplama (6–10)

---

### Soru 6 — Gruplama: Kategoriye göre ürünleri grupla

```java
List<Product> products = List.of(
    new Product("Laptop",   "Electronics", 15000.0, 5),
    new Product("Mouse",    "Electronics",   250.0, 50),
    new Product("Monitor",  "Electronics",  4500.0, 15),
    new Product("Desk",     "Furniture",    3000.0, 10),
    new Product("Chair",    "Furniture",    1500.0, 20)
);
```

Ürünleri **kategoriye göre grupla** → `Map<String, List<Product>>`

**Beklenen çıktı:**
```
{
  "Electronics" → [Laptop, Mouse, Monitor],
  "Furniture"   → [Desk, Chair]
}
```

> 💡 **Püf nokta:** `Collectors.groupingBy()` ile `Collectors.partitioningBy()` arasındaki fark nedir?

---

### Soru 7 — Gruplama + sayma

Soru 6'daki listeyi kullanarak **kategorideki ürün sayısını** döndür.

**Beklenen çıktı:** `Map<String, Long>` → `{"Electronics": 3, "Furniture": 2}`

> 💡 **Püf nokta:** `groupingBy` ile `counting()` downstream collector nasıl birleştirilir?

---

### Soru 8 — Gruplama + istatistik

Her kategori için **ortalama fiyatı** hesapla.

**Beklenen çıktı:** `Map<String, Double>` → `{"Electronics": 6583.33, "Furniture": 2250.0}`

---

### Soru 9 — flatMap: Siparişteki tüm ürünleri düzleştir

```java
List<Order> orders = List.of(
    new Order(1, "Ali",   List.of(new Product("Laptop", "Electronics", 15000.0, 1),
                                  new Product("Mouse",  "Electronics",   250.0, 2)), true),
    new Order(2, "Ayşe",  List.of(new Product("Desk",   "Furniture",   3000.0, 1)), false),
    new Order(3, "Mehmet",List.of(new Product("Chair",  "Furniture",   1500.0, 1),
                                  new Product("Monitor","Electronics",  4500.0, 1)), true)
);
```

Tüm siparişlerdeki **ürünlerin isimlerini** tek bir liste olarak döndür.

**Beklenen çıktı:** `["Laptop", "Mouse", "Desk", "Chair", "Monitor"]`

> 💡 **Püf nokta:** `map()` yerine neden `flatMap()` kullanman gerekiyor? `map()` kullanırsan ne döner?

---

### Soru 10 — flatMap + filter: Gönderilmiş siparişlerin ürünleri

Soru 9'daki `orders` listesinden yalnızca **gönderilmiş (`shipped=true`)** siparişlerin **Electronics kategorisindeki** ürünlerini döndür.

**Beklenen çıktı:** `[Product("Laptop", ...), Product("Mouse", ...), Product("Monitor", ...)]`

---

## Bölüm 3 — İleri Düzey Zincirler (11–15)

---

### Soru 11 — Distinct + sorted + limit

```java
List<Integer> numbers = List.of(5, 3, 1, 3, 7, 1, 9, 5, 2, 8, 2);
```

Tekrar eden sayıları çıkar, **küçükten büyüğe** sırala ve **ilk 5 tanesini** al.

**Beklenen çıktı:** `[1, 2, 3, 5, 7]`

---

### Soru 12 — reduce: Özel bir toplama işlemi

```java
List<Product> products = ...; // Soru 6'daki liste
```

`reduce()` kullanarak tüm ürünlerin **toplam stok değerini** hesapla (fiyat × stok).

**Beklenen çıktı:** `15000*5 + 250*50 + 4500*15 + 3000*10 + 1500*20 = 229000.0`

> 💡 **Püf nokta:** `reduce(identity, accumulator)` ile `reduce(accumulator)` arasındaki fark nedir? İkincisi neden `Optional` döner?

---

### Soru 13 — Collecting to Map

```java
List<Employee> employees = List.of(
    new Employee("Ali",    "Engineering", 15000, 28),
    new Employee("Ayşe",   "Engineering", 18000, 32),
    new Employee("Mehmet", "HR",          12000, 25),
    new Employee("Hüsna <3", "HR",          13500, 30),
    new Employee("Can",    "Engineering", 20000, 35)
);
```

Çalışan adını key, maaşını value olarak tutan bir `Map<String, Double>` oluştur.

**Beklenen çıktı:** `{"Ali": 15000, "Ayşe": 18000, ...}`

> 💡 **Püf nokta:** İki çalışan aynı isme sahip olsaydı `toMap()` ne fırlatırdı? Bunu nasıl handle edersin?

---

### Soru 14 — En yüksek/en düşük: Her departmandaki en yüksek maaş

Soru 13'teki `employees` listesini kullanarak her departmandaki **en yüksek maaşlı çalışanı** döndür.

**Beklenen çıktı:** `Map<String, Optional<Employee>>` → `{"Engineering": Can (20000), "HR": Hüsna <3 (13500)}`

> 💡 **Püf nokta:** `Collectors.maxBy()` nasıl bir downstream collector'dır?

---

### Soru 15 — String birleştirme: Joining

Soru 13'teki listeden **Engineering departmanındaki** çalışan isimlerini virgülle ayrılmış tek bir String olarak döndür.

**Beklenen çıktı:** `"Ali, Ayşe, Can"`

> 💡 **Püf nokta:** `Collectors.joining()` ile `String.join()` arasındaki fark ve üstünlüğü nedir?

---

## Bölüm 4 — Karmaşık Senaryolar (16–20)

---

### Soru 16 — Çoklu kriter sıralama

Soru 13'teki `employees` listesini **önce departmana göre alfabetik**, aynı departmandaysa **maaşa göre büyükten küçüğe** sırala.

**Beklenen çıktı:**
```
Engineering → Can(20000), Ayşe(18000), Ali(15000)
HR          → Hüsna <3(13500), Mehmet(12000)
```

> 💡 **Püf nokta:** `Comparator.comparing().thenComparing().reversed()` zincirinde `.reversed()` yalnızca son kriter için mi geçerlidir?

---

### Soru 17 — Nested groupingBy

Soru 13'teki `employees` listesini önce **departmana**, sonra departman içinde **yaş grubuna** (`<30` → "Junior", `>=30` → "Senior") göre grupla.

**Beklenen çıktı:**
```java
Map<String, Map<String, List<Employee>>>
{
  "Engineering" → { "Junior": [Ali], "Senior": [Ayşe, Can] },
  "HR"          → { "Junior": [Mehmet], "Senior": [Hüsna <3] }
}
```

---

### Soru 18 — Partition + statistics

Soru 13'teki `employees` listesini maaşı **15000'in üzerinde olanlar** ve **olmayanlar** olarak ikiye böl. Her iki grup için de **toplam maaşı** hesapla.

**Beklenen çıktı:** `Map<Boolean, Double>` → `{true: 53000.0, false: 25500.0}`

---

### Soru 19 — Lazy evaluation tuzağı

Aşağıdaki kodun **ne yazdıracağını** tahmin et, ardından neden o çıktıyı verdiğini açıkla.

```java
List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Ahmet");

Stream<String> stream = names.stream()
    .filter(n -> {
        System.out.println("filter: " + n);
        return n.startsWith("A");
    })
    .map(n -> {
        System.out.println("map: " + n);
        return n.toUpperCase();
    });

System.out.println("Stream oluşturuldu");

List<String> result = stream.collect(Collectors.toList());

System.out.println("Collect tamamlandı: " + result);
```

> 💡 **Püf nokta:** Terminal operasyon çağrılmadan stream işlemleri çalışır mı? `filter` + `map` her eleman için sırayla mı, tüm liste için sırayla mı uygulanır?

---

### Soru 20 — Gerçek dünya senaryosu: Sipariş raporu

Soru 9'daki `orders` listesini kullanarak aşağıdaki raporu oluştur:

> **"Gönderilmiş siparişlerin toplam tutarı nedir ve en pahalı ürün hangisidir?"**

Tek bir stream zinciriyle şu sonuçları hesapla:

1. Yalnızca **gönderilmiş** siparişleri al.
2. Tüm ürünleri düzleştir (`flatMap`).
3. Toplam tutarı (`sum`) ve en pahalı ürünü (`max`) hesapla.

**Beklenen çıktı:**
```
Toplam tutar : 25250.0
En pahalı ürün: Laptop (15000.0)
```

> 💡 **Püf nokta:** Hem `sum` hem `max` hesaplamak için stream iki kez mi tüketilmeli? `Collectors.teeing()` nasıl kullanılır? (Java 12+)
