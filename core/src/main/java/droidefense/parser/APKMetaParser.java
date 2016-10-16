package droidefense.parser;

import droidefense.handler.FileIOHandler;
import droidefense.parser.base.AbstractFileParser;
import droidefense.sdk.helpers.InternalConstant;
import droidefense.sdk.model.base.AbstractHashedFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sergio on 21/2/16.
 */
public class APKMetaParser extends AbstractFileParser {

    @Override
    public void parserCode() {

        //get app files
        ArrayList<AbstractHashedFile> files = currentProject.getAppFiles();
        ArrayList<AbstractHashedFile> dexList = new ArrayList<>();

        ArrayList<AbstractHashedFile> assetFiles = new ArrayList<>();
        ArrayList<AbstractHashedFile> libFiles = new ArrayList<>();
        ArrayList<AbstractHashedFile> rawFiles = new ArrayList<>();

        //manifest file
        AbstractHashedFile manifest = null;
        AbstractHashedFile certFile = null;
        for (AbstractHashedFile r : files) {
            //count dex files and search manifest file
            if (r.getName().toLowerCase().endsWith(InternalConstant.DEX_EXTENSION)) {
                dexList.add(r);
            } else if (r.getName().toLowerCase().endsWith(InternalConstant.CERTIFICATE_EXTENSION)) {
                certFile = r;
            } else if (r.getName().equals(InternalConstant.ANDROID_MANIFEST) && manifest == null) {
                manifest = r;
            } else if (r.getAbsolutePath().contains(File.separator + "assets" + File.separator)) {
                assetFiles.add(r);
            } else if (r.getAbsolutePath().contains(File.separator + "lib" + File.separator)) {
                libFiles.add(r);
            } else if (r.getAbsolutePath().contains(File.separator + "res" + File.separator + "raw")) {
                rawFiles.add(r);
            }
        }

        //set dex files
        this.currentProject.setDexList(dexList);

        //set assets, raw and lib files
        this.currentProject.setRawFiles(rawFiles);
        this.currentProject.setAssetsFiles(assetFiles);
        this.currentProject.setLibFiles(libFiles);

        //set number of dex files
        this.currentProject.setNumberofDex(dexList.size());

        //set manifest file
        this.currentProject.setManifestFile(manifest);

        //set certificate file
        this.currentProject.setCertificateFile(certFile);

        //set currentProject folder name
        this.currentProject.setProjectFolderName(FileIOHandler.getUnpackOutputPath(getApk()));
    }
}
