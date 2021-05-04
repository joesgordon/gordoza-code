package org.jutils.gitit.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.Utils;
import org.jutils.core.ValidationException;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.core.io.xs.XsUtils;
import org.jutils.core.ui.JGoodiesToolBar;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.RecentFilesViews;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.event.FileChooserListener;
import org.jutils.core.ui.event.FileChooserListener.IFileSelected;
import org.jutils.core.ui.event.FileChooserListener.ILastFile;
import org.jutils.core.ui.model.IView;
import org.jutils.gitit.GititIcons;
import org.jutils.gitit.GititMain;
import org.jutils.gitit.data.GititConfig;
import org.jutils.gitit.data.GititOptions;

/*******************************************************************************
 *
 ******************************************************************************/
public class GititFrameView implements IView<JFrame>
{
    /**  */
    private final StandardFrameView view;
    /**  */
    private final RecentFilesViews recentFiles;
    /**  */
    private final GititView gititView;
    /**  */
    private final GititConfigView configView;

    /**  */
    private final OptionsSerializer<GititOptions> options;

    /***************************************************************************
     * 
     **************************************************************************/
    public GititFrameView()
    {
        this.view = new StandardFrameView();
        this.recentFiles = new RecentFilesViews();
        this.gititView = new GititView();
        this.configView = new GititConfigView();
        this.options = GititMain.getUserOptions();

        view.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        view.setTitle( "Gitit" );
        view.setSize( 500, 500 );
        view.setContent( gititView.getView() );
        view.setToolbar( createToolbar() );
        view.getView().setIconImages( GititIcons.getAppImages() );

        gititView.setData( options.getOptions().config );
    }

    /***************************************************************************
     * @return the toolbar for this application
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        recentFiles.install( toolbar, createOpenActionListener() );
        toolbar.addSeparator( new Dimension( 3, 1 ) );
        SwingUtils.addActionToToolbar( toolbar, createSaveAction() );
        toolbar.addSeparator();
        SwingUtils.addActionToToolbar( toolbar, createEditAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return the open action listener
     **************************************************************************/
    private ActionListener createOpenActionListener()
    {
        IFileSelected ifs = ( f ) -> openConfig( f );
        ILastFile ifl = () -> options.getOptions().getMostRecentFile();
        FileChooserListener listener = new FileChooserListener( getView(),
            "Open Configuration", false, ifs, ifl );

        listener.addExtension( "Gitit Config", ".gititcfg" );

        return listener;
    }

    /***************************************************************************
     * @return the save action
     **************************************************************************/
    private Action createSaveAction()
    {
        IFileSelected ifs = ( f ) -> saveConfig( f );
        ILastFile ifl = () -> options.getOptions().getMostRecentFile();
        FileChooserListener listener = new FileChooserListener( getView(),
            "Save Configuration", true, ifs, ifl );
        Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );

        listener.addExtension( "Gitit Config", ".gititcfg" );

        return new ActionAdapter( listener, "Save", icon );
    }

    /***************************************************************************
     * @return the edit action
     **************************************************************************/
    private Action createEditAction()
    {
        ActionListener listener = ( e ) -> showConfigDialog();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_16 );
        return new ActionAdapter( listener, "Edit", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void showConfigDialog()
    {
        configView.setData( gititView.getData() );

        OkDialogView okView = new OkDialogView( getView(), configView.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        okView.setTitle( "Gitit Configuration" );

        okView.show( new Dimension( 700, 700 ) );

        GititOptions op = options.getOptions();
        op.config = gititView.getData();
        options.write( op );

        gititView.setData( op.config );

        getView().pack();
    }

    /***************************************************************************
     * @param file the file to be opened.
     **************************************************************************/
    private void openConfig( File file )
    {
        try
        {
            GititConfig config = XsUtils.readObjectXStream( file,
                XsUtils.buildDependencyList( GititConfig.class ).toArray(
                    new String[] {} ) );

            gititView.setData( config );
        }
        catch( IOException | ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to read configuration from" + Utils.NEW_LINE +
                    file.getAbsolutePath(),
                "Read Error" );
        }
    }

    /***************************************************************************
     * @param file the file to be saved.
     **************************************************************************/
    private void saveConfig( File file )
    {
        try
        {
            XsUtils.writeObjectXStream( gititView.getData(), file,
                XsUtils.buildDependencyList( GititConfig.class ).toArray(
                    new String[] {} ) );
        }
        catch( IOException | ValidationException ex )
        {
            OptionUtils.showErrorMessage( getView(),
                "Unable to write configuration to" + Utils.NEW_LINE +
                    file.getAbsolutePath(),
                "Write Error" );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        return view.getView();
    }
}
