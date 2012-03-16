package org.cc.edit.ui.nodes.creators;

import javax.swing.tree.TreeNode;

import org.cc.edit.ui.InfoNode;

public interface NodeCreator<T>
{
    public InfoNode<T> createNode( TreeNode parent );
}
