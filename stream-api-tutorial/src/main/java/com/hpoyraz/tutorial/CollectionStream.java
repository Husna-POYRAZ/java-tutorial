package com.hpoyraz.tutorial;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionStream {
    public static void main(String[] args) {
        // joining() example
        var names = Stream.of("John", "George", "Luke");
        String result = names.collect(Collectors.joining());
        System.out.println(result); // JohnGeorgeLuke

        var names2 = Stream.of("John", "George", "Luke", "Luke");
        String result2 = names2.collect(Collectors.joining("-"));
        System.out.println(result2); // John-George-Luke-Luke

        // averaging() example
        var names3 = Stream.of("John", "George", "Luke");
        double result3 = names3.collect(Collectors.averagingInt(String::length));
        System.out.println(result3); //4.666666666666667

        // toCollection() example
        var names4 = Stream.of("John", "George", "Luke", "Joe", "Joe");
        //TreeSet: Elemanları otomatik olarak alfabetik sıraya sokar ve benzersiz tutar. Bu yüzden "Joe", "John"'dan önce gelir.
        TreeSet<String> result4 = names4
                .filter(s -> s.startsWith("J"))
                .collect(
                        Collectors.toCollection(TreeSet::new)
                );
        System.out.println(result4); // [Joe, John]

        // toMap()
        var names5 = Stream.of("John", "George", "Luke");
        Map<String, Integer> result5 = names5
                .collect(
                        Collectors.toMap(s -> s, String::length)
                );
        System.out.println(result5);

        /**
        var names6 = Stream.of("John", "George", "Luke");
        Map<Integer, String> result6 = names6
                .collect(
                        Collectors.toMap(String::length, s -> s)
                );
        System.out.println(result6); // IllegalStateException (Duplicate key 4)
        */

        var names6 = Stream.of("John", "George", "Luke");
        // Collectors.toMap(key, value, merge)
        Map<Integer, String> result6 = names6
                .collect(
                        Collectors.toMap(
                                String::length,    // key
                                s -> s,     // value
                                // Çakışma fonksiyonu
                                (s1, s2) -> s1 + ";" + s2) // İki kelimenin anahtarı çakıştığında ne yapılacağını bu fonksiyon belirler
                );
        System.out.println(result6); // {4=John;Luke, 6=George}
        System.out.println(result6.getClass()); // class java.util.HashMap

        /**
         * Collectors.toMap varsayılan olarak hangi Map uygulamasını döneceğini garanti etmez
         * Eğer belirli bir Map türü (örneğin sırayı koruyan LinkedHashMap veya sıralı tutan TreeMap) kullanmak istiyorsanız, metoda 4. bir parametre (Supplier) eklersiniz:
         */
        var names7 = Stream.of("John", "George", "Luke");
        Map<Integer, String> result7 = names7
                .collect(
                        Collectors.toMap(
                                String::length,    // key
                                s -> s,     // value
                                // Çakışma fonksiyonu
                                (s1, s2) -> s1 + ";" + s2, // İki kelimenin anahtarı çakıştığında ne yapılacağını bu fonksiyon belirler
                                TreeMap::new)
                );
        System.out.println(result7); // {4=John;Luke, 6=George}
        System.out.println(result7.getClass()); // class java.util.TreeMap

        // groupingBy(Function f); Creates map
        var names8 = Stream.of("John", "George", "Luke", "Luke");
        Map<Integer, List<String>> result8 = names8.collect(
                Collectors.groupingBy(String::length)
        );
        System.out.println(result8); // {4=[John, Luke, Luke], 6=[George]}

        // groupingBy(Function f, Collector dc); Creates map where downstream collector is provided
        var names9 = Stream.of("John", "George", "Luke", "Luke");
        Map<Integer, Set<String>> result9 = names9.collect(
                Collectors.groupingBy(
                        String::length,
                        Collectors.toSet())
        );
        System.out.println(result9); // {4=[Luke, John], 6=[George]}
        System.out.println(result9.getClass()); // class java.util.HashMap
        // groupingBy metodunun arka planda varsayılan Map üreticisi olarak HashMap::new kullanılır

        // groupingBy(Function f, Supplier s, Collector dc); Creates map where both map supplier and downstream collector are provided
        // groupingBy metoduna da 3. bir parametre (Supplier) vererek HashMap yerine TreeMap veya LinkedHashMap kullanılabilir
        var names10 = Stream.of("John", "George", "Luke", "Luke");
        TreeMap<Integer, Set<String>> result10 = names10.collect(
                Collectors.groupingBy(
                        String::length,
                        TreeMap::new,
                        Collectors.toSet()
                )
        );
        System.out.println(result10); // {4=[Luke, John], 6=[George]}
        System.out.println(result10.getClass()); // class java.util.TreeMap

        // partitioningBy() has only two groups: true and false
        var names11 = Stream.of("John", "George", "Luke", "Luke");
        Map<Boolean, List<String>> result11 = names11.collect(
                Collectors.partitioningBy(s -> s.length() <= 4)
        );
        System.out.println(result11); // {false=[George], true=[John, Luke, Luke]}


        var names12 = Stream.of("John", "George", "Luke", "Luke");
        Map<Boolean, Set<String>> result12 = names12.collect(
                Collectors.partitioningBy(
                        s -> s.length() <= 4,
                        Collectors.toSet()
                )
        );
        System.out.println(result12); // {false=[George], true=[Luke, John]}


        // teeing(); is used for returning multiple values, e.g. sum and average
        /***
         * Normalde bir Stream tüketildiğinde (örneğin sum() veya average() çağrıldığında) akış kapanır ve ikinci bir işlem yapamazsınız.
         * teeing toplayıcısı ise gelen veriyi iki farklı yola (collector) bölüp sonuçları en sonda tek bir nesnede birleştirmeyi sağlar.
         */
        // step 1: create a type which stores values:
        record MyData(int sum, double avg) {}

        // step 2: use stream to return the result of the type MyData
        var numbers = Stream.of(1, 2, 3, 4, 5);
        MyData myDataResult13 = numbers.collect(
                Collectors.teeing(
                        Collectors.summingInt(i -> i),
                        Collectors.averagingDouble(i -> i),
                        MyData::new
                ));
        System.out.println("Sum: " + myDataResult13.sum() + ", Average: " + myDataResult13.avg());

    }
}
