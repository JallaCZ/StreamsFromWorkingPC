package org.example;

import org.example.pojo.FileObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static FileObject createFileObject (Path path){
        try {
            return FileObject
                    .builder()
                    .name(path.toFile().getName())
                    .size(Files.size(path))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main( String[] args ) throws IOException {


        Path path = Paths.get(new File("").getAbsolutePath());
        List<Path> result;
        List<FileObject>  fileObjects;

        try (Stream <Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isRegularFile)
                    .toList();
        }

        //1)
        result.stream()
                .filter(p->p.getFileName().toString().endsWith(".java"))
                .forEach(p-> System.out.println(p.getFileName()));

         //2)

        fileObjects=result.stream()
                    .filter(p -> !Files.isDirectory(p))
                    .map(App::createFileObject)
                    .toList();


        Optional<FileObject> min=fileObjects.stream().min(Comparator.comparing(FileObject::getSize));
        Optional<FileObject> max=fileObjects.stream().max(Comparator.comparing(FileObject::getSize));
        long sum= fileObjects.stream()
                .mapToLong(FileObject::getSize)
                .sum();

        min.ifPresent(System.out::println);
        max.ifPresent(System.out::println);
        System.out.println(sum);



    }
}
