package org.jutils.ui;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import org.jutils.ui.model.IView;

public class IconTextField implements IView<JPanel>
{
    /**  */
    private final JPanel panel;
    /**  */
    private final JLabel iconLabel;
    /**  */
    private final JTextComponent textField;

    public IconTextField( JTextComponent textField )
    {
        this.iconLabel = new JLabel();
        this.textField = textField;

        this.panel = createView();

        textField.addPropertyChangeListener( ( e ) -> updateBackground() );
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( iconLabel, BorderLayout.WEST );
        panel.add( textField, BorderLayout.CENTER );

        panel.setBorder( textField.getBorder() );
        panel.setOpaque( textField.isOpaque() );
        panel.setBackground( textField.getBackground() );

        Insets i = textField.getMargin();

        iconLabel.setBorder( new EmptyBorder( i.top, i.left, i.bottom, 0 ) );
        textField.setBorder( new EmptyBorder( i.top, i.left, i.bottom, 0 ) );

        return panel;
    }

    private void updateBackground()
    {
        panel.setBackground( textField.getBackground() );
    }

    public void setIcon( Icon icon )
    {
        iconLabel.setIcon( icon );
    }

    @Override
    public JPanel getView()
    {
        return panel;
    }

}
