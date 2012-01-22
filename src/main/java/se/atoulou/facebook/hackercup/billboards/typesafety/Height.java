package se.atoulou.facebook.hackercup.billboards.typesafety;

import com.google.common.collect.ForwardingObject;

public final class Height extends ForwardingObject implements Comparable<Height> {
    private final Integer num;

    public Height(Integer num) {
        this.num = num;
    }

    @Override
    protected Integer delegate() {
        return num;
    }

    @Override
    public int compareTo(Height o) {
        return num.compareTo(o.num);
    }

    public Integer get() {
        return num;
    }
}