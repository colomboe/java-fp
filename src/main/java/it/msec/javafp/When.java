package it.msec.javafp;

import java.util.Optional;
import java.util.function.Function;

public abstract class When {
    private When() {
    }

    public static <IN> WhenCondition<IN> when(IN input) {
        return new WhenCondition<>(input);
    }

    public static class WhenCondition<IN> {
        private final IN input;

        public WhenCondition(IN input) {
            this.input = input;
        }

        @SuppressWarnings("unchecked")
        public <T extends IN, OUT> MatchCondition<IN, OUT> is(Class<T> type, Function<T, ? extends OUT> f) {
            if (type.isInstance(input))
                return new MatchCondition.Matched<>(f.apply((T) input));
            else
                return new MatchCondition.NotMatched<>(input);
        }

        public <OUT> MatchCondition<IN, OUT> returning(Class<OUT> c) {
            return new MatchCondition.NotMatched<>(input);
        }

    }

    public abstract static class MatchCondition<IN, OUT> {

        public abstract <T extends IN> MatchCondition<IN, OUT> is(Class<T> type, Function<T, ? extends OUT> f);

        public abstract OUT orElse(Function<IN, OUT> f);

        public abstract OUT get();

        public abstract Optional<OUT> asOptional();

        public static class NotMatched<IN, OUT> extends MatchCondition<IN, OUT> {
            private final IN input;

            public NotMatched(IN input) {
                this.input = input;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T extends IN> MatchCondition<IN, OUT> is(Class<T> type, Function<T, ? extends OUT> f) {
                if (type.isInstance(input))
                    return new Matched<>(f.apply((T) input));
                else
                    return new NotMatched<>(input);
            }

            @Override
            public OUT orElse(Function<IN, OUT> f) {
                return f.apply(input);
            }

            @Override
            public OUT get() {
                throw new NoMatchException(input);
            }

            @Override
            public Optional<OUT> asOptional() {
                return Optional.empty();
            }

            public static class NoMatchException extends RuntimeException {
                private Object input;

                public NoMatchException(Object input) {
                    super("No match for value: " + input.toString());
                }
            }

        }

        public static class Matched<IN, OUT> extends MatchCondition<IN, OUT> {
            private final OUT output;

            public Matched(OUT output) {
                this.output = output;
            }

            @Override
            public <T extends IN> MatchCondition<IN, OUT> is(Class<T> type, Function<T, ? extends OUT> f) {
                return this;
            }

            @Override
            public OUT orElse(Function<IN, OUT> f) {
                return output;
            }

            @Override
            public OUT get() {
                return output;
            }

            @Override
            public Optional<OUT> asOptional() {
                return Optional.of(output);
            }
        }

    }

}
