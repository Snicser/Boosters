package io.github.jordieh.boosters.common;

import io.github.jordieh.boosters.framework.booster.Booster;

import java.util.HashSet;
import java.util.Iterator;

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

}
