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
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
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
public class IndexerTest {
	private int[] ids = {1,2,3};
	private String[] citys = {"南京","上海","武汉"};
	private String[] desc = {"南京，简称“宁”，古称金陵、建康，是江苏省会、副省级市、南京都市圈核心城市，是国务院批复确定的中国东部地区重要的中心城市、全国重要的科研教育基地和综合交通枢纽[1]  。南京地处中国东部地区、长江下游、濒江近海。全市下辖11个区，总面积6597平方公里，2015年建成区面积923.8平方公里，常住人口823.6万，城镇人口670.4万人，城镇化率81.4%，是长三角地区及华东地区唯一的特大城市南京是中国四大古都、首批国家历史文化名城[5]  ，是中华文明的重要发祥地[6]  ，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心。南京地区早在100至120万年前就有古人类活动，35至60多万年前已有南京猿人在南京汤山生活。[7]  公元229年，吴大帝孙权在此建都，此后东晋、南朝的刘宋、萧齐、萧梁、陈均相继在此建都，故南京有“六朝古都”之称。继此之后，南京又先后成为杨吴西都、南唐国都、南宋行都、明朝京师、太平天国天京、中华民国首都，故又称“十朝都会","上海是繁华的城市","武汉是有活力的城市"};
	private Directory directory;
	/**
	 * Creates a new instance of Indexer.  
	 * @param indexDir
	 * @throws Exception
	 */
	/*public IndexerTest(String indexDir) throws Exception{
		//指定 索引存放位置
		directory = FSDirectory.open(new File(indexDir));
		//Directory directory = new RAMDirectory();
	}*/
	
	/**
	 * close:(关闭写索引). <br/>   
	 * @author Administrator  
	 * @throws Exception  
	 * @since JDK 1.7
	 */
	/*public void close()throws Exception{
		writer.close();
	}*/
	
	/**
	 * index:(遍历数据,索引指定目录的文件). <br/>   
	 * @author Administrator  
	 * @param datadir
	 * @throws Exception  
	 * @since JDK 1.7
	 */
	public IndexWriter getWriter() throws Exception{
		//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);//创建标准分词器
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);//中文分词
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		IndexWriter writer  = new IndexWriter(directory, config);
		return writer;
		//返回索引的文件数
	}
	
	private void index(String indexDir)throws Exception{
		directory = FSDirectory.open(new File(indexDir));
		IndexWriter writer = getWriter();
		for(int i =0 ; i<ids.length;i++){
			Document document = new Document();
			document.add(new IntField("id",ids[i],Field.Store.YES));
			document.add(new StringField("city",citys[i],Field.Store.YES));
			document.add(new TextField("desc",desc[i],Field.Store.YES));
			writer.addDocument(document);
		}
		writer.close();
	}

	/**
	 * IndexFileNames:(索引指定文件). <br/>   
	 * @author Administrator  
	 * @param f  
	 * @since JDK 1.7
	 */
	
	/*private void IndexFile(File f)throws Exception {
		Document doc = getDocument(f);
		writer.addDocument(doc);
		writer.commit();
	}*/

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
	
	//删除索引
    public void deleteIndex(String indexDir,long blogId)throws Exception{
    	directory = FSDirectory.open(new File(indexDir));
    	IndexWriter writer=getWriter();
		writer.deleteDocuments(new Term("id",String.valueOf(blogId)));
		writer.forceMergeDeletes(); // 强制删除
		writer.commit();
		writer.close();
    }
	
    public static void main(String[] args) {
		String indexDir = "E:\\lucene\\index";
		
		/*String dataDir = "E:\\javaExt\\lucene\\data";
		CopyOfIndexer indexer=null;
		int  numIndexer =0;
		long start  = System.currentTimeMillis();
		try {
			indexer=new CopyOfIndexer(indexDir);
			//numIndexer = indexer.index(dataDir);
		} catch (Exception e) {
			  
			try {
				indexer.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();  
			}
			e.printStackTrace();  
		}
		long end = System.currentTimeMillis();
		System.out.println("共索引了"+numIndexer+"个文件,共花费"+(end-start)+"毫秒");*/
    	try {
			new IndexerTest().deleteIndex(indexDir,43);
		} catch (Exception e) {
			  
			e.printStackTrace();  
			
		};
	}
	
	
}
  
