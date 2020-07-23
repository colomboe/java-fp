package it.msec.javafp;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static it.msec.javafp.Result.success;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;

public class ResultListTraverse {

    public static <A, B, C> Result<A, List<C>> traverse(List<Result<A, B>> list, Function<B, C> f) {
        return list.stream().reduce(
                success(emptyList()),
                (acc, e) -> acc.flatMap(l -> e.map(r -> addToList(l, f.apply(r)))),
                (a, b) -> a.flatMap(l1 -> b.map(l2 -> mergeLists(l1, l2)))
        );
    }

    public static <A, B> Result<A, List<B>> sequence(List<Result<A, B>> list) {
        return traverse(list, identity());
    }

    private static <B> List<B> mergeLists(List<B> l1, List<B> l2) {
        final LinkedList<B> newList = new LinkedList<>(l1);
        newList.addAll(l2);
        return newList;
    }

    private static <B> List<B> addToList(List<B> list, B b) {
        final LinkedList<B> newList = new LinkedList<>(list);
        newList.add(b);
        return newList;
    }

}
