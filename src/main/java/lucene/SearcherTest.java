/**  
 * Project Name:lucene01  
 * File Name:Searcher.java  
 * Package Name:lucene  
 * Date:2016年12月8日下午12:18:40  
 * Copyright (c) 2016, jingmendh@163.com All Rights Reserved.  
 *  
*/  
  
package lucene;  

import java.io.File;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**  
 * ClassName:Searcher <br/>  
 * Function: 查询. <br/>  
 * Date:     2016年12月8日 下午12:18:40 <br/>  
 * @author   Administrator  
 * @version    
 * @since    JDK 1.6  
 * @see        
 */
public class SearcherTest {
	
	/**
	 * search:(这里用一句话描述这个方法的作用). <br/>   
	 * @author Administrator  
	 * @param indexDir 索引目录
	 * @param q  查询关键字
	 * @throws Exception  
	 * @since JDK 1.6
	 */
	public static void search(String indexDir,String q)throws Exception{
		long start  = System.currentTimeMillis();
		
		Directory dir = FSDirectory.open(new File(indexDir));
		IndexReader ir = DirectoryReader.open(dir); 
		IndexSearcher is = new IndexSearcher(ir);
		//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_46);
		QueryParser parser = new QueryParser(Version.LUCENE_46,"content",analyzer);
		Query query = parser.parse(q);
		TopDocs docs = is.search(query,10);
		long end  = System.currentTimeMillis();
		System.out.println("匹配"+q+"共花费"+(end-start)+"毫秒，查询到"+docs.totalHits+"个记录");

		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","<b/><font/>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		for (ScoreDoc doc:docs.scoreDocs) {
			Document document = is.doc(doc.doc);
		    String desc = document.get("content");
		    String id = document.get("id");
			if(desc != null){
		    	TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(desc));
		    	System.out.println(id);
		    	System.out.println(highlighter.getBestFragment(tokenStream, desc));
		   
			}
		}
	}
	
	public static void main(String[] args) {
		String indexDir ="E:\\lucene\\index";
		String q = "我的";
		try {
			search(indexDir, q);
		} catch (Exception e) {
			e.printStackTrace();  
		}
	}

}
  
