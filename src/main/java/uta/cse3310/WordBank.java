package uta.cse3310;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
public class WordBank {
    private int MAXWORDS = 50;//this value is only for testing purposes, maxwords is still tbd
    private ArrayList<Word> Words = new ArrayList<>();
    private HashMap<String, Word> wordBankMap = new HashMap<>(); //the key is the String word and the value is the word object

    public WordBank(String filename) throws IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null) {
               line = line.trim();
               if(line.length() < 3){
                    continue;
               }else{
                    Word word = new Word(line);
                    Words.add(word);
               }
            }
        } catch (IOException e){System.err.println("Unable to open file or wrong file path");}
    }
    public void setRandomWords(){
        int randomIndex = 0;
        Random random = new Random();
        Word randomWord;
        for(int i = 0; i < MAXWORDS; i++){
            randomIndex = random.nextInt(Words.size() + 1);
            randomWord = Words.get(randomIndex);
            wordBankMap.put(randomWord.word,randomWord);
            Words.remove(randomIndex);
            
        }
    }
    public int wordsLeft(){
        return wordBankMap.size();
    }
    public void removeWord(Word wordToRemove){
        String keyWord = wordToRemove.word;
        wordBankMap.remove(keyWord);
    }
}