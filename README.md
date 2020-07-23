# Functional domain modelling tools for Java
Utilities and conventions for functional domain modelling in Java

---

### Code generation
Using the `Code Generator` plugin for IntelliJ IDEA, import the code-generator.xml template in order to have a one-click code generation tool that, starting from some code like this:

```java
public class Customer {

    private final String name;
    private final String surname;
    private final Integer age;
    
}
```

it generates constructor, getters, equals, hashCode, toString, builder and copy method. Essentially an equivalent of the Kotlin `data class` (immutable) definition. No plugin/annotation processor are required at compile time, it is just an IDE shortcut that generates the code for you.

---

### When.java
An utility class for pattern matching over different sub-types, with smart casting. Usage example:

```java
var description = when(address)
                .is(EmailAddress.class, a -> "Email address " + a.email)
                .is(PostalAddress.class, a -> "Postal address " + a.postalAddress)
                .is(EmailAndPostalAddress.class, a -> "Email " + a.email + ", postal " + a.postalAddress)
                .orElse(a -> "No address provided");
```

It provides also the ability to don't make exhaustive matches with `asOptional()` method, or to don't provide a catch-all alternative replacing the `orElse()` with `get()` (that throws an exception if no match is found). Compile time exhaustiveness check can't be provided.

If the return type of the `when()` expression is implicit and uses sub-typing (eg. a sum type), you can specify the return type using the `returning()` method just after the `when()` call. In this way you don't have to make a cast inside the lambda expressions of the matches.

---

### Algebraic data types

#### Product type:
Just use POJO-like classes, using the code generation tool provided here. Define all fields as `private` and `final`; even if debated, use `Optional<T>` (or `Option<T>` from [vavr](https://www.vavr.io/vavr-docs/#_option)) in order to describe fields that can have no value. Never use `null` values.

#### Coproduct (sum) type:
Define an abstract class with a private constructor, then you can define all the possible "constructors" you need by defining some static inner classes that extend the abstract class. In this way, only inner classes defined inside the abstract class body can extend it, modelling something that can be compared to a Kotlin sealed class. Example:

```java
public abstract class Shipment {
    
    private Shipment() { }

    public static class ElectronicShipment extends Shipment {
        public final EmailAddress email;

        // ... Constructor, equals, hashcode and toString ...
    }

    public static class PostalShipment extends Shipment {
        public final PostalAddress postalAddress;

        // ... Constructor, equals, hashcode and toString ...
    }

}
```

---

### Railway oriented programming and other functional programming stuff 
You can use the Result class provided here. Either from 
[vavr](https://www.vavr.io/vavr-docs/#_either) is also an alternative. 
[Vavr](https://www.vavr.io/) also provide some more tools that can be useful
for functional programming in Java, so I suggest to look at it before implementing
something from scratch. The `When.java` code is proposed here instead of the vavr
pattern matching feature just because it is simpler and more readable. If you need
something more advanced, you can use the Vavr pattern matching.

For most use cases, the `Result` and the `When` classes in this repository are what
 you need in order to structure your domain code. 
