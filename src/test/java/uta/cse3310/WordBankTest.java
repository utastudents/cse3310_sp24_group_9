package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static junit.framework.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class WordBankTest {
    public void testFileRead() throws IOException {//test that we can open the file and create a wordbank
        try{
            WordBank wordbank = new WordBank("Data/words.txt");
            wordbank.setRandomWords();
            assertEquals(50,wordbank.wordsLeft());//ensure that our wordbank is of size maxwords
        } catch (IOException e){System.err.println("Unable to open file or wrong file path");}
        
   }
}
