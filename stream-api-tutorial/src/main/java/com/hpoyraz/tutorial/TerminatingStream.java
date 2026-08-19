package com.hpoyraz.tutorial;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerminatingStream {
    public static void main(String[] args) {
        /**********************************************************************/
        // count(): Eleman sayısını döner.
        Stream<String> namesx = Stream.of("John", "George", "Ben");
        System.out.println(namesx.count()); // 3

        /**********************************************************************/
        // min() method
        Stream<String> names = Stream.of("John", "George", "Ben");
        Optional<String> min = names.min((s1, s2) -> s1.length() - s2.length());
        min.ifPresent(System.out::println); // Ben

        /**********************************************************************/
        // empty Stream
        Optional<?> minEmpty = Stream.empty().min((s1, s2) -> 0);
        System.out.println(minEmpty); // Optional.empty
        System.out.println(minEmpty.isEmpty()); // true
        System.out.println(minEmpty.isPresent()); // false

        /**********************************************************************/
        // findAny() method; return Optional, İlk veya herhangi bir elemanı getirir.
        // stream üzerinde eğer değer varsa herhangi birini getirir
        Stream<String> namesFinding = Stream.of("John", "George", "Ben");
        Stream<String> inf = Stream.generate(() -> "Luke");
        inf.findAny().ifPresent(System.out::println);
        namesFinding.findAny().ifPresent(System.out::println);

        /**********************************************************************/
        // findFirst(); return Optional, İlk elemanı getirir
        Stream<String> namesFindFirst = Stream.of("John", "George", "Ben");
        namesFindFirst.findFirst().ifPresent(System.out::println); //John

        /**********************************************************************/
        // matching()
        var myList = List.of("George", "21", "Ben");
        Stream<String> infinite = Stream.generate(() -> "Luke");
        // Predicate, koşulu test etmek için kullanılır
        Predicate<String> p = s -> Character.isLetter(s.charAt(0));
        // anyMatch(); En az bir eleman şartı sağlıyor mu?
        System.out.println(myList.stream().anyMatch(p)); // true
        // allMatch(); Tüm elemanlar şartı sağlıyor mu?
        System.out.println(myList.stream().allMatch(p)); // false
        // noneMatch(); Hiçbir eleman şartı sağlamıyor mu?
        System.out.println(myList.stream().noneMatch(p)); // false
        System.out.println(infinite.anyMatch(p)); // true

        /**********************************************************************/
        // forEach(): Her eleman için bir işlem çalıştırır.
        Stream<String> names2 = Stream.of("Ali", "Veli", "Ahmet");
        names2.forEach(System.out::println);
        // NOTE: Stream'de traditional loop kullanılmaz. Stream bir veri kümesi, veri yapısı değildir.
        // Sadece Collectiondan alınan verileri manipüle etmeyi sağlar.

        /**********************************************************************/
        // reduce(): Elemanları birleştirip tek bir değer üretir.
        var myArray = new String[]{"H", "ü", "s", "n", "a"};
        var result = "";
        for (var s : myArray) result = result + s;
        System.out.println(result); // Hüsna

        // stream ile yapılış
        Stream<String> myStream = Stream.of("P", "o", "y", "r", "a", "z");
//        String mySurname = myStream.reduce("", (s, c) -> s +c);
        String mySurname = myStream.reduce("", String::concat);
        System.out.println(mySurname); // Poyraz

        Stream<Integer> stream = Stream.of(3,7,10);
        System.out.println(stream.reduce(1, (a, b) -> a*b)); // 210

        BinaryOperator<Integer> op = (a, b) -> a*b;
        Stream<Integer> empty = Stream.empty();
        Stream<Integer> oneElement = Stream.of(7);
        Stream<Integer> threeElements = Stream.of(3, 7, 10);

        empty.reduce(op).ifPresent(System.out::println); // hiç bir sonuç döndürmez
        oneElement.reduce(op).ifPresent(System.out::println); // 7
        threeElements.reduce(op).ifPresent(System.out::println); // 210

        Stream<String> names3 = Stream.of("John", "George", "Ben");
        // initializer, accumulator, combiner
        // Combiner accumlatordan bir önceki sonucu alır ve bir önceki sonuçla birleştirir.
        int len = names3.reduce(0, (i, s) -> i + s.length(), (a, b) -> a + b);
        System.out.println(len); // 13

        /**********************************************************************/
        // collec(); Elemanları bir veri yapısına toplar
        Stream<String> myStream2= Stream.of("L", "u", "k", "e");
        StringBuilder myName = myStream2.collect(
                StringBuilder::new, // supplier
                StringBuilder::append, // accumulator
                StringBuilder::append); // combiner
        System.out.println(myName); // Luke


        Stream<String> myStream3 = Stream.of("L", "u", "k", "e");
        TreeSet<String> mySet = myStream3.collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll);
        System.out.println(mySet); // [L, e, k, u] -> küçükten büyüğe sıralı gelir

        Stream<String> myStream4 = Stream.of("L", "u", "k", "e");
        TreeSet<String> mySet1 = myStream4.collect(Collectors.toCollection(TreeSet::new));
        System.out.println(mySet1); // [L, e, k, u] -> küçükten büyüğe sıralı gelir

        Stream<String> myStream5 = Stream.of("L", "u", "k", "e");
        Set<String> mySet3 = myStream5.collect(Collectors.toSet());
        System.out.println(mySet3); // [u, e, k, L] -> Sıra önemsiz gelir
    }

}
