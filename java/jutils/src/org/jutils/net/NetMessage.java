package org.jutils.net;

import java.time.LocalDateTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessage
{
    // TODO Consider using java.time.Instant instead of LocalDateTime.

    /** {@code true} indicates the message was received; {@code false}, sent */
    public final boolean received;
    /** Time of transmission or reception. */
    public final LocalDateTime time;
    /** The local NIC address on which the message was Tx/Rx. */
    public final String localAddress;
    /** The local port on which the message was Tx/Rx. */
    public final int localPort;
    /** The address of the remote system the message was Tx/Rx. */
    public final String remoteAddress;
    /** The port on the remote system the message was Tx/Rx. */
    public final int remotePort;
    /** The raw message contents. */
    public final byte [] contents;

    /***************************************************************************
     * @param received
     * @param localAddress
     * @param localPort
     * @param remoteAddress
     * @param remotePort
     * @param contents
     **************************************************************************/
    public NetMessage( boolean received, String localAddress, int localPort,
        String remoteAddress, int remotePort, byte [] contents )
    {
        this( received, LocalDateTime.now(), localAddress, localPort,
            remoteAddress, remotePort, contents );
    }

    /***************************************************************************
     * @param received
     * @param time
     * @param localAddress
     * @param localPort
     * @param remoteAddress
     * @param remotePort
     * @param contents
     **************************************************************************/
    public NetMessage( boolean received, LocalDateTime time,
        String localAddress, int localPort, String remoteAddress,
        int remotePort, byte [] contents )
    {
        this.received = received;
        this.time = time;
        this.localAddress = localAddress;
        this.localPort = localPort;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
        this.contents = contents;
    }
}
