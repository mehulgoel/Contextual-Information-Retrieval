package com.naivedya.ir_system;

import java.util.Scanner;

public class SpellCheckerApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // You can change your file path accordingly.
        MySpellChecker spellChecker = new MySpellChecker("/home/naivedya/IdeaProjects/ir_system/src/main/resources/public/dictionary/corncob_lowercase.txt");

        System.out.println("Enter the Sentence with Spelling Mistake..");

        // Reading Input from User
        String line = scanner.nextLine();

        System.out.println("Before Correction : "+line);

        // Method Invocation for Spelling Correction
        line = spellChecker.doCorrection(line);

        System.out.println("After Correction : "+line);

        scanner.close();

    }

}
