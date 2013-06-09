package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.model.ItemComboBoxModel;

public class BarRunner extends FrameRunner
{
    private BusyBar2 bar;
    private ItemComboBoxModel<BusyBar2.BarColor> model;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new BarRunner() );
    }

    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel( new GridBagLayout() );
        model = new ItemComboBoxModel<BusyBar2.BarColor>();
        JComboBox combo = new JComboBox( model );
        model.addAll( Arrays.asList( BusyBar2.BarColor.values() ) );

        combo.setSelectedItem( BusyBar2.BarColor.GREEN );
        combo.addActionListener( new ColorSelectedListener() );

        bar = new BusyBar2( 30, BusyBar2.BarColor.GREEN );
        bar.setBorder( BorderFactory.createRaisedBevelBorder() );
        bar.setBusy( true );

        panel.add( new JLabel( "Color :" ), new GridBagConstraints( 0, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( combo, new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        panel.add( bar, new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 10 ) );

        frame.setContentPane( panel );
        frame.setSize( 400, 200 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

    private class ColorSelectedListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            BusyBar2.BarColor c = model.getSelectedItem();
            bar.setColors( c );
        }
    }
}