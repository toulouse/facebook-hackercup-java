package se.atoulou.facebook.hackercup.billboards.typesafety;

import com.google.common.collect.ForwardingObject;

public final class LineWidth extends ForwardingObject implements Comparable<LineWidth> {
    private final Integer num;

    public LineWidth(Integer num) {
        this.num = num;
    }

    @Override
    protected Integer delegate() {
        return num;
    }

    @Override
    public int compareTo(LineWidth o) {
        return num.compareTo(o.num);
    }

    public Integer get() {
        return num;
    }
}