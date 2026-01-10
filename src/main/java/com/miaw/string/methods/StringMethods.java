package com.miaw.string.methods;

/**
 * Java String Metodları Tutorial
 * Bu sınıf Java'daki önemli String metodlarını örneklerle gösterir
 */
public class StringMethods {

    public static void main(String[] args) {
        indentMethods();
    }

    /**
     * indent() ve stripIndent() metodları
     * Java 12+ ile gelen, string'lerdeki girintileri yönetmeye yarayan metodlar
     */
    public static void indentMethods() {
        System.out.println("=== INDENT METODLARI ===\n");

        String str = " John\n D.\n Wayne";

        // Orijinal string
        System.out.println("Orijinal string:");
        System.out.println("--");
        System.out.println(str);
        System.out.println("--\n");

        // indent(n): Pozitif değer - Her satıra baştan n boşluk ekler
        // DİKKAT: Sonuna otomatik olarak \n (newline) ekler!
        System.out.println("indent(2) - Her satıra 2 boşluk ekle:");
        System.out.println("--");
        System.out.println(str.indent(2));
        System.out.println("--\n");

        // indent(-n): Negatif değer - Her satırdan baştan n boşluk çıkarır (varsa)
        // Eğer satırda yeterli boşluk yoksa, olduğu kadarını çıkarır (negatif olmaz)
        System.out.println("indent(-2) - Her satırdan 2 boşluk çıkar:");
        System.out.println("--");
        System.out.println(str.indent(-2));
        System.out.println("--\n");

        // stripIndent(): Tüm satırlardaki ortak minimum girintiyi kaldırır
        // En az girintili satıra göre diğerlerini normalize eder
        // Bu örnekte Wayne satırında 0 boşluk var, bu yüzden ortak minimum 0
        // Sonuç: String değişmez (sadece normalize edilir)
        System.out.println("stripIndent() - Ortak minimum girintiyi kaldır:");
        System.out.println("--");
        System.out.println(str.stripIndent());
        System.out.println("--\n");

        // Daha iyi bir stripIndent() örneği
        String indentedText = "    Line 1\n    Line 2\n    Line 3";
        System.out.println("stripIndent() daha net örnek:");
        System.out.println("Önce:");
        System.out.println("--");
        System.out.println(indentedText);
        System.out.println("--");
        System.out.println("Sonra:");
        System.out.println("--");
        System.out.println(indentedText.stripIndent());
        System.out.println("--");
    }
}