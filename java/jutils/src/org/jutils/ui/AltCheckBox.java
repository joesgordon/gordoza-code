package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import com.jgoodies.looks.Options;

/*******************************************************************************
 *
 ******************************************************************************/
public class AltCheckBox extends JCheckBox
{
    /** Used to be 0xD4D0C8 */
    private Color disabledBackground = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public AltCheckBox()
    {
        this( null );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public AltCheckBox( String text )
    {
        this( text, false );
    }

    /***************************************************************************
     * @param text
     * @param selected
     **************************************************************************/
    public AltCheckBox( String text, boolean selected )
    {
        super( text, null, selected );
    }

    /***************************************************************************
     * @param text
     * @param icon
     * @param selected
     **************************************************************************/
    public AltCheckBox( String text, Icon icon, boolean selected )
    {
        super( text, icon, selected );
    }

    /***************************************************************************
     * @param bg
     **************************************************************************/
    public void setDiabledBackground( Color bg )
    {
        disabledBackground = bg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Color getBackground()
    {
        if( this.isEnabled() || disabledBackground == null )
        {
            return super.getBackground();
        }

        return disabledBackground;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                }
                catch( Exception exception )
                {
                    exception.printStackTrace();
                }
                JPanel panel = new JPanel();
                JFrame frame = new JFrame();
                AltCheckBox plainCb = new AltCheckBox( "Plain" );
                AltCheckBox disabledCb = new AltCheckBox( "Disabled" );

                AltCheckBox customBgCb = new AltCheckBox( "Custom BG" );
                AltCheckBox customDisabledBgCb = new AltCheckBox(
                    "Custom BG Disabled" );

                AltCheckBox customDbgCb = new AltCheckBox( "Custom DBG" );
                AltCheckBox customDisabledDbgCb = new AltCheckBox(
                    "Custom DBG Disabled" );

                AltCheckBox customBgDbgCb = new AltCheckBox( "Custom BG/DBG" );
                AltCheckBox customDisabledBgDbgCb = new AltCheckBox(
                    "Custom BG/DBG Disabled" );

                disabledCb.setEnabled( false );

                customBgCb.setBackground( Color.red );

                customDisabledBgCb.setBackground( Color.red );
                customDisabledBgCb.setEnabled( false );

                customDbgCb.setDiabledBackground( Color.gray );

                customDisabledDbgCb.setDiabledBackground( Color.gray );
                customDisabledDbgCb.setEnabled( false );

                customBgDbgCb.setBackground( Color.red );
                customBgDbgCb.setDiabledBackground( Color.gray );

                customDisabledBgDbgCb.setBackground( Color.red );
                customDisabledBgDbgCb.setDiabledBackground( Color.gray );
                customDisabledBgDbgCb.setEnabled( false );

                panel.setLayout( new GridBagLayout() );

                panel.add( plainCb, new GridBagConstraints( 0, 0, 1, 1, 1.0,
                    0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( disabledCb, new GridBagConstraints( 0, 1, 1, 1, 1.0,
                    0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customBgCb, new GridBagConstraints( 0, 2, 1, 1, 1.0,
                    0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customDisabledBgCb, new GridBagConstraints( 0, 3, 1,
                    1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customDbgCb, new GridBagConstraints( 0, 4, 1, 1,
                    1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customDisabledDbgCb, new GridBagConstraints( 0, 5,
                    1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customBgDbgCb, new GridBagConstraints( 0, 6, 1, 1,
                    1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                panel.add( customDisabledBgDbgCb, new GridBagConstraints( 0, 7,
                    1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets( 2, 2, 2, 2 ), 0, 0 ) );

                frame.setTitle( "UCheckBox test" );
                frame.setContentPane( panel );
                frame.pack();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }
}
