package EDT.UI.screens;

import EDT.services.data.NAryTree;

public class EDTNode {
    private NAryTree.NodeType type;
    private String parentValue;
    private String value;
    private String metadata;

    public EDTNode(String type, String parentValue, String value, String metadata) {
        switch (type) {
            case "Entregable" -> this.type = NAryTree.NodeType.PACKAGE_NODE;
            case "Paquete" -> this.type = NAryTree.NodeType.DELIVERABLE_NODE;
        }

        this.parentValue = parentValue;
        this.value = value;
        this.metadata = metadata;
    }

    public NAryTree.NodeType getType() {
        return type;
    }

    public void setType(NAryTree.NodeType type) {
        this.type = type;
    }

    public String getParentValue() {
        return parentValue;
    }

    public void setParentValue(String parentValue) {
        this.parentValue = parentValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
