import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());
        int counter =0;

        while (true){
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
        }
    }
}
