package org.jutils.chart.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.ui.model.IView;

public class OkDialog implements IView<JDialog>
{
    private final JDialog dialog;
    private final IView<? extends Component> view;

    public OkDialog( IView<? extends Component> view, JFrame frame )
    {
        this.dialog = new JDialog( frame );
        this.view = view;

        dialog.setContentPane( createContentPane() );
    }

    private Container createContentPane()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( view.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( createButtonPanel(), constraints );

        return panel;
    }

    private Component createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        JButton button;

        button = new JButton( "OK" );
        button.setFocusable( false );
        button.addActionListener( new OkListener( this ) );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 8,
                8, 8, 8 ), 40, 10 );
        panel.add( button, constraints );

        return panel;
    }

    @Override
    public JDialog getView()
    {
        return dialog;
    }

    private class OkListener implements ActionListener
    {
        private final OkDialog d;

        public OkListener( OkDialog d )
        {
            this.d = d;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            d.dialog.dispose();
        }
    }
}
