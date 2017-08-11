package edu.columbia.rdf.edb.ui;

import java.awt.Frame;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.abh.common.io.FileUtils;
import org.abh.common.io.PathUtils;
import org.abh.common.settings.SettingsService;
import org.abh.common.ui.dialog.MessageDialogType;
import org.abh.common.ui.dialog.ModernDialogStatus;
import org.abh.common.ui.dialog.ModernMessageDialog;
import org.abh.common.ui.io.RecentFilesService;
import org.abh.common.ui.io.ZipGuiFileFilter;
import org.abh.common.ui.window.ModernWindow;

import edu.columbia.rdf.edb.FileDescriptor;


public class DownloadManager {
	public static final String MAS5 = "mas5-annotated";
	public static final String RMA = "rma-annotated";

	public static Path downloadAsZip(ModernWindow parent, 
			Set<FileDescriptor> files) throws IOException {

		if (files.size() == 0) {
			ModernMessageDialog.createDialog(parent,
					parent.getAppInfo().getName(),
					"You must select at least one file to download.",
					MessageDialogType.WARNING);

			return null;
		}

		JFileChooser fc = new JFileChooser();

		FileFilter filter = new ZipGuiFileFilter();
		
		fc.addChoosableFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
		fc.setCurrentDirectory(RecentFilesService.getInstance().getPwd().toFile());
		
		fc.setFileFilter(filter);
		
		if (fc.showSaveDialog(parent) == JFileChooser.CANCEL_OPTION) {
        	return null;
        }

		Path output = PathUtils.addExtension(fc.getSelectedFile().toPath(), "zip");

		if (FileUtils.exists(output)) {
			ModernDialogStatus status = 
					ModernMessageDialog.createFileReplaceDialog(parent, output);

    		if (status == ModernDialogStatus.CANCEL) {
    			return downloadAsZip(parent, files);
    		}
    	}
		
		downloadAsZip(files, output);
		
		RecentFilesService.getInstance().add(output);
			
		ModernMessageDialog.createFileSavedDialog(parent, 
				parent.getAppInfo().getName(), 
				output);
			
		return output;
	}
	
	public static void downloadAsZip(Set<FileDescriptor> files, Path output) throws IOException {	
		Repository connection = 
				RepositoryService.getInstance().getRepository(RepositoryService.DEFAULT_REP);
		
		FileDownloader downloader = connection.getFileDownloader();
		
		downloader.downloadZip(files, output);
	}
	
	public static Path chooseDownloadDirectory(Frame parent) throws IOException {

		Path downloadDirectory = PathUtils.getPath(SettingsService.getInstance().getAsString("downloads/directory"));
		
		// Make sure the directory exists
		FileUtils.mkdir(downloadDirectory);

		JFileChooser fc = new JFileChooser();

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setCurrentDirectory(downloadDirectory.toFile());
		
		if (fc.showDialog(parent, "Choose download directory") == JFileChooser.CANCEL_OPTION) {
        	return null;
        }

		return fc.getSelectedFile().toPath();
	}
	
	public static void download(FileDescriptor file, Path output) throws IOException {	
		Repository connection = 
				RepositoryService.getInstance().getRepository(RepositoryService.DEFAULT_REP);
		
		FileDownloader downloader = connection.getFileDownloader();
		
		downloader.downloadFile(file, output);
	}
}
