package org.jutils.apps.filespy;

import javax.swing.JFrame;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.apps.filespy.ui.FileSpyFrameView;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.app.IFrameApp;

public class FileSpyApp implements IFrameApp
{
    private final UserOptionsSerializer<FileSpyData> userio;

    public FileSpyApp( UserOptionsSerializer<FileSpyData> userio )
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
