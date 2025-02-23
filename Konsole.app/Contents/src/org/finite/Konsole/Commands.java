package org.finite.Konsole;

import org.finite.Konsole.Konsole;
import org.Finite.MicrOS.core.VirtualFileSystem;
import org.Finite.MicrOS.core.CLIRegistry;

import java.io.File;
import java.util.Arrays;

public class Commands {
    private Konsole konsole;

    public Commands(Konsole konsole) {
        this.konsole = konsole;
    }

    public void changeDirectory(String path) {
        // Simple path handling - you might want to make this more sophisticated
        if (path.startsWith("/")) {
            konsole.currentDirectory = path;
        } else if (path.equals("..")) {
            int lastSlash = konsole.currentDirectory.lastIndexOf('/');
            if (lastSlash > 0) {
                konsole.currentDirectory = konsole.currentDirectory.substring(0, lastSlash);
            } else {
                konsole.currentDirectory = "/";
            }
        } else {
            konsole.currentDirectory = konsole.currentDirectory.equals("/") ? 
                "/" + path : konsole.currentDirectory + "/" + path;
        }
    }

    public void listFiles(String[] args) {
        VirtualFileSystem vfs = VirtualFileSystem.getInstance();
        String path = args.length > 0 ? args[0] : konsole.currentDirectory;
        
        File[] files = vfs.listFiles(path);
        if (files.length == 0) {
            konsole.println("Directory is empty");
            return;
        }

        // Calculate the longest filename for formatting
        int maxLength = Arrays.stream(files)
            .mapToInt(f -> f.getName().length())
            .max()
            .orElse(0);

        // Format and print each file
        for (File file : files) {
            String name = file.getName();
            String type = file.isDirectory() ? "DIR" : "FILE";
            String size = String.format("%5d", file.length());
            
            konsole.println(String.format("%-" + maxLength + "s  %5s  %s", 
                name, size, type));
        }
    }

    public void showHelp() {
        konsole.println("\nAvailable commands:");
        konsole.println("  clear  - Clear the terminal");
        konsole.println("  help   - Show this help message");
        konsole.println("  exit   - Exit the terminal");
        konsole.println("  cd    - Change directory");
        konsole.println("  pwd   - Print working directory");
        konsole.println("  ls    - List directory contents");
        konsole.println("\nCLI Applications:");
        for (String cmd : CLIRegistry.getInstance().getAvailableCommands()) {
            konsole.println("  " + cmd);
        }
    }
}
