package com.hpoyraz;

import com.hpoyraz.model.Employee;
import com.hpoyraz.model.Product;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hpoyraz.Main.employees;
import static com.hpoyraz.Main.products;

public class Chapter3 {

    static List<Integer> question11 () {
        List<Integer> numbers = List.of(5, 3, 1, 3, 7, 1, 9, 5, 2, 8, 2);

        return numbers.stream()
                .distinct()
                .sorted()
                .limit(5)
                .toList();
    }

    static double question12 () {
        return products.stream()
                .mapToDouble(p -> p.stock() * p.price())
                .sum();
    }

    static double question12_1 () {
        return products.stream()
                .reduce(0.0,
                        (acc, p) -> acc + p.price() * p.stock(),   // accumulator
                        Double::sum);
    }

    static Map<String, Double> question13 () {
        return employees.stream()
                .collect(Collectors.toMap(Employee::name, Employee::salary));
    }

    static Map<String, Double> question13_1 () {
        return employees.stream()
                .collect(Collectors.toMap(
                        Employee::name,
                        Employee::salary,
                        (e, r) -> e,
                        LinkedHashMap::new
                ));
    }

    static Map<String, Optional<Employee>> question14 () {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.maxBy(Comparator.comparing(Employee::salary))
                ));
    }

    static List<String> question15 () {
        return employees.stream()
                .filter(employee -> employee.department().equals("Engineering"))
                .map(Employee::name)
                .toList();
    }

    public static void main(String[] args) {
        System.out.println(question11());
        System.out.println(question12());
        System.out.println(question12_1());
        System.out.println(question13());
        System.out.println(question13_1());
        System.out.println(question14());
        System.out.println(question15());
    }
}
