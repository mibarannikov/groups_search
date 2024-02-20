import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    private static final Pattern pattern = Pattern.compile("^\"[^\"]*\"$");

    private static Set<Integer> getNeighborhood(Integer i, List<String[]> mas, List<Map<String, List<Integer>>> matrix) {
        Set<Integer> out = new HashSet<>();
        if (i >= mas.size()) {
            return out;
        }
        String[] st = mas.get(i);
        for (int j = 0; j < st.length; j++) {
            if (!"\"\"".equals(st[j]) && matrix.get(j).get(st[j]) != null) {
                out.addAll(matrix.get(j).get(st[j]));
            }
        }
        return out;
    }

    private static boolean validateString(String line) {
        if (!line.contains(";")) {
            System.out.println("wrong line: " + line);
            return false;
        }
        String[] ar = line.split(";");
        for (String s : ar) {
            if (!pattern.matcher(s).matches()) {
                System.out.println("wrong line: " + line);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String filePath = "";
        if (args.length > 0) {
            filePath = args[0];
        }
        Path path = Paths.get(filePath);
        if (!Files.isReadable(path)) {
            System.out.println("Приложение не имеет прав на чтение файла.");
            return;
        }
        List<String[]> mas = new ArrayList<>();
        List<Map<String, List<Integer>>> matrix = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] ar = line.split(";");
                if (validateString(line)) {
                    mas.add(ar);
                    for (int i = 0; i < ar.length; i++) {
                        if (matrix.size() == i) {
                            matrix.add(new HashMap<>());
                        }
                        if (!"\"\"".equals(ar[i])) {
                            if (matrix.get(i).containsKey(ar[i])) {
                                matrix.get(i).get(ar[i]).add(mas.size() - 1);
                            } else {
                                List<Integer> newList = new ArrayList<>();
                                newList.add(mas.size() - 1);
                                matrix.get(i).put(ar[i], newList);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Integer> vertexAll = matrix.stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        matrix.forEach(map ->
                map.entrySet().removeIf(entry -> entry.getValue().size() < 2)
        );
        List<Set<Integer>> out = new ArrayList<>();
        while (vertexAll.size() > 0) {
            Set<Integer> neighborhood;
            Set<Integer> connectivitySubset = new HashSet<>();
            Integer vertex = vertexAll.stream().findFirst().orElseThrow(ArrayIndexOutOfBoundsException::new);
            vertexAll.remove(vertex);
            if (getNeighborhood(vertex, mas, matrix).size() > 1) {
                connectivitySubset.add(vertex);
                Set<Integer> isolatedComponent = new HashSet<>();
                while (connectivitySubset.size() > 0) {
                    vertex = connectivitySubset.stream().findFirst().orElseThrow(ArrayIndexOutOfBoundsException::new);
                    isolatedComponent.add(vertex);
                    vertexAll.remove(vertex);
                    neighborhood = getNeighborhood(vertex, mas, matrix);
                    neighborhood.removeAll(isolatedComponent);
                    connectivitySubset.remove(vertex);
                    connectivitySubset.addAll(neighborhood);
                }
                out.add(isolatedComponent);
            }
        }
        List<Set<String>> outStr = out.stream()
                .map(setI -> setI.stream().map(i -> String.join(";", mas.get(i))).collect(Collectors.toSet()))
                .filter(setS -> setS.size() > 1)
                .sorted((o1, o2) -> Integer.compare(o2.size(), o1.size()))
                .collect(Collectors.toList());
        filePath = "out.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Количество групп: " + outStr.size());
            writer.newLine();
            int kl = 1;
            for (Set<String> set : outStr) {
                writer.write("Группа " + kl);
                writer.newLine();
                for (String str : set) {
                    writer.write(str);
                    writer.newLine();
                }
                kl++;
            }
            System.out.println("Запись завершена в файл out.txt");
            System.out.println("Время выполнения:"+(System.currentTimeMillis()-start)+" мс");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



