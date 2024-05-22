package com.example;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.List;

public class SwingBackground extends JFrame implements DropTargetListener {
    JTextPane pane = new JTextPane();
    SwingBackground(){
        new DropTarget(pane, DnDConstants.ACTION_COPY_OR_MOVE, this);

    }


    @Override
    public void dragEnter(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {

    }

    @Override
    public void dragExit(DropTargetEvent dte) {

    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(dtde.getDropAction());
        Transferable fr = dtde.getTransferable();
        if(fr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
            try{
                List list = (List) fr.getTransferData(DataFlavor.javaFileListFlavor);
                System.out.println(list);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedFlavorException e) {
                throw new RuntimeException(e);
            }
            dtde.dropComplete(true);
        }

    }
}
