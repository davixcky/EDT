package EDT.UI.components.wrapper.fileChooser;

import javax.swing.*;

public class UIFileChooser {

    private JFileChooser fileChooser;

    public UIFileChooser() {
        init();
    }

    private void init() {
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new FileTypeFilter(".nortegraph", "Uninorte graph file"));
    }

    public void launchDialog(IFileController handler) {
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.CANCEL_OPTION) {
            handler.onCancel();
            return;
        }

        handler.onSuccess(fileChooser.getSelectedFile());
    }
}
