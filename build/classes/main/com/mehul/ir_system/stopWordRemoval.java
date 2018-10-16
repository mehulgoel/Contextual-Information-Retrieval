package com.naivedya.ir_system;

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by naivedya on 5/4/17.
 */
public class stopWordRemoval {
    public static void main(String[] args) throws InvalidDataException {
        String inputData = "Where is the city of DehraDun located?";
        String output = ExudeData.getInstance().filterStoppings(inputData);
        System.out.println("output : "+output);    }
}
