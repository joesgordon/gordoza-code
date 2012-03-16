package org.cc.edit.ui.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.*;

import org.cc.data.LockInfo;
import org.cc.edit.ui.InfoPanel;
import org.cc.edit.ui.panels.model.LockInfoPanel;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NullableLockInfoPanel extends InfoPanel<LockInfo>
{
    /**  */
    private JCheckBox lockCheckbox;
    /**  */
    private LockInfoPanel lockPanel;
    /**  */
    private ItemActionList<LockInfo> selectedListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public NullableLockInfoPanel()
    {
        super( new GridBagLayout() );

        selectedListeners = new ItemActionList<LockInfo>();

        lockCheckbox = new JCheckBox( "Locked" );
        lockPanel = new LockInfoPanel();

        lockCheckbox.addActionListener( new LockedActionListener() );
        lockPanel.setBorder( BorderFactory.createTitledBorder( "Lock Info" ) );

        add( lockCheckbox, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );

        add( lockPanel, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 10, 4, 1,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 4, 0, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addLockSelectedListener( ItemActionListener<LockInfo> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeLockSelectedListener( ItemActionListener<LockInfo> l )
    {
        selectedListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( LockInfo data )
    {
        boolean locked = data != null;

        lockCheckbox.setSelected( locked );

        lockPanel.setVisible( locked );
        if( locked )
        {
            lockPanel.setData( data );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class LockedActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            LockInfo info = null;
            lockPanel.setVisible( lockCheckbox.isSelected() );

            if( lockCheckbox.isSelected() && getData() == null )
            {
                if( getData() == null )
                {
                    info = new LockInfo();
                    GregorianCalendar cal = new GregorianCalendar();
                    info.setTime( ( long )( cal.getTimeInMillis() / 1000.0 ) );
                }
                lockPanel.setData( info );
            }

            selectedListeners.fireListeners( NullableLockInfoPanel.this, info );
        }
    }
}
