package org.jutils.minesweeper.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import org.jutils.minesweeper.MsIcons;
import org.jutils.minesweeper.data.GridSpace;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GridSpaceView implements IView<JComponent>
{
    /**  */
    public final GridSpace space;

    /**  */
    private final JLabel label;
    /**  */
    private final FlagIcon flag;
    /**  */
    private final Icon mine;

    /***************************************************************************
     * @param space
     **************************************************************************/
    public GridSpaceView( GridSpace space )
    {
        this.space = space;
        this.label = new JLabel();
        this.flag = new FlagIcon( 22 );
        this.mine = MsIcons.getIcon( "bomb024.png" );

        label.setFont(
            label.getFont().deriveFont( 24.0f ).deriveFont( Font.BOLD ) );
        label.setHorizontalAlignment( SwingConstants.CENTER );
        label.setHorizontalTextPosition( SwingConstants.CENTER );
        // label.setAlignmentX( JLabel.CENTER_ALIGNMENT );

        label.setPreferredSize( new Dimension( 30, 30 ) );
        label.setMinimumSize( label.getPreferredSize() );
        label.setMaximumSize( label.getPreferredSize() );

        label.setOpaque( true );
        label.setBackground( MsView.FORM_BG );

        repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        String text = "";
        Color bdrClr = MsView.TILE_BG.darker();
        Color bg = label.getBackground();
        Icon icon = null;

        int bw = 2;

        int t = space.y == 0 ? bw : 0;
        int l = space.x == 0 ? bw : 0;
        int b = bw;
        int r = bw;

        switch( space.status )
        {
            case TILE:
                text = "";
                bg = MsView.TILE_BG;
                break;

            case FLAGGED:
                text = null;
                bg = MsView.TILE_BG;
                icon = flag;
                break;

            case REVEALED:
                text = space.numAdj == 0 ? "" : "" + space.numAdj;
                bg = MsView.GAME_BG;
                break;

            default:
                break;
        }

        if( space.isDetonated() )
        {
            text = "B";
            icon = mine;
            bg = Color.red;
        }

        MatteBorder border = new MatteBorder( t, l, b, r, bdrClr );

        label.setText( text );
        label.setBackground( bg );
        label.setIcon( icon );
        label.setBorder( border );

        label.invalidate();
        label.repaint();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return label;
    }
}
