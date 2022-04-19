package EDT.UI.screens.EDT;

import EDT.UI.Utils;
import EDT.services.data.ILinkedHelper;
import EDT.services.data.LinkedList;
import EDT.services.data.NAryTree;
import EDT.services.data.TreeNode;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;

class EDTController {
    private JFileChooser fileChooser;
    private NAryTree dataTree;

    EDTController(JFileChooser fileChooser, NAryTree dataTree) {
        this.fileChooser =  fileChooser;
        this.dataTree = dataTree;
    }

    public void generateFullReport() {
        fileChooser.setSelectedFile(new File(FileSystemView.getFileSystemView().getDefaultDirectory(), dataTree.getTitle() + "_report.txt"));
        while (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
        }

        StringBuilder data = new StringBuilder();
        data.append(String.format("Reporte para [%s]:\n\n", dataTree.getTitle()));

        data.append("1. Numero de elementos (segun tipo)\n");
        data.append(String.format("\t# Paquetes       = %d\n", dataTree.getTotalPackagesNode()));
        data.append(String.format("\t# Entregables    = %d\n", dataTree.getTotalDeliverableNode()));
        data.append(String.format("\t---------- TOTAL = %d\n\n", dataTree.getSize()));

        data.append("2. Profundidad maxima\n");
        data.append(String.format("\t# Profundidad       = %d\n\n", dataTree.depth()));

        // Calculates nodes with a single deliverable node
        LinkedList<TreeNode> nodes = new LinkedList<>();
        final int[] counter = {0};
        dataTree.forEachNode(new ILinkedHelper<TreeNode>() {
            @Override
            public void handle(TreeNode node) {
                counter[0] = 0;
                node.forEachChild(new ILinkedHelper<TreeNode>() {
                    @Override
                    public void handle(TreeNode child) {
                        if (NAryTree.isNotPackageInstance(child)) {
                            counter[0] += 1;
                        }
                    }
                });

                if (counter[0] == 1) {
                    nodes.insert(node);
                }
            }
        });


        data.append("3. Nodos con un solo entregable\n");
        int i = 0;
        for (TreeNode node : nodes) {
            data.append(String.format("\t%d. \"%s\" (hijo de \"%s\")\n", i++, node.getValue(), node.getParentValue()));
        }
        data.append(String.format("---Total---   %d elements\n\n", nodes.size()));

        try {
            FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile());
            fileWriter.write(data.toString());
            fileWriter.close();
        } catch (Exception e) {
            Utils.showMessage(e);
        }

    }
}
