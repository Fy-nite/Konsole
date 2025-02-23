package org.finite.Konsole;

import javax.swing.JTextArea;

public class Commmon {
    private JTextArea console;
    private String prompt;
    private StringBuilder inputBuffer;

    public Commmon(JTextArea console, String prompt, StringBuilder inputBuffer) {
        this.console = console;
        this.prompt = prompt;
        this.inputBuffer = inputBuffer;
    }

    public void print(String text) {
        console.append(text);
        console.setCaretPosition(console.getDocument().getLength());
    }

    public void displayPrompt() {
        console.append(prompt);
        console.setCaretPosition(console.getDocument().getLength());
    }

    public void updateInputLine(String text) {
        String currentText = console.getText();
        int lastPromptIndex = currentText.lastIndexOf(prompt);
        if (lastPromptIndex != -1) {
            console.replaceRange(text, lastPromptIndex + prompt.length(), console.getDocument().getLength());
            inputBuffer.setLength(0);
            inputBuffer.append(text);
        }
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
