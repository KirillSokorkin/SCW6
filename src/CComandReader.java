import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс, отвечающий за ввод команд пользователем и диалог м/д программой и пользователем
 */

public class CComandReader {


    ObjectInputStream fromServer;
    ObjectOutputStream toServer;

    CComandReader(ObjectInputStream fromServer, ObjectOutputStream toServer){
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    /**
     * Метод, взаимодействующий с консолью
     */

    public void start_reading(HashSet<String> is_console, String Path) throws IOException, ClassNotFoundException {
        Boolean is_ok = true;

        CCommand hub = new CCommand(fromServer, toServer);
        Scanner sc = new Scanner(System.in);

        if (is_console.contains(Path)) {
            is_ok = false;
            System.out.println(">Вы попытались вызвать из скрипта тот же самый скрипт. Действие было пропущено");
        }

        is_console.add(Path);

        if (is_console.size()==1 && is_ok) sc = new Scanner(System.in);
        else
            try {
                sc = new Scanner(new File(Path));
            } catch (Exception e) {
                System.out.println(">Проблема с файлом");
                is_ok = false;
            }

        if (is_ok) {
            String command = sc.nextLine().toLowerCase();
            String[] commandParts;
            while (!command.equals("exit")) {
                commandParts = command.split(" ", 3);

                switch (commandParts[0]) {
                    case "":
                        break;
                    case "help":
                        hub.help(command);
                        break;
                    case "info":
                        hub.info();
                        break;
                    case "show":
                        hub.show();
                        break;
                    case "add":
                        hub.add();
                        break;
                    case "update_by_id":
                        hub.update_by_id(Long.parseLong(commandParts[1]));
                        break;
                    case "remove_by_id":
                        hub.remove_by_id(Long.parseLong(commandParts[1]));
                        break;
                    case "clear":
                        hub.clear();
                        break;
                    case "execute_script":
                        try {
                            this.start_reading(is_console, commandParts[1]);
                        }
                        catch (Exception e)
                        {
                            System.out.println("Скрипт выполнен");
                        }
                        finally {
                            is_console.remove(commandParts[1]);
                        }
                        break;
                    case "add_if_min":
                        hub.add_if_min();
                        break;
                    case "remove_greater":
                        try {
                            hub.remove_greater(commandParts[1]);}
                        catch (Exception e)
                        {
                            System.out.println("Попробуйте еще раз"); }
                        break;
                    case "remove_lower":
                        try {
                            hub.remove_lower(commandParts[1]);}
                        catch (Exception e)
                        {
                            System.out.println("Попробуйте еще раз"); }
                        break;
                    default:
                        System.out.println('"' + command + "\" не является командой. спользуйте help, чтобы узнать список доступных команд.");
                        break;
                }
                System.out.print('>');
                command = sc.nextLine().toLowerCase();
            }
        }
    }
}