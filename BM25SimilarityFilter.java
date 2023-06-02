package nlp;

import gem.util.database.DBConnection;
import smile.nlp.SimpleCorpus;
import smile.nlp.Text;
import smile.nlp.relevance.Relevance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class BM25SimilarityFilter {
    public static void main(String[] args)
    {
        BM25SimilarityFilter bm25 = new BM25SimilarityFilter();
        bm25.RunMe(bm25);
    }
    private void RunMe(BM25SimilarityFilter bm25)
    {
       // String col_with_text = "doc_text";
       // String table_with_recs = "ytex.document";
         String col_with_text = "doc_text";
         String table_with_recs = "ytex.document";
        String[] matchterms = {"heart","failure","congestive"};
        double min_score = 10.0;

        ArrayList<String> recsAsList  =  bm25.getRecText(table_with_recs,col_with_text);
        SimpleCorpus corpus = new SimpleCorpus();
        for(String line : recsAsList)
        {
            corpus.add(new Text(line));
        }

        Iterator<Relevance> hits = corpus.search(new BM25(), matchterms);
        while (hits.hasNext()) {
            Relevance hit = hits.next();
            if(hit.score>min_score)
            {
                System.out.println("-------------");
                System.out.println(hit.text.body + "\t" + hit.score);
                System.out.println("-------------");
            }
            //   System.out.println(hit.text.body.length()+"\t "+ hit.text.body.substring(0,hit.text.body.length()/2) + "\t" + hit.score);
        }
    }
    private  ArrayList<String>  getRecText(String tableName, String recsCol)
    {
        ArrayList<String> recsAsList = new ArrayList<>();

        try {
            String dclSelect = "Select "+recsCol+" from "+tableName+" where "+recsCol+" is not null";
            DBConnection dbConnection = new DBConnection();
            PreparedStatement pss = dbConnection.getDataBaseConnection().prepareStatement(dclSelect);
            ResultSet resultSet = pss.executeQuery();
                while (resultSet.next()) {
                    recsAsList.add(resultSet.getString(recsCol));
                }
            resultSet.close();
            pss.close();
            dbConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recsAsList;
    }

}
