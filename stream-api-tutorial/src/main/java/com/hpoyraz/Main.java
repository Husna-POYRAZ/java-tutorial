package com.hpoyraz;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        // Chapter 1
        // 1. Stream API nedir?
        // Stream API, Java 8 ile birlikte gelen bir özelliktir ve koleksiyonlar üzerinde fonksiyonel tarzda işlemler yapmamızı sağlar.
        // Stream API, verileri işlemek için daha okunabilir ve daha az hata yapma olasılığı olan bir yol sunar.

        // 2. Stream API'nin avantajları nelerdir?
        // - Daha okunabilir kod: Stream API, işlemleri zincirleme yaparak kodun daha okunabilir olmasını sağlar.
        // - Daha az hata yapma olasılığı: Stream API, null kontrolü gibi hataları azaltır.
        // - Paralel işlem yapabilme: Stream API, paralel işlemler yaparak performansı artırabilir.

        // 3. Stream API nasıl kullanılır?
        // Stream API'yi kullanmak için öncelikle bir koleksiyon oluşturmanız gerekir. Daha sonra, koleksiyonu stream'e dönüştürerek işlemleri gerçekleştirebilirsiniz.

        // Örnek:
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // Tüm isimleri büyük harfe çevirme
        List<String> upperCaseNames = names.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        System.out.println(upperCaseNames); // [ALICE, BOB, CHARLIE, DAVID]

    }
}