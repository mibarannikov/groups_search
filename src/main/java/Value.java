import java.util.List;

public class Value {
    private List<String> list;
    private Integer groupNumber;

    public Value() {
    }

    public Value(List<String> list, Integer groupNumber) {

        this.list = list;
        this.groupNumber = groupNumber;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }
}
