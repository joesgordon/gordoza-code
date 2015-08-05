package org.jutils.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.Utils;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Represents a view that creates a dialog with an OK button at the bottom and
 * the content that the user provides above.
 ******************************************************************************/
public class OkDialogView implements IView<JDialog>
{
    /** The dialog to be shown. */
    private final JDialog dialog;
    /**  */
    private final JButton okButton;
    /**  */
    private final JButton cancelButton;
    /**  */
    private final JButton applyButton;
    /**
     * The listeners to be called when the dialog is closed by either the ok
     * button or the dialog's 'x' button.
     */
    private final ItemActionList<Boolean> okListeners;
    /**  */
    private final OkDialogButtons buttons;

    /***************************************************************************
     * Creates a {@link ModalityType#APPLICATION_MODAL} dialog with the provided
     * owner.
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     **************************************************************************/
    public OkDialogView( Window owner, Component content )
    {
        this( owner, content, ModalityType.DOCUMENT_MODAL );
    }

    /***************************************************************************
     * Creates a dialog with the provided {@link ModalityType} and owner.
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        ModalityType modalityType )
    {
        this( owner, content, modalityType, OkDialogButtons.OK_ONLY );
    }

    /***************************************************************************
     * @param owner
     * @param content
     * @param buttons
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        OkDialogButtons buttons )
    {
        this( owner, content, buttons.getModalityType(), buttons );
    }

    /***************************************************************************
     * Creates a {@link ModalityType#APPLICATION_MODAL} dialog whose owner is
     * the window containing the provided component.
     * @param parent a component in the window to be the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     **************************************************************************/
    public OkDialogView( Component parent, Component content )
    {
        this( parent, content, ModalityType.DOCUMENT_MODAL );
    }

    /***************************************************************************
     * Creates a dialog with the provided {@link ModalityType} whose owner is
     * the window containing the provided component.
     * @param parent a component in the window to be the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        ModalityType modalityType )
    {
        this( parent, content, modalityType, OkDialogButtons.OK_ONLY );
    }

    /***************************************************************************
     * @param parent
     * @param content
     * @param buttons
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        OkDialogButtons buttons )
    {
        this( parent, content, ModalityType.DOCUMENT_MODAL, buttons );
    }

    /***************************************************************************
     * @param parent
     * @param content
     * @param modalityType
     * @param buttons
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        ModalityType modalityType, OkDialogButtons buttons )
    {
        this( Utils.getComponentsWindow( parent ), content, modalityType,
            buttons );
    }

    /***************************************************************************
     * @param owner
     * @param content
     * @param modalityType
     * @param buttons
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        ModalityType modalityType, OkDialogButtons buttons )
    {
        this.okListeners = new ItemActionList<>();
        this.dialog = new JDialog( owner, modalityType );
        this.okButton = new JButton();
        this.cancelButton = new JButton();
        this.applyButton = new JButton();
        this.buttons = buttons;

        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );

        dialog.addWindowListener( new DialogListener() );

        dialog.setContentPane( createContentPane( content ) );

        SwingUtils.installEscapeCloseOperation( dialog );

        if( buttons.hasApply )
        {
            dialog.getRootPane().setDefaultButton( applyButton );
        }
        else
        {
            dialog.getRootPane().setDefaultButton( okButton );
        }

        if( owner != null )
        {
            dialog.setIconImages( owner.getIconImages() );
        }
    }

    public void setOkButtonText( String text )
    {
        okButton.setText( text );
    }

    public void setOkButtonIcon( Icon icon )
    {
        okButton.setIcon( icon );
    }

    public void setOkButtonText( String text, Icon icon )
    {
        okButton.setText( text );
        okButton.setIcon( icon );
    }

    /***************************************************************************
     * Adds the provided listener to the list of listeners to be called when the
     * dialog is closed. The boolean will be {@code true} if the ok button was
     * pressed and {@code false} if the cancel button was pressed or the dialog
     * is closed via the close button.
     * @param l the listener to be added.
     **************************************************************************/
    public void addOkListener( ItemActionListener<Boolean> l )
    {
        okListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JDialog getView()
    {
        return dialog;
    }

    /***************************************************************************
     * Creates the content pane for this dialog.
     * @param content the user content to be displayed.
     **************************************************************************/
    private Container createContentPane( Component content )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( content, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createButtonPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the button panel for this dialog.
     **************************************************************************/
    private Component createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        okButton.setText( "OK" );
        // okButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        okButton.addActionListener( new OkListener( this ) );

        cancelButton.setText( "Cancel" );
        // cancelButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        cancelButton.addActionListener( new CancelListener( this ) );

        applyButton.setText( "Apply" );
        // applyButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        applyButton.addActionListener( new ApplyListener( this ) );

        Utils.setMaxComponentSize( okButton, cancelButton, applyButton );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        if( buttons.hasOk )
        {
            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                    8, 8, 8, 8 ), 50, 5 );
            panel.add( okButton, constraints );
        }

        if( buttons.hasCancel )
        {
            constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                    8, 0, 8, 8 ), 50, 5 );
            panel.add( cancelButton, constraints );
        }

        if( buttons.hasApply )
        {
            constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                    8, 0, 8, 8 ), 50, 5 );
            panel.add( applyButton, constraints );
        }

        return panel;
    }

    /***************************************************************************
     * Listener added to the ok button to programmatically close the dialog and
     * call the close listeners.
     **************************************************************************/
    private static class OkListener implements ActionListener
    {
        private final OkDialogView view;

        public OkListener( OkDialogView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Utils.closeWindow( view.dialog );
            view.okListeners.fireListeners( view, true );
        }
    }

    private static class CancelListener implements ActionListener
    {
        private final OkDialogView view;

        public CancelListener( OkDialogView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Utils.closeWindow( view.dialog );
            view.okListeners.fireListeners( view, false );
        }
    }

    private static class ApplyListener implements ActionListener
    {
        private final OkDialogView view;

        public ApplyListener( OkDialogView view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            view.okListeners.fireListeners( view, true );
        }
    }

    /***************************************************************************
     * Added because <a
     * href="http://stackoverflow.com/questions/2873449">bug</a>
     **************************************************************************/
    private static class DialogListener extends WindowAdapter
    {
        public void windowClosed( WindowEvent e )
        {
            System.gc();
        }
    }

    public static enum OkDialogButtons
    {
        OK_ONLY( true, false, false ),
        OK_CANCEL( true, false, true ),
        OK_APPLY_CANCEL( true, true, true ),
        OK_APPLY( true, true, false );

        public final boolean hasOk;
        public final boolean hasApply;
        public final boolean hasCancel;

        private OkDialogButtons( boolean hasOk, boolean hasApply,
            boolean hasCancel )
        {
            this.hasOk = hasOk;
            this.hasApply = hasApply;
            this.hasCancel = hasCancel;
        }

        public ModalityType getModalityType()
        {
            return hasApply ? ModalityType.MODELESS
                : ModalityType.APPLICATION_MODAL;
        }
    }
}
