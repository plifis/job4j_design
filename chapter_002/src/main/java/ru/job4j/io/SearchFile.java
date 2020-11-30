package ru.job4j.io;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SearchFile {
    private ArgsSearch argsSearch;
    private String dir;
    private String include;
    private String out;
    private String argFind;

    /**
     * Конструктор, если аргументы получаем в виде массива
     * @param args массив строк ключей с параметрами
     */
    public SearchFile(String[] args) {
        this.argsSearch = new ArgsSearch(args);
        this.setDir();
        this.setInclude();
        this.setOut();
        this.setArgFind();
    }

    /**
     * Метод возвращает значение параметра
     * @param arg строка содержащая ключ и параметр
     * @return возращает параметр
     */
    private String getArgument(String arg) {
       return arg.substring(arg.indexOf(" "));
    }

    /**
     * Метод ищет файлы в папке и попдпаках и вовзращает список находок
     * @param dir Корень откуда начинать искать
     * @param include что ищем (используется как регулярное выражение)
     * @return список файлов удовлетворяющих требованиям
     * @throws IOException искллючение ввода-вывода при итерации по сущностям "Path"
     */
    private List<Path> search(String dir, String include) throws IOException {
        Pattern pattern = Pattern.compile(include, Pattern.CASE_INSENSITIVE);
        SearchFiles searcher = new SearchFiles(pattern.asPredicate());
        Files.walkFileTree(Path.of(dir), searcher);
        return searcher.getPaths();
    }

    static class SearchFiles extends SimpleFileVisitor<Path> {
        private List<Path> pathsList = new ArrayList<>();
        Predicate<String> filter;
        SearchFiles(Predicate<String> filter) {
            this.filter = filter;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (filter.test(file.toString())) {
                pathsList.add(file);
            }
            return super.visitFile(file, attrs);
        }
        public List<Path> getPaths() {
            return pathsList;
        }
    }

    /**
     * Записываем список файлов в файл
     * @param list список файлов, удовлетворяющих требованию
     * @param out файл в который пишем результат работы
     */

    private void writeResult(List<Path> list, String out) {
        for (Path current : list) {
            try (Writer fileWriter = new FileWriter(out, true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                 bufferedWriter.write(current.toString() + System.lineSeparator());
                 bufferedWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод запускает метод записи файла в параметрах которого метод поиска
     * @throws IOException исключение ввода-вывода
     */

    public void work() throws IOException {
        writeResult(search(this.getDir(), getInclude()), getOut());
    }


    public String getDir() {
        return dir;
    }

    public void setDir() {
        this.dir = argsSearch.directory();
    }

    public String getInclude() {
        return include;
    }

    public void setInclude() {
        this.include = argsSearch.include();
    }

    public String getOut() {
        return out;
    }

    public void setOut() {
        this.out = argsSearch.out();
    }
    public String getArgFind() {
        return argFind;
    }
    public void setArgFind() {
        this.argFind = argsSearch.argFind();
    }


    public static void main(String[] args) throws IOException {
        SearchFile searchFile = new SearchFile(args);
        searchFile.work();
    }
}
