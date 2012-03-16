package org.jutils.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class SliderPanel extends JPanel
{
    private GridLayout gl;
    private List<JComponent> panels;
    private JScrollPane scrollPane;
    private MultiPanel scrollPanel;
    private Timer swingTimer;
    private PanelMover mover;
    private int currentPanel;
    private JViewport viewport;

    public SliderPanel()
    {
        super( new GridBagLayout() );

        createScrollPane();
        super.add( scrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );

        super.addComponentListener( new ResizeListener() );

        currentPanel = 0;

        mover = new PanelMover();
        swingTimer = new Timer( 25, mover );

        panels = new ArrayList<JComponent>();

        swingTimer.setInitialDelay( 0 );
        swingTimer.setRepeats( true );
    }

    private void createScrollPane()
    {
        gl = new GridLayout( 1, 0 );
        scrollPanel = new MultiPanel( gl );
        scrollPane = new JScrollPane( scrollPanel );
        viewport = scrollPane.getViewport();

        scrollPane.setBorder( BorderFactory.createEmptyBorder() );
        scrollPane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
        scrollPane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
    }

    public void addPanel( JComponent comp )
    {
        panels.add( comp );

        gl.setColumns( panels.size() );

        scrollPanel.add( comp );
    }

    @Override
    public void setSize( Dimension dim )
    {
        super.setPreferredSize( dim );

        System.out.println( "Preferred size set to " + dim.toString() );

        Dimension spDim = new Dimension( dim );
        spDim.width *= panels.size();
        scrollPanel.setPreferredSize( spDim );
        scrollPanel.setMinimumSize( spDim );
    }

    public void removePanel( JComponent comp )
    {
        int index = panels.indexOf( comp );

        if( index > -1 )
        {
            panels.remove( index );

            gl.setColumns( panels.size() );

            scrollPanel.remove( comp );
        }
        else
        {
            throw new IllegalArgumentException( "Component not found" );
        }
    }

    public void setPanel( JComponent comp )
    {
        int index = panels.indexOf( comp );
        setPanel( index );
    }

    public void setPanel( int index )
    {
        if( index > -1 )
        {
            swingTimer.stop();
            mover.setDirectionForward( index > currentPanel );
            currentPanel = index;
            swingTimer.start();
        }
        else
        {
            throw new ArrayIndexOutOfBoundsException( "Index out of bounds" );
        }
    }

    public int getCurrentPanel()
    {
        return currentPanel;
    }

    public boolean canMoveForward()
    {
        return currentPanel + 1 < panels.size();
    }

    public void next()
    {
        if( !canMoveForward() )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Cannot move forward past end" );
        }

        setPanel( currentPanel + 1 );
        // swingTimer.stop();
        // currentPanel++;
        // mover.setDirectionForward( true );
        // swingTimer.start();
    }

    public boolean canMoveBackward()
    {
        return currentPanel > 0;
    }

    public void last()
    {
        if( !canMoveBackward() )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Cannot move backward past beginning" );
        }

        setPanel( currentPanel - 1 );
        // swingTimer.stop();
        // currentPanel--;
        // mover.setDirectionForward( false );
        // swingTimer.start();
    }

    private class ResizeListener extends ComponentAdapter
    {
        public void componentResized( ComponentEvent e )
        {
            if( !panels.isEmpty() )
            {
                Point position = viewport.getViewPosition();
                position.x = currentPanel *
                    panels.get( currentPanel ).getWidth();
                viewport.setViewPosition( position );
                scrollPanel.repaint();
            }
        }
    }

    private class MultiPanel extends JPanel
    {
        public MultiPanel( LayoutManager layout )
        {
            super( layout );
        }

        @Override
        public Dimension getPreferredSize()
        {
            Dimension dim = super.getPreferredSize();
            dim.width = SliderPanel.this.getWidth() * panels.size();

            return dim;
        }
    }

    private class PanelMover implements ActionListener
    {
        private int direction;

        public PanelMover()
        {
            direction = 1;
        }

        public void setDirectionForward( boolean b )
        {
            direction = b ? 1 : -1;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Point position = viewport.getViewPosition();

            int nextPos = currentPanel * SliderPanel.this.getWidth();
            int delta = ( int )( direction * SliderPanel.this.getWidth() / 45.0 );

            position.x += delta;

            if( direction > 0 && position.x > nextPos )
            {
                swingTimer.stop();
                position.x = nextPos;
            }
            else if( direction < 0 && nextPos > position.x )
            {
                swingTimer.stop();
                position.x = nextPos;
            }

            viewport.setViewPosition( position );
        }
    }
}
