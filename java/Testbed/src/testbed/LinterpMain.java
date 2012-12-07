package testbed;

public class LinterpMain
{
    public static void main( String[] args )
    {
        final double x = 5;
        double y;

        CartPoint p0 = new CartPoint( 4, 1 );
        CartPoint p1 = new CartPoint( 6, 3 );

        y = linterp( x, p0, p1 );
        System.out.println( "Standard: " + y );

        CartPoint log0 = new CartPoint( Math.log10( p0.x ), p0.y );
        CartPoint log1 = new CartPoint( Math.log10( p1.x ), p1.y );

        y = linterp( Math.log10( x ), log0, log1 );
        System.out.println( "log10:    " + y );

        CartPoint ln0 = new CartPoint( Math.log( p0.x ), p0.y );
        CartPoint ln1 = new CartPoint( Math.log( p1.x ), p1.y );

        y = linterp( Math.log( x ), ln0, ln1 );
        System.out.println( "ln:       " + y );

        CartPoint exp0 = new CartPoint( p0.x, Math.pow( 10, p0.y ) );
        CartPoint exp1 = new CartPoint( p1.x, Math.pow( 10, p1.y ) );

        y = linterp( x, exp0, exp1 );
        System.out.println( "exp:      " + Math.log10( y ) + " = log10(" + y +
            ")" );

        CartPoint expn0 = new CartPoint( p0.x, Math.pow( Math.E, p0.y ) );
        CartPoint expn1 = new CartPoint( p1.x, Math.pow( Math.E, p1.y ) );

        y = linterp( x, expn0, expn1 );
        System.out.println( "exp:      " + Math.log( y ) + " = ln(" + y + ")" );
    }

    private static double linterp( double x, CartPoint p0, CartPoint p1 )
    {
        return ( x - p0.x ) * ( p1.y - p0.y ) / ( p1.x - p0.x ) + p0.y;
    }

    private static class CartPoint
    {
        public final double x;
        public final double y;

        public CartPoint( double x, double y )
        {
            this.x = x;
            this.y = y;
        }
    }
}
