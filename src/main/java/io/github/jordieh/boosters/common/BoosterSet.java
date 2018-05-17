package io.github.jordieh.boosters.common;

import io.github.jordieh.boosters.framework.booster.Booster;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public final class BoosterSet extends HashSet<Booster> {

    public void clean() {
        if (isEmpty()) {
            return;
        }
        Iterator<Booster> iterator = iterator();
        while (iterator.hasNext()) {
            Booster booster = iterator.next();
            if (booster.getRemainingTime() < 0) {
                booster.deactivate();
                iterator.remove();
            }
        }
    }

    public int totalPercentage() {
        int sum = 0;
        for (Booster booster : this) {
            if (booster.isActive()) {
                int percentage = booster.getPercentage();
                sum += percentage;
            }
        }
        return sum;
    }

    public List<Booster> sorted() {
        ArrayList<Booster> list = new ArrayList<>(this);
        list.sort(Comparator.comparingLong(Booster::getRemainingTime));
        return list;
    }

}
