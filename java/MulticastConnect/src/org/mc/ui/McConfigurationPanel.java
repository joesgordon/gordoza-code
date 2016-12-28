package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.model.IView;
import org.mc.io.MulticastSocketDef;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McConfigurationPanel implements IView<JPanel>
{
    /**  */
    public static final String bindText = "Bind";
    /**  */
    public static final String unbindText = "Unbind";

    /**  */
    private final JPanel view;
    /**  */
    private final JButton bindButton;
    /**  */
    private final Icon checkIcon;
    /**  */
    private final Icon deleteIcon;
    /**  */
    private final MulticastSocketDefView multicastView;

    /***************************************************************************
     * 
     **************************************************************************/
    public McConfigurationPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        this.bindButton = new JButton();
        this.checkIcon = IconConstants.loader.getIcon( IconConstants.CHECK_16 );
        this.deleteIcon = IconConstants.loader.getIcon( IconConstants.STOP_16 );

        this.multicastView = new MulticastSocketDefView();

        bindButton.setIcon( checkIcon );
        bindButton.setText( bindText );

        view.setBorder( BorderFactory.createTitledBorder( "Configuration" ) );

        view.add( multicastView.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 10 ) );
        view.add( bindButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE,
                new Insets( 6, 0, 6, 6 ), 0, 10 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public MulticastSocketDef getSocket()
    {
        return multicastView.getData();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBound()
    {
        return bindButton.getText().equals( bindText );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addBindActionListener( ActionListener l )
    {
        bindButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeBindActionListener( ActionListener l )
    {
        bindButton.removeActionListener( l );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setBindEnabled( boolean enabled )
    {
        bindButton.setEnabled( enabled );
    }

    /***************************************************************************
     * @param bound
     **************************************************************************/
    public void setBound( boolean bound )
    {
        bindButton.setText( bound ? unbindText : bindText );
        bindButton.setIcon( bound ? deleteIcon : checkIcon );

        multicastView.setEnabled( !bound );
    }
}
