package org.jutils.licensing;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.jutils.ui.AltEditorPane;

import com.jgoodies.looks.Options;

/***************************************************************************
 * @author jgordon
 **************************************************************************/
public class LicenseDialog extends JDialog
{
    /**  */
    private static final String LICENSE_HEX_EDITOR = "hexEditorLicense.html";

    /**  */
    private static final String LICENSE_FORMS = "jgoodiesFormsLicense.html";

    /**  */
    private static final String LICENSE_LOOKS = "jgoodiesLooksLicense.html";

    /**  */
    private static final String LICENSE_XPP3 = "xpp3License.html";

    /**  */
    private static final String LICENSE_XSTREAM = "xstreamLicense.html";

    /**  */
    private static final String LICENSE_OPENICON = "openIconLibraryLicense.html";

    /**  */
    private static final String LICENSE_FARMFRESH = "farmFreshWebIconsLicense.html";

    /**  */
    private static final String LICENSE_CRYSTALCLEAR = "crystalClearLicense.html";

    /**  */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public LicenseDialog( Frame owner )
    {
        super( owner, true );
        Class<? extends LicenseDialog> ldClass = this.getClass();
        LicensePanel hePanel = new LicensePanel(
            ldClass.getResource( LICENSE_HEX_EDITOR ) );
        LicensePanel jgFormsPanel = new LicensePanel(
            ldClass.getResource( LICENSE_FORMS ) );
        LicensePanel jgLooksPanel = new LicensePanel(
            ldClass.getResource( LICENSE_LOOKS ) );
        LicensePanel xpp3Panel = new LicensePanel(
            ldClass.getResource( LICENSE_XPP3 ) );
        LicensePanel xstreamPanel = new LicensePanel(
            ldClass.getResource( LICENSE_XSTREAM ) );
        LicensePanel crystalClearPanel = new LicensePanel(
            ldClass.getResource( LICENSE_CRYSTALCLEAR ) );
        LicensePanel farmFreshPanel = new LicensePanel(
            ldClass.getResource( LICENSE_FARMFRESH ) );
        LicensePanel openIconPanel = new LicensePanel(
            ldClass.getResource( LICENSE_OPENICON ) );

        this.setLayout( new BorderLayout() );

        this.add( tabbedPane, BorderLayout.CENTER );

        tabbedPane.addTab( "HexEditor", hePanel );
        tabbedPane.addTab( "JGoodies Forms", jgFormsPanel );
        tabbedPane.addTab( "JGoodies Looks", jgLooksPanel );
        tabbedPane.addTab( "XPP3", xpp3Panel );
        tabbedPane.addTab( "XStream", xstreamPanel );
        tabbedPane.addTab( "Crystal Clear Icons", crystalClearPanel );
        tabbedPane.addTab( "Farm-Fresh Web Icons", farmFreshPanel );
        tabbedPane.addTab( "Open Icon Library", openIconPanel );

        this.setTitle( "License Information" );
        this.setSize( 400, 400 );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( Options.PLASTICXP_NAME );
                    UIManager.put( "TabbedPaneUI",
                        BasicTabbedPaneUI.class.getCanonicalName() );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }

                LicenseDialog d = new LicenseDialog( null );
                d.validate();
                d.setLocationRelativeTo( null );
                d.setVisible( true );
            }
        } );
    }
}

class LicensePanel extends JPanel
{
    private AltEditorPane editorPane = new AltEditorPane();

    private JScrollPane scrollPane = new JScrollPane( editorPane );

    public LicensePanel( URL pageUrl )
    {
        this.setLayout( new BorderLayout() );

        editorPane.setContentType( "text/html" );
        if( pageUrl != null )
        {
            try
            {
                editorPane.setPage( pageUrl );
            }
            catch( IOException e )
            {
                throw new IllegalArgumentException( pageUrl.getFile() +
                    " not found!" );
            }
        }
        else
        {
            editorPane.setText( "<html>null url</html>" );
        }

        this.add( scrollPane, BorderLayout.CENTER );
    }
}
