package nlp;

import gem.data.SearchResult;
import gem.util.database.DBConnection;
import smile.nlp.SimpleCorpus;
import smile.nlp.Text;
import smile.nlp.relevance.Relevance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BM25Runner {

    public static void main(String[] args)
    {
        BM25Runner bm25 = new BM25Runner();
        bm25.RunMe();
    }

    private void RunMe()
    {
        SimpleCorpus corpus = new SimpleCorpus();
        try {
            String dclSelect = "select rec_text from recs_ontologies";
            DBConnection dbConnection = new DBConnection();
            PreparedStatement pss = dbConnection.getDataBaseConnection().prepareStatement(dclSelect);
            ResultSet rs = pss.executeQuery();
            ArrayList<String> lines = new ArrayList<>();
            while (rs.next()) {
                String cond = rs.getString("rec_text");
               lines.add(cond);
            }
            for(String line : lines)
            {
                corpus.add(new Text(line));
            }
            String[] terms = {"systolic","arterial","pressure"};
            Iterator<Relevance> hits = corpus.search(new BM25(), terms);
            int n = 0;
            while (hits.hasNext()) {
                n++;
                Relevance hit = hits.next();
                System.out.println(hit.text.body + "\t" + hit.score);
             //   System.out.println(hit.text.body.length()+"\t "+ hit.text.body.substring(0,hit.text.body.length()/2) + "\t" + hit.score);
            }
            pss.close();
            rs.close();
            dbConnection.close();
        } catch (Exception e) {

        }

//
//        System.out.println("rank");
//        int freq = 3;
//        int docSize = 100;
//        int avgDocSize = 150;
//        int N = 10000000;
//        int n = 1000;
//        BM25 instance = new BM25(2.0, 0.75, 0.0);
//        double expResult = 18.419681;
//        double result = instance.score(freq, docSize, avgDocSize, N, n);


    }

}
