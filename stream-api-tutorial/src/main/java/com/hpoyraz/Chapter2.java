package com.hpoyraz;

import com.hpoyraz.model.Order;
import com.hpoyraz.model.Product;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hpoyraz.Main.orders;
import static com.hpoyraz.Main.products;

public class Chapter2 {

    static Map<String, List<Product>> question6 () {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category));

    }

    static Map<String, List<String>> question6_1 () {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.mapping(Product::name, Collectors.toList()))
                );

    }

    static Map<Boolean, List<Product>> question6_2 () {
        return products.stream()
                .collect(Collectors.partitioningBy(product -> product.price() > 2000));
    }

    static Map<Boolean, List<String>> question6_3() {
        return products.stream()
                .collect(Collectors.partitioningBy(
                        product -> product.price() > 2000,
                        Collectors.mapping(Product::name, Collectors.toList())
                ));
    }

    static Map<String, Long> question7 () {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.counting())
                );
    }

    static  Map<String, Double> question8 () {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.averagingDouble(Product::price)
                ));
    }

    static Map<String, DoubleSummaryStatistics> question8_1 () {
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::category,
                        Collectors.summarizingDouble(Product::price)
                ));
    }

    static List<String> question9 () {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .map(Product::name)
                .toList();
    }

    static List<Product> question10 () {
        return orders.stream()
                .filter(Order::shipped)
                .flatMap(order -> order.products().stream())
                .filter(product -> product.category().equals("Electronics"))
                .toList();
    }



    public static void main(String[] args) {
        System.out.println(question6());
        System.out.println(question6_1());
        System.out.println(question6_2());
        System.out.println(question6_3());
        System.out.println(question7());
        System.out.println(question8());
        System.out.println(question8_1().get("Electronics").getAverage());
        System.out.println(question8_1().get("Electronics").getCount());
        System.out.println(question9());
        System.out.println(question10());
    }
}
