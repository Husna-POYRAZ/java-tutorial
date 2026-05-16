package com.hpoyraz.model;

import java.util.List;

public record Order(int id, String customerName, List<Product> products, boolean shipped) {}