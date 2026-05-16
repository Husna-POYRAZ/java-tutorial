package com.hpoyraz;

import com.hpoyraz.model.Employee;
import com.hpoyraz.model.Order;
import com.hpoyraz.model.Product;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static final List<Product> products = List.of(
            new Product("Laptop", "Electronics", 15000.0, 5),
            new Product("Mouse",  "Electronics",   250.0, 50),
            new Product("Desk",   "Furniture",    3000.0, 10),
            new Product("Chair",  "Furniture",    1500.0, 20)
    );

    static final List<Order> orders = List.of(
            new Order(1, "Ali",   List.of(new Product("Laptop", "Electronics", 15000.0, 1),
                    new Product("Mouse",  "Electronics",   250.0, 2)), true),
            new Order(2, "Ayşe",  List.of(new Product("Desk",   "Furniture",   3000.0, 1)), false),
            new Order(3, "Mehmet",List.of(new Product("Chair",  "Furniture",   1500.0, 1),
                    new Product("Monitor","Electronics",  4500.0, 1)), true)
    );

    static final List<Employee> employees = List.of(
            new Employee("Ali",    "Engineering", 15000, 28),
            new Employee("Ayşe",   "Engineering", 18000, 32),
            new Employee("Mehmet", "HR",          12000, 25),
            new Employee("Hüsna <3", "HR",          13500, 30),
            new Employee("Can",    "Engineering", 20000, 35)
    );

    public static void main(String[] args) { }
}