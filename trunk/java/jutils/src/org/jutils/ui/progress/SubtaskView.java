package org.jutils.ui.progress;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.jutils.ui.model.IDataView;

public class SubtaskView implements IDataView<JPanel>
{
    public SubtaskView()
    {
        ;
    }

    @Override
    public JPanel getView()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JPanel getData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setData( JPanel data )
    {
        // TODO Auto-generated method stub

    }

    static class TaskState
    {
        public Icon icon;
        public String title;
        public int percent;
        public String message;
    }
}
