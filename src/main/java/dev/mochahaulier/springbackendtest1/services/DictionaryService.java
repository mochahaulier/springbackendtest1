package dev.mochahaulier.springbackendtest1.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service("ds")
public class DictionaryService {

    private List<String> hiddenWords;
    private List<String> validWords;

    @PostConstruct
    private void loadHiddenWords() throws IOException {
        hiddenWords = FileUtils.readLines(new File("src/main/resources/hiddenwords.txt"), "utf-8");
    }

    @PostConstruct
    private void loadValidWords() throws IOException {
        validWords = FileUtils.readLines(new File("src/main/resources/validwords.txt"), "utf-8");
    }

    public String getSolution() {
        Random random = new Random();
        return hiddenWords.get(random.nextInt(hiddenWords.size()));
    }

    public boolean isValidWord(String word) {
        return (validWords.contains(word) || hiddenWords.contains(word));
    }
}
