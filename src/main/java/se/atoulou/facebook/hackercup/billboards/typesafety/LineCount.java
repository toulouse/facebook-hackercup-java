package se.atoulou.facebook.hackercup.billboards.typesafety;

import com.google.common.collect.ForwardingObject;

public final class LineCount extends ForwardingObject implements Comparable<LineCount> {
    private final Integer num;

    public LineCount(Integer num) {
        this.num = num;
    }

    @Override
    protected Integer delegate() {
        return num;
    }

    @Override
    public int compareTo(LineCount o) {
        return num.compareTo(o.num);
    }

    public Integer get() {
        return num;
    }
}