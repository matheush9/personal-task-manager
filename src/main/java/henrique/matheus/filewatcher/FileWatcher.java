package henrique.matheus.filewatcher;

import henrique.matheus.parameters.GlobalParameters;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;
public class FileWatcher {
    public static void watch(String fileName, Consumer<Path> tasksReader) {
        //Tá identificando 2 vezes porque o obsidian salva várias vezes mesmo.
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            if (!Files.exists(GlobalParameters.tasksFolder)) {
                System.out.printf("Creating directory: %s \n", GlobalParameters.tasksFolder);
                Files.createDirectory(GlobalParameters.tasksFolder);
            }

            GlobalParameters.tasksFolder.register(watchService, ENTRY_MODIFY, ENTRY_CREATE);
            System.out.println("Watching: " + GlobalParameters.tasksFolder.toAbsolutePath());

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    System.out.println("Watcher interrupted");
                    return;
                }

                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == OVERFLOW) continue;

                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path evFileName = ev.context();
                    if (evFileName.getFileName().toString().equals(fileName)) {
                        if (Files.exists(GlobalParameters.tasksFolder.resolve(evFileName))){
                            tasksReader.accept(GlobalParameters.tasksFolder.resolve(evFileName));
                        }
                    }
                }
                boolean valid = key.reset();
                if (!valid) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
