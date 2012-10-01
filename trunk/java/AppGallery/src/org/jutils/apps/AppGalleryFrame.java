package org.jutils.apps;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.model.IJcompView;

/*******************************************************************************
 * This class defines the frame that displays the main applications contained in
 * JUtils.
 ******************************************************************************/
public class AppGalleryFrame implements IJcompView
{
    private final int APPLICATION_GAP = 40;
    private final int COMPONENT_GAP = 10;

    private final JPanel panel;

    /***************************************************************************
     * Creates a new AppGallery frame.
     **************************************************************************/
    public AppGalleryFrame( List<ILibraryApp> apps )
    {
        panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        Insets insets;

        for( int i = 0; i < apps.size(); i++ )
        {
            ILibraryApp app = apps.get( i );
            int topGap = COMPONENT_GAP;
            int bottomGap = COMPONENT_GAP;
            JButton button = new JButton();

            button.setText( "Launch " + app.getName() );
            button.setIcon( app.getIcon() );
            button.setHorizontalAlignment( SwingConstants.LEFT );
            button.addActionListener( new AppButtonListener( app ) );

            if( i == 0 )
            {
                topGap *= 2;
            }
            else if( i == ( apps.size() - 1 ) )
            {
                bottomGap *= 2;
            }

            insets = new Insets( topGap, APPLICATION_GAP, bottomGap,
                APPLICATION_GAP );
            constraints = new GridBagConstraints( 0, i, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
                10 );
            panel.add( button, constraints );
        }
    }

    @Override
    public JPanel getView()
    {
        return panel;
    }
}
