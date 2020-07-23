package it.msec.javafp;

import org.junit.Test;

import java.util.ArrayList;

import static it.msec.javafp.Result.*;
import static org.junit.Assert.assertEquals;

public class ResultTest {

    @Test
    public void successful() {

        final var r = Result.<Integer, String>success("Hello")
                .map(s -> s + " world!")
                .flatMap(s -> success("Very " + s))
                .mapFailure(i -> i + 1)
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Successfully Very Hello world!", r);
    }

    @Test
    public void startWithFailure() {

        final var r = Result.<Integer, String>failure(11)
                .map(s -> s + " world!")
                .flatMap(s -> success("Very " + s))
                .mapFailure(i -> i + 1)
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Bad 12", r);
    }

    @Test
    public void intermediateFailure() {

        final var r = Result.<Integer, String>success("Hello")
                .map(s -> s + " world!")
                .flatMap(s -> failure(33))
                .mapFailure(i -> i + 1)
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Bad 34", r);
    }

    @Test
    public void bracketSuccessful() {

        var released = new ArrayList<String>();

        final var r =
                bracket(
                        () -> success("diamond"),
                        (s) -> released.add("Released " + s),
                        (s) -> success("Very precious " + s))
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Successfully Very precious diamond", r);
        assertEquals("Released diamond", released.get(0));
    }

    @Test
    public void bracketAcquisitionFailed() {

        var released = new ArrayList<String>();

        final var r =
                bracket(
                        () -> failure(33),
                        (s) -> released.add("Released " + s),
                        (s) -> success("Very precious " + s))
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Bad 33", r);
        assertEquals(0, released.size());
    }

    @Test
    public void bracketUsageFailed() {

        var released = new ArrayList<String>();

        final var r =
                bracket(
                        () -> success("diamond"),
                        (s) -> released.add("Released " + s),
                        (s) -> failure("bad usage of " + s))
                .fold(i -> "Bad " + i, s -> "Successfully " + s);

        assertEquals("Bad bad usage of diamond", r);
        assertEquals(1, released.size());
    }

    @Test
    public void bracketException() {

        var released = new ArrayList<String>();

        try {
            bracket(
                    () -> success("diamond"),
                    (s) -> released.add("Released " + s),
                    (s) -> { throw new IllegalStateException("Bla bla bla"); })
                    .fold(i -> "Bad " + i, s -> "Successfully " + s);
        } catch (Throwable ignored) { }

        assertEquals(1, released.size());
    }


}
