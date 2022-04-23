package EDT.UI.components.wrapper.fileChooser;

import java.io.File;

public interface IFileController {
    void onSuccess(File f);
    void onCancel();
}
