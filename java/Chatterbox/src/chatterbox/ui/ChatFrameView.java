package chatterbox.ui;

import java.awt.event.*;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.IView;

import chatterbox.messenger.Chat;
import chatterbox.view.IChatView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatFrameView implements IView<JFrame>
{
    private final ChatView chatView;
    private final StandardFrameView frameView;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatFrameView()
    {
        this.frameView = new StandardFrameView();
        this.chatView = new ChatView();

        frameView.setContent( chatView.getView() );
        frameView.setToolbar( createToolbar() );

        JFrame frame = getView();

        frame.addWindowListener( new FrameListener( this ) );

        frame.setIconImages( IconConstants.loader.getImages(
            IconConstants.CHAT_16, IconConstants.CHAT_32,
            IconConstants.CHAT_48, IconConstants.CHAT_64 ) );

        frame.setTitle( "Chatterbox" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        Action action;
        Icon icon;

        icon = IconConstants.loader.getIcon( IconConstants.CLOCK_24 );
        action = new ActionAdapter( new HistoryListener( this ),
            "View Chat History", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        icon = IconConstants.loader.getIcon( IconConstants.CONFIG_24 );
        action = new ActionAdapter( new ConfigListener( this ),
            "Edit Configuration", icon );
        SwingUtils.addActionToToolbar( toolbar, action );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IChatView getChatView()
    {
        return chatView;
    }

    /***************************************************************************
     * @param chat
     **************************************************************************/
    public void setChat( Chat chat )
    {
        chatView.setChat( chat );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FrameListener extends WindowAdapter
    {
        private final ChatFrameView view;

        public FrameListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            view.chatView.getChat().disconnect();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HistoryListener implements ActionListener
    {
        private final ChatFrameView view;

        public HistoryListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JOptionPane.showMessageDialog( view.getView(),
                "This functionality is not yet supported. Good try, though.",
                "Not Supported", JOptionPane.ERROR_MESSAGE );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ConfigListener implements ActionListener
    {
        private final ChatFrameView view;

        public ConfigListener( ChatFrameView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            new ConfigDialog( view.getView(), view.chatView.getChat() );
        }
    }
}
