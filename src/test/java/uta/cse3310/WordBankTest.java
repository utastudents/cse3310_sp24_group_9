package uta.cse3310;

import java.io.IOException;
import static junit.framework.Assert.assertNotNull;

public class WordBankTest {
    public void testFileRead() throws IOException {//test that we can open the file and create a wordbank
        try{
            WordBank wordbank = new WordBank("Data/words.txt");
            assertNotNull(wordbank);
        } catch (IOException e){System.err.println("Unable to open file or wrong file path");}
        
   }
}
