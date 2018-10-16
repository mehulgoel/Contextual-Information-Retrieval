package com.naivedya.ir_system;

     import edu.smu.tspell.wordnet.Synset;
        import edu.smu.tspell.wordnet.WordNetDatabase;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.HashSet;
     import java.util.List;

public class Synonyms {
    public ArrayList< String > getSynonyms(String wordForm){
        //  Get the synsets containing the word form=capicity

        File f=new File("/usr/local/WordNet-3.0/dict");

        //`System.setProperty("wordnet.database.dir", "C:\WordNet-3.0\dict\");`
        System.setProperty("wordnet.database.dir", "/usr/local/WordNet-3.0/dict");
        //setting path for the WordNet Directory

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(wordForm);
        //  Display the word forms and definitions for synsets retrieved

        ArrayList<String> al = new ArrayList<String>();
        if (synsets.length > 0){
            // add elements to al, including duplicates
            HashSet hs = new HashSet();
            for (int i = 0; i < synsets.length; i++){
                String[] wordForms = synsets[i].getWordForms();
                for (int j = 0; j < wordForms.length; j++)
                {
                    al.add(wordForms[j]);
                }


                //removing duplicates
                hs.addAll(al);
                al.clear();
                al.addAll(hs);

            }
        }
        return al;
    }


}
