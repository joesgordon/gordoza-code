package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.model.ItemComboBoxModel;

import testbed.BusyBar.BarColor;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BusyBarApp implements IFrameApp
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new BusyBarApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel( new GridBagLayout() );
        ItemComboBoxModel<BarColor> model = new ItemComboBoxModel<>();
        JComboBox<BarColor> combo = new JComboBox<BarColor>( model );
        model.addAll( Arrays.asList( BarColor.values() ) );

        combo.setSelectedItem( BarColor.GREEN );

        BusyBar bar = new BusyBar( 30, BarColor.GREEN );
        JComponent comp = bar.getView();
        comp.setBorder( BorderFactory.createRaisedBevelBorder() );
        bar.setBusy( true );

        combo.addActionListener( new ColorSelectedListener( bar, model ) );

        panel.add( new JLabel( "Color :" ),
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
        panel.add( combo,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        panel.add( comp,
            new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 4, 4, 4 ), 0, 10 ) );

        frame.setContentPane( panel );
        frame.setSize( 400, 200 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ColorSelectedListener implements ActionListener
    {
        private final BusyBar bar;
        private final ItemComboBoxModel<BarColor> model;

        public ColorSelectedListener( BusyBar bar,
            ItemComboBoxModel<BarColor> model )
        {
            this.bar = bar;
            this.model = model;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            BarColor c = model.getSelectedItem();
            bar.setColor( c );
        }
    }
}
