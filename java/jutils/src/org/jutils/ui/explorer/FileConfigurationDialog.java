package org.jutils.ui.explorer;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import org.jutils.ui.app.AppRunnable;
import org.jutils.ui.app.IApplication;
import org.jutils.ui.explorer.data.*;

/*******************************************************************************
 * 
 *
 ******************************************************************************/
public class FileConfigurationDialog extends JDialog
{
    /**  */
    private final JCheckBox useCustomCheckBox;
    /**  */
    private final ExtensionsPanel extsPanel;

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public FileConfigurationDialog( JFrame parent )
    {
        super( parent, "File Configuration", ModalityType.DOCUMENT_MODAL );

        this.useCustomCheckBox = new JCheckBox();
        this.extsPanel = new ExtensionsPanel();

        JPanel contentPanel = ( JPanel )this.getContentPane();

        contentPanel.setLayout( new GridBagLayout() );

        useCustomCheckBox.setText( "Use Custom File Manager" );

        contentPanel.add( useCustomCheckBox,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 10, 20, 10, 10 ), 0, 0 ) );

        contentPanel.add( extsPanel,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setData( FileAppConfiguration data )
    {
        useCustomCheckBox.setSelected( data.useCustom );

        extsPanel.setData( data );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        SwingUtilities.invokeLater( new AppRunnable( new TestMainApp() ) );
    }

    /***************************************************************************
     * @param frame
     * @return
     **************************************************************************/
    public static FileConfigurationDialog showDialog( JFrame frame )
    {
        FileConfigurationDialog dialog = new FileConfigurationDialog( frame );
        dialog.setSize( new Dimension( 600, 400 ) );
        dialog.validate();
        dialog.setLocationRelativeTo( frame );
        dialog.setVisible( true );
        return dialog;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static FileAppConfiguration getUnitTestData()
    {
        FileAppConfiguration configData = new FileAppConfiguration();
        ApplicationConfig pgm = null;
        ExtensionConfig ext = null;

        // Create java files and associated programs.

        ext = new ExtensionConfig( "java", "Java Source File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = ( "gedit" );
        pgm.path = new File( "/usr/bin/gedit" );
        pgm.args = ( "-hoopde" );

        configData.apps.add( pgm );
        ext.programs.add( pgm.name );

        // Create txt files and associated programs.

        ext = new ExtensionConfig( "txt", "Ascii Text File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = "file-roller";
        pgm.path = new File( "/usr/bin/file-roller" );

        configData.apps.add( pgm );
        ext.programs.add( pgm.name );

        pgm = new ApplicationConfig();
        pgm.name = "Firefox";
        pgm.path = new File( "/usr/bin/firefox" );

        configData.apps.add( pgm );
        ext.programs.add( pgm.name );

        return configData;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestMainApp implements IApplication
    {
        @Override
        public void createAndShowUi()
        {
            FileConfigurationDialog dialog = new FileConfigurationDialog(
                null );
            dialog.setData( getUnitTestData() );
            dialog.setTitle( "File Config Test" );
            dialog.setSize( new Dimension( 600, 400 ) );
            dialog.validate();
            dialog.setLocationRelativeTo( null );
            dialog.setVisible( true );
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }
    }
}
