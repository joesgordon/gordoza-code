package org.jutils.ui.fields;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.io.parsers.ExistenceType;
import org.jutils.ui.*;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.model.IView;
import org.jutils.utils.BitArray;
import org.jutils.utils.Usable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FormFieldsTest
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new IntegerFormFieldTestApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class IntegerFormFieldTestApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            UiTestFrameView frameView = new UiTestFrameView();

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ViewItem
    {
        public final String name;
        public final IView<?> view;

        public ViewItem( String name, IView<?> view )
        {
            this.name = name;
            this.view = view;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UiTestFrameView implements IView<JFrame>
    {
        private final StandardFrameView frameView;
        private final DefaultListModel<ViewItem> itemsModel;
        private final JList<ViewItem> itemsList;
        private final ComponentView cview;

        public UiTestFrameView()
        {
            this.frameView = new StandardFrameView();
            this.itemsModel = new DefaultListModel<>();
            this.itemsList = new JList<>( itemsModel );
            this.cview = new ComponentView();

            frameView.setContent( createContent() );
            frameView.setTitle( "Integer Form Field Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 600, 500 );

            createViews( itemsModel );

            itemsList.addListSelectionListener(
                new ItemSelectedListener( itemsList, cview ) );
        }

        private Container createContent()
        {
            JPanel panel = new JPanel( new GridBagLayout() );
            GridBagConstraints constraints;

            JScrollPane pane = new JScrollPane( itemsList );

            Dimension size = pane.getPreferredSize();
            size.width = 200;

            pane.setMinimumSize( size );
            pane.setPreferredSize( size );

            constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 );
            panel.add( pane, constraints );

            constraints = new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 4, 0, 4, 4 ), 0, 0 );
            panel.add( cview.getView(), constraints );

            return panel;
        }

        private static void createViews( DefaultListModel<ViewItem> itemsModel )
        {
            itemsModel.addElement( createFormFieldItem(
                new BitsField( "Bits Field" ), new BitArray( "FAF320" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new BooleanFormField( "Boolean Form Field" ), true ) );
            itemsModel.addElement( createFormFieldItem( new ButtonedFormField<>(
                new IntegerFormField( "Integer Form Field" ) ), 8 ) );
            itemsModel.addElement( createFormFieldItem(
                new ComboFormField<>( "Combo Form Field",
                    Character.UnicodeScript.values() ),
                Character.UnicodeScript.JAVANESE ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (File/Save)",
                    ExistenceType.DO_NOT_CHECK ),
                new File( "" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new FileFormField( "File Form Field (Dir/Save)",
                    ExistenceType.DIRECTORY_ONLY ),
                new File( "" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new HexByteFormField( "Hex Byte Form Field" ), ( byte )0x81 ) );
            itemsModel.addElement( createFormFieldItem(
                new HexBytesFormField( "Hex Byte Form Field" ),
                HexUtils.fromHexStringToArray( "FE6B2840" ) ) );
            itemsModel.addElement( createFormFieldItem(
                new HexIntFormField( "Hex Int Form Field" ), 1729 ) );
            itemsModel.addElement( createFormFieldItem(
                new HexLongFormField( "Hex Long Form Field" ),
                0xBA5E1E55DEADBEEFL ) );
            itemsModel.addElement( createFormFieldItem(
                new IntegerFormField( "Integer Form Field" ), 6 ) );
            itemsModel.addElement(
                createFormFieldItem( new LongFormField( "Long Form Field" ),
                    60L * 186282L * 60L * 24L ) );
            itemsModel.addElement( createFormFieldItem(
                new ShortFormField( "Short Form Field" ), ( short )1992 ) );
            itemsModel.addElement(
                createFormFieldItem( new StringFormField( "String Form Field" ),
                    "Flibbidy Gibblets" ) );
            itemsModel.addElement( createFormFieldItem(
                new UsableFormField<>(
                    new StringFormField( "Usable Form Field" ) ),
                new Usable<>( true, "Gibblidy Flibbets" ) ) );
        }

        private static <D> ViewItem createFormFieldItem(
            IDataFormField<D> field, D data )
        {
            StandardFormView form = new StandardFormView();

            field.setValue( data );

            // field.setUpdater( new ReflectiveUpdater<>( this, "testValue" ) );

            form.addField( field );

            return new ViewItem( field.getName(), form );
        }

        @Override
        public JFrame getView()
        {
            return frameView.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ItemSelectedListener
        implements ListSelectionListener
    {
        private final JList<ViewItem> list;
        private final ComponentView view;

        public ItemSelectedListener( JList<ViewItem> list, ComponentView view )
        {
            this.list = list;
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            ViewItem item = list.getSelectedValue();

            if( item != null )
            {
                view.setComponent( item.view.getView() );
            }
            else
            {
                view.setComponent( new JPanel() );
            }
        }
    }
}
