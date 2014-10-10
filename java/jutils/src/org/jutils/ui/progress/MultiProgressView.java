package org.jutils.ui.progress;

import javax.swing.JList;
import javax.swing.JPanel;

import org.jutils.ui.model.IView;

public class MultiProgressView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ProgressView mainField;
    /**  */
    private final JList<TaskState> progressList;

    public MultiProgressView()
    {
        this.mainField = new ProgressView();
        this.progressList = new JList<TaskState>();
        this.view = createView();
    }

    private JPanel createView()
    {
        JPanel panel = new JPanel();
        // TODO Auto-generated method stub
        return panel;
    }

    @Override
    public JPanel getView()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
