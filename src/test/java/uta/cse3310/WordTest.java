package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static junit.framework.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WordTest {
   public void testWordGson(){//test the difference of how json prints with or without coordinates
   Word firstWord = new Word("hello"); //this variable gets its coordinates set
   Word secondWord = new Word("world"); //this variable does not get its coordinates set

   firstWord.setStart(5,10);
   firstWord.setEnd(15,20);

   assertEquals("{\"xOne\":5,\"yOne\":10,\"xTwo\":15,\"yTwo\":20,\"word\":\"hello\"}", firstWord.wordJson());
   assertEquals("{\"xOne\":0,\"yOne\":0,\"xTwo\":0,\"yTwo\":0,\"word\":\"world\"}", secondWord.wordJson());

   }
}
