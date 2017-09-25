package org.jutils.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.ListView.IItemListModel;
import org.jutils.ui.ListView.ItemListCellRenderer;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;
import org.jutils.ui.fields.StringFormField;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.IView;

/***************************************************************************
 * 
 **************************************************************************/
public class ItemListViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new ItemListViewApp() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ItemListViewApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();
            AppView view = new AppView();

            frameView.setContent( view.getView() );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 500, 500 );
            frameView.setTitle( "Item List View Test App" );

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
    private static final class AppView implements IView<JComponent>
    {
        private final JPanel view;
        private final ItemListView<TestData> normalList;
        private final ItemListView<TestData> rendererList;

        public AppView()
        {
            TestDataView ntdv = new TestDataView();
            TestDataView rtdv = new TestDataView();

            this.normalList = new ItemListView<TestData>( ntdv,
                new TestDataModel() );
            this.rendererList = new ItemListView<TestData>( rtdv,
                new TestDataModel() );
            this.view = createView();

            ntdv.addNameUpdater( ( d ) -> normalList.refreshSelected() );
            rtdv.addNameUpdater( ( d ) -> rendererList.refreshSelected() );

            rendererList.setItemRenderer( new TestDataRenderer() );
        }

        private JPanel createView()
        {
            JPanel panel = new JPanel( new BorderLayout() );
            JTabbedPane tabs = new JTabbedPane();

            tabs.add( "Item List", normalList.getView() );
            tabs.add( "Rendered List", rendererList.getView() );

            panel.add( tabs, BorderLayout.CENTER );

            return panel;
        }

        @Override
        public JComponent getView()
        {
            return view;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataView implements IDataView<TestData>
    {
        private final JPanel view;
        private final StringFormField nameField;
        private final UpdaterList<String> nameUpdaters;

        private TestData data;

        public TestDataView()
        {
            this.nameField = new StringFormField( "Name" );
            this.view = createView();
            this.nameUpdaters = new UpdaterList<>();

            nameField.setUpdater( ( d ) -> {
                data.name = d;
                nameUpdaters.fireListeners( d );
            } );
        }

        private JPanel createView()
        {
            StandardFormView form = new StandardFormView();

            form.addField( nameField );

            return form.getView();
        }

        public void addNameUpdater( IUpdater<String> u )
        {
            nameUpdaters.addUpdater( u );
        }

        @Override
        public Component getView()
        {
            return view;
        }

        @Override
        public TestData getData()
        {
            return data;
        }

        @Override
        public void setData( TestData data )
        {
            this.data = data;

            nameField.setValue( data.name );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataModel implements IItemListModel<TestData>
    {
        @Override
        public String getTitle( TestData item )
        {
            return item.name;
        }

        @Override
        public TestData promptForNew( ListView<TestData> view )
        {
            String name = view.promptForName( "Test Data" );

            if( name != null )
            {
                TestData data = new TestData();
                data.name = name;
                return data;
            }

            return null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestDataRenderer
        implements ItemListCellRenderer<TestData>
    {
        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        private final ColorIcon icon = new ColorIcon( Color.green );

        @Override
        public Component getListCellRendererComponent( JList<?> list,
            TestData value, int index, boolean isSelected, boolean cellHasFocus,
            String text )
        {
            Component c = renderer.getListCellRendererComponent( list, value,
                index, isSelected, cellHasFocus );

            icon.setColor( new Color( value.name.hashCode() ) );
            renderer.setIcon( icon );
            renderer.setText( value.name );

            return c;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestData
    {
        public String name;
    }
}