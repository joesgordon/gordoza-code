package org.jutils.ui.fields;

import java.awt.Container;

import javax.swing.JFrame;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.updater.ReflectiveUpdater;

public class IntegerFormFieldTest
{

    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new IntegerFormFieldTestApp() );
    }

    private static final class IntegerFormFieldTestApp implements IFrameApp
    {
        private IntegerFormField field;
        private int testValue;

        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            frameView.setContent( createContent() );
            frameView.setTitle( "Integer Form Field Test" );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setSize( 400, 400 );

            return frameView.getView();
        }

        private Container createContent()
        {
            StandardFormView form = new StandardFormView();
            field = new IntegerFormField( "Test Field" );

            field.setUpdater( new ReflectiveUpdater<>( this, "testValue" ) );

            form.addField( field );

            return form.getView();
        }

        @Override
        public void finalizeGui()
        {
            this.testValue = 6;

            field.setValue( testValue );
        }
    }
}
