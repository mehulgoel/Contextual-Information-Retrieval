package com.naivedya.ir_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.swabunga.spell.engine.SpellDictionaryHashMap;

public class MyDictionaryMap {

    private static MyDictionaryMap dictionary;
    private File file;
    private SpellDictionaryHashMap dictionaryHashMap;

    private MyDictionaryMap(File file) {

        this.file = file;
    }

    public static MyDictionaryMap getInstance(String filePath) {

        if (filePath != null && !filePath.isEmpty()) {

            if (dictionary == null) {
                File file = new File(filePath);
                dictionary = new MyDictionaryMap(file);
            } else {
                return dictionary;
            }
        }
        return dictionary;
    }

    public SpellDictionaryHashMap getDictionaryMap() {

        if (file != null) {

            try {

                dictionaryHashMap = new SpellDictionaryHashMap(file);

            } catch (FileNotFoundException e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {

                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return dictionaryHashMap;
    }
}
