package se.atoulou.facebook.hackercup.billboards.typesafety;

import com.google.common.collect.ForwardingObject;

public final class Width extends ForwardingObject implements Comparable<Width> {
    private final Integer num;

    public Width(Integer num) {
        this.num = num;
    }

    @Override
    protected Integer delegate() {
        return num;
    }

    @Override
    public int compareTo(Width o) {
        return num.compareTo(o.num);
    }

    public Integer get() {
        return num;
    }
}