package cn.myhomespace.zhou.object;

/**
 * Created by zhouw on 2018/5/2.
 */
public class WhereParam {
    private String left;
    private String oper;
    private String right;

    public WhereParam(String left, String oper, String right) {
        this.left = left;
        this.oper = oper;
        this.right = right;
    }

    public WhereParam() {
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
