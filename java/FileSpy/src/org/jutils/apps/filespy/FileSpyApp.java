package org.jutils.apps.filespy;

import javax.swing.JFrame;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.apps.filespy.ui.FileSpyFrameView;
import org.jutils.io.OptionsSerializer;
import org.jutils.ui.app.IFrameApp;

public class FileSpyApp implements IFrameApp
{
    private final OptionsSerializer<FileSpyData> userio;

    public FileSpyApp( OptionsSerializer<FileSpyData> userio )
    {
        this.userio = userio;
    }

    @Override
    public JFrame createFrame()
    {
        FileSpyFrameView frameView = new FileSpyFrameView( userio );
        JFrame frame = frameView.getView();

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
