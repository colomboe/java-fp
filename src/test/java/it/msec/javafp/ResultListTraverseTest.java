package it.msec.javafp;

import it.msec.javafp.Result.Failure;
import it.msec.javafp.Result.Success;
import org.junit.Test;

import java.util.List;

import static it.msec.javafp.Result.failure;
import static it.msec.javafp.Result.success;
import static it.msec.javafp.ResultListTraverse.traverse;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ResultListTraverseTest {

    @Test
    public void traverseAllSuccess() {

        final var listOfResults = asList(success(11), success(22), success(33));
        final var resultOfList = traverse(listOfResults, (i) -> "Number " + i);

        assertEquals(Success.class, resultOfList.getClass());

        final var success = (Success<?, List<String>>) resultOfList;
        final var content = success.getValue();
        assertEquals(3, content.size());
        assertEquals("Number 11", content.get(0));
        assertEquals("Number 22", content.get(1));
        assertEquals("Number 33", content.get(2));
    }

    @Test
    public void traverseWithAFailure() {

        final List<Result<String, Integer>> listOfResults = asList(success(11), failure("ERROR!!!"), success(33));
        final var resultOfList = traverse(listOfResults, (i) -> "Number " + i);

        assertEquals(Failure.class, resultOfList.getClass());

        final var failure = (Failure<String, ?>) resultOfList;
        assertEquals("ERROR!!!", failure.getValue());
    }
}
