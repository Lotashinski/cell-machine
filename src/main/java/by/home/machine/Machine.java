package by.home.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Machine {
    private Field field;

    private Predicate<CellInterface> ruleBorn;

    private Predicate<CellInterface> ruleDie;

    private Function<CellInterface, Collection<CellInterface>> neighborsRule;

    public Machine() {
        neighborsRule = c -> Arrays.asList(
                new Cell(c.getX() - 1, c.getY()),
                new Cell(c.getX(), c.getY() - 1),
                new Cell(c.getX() + 1, c.getY()),
                new Cell(c.getX(), c.getY() + 1)
        );

        ruleDie = (c) -> {
            var count = neighborsRule.apply(c).parallelStream()
                    .filter(field::contains)
                    .count();
            return count == 1 || count == 4;
        };

        ruleBorn = (c) -> {
            var count = neighborsRule.apply(c).parallelStream()
                    .filter(field::contains)
                    .count();
            return count > 1 && count < 4;
        };
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Predicate<CellInterface> getRuleBorn() {
        return ruleBorn;
    }

    public void setRuleBorn(Predicate<CellInterface> ruleBorn) {
        this.ruleBorn = ruleBorn;
    }

    public Predicate<CellInterface> getRuleDie() {
        return ruleDie;
    }

    public void setRuleDie(Predicate<CellInterface> ruleDie) {
        this.ruleDie = ruleDie;
    }

    public Function<CellInterface, Collection<CellInterface>> getNeighborsRule() {
        return neighborsRule;
    }

    public void setNeighborsRule(Function<CellInterface, Collection<CellInterface>> neighborsRule) {
        this.neighborsRule = neighborsRule;
    }

    public void tick() {
        var n = new Field();
        var collection =
                field.parallelStream()
                        // create new cells
                        .peek(c -> n.addAll(
                                // create new
                                neighborsRule.apply(c).parallelStream()
                                        // where not exist
                                        .filter(cell -> !field.contains(cell))
                                        // where born rule is true
                                        .filter(ruleBorn)
                                        .collect(Collectors.toList())
                                )
                        )
                        // die old cells
                        .filter(cell -> !ruleDie.test(cell))
                        .collect(Collectors.toList());
        n.addAll(collection);
        this.field = n;
    }
}
