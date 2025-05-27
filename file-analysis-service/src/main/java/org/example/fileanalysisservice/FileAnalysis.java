package org.example.fileanalysisservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FileAnalysis {
    @Id
    private String fileId;

    private int wordCount;
    private int characterCount;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return this.fileId;
    }


    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getWordCount() {
        return this.wordCount;
    }

    public void setCharacterCount(int characterCount) {
        this.characterCount = characterCount;
    }

    public int getCharacterCount() {
        return this.characterCount;
    }

}
