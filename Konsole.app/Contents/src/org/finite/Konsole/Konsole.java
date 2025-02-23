package org.finite.Konsole;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.Finite.MicrOS.apps.AppManifest;
import org.Finite.MicrOS.apps.MicrOSApp;
import org.Finite.MicrOS.core.CLIRegistry;
import org.Finite.MicrOS.core.VirtualFileSystem;
import org.Finite.MicrOS.ui.FontLoader;
import org.Finite.MicrOS.util.AppLauncher;

public class Konsole extends MicrOSApp {
    private JTextArea console;
    private JScrollPane scrollPane;
    private StringBuilder inputBuffer;
    private java.util.List<String> commandHistory;
    private int historyIndex;
    public String currentDirectory = "/";
    private String systemName = initSystemName();
    private Commands commands;
    private Commmon common;

    private String initSystemName() {
        if (AppLauncher.isRunningInMicrOS()) {
            try {
                return "micrOS@" + InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                return "micrOS";
            }
        } else {
            // return the system name
            try
            {
                InetAddress addr;
                addr = InetAddress.getLocalHost();
                return addr.getHostName();
            }
            catch (UnknownHostException ex)
            {
                return System.getProperty("user.os");
            }
        }
    }
    
    private String prompt = "[%s@%s %s]$ ";  // format: [user@system path]$
    // get the system font

    public Font font = FontLoader.getFont("iJetBrainsMono-Regular.ttf", Font.PLAIN, 19);;
    @Override
    public JComponent createUI() {
        commandHistory = new java.util.ArrayList<>();
        historyIndex = -1;
        
        console = new JTextArea(10, 50);
        console.setFont(font);
        console.setBackground(Color.black);
        console.setForeground(Color.white);
        
        console.setEditable(true);
        console.setLineWrap(true); // Enable word wrapping
        console.setWrapStyleWord(true); // Wrap at word boundaries
        inputBuffer = new StringBuilder();
        // set the font size
        console.setFont(font);
        
        commands = new Commands(this);
        updatePrompt();
        common = new Commmon(console, prompt, inputBuffer);

        console.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    String command = inputBuffer.toString().trim();
                    if (!command.isEmpty()) {
                        commandHistory.add(command);
                        historyIndex = commandHistory.size();
                    }
                    processCommand(command);
                    inputBuffer.setLength(0);
                 
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    // remove the last character
                    e.consume();
                    if (inputBuffer.length() > 0) {
                        inputBuffer.setLength(inputBuffer.length() - 1);
                        common.updateInputLine(inputBuffer.toString());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    e.consume();
                    if (historyIndex > 0) {
                        historyIndex--;
                        String historyCommand = commandHistory.get(historyIndex);
                        common.updateInputLine(historyCommand);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.consume();
                    if (historyIndex < commandHistory.size() - 1) {
                        historyIndex++;
                        String historyCommand = commandHistory.get(historyIndex);
                        common.updateInputLine(historyCommand);
                    } else if (historyIndex == commandHistory.size() - 1) {
                        historyIndex = commandHistory.size();
                        common.updateInputLine("");
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() >= 32 && e.getKeyChar() < 127) {
                    inputBuffer.append(e.getKeyChar());
                    common.updateInputLine(inputBuffer.toString());
                }
            }
        });
        
        scrollPane = new JScrollPane(console);
        
        // Setup basic manifest
        AppManifest manifest = new AppManifest();
        manifest.setName("Konsole");
        manifest.setIdentifier("org.finite.Konsole");
        manifest.setMainClass(getClass().getName());
        setManifest(manifest);
        
        return scrollPane;
    }

    private void updatePrompt() {
        String user = System.getProperty("user.name", "user");
        prompt = String.format("[%s@%s %s]$ ", user, systemName, currentDirectory);
        if (common != null) {
            common.setPrompt(prompt);
        }
    }

    private void processCommand(String command) {
        println("");
        String[] parts = command.split("\\s+");
        if (parts.length == 0) return;
        
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        
        switch (cmd) {
            case "clear":
                console.setText("");
                break;
            case "help":
                commands.showHelp();
                break;
            case "exit":
                onStop();
                break;
            case "cd":
                if (args.length > 0) {
                    commands.changeDirectory(args[0]);
                } else {
                    currentDirectory = "/";
                }
                updatePrompt();
                break;
            case "pwd":
                println(currentDirectory);
                break;
            case "ls":
                commands.listFiles(args);
                break;
            default:
                // Try executing as CLI app
                if (!CLIRegistry.getInstance().executeCommand(cmd, args, getManifest().getIdentifier())) {
                    println("\nUnknown command: " + cmd);
                }
        }
        updatePrompt();
        common.displayPrompt();
    }

    public void println(String text) {
        console.append(text + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }

    @Override
    public void onStart() {
        System.out.println("Konsole started");
        updatePrompt();
        println("MicrOS Konsole v1.0");
        common.displayPrompt();
    }

    @Override
    public void onStop() {
        System.out.println("Konsole stopped");
        System.exit(0);
    }
}