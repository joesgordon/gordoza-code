package org.eglsht;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JFrame;

import org.jutils.core.ui.IToolView;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 *
 ******************************************************************************/
public class SheetTool implements IToolView
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Icon getIcon24()
    {
        return EagleSheetIcons.getApp24();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "EagleSheet";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame getView()
    {
        IFrameApp app = new EagleSheetApp();

        return app.createFrame();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getDescription()
    {
        return "Spreadsheet";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Image> getImages()
    {
        return EagleSheetIcons.getApplicationImages();
    }
}
