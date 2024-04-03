import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
                System.out.println("Указанный файл не существует"); continue;
            }
            if (isDirectory) {
                System.out.println("Указан путь к папке, а не к файлу"); continue;
            }
            counter++;
            System.out.println("Путь указан верно");
            System.out.println("Это файл № " + counter);

            //Блок для чтения строк указанного файла и поиска самой длинной и короткой строки, а так же общего количества строк
            int linesCounter = 0;
            int longestLine = 0;
            ArrayList<Integer> tmp = new ArrayList<>();

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    linesCounter++;
                    int length = line.length();
                    if (length > 1024) throw new RuntimeException("Строка не должна содержать более 1024 символа");
                    if (length > longestLine) longestLine = length;
                    tmp.add(length);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            int shortestLine = tmp.get(0);
            for (int i = 1; i < tmp.size(); i++) {
                if (shortestLine > tmp.get(i)) shortestLine = tmp.get(i);
            }
            System.out.println("Всего строк в файле: " + linesCounter);
            System.out.println("Самая длинная строка содержит символов: " + longestLine);
            System.out.println("Самая короткая строка содержит символов : " + shortestLine);
        }



    }
}
