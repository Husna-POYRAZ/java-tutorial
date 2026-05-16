package com.hpoyraz;

import com.hpoyraz.model.Employee;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hpoyraz.Main.employees;
import static com.hpoyraz.Main.orders;

public class Chapter4 {

    static Map<String, Map<String, Double>> question16 () {
        return employees.stream()
                .sorted(
                        Comparator.comparing(Employee::name)
                                .thenComparing(Employee::salary).reversed())
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.toMap(
                                Employee::name,
                                Employee::salary
                        )
                ));
    }

    static List<Employee> question16_1 () {
        return employees.stream()
                .sorted(
                        Comparator.comparing(Employee::department)
                                .thenComparingDouble(Employee::salary).reversed())
                .toList();
    }

    static Map<String, Map<String, List<Employee>>> question17 () {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.groupingBy(
                                employee -> employee.age() < 30 ? "Junior" : "Senior"
                        )
                ));
    }

    static Map<Boolean, Double> question18 () {
        return  employees.stream()
                .collect(Collectors.partitioningBy(
                        e -> e.salary() > 15000,
                        Collectors.summingDouble(Employee::salary)  // her partition için toplam
                ));

    }

    static void question19 () {
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

        List<String> result = stream.toList();

        System.out.println("Collect tamamlandı: " + result);
    }

    // TODO
    static void question20 () {

    }

    public static void main(String[] args) {
        System.out.println(question16());
        System.out.println(question16_1());
        System.out.println(question17());
        System.out.println(question18());
        question19();
    }
}
