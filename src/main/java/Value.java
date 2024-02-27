public class Value {
    private String[] list;
    private int groupNumber;

    public Value() {
    }

    public Value(String[] list, int groupNumber) {

        this.list = list;
        this.groupNumber = groupNumber;
    }

    public String[] getList() {
        return list;
    }

    public void add(String s) {
        String[] newArray = new String[list.length + 1];
        System.arraycopy(list, 0, newArray, 0, list.length);
        newArray[newArray.length - 1] = s;
        list = newArray;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }
}
