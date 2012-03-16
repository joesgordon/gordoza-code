package nmrc;

import java.awt.Image;
import java.util.List;

import org.jutils.IconLoader;

public class NmrIconLoader extends IconLoader
{
    public static final String NMR_16 = "nmrc16.png";
    public static final String NMR_24 = "nmrc24.png";
    public static final String NMR_32 = "nmrc32.png";
    public static final String NMR_48 = "nmrc48.png";
    public static final String NMR_64 = "nmrc64.png";
    public static final String NMR_128 = "nmrc128.png";

    public NmrIconLoader()
    {
        super( NmrIconLoader.class, "icons" );
    }

    public List<Image> getNmrImages()
    {
        return super.getImages( NMR_32, NMR_48, NMR_64, NMR_128 );
    }
}
