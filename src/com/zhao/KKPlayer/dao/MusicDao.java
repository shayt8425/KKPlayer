package com.zhao.KKPlayer.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalDataException;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import com.zhao.KKPlayer.model.Music;
import com.zhao.KKPlayer.util.XmlUtil;

public class MusicDao {

	private Document doc;
	private List<Music> musics = new ArrayList<Music>();
	// 当前音乐列表的路径
	private String musicListPath = "file/默认列表.xml";

	public String getMusicListPath() {
		return musicListPath;
	}

	public Document getDoc() {
		if (doc == null) {
			try {
				doc = XmlUtil.loadXml(musicListPath, MusicDao.class
						.getResource("/xsd/MusicList.xsd"));
			} catch (Exception e) {

			}
		}
		return doc;
	}

	/**
	 * 更新xml的document
	 * 
	 * @param xmlPath
	 *            xml文件路径
	 * @return
	 */
	public void updateDoc(String xmlPath) {
		try {
			musicListPath = xmlPath;
			doc = XmlUtil.loadXml(xmlPath, MusicDao.class
					.getResource("/xsd/MusicList.xsd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Music> getMusics() {
		if (getDoc() != null) {
			musics = new ArrayList<Music>();
			try {
				XPath xPath = XPath.newInstance("//Music");
				List<?> elements = xPath.selectNodes(getDoc().getRootElement());
				for (Iterator<?> i = elements.iterator(); i.hasNext();) {
					Element e = (Element) i.next();
					Music music = new Music();
					music.setId(Integer.parseInt(e.getAttributeValue("id")));
					music.setName(e.getAttributeValue("name"));
					music.setPath(e.getAttributeValue("path"));
					music.setTotalTime(Long.parseLong(e
							.getAttributeValue("totalTime")));
					music.setArtist(e.getAttributeValue("artist"));
					music.setTitle(e.getAttributeValue("title"));
					music.setAlbum(e.getAttributeValue("album"));
					music.setGenre(e.getAttributeValue("genre"));
					music.setTrackNumber(e.getAttributeValue("trackNumber"));
					music.setYear(e.getAttributeValue("year"));
					music.setComment(e.getAttributeValue("comment"));
					musics.add(music);
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			}
		}
		return musics;
	}

	public void addMusics(Music music) {
		Element parent = doc.getRootElement();
		Element child = new Element("Music");
		try {
			int id = XmlUtil.getAutoId("Music", getDoc());
			music.setId(id);
			child.setAttribute("id", id+"");
			child.setAttribute("name", music.getName());
			child.setAttribute("path", music.getPath());
			child.setAttribute("totalTime", music.getTotalTime() + "");
			try {
				child.setAttribute("artist", music.getArtist());
			} catch (IllegalDataException e) {
				child.setAttribute("title", "");
			}
			try {
				child.setAttribute("title", music.getTitle());
			} catch (IllegalDataException e) {
				child.setAttribute("title", "");
			}
			try {
				child.setAttribute("album", music.getAlbum());
			} catch (IllegalDataException e) {
				child.setAttribute("album", "");
			}
			try {
				child.setAttribute("genre", music.getGenre());
			} catch (IllegalDataException e) {
				child.setAttribute("genre", "");
			}
			try {
				child.setAttribute("trackNumber", music.getTrackNumber());
			} catch (IllegalDataException e) {
				child.setAttribute("trackNumber", "");
			}
			try {
				child.setAttribute("year", music.getYear());
			} catch (IllegalDataException e) {
				child.setAttribute("year", "");
			}
			try {
				child.setAttribute("comment", music.getComment());
			} catch (IllegalDataException e) {
				child.setAttribute("comment", "");
			}
			parent.addContent(child);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	public void addMusics(Music music, int index) {
		Element parent = doc.getRootElement();
		Element child = new Element("Music");
		try {
			child.setAttribute("id", XmlUtil.getAutoId("Music", getDoc()) + "");
			child.setAttribute("name", music.getName());
			child.setAttribute("path", music.getPath());
			child.setAttribute("totalTime", music.getTotalTime() + "");
			child.setAttribute("artist", music.getArtist());
			child.setAttribute("title", music.getTitle());
			child.setAttribute("album", music.getAlbum());
			child.setAttribute("genre", music.getGenre());
			child.setAttribute("trackNumber", music.getTrackNumber());
			child.setAttribute("year", music.getYear());
			child.setAttribute("comment", music.getComment());
			if (index == 1) {
				parent.addContent(0, child);
			} else {
				parent.addContent(index, child);
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	public void deleteMusic(Music music) {
		Element parent = doc.getRootElement();
		try {
			XPath xPath = XPath.newInstance("//Music[@id=" + music.getId()
					+ "]");
			Element child = (Element) xPath.selectSingleNode(parent);
			parent.removeContent(child);
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新列表中的音乐信息
	 * 
	 * @param music
	 */
	public void updateMusicInformation(Music music) {
		Element parent = doc.getRootElement();
		try {
			XPath xPath = XPath.newInstance("//Music[@id=" + music.getId()
					+ "]");
			Element child = (Element) xPath.selectSingleNode(parent);
			child.setAttribute("name", music.getName());
			child.setAttribute("path", music.getPath());
			child.setAttribute("totalTime", music.getTotalTime() + "");
			child.setAttribute("artist", music.getArtist());
			child.setAttribute("title", music.getTitle());
			child.setAttribute("album", music.getAlbum());
			child.setAttribute("genre", music.getGenre());
			child.setAttribute("trackNumber", music.getTrackNumber());
			child.setAttribute("year", music.getYear());
			child.setAttribute("comment", music.getComment());
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}

	public void setMusicLyricPath(int id, String lyricPath) {
		try {
			XPath xPath = XPath.newInstance("//Music[@id=" + id + "]");
			Element child = (Element) xPath.selectSingleNode(doc
					.getRootElement());
			child.setAttribute("lyricPath", lyricPath);
			XmlUtil.writeXMLFile(doc, getMusicListPath());
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getMusicLyricPath(int id) {
		try {
			XPath xPath = XPath.newInstance("//Music[@id=" + id + "]");
			Element child = (Element) xPath.selectSingleNode(doc
					.getRootElement());
			if (child.getAttribute("lyricPath") != null
					&& !child.getAttributeValue("lyricPath").equals("")) {
				return child.getAttributeValue("lyricPath");
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteMusicLyricPath(int id) {
		try {
			XPath xPath = XPath.newInstance("//Music[@id=" + id + "]");
			Element e = (Element) xPath.selectSingleNode(doc.getRootElement());
			e.removeAttribute("lyricPath");
			XmlUtil.writeXMLFile(doc, getMusicListPath());
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
