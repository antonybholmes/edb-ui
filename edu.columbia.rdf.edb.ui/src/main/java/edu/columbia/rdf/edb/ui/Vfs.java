package edu.columbia.rdf.edb.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.abh.common.json.Json;
import org.abh.common.json.JsonParser;
import org.abh.common.network.UrlBuilder;
import org.abh.common.text.DateUtils;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.FileDescriptor;
import edu.columbia.rdf.edb.FileType;
import edu.columbia.rdf.edb.Tag;

public class Vfs {
	private EDBWLogin mLogin;

	public Vfs(EDBWLogin login) {
    	mLogin = login;
    }
	
	/**
	 * Return the VFS root.
	 * 
	 * @return
	 * @throws ParseException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws java.text.ParseException 
	 */
	public List<FileDescriptor> ls() throws ParseException, MalformedURLException, IOException, java.text.ParseException {
		return ls(-1);
	}
	
	public List<FileDescriptor> ls(int vfsId) throws ParseException, MalformedURLException, IOException {
		UrlBuilder urlFile = mLogin
				.getOTKAuthUrl()
				.resolve("vfs")
				.resolve("ls")
				.resolve(vfsId);
		
		System.err.println(urlFile);
		
		Json json = new JsonParser().parse(urlFile.toUrl());
		
		List<FileDescriptor> files = new ArrayList<FileDescriptor>();
		
		for (int i = 0; i < json.size(); ++i) {
			Json fileJson = json.get(i);
			
			FileDescriptor f = new FileDescriptor(fileJson.getAsInt("id"),
					fileJson.getAsString("n"),
					FileType.parse(fileJson.getAsInt("t")),
					DateUtils.parseRevFormattedDate(fileJson.getAsString("d")));
			
			files.add(f);
		}
		
		Collections.sort(files);
		
		return files;
	}
	
	public List<Tag> tags() throws MalformedURLException, IOException, ParseException {
		UrlBuilder urlFile = mLogin.getOTKAuthUrl().resolve("vfs").resolve("tags");
		
		System.err.println(urlFile);
		
		Json json = new JsonParser().parse(urlFile.toUrl());
		
		List<Tag> tags = new ArrayList<Tag>();
		
		for (int i = 0; i < json.size(); ++i) {
			Json tagJson = json.get(i);
			
			//System.err.println("vfs " + i + " " + json.get(i).getAsString("n"));
			
			Tag tag = new Tag(tagJson.getAsInt("id"), tagJson.getAsString("n"));
			
			tags.add(tag);
		}
		
		return tags;
	}
}
