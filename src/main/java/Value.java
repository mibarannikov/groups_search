import java.util.List;

public class Value {
    private List<Integer> list;
    private Integer groupNumber;

    public Value() {
    }

    public Value(List<Integer> list, Integer groupNumber) {

        this.list = list;
        this.groupNumber = groupNumber;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }
}
