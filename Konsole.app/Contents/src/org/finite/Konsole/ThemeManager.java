package org.finite.Konsole;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;
import org.Finite.MicrOS.core.VirtualFileSystem;

public class ThemeManager {
    private Map<String, Theme> themes = new HashMap<>();
    private Theme currentTheme;
    private final String themesDirectory = "/themes";
    private Konsole konsole;

    public ThemeManager(Konsole konsole) {
        this.konsole = konsole;
        loadDefaultThemes();
    }

    private void loadDefaultThemes() {
        // Add a default dark theme
        themes.put("default", new Theme("Default", 
                                       Color.BLACK, 
                                       Color.WHITE, 
                                       Color.GREEN,
                                       "JetBrainsMono", 
                                       19, 
                                       Font.PLAIN));
        
        // Set default theme
        currentTheme = themes.get("default");
        
        // Try to load themes from the themes directory
        loadThemesFromDirectory();
    }

    private void loadThemesFromDirectory() {
        try {
            // Create themes directory if it doesn't exist
            VirtualFileSystem vfs = VirtualFileSystem.getInstance();
            if (!vfs.exists(themesDirectory)) {
                vfs.createDirectory(themesDirectory);
                createSampleTheme();
            }
            
            // Load all .toml files from the themes directory
            File themesDir = new File(vfs.getMountPoint() + themesDirectory);
            if (themesDir.exists() && themesDir.isDirectory()) {
                for (File file : themesDir.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".toml")) {
                        loadThemeFromFile(file);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading themes: " + e.getMessage());
        }
    }

    private void createSampleTheme() {
        // Create a sample theme file
        String sampleTheme = 
            "[theme]\n" +
            "name = \"Light\"\n" +
            "description = \"Light theme for Konsole\"\n\n" +
            "[colors]\n" +
            "background = \"#FFFFFF\"\n" +
            "foreground = \"#000000\"\n" +
            "prompt = \"#0000FF\"\n\n" +
            "[font]\n" +
            "family = \"JetBrainsMono\"\n" +
            "size = 19\n" +
            "style = \"PLAIN\"\n";
        
        try {
            VirtualFileSystem vfs = VirtualFileSystem.getInstance();
            vfs.createFile(themesDirectory + "/light-theme.toml", sampleTheme.getBytes());
        } catch (Exception e) {
            System.err.println("Failed to create sample theme: " + e.getMessage());
        }
    }

    private void loadThemeFromFile(File file) {
        try {
            String content = Files.readString(file.toPath());
            TomlParseResult result = Toml.parse(content);
            
            if (result.errors().isEmpty()) {
                String name = result.getString("theme.name");
                String bgColor = result.getString("colors.background");
                String fgColor = result.getString("colors.foreground");
                String promptColor = result.getString("colors.prompt");
                String fontFamily = result.getString("font.family");
                Long fontSize = result.getLong("font.size");
                String fontStyleStr = result.getString("font.style");
                
                int fontStyle = Font.PLAIN;
                if (fontStyleStr != null) {
                    switch (fontStyleStr.toUpperCase()) {
                        case "BOLD": fontStyle = Font.BOLD; break;
                        case "ITALIC": fontStyle = Font.ITALIC; break;
                        case "BOLD_ITALIC": fontStyle = Font.BOLD | Font.ITALIC; break;
                    }
                }
                
                Theme theme = new Theme(name,
                                       Color.decode(bgColor),
                                       Color.decode(fgColor),
                                       Color.decode(promptColor),
                                       fontFamily,
                                       fontSize != null ? fontSize.intValue() : 19,
                                       fontStyle);
                
                themes.put(name.toLowerCase(), theme);
                konsole.println("Loaded theme: " + name);
            } else {
                konsole.println("Error parsing theme file: " + file.getName());
                result.errors().forEach(error -> konsole.println("  " + error.getMessage()));
            }
        } catch (Exception e) {
            konsole.println("Failed to load theme from " + file.getName() + ": " + e.getMessage());
        }
    }

    public void applyTheme(String themeName) {
        Theme theme = themes.get(themeName.toLowerCase());
        if (theme != null) {
            currentTheme = theme;
            konsole.applyTheme(theme);
            konsole.println("Applied theme: " + theme.getName());
        } else {
            konsole.println("Theme not found: " + themeName);
        }
    }

    public void listThemes() {
        konsole.println("Available themes:");
        themes.values().forEach(theme -> konsole.println("- " + theme.getName()));
    }
    
    public Theme getCurrentTheme() {
        return currentTheme;
    }
}
