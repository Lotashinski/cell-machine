package by.home.machine;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Field extends CopyOnWriteArraySet<CellInterface> implements Serializable {
    private Predicate<CellInterface> filter;

    public Field() {
        filter = c -> true;
    }

    public Field(Collection<? extends Cell> c, Predicate<CellInterface> filter) {
        super(c);
        this.filter = filter;
    }

    @Override
    public boolean add(CellInterface cell) {
        var filter = this.filter;
        return filter.test(cell)
                && super.add(cell);
    }

    @Override
    public boolean addAll(Collection<? extends CellInterface> c) {
        var filter = this.filter;
        return super.addAll(c.parallelStream()
                .filter(filter)
                .collect(Collectors.toList())
        );
    }
}
