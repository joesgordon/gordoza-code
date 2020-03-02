package org.jutils.minesweeper.ui;

import javax.swing.JFrame;

import org.jutils.minesweeper.MsIcons;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final MsView gameView;

    /***************************************************************************
     * 
     **************************************************************************/
    public MsFrameView()
    {
        this.view = new StandardFrameView();
        this.gameView = new MsView();

        view.setTitle( "Minesweeper" );
        view.setContent( gameView.getView() );
        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setSize( 800, 800 );
        view.getView().setIconImages( MsIcons.getAppImages() );
        view.getView().setResizable( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
