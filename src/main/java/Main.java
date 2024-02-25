import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Pattern pattern = Pattern.compile("^\"[^\"]*\"$");

    private static boolean validateString(String line) {
        if (!line.contains(";")) {
            System.out.println("wrong line: " + line);
            return false;
        }
        String[] ar = line.split(";");
        for (String s : ar) {
            if (!s.isEmpty()) {
                if (!pattern.matcher(s).matches()) {
                    System.out.println("wrong line: " + line);
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long start1 = System.currentTimeMillis();
        String filePath = "C:\\Users\\mibar\\IdeaProjects\\new\\ing.txt";
        if (args.length > 0) {
            filePath = args[0];
        }
        Path path = Paths.get(filePath);
        if (!Files.isReadable(path)) {
            System.out.println("Приложение не имеет прав на чтение файла.");
            return;
        }
        List<String> mas = new ArrayList<>();
        List<Map<String, Value>> matrix = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path)) {
            lines.filter(Main::validateString)
                    .forEach(line -> {
                        String[] ar = line.split(";");
                        mas.add(line);
                        List<Value> listValue = new ArrayList<>();
                        for (int i = 0; i < ar.length; i++) {
                            if (matrix.size() == i) {
                                matrix.add(new HashMap<>());
                            }
                            if (!"\"\"".equals(ar[i]) && !ar[i].isEmpty()) {
                                Value value;
                                if (matrix.get(i).containsKey(ar[i])) {
                                    value = matrix.get(i).get(ar[i]);
                                    value.getList().add(mas.size() - 1);

                                } else {
                                    List<Integer> newList = new ArrayList<>();
                                    newList.add(mas.size() - 1);
                                    value = new Value(newList, mas.size());
                                    matrix.get(i).put(ar[i], value);
                                }
                                listValue.add(value);
                            }
                        }
                        Integer min = listValue.stream().mapToInt(Value::getGroupNumber).min().orElse(0);//todo throw
                        listValue.forEach(v -> v.setGroupNumber(min));
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        List< Set<String>> outt = matrix.stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .map(Map.Entry::getValue)
                .filter(v -> v.getList().size() > 1)
                .collect(Collectors.groupingBy(Value::getGroupNumber, Collectors.toList()))
                .values()
                .stream()
                .map(values -> values.stream().map(Value::getList).flatMap(List::stream).collect(Collectors.toList()))
                .map(l -> l.stream().map(mas::get).collect(Collectors.toSet()))
                .filter(l -> l.size()>1)
                .sorted((o1,o2) -> Integer.compare(o2.size(),o1.size()))
                .collect(Collectors.toList());
        System.out.println("Количество групп " + outt.size());
        filePath = "out.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Количество групп: " + outt.size());
            writer.newLine();
            int kl = 1;
            for (Set<String> set : outt) {
                writer.write("Группа " + kl);
                writer.newLine();
                for (String str : set) {
                    writer.write(str);
                    writer.newLine();
                }
                kl++;
            }
            System.out.println();
            System.out.println("Запись завершена в файл out.txt");
            System.out.println("Время выполнения:" + (System.currentTimeMillis() - start1) + " мс");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



