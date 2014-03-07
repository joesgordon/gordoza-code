package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardFrameView implements IView<JFrame>
{
    /**  */
    private final JFrame frame;
    /**  */
    private final ComponentView toolbarView;
    /**  */
    private final ComponentView contentView;

    /***************************************************************************
     * 
     **************************************************************************/
    public StandardFrameView()
    {
        this.frame = new JFrame();
        this.toolbarView = new ComponentView();
        this.contentView = new ComponentView();

        frame.setJMenuBar( createMenuBar() );
        frame.setContentPane( createContentPane() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createContentPane()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        StatusBarPanel statusbar = new StatusBarPanel();
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( toolbarView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( contentView.getView(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( statusbar.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JMenuBar createMenuBar()
    {
        JMenuBar menubar = new JGoodiesMenuBar();
        JMenu menu;

        menu = new JMenu( "File" );
        menubar.add( menu );

        menu.add( ExitListener.createStandardExitAction( frame ) );

        return menubar;
    }

    /***************************************************************************
     * @param toolbar
     **************************************************************************/
    public void setToolbar( JToolBar toolbar )
    {
        toolbarView.setComponent( toolbar );
    }

    /***************************************************************************
     * @param content
     **************************************************************************/
    public void setContent( Container content )
    {
        contentView.setComponent( content );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frame;
    }
}
