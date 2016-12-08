/**  
 * Project Name:lucene01  
 * File Name:Indexer.java  
 * Package Name:lucene  
 * Date:2016年12月7日上午12:18:01  
 * Copyright (c) 2016, jingmendh@163.com All Rights Reserved.  
 *  
*/  
  
package lucene;  

import java.io.File;
import java.io.FileReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**  
 * ClassName:Indexer <br/>  
 * Function: 全文检索demo. <br/>  
 * Date:     2016年12月7日 上午12:18:01 <br/>  
 * @author   Administrator  
 * @version    
 * @since    JDK 1.7  
 * @see        
 */
public class Indexer {
	private IndexWriter writer;
	
	/**
	 * Creates a new instance of Indexer.  
	 * @param indexDir
	 * @throws Exception
	 */
	public Indexer(String indexDir) throws Exception{
		//指定 索引存放位置
		Directory directory = FSDirectory.open(new File(indexDir));
		//Directory directory = new RAMDirectory();
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);//创建标准分词器
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		writer = new IndexWriter(directory, config);
	}
	
	/**
	 * close:(关闭写索引). <br/>   
	 * @author Administrator  
	 * @throws Exception  
	 * @since JDK 1.7
	 */
	public void close()throws Exception{
		writer.close();
	}
	
	/**
	 * index:(遍历数据,索引指定目录的文件). <br/>   
	 * @author Administrator  
	 * @param datadir
	 * @throws Exception  
	 * @since JDK 1.7
	 */
	public int index(String datadir) throws Exception{
		
		File[] files =  new File(datadir).listFiles();
		for(File f:files){
			IndexFile(f);
		}
		//返回索引的文件数
		return writer.numDocs();
	}

	/**
	 * IndexFileNames:(索引指定文件). <br/>   
	 * @author Administrator  
	 * @param f  
	 * @since JDK 1.7
	 */
	
	private void IndexFile(File f)throws Exception {
		Document doc = getDocument(f);
		writer.addDocument(doc);
		writer.commit();
	}

	/**
	 * getDocument:(获取文档). <br/>   
	 * @author Administrator  
	 * @param f  
	 * @since JDK 1.7
	 */
	private Document getDocument(File f)throws Exception {
		  Document doc = new Document();
		  doc.add(new TextField("contents", new FileReader(f)));
		  doc.add(new TextField("filename", f.getName(),Field.Store.YES));
		  doc.add(new TextField("fullPath", f.getCanonicalPath(),Field.Store.YES));
		  return doc;	  
	}
	
    public static void main(String[] args) {
		String indexDir = "E:\\javaExt\\lucene\\index";
		String dataDir = "E:\\javaExt\\lucene\\data";
		Indexer indexer=null;
		int  numIndexer =0;
		long start  = System.currentTimeMillis();
		try {
			indexer=new Indexer(indexDir);
			numIndexer = indexer.index(dataDir);
		} catch (Exception e) {
			  
			try {
				indexer.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();  
			}
			e.printStackTrace();  
		}
		long end = System.currentTimeMillis();
		System.out.println("共索引了"+numIndexer+"个文件,共花费"+(end-start)+"毫秒");
	}
	
	
}
  
