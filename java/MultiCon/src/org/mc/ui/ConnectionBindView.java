package org.mc.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionBindView implements IView<JPanel>
{
    /**  */
    public static final String BIND_TEXT = "Bind";
    /**  */
    public static final String UNBIND_TEXT = "Unbind";

    /**  */
    private final JPanel view;
    /**  */
    private final JButton bindButton;
    /**  */
    private final Icon bindIcon;
    /**  */
    private final Icon unbindIcon;

    /**  */
    private IUpdater<Boolean> updater;
    /**  */
    private boolean bound;

    /***************************************************************************
     * @param dataView
     **************************************************************************/
    public ConnectionBindView( IView<?> dataView )
    {
        this.view = new JPanel( new GridBagLayout() );

        this.bindButton = new JButton();
        this.bindIcon = IconConstants.getIcon( IconConstants.CHECK_16 );
        this.unbindIcon = IconConstants.getIcon( IconConstants.STOP_16 );

        bindButton.setIcon( bindIcon );
        bindButton.setText( BIND_TEXT );
        bindButton.addActionListener( ( e ) -> invokeCallback() );

        view.setBorder( BorderFactory.createTitledBorder( "Configuration" ) );

        view.add( dataView.getView(),
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 10 ) );
        view.add( bindButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTH, GridBagConstraints.NONE,
                new Insets( 6, 0, 6, 6 ), 0, 10 ) );

        setBound( false );
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
     * 
     **************************************************************************/
    private void invokeCallback()
    {
        if( this.updater != null )
        {
            this.updater.update( !bound );
        }
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void setCallback( IUpdater<Boolean> updater )
    {
        this.updater = updater;
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
        this.bound = bound;

        bindButton.setText( bound ? UNBIND_TEXT : BIND_TEXT );
        bindButton.setIcon( bound ? unbindIcon : bindIcon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isBound()
    {
        return bound;
    }
}
