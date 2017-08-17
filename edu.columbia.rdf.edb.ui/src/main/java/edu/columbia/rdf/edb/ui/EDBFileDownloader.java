package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;

import org.jebtk.core.io.FileUtils;
import org.jebtk.core.network.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.FileDescriptor;

/**
 * Maintains a connection to a caArray server.
 *
 * @author Antony Holmes Holmes
 *
 */
public class EDBFileDownloader implements FileDownloader {
	private static final Logger LOG = 
			LoggerFactory.getLogger(EDBFileDownloader.class);

	private EDBWLogin mLogin;

	public EDBFileDownloader(EDBWLogin login) throws UnsupportedEncodingException {
    	mLogin = login;
		
		//serveFileUrl = 
    	//		new UrlBuilder(mLogin).addPath("download").addPath("file");
    	
    	//serveFilesUrl = 
    	//		new UrlBuilder(mLogin).addPath("download").addPath("zip");
    }

	/*
	@Override
	public void downloadFile(ExperimentDbFileDescriptor file,
			Path localFile) throws NetworkFileException {
		
		URL urlFile = null;
		
		try {
			System.err.println(file.getName());
			
			urlFile = new URL(serveFilesUrl + "?id=" + file.getId());
			
			System.err.println(urlFile);
			
			downloadFile(urlFile, localFile);
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
			
			throw new NetworkFileException(urlFile + " is badly formed.");
		}
	}
	*/

	public void downloadFile(UrlBuilder url, Path localFile) throws IOException {
		downloadFile(url.toUrl(), localFile);
	}
	
	public void downloadFile(URL urlFile, Path localFile) throws IOException {
		LOG.info("Download {} to {}", urlFile, localFile);
		
		OutputStream output = FileUtils.newOutputStream(localFile);
		
		InputStream input = urlFile.openStream();
			
		try {
			byte[] buffer = new byte[2048];
			
			int bytesRead;    
		    
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	@Override
	public void downloadFile(FileDescriptor file, 
			Path localFile)
			throws IOException {
		UrlBuilder urlFile = mLogin
				.getOTKAuthUrl()
				.resolve("download")
				.resolve("files")
				.resolve(file.getId());
		
		downloadFile(urlFile, localFile);
	}
	
	@Override
	public void downloadZip(Set<FileDescriptor> files, 
			Path localFile) throws IOException {
		UrlBuilder url = mLogin
				.getOTKAuthUrl()
				.resolve("download")
				.resolve("files")
				.resolve("zip");
		
		// Add the file ids we want to download
		for (FileDescriptor file : files) {
			url = url.param("id", file.getId());
		}

		downloadFile(url.toUrl(), localFile);
	}
}
