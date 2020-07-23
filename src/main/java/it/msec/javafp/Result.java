package it.msec.javafp;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Result<ERROR, SUCCESS> {
    private Result() { }

    @SuppressWarnings("unchecked")
    public static <ERROR, SUCCESS> Result<ERROR, SUCCESS> narrow(Result<? extends ERROR, ? extends SUCCESS> result) {
        return (Result<ERROR, SUCCESS>) result;
    }

    public abstract <U> Result<ERROR, U> map(Function<? super SUCCESS, ? extends U> f);
    public abstract <U> Result<U, SUCCESS> mapFailure(Function<? super ERROR, ? extends U> f);

    public abstract <U> Result<ERROR, U> flatMap(Function<? super SUCCESS, ? extends Result<ERROR, ? extends U>> f);

    public abstract <U> U fold(Function<? super ERROR, ? extends U> fl, Function<? super SUCCESS, ? extends U> fr);

    public static <E, A, U> Result<E, U> bracket(Supplier<Result<E, A>> acquire,
                                                                 Consumer<A> release,
                                                                 Function<A, Result<E, U>> use) {
        return acquire.get()
                .flatMap(a -> {
                    try {
                        return use.apply(a);
                    } finally {
                        release.accept(a);
                    }
                });
    }

    public static <L, R> Result<L, R> success(R r) {
        return new Success<>(r);
    }

    public static <L, R> Result<L, R> failure(L l) {
        return new Failure<>(l);
    }

    public static class Success<L, R> extends Result<L, R>
    {

        private final R value;

        private Success(R value) {
            this.value = value;
        }


        @Override
        public <U> Result<L, U> map(Function<? super R, ? extends U> f) {
            return new Success<>(f.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<U, R> mapFailure(Function<? super L, ? extends U> f) {
            return (Result<U, R>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<L, U> flatMap(Function<? super R, ? extends Result<L, ? extends U>> f) {
            return (Result<L, U>) f.apply(value);
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> fl, Function<? super R, ? extends U> fr) {
            return fr.apply(value);
        }

        public R getValue() {
            return value;
        }
    }

    public static class Failure<L, R> extends Result<L, R>
    {

        private final L value;

        private Failure(L value) {
            this.value = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<L, U> map(Function<? super R, ? extends U> f) {
            return (Result<L, U>) this;
        }

        @Override
        public <U> Result<U, R> mapFailure(Function<? super L, ? extends U> f) {
            return new Failure<>(f.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <U> Result<L, U> flatMap(Function<? super R, ? extends Result<L, ? extends U>> f) {
            return (Result<L, U>) this;
        }

        @Override
        public <U> U fold(Function<? super L, ? extends U> fl, Function<? super R, ? extends U> fr) {
            return fl.apply(value);
        }

        public L getValue() {
            return value;
        }
    }

}
