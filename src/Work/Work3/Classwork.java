package Work.Work3;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Classwork {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        try (var fis = new FileInputStream("Work6_log_test/1/2.txt")) {
            try (var zos = new ZipOutputStream(new FileOutputStream("Work6_log_test/zipped.zip"))) {
                var buf = new byte[512];
                zos.putNextEntry(new ZipEntry("Work6_log_test/1/2.txt"));
                int x;
                while ((x = fis.read(buf)) > 0) {
                    zos.write(buf, 0, x);
                }

            }
        }

    }

    private static void externalizableExample() throws IOException, ClassNotFoundException {
        var cat = new CatEx("Barsik", "purple");


        try (var ois = new ObjectInputStream(new FileInputStream("Work6_log_test/barsik"))) {
            var deserializedCat = (CatEx) ois.readObject();
            System.out.println(cat);
            System.out.println(deserializedCat);
            System.out.println(cat == deserializedCat);
            System.out.println(cat.equals(deserializedCat));
        }
    }

    private static void serializationExample() throws IOException, ClassNotFoundException {
        Cat cat = new Cat("Murzik", "purple");


        try (var ois = new ObjectInputStream(new FileInputStream("Work6_log_test/murzik"))) {
            var deserializedCat = (Cat) ois.readObject();
            System.out.println(cat);
            System.out.println(deserializedCat);
            System.out.println(cat == deserializedCat);
            System.out.println(cat.equals(deserializedCat));
        }
    }

    private static void rafExample() throws IOException {
        try (var raf = new RandomAccessFile("Work6_log_test/1/1.txt", "r")) {
            raf.seek(52);
            int x;
            while ((x = raf.read()) > -1) {
                System.out.println((char) x);
            }
        }
    }

    private static void sequenseExample() throws IOException {
        var streams = new ArrayList<InputStream>();
        streams.add(new FileInputStream("Work6_log_test/1/1.txt"));
        streams.add(new FileInputStream("Work6_log_test/1/2.txt"));
        streams.add(new FileInputStream("Work6_log_test/1/3.txt"));
        var sis = new SequenceInputStream(Collections.enumeration(streams));

        int x;
        while ((x = sis.read()) != -1) {
            System.out.print((char) x);
        }
    }

    private static void readExample() throws IOException {
        try (var br = new BufferedReader(new FileReader("Work6_log_test/ex3.txt"))) {


            br.lines()
                    .distinct()
                    .filter(s -> s.contains("s"))

                    .flatMap(s -> Stream.of(s.getBytes(StandardCharsets.UTF_8)))
                    .forEach(System.out::println);


        }
    }

    private static void bufferingExample() throws IOException {


        var startTime = System.currentTimeMillis();

        try (var bis = new BufferedInputStream(new FileInputStream("Work6_log_test/ex3.txt"))) {
            int x;
            while ((x = bis.read()) > -1) {
            }
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }

    private static void simpleReadExample() throws IOException {
        try (var fis = new FileInputStream("Work6_log_test/ex2.txt")) {
            int x;
            while ((x = fis.read()) > -1) {
                System.out.print((char) x);
            }
        }
    }

    private static void simpleFileFileWriteExample() throws IOException {
        var s = "Hello world!";
        var file = new File("Work6_log_test/ex1.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        try (var fos = new FileOutputStream("Work6_log_test/ex1.txt")) {
            fos.write(s.getBytes(StandardCharsets.UTF_8));

        }
    }

    private static void filesExample() {
        var file2 = new File("Work6_log_test/1.txt");
        var file6 = new File("Work6_log_test");
        recursiveFileWalkAndPrint(new File("src"));
    }

    private static void recursiveFileWalkAndPrint(File file) {
        if (file.isFile()) {
            System.out.println("File -->> " + file.getPath());
        } else {
            System.out.println("Catalog -->> " + file.getPath());
            var files = file.listFiles();
            for (File innerFile : files) {
                recursiveFileWalkAndPrint(innerFile);
            }
        }
    }
}