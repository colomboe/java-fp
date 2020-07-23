package it.msec.javafp;

import org.junit.Test;

import java.util.Objects;

import static it.msec.javafp.When.when;
import static org.junit.Assert.assertEquals;

public class WhenTest {

    public static abstract class MySumType {
        private MySumType() {
        }

        public static class First extends MySumType {
            private final String firstValue;

            //region ...
            public First(String firstValue) {
                this.firstValue = firstValue;
            }

            private First(Builder builder) {
                this.firstValue = builder.firstValue;
            }

            public String getFirstValue() {
                return firstValue;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                First first = (First) o;
                return Objects.equals(firstValue, first.firstValue);
            }

            @Override
            public int hashCode() {
                return Objects.hash(firstValue);
            }

            @Override
            public String toString() {
                return "First{" +
                        "firstValue='" + firstValue + '\'' +
                        '}';
            }

            public Builder copy() {
                return builder()
                        .withFirstValue(firstValue);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {

                private String firstValue;

                public Builder withFirstValue(String firstValue) {
                    this.firstValue = firstValue;
                    return this;
                }

                public First build() {
                    return new First(this);
                }

            }
            //endregion
        }

        public static class Second extends MySumType {
            private final int secondValue;

            //region ...
            public Second(int secondValue) {
                this.secondValue = secondValue;
            }

            private Second(Builder builder) {
                this.secondValue = builder.secondValue;
            }

            public int getSecondValue() {
                return secondValue;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Second second = (Second) o;
                return secondValue != second.secondValue;
            }

            @Override
            public int hashCode() {
                return Objects.hash(secondValue);
            }

            @Override
            public String toString() {
                return "Second{" +
                        "secondValue=" + secondValue +
                        '}';
            }

            public Builder copy() {
                return builder()
                        .withSecondValue(secondValue);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {

                private int secondValue;

                public Builder withSecondValue(int secondValue) {
                    this.secondValue = secondValue;
                    return this;
                }

                public Second build() {
                    return new Second(this);
                }

            }
            //endregion
        }

        public static class Third extends MySumType {
            private final boolean thirdValue;

            //region ...
            public Third(boolean thirdValue) {
                this.thirdValue = thirdValue;
            }

            private Third(Builder builder) {
                this.thirdValue = builder.thirdValue;
            }

            public boolean isThirdValue() {
                return thirdValue;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Third third = (Third) o;
                return thirdValue != third.thirdValue;
            }

            @Override
            public int hashCode() {
                return Objects.hash(thirdValue);
            }

            @Override
            public String toString() {
                return "Third{" +
                        "thirdValue=" + thirdValue +
                        '}';
            }

            public Builder copy() {
                return builder()
                        .withThirdValue(thirdValue);
            }

            public static Builder builder() {
                return new Builder();
            }

            public static class Builder {

                private boolean thirdValue;

                public Builder withThirdValue(boolean thirdValue) {
                    this.thirdValue = thirdValue;
                    return this;
                }

                public Third build() {
                    return new Third(this);
                }

            }
            //endregion
        }

    }

    @Test
    public void exhaustiveMatch() {

        final MySumType first = new MySumType.First("myFirstValue");
        final MySumType second = new MySumType.Second(33);
        final MySumType third = new MySumType.Third(true);

        assertEquals("First: myFirstValue", exhaustive(first));
        assertEquals("Second: 33", exhaustive(second));
        assertEquals("Third: true", exhaustive(third));
    }

    private String exhaustive(MySumType m) {
        return when(m)
                .is(MySumType.First.class, f -> "First: " + f.firstValue)
                .is(MySumType.Second.class, s -> "Second: " + s.secondValue)
                .is(MySumType.Third.class, s -> "Third: " + s.thirdValue)
                .get();
    }

    @Test
    public void elseMatch() {

        final MySumType first = new MySumType.First("myFirstValue");
        final MySumType second = new MySumType.Second(33);
        final MySumType third = new MySumType.Third(true);

        assertEquals("First: myFirstValue", notExhaustiveWithElse(first));
        assertEquals("Second: 33", notExhaustiveWithElse(second));
        assertEquals("Else: Third{thirdValue=true}", notExhaustiveWithElse(third));
    }

    private String notExhaustiveWithElse(MySumType m) {
        return when(m)
                .is(MySumType.First.class, f -> "First: " + f.firstValue)
                .is(MySumType.Second.class, s -> "Second: " + s.secondValue)
                .orElse(s -> "Else: " + s);
    }

    @Test
    public void optionalMatch() {

        final MySumType first = new MySumType.First("myFirstValue");
        final MySumType second = new MySumType.Second(33);
        final MySumType third = new MySumType.Third(true);

        assertEquals("First: myFirstValue", notExhaustiveWithOptional(first));
        assertEquals("Second: 33", notExhaustiveWithOptional(second));
        assertEquals("Empty optional", notExhaustiveWithOptional(third));
    }

    private String notExhaustiveWithOptional(MySumType m) {
        return when(m)
                .is(MySumType.First.class, f -> "First: " + f.firstValue)
                .is(MySumType.Second.class, s -> "Second: " + s.secondValue)
                .asOptional()
                .orElse("Empty optional");
    }


}
