package dev.mochahaulier.springbackendtest1.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class DictionaryService {

    private List<String> hiddenWords = new ArrayList<>();
    private List<String> validWords = new ArrayList<>();

    @PostConstruct
    private void loadHiddenWords() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("/hiddenwords.txt"), "UTF-8"));
            String line = null;

            while ((line = br.readLine()) != null) {
                hiddenWords.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    private void loadValidWords() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("/validwords.txt"), "UTF-8"));
            String line = null;

            while ((line = br.readLine()) != null) {
                validWords.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSolution() {
        Random random = new Random();
        return hiddenWords.get(random.nextInt(hiddenWords.size()));
    }

    public boolean isValidWord(String word) {
        return (validWords.contains(word) || hiddenWords.contains(word));
    }
}
