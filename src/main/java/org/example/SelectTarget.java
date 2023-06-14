package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectTarget {
    private String path;
    private String file;
    private FileHandler fileHandler;
    private File setting;
    private Map<String,String> mapTargets = new HashMap<>();
    private Map<String,String> mapFilter = new HashMap<>();

    public SelectTarget(String path, String file) {
        this.path = path;
        this.file = file;
        setting = new File(this.path,this.file);
    }
    public void fullTargetFromSetting() {
        Logger logger = Logger.getLogger(SelectTarget.class.getName());
        logger.setLevel(Level.INFO);
        logger.addHandler(this.fileHandler);
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.setting);
            Node root = document.getDocumentElement();
            logger.info("root: " + root.getNodeName());
            NodeList catalogs = root.getChildNodes();
            for (int i = 0; i < catalogs.getLength(); i++) {
                Node catalog = catalogs.item(i);
                logger.info("catalog: " + catalog.getNodeName());
                if(catalog.getNodeType() != Node.TEXT_NODE){
                    if(catalog.getNodeName().equals("targets")){
                        NodeList targets = catalog.getChildNodes();
                        logger.info("targets: " + targets.toString());
                        for (int j = 0; j < targets.getLength(); j++) {
                            Node targetCurrent = targets.item(j);
                            if(targetCurrent.getNodeType() != Node.TEXT_NODE){
                                this.mapTargets.put(targetCurrent.getAttributes().item(0).getTextContent().toString(),
                                        targetCurrent.getChildNodes().item(0).getTextContent().toString());
//                                this.mapFilter.put(targetCurrent.getAttributes().item(0).getTextContent().toString(),
//                                        targetCurrent.getAttributes().item(0).getTextContent().toString());
                            }
                        }
                    }
                }
            }
        } catch (SAXException e) {
            throw new RuntimeException();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        public Map<String, String> getMapTargets() {
        return mapTargets;
    }

    public Map<String, String> getMapFilter() {
        return mapFilter;
    }

    public void setMapFilter(Map<String, String> mapFilter) {
        this.mapFilter = mapFilter;
    }

    public void setMapTargets(Map<String, String> mapTargets) {
        this.mapTargets = mapTargets;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public File getSetting() {
        return setting;
    }

    public void setSetting(File setting) {
        this.setting = setting;
    }
}
