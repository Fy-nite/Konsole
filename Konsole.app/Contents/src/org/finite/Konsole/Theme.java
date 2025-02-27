package org.finite.Konsole;

import java.awt.Color;
import java.awt.Font;

public class Theme {
    private String name;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color promptColor;
    private String fontFamily;
    private int fontSize;
    private int fontStyle;

    public Theme(String name, Color backgroundColor, Color foregroundColor, Color promptColor, 
                 String fontFamily, int fontSize, int fontStyle) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.promptColor = promptColor;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.fontStyle = fontStyle;
    }

    public String getName() {
        return name;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getPromptColor() {
        return promptColor;
    }

    public Font getFont() {
        return new Font(fontFamily, fontStyle, fontSize);
    }

    @Override
    public String toString() {
        return name;
    }
}
