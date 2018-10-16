package com.naivedya.ir_system;

import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;

public class MySpellChecker {

    private SpellChecker spellChecker;
    private List<String> misspelledWords = new ArrayList<String>();;

    private MyDictionaryMap dictionaryMap;

    public MySpellChecker(String filePath) {

        if(filePath != null && !filePath.isEmpty()) {

            dictionaryMap = MyDictionaryMap.getInstance(filePath);
            spellChecker = new SpellChecker(dictionaryMap.getDictionaryMap());

            MySpellCheckListener listener = new MySpellCheckListener(misspelledWords);
            spellChecker.addSpellCheckListener(listener);
        }
    }

    // Method to Detect Misspelled Words From Given Line
    public List<String> detectMisspelledWords(String text) {

        StringWordTokenizer strTokenizer = new StringWordTokenizer(text, new TeXWordFinder());
        spellChecker.checkSpelling(strTokenizer);
        return misspelledWords;
    }

    // Method to do Correction on Misspelled Words for the given Line
    public String doCorrection(String line) {

        List<String> misSpelledWords = detectMisspelledWords(line);

        for (String misSpelledWord : misSpelledWords) {

            List<String> suggestions = getSuggestions(misSpelledWord);
            if (suggestions.size() == 0)
                continue;
            String bestSuggestion = suggestions.get(0);
            line = line.replace(misSpelledWord, bestSuggestion);
        }
        return line;
    }

    // Getting Suggestions for Misspelled Words
    public List<String> getSuggestions(String misspelledWord) {

        List<Word> words = spellChecker.getSuggestions(misspelledWord, 0);
        List<String> suggestions = new ArrayList<String>();

        for (Word suggestion : words) {
            suggestions.add(suggestion.getWord());
        }
        return suggestions;
    }
}
