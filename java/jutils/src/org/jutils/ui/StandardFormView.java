package org.jutils.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StandardFormView implements IView<JPanel>
{
    /**  */
    private final GridBagLayout layout;
    /**  */
    private final JPanel view;
    /**  */
    private final int formMargin;
    /**  */
    private final int fieldMargin;
    /**  */
    private final Component spacer;
    /**  */
    private final List<FieldInfo> fields;

    /**  */
    private int fieldFill;

    /***************************************************************************
     * 
     **************************************************************************/
    public StandardFormView()
    {
        this( 4 );
    }

    /***************************************************************************
     * @param formMargin
     **************************************************************************/
    public StandardFormView( int fieldMargin )
    {
        this( fieldMargin, fieldMargin * 2 );
    }

    /***************************************************************************
     * @param fieldMargin
     * @param formMargin
     **************************************************************************/
    public StandardFormView( int fieldMargin, int formMargin )
    {
        this.formMargin = formMargin;
        this.fieldMargin = fieldMargin;

        this.fields = new ArrayList<FieldInfo>();
        this.layout = new GridBagLayout();
        this.view = new JPanel( layout );

        this.fieldFill = GridBagConstraints.NONE;

        GridBagConstraints constraints;
        spacer = Box.createHorizontalStrut( 2 );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                formMargin, 0, 0, 0 ), 0, 0 );

        view.add( spacer, constraints );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param field
     **************************************************************************/
    public void addField( IFormField field )
    {
        addField( field, fields.size() );
    }

    /***************************************************************************
     * @param field
     * @param index
     **************************************************************************/
    public void addField( IFormField field, int index )
    {
        GridBagConstraints constraints;
        int top = formMargin;

        JLabel label = null;

        if( field.getFieldName() != null )
        {
            label = new JLabel( field.getFieldName() + ":" );

            constraints = new GridBagConstraints( 0, index * 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                    formMargin, formMargin, 0, formMargin ), 0, 0 );
            view.add( label, constraints );
            top = fieldMargin;
        }

        fields.add( new FieldInfo( label, field ) );

        constraints = new GridBagConstraints( 0, index * 2 + 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, fieldFill, new Insets( top, formMargin, 0,
                formMargin ), 0, 0 );
        view.add( field.getField(), constraints );

        refreshForm( index + 1 );
    }

    /***************************************************************************
     * @param field
     **************************************************************************/
    public void removeField( IFormField field )
    {
        int formIndex = indexOfField( field );

        if( formIndex > -1 )
        {
            removeField( formIndex );
        }
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public void removeField( int index )
    {
        FieldInfo info = fields.remove( index );

        if( info.label != null )
        {
            view.remove( info.label );
        }
        view.remove( info.field.getField() );

        refreshForm( index );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getFieldCount()
    {
        return fields.size();
    }

    /***************************************************************************
     * @param c
     * @param visible
     **************************************************************************/
    public void setVisible( Component c, boolean visible )
    {
        int index = indexOfField( c );
        FieldInfo info = fields.get( index );

        info.field.getField().setVisible( visible );
        if( info.label != null )
        {
            info.label.setVisible( visible );
        }
    }

    /***************************************************************************
     * @param stretch
     **************************************************************************/
    public void setHorizontalStretch( boolean stretch )
    {
        int newFill = GridBagConstraints.NONE;

        if( stretch )
        {
            newFill = GridBagConstraints.HORIZONTAL;
        }

        fieldFill = newFill;
    }

    /***************************************************************************
     * @param field
     * @return
     **************************************************************************/
    private int indexOfField( IFormField field )
    {
        for( int i = 0; i < fields.size(); i++ )
        {
            if( fields.get( i ).field == field )
            {
                return i;
            }
        }

        return -1;
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    private int indexOfField( Component c )
    {
        for( int i = 0; i < fields.size(); i++ )
        {
            if( fields.get( i ).field.getField() == c )
            {
                return i;
            }
        }

        return -1;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private void refreshForm( int index )
    {
        GridBagConstraints constraints;
        FieldInfo info;

        for( int i = index; i < fields.size(); i++ )
        {
            info = fields.get( i );

            if( info.field.getFieldName() != null )
            {
                constraints = layout.getConstraints( info.label );
                constraints.gridy = i * 2;
                layout.setConstraints( info.label, constraints );
            }

            constraints = layout.getConstraints( info.field.getField() );
            constraints.gridy = i * 2 + 1;
            layout.setConstraints( info.field.getField(), constraints );
        }

        constraints = layout.getConstraints( spacer );
        constraints.gridy = fields.size() * 2;
        layout.setConstraints( spacer, constraints );

        view.revalidate();
        view.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FieldInfo
    {
        public final JLabel label;
        public final IFormField field;

        public FieldInfo( JLabel label, IFormField field )
        {
            this.label = label;
            this.field = field;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IFormField
    {
        public String getFieldName();

        public Component getField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DefaultFormField implements IFormField
    {
        public String name;
        public Component comp;

        public DefaultFormField( String name, Component comp )
        {
            this.name = name;
            this.comp = comp;
        }

        @Override
        public String getFieldName()
        {
            return name;
        }

        @Override
        public Component getField()
        {
            return comp;
        }
    }
}
