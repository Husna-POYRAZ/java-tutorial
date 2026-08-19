package com.hpoyraz;

import com.hpoyraz.model.Product;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hpoyraz.Main.products;

public class Chapter1 {

    static int deneme () {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        return numbers.stream()
                .filter(n -> n%2 == 0)
                .reduce(0, Integer::sum);
    }

    static List<String> question1 () {
        List<String> names = List.of("Ali", "Ayşe", "Mehmet", "Ahmet", "Hüsna <3");

        return names.stream()
                .filter(name -> name.startsWith("A"))
                .toList();
    }

    static List<String> question2 () {
        List<String> names = List.of("ali", "ayşe", "mehmet");
        return names.stream()
                .map(String::toUpperCase)
                .toList();
    }

    static int question3() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sum = numbers.stream()
                .filter(number -> number % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();

        int sum2 = numbers.stream()
                .filter(number -> number % 2 == 0)
                .reduce(0, Integer::sum);

        return sum;
    }

    static List<String> question4() {
        return products.stream()
                        .sorted(Comparator.comparingDouble(Product::price))
                                .map(Product::name)
                                        .toList();

    }

    static String question5() {
        return products.stream()
                .filter(product -> product.price() > 1000)
                .findFirst()
                .map(Product::name)
                .orElseGet(() -> "Product not found");
    }

    public static void main(String[] args) {
        System.out.println(question1());
        System.out.println(question2());
        System.out.println(question3());
        System.out.println(question4());
        System.out.println(question5());
    }
}
