package org.jutils.appgallery;

import javax.swing.Icon;
import javax.swing.JFrame;

public interface ILibraryApp
{
    public Icon getIcon();

    public String getName();

    public JFrame runApp();
}
