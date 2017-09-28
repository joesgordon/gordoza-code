package org.jutils.ui.net;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.jutils.Utils;
import org.jutils.net.NetMessage;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.model.ITableItemsConfig;

/***************************************************************************
 * 
 **************************************************************************/
public class NetMessagesTableConfig implements ITableItemsConfig<NetMessage>
{
    /**  */
    private static final String [] NAMES = new String[] { "Tx/Rx", "Time",
        "Address", "Port", "Contents" };
    /**  */
    private static final Class<?> [] CLASSES = new Class<?>[] { String.class,
        LocalDateTime.class, InetAddress.class, Integer.class, String.class };

    /**  */
    private final Charset utf8;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessagesTableConfig()
    {
        this.utf8 = Charset.forName( "UTF-8" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return NAMES;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return CLASSES;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getItemData( NetMessage item, int col )
    {
        switch( col )
        {
            case 0:
                return item.received ? "Rx" : "Tx";

            case 1:
                return item.time;

            case 2:
                return item.address;

            case 3:
                return item.port;

            case 4:
            {
                int cnt = Math.min( item.contents.length, 64 );
                byte [] buf = new byte[cnt];
                Utils.byteArrayCopy( item.contents, 0, buf, 0, buf.length );
                HexUtils.cleanAscii( buf, 0, cnt );
                return new String( buf, 0, cnt, utf8 );
            }

            default:
                break;
        }

        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( NetMessage item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( NetMessage item, int col )
    {
        return false;
    }
}
