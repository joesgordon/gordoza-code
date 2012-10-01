package org.jutils.apps;

import javax.swing.Icon;
import javax.swing.JFrame;

public interface ILibraryApp
{
    public Icon getIcon();

    public String getName();

    public JFrame runApp();
}
