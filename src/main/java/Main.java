import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        int counter = 0;

        while (true) {
            //Блок для проверки наличия указанного файла
            System.out.println("Введите путь к файлу для парсинга и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExist = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExist) {
                System.out.println("Указанный файл не существует");
                continue;
            }
            if (isDirectory) {
                System.out.println("Указан путь к папке, а не к файлу");
                continue;
            }
            counter++;
            System.out.println("Путь указан верно");
            System.out.println("Это файл № " + counter);

            //Блок для чтения строк указанного файла и поиска запросов от GoogleBot и YandexBot
            int linesCounter = 0;
            int googleBots = 0;
            int yandexBots = 0;

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    linesCounter++;
                    int length = line.length();
                    if (length > 1024) throw new RuntimeException("Строка не должна содержать более 1024 символа");
                    if (isYandexBot(processUserAgent(getUserAgent(line)))) yandexBots++;
                    if (isGoogleBot(processUserAgent(getUserAgent(line)))) googleBots++;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Всего строк в файле: " + linesCounter);

            double yandexRequests = (double) yandexBots/linesCounter * 100;
            double googleRequests = (double) googleBots/linesCounter * 100;
            System.out.println("Запросов от YandexBot: "+ yandexBots + " (" + yandexRequests + "% от всех запросов)");
            System.out.println("Запросов от GoogleBot: "+ googleBots + " (" + googleRequests + "% от всех запросов)");


        }

    }

    //метод для выделения User-Agent
    public static String getUserAgent(String str) {
        String[] res = str.split("\" \"");
        return res[res.length - 1];
    }


    //метод принимает строку с User-Agent и обрабатывает его, отрезая лишние части и удаляет пробелы
    public static String processUserAgent(String str) {
        String[] tmp = str.split("compatible;");
        String processing;
        if (tmp.length >= 2) {
            processing = tmp[1].trim();
        } else processing = tmp[0].trim();
        String[] res = processing.split("/");
        return res[0];
    }

    public static boolean isYandexBot(String str){
        return str.equals("YandexBot");
    }
    public static boolean isGoogleBot(String str){
        return str.equals("Googlebot");
    }


}
