package testbed;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.ScrollableEditorPaneView;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;

public class ReverserView implements IFrameApp
{
    public ReverserView()
    {
        ;
    }

    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JToolBar toolbar = new JGoodiesToolBar();
        JFrame frame = frameView.getView();
        JEditorPane textField = new ScrollableEditorPaneView().getView();
        JScrollPane textPane = new JScrollPane( textField );
        Action reverseAction = new ActionAdapter(
            new ReverseAction( textField ), "Reverse",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );

        frameView.setToolbar( toolbar );

        SwingUtils.addActionToToolbar( toolbar, reverseAction );

        frameView.setContent( textPane );

        frame.setSize( 500, 500 );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
        // TODO Auto-generated method stub
    }

    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new ReverserView() );
    }

    public class ReverseAction implements ActionListener
    {
        private final JEditorPane textField;

        public ReverseAction( JEditorPane textField )
        {
            this.textField = textField;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            String text = textField.getText();

            // LogUtils.printDebug( "here1:" + text );

            if( text.isEmpty() )
            {
                return;
            }

            List<String> lines = new ArrayList<>();

            Matcher m = Pattern.compile( "(?m)^.*$" ).matcher( text );

            while( m.find() )
            {
                lines.add( m.group() );
            }

            if( lines.size() < 2 )
            {
                return;
            }

            lines = new ArrayList<>( lines );

            // while( lines.re )

            Collections.reverse( lines );

            text = Utils.collectionToString( lines, Utils.NEW_LINE );

            // LogUtils.printDebug( "here2:" + text );

            textField.setText( text );
        }
    }
}
