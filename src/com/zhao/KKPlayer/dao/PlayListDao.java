package com.zhao.KKPlayer.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import com.zhao.KKPlayer.model.PlayList;
import com.zhao.KKPlayer.util.XmlUtil;

/**
 * 用于访问播放列表的数据访问类
 * 
 * @author Administrator
 * 
 */
public class PlayListDao {

	private Document doc;
	private List<PlayList> playLists = new ArrayList<PlayList>();
	
	public Document getDoc() {
		if (doc == null) {
			try {
				doc = XmlUtil.loadXml("file/PlayList.xml", PlayListDao.class
						.getResource("/xsd/PlayList.xsd"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public List<PlayList> getPlayLists() {
		if (playLists.size() == 0) {
			try {
				XPath xPath = XPath.newInstance("//PlayList");
				List<?> list = xPath.selectNodes(getDoc()
						.getRootElement());
				for (Iterator<?> i = list.iterator(); i.hasNext();) {
					Element e = (Element) i.next();
					PlayList playList = new PlayList();
					playList.setId(Integer.parseInt(e.getAttributeValue("id")));
					playList.setMusicListPath(e
							.getAttributeValue("musicListPath"));
					playList.setName(e.getAttributeValue("name"));
					playLists.add(playList);
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}
		return playLists;
	}

	/**
	 * 添加播放列表
	 * 
	 * @param name
	 *            添加的列表的名字
	 * @param musicListPath
	 *            文件存放位置的相对路径
	 * @return 添加的PlayList实例
	 */
	public PlayList addPlayList(String name, String musicListPath) {
		Element parent = getDoc().getRootElement();
		Element child = new Element("PlayList");
		try {
			child.setAttribute("id", XmlUtil.getAutoId("PlayList", getDoc())
					+ "");
			child.setAttribute("name", name);
			child.setAttribute("musicListPath", musicListPath);
			PlayList playList = new PlayList();
			playList.setId(Integer.parseInt(child.getAttributeValue("id")));
			playList.setName(child.getAttributeValue("name"));
			playList.setMusicListPath(new File(child.getAttributeValue("musicListPath")).getAbsolutePath());
			parent.addContent(child);
			XmlUtil.writeXMLFile(getDoc(), "file/PlayList.xml");
			// 在xml文件中加入相关的元素之后,再在相关的路径创建文件
			FileOutputStream fos = new FileOutputStream(musicListPath);
			String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "\r\n"
					+ "<MusicList xsi:noNamespaceSchemaLocation=\"MusicList.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
					+ "\r\n" + "</MusicList>";
			byte[] b = xmlHeader.getBytes();
			fos.write(b);
			fos.flush();
			fos.close();
			return playList;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deletePlayList(PlayList playList) {
		Element parent = getDoc().getRootElement();
		try {
			XPath xPath = XPath.newInstance("//PlayList[@id="
					+ playList.getId() + "]");
			Element e = (Element) xPath.selectSingleNode(parent);
			parent.removeContent(e);
			// 删除存放在磁盘相关的文件
			boolean b = new File(playList.getMusicListPath()).delete();
		} catch (JDOMException e) {
			e.printStackTrace();
		} finally {
			try {
				XmlUtil.writeXMLFile(getDoc(), "file/PlayList.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void renamePlayList(PlayList playList) {
		try {
			XPath xPath = XPath.newInstance("//PlayList[@id="
					+ playList.getId() + "]");
			Element e = (Element) xPath.selectSingleNode(getDoc()
					.getRootElement());
			e.setAttribute("name", playList.getName());
			e.setAttribute("musicListPath", playList.getMusicListPath());
		} catch (JDOMException e) {
			e.printStackTrace();
		} finally {
			try {
				XmlUtil.writeXMLFile(getDoc(), "file/PlayList.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化列表至少有一个默认列表
	 */
	public void init() {
		addPlayList("默认列表", "file/默认列表.xml");
	}
}
