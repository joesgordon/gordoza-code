package testbed;

/**
 * http://stackoverflow.com/questions/8185472/
 */
public class GenericsTest
{
    public interface A<X>
    {
        X get();

        void doStuff( X x );
    }

    @SuppressWarnings( "unused")
    private static <T extends A<?>> void foo( T t )
    {
        // ---------------------------------------------------------------------
        // Doesn't compile:
        // The method bar(GenericsTest.A<X>) in the type GenericsTest is not
        // applicable for the arguments (T)
        // ---------------------------------------------------------------------

        bar( ( A<?> )t );
    }

    @SuppressWarnings( "unused")
    private static <T extends A<?>> void foo2( T t )
    {
        // ---------------------------------------------------------------------
        // No explicit cast required, no "unchecked" warning!
        // Why does this work?
        // ---------------------------------------------------------------------
        A<?> u = t;
        bar( u );
    }

    @SuppressWarnings( "unused")
    private static <X, T extends A<X>> void foo3( T t )
    {
        bar( t );
    }

    @SuppressWarnings( "unused")
    private <T extends A<?>> void foo4( /* T t */ )
    {
        // ---------------------------------------------------------------------
        // Doesn't compile:
        // The method bar(GenericsTest.A<X>) in the type GenericsTest is not
        // applicable for the arguments (T)
        // ---------------------------------------------------------------------

        // T u = t;
        // bar( u );
    }

    private static <X> void bar( A<X> t )
    {
        X x = t.get();
        t.doStuff( x );
    }
}
